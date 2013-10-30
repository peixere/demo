/**
 * author 裴绍国
 * 
 * @class Ext.app.Portal demo
 * @extends Object A sample portal layout application class.
 */

Ext.define('Ext.app.OrgTree',
{
    extend : 'Ext.container.Viewport',
    alias : 'OrgTree',
    requires : [ 'Ext.app.PortalPanel', 'Ext.app.PortalColumn', 'Ext.app.GridPortlet', 'Ext.app.ChartPortlet'
    ],

    header : Ext.create("Ext.panel.Panel",
    {
	title : '头',
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
	// title : '内容页',
	// layout:'fit',
	region : 'center'
    }),

    center : Ext.create("Ext.panel.Panel",
    {
	title : '中间',
	xtype : 'container',
	region : 'center',
	layout : 'border'
    }),
    tree : Ext.create("Ext.tree.TreePanel",
    {
	id : 'tree',
	title : '菜单管理',
	region : 'west',
	width : 250,
	minWidth : 100,
	maxWidth : 400,
	height : 200,
	rootVisible : false,
	//store : treeStore('p/right!tree.do'),
	// store : treeStore('p/organization!tree.do'),
	listeners :
	{
	    itemclick : function(view, node)
	    {
		// menuClick(view, node);
	    }
	}
    }),
    initComponent : function()
    {
	var me = this;
	Ext.applyIf(me,
	{
	    items : [ me.header, me.center
	    ]
	});
	this.center.add(me.tree);
	//this.center.add(this.content);
	me.callParent(arguments);
    }
});
Ext.onReady(function()
{
    var view = Ext.create("Ext.container.Viewport",
    {
	layout :
	{
	    type : 'border',
	    padding : '0 5 5 5'
	}
    });
    var OrgTree = Ext.create("OrgTree");
    view.add(OrgTree);
});