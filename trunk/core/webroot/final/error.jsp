<%@ page pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ page language="java" isErrorPage="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:text name="errorPage.title" /></title>
<meta http-equiv="Refresh" content="5000;url=${ctx}/index"/>
<meta http-equiv="Pragma" content="no-cache"/>
<!-- error -->
</head>

<body id="error">
<p align="center">
<s:text name="errorPage.heading" />
<s:actionerror />
</p>

<p align="center">
	
	<%
		if (request.getAttribute("java.lang.Throwable") != null)
		{
			exception = ((Exception) request.getAttribute("java.lang.Throwable"));
		}
		if (exception != null)
		{
	%>
	<textarea rows="" cols="" style="width:800px;height:600px">
	<%
			exception.printStackTrace(new java.io.PrintWriter(out));
	%>
	</textarea>
	<%
		}
	%>
	
</p>
</body>
</html>
