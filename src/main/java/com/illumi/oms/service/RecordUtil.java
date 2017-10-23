package com.illumi.oms.service;

import java.util.List;

import com.jayqqaa12.jbase.jfinal.ext.model.Db;
import com.jayqqaa12.jbase.util.Txt;
import com.jayqqaa12.jbase.util.Validate;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jayqqaa12.model.easyui.Form;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.plugin.activerecord.Record;


public class RecordUtil 
{
    
    public static DataGrid<Record> listByDataGridDefault(String sql,DataGrid<Record> dg, Form f)
    {
        List<Record> list = Db.find(sql + f.getWhereAndLimit(dg));
        dg.rows = list;
        dg.total = (int)getCountByDefualt(sql+f.getWhereAndSort(dg));
        return dg;
    }
    
	public static DataGrid<Record> listByDataGrid(String sql,DataGrid<Record> dg, Form f)
	{
		List<Record> list = Db.use("pokerdb").find(sql + f.getWhereAndLimit(dg));
		dg.rows = list;
		dg.total = (int)getCount(sql+f.getWhereAndSort(dg));
		return dg;
	}
	public static DataGrid<Record> listByDataGrid(String dbname,String sql,DataGrid<Record> dg, Form f)
    {
	    
        List<Record> list = Db.use(dbname).find(sql + f.getWhereAndLimit(dg).replace("where", "and"));
        System.out.println(sql + f.getWhereAndLimit(dg).replace("where", "and"));
        dg.rows = list;
        dg.total = (int)getCountByDb(dbname,sql+f.getWhereAndSort(dg).replace("where", "and"));
        return dg;
    }
	
	public static DataGrid<Record> listByDataGridForFrom(String sql,DataGrid<Record> dg, Form f)
	{
		List<Record> list = Db.use("pokerdb").find(sql + f.getWhereAndLimit(dg));
		dg.rows = list;
		dg.total = (int)getCountForFrom(sql+f.getWhereAndSort(dg));
		return dg;
	}
	public static DataGrid<Record> listByDataGridByWhere(String sql,DataGrid<Record> dg, Form f)
	{
		List<Record> list = Db.use("pokerdb").find(sql + f.getWhereAndLimit(dg).replace("where", "and"));
		dg.rows = list;
		dg.total = (int)getCount(sql+f.getWhereAndSort(dg).replace("where", "and"));
				
		return dg;
	}
	public static DataGrid<Record> listByDataGridByWhereAndFrom(String sql,DataGrid<Record> dg, Form f)
	{
		if(sql.contains("group by")){
			String[] sqlStrs = sql.split("group by");
			List<Record> list = Db.use("pokerdb").find(sqlStrs[0] +f.getAndWhere() + "group by" + sqlStrs[1] + f.getSortAndLimit(dg));
			dg.rows = list;
			dg.total = (int)getCountForAll(sqlStrs[0] +f.getAndWhere() + "group by" + sqlStrs[1]);
		}else{
			List<Record> list = Db.use("pokerdb").find(sql + f.getWhereAndLimit(dg).replace("where", "and"));
			dg.rows = list;
			dg.total = (int)getCountForFrom(sql+f.getWhereAndSort(dg).replace("where", "and"));
		}
		return dg;
	}
	public static DataGrid<Record> listByDataGridByWhereInWP(String sql,DataGrid<Record> dg, Form f)
	{
		List<Record> list = Db.use("wcpaydb").find(sql + f.getWhereAndLimit(dg));
		dg.rows = list;
		dg.total = (int)getCountInWP(sql+f.getWhereAndSort(dg));
		return dg;
	}
	
	public static DataGrid<Record> listByDataGrid(String dbName,String sqlName,DataGrid<Record> dg, Object... paras)
	{
		String sql = getLimitSort(SqlKit.sql(sqlName),dg);
		List<Record> rdList = Db.use(dbName).find(sql,paras);
	    long total = (Db.use(dbName).queryLong(RecordUtil.getCountSql(SqlKit.sql(sqlName)), paras));
		dg.rows = rdList;
		dg.total = (int)total;	
		return dg;
	
	}
	public static DataGrid<Record> listBySqlDataGrid(String dbName,String sql,DataGrid<Record> dg, Object... paras)
	{
		long total = Db.use(dbName).queryLong(RecordUtil.getCountSql2(sql), paras);
		sql = getLimitSort(sql,dg);
		List<Record> rdList = Db.use(dbName).find(sql,paras);
	   
		dg.rows = rdList;
		dg.total = (int)total;	
		return dg;
	
	}
	
