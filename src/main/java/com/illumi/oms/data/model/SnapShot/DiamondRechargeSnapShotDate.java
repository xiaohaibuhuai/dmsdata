package com.illumi.oms.data.model.SnapShot;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_diamond_recharge_daily_snapshot")
public class DiamondRechargeSnapShotDate extends EasyuiModel<DiamondRechargeSnapShotDate>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5159072701350225451L;
	public static DiamondRechargeSnapShotDate dao = new DiamondRechargeSnapShotDate();

}
