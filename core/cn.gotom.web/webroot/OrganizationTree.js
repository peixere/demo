Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.tip.QuickTipManager'
]);
Ext.onReady(function()
{
    var treedata = treeStore('p/right!tree.do', '');// treeStore('p/organization!tree.do')
    var portal = Ext.create('Portal');
    function menuClick(view, node)
    {
	alert(node.data.text);
	portal.content.update('<iframe width="100%" height="100%" frameborder="0" src="http://www.google.com.hk"></iframe>');
    }
    
    var tree = Ext.create("Ext.tree.TreePanel",
    {
	id : 'tree',
	title : '菜单管理',
	region : 'west',
	width : 250,
	minWidth : 100,
	maxWidth : 400,
	height : 200,
	animate : true,
	border : false,
	bodyborder : false,
	lines : true,
	split : true,
	stateful : true,
	collapsible : true,
	frame : false,
	enableDD : true,
	autoScroll : true,
	autoHeight : false,
	rootVisible : false,
	store : treedata,
	listeners :
	{
	    itemclick : function(view, node)
	    {
		menuClick(view, node);
	    }
	}
    });
    portal.center.add(tree);
    portal.doLayout();
});