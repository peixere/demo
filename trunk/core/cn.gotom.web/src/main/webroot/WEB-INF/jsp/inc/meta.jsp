	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="${ctxp}/resources/favicon.ico" type="text/css" rel="shortcut icon"/>
	<link href="${ctxp}/resources/portal.css" type="text/css" rel="stylesheet"/>
	<script type="text/javascript" src="${ctxp}/resources/jquery/jquery-1.10.1.min.js"></script>
	<script type="text/javascript" src="${ctxp}/resources/highcharts/3.0.7/highcharts.js"></script>
	<script type="text/javascript" src="${ctxp}/resources/highcharts/3.0.7/modules/exporting.js"></script>
    <script type="text/javascript" src="${ctxp}/ext4/shared/include-ext.js?theme=classic"></script>
	<script type="text/javascript" src="${ctxp}/ext4/shared/options-toolbar.js"></script> 
    <script type="text/javascript">    
        Ext.Loader.setPath('Gotom', 'classes');
        Ext.Loader.setPath('MyApp', 'ext/demo');
    	Ext.require('Gotom.view.PortalView');
        Ext.onReady(function(){
            Ext.create('Gotom.view.PortalView');
        });
    </script>    