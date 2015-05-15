var LIMIT = 26;var editWin;var mbszWin;var superId;var jssqWin;

var records = new Ext.data.Record.create([
                                          {name:'fid'},
                                          {name: 'fnumber'},
                                          {name: 'fname'},
                                          {name: 'fremark'},
                                          {name: 'fstate'},
                                          {name: 'fcreaterId'},
                                          {name: 'fmodifiedId'},
                                          {name: 'flastModifiedId'},
                                          {name: 'fcreateTime'},
                                          {name: 'fmodifiedTime'},
                                          {name: 'flastModifiedTime'},
                                          {name: 'fisGroups'},
                                          {name: 'fprivileges'},
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
			url: getPath() + 'character/getAllDynamicPagingList'
	}),
	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});

var tool=new Ext.Toolbar({ //工具栏
	items:['角色名称：',{
		xtype:'textfield',
		emptyText:'请输入角色名',
		id:'searchCondition',
		width:150
	},'-']
});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 19
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
        	alertWarring("没有符合条件的数据!"); 
        }
    });
    store.load({params:{start:0,limit:LIMIT}});
    var cm = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer(),{id:'fid',hidden:true},
    	{
    		id:'fnumber', 
    		header: '编号', 
    		width: 240, 
    		sortable: true, 
    		dataIndex:'fnumber'
       },{
    		id:'fname', 
    		header: '名称', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fname'
       },{
    		id:'fremark', 
    		header: '备注',
    		width: 240, 
    		sortable: true,
    		dataIndex:'fremark'
       },{
    		id:'fstate', 
    		header: '状态', 
    		width: 100, 
    		sortable: false, 
    		renderer:status,
    		dataIndex:'fstate'
       },{
	   		id:'fprivileges', 
			header: '授权状态', 
			width: 100, 
			sortable: false, 
			renderer:privileges,
			dataIndex:'fprivileges'
       },{
    		id:'fcreaterId', 
    		header: '创建人', 
    		width: 300, 
    		sortable: true, 
    		dataIndex:'fcreaterId',
    		hidden:true
       },{
    		id:'fmodifiedId', 
    		header: '修改人', 
    		width: 150, 
	        maxLength:125,
    		sortable: true, 
    		allowBlank: true,
    		dataIndex:'fmodifiedId',
    		hidden:true  
       },{
	       	id:'flastModifiedId', 
	   		header: '最后修改人', 
	   		width: 200, 
	   		sortable: true, 
	   		dataIndex:'flastModifiedId' ,
    		hidden:true  
       },{
	   	    id:'fcreateTime',
		    header: '创建时间', 
	   		width: 240, 
	   		sortable: true, 
	   		dataIndex:'fcreateTime' ,
			hidden:true  
       },{
    	    id:'fmodifiedTime',
    	    header: '修改时间', 
	   		width: 240, 
	   		sortable: true, 
	   		dataIndex:'fmodifiedTime' ,
    		hidden:true  
       },{
    		id:'flastModifiedTime', 
    		header: '最后修改时间', 
    		width: 70, 
    		sortable: true,
    		dataIndex:'flastModifiedTime',
    		hidden:true  
       },{
	   		id:'flag', 
			header: '自增标识', 
			width: 70, 
			sortable: true,
			dataIndex:'flag',
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
    		return '失效';
    	}else if(data == 7){
    		return '启用';
    	}else if(data == 8){
    		return '禁用';
    	}else if(data == 9){
    		return '作废';
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
	grid.on('dblclick', editData);
});

/**************************** 自定义元素 **********************************/

