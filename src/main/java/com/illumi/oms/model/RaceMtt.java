package com.illumi.oms.model;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jayqqaa12.model.easyui.Form;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_race_mtt")
public class RaceMtt extends EasyuiModel<RaceMtt>
{
	private static final long serialVersionUID = -7615377924993713398L;

	public static RaceMtt dao = new RaceMtt();
	
}
