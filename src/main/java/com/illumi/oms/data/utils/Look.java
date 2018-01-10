package com.illumi.oms.data.utils;

public class Look {
	// 类型
	private String type;
	// 日志记录字段
	private String key;
	// 表情名称
	private String name;
//	//数量
//	private Long num;

	public Look(String key, String type, String name) {
		super();
		this.key = key;
		this.type = type;
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}