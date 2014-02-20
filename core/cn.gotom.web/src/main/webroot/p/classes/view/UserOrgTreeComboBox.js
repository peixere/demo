Ext.define('Gotom.view.UserOrgTreeComboBox', {
    extend: 'Ext.form.field.ComboBox',

    url: '',
    selected:[],
    selectedText:[],
    tree: {},
    //textProperty: 'text',
    //valueProperty: '',
    bodyBorder: false,
    treeRender: function () {
    	if (!this.tree.rendered) {
	        this.tree.render(this.treeid);
	    }
    },
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
	        	var selectedIds = [];
	        	var selectedTexts = [];
	        	var checked = !record.data.checked;
	        	record.set('checked', checked);
	        	//Common.onTreePanelCheckChange(record,false);
	            Common.onTreeChildNodesChecked(record,false);
	            Common.setTreeParentNodeChecked(record,false);
	            var items = view.getSelectionModel().store.data.items;
	            Ext.each(items, function()
	            {
	                var nd = this;
	                if(nd.data.checked)
	                {
	                	selectedIds.push(nd.data.id);
	                	selectedTexts.push(nd.data.text);
	                }
	            });         	
	        	me.selectedText = selectedTexts;
	        	me.selected = selectedIds;
	        	me.setValue(selectedTexts);
                //me.collapse();
            });
            me.on('expand', function () {
                me.treeRender();
            });
        }
        this.callParent(arguments);
    }
});
