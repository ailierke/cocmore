<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
	<title>云筑圈管理系统V1.0-活动信息管理</title>
	<link rel="stylesheet" type="text/css" href=" ../static/ext/ext-all.css" />
	<link rel="stylesheet" href=" ../static/ext/resources/css/xtheme-blue.css" type="text/css"></link>
	<link rel="stylesheet" type="text/css" href=" ../static/css/button.css"/>
	<link rel="stylesheet" href="../static/ext/ux/fileuploadfield/css/fileuploadfield.css" type="text/css"></link>
	<script type="text/javascript" src=" ../static/ext/ext-base.js"></script>	
	<script type="text/javascript" src=" ../static/ext/ext-all.js"></script>
	<script type="text/javascript" src=" ../static/ext/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src=" ../static/ext/ux/ProgressBarPager.js"></script>
	<script type="text/javascript" src=" ../static/ext/ux/PanelResizer.js"></script>
	<script type="text/javascript" src=" ../static/ext/ux/PagingMemoryProxy.js"></script>
	<script type="text/javascript" src=" ../static/js/StarHtmleditor.js"></script>
	<script type="text/javascript" src=" ../static/ext/ux/fileuploadfield/FileUploadField.js"></script>
	<script type="text/javascript" src=" ../static/js/commonUtil.js"></script>
	<link href="../static/webApp/shhdxq.css" type="text/css" rel="stylesheet"/>
	<script src='../static/jquery/jquery.min.js' type='text/javascript' charset='utf-8'></script>
	<script src='../static/jquery/imgScroll/hhSwipe.js'></script>
	<script type="text/javascript">
	var LIMIT = 26;var editWin;var mbszWin;var excel;var editIMGList;var detailWin;


	//上传图片类型  
	var img_reg = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/;
	var records = new Ext.data.Record.create([  
	                                          {name: 'fid'},
	                                          {name: 'fnumber'},
	                                          {name: 'fheadline'},
	                                          {name: 'fimages'},
	                                          {name: 'fmessage'},
	                                          {name: 'fsponsor'},
	                                          {name: 'fsite'},
	                                          {name: 'fcost'},
	                                          {name: 'ybasicMember'},
	                                          {name: 'ybasicType'},
	                                          {name: 'ybasicSocialgroups'},
	                                          {name: 'fcomment'},
	                                          {name: 'fbillState'},
	                                          {name: 'fstartTime'},
	                                          {name: 'ffinishTime'},
	                                          {name: 'fpeopleNum'},
	                                          {name: 'fsource'},
	                                          {name: 'flag'}
	                                   ]);

	var store = new Ext.data.Store({
		reader:new Ext.data.JsonReader({
			root:'list',
			totalProperty:'count'
		},records),
		proxy:new Ext.data.HttpProxy({
				disableCaching :false,
				url: getPath() + 'groupsActivity/findAllActivityPagingList'
		}),
		remoteStore : true, // 是否远程调用数据
		remoteSort : true   // 是否远程排序
	});
	store.on("beforeload", function() {
		this.baseParams = {
				groupId : window.parent.groupId
		};
	});
	var tool = new Ext.Toolbar({ //工具栏
		items:['活动标题：',{
			xtype:'textfield',
			emptyText:'请输入标题',
			id:'searchCondition',
			width:150 
		},'-']
	});
	Ext.Ajax.request({
		url : getPath() + 'systemconfigurationMenu/getfunctionjs',
		method : 'GET',
		params : {
			functionId : 2
		},
		success : function(response, options) {
			obj=Ext.util.JSON.decode(response.responseText);
			for(var i=0;i<obj.length;i++){
				tool.add(Ext.util.JSON.decode(obj[i]));tool.add('-');
			}
			tool.doLayout();
		}
	});
	Ext.onReady(function(){
	    Ext.QuickTips.init();
	    Ext.form.Field.prototype.msgTarget = 'side';
	    
	    // create the data store
	    
	    store.on('load', function(store, records, options) {
	        if (records.length = 0) {
	            Ext.Msg.alert("提示信息", "没有符合条件的数据!"); 
	        }
	    });
	    store.load({params:{start:0,limit:LIMIT,groupId:window.parent.groupId}});
	    
	    var cm = new Ext.grid.ColumnModel([
	    	new Ext.grid.RowNumberer(),
	    	{id:'fid',hidden:true},
	    	{id:'flag',hidden:true},
	    	{
	    		id:'fnumber', 
	    		header: '编号', 
	    		hidden: true, 
	    		dataIndex:'fnumber'
	       },{
	    		id:'fheadline', 
	    		header: '标题', 
	    		width: 150, 
	    		dataIndex:'fheadline'
	       },{
	    		id:'fimages', 
	    		hidden: true, 
	    		dataIndex:'fimages'
	       },{
	    		id:'fmessage', 
	    		header: '活动信息', 
	    		width: 150, 
	    		hidden:true,
	    		dataIndex:'fmessage'
	       },{
	        	id: 'fsponsor',
	        	dataIndex:'fsponsor',
	    		width: 70, 
	        	header:'赞助商'
			},{
	        	id: 'fsite', 
	        	dataIndex: 'fsite', 
	    		width: 70, 
	        	header:'地址'
			},{
	        	id: 'fstartTime',
	        	dataIndex:'fstartTime',
	        	header:'开始时间', 
	    		format:"Y-m-d H:i:s"
			},{
	        	id: 'ffinishTime', 
	        	dataIndex: 'ffinishTime', 
	        	header:'结束时间', 
	    		format:"Y-m-d H:i:s"
			},{
	        	id: 'ybasicMember', 
	        	dataIndex: 'ybasicMember', 
	        	header:'发布人',
	        	hidden:true
			},{
				id:'ybasicType',
				dataIndex: 'ybasicType', 
				header:'类型',
				hidden:true
			},{
	        	id: 'ybasicSocialgroups', 
	        	dataIndex: 'ybasicSocialgroups', 
	        	header:'团体名称',
	        	renderer:function(value){
	        		return value.fname;
	        	}
			},{
				header:'费用',
	        	id: 'fcost',
	        	dataIndex:'fcost'
			},{
				header:'人数',
				id:'fpeopleNum',
				dataIndex:'fpeopleNum'
			},{
				header:'来源',
				id:'fsource',
				dataIndex:'fsource'
			},{
	        	id: 'fcomment', 
	        	dataIndex: 'fcomment', 
	        	header:'备注'
			},{
				id:'fbillState',
				dataIndex: 'fbillState', 
				header:'状态',
	    		renderer:status
			}
	    ]);
	    function status(data){
	    	if(data == 0){
	    		return '新增';
	    	}else if(data == 1){
	    		return '保存';
	    	}else if(data == 2){
	    		return '提交';
	    	}else if(data == 3){
	    		return '审核';
	    	}else if(data == 4){
	    		return '反审核';
	    	}else if(data == 5){
	    		return '生效';
	    	}else if(data == 6){
	    		return '<span style="color:red">失效</span>';
	    	}else if(data == 7){
	    		return '启用';
	    	}else if(data == 8){
	    		return '禁用';
	    	}else if(data == 9){
	    		return '<span style="color:red">作废</span>';
	    	}else if(data == 10){
	    		return '变更';
	    	}
		} 

	    var pagingToolBar = new Ext.PagingToolbar({
	    	pageSize:LIMIT,
	    	store:store,
	    	displayInfo: true,
	    	displayMsg: '当前显示第&nbsp;{0}&nbsp;到&nbsp;{1}&nbsp;条,共&nbsp;{2}&nbsp;条',
	    	emptyMsg: "没有记录",
			plugins: new Ext.ux.ProgressBarPager()
		}); 
	    
	    // create the Grid
	    var grid = new Ext.grid.GridPanel({
	    	region : 'center',
	    	id:'grid',
	    	border: false,
	    	columnLines:true,
	    	loadMask:true,
	    	store: store,
	    	frame:false,
	    	viewConfig: {forceFit:true},
	    	monitorResize: true,
	    	cm: cm,
	    	tbar: tool,
			bbar: pagingToolBar
	    });
	    	
	     if(getUser()[2]==1){
	    	new Ext.Viewport({
	        	renderTo:'mainDiv',
	        	id:'viewport',
	        	layout:'border',
	        	width:300,
	        	items:[treePanelTuanti,grid]
	        });
	    }else{ 
	    	new Ext.Viewport({
	        	renderTo:'mainDiv',
	        	id:'viewport',
	        	layout:'border',
	        	items:[grid]
	        });
	    }
	    Ext.EventManager.onWindowResize(function(){ 
	    	grid.getView().refresh() ;
		});
		grid.on('dblclick', function(){ editData("detail")});
	});
	/**************************** 自定义元素 **********************************/
	
	var myImage1 = new Ext.BoxComponent({
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
	var myImage2 = new Ext.BoxComponent({
	    id : 'browseImage2',
	    autoEl : {
	        width : 60,
	        height : 72,
	        tag : 'img',
	        src : Ext.BLANK_IMAGE_URL,
	        style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
	        complete : 'off',
	        id : 'photo2'
	    }
	});
	var myImage3 = new Ext.BoxComponent({
	    id : 'browseImage3',
	    autoEl : {
	        width : 60,
	        height : 72,
	        tag : 'img',
	        src : Ext.BLANK_IMAGE_URL,
	        style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
	        complete : 'off',
	        id : 'photo3'
	    }
	});
	var myImage4 = new Ext.BoxComponent({
	    id : 'browseImage4',
	    autoEl : {
	        width : 60,
	        height : 72,
	        tag : 'img',
	        src : Ext.BLANK_IMAGE_URL,
	        style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
	        complete : 'off',
	        id : 'photo4'
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
	ybasicTypeStore.load({params:{fmodelId:2}});
	var YBasicType = new Ext.form.ComboBox({
				fieldLabel : '信息分类',
				id : 'ybasicType',
				hiddenName : 'YBasicType.fid',
				typeAhead : true,
				triggerAction : 'all',
				lazyRender : true,
				mode : 'local',
				store : ybasicTypeStore,
				forceSelection : true,
	            blankText:'该字段不允许为空',
	            allowBlank: false,
				valueField : 'fid',
				displayField : 'fname'
			});
	//发布人
	var memberStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({ 
			disableCaching :false,
			method:'post',
			url : getPath() + '/member/findMemgerHql'
		}),
		reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
	});
	memberStore.load({params:{groupId:window.parent.groupId}});
	var YBasicMember = new Ext.form.ComboBox({
		fieldLabel : '发布人',
		id : 'ybasicMember',
		hiddenName : 'YBasicMember.fid',
		typeAhead : true,
		triggerAction : 'all',
		lazyRender : true,
		mode : 'local',
		store : memberStore,
		valueField : 'fid',
		displayField : 'fname',
		emptyText : '请选择发布人',
        blankText:'该字段不允许为空',
        allowBlank: false
	});
	var editHdxxglForm = new Ext.form.FormPanel({
		 y:100,
	     border : false,
	     fileUpload : true,
         waitMsgTarget : true,
         enctype : 'multipart/form-data',
		 frame	: true,
		 labelAlign: 'left',
		 buttonAlign: 'center',
		 labelWidth:60,
		 autoScroll:false,
		 containerScroll: true,
		 width: 680,
		 items:[{
				        layout : 'column',
				        border : false,
				        items : [{
							            columnWidth: .5,
							            layout: 'form',
							            border: false,
							            defaultType:'textfield',
							            defaults:{anchor:'93%'},
							            items:[{xtype:'hidden',id:'fid',name:'fid'},{
										        id:'fnumber',
										        fieldLabel:'编号',
										        maxLength:50,
										        name:'fnumber',
										        xtype:'hidden',
									            blankText:'该字段不允许为空',
									            allowBlank: false
										 },{
										        fieldLabel:'标题',
										        id:'fheadline',
										        name:'fheadline',
										        maxLength:50,
									            blankText:'该字段不允许为空',
									            allowBlank: false
										 },{
											 	xtype:'hidden',
										        id:'fimages',
										        name:'fimages'
										 },{
										        fieldLabel:'赞助商',
										        id:'fsponsor',
										        name:'fsponsor',
										        maxLength:50
										 },YBasicType,{
											 	xtype:'datefield',
											    fieldLabel:'开始时间',
											    id:'fstartTime',
											    name:'fstartTime',
												format:"Y-m-d H:i:s",
									            blankText:'该字段不允许为空',
									            allowBlank: false,
									            vtype : 'daterange',
											    endDateField : 'ffinishTime'
										},{
										        fieldLabel:'地址',
										        id:'fsite',
										        name:'fsite',
										        maxLength:25
										 }]
			            },{
			            	columnWidth: .5,
				            layout: 'form',
				            border: false,
				            defaultType:'textfield',
				            defaults:{anchor:'93%'},
				            items:[{
				            				xtype:'numberfield',
				            				allowDecimals:true,
				            				decimalPrecision:2,
										 	fieldLabel:'费用',
										 	id:'fcost',
										 	name:'fcost',
								            blankText:'该字段不允许为空',
								            allowBlank: false
										},{
											xtype:'numberfield',
											id:'fpeopleNum', 
											fieldLabel: '人数', 
											name:'fpeopleNum'
										},{
											readOnly:true,
											id:'fsource',
											name:'fsource',
											fieldLabel: '来源',
											value:'系统'
										},YBasicMember,{
										 	xtype:'datefield',
										    fieldLabel:'结束时间',
										    id:'ffinishTime',
										    name:'ffinishTime',
											format:"Y-m-d H:i:s",
								            blankText:'该字段不允许为空',
								            allowBlank: false,
								            vtype : 'daterange',
										    startDateField : 'fstartTime'
										},{
											id:'fcomment', 
											fieldLabel: '备注', 
											width:590,
											name:'fcomment',
											maxLength:125
									}]
			            }]
		 },{
			 layout:'column',
			 height:100,
	         column:4,
	         labelAlign: 'left',
	         items:[{
				        	 columnWidth: .25,
							 layout : "form",
							 defaults : {anchor : '100%',xtype : 'textfield'},
				             items:[myImage1,new Ext.ux.form.FileUploadField({
					                id : 'flsh',
					                name : 'file',
					                emptyText : 'Select an image',
					                buttonText : '浏览....'
					            })
			            	 ]
		             	},{
		                        columnWidth : .25,
								layout : "form",
								defaults : {anchor : '100%',xtype : 'textfield'},
		                        border : false,
		                        items:[myImage2,
							            new Ext.ux.form.FileUploadField({
							                id : 'flsh2',
							                name : 'file',
							                emptyText : 'Select an image',
							                buttonText : '浏览....'
							            })
		                        ]
		            	},{
	                        columnWidth : .25,
							layout : "form",
							defaults : {anchor : '90%',xtype : 'textfield'},
	                        border : false,
	                        items:[myImage3,
						            new Ext.ux.form.FileUploadField({
						                id : 'flsh3',
						                name : 'file',
						                emptyText : 'Select an image',
						                buttonText : '浏览....'
						            })]
			           },{
		                     columnWidth : .25,
							 layout : "form",
							 defaults : {anchor : '90%',xtype : 'textfield'},
		                     border : false,
		                     items:[myImage4,
							            new Ext.ux.form.FileUploadField({
							                id : 'flsh4',
							                name : 'file',
							                emptyText : 'Select an image',
							                buttonText : '浏览....'
							            })]
		          		}]
		 },{
			 	xtype:'htmleditor',
		        fieldLabel:'活动信息',
		        height:120,
		        width:590,
		        id:'fdetails',
		        name:'fmessage',
	            blankText:'该字段不允许为空',
	            allowBlank: false
		 },{
			 xtype:'hidden',
		     id:'ybasicSocialgroups',
		     name:'YBasicSocialgroups.fid'
		 },{
		        xtype:'hidden',
		        id:'fbillState',
		        name:'fbillState'
		 },{
			 xtype:'hidden',
		     id:'flag',
		     name:'flag'
		 }]
	});
	function searchBy(){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
		//通过id获取输入的数据
		var searchCondition = Ext.getCmp("searchCondition").getValue();
		//模糊查询
		store.load({params:{fheadline:searchCondition,start:0,limit:LIMIT,groupId:window.parent.groupId}});
	}

	//删除
	function delBy(){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
		var row = Ext.getCmp("grid").getSelectionModel().getSelected();
		if(!row){
	   	 	alertWarring("请选择需要删除的活动信息!");return;
	    }
		Ext.Ajax.request({
			url : getPath() + 'groupsActivity/deleteActivity',
			params : {fid : row.get('fid')},
			
			success: function (result, request) 
			{
				//var msg = eval(result.responseText);
				Ext.MessageBox.show({
					title : "提示",
					msg : "删除成功！",
					width : 250,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.INFO
				});
				Ext.getCmp("grid").store.reload();
			},
			failure: function ( result, request) {
				alertError("系统运行超时或执行失败");
			}
		});
	}
	function editData(row){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
			var detail = row;
			if(row!='add' && row!='detail'){
			     row = Ext.getCmp("grid").getSelectionModel().getSelected();
			     if(!row){
			    	 alertWarring("请选择需要编辑的活动信息!");return;
			     }
			     var billState = row.get("fbillState");
			     if(billState == 3){
						alertError("该信息只能做反审核，生效操作！");return;
				 }else if(billState == 5){
						alertError("该信息只能做反审核，失效操作！");return;
				 }else if(billState == 6){
						alertError("该信息只能做作废操作！");return;
				 }else if(billState == 9){
						alertError("该信息已作废，无法进行操作！");return;
				 }
			}else if(row =='detail'){
				row = Ext.getCmp("grid").getSelectionModel().getSelected();
				if(!row){
			    	 alertWarring("请选择需要查看详情的活动信息!");
			    }else{
			 		showDetail(row);
			    }
				return;
			}else{
				getNumberStore();
			}
	    	this.row = row;
	    	var that = this;
	         if(!editWin){
	            editWin = new Ext.Window({
	            	y:100,
			        id: 'editWin',
			        layout:'fit',
	                width:700,
	                height:460,
	                frame:true,
	                plain: true,
			        closeAction:'hide',
					maximizable:false,
					resizable:false,
					modal:true,
					border : false,
				    items :[editHdxxglForm],
				    buttons:[{
					    text: '保存',
					    handler:function(){			
					    	var flsh1 = Ext.getCmp('flsh').getValue();
					    	var flsh2 = Ext.getCmp('flsh2').getValue();
					    	var flsh3 = Ext.getCmp('flsh3').getValue();
					    	var flsh4 = Ext.getCmp('flsh4').getValue();
							if(flsh1 !="" && !img_reg.test(flsh1)){
								alertWarring("文件格式不正确！");return;
							}
							if(flsh2 !="" && !img_reg.test(flsh2)){
								alertWarring("文件格式不正确！");return;
							}
							if(flsh3 !="" && !img_reg.test(flsh3)){
								alertWarring("文件格式不正确！");return;
							}
							if(flsh4 !="" && !img_reg.test(flsh4)){
								alertWarring("文件格式不正确！");return;
							}
					          if(editHdxxglForm.form.isValid()){
					        	  editHdxxglForm.form.submit({
					                   method:'POST',
					                   waitMsg:'正在提交.....',
									   waitTitle:'请稍等',
					                   success:function(form, action){
						                  	 Ext.getCmp("grid").store.reload();
						                	 Ext.getCmp("editWin").hide();
					                   },
					                   failure:function(form, action){
							                      alertWarring(action.result.msg);
							                   }
						             });   
					          }else{
					               alertWarring("内容填写不完整");
					          }
					     }
					 },{
						 text:'取消',
					      handler:function(){
					    	  Ext.getCmp('editWin').hide();
					      }
						 
					 }],
				    listeners:{
				    	hide:function(){
				    		editHdxxglForm.form.reset();

					    	Ext.getCmp('browseImage1').getEl().dom.src =Ext.BLANK_IMAGE_URL;
					    	Ext.getCmp('browseImage2').getEl().dom.src =Ext.BLANK_IMAGE_URL;
					    	Ext.getCmp('browseImage3').getEl().dom.src =Ext.BLANK_IMAGE_URL;
					    	Ext.getCmp('browseImage4').getEl().dom.src =Ext.BLANK_IMAGE_URL;
			    		}
            		}
	            });
			}
	        setTitleAndUrl(row, editWin);
	        editHdxxglForm.getForm().findField('fbillState').setValue(0);
		    
		    if(row != 'add') {
		    	if(detail && detail == "detail"){
		    		var tool = Ext.getCmp('editWin').buttons;
		    		for(var i =0;i<tool.length;i++){
		    			tool[i].setVisible(false);
		    		}
		    		Ext.getCmp('editWin').doLayout(); 
		    		Ext.getCmp('editWin').setTitle("查看详情");
		    	}else{
		    		var tool = Ext.getCmp('editWin').buttons;
					for(var i =0;i<tool.length;i++){
						tool[i].setVisible(true);
					}
					Ext.getCmp('editWin').doLayout(); 
		    	}
		        loadData(row);
			}else{
				//增加的时候默认赋值新增人、类型、所属团体   目前写死  
					editHdxxglForm.form.setValues({
						ybasicSocialgroups: window.parent.groupId
				});
				var tool = Ext.getCmp('editWin').buttons;
				for(var i =0;i<tool.length;i++){
					tool[i].setVisible(true);
				}
			}
		}

	function loadData(row) {
		editHdxxglForm.form.setValues({
					fid 							: row.get('fid'),
					fnumber  				: row.get('fnumber'),
					fheadline    				: row.get('fheadline'),
					fimages				  	: row.get('fimages'),
					fmessage					: row.get('fmessage'),
					fsponsor					: row.get('fsponsor'),
					fsite							: row.get('fsite'),
					fstartTime				: row.get('fstartTime'),
					ffinishTime				: row.get('ffinishTime'),
					fcost						: row.get('fcost'),
					fpeopleNum			: row.get('fpeopleNum'),
					fsource					: row.get('fsource'),
					ybasicMember		: row.get('ybasicMember').fid,
					ybasicType				: row.get('ybasicType').fid,
					ybasicSocialgroups: row.get('ybasicSocialgroups').fid,
					fcomment  				: row.get('fcomment'),
					fbillState					: '10',
					flag							: row.get('flag')
			});
		 var imgLength = row.get('fimages').split(',');

         for(var i =0;i<imgLength.length;i++){
        	 if(i==imgLength.length-1)return;
             photoSrc(imgLength[i],(i+1));
         }
	}
	function setTitleAndUrl(row, win) {
		//alert(row);
		if(row == 'add') {
				win.setTitle('增加活动信息');
				editHdxxglForm.form.url = getPath() + 'groupsActivity/addActivity';
		}else{
				win.setTitle('修改活动信息');
				editHdxxglForm.form.url = getPath() + 'groupsActivity/updateActivity';
		}
		win.show();
	}
	
	//作废状态填写原因
	function showDiscarded(){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
		var selectRow = Ext.getCmp("grid").getSelectionModel().getSelected();
		if(selectRow){
			var fid= selectRow.get("fid");
			var state = selectRow.get('fbillState');
			if(state == 9){
				 alertError("该信息已作废，无法操作！");return;
			}else if(state == 5){
				 alertError("该信息只能做反审核，失效操作！");return;
			}else if(state == 3){
				 alertError("该信息只能做反审核，生效操作！");return;
			}else if(state == 0 || state == 10){
				 alertError("该信息只能做审核操作！");return;
			}else if(state == 4 ){
				 alertError("该信息只能做修改，审核操作！");return;
			}
			Ext.MessageBox.prompt("提示", "请输入将这条信息作废的原因！",function(bu,txt){
				if(txt && 0<txt.length<125){
					Ext.Ajax.request({
						url : getPath()+'groupsActivity/Invalid',
						params : {fid : fid ,fbillState : 9,comment:txt},
						
						success: function (result, request) 
						{
							//var msg = eval(result.responseText);
							Ext.MessageBox.show({
								title : "提示",
								msg : "更新成功！",
								width : 250,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.INFO
							});
							Ext.getCmp("grid").store.reload();
						},
						failure: function ( result, request) {
							alertError("系统运行超时或执行失败");
						}
					});
				}else{
					alertWarring("请输入作废原因(1-125个文字)");
				}
			},this,50);
		}else{
			Ext.MessageBox.show({
				title: '提示',
				msg: "请选择需要作废的信息!",
				width: 250,
				buttons: Ext.MessageBox.OK,
				buttonAlign: 'center'
			});
		}
	}
	
	function setState(stateId){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
		var selectRow = Ext.getCmp("grid").getSelectionModel().getSelected();
		if(selectRow){
			 fid=selectRow.get('fid');
			 var state = selectRow.get('fbillState');
			 if(state == stateId){
				 alertError("状态一致，勿重复操作！");return;
			 }else if(stateId != 9 && state == 6){
				 alertError("该信息只能做作废操作！");return;
			 }else if((stateId != 6 && stateId != 4) && state == 5){
				 alertError("该信息只能做反审核，失效操作！");return;
			 }else if((stateId != 4 && stateId != 5) && state == 3){
				 alertError("该信息只能做反审核，生效操作！");return;
			 }else if(stateId != 3 && (state == 0 || state == 10)){
				 alertError("该信息只能做审核操作！");return;
			 }else if(stateId != 3 && state==4){
				 alertError("该信息只能做修改,审核操作!");return;
			 }
			 Ext.MessageBox.show({
			    title: '提示',
			    msg: "确认要更新当前活动信息状态!",
			    width:300,
			    buttons: Ext.MessageBox.YESNO,
			    fn: function(e) {
			    		if(e == 'yes') {
			    			gridSubAjax(fid,stateId);
			    		}
			    	},
			    icon : Ext.MessageBox.QUESTION
			});
		}else{
			Ext.MessageBox.show({
				title: '提示',
				msg: "请选择需要更新状态的活动信息!",
				width: 250,
				buttons: Ext.MessageBox.OK,
				buttonAlign: 'center'
			});
		}
	}



	function gridSubAjax(fid,stateId)
	{
		//alert(stateId);
		var	  url = "";
		if(stateId=="3"||stateId=="4"){
			url = getPath()+'groupsActivity/auditActivity';
		}else if(stateId=="5"||stateId=="6"){
			url = getPath()+'groupsActivity/effectActivity';
		}
		//alert(url);
		Ext.Ajax.request({
			url : url,
			params : {fid : fid ,fbillState : stateId},
			
			success: function (result, request) 
			{
				//var msg = eval(result.responseText);
				Ext.MessageBox.show({
					title : "提示",
					msg : "更新成功！",
					width : 250,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.INFO
				});
				Ext.getCmp("grid").store.reload();
			},
			failure: function ( result, request) {
				alertError("系统运行超时或执行失败");
			}
		});
		
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
	
	
	function getNumberStore(){
		Ext.Ajax.request({
			url:getPath() + '/serialNumber/getSerialNumber',
			params:{prefix:'XX-SHTTHD'},
			success:function(response, options) {
				Ext.getCmp("fnumber").setValue(Ext.util.JSON.decode(response.responseText).serialNumber);
			}
		});
	}
	treePanel2.on('click',function(n){
		window.parent.groupId = n.attributes.id;
		memberStore.load({params:{groupId:window.parent.groupId}});
	});
	

	//以手机端查看信息详情
	function showDetail(row){
		if(!detailWin){
			detailWin = new Ext.Window({
	        	id: 'detailWin',
	        	title:'查看详情',
	        	autoScroll:true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
	            width:360,
	            height:590,
				bodyStyle:'background:#F7F7F7;;padding:5px;',
				html:'<div class="xx_img scroll relative"><div class="scroll_box" id="scroll_img"><ul class="scroll_wrap"></ul></div></div>'+
					'<div class="xx_center hide"><h1 class="xx_title"></h1><p class="xx_time"><label></label></p>'+
					'<ul class="scroll_position scroll_position_bg hide" id="scroll_position"></ul><div class="xx_detail"></div></div><div class="xx_loader"></div>'
			});
		}
		detailWin.show();
		$(".xx_title").html(row.get('fheadline'));
    	//图片相关展示
 		  var img = row.get('fimages');

 		  var images="";
 		  var position ="";
 		  var imgAarry = img.split(",");
 		  for(var i = 0;i<imgAarry.length-1;i++){
 			 if(i==0){
 				position+='<li class="on"><a href="javascript:void(0);">'+(i+1)+'</a></li>';
 			 }else{
	 			position+='<li><a href="javascript:void(0);">'+(i+1)+'</a></li>';
 			 } 
 			images+='<li><img src="'+imgAarry[i]+'"/></li>';
 		  }
 		  $(".scroll_wrap").html(images);
 		  $("#scroll_position").html(position);

 		  if(row.get('fstartTime')){
 			  var detail=(row.get('fstartTime').length>11?row.get('fstartTime').substr(0,10):row.get('fstartTime'));
 			  if(row.get('ffinishTime'))
 				  detail += '&nbsp;至&nbsp;'+(row.get('ffinishTime').length>11?row.get('ffinishTime').substr(0,10):row.get('ffinishTime'));
 			  if(row.get('fparticipationNum') && row.get('fparticipationNum')>0){
				  	detail += "<label>("+row.get('fparticipationNum')+"人已参加)</label>";
				  }
	 		  $(".xx_time").html(detail);
 		  	}
 		  	$('.xx_detail').html(row.get('fmessage').replace(/width:/g,"").replace(/text-indent:/g,""));
			var bullets = $('#scroll_position >li');
 			Swipe(document.getElementById('scroll_img'), {
 					auto: 3000,
 					continuous: true,
 					callback: function(pos) {
 						var i = bullets.length;
 						while (i--) {
 							bullets[i].className = ' ';
 						}
 						bullets[pos].className = 'on';
 					}
 				});
 				$(function(){
 					$('.scroll_position_bg').css({
 						width:$('#scroll_position').width()
 					});
 				});

		$(".xx_center").removeClass('hide');
		$(".xx_loader").addClass('hide');
	}
	</script>
</head>
	<body>
		<div id="mainDiv"></div>
	</body>
</html>