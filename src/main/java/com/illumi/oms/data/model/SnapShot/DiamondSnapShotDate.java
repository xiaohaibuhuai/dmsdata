package com.illumi.oms.data.model.SnapShot;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.tablebind.TableBind;




@TableBind(tableName = "t_diamond_daily_snapshot")
public class DiamondSnapShotDate extends EasyuiModel<DiamondSnapShotDate>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7184424926014672644L;

	public static DiamondSnapShotDate dao = new DiamondSnapShotDate();

}
