package com.illumi.dms.system.controller;

import com.illumi.dms.common.Consts;
import com.illumi.dms.common.UrlConfig;
import com.illumi.dms.common.utils.StringUtil;
import com.illumi.dms.shiro.ShiroCache;
import com.illumi.dms.shiro.ShiroInterceptor;
import com.illumi.dms.system.model.Res;
import com.illumi.dms.system.model.Role;
import com.illumi.dms.system.model.User;
import com.illumi.dms.system.validator.ResValidator;
import com.jayqqaa12.jbase.jfinal.ext.ShiroExt;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.Controller;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;

@ControllerBind(controllerKey = "/system/res", viewPath = UrlConfig.SYSTEM)
public class ResController extends Controller<Res>
{
	private static final Logger log = Logger.getLogger(ResController.class);
	public void tree()
	{
	    log.info(StringUtil.report(this.keepModel(this.getClass())));
		Integer pid = getParaToInt("id");
		Integer passId = getParaToInt("passId");
		int type = getParaToInt("type", Res.TYPE_MEUE);
		renderJson(Res.dao.getTree(pid, type, passId));
	}
	
	public void datatree()
    {
        log.info(StringUtil.report(this.keepModel(this.getClass())));
        Integer pid = getParaToInt("id");
        Integer passId = getParaToInt("passId");
        int type = getParaToInt("type", Res.TYPE_MEUE);
        renderJson(Res.dao.getDataTree(pid, type, passId));
    }

	public void list()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",资源管理资源列表查看(/system/res/list)");
		renderJson(Res.dao.listOrderBySeq());
	}

	public void delete()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",资源管理删除资源(/system/res/delete),请求参数/"+getParaToInt("id"));
		renderJsonResult(Res.dao.deleteByIdAndPid(getParaToInt("id")));
		removeAuthorization();
	}

	@Before(value = { ResValidator.class })
	public void add()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",资源管理添加资源(/system/res/edit),请求参数/"+ getParasLog());
		Res res=getModel();
		boolean result=res.save();
		renderJsonResult(result);
		if(result) Role.dao.grant(1, res.getId()+"");
		removeAuthorization();
	}

	@Before(value = { ResValidator.class })
	public void edit()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",资源管理编辑资源(/system/res/edit),请求参数/"+ getParasLog());
		Res res = getModel();

		if (res.getId() == res.getPid()) renderJsonError("父节点不能为自己");
		else if (res.getType() == Res.TYPE_PERMISSION && Res.dao.getChild(res.getId(), null).size() > 0) renderJsonError("功能属性不能有子节点");
		else if (Res.dao.pidIsChild(res.getId(), res.getPid())) renderJsonError("父节点不能为子节点");
		else
		{
			renderJsonResult(res.update());
			removeAuthorization();
		}

	}

	private void removeAuthorization()
	{
		ShiroCache.clearAuthorizationInfoAll();
		ShiroInterceptor.updateUrls();
	}
	
	private String getParasLog(){
    	StringBuffer sb = new StringBuffer();
    	String paralog = "";
    	
    	if(!StringUtil.isNullOrEmpty(getPara("res.name"))){
    		sb.append(",资源名称:"+getPara("res.name"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("res.url"))){
    		sb.append(",资源路径:"+getPara("res.url"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("res.type"))){
    		sb.append(",资源类型:"+getPara("res.type"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("res.seq"))){
    		sb.append(",排序:"+getPara("res.seq"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("res.pid"))){
    		sb.append(",上级资源:"+getPara("res.pid"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("res.iconCls"))){
    		sb.append(",菜单图标:"+getPara("res.iconCls"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("res.des"))){
    		sb.append(",备注:"+getPara("res.des"));
    	}
    	if(sb != null && sb.length()>0){
    		paralog = sb.substring(1);
    	}
    	return paralog;
    }

}
