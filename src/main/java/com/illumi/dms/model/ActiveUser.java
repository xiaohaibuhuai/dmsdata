package com.illumi.dms.model;import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;import com.jayqqaa12.model.easyui.DataGrid;import com.jayqqaa12.model.easyui.Form;import com.jfinal.ext.plugin.tablebind.TableBind;@TableBind(tableName = "dms_active_user_view")public class ActiveUser extends EasyuiModel<ActiveUser> {    private static final long serialVersionUID = -7615377924993713398L;    public static ActiveUser dao = new ActiveUser();    @Override    public DataGrid<ActiveUser> listByDataGridBySortSql(DataGrid<ActiveUser> dg, Form f, String sortStr) {        DataGrid<ActiveUser> activeUserDataGrid = super.listByDataGridBySortSql(dg, f, sortStr);        return activeUserDataGrid;    }}