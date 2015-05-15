var LIMIT = 26;var editWin;var mbszWin;var jsonStr=[];var orgId;

var records = new Ext.data.Record.create([
                                          {name: 'fid'},
                                          {name: 'jsonStr'},
                                          {name: 'ySystemUsers'},
                                          {name: 'fnumber'},
                                          {name: 'fname'},
                                          {name: 'fsex'},
                                          {name: 'fbirthday'},
                                          {name: 'femail'},
                                          {name: 'fpreviousEmail'},
                                          {name: 'fhomePhone'},
                                          {name: 'fofficePhone'},
                                          {name: 'fmobilePhone'},
                                          {name: 'fsecondPhone'},
                                          {name: 'fsite'},
                                          {name: 'forganizationId'},
                                          {name: 'fcomment'},
                                          {name: 'fbillState'},
                                          {name: 'fcreaterId'},
                                          {name: 'fcreateTime'},
                                          {name: 'fmodifiedId'},
                                          {name: 'fmodifiedTime'},
                                          {name: 'flastModifiedId'},
                                          {name: 'flastModifiedTime'},
                                          {name: 'flag'}
                                       ]
                                   );
                                   
   //根据组织获取职员信息    
   var storeEmp = new Ext.data.Store({
   		autoLoad:true,
       	reader:new Ext.data.JsonReader({
       		root:'obj.list',
       		totalProperty:'obj.count'
       	},records),
       	proxy:new Ext.data.HttpProxy({
       			disableCaching :false,
   				url: getPath() + '/emp/findAllEmp'
   		}),
       	remoteStore : true, // 是否远程调用数据
   		remoteSort : true   // 是否远程排序
   });