var editjsglForm = new Ext.form.FormPanel({
    border : false,
	 frame	: true,
	 labelAlign: 'left',
	 buttonAlign: 'center',
	 labelWidth:80,
	 width:400,
     height:200,
	 autoScroll:false,
	 containerScroll: true,
     defaults:{anchor:'90%',xtype:'textfield'},
	 items:[{xtype:'hidden',id:'fid',name:'fid'},{
		        fieldLabel:'编号',
		        id:'fnumber',
		        name:'fnumber',
		        readOnly:true,
	            blankText:'该字段不允许为空',
	            allowBlank: false
	        },{
		        fieldLabel:'名称',
		        id:'fname',
		        maxLength:24,
		        name:'fname',
	            blankText:'该字段不允许为空',
	            allowBlank: false
		 	},{
		        fieldLabel:'备注',
		        xtype:'textarea',
		        maxLength:125,
		        id:'fremark',
		        name:'fremark'
     		},{
			 	xtype:'hidden',
		        id:'fcreateTime',
		        name:'fcreateTime'
     		},{
			 	xtype:'hidden',
		        id:'fstate',
		        name:'fstate'
     		},{
		        xtype:'hidden',
		        id:'fcreaterId',
		        name:'fcreaterId'
     		},{
     			 xtype:'hidden',
     		     id:'flag',
     		     name:'flag'
     		 },{
     			 xtype:'hidden',
    		     id:'fmodifiedId',
    		     name:'fmodifiedId'
     		 },{
     			 xtype:'hidden',
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
    		 }
	]
});

var mkPanelLoader = new Ext.tree.TreeLoader({
	dataUrl:getPath() + 'roleauthorization/getByBasicFunctionTree'
});
var mkPanel = new  Ext.tree.TreePanel({
	title 			 : '功能模块', 
  	border     		 : true,
    split      		 : true,  
  	animate    		 : true,  
  	rootVisible		 : false,
  	autoScroll 		 : false,
	height			 : 440,
	useArrows: true,
	autoScroll: true,
	animate: true,
	enableDD: true,
	containerScroll: true,
	border: false,
	loader:mkPanelLoader,
	root: {
	    nodeType: 'async',
	    text: 'mkRoot',
	    draggable: false,
	    id: 'mkRoot'
	}//,
//	tbar 			: new Ext.Toolbar({
//    	items:[{
//				text: '&nbsp;保&nbsp;存&nbsp;',
//				enableToggle: false,
//				iconCls:'common_add',
//				handler: gnTreeSave
//			}]
//    })
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
/**************************** 自定义元素结束 *******************************/
/**************************** 操作 **********************************/
//function getNumberStore(){
////	Ext.Ajax.request({
////		url:getPath() + '/base/position!doNotNeedSessionAndSecurity_getFNumber.ad',
////		success:function(response, options) {
////			Ext.getCmp("fnumber").setValue(response.responseText);
////		}
////	});
//	var date = new Date();  // 得到系统日期
//	var dateString = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
//	Ext.getCmp("fnumber").setValue('  ddS');
//}

function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var searchCondition = Ext.getCmp('searchCondition').getValue();
	//模糊查询
	store.load({params:{searchName:searchCondition,start:0,limit:LIMIT}});
}

function editData(row){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		if(row!='add'){
		     row = Ext.getCmp("grid").getSelectionModel().getSelected();
		     if(!row){
		    	 alertWarring("请选择需要编辑的角色!");return;
		     }
		    var selectRow = Ext.getCmp('grid').getSelectionModel().getSelected();
			var billState = selectRow.get("fstate");
			if(billState == 3){
				alertWarring("该状态为审核状态，不能修改！");
			}else if(billState == 7){
				alertWarring("该状态为启用状态，不能修改！");
			}
		}else{
			getNumberStore();
		}
    	this.row = row;
    	var that = this;
         if(!editWin){
            editWin = new Ext.Window({
		        id: 'editWin',
		        layout:'fit',
                width:400,
                height:240,frame:true,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
			    items :{layout:'vbox',items:[editjsglForm]},
			    buttons:[{
					    text: '保存',
					    handler:function(){
					          if(editjsglForm.form.isValid()){
					        	  editjsglForm.form.submit({
					                   method:'POST',
					                   waitMsg:'正在提交.....',
									   waitTitle:'请稍等',
					                   success:function(form, action){
					                	   	 Ext.getCmp('grid').getStore().reload();
						                	 Ext.getCmp("editWin").hide();
					                   },
					                   failure:function(form, action){
					                      alertWarring(action.msg);
					                   }
					              });   
					          }else{
					               alertWarring("内容填写不完整");
					          }
					     }
					 },{
						 text:'取消',
					      handler:function(){
					    	  Ext.getCmp("editWin").hide();
					      }
						 
				}],
			    listeners:{hide:function(){
			    	editjsglForm.form.reset();
			    	
			    }}
            });
		}
        setTitleAndUrl(row, editWin);

	    var date = new Date();  // 得到系统日期
	    var dateString = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	    editjsglForm.getForm().findField('fstate').setValue(0);
	    editjsglForm.getForm().findField('fcreateTime').setValue(dateString);
	    
	    editjsglForm.getForm().findField('fcreaterId').setValue(getUser()[1]);	
	    
	    editjsglForm.getForm().findField('fmodifiedTime').setValue(dateString);
    	editjsglForm.getForm().findField('fmodifiedId').setValue(getUser()[1]);
    	editjsglForm.getForm().findField('flastModifiedTime').setValue(dateString);
    	editjsglForm.getForm().findField('flastModifiedId').setValue(getUser()[1]);
	    
	    if(row != 'add') {
	      	loadData(row);
		}		
	}

function loadData(row) {
		editjsglForm.form.setValues({
			fid						: row.get('fid'),
			fnumber  				: row.get('fnumber'),
			fname    				: row.get('fname'),	
			fstate  				: 10,
			fremark  				: row.get('fremark'),
			fcreateTime				: row.get('fcreateTime'),
			fcreaterId				: row.get('fcreaterId'),
			fmodifiedId             : row.get('fmodifiedId'),
			fmodifiedTime           : row.get('fmodifiedTime'),
			flastModifiedId         : row.get('flastModifiedId'),
			flastModifiedTime       : row.get('flastModifiedTime'),
			flag					:row.get('flag')
		});	
}
function setTitleAndUrl(row, win) {
	if(row == 'add') {
 		win.setTitle('增加角色信息');
 		editjsglForm.form.url = getPath() + 'character/save';
	} else {
 		win.setTitle('修改角色信息');
  		editjsglForm.form.url = getPath() + 'character/update';
	}
	win.show();
}

function setState(stateId){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  var fids=new Array();
		  for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
		 }
		Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认需要更新角色状态!",
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
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择需要修改状态的角色信息!",
			width: 250,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}



