/*
 * File: classes/view/RightPanel.js
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

Ext.define('Gotom.view.RightPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.RightPanel',

    requires: [
        'Gotom.model.RightTreeCheckModel'
    ],

    border: false,
    layout: {
        type: 'border'
    },

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'treepanel',
                    region: 'center',
                    id: 'RightTreePanel',
                    autoScroll: true,
                    rootVisible: false,
                    listeners: {
                        itemdblclick: {
                            fn: me.onRightTreePanelItemDblClick,
                            scope: me
                        },
                        afterlayout: {
                            fn: me.onRightTreePanelAfterLayout,
                            single: true,
                            scope: me
                        }
                    },
                    viewConfig: {
                        id: 'RightTreePanelView',
                        listeners: {
                            itemclick: {
                                fn: me.onRightTreePanelViewItemClick,
                                scope: me
                            }
                        }
                    },
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'top',
                            id: 'RightTreePanelToolbar',
                            items: [
                                {
                                    xtype: 'button',
                                    iconCls: 'icon-add',
                                    text: '添加目录',
                                    listeners: {
                                        click: {
                                            fn: me.onButtonAddClick,
                                            scope: me
                                        }
                                    }
                                },
                                {
                                    xtype: 'button',
                                    iconCls: 'icon-add-p',
                                    text: '添加资源',
                                    listeners: {
                                        click: {
                                            fn: me.onButtonNewClick,
                                            scope: me
                                        }
                                    }
                                },
                                {
                                    xtype: 'button',
                                    iconCls: 'icon-edit',
                                    text: '修改',
                                    listeners: {
                                        click: {
                                            fn: me.onButtonEditClick,
                                            scope: me
                                        }
                                    }
                                },
                                {
                                    xtype: 'button',
                                    iconCls: 'icon-del',
                                    text: '删除',
                                    listeners: {
                                        click: {
                                            fn: me.onButtonDelClick,
                                            scope: me
                                        }
                                    }
                                }
                            ]
                        }
                    ],
                    columns: [
                        {
                            xtype: 'treecolumn',
                            width: 200,
                            sortable: false,
                            dataIndex: 'text',
                            menuDisabled: true,
                            text: '菜单名称',
                            flex: 1
                        },
                        {
                            xtype: 'gridcolumn',
                            dataIndex: 'iconCls',
                            text: '使用样式'
                        },
                        {
                            xtype: 'gridcolumn',
                            width: 200,
                            defaultWidth: 160,
                            dataIndex: 'component',
                            text: '控件或连接'
                        },
                        {
                            xtype: 'gridcolumn',
                            defaultWidth: 200,
                            sortable: false,
                            dataIndex: 'resource',
                            text: '数据资源',
                            flex: 1
                        },
                        {
                            xtype: 'gridcolumn',
                            dataIndex: 'sort',
                            text: '排列顺序'
                        }
                    ],
                    selModel: Ext.create('Ext.selection.RowModel', {

                    })
                }
            ]
        });

        me.callParent(arguments);
    },

    onRightTreePanelItemDblClick: function(dataview, record, item, index, e, eOpts) {
        this.showform(record);
    },

    onRightTreePanelViewItemClick: function(dataview, record, item, index, e, eOpts) {
        var me = this;
        if(record.data.checked)
        {
            record.set('checked', false);
            Gotom.view.Common.onTreePanelCheckChange(record,false);
        }
        else
        {
            record.set('checked', true);
        }
        Gotom.view.Common.onTreeChildNodesChecked(record,record.data.checked);
    },

    onButtonAddClick: function(button, e, eOpts) {
        this.onBtnAddClick(false);
    },

    onButtonNewClick: function(button, e, eOpts) {
        this.onBtnAddClick(true);
    },

    onButtonEditClick: function(button, e, eOpts) {
        var selected = Ext.getCmp('RightTreePanel').getSelectionModel().selected;
        var record = selected.items[0];
        if(!Ext.isEmpty(record))
        {
            this.showform(record);
        }
    },

    onButtonDelClick: function(button, e, eOpts) {
        var ids = [];
        var tree = Ext.getCmp('RightTreePanel');
        var items = tree.getSelectionModel().store.data.items;
        Ext.each(items, function()
        {
            var nd = this;
            if(nd.data.checked)
            {
                ids.push(nd.data.id);
            }
        });
        if (ids.length === 0)
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
                    url : ctxp + '/p/right!remove.do',
                    method : 'POST',
                    params :
                    {
                        id : ids
                    },
                    success : function(response, options)
                    {
                        Ext.Msg.alert("删除提示", "删除成功");
                        tree.getStore().reload();
                        Ext.defer(function(){tree.expandAll();},100);
                    },
                    failure : function(response, options)
                    {
                        Ext.Msg.alert("删除提示", "删除失败");
                    }
                });
            }
        });
    },

    onRightTreePanelAfterLayout: function(container, layout, eOpts) {
        var me = this;
        var myStore = Ext.create("Ext.data.TreeStore",
            {
                defaultRootId : '',
                proxy :
                {
                    type : "ajax",
                    url : ctxp+'/p/right!tree.do'
                },
                clearOnLoad : true,
                model : 'Gotom.model.RightTreeCheckModel',
                nodeParam : "id",
                listeners: {
                    exception: {
                        fn: function(response){Gotom.view.Common.onAjaxException(response)},
                        scope: me
                    }
                }        
            });
        var tree = Ext.getCmp('RightTreePanel');
        tree.bindStore(myStore);
        myStore.reload();
        Ext.defer(function(){tree.expandAll();},100);
    },

    onBtnAddClick: function(leaf) {
        var me = this;
        var selected = Ext.getCmp('RightTreePanel').getSelectionModel().selected;
        var p = selected.items[0];
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
            url : ctxp + '/p/right!fresh.do',
            method : 'POST',
            success : function(response, options)
            {
                var result = Ext.JSON.decode(response.responseText);
                var data = Ext.create('Gotom.model.RightTreeCheckModel');
                result.parentId = parentId;
                data.data = result;
                data.data.leaf = leaf;
                if(leaf){
                    data.data.type = 'URL';
                }else{
                    data.data.type = 'DIR';
                }
                wait.close();
                me.showform(data);
            },
            failure : function(response, options)
            {
                wait.close();
                Ext.Msg.alert("操作提示", "操作失败");
            }
        });
    },

    showform: function(record) {
        if (record !== null && record !== '')
        {
            var formwin = Ext.create('Gotom.view.RightWindow');
            formwin.show();
            formwin.form.getForm().reset();
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

});