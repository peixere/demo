/**
 * author 裴绍国
 * 
 */

// @require @packageOverrides
Ext.Loader.setConfig(
{
    enabled : true
});

Ext.Loader.setPath('Ext.app', 'ext4/classes');

function ajax(config)
{
    Ext.Ajax.request(
    {
	url : config.url,
	params : config.params,
	method : 'post',

	callback : function(options, success, response)
	{
	    if (success)
	    {
		config.callback(Ext.JSON.decode(response.responseText),options, success, response);// 调用回调函数
	    }
	    else
	    {
		Ext.Msg.alert('信息提示', response.responseText);
	    }
	}
    });
    return false;
};

var TreeModel = Ext.define("TreeModel",
{
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

function treeStore(url, pid)
{
    return Ext.create("Ext.data.TreeStore",
    {
	defaultRootId : pid,
	model : TreeModel,
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


function getSelectedNode(tree)
{
    var selectedNode = '';
    findSelectedNodeCallback(tree.getRootNode());
    return selectedNode;
    function findSelectedNodeCallback(node)
    {
	var childnodes = node.childNodes;
	Ext.each(childnodes, function()
	{ // 从节点中取出子节点依次遍历
	    var nd = this;
	    if (nd.data.checked)
	    {
		selectedNode = nd;
		return;
	    }
	    else if (nd.hasChildNodes())
	    {
		findSelectedNodeCallback(nd);
	    }
	});
    }
}

function getAllChecked(tree)
{
    var temp = [];
    var rootNode = tree.getRootNode();// 获取根节点
    findchildnode(rootNode); // 开始递归
    var nodevalue = temp.join(",");
    return nodevalue;
    function findchildnode(node)
    {
	var childnodes = node.childNodes;
	Ext.each(childnodes, function()
	{
	    var nd = this;
	    if (nd.data.checked)
	    {
		temp.push(nd.data.id);
	    }
	    if (nd.hasChildNodes())
	    {
		findchildnode(nd);
	    }
	});
    }
    ;
}
