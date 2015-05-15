var LIMIT = 26;var editWin;

//create the data store
var records = new Ext.data.Record.create([
       {name: 'fid'},
       {name: 'fip'},
       {name: 'fequipment'},
       {name: 'faccessTime'},
       {name: 'fsystemVersion'},
       {name: 'ftelphone'}
    ]
);

var store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
		root:'obj.list',
		totalProperty:'obj.count'
	},records),
	proxy:new Ext.data.HttpProxy({
			disableCaching :false,
			url: getPath() + '/notlogin/findAllNotLogin'
	}),
	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});

var tool = new Ext.Toolbar({ //工具栏
	items:['设备：',{
		xtype:'textfield',
		emptyText:'请输入设备',
		id:'searchCondition',
		width:150
	},'-']
});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 17
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
    	{
    			id:'ftelphone',
    			dataIndex:'ftelphone',
    			header:'电话',
        		renderer:function(value){
        			return decryptByABC(value);
        		}
    	},{
	    		id:'fip', 
	    		header: 'IP', 
	    		sortable: true, 
	    		dataIndex:'fip'
         },{
	    		id:'fequipment', 
	    		header: '设备', 
	    		dataIndex:'fequipment'
         },{
        	 	name: 'faccessTime', 
	    		header: '访问时间', 
	    		dataIndex:'faccessTime'
         },{
	    		id:'fsystemVersion', 
	    		header: '系统版本号', 
	    		dataIndex:'fsystemVersion'
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
     height: 380,
     defaults:{anchor:'90%',xtype:'textfield'},
	 items:[{
	        fieldLabel:'电话',
	        id:'ftelphone',
	        name:'ftelphone',
	        readOnly:true
	 },{
	        fieldLabel:'IP',
	        id:'fip',
	        name:'fip',
	        readOnly:true
	 },{
	        fieldLabel:'设备',
	        id:'fequipment',
	        name:'fequipment',
	        readOnly:true
	 },{
		 	fieldLabel:'访问时间',
		 	id:'faccessTime',
		 	name:'faccessTime',
		 	readOnly:true
	 },{
		 	fieldLabel:'系统版本',
		 	id:'fsystemVersion',
		 	name:'fsystemVersion',
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
                height:190,
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
		alertWarring("请选中列!");
	}
}

function loadData(row) {
	editRzxxForm.form.setValues({
		ftelphone  	: 								row.get('ftelphone'),
		fip  										: row.get('fip'),
		fequipment  								: row.get('fequipment'),
		faccessTime  											: row.get('faccessTime'),
		fsystemVersion  									: row.get('fsystemVersion')
	});
}
function setTitleAndUrl(row, win) {
	win.setTitle('未登陆APP用户行为详情');
	win.show();
}