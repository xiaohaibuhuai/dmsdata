package com.illumi.oms.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Record;

public class ExcelTableSheet {
	    private String title;
		private String sheetname;
		private String[] head; //表头
		private List<Map<String,Object>> rows;//每一行数据    map("列名"，"值")
		//映射关系
		private Map<String, String> transformMap;
		
		
		public ExcelTableSheet(String title,String[] head, List<Map<String, Object>> rows,String sheetname,Map<String, String> transformMap) {
			super();
			this.title = title;
			this.sheetname = sheetname;
			this.head = head;
			this.rows = rows;
			this.transformMap = transformMap;
		}
		public ExcelTableSheet(String title,String[] head,String sheetname,List<Record> rows,Map<String, String> transformMap) {
			super();
			this.title = title;
			this.sheetname = sheetname;
			this.head = head;
			this.rows = paseRecord(rows);
			this.transformMap = transformMap;
		}
		
		
		
		public Map<String, String> getTransformMap() {
			return transformMap;
		}
		public void setTransformMap(Map<String, String> transformMap) {
			this.transformMap = transformMap;
		}
		private List<Map<String, Object>> paseRecord(List<Record> rows) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			
			
			for(Record r:rows) {
				String[] names = r.getColumnNames();
				Map<String, Object> itemMap = new HashMap<String,Object>();
				for(String name:names) {
					itemMap.put(name, r.get(name));
				}
				list.add(itemMap);
			}
			
			
			return list;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getSheetname() {
			return sheetname;
		}
		public void setSheetname(String sheetname) {
			this.sheetname = sheetname;
		}
		public String[] getHead() {
			return head;
		}
		public void setHead(String[] head) {
			this.head = head;
		}
		public List<Map<String, Object>> getRows() {
			return rows;
		}
		public void setRows(List<Map<String, Object>> rows) {
			this.rows = rows;
		}
		
		
		
		
}
