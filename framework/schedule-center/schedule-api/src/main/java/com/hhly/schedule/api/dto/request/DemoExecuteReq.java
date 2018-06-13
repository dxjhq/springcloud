package com.hhly.schedule.api.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author pengchao
 * @create 2017-09-29
 * @desc
 */
@Getter
@Setter
public class DemoExecuteReq extends JobExecuteReq {
    private String code;

    private String name;
}
