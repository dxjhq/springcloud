package com.hhly.api.constant;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;

/**
 * @author pengchao
 * @create 2017-12-07
 * @desc 分页公共常量池
 */
public final class PageConst {

    /** 分页默认页 */
    public static final int DEFAULT_PAGE_NO = 1;
    /** 分页默认的每页条数 */
    public static final int DEFAULT_LIMIT = 15;
    /** 前台传递过来的分页参数名 */
    public static final String GLOBAL_PAGE = "page";
    /** 前台传递过来的每页条数名 */
    public static final String GLOBAL_LIMIT = "limit";
    /** 跟踪 id. 放入 cookie */
    public static final String GLOBAL_TRACK_ID = "tcd";
    /** 分页空对象 **/
    public static final PageList EMPTY_PAGE_LIST = new PageList<>( new Paginator( DEFAULT_PAGE_NO, DEFAULT_LIMIT, 0 ) );

}
