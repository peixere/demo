Ext.define('Ext.app.RightWindow',
{
    extend : 'Ext.window.Window',
    requires : [ 'Ext.form.*', 'Ext.data.*', 'Ext.tip.QuickTipManager'
    ],
    height : 250,
    width : 400,
    title : '菜单编辑',
    titleCollapse : false,
    closeAction : true,
    modal : true,
    layout :
    {
	type : 'border'
    },
    form : Ext.create('Ext.form.Panel',
    {
	region : 'center',
	// padding: 20,
	bodyPadding : 10,
	defaults :
	{
	    labelAlign : 'right',
	    anchor : '100%',
	    labelWidth : 100
	},

	reader : new Ext.data.JsonReader(
	{
	    successProperty : 'success',
	}),
	items : [
	{
	    xtype : 'hiddenfield',
	    anchor : '100%',
	    name : 'id',
	},
	{
	    xtype : 'textfield',
	    anchor : '100%',
	    allowBlank : false,
	    msgTarget : 'side',
	    name : 'text',
	    fieldLabel : '菜单名称'
	},
	{
	    xtype : 'textfield',
	    anchor : '100%',
	    name : 'iconCls',
	    fieldLabel : '图标样式'
	},
	{
	    xtype : 'textfield',
	    anchor : '100%',
	    allowBlank : false,
	    name : 'resource',
	    msgTarget : 'side',
	    fieldLabel : '菜单资源'
	}
	]
    }),
    initComponent : function()
    {
	var me = this;
	var btnsave =
	{
	    xtype : 'button',
	    border : true,
	    iconCls : 'icon-save',
	    text : '保存',
	    handler : function(button, e)
	    {
		if (form.isValid())
		{
		    Ext.Msg.alert('信息提示', '添加时出现异常！');
		}
	    }
	};
	var btnClose =
	{
	    xtype : 'button',
	    border : true,
	    iconCls : 'icon-cancel',
	    text : '关闭',
	    handler : function(button, e)
	    {
		me.hide();
	    }
	};
	Ext.applyIf(me,
	{
	    items : [ this.form
	    ],
	    buttons : [ btnsave, btnClose
	    ]
	});

	me.callParent(arguments);
    }

});

Ext.Loader.setConfig(
{
    enabled : true
});

