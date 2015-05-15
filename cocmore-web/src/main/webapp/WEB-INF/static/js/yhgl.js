 var LIMIT = 26;var editWin;var jureditWin;var jurManageWin;var jurManageEditWin;var mbszWin;var funeditWin;var hyglPassWin;
 Ext.form.VTypes.emailText = '格式不正确';

 var records = new Ext.data.Record.create([
                                           {name: 'fid'},
                                           {name: 'faccount'},
                                           {name: 'ftypeId'},
                                           {name: 'fuserPassword'},
                                           {name: 'fbillState'},
                                           {name: 'flag'},
                                           {name: 'fSGName'}
                                         ]
                                     );
                                    
                                    
var store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
   		root:"obj.list",
		totalProperty:'obj.count'
	},records),
	proxy:new Ext.data.HttpProxy({
		disableCaching :false,
		url: getPath() + '/user/findUserAll'
	}),
});
                                    
 var tool =  new Ext.Toolbar({ //工具栏
		items:['账号：',{
			xtype:'textfield',
			emptyText:'请输入账号',
			id:'searchCondition',
			width:150 
		},'-']
	});
 Ext.Ajax.request({
		url : getPath() + 'systemconfigurationMenu/getfunctionjs',
		method : 'GET',
		params : {
			functionId : 25
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
    
    var order_sm = new Ext.grid.CheckboxSelectionModel({handleMouseDown: Ext.emptyFn});
    var cm = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer(),
    	{id:'fid',hidden:true},
    	{
    		id:'faccount', 
    		header: '账号', 
    		width: 100, 
    		sortable: true, 
    		dataIndex:'faccount'
       },{
    		id:'ftypeId', 
    		header: '账号类型', 
    		width: 100, 
    		sortable: true, 
    		allowBlank: true,
    		renderer:function(value){
    			if(value==0)
    				return "团体";
    			else
    				return "职员";
    		},
    		dataIndex:'ftypeId'
       },{
    		id:'fbillState', 
    		header: '状态', 
    		width: 70, 
    		sortable: true,
    		renderer:status,
    		dataIndex:'fbillState'
       },{
    	    id:'fuserPassword', 
	   		width: 70, 
	   		sortable: true,
	   		hidden:true,
	   		dataIndex:'fuserPassword'
       },{
   	    id:'flag', 
   		hidden:true,
   		dataIndex:'flag'
       },{
    	   header: '团体', 
      	    id:'fSGName', 
       		dataIndex:'fSGName'
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
    	}else if(data == 11){
    		return '已关联';
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
    	id : 'grid',
    	border: false,
    	columnLines:true,
    	loadMask:true,
    	store: store,
    	frame:false,
    	viewConfig: {forceFit:true},
    	monitorResize: true,
    	cm: cm,
    	tbar : tool,
		bbar: pagingToolBar

    });

    // render the grid to the specified div in the page
    
    var displayPanel = new Ext.Viewport({
    	renderTo:'mainDiv',
    	layout:'border',
    	items:[grid]
    });
    Ext.EventManager.onWindowResize(function(){ 
    	grid.getView().refresh();
	});
    grid.on('dblclick', editData);
});

/**************************** 自定义元素 **********************************/

var editYhxxForm = new Ext.form.FormPanel({
     border : false,
	 frame	: true,
	 labelAlign: 'right',
	 buttonAlign: 'center',
	 labelWidth:100,
	 autoScroll:false,
	 containerScroll: true,
	 width: 480,
     height: 140,
     defaults:{anchor:'90%',xtype:'textfield'},
	 items:[
	        {id:'fid',name:'fid',xtype:'hidden'},
	        {
		        xtype:'combo',
		        fieldLabel:'账号类型',
		        id:'ftypeId',
		        hiddenName:'ftypeId',
		        typeAhead: true,
			    triggerAction: 'all',
			    lazyRender:true,
			    mode: 'local',
			    allowBlank: false,
				store: new Ext.data.ArrayStore({
			        id: 0,
			        fields: [
			            'myId',
			            'displayText'
			        ],
			        data: [[0, '团体'], [1, '职员']]
			    }),
			    valueField: 'myId',
			    displayField: 'displayText'
	        },{
		        fieldLabel:'账号',
		        id:'faccount',
		        name:'faccount',
		        regex:/^[0-9a-zA-Z]*$/,
		        regexText:"格式不正确!",
		        maxLength:24,
	            blankText:'该字段不允许为空',
	            allowBlank: false,
	            listeners:{
		            'blur': function(data){
		            	//获取选中职员对象数据
	 					Ext.Ajax.request({
	 						url : getPath() + '/user/checkUserFaccount',
	 						params : {faccount : data.getValue()},
	                		success : function(response, options) {
	                			var obj = Ext.util.JSON.decode(response.responseText);
	                			if(!obj.success){
	                				Ext.MessageBox.show({
	                					title : "提示",
	                					msg : obj.msg,
	                					width : 250,
	                					buttons : Ext.MessageBox.OK,
	                					icon : Ext.MessageBox.INFO
	                				});
	                			}
	                		}
	                	});
		            }
		       }
	        },{
			        xtype:'hidden',
			        fieldLabel:'用户密码',
			        id:'fuserPassword',
			        name:'fuserPassword'
			},{
		        xtype:'hidden',
		        fieldLabel:'状态',
		        id:'fbillState',
		        name:'fbillState'
			},{
		        xtype:'hidden',
		        id:'flag',
		        name:'flag'
			},{
				fieldLabel:'团体',
		        id:'fSGName',
		        name:'fSGName'
			}]
});
var changepwd = new Ext.form.FormPanel({
    baseCls: 'x-plain',
    labelWidth: 100,
    labelAlign: 'right',
    border: true,
	frame: true,
    defaultType: 'textfield',
    items: [{id:'pwdFid',name:'fid',xtype:'hidden'},{
			        fieldLabel: '新密码',
			        id:'startpwd',
			        name: 'password',
			        inputType : 'password',
			        allowBlank:false,
			        blankText:'不能为空'
        	}]
});

