package com.hhly.schedule.mapper;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.hhly.common.dao.BaseMapper;
import com.hhly.schedule.api.dto.request.TaskInfoQueryReq;
import com.hhly.schedule.entity.TaskInfo;

import java.util.List;
import org.apache.ibatis.annotations.Param;
/**
  * @author ：pengchao
 * @createTime ：2017/09/25
 */
public interface TaskInfoMapper extends BaseMapper<TaskInfo> {
    TaskInfo selectUniqueness(String taskName);

    int updatePreExecuteTime(int id);

    List<TaskInfo> selectWithPage(@Param("condition") TaskInfoQueryReq condition, PageBounds pageBounds);
}