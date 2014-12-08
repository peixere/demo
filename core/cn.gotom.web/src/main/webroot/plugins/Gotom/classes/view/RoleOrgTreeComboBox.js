Ext.define('Gotom.view.RoleOrgTreeComboBox', {
			extend : 'Ext.form.field.ComboBox',

			url : '',
			selected : '',
			tree : {},
			// textProperty: 'text',
			// valueProperty: '',
			bodyBorder : false,

			initComponent : function() {
				Ext.apply(this, {
							editable : false,
							queryMode : 'local',
							select : Ext.emptyFn
						});
				// this.displayField = this.displayField || 'text',
				this.treeid = Ext.String.format('tree-combobox-{0}', Ext.id());
				this.tpl = Ext.String.format('<div id="{0}"></div>',
						this.treeid);

				if (this.url) {
					var me = this;
					var store = Ext.create('Ext.data.TreeStore', {
								defaultRootId : '',
								defaultRootText : '',
								nodeParam : 'id',
								root : {
									expanded : true
								},
								proxy : {
									type : 'ajax',
									url : me.url,
									reader : {
										type : 'json',
										idProperty : 'id'
									}
								}
							});
					this.tree = Ext.create('Ext.tree.Panel', {
								rootVisible : false,
								autoScroll : true,
								height : 200,
								store : store
							});
					this.tree.on('itemclick', function(view, record) {
								me.selected = record.data.id;
								me.setValue(record.data.text);
								me.collapse();
							});
					me.on('expand', function() {
								if (!this.tree.rendered) {
									this.tree.render(this.treeid);
								}
							});
				}
				this.callParent(arguments);
			}
		});
