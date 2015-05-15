var LIMIT = 26;var editWin;

//create the data store
var records = new Ext.data.Record.create([
       {name: 'fid'},
       {name: 'fnumber'},
       {name: 'finvoicesType'},
       {name: 'ffixedValue'},
       {name: 'fdateValue'},
       {name: 'fserialNumber'},
       {name: 'fpreview'},
       {name: 'ybasicOrganization'},
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
			url: getPath() + '/enc/findAllEnc'
	}),
	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});

var tool = new Ext.Toolbar({ //工具栏
});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 14
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
	    		width: 220, 
	    		sortable: true, 
	    		dataIndex:'fnumber'
         },{
	    		id:'ffixedValue', 
	    		header: '固定值', 
	    		width: 100, 
	    		sortable: true, 
	    		dataIndex:'ffixedValue'
         },{
	    		id:'fdateValue', 
	    		header: '是否使用日期', 
	    		width: 100, 
	    		sortable: true, 
	    		dataIndex:'fdateValue'
         },{
        	    id: 'fserialNumber', 
	    		header: '流水号', 
	    		width: 100, 
	    		sortable: true, 
	    		dataIndex:'fserialNumber'
    	 },{
        	 	name: 'fpreview', 
	    		header: '摘要', 
	    		width: 100, 
	    		sortable: true, 
	    		dataIndex:'fpreview'
         },{
        	 	name: 'ybasicOrganization', 
	    		width: 100, 
	    		hidden: true, 
	    		dataIndex:'ybasicOrganization'
         },{
        	 	name: 'finvoicesType', 
	    		header: '单据类型', 
	    		hidden: true, 
	    		dataIndex:'finvoicesType'
         },{
	    		id:'fcreaterId', 
	    		header: '创建人', 
	    		width: 100, 
	    		hidden: true, 
	    		dataIndex:'fcreaterId'
         },{
	    		id:'fcreateTime', 
	    		header: '创建时间', 
	    		width: 150, 
	    		hidden: true, 
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
	    		width: 70, 
	    		sortable: true,
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
	fieldLabel : "行政组织",
	store : yBasicOrganizationStore,
	id:'ybasicOrganization',
	hiddenName : 'YBasicOrganization.fid',
	triggerAction : 'all',
	lazyInit : true,
	valueField : 'fid',
	displayField : 'fname',
	selectOnFocus:true,
	editable:false,
	allowBlank:false,
	readOnly:true
});

var bmgzForm = new Ext.form.FormPanel({
     border : false,
	 frame	: true,
	 labelAlign: 'left',
	 buttonAlign: 'center',
	 labelWidth:80,
	 autoScroll:false,
	 containerScroll: true,
	 width: 590,
     height: 240,
	 defaults:{anchor:'96%',xtype:'textfield'},
     items:[{xtype:'hidden',id:'fid',name:'fid'},{
					        fieldLabel:'编号',
					        id:'fnumber',
					        name:'fnumber',
					        readOnly:false
					 	},{
					        fieldLabel:'固定值',
					        id:'ffixedValue',
					        name:'ffixedValue',
					        maxLength:12,
					        blankText:'该字段不允许为空',
					        listeners:{
					        	blur:function(){
					        		var ffixedValue = Ext.getCmp('ffixedValue').getValue();
					        		var fserialNumber = Ext.getCmp('fserialNumber').getValue();
					        		var fdateValue = Ext.getCmp('fdateValue').getValue();
					        		// 得到系统日期
					        		var date = new Date();
								    var dateString = date.getFullYear()+"_"+(date.getMonth()+1)+"_"+date.getDate();
					        		if(fdateValue=='是'){
					        			Ext.getCmp('fpreview').setValue(ffixedValue + '_' + dateString + '_' + fserialNumber);
					        		}else{
					        			Ext.getCmp('fpreview').setValue(ffixedValue + '_' + fserialNumber);
					        		}
					        	}
					        },
					        allowBlank: false
					 	},{
					        fieldLabel:'是否添加日期',
					        id:'fdateValue',
					        layout:'column',
						 	columns:2,
						 	xtype:'radiogroup',
						 	items:[{boxLabel: "是", name: 'fdateValue',inputValue: "是",checked:true},  
						 	       {boxLabel: "否", name: 'fdateValue',inputValue: "否"}]
					 	},{
					        fieldLabel:'流水号',
					        id:'fserialNumber',
					        name:'fserialNumber',
					        listeners:{
					        	blur:function(){
					        		var ffixedValue = Ext.getCmp('ffixedValue').getValue();
					        		var fserialNumber = Ext.getCmp('fserialNumber').getValue();
					        		var fdateValue = Ext.getCmp('fdateValue').getValue();
					        		//获取流水号长度
					        		var length = fserialNumber.length;
					        		Ext.getCmp('fserialNumberLength').setValue(length);
					        		// 得到系统日期
					        		var date = new Date();
								    var dateString = date.getFullYear()+"_"+(date.getMonth()+1)+"_"+date.getDate();
					        		if(fdateValue=='是'){
					        			Ext.getCmp('fpreview').setValue(ffixedValue + '_' + dateString + '_' + fserialNumber);
					        		}else{
					        			Ext.getCmp('fpreview').setValue(ffixedValue + '_' + fserialNumber);
					        		}
					        	}
					        },
					        maxLength:150
					 	},{
					        fieldLabel:'摘要',
					        id:'fpreview',
					        name:'fpreview',
					        maxLength:150
					 	},{
			            	hidden:true,
					 		fieldLabel:'单据类型',
					 		id:'finvoicesType',
					 		name:'finvoicesType'
					 	},{
						 	xtype:'hidden',
					        id:'fbillState',
					        name:'fbillState'
					 	},{
					        xtype:'hidden',
					        id:'fcreateTime',
					        name:'fcreateTime'
					 	},{
					        xtype:'hidden',
					        id:'fcreaterId',
					        name:'fcreaterId'
					 	},{
					        xtype:'hidden',
					        id:'fmodifiedTime',
					        name:'fmodifiedTime'
					 	},{
					        xtype:'hidden',
					        id:'fmodifiedId',
					        name:'fmodifiedId'
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
						     id:'flag',
						     name:'flag'
					 	}]
});
/**************************** 自定义元素结束 *******************************/
/******************************** 操作 **********************************/

