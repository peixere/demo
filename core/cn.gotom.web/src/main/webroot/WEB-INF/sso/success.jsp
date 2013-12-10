<%@ page pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ include file="inc/inc.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="inc/meta.jsp"%>
</head>
<body>
<%@ include file="inc/header.jsp"%>
<script type="text/javascript">
<!--//
window.location.href='${service}';
-->
</script>
<a href="${service}">登录成功</a>
<a href="${ctxp}/?method=logout">退出</a>
<%@ include file="inc/footer.jsp"%>
</body>
</html>
