<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<title>Hello World</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
	<h1>${message}</h1>
	<s:form action="welcome">
		<s:textfield name="userName" label="User Name" />
		<s:submit />
	</s:form>
</body>
</html>