/**************************** 自定义元素结束 *******************************/
/**************************** 操作 **********************************/
function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	//通过id获取输入的数据
	var searchCondition = Ext.getCmp("searchCondition").getValue();
	//模糊查询
	store.load({params:{searchName:searchCondition,start:0,limit:LIMIT}});
}

function updatePass(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var row = Ext.getCmp('grid').getSelectionModel().getSelected();
	if(row){
		if(!hyglPassWin){
			hyglPassWin = new Ext.Window({
			    width: 320,
			    height: 120,
			    closable : true,
			    closeAction:'hide',
			    resizable : false,
			    title : '请记住您填写的新密码',
			    expandOnShow:true,
			    bodyStyle:'padding:15px;',
			    buttonAlign:'center',
			    modal : true,
			    items: [changepwd],
			    buttons: [{
			        text: '修 改',
			        handler: function(){
			        	
			            	if(changepwd.form.isValid()){
			            		
								changepwd.form.submit({
										waitTitle : '请稍候',
										waitMsg : '正在修改.......',
										url : getPath() + '/user/updateUserPassword',
										method : 'POST',
										success : function(form, action) {
											Ext.MessageBox.alert('提示', action.result.msg);
											hyglPassWin.hide();
										},
										failure : function(form, action) {
											Ext.MessageBox.alert('警告', action.result.errors);
											changepwd.form.reset();
										}
								}); 
			            	}
			           }
			    },{
			        text: '重 置',
			        handler: function(){
			        	changepwd.form.reset();
			        }
			    }],
			    listeners:{
			    		hide:function(){
			    			changepwd.form.reset();
			    		}
			    }
			});
		}
		changepwd.getForm().findField('pwdFid').setValue(row.get('fid'));
	}else{
		alertWarring("请选择需要修改密码的用户!");return;
	}
	hyglPassWin.show();
}
function editData(row){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(row!= 'add'){
    		 row = Ext.getCmp('grid').getSelectionModel().getSelected();
    		 if(!row){
		    	 alertWarring("请选择需要编辑的用户信息!");return;
		     }
		     var billState = row.get("fbillState");
		     if(billState == 9){
				alertError("该信息已作废，不能修改！");return;
			 }
    	}else{
			//getNumberStore();
    	}
    	var that = this;
         if(!editWin){
            editWin = new Ext.Window({
		        id: 'editWin',
		        layout:'fit',
                width:500,
                height:170,frame:true,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
			    items :[editYhxxForm],
			    buttons:['			默认密码:888888',{
				    text: '保存',
				    handler:function(){
				    	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
				    	if(editYhxxForm.form.isValid()){
				        	editYhxxForm.form.submit({
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
				 }]
            });
		}
        setTitleAndUrl(row, editWin);
	    editYhxxForm.getForm().findField('fbillState').setValue(5);
	    
	    if(row != 'add') {
	    	loadData(row);
		}
		
}
function loadData(row) {
	editYhxxForm.form.setValues({
		fid    					: row.get('fid'),
		faccount    			: row.get('faccount'),
		ftypeId  				: row.get('ftypeId'),
		fuserPassword  	: row.get('fuserPassword'),
		fbillState  			: row.get('fbillState'),
		flag  			: row.get('flag'),
		fSGName  			: row.get('fSGName')
	});
	
}
function setTitleAndUrl(row, win) {
	if(row == 'add') {
 		win.setTitle('增加用户');
 		editYhxxForm.form.url = getPath()+'/user/saveUser';
	} else {
 		win.setTitle('修改用户');
 		editYhxxForm.form.url = getPath()+'/user/updateUser';
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
		    msg: "确认需要更新当前组织状态!",
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
			msg: "请选择需要更新状态的组织信息!",
			width: 250,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}


function gridSubAjax(param,stateId)
{
	
	Ext.Ajax.request({
		url : getPath() + '/user/updateUserStatus',
		params : {fids : param , status : stateId},
		
		success: function (result, request) 
		{
			//var msg = eval(result.responseText);
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