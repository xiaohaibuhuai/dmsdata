package com.illumi.oms.model;

import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "t_flashpage_batchinfo")
public class FlashBatchInfo extends EasyuiModel<FlashBatchInfo>
{
	private static final long serialVersionUID = -7615377924993713398L;
	public static FlashBatchInfo dao = new FlashBatchInfo();
}