var tool =  new Ext.Toolbar({ //工具栏
		items:['名称：',{
			xtype:'textfield',
			emptyText:'请输入名称',
			id:'searchCondition',
			width:150
		},'-']
	});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 28
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
storeEmp.on('beforeload', function() {    
	  this.baseParams = {
			  orgId:orgId,start:0,limit:LIMIT
	  };
});
storeEmp.on('load', function(store, records, options) {
    if (records.length = 0) {
        Ext.Msg.alert("提示信息", "没有符合条件的数据!"); 
    }
});
storeEmp.load({params:{start:0,limit:LIMIT}});
    var order_sm = new Ext.grid.CheckboxSelectionModel({handleMouseDown: Ext.emptyFn});
    var cm = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer(), {id:'fid',hidden:true},{id:'jsonStr',hidden:true},
    	{
    		id:'fnumber', 
    		header: '编号', 
    		width: 150, 
    		hidden: true, 
    		dataIndex:'fnumber'
       },{
    		id:'fname', 
    		header: '姓名', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fname'
       },{
    		id:'fsex', 
    		header: '性别', 
    		width: 80, 
    		sortable: true, 
    		dataIndex:'fsex',
    		renderer:function(value){
    			if(value==0){
    				return '先生';
    			}else
    				return '女士';
    		}
       },{
    		id:'fbirthday', 
    		header: '出生年月', 
    		format:"Y-m-d H:i:s", 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fbirthday'
	  },{
    		id:'femail', 
    		header: '邮箱', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'femail'
       },{
    		id:'fpreviousEmail', 
    		header: '备用邮箱', 
    		dataIndex:'fpreviousEmail',
    		hidden:true
       },{
    		id:'fhomePhone', 
    		header: '家庭电话', 
    		dataIndex:'fhomePhone',
    		hidden:true
       },{
    		id:'fofficePhone', 
    		header: '办公电话', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fofficePhone'
       },{
    		id:'fmobilePhone', 
    		header: '手机', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fmobilePhone'
       },{
    		id:'fsecondPhone', 
    		header: '备用手机', 
    		dataIndex:'fsecondPhone',
    		hidden:true
       },{
    		id:'fsite', 
    		header: '地址', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fsite'
       },{
    		id:'forganizationId', 
    		header: '所属组织', 
    		hidden:true,
    		dataIndex:'forganizationId'
       },{
    		id:'fcomment', 
    		header: '描述', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fcomment'
       },{
    		id:'fbillState', 
    		header: '状态', 
    		width: 70, 
    		sortable: true,
    		renderer:status,
    		dataIndex:'fbillState'
       },{
	       	id: 'fcreaterId',
	    	dataIndex:'fcreaterId',
	    	hidden:true
		},{
	    	id: 'fcreateTime', 
	    	dataIndex: 'fcreateTime', 
	    	hidden:true
		},{
	       	id: 'fmodifiedId',
	    	dataIndex:'fmodifiedId',
	    	hidden:true
		},{
	    	id: 'fmodifiedTime', 
	    	dataIndex: 'fmodifiedTime', 
	    	hidden:true
		},{
	    	id: 'flag', 
	    	dataIndex: 'flag', 
	    	hidden:true
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
    		return '<span style="color:red">禁用</span>';
    	}else if(data == 9){
    		return '<span style="color:red">作废</span>';
    	}else if(data == 10){
    		return '变更';
    	}
	}
    
    var pagingToolBar = new Ext.PagingToolbar({
    	pageSize:LIMIT,
    	store:storeEmp,
    	displayInfo: true,
    	displayMsg: '当前显示第&nbsp;{0}&nbsp;到&nbsp;{1}&nbsp;条,共&nbsp;{2}&nbsp;条',
    	emptyMsg: "没有记录",
		plugins: new Ext.ux.ProgressBarPager()
	}); 
    // create the Grid
    var grid = new Ext.grid.GridPanel({
    	region : 'center', 
    	id : 'grid',
    	border: false,
    	columnLines:true,
    	loadMask:true,
    	store: storeEmp,
    	frame:false,
    	viewConfig: {forceFit:true},
    	monitorResize: true,
    	cm: cm,
    	tbar: tool,
		bbar: pagingToolBar
    });
    
    /************************************组织信息******************************/
    var root = new Ext.tree.AsyncTreeNode({
    	draggable:false,
		id : "source",
		text : "集团",
		loader: new Ext.tree.TreeLoader({dataUrl:getPath() + '/org/getOrgTree'})
    });
    var treePanel = new Ext.tree.TreePanel({
    	region : 'west',
		width : 200, 
    	title:'组织分类',
		root : root,
		rootVisible : false,
		autoScroll : true,
		monitorResize: true,
		//bodyStyle:'background:url(ext/resources/images/item_content_bg.jpg);',
		listeners:{
    		click:function(n){
    			orgId = n.attributes.id;
    			storeEmp.load({params:{orgId:n.attributes.id,start:0,limit:LIMIT}});
    		}
      	}
	});
    
    // render the grid to the specified div in the page
    var displayPanel = new Ext.Viewport({
    	renderTo:'mainDiv',
    	layout:'border',
    	items:[treePanel,grid]
    });
    Ext.EventManager.onWindowResize(function(){ 
    	grid.getView().refresh() 
	}) ;
    grid.on('dblclick',function(){ editData("detail")});
});
/**************************** 自定义元素 **********************************/
var yBasicOrganizationStore = new Ext.data.Store({
	autoLoad:true,
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'get',
		url : getPath() + '/org/findAllOrgBySuperId'
	}),
	reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
});
//行政组织
var yBasicOrganization = new Ext.form.ComboBox({
	fieldLabel : "部门",
	store : yBasicOrganizationStore,
	id:'forganizationId',
	hiddenName : 'forganizationId',
	triggerAction : 'all',
	lazyInit : true,
	valueField : 'fid',
	displayField : 'fname',
	selectOnFocus:true,
	editable:false,
	emptyText:'请选择部门',
	blankText:'部门不能为空',
	allowBlank:false
});

