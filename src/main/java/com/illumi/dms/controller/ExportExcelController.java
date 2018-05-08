package com.illumi.dms.controller;

import com.alibaba.fastjson.JSON;
import com.illumi.dms.common.utils.ValidateObjectUtil;
import com.illumi.dms.model.test_dms.DmsUserView;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;
import com.jfinal.render.RenderFactory;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
@ControllerBind(controllerKey = "/export",viewPath="/page/system")
public class ExportExcelController extends  EasyuiController  {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportExcelController.class);

    private static abstract class ColumnFormat<T> {
        protected static Map<String, Object> REQUEST_PARAM = new HashMap<String, Object>();
        protected static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        protected static String FIELD = "name";
        protected static Map<String, Object> QUERY_PARAM =  new HashMap<String, Object>();

        public static void intData(Map<String, Object> requestParams) {
            REQUEST_PARAM.putAll(requestParams);
            QUERY_PARAM=ValidateObjectUtil.isBlankDefault(REQUEST_PARAM.get("queryParams"),new HashMap<String, Object>());
        }
        public abstract String[] format(Map<String, Object> row, List<Map<String, Object>> column);

        public abstract String[] format(T row, List<Map<String, Object>> column);

        public abstract List<T> execute();
    }



    public static class DmsUserViewCSV extends ColumnFormat<DmsUserView> {
        //columns:[{name:"date",text:"日期"},{name:"regist_user_num",text:"注册人数"},{name:"regist_buyin_user_num",text:"当日注册且买入人数"},{name:"total_buyin_user_num",text:"当日总买入用户数"},{name:"total_user_num",text:"累计注册人数"},{name:"total_buyin_user_num",text:"累计买入独立用户数"}],
        @Override
        public String[] format(Map<String, Object> row, List<Map<String, Object>> column) {
           return  null;
        }

        @Override
        public String[] format(DmsUserView row, List<Map<String, Object>> column) {
            String[] result = null;
            if (ValidateObjectUtil.isNotBlank(column, row)) {
                result = new String[column.size()];
                for (int i = 0; i < result.length; i++) {
                    String field = ValidateObjectUtil.isBlankDefault(column.get(i).get(DmsUserViewCSV.FIELD), "");
                    //Map<String, Object> fieldMap = column.get(i);
                    String value = ValidateObjectUtil.isBlankDefault(row.get(field),"");
                    result[i] = value;
                }
            }
            return result;
        }

        @Override
        public List<DmsUserView> execute() {
            try {
                int page =  ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("page"), 1);
                int pageSize = ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.REQUEST_PARAM.get("total"), 10000);
                String type= ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.REQUEST_PARAM.get("_type"), "all");
                String startDate = ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("startDate"), null);
                String endDate = ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("endDate"), null);
                String sort= ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("sort"),"date");
                StringBuffer sql = new StringBuffer("select 1 as gpid, date ,sum(regist_user_num) as regist_user_num, sum(buyin_user_num) as buyin_user_num,sum(regist_buyin_user_num)as regist_buyin_user_num,sum(total_user_num) as total_user_num, sum(total_buyin_user_num)as total_buyin_user_num from dms_user_view where 1=1");

                if(ValidateObjectUtil.isNotBlank(startDate)){

                    sql.append(" and date ").append(">= '").append(startDate).append("'");
                }
                if(ValidateObjectUtil.isNotBlank(endDate)){
                    sql.append(" and date ").append("< '").append(endDate).append("'");
                }

                if(ValidateObjectUtil.isNotBlank(type)){
                    if(type.equals("all")){
                        sql.append(" group by ").append(" date ");
                    }else if(type.equals("abroad")){
                        sql.append(" and is_abroad ").append("=").append("1");
                        sql.append(" group by ").append(" date ");
                    }else {
                        sql.append(" and is_abroad ").append("=").append("0");
                        sql.append(" group by ").append(" date ");
                    }
                }
                if(ValidateObjectUtil.isNotBlank(sort)){
                    sql.append(" order by ").append(sort).append(" desc ");
                }
                List<DmsUserView> list = DmsUserView.dao.find(sql.toString());
            return  list;

            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw  new RuntimeException(e);
            }
        }
    }











        enum TableExcel {
            //["/cms/transactionLog/topUpList","/cms/transactionLog/withDrawList","/cms/transactionLog/goldToGoldBeanList","/cms/transactionLog/goldBeanTogoldDiamondList","/cms/transactionLog/receiveGiftList","/cms/transactionLog/sendGiftList","/cms/transactionLog/floodScreenList","/cms/transactionLog/transferList","/cms/transactionLog/recycleList","/cms/transactionLog/freezeList","/cms/transactionLog/consumeDiamendList","/cms/transactionLog/sendGoldList"];
            //TOP_UP("/cms/transactionLog/topUpList",null);
            DMS_USER_VIEW("/statistic/dmsuserview/user/list",new DmsUserViewCSV());
            private String url;
            private ColumnFormat columnFormat;

            private TableExcel(String url, ColumnFormat columnFormat) {
                this.url = url;
                this.columnFormat = columnFormat;
            }

            public String getValue() {
                return this.url;
            }

            public ColumnFormat getFormat() {
                return this.columnFormat;
            }

            public static ColumnFormat getFormatInstance(String url) {
                if (ValidateObjectUtil.isNotBlank(url)) {
                    switch (url) {
                        case "/statistic/dmsuserview/user/list": {
                            return DMS_USER_VIEW.getFormat();
                        }
                        default: {
                            return null;
                        }
                    }
                } else {
                    throw new RuntimeException("访问路径为空");
                }
            }
        }




        public void csv() {
            render(new Render() {
                @Override
                public void render() {
                    try {

                        final HttpServletResponse response = getResponse();
                        String data = getPara("data");
                        if(ValidateObjectUtil.isBlank(data)){
                            throw  new RuntimeException("参数异常");
                        }
                        data = data.replace("&quot;","\"");
                        final Map<String, Object> requestParam = JSON.parseObject(data, Map.class);
                        List<Map<String, Object>> column = (List<Map<String, Object>>) requestParam.get("column");
                        LOGGER.info(String.format("------------->>>>>>> %s", requestParam));
                        ColumnFormat.intData(requestParam);

                        int toatl = ValidateObjectUtil.isBlankDefault(requestParam.get("total").toString(), 0);
                        Map<String, Object> excelDatas = new HashMap<String, Object>();

                        String fileName =  getPara("fileName");
                        fileName = fileName + "_" + new SimpleDateFormat("yyyy.MM.dd").format(Calendar.getInstance().getTime()) + "_.csv";
                        if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                            fileName = URLEncoder.encode(fileName, "UTF-8");
                        } else {
                            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
                        }
/*                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("multipart/form-data");
                        response.setHeader("Content-Disposition",
                                "attachment;fileName=" + fileName);*/
                        ServletOutputStream output = response.getOutputStream();
                        String excelData[][] = null;
                        List<Map<String, Object>> rows = null;
                        if (ValidateObjectUtil.isNotBlank(column)) {
                            excelData = new String[toatl + 1][column.size()];
                            String title[] = new String[column.size()];
                            for (int i = 0; i < column.size(); i++) {
                                title[i] = ValidateObjectUtil.isBlankDefault(column.get(i).get("text"), "");
                            }
                            excelData[0] = title;
                        }
                        List<Object> list=TableExcel.getFormatInstance(requestParam.get("url").toString()).execute();
                        for (int i = 1; i < excelData.length; i++) {
                            String dataSub[] = TableExcel.getFormatInstance(requestParam.get("url").toString()).format(list.get(i-1),column);
                            excelData[i] = dataSub;
                        }
                        StringBuffer buffer = new StringBuffer();
                        if (ValidateObjectUtil.isNotBlank(excelData)) {
                            for (int i = 0; i < excelData.length; i++) {
                                String temp = Arrays.toString(excelData[i]);
                                buffer.append(temp.substring(1, temp.length() - 1)).append("\r");
                            }
                        }
                        try {
                            output.write(buffer.toString().getBytes("GBK"));
                            //output.flush();
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage(), e);
                        } finally {
                            if (output != null)
                                try {
                                    output.close();
                                } catch (IOException e) {
                                    LOGGER.error("文件下载失败", e);
                                }
                        }

                    }catch (IOException ex){
                        ex.printStackTrace();
                    }
                }
            });
        }


}
