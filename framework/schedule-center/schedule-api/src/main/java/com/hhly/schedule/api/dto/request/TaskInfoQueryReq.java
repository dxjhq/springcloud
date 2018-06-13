package com.hhly.schedule.api.dto.request;

import com.hhly.common.Const;
import com.hhly.common.annotation.DateCheck;

/**
 * @author pengchao
 * @create 2017-09-26
 * @desc
 */
public class TaskInfoQueryReq {
    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务组名
     */
    private String taskGroup;

    /**
     * 服务名称
     */
    private String remoteServiceName;
    /**
     * 创建时间(开始)
     */
    @DateCheck(value="yyyy-MM-dd HH:mm:ss", message="创建时间格式错误")
    private String createStartTime;

    /**
     * 创建时间(截止)
     */
    @DateCheck(value="yyyy-MM-dd HH:mm:ss", message="创建时间格式错误")
    private String createEndTime;

    private int page = Const.DEFAULT_PAGE_NO;

    private int limit = Const.DEFAULT_LIMIT;
}
