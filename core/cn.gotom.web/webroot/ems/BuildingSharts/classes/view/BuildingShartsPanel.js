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

    requires: [
        'ems.view.BuildingShartsTreePanel'
    ],

    id: 'BuildingShartsPanel',
    layout: {
        type: 'border'
    },

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'BuildingShartsTreePanel',
                    store: 'TreeStore',
                    region: 'west',
                    split: true
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
                            height: 93,
                            id: 'FormPanel',
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
                                            id: 'btnSearch',
                                            iconCls: 'icon-search',
                                            text: '查询'
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            xtype: 'panel',
                            region: 'center',
                            id: 'ContentPanel'
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    },

    onBuildingShartsTreePanelItemClick: function(dataview, record, item, index, e, eOpts) {
        alert('');
    },

    getTreeStore: function(id, type, treeModel) {
        return Ext.create("Ext.data.TreeStore",
        {
            defaultRootId : id,
            model : treeModel,
            proxy :
            {
                type : "ajax",
                url : '../buildingSharts!tree.do?action='+type
                //url : '/p/right!tree.do?action='+type
            },
            clearOnLoad : true,
            nodeParam : "id"
        });
    }

});