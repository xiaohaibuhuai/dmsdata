package com.illumi.oms.controller;

import java.util.Date;

import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.common.utils.StringUtil;
import com.illumi.oms.model.Notice;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;


@ControllerBind(controllerKey = "/msg/notice" ,viewPath=UrlConfig.MSG)
public class NoticeController extends EasyuiController<Notice> {

    private static final Logger log = Logger.getLogger(NoticeController.class);
	public void list()
	{
		renderJson(Notice.me.listByDataGrid(getDataGrid(),getFrom("t_notice") ));
	}


	public void add()
	{
	    log.info(StringUtil.report(this.keepModel(this.getClass())));
	    
		Notice am = getModel();
		Date date = new Date();
		am.set("ctime", date);
		am.set("utime",date);
		String content = am.getStr("content");
		content = content.replaceAll("<br />", "<br>");
		am.set("content", content);
		boolean	flag = am.save();
		renderJsonResult(flag);
	}

	public void edit()
	{
	    log.info(StringUtil.report(this.keepModel(this.getClass())));
		Notice am = getModel();
		am.set("utime", new Date());
		String content = am.getStr("content");
        content = content.replaceAll("<br />", "<br>");
        am.set("content", content);
		boolean  result = am.update();
		renderJsonResult(result);
	}

	@Override
	public void delete()
	{
		Notice am = Notice.me.findById(getPara("id"));
		boolean flag = am.delete();
		renderJsonResult(flag);
	}

}





