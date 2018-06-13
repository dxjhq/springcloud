package com.hhly.txmonitor.service;

import com.google.gson.Gson;
import com.hhly.cache.service.RedisService;
import com.hhly.tx.componet.PublishConfig;
import com.hhly.tx.constant.TxConstant;
import com.hhly.tx.pojo.TxMessage;
import com.hhly.txmonitor.dto.QueryReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author wangxianchen
 * @create 2017-11-30
 * @desc
 */
@Service
public class MonitorService {

/*    private Logger logger = LoggerFactory.getLogger(MonitorService.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private TimerService timerService;

    @Autowired
    private Gson gson;

    *//**
     * @desc 查询
     * @author wangxianchen
     * @create 2017-12-01
     * @param req
     * @return
     *//*
    public ResultObject query(QueryReq req){
        ResultObject ro = new ResultObject();
        List<Map<Object,Object>> list = new ArrayList<>();
        String keys = null;
        if(StringUtils.isEmpty(req.getServiceName())){
            keys = "*:"+ TxConstant.TX_MSG_MAP_PREFIX+"*";
        }else{
            keys = req.getServiceName()+":"+TxConstant.TX_MSG_MAP_PREFIX+"*";
        }
        Set<String> set = redisService.getRedisTemplate().keys(keys);
        Map<Object,Object> retMap;
        if(A.isNotEmpty(set)){
            for(String key : set){
                Map<Object,Object> map = redisService.getRedisTemplate().opsForHash().entries(key);
                if(A.isNotEmpty(map)){
                    retMap = new HashMap<>();
                    TxMessage[] arr = new TxMessage[map.size()];
                    int index = 0;
                    String callerService = null;
                    for(Map.Entry<Object, Object> entry : map.entrySet()){
                        arr[index] = (TxMessage)entry.getValue();
                        if(arr[index].getId().equals(key)){
                            callerService = arr[index].getServiceName();
                        }
                        index++;
                    }

                    retMap.put("callerService",callerService);
                    retMap.put("key",key);
                    retMap.put("arr",arr);
                    retMap.put("size",map.size());
                    list.add(retMap);
                }
            }

        }
        ro.setData(list);
        return ro;
    }

    *//**
     * @desc 删除
     * @author wangxianchen
     * @create 2017-12-01
     * @param req
     * @return
     *//*
    public ResultObject giveup(QueryReq req){
        ResultObject ro = new ResultObject();
        if(StringUtils.isEmpty(req.getMsgKey())){
            ro.doFail();
            ro.setMsg("MSG_ID不能为空");
        }
        redisService.remove(req.getMsgKey());
        return ro;
    }

    *//**
     * @desc 执行回滚
     * @author wangxianchen
     * @create 2017-12-01
     * @param req
     * @return
     *//*
    public ResultObject execut(QueryReq req){
        ResultObject ro = new ResultObject();
        if(StringUtils.isEmpty(req.getMsgKey())){
            ro.doFail();
            ro.setMsg("MSG_ID不能为空");
        }
        Map<Object,Object> map = redisService.getRedisTemplate().opsForHash().entries(req.getMsgKey());
        if(A.isNotEmpty(map)){
            for(Map.Entry<Object, Object> entry : map.entrySet()){
                TxMessage msg = (TxMessage)entry.getValue();
                redisService.getRedisTemplate().convertAndSend(
                        msg.getServiceName() + PublishConfig._TOPIC,
                        gson.toJson(msg));
            }
        }
        return ro;
    }

    public ResultObject timerControl(String op){
        ResultObject ro = new ResultObject();
        if(StringUtils.isEmpty(op)){
            ro.doFail();
            ro.setMsg("参数不能为空");
            logger.error("参数不能为空");
            return ro;
        }
        boolean notRun = timerService.isNotRun(); //true:不在运行  false:在运行
        if("start".equals(op)){
            if(notRun){
                timerService.start();
            }
        }else if("stop".equals(op)){
            if(!notRun){
                timerService.stop();
            }
        }else{
            ro.doFail();
            ro.setMsg("参数不正确");
            logger.error("参数不正确:{}",op);
        }
        return ro;
    }

    public ResultObject timerRestart(QueryReq req){
        ResultObject ro = new ResultObject();
        try {
            boolean notRun = timerService.isNotRun(); //true:不在运行  false:在运行
            if(!notRun){
                timerService.stop();
            }
            timerService.setDelay(req.getDelay());
            timerService.setPeriod(req.getPeriod());
            timerService.start();
        } catch (Exception e) {
            ro.doFail();
            ro.setMsg("重新运行timer失败,请确认参数是否填写正确");
            logger.error("重新运行timer失败,请确认参数是否填写正确",e);
        }

        return ro;
    }*/
}
