/**
* author 裴绍国
* @class Ext.app.Portal demo
* @extends Object
* A sample portal layout application class.
*/

var menuUrl = "menu";

var createStore = function(id) {// 创建树面板数据源
	return Ext.create("Ext.data.TreeStore", {
		defaultRootId : id, // 默认的根节点id
		model : model,
		proxy : {
			type : "ajax", // 获取方式
			url : menuUrl // 获取树节点的地址
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
		var iconPanel = Ext.create("Ext.panel.Panel", {
			border: false,
        	split : false,
			id : 'iconPanel',
			html : '<div style="line-height : 60px;padding-left:20px;font-size:28px;color:#000000;">管理平台</div>',
			region : 'center',
			//bodyStyle : 'backgroud-color:#FFFFFF;line-height : 60px;padding-left:20px;font-size:28px;color:#000000;',
		});
		var userPanel = Ext.create("Ext.panel.Panel", {
			border: false,
        	split : false,
			id : 'userPanel',
			html : '<a href="#">管理员</a>      <a href="#">注销</a>',
			region : 'east',
			width: 150,
			bodyStyle : 'backgroud-color:#FFFFFF;line-height : 60px;padding-left:20px;font-size:13px;color:#000000;',
		});		
		//titlePanel.update("测试");
		titlePanel.add(userPanel);
		titlePanel.add(iconPanel);
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
		//var data = res.menuList;
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
	navPanel.setLoading(navPanel.id+' Loading...');
	Ext.defer(function() {
		ajax({
			url : menuUrl,
			callback : addTree
		});
		navPanel.setLoading(false);
	},1000);

	
	tabPanel.setLoading(tabPanel.id+' Loading...');
	Ext.defer(function() {
		var portalPanel =  portal.getPortalPanel();
		var portalcolumn = Ext.create('Ext.app.PortalColumn',
		{
			columnWidth : 0.7,
			items : [{
				title : '新闻动态',
				height : 150,	
				tools: portal.getTools(),
				html : '<iframe width="100%" height="100%" frameborder="0" src="http://www.baidu.com"></iframe>',
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