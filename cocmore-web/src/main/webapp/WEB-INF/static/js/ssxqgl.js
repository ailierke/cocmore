var LIMIT = 150;var jsonStr=[];var detailWin;var timeInterval;var type=2;var wallByManagerWin;var commentByManagerWin;var excel;var sendWin;

//上传图片类型  
var img_reg = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/;

//create the data store
var records = new Ext.data.Record.create([  
                                          {name: 'fid'},
                                          {name: 'fnumber'},
                                          {name: 'fheadline'},
                                          {name: 'fimages'},
                                          {name: 'fmessage'},
                                          {name: 'fcontacts'},
                                          {name: 'ftel'},
                                          {name: 'ybasicSocialgroups'},
                                          {name: 'ybasicMember'},
                                          {name: 'ybasicCity'},
                                          {name: 'ybasicTrade'},
                                          {name: 'ybasicCounty'},
                                          {name: 'ybasicProvince'},
                                          {name: 'fcomment'},
                                          {name: 'fbillState'},
                                          {name: 'fstartTime'},
                                          {name: 'ffinishTime'},
                                          {name: 'fpublisherTime'},
                                          {name: 'fisHide'},
                                          {name: 'flevel'},
                                          {name: 'flag'}
                                         ]);
var store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
		root:'list',
		totalProperty:'count'
	},records),
	proxy:new Ext.data.HttpProxy({
			disableCaching :false,
			url: getPath() + 'demand/findDemand'
	}),
	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});
var tool = new Ext.Toolbar({ //工具栏
	items:[
		    {text: '查看详细',enableToggle: false,iconCls:'common_details',handler:function(){editData("detail");}},
		    '-',{text: '启动刷新',enableToggle: false,iconCls:'common_flag',handler:function(){yunzo_start();}},
			'-',{text: '关闭刷新',enableToggle: false,iconCls:'common_nopast',handler:function(){yunzo_stop();}},
			'-',{text: '按评论排序',enableToggle: false,iconCls:'common_top',handler:function(){
				type=1;
				Ext.getCmp("grid").store.reload({params:{start:0,limit:LIMIT,type:type}});
			}},
			'-',{text: '按点赞排序',enableToggle: false,iconCls:'common_top',handler:function(){
				type=0;
				Ext.getCmp("grid").store.reload({params:{start:0,limit:LIMIT,type:type}});
			}},
			'-',{
				text:'人工点赞',enableToggle: false,iconCls:'regedit',handler:function(){
					wallByManager();
				}
			},
			'-',{
				text:'人工点评',enableToggle: false,iconCls:'common_edit',handler:function(){
					commentByManager();
				}
			},
			'-',{
				text: '推送消息',enableToggle: false,iconCls:'common_submit',handler:function(){
					sendData();
				}
			},
			'-',{text: '&nbsp;模板下载&nbsp;',enableToggle: false,iconCls:'excel',handler: function(){mbsz();}},
			'-',{text: '&nbsp;导入&nbsp;',enableToggle: false,iconCls:'icon_excel',handler: function(){showImportExcel();}}]
});
Ext.onReady(function(){
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    
    
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
    	{id:'flag',hidden:true},
    	{
    		id:'fnumber', 
    		header: '编号', 
    		width: 150, 
    		hidden:true,
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
    		header: '需求信息', 
    		width: 150,
    		hidden:true,
    		dataIndex:'fmessage'
       },{
        	id: 'fcontacts',
        	dataIndex:'fcontacts',
    		width: 150, 
        	header:'联系人'
		},{
        	id: 'ftel', 
        	dataIndex: 'ftel', 
    		width: 150, 
        	header:'联系电话'
		},{
        	id: 'fstartTime',
        	dataIndex:'fstartTime',
        	header:'开始时间',
        	width: 150, 
    		hidden:true,
    		format:"Y-m-d H:i:s"
		},{
        	id: 'ffinishTime', 
        	dataIndex: 'ffinishTime', 
        	header:'结束时间',
    		hidden:true,
        	width: 150, 
    		format:"Y-m-d H:i:s"
		},{
        	id: 'fpublisherTime', 
        	dataIndex: 'fpublisherTime', 
        	header:'发布时间',
    		hidden:true,
        	width: 150, 
    		format:"Y-m-d H:i:s"
		},{
        	id: 'ybasicMember', 
        	dataIndex: 'ybasicMember', 
        	header:'发布人',
        	hidden:true,
        	renderer:function(value){
				if(value){
					return value.fname;
				}else{
					return "";
				}
        	}
		},{
        	id: 'ybasicTrade', 
        	dataIndex: 'ybasicTrade', 
        	header:'行业',
    		hidden:true,
        	renderer:function(value){
				if(value){
					return value.fname;
				}else{
					return "";
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
				}else{
					return "";
				}
        	}
		},{
        	id: 'ybasicProvince', 
        	dataIndex: 'ybasicProvince', 
        	header:'省份',
    		hidden:true,
        	renderer:function(value){
				if(value){
					return value.fname;
				}else{
					return "";
				}
        	}
		},{
        	id: 'ybasicCity', 
        	dataIndex: 'ybasicCity', 
        	header:'城市',
    		hidden:true,
        	renderer:function(value){
				if(value){
					return value.fname;
				}else{
					return "";
				}
        	}
		},{
        	id: 'ybasicCounty', 
        	dataIndex: 'ybasicCounty', 
        	header:'区县',
    		hidden:true,
        	renderer:function(value){
				if(value){
					return value.fname;
				}else{
					return "";
				}
        	}
		},{
			header:'级别',
        	id: 'flevel',
        	dataIndex:'flevel',
        	hidden:true
		},{
			id:'fisHide', 
			header: '是否隐藏', 
			width: 80, 
    		hidden:true,
			dataIndex:'fisHide',
			renderer:function(value){
				if(value==0){
					return '否';
				}else
					return '是';
			}
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
    	new Ext.Viewport({
        	renderTo:'mainDiv',
        	id:'viewport',
        	layout:'border',
        	items:[grid]
        });
    Ext.EventManager.onWindowResize(function(){ 
    	grid.getView().refresh() 
	}) 
	grid.on('dblclick',function(){ editData("detail")});
});
/**************************** 自定义元素 **********************************/
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

//行业
var tradeStore = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'post',
		url : getPath() + '/trade/findAllTrade'
	}),
	reader : new Ext.data.JsonReader({root:'obj.list'}, [{name : 'fid'}, {name : 'fname'}])
});
tradeStore.load();

