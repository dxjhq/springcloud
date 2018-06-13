消息推送整合
---

 **version** 1.0
 
**一.设计思路**

 - 对消息推送进行了封装,让其异步发送.
 - 邮件基于SpringBoot Mail,可发送纯文本格式,HTML格式和以freemarker为模板的HTML邮件
 - 短信基于common包下的SDK
 - 操作简单,在POM中引入该模块,并在boot启动类中引入配置即可.具体请参考下文中的使用方法

 

**二.使用方法**

 - POM引入im-service依赖
    
         <dependency>
           <groupId>${project.groupId}</groupId>
           <artifactId>im-service</artifactId>
           <version>${project.version}</version>
         </dependency>
       		
 - yml文件配置Email连接信息
    
         spring:
           mail:
             host: smtp.163.com
             username: wxc_002@163.com
             password: ENC(61dZY3QL4fCe1Q1tn4zBZvG91wchCXme)
             properties:
               mail:
                 smtp:
                   auth: true
                   starttls:
                     enable: true
                     required: true
   
 - 在root启动类中引入ImTaskConfig配置
    
         如下例所示:
         ...
         @SpringBootApplication
         @Import({BaseConfiguration.class,
                ImTaskConfig.class
         })
         public class Application {
        
            public static void main(String[] args) {
                SpringApplication.run(Application.class, args);
            }
         }
         ...
         注意引用顺序

        
 - 服务调用
        
         1.邮件
            @Autowired
            EmailService emailService;
            
            连接池配置,请创建一个bean. 不配置将使用默认值. 例子如下:
            @Bean
            public TaskExecutorProperties taskExecutorProperties(){
                TaskExecutorProperties properties = new TaskExecutorProperties();
                properties.setCorePoolSize(3);
                properties.setMaxPoolSize(10);
                properties.setQueueCapacity(50);
                properties.setKeepAliveSeconds(300);
                return properties;
            }
            
            本服务利用freemarker进行HTML类型邮件的模板渲染,如不配置freemarker,默认寻找classpath:/templates/路径下的以.ftl结尾的模板文件
            配置例子如下:
            spring:
              freemarker:
                 allow-request-override: false
                 allow-session-override: false
                 check-template-location: true
                 cache: true
                 charset: UTF-8
                 enabled: true
                 content-type: text/html
                 order: 1
                 expose-request-attributes: false
                 expose-session-attributes: false
                 expose-spring-macro-helpers: true
                 prefer-file-system-access: true
                 suffix: .ftl
                 template-loader-path: classpath:/templates/
                 #设定静态文件路径，js,css等
                 static-path-pattern: /static/**
                 settings:
                    template_update_delay: 0
                    default_encoding: UTF-8
                    classic_compatible: true
        
         2.短信
            @Autowired
            SmsService smsService
            
            服务默认实现了发送同一类消息的时间间隔校验,如果使用此方法,请创建一个bean,例子如下:
            @Bean
            public SmsConfigProperties smsConfigProperties(){
                SmsConfigProperties smsConfigProperties = new SmsConfigProperties();
                Map<String,Integer> bizMap = new HashMap<>();
                bizMap.put("Login",60);  //业务类型--间隔秒数
                bizMap.put("Register",120);
                smsConfigProperties.setBizMap(bizMap);
                return smsConfigProperties;
            }
            
         3.自定义扩展
            邮件可  extends EmailService 或者是 extends AbstractEmailService
            短信可  extends SmsService 或者是 extends AbstractSmsService

       
        
**三.关于数据源连接信息中的密码加密设置**        
 
 - 添加加/解密工具
 
         <dependency>
             <groupId>com.github.ulisesbocchio</groupId>
             <artifactId>jasypt-spring-boot-starter</artifactId>
             <version>1.16</version>
         </dependency>
          
 - 在application.yml文件中添加配置
 
         jasypt:
           encryptor:
             password: ABCD123456 #这是密码盐
              
         ---------使用很简单.如下: ENC(...) 是格式, 里面是已经过jasypt加密的字符串--------       
         spring:
            xxx:
              username: xxxxxx
              password: ENC(61dZY3QL4fCe1Q1tn4zBZvG91wchCXme)
                 
 - [关于jasypt更多,请点击参阅](https://github.com/ulisesbocchio/jasypt-spring-boot)     
         
---
@author: wangxianchen

@date:2017/08/18