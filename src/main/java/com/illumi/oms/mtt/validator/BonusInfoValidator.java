package com.illumi.oms.mtt.validator;


import com.jayqqaa12.jbase.jfinal.ext.Validator;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

public class BonusInfoValidator extends Validator
{
    private static final Logger log = Logger.getLogger(BonusInfoValidator.class);
	@Override
	protected void validate(Controller c)
	{
	    if(c.getParaToInt("fixedBonusLevelInfo.type") == 1){
	        validateString("fixedBonusLevelInfo.inkindaward", 1, 18, "中文奖励不能为空，并且长度不能超过18个字符");
	        validateString("fixedBonusLevelInfo.eninkindaward", 1, 36, "英文奖励不能为空，并且长度不能超过36个字符");
	        validateString("fixedBonusLevelInfo.zhtinkindaward", 1, 18, "繁体奖励不能为空，并且长度不能超过18个字符");
	    }
	}
	
}
