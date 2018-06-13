package com.hhly.schedule.dao;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.hhly.common.dao.AbstractBaseDao;
import com.hhly.schedule.api.dto.request.TaskExecuteLogQueryReq;
import com.hhly.schedule.entity.TaskExecuteLog;
import com.hhly.schedule.mapper.TaskExecuteLogMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ：pengchao
 * @createTime ：2017/09/25
 */
@Repository
public class TaskExecuteLogDao extends AbstractBaseDao<TaskExecuteLogMapper, TaskExecuteLog> {
    public TaskExecuteLog getLastExecuteLogByTask(Long taskId){
        return this.baseMapper.getLastExecuteLogByTask(taskId);
    }

    public TaskExecuteLog getLastSuccessLogByTask(Long taskId){
        return this.baseMapper.getLastSuccessLogByTask(taskId);
    }

    public List<TaskExecuteLog> selectWithPage(@Param("condition") TaskExecuteLogQueryReq condition, PageBounds pageBounds){
        return this.baseMapper.selectWithPage(condition,pageBounds);
    }
}