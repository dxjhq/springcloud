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

import java.util.Random;

/**
 * @author pengchao
 * @create 2017-09-20
 * @desc
 */
@RestController
@ScheduleTag
public class DemoScheduleController{

    private static final Logger logger = LoggerFactory.getLogger(DemoScheduleController.class);

    /**
     * 根据ID获取
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ScheduleUrl.SCHEDULE_DEMO_EXECUTE, method = RequestMethod.GET)
    @ScheduleHandlerTag(cronExpression = "0/10 * 19 * * ?", scheduleApi = ScheduleUrl.SCHEDULE_DEMO_EXECUTE, paramsJson = "{\"code\":\"code1\",\"name\":\"name1\"}")
    public ResultObject execute(DemoExecuteReq demoExecuteReq) {
        ResultObject result = new ResultObject();
        try {
            Random r = new Random();
            Double d = r.nextDouble();
            logger.info(d.toString() + " : Schedule执行开始");
            Thread.sleep(15000);
            result.setData("Schedule执行成功");
            logger.info(d.toString() + " : Schedule执行结束");
            //System.out.println("Schedule执行成功");
        } catch (Exception e) {
            LogUtil.ROOT_LOG.error(ErrorCodeEnum.FAIL.message(), e);
        }
        return result;
    }

    /**
     * 根据ID获取
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ScheduleUrl.SCHEDULE_DEMO_EXECUTE1, method = RequestMethod.GET)
    @ScheduleHandlerTag(cronExpression = "0/10 * * * * ?", scheduleApi = ScheduleUrl.SCHEDULE_DEMO_EXECUTE, paramsJson = "{\"code\":\"code1\",\"name\":\"name1\"}")
    public void execute1() {
        try {
            Random r = new Random();
            Double d = r.nextDouble();
            logger.info(d.toString() + " : Schedule执行开始");
            Thread.sleep(1000);
            logger.info(d.toString() + " : Schedule执行结束");
            //System.out.println("Schedule执行成功");
        } catch (Exception e) {
            LogUtil.ROOT_LOG.error(ErrorCodeEnum.FAIL.message(), e);
        }
    }
}
