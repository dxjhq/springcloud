package com.hhly.tx.componet;


import com.hhly.api.dto.ResultObject;
import com.hhly.api.enums.ErrorCodeEnum;
import com.hhly.tx.annotation.TxRollbackHandle;
import com.hhly.tx.constant.TxConstant;
import com.hhly.tx.enums.TxEnum;
import com.hhly.tx.pojo.TxMessage;
import com.google.gson.Gson;
import com.hhly.cache.service.RedisService;
import com.hhly.tx.service.AbstractRollbackService;
import com.hhly.utils.ValueUtil;
import com.hhly.utils.date.DateFormatType;
import com.hhly.utils.date.DateUtil;
import com.hhly.utils.json.JsonResult;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;


/**
 * @author wangxianchen
 * @create 2017-11-13
 * @desc AOP配置
 */

@Aspect
@Component
@Order(1)
public class TxAspect {
    private Logger logger = LoggerFactory.getLogger(TxAspect.class);

    private ThreadLocal<String> atomicCallerMsgId = new ThreadLocal<String>();

    private ThreadLocal<Boolean> atomicIsContinue = new ThreadLocal<Boolean>();

    private static ThreadLocal<String> atomicLocalMsgId = new ThreadLocal<>();

    private static ThreadLocal<String> atomicRollbackId = new ThreadLocal<>();

    @Autowired
    private RedisService redisService;

    @Autowired
    private Gson gson;

    @Value("${spring.application.name}")
    private String appServiceName;

    @Pointcut("@annotation(com.hhly.tx.annotation.TxRollbackHandle)")
    private void controllerAspect() {
    }

    //请求method前打印内容
    @Before(value = "controllerAspect()")
    public void methodBefore(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Class returnType = methodSignature.getReturnType();
        if(!returnType.equals(ResultObject.class) && !returnType.equals(JsonResult.class)){
            logger.error("分布式事务不支持的返回类型,事务未开启！returnType={}",returnType.getTypeName());
            atomicIsContinue.set(false);
            throw new IllegalArgumentException("分布式事务不支持的返回类型,returnType:"+returnType.getTypeName());
        }
        Method method = methodSignature.getMethod();
        TxRollbackHandle  txRollbackHandle =  method.getAnnotation(TxRollbackHandle.class);
        if(txRollbackHandle.bean().equals(AbstractRollbackService.class)){
            logger.error("没有设置回滚服务名");
            atomicIsContinue.set(false);
            throw new NullPointerException("没有设置回滚服务名");
        }
        atomicIsContinue.set(true);
        String msgUuid = ValueUtil.uuid();
        atomicRollbackId.set(msgUuid);
        atomicLocalMsgId.set(appServiceName+":"+ TxConstant.TX_MSG_MAP_PREFIX + msgUuid);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String callerMsgId = request.getHeader(TxConstant.TX_MSG_ID);
        TxMessage txMessage = new TxMessage();
        txMessage.setCreateTime(DateUtil.formatDateToString(new Date(), DateFormatType.YYYY_MM_DD_HH_MM_SS.getValue()));
        txMessage.setState(TxEnum.WAIT_FOR_PERFORMED.code());
        txMessage.setServiceName(appServiceName);
        txMessage.setRollbackId(msgUuid);
        txMessage.setRollbackBeanType(txRollbackHandle.bean().getTypeName());
        txMessage.setRollbackMethod(StringUtils.isBlank(txRollbackHandle.method()) ? method.getName() : txRollbackHandle.method());
        //被调方
        if(callerMsgId != null){
            atomicCallerMsgId.set(callerMsgId); //调用方ID
            txMessage.setId(atomicLocalMsgId.get());
            txMessage.setCallerMsgId(callerMsgId);
            redisService.hmSet(callerMsgId,atomicLocalMsgId.get(),txMessage);

        //请求发起方
        }else{
            txMessage.setId(atomicLocalMsgId.get());
            txMessage.setCallerMsgId(atomicLocalMsgId.get());
            redisService.hmSet(atomicLocalMsgId.get(),atomicLocalMsgId.get(),txMessage);
            redisService.resetExpire(atomicLocalMsgId.get(),TxConstant.EXPIRE_TIME);
        }
    }

