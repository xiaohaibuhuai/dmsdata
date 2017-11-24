package com.illumi.oms.data.utils;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jayqqaa12.jbase.jfinal.ext.ctrl.Controller;
import com.jfinal.plugin.activerecord.Record;

public class ExcelController extends Controller<Record> {

	
	public void renderNewExcel(XSSFWorkbook book,String fileName) {
		ExcelRender er = new ExcelRender();
		er.setBook(book);
		er.setFileName(fileName);
		render(er);
	}
}
