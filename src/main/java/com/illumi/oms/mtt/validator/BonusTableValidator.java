package com.illumi.oms.mtt.validator;


import com.jayqqaa12.jbase.jfinal.ext.Validator;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

public class BonusTableValidator extends Validator
{
    private static final Logger log = Logger.getLogger(BonusTableValidator.class);
	@Override
	protected void validate(Controller c)
	{
	    validateString("fixedPrizeTable.bonusTotal", 1, 12, "中文总奖池不能为空，并且长度不能超过12个字符");
	    validateString("fixedPrizeTable.enbonusTotal", 1, 24, "英文总奖池不能为空，并且长度不能超过24个字符");
	    validateString("fixedPrizeTable.zhtbonusTotal", 1, 12, "繁体总奖池不能为空，并且长度不能超过12个字符");
	    
	}
	
}
