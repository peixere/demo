/*
 * File: classes/view/BuildingPanel.js
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

Ext.define('ems.view.BuildingPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.buildingpanel',

    requires: [
        'ems.view.BuildingGrid'
    ],

    id: 'BuildingPanel',
    layout: {
        type: 'border'
    },
    animCollapse: false,
    collapsed: false,
    header: false,
    title: '建筑信息管理',
    titleCollapse: false,

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
                    collapsible: true,
                    title: '建筑信息管理',
                    store: 'BuildingTreeStore',
                    rootVisible: false,
                    viewConfig: {

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
                    ],
                    listeners: {
                        itemclick: {
                            fn: me.onBuildingTreePanelItemClick,
                            scope: me
                        }
                    }
                },
                {
                    xtype: 'form',
                    region: 'center',
                    id: 'BuildingForm',
                    layout: {
                        type: 'border'
                    },
                    bodyPadding: 0,
                    header: false,
                    title: 'BuildingForm',
                    items: [
                        {
                            xtype: 'BuildingGrid',
                            region: 'center'
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    },

    onToolClick: function(tool, e, eOpts) {
        //alert('aaa');
        var store = Ext.getCmp('BuildingTreePanel').getStore();
        store.getRootNode().removeAll();
        store.load();
    },

    onBuildingTreePanelItemClick: function(dataview, record, item, index, e, eOpts) {

        //Ext.getCmp('name').setValue(record.data.text);
        Ext.getCmp('id').setValue(record.data.id);

        var winform = Ext.getCmp('BuildingGrid');
        alert(winform);
        winform.bindBfData(record.data.id);
    }

});