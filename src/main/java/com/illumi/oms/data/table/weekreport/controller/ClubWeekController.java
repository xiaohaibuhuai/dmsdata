package com.illumi.oms.data.table.weekreport.controller;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.model.ExcelTableSheet;
import com.illumi.oms.data.utils.ExcelController;
import com.illumi.oms.data.utils.ExcelUtil;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 俱乐部周报Controller
 * */
@ControllerBind(controllerKey = "/data/table/weekreport/clubweek", viewPath = UrlConfig.DATA_TAB_WEEKREPORT)
public class ClubWeekController extends ExcelController{

	private static final Logger log = Logger.getLogger(ClubWeekController.class);


	/**
	 * 俱乐部周报-页面显示
	 * @author Zhang Ao
	 * @date 2016-12-08 15：41：00
	 * */
	public void clubDataQuery() {
		log.info("开始处理俱乐部周报页面展示请求");
		String dateStart = getPara("dateStart");
		String dateEnd = getPara("dateEnd");
		Integer rows = Integer.parseInt(getPara("rows"));
		Integer page = Integer.parseInt(getPara("page"));

		log.info("dateStart = " + dateStart);
		log.info("dateEnd = " + dateEnd);

		List<Record> list = getDataList(dateStart,dateEnd,rows,page);
		Long total = getDataAmount(dateStart,dateEnd);
		DataGrid<Record> data = new DataGrid<>();
		data.total=total.intValue();
		data.setRows(list);
		data.page=page;
		renderJson(data);
	}

	/**
	 * 俱乐部周报-导出excel
	 * @author Zhang Ao
	 * @date 2016-12-11 11:09:33
	 * */
	public void excelDown() {
		log.info("开始处理俱乐部周报报表导出请求");
		String dateStart = getPara("dateStart");
		String dateEnd = getPara("dateEnd");

		//数据源
		List<Record> dataList = getDataList(dateStart,dateEnd,0,0);

		//1.处理俱乐部上桌等级汇总表sheet
		log.info("1.处理俱乐部上桌等级汇总表sheet");

		//excel表头
		String[]  clubWeekHead = {"俱乐部id","俱乐部名字","总上桌人次","总上桌人次环比上周","俱乐部局上桌人次","联盟局上桌人次","现有人数/最大人数","现有人数较上周变化值","当前经验","当前等级",
				"经验变化值","等级变化值","主机常用登陆地址","所属联盟名称"};
		String clubWeekTitle = "俱乐部上桌等级";
		Map<String, String> clubWeekTransformMap = getClubWeekTransforMap();

		ExcelTableSheet sheet1 = new ExcelTableSheet(clubWeekTitle,clubWeekHead,clubWeekTitle,dataList,clubWeekTransformMap);

		//2.处理俱乐部伙牌sheet
		log.info("2.处理俱乐部伙牌sheet");

		//excel表头
		String[] clubCheatHead = {"俱乐部id","俱乐部名字","总上桌人次","总上桌人次环比上周","总伙牌人次","总伙牌人次占比","总伙牌人次环比上周","俱乐部局上桌人次","俱乐部局伙牌人次","联盟局上桌人次",
				"联盟局伙牌人次","现有人数/最大人数","主机常用登陆地址","所属联盟名称"};

		String clubCheatTitle = "俱乐部伙牌";
		Map<String, String> clubCheatTransformMap = getClubCheatTransforMap();

		ExcelTableSheet sheet2 = new ExcelTableSheet(clubCheatTitle,clubCheatHead,clubCheatTitle,dataList,clubCheatTransformMap);

		XSSFWorkbook xs = ExcelUtil.getXSSFWorkbook(sheet1,sheet2);
		renderNewExcel(xs, "俱乐部周报");
	}

	/**
	 * 数据库中查询数据
	 * @author Zhang Ao
	 * @date 2016-12-08 15：46：00
	 * @param
	 * */
	private List<Record> getDataList(String dateStart,String dateEnd,Integer rows,Integer page) {
		if(rows == 0 && page == 0){
			//不包含分页查询
			log.info("不分页查询sql");
			return Db.use(Consts.DB_POKERDATA).find(SqlKit.sql("data.clubWeek.getClubLevelWeeklyWithoutPage"), new Object[]{dateStart, dateEnd});
		}else {
			//分页查询
			log.info("分页查询sql");
			int limitStart = (page - 1) * rows;
			return Db.use(Consts.DB_POKERDATA).find(SqlKit.sql("data.clubWeek.getClubLevelWeeklyWithPage"), new Object[]{dateStart, dateEnd, limitStart, rows});
		}
	}

	/**
	 * 查询数据库中数据数量
	 * @author Zhang Ao
	 * @date 2016-12-08 17:18:30
	 * */
	private Long getDataAmount(String dateStart,String dateEnd){
		return Db.use(Consts.DB_POKERDATA).queryLong(SqlKit.sql("data.clubWeek.getClubLevelWeeklyCount"),new Object[] {dateStart,dateEnd});
	}

	/**
	 * 获取俱乐部上桌等级汇总表映射关系
	 * @author Zhang Ao
	 * */
	private  Map<String,String> getClubWeekTransforMap(){
		Map<String, String> transformMap = new HashMap<String, String>();
		transformMap.put("俱乐部id","clubId");
		transformMap.put("俱乐部名字","clubName");
		transformMap.put("总上桌人次","roomidNum");
		transformMap.put("总上桌人次环比上周","numWeek");
		transformMap.put("俱乐部局上桌人次","clubNum");
		transformMap.put("联盟局上桌人次","leagNum");
		transformMap.put("现有人数/最大人数","person");
		transformMap.put("现有人数较上周变化值","personChange");
		transformMap.put("当前经验","exp");
		transformMap.put("当前等级","expLevel");
		transformMap.put("经验变化值","expChan");
		transformMap.put("等级变化值","levelChan");
		transformMap.put("主机常用登陆地址","Host");
		transformMap.put("所属联盟名称","leaName");
		return  transformMap;
	}

	/**
	 * 获取俱乐部伙牌映射关系
	 * @author Zhang Ao
	 * */
	private  Map<String,String> getClubCheatTransforMap(){
		Map<String, String> transformMap = new HashMap<String, String>();
		transformMap.put("俱乐部id","clubId");
		transformMap.put("俱乐部名字","clubName");
		transformMap.put("总上桌人次","roomidNum");
		transformMap.put("总上桌人次环比上周","numWeek");
		transformMap.put("总伙牌人次","cheatTimes");
		transformMap.put("总伙牌人次占比","timesPer");
		transformMap.put("总伙牌人次环比上周","timesWeek");
		transformMap.put("俱乐部局上桌人次","clubNum");
		transformMap.put("俱乐部局伙牌人次","clubTimes");
		transformMap.put("联盟局上桌人次","leagNum");
		transformMap.put("联盟局伙牌人次","leagTimes");
		transformMap.put("现有人数/最大人数","person");
		transformMap.put("主机常用登陆地址","Host");
		transformMap.put("所属联盟名称","leaName");
		return  transformMap;
	}
}
