package com.illumi.oms.data.model;

import java.util.List;
import java.util.Map;

public class ExcelTableSheet {
	    private String title;
		private String sheetname;
		private String[] head; //表头
		private List<Map<String,String>> rows;//每一行数据    map("列名"，"值")
		
		
		public ExcelTableSheet() {}
		public ExcelTableSheet(String title,String[] head, List<Map<String, String>> rows,String sheetname) {
			super();
			this.title = title;
			this.sheetname = sheetname;
			this.head = head;
			this.rows = rows;
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
		public List<Map<String, String>> getRows() {
			return rows;
		}
		public void setRows(List<Map<String, String>> rows) {
			this.rows = rows;
		}
		
		
		
		
}
