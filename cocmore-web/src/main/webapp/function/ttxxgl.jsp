<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>云筑圈管理系统V1.0-团体信息管理</title>
<link rel="stylesheet" type="text/css" href=" ../static/ext/ext-all.css" />
<link rel="stylesheet" href=" ../static/ext/resources/css/xtheme-blue.css" type="text/css"></link>
	<link rel="stylesheet" href="../static/ext/ux/fileuploadfield/css/fileuploadfield.css" type="text/css"></link>
<link rel="stylesheet" type="text/css" href=" ../static/css/button.css" />
<script type="text/javascript" src=" ../static/ext/ext-base.js"></script>
<script type="text/javascript" src=" ../static/ext/ext-all.js"></script>
<script type="text/javascript" src=" ../static/ext/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src=" ../static/ext/ux/fileuploadfield/FileUploadField.js"></script>
<script type="text/javascript" src=" ../static/js/commonUtil.js"></script>
<script type="text/javascript">

/*组织加载器*/
var treeOrgLoader = new Ext.tree.TreeLoader({});
/*团体加载器*/
var treeGroupLoader = new Ext.tree.TreeLoader({
	dataUrl:getPath() + 'groups/getGroupTree'
});
/*组织根节点*/
var rootOrgNode = new Ext.tree.AsyncTreeNode({
	draggable:false,
	id : "source",
	text : "组织",
	draggable : false// 根节点不容许拖动
});
/*团体根节点*/
var rootGroupNode = new Ext.tree.AsyncTreeNode({
	draggable:false,
	id : "source",
	text : "团体",
	draggable : false// 根节点不容许拖动

});

var treePanel1 =  new Ext.tree.TreePanel({
	title:'组织结构',
	height:600,
	loader : treeOrgLoader,
	rootVisible : false,
	autoScroll : true,
	monitorResize: true,
	root:rootOrgNode,
	listeners:{
		click:function(n){//点击事件
			if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
			window.parent.orgId = n.attributes.id;
			treePanel2.loader.dataUrl = getPath() + 'groups/getGroupTree';
			treePanel2.loader.baseParams  ={orgId:window.parent.orgId},
			treePanel2.root.reload();
			fsuperSocialGroupsIdStore.load({params:{orgId:window.parent.orgId}});
		},
		beforeload:function(node) {//加载前
			treeOrgLoader.dataUrl = getPath() + 'org/getOrgTree'; // 定义每个节点的Loader
		}
	}
});
var groupTool2 = new Ext.Toolbar({
	id:'groupTool2',
	items:[{
		xtype:'textfield',
		emptyText:'请输入团体名称',
		id:'searchGroupName'
	}]
});
var groupTool3 = new Ext.Toolbar({
	id:'groupTool3'
});
	
