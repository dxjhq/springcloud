package com.hhly.businesscomm.service;

import com.hhly.businesscomm.ice.IceComponent;
import com.hhly.membercenter.protocol.user.PlatFormRegisterReq;
import com.hhly.businesscomm.BusinessBaseTest;
import com.hhly.utils.LogUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hhly.businesscomm")
public class IceTest extends BusinessBaseTest {

    @Autowired
    private IceComponent iceComponent;


    @Test
    public void ice() {
        PlatFormRegisterReq formReq = new PlatFormRegisterReq();
        formReq.setIp("192.168.10.243");
        formReq.setPort("40000");
        formReq.setName("律师平台");
        formReq.setRate(1D);
        formReq.setGamePageAddress("");
        formReq.setIndexPageAddress("");
        LogUtil.ROOT_LOG.info("测试结果: " + iceComponent.handleMember(formReq));
    }


}
