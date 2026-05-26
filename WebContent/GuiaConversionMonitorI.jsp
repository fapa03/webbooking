<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<script type="text/javascript" src="barraEspera.js"></script>
<script type="text/javascript" src="GuiaConversionMonitor.js"></script>
<title>Insert title here</title>
<%
String clickedItems = (String)request.getAttribute("clickedItems");
String error = "";
if (request.getAttribute("error")!=null) {
	error = (String)request.getAttribute("error");
}
%>
</head>
<body onload="initMonitorI();">
<form name="fma">
	<input type="hidden" name="error" value="<%=error%>"/>
	<input type="hidden" name="clickedItems" value="<%=clickedItems%>"/>
</form>
</body>
</html>