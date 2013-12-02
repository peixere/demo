Ext.define('CommonUtil', {
    extend: 'Ext.Base',
    alias: 'widget.CommonUtil',

    statics: {
        onAjaxException: function(response) {
            if(response.status == 200)
            {
                var result = Ext.JSON.decode(response.responseText);
                Ext.Msg.alert('操作异常提示'+response.status, result.data);
            }
            else
            {
                Ext.Msg.alert('操作异常提示', response.responseText);
            }
        },

        ajax: function(config) {
            if(config.component)
            {
                if(config.message)
                {
                    config.component.setLoading(config.message);
                }
                else
                {
                    config.component.setLoading('正在下载...');
                }
            }
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
                    if(config.component)
                    {
                        config.component.setLoading(false);
                    }
                }
            });
        },

        createTreeStore: function(URL, pid) {
            var store = Ext.create("Ext.data.TreeStore",
                {
                    defaultRootId : pid,
                    clearOnLoad : true,
                    nodeParam : 'id',
                    fields: [
                    {
                        name: 'id'
                    },
                    {
                        name: 'sort',
                        type: 'int'
                    },
                    {
                        name: 'text'
                    },
                    {
                        name: 'iconCls'
                    },
                    {
                        name: 'leaf',
                        type: 'boolean'
                    },
                    {
                        name: 'type'
                    },
                    {
                        name: 'resource'
                    },
                    {
                        name: 'component'
                    },
                    {
                        name: 'parentId'
                    }
                    ],
                    proxy :
                    {
                        type : 'ajax',
                        url : URL            
                    }
                });
            return store;
        },

        getQueryParam: function(name) {
            var regex = RegExp('[?&]' + name + '=([^&]*)');
            var scriptEls = document.getElementsByTagName('script');
            var path = scriptEls[scriptEls.length - 1].src;
            var match = regex.exec(location.search) || regex.exec(path);
            return match && decodeURIComponent(match[1]);
        },

        addQueryParam: function(url, name, value) {
            var path = url;
            if (value !== null && value.length > 0)
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
        },

        onTreePanelCheckChange: function(node, checked) {
            this.onTreeChildNodesChecked(node,checked);
            this.onTreeParentNodeChecked(node,checked);
        },

        onTreeChildNodesChecked: function(node, checked) {
            var me = this;
            Ext.each(node.childNodes,function(childNode)
            {
                childNode.set('checked', checked);   
                if(childNode.childNodes.length >0)
                {
                    me.onTreeChildNodesChecked(childNode,checked);
                }
            });
        },

        onTreeParentNodeChecked: function(node, checked) {
            if(node.parentNode !== null)
            {
                if(node.parentNode.childNodes.length >0)
                {
                    var parentCheck = false;
                    Ext.each(node.parentNode.childNodes,function(childNode)
                    {
                        if(childNode.data.checked)
                        {
                            parentCheck = true;
                        }
                    });
                    node.parentNode.set('checked', parentCheck);
                    this.onTreeParentNodeChecked(node.parentNode,checked);
                }
            }
        }       
    }


});