//职位
var fPositionStore = new Ext.data.Store({
	autoLoad:true,
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'get',
		url : getPath() + '/pos/findSuper'
	}),
	reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
});
//职员信息表单
var editForm = new Ext.form.FormPanel({
     border : false,
     id:'editZyxxForm',
	 frame	: true,
	 border:false,
	 labelAlign: 'left',
	 buttonAlign: 'center',
	 labelWidth:60,
	 autoScroll:false,
	 containerScroll: true,
	 width: 500,
     height: 200,
	 items:{
		layout:'column',
		items:[{
			 columnWidth: .5,
		     layout: 'form',
		     border: false,
		     defaults:{xtype:'textfield',anchor:'90%'},
		     items: [{id:'fid',name:'fid',xtype:'hidden'},{
			        fieldLabel:'编号',
			        id:'fnumber',
			        name:'fnumber',
			        xtype:'hidden'
			 },{
			        fieldLabel:'名称',
			        id:'fname',
			        name:'fname',
			        maxLength:12,
		            blankText:'该字段不允许为空',
		            allowBlank: false
			 },{
				 	xtype:'radiogroup',
				 	layout:'column',
				 	fieldLabel:'性别',
				 	id:'fsex',
				 	columns:2,
				 	items:[{boxLabel: "先生", name: 'fsex',inputValue: 0},  
				 	       {boxLabel: "女士", name: 'fsex',inputValue: 1}],
    	            blankText:'该字段不允许为空',
    	            allowBlank: false
			 },{
			        xtype:'datefield',
			        fieldLabel:'出生年月',
			        format:"Y-m-d H:i:s",
			        id:'fbirthday',
			        name:'fbirthday',
			        editable:false,
			        blankText:'该字段不允许为空',
    	            allowBlank: false
		     },{
			        fieldLabel:'邮件',
			        id:'femail',       	
			        name:'femail',
			        maxLength:24,
    	            blankText:'该字段不允许为空',
    	            allowBlank: false,
    	            vtype:'email'
			 },{
			        fieldLabel:'备用邮件',
		            id:'fpreviousEmail',
			        name:'fpreviousEmail',
			        maxLength:24,
			        vtype:'email'
			 },{
			        fieldLabel:'描述',
			        id:'fcomment',
			        maxLength:125,
			        name:'fcomment'
			 }
		 ]},{ 
			 columnWidth: .5,
             layout: 'form',
             border: false,
             defaults:{xtype:'textfield',anchor:'90%'},
             items: [{
			        fieldLabel:'家庭电话',
		            id:'fhomePhone',
			        name:'fhomePhone',
			        regex: /^((\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/,
		        	regexText:'格式不正确'
			 },{
			        fieldLabel:'办公电话',
		            id:'fofficePhone',
			        name:'fofficePhone',
		        	regex: /^((\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/,
		        	regexText:'格式不正确'
			 },{
			        fieldLabel:'手机',
		            id:'fmobilePhone',
			        name:'fmobilePhone',
			        regex : /^1+\d{10}$/,
			        regexText:"格式不正确!", //正则表达式错误提示  
			        allowBlank:false,
			        blankText:'该字段不允许为空'
			 },{
			        fieldLabel:'备用手机',
		            id:'fsecondPhone',
			        name:'fsecondPhone',
			        regex : /^1+\d{10}$/,
			        regexText:"格式不正确!" //正则表达式错误提示  
			 },{
				 	xtype:'hidden',
			        fieldLabel:'状态',
		            id:'fbillState',
			        name:'fbillState'
			 },{
				 	xtype:'hidden',
			        fieldLabel:'创建人ID',
		            id:'fcreaterId',
			        name:'fcreaterId'
			 },{
				 	xtype:'hidden',
			        fieldLabel:'创建时间',
		            id:'fcreateTime',
			        name:'fcreateTime'
			 },{
				 	xtype:'hidden',
			        fieldLabel:'修改人ID',
		            id:'fmodifiedId',
			        name:'fmodifiedId'
			 },{
				 	xtype:'hidden',
			        fieldLabel:'修改时间',
		            id:'fmodifiedTime',
			        name:'fmodifiedTime'
			 },{
				 xtype:'hidden',
			     id:'flastModifiedId',
			     name:'flastModifiedId'
			 },{
				 xtype:'hidden',
			     id:'flastModifiedTime',
			     name:'flastModifiedTime'
			 },{
	     	        xtype:'hidden',
	    	        id:'jsonStr',
	    	        name:'jsonStr'
            },{
     	        xtype:'hidden',
    	        id:'flag',
    	        name:'flag'
            },{
			        fieldLabel:'地址',
			        id:'fsite',
			        name:'fsite',
			        maxLength:50,
			        allowBlank:false,
			        blankText:'该字段不允许为空'
			 },yBasicOrganization]
		 }]
	}
});

/***********************************职位分录信息****************************/
//职位
var fposition = new Ext.form.ComboBox({
	fieldLabel : "职位",
	store : fPositionStore,
	id:'yBasicPosition',
	hiddenName : 'YBasicPosition.fid',
	triggerAction : 'all',
	lazyInit : true,
	valueField : 'fid',
	displayField : 'fname',
	selectOnFocus:true,
	editable:false,
	emptyText:'请选择职位',
	blankText:'职位不能为空',
	allowBlank:false
});
//组织
//var fOrganization = new Ext.form.ComboBox({
//	fieldLabel : "组织",
//	store : yBasicOrganizationStore,
//	id:'yBasicOrganizations',
//	hiddenName : 'data.YBasicOrganization.fid',
//	triggerAction : 'all',
//	lazyInit : true,
//	valueField : 'fid',
//	displayField : 'fname',
//	selectOnFocus:true,
//	editable:false,
//	emptyText:'请选择组织',
//	blankText:'组织不能为空',
//	allowBlank:false
//	
//});

var order_sm = new Ext.grid.CheckboxSelectionModel({handleMouseDown: Ext.emptyFn});
var order_cm = new Ext.grid.ColumnModel([
   	new Ext.grid.RowNumberer(),
   		order_sm,	
   	  {header:'ID',dataIndex:'fid',hidden:true,editor: {xtype:'hidden',name:'fid',value:'1'}},
      {header:'主要职位',dataIndex:'fkeyPost',editor: {xtype:'radio',name:'mainJob'},blankText:'请选择是否为主要职位',allowBlank:false},
      {header:'职位',dataIndex:'ybasicPosition.fid',editor: fposition,
    	  renderer: function(value, cellmeta, record,rowIndex,columnIndex) {
              var index = fPositionStore.find(Ext.getCmp('yBasicPosition').valueField,value);
              var record = fPositionStore.getAt(index);
              var returnvalue = "";
              if (record) {
                 returnvalue = record.data.fname;
              }
              return returnvalue;//注意这个地方的value是上面displayField中的value
        }
      }
      
//      ,{header:'组织',dataIndex:'yBasicOrganization.fid',editor: fOrganization,
//    	  renderer: function(value, cellmeta, record,rowIndex,columnIndex) {
//              var index = yBasicOrganizationStore.find(Ext.getCmp('yBasicOrganizations').valueField,value);
//              var record = yBasicOrganizationStore.getAt(index);
//              var returnvalue = "";
//              if (record) {
//                 returnvalue = record.data.fname;
//              }
//              return returnvalue;//注意这个地方的value是上面displayField中的value
//        }
//      }
     ]);
	 
var proxy = new Ext.data.MemoryProxy(jsonStr); 
var order_store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
		root:'obj',
	},[{name: 'fid'},
       {name: 'fkeyPost'},
       {name: 'ybasicPosition.fid'}
      ]),
	proxy:new Ext.data.HttpProxy({
			disableCaching :false,
			url : getPath() + '/emp/findEDByPosID'
	}),
	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});


