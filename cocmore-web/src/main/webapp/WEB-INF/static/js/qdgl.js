var LIMIT = 26;var editWin;var mbszWin;var superId;var excel;var SeatWin;

var records = new Ext.data.Record.create([
                                          {name: 'fid'},
                                          {name: 'userName'},
                                          {name: 'tel'},
                                          {name: 'setNumber'},
                                          {name: 'groupName'},
                                          {name: 'ywallactivity'}
                                       ]);
var store = new Ext.data.Store({
	   	reader:new Ext.data.JsonReader({
	   		root:'list',
	   		totalProperty:'count'
		},records),
		proxy:new Ext.data.HttpProxy({
				disableCaching :false,
				url: getPath() + 'wallActivity/findAllJionWallActivitypeople'
		}),
		remoteStore : true, // 是否远程调用数据
		remoteSort : true   // 是否远程排序
});
//上墙主题
var ywallactivityStore = new Ext.data.Store({
	        proxy : new Ext.data.HttpProxy({
	                disableCaching : false,
	                method : 'post',
	                url : getPath() + '/wallActivity/findActivityPage'
          }),
	        reader : new Ext.data.JsonReader({root : 'list'}, [{name : 'fid'}, {name : 'ftheme'}])
	});
ywallactivityStore.on("beforeload", function() {
	this.baseParams = {
			groupId : window.parent.groupId
	};
});
ywallactivityStore.load();

var tool = new Ext.Toolbar({ //工具栏
	items:['姓名:',
	       {xtype:'textfield',
	    	   emptyText:'请输入姓名',
	    	   id:'searchCondition',
	    	   width:150
    	   },'-','活动主题:',{
    		   	xtype:'combo',
    		    fieldLabel : '上墙主题',
				id : 'ztid',
				typeAhead : true,
				triggerAction : 'all',
				lazyRender : true,
				mode : 'local',
				store : ywallactivityStore,
				valueField : 'fid',
				displayField : 'ftheme'
			}]
});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 33
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
 // create the data store
    
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
	       	id:'userName', 
	   		dataIndex:'userName',
	   		width:150,
	   		header: '姓名'
       },{
    		id:'tel', 
    		header: '电话', 
    		dataIndex:'tel',
    		maxLength:18
       },{
	       	id:'', 
	   		dataIndex:'setNumber',
	   		width:150,
	   		header: '座位号'
      },{
	   		id:'ywallactivity', 
	   		header: '所属上墙活动', 
	   		dataIndex:'ywallactivity',
	   		renderer:function(value){
	   			return value.ftheme;
	   		}
      },{
    	  id:'groupName',
    	  dataIndex:'groupName',
    	  header:'团体名称'
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
	grid.on('dblclick', editData);
});

/**************************** 自定义元素 **********************************/
//excel模板设置
function mbsz(){
	var export_url = getPath() + '/wallActivity/download';
    window.location.href = export_url;
}
//导入
/*导入数据功能*/
function showImportExcel(){
	if(Ext.getCmp("ztid").getValue()==""){
		alertWarring("请先选择主题活动信息");
	}else{
		if(!excel){
			excel = new Ext.Window({
		        id: 'showImportExcel',
		        layout:'fit',
	            width:300,
	            height:140,
	            frame:true,
	            plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
				border:false,
			    items :[ importExcelForm ]
	        });
		}
		Ext.getCmp('showImportExcel').show();
	}
	
} 
var importExcelForm= new Ext.form.FormPanel({
	frame: true,
	fileUpload: true, 
	waitMsgTarget : true,
	labelAlign : 'left',
	enctype:'multipart/form-data', 
	bodyStyle : 'padding:5px 0px 0',
	buttonAlign : 'center',
	id : 'uploadFileLogo',
	items : [
		new Ext.ux.form.FileUploadField({
	    	id : 'flshlogo',
	    	name : 'file',
	    	emptyText: '选择xls文件',
			width : 150,
			buttonText: '浏览...'
		})
	],
    buttons:[{
	    text: '上传',
	    handler:function(){
	    	var flshlogo = Ext.getCmp('flshlogo').getValue();
    		if(Ext.getCmp('uploadFileLogo').getForm().isValid() &&  ( /\.(xls)$/.test(flshlogo) || /\.(xlsx)$/.test(flshlogo)  ) ){       
    			Ext.getCmp('uploadFileLogo').getForm().submit({
		                   method:'POST',
		                   waitMsg:'正在提交.....',
						   waitTitle:'请稍等',
						   url : getPath() + 'wallActivity/importJoinActivity',
		                   success:function(form, action){
		                	    location.reload();
		                	    Ext.getCmp("showImportExcel").hide();
		                   },
		                   params:{activityId:Ext.getCmp("ztid").getValue()},
		                   failure:function(form, action){
		                	   alertWarring(action.result.msg);
		                   }      
		              });  
			}else{
				alertWarring("请选择后缀名为xls的文件");
			}
		}
    }] 
});

