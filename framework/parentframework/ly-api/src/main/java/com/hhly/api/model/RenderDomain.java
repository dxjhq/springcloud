package com.hhly.api.model;

import lombok.Getter;
import lombok.Setter;

@Deprecated
@Getter
@Setter
public class RenderDomain {

    /** pc 端用户对应的域名 */
    private String index;

    /** pc 端律师对应的域名 */
    private String lawyer;

    /** 接口调用的域名, 走 https */
    private String api;

    /** 后台系统的域名 */
    private String manager;


    /** 资源文件域名, css png js 等 */
    private String still;

    /** 用户上传的图片、文件(按目录区分)域名 */
    private String upload;

    /**机器人对应域名**/
    private String bot;
}
