/*
 * File: classes/view/PortalView.js
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

Ext.define('Gotom.view.PortalView', {
    extend: 'Ext.container.Viewport',
    alias: 'widget.PortalView',

    id: 'app-viewport',
    padding: '0 5 5 5',
    layout: {
        type: 'border'
    },

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'panel',
                    region: 'north',
                    border: false,
                    height: 60,
                    id: 'app-header',
                    animCollapse: false,
                    title: ''
                },
                {
                    xtype: 'panel',
                    region: 'west',
                    split: true,
                    id: 'app-options',
                    maxWidth: 450,
                    minWidth: 100,
                    width: 180,
                    collapsed: false,
                    collapsible: true,
                    title: '平台功能',
                    tools: [
                        {
                            xtype: 'tool',
                            id: 'MenuTool',
                            type: 'refresh'
                        }
                    ]
                },
                {
                    xtype: 'tabpanel',
                    region: 'center',
                    split: true,
                    id: 'app-tab'
                },
                {
                    xtype: 'panel',
                    region: 'south',
                    border: false,
                    height: 30,
                    id: 'app-footer'
                }
            ]
        });

        me.callParent(arguments);
    }

});