package com.illumi.oms.model;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_user_black")
public class UserBlack extends EasyuiModel<UserBlack>
{
	private static final long serialVersionUID = -7615377924993713398L;

	public static UserBlack dao = new UserBlack();
	
}
