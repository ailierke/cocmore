var LIMIT = 26;var editWin;var mbszWin;var superId;var yhsqWin;var jssqWin;var userId=0;
var fids=[];

var records = new Ext.data.Record.create([
                                          {name:'fid'},
                                          {name: 'faccount'},
                                          {name: 'fuserPassword'},
                                          {name: 'fbillState'},
                                          {name: 'fprivileges'},
                                          {name: 'ftypeId'}
                                       ]
                                   );
var store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
		root:'obj.list',
		totalProperty:'obj.count'
	},records),
	proxy:new Ext.data.HttpProxy({
			disableCaching :false,
			url: getPath() + 'user/findUserAll'
	}),
	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});

var tool=new Ext.Toolbar({ //工具栏
	items:['用户名称：',{
		xtype:'textfield',
		emptyText:'请输入用户账号',
		id:'searchCondition',
		width:150 
	},'-','类型：',{
   			xtype:'combo',
   			width:100,
			id:'type', 
			triggerAction: 'all',
			lazyRender:true,
		    mode: 'local', 
		  	store: new Ext.data.ArrayStore({
		          fields: [
		              'myId',
		              'displayText'
		          ],
		          data: [[0,'团体'], [1, '职员']]
		      }),
		     valueField: 'myId',
		     displayField: 'displayText',
		     renderer:function(value){
		      	return value.displayText;
		     }
   		},'-']
});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 20
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

    store.on('load', function(store, records, options) {
        if (records.length = 0) {
            Ext.Msg.alert("提示信息", "没有符合条件的数据!"); 
        }
    });
    store.load(/*{params:{start:0,limit:LIMIT}}*/);
    var cm = new Ext.grid.ColumnModel([                                       
    	new Ext.grid.RowNumberer(),{id:'fid',hidden:true},
    	{
    		id:'faccount', 
    		header: '管理员账号', 
    		width: 240, 
    		sortable: true, 
    		dataIndex:'faccount'
       },{
    		id:'fbillState', 
    		header: '状态', 
    		width: 150, 
    		sortable: true, 
    		renderer:status,
    		dataIndex:'fbillState'
       },{
	       	id:'fuserPassword', 
	   		header: '密码', 
	   		width: 200, 
	   		sortable: true, 
	   		dataIndex:'fuserPassword',
	   		hidden:true
	   		
       },{
    		id:'ftypeId', 
    		header: '类型',
    		width: 240, 
    		sortable: true,
    		dataIndex:'ftypeId',
    		renderer:function(value){
    			if(value==1){
    				return '职员'
    			}else{
    				return '团体'
    			}
    		}
       },{
	   		id:'fprivileges', 
			header: '授权状态', 
			width: 100, 
			sortable: false, 
			renderer:privileges,
			dataIndex:'fprivileges'
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
    function privileges(data){
    	if(data == 0){
    		return '未授权';
    	}else if(data == 1){
    		return '授权';
    	}else if(data == 2){
    		return '角色权限';
    	}else if(data == 3){
    		return '用户权限';
    	}else if(data == 4){
    		return '角色和用户权限';
    	}else if(data == 5){
    		return '待定';
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

    // render the grid to the specified div in the page
    
    var displayPanel = new Ext.Viewport({
    	renderTo:'mainDiv',
    	layout:'border',
    	items:[grid]
    });
    Ext.EventManager.onWindowResize(function(){ 
    	grid.getView().refresh() 
	});
    grid.on('dblclick', yhsq);
});

/**************************** 自定义元素 **********************************/
var mkPanelLoader = new Ext.tree.TreeLoader({
	dataUrl:getPath() + 'organizationauthorized/getByBasicFunctionTree'
});
var mkPanel = new  Ext.tree.TreePanel({
	title 			 : '功能模块', 
  	border     		 : true,
    split      		 : true,  
  	animate    		 : true,  
  	rootVisible		 : false,
  	autoScroll 		 : true,
	height			 : 435,
	useArrows: true,
	autoScroll: true,
	animate: true,
	enableDD: true,
	containerScroll: true,
	border: false,
	loader:mkPanelLoader,
	//dataUrl: getPath() + 'organizationauthorized/getByBasicFunctionTree',
	root: {
	    nodeType: 'async',
	    text: 'mkRoot',
	    draggable: false,
	    id: 'mkRoot'
	}
}); 
mkPanel.on('checkchange', function(node, checked) {   
	node.expand();   
	node.attributes.checked = checked;   
	node.eachChild(function(child) {   
		child.ui.toggleCheck(checked);   
		child.attributes.checked = checked;   
		child.fireEvent('checkchange', child, checked);   
	});   
}, mkPanel); 

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
var orgRoot = new Ext.tree.AsyncTreeNode({
	draggable:false,
	id : "orgRoot",
	text : "集团",
	loader: new Ext.tree.TreeLoader({dataUrl:getPath() + 'org/getOrgTree'})
});
var orgPanel = new Ext.tree.TreePanel({
	height:320,
	title:'组织结构',
	loader : treeOrgLoader,
	rootVisible : false,
	autoScroll : true,
	monitorResize: true,
	root:rootOrgNode,
	listeners:{
		checkchange:function(n){//点击事件
			var opids=orgPanel.getChecked("id");
			//alert(opids);
			sgPanel.loader.dataUrl = getPath() + 'roleauthorization/getGroupTree';
			sgPanel.loader.baseParams  ={opids:opids,id:userId},
			sgPanel.root.reload();
			//jsStore.load({params:{orgId:n.attributes.id}});
		},
		beforeload:function(node) {//加载前
			treeOrgLoader.dataUrl = getPath() + 'roleauthorization/getOrgTree'; // 定义每个节点的Loader
		}
	}
});
var sgRoot = new Ext.tree.AsyncTreeNode({
	draggable:false,
	id : "sgRoot",
	text : "团体",
	loader: new Ext.tree.TreeLoader({dataUrl:getPath() + 'roleauthorization/getGroupTree'})
});
var sgPanel = new Ext.tree.TreePanel({
	height:320,
	title:'团体信息',
	loader:treeGroupLoader,
	rootVisible : false,
	autoScroll : true,
	monitorResize: true,
	root:rootGroupNode
//	listeners:{
//		click:function(n){
//			jsStore.load({params:{groupId:n.attributes.id}});
//		}
//  	}
});
var jsStore = new Ext.data.Store({
    reader: new Ext.data.JsonReader({ //使用JSON传输入数据
        root : 'obj',
        totalProperty : 'totalProperty'           // 定义根节点和分页总条数属性，root与totalProperty已经在CommonUtil中包含，这里只需要在页面属性添加即可
    }, [{name: 'fid'},
        {name: 'fnumber'},
        {name: 'fname'},
        {name: 'fremark'}
		]),
    proxy:new Ext.data.HttpProxy({url:getPath() + 'character/findAll'}),
    remoteStore:true,    // 是否远程调用数据
    remoteSort:true      // 是否远程排序
});
var jsSm = new Ext.grid.CheckboxSelectionModel({handleMouseDown: Ext.emptyFn});
var jsCm = new Ext.grid.ColumnModel([
                                 	new Ext.grid.RowNumberer(),jsSm,{id:'fid',dataIndex:'fid',hidden:true},
                                	{
                                		id:'fnumber', 
                                		header: '编号', 
                                		width: 240, 
                                		dataIndex:'fnumber'
                                   },{
                                		id:'fname', 
                                		header: '名称', 
                                		width: 150, 
                                		dataIndex:'fname'
                                   },{
                                		id:'fremark', 
                                		header: '备注',
                                		width: 240, 
                                		dataIndex:'fremark'
                                   }
                                ]);
/*团体加载器*/
var jsPanelLoader = new Ext.tree.TreeLoader({
	dataUrl:getPath() + 'character/findAll'
});
/*团体根节点*/
var jsPanelNode = new Ext.tree.AsyncTreeNode({
	draggable:false,
	id : "source",
	text : "角色",
	draggable : false// 根节点不容许拖动

});
var jsPanel = new Ext.tree.TreePanel({
	height:320,
	title:'<b style="color:#15428b;margin-right:220px">勾选角色</b>',
	loader:jsPanelLoader,
	rootVisible : false,
	autoScroll : true,
	monitorResize: true,
	root:jsPanelNode
});
/**************************** 自定义元素结束 *******************************/
/**************************** 操作 **********************************/
//操作用户授权  显示界面
function yhsq(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		var row = Ext.getCmp('grid').getSelectionModel().getSelected();
		if(fids.length == 0){ //单个用户授权
			fids[0] = row.get('fid'); 
		}
		if(!yhsqWin){
				yhsqWin = new Ext.Window({
					id:'yhsqWin',
					width:300, 
					height:500,
					closeAction:'hide',
					maximizable:false,
					resizable:false,
					border:false,
					autoScroll : false,
					items:[mkPanel],
					listeners:{
						hide:function(){
							location.reload();
						}
					},
					buttons:[{
					    text: '保存',
					    handler:function(){
					    	gnTreeSave()
					     }
					}]
				});
		}
		mkPanel.loader.dataUrl = getPath() + 'organizationauthorized/getByBasicFunctionTree';
		if(fids.length>1){
			mkPanel.loader.baseParams  ={yhid:""};
			yhsqWin.setTitle('批量用户授权');
		}else{
			mkPanel.loader.baseParams  ={yhid:fids[0]};
			yhsqWin.setTitle('用户授权');
		}
		mkPanel.root.reload();
		Ext.getCmp('yhsqWin').show();
	}else{
		alertWarring("请选择需要进行用户授权的用户!");return;
	}
}

//操作角色授权  显示界面
function jssq(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	jsStore.load();
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		var row = Ext.getCmp('grid').getSelectionModel().getSelected();
		if(fids.length == 0){ //单个用户角色授权
			 fids[0]=row.get('fid');
		}
		if(!jssqWin){
			if(row.get('ftypeId')==1){//fids为空的时候 为单个职员用户授权
				jssqWin = new Ext.Window({
					width:700,
					height:372,
					id:'jssqWin',
					closeAction:'hide',
					maximizable:false,
					resizable:false,
					autoScroll:false,
					items:{
						layout				:'column',
						column			:3,
						border:false,//var ids = leftPanel.getChecked("id");
						items: [{columnWidth:.25,border:false,items:[orgPanel]},{columnWidth:.25,border:false,items:[sgPanel]},{columnWidth:.5,border:false,items:[jsPanel]}]
					},
					listeners:{
						hide:function(){
							location.reload();
						}
					},
					buttons:[{
						text: '&nbsp;保&nbsp;存&nbsp;',
						enableToggle: false,
						iconCls:'yunzo_save',
						handler: function(){
							jsTreeSave();
						}
					}]
				});
			}else{
	 		   jssqWin = new Ext.Window({
					width:350,
					height:352,
					id:'jssqWin',
					closeAction:'hide',
					maximizable:false,
					resizable:false,
					autoScroll:false,
					items:[jsPanel],
					listeners:{
						hide:function(){
							location.reload();
						}
					},
					buttons:[{
						text: '&nbsp;保&nbsp;存&nbsp;',
						enableToggle: false,
						iconCls:'yunzo_save',
						handler: function(){
							jsTreeSave();
						}
					}]
				});
	 	   	}
		}

	 	orgPanel.loader.baseParams  ={id:row.get('fid')};
	 	orgPanel.root.reload();
	 	userId = row.get('fid');
	 	sgPanel.root.reload();
	 	
		if(fids.length>1){
			jsPanel.loader.baseParams  ={id:""};
			jssqWin.setTitle('批量角色授权');
		}else{
			jsPanel.loader.baseParams  ={id:row.get('fid')};
			jssqWin.setTitle('角色授权');
		}
		jsPanel.root.reload();
		jssqWin.show();
	}else{
		alertWarring("请选择需要进行角色授权的用户!");return;
	}
}
//用户 保存用户授权功能点
 var gnTreeSave = function(){
	 if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
			var ids = mkPanel.getChecked("id");
	 		var myMask = new Ext.LoadMask(Ext.getBody(), {
					msg : '正在处理......请稍后！',
					removeMask : true // 完成后移除
				});
		 	myMask.show();
		if(fids.length==1){
			Ext.Ajax.request({
				url: getPath() +'organizationauthorized/save',
				method: 'POST',
				params:'ids='+ids+'&userId='+fids[0],
     			text: "saving...",    
     			success: function (result, request){
     					var data = Ext.util.JSON.decode(result.responseText);
			    		if(data.msg == '成功'){
          			   	 	Ext.Msg.alert("消息","保存成功！");
			    		}else if(data.msg == '失败'){
          			   	 	Ext.Msg.alert("消息","保存失败！");
			    		}
						myMask.hide();
			    		Ext.getCmp('yhsqWin').hide();
     			}
			});
		}else{
			Ext.Ajax.request({
				url: getPath() +'organizationauthorized/saves',
				method: 'POST',
				params:'ids='+ids+'&userIds='+fids,
     			text: "saving...",    
     			success: function (result, request){
     					var data = Ext.util.JSON.decode(result.responseText);
			    		if(data.msg == '成功'){
          			   	 	Ext.Msg.alert("消息","保存成功！");
			    		}else if(data.msg == '失败'){
          			   	 	Ext.Msg.alert("消息","保存失败！");
			    		}
						myMask.hide();
			    		Ext.getCmp('yhsqWin').hide();
     			}
			});
		}
}

//用户添加角色 角色授权 
var jsTreeSave = function(){

	 if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	 	var jsPanelids = jsPanel.getChecked("id");
		var orgids = orgPanel.getChecked("id");
		var groupids=sgPanel.getChecked("id");
 		var myMask = new Ext.LoadMask(Ext.getBody(), {
				msg : '正在处理......请稍后！',
				removeMask : true // 完成后移除
		});
		myMask.show();
		if(fids.length==1){
			Ext.Ajax.request({
				url:getPath() +'roleauthorization/savefunctionRoleauthorization',
				method: 'POST',
				params:'jsids='+jsPanelids+'&userId='+fids[0]+'&orgids='+orgids+'&groupids='+groupids,
     			text: "saving...",  
     			timeout : 10*60000,  
     			success: function (result, request){
			    		
			    		var data = Ext.util.JSON.decode(result.responseText);
			    		if(data.msg == '成功'){
	      			   	 	Ext.Msg.alert("消息","保存成功！");
			    		}else if(data.msg == '失败'){
	      			   	 	Ext.Msg.alert("消息","保存失败！");
			    		}

						myMask.hide();
			    		Ext.getCmp('jssqWin').hide();
     			},
                failure : function(form, action) {
                    alertWarring("保存失败！");
                }
			});
		}else{//批量角色授权
			Ext.Ajax.request({
				url:getPath() +'roleauthorization/saveRoleauthorizations',
				method: 'POST',
				params:'jsids='+jsPanelids+'&userIds='+fids+'&orgids=null&groupids=null',
     			text: "saving...", 
     			timeout : 10*60000,   
     			success: function (result, request){
			    		
			    		var data = Ext.util.JSON.decode(result.responseText);
			    		if(data.msg == '成功'){
	      			   	 	Ext.Msg.alert("消息","保存成功！");
			    		}else if(data.msg == '失败'){
	      			   	 	Ext.Msg.alert("消息","保存失败！");
			    		}

						myMask.hide();
			    		Ext.getCmp('jssqWin').hide();
     			},
                failure : function(form, action) {
                    alertWarring("保存失败！");
                }
			});
		}
 }
function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var searchCondition = Ext.getCmp('searchCondition').getValue();
	var type = Ext.getCmp('type').getValue();
	//模糊查询
	store.load({params:{searchName:searchCondition,type:type,start:0,limit:LIMIT}});
}


//批量用户授权
function moreYhsq(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
			  if(records[i].get('ftypeId') == 1){
				  alertWarring("团体类型用户才能批量授权!");
				  fids=[];
				  return;
			  }
		  }
		  yhsq();
	}else{
		alertWarring("请选择用户!");return;
	}
}
//批量角色授权
function moreJssq(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
			  if(records[i].get('ftypeId') == 1){
				  alertWarring("团体类型用户才能批量授权!");
				  fids = [];
				  return;
			  }
		  }
		  jssq();
	}else{
		alertWarring("请选择用户!");return;
	}
}