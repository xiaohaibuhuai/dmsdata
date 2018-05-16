package com.illumi.dms.controller.user;import com.illumi.dms.common.Consts;import com.illumi.dms.common.utils.SqlUtils;import com.illumi.dms.model.user.AppPerson;import com.illumi.dms.model.user.AppPerson;import com.illumi.dms.model.user.AppVersion;import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;import com.jayqqaa12.model.easyui.DataGrid;import com.jayqqaa12.model.easyui.Form;import com.jfinal.ext.render.csv.CsvRender;import com.jfinal.ext.route.ControllerBind;import org.apache.log4j.Logger;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import java.util.Map;@ControllerBind(controllerKey = "/user/version",viewPath="/page")public class AppVersionController extends EasyuiController {    private static final Logger logger = Logger.getLogger(AppVersionController.class);    public void getVersion(){        List<AppVersion> appVersions = AppVersion.dao.find(String.format("select distinct app_version from %s order by app_version desc", AppVersion.dao.tableName));        renderJson(appVersions);    }    public void  newVersion(){        Integer page = getParaToInt(Consts.PAGE);        String order = getPara(Consts.ORDER);        String sortField = getPara(Consts.SORT_FIELD);        Map<String,Object> map = new HashMap<>();        map.put(Consts.START_TIME,getPara(Consts.START_TIME));        map.put(Consts.END_TIME,getPara(Consts.END_TIME));        map.put(Consts.IS_ABROAD,getPara(Consts.IS_ABROAD));        if (getPara(Consts.APP_VERSION)!=null){            map.put(Consts.APP_VERSION,getPara(Consts.APP_VERSION));        }        String where =SqlUtils.concatSql(map);        DataGrid<AppVersion> dg = new DataGrid();        dg.sortName = this.getPara("sort", sortField);        dg.sortOrder = this.getPara("order", order);        if (page==null){            dg.page = this.getParaToInt("page", 1);        }else {            dg.page = this.getParaToInt("page", page);        }        dg.total = this.getParaToInt("rows", getParaToInt(Consts.TOTAL));        Form form = new Form();        String limit = form.limit(dg.page, dg.total);        if(getPara(Consts.IS_ABROAD)==null){            where = SqlUtils.getGroupSql(where,"date,app_version");            where = SqlUtils.getOrderSql(where, sortField, order);            String what ="select date,app_version,sum(is_new) is_new,sum(isupdate) isupdate,sum(sum_two) sum_two,sum(token_num) token_num from ";            String sql = String.format("%s %s where 1=1  %s  %s",what,AppVersion.dao.tableName,where,limit);            renderJson(AppVersion.dao.myFind(sql,limit));        }else {            where = SqlUtils.getOrderSql(where, sortField, order);            renderJson(AppVersion.dao.listByDataGridBySortSql(dg,getFrom(AppVersion.dao.tableName),where));        }    }    public void  totalVersion(){        Integer page = getParaToInt(Consts.PAGE);        String order = getPara(Consts.ORDER);        String sortField = getPara(Consts.SORT_FIELD);        Map<String,Object> map = new HashMap<>();        map.put(Consts.START_TIME,getPara(Consts.START_TIME));        map.put(Consts.END_TIME,getPara(Consts.END_TIME));        map.put(Consts.IS_ABROAD,getPara(Consts.IS_ABROAD));        if (getPara(Consts.APP_VERSION)!=null){            map.put(Consts.APP_VERSION,getPara(Consts.APP_VERSION));        }        String where =SqlUtils.concatSql(map);        DataGrid<AppPerson> dg = new DataGrid();        dg.sortName = this.getPara("sort", sortField);        dg.sortOrder = this.getPara("order", order);        if (page==null){            dg.page = this.getParaToInt("page", 1);        }else {            dg.page = this.getParaToInt("page", page);        }        dg.total = this.getParaToInt("rows", getParaToInt(Consts.TOTAL));        Form form = new Form();        String limit = form.limit(dg.page, dg.total);        if(getPara(Consts.IS_ABROAD)==null){            where = SqlUtils.getGroupSql(where,"app_version");            where = SqlUtils.getOrderSql(where, sortField, order);            String what ="select app_version,sum(token_num) token_num,sum(user_num) user_num from ";            String sql = String.format("%s %s where 1=1  %s  %s",what,AppPerson.dao.tableName,where,limit);            renderJson(AppPerson.dao.myFind(sql,limit));        }else {            where = SqlUtils.getOrderSql(where, sortField, order);            renderJson(AppPerson.dao.listByDataGridBySortSql(dg,getFrom(AppPerson.dao.tableName),where));        }    }    public void versionDownload(){        Map<String,Object> map = new HashMap<>();        map.put(Consts.START_TIME,getPara(Consts.START_TIME));        map.put(Consts.END_TIME,getPara(Consts.END_TIME));        map.put(Consts.IS_ABROAD,getPara(Consts.IS_ABROAD));        if (getPara(Consts.APP_VERSION)!=null){            map.put(Consts.APP_VERSION,getPara(Consts.APP_VERSION));        }        String where =SqlUtils.concatSql(map);        List<String> header = new ArrayList();        header.add("日期");        header.add("App版本号");        header.add("新增人数");        header.add("升级用户");        header.add("新增+升级用户数");        header.add("活跃设备数");        String what ="select DATE_FORMAT(date,'%Y-%m-%d') date,app_version,sum(is_new) is_new,sum(isupdate) isupdate,sum(sum_two) sum_two,sum(token_num) token_num from ";        where = " where 1=1" + where;        where = SqlUtils.getGroupSql(where,"date,app_version");        where = SqlUtils.getOrderSql(where,"date,app_version","desc");        List<AppVersion> list = AppVersion.dao.list(what, where);        logger.info(String.format("sql---> %s %s %s",what,AppVersion.dao.tableName,where));        render(CsvRender.me(header, list).encodeType("gbk").fileName("dms"+System.currentTimeMillis()+".csv"));    }    public void personDownload(){        Map<String,Object> map = new HashMap<>();        map.put(Consts.START_TIME,getPara(Consts.START_TIME));        map.put(Consts.END_TIME,getPara(Consts.END_TIME));        map.put(Consts.IS_ABROAD,getPara(Consts.IS_ABROAD));        String where =SqlUtils.concatSql(map);        List<String> header = new ArrayList();        header.add("App版本号");        header.add("累计设备数");        header.add("累计独立用户数");        String what ="select app_version,sum(token_num) token_num,sum(user_num) user_num from ";        where = " where 1=1" + where;        where = SqlUtils.getGroupSql(where,"date");        where = SqlUtils.getOrderSql(where,"date","desc");        List<AppPerson> list = AppPerson.dao.list(what, where);        logger.info(String.format("sql---> %s %s %s",what,AppPerson.dao.tableName,where));        render(CsvRender.me(header, list).encodeType("gbk").fileName("dms"+System.currentTimeMillis()+".csv"));    }    }