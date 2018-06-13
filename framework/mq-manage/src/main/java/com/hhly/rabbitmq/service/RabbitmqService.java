package com.hhly.rabbitmq.service;

import com.hhly.rabbitmq.component.RabbitmqYml;
import com.hhly.rabbitmq.constant.RabbitmqConstants;
import com.hhly.rabbitmq.pojo.RabbitmqQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangxianchen
 * @create 2017-08-17
 * @desc rabbitmq组件初始化.创建queue 并绑定exchange和routingKey.
 */
@Service
public class RabbitmqService implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(RabbitmqService.class);

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private RabbitmqYml rabbitmqYml;

    private RabbitTemplate rabbitTemplate;

    /**
     * @desc 在应用启动时调用
     * @author wangxianchen
     * @create 2017-08-18
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //exchange默认持久化,交换机不自动删除
        DirectExchange directExchange = new DirectExchange(RabbitmqConstants.DIRECT_EXCHANGE);
        TopicExchange topicExchange = new TopicExchange(RabbitmqConstants.TOPIC_EXCHANGE);
        FanoutExchange fanoutExchange = new FanoutExchange(RabbitmqConstants.FANOUT_EXCHANGE);

        logger.info("rabbitmq initial configuration start");
        List<RabbitmqQueue> queues = this.rabbitmqYml.getQueues();
        if(queues != null && queues.size() != 0){
            RabbitAdmin admin = new RabbitAdmin(this.connectionFactory);
            admin.declareExchange(directExchange);
            admin.declareExchange(topicExchange);
            admin.declareExchange(fanoutExchange);
            this.rabbitTemplate = admin.getRabbitTemplate();
            for(RabbitmqQueue rq : queues){
                Queue queue = new Queue(rq.getName(),rq.getDurable(),rq.getExclusive(),rq.getAutoDelete());
                admin.declareQueue(queue);
                if(RabbitmqConstants.DIRECT_EXCHANGE.equals(rq.getExchangeName())){
                    admin.declareBinding(BindingBuilder.bind(queue).to(directExchange).with(rq.getRoutingKey()));

                }else if(RabbitmqConstants.TOPIC_EXCHANGE.equals(rq.getExchangeName())){
                    admin.declareBinding(BindingBuilder.bind(queue).to(topicExchange).with(rq.getRoutingKey()));

                }else if(RabbitmqConstants.FANOUT_EXCHANGE.equals(rq.getExchangeName())){
                    admin.declareBinding(BindingBuilder.bind(queue).to(fanoutExchange));
                }
            }
            //admin.setAutoStartup(false); default is true
        }
        logger.info("Rabbitmq initial configuration completed");
    }


    /**
     * @desc 以directExchange模式发送
     * @author wangxianchen
     * @create 2017-08-18
     * @param routingKey
     * @param msg
     */
    public void sendByDirectExchange(String routingKey,Object msg){
        this.rabbitTemplate.convertAndSend(RabbitmqConstants.DIRECT_EXCHANGE,routingKey,msg);
    }

    /**
     * @desc 以topicExchange模式发送
     * @author wangxianchen
     * @create 2017-08-18
     * @param routingKey
     * @param msg
     */
    public void sendByTopicExchange(String routingKey,Object msg){
        this.rabbitTemplate.convertAndSend(RabbitmqConstants.TOPIC_EXCHANGE,routingKey,msg);
    }

    /**
     * @desc 以fanoutExchange模式发送
     * @author wangxianchen
     * @create 2017-08-18
     * @param msg
     */
    public void sendByFanoutExchange(Object msg){
        this.rabbitTemplate.convertAndSend(RabbitmqConstants.FANOUT_EXCHANGE,null,msg);
    }

    /**
     * @desc 以指定的exchange,routingKey进行发送
     * @author wangxianchen
     * @create 2017-08-18
     * @param exchange
     * @param routingKey
     * @param msg
     */
    public void send(String exchange,String routingKey,Object msg){
        this.rabbitTemplate.convertAndSend(exchange,routingKey,msg);
    }



    /**
     * @desc 获取发送模板,可自由配置发送
     * @author wangxianchen
     * @create 2017-08-18
     * @return
     */
    public RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }
}