/*******导出数据******/
function exprotData(){
	var vExportContent = Ext.getCmp('grid').getExcelXml();  
	if (Ext.isIE9||Ext.isIE8||Ext.isIE6 || Ext.isIE7 || Ext.isSafari|| Ext.isSafari2 || Ext.isSafari3){ //判断浏览器
        var fd = Ext.get('frmDummy');
        if (!fd) {
            fd = Ext.DomHelper.append(
                    Ext.getBody(), {
                        tag : 'form',
                        method : 'post',
                        id : 'frmDummy',
                        action : getPath()+'/exportUrl.jsp',
                        target : '_blank',
                        name : 'frmDummy',
                        cls : 'x-hidden',
                        cn : [ {
                            tag : 'input',
                            name : 'exportContent',
                            id : 'exportContent',
                            type : 'hidden'
                        } ]
                    }, true);
            
        }
        fd.child('#exportContent').set( {
            value : vExportContent
        });
        fd.dom.submit();
    } else {
    	document.location = 'data:application/vnd.ms-excel;base64,' + Base64.encode(vExportContent);
	}
}

var YWallactivity = new Ext.form.ComboBox({
			fieldLabel : '上墙主题',
			id : 'ywallactivity',
			hiddenName : 'YWallactivity.fid',
			typeAhead : true,
			triggerAction : 'all',
			lazyRender : true,
			mode : 'local',
			store : ywallactivityStore,
			valueField : 'fid',
			displayField : 'ftheme',
	        blankText:'该字段不允许为空',
	        allowBlank: false
		});

var editDbxxForm = new Ext.form.FormPanel({
     border : false,
	 frame	: true,
	 labelAlign: 'left',
	 buttonAlign: 'center',
	 labelWidth:80,
	 autoScroll:false,
	 containerScroll: true,
	 defaults:{anchor:'93%',xtype:'textfield'},
	 items:[{xtype:'hidden',id:'fid',name:'fid'},
		        {
			        fieldLabel:'姓名',
			        id:'userName',
			        name:'userName',
					maxLength:25,
		            blankText:'该字段不允许为空',
		            allowBlank: false
		        },{
			        fieldLabel:'联系电话',
			        id:'tel',
			        name:'tel',
			        regex : /^((1+\d{10})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/
		        },{
		        	xtype:'numberfield',
			        fieldLabel:'座位号',
			        id:'setNumber',
			        name:'setNumber',
		            blankText:'该字段不允许为空',
		            allowBlank: false
		        },{
			        fieldLabel:'团体名称',
			        maxLength:125,
			        id:'groupName',
			        name:'groupName'
		        },YWallactivity]
});


var SeatForm = new Ext.form.FormPanel({
     border : false,
	 frame	: true,
	 labelAlign: 'left',
	 buttonAlign: 'right',
	 labelWidth:70,
	 
	 autoScroll:false,
	 containerScroll: true,
	 items:[{
		 		fieldLabel:'起始范围',
	 			xtype:'numberfield',
		        id:'startNum',
		        name:'startNum',
	            blankText:'该字段不允许为空',
	            allowBlank: false
	        },{
	        	fieldLabel:'结束范围',
	        	xtype:'numberfield',
		        id:'endNum',
		        name:'endNum',
	            blankText:'该字段不允许为空',
	            allowBlank: false
	        }]
});
/**************************** 自定义元素结束 *******************************/
/**************************** 操作 **********************************/
//条件查询
function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	//通过id获取输入的数据
	var searchCondition = Ext.getCmp("searchCondition").getValue();
	var ztid = Ext.getCmp("ztid").getValue();

	store.on("beforeload", function() {
		this.baseParams = {
				ztid : ztid,
				searchCondition:searchCondition
		};
	});
	//模糊查询
	store.load({params:{start:0,limit:LIMIT}});
}

