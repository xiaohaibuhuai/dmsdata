package com.illumi.dms.common;

public class ResultInfo {
	private int code;
	private String msg;
	private Object obj;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	@Override
	public String toString(){
		return "code:"+code+",msg:"+msg+",obj:"+obj.toString();
	}
	
}
