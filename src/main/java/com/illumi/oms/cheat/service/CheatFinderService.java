package com.illumi.oms.cheat.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.illumi.oms.common.utils.StringUtil;
import com.jfinal.ext.plugin.config.ConfigKit;
import com.jfinal.log.Logger;


public class CheatFinderService {

	private final static String R_SCRIPT_CMD = "Rscript ";

	private static final Logger logger = Logger.getLogger(CheatFinderService.class);

	public static String findCheatByIds(long uuid, String[] idArr) {
		logger.info("Prepare for | Pre041 | " + uuid + " | " + StringUtil.join(idArr));
		try {
			String cmd = R_SCRIPT_CMD + ConfigKit.getStr("CheatFinderScriptPath") + " "
					+ StringUtil.join(idArr, " ");
			Process ps = Runtime.getRuntime().exec(cmd);
			ps.waitFor();
			BufferedReader breader = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			StringBuffer strb = new StringBuffer();
			String line;
			while ((line = breader.readLine()) != null) {
				strb.append(line);
			}
			logger.info("Info041 | " + " | " + uuid + " | Response : " + strb.toString());
			return strb.toString();
		} catch (IOException | InterruptedException e) {
			logger.warn("Err041 | " + " | " + uuid + " | Error occur while call Method findCheatByIds, and error is "
					+ e.getClass() + ":" + e.getMessage());
			return null;
		}
	}
}
