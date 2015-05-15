<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
	<title>云筑圈管理系统V1.0-新闻</title>
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
	<script type="text/javascript" src=" ../static/ext/ux/Spinner.js"></script>
	<script type="text/javascript" src=" ../static/ext/ux/SpinnerField.js"></script>
	<script type="text/javascript" src=" ../static/ext/ux/DateTimeField.js"></script>
	<script type="text/javascript" src=" ../static/js/commonUtil.js"></script>
	<script type="text/javascript">
	var LIMIT = 3;var editWin;var replyWin;

	var records = new Ext.data.Record.create([  
	                                          {name: 'ftid'},
	                                          {name: 'ftitle'},
	                                          {name: 'fimageUrl'},
	                                          {name: 'ftImageUrl'},
	                                          {name: 'fnewsContent'},
	                                          {name: 'fsource'},
	                                          {name: 'freleaseTime'},
	                                          {name: 'fdetailsUrl'},
	                                          {name: 'fclassification'},
	                                          {name: 'fdescribe'},
	                                          {name: 'ftype'},
	                                          {name: 'fcreateTime'},
	                                          {name: 'fisPush'}
	                                   ]);

	var store = new Ext.data.Store({
		reader:new Ext.data.JsonReader({
			root:'obj',
			totalProperty:'count'
		},records),
		proxy:new Ext.data.HttpProxy({
				disableCaching :false,
				url: getPath() + 'tnewsHeadlineController/findAll'
		}),
		remoteStore : true, // 是否远程调用数据
		remoteSort : true   // 是否远程排序
	});

	var tool = new Ext.Toolbar({ //工具栏
		items:['新闻标题：',{
			xtype:'textfield',
			emptyText:'请输入标题',
			id:'searchCondition',
			width:120 
		},'-',{
			xtype:'combo',
   			width:120,
   			emptyText:'请选择新闻分类',
   			id:'xwfl',
			typeAhead: true,
			triggerAction: 'all',
			lazyRender:true,
		    mode: 'local', 
		  	store: new Ext.data.ArrayStore({
		          fields: [
		              'myId',
		              'displayText'
		          ],
		          data:[['财经','财经'], ['汽车', '汽车'],['房产','房产'],['科技','科技'],['体育','体育'],['军事','军事'],['娱乐','娱乐']]
		      }),
		     valueField: 'myId',
		     displayField: 'displayText',
		     renderer:function(value){
		      	return value.displayText;
		     }
		},'-',{
			xtype:'combo',
   			width:120,
   			emptyText:'请选择展示形式',
			id:'zsxs',
			typeAhead: true,
			triggerAction: 'all',
			lazyRender:true,
		    mode: 'local', 
		  	store: new Ext.data.ArrayStore({
		          fields: [
		              'myId',
		              'displayText'
		          ],
		          data: [[1,'轮播图'], [0, '列表']]
		      }),
		     valueField: 'myId',
		     displayField: 'displayText',
		     renderer:function(value){
		      	return value.displayText;
		     }
		},'-',{
			xtype:'datefield',
			emptyText:'请输入发布日期',
			id:'publicTime',
			format:'Y-m-d',
			width:120 
		},'-',{
			text: '&nbsp;查&nbsp;询&nbsp;',
    		enableToggle: false,
    		iconCls:'common_search',
    		handler:function(){
    			searchBy();
    		}
		},'-',{
			text: '&nbsp;新&nbsp;增&nbsp;',
    		enableToggle: false,
    		iconCls:'common_add',
    		handler:function(){
    			editData('add');
    		}
		},'-',{
			text: '&nbsp;修&nbsp;改&nbsp;',
    		enableToggle: false,
    		iconCls:'common_edit',
    		handler:function(){
    			editData();
    		}
		},'-',{
			text: '&nbsp;删&nbsp;除&nbsp;',
    		enableToggle: false,
    		iconCls:'common_delete',
    		handler:function(){
    			delBy();
    		}
		},'-',{
			text: '&nbsp;查看详情&nbsp;',
    		enableToggle: false,
    		iconCls:'common_details',
    		handler:function(){
    			Detail();
    		}
		},'-',{
			text: '&nbsp;查看评论&nbsp;',
    		enableToggle: false,
    		iconCls:'view',
    		handler:function(){
    			seachReply();
    		}
		}]
	});
	Ext.Ajax.request({
		url : getPath() + 'systemconfigurationMenu/getfunctionjs',
		method : 'GET',
		params : {
			functionId : 38
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
	    store.load({params:{start:0,limit:LIMIT}});
	    
	    var cm = new Ext.grid.ColumnModel([
	    	new Ext.grid.RowNumberer(),
	    	{id:'ftid',dataIndex:'ftid',hidden:true},
	    	{
	    		id:'ftitle', 
	    		header: '标题', 
	    		width: 150, 
	    		dataIndex:'ftitle'
	       },{
	    		id:'fimageUrl', 
	    		header: '图片', 
	    		width: 150, 
	    		dataIndex:'fimageUrl'
	       },{
	    		id:'ftImageUrl', 
	    		hidden: true, 
	    		dataIndex:'ftImageUrl'
	       },{
	    		id:'fnewsContent', 
	    		header: '新闻内容', 
	    		width: 150, 
	    		dataIndex:'fnewsContent'
	       },{
	        	id: 'fsource',
	        	dataIndex:'fsource',
	    		width: 70, 
	        	header:'新闻来源'
			},{
	        	id: 'freleaseTime', 
	        	dataIndex: 'freleaseTime', 
	    		width: 70, 
	        	header:'发布时间'
			},{
	        	id: 'fdetailsUrl',
	        	dataIndex:'fdetailsUrl',
	        	header:'详细页面路径'
			},{
	        	id: 'fclassification', 
	        	dataIndex: 'fclassification', 
	        	header:'新闻分类'
			},{
	        	id: 'fdescribe', 
	        	dataIndex: 'fdescribe', 
	        	header:'新闻描述'
			},{
				id:'ftype',
				dataIndex: 'ftype', 
				header:'展现形式',
				renderer:function(value){
					if(value==0){
						return '列表'
					}else
						return '轮播图'
				}
			},{
	        	id: 'fcreateTime', 
	        	dataIndex: 'fcreateTime', 
	        	header:'记录时间'
			},{
				header:'是否推送',
	        	id: 'fisPush',
	        	dataIndex:'fisPush'
			}]);

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
	    
    	new Ext.Viewport({
        	renderTo:'mainDiv',
        	id:'viewport',
        	layout:'border',
        	items:[grid]
        });
	    Ext.EventManager.onWindowResize(function(){ 
	    	grid.getView().refresh() ;
		});
		grid.on('dblclick', editData);
	});
	/**************************** 自定义元素 **********************************/
	
	var myImage1 = new Ext.BoxComponent({
		fieldLabel:'新闻图片',
	    id : 'browseImage1',
	    autoEl : {
	        width : 60,
	        height : 60,
	        tag : 'img',
	        src : Ext.BLANK_IMAGE_URL,
	        style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
	        complete : 'off',
	        id : 'photo1'
	    }
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
							            items:[{xtype:'hidden',id:'ftid',name:'ftid'},{
										        id:'ftitle',
										        fieldLabel:'标题',
										        maxLength:32,
										        name:'ftitle',
									            blankText:'该字段不允许为空',
									            allowBlank: false
										 },{
										        xtype:'combo',
												typeAhead: true,
												triggerAction: 'all',
												lazyRender:true,
											    mode: 'local', 
											  	store: new Ext.data.ArrayStore({
											  		  fields: [
												              'myId',
												              'displayText'
												          ],
											          data:[['财经','财经'], ['汽车', '汽车'],['房产','房产'],['科技','科技'],['体育','体育'],['军事','军事'],['娱乐','娱乐']]
											      }),
											    valueField: 'myId',
											    displayField: 'displayText',
										        fieldLabel:'新闻类型',
										        id:'fclassification',
										        hiddenName:'fclassification'
										 },{
											 	fieldLabel:'展现形式',
									   			xtype:'combo',
												id:'ftype', 
												hiddenName:'ftype',
												typeAhead: true,
												triggerAction: 'all',
												lazyRender:true,
											    mode: 'local', 
											  	store: new Ext.data.ArrayStore({
											          fields: [
											              'myId',
											              'displayText'
											          ],
											          data: [[1,'轮播图'], [0, '列表']]
											      }),
											     valueField: 'myId',
											     displayField: 'displayText',
											     renderer:function(value){
											      	return value.displayText;
											     }
									   		},{
										        fieldLabel:'详细页路径',
										        id:'fdetailsUrl',
										        name:'fdetailsUrl',
										        xtype:'hidden',
										        maxLength:200
											},{
												id:'fsource',
												name:'fsource',
												fieldLabel: '来源',
												maxLength:32
											},{
											 	xtype:'datetimefield',
											    fieldLabel:'发布时间',
											    id:'freleaseTime',
											    name:'freleaseTime',
												format:"Y-m-d H:i:s"
											},{
												id:'fcreateTime',
												name:'fcreateTime',
												xtype:'hidden'
											}]
			            },{
			            	columnWidth: .5,
				            layout: 'form',
				            border: false,
				            defaultType:'textfield',
				            defaults:{anchor:'93%'},
				            items:[myImage1,new Ext.ux.form.FileUploadField({
							                id : 'flsh',
							                name : 'file',
							                emptyText : 'Select an image',
							                buttonText : '浏览....'
							            }),{
											id:'fdescribe', 
											fieldLabel: '新闻描述', 
											maxLength:20,
											name:'fdescribe'
										},{
											xtype:'radiogroup',
										 	layout:'column',
										 	fieldLabel:'是否推送',
										 	id:'fisPush',
										 	columns:2,
										 	items:[{boxLabel: "否", name: 'fisPush',inputValue: 0,checked:true},  
										 	       {boxLabel: "是", name: 'fisPush',inputValue: 1}],
						    	            blankText:'该字段不允许为空',
						    	            allowBlank: false
										},{
										 	xtype:'hidden',
									        fieldLabel:'图片',
									        id:'fimageUrl',
									        name:'fimageUrl'
									 	}]
			            }]
		 },{
			 	xtype:'htmleditor',
		        fieldLabel:'新闻内容',
		        height:140,
		        width:590,
		        id:'fnewsContent',
		        name:'fnewsContent'
		 }]
	});


	/* function getNumberStore(){
		Ext.Ajax.request({
			url:getPath() + '/base/industry!doNotNeedSessionAndSecurity_getFNumber.ad',
			success:function(response, options) {
				Ext.getCmp("fnumber").setValue(response.responseText);
			}
		});
	} */

	function searchBy(){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 请重新登陆");return;}
		//通过id获取输入的数据
		var searchCondition = Ext.getCmp("searchCondition").getValue();
		var xwfl = Ext.getCmp("xwfl").getValue();
		var zsxs = Ext.getCmp("zsxs").getValue();
		var publicTime = Ext.getCmp("publicTime").getValue();
		//模糊查询
		store.load({params:{fheadline:searchCondition,start:0,limit:LIMIT,xwfl:xwfl,zsxs:zsxs,publicTime:publicTime}});
	}

	//删除
	function delBy(){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 请重新登陆");return;}
		if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
			  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
			  var fids=new Array();
			  for(var i=0;i<records.length;i++){
				  fids[i]=records[i].get('ftid');
			  }
				Ext.Ajax.request({
					url : getPath() + 'tnewsHeadlineController/delete',
					params : {ftids : fids},
					success: function (result, request) {
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
		}else{
	   	 	alertWarring("请选择需要删除的新闻信息!");return;
	    }
	}
	function editData(row){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 请重新登陆");return;}
			if(row!='add'){
			     row = Ext.getCmp("grid").getSelectionModel().getSelected();
			     if(!row){
			    	 alertWarring("请选择需要编辑的新闻信息!");return;
			     }
			}else{
				//getNumberStore();
			}
	    	this.row = row;
	    	var that = this;
	         if(!editWin){
	            editWin = new Ext.Window({
	            	y:100,
			        id: 'editWin',
			        layout:'fit',
	                width:700,
	                height:360,
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
			    		}
            		}
	            });
			}
	        setTitleAndUrl(row, editWin);
		    
		    if(row != 'add') {
		        loadData(row);
			}
		}

	function loadData(row) {
		editHdxxglForm.form.setValues({
					ftid 					: row.get('ftid'),
					ftitle  				: row.get('ftitle'),
					fimageUrl    	: row.get('fimageUrl'),
					fnewsContent	: row.get('fnewsContent'),
					fsource			: row.get('fsource'),
					freleaseTime	: row.get('freleaseTime'),
					fdetailsUrl		: row.get('fdetailsUrl'),
					fclassification	: row.get('fclassification'),
					fdescribe			: row.get('fdescribe'),
					ftype				: row.get('ftype'),
					fcreateTime	: row.get('fcreateTime'),
					fisPush			: row.get('fisPush')
			});
		 var imgLength = row.get('fimageUrl').split(',');

         for(var i =0;i<imgLength.length;i++){
        	 if(i==imgLength.length-1)return;
             photoSrc(imgLength[i],(i+1));
         }
	}
	function setTitleAndUrl(row, win) {
		if(row == 'add') {
				win.setTitle('增加新闻信息');
				editHdxxglForm.form.url = getPath() + 'tnewsHeadlineController/save';
		}else{
				win.setTitle('修改新闻信息');
				editHdxxglForm.form.url = getPath() + 'tnewsHeadlineController/update';
		}
		win.show();
	}
	function Detail(){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 请重新登陆");return;}
		var selectRow = Ext.getCmp("grid").getSelectionModel().getSelected();
		if(selectRow){
				//if(!detailWin){
				var   detailWin  =   new  Ext.Window(
			                {
			                    title:selectRow.get("title") ,//标题
			                    width: 600 ,
			                    height: 500 ,
			                    html: "<div style='width:100%;height:100%;background-color:white'><p ALIGN='center'>标题:"+selectRow.get("title")+"</p><p>"+selectRow.get("fsource")+"&nbsp;&nbsp;"+selectRow.get("freleaseTime")+"</p><div>"+selectRow.get("fnewsContent")+"</div>"//panel主体中的内容，可以执行html代码
	
			                });    
			//   }        
			   detailWin.show();
	   }else{
		   alertError("请选择新闻信息!");
	   }
	}
	
	/****************评论相关****************/
	var replyRecords = new Ext.data.Record.create([
	                                               {name: 'fid'},
	                                               {name: 'ybasicMember'},
	                                               {name: 'fcontents'},
	                                               {name: 'ftime'}
	                                            ]);
	     var replyStore = new Ext.data.Store({
	     	   	reader:new Ext.data.JsonReader({
	     	   		root:'list',
	     	   		totalProperty:'count'
	     		},replyRecords),
	     		proxy:new Ext.data.HttpProxy({
	     				disableCaching :false,
	     				url: getPath() + '/wallReply/findReplyPage'
	     		}),
	     		remoteStore : true, // 是否远程调用数据
	     		remoteSort : true   // 是否远程排序
	     });
	     var replyCm = new Ext.grid.ColumnModel([
	                                    	new Ext.grid.RowNumberer(),
	                                    	{id:'fid',hidden:true},
	                                    	{
	                                    		header: '真实名称',
	                                    		id:'ybasicMember',
	                                    		dataIndex:'ybasicMember',
	                                    		renderer:function(value){
	                                    			return value.fname;
	                                    		}
		                                     },{
		                                	       	id:'fcontents', 
		                                	   		dataIndex:'fcontents',
		                                	   		width:150,
		                                	   		header: '评论内容'
		                                     },{
		                                    		id:'ftime', 
		                                    		header: '回复时间', 
		                                    		dataIndex:'ftime'
		                                    }]);
	     var replyPagingToolBar = new Ext.PagingToolbar({
	     	pageSize:LIMIT,
	     	store:replyStore,
	     	displayInfo: true,
	     	displayMsg: '当前显示第&nbsp;{0}&nbsp;到&nbsp;{1}&nbsp;条,共&nbsp;{2}&nbsp;条',
	     	emptyMsg: "没有记录",
	     	plugins: new Ext.ux.ProgressBarPager()
	     }); 

	     // create the Grid
	  var replyGrid = new Ext.grid.GridPanel({
	     	id:'replyGrid',
	     	border: false,
	     	columnLines:true,
	     	loadMask:true,
	     	store: replyStore,
	     	frame:false,
	     	viewConfig: {forceFit:true},
	     	monitorResize: true,
	     	cm: replyCm,
	     	tbar: new Ext.Toolbar({ //工具栏
	     		items:[{
	     			text: '&nbsp;删&nbsp;除&nbsp;',
	     			enableToggle: false,
	     			iconCls:'common_delete',
	     			handler: function(){
	     				deleteReply();
	     			}
	     		}]
	     	}),
	     	bbar: replyPagingToolBar
	     });

	  //查看评论
	  function seachReply(){
	  	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 请重新登陆");return;}
	  	var row = Ext.getCmp("grid").getSelectionModel().getSelected();
	  	if(!row){
	     	 	alertWarring("请选择新闻信息!");return;
	      }
	  	if(!replyWin){
	  		replyWin = new Ext.Window({
	  			title:'新闻信息评论',
	  	        id: 'replyWin',
	  	        layout:'fit',
	            width:700,
	            height:450,
	            frame:true,
	            plain: true,
	  	        closeAction:'hide',
	  			maximizable:false,
	  			resizable:false,
	  			modal:true,
	  			border : false,
	  		    items :[replyGrid]
	          });
	  	}
	  	replyStore.load({params:{ftid:row.get('fid'),start:0,limit:LIMIT}});
	  	replyWin.show();
	  }
	  
	  function deleteReply(){
			if(checkLoginTimeOut()){ alertWarring("登陆已超时! 请重新登陆");return;}
			if(Ext.getCmp('replyGrid').getSelectionModel().hasSelection()){
				  var records=Ext.getCmp('replyGrid').getSelectionModel().getSelections();
				  var fids=new Array();
				  for(var i=0;i<records.length;i++){
					  fids[i]=records[i].get('fid');
				  }
				 Ext.MessageBox.show({
				    title: '提示',
				    msg: "确认要删除活动回复!",
				    width:250,
				    buttons: Ext.MessageBox.YESNO,
				    fn: function(e) {
				    		if(e == 'yes') {
				    			deleteReplyAjax(fids);
				    		}
				    	},
				    icon : Ext.MessageBox.QUESTION
				});
			}
			else
			{
				Ext.MessageBox.show({
					title: '提示',
					msg: "请选择活动回复!",
					width: 150,
					buttons: Ext.MessageBox.OK,
					buttonAlign: 'center'
				});
			}
		}

		function deleteReplyAjax(param){
			Ext.Ajax.request({
				url : getPath() + '/wallReply/deleteWallReply',
				params : {fids : param},
				success: function (result, request) {
					//var msg = eval(result.responseText);
					Ext.MessageBox.show({
						title : "提示",
						msg : "删除成功！",
						width : 250,
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.INFO
					});
					Ext.getCmp("replyGrid").store.reload();
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
	</script>
</head>
	<body>
		<div id="mainDiv"></div>
	</body>
</html>