var treePanel2 = new Ext.tree.TreePanel({
	height:600,
	title:'团体信息',
	loader:treeGroupLoader,
	rootVisible : false,
	autoScroll : true,
	monitorResize: true,
	root:rootGroupNode,
	tbar:groupTool2,
	listeners:{
		click:function(n){
			if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
			if(!window.parent.orgId){ alertWarring("请先选择组织，再进行操作");return;}
			window.parent.groupId = n.attributes.id;
			Ext.getCmp('detailAndUpdateForm').getForm().reset();
			loadData(n.attributes.id);
		}
	}
});
var treePanelTuanti = new Ext.Panel({
	region:'west',
	layout:'fit',
	border:false,
	collapsible : true,
	split : true,
	width:460,
	items:[{
		layout:'column',
		columns: 2,
		items:[
			{columnWidth:0.39,border:false,items:[treePanel1]},
			{columnWidth:0.61,border:false,items:[treePanel2]}
		]
	}]
});


	var editIMG;
	var excel;
	var date = new Date(); // 得到系统日期
	var dateString = date.getFullYear() + "-" + (date.getMonth() + 1) + "-"
			+ date.getDate() + " " + date.getHours() + ":"
			+ date.getMinutes() + ":" + date.getSeconds();
	var editWin;/*更改，新增window*/
	var detailWin;/*查看详情window*/
	
	//上传图片类型  
	var img_reg = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/;
	
	/*extjs准备初始化*/
	Ext.onReady(function() {
				Ext.QuickTips.init();
				 /*根据会员或者职员来进行页面的不同显示*/
			   if(getUser()[2]==1){
			    	new Ext.Viewport({
			        	renderTo:'mainDiv',//渲染到什么地方
			        	id:'viewport',
			        	layout:'border',
			        	 width:300,
			        	items:[treePanelTuanti,detailAndUpdateForm]
			        });
			    }else{
			    	new Ext.Viewport({
			        	renderTo:'mainDiv',//渲染到什么地方
			        	id:'viewport',
			        	layout:'border',
			        	items:[detailAndUpdateForm]
			        });
			    } 
			   /*var displayPanel = new Ext.Viewport({
					renderTo : 'mainDiv',
					layout : 'border',
					items : [ grid ]
				});*/
			/* 	Ext.EventManager.onWindowResize(function() {
					grid.getView().refresh();
				}); */
				
				loadData(window.parent.groupId) ;
				
				/* grid.on('dblclick', editData); */
				/*新增pannel的打开*/
			});
	/*======================自定义元素==========================================*/
		//区域
		var districtStore  = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({ 
				disableCaching :false,
				method:'post',
				url : getPath() + '/dis/findAllDis'
			}),
			reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
		});
		districtStore.load();
		
		//省份
		var provinceStore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({ 
				disableCaching :false,
				method:'post',
				url : getPath() + '/province/findProvinceById'
			}),
			reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
		});
		
		//城市
		var cityStore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({ 
				disableCaching :false,
				method:'post',
				url : getPath() + '/city/findCityHql'
			}),
			reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
		});
		
		//区县
		var countyStore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({ 
				disableCaching :false,
				method:'post',
				url : getPath() + '/county/findCountyHql'
			}),
			reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
		});
		//上级团体
		var fsuperSocialGroupsIdStore = new Ext.data.Store({
			        proxy : new Ext.data.HttpProxy({
			                disableCaching : false,
			                method : 'post',
			                url : getPath()
			                    + '/groups/findAllGroupPagingList'
		            }),
			        reader : new Ext.data.JsonReader({root : 'list'}, [{name : 'fid'}, {name : 'fname'}])
			});
		var fsuperSocialGroupsIdCom = new Ext.form.ComboBox({
					fieldLabel : '上级团体',
					id : 'fsuperSocialGroupsId',
					hiddenName : 'fsuperSocialGroupsId',
					typeAhead : true,
					triggerAction : 'all',
					lazyRender : true,
					mode : 'local',
					store : fsuperSocialGroupsIdStore,
					valueField : 'fid',
					displayField : 'fname'
				});
		//销售人员
		var YBasicEmployeeStore = new Ext.data.Store({
					autoLoad:true,
			        proxy : new Ext.data.HttpProxy({
			                disableCaching : false,
			                method : 'post',
			                url : getPath() + '/emp/findAllEmp'
		            }),
			        reader : new Ext.data.JsonReader({root : 'obj.list'}, [{name : 'fid'}, {name : 'fname'}])
			});
		var YBasicEmployeeCom = new Ext.form.ComboBox({
					fieldLabel : '销售人员',
					id : 'YBasicEmployee',
					hiddenName : 'YBasicEmployee.fid',
					typeAhead : true,
					triggerAction : 'all',
					lazyRender : true,
					mode : 'local',
					store : YBasicEmployeeStore,
					valueField : 'fid',
					displayField : 'fname',
					blankText : '该字段不允许为空',
					allowBlank : false
				});
		var myImage1 = new Ext.BoxComponent({
			fieldLabel:'logo',
		    id : 'browseImage1',
		    autoEl : {
		        width : 60,
		        height : 72,
		        tag : 'img',
		        src : Ext.BLANK_IMAGE_URL,
		        style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
		        complete : 'off',
		        id : 'photo1'
		    }
		});
		
		//类型
		var ybasicTypeStore  = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({ 
				disableCaching :false,
				method:'post',
				url : getPath() + '/type/findTypeHql'
			}),
			reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
		});
		ybasicTypeStore.load({params:{fmodelId:1}});
		var YBasicType = new Ext.form.ComboBox({
					fieldLabel : '类型',
					id : 'YBasicTypeFid',
					hiddenName : 'YBasicType.fid',
					typeAhead : true,
					triggerAction : 'all',
					lazyRender : true,
					mode : 'local',
					store : ybasicTypeStore,
					valueField : 'fid',
					displayField : 'fname',
					forceSelection : true,
					blankText : '该字段不允许为空',
					allowBlank : false
				});
	/*更新修改所需要的form*/

	var detailAndUpdateForm = new Ext.form.FormPanel({
		region:"center",
		layout : 'hbox',
		layoutConfig : {
			align : 'middle',
			pack : 'center'
		},
		id:'detailAndUpdateForm',
		border : true,
		frame : true,
		autoScroll:true,
		labelAlign : 'left',
		fileUpload:true,
		enctype:'multipart/form-data', 
		buttonAlign : 'center',
		buttons : [ {
			text : '保存',
			handler : function() {
				if(window.parent.orgId){
					if(Ext.getCmp("fbillState").getValue() != 5){
						alertError("该团体还未通过审核，不能操作!");return;
					}
					if(Ext.getCmp('fid').getValue()){
						Ext.Ajax.request({
							url : getPath()+'groups/checkChildrenNode',
							params : {groupId : Ext.getCmp('fid').getValue()},
							success: function (response, options) 
							{
								var data = Ext.util.JSON.decode(response.responseText);
								//查询到无子集(子集均失效)则可以调整组织
								if(data.object){
									if(window.parent.orgId !=Ext.getCmp('YBasicOrganization').getValue()){
										Ext.MessageBox.confirm('提示', '是否确认将该团体转移组织?', function(btn){
											if(btn=='yes'){
												if(Ext.getCmp('fsuperSocialGroupsId').getValue()!=""){
													Ext.getCmp('fsuperSocialGroupsId').setValue("");
												}
												Ext.getCmp('YBasicOrganization').setValue(window.parent.orgId);
											}
											updateGroup();
										});
									}else{
										updateGroup();
									}
								}else{//不更改其所属组织
									alertError("该团体有下属团体未失效，无法转移该团体所属组织");
								}
							},
							failure: function ( result, request) {
								alertError("系统运行超时或执行失败");
							}
						});
					}else{
						updateGroup();
					}
				}else{
					if(!window.parent.orgId){ alertWarring("请先选择组织，再进行操作");return;}
				}
			}
		}
		],
		containerScroll : true,
		items : [{
			xtype : 'fieldset',
			layout : 'form',
			labelWidth : 80,
			width : 750,
			title : '团体信息',
			items : [{
				layout:'column',
				border:false,
				items:[{
							columnWidth:0.5,
							layout : "form",
							defaults : {anchor : '90%',xtype : 'textfield'},
							items:[ {
											xtype : 'hidden',
											fieldLabel : 'id主键',
											id : 'fid',
											name : 'fid'
										},{
											xtype: 'hidden',
											id : 'flag',
											name : 'flag'
										}, {
											id : 'fnumber',
											fieldLabel : '编号',
											name : 'fnumber',
											xtype: 'hidden'
										}, {
											fieldLabel : '团体名称',
											id : 'fname',
											name : 'fname',
											maxLength : 50,
											blankText : '该字段不允许为空',
											allowBlank : false
										}, {
											fieldLabel : '团体简称',
											id : 'fabbreviation',
											name : 'fabbreviation',
											maxLength : 18,
											blankText : '该字段不允许为空',
											allowBlank : false
										}, {
											fieldLabel : '人数',
											id : 'fnumberPeople',
											name : 'fnumberPeople',
											xtype:'numberfield',
											blankText : '该字段不允许为空',
											allowBlank : false
										},
										fsuperSocialGroupsIdCom,
										//YBasicOrganizationCom, 
										{xtype:'hidden',id:'YBasicOrganization',name:'YBasicOrganization.fid'},
										YBasicEmployeeCom, YBasicType,{
											fieldLabel : '客户联系人',
											id : 'fclientContacts',
											name : 'fclientContacts',
											maxLength : 24,
											blankText : '该字段不允许为空',
											allowBlank : false
										}, {
											fieldLabel : '电话',
											id : 'fphone',
											name : 'fphone',
											regex : /^((1+\d{10})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/,
											blankText : '该字段不允许为空',
											allowBlank : false
										}, {
											fieldLabel : '邮箱',
											id : 'femail',
											name : 'femail',
											vtype: 'email',
											maxLength:'25',
											blankText : '该字段不允许为空',
											allowBlank : false
										}, {
											fieldLabel : '手机',
											id : 'fiphone',
											name : 'fiphone',
											regex : /^1+\d{10}$/,
											blankText : '该字段不允许为空',
											allowBlank : false
										}, {
											fieldLabel : '是否签约',
											id : 'fynsigned',
											xtype:'radiogroup',
										 	layout:'column',
										 	columns:2,
										 	items:[{boxLabel: "是", name: 'fynsigned',inputValue: 1},  
										 	       {boxLabel: "否", name: 'fynsigned',inputValue: 0,checked:true}],
										 	blankText:'该字段不允许为空',
										 	allowBlank: false
										},{
											fieldLabel:'签约时间',
											id:'fsignedTime',
											name:'fsignedTime',
											xtype:'datefield',
											format:'Y-m-d H:i:s'
										}, {
											fieldLabel : '签约人数',
											id : 'fregisterNum',
											name : 'fregisterNum',
											xtype:'numberfield'
										}, {
											xtype:'hidden',
											fieldLabel : '状态',
											id : 'fbillState',
											name : 'fbillState'
										}, {
											fieldLabel : '备注',
											id : 'fcomment',
											name : 'fcomment',
											maxLength : 125
										}
							]},{
										columnWidth:0.5,
										layout : "form",
										defaults : {anchor : '90%',xtype : 'textfield'},
										items:[myImage1,new Ext.ux.form.FileUploadField({
				        							id:'file',
							        		    	name : 'file',
							        		    	emptyText: 'Select an image',
							        				width : 150,
							        				buttonText: '浏览....'
							        			}),{
				        		    				id : 'logo',
				        		    				name:'logo',
				        		    				xtype:'hidden'
				        						}, {
													fieldLabel : '信息来源',
													id : 'fsource',
													name : 'fsource',
													value:'系统管理员添加',
													//maxLength : 12,
													blankText : '该字段不允许为空',
													allowBlank : false
												},{
													fieldLabel:'原编号',
													id:'fpreviousNumber',
													name:'fpreviousNumber',
													xtype:'hidden'
												}, {
													fieldLabel : '级别',
													id : 'flevel',
													name : 'flevel',
													maxLength : 18
												},
											    {xtype : 'combo',fieldLabel : '地区',id : 'YBasicDistrictFid',hiddenName : 'YBasicDistrict.fid',triggerAction : 'all',lazyInit : true,mode : 'local',
													store: districtStore,
													valueField : 'fid',
													displayField : 'fname',selectOnFocus : true,emptyText : '请选择地区',
												 	blankText:'该字段不允许为空',
												 	allowBlank: false,
													listeners:{
												        select:function(groupSelect){
												        	provinceStore.load({params:{id:groupSelect.getValue()}});
												        	Ext.getCmp('YBasicProvinceFid').focus();
														}
												    }
												} , 
												{xtype : 'combo',fieldLabel : '省份',id : 'YBasicProvinceFid',hiddenName : 'YBasicProvince.fid',triggerAction : 'all',lazyInit : true,mode : 'local',
													store: provinceStore,
													valueField : 'fid',
													displayField : 'fname',selectOnFocus : true,emptyText : '请选择省份',
												 	blankText:'该字段不允许为空',
												 	allowBlank: false,
													listeners:{
												        select:function(groupSelect){
												        	cityStore.load({params:{id:groupSelect.getValue()}});
												        	Ext.getCmp('YBasicCityFid').focus();
														}
												    }
												},
												{xtype : 'combo',fieldLabel : '城市',id : 'YBasicCityFid',hiddenName : 'YBasicCity.fid',triggerAction : 'all',lazyInit : true,mode : 'local',
													store: cityStore,
													valueField : 'fid',
													displayField : 'fname',selectOnFocus : true,emptyText : '请选择城市',
												 	blankText:'该字段不允许为空',
												 	allowBlank: false,
													listeners:{
												        select:function(groupSelect){
												        	countyStore.load({params:{id:groupSelect.getValue()}});
												        	Ext.getCmp('YBasicCountyFid').focus();
														}
												    }},
												{xtype : 'combo',fieldLabel : '区县',id : 'YBasicCountyFid',hiddenName : 'YBasicCounty.fid',triggerAction : 'all',lazyInit : true,mode : 'local',
												    store: countyStore,
												    valueField : 'fid',
												    displayField : 'fname',selectOnFocus : true,emptyText : '请选择区县',
												 	blankText:'该字段不允许为空',
												 	allowBlank: false
											    }, {
													fieldLabel : '地址',
													id : 'faddress',
													name : 'faddress',
													maxLength : 100
												}, {
													fieldLabel : '是否允许加入商会',
													id : 'allowadd',
													xtype:'radiogroup',
												 	layout:'column',
												 	columns:2,
												 	items:[{boxLabel: "是", name: 'allowadd',inputValue: 1},  
												 	       {boxLabel: "否", name: 'allowadd',inputValue: 0,checked:true}],
												 	blankText:'该字段不允许为空',
												 	allowBlank: false
												},{
													fieldLabel : '是否为叶子节点',
													id : 'fleafNode',
													xtype:'radiogroup',
												 	layout:'column',
												 	columns:2,
												 	items:[{boxLabel: "是", name: 'fleafNode',inputValue: 1},  
												 	       {boxLabel: "否", name: 'fleafNode',inputValue: 0,checked:true}],
												 	blankText:'该字段不允许为空',
												 	allowBlank: false
												},{
													fieldLabel : '创建人id',
													id : 'fcreaterId',
													name : 'fcreaterId',
													hidden:true
												}, {
													fieldLabel : '修改人id',
													id : 'fmodifiedId',
													name : 'fmodifiedId',
													hidden:true
												}, {
													fieldLabel : '最后修改人id',
													id : 'flastModifiedId',
													name : 'flastModifiedId',
													hidden:true
												}, {
													fieldLabel : '创建时间',
													id : 'fcreateTime',
													name : 'fcreateTime',
													hidden:true
												}, {
													fieldLabel : '修改时间',
													id : 'fmodifiedTime',
													name : 'fmodifiedTime',
													hidden:true
												}, {
													fieldLabel : '最后修改时间',
													id : 'flastModifiedTime',
													name : 'flastModifiedTime',
													hidden:true
												},{
													fieldLabel : '版本',
													id:'localCacheVersion',
													name:'localCacheVersion',
													hidden:true
												}
					        			]
						}]
				}]
		}]
	});

	/**************************** 操作 **********************************/

	/*根据商会的id来获取信息*/
	var row = "" ;
	function loadData(groupId) {
		
	if(groupId && groupId !=''){//最多有一条数据
			/*根据groupId获取信息，如果不存在则row=add.存在则为update*/
			Ext.Ajax.request({
				   url: getPath()+'groups/findAllGroupPagingList',//查询单条根据groupId
				   success: function(response, opts){
					   var data = Ext.util.JSON.decode(response.responseText);
					 //  alert("条数："+data.count);
					// alert( data.list.fid);
					   var group = data.list;
					   if(data.count==1){//最多有一条数据
						   detailAndUpdateForm.form.setValues({
								fid 					: group.fid,
								fnumber 				: group.fnumber,
								fname 					: group.fname,
								fabbreviation 			: group.fabbreviation,
								fnumberPeople 			: group.fnumberPeople,
								fleafNode				: group.fleafNode,
								fsource					: group.fsource,
								fynsigned				: group.fynsigned,
								fregisterNum			: group.fregisterNum,
								flevel					: group.flevel,
								fsignedTime				: group.fsignedTime,
								fclientContacts			: group.fclientContacts,
								faddress				: group.faddress,
								fphone					: group.fphone,
								femail					: group.femail,
								fiphone					: group.fiphone,
								fbillState				: group.fbillState,
								fcomment				: group.fcomment,
								fcreaterId				: group.fcreaterId,
								fmodifiedId				: group.fmodifiedId,
								flastModifiedId			: group.flastModifiedId,
								fcreateTime				: group.fcreateTime,
								fmodifiedTime			: group.fmodifiedTime,
								flastModifiedTime		: group.flastModifiedTime,
								logo					: group.logo,
								allowadd				: group.allowadd,
								YBasicOrganization 		: group.ybasicOrganization.fid,
								YBasicDistrictFid 		: group.ybasicDistrict?group.ybasicDistrict.fid:"",
								YBasicEmployee 			: group.ybasicEmployee?group.ybasicEmployee.fid:"",
								YBasicTypeFid			: group.ybasicType?group.ybasicType.fid:"",
								flag					: group.flag,
								localCacheVersion		: group.localCacheVersion
							});
					   
					   	   if(group.fsuperSocialGroupsId && group.fsuperSocialGroupsId!=""){//上级团体不为空是加载上级团体
							   fsuperSocialGroupsIdStore.load({params:{orgId:group.ybasicOrganization.fid},callback:function(){
								   detailAndUpdateForm.form.setValues({fsuperSocialGroupsId 	: group.fsuperSocialGroupsId});
							   }});
						   }
						   //获取city和county的stroe
						   if(group.ybasicDistrict && group.ybasicDistrict.fid){
							   provinceStore.load({//省份
								   params:{id:group.ybasicDistrict.fid},
								   callback:function(){
									   if(group.ybasicProvince && group.ybasicProvince.fid){
										   detailAndUpdateForm.form.setValues({YBasicProvinceFid : group.ybasicProvince?group.ybasicProvince.fid:""});
										   cityStore.load({params:{id:group.ybasicProvince.fid},//城市
											   callback:function(){
												   if(group.ybasicCity && group.ybasicCity.fid){
													   detailAndUpdateForm.form.setValues({YBasicCityFid : group.ybasicCity?group.ybasicCity.fid:""});
													   countyStore.load({params:{id:group.ybasicCity.fid},//区域
														   callback:function(){
															   detailAndUpdateForm.form.setValues({YBasicCountyFid: group.ybasicCounty?group.ybasicCounty.fid:""});
													   		}
														});
												   }
										   		}
											});
									   }
							   		}
								});
							   
						   }
								   
						     fsuperSocialGroupsIdCom.setReadOnly(true);
						     if( group.logo &&  group.logo.length>0){
							     var imgLength = group.logo.split(',');
						         for(var i =0;i<imgLength.length;i++){
						        	 if(i==imgLength.length-1)return;
						             photoSrc(imgLength[i],(i+1));
						         }
					         }
					   }else{
						   alertWarring("系统异常,联系管理员");
					   }
				   },
				   params: { groupId: groupId}//暂时写死，需要动态取值
				});
			   row = "update";
		} else{
			row = "add";
			if(!window.parent.orgId){
				alertWarring("请先选择组织，再进行操作");return false;
			}
			fsuperSocialGroupsIdCom.setReadOnly(false);
			fsuperSocialGroupsIdStore.reload({params:{orgId:window.parent.orgId}});
		}
	
		var image = Ext.getCmp('browseImage1').getEl().dom;
	    image.src = Ext.BLANK_IMAGE_URL;
		setTitleAndUrl(row, editWin);
	}
	detailAndUpdateForm.getForm().findField('fmodifiedTime').setValue(dateString);
	detailAndUpdateForm.getForm().findField('flastModifiedTime').setValue(dateString);
	
	/*为update和add加form的条状uri*/
	function setTitleAndUrl(row) {
		if (row == 'add') {
			Ext.getCmp('fbillState').setValue(5);
			Ext.getCmp('YBasicOrganization').setValue(window.parent.orgId);
			detailAndUpdateForm.getForm().findField('fmodifiedTime').setValue(dateString);
			detailAndUpdateForm.getForm().findField('fcreaterId').setValue(getUser()[1]);
			detailAndUpdateForm.getForm().findField('fcreateTime').setValue(dateString);
			getNumberStore();
			detailAndUpdateForm.form.url = getPath() + 'groups/addGroup';
		} else {
			detailAndUpdateForm.form.url = getPath()+ 'groups/updateGroup';
			detailAndUpdateForm.getForm().findField('fmodifiedId').setValue(getUser()[1]);
			detailAndUpdateForm.getForm().findField('flastModifiedId').setValue(getUser()[1]);
		}
	}
//图片上传成功 给产品图片文本，预览img赋值
function photoSrc(src,index){
    if(Ext.isIE){
        var image = Ext.getCmp('browseImage'+index).getEl().dom;
        image.src = Ext.BLANK_IMAGE_URL;
        image.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod = scale)";
        image.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = src;
    }else{ // 支持FF
        Ext.getCmp('browseImage'+index).getEl().dom.src =src;
    }
}
treePanel2.on('render',function(){
	groupTool3.render(this.tbar); //add one tbar
});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 1
	},
	success : function(response, options) {
		obj=Ext.util.JSON.decode(response.responseText);
		for(var i=0;i<obj.length;i++){
			if(i==0){
				
			}else if(i<5){
				groupTool3.add(Ext.util.JSON.decode(obj[i]));groupTool3.add('-');
			}else{
				groupTool2.add('-');
				groupTool2.add(Ext.util.JSON.decode(obj[i]));
				groupTool2.doLayout();
			}
		}
		groupTool3.doLayout();
	}
});

