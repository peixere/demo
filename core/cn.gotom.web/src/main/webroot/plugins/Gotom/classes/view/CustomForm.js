/*
 * File: classes/view/CustomForm.js
 *
 * This file was generated by Sencha Architect version 2.2.3.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 4.2.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 4.2.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

Ext.define('Gotom.view.CustomForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.CustomForm',

    height: 244,
    bodyPadding: 10,
    title: '编辑信息',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'textfield',
                    anchor: '100%',
                    fieldLabel: '公司名称',
                    labelWidth: 60,
                    name: 'name',
                    allowBlank: false,
                    blankText: '必填项'
                },
                {
                    xtype: 'textfield',
                    anchor: '100%',
                    fieldLabel: '首页标题',
                    labelWidth: 60,
                    name: 'titlename',
                    allowBlank: false,
                    blankText: '必填项',
                    maxLength: 200,
                    minLength: 2
                },
                {
                    xtype: 'textfield',
                    anchor: '100%',
                    fieldLabel: '首页面板',
                    labelWidth: 60,
                    name: 'desktopPanel'
                },
                {
                    xtype: 'textfield',
                    anchor: '100%',
                    fieldLabel: '标题样式',
                    labelWidth: 60,
                    name: 'fontStyle'
                },
                {
                    xtype: 'filefield',
                    anchor: '100%',
                    width: 208,
                    fieldLabel: '标题背景',
                    labelWidth: 60,
                    name: 'topbg'
                },
                {
                    xtype: 'filefield',
                    anchor: '100%',
                    width: 208,
                    fieldLabel: '公司图标',
                    labelWidth: 60,
                    name: 'logo'
                },
                {
                    xtype: 'textareafield',
                    anchor: '100%',
                    height: 38,
                    fieldLabel: '公司简介',
                    labelWidth: 60,
                    name: 'description'
                },
                {
                    xtype: 'hiddenfield',
                    anchor: '100%',
                    fieldLabel: 'Label',
                    name: 'id'
                },
                {
                    xtype: 'hiddenfield',
                    anchor: '100%',
                    fieldLabel: 'Label',
                    name: 'logoId'
                },
                {
                    xtype: 'hiddenfield',
                    anchor: '100%',
                    fieldLabel: 'Label',
                    name: 'topbgId'
                },
                {
                    xtype: 'hiddenfield',
                    anchor: '100%',
                    fieldLabel: 'Label',
                    name: 'rightIds'
                }
            ]
        });

        me.callParent(arguments);
    }

});