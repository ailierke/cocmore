var LIMIT = 26;var editWin;var mbszWin;var superId;var adsWin;var adsAdd;var adsWin;var replyWin;var qrcodeWin;


//上传图片类型  
var img_reg = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/;

function status(data){
	if(data == 0){
		return '新增';
	}else if(data == 9){
		return '<span style="color:red">作废</span>';
	}else if(data == 12){
		return '开始';
	}else if(data == 13){
		return '<span style="color:red">关闭</span>';
	}else if(data == 14){
		return '启动';
	}else if(data == 15){
		return '<span style="color:blue">待审核</span>';
	}else if(data == 16){
		return '通过';
	}else if(data == 17){
		return '<span style="color:red">拒绝</span>';
	}else if(data == 10){
		return '变更';
	}
} 

var records = new Ext.data.Record.create([
                                          {name: 'fid'},
                                          {name: 'ftheme'},
                                          {name: 'ftheUrl'},
                                          {name: 'fstate'},
                                          {name: 'fcreateTime'},
                                          {name: 'ybasicCity'},
                                          {name: 'ybasicProvince'},
                                          {name: 'ybasicSocialgroups'},
                                          {name: 'fcomment'},
                                          {name: "flag"}
                                       ]);
var store = new Ext.data.Store({
	   	reader:new Ext.data.JsonReader({
	   		root:'list',
	   		totalProperty:'count'
		},records),
		proxy:new Ext.data.HttpProxy({
				disableCaching :false,
				url: getPath() + '/wallActivity/findActivityPage'
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
	items:['上墙主题名称：',{
		xtype:'textfield',
		emptyText:'请输入上墙主题名称',
		id:'searchCondition',
		width:120
	},'-']
});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 11
	},
	success : function(response, options) {
		obj=Ext.util.JSON.decode(response.responseText);
		for(var i=0;i<obj.length;i++){
			tool.add(Ext.util.JSON.decode(obj[i]));tool.add('-');
		}
		tool.doLayout();
	}
});
//======================上墙主题活动查询=======================
Ext.onReady(function(){
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
 // create the data store
    
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
	       	id:'ftheme', 
	   		dataIndex:'ftheme',
	   		width:150,
	   		header: '主题'
       },{
    		id:'ftheUrl', 
    		header: '网址', 
    		dataIndex:'ftheUrl'
       },{
	   		id:'fcreateTime', 
			header: '发起时间', 
			dataIndex:'fcreateTime'
	   },{
		   id:'ybasicProvince',
			header: '省份', 
		   dataIndex:'ybasicProvince',
		   renderer:function(value){
			   if(value){
				   return value.fname;
			   }else{
				   return "";
			   }
		   }
	   },{
		   id:'ybasicCity',
			header: '城市', 
		   dataIndex:'ybasicCity',
		   renderer:function(value){
			   if(value){
				   return value.fname;
			   }else{
				   return "";
			   }
		   }
	   },{
		   id:'ybasicSocialgroups',
		   header:'团体',
		   hidden:true,
		   dataIndex:'ybasicSocialgroups'
	   },{
		   id:'fcomment',
		   header:'备注',
		   dataIndex:'fcomment'
	   },{
		   id:'fstate',
		   header:'状态',
		   dataIndex:'fstate',
		   renderer:status
	   },{
		   id:'flag',
		   header:'自动增长',
		   dataIndex:'flag',
		   hidden:true
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
    
    if(getUser()[2]==1){
    	new Ext.Viewport({
        	renderTo:'mainDiv',
        	id:'viewport',
        	layout:'border',
        	width:300,
        	items:[treePanelTuanti,grid]
        });
    }else{
    	new Ext.Viewport({
        	renderTo:'mainDiv',
        	id:'viewport',
        	layout:'border',
        	items:[grid]
        });
    }
    Ext.EventManager.onWindowResize(function(){ 
    	grid.getView().refresh() 
	});
	grid.on('dblclick', openUrl);
});