    //在方法执行完结后打印返回内容
    @AfterReturning(returning = "obj", pointcut = "controllerAspect()")
    public void methodAfterReturing(Object obj) {
        if(!atomicIsContinue.get()){
            return;
        }
        String callerMsgId = atomicCallerMsgId.get();
        //被调方
        if(callerMsgId != null){
            Object msg = redisService.hmGet(callerMsgId,atomicLocalMsgId.get());
            if(msg == null){
                logger.error("未找到发起方消息. callerMsgId={}",callerMsgId);
                return;
            }
            TxMessage txMessage = (TxMessage)msg;
            if(obj != null){
                int errCode = 0; //默认是失败的
                if(obj instanceof ResultObject){
                    errCode = ((ResultObject) obj).getCode();
                }else if(obj instanceof JsonResult){
                    errCode = ((JsonResult) obj).getCode();
                }
                if(errCode != ErrorCodeEnum.SUCCESS.code()) { //不为成功
                    //设置抛出的异常消息
                    txMessage.setErrCode(String.valueOf(errCode));
                    //设置失败状态
                    txMessage.setState(TxEnum.FAIL.code());
                }else{//成功
                    txMessage.setState(TxEnum.SUCCESS.code());
                }
            }else{
                //返回空默认为 成功
               // logger.error("返回结果为空,或不符合规范");
                txMessage.setState(TxEnum.SUCCESS.code());
            }
            txMessage.setUpdateTime(DateUtil.formatDateToString(new Date(), DateFormatType.YYYY_MM_DD_HH_MM_SS.getValue()));
            redisService.hmSet(callerMsgId,atomicLocalMsgId.get(),txMessage);

        //请求发起方,查看所有消息状态,确定最终终状态
        }else{
            Object msg = redisService.hmGet(atomicLocalMsgId.get(),atomicLocalMsgId.get());
            if(msg == null){
                logger.error("未找到发起方消息. callerMsgId={}",callerMsgId);
                return;
            }

            TxMessage txMessage = (TxMessage)msg;

            Map<Object,Object> entries = redisService.getRedisTemplate().opsForHash().entries(atomicLocalMsgId.get());
            //发起方结果失败的情况,
            int errCode = 0; //默认是失败的
            if(obj instanceof ResultObject){
                errCode = ((ResultObject) obj).getCode();
            }else if(obj instanceof JsonResult){
                errCode = ((JsonResult) obj).getCode();
            }
            if(errCode != ErrorCodeEnum.SUCCESS.code()){ //不为成功
                //设置失败状态
                txMessage.setState(TxEnum.FAIL.code());
                txMessage.setUpdateTime(DateUtil.formatDateToString(new Date(), DateFormatType.YYYY_MM_DD_HH_MM_SS.getValue()));
                //设置抛出的异常消息
                txMessage.setErrCode(String.valueOf(errCode));
                redisService.hmSet(atomicLocalMsgId.get(),atomicLocalMsgId.get(),txMessage);
                entries.put(atomicLocalMsgId.get(),txMessage);
                //不为成功,发送通知 异步执行回滚操作
                sendMsg(txMessage,entries);
                return;
            }

            //发起方结果成功的情况,需要查看所有被调方的结果,再作最终的判断
            boolean isFail = false;
            for(Object key : entries.keySet()){
                if(key != atomicLocalMsgId.get()){
                    if(((TxMessage)entries.get(key)).getState() == TxEnum.FAIL.code()){
                        isFail = true;
                        break;
                    }
                }
            }
            //调用的其它服务有失败的情况
            if(isFail){
                txMessage.setState(TxEnum.FAIL.code());
                txMessage.setUpdateTime(DateUtil.formatDateToString(new Date(), DateFormatType.YYYY_MM_DD_HH_MM_SS.getValue()));
                redisService.hmSet(atomicLocalMsgId.get(),atomicLocalMsgId.get(),txMessage);
                entries.put(atomicLocalMsgId.get(),txMessage);
                //不为成功,发送通知 异步执行回滚操作
                sendMsg(txMessage,entries);
            }else{
                txMessage.setState(TxEnum.SUCCESS.code());
                txMessage.setUpdateTime(DateUtil.formatDateToString(new Date(), DateFormatType.YYYY_MM_DD_HH_MM_SS.getValue()));
                redisService.hmSet(atomicLocalMsgId.get(),atomicLocalMsgId.get(),txMessage);
                //都成功了, 删除此次事务的所有消息
                redisService.remove(atomicLocalMsgId.get());
            }

        }

    }
    @AfterThrowing(throwing="ex",value = "controllerAspect()")
    public void afterThrowing(Throwable ex) {
        if(!atomicIsContinue.get()){
            return;
        }
        String callerMsgId = atomicCallerMsgId.get();
        //被调方
        if(callerMsgId != null){
            Object msg = redisService.hmGet(callerMsgId,atomicLocalMsgId.get());
            if(msg == null){
                logger.error("未找到发起方消息. callerMsgId={}",callerMsgId);
                return;
            }
            //更新结果状态
            TxMessage txMessage = (TxMessage)msg;
            txMessage.setState(TxEnum.FAIL.code());
            txMessage.setUpdateTime(DateUtil.formatDateToString(new Date(), DateFormatType.YYYY_MM_DD_HH_MM_SS.getValue()));
            //设置抛出的异常消息
            txMessage.setErrType(ex.getClass().getTypeName());
            redisService.hmSet(callerMsgId,atomicLocalMsgId.get(),txMessage);

        //请求发起方
        }else{
            Map<Object,Object> entries = redisService.getRedisTemplate().opsForHash().entries(atomicLocalMsgId.get());
            if(entries == null){
                logger.error("未找到消息列表. callerMsgId={}",callerMsgId);
                return;
            }
            Object msg = entries.get(atomicLocalMsgId.get());
            if(msg == null){
                logger.error("未找到发起方消息. callerMsgId={}",callerMsgId);
                return;
            }
            //更新结果状态
            TxMessage txMessage = (TxMessage)msg;
            txMessage.setState(TxEnum.FAIL.code());
            txMessage.setUpdateTime(DateUtil.formatDateToString(new Date(), DateFormatType.YYYY_MM_DD_HH_MM_SS.getValue()));
            //设置抛出的异常消息
            txMessage.setErrType(ex.getClass().getTypeName());
            redisService.hmSet(atomicLocalMsgId.get(),atomicLocalMsgId.get(),txMessage);
            entries.put(atomicLocalMsgId.get(),txMessage);
            //不为成功,发送通知 异步执行回滚操作
            sendMsg(txMessage,entries);

        }
    }

    /**
     * @desc 发布消息，通知回滚
     * @author wangxianchen
     * @create 2017-12-07
     * @param txMessage
     * @param entries
     */
    private void sendMsg(TxMessage txMessage,Map<Object,Object> entries){
        if(entries != null && entries.size() != 0){
            TxMessage msg = null;
            for(Map.Entry<Object, Object> entry : entries.entrySet()) {
                msg = (TxMessage) entry.getValue();
                //对已成功的消息发出回滚指令
                if(msg.getState() == TxEnum.SUCCESS.code() ||
                        (msg.getId().equals(msg.getCallerMsgId()) && msg.getState() == TxEnum.FAIL.code())){
                    redisService.getRedisTemplate().convertAndSend(
                            msg.getServiceName() + PublishConfig._TOPIC,
                            gson.toJson(msg));
                }
            }
        }
    }

    /**
     * @desc 获取消息ID
     * @author wangxianchen
     * @create 2017-12-07
     * @return
     */
    static String getLocalMsgId(){
        return atomicLocalMsgId.get();
    }

    /**
     * @desc 获取回滚惟一标志ID
     * @author wangxianchen
     * @create 2017-12-07
     * @return
     */
    public static String getRollbackId(){
        return atomicRollbackId.get();
    }
}
