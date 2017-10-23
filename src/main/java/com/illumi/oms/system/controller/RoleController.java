package com.illumi.oms.system.controller;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.common.utils.StringUtil;
import com.illumi.oms.shiro.ShiroCache;
import com.illumi.oms.system.model.Role;
import com.illumi.oms.system.model.User;
import com.illumi.oms.system.validator.RoleValidator;
import com.jayqqaa12.jbase.jfinal.ext.ShiroExt;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.Controller;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;

@ControllerBind(controllerKey = "/system/role", viewPath = UrlConfig.SYSTEM)
public class RoleController extends Controller<Role>
{
	private static final Logger log = Logger.getLogger(RoleController.class);
	public void list()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",角色管理列表(/system/role/list)");
		renderJson(Role.dao.list());
	}
	
	

	public void tree()
	{

		Integer pid = getParaToInt("id");
		Integer passId = getParaToInt("passId");
		renderJson(Role.dao.getTree(pid, passId));

	}

	public void grant()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",角色管理角色权限管理(/system/role/grant),请求参数/"+ getParasLog());
		
		Role role = getModel();
		String res_ids = getPara("res_ids");
		
		renderJsonResult(Role.dao.batchGrant(role.getId(), res_ids));
		ShiroCache.clearAuthorizationInfoAll();

	}

	@Override
	@Before(value = { RoleValidator.class })
	public void add()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",角色管理添加角色(/system/role/add),请求参数/"+getParasLog());
		renderJsonResult(getModel().save());
	}

	@Override
	@Before(value = { RoleValidator.class })
	public void edit()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",角色管理编辑角色(/system/role/edit),请求参数/"+getParasLog());
		Role role = getModel();

		if (role.getId() == role.getPid()) renderJsonError("父节点不能为自己");
		else if (Role.dao.pidIsChild(role.getId(), role.getPid())) renderJsonError("父节点不能为子节点");
		else renderJsonResult(role.update());

	}

	public void delete()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",角色管理删除角色(/system/role/delete),请求参数/"+getParaToInt("id"));
		int id = getParaToInt("id");

		for (Role r : Role.dao.getRole(((User) ShiroExt.getSessionAttr(Consts.SESSION_USER)).getId()))
		{
			if (r.getId() == id)
			{
				renderJsonError("无法删除 自己的角色");
				return;
			}
		}

		if (id == 1) renderJsonError("admin 无法删除");
		else renderJsonResult(Role.dao.deleteByIdAndPid(id));
	}
	
	private String getParasLog(){
    	StringBuffer sb = new StringBuffer();
    	String paralog = "";
    	
    	if(!StringUtil.isNullOrEmpty(getPara("role.name"))){
    		sb.append(",角色名称:"+getPara("role.name"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("role.seq"))){
    		sb.append(",排序:"+getPara("role.seq"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("role.pid"))){
    		sb.append(",上级角色:"+getPara("role.pid"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("role.des"))){
    		sb.append(",备注:"+getPara("role.des"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("res_ids"))){
    		sb.append(",权限列表:"+getPara("res_ids"));
    	}
    	if(sb != null && sb.length()>0){
    		paralog = sb.substring(1);
    	}
    	return paralog;
    }

}