//省份
var provinceStore = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'post',
		url : getPath() + '/province/findAllProvince'
	}),
	reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
});
provinceStore.load();

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

function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	//通过id获取输入的数据
	var searchCondition = Ext.getCmp("searchCondition").getValue();
	//模糊查询
	store.load({params:{headline:searchCondition,groupId:window.parent.groupId,start:0,limit:LIMIT}});
}

function editData(row){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	
		row = Ext.getCmp("grid").getSelectionModel().getSelected();
		if(!row){
	    	 alertWarring("请选择需要查看详情的需求信息!");
	    }else{
	 		showDetail(row);
	    }
		return;
}

//作废状态填写原因
function showDiscarded(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var selectRow = Ext.getCmp("grid").getSelectionModel().getSelected();
	if(selectRow){
		var fid= selectRow.get("fid");
		Ext.MessageBox.prompt("提示", "请输入将这条信息作废的原因！",function(bu,txt){
			if(txt && 0<txt.length<125){
				Ext.Ajax.request({
					url : getPath()+'supply/Invalid',
					params : {fid : fid ,fbillState : 9,comment:txt},
					
					success: function (result, request) 
					{
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

//=================================删除====================================
function deleteDemand(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  var fids=new Array();
		  for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
		  }
		 Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认要删除当前需求信息？!",
		    width:300,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			demandAjax(fids);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}
	else
	{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择需要删除的需求信息!",
			width: 250,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}

function demandAjax(param)
{
	Ext.Ajax.request({
		url : getPath() + '/demand/deleteDemand',
		params : {fids : param},
		
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


//图片上传成功 给产品图片文本，预览img赋值
function photoSrc(src,index){
	Ext.getCmp('editForm').add(
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
	Ext.getCmp('editForm').doLayout();
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
		html:'<div class="xx_center hide"><div class=" xx_img scroll relative"><div class="scroll_box" id="scroll_img"><ul class="scroll_wrap"></ul></div><ul class="scroll_position scroll_position_bg hide" id="scroll_position">'+
		    '<li class="on"><a href="javascript:void(0);">1</a></li><li><a href="javascript:void(0);">2</a></li><li><a href="javascript:void(0);">3</a></li></ul></div><h1 class="xx_title"></h1>'+
			'<p class="xx_contect"><label></label></p><ul class="xx_detail"></ul><p class="publisher"> <img src=""/></p><div class="xx_desc"></div></div><div class="xx_loader"></div>'
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
	//发布人信息相关
	var detail = "";
	if(row.get('ybasicTrade')){
		  detail+='<li>行业&nbsp;'+row.get('ybasicTrade').fname+'</li>';
	}
	if(row.get('ybasicProvince')){
		  detail+='<li>地域&nbsp;'+row.get('ybasicProvince').fname;
		  if(row.get('ybasicCity')){
			  detail += '&nbsp;'+row.get('ybasicCity').fname;
		  }
		  if(row.get('ybasicCounty')){
			  detail += '&nbsp;'+row.get('ybasicCounty').fname;
		  }
		  detail+='</li>';
	}
	if(row.get('fauditTime')){
		  detail+='<li>时间&nbsp;'+(row.get('fstartTime').length>11?row.get('fstartTime').substr(0,10):row.get('fstartTime'));
		  if(row.get('fexpireTime'))
			  detail += '&nbsp;至&nbsp;'+(row.get('ffinishTime').length>11?row.get('ffinishTime').substr(0,10):row.get('ffinishTime'));
		  detail+='</li>';
	}
	//发布人
	if(row.get('ybasicMember')){
		 var publisher = '';
		 if(row.get('ybasicMember').fheadImage){
			publisher+='<img src="'+row.get('ybasicMember').fheadImage+'"/>';
		 }
		 publisher += '发布人&nbsp;&nbsp;'+row.get('ybasicMember').fname;
		 $(".publisher").html(publisher);
	}
	  
	$('.xx_detail').html(detail);
	$('.xx_desc').html('<h2>需求简介</h2><p>'+row.get('fmessage')+'</p>');

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

/***********************************推送生活信息给会员*******************************************/

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
	loader : treeLoaderHY,
	border:false,
	rootVisible : false,
	autoScroll : true,
	monitorResize: true,
	mode:'local',
	root:rootNodeHY,
	listeners:{
		beforeload:function(node) {//加载前
			if(node.attributes.level=='1'){
				treeLoaderHY.dataUrl = getPath() + 'life/createTreechild?id='+node.attributes.id; // 定义每个节点的Loader	加载团体
			}else if(node.attributes.level=='2'){
				treeLoaderHY.dataUrl = getPath() + 'life/createTrees?id='+node.attributes.id; // 定义每个节点的Loader	加载会员
			}else{
				treeLoaderHY.dataUrl = getPath() + 'life/getOrgTree'; // 定义每个节点的Loader	加载组织
			}
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
/*会员加载器*/
var treeLoaderHY1 = new Ext.tree.TreeLoader({});
/*会员根节点*/
var rootNodeHY1 = new Ext.tree.AsyncTreeNode({
	draggable:false,
	id : "source",
	text : "会员",
	draggable : false// 根节点不容许拖动
});
var treePanelHY1 =  new Ext.tree.TreePanel({
	title:'选择会员',
	loader : treeLoaderHY1,
	border:false,
	rootVisible : false,
	height:330,
	autoScroll : true,
	monitorResize: true,
	mode:'local',
	root:rootNodeHY1,
	listeners:{
		beforeload:function(node) {//加载前
			if(node.attributes.level=='1'){
				treeLoaderHY1.dataUrl = getPath() + 'life/createTreechild?id='+node.attributes.id; // 定义每个节点的Loader	加载团体
			}else if(node.attributes.level=='2'){
				treeLoaderHY1.dataUrl = getPath() + 'life/createTrees?id='+node.attributes.id; // 定义每个节点的Loader	加载会员
			}else{
				treeLoaderHY1.dataUrl = getPath() + 'life/getOrgTree'; // 定义每个节点的Loader	加载组织
			}
		}
	}
});
treePanelHY1.on('checkchange', function(node, checked) {   
	node.expand();   
	node.attributes.checked = checked;   
	node.eachChild(function(child) {   
		child.ui.toggleCheck(checked);   
		child.attributes.checked = checked;   
		child.fireEvent('checkchange', child, checked);   
	});   
}, treePanelHY1); 
//人工点赞
function wallByManager(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var fids=new Array();
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
		  }
	}else{
		alertWarring("请选择需要点赞的信息!");return;
	}
	if(!wallByManagerWin){
	    	 wallByManagerWin = new Ext.Window({
		        id: 'wallByManagerWin',
		        title:'人工点赞',
		        layout:'fit',
	            width:400,
	            height:580,
	            frame:true,
	            plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
				autoScroll:false,
			    items :[treePanelHY],
			    buttons:[{
				    text: '保存',
				    handler:function(){
				    	var ids = treePanelHY.getChecked();
				    	var funEntryIds=[];
				    	if(ids && ids.length>0){
			    			 for(var i=0;i<ids.length;i++){
			    				funEntryIds.push(ids[i].attributes.id);
			    			 }
			    		}else{
			    			alertWarring("你还未选择会员！");return;
			    		}
				 		var myMask = new Ext.LoadMask(Ext.getBody(), {
								msg : '正在处理......请稍后！',
								removeMask : true // 完成后移除
							});
					 	myMask.show();
					 	Ext.Ajax.request({
							url: getPath() +'supply/savePotlike',
							method: 'POST',
							params:'memberids='+funEntryIds+'&fmanShowIds='+fids+'&type=1',
			     			text: "saving...",    
			     			success: function (result, request){
			     					var data = Ext.util.JSON.decode(result.responseText);
			          			   	Ext.Msg.alert("消息",data.msg);
			          			   	
									myMask.hide();
						    		Ext.getCmp('wallByManagerWin').hide();
			     			}
						});
				    }
			    },{
			    	text:'取消',
				    handler:function(){
					 	 Ext.getCmp('wallByManagerWin').hide();
				    }
			    }]
	        });
		 }
	     wallByManagerWin.show();
}
/****************************人工点评相关************************/
var sendForm = new Ext.form.FormPanel({
    height: 380,
    margin:'0 0 5 0',
    border: false,
    labelAlign: 'left',
    buttonAlign: 'center',
    viewConfig: {forceFit:true},
    monitorResize: true,
    frame: true,
    labelWidth: 80,
    defaultType: 'textfield',defaults: {anchor: '95%'},
    items: [{fieldLabel: '内容',xtype:'textarea',id:'fcontent',name:'fcontent',height:300}]
});
//人工评论
function commentByManager(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var fids=new Array();
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
		  }
	}else{
		alertWarring("请选择需要点评的信息!");return;
	}
	if(!commentByManagerWin){
	    	 commentByManagerWin = new Ext.Window({
		        id: 'commentByManagerWin',
		        title:'手动评论',
		        layout:'fit',
	            width:600,
	            height:380,
	            frame:true,
	            plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
				autoScroll:false,
			    items :[{layout:'column',border:false,items:[{columnWidth:.4,border:false,items:[treePanelHY1]},{columnWidth:.6,border:false,items:[sendForm]}]}],
			    buttons:[{
				    text: '保存',
				    handler:function(){
				    	var ids = treePanelHY1.getChecked();
				    	var funEntryIds=[];
				    	if(ids && ids.length>0){
			    			 for(var i=0;i<ids.length;i++){
			    				funEntryIds.push(ids[i].attributes.id);
			    			 }
			    		}else{
			    			alertWarring("你还未选择会员！");return;
			    		}
				    	var fcontent = Ext.getCmp('fcontent').getValue();
				    	if(!fcontent || fcontent.length<1){
				    		 alertWarring("请先填写评论内容！");return;
				    	}
				 		var myMask = new Ext.LoadMask(Ext.getBody(), {
								msg : '正在处理......请稍后！',
								removeMask : true // 完成后移除
							});
					 	myMask.show();
						Ext.Ajax.request({
							url: getPath() +'supply/saveComment',
							method: 'POST',
							params:'memberIds='+funEntryIds+'&fforeignIds='+fids+'&fcontents='+fcontent+'&type=1',
			     			text: "saving...",    
			     			success: function (result, request){
			     					var data = Ext.util.JSON.decode(result.responseText);
			          			   	Ext.Msg.alert("消息",data.msg);
			          			   	
									myMask.hide();
						    		Ext.getCmp('commentByManagerWin').hide();
			     			}
						});
				    }
			    },{
			    	text:'取消',
				    handler:function(){
				    	Ext.getCmp('fcontent').setValue("");
					 	Ext.getCmp('commentByManagerWin').hide();
				    }
			    }]
	        });
		 }
	     commentByManagerWin.show();
}

//导入
/*导入数据功能*/
function showImportExcel(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
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
	defaults:{anchor:'90%'},
	buttonAlign : 'center',
	id : 'uploadFileLogo',
	items : [{  id:'importGroupId',
				hiddenName:'groupId',
				xtype : 'combo',
				fieldLabel : '团体',
				triggerAction : 'all',
				lazyInit : true,
				mode : 'local',
				store: new Ext.data.Store({
					autoLoad:true,
					proxy : new Ext.data.HttpProxy({ 
						disableCaching :false,
						method:'post',
						url : getPath() + 'groups/findAllGroupPagingList1'
					}),
					reader : new Ext.data.JsonReader({root:'list'}, [{name : 'fid'}, {name : 'fname'}])
				}),
				valueField : 'fid',
				displayField : 'fname',
				emptyText : '请选择团体',
				allowBlank: false
		},
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
    		if(Ext.getCmp('uploadFileLogo').getForm().isValid() && /\.(xls)$/.test(logourl)){
    			Ext.getCmp('uploadFileLogo').getForm().submit({
		                   method:'POST',
		                   waitMsg:'正在提交.....',
						   waitTitle:'请稍等',
						   url : getPath() + '/member/import',
		                   success:function(form, action){
		                	   var reloadUrl = window.location.href;
		                	   Ext.getCmp("showImportExcel").hide();
		                	   Ext.MessageBox.confirm('提示', '是否查看导入详情?', function(btn){
									if(btn=='yes'){
										var export_url = getPath() + 'member/downloadImportStatus';
									    window.location.href = export_url;
									}
								});
							    store.reload();
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

//excel模板设置
function mbsz(){
	var export_url = getPath() + '/member/download';
    window.location.href = export_url;
}
/***********************************推送需求信息给会员*******************************************/

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
	loader : treeLoaderHY,
	border:false,
	rootVisible : false,
	autoScroll : true,
	monitorResize: true,
	mode:'local',
	root:rootNodeHY,
	listeners:{
		beforeload:function(node) {//加载前
			if(node.attributes.level=='1'){
				treeLoaderHY.dataUrl = getPath() + 'life/createTreechild?id='+node.attributes.id; // 定义每个节点的Loader	加载团体
			}else if(node.attributes.level=='2'){
				treeLoaderHY.dataUrl = getPath() + 'life/createTrees?id='+node.attributes.id; // 定义每个节点的Loader	加载会员
			}else{
				treeLoaderHY.dataUrl = getPath() + 'life/getOrgTree'; // 定义每个节点的Loader	加载组织
			}
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

function sendData(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var row = Ext.getCmp("grid").getSelectionModel().getSelected();
	if(!row){
    	 alertWarring("请选择需要推送的生活项目信息!");
    }else{
	     if(!sendWin){
	    	 sendWin = new Ext.Window({
		        id: 'sendWin',
		        title:'推送消息',
		        layout:'fit',
	            width:400,
	            height:580,
	            frame:true,
	            plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
				autoScroll:false,
			    items :[treePanelHY],
			    buttons:[{
				    text: '发送',
				    handler:function(){
				    	var ids = treePanelHY.getChecked();
				    	var funEntryIds=[];
				    	if(ids && ids.length){
			    			 for(var i=0;i<ids.length;i++){
			    				funEntryIds.push(ids[i].attributes.id);
			    			 }
			    		}
				    	var lifeIds = new Array();
				    	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
							  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
							  for(var i=0;i<records.length;i++){
								  lifeIds[i]=records[i].get('fid');
							  }
				    	}else{
				    		Ext.Msg.alert("消息","未选中生活项目！");
				    		return;
				    	}
				 		var myMask = new Ext.LoadMask(Ext.getBody(), {
								msg : '正在处理......请稍后！',
								removeMask : true // 完成后移除
							});
					 	myMask.show();
						Ext.Ajax.request({
							url: getPath() +'demand/pushDemand',
							method: 'POST',
							params:'memberIds='+funEntryIds+'&demandIds='+lifeIds,
			     			text: "saving...",    
			     			success: function (result, request){
			     					var data = Ext.util.JSON.decode(result.responseText);
			          			   	Ext.Msg.alert("消息",data.msg);
									myMask.hide();
						    		Ext.getCmp('sendWin').hide();
			     			}
						});
				    }
			    },{
			    	text:'取消',
				    handler:function(){
					 	 Ext.getCmp('sendWin').hide();
				    }
			    }]
	        });
		 }
	     sendWin.show();
     }
}

