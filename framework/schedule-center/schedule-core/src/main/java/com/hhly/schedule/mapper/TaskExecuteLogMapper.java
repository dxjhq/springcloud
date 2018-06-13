package com.hhly.schedule.mapper;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.hhly.common.dao.BaseMapper;
import com.hhly.schedule.api.dto.request.TaskExecuteLogQueryReq;
import com.hhly.schedule.api.dto.request.TaskInfoQueryReq;
import com.hhly.schedule.entity.TaskExecuteLog;
import com.hhly.schedule.entity.TaskInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ：pengchao
 * @createTime ：2017/09/25
 */
public interface TaskExecuteLogMapper extends BaseMapper<TaskExecuteLog> {
    TaskExecuteLog getLastExecuteLogByTask(Long taskId);

    TaskExecuteLog getLastSuccessLogByTask(Long taskId);

    List<TaskExecuteLog> selectWithPage(@Param("condition") TaskExecuteLogQueryReq condition, PageBounds pageBounds);
}