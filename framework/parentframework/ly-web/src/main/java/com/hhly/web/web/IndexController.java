package com.hhly.web.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wangxianchen
 * @create 2017-12-25
 * @desc springcloud管理界面服务状态
 */
@Controller
public class IndexController {
    private static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Value("${spring.application.name:}")
    String appName;

    @Value("${spring.cloud.config.profile:}")
    String profile;

    @Value("${spring.cloud.config.label:}")
    String label;

    @Value("${spring.profiles.active:}")
    String active;


    /**
     * @desc 查看服务状态
     * @author wangxianchen
     * @create 2018-01-31
     * @param modelMap
     * @return
     */
    @RequestMapping("/status")
    public String status(ModelMap modelMap){
        modelMap.put("appName",appName);
        modelMap.put("profile",profile);
        modelMap.put("label",label);
        modelMap.put("active",active);
        return "statusInfo";
    }

    /**
     * @desc 解决浏览器自发的默认请求，去掉那厌人的报错
     * @author wangxianchen
     * @create 2018-01-31
     */
    @RequestMapping("/favicon.ico")
    @ResponseBody
    public String index(){
        return "favicon.ico";
    }
}
