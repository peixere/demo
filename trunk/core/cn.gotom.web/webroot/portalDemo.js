/**
* author 裴绍国
* @class Ext.app.Portal demo
* @extends Object
* A sample portal layout application class.
*/

var appURL = "main.json";

var model = Ext.define("TreeModel", {// 定义树节点数据模型
	extend : "Ext.data.Model",
	fields : [{
		name : "id",
		type : "string"
	}, {
		name : "text",
		type : "string"
	}, {
		name : "iconCls",
		type : "string"
	}, {
		name : "leaf",
		type : "boolean"
	}, {
		name : 'type'
	}, {
		name : 'component'
	}]
});

var createStore = function(id) {// 创建树面板数据源
	return Ext.create("Ext.data.TreeStore", {
		defaultRootId : id, // 默认的根节点id
		model : model,
		proxy : {
			type : "ajax", // 获取方式
			url : appURL + "?action=node" // 获取树节点的地址
		},
		clearOnLoad : true,
		nodeParam : "id"// 设置传递给后台的参数名,值是树节点的id属性
	});
};

Ext.onReady(function(){
	var portal = Ext.create('Ext.app.Portal');
	var titlePanel = portal.getTitlePanel();
	var navPanel = portal.getNavPanel();
	var tabPanel = portal.getTabPanel();

	titlePanel.setLoading(titlePanel.id+' Loading...');
	Ext.defer(function() {
		titlePanel.update("测试")
		titlePanel.setLoading(false);
	}, 2000);

	function itemClick(view, node) {
		var has = false;
		for(var i = 0;i<tabPanel.items.length;i++)
		{
			if(tabPanel.items.get(i).id == node.data.id)
			{
				has = true;
				tabPanel.setActiveTab(tabPanel.items.get(i));
				break;
			}
		}
		if(has){
			return;
		}
		if (node.isLeaf()) {// 判断是否是根节点
			if (node.data.type === 'URL') {// 判断资源类型
				var panel = Ext.create('Ext.panel.Panel', {
					id : node.data.id,
					title : node.data.text,
					closable : true,
					iconCls : 'icon-activity',
					html : '<iframe width="100%" height="100%" frameborder="0" src="http://www.baidu.com"></iframe>'
				});
				tabPanel.add(panel);
				tabPanel.setActiveTab(panel);
			} else if (node.data.type === 'COMPONENT') {
				var panel = Ext.create(node.data.component, {
					id : node.data.id,
					title : node.data.text,
					closable : true,
					iconCls : 'icon-activity'
				});
				tabPanel.add(panel);
				tabPanel.setActiveTab(panel);
			}
		}
	}

	function addTree(data) {
		for (var i = 0; i < data.length; i++) {
			navPanel.add(Ext.create("Ext.tree.Panel", {
				title : data[i].text,
				iconCls : data[i].iconCls,
				// useArrows: true,
				autoScroll : true,
				rootVisible : false,
				viewConfig : {
					loadingText : "正在加载..."
				},
				store : createStore(data[i].id),
				listeners : {
					afterlayout : function() {
						if (this.getView().el) {
							var el = this.getView().el;
							var table = el.down("table.x-grid-table");
							if (table) {
								table.setWidth(el.getWidth());
							}
						}
					},
					itemclick : function(view, node) {
						itemClick(view,node);
					}
				}
			}));
			navPanel.doLayout();
		}
	}
	ajax({
		url : appURL, // 获取面板的地址
		params : {
			action : "list"
		},
		callback : addTree
	});
	/*
	navPanel.setLoading(navPanel.id+' Loading...');
	Ext.defer(function() {
		var navigation = Ext.create('Ext.tree.Panel', {
			title:'Navigation',
			autoScroll: true,
			border: false,
			iconCls: 'nav',
			html : 'Navigation'
		});

		var settings = Ext.create('Ext.tree.Panel', {
			title:'Settings',
			autoScroll: true,
			border: false,
			iconCls: 'settings',
		});
		navPanel.add(navigation);
		navPanel.add(settings);
		portal.addNavItem('baidu','nav','');
		navPanel.setLoading(false);
	}, 2000);
	*/
	
	tabPanel.setLoading(navPanel.id+' Loading...');
	Ext.defer(function() {
		var panela = Ext.create('Ext.panel.Panel', {
			xtype: 'panel',
			closable: true,
			title: '动态页A',
			html : 'ssssssssssss',	
		});	
		var panelb = Ext.create('Ext.panel.Panel', {
			xtype: 'panel',
			closable: true,
			title: '动态页B',
			html : '<iframe width="100%" height="100%" frameborder="0" src="http://www.baidu.com"></iframe>',
		});		
		tabPanel.add(panela);
		tabPanel.add(panelb);
		var portalPanel =  portal.getPortalPanel();
		var portalcolumn = Ext.create('Ext.app.PortalColumn',
		{
			columnWidth : 0.7,
			items : [{
				title : '新闻动态',
				height : 150,	
				tools: portal.getTools(),
				listeners: {
					'close': Ext.bind(portal.onPortletClose, portal)
				}
			}, {
				title : '最新通知',
				height : 150,
				tools: portal.getTools(),
				listeners: {
					'close': Ext.bind(portal.onPortletClose, portal)
				}
			}, {
				title : '业绩报表',
				height : 250,
				tools: portal.getTools(),
				items: Ext.create('Ext.app.ChartPortlet'),
				listeners: {
					'close': Ext.bind(portal.onPortletClose, portal)
				}
			}]
		});
		portalPanel.add(portalcolumn);
		var portalcolumn2 = Ext.create('Ext.app.PortalColumn',
		{
			columnWidth : 0.7,
			items : [{
				title : '功能链接',
				height : 150,	
				tools: portal.getTools(),
				listeners: {
					'close': Ext.bind(portal.onPortletClose, portal)
				}
			}, {
				title : '待办事项',
				height : 150,
				tools: portal.getTools(),
				listeners: {
					'close': Ext.bind(portal.onPortletClose, portal)
				}
			}, {
				title : '业绩报表',
				height : 250,
				tools: portal.getTools(),
				items: Ext.create('Ext.app.ChartPortlet'),
				listeners: {
					'close': Ext.bind(portal.onPortletClose, portal)
				}
			}]
		});
		portalPanel.add(portalcolumn2);
		tabPanel.setLoading(false);
	}, 2000);
});