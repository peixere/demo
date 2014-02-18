Ext.define('Gotom.view.TreeComboBox', {
    extend: 'Ext.form.field.ComboBox',

    url: '',
    selected:[],
    selectedText:[],
    tree: {},
    //textProperty: 'text',
    //valueProperty: '',
    bodyBorder: false,
    
    initComponent: function () {
        Ext.apply(this, {
            editable: false,
            queryMode: 'local',
            select: Ext.emptyFn
        });

        this.displayField = this.displayField || 'text',
        this.treeid = Ext.String.format('tree-combobox-{0}', Ext.id());
        this.tpl = Ext.String.format('<div id="{0}"></div>', this.treeid);

        if (this.url) {
            var me = this;
            var store = Ext.create('Ext.data.TreeStore', {
                defaultRootId: '',
                defaultRootText: '',
                nodeParam: 'id',        	
                root: { expanded: true },
                proxy: {
                    type: 'ajax',
                    url: me.url,
                    reader: {
                        type: 'json',
                        idProperty: 'id'
                    }  
                }
                //proxy: { type: 'ajax', url: this.url }
            });
            this.tree = Ext.create('Ext.tree.Panel', {
                rootVisible: false,
                autoScroll: true,
                height: 200,
                store: store
            });
            this.tree.on('itemclick', function (view, record) {
        	for(var i = 0;i<me.selected.length;i++){
        	    if(me.selected[i] === record.data.id){
        		me.selected.splice(i,1);
        		me.selectedText.splice(i,1);
        		me.setValue(me.selectedText);
        		record.set('checked', false);
        		return;
        	    }
        	}
        	record.set('checked', true);
        	me.selectedText.push(record.data.text);
        	me.selected.push(record.data.id);
        	me.setValue(me.selectedText);
                //me.collapse();
            });
            me.on('expand', function () {
                if (!this.tree.rendered) {
                    this.tree.render(this.treeid);
                }
            });
        }
        this.callParent(arguments);
    }
});