var order_grid = new Ext.grid.EditorGridPanel({
	height: 170,
	id:'order_grid',
	title:'职位信息',
	anchor:'100%',
    style:'margin-bottom: 6px;',
    stripeRows: true,   //每行交替颜色
    loadMask: true,     //加载时提示load...
    store: order_store,
    clicksToEdit:1,
    region : 'center', 
	border: false,
	columnLines:true,
	frame:false,
	monitorResize: true,
	autoScroll:true,
    selModel: new Ext.grid.RowSelectionModel({singleSelect: false}), //设置为单行选中
    cm: order_cm,
    sm:order_sm,
    viewConfig:{
        forceFit: true
    },
    columns : [{
    	id : "fid",
    	header : "id",
    	dataIndex : "fid",
    	sortable : true,
    	editor : new Ext.form.Hidden()
    	}, {
    	header : "fkeyPost",
    	dataIndex : "fkeyPost",
    	editor : new Ext.form.Radio()
    	}, {
    	header : "yBasicPosition",
    	dataIndex : "yBasicPosition",
    	renderer : function(value){return value.fid},
    	editor : new Ext.form.ComboBox({
    		store : fPositionStore,
    		id:'yBasicPosition',
    		hiddenName : 'YBasicPosition.fid',
    		triggerAction : 'all',
    		lazyInit : true,
    		valueField : 'fid',
    		displayField : 'fname',
    		selectOnFocus:true,
    		editable:false
    	})
    	}],
    tbar: new Ext.Toolbar([
        '-',
        {
            iconCls : 'common_add',
            text : '新增',
            handler: function(){
                var n = order_grid.getStore().getCount();// 获得总行数
                var Plant = order_grid.getStore().recordType;
                var r = new Plant({
                	fnumberOfDays: '',
                	fdiscountRate: '',
                	fexplain: ''
                });
                order_grid.stopEditing();
                order_store.insert(n,r);
                order_grid.startEditing(n, 2);// 开始编辑2单元格
            }
        },
        '-',
        {
            iconCls : 'common_delete',
            text : '删除',
            id : 'deleteColumn',
            handler: function() {
                var rows = order_grid.getSelectionModel().getSelections();// 返回值为 Record 数组
                if(rows.length == 0){
                    alertError("请选择要删除的数据", 'deleteColumn');
                }
                else{
                    for(i = 0; i<rows.length; i++){
                    	order_store.remove(rows[i]);
                    }               
                }
            } 
        }
    ])
});

