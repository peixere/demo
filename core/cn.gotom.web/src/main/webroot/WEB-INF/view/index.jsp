<%@ page pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ include file="/WEB-INF/view/inc/taglibs.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Gotom.cn</title>
    <script type="text/javascript" src="${ctxp}/ext4/shared/include-ext.js"></script>
    <!-- 
	<script type="text/javascript" src="${ctxp}/ext4/shared/options-toolbar.js"></script> 
     -->
    <script type="text/javascript">  
    <!--//  
    var desktopPanel = '${custom.desktopPanel}';
    Ext.Loader.setPath('Gotom', '${ctxp}/p/classes');    
    ${plugins}
    Ext.onReady(function(){Ext.create('Gotom.view.PortalView');});
   	<%if(request.getParameter("p")!= null){%>
   	Ext.require('<%=request.getParameter("p")%>');
   	<%}%>    	
    //-->
    </script>    
    <%@ include file="/WEB-INF/view/inc/meta.jsp"%>
</head>
<body>
</body>
</html>
