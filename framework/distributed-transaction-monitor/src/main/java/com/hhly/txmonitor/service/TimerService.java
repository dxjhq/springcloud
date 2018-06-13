package com.hhly.txmonitor.service;

import com.google.gson.Gson;
import com.hhly.cache.service.RedisService;
import com.hhly.tx.componet.PublishConfig;
import com.hhly.tx.constant.TxConstant;
import com.hhly.tx.pojo.TxMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author wangxianchen
 * @create 2017-12-01
 * @desc
 */
@Service
public class TimerService implements InitializingBean{

    private Logger logger = LoggerFactory.getLogger(TimerService.class);

    private final long defaultDelay = 60000L; //默认延迟时间1分钟

    private final long defaultPeriod = 180000L; //默认间隔执行时间3分钟

    @Autowired
    private RedisService redisService;

    @Autowired
    private Gson gson;

    private Timer timer;

    private long delay = defaultDelay;

    @Value("${txMonitorTimer.period}")
    private long period = 0;

    private boolean notRun = true;

    public void start(){
        if(timer == null || notRun){ //或者是不为运行状态
            timer = new Timer("TxMonitorTimer");
            if(delay < 0){
                delay = defaultDelay;
                logger.info("delay less than defaultDelay,will be use defaultDelay");
            }
            if(period < defaultDelay){
                period = defaultPeriod;
                logger.info("period less than defaultPeriod,will be use defaultPeriod");
            }
            timer.schedule(createNewTask(),delay,period);
            notRun = false;
            logger.info("TxMonitorTimer is running...");
        }
    }

    public void stop(){
        if(timer != null && !notRun){ //不为空 或者是在运行状态
            notRun = true;
            timer.cancel();
            timer.purge();
            logger.info("TxMonitorTimer is stopped!");
        }
    }

    public TimerTask createNewTask(){
        return new TimerTask() {
            @Override
            public void run() {
                logger.info("TxMonitorTimer task is doing. period time={}s ..........................",period);
                String keys = "*:"+ TxConstant.TX_MSG_MAP_PREFIX+"*";
                Set<String> set = redisService.getRedisTemplate().keys(keys);
                if(set != null && set.size() > 0){
                    for(String key : set){
                        Map<Object,Object> map = redisService.getRedisTemplate().opsForHash().entries(key);
                        if(map != null && map.size() > 0){
                            for(Map.Entry<Object, Object> entry : map.entrySet()){
                                TxMessage msg = (TxMessage)entry.getValue();
                                redisService.getRedisTemplate().convertAndSend(
                                        msg.getServiceName() + PublishConfig._TOPIC,
                                        gson.toJson(msg));
                                logger.info("TxMonitorTimer publish msg to service {}.  id={}, callerMsgId={}"
                                        ,msg.getServiceName(),msg.getId(),msg.getCallerMsgId());
                            }
                        }
                    }

                }
            }
        };
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public boolean isNotRun() {
        return notRun;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }
}
