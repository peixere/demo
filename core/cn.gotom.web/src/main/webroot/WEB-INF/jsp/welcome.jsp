<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
