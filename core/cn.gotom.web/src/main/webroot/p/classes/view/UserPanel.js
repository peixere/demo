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

    border: false,
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
                    border: false,
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
                            iconCls: 'settings',
                            text: '密码',
                            tooltip: '设置为初始密码',
                            listeners: {
                                click: {
                                    fn: me.onResetPassClick,
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
                    bodyBorder: false,
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
                            defaultWidth: 40,
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
                            region: 'center',
                            border: false,
                            id: 'UserForm',
                            autoScroll: true,
                            bodyPadding: 10,
                            title: '编辑用户',
                            items: [
                                {
                                    xtype: 'hiddenfield',
                                    anchor: '100%',
                                    fieldLabel: 'Label',
                                    name: 'id'
                                },
                                {
                                    xtype: 'hiddenfield',
                                    anchor: '100%',
                                    labelWidth: 60,
                                    name: 'orgIds'
                                },
                                {
                                    xtype: 'textfield',
                                    anchor: '100%',
                                    fieldLabel: '登录帐号',
                                    labelWidth: 60,
                                    name: 'username',
                                    allowBlank: false,
                                    enforceMaxLength: true,
                                    maxLength: 50,
                                    minLength: 3
                                },
                                {
                                    xtype: 'textfield',
                                    anchor: '100%',
                                    fieldLabel: '用户名称',
                                    labelWidth: 60,
                                    name: 'name',
                                    allowBlank: false,
                                    enforceMaxLength: false,
                                    maxLength: 50,
                                    minLength: 2
                                },
                                {
                                    xtype: 'textfield',
                                    anchor: '100%',
                                    fieldLabel: '手机号码',
                                    labelWidth: 60,
                                    name: 'mobile',
                                    enforceMaxLength: false,
                                    maxLength: 50,
                                    minLength: 2
                                },
                                {
                                    xtype: 'textfield',
                                    anchor: '100%',
                                    fieldLabel: '工作卡号',
                                    labelWidth: 60,
                                    name: 'cardRFID',
                                    enforceMaxLength: false,
                                    maxLength: 50,
                                    minLength: 2
                                },
                                {
                                    xtype: 'textfield',
                                    anchor: '100%',
                                    fieldLabel: '证件号码',
                                    labelWidth: 60,
                                    name: 'cardId',
                                    enforceMaxLength: false,
                                    maxLength: 50,
                                    minLength: 2
                                },
                                {
                                    xtype: 'checkboxgroup',
                                    id: 'UserRoleCheckboxGroup',
                                    fieldLabel: '用户角色',
                                    labelWidth: 60,
                                    columns: 2,
                                    items: [
                                        {
                                            xtype: 'checkboxfield',
                                            boxLabel: 'Box Label'
                                        },
                                        {
                                            xtype: 'checkboxfield',
                                            boxLabel: 'Box Label'
                                        }
                                    ]
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

    onResetPassClick: function(button, e, eOpts) {
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
        Ext.Msg.confirm("警告", "确定将选取的用户初始密码为123456吗？", function(button)
        {
            if (button == "yes")
            {
                Ext.Msg.wait("正在执行......", "操作提示");
                Ext.Ajax.request(
                {
                    url : ctxp+'/p/user!resetpass.do',
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
        var from = Ext.getCmp('UserForm');
        Common.ajax({
            params:{'user.id':id},
            component : from,
            message : '正在加载......',    
            url : ctxp+'/p/user.do',
            callback : function(result) {
                from.getForm().setValues(result.user);
                from.getForm().findField('orgIds').setValue(result.orgIds);     
                var roleCheckbox = Ext.getCmp('UserRoleCheckboxGroup');
                roleCheckbox.removeAll();
                var items = result.data;
                Ext.each(items, function()
                {
                    var nd = this;
                    roleCheckbox.add({xtype: 'checkboxfield',name: 'roleIds',boxLabel: nd.text,inputValue: nd.id,checked: nd.checked});
                });  
            }
        });
    },

    saveForm: function() {
        var me = this;
        try{
            var form = Ext.getCmp('UserForm');
            Common.formSubmit({  
                url : ctxp+'/p/user!save.do',
                form:form,
                callback : function(result)
                {
                    if(result.success)
                    {
                        me.loadGrid();
                        me.loadFormData('');
                    }else{
                        Ext.Msg.alert('信息提示', result.data);
                    }	
                }
            });
        }catch(error){
            alert(error);
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