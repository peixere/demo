/*
 * File: classes/view/BuildingGrid.js
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

Ext.define('ems.view.BuildingGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.BuildingGrid',

    autoRender: true,
    height: 461,
    id: 'BuildingGrid',
    width: 914,
    header: false,
    title: '建筑信息管理',
    enableColumnHide: false,
    enableColumnMove: false,
    forceFit: true,
    store: 'BuildingStore',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            columns: [
                {
                    xtype: 'gridcolumn',
                    defaultWidth: 60,
                    dataIndex: 'code',
                    text: '编码'
                },
                {
                    xtype: 'gridcolumn',
                    defaultWidth: 180,
                    dataIndex: 'name',
                    text: '名称'
                },
                {
                    xtype: 'numbercolumn',
                    dataIndex: 'buildYear',
                    text: '建设年代'
                },
                {
                    xtype: 'numbercolumn',
                    dataIndex: 'buildFloor',
                    text: '建筑层数（层）'
                },
                {
                    xtype: 'numbercolumn',
                    autoRender: false,
                    dataIndex: 'buildArea',
                    text: '建筑总面积（平方米）'
                },
                {
                    xtype: 'numbercolumn',
                    dataIndex: 'airconditioner',
                    text: '空调总面积（平方米）'
                },
                {
                    xtype: 'numbercolumn',
                    dataIndex: 'heatingArea',
                    text: '采暖总面积（平方米）'
                }
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    items: [
                        {
                            xtype: 'button',
                            id: 'btnBdRef',
                            iconCls: 'icon-refresh',
                            text: '刷新',
                            listeners: {
                                click: {
                                    fn: me.onBtnBdRefClick,
                                    scope: me
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'btnBfNew',
                            iconCls: 'icon-add',
                            text: '新增',
                            listeners: {
                                click: {
                                    fn: me.onBtnBfAddClick,
                                    scope: me
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'btnBfEdit',
                            iconCls: 'icon-edit',
                            text: '修改',
                            listeners: {
                                click: {
                                    fn: me.onBtnBfEditClick,
                                    scope: me
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            id: 'btnBfDel',
                            iconCls: 'icon-del',
                            text: '删除',
                            listeners: {
                                click: {
                                    fn: me.onBtnBfDelClick,
                                    scope: me
                                }
                            }
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    },

    onBtnBdRefClick: function(button, e, eOpts) {
        window.location.reload();
    },

    onBtnBfAddClick: function(button, e, eOpts) {
        var id = Ext.getCmp('params').getValue();  
        //alert('add-id:'+id); 
        this.openWinForm(id,'');
    },

    onBtnBfEditClick: function(button, e, eOpts) {
        var selected = this.getSelectionModel().selected;
        var record = selected.items[0];
        if(!Ext.isEmpty(record))
        {
            var id = getCmp('params').getValue();
            this.openWinForm(id, record.data.id);
        }
        else
        { 
            Ext.Msg.show(
            {
                title : "操作提示",
                msg : "请选择要编辑的项!",
                icon : Ext.Msg.WARNING
            }); 
            return;
        }
    },

    onBtnBfDelClick: function(button, e, eOpts) {
        var selected = this.getSelectionModel().selected;
        var selecteditems = selected.items;

        if (selecteditems.length == 0)
        {
            Ext.Msg.show(
            {
                title : "操作提示",
                msg : "请选择要删除的项!",
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
        Ext.Msg.confirm("警告", "确定要删除吗？", function(button)
        {
            if (button == "yes")
            {
                Ext.Msg.wait("正在执行......", "操作提示");
                Ext.Ajax.request(
                {
                    url : '../build!remove.do',
                    method : 'POST',
                    params :
                    {
                        bid : ids.join(",")
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
    },

    openWinForm: function(id, bid) {
        //alert("open-id:"+id+"--params: "+Ext.getCmp('params').getValue()); 
        var winform = Ext.create('ems.view.BuildingWin'); 
        winform.getForm().bindData(id, bid);
        winform.show();
    },

    refreshBuildings: function(id) {
        //alert("refreshBuildings-id:"+id); 
        var store = Ext.getCmp('BuildingGrid').getStore();
        //store.removeAll();
        //store.load();
        store.load({
            params: {id:id},
            callback: function(records, options, success){
                //Ext.Msg.alert('info', '加载完毕,id:'+id);
            },
            scope: store,
            add: false
        });


    }

});