var LIMIT = 26;var editWin;var sendWin;var jsonStr=[];var typeId;var excel;
var records = new Ext.data.Record.create([  
                                          {name: 'fid'},
                                          {name: 'ybasicMember'},
                                          {name: 'fmobilePhone'},
                                          {name: 'fsendTime'},
                                          {name: 'fcontent'},
                                          {name: 'fbillState'},
                                          {name: 'ybasicSocialgroups'}
                                       ]
                                   );

var store = new Ext.data.Store({
   	reader:new Ext.data.JsonReader({
   		root:'list',
   		totalProperty:'count'
   	},records),
   	proxy:new Ext.data.HttpProxy({
   			disableCaching :false,
			url: getPath() + 'sms/doNotNeedSessionAndSecurity_findSmsByGroupId'
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
	items:['会员名：',{
		xtype:'textfield',
		emptyText:'请输入会员名',
		id:'searchCondition',
		width:240
	},'-']
});

Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 22
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

    

  //读入数据之前的事件,store不需要在写baseParams,因为会覆盖掉. (每次调用都载入此函数,'load'则第一次不调用外,其余都调用).
//    store.on('beforeload', function() {    
//    	  this.baseParams = {
//    			  classifyId:typeId,start:0,limit:LIMIT
//    	  };
//    });
    
    store.on('load', function(store, records, options) {
        if (records.length = 0) {
            Ext.Msg.alert("提示信息", "没有符合条件的数据!"); 
        }
    });
    store.load({params:{start:0,limit:LIMIT,groupId:window.parent.groupId}});
    
    var cm = new Ext.grid.ColumnModel([
                                   	new Ext.grid.RowNumberer(),{id:'fid',hidden:true},
                                   	  {
	                                   		id:'ybasicMember', 
	                                   		header: '会员名', 
	                                   		width: 150, 
	                                   		sortable: true, 
	                                   		dataIndex:'ybasicMember',
	                                   		renderer:function(value){
	                                   			return value.fname
	                                   		}
                                      },{
                                    	  id:'ybasicSocialgroups',
                                    	  header:'团体名',
                                    	  width:150,
                                    	  sortable:true,
                                    	  dataIndex:'ybasicSocialgroups',
                                    	  renderer:function(value){
                                    		  return value.fname
                                    	  }
                                      },{
	                                   		id:'fmobilePhone', 
	                                   		header: '手机号', 
	                                   		width: 100, 
	                                   		sortable: true, 
	                                   		dataIndex:'fmobilePhone',
	                                   		renderer:function(value){
	                                			return decryptByABC(value);
	                                		}
                                      },{
	                                   		id:'fcontent', 
	                                   		header: '短信内容', 
	                                   		width: 200, 
	                                   		sortable: true, 
	                                   		dataIndex:'fcontent'
                                      },{
	                                   		id:'fsendTime', 
	                                   		header: '发送时间', 
	                                   		dataIndex:'fsendTime', 
	                                   		width: 150
                                      },{
                                    	    id:'fbillState', 
	                                   		header: '状态', 
	                                   		dataIndex:'fbillState', 
	                                   		renderer:function(value){
	                                   			if(value==18){
	                                   				return '<span style="color:blue">待发送</span>';
	                                   			}else if(value==19){
	                                   				return '发送成功';
	                                   			}else if(value==20){
	                                   				return '<span style="color:blue">发送失败</span>';
	                                   			}
	                                   		},
	                                   		width: 150
                                      }
                                   ]);
    var pagingToolBar = new Ext.PagingToolbar({
    	pageSize:LIMIT,
    	store:store,
    	displayInfo: true,
    	displayMsg: '当前显示第&nbsp;{0}&nbsp;到&nbsp;{1}&nbsp;条,共&nbsp;{2}&nbsp;条',
    	emptyMsg: "没有记录"
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
    	grid.getView().refresh() 
	}) 
	grid.on('dblclick', editData);
});

