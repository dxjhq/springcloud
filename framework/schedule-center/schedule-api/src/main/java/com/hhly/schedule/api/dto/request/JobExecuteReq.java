package com.hhly.schedule.api.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author pengchao
 * @create 2017-09-19
 * @desc
 */
@Getter
@Setter
public class JobExecuteReq implements Serializable {
    private int taskId;
    private String taskName;
    private int logId;
}

