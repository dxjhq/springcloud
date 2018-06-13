package com.hhly.tx.service;

import com.google.gson.Gson;
import com.hhly.cache.exception.ExclusiveLockException;
import com.hhly.cache.service.RedisService;
import com.hhly.tx.constant.TxConstant;
import com.hhly.tx.pojo.TxMessage;
import com.hhly.utils.spring.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author wangxianchen
 * @create 2017-11-20
 * @desc 回滚服务抽象处理
 */
public class MessageReceiverService{

    private Logger logger = LoggerFactory.getLogger(MessageReceiverService.class);

    private TxMessage txMessage;

    @Autowired
    private Gson gson;

    @Resource(name = "txThreadPoolTaskExecutor")
    protected ThreadPoolTaskExecutor taskExecutor;

    @Value("${spring.application.name}")
    private String appServiceName;

    @Autowired
    private RedisService redisService;

    /**
     * @desc 接收订阅redis的消息
     * @author wangxianchen
     * @create 2017-11-28
     * @param msg
     */
    public void handleMessage(String msg) {
        try {
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    //logger.info(appServiceName +"收到消息:"+msg);
                    String message = msg.replace("\"{","{").replace("}\"","}").replace("\\\"","\"");
                    txMessage = gson.fromJson(message,TxMessage.class);
                    String lockName = appServiceName+":"+ TxConstant.TX_MSG_LOCK + txMessage.getId().substring(txMessage.getId().lastIndexOf(":")+1);
                    try {
                        //redis锁,防止竞争处理
                        redisService.exclusiveLock(lockName,10,"处理回滚消息");
                        if(isExistMsg()){
                            doRollback();
                            doAfter();
                        }
                    } catch(ExclusiveLockException e){
                        logger.error(e.getMessage());
                    } catch (ClassNotFoundException e) {
                        logger.error("没有找到类",e);
                    } catch (NoSuchMethodException e) {
                        logger.error("没有该方法",e);
                    } catch(InvocationTargetException e){
                        logger.error("调用方法异常",e);
                    } catch(IllegalAccessException e){
                        logger.error("调用方法异常",e);
                    } catch(InstantiationException e){
                        logger.error("调用方法异常",e);
                    } catch(Exception e){
                        logger.error("执行回滚事务异常",e);
                    }

                }
            });
        } catch (Exception e) {
            logger.error("订阅redis消息异常",e);
        }


    }

    /**
     * @desc 执行回滚操作.根据类型来获取bean失败,只能通过beanName获取
     * @author wangxianchen
     * @create 2017-11-28
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void doRollback() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Class clzz = Class.forName(this.txMessage.getRollbackBeanType());
        Service annotaion = (Service) clzz.getAnnotation(Service.class);
        String beanName = annotaion.value();
        if(StringUtils.isEmpty(beanName)){
            beanName = clzz.getSimpleName();
            beanName = (new StringBuilder()).append(Character.toLowerCase(beanName.charAt(0))).append(beanName.substring(1)).toString();
        }
        Method setTxMessage = clzz.getSuperclass().getDeclaredMethod("setTxResErroInf",TxMessage.class,String.class);
        Object object = ApplicationContextUtil.getBean(beanName);
        ReflectionUtils.invokeMethod(setTxMessage,object,this.txMessage,this.txMessage.getRollbackMethod());

    }

    /**
     * @desc 是否存在该消息
     * @author wangxianchen
     * @create 2017-11-28
     * @return
     */
    public boolean isExistMsg(){
        return redisService.getRedisTemplate().opsForHash().hasKey(this.txMessage.getCallerMsgId(),this.txMessage.getId());
    }

    /**
     * @desc 处理最终的结果,回滚成功后删除该消息
     * @author wangxianchen
     * @create 2017-11-28
     */
    public void doAfter(){
        redisService.getRedisTemplate().opsForHash().delete(this.txMessage.getCallerMsgId(),this.txMessage.getId());
        logger.info("删除消息 callerMsgId={}, id={}",this.txMessage.getCallerMsgId(),this.txMessage.getId());
    }
}
