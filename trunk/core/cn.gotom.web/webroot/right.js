Ext.define('Ext.app.RightWindow', {
    extend: 'Ext.window.Window',

    height: 250,
    width: 400,
    layout: {
        type: 'border'
    },
    title: '菜单编辑',
    titleCollapse: false,
    modal: true,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'rightform',
                    region: 'center'
                }
            ]
        });

        me.callParent(arguments);
    }

});