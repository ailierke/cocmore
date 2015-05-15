var LIMIT = 26;var editWin;var mbszWin;var superId;var i=0;var replyWin;

 var records = new Ext.data.Record.create([
                                          {name: 'fid'},
                                          {name: 'ybasicMember'},
                                          {name: 'fcontents'},
                                          {name: 'fpublished'},
                                          {name: 'fisOpen'}
                                       ]);
var store = new Ext.data.Store({
	proxy:new Ext.data.HttpProxy({
		disableCaching :false,
		method:'post',
		url : getPath() + '/manshow/findAllDynamicPagingList'
	}),  //是指获取数据的方式
	reader:new Ext.data.JsonReader({
		root:'list',
		totalProperty:'count'
	},records),
	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});

store.on("beforeload", function() {
	this.baseParams = {
			groupId : window.parent.groupId
	};
});
//会员列表
var memberStore = new Ext.data.ArrayStore({
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'post',
		url : getPath() + '/member/findByGroupId'
	}),
	fields: [
	         'fid',
	         'fname'
	     ]
});
var tool = new Ext.Toolbar({ //工具栏
	items:[{
	    	   	xtype:'combo',
	   			width:100,
				id:'searchCondition', 
				triggerAction : 'all',
				lazyInit : true,
				mode:'local',
				valueField : 'fid',
				displayField : 'fname',
				selectOnFocus:true,
				editable:false,
				emptyText:'请选择会员',
			  	store: memberStore,
			    width:150
    	   },'-']
});


Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 210
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
	       	id:'ybasicMember', 
	   		dataIndex:'ybasicMember',
	   		header: '会员', 
	   		renderer:function(value){
	   			return value.fname;
	   		}
       },{
    		id:'fcontents', 
    		header: '内容', 
    		dataIndex:'fcontents'
       },{
	   		id:'fpublished', 
			header: '发布时间', 
			dataIndex:'fpublished'
	   },{
			id:'fisOpen', 
			header: '公开范围', 
			dataIndex:'fisOpen',
			renderer:function(value){
				if(value==0){
					return '全部开放';
				}else
					return '对会员开放';
			}
	   }/*,{
		    hidden:true,
		    id:'YImages',
		    dataIndex:'YImages'
	   }*/
    ]);
    
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

    // render the grid to the specified div in the page
    
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
    	grid.getView().refresh() 
	});
	grid.on('dblclick', editData);
});

/**************************** 自定义元素 **********************************/
var editDbxxForm = new Ext.form.FormPanel({
     border : false,
	 frame	: true,
	 id:'DbxxForm',
	 labelAlign: 'left',
	 buttonAlign: 'center',
	 labelWidth:80,
	 containerScroll: true,
     autoScroll:true,
	 defaults:{anchor:'98%',xtype:'textfield'},
	 items:[{
				xtype : 'combo',
				fieldLabel : '会员名',
				id : 'ybasicMember',
				hiddenName : 'YBasicMember.fid',
				triggerAction : 'all',
				lazyInit : true, 
				mode : 'local',
		     	store: memberStore,
		    	valueField : 'fid',
		    	displayField : 'fname',
	            readOnly:true
    	   },{
			    fieldLabel:'发布时间',
			    id:'fpublished',
			    name:'fpublished',
			    readOnly:true
		   },{
			    fieldLabel:'公开范围',
		    	xtype:'radiogroup',
			 	layout:'column',
			 	id:'fisOpen',
			 	columns:2,
			 	items:[{boxLabel: "全部开放", name: 'fisOpen',inputValue: 0,checked:true},  
			 	       {boxLabel: "对会员开放", name: 'fisOpen',inputValue: 1}],
	            readOnly: true
		   },{
			   	xtype:'hidden',
			    fieldLabel:'图片',
			    id:'yimages',
			    name:'yimages',
			    readOnly:true
		   },{
			    xtype:'textarea',
		        fieldLabel:'内容',
		        id:'fcontents',
		        name:'fcontents',
	            readOnly:true
		   },{
	        	xtype:'hidden',
		        fieldLabel:'团体',
		        id:'ybasicSocialgroups',
		        name:'YBasicSocialgroups.fid'
		   }]
});
/***********************回复相关**************************/
var replyRecords = new Ext.data.Record.create([
                                               {name: 'fid'},
                                               {name: 'ybasicMember'},
                                               {name: 'fcontents'},
                                               {name: 'ftime'}
                                            ]);