//添加监听
order_grid.addListener('afteredit', setOrg);          
function setOrg(e){
	
	//主要职员编辑列
	if(e.column == 3){
		var fkeyPost = order_grid.getColumnModel().getDataIndex(3);	//获取列
		var record = order_grid.getStore().getAt(e.row);	//获取当前编辑的行
		
		//判断当前编辑列，如果是选中，则为true
		if(record.get(fkeyPost)){
			var records = order_grid.getStore();	//获取grid所有行
			
			//遍历设置其它行该列的值为false
			for(var i = 0; i < records.getCount(); i++){
				if(e.row == i){
					records.getAt(i).set(fkeyPost,'是');
				}else{
					records.getAt(i).set(fkeyPost,'否');
				}
			}
		}else{
			record.set(fkeyPost,'否');
			return;
		}
    }
	
	//职位编辑列
//     if(e.column == 4){
//         var record = order_grid.getStore().getAt(e.row);
//         var position = order_grid.getColumnModel().getDataIndex(4); 
//         var organization = order_grid.getColumnModel().getDataIndex(5); 
//         var pValue = record.get(position);
//         
//         Ext.Ajax.request({
//        		//url : getPath() + 'base/position!doNotNeedSessionAndSecurity_findPositionById.ad',
//        		params : {id : pValue},
//        		success : function(response, options) {
//        			var txt = Ext.util.JSON.decode(response.responseText);
//        			record.set(organization, txt.obj.yBasicOrganization.fid);
//        		}
//        });
//     }
}


	
//
/**************************** 自定义元素结束 *******************************/
/**************************** 操作 **********************************/

