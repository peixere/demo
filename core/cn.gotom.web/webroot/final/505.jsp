<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>您的访问出错了</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="shortcut icon" href="${ctx}/resources/favicon.ico" />
</head>
<body>
	<p><img src="${ctx}/resources/icons/fam/logo.png"/>您的访问出错了</p>
	<p>很抱歉，您要访问的页面出现异常!</p>
	<p style="text-align: center; margin-top: 20px">
		<img src="${ctx}/resources/icons/fam/505.png"/>
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