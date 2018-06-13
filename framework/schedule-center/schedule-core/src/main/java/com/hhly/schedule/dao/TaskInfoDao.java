package com.hhly.schedule.dao;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.hhly.common.dao.AbstractBaseDao;
import com.hhly.schedule.api.dto.request.TaskInfoQueryReq;
import com.hhly.schedule.entity.TaskInfo;
import com.hhly.schedule.mapper.TaskInfoMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ：pengchao
 * @createTime ：2017/09/25
 */
@Repository
public class TaskInfoDao extends AbstractBaseDao<TaskInfoMapper, TaskInfo> {
    public TaskInfo selectUniqueness(String taskName){
        return this.baseMapper.selectUniqueness(taskName);
    }

    public List<TaskInfo> selectWithPage(@Param("condition") TaskInfoQueryReq condition, PageBounds pageBounds){
        return this.baseMapper.selectWithPage(condition,pageBounds);
    }

    public boolean updatePreExecuteTime(int id){
        return this.baseMapper.updatePreExecuteTime(id)==1;
    }
}