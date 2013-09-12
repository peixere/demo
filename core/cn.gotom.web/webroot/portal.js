/**
 * author 裴绍国
 * @class Ext.app.Portal
 * @extends Object
 * A sample portal layout application class.
 */

//@require @packageOverrides
Ext.Loader.setConfig({
    enabled: true
});
Ext.Loader.setPath('Ext.app', 'ext4/classes');
var ajax = function(config) {// 封装、简化AJAX
	Ext.Ajax.request({
		url : config.url, // 请求地址
		params : config.params, // 请求参数
		method : 'post', // 方法
		callback : function(options, success, response) {
			config.callback(Ext.JSON.decode(response.responseText));
			// 调用回调函数
		}
	});
	return false;
};

Ext.define('Ext.app.Portal',
{
	extend: 'Ext.container.Viewport',
	requires: ['Ext.app.PortalPanel', 'Ext.app.PortalColumn', 'Ext.app.GridPortlet', 'Ext.app.ChartPortlet'],
	getTools: function()
	{
		return [{
			xtype: 'tool',
			type: 'gear',
			handler: function(e, target, header, tool){
				var portlet = header.ownerCt;
				portlet.setLoading(portlet.id+'Loading...');
				Ext.defer(function() {
					portlet.setLoading(false);
				}, 2000);
			}
		}];
	},

	getTitlePanel:function()
	{
		return this.items.get(0);
	},

	getNavPanel:function()
	{
		return this.items.get(1);
	},

	addNavItem:function(titles,icon,htmlStr)
	{
		var panel = this.getNavPanel();
		panel.add({
			title:titles,
			autoScroll: true,
			border: false,
			iconCls: icon,
			html:htmlStr,
		});
	},

	getTabPanel:function()
	{
		return this.items.get(3);
	},

	getPortalPanel:function()
	{
		var portal = this.getTabPanel().items.get(0);
		return portal;
	},

    onPortletClose: function(portlet) 
	{
        alert('"' + portlet.title + '" was removed');
    },

	onloadPortal: function(portal)
	{
		portal.setLoading(panel.id+'Loading...');
		Ext.defer(function() {
			portal.setLoading(false);
		}, 2000);
	},

	initComponent: function()
	{
		var titlePanel = Ext.create("Ext.panel.Panel", {
			id : 'title',
			height : 60,
			html : 'title',
			region : 'north',
			border: false,
			split : false,
			bodyStyle : 'backgroud-color:#FFFFFF;line-height : 60px;padding-left:20px;font-size:28px;color:#000000;font-family:宋体;font-weight:bolder;' + 'background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, rgba(153,187, 232, 0.4) ), color-stop(50%, rgba(153, 187, 232, 1) ),color-stop(0%, rgba(153, 187, 232, 0.4) ) )',
			listeners : 
				{
					//afterrender : Ext.bind(this.onloadTitle, this)
				},
		});

		var navPanel = Ext.create("Ext.panel.Panel", {
			id:'navPanel',
			title : "系统菜单",
			region: 'west',
			animCollapse: true,
			width: 200,
			minWidth: 150,
			maxWidth: 400,
			split: true,
			collapsible: true,
			layout:{
				type: 'accordion',
				animate: true
			},
			listeners : 
			{
				//afterrender : Ext.bind(this.onloadNavPanel, this)
			},
		});

		var portal = Ext.create("Ext.app.PortalPanel", 
		{
			id: 'app-portal',
			xtype: 'portalpanel',
			region: 'center',
			title : "平台首页",
			layout : 'column',
		});

		var tabPanel = Ext.create('Ext.tab.Panel', {
			id:'tabPanel',
			region : 'center',
			activeTab : 0,
			enableTabScroll : true,
			animScroll : true,
			border : true,
			autoScroll : true,
			split : true,
			items: [portal]
		});

		Ext.apply(this, 
		{
			title: "Viewport",
            layout: {
                type: 'border',
                padding: '0 5 5 5' // pad the layout from the window edges
            },
			defaults: {
				bodyStyle: "background-color: #FFFFFF;",
				frame: true
			},
			items: [titlePanel,navPanel,tabPanel],
			listeners : {
				afterrender : function() {
					Ext.getBody().mask('正在加载系统....');
					Ext.defer(function() {
						Ext.getBody().unmask();
					}, 0000);					
				}
			},
		});
		this.callParent(arguments);
	},
	
});