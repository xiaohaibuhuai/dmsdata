package com.illumi.oms.model;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jayqqaa12.model.easyui.Form;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_third_party")
public class ThirdParty extends EasyuiModel<ThirdParty>
{
	private static final long serialVersionUID = -7615377924993713398L;

	public static ThirdParty dao = new ThirdParty();

	/***
	 * 隐藏 id 1
	 */
	public DataGrid<ThirdParty> listByDataGrid(DataGrid<ThirdParty> dg, Form f)
	{
		dg = super.listByDataGrid(sql("sql.thirdparty.list"), dg, f);
		return dg;
	}



	
	
}