function editData(row){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}

		if(row!='add'){
		     row = Ext.getCmp("grid").getSelectionModel().getSelected();
		     if(!row){
		    	 alertWarring("请选择需要编辑的人员信息!");return;
		     }
		}else{
//			getNumberStore();
//			Ext.getCmp('ybasicSocialgroups').setValue(window.parent.groupId);
		}
    	this.row = row;
    	var that = this;
         if(!editWin){
            editWin = new Ext.Window({
		        id: 'editWin',
		        layout:'fit',
                width:360,
                height:210,
                frame:true,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
				border : false,
			    items :[editDbxxForm],
			    buttons:[{
					    text: '保存',
					    handler:function(){
					          if(editDbxxForm.form.isValid()){
					        	  editDbxxForm.form.submit({
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
			    	hide:function(){editDbxxForm.form.reset();}
			    }
            });
		}
        setTitleAndUrl(row, editWin);
        
        
	    if(row != 'add') {
	      	loadData(row);
		}		
	}

function loadData(row) {
	editDbxxForm.form.setValues({
		fid				: row.get('fid'),
		userName  		: row.get('userName'),
		tel  			: row.get('tel'),
		setNumber		: row.get('setNumber'),
		groupName		: row.get('groupName'),
		ywallactivity	: row.get('ywallactivity').fid
	});
}
function setTitleAndUrl(row, win) {
	if(row == 'add') {
 		win.setTitle('增加人员信息');
 		editDbxxForm.form.url = getPath() + '/wallActivity/saveJoinActivity';
	} else {
 		win.setTitle('修改人员信息');
 		editDbxxForm.form.url = getPath() + '/wallActivity/updateJoinActivity';
	}
	win.show();
}

//================================删除=======================================
function deleteContent(){
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
		    msg: "确认要删除当前人员信息？!",
		    width:300,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			gridSubAjax(fids);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}else{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择需要删除的人员信息!",
			width: 250,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}
function gridSubAjax(param){
	Ext.Ajax.request({
		url : getPath() + 'wallActivity/deleteJoinActivity',
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
/*填写随机座位号的范围，改变签到人员的座位号*/
function setSeat(){
	if(Ext.getCmp("ztid").getValue()==""){
		alertWarring("请先选择所属的上墙主题活动");
	}else{
		if(!SeatWin){
			SeatWin = new Ext.Window({
		        id: 'SeatWin',
		        layout:'fit',
	            width:300,
	            height:150,
	            y:130,
	            frame:true,
	            plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
				border : false,
			    items :[SeatForm],
			    buttons:[{
				    text: '保存',
				    handler:function(){
				          if(SeatForm.form.isValid()){
				        	if(Ext.getCmp('startNum').getValue()<Ext.getCmp('endNum').getValue()){
				        		SeatForm.form.submit({
					                   method:'POST',
					                   waitMsg:'正在提交.....',
									   waitTitle:'请稍等',
									   url : getPath() + 'wallActivity/changeSeatNumberForJionWallActivitypeople',
									   params:{ztid:Ext.getCmp("ztid").getValue()},
					                   success:function(form, action){
//					                	   alert(action.result.success);
					                	   if(action.result.success){
					                		     Ext.getCmp('grid').getStore().reload();
							                	 Ext.getCmp("SeatWin").hide();
					                	   }else{
					                		   //起始范围太小，不能给所有没有座位号的人员自动设置，请重新设置范围。
					                		   alertWarring("起始范围太小，不能给所有没有座位号的人员自动设置，请重新设置范围");
								        		Ext.getCmp('startNum').setValue("");
								        		Ext.getCmp('endNum').setValue("");
					                	   }
					                   },
					                   failure:function(form, action){
					                      alertWarring("设置异常,请联系管理员");
					                   }
					              }); 
				        	}else{
				        		alertWarring("初始范围一定要大于结束范围.");
				        		Ext.getCmp('startNum').setValue("");
				        		Ext.getCmp('endNum').setValue("");
				        	}
				          }else{
				               alertWarring("内容填写不完整");
				          }
				     }
				 },{
					 text:'取消',
				     handler:function(){
				    	  Ext.getCmp("SeatWin").hide();
				     }
				 }]
	        });
		}
		SeatWin.show();
	}
}

//重写团体树的单击事件
treePanel2.removeListener("click", groupClick);
treePanel2.on('click',function(n){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.orgId){ alertWarring("请先选择组织，再进行操作");return;}
	window.parent.groupId = n.attributes.id;
	ywallactivityStore.load({params:{groupId:window.parent.groupId}});
	store.load({params:{ztid:""}});
	
});