package com.illumi.oms.chart.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PhantomjsUtil {

	/**
	 * @author sea.zeng
	 * @param infile
	 *            String infile = " d:/phantomjs/capture.js " ;
	 * @param url
	 *            String url = "
	 *            http://ip:8080/xxx/router.do?service=xxxReport.xxxReportChart
	 *            " ;
	 * @param outfile
	 *            String outfile = " D:/phantomjs/testReportChart"+new
	 *            Date().getTime()+"_"+new Random().nextInt(9999)+".png" ;
	 * @return String fail success
	 * @throws IOException
	 */
	public static String initHighchartImage(String infile, String url, String outfile) throws IOException {
		String shell = " /Users/illuminate/Downloads/phantomjs-2.1.1-macosx/bin/phantomjs ";
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec(shell + " " + infile + " " + url + " " + outfile);
		InputStream is = p.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer sbf = new StringBuffer();
		String tmp = "";
		while ((tmp = br.readLine()) != null) {
			sbf.append(tmp);
		}
		System.out.println("@@@"+sbf.toString());
		return sbf.toString();
	}
}
