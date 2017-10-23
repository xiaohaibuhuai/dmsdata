package com.illumi.oms.controller;

import java.net.URLEncoder;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.common.utils.StringUtil;
import com.illumi.oms.model.CoinRecord;
import com.illumi.oms.model.ThirdParty;
import com.illumi.oms.system.model.User;
import com.illumi.oms.system.validator.ThirdPartyValidator;
import com.jayqqaa12.jbase.jfinal.ext.ShiroExt;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jayqqaa12.model.easyui.Form;
import com.jfinal.aop.Before;
import com.jfinal.ext.plugin.config.ConfigKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;

@ControllerBind(controllerKey = "/partner/thirdparty", viewPath = UrlConfig.PARTNER)
public class ThirdPartyController extends EasyuiController<ThirdParty>
{
	private static final Logger log = Logger.getLogger(ThirdPartyController.class);

	public void list()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",第三方列表查询(/partner/thirdparty/list),请求参数/"+getParasLog());
		renderJson(ThirdParty.dao.listByDataGrid(getDataGrid(),getForm() ));
	}
	private Form getForm(){
		Form f = getFrom("");
		f.setFromParm("name", getPara("name"));
		f.setFromParm("email", getPara("email"));

		f.fuzzySerach.add("name");
		f.fuzzySerach.add("email");

		return f;
	}

	@Before(value = { ThirdPartyValidator.class })
	public void add()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",第三方列表添加(/partner/thirdparty/add),请求参数/"+getParasLog());
		ThirdParty tp = getModel();
		boolean flag = false;
		try{
			tp.set("remain", tp.get("amount"));
			tp.set("secretId", StringUtil.getUniqueString(36, false));
			tp.set("secretKey", StringUtil.getUniqueString(36, false));
			tp.set("mails", 0);
			tp.set("ctime", new Date());
			tp.set("utime", new Date());
			flag = tp.save();
			if(flag){
//				User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
				//operator action name amount remain id
				log.info(user.getName()+" Create "+tp.getName()+" "+tp.getBigDecimal("amount")+" "+tp.getBigDecimal("remain")+" "+tp.getInt("id"));
			}
		}catch(Exception e){
			renderJsonResult(flag);
		}
		renderJsonResult(flag);
	}

	@Before(value = { ThirdPartyValidator.class })
	public void edit()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",第三方列表编辑(/partner/thirdparty/edit),请求参数/"+getParasLog());
		ThirdParty tp = getModel();
		tp.set("utime", new Date());
        boolean flag = tp.update();
		if(flag){
//			User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
			log.info(user.getName()+" Update "+tp.getName()+" "+tp.getBigDecimal("amount")+" "+tp.getInt("id"));
		}
		
		renderJsonResult(flag);

	}
	public void reset()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",第三方列表重置(/partner/thirdparty/reset),请求参数/"+getPara("id"));
		ThirdParty tp = ThirdParty.dao.findById(getPara("id"));
		log.info(user.getName()+" Current "+tp.getName()+" "+tp.getBigDecimal("amount")+" "+tp.getBigDecimal("remain")+" "+tp.getInt("id"));
		tp.set("remain", tp.getBigDecimal("amount"));
		tp.set("mails", 0);
		tp.set("utime", new Date());
        boolean flag = tp.update();
		if(flag){
			
			log.info(user.getName()+" Reset "+tp.getName()+" "+tp.getBigDecimal("amount")+" "+tp.getBigDecimal("remain")+" "+tp.getInt("id"));
		}
		
		renderJsonResult(flag);

	}
	@Override
	public void delete()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",第三方列表删除(/partner/thirdparty/delete),请求参数/"+getPara("id"));
		ThirdParty tp = ThirdParty.dao.findById(getPara("id"));
		boolean flag = tp.delete();
		if(flag){
//			User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
			log.info(user.getName()+" Delete "+tp.getName()+" "+tp.getBigDecimal("amount")+" "+tp.getBigDecimal("remain")+" "+tp.getInt("id"));
		}
		renderJsonResult(flag);
	}
	
	public void createurl() {
        String url = "";
        log.info(StringUtil.report(this.keepModel(this.getClass())));
        long time = System.currentTimeMillis();
        int flag = 0;
        try {
            String localUrl = "time=" + time;
            String localSign = URLEncoder.encode(Base64
                    .encodeBase64String(HmacUtils.hmacSha1(ConfigKit.getStr("pay.secretId"), localUrl)));
            url = "?time=" + time + "&sig=" + localSign;
        } catch (Exception e) {
            log.error("生成二维码异常：ERROR" + e.getMessage());
            flag = 1;
        }
        renderJson("{\"flag\":\"" + flag + "\",\"url\":\"" + url + "\"}");
    }
	
	private String getParasLog(){
    	StringBuffer sb = new StringBuffer();
    	String paralog = "";
    	
    	if(!StringUtil.isNullOrEmpty(getPara("name"))){
    		sb.append(",第三方名称:"+getPara("name"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("email"))){
    		sb.append(",第三方邮箱:"+getPara("email"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("thirdParty.name"))){
    		sb.append(",第三方名称:"+getPara("thirdParty.name"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("thirdParty.email"))){
    		sb.append(",email:"+getPara("thirdParty.email"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("thirdParty.amount"))){
    		sb.append(",信用额度:"+getPara("thirdParty.amount"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("thirdParty.ip"))){
    		sb.append(",调用IP:"+getPara("thirdParty.ip"));
    	}
    	if(sb != null && sb.length()>0){
    		paralog = sb.substring(1);
    	}
    	return paralog;
    }


}