var replyStore = new Ext.data.Store({
	   	reader:new Ext.data.JsonReader({
	   		root:'obj.list',
 	   		totalProperty:'obj.count'
		},replyRecords),
		proxy:new Ext.data.HttpProxy({
				disableCaching :false,
				url: getPath() + 'tnewsHeadlineController/getAllDynamicPagingListByComment'
		}),
		remoteStore : true, // 是否远程调用数据
		remoteSort : true   // 是否远程排序
});
replyStore.on('beforeload', function() {   
	  var row = Ext.getCmp("grid").getSelectionModel().getSelected();
  	  this.baseParams = {
  			 start:0,limit:LIMIT,ftid:row.get('fid')
  	  };
});
var replyCm = new Ext.grid.ColumnModel([
                                      	new Ext.grid.RowNumberer(),
                                    	{id:'fid',hidden:true},
                                    	{
                                    		header: '会员名',
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
       	border: true,
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

/**************************** 自定义元素结束 *******************************/
/**************************** 操作 **********************************/

  //查询回复
  function seachReply(){
  	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
  	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
  	var row = Ext.getCmp("grid").getSelectionModel().getSelected();
  	if(!row){
  		alertWarring("请选择个人秀信息!");return;
  	}
  	if(!replyWin){
  		replyWin = new Ext.Window({
  			title:'个人秀回复信息',
  		    id: 'replyWin',
  		    layout:'fit',
  		    width:500,
  		    height:350,
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
  
/**************************删除回复****************************/
function deleteReply(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	if(Ext.getCmp('replyGrid').getSelectionModel().hasSelection()){
		var records=Ext.getCmp('replyGrid').getSelectionModel().getSelections();
		var fids=new Array();
		for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
		}
		Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认要删除该个人秀回复?",
		    width:250,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
						Ext.Ajax.request({
							url : getPath() + 'tnewsHeadlineController/deleteByComment',
							params : {fids : fids},
							success: function (result, request){
									Ext.MessageBox.show({
										title : "提示",
										msg : "删除成功！",
										width : 250,
										buttons : Ext.MessageBox.OK,
										icon : Ext.MessageBox.INFO
									});
									Ext.getCmp("replyGrid").store.reload();
							},failure: function ( result, request) {
									alertError("系统运行超时或执行失败");
							}
						});
		    		}
		    	}
		   });
	}else{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择回复!",
			width: 150,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}


function editData(row){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	row = Ext.getCmp("grid").getSelectionModel().getSelected();
	if(!row){
		alertWarring("请选择需要查看详细个人秀信息!");return;
	}
    this.row = row;
     if(!editWin){
        editWin = new Ext.Window({
        	title:'查看详情',
	        id: 'editWin',
	        layout:'fit',
            width:360,
            height:360,
            frame:true,
            plain: true,
            autoScroll:true,
	        closeAction:'hide',
			maximizable:false,
			resizable:false,
			modal:true,
			border : false,
		    items :[editDbxxForm],
		    listeners:{
		    	hide:function(){
		    		editDbxxForm.form.reset();
		    		for(i;i>-1;i--){
		    			editDbxxForm.remove('browseImage'+i,true);
		    		}
		    		i=0;
		    		Ext.getCmp('DbxxForm').doLayout();
		    	}
		    }
        });
	}
    loadData(row);	
}

function loadData(row) {
	editDbxxForm.form.setValues({
		fid			: row.get('fid'),
		ybasicMember: row.get('ybasicMember').fid,
		fcontents  	: row.get('fcontents'),
		fpublished  : row.get('fpublished'),
		fisOpen		: row.get('fisOpen')
	});
	
	//获取点赞数量
	Ext.Ajax.request({
		url:getPath() + 'manshow/getNumByshowId',
		params:{informId:row.get('fid')},
		success:function(response, options) {
			var data = Ext.util.JSON.decode(response.responseText);
			if(data && data.success){
				var span = "<span stype='margin-left:20px;font-weight:normal;color:blue;'>";
				if(data.list){
					span+="当前点赞数"+data.list+"&nbsp;&nbsp;";
				}
				Ext.getCmp('editWin').setTitle(Ext.getCmp('editWin').title+span+"</span>");
			}
		}
	});
	
	//读取个人秀图片
    Ext.Ajax.request({
        url:getPath()+"/manshow/getimgByshowId",
        method : 'GET',
        params:{showId:row.get('fid')},
        success:function(response, opts){
            var obj = Ext.util.JSON.decode(response.responseText).list;
            for(i;obj && i<obj.length;i++){
            	var img = obj[i];
            	photoSrc(img.faddress,i);
            }
            i--;
        }
    });
    editWin.show();
}
//图片动态生成预览
function photoSrc(src,index){
	Ext.getCmp('DbxxForm').add(
		new Ext.BoxComponent({
		    id : 'browseImage'+index,
		    autoEl : {
		        height : 60,
		        tag : 'img',
		        src : src,
		        style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
		        complete : 'off'
		    }
		})
	);
    Ext.getCmp('DbxxForm').doLayout();
}
//条件查询
function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	//通过id获取输入的数据
	var searchCondition = Ext.getCmp("searchCondition").getValue();
	//模糊查询
	store.load({params:{memberId:searchCondition,groupId:window.parent.groupId,start:0,limit:LIMIT}});
}
//================================删除=======================================
function delBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  var fids=new Array();
		  for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
		  }
		Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认要删除当前个人秀信息？!",
		    width:300,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			gridSubAjax(fids);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}else{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择需要删除的个人秀信息!",
			width: 250,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}
function gridSubAjax(param){
	Ext.Ajax.request({
		url : getPath() + 'manshow/delete',
		params : {fids : param},
		
		success: function (result, request) 
		{
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

treePanel2.on('click',function(n){
	window.parent.groupId = n.attributes.id;
	memberStore.load({
     	params:{groupId:window.parent.groupId}
 	});
});