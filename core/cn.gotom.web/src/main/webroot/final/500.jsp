<%@ page language="java" isErrorPage="true"%>
<%@ include file="/WEB-INF/view/inc/taglibs.jsp"%>
<%response.setStatus(500);%>
<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>页面出现异常</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="/WEB-INF/view/inc/meta.jsp"%>
</head>
<body>
	<p><img src="${ctx}/resources/icons/fam/logo.png" title="错误"/>您要访问的页面出现异常</p>
	<p>${pageContext.request.scheme}://${header.host}${pageContext.errorData.requestURI}</p>
<p>
		<%	
		exception = null;
		if (request.getAttribute("java.lang.Throwable") != null)
		{
			exception = ((Exception) request.getAttribute("java.lang.Throwable"));
		}
		if (exception != null)
		{
			exception.printStackTrace(new java.io.PrintWriter(out));
		}
	%>
	
</p>	
</body>
</html>