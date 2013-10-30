Ext.define('OrganizationWindow',
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
    datalist : '',
    layout :
    {
	type : 'border'
    },
    form : Ext.create('Ext.form.Panel',
    {
	region : 'center',
	// padding: 20,
	bodyPadding : 20,
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
	    fieldLabel : '节点标识',
	    name : 'id'
	},
	{
	    xtype : 'hiddenfield',
	    anchor : '100%',
	    fieldLabel : '父节点标识',
	    name : 'parentId'
	},
	{
	    allowBlank : false,
	    msgTarget : 'side',	    
	    xtype : 'textfield',
	    anchor : '100%',
	    name : 'code',
	    fieldLabel : '组织编码'
	},
	{
	    xtype : 'textfield',
	    anchor : '100%',
	    allowBlank : false,
	    msgTarget : 'side',
	    name : 'text',
	    fieldLabel : '组织名称'
	},
	{
	    xtype : 'numberfield',
	    anchor : '100%',
	    fieldLabel : '排列顺序',
	    name : 'sort'
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
		if (me.form.isValid())
		{
		    me.form.submit(
		    {
			url : urlprefix + '!save.do',
			method : 'POST',
			waitMsg : '正在保存数据，稍后...',
			success : function(f, action)
			{
			    Ext.Msg.alert('信息提示', '保存成功');
			    me.close();
			    location.reload();
			},
			failure : function(f, action)
			{
			    Ext.Msg.alert('信息提示', '保存失败！');
			}
		    });
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
var urlprefix = '../p/organization';
var MyTreeModel = Ext.define("MyTreeModel",
{// 定义树节点数据模型
    extend : "Ext.data.Model",
    fields : [
    {
	name : "id",
	type : "string"
    },
    {
	name : "parentId",
	type : "string"
    },
    {
	name : 'sort',
	type : 'int'
    },

    {
	name : "code",
	type : "string"
    },
    {
	name : "leaf",
	type : "boolean"
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

function myTreeStore(url, pid)
{
    return Ext.create("Ext.data.TreeStore",
    {
	defaultRootId : pid,
	model : MyTreeModel,
	proxy :
	{
	    type : "ajax",
	    url : url
	},
	clearOnLoad : true,
	nodeParam : "id"
    });
};
var treeStore = myTreeStore(urlprefix + '!tree.do', '');
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
	collapsible : false,
	frame : false,
	enableDD : true,
	autoScroll : true,
	autoHeight : false,
	rootVisible : false,
	store : treeStore,
	multiSelect : false,
	root :
	{
	    expanded : true
	},
	tbar : [
	{
	    xtype : 'button',
	    iconCls : 'icon-refresh',
	    text : '刷新',
	    handler : function(button, e)
	    {
		Ext.Msg.wait("正在执行......", "操作提示");
		window.location.reload();
	    }
	},
	{
	    xtype : 'button',
	    iconCls : 'icon-add',
	    text : '添加目录',
	    handler : function(button, e)
	    {
		handlernew(false);
	    }
	},
	{
	    xtype : 'button',
	    iconCls : 'icon-add-p',
	    text : '添加资源',
	    handler : function(button, e)
	    {
		handlernew(true);
	    }
	},
	{
	    xtype : 'button',
	    iconCls : 'icon-edit',
	    text : '修改',
	    handler : function(button, e)
	    {
		handleredit(button, e);
	    }
	},
	{
	    xtype : 'button',
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
	    dataIndex : 'code',
	    text : '数据资源'
	},
	{
	    xtype : 'actioncolumn',
	    menuDisabled : true,
	    align : 'center',
	    text : '修改',
	    width : 40,
	    iconCls : 'icon-edit',
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

    function handlernew(left)
    {
	var p = getSelectedNode(tree);
	var parentId = '';
	if (!Ext.isEmpty(p, false))
	{
	    parentId = p.data.id;
	    if (p.data.leaf)
	    {
		Ext.Msg.alert("操作提示", "请选择目录类型节点！");
		return;
	    }
	}
	var wait = Ext.Msg.wait("正在执行......", "操作提示");
	Ext.Ajax.request(
	{
	    url : urlprefix + '.do',
	    method : 'POST',
	    success : function(response, options)
	    {
		var result = Ext.JSON.decode(response.responseText);
		var data = Ext.create('MyTreeModel');
		result.parentId = parentId;
		data.data = result;
		data.data.leaf = left;
		wait.close();
		showform(data);
	    },
	    failure : function(response, options)
	    {
		wait.close();
		Ext.Msg.alert("操作提示", "操作失败");
	    }
	});
    }
    function handlerdel(button, e)
    {
	var ids = getAllChecked(tree);
	if (ids.length == 0)
	{
	    Ext.Msg.show(
	    {
		// width : 200
		title : "操作提示",
		msg : "请选择要删除的节点!",
		icon : Ext.Msg.WARNING
	    });
	    return;
	}
	Ext.Msg.confirm("警告", "确定要删除吗？", function(button)
	{
	    if (button == "yes")
	    {
		Ext.Msg.wait("正在执行......", "操作提示");
		Ext.Ajax.request(
		{
		    url : urlprefix + '!remove.do',
		    method : 'POST',
		    params :
		    {
			id : ids
		    },
		    success : function(response, options)
		    {
			Ext.Msg.alert("删除提示", "删除成功");
			window.location.reload();
		    },
		    failure : function(response, options)
		    {
			Ext.Msg.alert("删除提示", "删除失败");
		    }
		});
	    }
	});
    }

    function showform(record)
    {
	if (record != null && record != '')
	{
	    if (!formwin)
		formwin = Ext.create('OrganizationWindow');
	    formwin.form.getForm().reset();
	    formwin.show();
	    formwin.form.loadRecord(record);
	}
	else
	{
	    Ext.Msg.show(
	    {
		// width : 200
		title : "操作提示",
		msg : "请选择要修改的节点!",
		icon : Ext.Msg.WARNING
	    });
	}
    }
    tree.expandAll();
    view.add(tree);
});