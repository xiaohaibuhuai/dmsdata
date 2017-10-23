package com.illumi.oms.model;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_giveticket_record")
public class GiveTicketRecord extends EasyuiModel<GiveTicketRecord>
{
	private static final long serialVersionUID = -7615377924993713398L;
	public static GiveTicketRecord dao = new GiveTicketRecord();

}
