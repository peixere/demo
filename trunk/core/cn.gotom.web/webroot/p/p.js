/**
 * @class Gotom.view.Portal
 * @extends Object
 * A sample portal layout application class.
 */
Ext.define('Gotom.view.MyPortal', {
    extend: 'Ext.container.Viewport',

    requires: [
        'Gotom.view.Portal'
    ],

    layout: {
        type: 'border'
    },

    initComponent: function() {
        var me = this;

        Ext.apply(me, {
            items: [
                {
                    xtype: 'Portal',
                    region: 'center'
                }
            ]
        });

        me.callParent(arguments);
    }

});
