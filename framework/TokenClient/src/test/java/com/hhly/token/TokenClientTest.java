package com.hhly.token;

import com.hhly.token.component.SpringContextUtil;
import com.hhly.token.model.LocalProfile;
import com.hhly.token.service.TokenClient;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Suite;
import org.springframework.context.annotation.Import;

/**
 * @author pengchao
 * @create 2017-12-13
 * @desc
 */
/*
@RunWith就是一个运行器
@RunWith(JUnit4.class)就是指用JUnit4来运行
@RunWith(SpringJUnit4ClassRunner.class),让测试运行于Spring测试环境
@RunWith(Suite.class)的话就是一套测试集合，
*/
@RunWith(JUnit4.class)
//@RunWith(Suite.class)
@Suite.SuiteClasses({TokenClient.class})
@Import({SpringContextUtil.class})
public class TokenClientTest {
    static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW5vVGltZSI6NTY5NTkxMTQ4NzA0MjczMywiY2VsbHBob25lIjoiMTU4MTM4MDQ2MzUiLCJ1c2VyaWQiOiIzNzUxIiwiYWNjb3VudCI6IjE1ODEzODA0NjM1Iiwia2V5IjoidXNlcjpzZXNzaW9uX3VzZXI6Mzc1MTpGOUM1NDI0MDc5RUQ3QkVDMTBGMTIzQTMzODdBOTdBQyIsInVzZXJuYW1lIjpudWxsfQ.fSjo6TtzqD47r4T14D6qjf0CCaqLseeOtoyoud9PryI-0073cb8a48781100fe81b2f28a729c10";

    /**
     * @throws Exception
     * @BeforeClass和@AfterClass在类被实例化前（构造方法执行前）就被调用了，而且只执行一次，通常用来初始化和关闭资源。
     */
    @BeforeClass
    public static void beforeClass() throws Exception {
    }
    @AfterClass
    public static void afterClass() throws Exception {
    }

    /**
     * @throws Exception
     * @Before和@After和在每个@Test执行前后都会被执行一次。
     */
    @Before
    public void before() throws Exception {
    }
    @After
    public void after() throws Exception {
    }

    @Test
    public void calculateTokenTest()  throws Exception {
        LocalProfile localProfile = new LocalProfile();
        //localProfile.setSid("0073cb8a48781100fe81b2f28a729c10");
        localProfile.setTimeout(7200L);
        localProfile.setAccount("15813804635");
        localProfile.setCellphone("15813804635");
        localProfile.setUserid("3751");
        localProfile.setNanoTime(System.nanoTime());
        localProfile.setUserkey("user:session_user:3751:F9C5424079ED7BEC10F123A3387A97AC");

        localProfile = TokenClient.calculateToken(localProfile);
        System.out.println(localProfile);
    }

    @Test
    public void getOriginalJwtTest() throws Exception {
        System.out.println(TokenClient.getOriginalJwt(token));
    }

    @Test
    public void getProfileMapTest() throws Exception {
        System.out.println(TokenClient.getProfileMap(token));
    }

    @Test
    public void validateTest() throws Exception {
        boolean flag = TokenClient.validate(token);
        System.out.println(flag);
    }

    @Test
    public void refreshExpireTest() throws Exception {
        boolean flag = TokenClient.refreshExpire(token,0L);
        System.out.println(flag);
    }
}