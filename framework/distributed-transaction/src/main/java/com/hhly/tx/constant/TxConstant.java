package com.hhly.tx.constant;

/**
 * @author wangxianchen
 * @create 2017-11-30
 * @desc 常量池
 */
public class TxConstant {

    //消息redis key中间标识
    public static final String TX_MSG_MAP_PREFIX = "tx_msg_map:";

    //消息默认过期时间 1天
    public static final long EXPIRE_TIME = 86400L;

    //消息ID标志
    public static final String TX_MSG_ID = "tx_msg_id";

    //消息锁redis key中间标识
    public static final String TX_MSG_LOCK = "tx_msg_lock:";
}
