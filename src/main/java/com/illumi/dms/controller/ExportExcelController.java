package com.illumi.dms.controller;

import com.alibaba.fastjson.JSON;
import com.illumi.oms.common.utils.ValidateObjectUtil;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExportExcelController extends  EasyuiController  {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportExcelController.class);

    private static abstract class ColumnFormat<T> {
        protected static Map<String, Object> REQUEST_PARAM = new HashMap<String, Object>();
        protected static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        protected static Map<String, Object> QUERY_PARAM =  new HashMap<String, Object>();
        public static String TIELD = "field";
        public static void intData(Map<String, Object> requestParams) {
            REQUEST_PARAM = requestParams;
            QUERY_PARAM=ValidateObjectUtil.isBlankDefault(requestParams.get("queryParams"),new HashMap<String, Object>());
            List<Map<String, Object>> org = ValidateObjectUtil.isBlankDefault(REQUEST_PARAM.get("organization"), new ArrayList<Map<String, Object>>());
        }
        public abstract String[] format(Map<String, Object> row, List<Map<String, Object>> column);

        public abstract String[] format(T row, List<Map<String, Object>> column);

        public abstract List<T> execute();
    }



    /*public static class ReceiveGift extends ColumnFormat<ExchangeOrderResult> {
        *//*
                                      {field: 'toUserId', title: '收礼物用户ID', align: 'center', width: '10%'},
                                      {field: 'toUser.name', title: '收用户昵称', align: 'center', width: '10%',formatter:function(value, row){
                                          return  row.toUser==undefined?"":row.toUser.name==undefined?"":row.toUser.name;
                                      }},
                                      {field: 'toOrgName', title: '所属工会', align: 'center', width: '10%',formatter:function(value, row){
                                          return util.statusFormat("exchangeOrder.toOrgName",row);
                                      }},
                                      {field: 'toAmountA', title: 'D1金币数', align: 'center', width: '10%',formatter:function(value, row){
                                          return util.statusFormat(" ",value);
                                      }},
                                      {field: 'toAmountB', title: 'D2金币数', align: 'center', width: '10%',formatter:function(value, row){
                                          return util.statusFormat("exchangeOrder.toAmountB",value);
                                      }},
                                      {field: 'productName', title: '礼物名称', align: 'center', width: '10%'},
                                      {field: 'productNum', title: '礼物数量', align: 'center', width: '10%'},
                                      {field: 'fromUserId',title: '赠送礼物用户ID', align: 'center', width: '10%'},
                                      {field: 'fromUser.name', title: '赠送礼物用户昵称', align: 'center', width: '10%',formatter:function(value, row){
                                          return  row.fromUser==undefined?"":row.fromUser.name==undefined?"":row.fromUser.name;
                                      }},
                                      {field: 'fromOrgName', title: '所属工会', align: 'center', width: '10%',formatter:function(value, row){
                                          return util.statusFormat("exchangeOrder.fromOrgName",row);
                                      }},
                                      {field: 'createAt', title: '时间', align: 'center', width: '10%',formatter:function(value, row){
                                          return $.fn.datebox.defaults.formatterTimestamp(new Date(value))
                                      }}
     *//*
        @Override
        public String[] format(Map<String, Object> row, List<Map<String, Object>> column) {
            String[] result = null;
            if (ValidateObjectUtil.isNotBlank(column, row)) {
                result = new String[column.size()];
                Map<String, Object> rowMap = row;
                for (int i = 0; i < result.length; i++) {
                    String field = ValidateObjectUtil.isBlankDefault(column.get(i).get(TopUp.TIELD), "");
                    String value = null;
                    switch (field) {
                        case "toUserId": {
                            value = ValidateObjectUtil.isBlankDefault(rowMap.get(field), "");
                        }
                        break;
                        case "toUser.name": {
                            Map<String, Object> formUser = ValidateObjectUtil.isBlankDefault(rowMap.get("toUser"), new HashMap<String, Object>());
                            if (ValidateObjectUtil.isNotBlank(formUser)) {
                                value = ValidateObjectUtil.isBlankDefault(formUser.get("name"), "");
                            } else {
                                value = "";
                            }
                        }
                        break;
                        case "toOrgName": {
                            value = ValidateObjectUtil.isBlankDefault(
                                    rowMap.get(field),
                                    this.getOrganizationName(
                                            ValidateObjectUtil.isBlankDefault(
                                                    rowMap.get("toOrgId"), "0"
                                            )
                                    )
                            );
                        }
                        break;
                        case "toAmountA": {
                            value = jiaGe(ValidateObjectUtil.isBlankDefault(rowMap.get(field), "0"));
                        }
                        break;
                        case "toAmountB": {
                            value = jiaGe(ValidateObjectUtil.isBlankDefault(rowMap.get(field), "0"));
                        }
                        break;
                        case "productName": {
                            value = ValidateObjectUtil.isBlankDefault(rowMap.get(field), "");
                        }
                        break;
                        case "productNum": {
                            value = ValidateObjectUtil.isBlankDefault(rowMap.get(field), "0");
                        }
                        break;
                        case "fromUserId": {
                            value = ValidateObjectUtil.isBlankDefault(rowMap.get(field), "");
                        }
                        break;
                        case "fromUser.name": {
                            Map<String, Object> formUser = ValidateObjectUtil.isBlankDefault(rowMap.get("fromUser"), new HashMap<String, Object>());
                            if (ValidateObjectUtil.isNotBlank(formUser)) {
                                value = ValidateObjectUtil.isBlankDefault(formUser.get("name"), "");
                            } else {
                                value = "";
                            }
                        }
                        break;
                        case "fromOrgName": {
                            value = ValidateObjectUtil.isBlankDefault(
                                    rowMap.get(field),
                                    this.getOrganizationName(
                                            ValidateObjectUtil.isBlankDefault(
                                                    rowMap.get("fromOrgId"), "0"
                                            )
                                    )
                            );
                        }
                        break;
                        case "createAt": {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(ValidateObjectUtil.isBlankDefault(rowMap.get(field), 0L));
                            value = DATE_FORMAT.format(calendar.getTime());
                        }
                        break;
                        default: {
                            value = "";
                        }
                        break;
                    }
                    result[i] = value;
                }
            }
            return result;
        }

        @Override
        public String[] format(ExchangeOrderResult row, List<Map<String, Object>> column) {
            String[] result = null;
            if (ValidateObjectUtil.isNotBlank(column, row)) {
                result = new String[column.size()];
                for (int i = 0; i < result.length; i++) {
                    String field = ValidateObjectUtil.isBlankDefault(column.get(i).get(TopUp.TIELD), "");
                    String value = null;
                    switch (field) {
                        case "toUserId": {
                            //value = ValidateObjectUtil.isBlankDefault(row.getToUserId(), "");
                            value=formatUserId(row.getToUser(),null);
                        }
                        break;
                        case "toUser.name": {
                            if(ValidateObjectUtil.isNotBlank(row.getToUser())){
                                value=ValidateObjectUtil.isBlankDefault(row.getToUser().getName(), "");
                            }
                            else {
                                value = "";
                            }
                            value = value.replace(",","，");
                        }
                        break;
                        case "toOrgName": {
                            value = ValidateObjectUtil.isBlankDefault(
                                    row.getToOrgName(),
                                    this.getOrganizationName(
                                            ValidateObjectUtil.isBlankDefault(
                                                    row.getToOrgId(), "0"
                                            )
                                    )
                            );
                        }
                        break;
                        case "toAmountA": {
                            value = jiaGe(ValidateObjectUtil.isBlankDefault(row.getToAmountA(), "0"));
                        }
                        break;
                        case "toAmountB": {
                            value = jiaGe(ValidateObjectUtil.isBlankDefault(row.getToAmountB(), "0"));
                        }
                        break;
                        case "productName": {
                            value = ValidateObjectUtil.isBlankDefault(row.getProductName(), "");
                        }
                        break;
                        case "productNum": {
                            value = ValidateObjectUtil.isBlankDefault(row.getProductNum(), "0");
                        }
                        break;
                        case "fromUserId": {
                            //value = ValidateObjectUtil.isBlankDefault(row.getFromUserId(), "");
                            value=formatUserId(row.getFromUser(),null);
                        }
                        break;
                        case "fromUser.name": {
                            if(ValidateObjectUtil.isNotBlank(row.getFromUser())){
                                value = ValidateObjectUtil.isBlankDefault(row.getFromUser().getName(), "");
                            }
                            else {
                                value = "";
                            }
                            value = value.replace(",","，");
                        }
                        break;
                        case "fromOrgName": {
                            value = ValidateObjectUtil.isBlankDefault(
                                    row.getFromOrgName(),
                                    this.getOrganizationName(
                                            ValidateObjectUtil.isBlankDefault(
                                                    row.getFromOrgId(), "0"
                                            )
                                    )
                            );
                        }
                        break;
                        case "createAt": {
                            if(ValidateObjectUtil.isNotBlank(row.getCreateAt())){
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(row.getCreateAt().getTime());
                                value = DATE_FORMAT.format(calendar.getTime());
                            }else{
                                value="";
                            }

                        }
                        break;
                        case "toDid": {
                            value = ValidateObjectUtil.isBlankDefault(row.getToDid(), "");
                        }
                        break;
                        case "toClientIp": {
                            value = ValidateObjectUtil.isBlankDefault(row.getToClientIp(), "");
                        }
                        break;
                        default: {
                            value = "";
                        }
                        break;
                    }
                    result[i] = value;
                }
            }
            return result;
        }

        @Override
        public List<ExchangeOrderResult> execute() {
            try {
                int page =  ValidateObjectUtil.isBlankDefault(ReceiveGift.QUERY_PARAM.get("page"), 1);
                int pageSize = ValidateObjectUtil.isBlankDefault(ReceiveGift.REQUEST_PARAM.get("total"), 10000);
                long toUserId =ValidateObjectUtil.isBlankDefault(ReceiveGift.QUERY_PARAM.get("userId"), 0L);
                long toOrgId = ValidateObjectUtil.isBlankDefault(ReceiveGift.QUERY_PARAM.get("organizationId"), 0L);
                String startTime = ValidateObjectUtil.isBlankDefault(ReceiveGift.QUERY_PARAM.get("startDate"), null);
                String endTime = ValidateObjectUtil.isBlankDefault(ReceiveGift.QUERY_PARAM.get("endDate"), null);
                String productName = ValidateObjectUtil.isBlankDefault(ReceiveGift.QUERY_PARAM.get("giftName"), null);
                List<ExchangeOrderResult> list = new ArrayList<ExchangeOrderResult>();
                *//*-----------------查询数据------------------*//*
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("toUserId", ValidateObjectUtil.isBlankDefault(toUserId, null));
                params.put("toOrgId", ValidateObjectUtil.isBlankDefault(toOrgId, null));
                params.put("productName", ValidateObjectUtil.isBlankDefault(productName, null));
                params.put("startTime", ValidateObjectUtil.isBlankDefault(startTime, null));
                params.put("endTime", ValidateObjectUtil.isBlankDefault(endTime, null));
                System.out.println(String.format("收礼物列表参数：%s", JSON.toJSONString(params)));
                int total=pageSize;
                pageSize=10000;
                int totalPage=((total)%pageSize)==0?((total)/pageSize):((total)+pageSize)/pageSize;

                for(int i=page;i<=totalPage;i++){
                    RespEntity<AccountEntityPage<ExchangeOrder>> respEntity1=this.orderRespService.getReceiveGiftQueryList(params, i, pageSize);
                    if (ValidateObjectUtil.isNotBlank(respEntity1)&&ValidateObjectUtil.isNotBlank(respEntity1.getEntinty())&&ValidateObjectUtil.isNotBlank(respEntity1.getEntinty().getList())) {
                        AccountEntityPage<ExchangeOrder> pageList2=respEntity1.getEntinty();
                        StringBuffer fromUserBuffer=new StringBuffer();
                        StringBuffer toUserBuffer=new StringBuffer();
                        Map<String,User> fromUserMap=new HashMap<String, User>();
                        Map<String,User> toUserMap=new HashMap<String, User>();
                        for (ExchangeOrder exchangeOrder : pageList2.getList()) {
                            fromUserBuffer.append(exchangeOrder.getFromUserId()).append(",");
                            toUserBuffer.append(exchangeOrder.getToUserId()).append(",");
                        }
                        if(fromUserBuffer.length()>0){
                            fromUserBuffer.substring(0, fromUserBuffer.length()-1);
                        }
                        if(toUserBuffer.length()>0){
                            toUserBuffer.substring(0, toUserBuffer.length()-1);
                        }
                        List<User> fromUsers=this.userService.getUserByIds(fromUserBuffer.toString());
                        List<User> toUsers=this.userService.getUserByIds(toUserBuffer.toString());
                        if(ValidateObjectUtil.isNotBlank(fromUsers)){
                            for (User user : fromUsers) {
                                fromUserMap.put(String.valueOf(user.getId()), user);
                            }
                        }
                        if(ValidateObjectUtil.isNotBlank(toUsers)){
                            for (User user : toUsers) {
                                toUserMap.put(String.valueOf(user.getId()), user);
                            }
                        }
                        for (ExchangeOrder exchangeOrder : pageList2.getList()) {
                            ExchangeOrderResult exchangeRecordResult = new ExchangeOrderResult(
                                    exchangeOrder);
                            UserResult fromUser=new UserResult();
                            fromUser.setId(exchangeOrder.getFromUserId());
                            if(ValidateObjectUtil.isNotBlank(fromUserMap.get(String.valueOf(ValidateObjectUtil.isBlankDefault(exchangeOrder.getFromUserId(),""))))){
                                fromUser.setName(ValidateObjectUtil.isBlankDefault(fromUserMap.get(String.valueOf(exchangeOrder.getFromUserId())).getName(),""));
                                fromUser.setDepartment(ValidateObjectUtil.isBlankDefault(fromUserMap.get(String.valueOf(exchangeOrder.getFromUserId())).getDepartment(),""));
                                fromUser.setType(ValidateObjectUtil.isBlankDefault(fromUserMap.get(String.valueOf(exchangeOrder.getFromUserId())).getType(),""));

                            }
                            UserResult toUser=new UserResult();
                            toUser.setId(exchangeOrder.getToUserId());
                            if(ValidateObjectUtil.isNotBlank(toUserMap.get(String.valueOf(ValidateObjectUtil.isBlankDefault(exchangeOrder.getToUserId(),""))))){
                                toUser.setName(ValidateObjectUtil.isBlankDefault(toUserMap.get(String.valueOf(exchangeOrder.getToUserId())).getName(),""));
                                toUser.setDepartment(ValidateObjectUtil.isBlankDefault(toUserMap.get(String.valueOf(exchangeOrder.getToUserId())).getDepartment(),""));
                                toUser.setType(ValidateObjectUtil.isBlankDefault(toUserMap.get(String.valueOf(exchangeOrder.getToUserId())).getType(),""));
                            }
                            exchangeRecordResult.setFromUser(fromUser);
                            exchangeRecordResult.setToUser(toUser);
                            list.add(exchangeRecordResult);
                        }
                    }
                }
            return  list;
        *//*-----------------查询数据------------------*//*
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw  new RuntimeException(e);
            }
        }
    }*/











        enum TableExcel {
            //["/cms/transactionLog/topUpList","/cms/transactionLog/withDrawList","/cms/transactionLog/goldToGoldBeanList","/cms/transactionLog/goldBeanTogoldDiamondList","/cms/transactionLog/receiveGiftList","/cms/transactionLog/sendGiftList","/cms/transactionLog/floodScreenList","/cms/transactionLog/transferList","/cms/transactionLog/recycleList","/cms/transactionLog/freezeList","/cms/transactionLog/consumeDiamendList","/cms/transactionLog/sendGoldList"];
            TOP_UP("/cms/transactionLog/topUpList",null);

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
                        case "/cms/transactionLog/topUpList": {
                            return TOP_UP.getFormat();
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
            try {
                final HttpServletRequest request = null;
                final HttpServletResponse response = null;
                String data = null;
                final Map<String, Object> requestParam = JSON.parseObject(data, Map.class);
                List<List<Map<String, Object>>> column = (List<List<Map<String, Object>>>) requestParam.get("column");
                LOGGER.info(String.format("------------->>>>>>> %s", requestParam));
                ColumnFormat.intData(requestParam);

                int toatl = ValidateObjectUtil.isBlankDefault(requestParam.get("total").toString(), 0);
                Map<String, Object> excelDatas = new HashMap<String, Object>();

                String fileName = request.getParameter("fileName");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("multipart/form-data");
                response.setHeader("Content-Disposition",
                        "attachment;fileName=" + UrlEncoded.encodeString(fileName + "_" + new SimpleDateFormat("yyyy.MM.dd").format(Calendar.getInstance().getTime()) + "_.csv", "utf-8"));
                ServletOutputStream output = response.getOutputStream();
                String excelData[][] = null;
                List<Map<String, Object>> rows = null;
                if (ValidateObjectUtil.isNotBlank(column)) {
                    excelData = new String[toatl + 1][column.get(0).size()];
                    String title[] = new String[column.get(0).size()];
                    for (int i = 0; i < column.get(0).size(); i++) {
                        title[i] = ValidateObjectUtil.isBlankDefault(column.get(0).get(i).get("title"), "");
                    }
                    excelData[0] = title;
                }
                List<Object> list=TableExcel.getFormatInstance(requestParam.get("url").toString()).execute();
                for (int i = 1; i < excelData.length; i++) {
                    String dataSub[] = TableExcel.getFormatInstance(requestParam.get("url").toString()).format(list.get(i-1),column.get(0));
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

}
