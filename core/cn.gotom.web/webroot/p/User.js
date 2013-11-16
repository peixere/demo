//@require @packageOverrides
Ext.Loader.setConfig({
    enabled: true
});

Ext.application({
    views: [
        'UserView',
        'UserPanel'
    ],
    appFolder: 'classes',
    appProperty: 'classes',
    autoCreateViewport: false,
    name: 'Gotom'
});
Ext.onReady(function()
{
    Ext.create('Gotom.view.UserView');
});