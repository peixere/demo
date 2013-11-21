
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
        'OrganizationCRUB'
    ],
    appFolder: 'classes',
    appProperty: 'classes',
    autoCreateViewport: false,
    name: 'Gotom'
});
Ext.onReady(function()
{
	Ext.create('Gotom.view.OrganizationCRUB');
});