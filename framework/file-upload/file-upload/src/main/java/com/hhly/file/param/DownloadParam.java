package com.hhly.file.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * @author BSW
 * @create 2017-09-29
 * @desc
 */
@Setter
@Getter
@ToString
public class DownloadParam {
    @NotEmpty(message = "用户ID不能为空")
    private String userId;
    @NotEmpty(message = "图片ID不能为空")
    private String picId;
    private int startWith = 0;

}
