package com.hhly.tx.service;

import com.hhly.tx.pojo.TxMessage;

import java.lang.reflect.InvocationTargetException;

/**
 * @author wangxianchen
 * @create 2017-11-20
 * @desc 事务回滚接口
 */
public interface BaseRollbackService {

    void setTxResErroInf(TxMessage txMessage,String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
