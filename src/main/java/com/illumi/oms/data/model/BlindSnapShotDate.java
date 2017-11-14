package com.illumi.oms.data.model;

import com.illumi.oms.model.LeagueData;
import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_blinds_daily_snapshot")
public class BlindSnapShotDate extends EasyuiModel<BlindSnapShotDate>{
	private static final long serialVersionUID = -2232687149997584373L;
	public static BlindSnapShotDate dao = new BlindSnapShotDate();

}
