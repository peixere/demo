Ext.define('Ext.app.RightWindow',
{
    extend : 'Ext.window.Window',
    requires : [ 'Ext.form.*', 'Ext.data.*', 'Ext.tip.QuickTipManager'
    ],
    height : 300,
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
	items : [
	{
	    xtype : 'hiddenfield',
	    anchor : '100%',
	    fieldLabel : '菜单标识',
	    name : 'id'
	},
	{
	    xtype : 'hiddenfield',
	    anchor : '100%',
	    fieldLabel : '父节点标识',
	    name : 'parentId'
	},
	{
	    xtype : 'hiddenfield',
	    anchor : '100%',
	    fieldLabel : '所属应用',
	    name : 'appCode'
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
	    name : 'component',
	    fieldLabel : '连接或控件'
	},
	{
	    xtype : 'numberfield',
	    anchor : '100%',
	    fieldLabel : '排列顺序',
	    name : 'sort'
	},	
	{
	    xtype : 'textareafield',
	    anchor : '100%',
	    allowBlank : false,
	    name : 'resource',
	    msgTarget : 'side',
	    fieldLabel : '菜单资源'
	}
	]
    }),
    handlersave : function(button, e)
    {
	if (this.form.isValid())
	{
	    Ext.Msg.alert('信息提示', '添加时出现异常！');
	}
    },
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
		me.handlersave(button, e);
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

function rightTreeStore(url, pid)
{
    return Ext.create("Ext.data.TreeStore",
    {
	defaultRootId : pid,
	model : RightTreeModel,
	proxy :
	{
	    type : "ajax",
	    url : url
	},
	clearOnLoad : true,
	nodeParam : "id"
    });
};

function getSelectedNode(tree)
{
    var selectedNode = '';
    findSelectedNodeCallback(tree.getRootNode());
    return selectedNode;
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
	    {
		findSelectedNodeCallback(nd);
	    }
	});
    }
}

function getAllChecked(tree)
{
    var temp = [];
    var rootNode = tree.getRootNode();// 获取根节点
    findchildnode(rootNode); // 开始递归
    var nodevalue = temp.join(",");
    return nodevalue;
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
    ;
}

Ext.Loader.setPath('Ext.app', 'ext4/classes');
Ext.onReady(function()
{
    var view = Ext.create("Ext.container.Viewport",
    {
	layout : "border"
    });

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
	store : rightTreeStore('/core/rightTree.do', ''),
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
	},
	{
	    xtype : 'button',
	    border : true,
	    iconCls : 'icon-del',
	    text : '删除',
	    handler : function(button, e)
	    {
		handlerdel(button, e);
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
	    xtype : 'gridcolumn',
	    dataIndex : 'iconCls',
	    text : '使用样式'
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
	},
	{
	    xtype : 'actioncolumn',
	    menuDisabled : true,
	    align : 'center',
	    text : '修改',
	    width : 40,
	    iconCls : 'icon-edit',// 'resources/icons/fam/edit.png',
	    handler : function(grid, rowIndex, colIndex, actionItem, event, record, row)
	    {
		showform(record);
	    }
	}
	]
    });

    var formwin = null;
    function handleredit(button, e)
    {
	showform(getSelectedNode(tree));
    }

    function handlerdel(button, e)
    {
	Ext.Msg.alert('选中项', getAllChecked(tree));
    }

    function showform(record)
    {
	if (record != null && record != '')
	{
	    if (!formwin)
		formwin = Ext.create('Ext.app.RightWindow');
	    formwin.show();
	    formwin.form.loadRecord(record);
	}
    }
    view.add(tree);
});