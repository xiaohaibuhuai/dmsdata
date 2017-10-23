package com.illumi.oms.mtt.validator;


import com.jayqqaa12.jbase.jfinal.ext.Validator;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

public class MttCreateManageValidator extends Validator
{
    private static final Logger log = Logger.getLogger(MttCreateManageValidator.class);
	@Override
	protected void validate(Controller c)
	{
	    validateString("mttRaceName", 1, 18, "中文名称不能为空，并且长度不能超过18个字符");
	    validateString("mttENRaceName", 1, 36, "英文名称不能为空，并且长度不能超过36个字符");
	    validateString("mttZHTRaceName", 1, 18, "繁体名称不能为空，并且长度不能超过18个字符");
	}
	
}
