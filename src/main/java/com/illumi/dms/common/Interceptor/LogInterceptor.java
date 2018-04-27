package com.illumi.dms.common.Interceptor;

import com.illumi.dms.common.utils.StringUtil;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

/**
 * Created by mbf on 2018/1/19.
 */
public class LogInterceptor implements Interceptor {

    private static final Logger log = Logger.getLogger(LogInterceptor.class);
    @Override
    public void intercept(ActionInvocation ai) {
        String ignore = "com.illumi.oms.common.index.IndexController";
        Controller controller = ai.getController();
        String cString = controller.toString();
        cString=cString.substring(0, cString.indexOf("@"));
        if(cString.equals(ignore)) {
            ai.invoke();
            return;
        }


        log.info(StringUtil.report(ai.getController()));
        ai.invoke();
    }

}
