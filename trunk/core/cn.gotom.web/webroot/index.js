/**
 * author 裴绍国
 * 
 * @class Ext.app.Portal demo
 * @extends Object A sample portal layout application class.
 */

var mainUrl = "main.do";

Ext.onReady(function()
{
    var portal = Ext.create('PortalView');
    var portalPanel = Ext.create("Ext.app.PortalPanel",
    {
	id : 'app-portal',
	xtype : 'portalpanel',
	region : 'center',
	title : "平台首页",
	layout : 'column'
    });

    var tabPanel = Ext.create('Ext.tab.Panel',
    {
	id : 'tabPanel',
	region : 'center',
	activeTab : 0,
	enableTabScroll : true,
	animScroll : true,
	border : true,
	autoScroll : true,
	split : true,
	items : [ portalPanel
	]
    });
    portal.setContent(tabPanel);   
    portal.header.setLoading(portal.header.id + ' Loading...');
    function callbackTitle(data)
    {
	document.title = data.title;
	var htmlStr = '<div class="logoPanel">　' + data.title + '</div>';
	htmlStr += '<div class="userPanel">';
	htmlStr += '欢迎您：<a href="#">' + data.username + '</a>　　';
	htmlStr += '<a href="' + data.casServerLogoutUrl + '">注销</a>';
	htmlStr += '</div>';
	var iconPanel = Ext.create("Ext.panel.Panel",
	{
	    border : false,
	    split : false,
	    id : 'iconPanel',
	    region : 'center',
	    html : htmlStr,
	    bodyStyle : 'background-image: url(resources/icons/fam/topbg.jpg) !important;'
	});
	portal.header.add(iconPanel);
	// HeaderPanel.update(htmlStr);
	portal.header.setLoading(false);
    }
    Ext.defer(function()
    {
	ajax(
	{
	    url : mainUrl,
	    callback : callbackTitle
	});
    }, 2000);

    function itemClick(view, node)
    {
	var has = false;
	for (var i = 0; i < tabPanel.items.length; i++)
	{
	    if (tabPanel.items.get(i).id == node.data.id)
	    {
		has = true;
		tabPanel.setActiveTab(tabPanel.items.get(i));
		break;
	    }
	}
	if (has)
	{
	    return;
	}
	if (node.isLeaf())
	{// 判断是否是根节点
	    if (node.data.type === 'URL')
	    {// 判断资源类型
		var url = addQueryParam(node.data.component,'theme',getQueryParam('theme'));
		var panel = Ext.create('Ext.panel.Panel',
		{
		    id : node.data.id,
		    title : node.data.text,
		    closable : true,
		    // iconCls : 'icon-activity',
		    html : '<iframe width="100%" height="100%" frameborder="0" src="'+url+'"></iframe>'
		});
		tabPanel.add(panel);
		tabPanel.setActiveTab(panel);
	    }
	    else if (node.data.type === 'COMPONENT')
	    {
		var panel = Ext.create(node.data.component,
		{
		    id : node.data.id,
		    title : node.data.text,
		    closable : true
		// iconCls : 'icon-activity'
		});
		tabPanel.add(panel);
		tabPanel.setActiveTab(panel);
	    }
	}
    }

    function addTree(data)
    {
	// var data = res.menuList;
	for (var i = 0; i < data.length; i++)
	{
	    portal.menus.add(Ext.create("Ext.tree.Panel",
	    {
		title : data[i].text,
		iconCls : data[i].iconCls,
		// useArrows: true,
		autoScroll : true,
		rootVisible : false,
		viewConfig :
		{
		    loadingText : "正在加载..."
		},
		store : RightTreeStore('core/rightTree.do', data[i].id),
		listeners :
		{
		    itemclick : function(view, node)
		    {
			itemClick(view, node);
		    }
		}
	    }));
	    portal.menus.doLayout();
	}
    }
    portal.menus.setLoading(portal.menus.id + ' Loading...');
    Ext.defer(function()
    {
	ajax(
	{
	    url : mainUrl + '?action=menu',
	    callback : addTree
	});
	portal.menus.setLoading(false);
    }, 1000);

    tabPanel.setLoading(tabPanel.id + ' Loading...');
    Ext.defer(function()
    {
	// var portalPanel = portal.getPortalPanel();
	var portalcolumn = Ext.create('Ext.app.PortalColumn',
	{
	    columnWidth : 0.7,
	    items : [
	    {
		title : '新闻动态',
		height : 150,
		tools : portal.getTools(),
		html : '<iframe width="100%" height="100%" frameborder="0" src="http://www.google.com.hk"></iframe>',
		listeners :
		{
		    'close' : Ext.bind(portal.onPortletClose, portal)
		}
	    },
	    {
		title : '最新通知',
		height : 150,
		tools : portal.getTools(),
		listeners :
		{
		    'close' : Ext.bind(portal.onPortletClose, portal)
		}
	    },
	    {
		title : '业绩报表',
		height : 250,
		tools : portal.getTools(),
		items : Ext.create('Ext.app.ChartPortlet'),
		listeners :
		{
		    'close' : Ext.bind(portal.onPortletClose, portal)
		}
	    }
	    ]
	});
	portalPanel.add(portalcolumn);
	var portalcolumn2 = Ext.create('Ext.app.PortalColumn',
	{
	    columnWidth : 0.7,
	    items : [
	    {
		title : '功能链接',
		height : 150,
		tools : portal.getTools(),
		listeners :
		{
		    'close' : Ext.bind(portal.onPortletClose, portal)
		}
	    },
	    {
		title : '待办事项',
		height : 150,
		tools : portal.getTools(),
		listeners :
		{
		    'close' : Ext.bind(portal.onPortletClose, portal)
		}
	    },
	    {
		title : '业绩报表',
		height : 250,
		tools : portal.getTools(),
		items : Ext.create('Ext.app.ChartPortlet'),
		listeners :
		{
		    'close' : Ext.bind(portal.onPortletClose, portal)
		}
	    }
	    ]
	});
	portalPanel.add(portalcolumn2);
	tabPanel.setLoading(false);
    }, 2000);
});