package com.illumi.oms.data.model;


import org.joda.time.DateTime;

public class ChartInfo {
    
	//private List<>
	private DateTime date;
	private Long num;
	public ChartInfo(DateTime date, Long num) {
		super();
		this.date = date;
		this.num = num;
	}
	public DateTime getDate() {
		return date;
	}
	
	/*转换为24小时
	private String parseDate() {
		try {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
    
        Date data = simpleDateFormat.parse(date);
        long time = data.getTime();
 
     
        simpleDateFormat = new SimpleDateFormat("HH");
        String result = simpleDateFormat.format(time);
        
        return result;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	*/
	public void setDate(DateTime date) {
		this.date = date;
	}
	public Long getNum() {
		return num;
	}
	public void setNum(Long num) {
		this.num = num;
	}
}
