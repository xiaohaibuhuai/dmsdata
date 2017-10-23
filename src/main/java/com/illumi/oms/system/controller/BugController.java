package com.illumi.oms.system.controller;

import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.system.model.Bug;
import com.illumi.oms.system.validator.BugValidator;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;

@ControllerBind(controllerKey = "/system/bug", viewPath = UrlConfig.SYSTEM)
public class BugController extends EasyuiController<Bug>
{

	public void list()
	{
		renderJson(Bug.dao.list(getDataGrid(), getFrom(null)));
	}

	public void status()
	{

		renderJsonResult(getModel().updateAndModifyDate());

	}

	@Before(value = { BugValidator.class })
	public void add()
	{
		renderJsonResult(getModel().saveAndCreateDate());

	}

	@Before(value = { BugValidator.class })
	public void edit()
	{
		renderJsonResult(getModel().updateAndModifyDate());

	}

	public void delete()
	{
		renderJsonResult(Bug.dao.deleteById(getPara("id")));
	}

}
