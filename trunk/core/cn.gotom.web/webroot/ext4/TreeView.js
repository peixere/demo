/**
 * author 裴绍国
 * 
 * @class Ext.app.Portal demo
 * @extends Object A sample portal layout application class.
 */

Ext.define('Ext.app.TreeView',
{
    extend : 'Ext.container.Viewport',
    alias: 'TreeView',
    requires : [ 'Ext.app.PortalPanel', 'Ext.app.PortalColumn', 'Ext.app.GridPortlet', 'Ext.app.ChartPortlet'
    ],
    loadTree : function(url)
    {
	var tree = Ext.create("Ext.tree.Panel",
	{
	    region : 'west',
	    width : 250,
	    minWidth : 100,
	    maxWidth : 400,
	    height : 200,
	    split : true,
	    stateful : true,
	    collapsible : true,
	    rootVisible : false,
	    store : createStore(url, '')
	});
	this.center.add(tree);
    },
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
	region : 'center',
	title : '内容页'
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