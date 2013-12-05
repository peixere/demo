<%@ page language="java" isErrorPage="true"%>
<%@ page pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ include file="/WEB-INF/view/inc/taglibs.jsp"%>
<%response.setStatus(404);%>
<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>404页面不存在</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="/WEB-INF/view/inc/meta.jsp"%>
</head>
<body>
	<p><img src="${ctxp}/resources/icons/fam/logo.png" title="错误"/>404您要访问的页面不存在!</p>
	<p>
	${pageContext.request.scheme}://${header.host}${pageContext.errorData.requestURI}
	</p>
	<p style="text-align: center; margin-top: 20px">
		<img src="${ctxp}/resources/icons/fam/404.png"/>
	</p>
</body>
</html>