package com.illumi.oms.model;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_pressuretest_record")
public class PressureTestRecord extends EasyuiModel<PressureTestRecord>
{
	private static final long serialVersionUID = -7615377924993713398L;

	public static PressureTestRecord dao = new PressureTestRecord();

	
	
}