//获取number
function getNumberStore(){
	Ext.Ajax.request({
		url:getPath() + '/serialNumber/getSerialNumber',
		params:{prefix:'XX-SHTTXX'},
		success:function(response, options) {
			Ext.getCmp("fnumber").setValue(Ext.util.JSON.decode(response.responseText).serialNumber);
		}
	});
}
function addGroup(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.orgId){ alertWarring("请先选择组织，再进行操作");return;}
	window.parent.groupId="";
	fsuperSocialGroupsIdStore.load({params:{orgId:window.parent.orgId}});
	detailAndUpdateForm.form.reset();
	var image = Ext.getCmp('browseImage1').getEl().dom;
    image.src = Ext.BLANK_IMAGE_URL;
    row="add";
	setTitleAndUrl(row);
}

function updateGroup(){
	var logourl = Ext.getCmp('file').getValue();
	if(logourl !="" && !img_reg.test(logourl)){
		alertWarring("文件格式不正确！");return;
	}
	if (detailAndUpdateForm.form.isValid()) {
			detailAndUpdateForm.form.submit({
				method : 'POST',
				waitMsg : '正在提交.....',
				waitTitle : '请稍等',
				success : function(form, action) {
					loadData(window.parent.groupId);//从新加载form
					alertWarring("保存成功...");
					treePanel2.root.reload();
				},
				failure : function(form, action) {
					alertWarring("失败...");
				}
			});
		} else {
			alertWarring("内容填写不完整");
		}
}
//excel模板设置
function mbsz(){
	var export_url = getPath() + 'groups/download';
    window.location.href = export_url;
}
//导入
/*导入数据功能*/
function showImportExcel(){
	if(!excel){
		excel = new Ext.Window({
	        id: 'showImportExcel',
	        layout:'fit',
            width:300,
            height:140,
            frame:true,
            plain: true,
	        closeAction:'hide',
			maximizable:false,
			resizable:false,
			modal:true,
			border:false,
		    items :[ importExcelForm ]
        });
	}
	Ext.getCmp('showImportExcel').show();
} 

