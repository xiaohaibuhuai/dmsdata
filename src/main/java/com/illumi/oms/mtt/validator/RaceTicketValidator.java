package com.illumi.oms.mtt.validator;

import com.illumi.oms.common.utils.StringUtil;
import com.jayqqaa12.jbase.jfinal.ext.Validator;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

public class RaceTicketValidator extends Validator
{
    private static final Logger log = Logger.getLogger(RaceTicketValidator.class);
	@Override
	protected void validate(Controller c)
	{
	    validateString("ticketInfo.zhname", 1, 12, "中文名称不能为空，并且长度不能超过12个字符");
	    validateString("ticketInfo.enname", 1, 24, "英文名称不能为空，并且长度不能超过24个字符");
	    validateString("ticketInfo.zhtname", 1, 12, "繁体名称不能为空，并且长度不能超过12个字符");
	    
	    if(!StringUtil.isNullOrEmpty(c.getPara("ticketInfo.zhtext"))){
	        validateString("ticketInfo.zhtext", 1, 12, "中文文字长度不能超过12个字符");
	    }
	    if(!StringUtil.isNullOrEmpty(c.getPara("ticketInfo.entext"))){
	        validateString("ticketInfo.entext", 1, 24, "英文文字长度不能超过24个字符");
	    }
        if(!StringUtil.isNullOrEmpty(c.getPara("ticketInfo.zhttext"))){
            validateString("ticketInfo.zhttext", 1, 12, "繁体文字长度不能超过12个字符");
        }
	    
	}
	
}
