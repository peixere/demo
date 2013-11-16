//@require @packageOverrides
Ext.Loader.setConfig({
    enabled: true
});

Ext.application({
    views: [
        'RoleView',
        'RolePanel'
    ],
    appFolder: 'classes',
    appProperty: 'classes',
    autoCreateViewport: false,
    name: 'Gotom'
});
Ext.onReady(function()
{
    Ext.create('Gotom.view.RoleView');
});