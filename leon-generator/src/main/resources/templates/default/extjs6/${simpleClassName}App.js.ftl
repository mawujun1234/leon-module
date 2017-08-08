<#assign simpleClassNameFirstLower = simpleClassName?uncap_first> 
<#-- //所在模块-->
<#assign module = basepackage?substring(basepackage?last_index_of(".")+1)> 
Ext.require("${extenConfig.extjs_packagePrefix}.${module}.${simpleClassName}");
<#if extenConfig.extjs_treeForm_model==false>
Ext.require("${extenConfig.extjs_packagePrefix}.${module}.${simpleClassName}Grid");
</#if>
<#if extenConfig.extjs_treeForm_model==true>
Ext.require("${extenConfig.extjs_packagePrefix}.${module}.${simpleClassName}Tree");
</#if>
Ext.require("${extenConfig.extjs_packagePrefix}.${module}.${simpleClassName}Form");
Ext.onReady(function(){
<#if extenConfig.isMasterSalve==true>
<#if extenConfig.extjs_treeForm_model==true>
	var grid=Ext.create('${extenConfig.extjs_packagePrefix}.${module}.${simpleClassName}Grid',{
		region:'west',
		split:true,
		collapsible : true,
		width:260,
		title:'XXX表格'
	});
	
	var mxgrid=Ext.create('${extenConfig.extjs_packagePrefix}.${module}.${extenConfig.subjectSlave.simpleClassName}Grid',{
		region:'center',
		title:'XXX表格',
		,listeners:{
			render:function(){
				mxgrid.mask();
			}
		}
	});
	grid.on("itemclick",function(view, record, item, index, e, eOpts){
		mxgrid.getStore().getProxy().extraParams=Ext.apply(mxgrid.getStore().getProxy().extraParams,{
			"${extenConfig.subjectSlave.fk_id}":record.get("id")
		});
		mxgrid.getStore().reload();
		
		mxgrid.unmask();
	});
	var viewPort=Ext.create('Ext.container.Viewport',{
		layout:'border',
		items:[grid,mxgrid]
	});
</#if>
<#if extenConfig.extjs_treeForm_model==true>
	var tree=Ext.create('${extenConfig.extjs_packagePrefix}.${module}.${simpleClassName}Grid',{
		region:'west',
		split:true,
		collapsible : true,
		width:260,
		title:'XXX表格'
	});
	
	var mxgrid=Ext.create('${extenConfig.extjs_packagePrefix}.${module}.${extenConfig.subjectSlave.simpleClassName}Grid',{
		region:'center',
		title:'XXX表格',
		,listeners:{
			render:function(){
				mxgrid.mask();
			}
		}
	});
	tree.on("itemclick",function(view, record, item, index, e, eOpts){
		mxgrid.getStore().getProxy().extraParams=Ext.apply(mxgrid.getStore().getProxy().extraParams,{
			"${extenConfig.subjectSlave.fk_id}":record.get("id")
		});
		mxgrid.getStore().reload();
		
		mxgrid.unmask();
	});
	var viewPort=Ext.create('Ext.container.Viewport',{
		layout:'border',
		items:[tree,mxgrid]
	});
</#if>
<#else>
<#if extenConfig.extjs_treeForm_model==false>
	var grid=Ext.create('${extenConfig.extjs_packagePrefix}.${module}.${simpleClassName}Grid',{
		region:'center',
		title:'XXX表格'
	});
	
	var viewPort=Ext.create('Ext.container.Viewport',{
		layout:'border',
		items:[grid]
	});
</#if>
<#if extenConfig.extjs_treeForm_model==true>
	var tree=Ext.create('${extenConfig.extjs_packagePrefix}.${module}.${simpleClassName}Tree',{
		title:'树',
		width:400,
		split:true,
		collapsible : true,
		region:'west'
	});
	var viewPort=Ext.create('Ext.container.Viewport',{
		layout:'border',
		items:[tree,{region:'center',html:"请填写对应的内容!"}]
	});
</#if>
</#if>
});