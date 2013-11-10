/*
 * File: classes/view/BuildingShartsPanel.js
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

Ext.define('ems.view.BuildingShartsPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.BuildingShartsPanel',

    id: 'BuildingShartsPanel',
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
                    id: 'BuildingShartsTreePanel',
                    maxWidth: 400,
                    minWidth: 100,
                    width: 150,
                    collapsed: false,
                    collapsible: true,
                    title: '建筑能耗报表',
                    store: 'BuildingShartsTreeStore',
                    rootVisible: false,
                    viewConfig: {

                    },
                    listeners: {
                        itemclick: {
                            fn: me.onBuildingShartsTreePanelItemClick,
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
                                    id: 'id',
                                    fieldLabel: 'Label',
                                    name: 'id'
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
                            html: '<div id="heightchartcontainer" style="min-width: 310px; width: 100%;height: 100%; margin: 0 auto"></div>',
                            id: 'ContentPanel'
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    },

    onBuildingShartsTreePanelItemClick: function(dataview, record, item, index, e, eOpts) {

        Ext.getCmp('name').setValue(record.data.text);
        Ext.getCmp('id').setValue(record.data.id);
    },

    onToolClick: function(tool, e, eOpts) {
        var store = Ext.getCmp('BuildingShartsTreePanel').getStore();
        store.getRootNode().removeAll();
        store.load();
    },

    onBtnRedClick: function(button, e, eOpts) {
        window.location.reload();
    },

    onBtnSearchClick: function(button, e, eOpts) {
        var me = this;
        //var heightchartcontainer = $('#heightchartcontainer');
        //var id = Ext.getCmp('id').getValue();
        //var name = Ext.getCmp('name').getValue();
        //var startDate = Ext.getCmp('startDate').getValue();
        //var endDate = Ext.getCmp('endDate').getValue();
        //var formdata = 'id='+id+'&name='+name+'&startDate='+startDate+'&endDate='+endDate;
        //var html = '<iframe width="100%" height="100%" frameborder="0" src="chart.html?'+formdata+'"></iframe>';

        var form = Ext.getCmp('FormPanel');
        if (form.isValid())
        {
            form.submit({
                url : '../BuildingSharts.do',
                method : 'POST',
                waitMsg : '正在生成报表，稍后...',
                success : function(f, action)
                {
                    var result = Ext.JSON.decode(action.response.responseText);
                    me.showHighcharts(result.data);
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

    showHighcharts: function(chartdata) {
        var html = '<div id="heightchartcontainer" width="100%" height="100"></div>';
        Ext.getCmp('ContentPanel').update(html);
        var options = {
            chart: {
                renderTo: 'heightchartcontainer',
                type: 'line'
            },
            title: {
                text: chartdata.title
            },
            subtitle: {
                text: chartdata.subtitle
            },		
            xAxis: {
                //type: 'datetime',
                type: 'int',
                dateTimeLabelFormats: {
                    month: '%e. %b',
                    year: '%b'
                }
            },
            yAxis: {
                title: {
                    text: chartdata.yAxisText
                }
            },	    
            tooltip:{
                formatter: function() {
                    return '<b>'+ this.series.name +'</b><br/>'+
                    Highcharts.dateFormat('%e. %b', this.x) +': '+ this.y;
                }
            },    
            series: []
        };			   
        var seriesList = chartdata.series;
        $.each(seriesList, function (i, serie) {
            options.series.push({
                data: [],
                name: serie.name
            });
            var plist = serie.data;
            $.each(plist, function (j, p){
                var x = new Date(p.x);
                var year = x.getYear();
                var month = x.getMonth();
                var date = x.getDate();
                //options.series[i].data.push([Date.UTC(year+1900,  month, date),p.y]); 
                options.series[i].data.push([j,p.y]); 
            });
        });
        new Highcharts.Chart(options);
    }

});