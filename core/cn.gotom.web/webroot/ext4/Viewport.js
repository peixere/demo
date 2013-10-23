/**
 * author 裴绍国
 * @class Ext.app.Viewport
 * @extends Object
 * A sample portal layout application class.
 */
Ext.define('Ext.app.Viewport',
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

	getHeaderPanel:function()
	{
		return this.items.get(0);
	},

	getNavPanel:function()
	{
		//return this.items.get(1);
		return this.items.get(1).items.get(0);
	},

	addNavItem:function(titles,icon,htmlStr)
	{
		var panel = this.getNavPanel();
		panel.add({
			title:titles,
			autoScroll: true,
			border: false,
			iconCls: icon,
			html:htmlStr
		});
	},

	getTabPanel:function()
	{
		//return this.items.get(3);
		return this.items.get(1).items.get(2);
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
		var HeaderPanel = Ext.create("Ext.panel.Panel", {
			id: 'app-header',
			//height : 60,
			region : 'north',
			border: false,
        	split : false
		});

		var navPanel = Ext.create("Ext.panel.Panel", {
			id:'app-options',
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
			}
		});

		var bodyPanel = Ext.create("Ext.panel.Panel", {
			id: 'app-header',
			region : 'center',
			border: false,
        	split : false
		});

		Ext.apply(this, 
		{
            id: 'app-viewport',
            layout: {
                type: 'border',
                padding: '0 5 5 5'
            },
			items: [HeaderPanel,{
                xtype: 'container',
                region: 'center',
                layout: 'border',
                items: [navPanel,bodyPanel]}
			]
		});
		this.callParent(arguments);
	}	
});