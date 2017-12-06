package com.illumi.oms.system.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chart
{
	public List<String> categories = new ArrayList<String>();
	
	public List<Object> series = new ArrayList<Object>();
	
	public List<Object> drilldown = new ArrayList<Object>();
	
	private String desc;
	
	public void setSeriesDate(String name ,String type,List  data){
		
		Map<String ,Object> datas = new HashMap<String ,Object>();
		
		datas.put("name", name);
		datas.put("type", type);
		datas.put("data", data);
		series.add( datas);
	}
	
	public void setDrilldown(String id,String type,List data) {
        Map<String ,Object> datas = new HashMap<String ,Object>();
		datas.put("id", id);
		datas.put("type", type);
		datas.put("data", data);
		drilldown.add( datas);
	}


	public List<String> getCategories() {
		return categories;
	}


	public void setCategories(List<String> categories) {
		this.categories = categories;
	}


	public List<Object> getSeries() {
		return series;
	}


	public void setSeries(List<Object> series) {
		this.series = series;
	}

	public List<Object> getDrilldown() {
		return drilldown;
	}

	public void setDrilldown(List<Object> drilldown) {
		this.drilldown = drilldown;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	/**
	 * 增加getset方法
	 */
	
}