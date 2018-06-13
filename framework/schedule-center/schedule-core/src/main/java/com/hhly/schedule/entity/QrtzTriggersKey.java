package com.hhly.schedule.entity;

import com.hhly.common.entity.BaseEntity;
import lombok.ToString;

/**
 * @author ：pengchao
 * @createTime ：2017/09/25
 */
@ToString
public class QrtzTriggersKey extends BaseEntity {

    private String schedName;

    private String triggerName;

    private String triggerGroup;

    /** 
     * 获取 QRTZ_TRIGGERS.SCHED_NAME
     * @return QRTZ_TRIGGERS.SCHED_NAME
     */
    public String getSchedName() {
        return schedName;
    }

    /** 
     * 设置 QRTZ_TRIGGERS.SCHED_NAME
     * @param schedName QRTZ_TRIGGERS.SCHED_NAME
     */
    public void setSchedName(String schedName) {
        this.schedName = schedName == null ? null : schedName.trim();
    }

    /** 
     * 获取 QRTZ_TRIGGERS.TRIGGER_NAME
     * @return QRTZ_TRIGGERS.TRIGGER_NAME
     */
    public String getTriggerName() {
        return triggerName;
    }

    /** 
     * 设置 QRTZ_TRIGGERS.TRIGGER_NAME
     * @param triggerName QRTZ_TRIGGERS.TRIGGER_NAME
     */
    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName == null ? null : triggerName.trim();
    }

    /** 
     * 获取 QRTZ_TRIGGERS.TRIGGER_GROUP
     * @return QRTZ_TRIGGERS.TRIGGER_GROUP
     */
    public String getTriggerGroup() {
        return triggerGroup;
    }

    /** 
     * 设置 QRTZ_TRIGGERS.TRIGGER_GROUP
     * @param triggerGroup QRTZ_TRIGGERS.TRIGGER_GROUP
     */
    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup == null ? null : triggerGroup.trim();
    }
}