var RightTreeModel = Ext.define("RightTreeModel",
{// 定义树节点数据模型
    extend : "Ext.data.Model",
    fields : [
    {
	name : "id",
	type : "string"
    },
    {
	name : 'sort',
	type : 'int'
    },

    {
	name : "iconCls",
	type : "string"
    },
    {
	name : "leaf",
	type : "boolean"
    },
    {
	name : 'type'
    },
    {
	name : 'resource',
	type : 'string'
    },
    {
	name : 'component',
	type : "string"
    },
    {
	name : "text",
	type : "string"
    },
    {
	name : 'checked',
	type : "boolean"
    }
    ]
});
var RightTreeStore = function(url, pid)
{// 创建树面板数据源
    return Ext.create("Ext.data.TreeStore",
    {
	defaultRootId : pid, // 默认的根节点id
	model : RightTreeModel,
	proxy :
	{
	    type : "ajax", // 获取方式
	    url : url
	// 获取树节点的地址
	},
	clearOnLoad : true,
	nodeParam : "id"// 设置传递给后台的参数名,值是树节点的id属性
    });
};
Ext.Loader.setPath('Ext.app', 'ext4/classes');
Ext.onReady(function()
{
    var view = Ext.create("Ext.container.Viewport",
    {
	layout : "border"
    });

    // var view = Ext.create('Ext.app.ListView');
    // view.header.setTitle('菜单管理');
    // view.center.setTitle('菜单列表');
    var treeStore = RightTreeStore('/core/rightTree.do', '');
    var selectedNode = null;
    var tree = Ext.create('Ext.tree.Panel',
    {
	// title: '菜单列表',
	region : 'center',
	animate : true,
	border : false,
	bodyborder : false,
	lines : true,
	split : true,
	stateful : true,
	// collapsible:true,
	frame : false,
	enableDD : true,
	autoScroll : true,
	autoHeight : false,
	rootVisible : false,
	store : treeStore,
	// multiSelect: true,
	tbar : [
	{
	    xtype : 'button',
	    border : true,
	    iconCls : 'icon-edit',
	    text : '修改',
	    handler : function(button, e)
	    {
		handleredit(button, e);
	    }
	}
	],
	columns : [
	{
	    xtype : 'treecolumn',
	    dataIndex : 'text',
	    text : '菜单名称',
	    sortable : false,
	    flex : 1,
	    menuDisabled : true
	},
	{
	    xtype : 'actioncolumn',
	    menuDisabled : true,
	    align : 'center',
	    text : '修改',
	    icon : 'resources/icons/fam/edit.png',
	    handler : function(grid, rowIndex, colIndex, actionItem, event, record, row)
	    {
		if (!formwin)
		    formwin = Ext.create('Ext.app.RightWindow');
		formwin.show();
		formwin.form.loadRecord(record);
		Ext.Msg.alert('Editing completed task', record.data.text);
	    }
	},
	{
	    xtype : 'gridcolumn',
	    dataIndex : 'iconCls',
	    text : '使用样式',
	},
	{
	    xtype : 'gridcolumn',
	    dataIndex : 'component',
	    text : '控件或连接'
	},
	{
	    xtype : 'gridcolumn',
	    dataIndex : 'resource',
	    text : '数据资源'
	}
	]
    });
    var temp = [];
    function getAllRoot(tree)
    {
	var rootNode = tree.getRootNode();// 获取根节点
	findchildnode(rootNode); // 开始递归
	var nodevalue = temp.join(",");
	// alert(nodevalue);
	return nodevalue;
    }

    // 获取所有的子节点
    function findchildnode(node)
    {
	var childnodes = node.childNodes;
	Ext.each(childnodes, function()
	{ // 从节点中取出子节点依次遍历
	    var nd = this;
	    if (nd.data.checked)
	    {
		temp.push(nd.data.id);
	    }
	    if (nd.hasChildNodes())
	    { // 判断子节点下是否存在子节点
		findchildnode(nd); // 如果存在子节点 递归
	    }
	});
    }
    function findSelectedNode(tree)
    {
	selectedNode = '';
	findSelectedNodeCallback(tree.getRootNode());
	return selectedNode;
    }

    function findSelectedNodeCallback(node)
    {
	var childnodes = node.childNodes;
	Ext.each(childnodes, function()
	{ // 从节点中取出子节点依次遍历
	    var nd = this;
	    if (nd.data.checked)
	    {
		selectedNode = nd;
		return;
	    }
	    else if (nd.hasChildNodes())
	    { // 判断子节点下是否存在子节点
		findSelectedNodeCallback(nd);
	    }
	});
    }
    var formwin = null;
    function handleredit(button, e)
    {
	if (!formwin)
	    formwin = Ext.create('Ext.app.RightWindow');
	formwin.show();
	var node = findSelectedNode(tree);
	formwin.form.loadRecord(node);
	Ext.Ajax.request(
	{
	    url : 'core/rightload.do?id=' + node.data.id, // 请求地址
	    method : 'post', // 方法
	    callback : function(options, success, response)
	    {
		formwin.form.getForm().load(Ext.JSON.decode(response.responseText));
	    }
	});

	formwin.form.getForm().load(
	{
	    url : 'core/rightload.do?id=' + node.data.id
	});
	// var data = Ext.create('RightTreeModel', {
	// 'text' : node.data.text,
	// 'resource' : node.data.resource,
	// 'iconCls': node.data.iconCls,
	// 'component' : node.data.component
	// });
	formwin.form.loadRecord(data);
    }
    view.add(tree);
});