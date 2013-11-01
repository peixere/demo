/*
 * File: classes/view/OrganizationWinForm.js
 *
 * This file was generated by Sencha Architect version 2.2.2.
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

Ext.define('Gotom.view.OrganizationWinForm', {
    extend: 'Ext.window.Window',

    requires: [
        'Gotom.model.OrganizationModel'
    ],

    height: 179,
    id: 'OrganizationWinForm',
    width: 400,
    layout: {
        type: 'border'
    },
    title: '组织架构',
    modal: true,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    bindData: function(id, parentId) {
                        var formPanel = this;
                        var wait = Ext.Msg.wait("正在载入......", "操作提示");
                        Ext.Ajax.request(
                        {
                            url : '../p/organization.do',
                            method : 'POST',
                            params:{  
                                id:id,
                                parentId:parentId
                            },  
                            success : function(response, options)
                            {
                                var result = Ext.JSON.decode(response.responseText);
                                var record = Ext.create('Gotom.model.OrganizationModel');
                                result.parentId = parentId;
                                record.data = result;
                                wait.close();
                                formPanel.getForm().reset();
                                formPanel.loadRecord(record);   
                            },
                            failure : function(response, options)
                            {
                                wait.close();
                                Ext.Msg.alert("操作提示", "载入失败");
                            }
                        });
                    },
                    region: 'center',
                    id: 'OrganizationForm',
                    bodyPadding: 20,
                    items: [
                        {
                            xtype: 'hiddenfield',
                            anchor: '100%',
                            fieldLabel: '标识',
                            name: 'id'
                        },
                        {
                            xtype: 'hiddenfield',
                            anchor: '100%',
                            fieldLabel: '父节点',
                            name: 'parentId'
                        },
                        {
                            xtype: 'textfield',
                            anchor: '100%',
                            fieldLabel: '组织编码',
                            labelAlign: 'right',
                            labelWidth: 60,
                            name: 'code',
                            invalidText: '组织编码不能为空',
                            blankText: '组织编码不能为空！'
                        },
                        {
                            xtype: 'textfield',
                            anchor: '100%',
                            fieldLabel: '组织名称',
                            labelAlign: 'right',
                            labelWidth: 60,
                            name: 'text',
                            invalidText: '不能为空！',
                            blankText: '不能为空！'
                        },
                        {
                            xtype: 'numberfield',
                            anchor: '100%',
                            fieldLabel: '排列顺序',
                            labelAlign: 'right',
                            labelWidth: 60,
                            name: 'sort',
                            value: 0,
                            maxLength: 6,
                            maxLengthText: '最小值为能< {0}',
                            size: 6,
                            maxText: '最大值为能> {0}',
                            maxValue: 10000,
                            minText: '最小值为能< {0}',
                            minValue: -10000
                        }
                    ],
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'bottom',
                            frame: false,
                            id: 'toobar',
                            shadowOffset: 10,
                            layout: {
                                pack: 'end',
                                padding: 3,
                                type: 'hbox'
                            },
                            items: [
                                {
                                    xtype: 'button',
                                    id: 'btnSave',
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
                                    id: 'btnCancel',
                                    iconCls: 'icon-cancel',
                                    text: '取消',
                                    listeners: {
                                        click: {
                                            fn: me.onBtnCancelClick,
                                            scope: me
                                        }
                                    }
                                }
                            ]
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    },

    onBtnSaveClick: function(button, e, eOpts) {

        if (this.getForm().isValid())
        {
            this.getForm().submit(
            {
                url : '../p/organization!save.do',
                method : 'POST',
                waitMsg : '正在保存数据，稍后...',
                success : function(f, action)
                {
                    Ext.Msg.alert('信息提示', '保存成功');
                    //me.close();
                    window.location.reload();
                },
                failure : function(f, action)
                {
                    Ext.Msg.alert('信息提示', '保存失败，服务器端程序出错！');
                }
            });
        }
    },

    onBtnCancelClick: function(button, e, eOpts) {
        this.close();
    },

    getForm: function() {
        return Ext.getCmp('OrganizationForm');
    }

});