function editData(row){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var  row = Ext.getCmp("grid").getSelectionModel().getSelected();
	     if(!row){
	    	 alertWarring("请选择需要编辑的编码规则!");return;
	     }
         if(!editWin){
            editWin = new Ext.Window({
		        id: 'editWin',
		        title:'修改编码规则',
		        layout:'fit',
                width:600,
                height:220,
                frame:true,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
			    items :[bmgzForm],
			    buttons:[{
				    text: '保存',
				    handler:function(){
				    	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
						
				          if(bmgzForm.form.isValid()){
				        	  bmgzForm.form.submit({
				                   method:'POST',
				                   waitMsg:'正在提交.....',
								   waitTitle:'请稍等',
				                   success:function(form, action){
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
			    listeners:{hide:function(){}}
            });
		}
         
	    var date = new Date();  // 得到系统日期
	    var dateString = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	    bmgzForm.getForm().findField('fmodifiedTime').setValue(dateString);
	    bmgzForm.getForm().findField('fmodifiedId').setValue(getUser()[1]);	
	    bmgzForm.getForm().findField('flastModifiedTime').setValue(dateString);
	    bmgzForm.getForm().findField('flastModifiedId').setValue(getUser()[1]);
	    if(row!='add'){
	    	loadData(row);
	    }

        bmgzForm.form.url = getPath() + '/enc/updateEnc';
        editWin.show();
	}
function loadData(row) {
	bmgzForm.form.setValues({
			fid : row.get('fid'),
			fnumber  : row.get('fnumber'),
			ffixedValue    : row.get('ffixedValue'),
			fdateValue  : row.get('fdateValue'),
			fserialNumber : row.get('fserialNumber'),
			fpreview : row.get('fpreview'),
			fdateValue:row.get('fdateValue'),
//			ybasicOrganization  : row.get('ybasicOrganization').fid,
			finvoicesType				: row.get('finvoicesType'),
			fcreaterId : row.get('fcreaterId'),
			fcreateTime:row.get('fcreateTime'),
			flag:row.get('flag'),
			fbillState:10
		});
}

function RcmdForm(stateId){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var  row = Ext.getCmp("grid").getSelectionModel().getSelected();
	if(row){
		Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认要更新当前编码规则状态!",
		    width:300,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			gridSubAjax(row.get('fid'),stateId);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}else{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择需要更新状态的编码规则信息!",
			width: 250,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}
function gridSubAjax(param,stateId)
{
	Ext.Ajax.request({
		url : getPath() + '/enc/updateEncStatus',
		params : {fid : param , status : stateId},
		//method : 'POST',
		success: function (result, request) 
		{
			Ext.MessageBox.show({
				title : "提示",
				msg : "更新成功！",
				width : 250,
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
			//store.reload();
			Ext.getCmp("grid").store.reload();
		},
		failure: function ( result, request) {
			alertError("系统运行超时或执行失败");
		}
	});
}