package com.illumi.oms.data.model.SnapShot;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_recharge_daily_snapshot")
public class RechargeSnapShotDate extends EasyuiModel<RechargeSnapShotDate>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2425728962891371642L;
	public static RechargeSnapShotDate dao = new RechargeSnapShotDate();
}
