package com.illumi.oms.model;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_stat_data")
public class Data extends EasyuiModel<Data>
{
	private static final long serialVersionUID = 3706516534681611550L;

	public static Data dao = new Data();




}
