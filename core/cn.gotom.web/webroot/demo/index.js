/*
 * File: app.js
 *
 * This file was generated by Sencha Architect version 2.2.2.
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

//@require @packageOverrides
Ext.Loader.setConfig({
    enabled: true
});

Ext.application({
    models: [
        'DemoModel'
    ],
    stores: [
        'TreeStore'
    ],
    views: [
        'DemoViewport',
        'DemoWindow'
    ],
    autoCreateViewport: false,
    name: 'MyApp'
});
var id = getQueryParam('id');
Ext.onReady(function()
{
    var view = Ext.create('MyApp.view.DemoViewport');    
    view.loadData(id);
    //view.expandAll();
});