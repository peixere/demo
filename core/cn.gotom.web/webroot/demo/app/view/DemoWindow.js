/*
 * File: app/view/DemoWindow.js
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

Ext.define('MyApp.view.DemoWindow', {
    extend: 'Ext.window.Window',

    height: 396,
    id: 'DemoWindow',
    width: 589,
    layout: {
        type: 'border'
    },
    title: 'DemoWindow',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    region: 'center',
                    id: 'demoForm',
                    layout: {
                        type: 'auto'
                    },
                    bodyPadding: 10,
                    header: false,
                    title: '表单',
                    items: [
                        {
                            xtype: 'combobox',
                            id: 'mycombobox',
                            fieldLabel: 'Label',
                            valueField: 'id'
                        },
                        {
                            xtype: 'combobox',
                            id: 'mycombobox1',
                            fieldLabel: 'Label',
                            displayField: 'optionValue',
                            valueField: 'optionCode'
                        }
                    ]
                }
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    split: false,
                    dock: 'bottom',
                    id: 'formToolbar',
                    layout: {
                        pack: 'end',
                        padding: 3,
                        type: 'hbox'
                    },
                    items: [
                        {
                            xtype: 'button',
                            id: 'btnSave',
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
            ],
            listeners: {
                afterlayout: {
                    fn: me.onDemoWindowAfterLayout,
                    scope: me
                }
            }
        });

        me.callParent(arguments);
    },

    onBtnSaveClick: function(button, e, eOpts) {

    },

    onBtnCancelClick: function(button, e, eOpts) {

    },

    onDemoWindowAfterLayout: function(container, layout, eOpts) {
        var store = new Ext.data.Store({
            autoLoad: true,
            storeId: 'ComBoboxStore',
            fields: [
            {
                name: 'id',
                type: 'string'
            },
            {
                name: 'text',
                type: 'string'
            }
            ],
            proxy: {
                type: 'ajax',
                url : '../p/right!list.do',
                listeners: {
                    exception: function(proxy, response, operation, eOpts) {
                        alert(response.statusText);
                    }
                }
            }
        });
        Ext.getCmp('mycombobox').bindStore(store);

        var store1 = new Ext.data.Store({
            autoLoad: true,
            storeId: 'ComBoboxStore1',
            fields: [
            {
                name: 'optionCode',
                type: 'string'
            },
            {
                name: 'optionValue',
                type: 'string'
            }
            ],
            proxy: {
                type: 'ajax',  
                url : '../build!options.do',
                listeners: {
                    exception: function(proxy, response, operation, eOpts) {
                        alert(response.statusText);
                    }
                }
            }
        });
        Ext.getCmp('mycombobox1').bindStore(store);
    }

});