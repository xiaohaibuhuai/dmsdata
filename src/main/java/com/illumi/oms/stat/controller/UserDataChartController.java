package com.illumi.oms.stat.controller;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.common.utils.StringUtil;
import com.illumi.oms.model.UserData;
import com.illumi.oms.system.model.Chart;
import com.illumi.oms.system.model.DataChart;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jayqqaa12.jbase.util.DateUtil;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

@ControllerBind(controllerKey = "/stat/userdatachart", viewPath = UrlConfig.STAT)
public class UserDataChartController extends EasyuiController<Record> {
	/**
	 * 
	 * 
	 */
	private static final Logger log = Logger.getLogger(UserDataChartController.class);
	public void sum() {
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		DataChart chart = new DataChart();
		Integer num = Db.queryLong(SqlKit.sql("stat.userdata.getTotalNum"), getDateParas()).intValue();

		Integer countryNum = num;
		if (getPara("country").equals("1")) {
			countryNum = Db.queryLong(SqlKit.sql("stat.userdata.getChinaTotalNum"), getDateParas()).intValue();
		} else if (getPara("country").equals("2")) {
			countryNum = Db.queryLong(SqlKit.sql("stat.userdata.getGATTotalNum"), getDateParas()).intValue();
		} else if (getPara("country").equals("3")) {
			countryNum = Db.queryLong(SqlKit.sql("stat.userdata.getForeignTotalNum"), getDateParas()).intValue();
		}
		chart.setTotalNum(num);
		chart.setCountryNum(countryNum);
		chart.setDatePara(getDateDisplay());
		List<UserData> provincelList = null;
		if (getPara("country").equals("1")) {
			provincelList = UserData.dao.find(SqlKit.sql("stat.userdata.getChinaProvinceDataByNum"),
					getProvinceParas());
		} else if (getPara("country").equals("2")) {
			provincelList = UserData.dao.find(SqlKit.sql("stat.userdata.getGATProvinceDataByNum"), getProvinceParas());
		} else if (getPara("country").equals("3")) {
			provincelList = UserData.dao.find(SqlKit.sql("stat.userdata.getForeignCountryDataByNum"),
					getProvinceParas());
		}
		List<String> categories = new ArrayList<String>();
		int i = 0;
		if (getPara("country").equals("3")) {
			for (UserData provincedata : provincelList) {
				String country = provincedata.getStr("country");
				Integer fcountryNum = provincedata.getLong("num").intValue();
				Integer fcountryRealnum = fcountryNum;
				countryNum -= fcountryNum;
				categories.add(country);
				List<UserData> provinceList = UserData.dao.find(SqlKit.sql("stat.userdata.getForeignProvinceDataByNum"),getCityParas(country));

				List<String> provinceNameList = new ArrayList<String>();
				List<Integer> provinceNumList = new ArrayList<Integer>();
				for (UserData fprovincedata : provinceList) {
					String fprovince = fprovincedata.getStr("province");
					Integer fprovinceNum = fprovincedata.getLong("num").intValue();

					provinceNameList.add(country + ":" + fprovince);
					provinceNumList.add(fprovinceNum);
					fcountryNum -= fprovinceNum;
				}
				if (fcountryNum > 0) {
					provinceNameList.add(country + ":" + "其他");
					provinceNumList.add(fcountryNum);
				}
				chart.setData(fcountryRealnum, i++ + "", country, provinceNameList, provinceNumList);
			}
			if (countryNum > 0) {
				categories.add("其他");
				List<String> cityNameList = new ArrayList<String>();
				List<Integer> cityNumList = new ArrayList<Integer>();

				cityNameList.add("其他:" + "其他");
				cityNumList.add(countryNum);

				chart.setData(countryNum, i + "", "其他", cityNameList, cityNumList);
			}
		} else {
			for (UserData provincedata : provincelList) {
				String province = provincedata.getStr("province");
				Integer provinceNum = provincedata.getLong("num").intValue();
				Integer provinceRealnum = provinceNum;
				countryNum -= provinceNum;
				categories.add(province);
				List<UserData> cityList = UserData.dao.find(SqlKit.sql("stat.userdata.getCityDataByNum"),
						getCityParas(province));

				List<String> cityNameList = new ArrayList<String>();
				List<Integer> cityNumList = new ArrayList<Integer>();
				for (UserData citydata : cityList) {
					String city = citydata.getStr("city");
					Integer cityNum = citydata.getLong("num").intValue();

					cityNameList.add(province + ":" + city);
					cityNumList.add(cityNum);
					provinceNum -= cityNum;
				}
				if (provinceNum > 0) {
					cityNameList.add(province + ":" + "其他");
					cityNumList.add(provinceNum);
				}
				chart.setData(provinceRealnum, i++ + "", province, cityNameList, cityNumList);
			}
			if (countryNum > 0) {
				categories.add("其他");
				List<String> cityNameList = new ArrayList<String>();
				List<Integer> cityNumList = new ArrayList<Integer>();

				cityNameList.add("其他:" + "其他");
				cityNumList.add(countryNum);

				chart.setData(countryNum, i + "", "其他", cityNameList, cityNumList);
			}
		}
		chart.setCategories(categories);
		renderGson(chart);
	}

