<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hello World</title>
</head>
<body>
	<h1>${user.username}</h1>
	<h1>${user.name}</h1>
	<h1>${user.password}</h1>
	<s:form action="helloSave">
		<s:textfield name="user.name" label="User Name" />
		<s:submit />
	</s:form>
</body>
</html>
