package com.illumi.oms.model;

import com.illumi.oms.common.ResultCode;

public class Result<T> {
	private ResultCode code;
	private T obj;
	private String msg;
	
	
	public ResultCode getCode() {
		return code;
	}
	public void setCode(ResultCode code) {
		this.code = code;
	}
	public T getObj() {
		return obj;
	}
	public void setObj(T obj) {
		this.obj = obj;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString(){
		return "code="+code+",msg="+msg+",obj="+obj;
	}
	
}
