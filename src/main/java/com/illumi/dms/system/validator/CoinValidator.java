package com.illumi.dms.system.validator;

import com.jayqqaa12.jbase.jfinal.ext.Validator;
import com.jfinal.core.Controller;

public class CoinValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		super.validate(c);

		validateString("coinRecord.uuClubid", 4, 10, "uuid或clubid不能为空，并且最短4位，最长10位");
		validateString("coinRecord.mcount", 1, 10, "充值金额不能为空 最长10位");
	}

}
