<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
	<title>云筑圈管理系统V1.0-通知信息管理</title>
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
	<link href="../static/webApp/shhdxq.css" type="text/css" rel="stylesheet"/>
	<script src='../static/jquery/jquery.min.js' type='text/javascript' charset='utf-8'></script>
	<script src='../static/jquery/imgScroll/hhSwipe.js'></script>
	<script type="text/javascript">
	var LIMIT = 26;var editWin;var mbszWin;var excel;var editIMGList;var sendWin;var replyWin;var detailWin;

	//上传图片类型  
	var img_reg = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/;
	var records = new Ext.data.Record.create([  
	                                          {name: 'fid'},
	                                          {name: 'fnumber'},
	                                          {name: 'fheadline'},
	                                          {name: 'flogoImage'},
	                                          {name: 'fmessage'},
	                                          {name: 'finformPeopleNum'},
	                                          {name: 'fparticipationNum'},
	                                          {name: 'fstartTime'},
	                                          {name: 'ffinishTime'},
	                                          {name: 'ybasicType'},
	                                          {name: 'ybasicSocialgroups'},
	                                          {name: 'fbillState'},
	                                          {name: 'fcomment'},
	                                          {name: 'fdetailAddress'},
	                                          {name: 'flag'}
	                                   ]);

	var store = new Ext.data.Store({
		reader:new Ext.data.JsonReader({
			root:'list',
			totalProperty:'count'
		},records),
		proxy:new Ext.data.HttpProxy({
				disableCaching :false,
				url: getPath() + 'groupsInform/findAllInformPagingList'
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
		items:['通知标题：',{
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
			functionId : 4
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
	    		id:'flogoImage', 
	    		hidden: true, 
	    		dataIndex:'flogoImage'
	       },{
	    		id:'fmessage', 
	    		header: '通知信息', 
	    		width: 150, 
	    		dataIndex:'fmessage',
	    		hidden:true
	       },{
	    		id:'finformPeopleNum', 
	    		header: '通知人数', 
	    		width: 150, 
	    		dataIndex:'finformPeopleNum'
	       },{
	    		id:'fparticipationNum', 
	    		header: '参与人数', 
	    		width: 150, 
	    		dataIndex:'fparticipationNum'
	       },{
	    		id:'fstartTime', 
	    		header: '开始时间', 
	    		width: 150, 
	    		dataIndex:'fstartTime'
	       },{
	    		id:'ffinishTime', 
	    		header: '结束时间', 
	    		width: 150, 
	    		dataIndex:'ffinishTime'
	       },{
				id:'ybasicType',
				dataIndex: 'ybasicType', 
				header:'类型',
	    		renderer:function(value){
	        		return value.fname;
	        	}
			},{
	        	id: 'ybasicSocialgroups', 
	        	dataIndex: 'ybasicSocialgroups', 
	        	header:'团体名称',
	        	renderer:function(value){
	        		return value.fname;
	        	}
			},{
	        	id: 'fcomment', 
	        	dataIndex: 'fcomment', 
	        	header:'备注',
	    		hidden:true
			},{
	        	id: 'fdetailAddress', 
	        	dataIndex: 'fdetailAddress', 
	        	header:'发布地址',
	    		hidden:true
			},{
				id:'fbillState',
				dataIndex: 'fbillState', 
				header:'状态',
	    		renderer:status
			},{id:'flag',dataIndex:'flag',hidden:true}
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
		grid.on('dblclick',function(){ editData("detail")});
	});
	/**************************** 自定义元素 **********************************/
	
	var myImage1 = new Ext.BoxComponent({
		fieldLabel:'logo图片',
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
	//类型
	var ybasicTypeStore  = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({ 
			disableCaching :false,
			method:'post',
			url : getPath() + '/type/findTypeHql'
		}),
		reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
	});
	ybasicTypeStore.load({params:{fmodelId:4}});
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
				valueField : 'fid',
				displayField : 'fname',
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
		 width: 650,
	     height: 330,
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
										        name:'fnumber',
										        maxLength:50,
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
										        fieldLabel:'Logo图片',
										        id:'flogoImage',
										        name:'flogoImage'
										 	},{
											    fieldLabel:'发布地点',
											    id:'fdetailAddress',
											    name:'fdetailAddress',
											    xtype:'hidden',
											    maxLength:125
											},{
											    xtype:'numberfield',
											    readOnly:true,
										        fieldLabel:'通知人数',
										        id:'finformPeopleNum',
										        name:'finformPeopleNum'
									 		},{
											    xtype:'numberfield',
											    readOnly:true,
										        fieldLabel:'参与人数',
										        id:'fparticipationNum',
										        name:'fparticipationNum'
									 		},{
											 	xtype:'datetimefield',
											    fieldLabel:'开始时间',
											    id:'fstartTime',
											    name:'fstartTime',
												format:"Y-m-d H:i:s",
									            blankText:'该字段不允许为空',
									            allowBlank: false
											},{
											 	xtype:'datetimefield',
											    fieldLabel:'结束时间',
											    id:'ffinishTime',
											    name:'ffinishTime',
												format:"Y-m-d H:i:s",
									            blankText:'该字段不允许为空',
									            allowBlank: false,
									            vtype : 'daterange',
											    startDateField : 'fstartTime'
											},{
												xtype:'hidden',
												id:'memberIds',
												name:'memberIds'
											}]
			            },{
			            	columnWidth: .5,
				            layout: 'form',
				            border: false,
				            defaultType:'textfield',
				            defaults:{anchor:'93%'},
				            items:[myImage1,
				                   new Ext.ux.form.FileUploadField({
						                id : 'flsh',
						                name : 'file',
						                emptyText : 'Select an image',
						                buttonText : '浏览....'
						            }),YBasicType,{
										id:'fcomment', 
										fieldLabel: '备注', 
										name:'fcomment',
										maxLength:125
									}]
			            }]
		 },{
		 	xtype:'htmleditor',
	        fieldLabel:'通知信息',
	        height:110,
	        width:550,
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
	/*会员加载器*/
	var treeLoaderHY = new Ext.tree.TreeLoader({});
	/*会员根节点*/
	var rootNodeHY = new Ext.tree.AsyncTreeNode({
		draggable:false,
		id : "source",
		text : "会员",
		draggable : false// 根节点不容许拖动
	});
	var treePanelHY =  new Ext.tree.TreePanel({
		title:'选择人员(默认发送所有)',
		width:150,
		height:295,
		loader : treeLoaderHY,
		rootVisible : false,
		monitorResize: true,
		autoScroll: true,
		border:false,
		root:rootNodeHY,
		listeners:{
			beforeload:function(node) {//加载前
				treeLoaderHY.dataUrl = getPath() + 'pos/getPositionTree?groupId='+window.parent.groupId; // 定义每个节点的Loader
			}
		}
	});
	treePanelHY.on('checkchange', function(node, checked) {   
		node.expand();   
		node.attributes.checked = checked;   
		node.eachChild(function(child) {   
			child.ui.toggleCheck(checked);   
			child.attributes.checked = checked;   
			child.fireEvent('checkchange', child, checked);   
		});   
	}, treePanelHY);
	treePanelHY.root.load();
	
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
	   	 	alertWarring("请选择需要删除的通知信息!");return;
	    }
		Ext.Ajax.request({
			url : getPath() + 'groupsInform/deleteInform',
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
			    	 alertWarring("请选择需要编辑的通知信息!");return;
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
			}else if(detail =='detail'){
				row = Ext.getCmp("grid").getSelectionModel().getSelected();
				if(!row){
			    	 alertWarring("请选择需要查看详情的通知信息!");
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
			        id: 'editWin',
			        layout:'fit',
	                width:800,
	                height:360,
	                frame:true,
	                plain: true,
			        closeAction:'hide',
					maximizable:false,
					resizable:false,
					modal:true,
					border : false,
				    items :[{layout:'column',border:false,items:[{columnWidth:.18,border:false,items:[treePanelHY]},{columnWidth:.82,border:false,items:[editHdxxglForm]}]}],
				    buttons:[{
					    text: '保存',
					    handler:function(){		
					    	var logourl = Ext.getCmp('flsh').getValue();
							if(logourl !="" && !img_reg.test(logourl)){
								alertWarring("文件格式不正确！");return;
							}
							var ids = treePanelHY.getChecked("id");
							Ext.getCmp('memberIds').setValue(ids);
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
					flogoImage				: row.get('flogoImage'),
					fmessage					: row.get('fmessage'),
					ybasicType				: row.get('ybasicType').fid,
					ybasicSocialgroups	: row.get('ybasicSocialgroups').fid,
					fcomment  				: row.get('fcomment'),
					fbillState					: '10',
					flag							: row.get('flag'),
					fdetailAddress		:row.get('fdetailAddress'),
					finformPeopleNum	:row.get('finformPeopleNum'),
					fparticipationNum	:row.get('fparticipationNum'),
					fstartTime				:row.get('fstartTime'),
					ffinishTime				:row.get('ffinishTime'),
					memberIds				:row.get('memberIds')
			});
		//获取点赞跟不赞同 的数量
		Ext.Ajax.request({
			url:getPath() + 'groupsInform/findPointLikeAndNotLike',
			params:{informId:row.get('fid')},
			success:function(response, options) {
				var data = Ext.util.JSON.decode(response.responseText);
				if(data && data.success){
					var span = "<span stype='margin-left:20px;font-weight:normal;color:blue;'>";
					if(data.pointLike){
						span+="当前点赞数"+data.pointLike+"&nbsp;&nbsp;";
					}
					if(data.notLike){
						span+="不赞同数"+data.pointLike+"&nbsp;&nbsp;";
					}
					editWin.setTitle(editWin.title+span+"</span>");
				}
			}
		});
		var imgLength = row.get('flogoImage').split(',');

	         for(var i =0;i<imgLength.length;i++){
	        	 if(i==imgLength.length-1)return;
	             photoSrc(imgLength[i],(i+1));
	         }
	}
	function setTitleAndUrl(row, win) { 
		if(row == 'add') {
				win.setTitle('增加通知信息');
				editHdxxglForm.form.url = getPath() + 'groupsInform/addInform';
		}else{
				win.setTitle('修改通知信息');
				editHdxxglForm.form.url = getPath() + 'groupsInform/updateInform';
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
						url : getPath()+'groupsInform/Invalid',
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
			    msg: "确认要更新当前通知信息状态!",
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
				msg: "请选择需要更新状态的通知信息!",
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
			url = getPath()+'groupsInform/auditInform';
		}else if(stateId=="5"||stateId=="6"){
			url = getPath()+'groupsInform/effectInform';
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
			params:{prefix:'XX-SHTTTZ'},
			success:function(response, options) {
				Ext.getCmp("fnumber").setValue(Ext.util.JSON.decode(response.responseText).serialNumber);
			}
		});
	}
	
	/***********************回复相关**************************/
		var replyRecords = new Ext.data.Record.create([
                                             {name: 'fid'},
                                             {name: 'freplyContent'},
                                             {name: 'finformPeopleId'},
                                             {name: 'fynparticipation'}
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
   replyStore.on("beforeload", function(currentStore, options) {
		var row = Ext.getCmp("grid").getSelectionModel().getSelected();
		this.baseParams = {
				activityId : row?row.get('fid'):0
		};
	});
   var replyCm = new Ext.grid.ColumnModel([
                                  	new Ext.grid.RowNumberer(),
                                  	{id:'fid',hidden:true},
                                  	{
                                  		header: '发布人',
                                  		id:'finformPeopleId',
                                  		dataIndex:'finformPeopleId'
                                   },{
                              	       	id:'freplyContent', 
                              	   		dataIndex:'freplyContent',
                              	   		width:150,
                              	   		header: '内容'
                                   },{
                                  		id:'fynparticipation', 
                                  		header: '是否参加', 
                                  		dataIndex:'fynparticipation',
                                  		renderer:function(value){
                                  			if(value==0){
                                  				return '否';
                                  			}else
                                  				return '是';
                                  		}
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
   	bbar: replyPagingToolBar
   });

//查询回复
function seachReply(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	var row = Ext.getCmp("grid").getSelectionModel().getSelected();
	if(!row){
   	 	alertWarring("请选择通知信息!");return;
    }
	if(!replyWin){
		replyWin = new Ext.Window({
			title:'通知信息回复信息',
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
	replyStore.load({params:{activityId:row.get('fid'),start:0,limit:LIMIT}});
	replyWin.show();
}

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
		  var img = row.get('flogoImage');

		  var images="";
		  var position ="";
		  var imgAarry = img.split(",");
		  for(var i = 0;i<imgAarry.length-1;i++){
			  images+='<li><img src="'+imgAarry[i]+'"/></li>';
		  }
		  $(".scroll_wrap").html(images);
		 if(row.get('fstartTime')){
			  var detail=(row.get('fstartTime').length>11?row.get('fstartTime').substr(0,10):row.get('fstartTime'));
			  if(row.get('ffinishTime'))
				  detail += '&nbsp;至&nbsp;'+(row.get('ffinishTime').length>11?row.get('ffinishTime').substr(0,10):row.get('ffinishTime'));
			  if(row.get('fparticipationNum') && row.get('fparticipationNum')>0){
			  	detail += "<label>("+row.get('fparticipationNum')+"人已参加)</label>";
			  }
 		  $(".xx_time").html(detail);
		  }
		  $('.xx_detail').html(row.get('fmessage'));
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
treePanel2.on('click',function(n){
	window.parent.groupId = n.attributes.id;
	treePanelHY.loader.dataUrl = getPath() + 'pos/getPositionTree?groupId='+window.parent.groupId;
	treePanelHY.root.reload();
});
	</script>
</head>
	<body>
		<div id="mainDiv"></div>
	</body>
</html>