Ext.define('Ext.app.ListView', {
    extend : 'Ext.container.Viewport',
    alias : 'ListView',
    requires : ['Ext.form.*', 'Ext.data.*', 'Ext.tip.QuickTipManager'],
   
    header : Ext.create("Ext.panel.Panel",
    {
	id : 'app-header',
	region : 'north',
	border : false,
	split : false
    }),

    center : Ext.create("Ext.panel.Panel",
    {
	xtype : 'container',
	region : 'center',
	layout : 'border'
    }),
    
    
    initComponent : function()
    {
    	Ext.apply(this,
    	{
    	    id : 'app-viewport',
    	    layout :
    	    {
    		type : 'border',
    		padding : '0 5 5 5'
    	    },
    	    items : [ this.header, this.center
    	    ]
    	});
    	this.callParent(arguments);
    }
});