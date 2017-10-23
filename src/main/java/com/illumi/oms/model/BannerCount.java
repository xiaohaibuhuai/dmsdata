package com.illumi.oms.model;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jayqqaa12.model.easyui.Form;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_banner_count")
public class BannerCount extends EasyuiModel<BannerCount>
{
	private static final long serialVersionUID = -7615377924993713398L;

	public static BannerCount dao = new BannerCount();

	/***
	 * 隐藏 id 1
	 */
	public DataGrid<BannerCount> listByDataGrid(DataGrid<BannerCount> dg, Form f)
	{
		dg = super.listByDataGrid(sql("sql.banner.countlist"), dg, f);
		return dg;
	}



	
	
}
