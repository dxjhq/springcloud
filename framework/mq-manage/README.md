rabbitmq整合
---

 **version** 1.0
 
**1.实现思路**

    在.yml文件里配置rabbitmq queue,应用启动时根据配置文件,创建队列并绑定exchange和routingKey

**2.使用方法**

 -    
    POM引入
    
       		<dependency>
       			    <groupId>com.hhly</groupId>
       			    <artifactId>rabbitmq</artifactId>
       			    <version>3.0.0.1</version>
       		</dependency> 
       		
 -    
     yml文件配置
    
        #rabbitmq初始化配置
        rabbitmqConfig:
          #队列
          queues:
            #名称
            - name: A
            #是否持久化,不配置时默认true
              durable: true
            #是否为排他队列,不配置时默认false
              exclusive: false
            #是否自动删除,不配置时默认false
              autoDelete: false
            #交换机名称,不配置时默为topicExchange,共三种模式(directExchange,topicExchange,fanoutExchange)
              exchangeName: directExchange
            #路由关键字,不配置时默为队列名称
              routingKey: A
        
            - name: B
        
            - name: C
              exchangeName: fanoutExchange
        
            - name: D
              exchangeName: fanoutExchange
        
            - name: E
              exchangeName: fanoutExchange
   

 -    
    消息发送
        
        在Controller 或 Service里引入
        ...
        @Autowired
        private RabbitmqService rabbitmqService;
        
        调用 rabbitmqService.send(...)
        或  rabbitmqService.getRabbitTemplate.convertAndSend(...)
        就行了
        ...
        
 -    
    消息接收
        
        在普通类里面标注
        @Component 如果是在service里可以不用此声明
        public class ReceiverService{
            @RabbitListener(queues = "A")
            @RabbitHandler
            public void A(String msg) {
                ...
            }
            or
            @RabbitListener(queues = "B")
            @RabbitHandler
            public void B(Object msg) {  #当发送的是对象时,该对象必需实现序列化
                ...
            }
            or
            @RabbitListener(queues = "C")
            @RabbitHandler
            public void C(Message msg) { #这是rabbitmq原始消息类型
                Object obj = msg.getBody();
                ...
            }            
        }
        用最上面两种即可满足所有的业务需求
        
        
**3.命名规范**

 
    队列名称请以各自的微服务名+：开头,如： user:xxx
   
---
@author: wangxianchen

@date:2017/08/18