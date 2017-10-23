package com.illumi.oms.model;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_move_clubuser_info")
public class MoveClubUserInfo extends EasyuiModel<MoveClubUserInfo>
{
	private static final long serialVersionUID = 3706516534681611550L;
	public static MoveClubUserInfo dao = new MoveClubUserInfo();
}
