package com.illumi.dms.model.user;import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;import com.jayqqaa12.model.easyui.DataGrid;import com.jfinal.ext.plugin.tablebind.TableBind;import org.apache.commons.lang.StringUtils;import java.util.List;@TableBind(tableName = "dms_retain_user_view")public class RetainUser extends EasyuiModel<RetainUser> {    private static final long serialVersionUID = -7615377924993713398L;    public static RetainUser dao = new RetainUser();    public DataGrid<RetainUser> myFind(String sql,String limit){        List<RetainUser> retainUsers = RetainUser.dao.find(sql);        if (sql.contains("limit")){            sql = sql.split("limit")[0];        }        if (sql.contains("group by")){            sql = sql.split("group by")[0];        }        long count =  super.getCount(sql)/2;        DataGrid<RetainUser> dg = new DataGrid<>();        dg.rows = retainUsers;        dg.total = (int)count;        return dg;    }}