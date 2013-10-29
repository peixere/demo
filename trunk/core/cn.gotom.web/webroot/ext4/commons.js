/**
 * author 裴绍国
 * 
 * @class Ext.app.Portal
 * @extends Object A sample portal layout application class.
 */

// @require @packageOverrides
Ext.Loader.setConfig(
{
    enabled : true
});
Ext.Loader.setPath('Ext.app', 'ext4/classes');
// Ext.Loader.setPath('Ext.ux', 'ext4/ux');
var ajax = function(config)
{// 封装、简化AJAX
    Ext.Ajax.request(
    {
	url : config.url, // 请求地址
	params : config.params, // 请求参数
	method : 'post', // 方法

	callback : function(options, success, response)
	{
	    config.callback(Ext.JSON.decode(response.responseText));// 调用回调函数
	}
    });
    return false;
};
var RightTreeModel = Ext.define("RightTreeModel",
{// 定义树节点数据模型
    extend : "Ext.data.Model",
    fields : [
    {
	name : "id",
	type : "string"
    },
    {
	name : 'sort',
	type : 'int'
    },
    {
	name : "text",
	type : "string"
    },
    {
	name : "iconCls",
	type : "string"
    },
    {
	name : "leaf",
	type : "boolean"
    },
    {
	name : 'type'
    },
    {
	name : 'resource',
	type : 'string'
    },
    {
	name : 'component',
	type : "string"
    }
    ]
});

var RightTreeStore = function(url, pid)
{
    return Ext.create("Ext.data.TreeStore",
    {
	defaultRootId : pid,
	model : RightTreeModel,
	proxy :
	{
	    type : "ajax",
	    url : url
	},
	clearOnLoad : true,
	nodeParam : "id"
    });
};
function getQueryParam(name)
{
    var regex = RegExp('[?&]' + name + '=([^&]*)');
    var scriptEls = document.getElementsByTagName('script');
    var path = scriptEls[scriptEls.length - 1].src;
    var match = regex.exec(location.search) || regex.exec(path);
    return match && decodeURIComponent(match[1]);
}
function addQueryParam(url, name, value)
{
    var path = url;
    if (value != null && value.length > 0)
    {
	if (url.indexOf('?') >= 0)
	{
	    path = url + '&' + name + '=' + value;
	}
	else
	{
	    path = url + '?' + name + '=' + value;
	}
    }
    return path;
}