package com.illumi.oms.common.validator;

import com.jayqqaa12.jbase.jfinal.ext.Validator;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

public class BannerValidator extends Validator
{
    private static final Logger log = Logger.getLogger(BannerValidator.class);
	@Override
	protected void validate(Controller c)
	{
	    if(c.getParaToInt("banner.languagetype") == 2){
	        validateString("banner.title", 1, 50, "英文标题不能为空，并且长度不能超过50个字符");
	        validateString("banner.summary", 1, 100, "英文摘要不能为空，并且长度不能超过100个字符");
	    }else{
	        validateString("banner.title", 1, 30, "标题不能为空，并且长度不能超过30个字符");
            validateString("banner.summary", 1, 60, "摘要不能为空，并且长度不能超过60个字符");
	    }
	}
}
