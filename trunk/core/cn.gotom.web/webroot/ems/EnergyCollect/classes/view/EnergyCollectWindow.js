/*
 * File: classes/view/EnergyCollectWindow.js
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

Ext.define('ems.view.EnergyCollectWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.EnergyCollectWindow',

    height: 557,
    id: 'EnergyCollectWindow',
    width: 745,
    layout: {
        type: 'border'
    },
    title: '数据采集',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'bottom',
                    id: 'mytoolbar',
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
                            id: 'btnReset',
                            iconCls: 'icon-reset',
                            text: '重置',
                            listeners: {
                                click: {
                                    fn: me.onBtnResetClick,
                                    scope: me
                                }
                            }
                        }
                    ]
                }
            ],
            items: [
                {
                    xtype: 'form',
                    region: 'center',
                    id: 'ecform',
                    bodyPadding: 10,
                    items: [
                        {
                            xtype: 'textfield',
                            id: 'building.name',
                            fieldLabel: '建筑名称',
                            labelAlign: 'right',
                            name: 'name',
                            readOnly: true
                        },
                        {
                            xtype: 'hiddenfield',
                            id: 'building.id',
                            width: 150,
                            fieldLabel: 'Label',
                            name: 'id'
                        },
                        {
                            xtype: 'datefield',
                            id: 'collectDate',
                            fieldLabel: '采集时间',
                            labelAlign: 'right',
                            name: 'collectDate'
                        },
                        {
                            xtype: 'numberfield',
                            id: 'electric',
                            fieldLabel: '电',
                            labelAlign: 'right',
                            name: 'electric'
                        },
                        {
                            xtype: 'numberfield',
                            id: 'water',
                            fieldLabel: '水',
                            labelAlign: 'right',
                            name: 'water'
                        },
                        {
                            xtype: 'numberfield',
                            id: 'gas',
                            fieldLabel: '燃气',
                            labelAlign: 'right',
                            name: 'gas'
                        },
                        {
                            xtype: 'numberfield',
                            id: 'heating',
                            fieldLabel: '集中供热量',
                            labelAlign: 'right',
                            name: 'heating'
                        },
                        {
                            xtype: 'numberfield',
                            id: 'otherEnergy',
                            fieldLabel: '其它能源',
                            labelAlign: 'right',
                            name: 'otherEnergy'
                        },
                        {
                            xtype: 'numberfield',
                            id: 'coal',
                            fieldLabel: '煤',
                            labelAlign: 'right',
                            name: 'coal'
                        },
                        {
                            xtype: 'numberfield',
                            id: 'lpg',
                            fieldLabel: ' 液化石油气',
                            labelAlign: 'right',
                            name: 'lpg'
                        },
                        {
                            xtype: 'numberfield',
                            id: 'manufacturedGas',
                            fieldLabel: '人工煤气',
                            labelAlign: 'right',
                            name: 'manufacturedGas'
                        },
                        {
                            xtype: 'numberfield',
                            id: 'gasoline',
                            fieldLabel: '汽油',
                            labelAlign: 'right',
                            name: 'gasoline'
                        },
                        {
                            xtype: 'numberfield',
                            id: 'kerosene',
                            fieldLabel: '煤油',
                            labelAlign: 'right',
                            name: 'kerosene'
                        },
                        {
                            xtype: 'numberfield',
                            id: 'solarThermal',
                            fieldLabel: '太阳能光热利用系统集热器面积',
                            labelAlign: 'right',
                            name: 'solarThermal'
                        },
                        {
                            xtype: 'numberfield',
                            id: 'geothermal',
                            fieldLabel: '浅层地热能利用系统装机容量',
                            labelAlign: 'right',
                            name: 'geothermal'
                        },
                        {
                            xtype: 'numberfield',
                            id: 'geothermalAuxiliary',
                            fieldLabel: '浅层地热能利用系统辅助热源供热量',
                            labelAlign: 'right',
                            name: 'geothermalAuxiliary'
                        },
                        {
                            xtype: 'numberfield',
                            id: 'solarPV',
                            fieldLabel: '太阳能光电利用系统装机容量',
                            labelAlign: 'right',
                            name: 'solarPV'
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    },

    onBtnSaveClick: function(button, e, eOpts) {

    },

    onBtnResetClick: function(button, e, eOpts) {

    },

    bindFields: function(id, name) {
        Ext.getCmp('building.id').setValue(id);
        Ext.getCmp('building.name').setValue(name);
    }

});