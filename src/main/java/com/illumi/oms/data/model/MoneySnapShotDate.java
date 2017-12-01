package com.illumi.oms.data.model;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_money_daily_snapshot")
public class MoneySnapShotDate extends EasyuiModel<MoneySnapShotDate>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7184424926014672644L;
	
	
	public static MoneySnapShotDate dao = new MoneySnapShotDate();

}