function gridSubAjax(param,stateId)
{
	
	Ext.Ajax.request({
		url : getPath() + 'character/updateState',
		params : {fids : param , stateId : stateId},
		
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
	
}

//操作授权  显示界面
function jssq(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var row = Ext.getCmp('grid').getSelectionModel().getSelected();
	if(row){
		if(!jssqWin){
			jssqWin = new Ext.Window({
				id:'jssqWin',
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
				}]//,
//				listeners:{
//					hide:function(){}
//				}
			});
		}
		mkPanel.loader.dataUrl = getPath() + 'roleauthorization/getByBasicFunctionTree';
		mkPanel.loader.baseParams  ={jsid:row.get('fid')},
		//alert(row.get('fid'));
		mkPanel.root.reload();
		jssqWin.setTitle('【'+row.get('fname')+'】授权');
		jssqWin.show();
	}else{
		alertWarring("请选择需要进行授权的角色!");return;
	}
}

//用户 保存用户授权功能点
var gnTreeSave = function(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var selectRow =  Ext.getCmp('grid').getSelectionModel().getSelected();
	 if(selectRow){
		 	var ids = mkPanel.getChecked("id");
	 		var myMask = new Ext.LoadMask(Ext.getBody(), {
					msg : '正在处理......请稍后！',
					removeMask : true // 完成后移除
				});
		 	myMask.show();
			Ext.Ajax.request({
				url:getPath() + 'roleauthorization/save',
				method: 'POST',
				params:'ids='+ids+'&characterId='+selectRow.get("fid"),
    			text: "saving...",    
    			success: function (result, request ){
    				var data = Ext.util.JSON.decode(result.responseText);//alert(data.msg);
		    		if(data.msg == '成功'){
		    			alertWarring("保存成功！");
		    		}else if(data.msg == '失败'){
		    			alertWarring("保存失败！");
		    		}

					myMask.hide();
		    		jssqWin.hide();
    			}
			});
	 }else{
		 alertWarring('没有选择角色！');
	 }
}


function getNumberStore(){
	Ext.Ajax.request({
		url:getPath() + '/serialNumber/getSerialNumber',
		params:{prefix:'QX-JSGL'},
		success:function(response, options) {
			Ext.getCmp("fnumber").setValue(Ext.util.JSON.decode(response.responseText).serialNumber);
		}
	});
}
