package com.illumi.oms.data.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jfinal.ext.render.excel.PoiKit;
import com.jfinal.log.Logger;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

public class ExcelRender extends Render{
	

	protected final Logger LOG = Logger.getLogger(getClass());
	private final static String CONTENT_TYPE = "application/msexcel;charset=" + getEncoding();
	private XSSFWorkbook book;
	private String fileName;
	//private String[] headers;
	
	@Override
	public void render() {
		response.reset();
        final String userAgent = request.getHeader("USER-AGENT");
        try {
            String finalFileName = null;
            if(StringUtils.contains(userAgent, "MSIE")){//IE浏览器
                finalFileName = URLEncoder.encode(fileName,"UTF8");
            }else if(StringUtils.contains(userAgent, "Mozilla")){//google,火狐浏览器
                finalFileName = new String(fileName.getBytes(), "ISO8859-1");
            }else{
                finalFileName = URLEncoder.encode(fileName,"UTF8");//其他浏览器
            }
            response.setHeader("Content-Disposition", "attachment; filename=\"" + finalFileName+".xlsx" + "\"");//这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
        } catch (UnsupportedEncodingException e) {
        }
       // response.setHeader("Content-disposition", "attachment; filename=" + finalFileName);
        response.setContentType(CONTENT_TYPE);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
           book.write(os);
        } catch (Exception e) {
            throw new RenderException(e);
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }

        }
    
		
	}

	public XSSFWorkbook getBook() {
		return book;
	}

	public void setBook(XSSFWorkbook book) {
		this.book = book;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public static String getContentType() {
		return CONTENT_TYPE;
	}
	
	

}
