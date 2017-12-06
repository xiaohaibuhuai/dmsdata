package com.illumi.oms.data.model.SnapShot;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_game_ext_daily_snapshot")
public class GameExtSnapShotDate extends EasyuiModel<GameExtSnapShotDate>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9195038598541443354L;
	
	public static GameExtSnapShotDate dao  = new GameExtSnapShotDate();
	
}
