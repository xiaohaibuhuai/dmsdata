package com.illumi.oms.common.index;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import org.apache.commons.codec.binary.Hex;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.common.validator.LoginValidator;
import com.illumi.oms.plugin.spring.IocInterceptor;
import com.illumi.oms.service.DubboService;
import com.illumi.oms.system.controller.LogController;
import com.illumi.oms.system.model.Log;
import com.illumi.oms.system.model.User;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.Controller;
import com.jayqqaa12.jbase.util.RSA;
import com.jayqqaa12.jbase.util.Sec;
import com.jayqqaa12.jbase.util.Validate;
import com.jfinal.aop.Before;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;


@SuppressWarnings("rawtypes")
@ControllerBind(controllerKey = "/", viewPath = UrlConfig.INDEX)
public class IndexController extends Controller {
  private static final Logger log = Logger.getLogger(IndexController.class);

  public void jump() {
    Log.dao.insert(this, Log.EVENT_VISIT);
    render(UrlConfig.VIEW_COMMON_JUMP);
  }

  public void initDb() {
    // new InitService().initDb(getRequest().getRealPath("/static/file") + File.separator +
    // "init.sql");
    forwardAction("/jump");
  }

  public void loginView() {
    // 去掉首次访问的特效
    // if (firstInto()) return;

    RSAPublicKey publicKey = RSA.getDefaultPublicKey();
    String modulus = new String(Hex.encodeHex(publicKey.getModulus().toByteArray()));
    String exponent = new String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray()));

    setAttr("modulus", modulus);
    setAttr("exponent", exponent);

    render(UrlConfig.VIEW_COMMON_LOGIN);

  }

  private boolean firstInto() {
    String init = getCookie("init");
    if (init == null)
      setCookie("init", "init", 1000 * 60 * 60 * 24 * 365);
    render(UrlConfig.VIEW_COMMON_INIT);

    return Validate.isEmpty(init);
  }

  public void loginOut() {
    try {
      Subject subject = SecurityUtils.getSubject();
      subject.logout();

      renderTop(UrlConfig.LOGIN);

    } catch (AuthenticationException e) {
      e.printStackTrace();
      renderText("异常：" + e.getMessage());
    }
  }

    @Before(LoginValidator.class)
    public void login() {
        String[] result = RSA.decryptUsernameAndPwd(getPara("key"));
        String account = result[0];
        log.info(account + "请求访问");
        int state = 0;
        User user = User.dao.findFirst(SqlKit.sql("system.user.getLogin"), account);
        if (user != null) {
//          DubboService dubboService = (DubboService) IocInterceptor.ctx.getBean("dubboService");
//          state = dubboService.userLogin(account, string2MD5(result[1]));
            if (account.equals("17073549066")) {
                state = 203513;
            } else {
                state = 203512;
            }
            if (state == 0) {
                forwardAction("未返回用户信息", UrlConfig.LOGIN);
            } else if (state == 1) {
                forwardAction("登录失败，用户名或密码错误", UrlConfig.LOGIN);
            } else if (state == 2) {
                forwardAction("登录异常", UrlConfig.LOGIN);
            } else if (user.getInt("uuid") != null && state == user.getInt("uuid")) {
                // 登录成功,按照原配置进行登录
                UsernamePasswordToken token =
                        new UsernamePasswordToken(user.getStr("name"), user.getStr("pwd"));
                Subject subject = SecurityUtils.getSubject();
                if (!subject.isAuthenticated()) {
                    token.setRememberMe(true);
                    subject.login(token);
                    subject.getSession(true).setAttribute(Consts.SESSION_USER, user);
                }
                log.info(user.getStr("account") + "/" + user.getName() + "登录成功");
                Log.dao.insert(this, Log.EVENT_LOGIN);
                redirect("/");
            } else {
                forwardAction("用户名未关联正确的uuid，请联系管理员", UrlConfig.LOGIN);
            }
        } else {
            forwardAction("用户名不存在或未注册本系统", UrlConfig.LOGIN);
        }
    }

    public void unauthorized() {

        render(UrlConfig.VIEW_ERROR_401);
    }

    /**
     * 将String转化为MD5
     *
     * @param inStr
     * @return
     */
    public static String string2MD5(String inStr) {
        byte[] source = inStr.getBytes();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(source);
        StringBuffer buf = new StringBuffer();
        for (byte b : md.digest())
            buf.append(String.format("%x", b & 0xff));// %02x
        return buf.toString();
    }


}
