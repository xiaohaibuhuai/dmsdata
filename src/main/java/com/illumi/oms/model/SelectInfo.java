package com.illumi.oms.model;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_select_info")
public class SelectInfo extends EasyuiModel<SelectInfo>
{
	private static final long serialVersionUID = -7615377924993713398L;
	public static SelectInfo dao = new SelectInfo();
	
}
