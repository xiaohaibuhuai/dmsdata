package com.illumi.oms.model;

import java.util.List;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jayqqaa12.jbase.jfinal.ext.model.Model;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jayqqaa12.model.easyui.Form;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_banner_info")
public class BannerInfo extends EasyuiModel<BannerInfo>
{
	private static final long serialVersionUID = -7615377924993713398L;

	public static BannerInfo dao = new BannerInfo();

	/***
	 * 隐藏 id 1
	 */
	public DataGrid<BannerInfo> listByDataGrid(DataGrid<BannerInfo> dg, Form f)
	{
		dg = this.listByDataGridByMySort(sql("sql.banner.infolist"), dg, f);
		return dg;
	}

	
	public DataGrid<BannerInfo> listByDataGridByMySort(String sql, DataGrid<BannerInfo> dg, Form f)
	{
		int page = dg.page;
		
		List<BannerInfo> list = find(sql +f.getWhere() + f.sort(dg.sortName, dg.sortOrder) +", ostype asc,languagetype asc, id asc "+ f.limit(page, dg.total));
		
		dg.rows = list;
		
		dg.total = (int) getCount(sql + f.getWhereAndSort(dg));

		return dg;
	}


	
	
}
