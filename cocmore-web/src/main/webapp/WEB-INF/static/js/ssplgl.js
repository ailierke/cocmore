var LIMIT = 150;var excel;var editIMGList;var sendWin;var replyWin;var detailWin;var timeInterval;

	//上传图片类型  
	var img_reg = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/;
	var records = new Ext.data.Record.create([  
	                                          {name: 'fid'},
                                              {name: 'fcontents'},
                                              {name: 'ftime'},
                                              {name: 'ybasicMember'},
                                              {name: 'flag'}
	                                   ]);

	var store = new Ext.data.Store({
		reader:new Ext.data.JsonReader({
			root:'object',
			totalProperty:'count'
		},records),
		proxy:new Ext.data.HttpProxy({
				disableCaching :false,
				url: getPath() + 'groups/getLeastYComent'
		}),
		remoteStore : true, // 是否远程调用数据
		remoteSort : true   // 是否远程排序
	});/*
	store.on("beforeload", function() {
		this.baseParams = {
				groupId : window.parent.groupId
		};
	});*/
	var tool = new Ext.Toolbar({ //工具栏
		items:[{text: '删除',enableToggle: false,iconCls:'common_delete',handler:function(){delBy();}},
		'-',{text: '启动刷新',enableToggle: false,iconCls:'common_flag',handler:function(){yunzo_start();}},
		'-',{text: '关闭刷新',enableToggle: false,iconCls:'common_nopast',handler:function(){yunzo_stop();}}]
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
	    	{id:'fid',dataIndex:'fid',hidden:true},{
          		header: '发布人',
          		id:'ybasicMember',
          		dataIndex:'ybasicMember',
          		renderer:function(value){
          			return value.fname;
          		}
           },{
      	       	id:'fcontents', 
      	   		dataIndex:'fcontents',
      	   		width:150,
      	   		header: '内容'
           },{
          		id:'ftime', 
          		header: '评论时间', 
          		dataIndex:'ftime'
          },{
      	   		id:'flag',
      	   		dataIndex:'flag',
      	   		hidden:true
      	   }
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
	    
    	new Ext.Viewport({
        	renderTo:'mainDiv',
        	id:'viewport',
        	layout:'border',
        	items:[grid]
        });
    	
	    Ext.EventManager.onWindowResize(function(){ 
	    	grid.getView().refresh() ;
		});
	});
	/**************************** 自定义元素 **********************************/
	
	//删除
	function delBy(){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		var row = Ext.getCmp("grid").getSelectionModel().getSelected();
		if(!row){
	   	 	alertWarring("请选择需要删除的评论信息!");return;
	    }
		Ext.Ajax.request({
			url : getPath() + 'groups/delYComent',
			params : {fid : row.get('fid')},
			success: function (result, request){
				var msg = Ext.util.JSON.decode(result.responseText);
				if(msg.success){
					Ext.getCmp("grid").store.reload();
				}
				Ext.MessageBox.show({
					title : "提示",
					msg : msg.msg,
					width : 250,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.INFO
				});
			},
			failure: function ( result, request) {
				alertError("系统运行超时或执行失败");
			}
		});
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