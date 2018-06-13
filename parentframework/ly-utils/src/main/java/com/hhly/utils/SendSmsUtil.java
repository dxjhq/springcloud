package com.hhly.utils;

import com.hhly.utils.web.HttpHelper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author zhangfg
 * @create 2017-07-13 11:37
 **/
public class SendSmsUtil {

    // 帐号
    static String account = "ZD30059";
    // 密码
    static String password = "huahai30059";
    // 校验码
    static String veryCode = "hic3iamjsxvf";

    final static String HTTP_URL = "http://120.27.149.106:8030";

    /**
     * 模版短信,无需人工审核，直接发送 (短信模版的创建参考客户端操作手册)
     *
     * 模版：@1@会员，动态验证码@2@(五分钟内有效)。请注意保密，勿将验证码告知他人。 参数值:@1@=member,@2@=4293
     * 最终短信内容：【短信签名】member会员，动态验证码4293(五分钟内有效)。请注意保密，勿将验证码告知他人。
     *
     * 提交路径： UTF-8编码：/service/httpService/httpInterface.do?method=sendUtf8Msg
     * GBK编码：/service/httpService/httpInterface.do?method=sendGbkMsg
     *
     * @param mobile
     *            手机号码
     * @param tempId
     *            模版编号，对应客户端模版编号
     * @param msgContentParams
     *            各参数值，以英文逗号隔开，如：@1@=member,@2@=4293
     * @return
     */
    public static String sendTemplateSms(String mobile, String tempId, LinkedList<String> msgContentParams) {
        String address = HTTP_URL + "/service/httpService/httpInterface.do?method=sendUtf8Msg";

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < msgContentParams.size(); i++) {
            sb.append(",");
            sb.append("@");
            sb.append((i + 1));
            sb.append("@=");
            sb.append(msgContentParams.get(i));
        }
        String content = sb.toString();
        content = content.replaceFirst(",", "");
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("username", account);
        reqParams.put("password", password);
        reqParams.put("veryCode", veryCode);
        reqParams.put("mobile", mobile);
        reqParams.put("content", content);
        reqParams.put("msgtype", "2");
        reqParams.put("tempid", tempId);
        reqParams.put("code", "utf-8");

        String result = HttpHelper.doPost(address, reqParams, "UTF-8");
        LogUtil.ROOT_LOG.info("发送短信结果:" + result);
        return result;
    }

    /**
     * 发送普通短信 普通短信发送需要人工审核 提交路径：
     * UTF-8编码：/service/httpService/httpInterface.do?method=sendUtf8Msg
     * GBK编码：/service/httpService/httpInterface.do?method=sendGbkMsg
     *
     * @param mobile  手机号码, 多个号码以英文逗号隔开,最多支持100个号码
     * @param content  短信内容
     * @return
     */
    public static String sendSms(String mobile, String content) {
        String address = HTTP_URL + "/service/httpService/httpInterface.do?method=sendUtf8Msg";

        Map<String, String> params = new HashMap<>();
        params.put("username", account);
        params.put("password", password);
        params.put("veryCode", veryCode);
        params.put("mobile", mobile);
        params.put("content", content);
        params.put("msgtype", "1");
        params.put("code", "utf-8");

        String result = HttpHelper.doPost(address, params, "UTF-8");
        LogUtil.ROOT_LOG.info("发送短信结果:" + result);
        return result;
    }

/*
    public static void main(String[] args) throws Exception {
        // 普通短信
         String ret = sendSms("15002190347", "亲爱的律师平台用户,您正在进行手机注册操作,验证码是");
        System.out.println(ret);

        // 模板短信
        //尊敬的@1@律师，您好！您的订单编号**@2@用户已确认交付，订单总额@3@，请登录查看。如有疑问，请关注“律正法律服务”微信号进行咨询，谢谢！
        LinkedList<String> repaceParams = new LinkedList<>();
        repaceParams.add("蓝天3434");
        repaceParams.add("521365");
        repaceParams.add("244");
        String result = sendTemplateSms("15002190347", "ZD30059-0005", repaceParams);
        System.out.println(result);
    }
*/
}