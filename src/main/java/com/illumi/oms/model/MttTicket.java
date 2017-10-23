package com.illumi.oms.model;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_mtt_ticket")
public class MttTicket extends EasyuiModel<MttTicket>
{
	private static final long serialVersionUID = -7615377924993713398L;
	public static final MttTicket me = new MttTicket();
	public static MttTicket dao = new MttTicket();
	
}
