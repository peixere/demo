<%@ page pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%response.setStatus(401);%>
<%@ include file="inc/inc.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="inc/meta.jsp"%>
<script type="text/javascript" src="${ctxp}/resources/js/jcontext.js"></script>
<script type="text/javascript" src="${ctxp}/resources/js/md5-min.js"></script>
<script type="text/javascript">
<!--//
if(window.top.location.href != window.location.href)
{
	window.top.location.href = window.top.location.href;
}
function passwordEncoding(){
    document.getElementById('password').value = hex_md5(document.getElementById('passwordinput').value);
    document.getElementById('passwordinput').value = '';
}
-->
</script>
</head>
<body>
<div id="view-container">
	<%@ include file="inc/header.jsp"%>
	<form id="form" action="${ctxp}${serverLoginUrl}" method="POST" onsubmit="passwordEncoding();">
		<div>请输入你的的用户名和密码.<hr/></div>		
		<p>
		<c:if test="${errorMsg != null}">
		<div id="errorMsg">${errorMsg}</div>
		</c:if>
		<input type="hidden" name="method" value="login" />
		<input type="hidden" name="passwordencoding" value="true" />
		<input type="hidden" name="password" id="password" />		
		<input type="hidden" name="${serviceParameterName}" value="${service}" />
			登录帐户: <br/><input type="text" name="username" style="width:260px;" />
		</p>
		<p>
			登录密码: <br/><input type="password" name="passwordinput" id="passwordinput" style="width:260px;"/>
		</p>
		<hr/>
		<div id="button"><input type="submit" value="" id="submit" style="background-image:url('resources/icons/login.gif');padding:0px"/></div>
	</form>
	</div>
	<%@ include file="inc/footer.jsp"%>
	
</body>
</html>
