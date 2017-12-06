package com.illumi.oms.data.model.SnapShot;

import com.illumi.oms.model.LeagueData;
import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_game_daily_snapshot")
public class GameSnapShotDate extends EasyuiModel<GameSnapShotDate>{

	private static final long serialVersionUID = 4152895106287552287L;
	
	public static GameSnapShotDate dao = new GameSnapShotDate();

}
