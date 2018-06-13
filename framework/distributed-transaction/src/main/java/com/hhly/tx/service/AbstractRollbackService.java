package com.hhly.tx.service;


import com.hhly.tx.pojo.TxMessage;
import com.hhly.tx.pojo.TxResErroInf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author wangxianchen
 * @create 2017-11-20
 * @desc 事务回滚抽象处理
 */
public abstract class AbstractRollbackService implements BaseRollbackService {

    protected TxResErroInf txResErroInf;

    @Override
    public final void setTxResErroInf(TxMessage txMessage, String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        this.txResErroInf = new TxResErroInf();
        this.txResErroInf.setRollbackId(txMessage.getRollbackId());
        this.txResErroInf.setErrCode(txMessage.getErrCode());
        this.txResErroInf.setErrType(txMessage.getErrType());
        Method method = this.getClass().getDeclaredMethod(methodName);
        method.invoke(this);
    }
}
