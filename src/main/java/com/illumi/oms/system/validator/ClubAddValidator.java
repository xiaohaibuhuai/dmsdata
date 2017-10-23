package com.illumi.oms.system.validator;

import com.illumi.oms.common.Consts;
import com.jayqqaa12.jbase.jfinal.ext.Validator;
import com.jfinal.core.Controller;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class ClubAddValidator extends Validator
{

	@Override
	protected void validate(Controller c)
	{
		boolean flag = true;
		Record rd = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.club.getClubMaxLength"));
		try{
			if(c.getPara("clubid").length() > rd.getLong("maxLength") || Integer.parseInt(c.getPara("clubid"))<10000 ){
				flag = false;
			}
		}catch(Exception e){
			flag = false;
		}
		if(!flag){
			addError("俱乐部ID最低5位最高"+rd.getLong("maxLength")+"位，请重新输入");
		}
		if(flag && c.getPara("clubname").length()>64){
			addError("俱乐部名字不能超过64个字符");
		}
	}
}
