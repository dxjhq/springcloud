package com.hhly.cache.service;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author pengchao
 * @create 2018-01-09
 * @desc 单元测试样例
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisServiceToken.class)
@EnableAutoConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //执行顺序按方法名字典排序
public class RedisServiceTokenTest {

    @Autowired
    RedisServiceToken tokenRedisService;
    @Test
    public void set() throws Exception {
        tokenRedisService.set("test:token:url","www.single.mix");
//        CacheKey key = new CacheKey();
//        key.setServerName("server");
//        key.setModuleName("pay");
//        defaultRedisService.set("test:" + key.getServerName()+":" + key.getModuleName(),key);
//        key = new CacheKey();
//        key.setServerName("server");
//        key.setModuleName("order");
//        defaultRedisService.set("test:" + key.getServerName()+":" + key.getModuleName(),key);
//
//        System.out.println("url:" + defaultRedisService.get("test:url"));
//        System.out.println("order:" + defaultRedisService.get("test:server:order"));
    }
}