/**************************** 自定义元素 **********************************/
//省份
var provinceStore = new Ext.data.Store({
	        proxy : new Ext.data.HttpProxy({
	                disableCaching : false,
	                method : 'post',
	                url : getPath() + '/province/findAllProvince'
            }),
	        reader : new Ext.data.JsonReader({root : 'obj'}, [{name : 'fid'}, {name : 'fname'}])
	});
provinceStore.load();
var YBasicProvince = new Ext.form.ComboBox({
			fieldLabel : '省份',
			id : 'ybasicProvince',
			hiddenName : 'YBasicProvince.fid',
			typeAhead : true,
			triggerAction : 'all',
			lazyRender : true,
			mode : 'local',
			store : provinceStore,
			valueField : 'fid',
			displayField : 'fname',
			emptyText : '请选择省份',
			listeners:{
                select:function(groupSelect){
                	cityStore.load({params:{id:groupSelect.getValue()}});
                	YBasicCity.focus();
        		}
            }
		});
//城市
var cityStore = new Ext.data.Store({
	        proxy : new Ext.data.HttpProxy({
	                disableCaching : false,
	                method : 'post',
	                url : getPath() + '/city/findCityHql'
            }),
	        reader : new Ext.data.JsonReader({root : 'obj'}, [{name : 'fid'}, {name : 'fname'}])
	});
var YBasicCity = new Ext.form.ComboBox({
			fieldLabel : '城市',
			id : 'ybasicCity',
			hiddenName : 'YBasicCity.fid',
			typeAhead : true,
			triggerAction : 'all',
			lazyRender : true,
			mode : 'local',
			store : cityStore,
			valueField : 'fid',
			displayField : 'fname',
			emptyText : '请选择城市'
		});

//==========================上墙主题活动新增===============================
var editSqztForm = new Ext.form.FormPanel({
     border : false,
	 frame	: true,
	 labelAlign: 'left',
	 buttonAlign: 'center',
	 labelWidth:70,
	 autoScroll:false,
	 containerScroll: true,
	 defaults:{anchor:'93%',xtype:'textfield'},
	 items:[{xtype:'hidden',id:'fid',name:'fid'},
		        {
			        fieldLabel:'主题',
			        id:'ftheme',
			        name:'ftheme',
			        maxLength:50,
			        blankText:'该字段不允许为空',
			        allowBlank: false
			    },{
			        fieldLabel:'网址',
			        id:'ftheUrl',
			        name:'ftheUrl',
			        maxLength:100,
			        blankText:'该字段不允许为空',
			        allowBlank: false
			    },
			    YBasicProvince,
			    YBasicCity,
			    {
			        xtype:'datefield',
					fieldLabel:'发起时间',
					id:'fcreateTime',
					name:'fcreateTime',
					vtype : 'daterange',
					format:"Y-m-d H:i:s"
			    },{
			        xtype:'hidden',
			        fieldLabel:'状态',
			        id:'fstate',
			        name:'fstate'
			    },{
			    	xtype:'hidden',
			    	fieldLabel:'团体',
			    	id:'ybasicSocialgroups',
			    	name:'YBasicSocialgroups.fid'
			    },{
			    	xtype:'hidden',
			    	fieldLabel:'自动增长',
			    	id:'flag',
			    	name:'flag'
			    }]
});

/**************************** 自定义元素结束 *******************************/
/**************************** 操作 **********************************/

//function getNumberStore(){
//	Ext.Ajax.request({
//		url:getPath() + '/base/position!doNotNeedSessionAndSecurity_getFNumber.ad',
//		success:function(response, options) {
//			Ext.getCmp("fnumber").setValue(response.responseText);
//		}
//	});
//}

//条件查询
function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	//通过id获取输入的数据
	var searchCondition = Ext.getCmp("searchCondition").getValue();
	//模糊查询
	store.load({params:{theme:searchCondition,groupId:window.parent.groupId,start:0,limit:LIMIT}});
}