function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var searchCondition = Ext.getCmp('searchCondition').getValue();
	//模糊查询
	store.load({params:{searchName:searchCondition,start:0,limit:LIMIT}});
}
//职员新增窗口
function editData(row){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var detail = row;	
	if(row!='add' && row!='detail'){
			row = Ext.getCmp('grid').getSelectionModel().getSelected();
		    if(!row){
		    	 alertWarring("请选择需要编辑的职员!");return;
		    }
		    var billState = row.get("fbillState");
		     if(billState == 3){
					alertError("该信息只能做反审核，生效操作！");return;
			 }else if(billState == 5){
					alertError("该信息只能做反审核，失效操作！");return;
			 }else if(billState == 6){
					alertError("该信息只能做作废操作！");return;
			 }else if(billState == 9){
					alertError("该信息已作废，无法进行操作！");return;
			 }
		     order_store.removeAll();
		}else if(row =='detail'){
			row = Ext.getCmp("grid").getSelectionModel().getSelected();
		}else{
			getNumberStore();
   			order_store.removeAll();
   			editForm.form.reset();
//			jsonStr=[];
//			order_store.loadData(jsonStr);
		}
    	this.row = row;
    	var that = this;
         if(!editWin){
            editWin = new Ext.Window({
		        id: 'editWin',
		        layout:'fit',
                width:500,
                height:450,
                frame:true,
                border:false,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
			    items :[{layout:'vbox',items:[editForm,order_grid]}],
			    buttons:[{
				    text: '保存',
				    handler:function(){
				    	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
				    	if(editForm.form.isValid()){
				        	  var n = order_grid.getStore().getCount();
				        	  jsonStr='[';
				        	  var aa = "aa";
							   for(var i =0;i<n;i++){
								   var fkeyPost = order_grid.getStore().getAt(i).get("fkeyPost");
								   var yBasicPosition = order_grid.getStore().getAt(i).get("ybasicPosition.fid");
								   var obj = [fkeyPost,yBasicPosition];
								   if(isNull(obj)){
									   jsonStr+= '{\"fid\":\"'+order_grid.getStore().getAt(i).get("fid")+'\",\"fkeyPost\":\"'
									   +fkeyPost+'\",\"yBasicPosition.fid\":\"'+yBasicPosition+'\"},'
								   }else{
									   jsonStr ="[]";
									   break;
								   }
							   }
							   if(jsonStr != "["){
								   jsonStr = jsonStr.substring(0, jsonStr.length-1) + ']';
							   }
							   

							   if(jsonStr.length == 0){
								   alertWarring("职位信息填写不完整");return;
							   }
							   editForm.getForm().findField('jsonStr').setValue(jsonStr);
				        	  
				        	  editForm.form.submit({
				                   method:'POST',
				                   waitMsg:'正在提交.....',
								   waitTitle:'请稍等',
								   onSubmit: function(){  
									    return $(this).form('validate');  
							       },
				                   success:function(form, action){
				                	   	 jsonStr = [];
					                  	 Ext.getCmp("grid").store.reload();
					                	 Ext.getCmp("editWin").hide();
					                	 
				                   },
				                   failure:function(form, action){
						                      alertWarring(action.result.msg);
				                   }
				              });   
				          }else{
				               alertWarring("内容填写不完整");
				          }
				     }
				 },{
					 text:'取消',
				      handler:function(){
					 	 Ext.getCmp('editWin').hide();
				      }
					 
			 }],
			    listeners:{
			    	hide:function(){
			    		jsonStr = [];
			    		editForm.getForm().reset();
			    	}
	    	 }
            });
		}
         
        setTitleAndUrl(row, editWin);

	    var date = new Date();  // 得到系统日期
	    var dateString = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	    editForm.getForm().findField('fbillState').setValue(0);
	    editForm.getForm().findField('fcreateTime').setValue(dateString);
	    editForm.getForm().findField('fcreaterId').setValue(getUser()[1]);
	    if(row != 'add') {
	    	if(detail && detail == "detail"){
	    		var tool = Ext.getCmp('editWin').buttons;
	    		for(var i =0;i<tool.length;i++){
	    			tool[i].setVisible(false);
	    		}
	    		Ext.getCmp('editWin').doLayout(); 
	    		Ext.getCmp('editWin').setTitle("查看详情");
	    	}else{
		    	editForm.getForm().findField('fmodifiedTime').setValue(dateString);
			    editForm.getForm().findField('fmodifiedId').setValue(getUser()[1]);
			    editForm.getForm().findField('flastModifiedTime').setValue(dateString);
			    editForm.getForm().findField('flastModifiedId').setValue(getUser()[1]);
			    var tool = Ext.getCmp('editWin').buttons;
				for(var i =0;i<tool.length;i++){
					tool[i].setVisible(true);
				}
				Ext.getCmp('editWin').doLayout(); 
	      	}
	      	loadData(row);
		}else{
			var tool = Ext.getCmp('editWin').buttons;
			for(var i =0;i<tool.length;i++){
				tool[i].setVisible(true);
			}
		}
	}

