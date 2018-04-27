package com.illumi.dms.system.controller;

import com.illumi.dms.common.Consts;
import com.illumi.dms.common.UrlConfig;
import com.illumi.dms.common.utils.StringUtil;
import com.illumi.dms.service.EmailService;
import com.illumi.dms.shiro.ShiroCache;
import com.illumi.dms.system.model.User;
import com.illumi.dms.system.validator.UserValidator;
import com.jayqqaa12.jbase.jfinal.ext.ShiroExt;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jayqqaa12.jbase.util.Validate;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;

@ControllerBind(controllerKey = "/system/user", viewPath = UrlConfig.SYSTEM)
public class UserController extends EasyuiController<User>
{
	private static final Logger log = Logger.getLogger(UserController.class);
	
	public void list()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",用户管理列表(/system/user/list),请求参数/"+getParasLog());
		renderJson(User.dao.listByDataGrid(getDataGrid(), getFrom(User.dao.tableName)));
	}
	
	
	public void select()
	{
		renderJson(User.dao.list(getDataGrid(), getFrom(User.dao.tableName)));
	}


	@Override
	public void delete()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",用户管理删除(/system/user/delete),请求参数/"+getPara("id"));
		renderJsonResult(User.dao.deleteById(getPara("id")));
	}

	public void freeze()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",用户管理冻结(/system/user/freeze),请求参数/"+getParasLog());
		renderJsonResult(User.dao.changeStaus(getParaToInt("id"), getParaToInt("status")));
	}

	public void batchDelete()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",用户管理批量删除(/system/user/batchDelete),请求参数/"+getPara("ids"));
		renderJsonResult(User.dao.batchDelete(getPara("ids")));
	}

	public void batchGrant()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",用户管理批量授权(/system/user/batchGrant),请求参数/"+getPara("ids"));
		
		Integer[] role_ids = getParaValuesToInt("role_ids");
		String ids = getPara("ids");

		renderJsonResult(User.dao.batchGrant(role_ids, ids));

		ShiroCache.clearAuthorizationInfoAll();

	}

	@Before(value = { UserValidator.class })
	public void add()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",用户管理增加(/system/user/add),请求参数/"+getParasLog());
		renderJsonResult(getModel().encrypt().saveAndDate());
	}

	@Override
	@Before(value = { UserValidator.class })
	public void edit()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",用户管理编辑(/system/user/edit),请求参数/"+getParasLog());
		renderJsonResult(getModel().update());

	}

	@Before(value = { UserValidator.class })
	public void pwd()
	{
		User user1 = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user1.getStr("account")+"/"+user1.getName()+",用户管理修改密码(/system/user/pwd),请求参数/"+getModel().getId());
		renderJsonResult(getModel().encrypt().update());
		
		
		//send eamil
		User user = User.dao.findById(getModel().getId());
		if (!Validate.isEmpty(user.getStr("email"))) ;
		new EmailService().sendModifyPwdEmail(user.getStr("email"));

	}

	public void grant()
	{
		User user1 = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user1.getStr("account")+"/"+user1.getName()+",用户管理授权(/system/user/grant),请求参数/"+getModel().getId());
		Integer[] role_ids = getParaValuesToInt("role_ids");
		renderJsonResult(User.dao.grant(role_ids, getModel().getId()));
		ShiroCache.clearAuthorizationInfoAll();

	}
	
	private String getParasLog(){
    	StringBuffer sb = new StringBuffer();
    	String paralog = "";
    	
    	if(!StringUtil.isNullOrEmpty(getPara("name"))){
    		sb.append(",登录名:"+getPara("name"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("id"))){
    		sb.append(",用户ID:"+getPara("id"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("status"))){
    		sb.append(",状态:"+getPara("status"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("dateStart"))){
    		sb.append(",创建时间起:"+getPara("dateStart"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("dateEnd"))){
    		sb.append(",创建时间止:"+getPara("dateEnd"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("role.id-i"))){
    		sb.append(",角色:"+getPara("role.id-i"));
    	}
    	
    	if(!StringUtil.isNullOrEmpty(getPara("user.name"))){
    		sb.append(",登录名称:"+getPara("user.name"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("user.uuid"))){
    		sb.append(",uuid:"+getPara("user.uuid"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("user.account"))){
    		sb.append(",账户（手机号）:"+getPara("user.account"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("user.email"))){
    		sb.append(",email:"+getPara("user.email"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("user.des"))){
    		sb.append(",描述:"+getPara("user.des"));
    	}

    	if(sb != null && sb.length()>0){
    		paralog = sb.substring(1);
    	}
    	return paralog;
    }

}
