package com.hhly.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：pengchao
 * @createTime ：2017/09/25
 */
@Getter
@Setter
@ToString
public class QrtzTriggers extends QrtzTriggersKey {

    private String jobName;

    private String jobGroup;

    private String description;

    private Long nextFireTime;

    private Long prevFireTime;

    private Integer priority;

    private String triggerState;

    private String triggerType;

    private Long startTime;

    private Long endTime;

    private String calendarName;

    private Short misfireInstr;

    private byte[] jobData;
}