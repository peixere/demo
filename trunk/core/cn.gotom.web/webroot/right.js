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

	findchildnode : function(data)
	{

	},
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