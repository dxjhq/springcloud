package com.hhly.schedule.core.web;

import com.hhly.common.dto.ErrorCodeEnum;
import com.hhly.common.dto.ResultObject;
import com.hhly.common.util.LogUtil;
import com.hhly.schedule.api.constant.ScheduleUrl;
import com.hhly.schedule.client.annotation.ScheduleHandlerTag;
import com.hhly.schedule.client.annotation.ScheduleTag;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author pengchao
 * @create 2017-09-20
 * @desc
 */
@RestController
@ScheduleTag
public class DemoTaskController {
    private static final Logger logger = LoggerFactory.getLogger(DemoTaskController.class);
    /**
     * 根据ID获取
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ScheduleUrl.TASK_DEMO_EXECUTE,method = RequestMethod.POST)
    @ScheduleHandlerTag(cronExpression = "0/35 * 19 * * ?",scheduleApi = ScheduleUrl.TASK_DEMO_EXECUTE,scheduleMethod="POST")
    public ResultObject execute() {
        ResultObject result = new ResultObject();
        try {
            result.setData("Task执行成功");
            logger.info("Task执行成功");
            //System.out.println("Task执行成功");
        } catch (Exception e) {
            LogUtil.ROOT_LOG.error(ErrorCodeEnum.FAIL.message(),e);
        }
        return result;
    }
}
