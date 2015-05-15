var LIMIT = 150;var mbszWin;var excel;var editIMGList;var sendWin;var replyWin;var detailWin;var timeInterval;var type=2;

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
				url: getPath() + 'groupsInform/findAllInformPagingList1'
		}),
		remoteStore : true, // 是否远程调用数据
		remoteSort : true   // 是否远程排序
	});
	var tool = new Ext.Toolbar({ //工具栏
		items:[/*'通知标题：',{
			xtype:'textfield',
			emptyText:'请输入标题',
			id:'searchCondition',
			width:150
		},
		'-',*/{text: '查看详细',enableToggle: false,iconCls:'common_details',handler:function(){editData("detail");}},
		'-',{text: '&nbsp;生&nbsp;效&nbsp;',enableToggle: false,iconCls:'common_past',handler:function(){setState(5);} },
		'-',{text: '&nbsp;失&nbsp;效&nbsp;',enableToggle: false,iconCls:'common_shutdown',handler: function(){setState(6);}},
		'-',{text: '启动刷新',enableToggle: false,iconCls:'common_flag',handler:function(){yunzo_start();}},
		'-',{text: '关闭刷新',enableToggle: false,iconCls:'common_nopast',handler:function(){yunzo_stop();}},
		'-',{text: '按评论排序',enableToggle: false,iconCls:'common_top',handler:function(){
			type=1;
			Ext.getCmp("grid").store.reload({params:{start:0,limit:LIMIT,type:type}});
		}},
		'-',{text: '按点赞排序',enableToggle: false,iconCls:'common_top',handler:function(){
			type=0;
			Ext.getCmp("grid").store.reload({params:{start:0,limit:LIMIT,type:type}});
		}}]
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
	    store.on('beforeload', function() {    
		  	  this.baseParams = {
		  			 start:0,limit:LIMIT,type:type
		  	  };
		});
	    store.load();
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
	    		dataIndex:'finformPeopleNum',
	    		hidden:true
	       },{
	    		id:'fparticipationNum', 
	    		header: '参与人数', 
	    		width: 150, 
	    		dataIndex:'fparticipationNum',
	    		hidden:true
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
	    		hidden:true,
	    		renderer:function(value){
	    			if(value){
	    				return value.fname;
	        		}
	        	}
			},{
	        	id: 'ybasicSocialgroups', 
	        	dataIndex: 'ybasicSocialgroups', 
	        	header:'团体名称',
	    		hidden:true,
	        	renderer:function(value){
	        		if(value){
	        			return value.fname;
        			}
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
	    
    	new Ext.Viewport({
        	renderTo:'mainDiv',
        	id:'viewport',
        	layout:'border',
        	items:[grid]
        });
    	
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
	function searchBy(){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		//通过id获取输入的数据
		var searchCondition = Ext.getCmp("searchCondition").getValue();
		//模糊查询
		store.load({params:{fheadline:searchCondition,start:0,limit:LIMIT}});
	}

	//删除
	function delBy(){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
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
		row = Ext.getCmp("grid").getSelectionModel().getSelected();
		if(!row){
	    	 alertWarring("请选择需要查看详情的通知信息!");
	    }else{
	 		showDetail(row);
	    }
		return;
	}
	
	function setState(stateId){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		var selectRow = Ext.getCmp("grid").getSelectionModel().getSelected();
		if(selectRow){
			 fid=selectRow.get('fid');
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
		var	  url = "";
		if(stateId=="3"||stateId=="4"){
			url = getPath()+'groupsInform/auditInform';
		}else if(stateId=="5"||stateId=="6"){
			url = getPath()+'groupsInform/effectInform';
		}
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
//启动实时刷新页面
function yunzo_start(){
	if(!timeInterval){
		timeInterval = setInterval(function(){
			Ext.getCmp("grid").store.reload();
	    },300000);
	}
	alertWarring("已启动实时刷新");
}
//关闭实时刷新页面
function yunzo_stop(){
	if(timeInterval){
		clearInterval(timeInterval);
	}
	alertWarring("已关闭实时刷新");
}