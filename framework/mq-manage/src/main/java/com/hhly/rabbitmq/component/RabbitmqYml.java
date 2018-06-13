package com.hhly.rabbitmq.component;

import com.hhly.rabbitmq.pojo.RabbitmqQueue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangxianchen
 * @create 2017-08-17
 * @desc 获取rabbitmq配置信息
 */
@Component
@ConfigurationProperties(prefix = "rabbitmqConfig")
public class RabbitmqYml implements InitializingBean {

    @Value("${spring.application.name}")
    private String serviceName;

    private List<RabbitmqQueue> queues = new ArrayList<RabbitmqQueue>();

    public List<RabbitmqQueue> getQueues() {
        return queues;
    }
    public void setQueues(List<RabbitmqQueue> queues) {
        this.queues = queues;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
/*        int i = 0;
        for(RabbitmqQueue queue : queues){
            if(queue.getName().equalsIgnoreCase(serviceName)){
                queue.setName(serviceName);
                queue.setRoutingKey(serviceName);
                queue.setExchangeName(RabbitmqConstants.DIRECT_EXCHANGE);
                break;
            }
            i++;
        }
        if(i == queues.size()){
            RabbitmqQueue queue = new RabbitmqQueue();
            queue.setName(serviceName);
            queue.setRoutingKey(serviceName);
            queue.setExchangeName(RabbitmqConstants.DIRECT_EXCHANGE);
            queues.add(queue);
        }*/
    }
}
