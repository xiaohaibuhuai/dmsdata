package com.illumi.oms;

import com.illumi.oms.task.DailyReportJobService;
import org.beetl.core.GroupTemplate;
import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.illumi.oms.plugin.spring.SpringPlugin;
import com.illumi.oms.shiro.SessionHandler;
import com.jayqqaa12.jbase.jfinal.ext.ShiroExt;
import com.jayqqaa12.jbase.jfinal.ext.xss.XssHandler;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.handler.FakeStaticHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.ext.plugin.config.ConfigKit;
import com.jfinal.ext.plugin.config.ConfigPlugin;
import com.jfinal.ext.plugin.quartz.QuartzPlugin;
import com.jfinal.ext.plugin.shiro.ShiroInterceptor;
import com.jfinal.ext.plugin.shiro.ShiroPlugin;
import com.jfinal.ext.plugin.sqlinxml.SqlInXmlPlugin;
import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.SqlReporter;
import com.jfinal.plugin.activerecord.cache.EhCache;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;


/**
 * API引导式配置
 */
public class MyConfig extends JFinalConfig
{
	public boolean OPEN_SHIRO = true;
	public boolean OPEN_ADV =true; // 可设置隐藏  项目介绍等
	
	private Routes routes;
	private boolean isDev = isDevMode();

	private boolean isDevMode()
	{
		String osName = System.getProperty("os.name");
		return osName.indexOf("Windows") != -1;
	}

	/**
	 * 配置常量
	 */
	public void configConstant(Constants me)
	{
		me.setError404View("/page/error/404.html");
		me.setError401View("/page/error/401.html");
		me.setError403View("/page/error/403.html");
		me.setError500View("/page/error/500.html");
		
//		new ConfigPlugin("config.txt").reload(false).start();
		new ConfigPlugin("dev.txt").reload(false).start();
//		new ConfigPlugin("test.txt").reload(false).start();
		me.setDevMode(isDev);
		// me.setViewType(ViewType.OTHER);
		
		// beel
		me.setMainRenderFactory(new BeetlRenderFactory());
		GroupTemplate gt = BeetlRenderFactory.groupTemplate;
		gt.registerFunctionPackage("so", new ShiroExt());
	}

	/**
	 * 配置路由
	 */
	public void configRoute(Routes me)
	{
		this.routes = me;
		// 自动扫描 建议用注解
		me.add(new AutoBindRoutes(false) );
	}

	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me)
	{
	
		// 配置Druid 数据库连接池插件
		DruidPlugin dbPlugin = new DruidPlugin(ConfigKit.getStr("jdbcUrl"), ConfigKit.getStr("user"), ConfigKit.getStr("password"));
		// 设置 状态监听与 sql防御
		WallFilter wall = new WallFilter();
		wall.setDbType(ConfigKit.getStr("dbType"));
		dbPlugin.addFilter(wall);
		dbPlugin.addFilter(new StatFilter());

		me.add(dbPlugin);
		// add EhCache
		me.add(new EhCachePlugin());
		// add sql xml plugin
		me.add(new SqlInXmlPlugin());
		// add shrio
		if (OPEN_SHIRO) me.add(new ShiroPlugin(this.routes));

		// 配置AutoTableBindPlugin插件
		AutoTableBindPlugin atbp = new AutoTableBindPlugin("dbconfig",dbPlugin);
		if (isDev) atbp.setShowSql(true);
		atbp.scanPackages("com.illumi.oms.common.index","com.illumi.oms.model","com.illumi.oms.system.model");
		atbp.autoScan(false);
		me.add(atbp);
		// sql记录
		SqlReporter.setLogger(true);
		// 配置只读库
		DruidPlugin pokerDbPlugin = new DruidPlugin(ConfigKit.getStr("poker.jdbcUrl"), ConfigKit.getStr("poker.user"), ConfigKit.getStr("poker.password"));
		me.add(pokerDbPlugin);
		ActiveRecordPlugin arpMysql = new ActiveRecordPlugin("pokerdb", pokerDbPlugin);
		me.add(arpMysql);
		arpMysql.setCache(new EhCache());
		//配置拆分库
		DruidPlugin pokerDbPlugin2 = new DruidPlugin(ConfigKit.getStr("poker2.jdbcUrl"), ConfigKit.getStr("poker2.user"), ConfigKit.getStr("poker2.password"));
		me.add(pokerDbPlugin2);
		ActiveRecordPlugin arpMysql2 = new ActiveRecordPlugin("pokerdb2", pokerDbPlugin2);
		me.add(arpMysql2);
		arpMysql2.setCache(new EhCache());
	
        
        
        //data查询库
        DruidPlugin pokerDataPlugin = new DruidPlugin(ConfigKit.getStr("pokerdata.jdbcUrl"), ConfigKit.getStr("pokerdata.user"), ConfigKit.getStr("pokerdata.password"));
		me.add(pokerDataPlugin);
		ActiveRecordPlugin arpMysql4 = new ActiveRecordPlugin("pokerdata", pokerDataPlugin);
		me.add(arpMysql4);
		arpMysql4.setCache(new EhCache());
	
		
		AutoTableBindPlugin proatbp = new AutoTableBindPlugin("pokerDataConfig",pokerDataPlugin);
		if (isDev) proatbp.setShowSql(true);
		proatbp.scanPackages("com.illumi.oms.data.model");
		proatbp.autoScan(false);
		me.add(proatbp);
		
		//配置定时任务插件
		QuartzPlugin quartzPlugin = new QuartzPlugin();
		me.add(quartzPlugin);
		//Spring
//		me.add(new SpringPlugin());
		//me.add(new SpringPlugin("//home/dyp/data/git/mytest/oms/target/oms-web/WEB-INF/classes/applicationContext.xml"));
	}

	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me)
	{
		// shiro权限拦截器配置
		if (OPEN_SHIRO) me.add(new ShiroInterceptor());
		if (OPEN_SHIRO) me.add(new com.illumi.oms.shiro.ShiroInterceptor());

		// 让 模版 可以使用session
		me.add(new SessionInViewInterceptor());
		
	}

	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me)
	{
		// 计算每个page 运行时间
		// me.add(new RenderingTimeHandler());

		// xss 过滤
		me.add(new XssHandler("s"));
		// 伪静态处理
		me.add(new FakeStaticHandler());
		// 去掉 jsessionid 防止找不到action
		me.add(new SessionHandler());
		me.add(new DruidStatViewHandler("/druid"));

		me.add(new ContextPathHandler());
	}
	
	/**
     * 启动后执行任务
     */
	@Override
	public void afterJFinalStart() {
//		new DailyReportJobService().defineExcuteByDay(System.currentTimeMillis(),20); ;
	}

	/**
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args)
	{
		JFinal.start("src/main/webapp", 2221, "/", 5);
	}

}
