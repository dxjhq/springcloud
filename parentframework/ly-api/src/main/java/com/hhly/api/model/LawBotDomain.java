package com.hhly.api.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by cy on 2017/4/21
 * 可能已经不使用
 */
@Deprecated
@Getter
@Setter
public class LawBotDomain {
    /**罪名预测**/
    private String crimeUrl;
    /**相似案例**/
    private String caseUrl;

    /***案件详情***/
    private String caseDetailUrl;
    /***法条详情***/
    private String  criminalLawDetail;

    /**刑法条款信息**/
    private String criminalLawUrl;

}
