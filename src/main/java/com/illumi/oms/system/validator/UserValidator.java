package com.illumi.oms.system.validator;

import com.illumi.oms.system.model.User;
import com.jayqqaa12.jbase.jfinal.ext.Validator;
import com.jayqqaa12.jbase.util.Validate;
import com.jfinal.core.Controller;

public class UserValidator extends Validator
{

	@Override
	protected void validate(Controller c)
	{
		super.validate(c);

		if (!isEmpty("repwd")) validateString("user.pwd", 5, 100, "密码不能为空 并且在5 到100个字符");
		else
		{
			validateString("user.name", 1, 20, "名称不能为空 并且不能超过20个字符");
			validateString("user.uuid", 4,11, "uuid不能为空 并且最短4位，最长11位");
			validateString("user.account", 11, 11, "账户不能为空 并且为11位手机号");
			validateString("user.des", false, 0, 100, "描述不能超过100个字符");
			validateString("user.email", false, 0, 100, "email不能超过100个字符");
			validateEmail("user.email", false);
			if (Validate.isEmpty(c.getPara("user.id")) && User.dao.checkNameExist(c.getPara("user.name"))) {
				addError("用户名已存在");
			}else if (User.dao.checkUuidExist(c.getPara("user.uuid"),c.getPara("user.id"))) {
				addError("uuid已经被绑定");
			}else if (User.dao.checkAccountExist(c.getPara("user.account"),c.getPara("user.id"))){
				addError("账户（手机号）已经被绑定");
			}
		}
	}

}
