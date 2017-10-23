package com.illumi.oms.common.validator;

import com.jayqqaa12.jbase.jfinal.ext.Validator;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

public class FlashValidator extends Validator
{
    private static final Logger log = Logger.getLogger(FlashValidator.class);
	@Override
	protected void validate(Controller c)
	{
	    validateString("flashBatchInfo.title", 1, 30, "标题不能为空，并且长度不能超过30个字符");
	}
}
