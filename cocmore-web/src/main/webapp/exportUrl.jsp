<%@ page language="java" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<% 
request.setCharacterEncoding("UTF-8");

response.setHeader("Content-Type","application/force-download");
response.setHeader("Content-Type","application/vnd.ms-excel");
response.setHeader("Content-Disposition","attachment;filename=export.xls");
out.print(request.getParameter("exportContent")); 
%>