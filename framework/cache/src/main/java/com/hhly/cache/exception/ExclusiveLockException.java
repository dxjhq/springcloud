package com.hhly.cache.exception;

/**
* @author wangxianchen
* @create 2017-11-03
* @desc 排它锁异常
*/
public class ExclusiveLockException extends RuntimeException {

	private static final long serialVersionUID = -4090324184852071544L;

	@Override
	public String getMessage() {
		return super.getMessage();
	}

	public ExclusiveLockException(String msg) {
		super(msg);
	}

	public ExclusiveLockException() {
		super();
	}
}