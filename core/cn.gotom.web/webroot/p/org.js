
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
    autoCreateViewport: false,
    name: 'Gotom'
});
Ext.onReady(function()
{
    var crub = Ext.create('Gotom.view.OrganizationCRUB');
    crub.getTreeGrid().expandAll();
});