	private Object[] getProvinceParas() {

		String provincePara = getPara("province");

		if (StrKit.isBlank(provincePara)) {
			provincePara = "500";
		}
		Object[] obj = { getDateParas()[0], getDateParas()[1], provincePara };

		return obj;
	}

	private Object[] getCityParas(String province) {

		String cityPara = getPara("city");
		if (StrKit.isBlank(cityPara)) {
			cityPara = "100";
		}
		Object[] obj = { province, getDateParas()[0], getDateParas()[1], cityPara };
		return obj;

	}

	private Object[] getDateParas() {
		String dateStart = "";
		String dateEnd = "";
		if (StrKit.isBlank(getPara("dateStart")) && StrKit.isBlank(getPara("dateEnd"))) {
			dateStart = DateTime.now().minusDays(1).toString("yyyy-MM-dd");
			dateEnd = DateTime.now().minusDays(1).toString("yyyy-MM-dd");

		} else if (StrKit.isBlank(getPara("dateStart")) && !StrKit.isBlank(getPara("dateEnd"))) {
			dateStart = DateTime.now().minusDays(1).toString("yyyy-MM-dd");
			dateEnd = getPara("dateEnd");

		} else if (!StrKit.isBlank(getPara("dateStart")) && StrKit.isBlank(getPara("dateEnd"))) {
			dateStart = getPara("dateStart");
			dateEnd = DateTime.now().minusDays(1).toString("yyyy-MM-dd");
		} else {
			dateStart = getPara("dateStart");
			dateEnd = getPara("dateEnd");
		}
		Object[] obj = { dateStart, dateEnd };
		return obj;
	}

	private String getDateDisplay() {
		Object[] obj = getDateParas();
		if (((String) obj[0]).equals((String) obj[1])) {
			return (String) obj[1];
		} else {
			return (String) obj[0] + "－" + (String) obj[1];
		}
	}

	public void provinceTrend() {
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		Chart chart = new Chart();
		List<UserData> provincelist = UserData.dao.find(SqlKit.sql("stat.userdata.getProvinceTrend"),
				DateTime.now().minusDays(31).toString("yyyy-MM-dd"),
				DateTime.now().minusDays(1).toString("yyyy-MM-dd"));

		for (UserData ud : provincelist) {
			boolean flag = true;
			if (flag) {
				flag = false;
				List<UserData> firstprovincelist = UserData.dao.find(SqlKit.sql("stat.userdata.getProvinceTrendData"),
						ud.getStr("province"), DateTime.now().minusDays(31).toString("yyyy-MM-dd"),
						DateTime.now().minusDays(1).toString("yyyy-MM-dd"));
				List<Integer> Series = new ArrayList<Integer>();
				for (UserData data : firstprovincelist) {
					chart.categories.add(DateUtil.format(data.getDate("date"), "MM-dd"));
					Series.add(data.getLong("num").intValue());
				}
				chart.setSeriesDate(ud.getStr("province"), "spline", Series);
			} else {
				List<UserData> provinceData = UserData.dao.find(SqlKit.sql("stat.userdata.getProvinceTrendData"),
						ud.getStr("province"), DateTime.now().minusDays(31).toString("yyyy-MM-dd"),
						DateTime.now().minusDays(1).toString("yyyy-MM-dd"));
				List<Integer> Series = new ArrayList<Integer>();
				for (UserData data : provinceData) {
					Series.add(data.getLong("num").intValue());
				}
				chart.setSeriesDate(ud.getStr("province"), "spline", Series);
			}
		}
		renderGson(chart);
	}

	public void cityTrend() {
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		Chart chart = new Chart();
		List<UserData> citylist = UserData.dao.find(SqlKit.sql("stat.userdata.getCityTrend"),
				DateTime.now().minusDays(31).toString("yyyy-MM-dd"),
				DateTime.now().minusDays(1).toString("yyyy-MM-dd"));

		for (UserData ud : citylist) {
			boolean flag = true;
			if (flag) {
				flag = false;
				List<UserData> firstcitylist = UserData.dao.find(SqlKit.sql("stat.userdata.getCityTrendData"),
						ud.getStr("city"), DateTime.now().minusDays(31).toString("yyyy-MM-dd"),
						DateTime.now().minusDays(1).toString("yyyy-MM-dd"));
				List<Integer> Series = new ArrayList<Integer>();
				for (UserData data : firstcitylist) {
					chart.categories.add(DateUtil.format(data.getDate("date"), "MM-dd"));
					Series.add(data.getLong("num").intValue());
				}
				chart.setSeriesDate(ud.getStr("city"), "spline", Series);
			} else {
				List<UserData> provinceData = UserData.dao.find(SqlKit.sql("stat.userdata.getCityTrendData"),
						ud.getStr("city"), DateTime.now().minusDays(31).toString("yyyy-MM-dd"),
						DateTime.now().minusDays(1).toString("yyyy-MM-dd"));
				List<Integer> Series = new ArrayList<Integer>();
				for (UserData data : provinceData) {
					Series.add(data.getLong("num").intValue());
				}
				chart.setSeriesDate(ud.getStr("city"), "spline", Series);
			}
		}
		renderGson(chart);
	}
}
