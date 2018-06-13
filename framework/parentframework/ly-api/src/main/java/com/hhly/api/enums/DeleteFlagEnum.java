package com.hhly.api.enums;

/**
 * @author wangxianchen
 * @create 2017-08-22
 * @desc 删除标识(0:未删除 1:已删除)
 */
public enum DeleteFlagEnum {

    NO((byte)0,"未删除"),

    YES((byte)1,"已删除");

    private Byte code;

    private String label;

    DeleteFlagEnum(byte code, String label){
        this.code = code;
        this.label = label;
    }

    public String getLabelByCode(Byte code){
        for(DeleteFlagEnum obj: DeleteFlagEnum.values()){
            if(obj.code.equals(code)){
                return obj.label;
            }
        }
        return null;
    }

    public Byte getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
