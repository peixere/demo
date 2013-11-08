/*
 * File: classes/store/BuildingShartsTreeStore.js
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

Ext.define('ems.store.BuildingShartsTreeStore', {
    extend: 'Ext.data.TreeStore',
    alias: 'store.BuildingShartsTreeStore',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'BuildingShartsTreeStore',
            defaultRootId: '',
            defaultRootText: '',
            nodeParam: 'id',
            proxy: {
                type: 'ajax',
                url: '../BuildingSharts!tree.do',
                listeners: {
                    exception: {
                        fn: me.onAjaxException,
                        scope: me
                    }
                },
                reader: {
                    type: 'json',
                    idProperty: 'id',
                    root: 'responseText'
                }
            },
            fields: [
                {
                    name: 'type',
                    type: 'string'
                },
                {
                    name: 'text',
                    type: 'string'
                },
                {
                    name: 'expand',
                    type: 'boolean'
                }
            ]
        }, cfg)]);
    },

    onAjaxException: function(proxy, response, operation, eOpts) {
        Ext.Msg.alert('操作提示',response.status);
    }

});