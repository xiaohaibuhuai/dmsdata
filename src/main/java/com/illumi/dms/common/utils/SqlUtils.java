package com.illumi.dms.common.utils;import com.illumi.dms.common.Consts;import org.apache.commons.lang.StringUtils;import java.util.Map;public class SqlUtils {    public static String concatSql (Map<String,Object> map) {        StringBuilder sb = new StringBuilder();        String fieldSql;        for (String fieldName : map.keySet()) {            if (map.get(fieldName)!=null){                fieldSql = getFieldSql(fieldName, map.get(fieldName));                sb.append(fieldSql);            }        }        return sb.toString();    }    public static String getFieldSql(String fieldName,Object fieldValue){        String result = "";        switch (fieldName)        {            case Consts.START_TIME: result = " and date >= '" + fieldValue +"'";break;            case Consts.END_TIME: result = " and date <= '" + fieldValue +"'";break;            case Consts.IS_ABROAD: result = " and "+fieldName+"="+fieldValue;break;            case Consts.APP_VERSION : result = " and "+fieldName+"="+fieldValue;break;        }        return result;    }    public static String getOrderSql(String oldSql,String sortField,String order) {        StringBuilder sb = new StringBuilder();        sb.append(oldSql);        sb.append(String.format(" order by %s %s",sortField,order));        return sb.toString();    }    public static String getGroupSql(String oldSql,String groupField){        StringBuilder sb = new StringBuilder();        sb.append(oldSql);        sb.append(String.format(" group by %s",groupField));        return sb.toString();    }    // 用户留存使用    public static String getSelectSql(){        // concat(left((sum(period_02)/sum(regist_user_num))*100,2),'%') period_02        StringBuilder sb = new StringBuilder();        String select = "select DATE_FORMAT(date,'%Y-%m-%d') date,sum(regist_user_num) regist_user_num ";        sb.append(select);        sb.append(getSelectByField("period_02"));        sb.append(getSelectByField("period_03"));        sb.append(getSelectByField("period_07"));        sb.append(getSelectByField("period_15"));        sb.append(getSelectByField("period_30"));        sb.append(" from ");        return sb.toString();    }    public static String getSelectByField(String field){        String result = "";        if (!StringUtils.isEmpty(field)){            result = " , concat(left((sum("+field+")/sum(regist_user_num))*100,2),'%') "+field;        }        return result;    }}