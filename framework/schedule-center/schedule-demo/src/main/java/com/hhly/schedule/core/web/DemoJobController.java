package com.hhly.schedule.core.web;

import com.hhly.common.dto.ErrorCodeEnum;
import com.hhly.common.dto.ResultObject;

import com.hhly.common.util.LogUtil;
import com.hhly.schedule.api.constant.ScheduleUrl;
import com.hhly.schedule.api.dto.request.DemoExecuteReq;

import com.hhly.schedule.client.annotation.ScheduleHandlerTag;
import com.hhly.schedule.client.annotation.ScheduleTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * @author pengchao
 * @create 2017-09-20
 * @desc
 */
@RestController
@ScheduleTag
public class DemoJobController {
    private static final Logger logger = LoggerFactory.getLogger(DemoJobController.class);
    /**
     * 根据ID获取
     * @return
     */

    @ResponseBody
    @RequestMapping(value = ScheduleUrl.JOB_DEMO_EXECUTE,method = RequestMethod.POST)
    @ScheduleHandlerTag(cronExpression = "0/5 * * * * ?",scheduleApi = ScheduleUrl.JOB_DEMO_EXECUTE,scheduleMethod="POST",paramsJson = "{\"code\":\"code1\",\"name\":\"name1\"}")
    public ResultObject execute(@RequestBody DemoExecuteReq demoExecuteReq) {
        ResultObject result = new ResultObject();
        try {
            result.setData("Job执行成功");
            logger.info("Job执行成功");
            Thread.sleep(8000);
            //System.out.println("Job执行成功");
        } catch (Exception e) {
            LogUtil.ROOT_LOG.error(ErrorCodeEnum.FAIL.message(),e);
        }
        return result;
    }

}
