package com.illumi.oms.service;

import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;

import com.jayqqaa12.jbase.util.Eml;
import com.jayqqaa12.jbase.util.EmlMixed;
import com.jayqqaa12.jbase.util.Validate;
import com.jfinal.ext.plugin.config.ConfigKit;

public class EmailService
{
//	private static final String EMAIL_SERVER = "smtp.exmail.qq.com";
//	private static final String EMAIL_USER = "monitor@paiyou.me";
//	private static final String EMAIL_PASSWD = "Asd1123";
	private static final String EMAIL_SERVER = ConfigKit.getStr("EMAIL_SERVER");
    private static final String EMAIL_USER = ConfigKit.getStr("EMAIL_USER");
    private static final String EMAIL_PASSWD = ConfigKit.getStr("EMAIL_PASSWD");

	public void sendModifyPwdEmail(String email)
	{

		try
		{
			if(Validate.isEmpty(email))return;
			
			Eml eml = new Eml(EMAIL_SERVER, EMAIL_USER, EMAIL_USER, EMAIL_PASSWD);
			eml.addTo(email);
			eml.setSubject("密码修改提示");
			eml.setBody("你最近修改了密码 如非本人操作 请及时 联系管理员");
			eml.send();
		} catch (MessagingException e)
		{
			e.printStackTrace();
		}

	}

	public static void sendThirdPartyReport(String email,String ccemial,String suject,String body)
	{

		try
		{
			if(Validate.isEmpty(email))return;
			
			Eml eml = new Eml(EMAIL_SERVER, EMAIL_USER, EMAIL_USER, EMAIL_PASSWD);
			eml.addTo(email);
//			eml.addCc(ccemial);
			eml.setSubject(suject);
			eml.setBody(body);
			eml.send();
		} catch (MessagingException e)
		{
			e.printStackTrace();
		}

	}
	
	public static void sendDevReport(String email,String suject,String body)
	{
		try
		{
			if(Validate.isEmpty(email))return;
			Eml eml = new Eml(EMAIL_SERVER, EMAIL_USER, EMAIL_USER, EMAIL_PASSWD);
			eml.addTo(email);
			eml.setSubject(suject);
			eml.setBody(body);
			eml.send();
		} catch (MessagingException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void sendEmail(String email,String ccemial,String suject,String body)
	{

		try
		{
			if(Validate.isEmpty(email))return;
			
			Eml eml = new Eml(EMAIL_SERVER, EMAIL_USER, EMAIL_USER, EMAIL_PASSWD);
			eml.addTo(email);
			eml.addCc(ccemial);
			eml.setSubject(suject);
			eml.setBody(body);
			eml.send();
		} catch (MessagingException e)
		{
			e.printStackTrace();
		}

	}
	
	public static void sendThreadEmail(final String email, final String suject,final String body)
	{

		try
		{
			new Thread(new Runnable(){
				@Override
				public void run(){
					try{
						if(Validate.isEmpty(email))return;
						Eml eml = new Eml(EMAIL_SERVER, EMAIL_USER, EMAIL_USER, EMAIL_PASSWD);
						eml.addTo(email);
						
						eml.setSubject(suject);
						eml.setBody(body);
						eml.send();
						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}).start();
			
	
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	public static void sendEmail_1(String email,String ccemial,String suject,List<HashMap<String, String>> bodyList)
	{

		try
		{
			if(Validate.isEmpty(email))return;
			if(Validate.isEmpty(ccemial))return;
			
			EmlMixed eml = new EmlMixed(EMAIL_SERVER, EMAIL_USER, EMAIL_USER, EMAIL_PASSWD);
			eml.addTo(email);
			eml.addCc(ccemial);
			eml.setSubject(suject);
			eml.setBody(bodyList);
			eml.send();
		} catch (MessagingException e)
		{
			e.printStackTrace();
		}

	}
	
	//excel作为附件
	public static void sendExcelEmail(String email,String ccemial,String suject, String filePath)
    {
        try
        {
            if(Validate.isEmpty(email))return;
            Eml eml = new Eml(EMAIL_SERVER, EMAIL_USER, EMAIL_USER, EMAIL_PASSWD);
            eml.addTo(email);
            eml.addCc(ccemial);
            eml.setSubject(suject);
            eml.addFile(filePath);
            eml.send();
        } catch (MessagingException e)
        {
            e.printStackTrace();
        }

    }
}
