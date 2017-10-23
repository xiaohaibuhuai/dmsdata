<%@ page language="java" contentType="text/html; charset=UTF-8" import="com.illumi.oms.util.ActionEnter"
  pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	
	String rootPath = application.getRealPath( "/" );
	
	out.write( new ActionEnter( request, rootPath ).exec() );
	
%>