	public static DataGrid<Record> listBySqlDataGrid(String sql,DataGrid<Record> dg, Object... paras)
	{
		long total = Db.queryLong(RecordUtil.getCountSql(sql), paras);
		sql = getLimitSort(sql,dg);
		List<Record> rdList = Db.find(sql,paras);
	   
		dg.rows = rdList;
		dg.total = (int)total;	
		return dg;
	
	}
	
	public static DataGrid<Record> listByDataGrid(String dbName,String sqlName,String sqlCount,DataGrid<Record> dg, Object... paras)
	{
		String sql = getLimitSort(SqlKit.sql(sqlName),dg);
		List<Record> rdList = Db.use(dbName).find(sql,paras);
	    long total = (Db.use(dbName).queryLong(RecordUtil.getCountSql(SqlKit.sql(sqlCount)), paras));
		dg.rows = rdList;
		dg.total = (int)total;	
		return dg;
	
	}
	//门票检索使用
	public static DataGrid<Record> listByDataGridForTicket(String sql,DataGrid<Record> dg, Form f,String spliceSql)
    {
        List<Record> list = Db.use("pokerdb").find(sql + f.getWhereAndLimit(dg).split("order")[0]+spliceSql+" order "+ f.getWhereAndLimit(dg).split("order")[1]);
        dg.rows = list;
        dg.total = (int)getCount(sql+f.getWhereAndSort(dg).split("order")[0] + spliceSql + " order "+  f.getWhereAndSort(dg).split("order")[1]);
        return dg;
    }
	
	
	public static long getCountByDb(String dbname,String sql)
    {
        sql = Txt.split(sql.toLowerCase(), "from")[1];
        if (sql.contains("order by")) sql = Txt.split(sql, "order by")[0];

        return Db.use(dbname).queryLong(" select count(*) as c from " + sql);
    }
	
	public static long getCount(String sql)
	{
		sql = Txt.split(sql.toLowerCase(), " from ")[1];
		if (sql.contains("order by")) sql = Txt.split(sql, "order by")[0];

		return Db.use("pokerdb").queryLong(" select count(*) as c from " + sql);
	}
	
	public static long getCountByDefualt(String sql)
    {
        sql = Txt.split(sql.toLowerCase(), "from")[1];
        if (sql.contains("order by")) sql = Txt.split(sql, "order by")[0];

        return Db.queryLong(" select count(*) as c from " + sql);
    }
	
	public static String getCountSql(String sql){
//		sql = sql.toLowerCase();
//		sql = sql.substring(sql.indexOf("from"));
//		if (sql.contains("order by")) sql = Txt.split(sql, "order by")[0];
		sql = " select count(*) as c  from ( " +sql+" ) as t";
		return sql;
	}
	public static String getCountSql2(String sql){
	
		sql = Txt.split(sql, "from")[1];
		if (sql.contains("order by")) sql = Txt.split(sql, "order by")[0];
		sql = " select count(*) as c from " + sql;
		return sql;
	}
	
	public static String getLimitSort(String sql,DataGrid<Record> dg){
		sql += sort(dg.sortName,dg.sortOrder)+limit(dg.page,dg.total);
		
		return sql;
	}
	
	private static String sort(String sortName, String sortOrder)
	{
		if (Validate.isEmpty(sortName)) return "";
		else return " order by " + sortName + " " + sortOrder;
	}

	private static String limit(int page, int size)
	{
		if (page > 0) page -= 1;
		return " limit " + size * page + "," + size;
	}
	
	public static long getCountInWP(String sql)
	{
		sql = Txt.split(sql, "from")[1];
		if (sql.contains("order by")) sql = Txt.split(sql, "order by")[0];
		
		return Db.use("wcpaydb").queryLong(" select count(*) as c from " + sql);
	}
	public static long getCountForFrom(String sql)
	{
		int num = sql.split("FROM_UNIXTIME").length-1;
		sql = Txt.split(sql.toLowerCase(), "from")[1+num];
		if (sql.contains("order by")) sql = Txt.split(sql, "order by")[0];

		return Db.use("pokerdb").queryLong(" select count(*) as c from " + sql);
	}
	
	public static long getCountForAll(String sql)
	{
		return Db.use("pokerdb").queryLong(" select count(*) as c from (" + sql +") as ct");
	}

}
