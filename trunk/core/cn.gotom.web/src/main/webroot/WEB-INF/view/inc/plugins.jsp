    <script type="text/javascript" src="${ctxp}/ext4/shared/include-ext.js"></script>
	<script type="text/javascript" src="${ctxp}/ext4/shared/options-toolbar.js"></script> 
    <script type="text/javascript">  
    <!--//  
    Ext.Loader.setPath('Gotom', '${ctxp}/p/classes');
    
    Ext.onReady(function(){Ext.create('Gotom.view.PortalView');});
    	${plugins}
   	<%if(request.getParameter("p")!= null){%>
   	Ext.require('<%=request.getParameter("p")%>');
   	<%}%>    	
    //-->
    </script>    