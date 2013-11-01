/*
 * File: classes/view/OrganizationTreeGrid.js
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

Ext.define('Gotom.view.OrganizationTreeGrid', {
    extend: 'Ext.tree.Panel',
    alias: 'widget.OrganizationTreeGrid',

    requires: [
        'Gotom.view.OrganizationWinForm',
        'Gotom.model.OrganizationModel',
        'Gotom.store.OrganizationTreeStore'
    ],

    title: '组织架构管理',
    store: 'OrganizationTreeStore',
    rootVisible: false,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            viewConfig: {

            },
            columns: [
                {
                    xtype: 'treecolumn',
                    dataIndex: 'text',
                    text: '组织名称',
                    flex: 1
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'code',
                    text: '组织编码'
                }
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    id: 'OrganizationTreeGridTBar',
                    layout: {
                        padding: 3,
                        type: 'hbox'
                    },
                    items: [
                        {
                            xtype: 'button',
                            id: 'btnNew',
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
                            id: 'btnEdit',
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
                            id: 'btnDel',
                            iconCls: 'icon-del',
                            text: '删除',
                            listeners: {
                                click: {
                                    fn: me.onBtnDelClick,
                                    scope: me
                                }
                            }
                        }
                    ]
                }
            ],
            listeners: {
                itemdblclick: {
                    fn: me.onTreepanelItemDblClick,
                    scope: me
                }
            }
        });

        me.callParent(arguments);
    },

    onBtnNewClick: function(button, e, eOpts) {
        var selected = this.getSelectionModel().selected;
        var record = selected.items[0];
        var pid = '';
        if(!Ext.isEmpty(record))
        {
            pid = record.data.id;
        }
        this.openWinForm('',pid);
    },

    onBtnEditClick: function(button, e, eOpts) {
        var selected = this.getSelectionModel().selected;
        var record = selected.items[0];
        if(!Ext.isEmpty(record))
        {
            this.openForm(record.data.id,'');
        }
    },

    onBtnDelClick: function(button, e, eOpts) {

    },

    onTreepanelItemDblClick: function(dataview, record, item, index, e, eOpts) {
        this.openWinForm(record.data.id,record.data.parentId);
    },

    openWinForm: function(id, parentId) {
        var winform = Ext.create('Gotom.view.OrganizationWinForm');
        winform.getForm().bindData(id,parentId);
        winform.show();
    }

});