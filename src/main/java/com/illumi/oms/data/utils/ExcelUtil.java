package com.illumi.oms.data.utils;
import java.util.List;
import java.util.Map;


import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.illumi.oms.data.model.ExcelTableSheet;



/**
 * 
 *
 */
public class ExcelUtil {
   
	
	public static XSSFWorkbook getXSSFWorkbook(ExcelTableSheet... sheets) {
		try {
		XSSFWorkbook workBook = new XSSFWorkbook();
		for(ExcelTableSheet sheet:sheets) {
			creatSheet(workBook, sheet.getTitle(),sheet.getHead(), sheet.getRows(), sheet.getSheetname());
		}
		// 文件输出流
	
		return workBook;
		}catch(Exception e) {
			e.printStackTrace();
		}
        return null;
	}
	
	
	public static XSSFWorkbook getXSSFWorkbookSingle(String title,String[] head,  List<Map<String,String>> list) {
		try {
		XSSFWorkbook workBook = new XSSFWorkbook();
		creatSheet(workBook, title,head, list,"sheet1");
		// 文件输出流
		return workBook;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	private static void creatSheet(XSSFWorkbook workBook, String titlename,String[] head, List<Map<String,String>> list,String sheetname) {
		XSSFSheet sheet = workBook.createSheet(sheetname);// 创建一个工作薄对象
		//0初始sheet
		initSheet(sheet,head.length);
		XSSFCellStyle style = initStyle(workBook);
		// 1 设置行数
		  //第一行大标题
		  //sheet.addMergedRegion()
		 //row.setHeightInPoints(23);// 设置行高23像素 
		XSSFRow row0 = sheet.createRow(0);
		XSSFCell titleCell = row0.createCell(0);
		titleCell.setCellStyle(style);
		titleCell.setCellValue(titlename);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, head.length-1));
        
		//第二行头部
		XSSFRow row1 = sheet.createRow(1);
		for(int j=0,n=head.length;j<n;j++) {
			XSSFCell cell = row1.createCell(j);
			//设置样式
			cell.setCellStyle(style);
			cell.setCellValue(head[j]);
			}
		
		
		
		for(int i=0,l=list.size();i<l;i++) {
			XSSFRow row = sheet.createRow(i+2);// 创建一个行对象
			//2创建单元格
			for(int j=0,n=head.length;j<n;j++) {
			XSSFCell cell = row.createCell(j);
			//设置样式
			cell.setCellStyle(style);
			
			
			//
			if(list.get(i).containsKey(head[j])) {
				cell.setCellValue(list.get(i).get(head[j]));
			}else {
				cell.setCellValue(0);
			}
			
			}
		}


	}

	private static void initSheet(XSSFSheet sheet,int size) {
		for(int i=0;i<size;i++) {
			
			sheet.setColumnWidth(i, 5000);// 设置第二列的宽度为
		}
	
		
	}

	private static XSSFCellStyle initStyle(XSSFWorkbook workBook) {
		XSSFCellStyle style = workBook.createCellStyle();// 创建样式对象

		// 设置字体

		XSSFFont font = workBook.createFont();// 创建字体对象

		font.setFontHeightInPoints((short) 15);// 设置字体大小

		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体

		// font.setFontName("黑体");// 设置为黑体字

		style.setFont(font);// 将字体加入到样式对象

		// 设置对齐方式

		style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中

		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中

		// 设置边框

		// style.setBorderTop(HSSFCellStyle.BORDER_THICK);// 顶部边框粗线

		// style.setTopBorderColor(HSSFColor.RED.index);// 设置为红色

		// style.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);// 底部边框双线

		// style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框

		// style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框

		// 格式化日期 
		//style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
		return style;
	}
}