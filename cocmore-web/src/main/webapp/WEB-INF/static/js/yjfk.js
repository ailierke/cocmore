var LIMIT = 26;var editWin;

//create the data store
var records = new Ext.data.Record.create([
       {name: 'fid'},
       {name: 'fcontactInfo'},
       {name: 'fmessage'}
    ]
);

var store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
		root:'obj.list',
		totalProperty:'obj.count'
	},records),
	proxy:new Ext.data.HttpProxy({
			disableCaching :false,
			url: getPath() + '/feedback/findAllfeedback'
	}),
	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});

var tool = new Ext.Toolbar({ //工具栏
	items:['内容：',{
		xtype:'textfield',
		emptyText:'请输入内容',
		id:'searchCondition',
		maxLength:200 
	},'-']
});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 18
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
	    		id:'fcontactInfo', 
	    		header: '联系方式', 
	    		sortable: true, 
	    		dataIndex:'fcontactInfo'
         },{
	    		id:'fmessage', 
	    		header: '内容', 
	    		width: 400, 
	    		dataIndex:'fmessage'
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

var feedback11 = new Ext.form.FormPanel({
    baseCls: 'x-plain',
    labelWidth: 40,
    labelAlign: 'right',
    border: true,
	frame: true,
    defaultType: 'textfield',
    defaults:{anchor:'93%'},
    items: [{
 		fieldLabel:'手机',
 		id:'fcontactInfo11',
 		name:'fcontactInfo',
        regex : /^((1[3|4|5|8]+\d{9})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/,
        regexText:"格式不正确!",
        blankText:'该字段不允许为空',
        allowBlank: false
 	},{
 		xtype:'textarea',
 		height:270,
 		fieldLabel:'内容',
 		id:'fmessage11',
 		name:'fmessage',
        allowBlank: false
 	}]
});

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
                width:320,
                height:340,
                frame:true,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
			    items :[feedback11],
			    listeners:{hide:function(){feedback11.form.reset();}}
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
	feedback11.form.setValues({
		fcontactInfo11  								: row.get('fcontactInfo'),
		fmessage11     									: row.get('fmessage')
	});
}
function setTitleAndUrl(row, win) {
	win.setTitle('意见反馈详情');
	win.show();
}