function editData(row){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
		var detail = row;
		if(row!='add' && row!='detail'){
		     row = Ext.getCmp("grid").getSelectionModel().getSelected();
		     if(!row){
		    	 alertWarring("请选择需要编辑的上墙主题!");return;
		     }
		     var billState = row.get("fbillState");
		     if(billState == 14){
					alertError("该信息只能做关闭操作！");return;
			 }else if(billState == 13){
					alertError("该信息只能做作废操作！");return;
			 }else if(billState == 9){
					alertError("该信息已作废，无法进行操作！");return;
			 }
		}else if(row =='detail'){
			row = Ext.getCmp("grid").getSelectionModel().getSelected();
		}else{
//			getNumberStore();
			Ext.getCmp('ybasicSocialgroups').setValue(window.parent.groupId);
			Ext.getCmp('ftheUrl').setValue(getPath()+"webApp/shangqiang.html");
		}
    	this.row = row;
    	var that = this;
         if(!editWin){
            editWin = new Ext.Window({
		        id: 'editWin',
		        layout:'fit',
                width:360,
                height:220,
                frame:true,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
				border : false,
			    items :[editSqztForm],
			    buttons:[{
					    text: '保存',
					    handler:function(){
					          if(editSqztForm.form.isValid()){
					        	  editSqztForm.form.submit({
					                   method:'POST',
					                   waitMsg:'正在提交.....',
									   waitTitle:'请稍等',
					                   success:function(form, action){
					                	   	 Ext.getCmp('grid').getStore().reload();
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
					    	  Ext.getCmp("editWin").hide();
					      }	 
				}],
			    listeners:{
			    	hide:function(){
			    		editSqztForm.form.reset();
					}
			    }
            });
		}
        setTitleAndUrl(row, editWin);
        //设置新增状态
        Ext.getCmp('fstate').setValue(0);
	    if(row != 'add') {
	    	if(detail && detail == "detail"){
	    		var tool = Ext.getCmp('editWin').buttons;
	    		for(var i =0;i<tool.length;i++){
	    			tool[i].setVisible(false);
	    		}
	    		Ext.getCmp('editWin').doLayout(); 
	    		Ext.getCmp('editWin').setTitle("查看详情");
	    	}else{
	    		var tool = Ext.getCmp('editWin').buttons;
				for(var i =0;i<tool.length;i++){
					tool[i].setVisible(true);
				}
				Ext.getCmp('editWin').doLayout(); 
	    	}
	      	loadData(row);
		}		
	}
//获取上墙主题活动修改信息
function loadData(row) {
	if(row.get('ybasicProvince') && row.get('ybasicProvince').fid){
		cityStore.load({
			params:{id:row.get('ybasicProvince').fid},
			callback:function(){
				editSqztForm.form.setValues({
					ybasicProvince				: row.get('ybasicProvince').fid
				});
				if(row.get('ybasicCity') && row.get('ybasicCity').fid){
					editSqztForm.form.setValues({
						ybasicCity					: row.get('ybasicCity').fid
					});
				}
			}
		});
	}
	editSqztForm.form.setValues({
				fid								: row.get('fid'),
				ftheme  						: row.get('ftheme'),
				ftheUrl  						: row.get('ftheUrl'),
				fstate							: 10,
				fcreateTime				: row.get('fcreateTime'),
				ybasicSocialgroups	: row.get('ybasicSocialgroups').fid,
				flag				: row.get('flag')
	});
}

//增加和修改上墙主题活动的请求
function setTitleAndUrl(row, win) {
	if(row == 'add') {
 		win.setTitle('增加上墙主题活动');
 		editSqztForm.form.url = getPath() + '/wallActivity/saveWallActivity';
	} else {
 		win.setTitle('修改上墙主题活动');
 		editSqztForm.form.url = getPath() + '/wallActivity/updateWallActivity';
	}
	win.show();
}

//作废状态填写原因
function showDiscarded(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	var selectRow = Ext.getCmp("grid").getSelectionModel().getSelected();
	if(selectRow){
		var fid= selectRow.get("fid");
		var state = selectRow.get('fbillState');
		if(state == 9){
			 alertError("该信息已作废，无法操作！");return;
		}else if(state == 14){
			 alertError("该信息只能做关闭操作！");return;
		}else if(state == 0 || state == 10){
			 alertError("该信息只能做启动操作！");return;
		}
		Ext.MessageBox.prompt("提示", "请输入将这条信息作废的原因！",function(bu,txt){
			if(txt && 0<txt.length<125){
				Ext.Ajax.request({
					url : getPath()+'wallActivity/Invalid',
					params : {fid : fid ,fbillState : 9,comment:txt},
					
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

//=====================修改================上墙主题活动的状态=======================
function updateActivityState(stateId){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  var fids=new Array();
		  var status = new Array();
		  for(var i=0;i<records.length;i++){
				  fids[i]=records[i].get('fid');
				  status[i] = records[i].get('fbillState');
				  if(status[i] == stateId){
						 alertError("状态一致，勿重复操作！");return;
				  }else if(stateId != 9 && status[i] == 13){
						 alertError("该信息只能做作废操作！");return;
				  }else if(status[i] != 13 && status[i] == 14){
						 alertError("该信息只能做关闭操作！");return;
				  }else if(stateId != 14 && (status[i] == 0 || status[i] == 10)){
						 alertError("该信息只能做启动操作！");return;
				  }
		 }
		 Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认要更新上墙主题状态!",
		    width:250,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			updateActivityAjax(fids,stateId);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}
	else
	{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择上墙主题!",
			width: 150,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}

function updateActivityAjax(param,stateId)
{
	Ext.Ajax.request({
		url : getPath() + '/wallActivity/updateState',
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

//=====================删除=================上墙主题活动=======================
function deleteActivityState(){
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
		    msg: "确认要删除上墙主题!",
		    width:250,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			deleteActivityAjax(fids);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}
	else
	{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择上墙主题!",
			width: 150,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}

function deleteActivityAjax(param)
{
	Ext.Ajax.request({
		url : getPath() + '/wallActivity/deleteWallActivity',
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

var editAdsForm = new Ext.form.FormPanel({
     border : false,
     fileUpload : true,
     waitMsgTarget : true,
     enctype : 'multipart/form-data',
	 frame	: true,
	 labelAlign: 'left',
	 buttonAlign: 'center',
	 labelWidth:60,
	 autoScroll:false,
	 containerScroll: true,
	 width: 680,
     height: 320,
     defaults:{anchor:'95%',xtype:'textfield'},
	 items:[{xtype:'hidden',id:'Adsfid',name:'fid'},
	        new Ext.ux.form.FileUploadField({
	        		fieldLabel:'图片',
    		    	id : 'fimage',
    		    	name : 'file',
    		    	emptyText: 'Select an image',
    				width : 150,
    				buttonText: '浏览....'
    			}) ,{
		 			id:'ftitle',
			        fieldLabel:'标题',
			        maxLength:18,
			        name:'ftitle'
		 		},{
		 			id:'fdescribe',
			        fieldLabel:'描述',
			        name:'fdescribe'
		 		},{
		 			id:'fcreationTime',
			        fieldLabel:'创建时间',
			        xtype:'hidden',
			        name:'fcreationTime'
		 		},{
		 			id:'ywallactivity',
			        fieldLabel:'所属活动',
			        xtype:'hidden',
			        name:'YWallactivity.fid'
		 		},{
		 			id:'flag',
			        fieldLabel:'自动增长',
			        xtype:'hidden',
			        name:'flag'
		 		}]
});
var adsRecords = new Ext.data.Record.create([
                                          {name: 'fid'},
                                          {name: 'fimage'},
                                          {name: 'ftitle'},
                                          {name: 'fdescribe'},
                                          {name: 'fcreationTime'},
                                          {name: 'ywallactivity'},
                                          {name: 'flag'}
                                       ]);
var adsStore = new Ext.data.Store({
	   	reader:new Ext.data.JsonReader({
	   		root:'list',
	   		totalProperty:'count'
		},adsRecords),
		proxy:new Ext.data.HttpProxy({
				disableCaching :false,
				url: getPath() + '/sadvertising/findSadvertisingPage'
		}),
		remoteStore : true, // 是否远程调用数据
		remoteSort : true   // 是否远程排序
});
adsStore.on("beforeload", function() {
	var row = Ext.getCmp("grid").getSelectionModel().getSelected();
	this.baseParams = {
			activityId : row?row.get('fid'):0
	};
});
var adsCm = new Ext.grid.ColumnModel([
                               	new Ext.grid.RowNumberer(),
                               	{id:'fid',hidden:true},
                               	{
                           	       	id:'fimage', 
                           	   		dataIndex:'fimage',
                           	   		width:150,
                           	   		header: '图片'
                                },{
                               		id:'ftitle', 
                               		header: '标题', 
                               		dataIndex:'ftitle'
                               },{
                            	   id:'fdescribe',
                            	   dataIndex:'fdescribe',
                            	   header:'描述'
                               },{
                           	   		id:'fcreationTime', 
                           			header: '创建时间', 
                           			dataIndex:'fcreationTime'
                           	   	},{
                           	   		header: '所属团体', 
                           	   		id:'ywallactivity',
                           	   		dataIndex:'ywallactivity',
                           	   		hidden:true
                           	   	},{
                           	   		id:'flag',
                           	   		hidden:true,
                           	   		dataIndex:'flag'
                           	   	}
                               ]);
var adsPagingToolBar = new Ext.PagingToolbar({
	pageSize:10,
	store:adsStore,
	displayInfo: true,
	displayMsg: '当前显示第&nbsp;{0}&nbsp;到&nbsp;{1}&nbsp;条,共&nbsp;{2}&nbsp;条',
	emptyMsg: "没有记录",
	plugins: new Ext.ux.ProgressBarPager()
}); 

// create the Grid
var adsGrid = new Ext.grid.GridPanel({
	id:'adsGrid',
	border: false,
	columnLines:true,
	loadMask:true,
	store: adsStore,
	frame:false,
	viewConfig: {forceFit:true},
	monitorResize: true,
	cm: adsCm,
	tbar: new Ext.Toolbar({ //工具栏
		items:[{
						text: '&nbsp;删&nbsp;除&nbsp;',
						enableToggle: false,
						iconCls:'common_delete',
						handler:function(){
							deleteSadvertisingState();
						} 
					}
		]
	}),
	bbar: adsPagingToolBar
});

//=====================删除=================活动广告=======================
function deleteSadvertisingState(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	if(Ext.getCmp('adsGrid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('adsGrid').getSelectionModel().getSelections();
		  var fids=new Array();
		  for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
		  }
		 Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认要删除活动广告!",
		    width:250,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			deleteSadvertisingAjax(fids);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}
	else
	{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择活动广告!",
			width: 150,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}

function deleteSadvertisingAjax(param)
{
	Ext.Ajax.request({
		url : getPath() + '/sadvertising/deleteWallAd',
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
			Ext.getCmp("adsGrid").store.reload();
		},
		failure: function ( result, request) {
			alertError("系统运行超时或执行失败");
		}
	});
}

//添加广告信息
function addAds(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	var row = Ext.getCmp("grid").getSelectionModel().getSelected();
	if(!row){
   	 	alertWarring("请选择上墙主题!");return;
    }
	var billState = row.get("fbillState");
    if(billState == 14){
		alertError("该信息只能做关闭操作！");return;
	}else if(billState == 13){
		alertError("该信息只能做作废操作！");return;
	}else if(billState == 9){
		alertError("该信息已作废，无法进行操作！");return;
	}
	if(!adsAdd){
		adsAdd = new Ext.Window({
	        id: 'adsAdd',
	        title:'新增广告',
	        layout:'fit',
            width:400,
            height:150,
            frame:true,
            plain: true,
	        closeAction:'hide',
			maximizable:false,
			resizable:false,
			modal:true,
			border : false,
		    items :[editAdsForm],
		    buttons:[{
			    text: '保存',
			    handler:function(){		
			    	
			    	var logourl = Ext.getCmp('fimage').getValue();
					if(logourl !="" && !img_reg.test(logourl)){
						alertWarring("文件格式不正确！");return;
					}
			          if(editAdsForm.form.isValid()){
			        	  editAdsForm.form.submit({
			                   method:'POST',
			                   waitMsg:'正在提交.....',
							   waitTitle:'请稍等',
			                   success:function(form, action){
				                	 Ext.getCmp("adsAdd").hide();
			                   },
			                   failure:function(form, action){
					                      alertWarring('失败了..');
				                   }
				             });   
			          }else{
			               alertWarring("内容填写不完整");
			          }
			     }
			 },{
				 text:'取消',
			      handler:function(){
			    	  Ext.getCmp('adsAdd').hide();
			      }
			 }],
		    listeners:{hide:function(){editAdsForm.form.reset();}}
        });
	}
	var date = new Date();  // 得到系统日期
    var dateString = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
    editAdsForm.getForm().findField('fcreationTime').setValue(dateString);
    
	Ext.getCmp('ywallactivity').setValue(row.get('fid'));
	
	editAdsForm.form.url = getPath() + '/sadvertising/saveWallAd';
	
	adsAdd.show();
}

//查询广告信息
function seachAds(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	var row = Ext.getCmp("grid").getSelectionModel().getSelected();
	if(!row){
   	 	alertWarring("请选择上墙主题!");return;
    }
	if(!adsWin){
		adsWin = new Ext.Window({
	        id: 'adsWin',
	        title:'广告信息',
	        layout:'fit',
            width:700,
            height:450,
            frame:true,
            plain: true,
	        closeAction:'hide',
			maximizable:false,
			resizable:false,
			modal:true,
			border : false,
		    items :[adsGrid]
        });
	}
	adsStore.load({params:{activityId:row.get('fid'),start:0,limit:10}});
	adsWin.show();
}


	var replyRecords = new Ext.data.Record.create([
                                             {name: 'fid'},
                                             {name: 'fcontent'},
                                             {name: 'fsum'},
                                             {name: 'fstate'},
                                             {name: 'freplyTime'},
                                             {name: 'fcommerceId'},
                                             {name: 'ybasicMember'},
                                             {name: 'ywallactivity'},
                                             {name: 'flag'}
                                          ]);
   var replyStore = new Ext.data.Store({
   	   	reader:new Ext.data.JsonReader({
   	   		root:'list',
   	   		totalProperty:'count'
   		},replyRecords),
   		proxy:new Ext.data.HttpProxy({
   				disableCaching :false,
   				url: getPath() + '/wallReply/findReplyPage'
   		}),
   		remoteStore : true, // 是否远程调用数据
   		remoteSort : true   // 是否远程排序
   });
   replyStore.on("beforeload", function(currentStore, options) {
		var row = Ext.getCmp("grid").getSelectionModel().getSelected();
		this.baseParams = {
				activityId : row?row.get('fid'):0
		};
	});
   var replyCm = new Ext.grid.ColumnModel([
                                  	new Ext.grid.RowNumberer(),
                                  	{id:'fid',hidden:true},
                                  	{
                                  		header: '所属活动',
                                  		id:'ywallactivity',
                                  		dataIndex:'ywallactivity',
                                  		renderer:function(value){
                                  			return value.ftheme;
                                  		}
                                   },{
                                  		header: '发布人',
                                  		id:'ybasicMember',
                                  		dataIndex:'ybasicMember',
                                  		renderer:function(value){
                                  			return value.fname;
                                  		}
                                   },{
                              	       	id:'fcontent', 
                              	   		dataIndex:'fcontent',
                              	   		width:150,
                              	   		header: '内容'
                                   },{
                                  		id:'freplyTime', 
                                  		header: '回复时间', 
                                  		dataIndex:'freplyTime'
                                  },{
	                               	   id:'fcommerceId',
	                               	   dataIndex:'fcommerceId',
	                               	   hidden:true,
	                               	   header:'商会'
                                  },{
                              	   		id:'fsum', 
                              			header: '回复总条数', 
                              			dataIndex:'fsum'
                              	  },{
                              	   		id:'fstate',
                              	   		dataIndex:'fstate',
                              	   		header:'状态',
                              	   		renderer:status
                              	  },{
                              	   		id:'flag',
                              	   		dataIndex:'flag',
                              	   		hidden:true
                              	   }
                                  ]);
   var replyPagingToolBar = new Ext.PagingToolbar({
   	pageSize:10,
   	store:replyStore,
   	displayInfo: true,
   	displayMsg: '当前显示第&nbsp;{0}&nbsp;到&nbsp;{1}&nbsp;条,共&nbsp;{2}&nbsp;条',
   	emptyMsg: "没有记录",
   	plugins: new Ext.ux.ProgressBarPager()
   }); 

   // create the Grid
var replyGrid = new Ext.grid.GridPanel({
   	id:'replyGrid',
   	border: false,
   	columnLines:true,
   	loadMask:true,
   	store: replyStore,
   	frame:false,
   	viewConfig: {forceFit:true},
   	monitorResize: true,
   	cm: replyCm,
   	tbar: new Ext.Toolbar({ //工具栏
   		items:['状态：',{
   			xtype:'combo',
   			width:100,
			id:'searchReply', 
			typeAhead: true,
			triggerAction: 'all',
			lazyRender:true,
		    mode: 'local', 
		  	store: new Ext.data.ArrayStore({
		          fields: [
		              'myId',
		              'displayText'
		          ],
		          data: [['','全部'], [15, '待审核'],[16, '通过'],[17, '拒绝']]
		      }),
		     valueField: 'myId',
		     displayField: 'displayText',
		     renderer:function(value){
		      	return value.displayText;
		     },
   			listeners : {
   				select: function() {
   					replyStore.load({params:{activityId:Ext.getCmp("grid").getSelectionModel().getSelected()?Ext.getCmp("grid").getSelectionModel().getSelected().get('fid'):0,state:this.getValue()}});
   				}
   			} 
   		},'-',{
   			text: '&nbsp;通&nbsp;过&nbsp;',
   			enableToggle: false,
   			iconCls:'common_past',
   			handler: function(){
   				updateReplyState(16);
   			}
   		},'-',{
   			text: '&nbsp;拒&nbsp;绝&nbsp;',
   			enableToggle: false,
   			iconCls:'common_nopast',
   			handler: function(){
   				updateReplyState(17);
   			}
   		},'-',{
   			text: '&nbsp;删&nbsp;除&nbsp;',
   			enableToggle: false,
   			iconCls:'common_delete',
   			handler: function(){
   				deleteReplyState();
   			}
   		}]
   	}),
   	bbar: replyPagingToolBar
   });

//查询回复
function seachReply(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	var row = Ext.getCmp("grid").getSelectionModel().getSelected();
	if(!row){
   	 	alertWarring("请选择上墙主题!");return;
    }
	if(!replyWin){
		replyWin = new Ext.Window({
			title:'上墙主题回复',
	        id: 'replyWin',
	        layout:'fit',
            width:700,
            height:450,
            frame:true,
            plain: true,
	        closeAction:'hide',
			maximizable:false,
			resizable:false,
			modal:true,
			border : false,
		    items :[replyGrid]
        });
	}
	replyStore.load({params:{activityId:row.get('fid'),start:0,limit:10}});
	replyWin.show();
}

//=====================修改================活动回复的状态=======================
function updateReplyState(stateId){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	if(Ext.getCmp('replyGrid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('replyGrid').getSelectionModel().getSelections();
		  var fids=new Array();
		  for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
		  }
		 Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认要更新活动回复状态!",
		    width:250,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			updateReplyAjax(fids,stateId);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}
	else
	{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择活动回复!",
			width: 150,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}

function updateReplyAjax(param,stateId)
{
	Ext.Ajax.request({
		url : getPath() + '/wallReply/updateState',
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
			Ext.getCmp("replyGrid").store.reload();
		},
		failure: function ( result, request) {
			alertError("系统运行超时或执行失败");
		}
	});
}

//=====================删除=================活动回复=======================
function deleteReplyState(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	if(Ext.getCmp('replyGrid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('replyGrid').getSelectionModel().getSelections();
		  var fids=new Array();
		  for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
		  }
		 Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认要删除活动回复!",
		    width:250,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			deleteReplyAjax(fids);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}
	else
	{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择活动回复!",
			width: 150,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}

function deleteReplyAjax(param)
{
	Ext.Ajax.request({
		url : getPath() + '/wallReply/deleteWallReply',
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
			Ext.getCmp("replyGrid").store.reload();
		},
		failure: function ( result, request) {
			alertError("系统运行超时或执行失败");
		}
	});
}

function replyStatu(stateId){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	if(Ext.getCmp('replyGrid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('replyGrid').getSelectionModel().getSelections();
		  var fids=new Array();
		  for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
		  }
		  Ext.Ajax.request({
				url : getPath() + 'base/industry!doNotNeedSessionAndSecurity_updateState.ad',
				params : {fids : fids , stateId : stateId},
				
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
	}else{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择回复!",
			width: 150,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}

function openUrl(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	var row = Ext.getCmp("grid").getSelectionModel().getSelected();
	if(!row){
   	 	alertWarring("请选择上墙主题!");return;
    }
	if(row.get("fstate")==14){
		var tmp=window.open("about:blank","","fullscreen=1");
		tmp.moveTo(0,0); 
		tmp.resizeTo(screen.width+20,screen.height); 
		tmp.focus(); 
		tmp.location=getPath()+'webApp/shangqiang.html?id='+row.get('fid'); 
	}else{
		alertWarring("请双击启动状态的活动!");return;
	}
}

//查看二维码
function searchQrCode(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	var row = Ext.getCmp("grid").getSelectionModel().getSelected();
	if(!row){
   	 	alertWarring("请选择上墙主题!");return;
    }
	var url = getPath()+'webApp/SeatNumber.html?id='+row.get('fid');
	//20150122深圳四川商会活动签到定制
	//url = getPath()+'webApp/SeatNumber1.html?id='+row.get('fid');
	var out, i, len, c; 
	out = ""; 
	len = url.length; 
	for (i = 0; i < len; i++) { 
		c = url.charCodeAt(i); 
		if ((c >= 0x0001) && (c <= 0x007F)) { 
			out += url.charAt(i); 
		} else if (c > 0x07FF) { 
			out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F)); 
			out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F)); 
			out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F)); 
		} else { 
			out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F)); 
			out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F)); 
		} 
	} 
	if(!qrcodeWin){
		qrcodeWin = new Ext.Window({
			title:'查看二维码',
	        id: 'qrcodeWin',
	        layout:'fit',
            width:270,
            height:290,
            frame:true,
            plain: true,
	        closeAction:'hide',
			maximizable:false,
			resizable:false,
			modal:true,
			border : false,
		    items :[new Ext.BoxComponent({
		        id : 'qrcode',
		        autoEl : {
		            tag : 'div',
		            complete : 'off',
		            id : 'qrcodeImg'
		        }
		    })],
		    listeners:{
		    	hide:function(){
		    		$('#qrcodeImg').html("");
		    	}
    	 }
        });
	}
	qrcodeWin.show();
	$('#qrcodeImg').qrcode(out); 
}