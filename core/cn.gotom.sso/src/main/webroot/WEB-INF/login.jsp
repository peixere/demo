<%@ page pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%response.setStatus(401);%>
<%@page import="cn.gotom.sso.util.CommonUtils"%>
<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%
	String serviceParameterName = CommonUtils.getInitParameter(this.getServletContext(), "serviceParameterName", "service");
	request.setAttribute("serviceParameterName", serviceParameterName);
%>
<c:set var="ctxp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Central Authentication Service</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="${ctxp}/resources/favicon.ico" type="image/x-icon" rel="icon"/>
	<link href="${ctxp}/resources/favicon.ico" type="image/x-icon" rel="Shortcut Icon"/>
	<script type="text/javascript" src="${ctxp}/resources/js/jcontext.js"></script>
	<script type="text/javascript" src="${ctxp}/resources/js/md5-min.js"></script>
	<script type="text/javascript">
	<!--//
	if(window.top.location.href != window.location.href)
	{
		window.top.location.href = window.top.location.href;
	}
	function passwordEncoding(){
	    document.getElementById('noScript').value = 'true';
	    document.getElementById('password').value = hex_md5(document.getElementById('passwordinput').value);
	    document.getElementById('passwordinput').value = '';
	}
	var num = 0;
	function image(){
	    num++;
	    document.getElementById('imageCode').src = '${ctxp}${serverLoginUrl}?method=code&'+num;
	}
	-->
	</script>
	<style type="text/css">
	<!--
	body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
		font-size:13px;
		background-color: #eee;
	}
	#view-container {
		margin: 0 auto; 
		width: 98%; 
		height: 100%;
		background-color: #ffffff;
		border: 1px solid #cccccc; 
	}
	#form {
		width: 320px; 
		margin: 0 auto; 
		background-color: #eee;
		padding: 6px; 
		-webkit-border-radius: 5px; 
		-moz-border-radius: 5px; 
		border-radius: 5px; 
		border: 1px solid #cccccc; 
		margin-bottom: 30px;
	}
	hr { display: block; height: 1px; border: 0; border-top: 1px solid #cccccc; padding: 0; }
	#company-name { 
		height: 0px;
		margin: 0 0 0px 0px; 
	}
	#app-name {
		background: url('resources/icons/logo.png') no-repeat;
		background-color: #210F7A; 
		color: white; 
		padding: 22px 0px 21px 150px;
		font-size: 24px; 
		font-weight: normal; 
	}
	#errorMsg { 
		padding: 20px; 
		margin-bottom: 10px;
		border: 1px dotted #BB0000; 
		color: #BB0000;
	}
	#footer {  
		padding: 10px;
		color: #000000;
	}
	.inputtext,input[type=text], input[type=password] { 
		width:260px;
		padding: 6px; 
		-webkit-border-radius: 3px; 
		-moz-border-radius: 3px; 
		border-radius: 3px; 
		border: 1px solid #DDDDDD; 
		background: #FFFFDD; 
	}
	#button{
		margin: 0 auto; 
	}
	#submit{
		background:url('resources/icons/login.gif');
		width: 79px; 
		height: 29px;
		border: none; 
		cursor: pointer;
	}
	-->
	</style>	
</head>
<body>
<div id="view-container">
	<div id="header">
		<h1 id="company-name"></h1>
		<h1 id="app-name">用户验证服务中心</h1>
	</div>	
	<form id="form" action="${ctxp}${serverLoginUrl}" method="POST" onsubmit="passwordEncoding();">
		<div>请输入你的的用户名和密码.<hr/></div>		
		<p>
		<c:if test="${errorMsg != null}">
		<div id="errorMsg">${errorMsg}</div>
		</c:if>
		<input type="hidden" name="method" value="login" />
		<input type="hidden" name="noScript" id="noScript" value="false" />
		<input type="hidden" name="passwordencoding" value="true" />
		<input type="hidden" name="password" id="password" />		
		<input type="hidden" name="${serviceParameterName}" value="${service}" />
			登录帐户: <br/><input type="text" name="username" style="width:260px;height:20px;" />
		</p>
		<p>
			登录密码: <br/><input type="password" name="passwordinput" id="passwordinput" style="width:260px;height:20px;"/>
		</p>
		<c:if test="${login > numCount}">
		<p>
			验证编码: <br/><input type="text" name="code" style="width:260px;height:20px;"/>
			<img id="imageCode" onclick="javascript:image();" alt="load..." src="${ctxp}${serverLoginUrl}?method=code">
		</p>	
		</c:if>	
		<hr/>
		<div id="button"><input type="submit" value="" id="submit" style="background-image:url('resources/icons/login.gif');padding:0px"/></div>
	</form>
	</div>
	<div id="footer"><p align="center">Gotom.cn</p></div>	
</body>
</html>
