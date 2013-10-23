/**
 * author 裴绍国
 * @class Ext.app.Portal
 * @extends Object
 * A sample portal layout application class.
 */

//@require @packageOverrides
Ext.Loader.setConfig({
    enabled: true
});
Ext.Loader.setPath('Ext.app', 'ext4/classes');
var ajax = function(config) {// 封装、简化AJAX
	Ext.Ajax.request({
		url : config.url, // 请求地址
		params : config.params, // 请求参数
		method : 'post', // 方法

		callback : function(options, success, response) {
			config.callback(Ext.JSON.decode(response.responseText));// 调用回调函数
		}
	});
	return false;
};
var model = Ext.define("TreeModel", {// 定义树节点数据模型
	extend : "Ext.data.Model",
	fields : [{
		name : "id",
		type : "string"
	}, {
		name : "text",
		type : "string"
	}, {
		name : "iconCls",
		type : "string"
	}, {
		name : "leaf",
		type : "boolean"
	}, {
		name : 'type'
	}, {
		name : 'component'
	}]	
});

var createStore = function(url, pid)
{// 创建树面板数据源
    return Ext.create("Ext.data.TreeStore",
    {
	defaultRootId : pid, // 默认的根节点id
	model : model,
	proxy :
	{
	    type : "ajax", // 获取方式
	    url : url // 获取树节点的地址
	},
	clearOnLoad : true,
	nodeParam : "id"// 设置传递给后台的参数名,值是树节点的id属性
    });
};