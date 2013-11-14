/*
 * File: classes/view/EnergyCollectPanel.js
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

Ext.define('ems.view.EnergyCollectPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.EnergyCollectPanel',

    requires: [
        'ems.model.EnergyConsumptionCollectModel'
    ],

    id: 'EnergyCollectPanel',
    layout: {
        type: 'border'
    },

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'treepanel',
                    region: 'west',
                    split: true,
                    id: 'BuildingTreePanel',
                    maxWidth: 400,
                    minWidth: 100,
                    width: 150,
                    collapsed: false,
                    collapsible: true,
                    title: '建筑能耗数据采集',
                    store: 'BuildingTreeStore',
                    rootVisible: false,
                    viewConfig: {

                    },
                    listeners: {
                        itemclick: {
                            fn: me.onBuildingTreePanelItemClick,
                            scope: me
                        }
                    },
                    tools: [
                        {
                            xtype: 'tool',
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
                    id: 'CenterPanel',
                    layout: {
                        type: 'border'
                    },
                    items: [
                        {
                            xtype: 'form',
                            region: 'north',
                            height: 101,
                            id: 'FormPanel',
                            layout: {
                                type: 'absolute'
                            },
                            bodyPadding: 10,
                            collapsed: false,
                            collapsible: true,
                            header: false,
                            title: '查询条件',
                            titleCollapse: false,
                            dockedItems: [
                                {
                                    xtype: 'toolbar',
                                    dock: 'top',
                                    id: 'toolbar',
                                    items: [
                                        {
                                            xtype: 'button',
                                            id: 'btnRed',
                                            iconCls: 'icon-refresh',
                                            text: '刷新',
                                            listeners: {
                                                click: {
                                                    fn: me.onBtnRedClick,
                                                    scope: me
                                                }
                                            }
                                        },
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
                                            id: 'btnDel',
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
                                            id: 'btnSearch',
                                            iconCls: 'icon-search',
                                            text: '查询',
                                            listeners: {
                                                click: {
                                                    fn: me.onBtnSearchClick,
                                                    scope: me
                                                }
                                            }
                                        }
                                    ]
                                }
                            ],
                            items: [
                                {
                                    xtype: 'textfield',
                                    x: 10,
                                    y: 10,
                                    id: 'name',
                                    width: 380,
                                    fieldLabel: '建筑标识',
                                    labelAlign: 'right',
                                    labelWidth: 80,
                                    name: 'name'
                                },
                                {
                                    xtype: 'datefield',
                                    x: 10,
                                    y: 40,
                                    id: 'startDate',
                                    width: 220,
                                    fieldLabel: '查询时间',
                                    labelAlign: 'right',
                                    labelWidth: 80,
                                    name: 'startDate'
                                },
                                {
                                    xtype: 'datefield',
                                    x: 230,
                                    y: 40,
                                    id: 'endDate',
                                    width: 160,
                                    fieldLabel: '至',
                                    labelAlign: 'right',
                                    labelSeparator: ' ',
                                    labelWidth: 20,
                                    name: 'endDate'
                                },
                                {
                                    xtype: 'hiddenfield',
                                    x: 417,
                                    y: 13,
                                    id: 'buildingId',
                                    fieldLabel: 'Label',
                                    name: 'buildingId'
                                }
                            ],
                            listeners: {
                                afterlayout: {
                                    fn: me.onFormPanelAfterLayout,
                                    scope: me
                                }
                            }
                        },
                        {
                            xtype: 'panel',
                            region: 'center',
                            id: 'ContentPanel',
                            layout: {
                                type: 'border'
                            },
                            items: [
                                {
                                    xtype: 'gridpanel',
                                    region: 'center',
                                    id: 'GataGridPanel',
                                    autoScroll: true,
                                    collapsible: true,
                                    title: '查询结果',
                                    store: 'MyJsonStore',
                                    columns: [
                                        {
                                            xtype: 'datecolumn',
                                            width: 160,
                                            defaultWidth: 200,
                                            dataIndex: 'collectDate',
                                            text: '采集时间',
                                            format: 'Y-m-d H:i:s'
                                        },
                                        {
                                            xtype: 'gridcolumn',
                                            sortable: false,
                                            dataIndex: 'electric',
                                            text: '耗电量(度)'
                                        },
                                        {
                                            xtype: 'gridcolumn',
                                            dataIndex: 'water',
                                            text: '用水量(吨)'
                                        },
                                        {
                                            xtype: 'gridcolumn',
                                            dataIndex: 'gas',
                                            text: '燃气(立方)'
                                        },
                                        {
                                            xtype: 'numbercolumn',
                                            dataIndex: 'heating',
                                            text: '集中供热量'
                                        },
                                        {
                                            xtype: 'numbercolumn',
                                            dataIndex: 'cooling',
                                            text: '集中供冷量'
                                        },
                                        {
                                            xtype: 'numbercolumn',
                                            dataIndex: 'otherEnergy',
                                            text: '其它能源'
                                        },
                                        {
                                            xtype: 'numbercolumn',
                                            dataIndex: 'coal',
                                            text: '煤'
                                        },
                                        {
                                            xtype: 'numbercolumn',
                                            dataIndex: 'lpg',
                                            text: '液化石油气'
                                        },
                                        {
                                            xtype: 'numbercolumn',
                                            dataIndex: 'manufacturedGas',
                                            text: '人工煤气'
                                        },
                                        {
                                            xtype: 'numbercolumn',
                                            dataIndex: 'gasoline',
                                            text: '汽油'
                                        },
                                        {
                                            xtype: 'numbercolumn',
                                            dataIndex: 'kerosene',
                                            text: '煤油'
                                        }
                                    ],
                                    viewConfig: {
                                        listeners: {
                                            itemclick: {
                                                fn: me.onViewItemClick,
                                                scope: me
                                            },
                                            itemdblclick: {
                                                fn: me.onViewItemDblClick,
                                                scope: me
                                            }
                                        }
                                    },
                                    selModel: Ext.create('Ext.selection.RowModel', {

                                    })
                                }
                            ]
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    },

    onBuildingTreePanelItemClick: function(dataview, record, item, index, e, eOpts) {
        Ext.getCmp('name').setValue(record.data.text);
        Ext.getCmp('buildingId').setValue(record.data.id);
    },

    onToolClick: function(tool, e, eOpts) {

    },

    onBtnRedClick: function(button, e, eOpts) {
        window.location.reload();
    },

    onBtnNewClick: function(button, e, eOpts) {
        this.showWinForm('');
    },

    onBtnDelClick: function(button, e, eOpts) {
        var me = this;
        var id = '';
        var wait = Ext.Msg.wait("正在删除......", "操作提示");
        Ext.Ajax.request(
        {
            url : '../EnergyCollect!remove.do',
            method : 'POST',
            params:{  
                id:id,
            },  
            success : function(response, options)
            {
                wait.close();
                var result = Ext.JSON.decode(response.responseText); 
                Ext.Msg.alert('信息提示', result.data);
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
        this.onBtnSearchClick(button,e,eOpts);
    },

    onBtnSearchClick: function(button, e, eOpts) {
        var me = this;
        var form = Ext.getCmp('FormPanel');
        if (form.isValid())
        {
            form.submit({
                url : '../EnergyCollect!list.do',
                method : 'POST',
                waitMsg : '正在生成报表，稍后...',
                success : function(f, action)
                {
                    var result = Ext.JSON.decode(action.response.responseText);
                    me.loadGridData(result.data);
                    //me.showHighcharts(result.data);
                },
                failure : function(f, action)
                {
                    if(action.response.status == 200)
                    {
                        var result = Ext.JSON.decode(action.response.responseText);
                        Ext.Msg.alert('信息提示', result.data);
                    }
                    else
                    {
                        Ext.Msg.alert('信息提示', action.response.responseText);
                    }
                }
            });
        }
    },

    onFormPanelAfterLayout: function(container, layout, eOpts) {
        Ext.getCmp('startDate').setValue(new Date());
        Ext.getCmp('endDate').setValue(new Date());
    },

    onViewItemClick: function(dataview, record, item, index, e, eOpts) {

    },

    onViewItemDblClick: function(dataview, record, item, index, e, eOpts) {
        this.showWinForm(record);
    },

    loadGridData: function(data) {
        /**
        var json_data={'out':[
        {"context":"Lisa", "reductions":"lisa@simpsons.com", "phone":"555-111-1224"},
        {"context":"Bart", "reductions":"bart@simpsons.com", "phone":"555--222-1234"},
        {"context":"Homer", "reductions":"home@simpsons.com", "phone":"555-222-1244"},
        {"context":"Marge", "reductions":"marge@simpsons.com", "phone":"555-222-1254"}
        ]};

        var jsonStore = Ext.create('Ext.data.Store', {
        storeId:'jsonStore',
        fields: ['context', 'reductions','phone'],
        data : json_data,        
        proxy:
        {
        type: 'memory',
        reader:{
        type: 'json',
        root: 'out'
        }
        }
        });    
        */

        var jsonStore = Ext.create('Ext.data.Store', {
            storeId:'jsonStore',
            model: 'ems.model.EnergyConsumptionCollectModel',
            data : data,
            proxy:
            {
                type: 'memory',
                reader:{
                    type: 'json',
                }
            }    
        });
        Ext.getCmp('GataGridPanel').bindStore(jsonStore);           
    },

    showWinForm: function(record) {
        var winform = Ext.create('ems.view.EnergyCollectWindow');
        var buildingId = Ext.getCmp('buildingId').getValue();
        var name = Ext.getCmp('name').getValue();
        winform.show();
        var id = '';
        if(record !== '')
        {
            id = record.data.id;
        }
        winform.bindFields(buildingId,name,id);

    }

});