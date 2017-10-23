package com.illumi.oms.model;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jayqqaa12.model.easyui.Form;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_third_referrer")
public class ThirdRefer extends EasyuiModel<ThirdRefer>
{
	private static final long serialVersionUID = -7615377924993713398L;

	public static ThirdRefer dao = new ThirdRefer();


	public DataGrid<ThirdRefer> getList(DataGrid<ThirdRefer> dg, Form f,Object... params){
		
		dg = dao.listByDataGridXml("sql.thirdparty.getAddRecordList", dg, f , " and thirdId=? and ctime>=? and ctime<?", params);


		return dg;
		
	}

	
	
}
