package com.illumi.dms.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.illumi.dms.common.Consts;
import com.illumi.dms.common.utils.ValidateObjectUtil;
import com.illumi.dms.model.user.*;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import com.jfinal.render.Render;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import javax.servlet.ServletOutputStream;
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
                String by= ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("by"),"desc");
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
                    sql.append(" order by ").append(sort).append(" ").append(by);
                }
                DmsUserView view0 = DmsUserView.getTodayView(type);
                List<DmsUserView> list = DmsUserView.dao.find(sql.toString());
                list.add(0,view0);
            return  list;

            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw  new RuntimeException(e);
            }
        }
    }





    public static class DmsPatTypeViewCSV extends ColumnFormat<DmsPayTypeView> {
        @Override
        public String[] format(Map<String, Object> row, List<Map<String, Object>> column) {
            return  null;
        }

        @Override
        public String[] format(DmsPayTypeView row, List<Map<String, Object>> column) {
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
        public List<DmsPayTypeView> execute() {
            try {
                int page =  ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("page"), 1);
                int pageSize = ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.REQUEST_PARAM.get("total"), 10000);
                String type= ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.REQUEST_PARAM.get("_type"), "all");
                String startDate = ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("startDate"), null);
                String endDate = ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("endDate"), null);
                String sort= ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("sort"),"date");
                String by= ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("by"),"desc");
                JSONObject josn = new JSONObject();

                StringBuffer sql = new StringBuffer("select 1 as groupid , pay_type ,sum(person_num) as person_num, sum(pay_times) as pay_times,sum(pay_sum)as pay_sum ")
                        .append(" from dms_pay_type_view where 1=1");

                if(ValidateObjectUtil.isNotBlank(startDate)){

                    sql.append(" and date ").append(">= '").append(startDate).append("'");
                }
                if(ValidateObjectUtil.isNotBlank(endDate)){
                    sql.append(" and date ").append("< '").append(endDate).append("'");
                }

                if(ValidateObjectUtil.isNotBlank(type)){
                    if(type.equals("all")){
                        sql.append(" group by ").append(" pay_type ");
                    }else if(type.equals("abroad")){
                        sql.append(" and isabroad ").append("=").append("1");
                        sql.append(" group by ").append(" pay_type ");
                    }else {
                        sql.append(" and isabroad ").append("=").append("0");
                        sql.append(" group by ").append(" pay_type ");
                    }
                }

                List<DmsPayTypeView> list = DmsPayTypeView.dao.find(sql.toString());

                if(ValidateObjectUtil.isNotBlank(list)){

                    if(!ValidateObjectUtil.numberIsNotBlank(page,1)){
                        //DmsUserView view0 = DmsUserView.getTodayView(type);
                        //list.add(0,view0);
                    }
                    for (DmsPayTypeView view : list){
                        LOGGER.info(String.format(">>>>>>>>>>>>:%s钻大礼包",view.get("pay_type")));
                        if("other".equals(view.get("pay_type"))){
                            view.put("pay_type","其它");
                        }
                        view.put("pay_type",String.format("%s钻大礼包",view.get("pay_type")));
                    }
                    josn.put("rows",list);
                }else {
                    list = new ArrayList<>();
                    if(!ValidateObjectUtil.numberIsNotBlank(page,1)){
                        //DmsUserView view0 = DmsUserView.getTodayView(type);
                        //list.add(0,view0);
                    }
                    josn.put("rows",list);
                }

                return  list;

            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw  new RuntimeException(e);
            }
        }
    }




    public static class DmsPayChanelCSV extends ColumnFormat<DmsPayChanelView> {
        //columns:[{name:"date",text:"日期"},{name:"regist_user_num",text:"注册人数"},{name:"regist_buyin_user_num",text:"当日注册且买入人数"},{name:"total_buyin_user_num",text:"当日总买入用户数"},{name:"total_user_num",text:"累计注册人数"},{name:"total_buyin_user_num",text:"累计买入独立用户数"}],
        @Override
        public String[] format(Map<String, Object> row, List<Map<String, Object>> column) {
            return  null;
        }

        @Override
        public String[] format(DmsPayChanelView row, List<Map<String, Object>> column) {
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
        public List<DmsPayChanelView> execute() {
            try {
                int page =  ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("page"), 1);
                int pageSize = ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.REQUEST_PARAM.get("total"), 10000);
                String type= ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.REQUEST_PARAM.get("_type"), "all");
                String startDate = ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("startDate"), null);
                String endDate = ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("endDate"), null);
                String sort= ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("sort"),"date");
                String by= ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("by"),"desc");

                StringBuffer sql = new StringBuffer("select 1 as gpid, date ,sum(pay_101) as pay_101, sum(pay_102) as pay_102,sum(pay_201)as pay_201,sum(pay_202) as pay_202, sum(pay_301)as pay_301 ,")
                        .append(" sum(pay_302) as pay_302, ")
                        .append(" sum(pay_303) as pay_303, ")
                        .append(" sum(pay_401) as pay_401, ")
                        .append(" sum(pay_402) as pay_402, ")
                        .append(" sum(pay_403) as pay_403, ")
                        .append(" sum(pay_404) as pay_404, ")
                        .append(" sum(pay_405) as pay_405, ")
                        .append(" sum(pay_406) as pay_406, ")
                        .append(" sum(pay_501) as pay_501, ")
                        .append(" sum(pay_502) as pay_502, ")
                        .append(" sum(pay_503) as pay_503, ")
                        .append(" sum(pay_601) as pay_601, ")
                        .append(" sum(pay_602) as pay_602, ")
                        .append(" sum(pay_603) as pay_603 ")
                        .append(" from dms_pay_chanel_view where 1=1");



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
                    sql.append(" order by ").append(sort).append(" ").append(by);
                }

                List<DmsPayChanelView> list = DmsPayChanelView.dao.find(sql.toString());

                return  list;

            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw  new RuntimeException(e);
            }
        }
    }




    public static class DmsCoinConsumeViewCSV extends ColumnFormat<DmsCoinConsumeView> {
        //columns:[{name:"date",text:"日期"},{name:"regist_user_num",text:"注册人数"},{name:"regist_buyin_user_num",text:"当日注册且买入人数"},{name:"total_buyin_user_num",text:"当日总买入用户数"},{name:"total_user_num",text:"累计注册人数"},{name:"total_buyin_user_num",text:"累计买入独立用户数"}],
        @Override
        public String[] format(Map<String, Object> row, List<Map<String, Object>> column) {
            return  null;
        }

        @Override
        public String[] format(DmsCoinConsumeView row, List<Map<String, Object>> column) {
            String[] result = null;
            if (ValidateObjectUtil.isNotBlank(column, row)) {
                result = new String[column.size()];
                for (int i = 0; i < result.length; i++) {
                    String field = ValidateObjectUtil.isBlankDefault(column.get(i).get(DmsUserViewCSV.FIELD), "");
                    System.out.println(String.format(" >>>>>>>>>>>>>row data:%s ", JsonKit.toJson(row,8)));
                    String value = ValidateObjectUtil.isBlankDefault(row.get(field),"");
                    result[i] = value;
                }
            }
            return result;
        }

        @Override
        public List<DmsCoinConsumeView> execute() {
            try {
                int page =  ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("page"), 1);
                int pageSize = ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.REQUEST_PARAM.get("total"), 10000);
                String type= ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.REQUEST_PARAM.get("_type"), "all");
                String startDate = ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("startDate"), null);
                String endDate = ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("endDate"), null);
                String sort= ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("sort"),"date");
                String by= ValidateObjectUtil.isBlankDefault(DmsUserViewCSV.QUERY_PARAM.get("by"),"desc");


                StringBuffer sql = new StringBuffer("select 1 as gpid, date,")
                        .append(" sum(service_fee) as service_fee, ")
                        .append(" sum(reward_service_fee) as reward_service_fee, ")
                        .append(" sum(mtt_enter) as mtt_enter, ")
                        .append(" sum(magic_emoji) as magic_emoji, ")
                        .append(" sum(interactive_props) as interactive_props, ")
                        .append(" sum(low_barrage) as low_barrage, ")
                        .append(" sum(mid_barrage) as mid_barrage, ")
                        .append(" sum(high_barrage) as high_barrage, ")
                        .append(" sum(draw_card) as draw_card, ")
                        .append(" sum(poker) as poker, ")
                        .append(" sum(caribbeangame) as caribbeangame, ")
                        .append(" sum(cocklain) as cocklain, ")
                        .append(" sum(eight_eight) as eight_eight, ")
                        .append(" sum(car_game) as car_game, ")
                        .append(" sum(fishing) as fishing, ")
                        .append(" sum(total_emoji) as total_emoji, ")
                        .append(" sum(total_barrage) as total_barrage, ")
                        .append(" sum(total_minigame) as total_minigame ")
                        .append(" from dms_coin_consume_view where 1=1");



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
                    sql.append(" order by ").append(sort).append(" ").append(by);
                }

                List<DmsCoinConsumeView> list = DmsCoinConsumeView.dao.find(sql.toString());

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
            DMS_USER_VIEW("/statistic/dmsuserview/user/list",new DmsUserViewCSV()),DMS_PAY_TYPW_VIEW("/statistic/dmspaytypeview/user/list",new DmsPatTypeViewCSV()),
            DMS_PAT_CHANEL_VIEW("/statistic/dmschanelview/user/list",new DmsPayChanelCSV()),DMS_COIN_CONSUME_VIEW("/statistic/dmscoinview/user/list",new DmsCoinConsumeViewCSV());
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
                        case "/statistic/dmspaytypeview/user/list": {
                            return DMS_PAY_TYPW_VIEW.getFormat();
                        }
                        case "/statistic/dmschanelview/user/list": {
                            return DMS_PAT_CHANEL_VIEW.getFormat();
                        }
                        case "/statistic/dmscoinview/user/list": {
                            return DMS_COIN_CONSUME_VIEW.getFormat();
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
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("multipart/form-data");
                        response.setHeader("Content-Disposition",
                                "attachment;fileName=" + fileName);
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
                        LOGGER.info(String.format(">>>>>>>>>>>>>>>>>>size:%s,excelData.shape:(%s,%s)", list.size(),excelData.length,excelData[0].length));
                        for (int i = 1; i < excelData.length; i++) {
                            if((i-1)>=list.size()){
                                continue;
                            }
                            String dataSub[] = TableExcel.getFormatInstance(requestParam.get("url").toString()).format(list.get(i-1),column);
                            LOGGER.info(String.format(">>>>>>>>>>>>>>>>>>excelData.shape:(%s,%s),rowdata:%s",excelData.length,excelData[0].length,dataSub.length));
                            LOGGER.info(JSON.toJSONString(dataSub));
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
