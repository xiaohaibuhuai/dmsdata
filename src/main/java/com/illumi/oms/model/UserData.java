package com.illumi.oms.model;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_ip_pool")
public class UserData extends EasyuiModel<UserData>
{
	private static final long serialVersionUID = 3706516534681611550L;

	public static UserData dao = new UserData();

}
