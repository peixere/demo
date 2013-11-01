
Ext.Loader.setConfig({
    enabled: true
});
Ext.application({
    models: [
        'OrganizationModel'
    ],
    stores: [
        'OrganizationTreeStore'
    ],
    views: [
        'OrganizationCRUB',
        'OrganizationWinForm'
    ],
    appFolder: 'classes',
    appProperty: 'gotom',
    autoCreateViewport: true,
    name: 'Gotom'
});
Ext.onReady(function()
{
    Ext.create('Gotom.view.OrganizationCRUB');
});