<%@ page session="true" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<spring:theme code="mobile.custom.css.file" var="mobileCss" text="" />
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
	<head>
	    <title>CAS &#8211; Central Authentication Service</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<script type="text/javascript">
	function formsubmit(){
		document.getElementById("fm1").submit();
	}
	</script>
	</head>
  <body>
<form:form method="post" id="fm1" name="fm1" cssClass="fm-v clearfix" commandName="${commandName}">
	<form:errors path="*" id="msg" cssClass="errors" element="div" />
	<form:input cssClass="required" cssErrorClass="error" id="username" value="admin" />
	<form:password cssClass="required" cssErrorClass="error" id="password"/>
	<input id="warn" name="warn" value="true" tabindex="3" type="checkbox" />
	<input type="hidden" name="lt" value="${loginTicket}" />
	<input type="hidden" name="execution" value="${flowExecutionKey}" />
	<input type="hidden" name="_eventId" value="submit" />
	<input class="btn-submit" name="submit" accesskey="l" tabindex="4" type="submit" value="login"/>
	<input type="button" name="btn" value="登录" onclick="formsubmit()" />
	<input class="btn-reset" name="reset" accesskey="c" value="reset" tabindex="5" type="reset" />
</form:form>
    </body>
</html>
    <script src="/ext/ext4.2.1/ext-all.js"></script>
    <link rel="stylesheet" href="/ext/ext4.2.1/resources/ext-theme-classic/ext-theme-classic-all.css">
    <script type="text/javascript" src="<c:url value="/login.js" />"></script>