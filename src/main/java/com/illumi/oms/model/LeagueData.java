package com.illumi.oms.model;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_league_daily_snapshot")
public class LeagueData extends EasyuiModel<LeagueData>
{
	private static final long serialVersionUID = 3706516534681611550L;
	public static LeagueData dao = new LeagueData();
}
