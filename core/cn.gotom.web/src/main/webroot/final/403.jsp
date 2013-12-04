<%@ page language="java" isErrorPage="true"%>
<%@ include file="/WEB-INF/view/inc/taglibs.jsp"%>
<%response.setStatus(403);%>
<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>权限不足</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="/WEB-INF/view/inc/meta.jsp"%>
</head>
<body>
	<p><img src="${ctxp}/resources/icons/fam/logo.png" title="错误"/>你的权限不足，访问受限，如有疑问请联系系统管理员</p>
	<p>${url}</p>
	<p>${pageContext.request.scheme}://${header.host}${pageContext.errorData.requestURI}</p>
	<p style="text-align: center; margin-top: 20px">
		<img src="${ctxp}/resources/icons/fam/403.png"/>
	</p>
</body>
</html>