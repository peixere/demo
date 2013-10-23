<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>
<head>
	<title>业务基础平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link type="text/css" rel="stylesheet" href="resources/portal.css">
	<script type="text/javascript">
	<!--
	var ctx = '${ctx}';
	-->
	</script>
    <!-- <x-bootstrap> -->
    <script type="text/javascript" src="ext4/shared/include-ext.js?theme=access"></script>
    <script type="text/javascript" src="ext4/shared/options-toolbar.js"></script>    
    <!-- </x-bootstrap> --> 	

	<script type="text/javascript" src="ext4/portal.js"></script>
	<script type="text/javascript" src="index.js"></script>
</head>
<body>
<span id="app-msg" style="display:none;"></span>
</body>
</html>