var importExcelForm= new Ext.form.FormPanel({
	frame: true,
	fileUpload: true, 
	waitMsgTarget : true,
	labelAlign : 'left',
	enctype:'multipart/form-data', 
	bodyStyle : 'padding:5px 0px 0',
	buttonAlign : 'center',
	id : 'uploadFileLogo',
	items : [
		new Ext.ux.form.FileUploadField({
	    	id : 'flshlogo',
	    	name : 'file',
	    	emptyText: '选择xls文件',
			width : 150,
			buttonText: '浏览...'
		})
	],
    buttons:[{
	    text: '上传',
	    handler:function(){
	    	 var logourl = Ext.getCmp('flshlogo').getValue();
    		if(Ext.getCmp('uploadFileLogo').getForm().isValid() && /\.(xlsx|xls)$/.test(logourl)){
    			Ext.getCmp('uploadFileLogo').getForm().submit({
		                   method:'POST',
		                   waitMsg:'正在提交.....',
						   waitTitle:'请稍等',
						   url : getPath() + 'groups/importOrUpdateGroups',
		                   success:function(form, action){
		                	   Ext.MessageBox.confirm('提示', '是否查看导入详情?', function(btn){
									if(btn=='yes'){
										var export_url = getPath() + 'groups/downloadImportStatus';
									    window.location.href = export_url;
									}
								});
		                	   Ext.getCmp("showImportExcel").hide();
		                   },
		                   failure:function(form, action){
		                	   alertWarring(action.result.msg);
		                   }       
		              });  
			}else{
				alertWarring("请选择后缀名为xls的文件");
			}
		}
    }] 
});

//Ext.getCmp('groupTool2').addButton(new Ext.Button({text:'新增',handler:function(){window.parent.groupId="";detailAndUpdateForm.form.reset();setTitleAndUrl('add')},iconCls:'common_add',icon: 'common_add'}));
</script>
</head>
<body>
	<div id="mainDiv"></div>
</body>
</html>