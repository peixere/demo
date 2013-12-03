    <script type="text/javascript" src="${ctxp}/ext4/shared/include-ext.js"></script>
	<script type="text/javascript" src="${ctxp}/ext4/shared/options-toolbar.js"></script> 
    <script type="text/javascript">  
    <!--//  
        Ext.Loader.setPath('Gotom', '${ctxp}/p/classes');
        //Ext.Loader.setPath('demo', '${ctxp}/plugins/demo/classes');
        ${plugins}
    	Ext.require('Gotom.view.PortalView');
        Ext.onReady(function(){
            Ext.create('Gotom.view.PortalView');
        });
       //-->
    </script>    