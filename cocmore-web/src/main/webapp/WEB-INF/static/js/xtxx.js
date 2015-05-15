var LIMIT = 26;var editWin;

//create the data store
var records = new Ext.data.Record.create([
       {name: 'fid'},
       {name: 'fnumber'},
       {name: 'fmessageType'},
       {name: 'fcontent'},
       {name: 'fcreaterId'},
       {name: 'fcreateTime'},
       {name: 'fmodifiedId'},
       {name: 'fmodifiedTime'},
       {name: 'flastModifiedId'},
       {name: 'flastModifiedTime'},
       {name: 'fbillState'},
       {name: 'flag'}
    ]
);

var store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
		root:'obj.list',
		totalProperty:'obj.count'
	},records),
	proxy:new Ext.data.HttpProxy({
			disableCaching :false,
			url: getPath() + '/msg/findAllMsg'
	}),
	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});

var tool = new Ext.Toolbar({ //工具栏
	items:['消息类型：',{
		xtype:'textfield',
		emptyText:'请输入类型名称',
		id:'searchCondition',
		width:150
	},'-']
});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 16
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
    store.load({params:{start:0,limit:LIMIT}});
    
    var cm = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer(),
    	{id:'fid',hidden:true},
    	{flag:'flag',hidden:true},
    	{
	    		id:'fnumber', 
	    		header: '编号', 
	    		hidden: true, 
	    		dataIndex:'fnumber'
         },{
	    		id:'fcontent', 
	    		header: '内容', 
	    		width: 400, 
	    		dataIndex:'fcontent'
         },{
        	 	name: 'fmessageType', 
	    		header: '类型', 
	    		width: 100, 
	    		dataIndex:'fmessageType'
         },{
	    		id:'fcreaterId', 
	    		header: '创建人', 
	    		dataIndex:'fcreaterId'
         },{
	    		id:'fcreateTime', 
	    		header: '创建时间', 
	    		dataIndex:'fcreateTime'
         },{
	    		id:'fmodifiedId', 
	    		header: '修改人', 
	    		dataIndex:'fmodifiedId',
	    		hidden:true
         },{
	    		id:'fmodifiedTime', 
	    		header: '修改时间', 
	    		dataIndex:'fmodifiedTime',
	    		hidden:true
         },{
	    		id:'flastModifiedId', 
	    		header: '最后修改人', 
	    		dataIndex:'flastModifiedId',
	    		hidden:true
         },{
	    		id:'flastModifiedTime', 
	    		header: '最后修改时间', 
	    		dataIndex:'flastModifiedTime',
	    		hidden:true
         },{
	    		id:'fbillState', 
	    		header: '状态', 
	    		renderer:status,
	    		dataIndex:'fbillState'
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
    
    // render the grid to the specified div in the page
    var displayPanel = new Ext.Viewport({
    	renderTo:'mainDiv',
    	layout:'border',
    	items:[grid]
    });
    Ext.EventManager.onWindowResize(function(){ 
    	grid.getView().refresh() 
	});
    grid.on('dblclick', editData);
});

/**************************** 自定义元素 **********************************/

var editRzxxForm = new Ext.form.FormPanel({
     border : false,
	 frame	: true,
	 labelAlign: 'right',
	 buttonAlign: 'center',
	 labelWidth:60,
	 autoScroll:false,
	 containerScroll: true,
	 width: 480,
     height: 300,
     defaults:{anchor:'90%',xtype:'textfield'},
	 items:[{
	        fieldLabel:'编号',
	        id:'fnumber',
	        name:'fnumber',
	        xtype:'hidden'
	 },{
	        fieldLabel:'类型',
	        id:'fmessageType',
	        name:'fmessageType',
	        readOnly:true
	 },{
		 	xtype:'textarea',
		 	height:60,
	        fieldLabel:'内容',
	        id:'fcontent',
	        name:'fcontent',
	        readOnly:true
	 }]
});
/**************************** 自定义元素结束 *******************************/
/**************************** 操作 **********************************/
function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var searchCondition = Ext.getCmp('searchCondition').getValue();
	//模糊查询
	store.load({params:{searchName:searchCondition,start:0,limit:LIMIT}});
}

function editData(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var row = Ext.getCmp('grid').getSelectionModel().getSelected();
	if(row){
		this.row = row;
    	var that = this;
         if(!editWin){
            editWin = new Ext.Window({
		        id: 'editWin',
		        layout:'fit',
                width:400,
                height:140,
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
		alertWarring("请选中系统消息列!");
	}
}

function loadData(row) {
	editRzxxForm.form.setValues({
		fnumber  		: row.get('fnumber'),
		fcontent     	: row.get('fcontent'),
		fmessageType  	: row.get('fmessageType')
	});
}
function setTitleAndUrl(row, win) {
	win.setTitle('系统消息详情');
	win.show();
}