function loadData(row) {
	editForm.form.setValues({
		fid             			: row.get('fid'),
		fnumber 				: row.get('fnumber'),
		fname    				: row.get('fname'),
		fsex  					: row.get('fsex'),
		fbirthday   			: row.get('fbirthday'),
		femail  				: row.get('femail'),
		fpreviousEmail  : row.get('fpreviousEmail'),
		fhomePhone  		: row.get('fhomePhone'),
		fofficePhone  		: row.get('fofficePhone'),
		fmobilePhone  	: row.get('fmobilePhone'),
		fsecondPhone	 	: row.get('fsecondPhone'),
		fsite  					: row.get('fsite'),
		fcomment  			: row.get('fcomment'),
		FKeyPost  			: row.get('fkeyPost'),
		positions  			: row.get('positions'),
		fbillState  			: 10,
		forganizationId 	: row.get('forganizationId'),
		jsonStr 				: row.get('jsonStr'),
		fcreaterId			:row.get('fcreaterId'),
		fcreateTime		:row.get('fcreateTime'),
		flag						:row.get('flag')
	});
	order_store.load({params : {fid : row.get("fid")}});
}

function setTitleAndUrl(row, win) {
	if(row == 'add') {
 		win.setTitle('增加职员信息');
 		editForm.form.url = getPath()+'/emp/saveEmp';
	} else {
 		win.setTitle('修改职员信息');
 		editForm.form.url = getPath()+'/emp/updateEmp';
	}
	win.show();
}

/*设置职员状态*/
function setState(stateId){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  var fids=new Array();
		  var status = new Array();
		  for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
			  status[i] = records[i].get('fbillState');
			  if(status[i] == stateId){
					 alertError("状态一致，勿重复操作！");return;
			  }else if(stateId != 9 && status[i] == 6){
					 alertError("该信息只能做作废操作！");return;
			  }else if((stateId != 6 && stateId != 4) && status[i] == 5){
					 alertError("该信息只能做反审核，失效操作！");return;
			  }else if((stateId != 4 && stateId != 5) && status[i] == 3){
					 alertError("该信息只能做反审核，生效操作！");return;
			  }else if(stateId != 3 && (status[i] == 0 || status[i] == 10)){
					 alertError("该信息只能做审核操作！");return;
			  }else if(stateId != 3 && status[i]==4){
					 alertError("该信息只能做修改,审核操作!");return;
			  }
		 }
		Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认要更新当前职员状态!",
		    width:300,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			gridSubAjax(fids,stateId);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}
	else
	{
		alertWarring("请选择需要更新的职员!");
	}
}
function gridSubAjax(param,status)
{
	Ext.Ajax.request({
		url : getPath() + '/emp/updateEmpStatus',
		params : {fids : param,status:status },
		success: function (result, request) 
		{
			Ext.MessageBox.show({
				title : "提示",
				msg : Ext.util.JSON.decode(result.responseText).msg,
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

/*将职员设置为管理员*/
function isAdmin(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  var fids=new Array();
		  for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
		 }
		  Ext.Ajax.request({
				url : getPath() + '/emp/regAdmin',
				params : {fids : fids },
				success: function (result, request) 
				{
					Ext.MessageBox.show({
						title : "提示",
						msg : Ext.util.JSON.decode(result.responseText).msg,
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
		alertWarring("请选择需要设置为管理员的职员!");
	}
}

//获取number
function getNumberStore(){
	Ext.Ajax.request({
		url:getPath() + '/serialNumber/getSerialNumber',
		params:{prefix:'JC-ZYGL'},
		success:function(response, options) {
			Ext.getCmp("fnumber").setValue(Ext.util.JSON.decode(response.responseText).serialNumber);
		}
	});
}