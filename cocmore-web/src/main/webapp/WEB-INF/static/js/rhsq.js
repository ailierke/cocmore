var LIMIT = 26;var editWin;

var records = new Ext.data.Record.create([
                                           {name: 'fid'},
								           {name: 'fname'},
								           {name: 'fphone'},
								           {name: 'fcompanyName'},
								           {name: 'fcompanyPosition'},
								           {name: 'fnativePlace'},
								           {name: 'fstate'}
								        ]
								    );
var store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
		root:'list',
		totalProperty:'count'
	},records),
	proxy:new Ext.data.HttpProxy({
			disableCaching :false,
			url: getPath() + '/apply/findApplyPage'
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
	items:['用户名：',{
		xtype:'textfield',
		emptyText:'请输入用户名',
		id:'searchCondition',
		width:150
	},'-']
});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 24
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
    store.load({params:{start:0,limit:LIMIT,groupId:window.parent.groupId}});
    var cm = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer(),
    	{id:'fid',hidden:true},
    	{
    		id:'fname', 
    		header: '姓名', 
    		width: 240, 
    		sortable: true, 
    		dataIndex:'fname'
           },{
    		id:'fphone', 
    		header: '联系电话', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fphone',
    		renderer:function(value){
    			return decryptByABC(value);
    		}
           },{
    		id:'fcompanyName', 
    		header: '公司名称', 
    		width: 150, 
    		sortable: true, 
    		allowBlank: true,
    		dataIndex:'fcompanyName'
           },{
    		id:'fcompanyPosition', 
    		header: '职位', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fcompanyPosition'
           },{
        	id:'fnativePlace', 
       		header: '籍贯', 
       		width: 150, 
       		sortable: true, 
       		dataIndex:'fnativePlace'   
           },{
    		id:'fstate', 
    		header: '状态', 
    		width: 150, 
    		sortable: false, 
    		renderer : status,
    		dataIndex:'fstate'
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
    	}else if(data == 16){
    		return '通过';
    	}else if(data == 17){
    		return '未通过';
    	}else if(data == 15){
    		return '待审核';
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
    if(getUser()[2]==1){
    	new Ext.Viewport({
        	renderTo:'mainDiv',
        	id:'viewport',
        	layout:'border',
        	items:[treePanelTuanti,grid]
        });
    }else{
    	new Ext.Viewport({
        	renderTo:'mainDiv',
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

var editRzxxForm = new Ext.form.FormPanel({
     border : false,
	 frame	: true,
	 labelAlign: 'left',
	 buttonAlign: 'center',
	 labelWidth:100,
	 autoScroll:false,
	 containerScroll: true,
	 width: 480,
     height: 380,
     defaults:{anchor:'92%',xtype:'textfield'},
	 items:[{
	        fieldLabel:'姓名',
	        id:'fname',
	        name:'fname',
	        readOnly:true
	 },{
	        fieldLabel:'联系电话',
	        id:'fphone',
	        name:'fphone',
	        readOnly:true
	 },{
	        fieldLabel:'公司名称',
	        id:'fcompanyName',
	        name:'fcompanyName',
	        readOnly:true,
            allowBlank: true
	 },{
	        fieldLabel:'职位',
	        id:'fcompanyPosition',
	        name:'fcompanyPosition',
	        readOnly:true,
           allowBlank: true
	 },{
	        fieldLabel:'籍贯',
	        id:'fnativePlace',
	        name:'fnativePlace',
	        readOnly:true,
           allowBlank: true
	 },{
	        fieldLabel:'状态',
	        id:'fstate',
	        name:'fstate',
	        readOnly:true,
            allowBlank: true,
            hidden:true
	 }]
});
/**************************** 自定义元素结束 *******************************/
/**************************** 操作 **********************************/
function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	var searchCondition = Ext.getCmp('searchCondition').getValue();
	//模糊查询
	store.load({params:{name:searchCondition,start:0,limit:LIMIT}});
}
function editData(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	var row = Ext.getCmp('grid').getSelectionModel().getSelected();
	if(row){
		this.row = row;
    	var that = this;
         if(!editWin){
            editWin = new Ext.Window({
		        id: 'editWin',
		        layout:'fit',
                width:400,
                height:200,
                frame:true,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
			    items :[editRzxxForm],
			    listeners:{hide:function(){editRzxxForm.form.reset();}}
            });
		}
        setTitleAndUrl(row, editWin);
        loadData(row);
	}
	else{
		alertWarring("请选中入会申请列!");
	}
}

function loadData(row) {
	editRzxxForm.form.setValues({
		fname  								: row.get('fname'),
		fphone     							: decryptByABC(row.get('fphone')),
		fcompanyName  						: row.get('fcompanyName'),
		fcompanyPosition  					: row.get('fcompanyPosition'),
		fnativePlace  						: row.get('fnativePlace'),
		fstate  							: row.get('fstate')
	});
}
function setTitleAndUrl(row, win) {
	win.setTitle('入会申请详情');
	win.show();
}

//==============================修改状态================================
function setState(stateId){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  var fids=new Array();
		  var companyName = new Array();
		  var position = new Array();
		  for(var i=0;i<records.length;i++){
			  fids[i] = records[i].get('fid');
			  companyName[i] = records[i].get('fcompanyName');
			  position[i] = records[i].get('fcompanyPosition');
		  }
		Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认要更新当前入会申请状态？!",
		    width:300,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			gridSubAjax(fids,companyName,position,stateId);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}else{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择需要更新状态的入会申请!",
			width: 250,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}

function gridSubAjax(param,fcompanyNames,fcompanyPositions,stateId)
{
	Ext.Ajax.request({
		url : getPath() + '/apply/updateState',
		params : {fids : param,fcompanyNames:fcompanyNames ,fcompanyPositions:fcompanyPositions, state : stateId,groupId:window.parent.groupId},
		success: function (result, request){
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

//=================================删除====================================
function deleteApply(){
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
		    msg: "确认要删除当前入会申请？!",
		    width:300,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			ApplyAjax(fids);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}else{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择需要删除的入会申请!",
			width: 250,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}

function ApplyAjax(param)
{
	Ext.Ajax.request({
		url : getPath() + '/apply/deleteApply',
		params : {fids : param},
		success: function (result, request){
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