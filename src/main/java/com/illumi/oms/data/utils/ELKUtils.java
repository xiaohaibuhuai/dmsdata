package com.illumi.oms.data.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.illumi.oms.system.model.Chart;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.illumi.oms.common.Consts;
import com.illumi.oms.data.model.ChartInfo;
import com.illumi.oms.data.model.RankInfo;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.*;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ParsedValueCount;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class ELKUtils {


    private static final Logger log = Logger.getLogger(ELKUtils.class);

    public static RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("10.105.92.212", 9200, "http")
//					,new HttpHost("10.154.15.9", 9200, "http"),new HttpHost("10.105.65.21", 9200, "http")
            ));


    public static void main(String[] args) {

        String[] urlhead = {"ilumi_transactionlog_", "ilumi_minigame_", "iii_ddd_bbb_"};
        String urlend = "/_search?size=30";

        String d = getUrl4SeletedTime(1504381500000l, 1511811900000l, urlhead, urlend);

        System.out.println(d);
    }

    public static List<ChartInfo> getchartChangeInfo(String urlheadTask, String target, long time, String timeformat) {
        String[] urlHead = {urlheadTask};
        return getchartChangeInfo(urlHead, target, time, timeformat);

    }

    public static List<ChartInfo> getchartChangeInfo(String[] urlhead, String target, long time, String timeformat) {
        long nowTime = new Date().getTime();
        long startTime = nowTime + time;
        String urlMethod = "GET";
        // String urlheadLog="/ilumi_transctionlog_";

//		String[] urlheadLog = { "/ilumi_transactionlog_", "ilumi_payment_" };
        String urlend = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        String urlLog = getIndices(nowTime, urlhead, urlend, df);

        SearchRequest searchRequest = new SearchRequest(urlLog);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.constantScoreQuery(QueryBuilders.rangeQuery("@timestamp").gte(startTime).lte(nowTime)));

        DateHistogramAggregationBuilder aggregation = AggregationBuilders.dateHistogram("NAME");
        aggregation.interval(3600000).timeZone(DateTimeZone.getDefault()).format("yyyy-MM-dd HH:mm:ss").field("@timestamp");        //一小时取一次数据
        aggregation.subAggregation(AggregationBuilders.sum("money_change").field("money_change_no"));
        aggregation.subAggregation(AggregationBuilders.sum("diamone_change").field("diamond_change_no"));
        aggregation.subAggregation(AggregationBuilders.sum("ticket_change").field("ticket_change_no"));

        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = new SearchResponse();
        try {
            searchResponse = client.search(searchRequest);
        } catch (IOException e) {
            log.error("查询失败！", e);
        }

        List<ChartInfo> list = paseChartJson(searchResponse, target);

        return list;

    }

    public static List<RankInfo> getRankInfo(String urlMethod, String urlhead, String urlend, String target, long time, String order) {
        long nowTime = new Date().getTime();
        long startTime = nowTime + time;
        String url = getIndices(startTime, urlhead, urlend, new SimpleDateFormat("yyyy-MM"));

        SearchRequest searchRequest = new SearchRequest(url);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.constantScoreQuery(QueryBuilders.rangeQuery("@timestamp").gte(startTime).lte(nowTime)));

        TermsAggregationBuilder aggregation = AggregationBuilders.terms("sum");
        aggregation.field("uuid");
        aggregation.showTermDocCountError(true);

        aggregation.order(BucketOrder.aggregation("money_sum",order.equals("asc")?true:false));
        aggregation.shardSize(100000);

        aggregation.subAggregation(AggregationBuilders.sum("money_sum").field(target));

        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = new SearchResponse();
        try {
            searchResponse = client.search(searchRequest);

        } catch (IOException e) {
            log.error("查询失败！", e);
        }

        return getRankInfo(searchResponse, urlMethod, url);
    }

    public static List<RankInfo> getRechargeTimesRequest(String urlMethod, String urlhead, String urlend,  long time, String order) {
        long nowTime = new Date().getTime();
        long startTime = nowTime + time;
        String url = getIndices(startTime, urlhead, urlend, new SimpleDateFormat("yyyy-MM"));

        SearchRequest searchRequest = new SearchRequest(url);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.constantScoreQuery(QueryBuilders.rangeQuery("@timestamp").gte(startTime).lte(nowTime)));

        TermsAggregationBuilder aggregation = AggregationBuilders.terms("sum");
        aggregation.field("uuid");
        aggregation.showTermDocCountError(true);
        aggregation.shardSize(100000);
        aggregation.order(BucketOrder.aggregation("money_sum",order.equals("asc")?true:false));
        aggregation.subAggregation(AggregationBuilders.count("money_sum").field("uuid"));

        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = new SearchResponse();

        log.info("searchRequest = " + searchRequest);
        try {
            searchResponse = client.search(searchRequest);

        } catch (IOException e) {
            log.error("查询失败！", e);
        }

        return paseRechargeCountJson(searchResponse);

    }

    // 重载
    public static List<RankInfo> getRankInfo(SearchResponse searchResponse, String urlMethod, String url) {
        try {
//			Response response = ELKUtils.getData(searchResponse, urlMethod, url);
            List<RankInfo> list = ELKUtils.paseRankJson(searchResponse);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 链接ELK
     *
     * @param jsonString
     * @param method
     * @param url
     * @return
     * @throws IOException 是否该抛异常
     */
    public static Response getData(String jsonString, String method, String url) {
        try {
            HttpHost httpHost = new HttpHost("10.105.92.212", 9200, "http");
            RestClient restClient = RestClient.builder(httpHost).build();
            Map<String, String> params = Collections.singletonMap("pretty", "true");
            HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
            Response response = restClient.performRequest(method, url, params, entity);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String getUrl4SeletedTime(long startTime, long endTime, String[] urlhead, String urlend) {

        String url = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        String stime = df.format(startTime);
        String etime = df.format(endTime);

        if (stime.equals(etime)) {
            String temp = "";
            for (int i = 0; i < urlhead.length; i++) {
                if (i == urlhead.length - 1) {
                    temp += urlhead[i] + stime;
                } else {
                    temp += urlhead[i] + stime + ",";
                }
                url = temp + urlend;
                return url;
            }
        } else {
            // 判断相差的日期
            try {
                int count = DateUtils.countMonths(stime, etime, "yyyy-MM");
                String[] urldate = new String[count + 1];

                for (int i = 0; i <= count; i++) {
                    // 开始时间改变月份
                    urldate[i] = stime;
                    stime = df.format(DateUtils.changeMonth(df.parse(stime).getTime(), 1));
                }
                // 拼装url
                String temp = "";
                boolean flag = false;
                for (int i = 0; i < urlhead.length; i++) {

                    if (i == urlhead.length - 1) {
                        flag = true;
                    }
                    for (int n = 0; n < urldate.length; n++) {

                        if (flag) {
                            if (n == urldate.length - 1) {
                                temp += urlhead[i] + urldate[n];
                            } else {
                                temp += urlhead[i] + urldate[n] + ",";
                            }
                        } else {
                            temp += urlhead[i] + urldate[n] + ",";
                        }
                    }
                    url = temp + urlend;

                }
                return url;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    /**
     * 获取解析后的url 天数为两天 月份可能为两月
     *
     * @param startTime
     * @return 年月日
     */
    public static String getIndices(long startTime, String urlhead, String urlend, DateFormat df) {
        String url = null;
        String[] urlarr = getUrlDate(startTime, df);
        if (urlarr[1] != null) {
            url = urlhead + urlarr[0] + "," + urlhead + urlarr[1] + urlend;
        } else {
            url = urlhead + urlarr[0] + urlend;
        }
        return url;
    }

    /**
     * 重载 多个url库
     *
     * @param startTime
     * @param urlheads
     * @param urlheads
     * @param urlend
     * @return
     */
    public static String getIndices(long startTime, String[] urlheads, String urlend, DateFormat dateformat) {
        String url = "";

        for (int i = 0; i < urlheads.length; i++) {
            String[] urlarr = getUrlDate(startTime, dateformat);
            if (urlarr[1] != null) {
                // 判断结尾
                url += urlheads[i] + urlarr[0] + "," + urlheads[i] + urlarr[1];
                if (i != urlheads.length - 1) {
                    url += ",";
                }
            } else {
                // 判断结尾
                url += urlheads[i] + urlarr[0];
                if (i != urlheads.length - 1) {
                    url += ",";
                }
            }
        }
        url += urlend;
        return url;
    }

    /**
     * 会返回三天的日期
     *
     * @param startTime
     * @param urlheads
     * @param urlend
     * @return
     */
    public static String getUrlThreeDay(long startTime, String urlheads, String urlend) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String nowDay = df.format(startTime);
        String lastDay = df.format(DateUtils.changeHour(startTime, -24));
        String nextDay = df.format(DateUtils.changeHour(startTime, +24));
        String url = "";
        url += urlheads + lastDay + "," + urlheads + nowDay + "," + urlheads + nextDay + urlend;
        return url;
    }

    /**
     * 优化查询 判断上一天是否是上一个月 日期为天直接返回上一天
     *
     * @param startTime
     * @return 年月日
     */
    private static String[] getUrlDate(long startTime, DateFormat df) {
        String end = df.format(new Date(startTime));
        String start = df.format(new Date(DateUtils.changeHour(startTime, -24)));
        String[] result = new String[2];

        if (!end.equals(start)) {
            result[0] = start;
            result[1] = end;
            return result;
        }
        result[0] = end;
        return result;
    }

    /**
     * 解析图表Json
     *
     * @param searchResponse
     * @return CoinChartInfo
     * <p>
     * 解析： aggregations获取 arr
     */
    private static List<ChartInfo> paseChartJson(SearchResponse searchResponse, String target) {
        try {
//			String jsonstring = EntityUtils.toString(searchResponse.toString());

            Aggregations aggregations = searchResponse.getAggregations();
            Map<String, Aggregation> map = aggregations.asMap();
            ParsedDateHistogram c = aggregations.get("NAME");

            List<ChartInfo> list = new ArrayList<>();
            for (Histogram.Bucket entity : c.getBuckets()) {
                DateTime key = (DateTime) entity.getKey();
                Sum sum = entity.getAggregations().get(target);
                double value = sum.getValue();
                list.add(new ChartInfo(key, new Double(value).longValue()));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析日期范围类 数据
     *
     * @param response
     * @param target
     * @return
     */

    public static Map<Long,Map<String, Long>> paseDailyResponse(Response response, String target) {
        try {
            String jsonstring = EntityUtils.toString(response.getEntity());
//			System.out.println("***************Response Json***************");
//			System.out.println(jsonstring);
            JSONObject jsonObj = JSON.parseObject(jsonstring);
            JSONObject ag = jsonObj.getJSONObject("aggregations");
            JSONObject na = ag.getJSONObject("NAME");
            JSONArray arry = na.getJSONArray("buckets");

            Map<Long,Map<String, Long>> mmap = new HashMap<Long,Map<String, Long>>();
            for (int i = 0, len = arry.size(); i < len; i++) {
                JSONObject temp = arry.getJSONObject(i);
                //日期
                Long time = temp.getLong("key");
                JSONObject sum = temp.getJSONObject("sum");
                JSONArray arr = sum.getJSONArray("buckets");

                Map<String, Long> map = new HashMap<String, Long>();
                for(int j = 0, length = arr.size(); j < length; j++) {
                    JSONObject te = arr.getJSONObject(j);
                    // String time = temp.getString("key_as_string");
                    String key = te.getString("key");
                    Long value = te.getJSONObject(target + "_sum").getLong("value");
                    int isErro = te.getInteger("doc_count_error_upper_bound");
                    if (isErro == 1) {
                        log.error("key:" + key + "|| value:" + value + "||" + isErro);
                    }
                    /**
                     * 打日志 如果isErro为1 出错
                     */
                    map.put(key, value);

                }
                mmap.put(time, map);

            }
            return mmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 解析排名Json 解析：aggregations
    private static List<RankInfo> paseRankJson(SearchResponse searchResponse) {
        try {
            Aggregations aggregations = searchResponse.getAggregations();
            ParsedLongTerms terms = aggregations.get("sum");
            int i = 0;
            List<RankInfo> list = new ArrayList<>();
            for (Terms.Bucket entity : terms.getBuckets()) {

                Long uuid = (Long) entity.getKey();            //uuid
                Sum sum = entity.getAggregations().get("money_sum");

                double value = sum.getValue();                //num
                Long isErro = entity.getDocCountError();

                /**
                 * 打日志 如果isErro为1 出错
                 */
                RankInfo rank = new RankInfo();
                rank.setUuid(uuid);
                rank.setChange(value);
                rank.setIsErro(isErro.intValue());

                // 查数据库再封装rank
                Record userTemp = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.player.getPlayerByUuid"), uuid);
                rank.setRank(i + 1);
                i++;
                if (userTemp != null) {
                    rank.setNickname(userTemp.getStr("nickname"));
                    rank.setShowid(userTemp.getStr("showid"));
                }
                list.add(rank);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 解析排名Json 解析：aggregations
    private static List<RankInfo> paseRechargeCountJson(SearchResponse searchResponse) {
        try {
            Aggregations aggregations = searchResponse.getAggregations();
            ParsedLongTerms terms = aggregations.get("sum");
            int i = 0;
            List<RankInfo> list = new ArrayList<>();
            for (Terms.Bucket entity : terms.getBuckets()) {

                Long uuid = (Long) entity.getKey();            //uuid
                ParsedValueCount valueCount = entity.getAggregations().get("money_sum");

                double value = valueCount.getValue();                //num
                Long isErro = entity.getDocCountError();

                /**
                 * 打日志 如果isErro为1 出错
                 */
                RankInfo rank = new RankInfo();
                rank.setUuid(uuid);
                rank.setChange(value);
                rank.setIsErro(isErro.intValue());

                // 查数据库再封装rank
                Record userTemp = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.player.getPlayerByUuid"), uuid);
                rank.setRank(i + 1);
                i++;
                if (userTemp != null) {
                    rank.setNickname(userTemp.getStr("nickname"));
                    rank.setShowid(userTemp.getStr("showid"));
                }
                list.add(rank);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
