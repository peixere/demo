var Common = {
	show : function(conf) {
		var p = Ext.getCmp('app-viewport');
		if (!Ext.isEmpty(p)) {
			p.showNotice(conf);
		} else {
			Ext.MessageBox.show({
						title : '操作提示',
						msg : '找不到控件：app-viewport',
						icon : Ext.MessageBox.ERROR
					});
		}
	},

	addTabURL : function(conf) {
		var p = Ext.getCmp('app-viewport');
		if (!Ext.isEmpty(p)) {
			var panel = '';
			var tabPanel = p.tabPanel;
			for (var i = 0; i < tabPanel.items.length; i++) {
				if (tabPanel.items.get(i).id === conf.id) {
					panel = tabPanel.items.get(i);
					break;
				}
			}
			if (panel === '') {
				panel = Ext.create('Ext.panel.Panel', {
							id : conf.id,
							title : conf.title,
							closable : true,
							// iconCls : 'icon-activity',
							html : '<iframe width="100%" height="100%" frameborder="0" src="' + conf.url + '"></iframe>'
						});
				tabPanel.add(panel);
			}
			tabPanel.setActiveTab(panel);
		} else {
			Ext.MessageBox.show({
						title : '操作提示',
						msg : '找不到控件：app-viewport',
						icon : Ext.MessageBox.ERROR
					});
		}
	},

	addTabPanel : function(conf) {
		var p = Ext.getCmp('app-viewport');
		if (!Ext.isEmpty(p)) {
			p.addTabPanel(conf.component, conf.id, conf.title);
		} else {
			Ext.MessageBox.show({
						title : '操作提示',
						msg : '找不到控件：app-viewport',
						icon : Ext.MessageBox.ERROR
					});
		}
	},

	removeTab : function(component) {
		var p = Ext.getCmp('app-viewport');
		if (!Ext.isEmpty(p)) {
			p.removeTab(component);
		}
	},

	onAjaxException : function(response, component) {
		if (component) {
			if (response.status === 403) {
				component.close();
			}
		}
		Common.onException(response);
	},

	onProxyException : function(proxy, response, operation, eOpts) {
		Common.onException(response);
	},

	onException : function(response) {

		if (response.status === 0) {
			Common.show({
						title : '连接服务器失败',
						html : '连接服务器失败'
					});
			return;
		} else if (response.status === 200) {
			var json = Ext.JSON.decode(response.responseText);
			Common.show({
						title : '操作提示',
						html : json.msg + '<br>' + json.data
					});
		} else if (response.status === 401) {
			window.location.href = ctxp;
		} else if (response.status === 403) {
			Common.show({
						title : '无操作权限',
						html : response.responseText
					});
		} else {
			Common.show({
						title : '操作提示',
						html : response.responseText
					});
		}
	},

	bindPageSize : function(component) {
		var store = Ext.create('Ext.data.Store', {
					fields : ['text'],
					data : [{
								"text" : 5
							}, {
								"text" : 10
							}, {
								"text" : 20
							}, {
								"text" : 25
							}, {
								"text" : 50
							}, {
								"text" : 100
							}, {
								"text" : 500
							}, {
								"text" : 1000
							}]
				});
		component.setValue(25);
		component.bindStore(store);
	},

	loadStore : function(conf) {
		var comp = conf.component;
		var model = conf.model;
		var fields = conf.fields;
		var url = conf.url;
		var pageSize = conf.pageSize;
		var params = conf.params;
		var root = 'data';
		if (!Ext.isEmpty(conf.root)) {
			root = conf.root;
		}
		if (Ext.isEmpty(pageSize)) {
			pageSize = 20;
		}
		if (Ext.isEmpty(model)) {
			model = '';
		}
		if (Ext.isEmpty(fields)) {
			fields = [];
		}
		var myStore = Ext.create('Ext.data.JsonStore', {
					model : model,
					fields : fields,
					pageSize : pageSize,
					autoLoad : true,
					proxy : {
						type : 'ajax',
						actionMethods : 'post',
						url : url,
						reader : {
							type : 'json',
							root : root
						},
						listeners : {
							exception : function(proxy, response, operation, eOpts) {
								loadMask.hide();
								Common.onAjaxException(response, comp);
							}
						}
					},
					listeners : {
						load : {
							fn : function() {
								if (!Ext.isEmpty(comp.pagingToolbar)) {
									var pageData = comp.pagingToolbar.getPageData();
									if (pageData.currentPage > pageData.pageCount) {
										comp.pagingToolbar.moveLast();
									}
								}
							}
						},
						beforeload : {
							fn : function() {
								loadMask.show();
							}
						}
					}
				});
		var loadMask = new Ext.LoadMask(comp, {
					msg : '正在加载中...',
					store : myStore
				});
		if (!Ext.isEmpty(params)) {
			Ext.apply(myStore.proxy.extraParams, params);
		}
		if (!Ext.isEmpty(comp.pagingToolbar)) {
			comp.pagingToolbar.bindStore(myStore);
		}
		comp.bindStore(myStore);
	},

	getSelectionIds : function(gridPanel) {
		var ids = [];
		var selecteditems = gridPanel.getSelectionModel().selected.items;
		if (selecteditems.length > 0) {
			Ext.each(selecteditems, function() {
						var nd = this;
						ids.push(nd.data.id);
					});
		}
		return ids;
	},

	deleteSelectionIds : function(gridPanel, url) {
		var ids = Common.getSelectionIds(gridPanel);
		if (ids.length === 0) {
			Ext.Msg.show({
						title : "操作提示",
						msg : "请选择要删除的节点!",
						buttons : Ext.Msg.OK,
						icon : Ext.Msg.WARNING
					});
			return;
		}
		Ext.Msg.confirm("警告", "确定要删除选中的数据吗？", function(button) {
					if (button == "yes") {
						try {
							Common.ajax({
										component : gridPanel,
										params : {
											'id' : ids.join(",")
										},
										message : '正在删除选中的数据...',
										url : url,
										callback : function(result) {
											if (!Ext.isEmpty(gridPanel.pagingToolbar))
												gridPanel.pagingToolbar.getStore().reload();
											var msg = result.msg + result.data;
											if (Ext.isEmpty(msg))
												msg = '删除成功';
											Ext.Msg.show({
														title : "删除数据提示",
														msg : msg,
														buttons : Ext.Msg.OK,
														icon : Ext.Msg.INFO
													});
										}
									});
						} catch (error) {
							Ext.Msg.show({
										title : "删除数据错误",
										msg : error,
										buttons : Ext.Msg.OK,
										icon : Ext.Msg.WARNING
									});
						}
					}
				});
	},

	ajax : function(config) {
		if (config.component) {
			if (config.message) {
				config.component.setLoading(config.message);
			} else {
				var lock = true;
				if (!Ext.isEmpty(config.lock)) {
					lock = config.lock;
				}
				if (lock)
					config.component.setLoading('正在下载...');
			}
		}
		Ext.Ajax.request({
					url : config.url,
					params : config.params,
					method : 'post',
					callback : function(options, success, response) {
						if (config.component) {
							config.component.setLoading(false);
						}
						if (success) {
							var json = Ext.JSON.decode(response.responseText);
							if (!Ext.isEmpty(json.success)) {
								if (json.success) {
									config.callback(json, options, success, response);
								} else {
									Common.onAjaxException(response, config.component);
								}
							} else {
								config.callback(json, options, success, response);
							}

						} else {
							Common.onAjaxException(response, config.component);
						}
					}
				});
	},

	submit : function(conf) {
		if (conf.form.isValid()) {
			var msg = '正在保存数据，稍后...';
			if (conf.msg) {
				msg = conf.msg;
			}
			conf.form.submit({
						url : conf.url,
						method : 'POST',
						waitMsg : msg,
						success : function(f, action) {
							if (action.result.success) {
								conf.callback(action.result);// 调用回调函数
							} else {
								Common.onAjaxException(action.response);
							}
						},
						failure : function(f, action) {
							Common.onAjaxException(action.response);
						}
					});
			return true;
		} else {
			return false;
		}
	},

	formSubmit : function(conf) {
		var isValid = false;
		Ext.Msg.confirm("确认提示", "确认要提交数据吗？", function(button) {
					if (button == "yes") {
						isValid = Common.submit(conf);
					}
				});
		return isValid;
	},

	createTreeStore : function(URL, pid) {
		var store = Ext.create("Ext.data.TreeStore", {
					defaultRootId : pid,
					clearOnLoad : true,
					nodeParam : 'id',
					fields : [{
								name : 'id'
							}, {
								name : 'sort',
								type : 'int'
							}, {
								name : 'text'
							}, {
								name : 'iconCls'
							}, {
								name : 'leaf',
								type : 'boolean'
							}, {
								name : 'type'
							}, {
								name : 'resource'
							}, {
								name : 'component'
							}, {
								name : 'parentId'
							}],
					proxy : {
						type : 'ajax',
						url : URL
					}
				});
		return store;
	},

	storeToJson : function(jsondata) {
		var listRecord;
		if (jsondata instanceof Ext.data.Store) {
			listRecord = new Array();
			jsondata.each(function(record) {
						listRecord.push(record.data);
					});
		} else if (jsondata instanceof Array) {
			listRecord = new Array();
			Ext.each(jsondata, function(record) {
						listRecord.push(record.data);
					});
		}
		return Ext.encode(listRecord);
	},

	getQueryParam : function(name) {
		var regex = RegExp('[?&]' + name + '=([^&]*)');
		var scriptEls = document.getElementsByTagName('script');
		var path = scriptEls[scriptEls.length - 1].src;
		var match = regex.exec(location.search) || regex.exec(path);
		return match && decodeURIComponent(match[1]);
	},

	addQueryParam : function(url, name, value) {
		var path = url;
		if (value !== null && value.length > 0) {
			if (url.indexOf('?') >= 0) {
				path = url + '&' + name + '=' + value;
			} else {
				path = url + '?' + name + '=' + value;
			}
		}
		return path;
	},

	onTreeParentNodeChecked : function(node, checked) {
		if (node.parentNode !== null) {
			var childNodes = node.parentNode.childNodes;
			var parentCheck = false;
			if (childNodes.length > 0) {
				Ext.each(childNodes, function(childNode) {
							if (childNode.data.checked) {
								parentCheck = true;
							}
						});
			}
			node.parentNode.set('checked', parentCheck);
			Common.onTreeParentNodeChecked(node.parentNode, checked);
		}
	},

	setTreeParentNodeChecked : function(node, checked) {
		if (node.parentNode !== null) {
			node.parentNode.set('checked', checked);
			Common.onTreeParentNodeChecked(node.parentNode, checked);
		}
	},

	onTreeChildNodesChecked : function(node, checked) {
		Ext.each(node.childNodes, function(childNode) {
					childNode.set('checked', checked);
					if (childNode.childNodes.length > 0) {
						Common.onTreeChildNodesChecked(childNode, checked);
					}
				});
	},

	onTreePanelCheckChange : function(node, checked) {
		Common.onTreeChildNodesChecked(node, checked);
		Common.onTreeParentNodeChecked(node, checked);
	}
};