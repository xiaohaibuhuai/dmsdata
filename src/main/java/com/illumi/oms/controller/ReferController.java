package com.illumi.oms.controller;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.model.ThirdRefer;
import com.illumi.oms.system.model.User;
import com.jayqqaa12.jbase.jfinal.ext.ShiroExt;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jayqqaa12.model.easyui.Form;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;

@ControllerBind(controllerKey = "/partner/refer", viewPath = UrlConfig.PARTNER)
public class ReferController extends EasyuiController<ThirdRefer>
{
	private static final Logger log = Logger.getLogger(ReferController.class);
	public void list()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",第三方推广员列表查询(/partner/refer/list),请求参数/"+getPara("id"));
		renderJson(ThirdRefer.dao.listByDataGrid(getDataGrid(),getForm()));
	}
	private Form getForm(){
		Form f = getFrom("");
		f.setFromParm("rfsrc", getPara("id"));
		f.integerValue.add("rfsrc");

		return f;
	}
	
	
	public void refer(){
		setAttr("id", getParaToInt("id"));
	}




}
