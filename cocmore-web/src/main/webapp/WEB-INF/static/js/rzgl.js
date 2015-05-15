var LIMIT = 26;var editWin;

var records = new Ext.data.Record.create([
                                          {name:'fid'},
								           {name: 'fnumber'},
								           {name: 'ftype'},
								           {name: 'fuserId'},
								           {name: 'ftime'},
								           {name: 'fcontent'},
								           {name: 'fip'},
								           {name: 'fremark'}
								        ]
								    );
var store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
		root:'obj.list',
		totalProperty:'obj.count'
	},records),
	proxy:new Ext.data.HttpProxy({
			disableCaching :false,
			url: getPath() + '/log/findAllLog'
	}),
	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});

var tool =  new Ext.Toolbar({ //工具栏
	items:['用户名：',{
		xtype:'textfield',
		emptyText:'请输入用户名',
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
		functionId : 13
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
    		id:'fnumber', 
    		header: '编号', 
    		width: 240, 
    		sortable: true, 
    		dataIndex:'fnumber'
           },{
    		id:'ftype', 
    		header: '类型', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'ftype'
           },{
    		id:'fuserId', 
    		header: '用户名', 
    		width: 150, 
    		sortable: true, 
    		allowBlank: true,
    		dataIndex:'fuserId'
           },{
    		id:'ftime', 
    		header: '时间', 
    		width: 200, 
	        format:'Y-m-d H:i:s',
    		sortable: true, 
    		allowBlank: true,
    		dataIndex:'ftime'
           },{
    		id:'fip', 
    		header: 'IP地址', 
    		width: 160, 
    		sortable: true, 
    		dataIndex:'fip'
           },{
        	id:'fremark', 
       		header: '备注', 
       		width: 200, 
       		sortable: true, 
       		dataIndex:'fremark'   
           },{
    		id:'fcontent', 
    		header: '日志内容', 
    		width: 470, 
    		sortable: false, 
    		dataIndex:'fcontent'
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
    	tbar:tool,
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
	 labelWidth:100,
	 autoScroll:false,
	 containerScroll: true,
	 width: 480,
     height: 380,
     defaults:{anchor:'90%',xtype:'textfield'},
	 items:[{
	        fieldLabel:'编号',
	        id:'fnumber',
	        name:'fnumber',
	        readOnly:true
	 },{
	        fieldLabel:'类型',
	        id:'ftype',
	        name:'ftype',
	        readOnly:true
	 },{
	        fieldLabel:'用户名',
	        id:'fuserId',
	        name:'fuserId',
	        readOnly:true,
            allowBlank: true
	 },{
	        xtype:'datefield',
	        fieldLabel:'时间',
	        id:'ftime',
	        format:'Y-m-d H:i:s',
	        name:'ftime',
	        readOnly:true
	 },{
	        fieldLabel:'IP地址',
	        id:'fip',
	        name:'fip',
	        readOnly:true,
           allowBlank: true
	 },{
	        fieldLabel:'备注',
	        id:'fremark',
	        name:'fremark',
	        readOnly:true,
           allowBlank: true
	 },{
		 	xtype:'textarea',
		 	height:60,
	        fieldLabel:'日志内容',
	        id:'fcontent',
	        name:'fcontent',
	        readOnly:true,
            allowBlank: true
	 }]
});
/**************************** 自定义元素结束 *******************************/
/**************************** 操作 **********************************/
function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var searchCondition = Ext.getCmp('searchCondition').getValue();
	var type = Ext.getCmp('type').getValue();
	//模糊查询
	store.load({params:{searchName:searchCondition,type:type,start:0,limit:LIMIT}});
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
                width:500,
                height:270,
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
		alertWarring("请选中日志列!");
	}
}

function loadData(row) {
	editRzxxForm.form.setValues({
		fnumber  								: row.get('fnumber'),
		ftype     									: row.get('ftype'),
		fuserId  	: 								row.get('fuserId'),
		ftime  										: row.get('ftime'),
		fcontent  								: row.get('fcontent'),
		fip  											: row.get('fip'),
		fremark  									: row.get('fremark')
	});
}
function setTitleAndUrl(row, win) {
	win.setTitle('日志详情');
	win.show();
}