package com.hhly.businesscomm.ice;

import com.hhly.membercenter.zreocsrvi.MemberPrxHelper;
import com.hhly.paycenter.zreocsrvi.PaymentPrxHelper;
import com.hhly.utils.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IceComponent {

    @Autowired
    private IceProperties iceProperties;

    /**
     * 调用用户中心的接口:<br><br>
     * 服务器端定义好的接口名和类分别是: member, Member.class<br>
     * 调用方法 memberOperation
     *
     * @param req 调用参数, 一般是 {@link com.hhly.membercenter.protocol.user.AlipayLoginReq } 这种
     * @param clazz 返回结果的类, 一般是 {@link com.hhly.membercenter.protocol.user.AlipayLoginResp } 这种
     * @return 调用成功则返回 json 结果对应的 类对象
     */
    public <T> T handleMember(Object req, Class<T> clazz) {
        Object result = handleMember(req);
        if (result == null) return null;

        return JsonUtil.toObject(result.toString(), clazz);
    }

    /**
     * 调用用户中心的接口<br><br>
     * 服务器端定义好的接口名和类分别是: member, Member.class<br>
     * 调用方法 memberOperation
     *
     * @param req 调用参数, 一般是 {@link com.hhly.membercenter.protocol.user.AlipayLoginReq } 这种
     * @return 调用成功则返回相关的 json 结果
     */
    public Object handleMember(Object req) {
        if (req == null) return null;

        IceProperties.Info user = iceProperties.getUser();
        return IceUtils.getClient(user.getCfgPath(), user.getIp(), user.getPort(), user.getType(), user.getTimeOut(),
                "member", MemberPrxHelper.class, "memberOperation", JsonUtil.toJson(req));
    }

    /**
     * 调用支付中心的接口<br><br>
     * 服务器端定义好的接口名和类分别是: Payment, Payment.class<br>
     * 调用方法 paymentOperation
     *
     * @param req 调用参数, 一般是 {@link com.hhly.paycenter.protocol.ali.AlipayQueryReq } 这种
     * @param clazz 返回结果的类, 一般是 {@link com.hhly.paycenter.protocol.ali.AlipayQueryResp } 这种
     * @return 调用成功则返回相关的 json 结果
     */
    public <T> T handlePayment(Object req, Class<T> clazz) {
        Object result = handlePayment(req);
        if (result == null) return null;

        return JsonUtil.toObject(result.toString(), clazz);
    }

    /**
     * 调用支付中心的接口<br><br>
     * 服务器端定义好的接口名和类分别是: Payment, Payment.class<br>
     * 调用方法 paymentOperation
     *
     * @param req 调用参数, 一般是 {@link com.hhly.paycenter.protocol.ali.AlipayQueryReq } 这种
     * @return 调用成功则返回相关的 json 结果
     */
    public Object handlePayment(Object req) {
        if (req == null) return null;

        IceProperties.Info pay = iceProperties.getPay();
        return IceUtils.getClient(pay.getCfgPath(), pay.getIp(), pay.getPort(), pay.getType(), pay.getTimeOut(),
                "Payment", PaymentPrxHelper.class, "paymentOperation", JsonUtil.toJson(req));
    }
}
