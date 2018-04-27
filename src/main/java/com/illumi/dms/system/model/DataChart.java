package com.illumi.dms.system.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataChart
{
	public List<String> categories = new ArrayList<String>();
	
	public List<Object> data = new ArrayList<Object>();
	
	public String datePara = "";
	
	public Integer totalNum = 0;
	
	public Integer countryNum = 0;
	
	public void setData(Integer yNum ,String color, String name,List<String> categories,List<Integer> dataNum){
		
		Map<String ,Object> datas = new HashMap<String ,Object>();
		
		Map<String ,Object> dataCtiy = new HashMap<String ,Object>();
		
		datas.put("y", yNum);
		datas.put("color", color);
		
		dataCtiy.put("name", name);
		dataCtiy.put("categories", categories);
		dataCtiy.put("data", dataNum);
		dataCtiy.put("color", color);
		
		datas.put("drilldown", dataCtiy);
		
		data.add( datas);
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public String getDatePara() {
		return datePara;
	}

	public void setDatePara(String datePara) {
		this.datePara = datePara;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public Integer getCountryNum() {
		return countryNum;
	}

	public void setCountryNum(Integer countryNum) {
		this.countryNum = countryNum;
	}
	
}