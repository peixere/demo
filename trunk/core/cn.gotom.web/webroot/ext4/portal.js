/**
 * author 裴绍国
 * 
 * @class Ext.app.Portal demo
 * @extends Object A sample portal layout application class.
 */

Ext.define('Ext.app.Portal',
{
    extend : 'Ext.container.Viewport',
    alias : 'Portal',
    requires : [ 'Ext.app.PortalPanel', 'Ext.app.PortalColumn', 'Ext.app.GridPortlet', 'Ext.app.ChartPortlet'
    ],

    header : Ext.create("Ext.panel.Panel",
    {
	id : 'app-header',
	region : 'north',
	border : false,
	split : false
    }),

    content : Ext.create("Ext.panel.Panel",
    {
	id : 'app-portal',
	autoScroll : true,
	border : true,
	title : '内容页',
	region : 'center'
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
	this.center.add(this.content);
	this.callParent(arguments);
    }
});