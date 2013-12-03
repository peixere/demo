/*
 * File: classes/view/UserPanel.js
 *
 * This file was generated by Sencha Architect version 2.2.3.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 4.2.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 4.2.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

Ext.define('Gotom.view.UserPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.UserPanel',

    id: 'UserPanel',
    layout: {
        type: 'border'
    },
    header: false,
    title: '用户管理',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    items: [
                        {
                            xtype: 'button',
                            id: 'btnNew2',
                            iconCls: 'icon-add',
                            text: '新增',
                            listeners: {
                                click: {
                                    fn: me.onBtnNewClick,
                                    scope: me
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'btnEdit2',
                            iconCls: 'icon-edit',
                            text: '修改',
                            listeners: {
                                click: {
                                    fn: me.onBtnEditClick,
                                    scope: me
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'btnNormal',
                            iconCls: 'icon-status-online',
                            text: '恢复',
                            listeners: {
                                click: {
                                    fn: me.onBtnNormalClick,
                                    scope: me
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'btnBanned',
                            iconCls: 'icon-status-offline',
                            text: '挂起',
                            listeners: {
                                click: {
                                    fn: me.onBtnBannedClick,
                                    scope: me
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'btnDel2',
                            iconCls: 'icon-del',
                            text: '删除',
                            listeners: {
                                click: {
                                    fn: me.onBtnDelClick,
                                    scope: me
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'btnSave2',
                            iconCls: 'icon-save',
                            text: '保存',
                            listeners: {
                                click: {
                                    fn: me.onBtnSaveClick,
                                    scope: me
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'btnRef2',
                            iconCls: 'icon-refresh',
                            text: '刷新',
                            listeners: {
                                click: {
                                    fn: me.onBtnRefClick,
                                    scope: me
                                }
                            }
                        }
                    ]
                }
            ],
            items: [
                {
                    xtype: 'gridpanel',
                    region: 'west',
                    id: 'UserGridPanel',
                    width: 326,
                    title: '用户列表',
                    columns: [
                        {
                            xtype: 'gridcolumn',
                            sortable: false,
                            dataIndex: 'username',
                            text: '登录帐号'
                        },
                        {
                            xtype: 'gridcolumn',
                            sortable: false,
                            dataIndex: 'name',
                            text: '用户名称'
                        },
                        {
                            xtype: 'gridcolumn',
                            defaultWidth: 50,
                            dataIndex: 'status',
                            text: '状态'
                        }
                    ],
                    viewConfig: {
                        id: 'UserGridView',
                        listeners: {
                            itemdblclick: {
                                fn: me.onViewItemDblClick,
                                scope: me
                            }
                        }
                    },
                    selModel: Ext.create('Ext.selection.CheckboxModel', {

                    }),
                    tools: [
                        {
                            xtype: 'tool',
                            id: 'UserTool',
                            type: 'refresh',
                            listeners: {
                                click: {
                                    fn: me.onToolClick,
                                    scope: me
                                }
                            }
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    region: 'center',
                    id: 'UserCenterPanel',
                    layout: {
                        type: 'border'
                    },
                    items: [
                        {
                            xtype: 'form',
                            region: 'north',
                            height: 98,
                            id: 'UserForm',
                            bodyPadding: 10,
                            title: '编辑用户',
                            items: [
                                {
                                    xtype: 'hiddenfield',
                                    anchor: '100%',
                                    id: 'user.id',
                                    fieldLabel: 'Label',
                                    name: 'user.id'
                                },
                                {
                                    xtype: 'textfield',
                                    anchor: '100%',
                                    id: 'user.username',
                                    fieldLabel: '登录帐号',
                                    name: 'user.username',
                                    allowBlank: false,
                                    enforceMaxLength: true,
                                    maxLength: 50,
                                    minLength: 3
                                },
                                {
                                    xtype: 'textfield',
                                    anchor: '100%',
                                    id: 'user.name',
                                    fieldLabel: '用户名称',
                                    name: 'user.name',
                                    allowBlank: false,
                                    enforceMaxLength: false,
                                    maxLength: 50,
                                    minLength: 2
                                }
                            ]
                        }
                    ]
                }
            ],
            listeners: {
                afterlayout: {
                    fn: me.onPanelAfterLayout,
                    single: true,
                    scope: me
                }
            }
        });

        me.callParent(arguments);
    },

    onBtnNewClick: function(button, e, eOpts) {
        this.loadFormData('');
    },

    onBtnEditClick: function(button, e, eOpts) {
        var selected = Ext.getCmp('UserGridPanel').getSelectionModel().selected;
        var record = selected.items[0];
        if(!Ext.isEmpty(record))
        {
            this.loadFormData(record.data.id);
        }
        else
        {
            Ext.Msg.alert('操作提示','请选择要修改的列!');
        }
    },

    onBtnNormalClick: function(button, e, eOpts) {
        this.userStatus('normal');
    },

    onBtnBannedClick: function(button, e, eOpts) {
        this.userStatus('banned');
    },

    onBtnDelClick: function(button, e, eOpts) {
        this.userStatus('remove');
    },

    onBtnSaveClick: function(button, e, eOpts) {
        this.saveForm();
    },

    onBtnRefClick: function(button, e, eOpts) {
        this.loadGrid();
        this.loadFormData('');
    },

    onViewItemDblClick: function(dataview, record, item, index, e, eOpts) {
        this.loadFormData(record.data.id);
    },

    onToolClick: function(tool, e, eOpts) {
        this.loadGrid();
    },

    onPanelAfterLayout: function(container, layout, eOpts) {
        this.onLoad();
    },

    onLoad: function() {
        this.loadGrid();
        this.loadFormData('');
    },

    loadGrid: function() {
        var me = this;
        Common.ajax({
            component : me,
            message : '正在加载......',    
            url : ctxp+'/p/user!list.do',
            callback : me.bindGrid
        });
    },

    bindGrid: function(result) {
        var me = this;
        var UserStore = Ext.create('Ext.data.Store', {
            storeId:'UserStore',
            fields: [
            {
                name: 'id'
            },
            {
                name: 'name'
            },
            {
                name: 'username'
            },
            {
                name: 'status'
            }
            ],
            data : result.data,
            proxy:
            {
                type: 'memory',
                reader:{
                    type: 'json'
                }
            }
        });
        Ext.getCmp('UserGridPanel').bindStore(UserStore); 
    },

    loadFormData: function(id) {
        var me = this;
        Common.ajax({
            params:{'user.id':id},
            component : Ext.getCmp('UserForm'),
            message : '正在加载......',    
            url : ctxp+'/p/user.do',
            callback : function(result) {
                Ext.getCmp('user.id').setValue(result.user.id);
                Ext.getCmp('user.name').setValue(result.user.name);                
                Ext.getCmp('user.username').setValue(result.user.username);  
                me.bindRoleTree(result.user.id);
            }
        });
    },

    bindRoleTree: function(userId) {
        var me = this;
        var myStore = Ext.create("Ext.data.TreeStore",
            {
                defaultRootId : userId,
                clearOnLoad : true,
                nodeParam : 'user.id',
                fields: [
                {
                    name: 'id'
                },
                {
                    name: 'text'
                },
                {
                    name: 'sort',
                    type: 'int'
                },
                {
                    name: 'checked',
                    type: 'boolean'
                }
                ],
                proxy :
                {
                    type : 'ajax',
                    url : ctxp+'/p/user!tree.do', 
                    listeners: {
                        exception: {
                            fn: Common.onAjaxException,
                            scope: me
                        }
                    }             
                }                      
            });
        var tree = Ext.create('Ext.tree.Panel',
            {
                region: 'center',
                id: 'RoleTreePanel',
                title: '用户角色',
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
                multiSelect : false,
                store:myStore,
                root :
                {
                    expanded : true
                },
                listeners: {
                    itemclick: 
                    {
                        fn: me.onTreeItemClick,
                        scope: me
                    },
                    checkchange: 
                    {
                        fn: Common.onTreePanelCheckChange,
                        scope: me
                    }
                },        
                columns : [
                {
                    xtype : 'treecolumn',
                    dataIndex : 'text',
                    text : '角色名称',
                    sortable : false,
                    flex : 1,
                    width:300,
                    menuDisabled : true
                },
                {
                    xtype : 'gridcolumn',
                    dataIndex : 'sort',
                    text : '排列顺序'
                }]
            });
        tree.expandAll();
        Ext.getCmp('UserCenterPanel').remove(1);
        Ext.getCmp('UserCenterPanel').add(tree);

    },

    saveForm: function() {
        var me = this;
        if (Ext.getCmp('UserForm').isValid())
        {
            var userId = Ext.getCmp('user.id').getValue();
            var name = Ext.getCmp('user.name').getValue();                
            var username = Ext.getCmp('user.username').getValue();
            var pkIds = [];
            var tree = Ext.getCmp('RoleTreePanel');
            var items = tree.getSelectionModel().store.data.items;
            Ext.each(items, function()
            {
                var nd = this;
                if(nd.data.checked)
                {
                    pkIds.push(nd.data.id);
                }
            });
            var wait = Ext.Msg.wait("正在加载......", "操作提示");
            Ext.Ajax.request(
            {
                url : ctxp+'/p/user!save.do',
                method : 'POST',
                params:{
                    'user.id':userId,
                    'user.name':name,
                    'user.username':username,
                    'roleIds':pkIds
                },  
                success : function(response, options)
                {
                    wait.close();
                    var result = Ext.JSON.decode(response.responseText); 
                    if(result.success)
                    {
                        me.loadGrid();
                        me.loadFormData('');
                    }
                    else
                    {
                        Ext.Msg.alert('信息提示', result.data);
                    }
                },
                failure : function(response, options)
                {
                    wait.close();
                    if(response.status == 200)
                    {
                        var result = Ext.JSON.decode(response.responseText);
                        Ext.Msg.alert('信息提示', result.data);
                    }
                    else
                    {
                        Ext.Msg.alert('信息提示', response.responseText);
                    }
                }
            });
        }
    },

    userStatus: function(status) {
        var me = this;
        var selected = Ext.getCmp('UserGridPanel').getSelectionModel().selected;
        var selecteditems = selected.items;
        if (selecteditems.length === 0)
        {
            Ext.Msg.show(
            {
                title : "操作提示",
                msg : "请选择节点!",
                icon : Ext.Msg.WARNING
            });
            return;
        }
        var ids = [];
        Ext.each(selecteditems, function()
        {
            var nd = this;
            ids.push(nd.data.id);
        });
        Ext.Msg.confirm("警告", "确定要执行吗？", function(button)
        {
            if (button == "yes")
            {
                Ext.Msg.wait("正在执行......", "操作提示");
                Ext.Ajax.request(
                {
                    url : ctxp+'/p/user!'+status+'.do',
                    method : 'POST',
                    params :
                    {
                        'user.id' : ids.join(",")
                    },
                    success : function(response, options)
                    {
                        Ext.Msg.alert("操作提示", "操作成功");
                        me.loadGrid();
                        me.loadFormData('');
                    },
                    failure : function(response, options)
                    {
                        Ext.Msg.alert("操作提示", "操作失败<br/>"+response.responseText);
                    }
                });
            }
        });
    },

    onTreeItemClick: function(dataview, record, item, index, e, eOpts) {
        if(record.data.checked)
        {
            record.set('checked', false);
            Common.onTreePanelCheckChange(record,false,eOpts);
        }
        else
        {
            record.set('checked', true);
            Common.onTreePanelCheckChange(record,true,eOpts);
        }
    }

});