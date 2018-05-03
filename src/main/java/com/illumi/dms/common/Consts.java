package com.illumi.dms.common;

public class Consts
{

	
	public static final String SESSION_USER = "user";


	/**
	 * 接受参数
	 */
	//----------------------start--------------------

	// 开始时间
	public static final String START_TIME = "start_time";
	// 结束时间
	public static final String END_TIME = "end_time";
	// 是否是海外
	public static final String IS_ABROAD = "is_abroad";
	// 过去间隔 默认 过去 1，7，30 天
	public static final String PERIOD = "period";
	// 页数
	public static final String PAGE = "page";
	// 每页的数量
	public static final Integer TOTAL = 50;
	// 排序 asc 或者desc
	public static final String ORDER = "order";
	// 排序字段名称
	public static final String SORT_FIELD = "sortField";

	// ----------------------end---------------------

	public static final boolean OPEN_REDIS = true;

	public static void main(String[] args){
	}

}
