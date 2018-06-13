package com.hhly.schedule.web;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.hhly.common.dto.ErrorCodeEnum;
import com.hhly.common.dto.ResultObject;
import com.hhly.common.exception.ValidationException;
import com.hhly.common.util.LogUtil;
import com.hhly.schedule.api.constant.ScheduleUrl;
import com.hhly.schedule.api.dto.request.TaskInfoQueryReq;
import com.hhly.schedule.api.dto.request.TaskInfoReq;
import com.hhly.schedule.service.TaskInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author pengchao
 * @create 2017-09-26
 * @desc
 */
@RestController
public class TaskInfoController {

    @Autowired
    private TaskInfoService taskInfoService;

    /**
     * 根据ID获取用户信息
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ScheduleUrl.SCHEDULE_TASK_GET,method = RequestMethod.GET)
    public ResultObject get(Integer id) {
        if(id<=0 ){
            return new ResultObject(ErrorCodeEnum.PARAM_EXCEPTION.format("id错误"));
        }

        ResultObject result = new ResultObject();
        try {
            result = taskInfoService.get(id);
        } catch (Exception e) {
            LogUtil.ROOT_LOG.error(ErrorCodeEnum.QUERY_DATA_ERROR.message(),e);
            result.fail(ErrorCodeEnum.QUERY_DATA_ERROR);
        }
        return result;
    }

    /**
     * @desc 新增
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ScheduleUrl.SCHEDULE_TASK_REGISTER, method = RequestMethod.POST)
    public ResultObject register(@RequestBody TaskInfoReq taskInfoReq){
        ResultObject result = new ResultObject();
        try {
            result = taskInfoService.register(taskInfoReq);
        } catch (Exception e) {
            LogUtil.ROOT_LOG.error(ErrorCodeEnum.QUERY_DATA_ERROR.message(),e);
            result.fail(ErrorCodeEnum.QUERY_DATA_ERROR);
        }
        return result;
    }

    /**
     * @desc 更新
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ScheduleUrl.SCHEDULE_TASK_UPDATE, method = RequestMethod.PUT)
    public ResultObject update(@RequestBody @Validated TaskInfoReq taskInfoReq) {
        if(taskInfoReq.getId()<=0 ){
            return new ResultObject(ErrorCodeEnum.PARAM_EXCEPTION.format("id错误"));
        }

        ResultObject result = new ResultObject();
        try {
            result = taskInfoService.update(taskInfoReq);
        } catch(ValidationException e){
            LogUtil.ROOT_LOG.error(e.getErrorCodeEnum().message()+e.getParam(),e);
            result.fail(e.getErrorCodeEnum());
        }catch (Exception e) {
            LogUtil.ROOT_LOG.error(ErrorCodeEnum.SAVE_DATA_ERROR.message(),e);
            result.fail(ErrorCodeEnum.UPDATE_DATA_ERROR);
        }
        return result;
    }

    /**
     * @desc 删除
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ScheduleUrl.SCHEDULE_TASK_DELETE, method = RequestMethod.DELETE)
    public ResultObject delete(Integer id) {
        if(id<=0 ){
            return new ResultObject(ErrorCodeEnum.PARAM_EXCEPTION.format("id错误"));
        }
        ResultObject result = new ResultObject();
        try {
            result = taskInfoService.delete(id);
        }catch (Exception e) {
            LogUtil.ROOT_LOG.error(ErrorCodeEnum.SAVE_DATA_ERROR.message(),e);
            result.fail(ErrorCodeEnum.DELETE_DATA_ERROR);
        }
        return result;
    }

    /**
     * @desc 获取分页列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ScheduleUrl.SCHEDULE_TASK_PAGE,method = RequestMethod.GET)
    public ResultObject page(TaskInfoQueryReq taskInfoQueryReq, PageBounds pageBounds) {
        return taskInfoService.page(taskInfoQueryReq,pageBounds);
    }

}
