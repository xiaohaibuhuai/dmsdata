package com.illumi.dms.system.validator;

import com.jayqqaa12.jbase.jfinal.ext.Validator;
import com.jfinal.core.Controller;

public class SendValidator extends Validator
{

	@Override
	protected void validate(Controller c)
	{
		validateString("sendRecord.title", 1, 50, "标题不能为空，并且长度不能超过64个字符");
		validateString("sendRecord.imgUrl",false, 0, 500, "封面url长度不能超过500个字符");
		validateString("sendRecord.url",false, 0, 500, "消息url长度不能超过500个字符");
		validateString("sendRecord.digest", 1, 128, "消息摘要不能为空，并且长度不能超过128个字符");
		validateString("sendRecord.content",false, 0, 2048, "消息内容长度不能超过2048个字符");
	}
}
