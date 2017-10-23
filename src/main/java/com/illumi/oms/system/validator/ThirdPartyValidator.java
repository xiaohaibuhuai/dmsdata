package com.illumi.oms.system.validator;

import com.illumi.oms.common.utils.StringUtil;
import com.jayqqaa12.jbase.jfinal.ext.Validator;
import com.jfinal.core.Controller;

public class ThirdPartyValidator extends Validator
{

	@Override
	protected void validate(Controller c)
	{
		validateIp(c,"thirdParty.ip",true, "ip格式不正确 多个ip用逗号隔开");
		validateString("thirdParty.ip",true, 1, 100, "IP不能为空 并且不能超过100个字符");
		validateInteger("thirdParty.amount",1,Integer.MAX_VALUE,"信用额度，必须在1～2000000000间的正整数");
		validateString("thirdParty.email", 1, 50, "email不能超过50个字符");
		validateEmail("thirdParty.email", true);
		validateString("thirdParty.name",true, 1, 50, "名称不能为空 并且不能超过50个字符");

	}
	
	private void validateIp(Controller c,String filed, boolean notBlank,String errorMessage){
		String value = c.getPara(filed);
	
		if(notBlank && ("".equals(value.trim())||null==value)){
			addError(ERROR_MSG, errorMessage);
		}
			
		if(value.startsWith(",")||value.endsWith(",")){
			addError(ERROR_MSG, errorMessage);
		}
		for(String ip:value.split(",")){
			if(!StringUtil.isIP(ip)){
				addError(ERROR_MSG, errorMessage);
				break;
			}
		}
	}

}
