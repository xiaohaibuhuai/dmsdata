package com.illumi.oms.common.index;

import com.illumi.oms.common.UrlConfig;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.Controller;
import com.jfinal.ext.route.ControllerBind;

@ControllerBind(controllerKey = "/layout",viewPath=UrlConfig.LAYOUT)
public class LayoutController extends Controller
{
}