/********************************自定义对象**************************************/
/**************************** 产品评论信息 **********************************/
var ordersForm = new Ext.form.FormPanel({
    height: 227,
    margin:'0 0 5 0',
    border: false,
    labelAlign: 'left',
    buttonAlign: 'center',
    viewConfig: {forceFit:true},
    monitorResize: true,
    frame: true,
    labelWidth: 80,
    defaultType: 'textfield',defaults: {anchor: '95%'},
    items: [{xtype:'hidden',id:'fidCK',name:'data.fid'},
            	{fieldLabel: '会员名',id:'YBasicMemberCK',name:'YBasicMemberCK',readOnly:true},
                {fieldLabel: '手机号',id:'fmobilePhoneCK',name:'fmobilePhoneCK',readOnly:true},
                {fieldLabel: '内容',xtype:'textarea',id:'fcontentCK',name:'fcontent',height:200},
                {fieldLabel: '发送时间',id:'fsendTimeCK',name:'fsendTime',readOnly:true}]
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
	title:'选择会员',
	width:185,
	height:210,
	loader : treeLoaderHY,
	border:false,
	rootVisible : false,
	autoScroll : true,
	monitorResize: true,
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
var sendForm = new Ext.form.FormPanel({
    height: 227,
    margin:'0 0 5 0',
    border: false,
    labelAlign: 'left',
    buttonAlign: 'center',
    viewConfig: {forceFit:true},
    monitorResize: true,
    frame: true,
    labelWidth: 80,
    defaultType: 'textfield',defaults: {anchor: '95%'},
    items: [
                {fieldLabel: '内容',xtype:'textarea',id:'fcontent',name:'fcontent',height:200,maxLength:400},
                {fieldLabel: '发送时间',id:'fsendTime',name:'fsendTime',xtype:'hidden'}]
});

function sendData(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	     if(!sendWin){
	    	 sendWin = new Ext.Window({
		        id: 'sendWin',
		        title:'发送短信',
		        layout:'fit',
	            width:600,
	            height:280,
	            frame:true,
	            plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
				autoScroll:false,
			    items :[{layout:'column',border:false,items:[{columnWidth:.3,border:false,items:[treePanelHY]},{columnWidth:.7,border:false,items:[sendForm]}]}],
			    buttons:[{
				    text: '发送',
				    handler:function(){
				    	var date = new Date();  // 得到系统日期
				    	var dateString = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
				    	Ext.getCmp('fsendTime').setValue(dateString);
				    	var ids = treePanelHY.getChecked();
				    	var funEntryIds=[];
				    	if(ids && ids.length){
			    			  for(var i=0;i<ids.length;i++){
			    				 // alert(nodes[i].attributes.id);
			    				  //判断是否为子节点，不需要获取父级节点的ID
			    				  if(ids[i].childNodes.length == 0){
			    					  funEntryIds.push(ids[i].attributes.id);
			    				  }
			    			  }
			    		}

				 		var myMask = new Ext.LoadMask(Ext.getBody(), {
								msg : '正在处理......请稍后！',
								removeMask : true // 完成后移除
							});
					 	myMask.show();
						Ext.Ajax.request({
							url: getPath() +'sms/send',
							method: 'POST',
							params:'fids='+funEntryIds+'&msgText='+Ext.getCmp("fcontent").getValue()+'&dateString='+dateString,
			     			text: "saving...",    
			     			success: function (result, request){
			     					var data = Ext.util.JSON.decode(result.responseText);
						    		if(data.msg == '成功'){
			          			   	 	Ext.Msg.alert("消息","保存成功！");
						    		}else if(data.msg == '失败'){
			          			   	 	Ext.Msg.alert("消息","保存失败！");
						    		}
									myMask.hide();
						    		Ext.getCmp('sendWin').hide();
			     			}
						});
				    }
			    }],
			    listeners:{
			    	hide:function() {
			    		sendForm.form.reset();
					}
			    }
	        });
	 }
	 treePanelHY.root.reload()
     sendWin.show();
}
function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	//通过id获取输入的数据
	var searchCondition = Ext.getCmp("searchCondition").getValue();
	//模糊查询
	store.load({params:{headline:searchCondition,groupId:window.parent.groupId,start:0,limit:LIMIT}});
}

function editData(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	var row = Ext.getCmp('grid').getSelectionModel().getSelected();
	if(row){
		this.row = row;
	     if(!editWin){
	        editWin = new Ext.Window({
		        id: 'editWin',
		        title:'短信详情',
		        layout:'fit',
	            width:400,
	            height:330,
	            frame:true,
	            plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
				autoScroll:false,
			    items :[ordersForm],
			    listeners:{
			    	hide:function() {
						ordersForm.form.reset();
					}
			    }
	        });
		}
	     editWin.show();
	    if(row != 'add') {
	    	loadData(row);
	    }
	}else{
		alertWarring("请选中短信记录列!");
	}
}
//读取短信详细信息
function loadData(row) {
		ordersForm.form.setValues({
			fidCK											: row.get('fid'),
			fmobilePhoneCK  						: decryptByABC(row.get('fmobilePhone')),
			YBasicMemberCK						: row.get('ybasicMember').fname,
			ybasicSocialgroups                             : row.get('ybasicSocialgroups').fname,
			fcontentCK									: row.get('fcontent'),
			fsendTimeCK								: row.get('fsendTime')
		});
		editWin.show();
}
