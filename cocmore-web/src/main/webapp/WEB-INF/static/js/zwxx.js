var LIMIT = 26;var editWin;var mbszWin;var selfFid;var orgId;

var records = new Ext.data.Record.create([
                                          {name:'fid'},
                                          {name: 'fnumber'},
                                          {name: 'fname'},
                                          {name: 'fsuperPositonId'},
                                          {name: 'fsocialGroupsId'},
                                          {name: 'forganizationId'},
                                          {name: 'fcreaterId'},
                                          {name: 'fmodifiedId'},
                                          {name: 'flastModified'},
                                          {name: 'pcreateTime'},
                                          {name: 'fmodifiedTime'},
                                          {name: 'flastModifiedTime'},
                                          {name: 'fhide'},
                                          {name: 'fleafNode'},
                                          {name: 'fseq'},
                                          {name: 'fcomment'},
                                          {name: 'fbillState'},
                                          {name: 'flag'},
                                          {name: 'fversion'}
                                       ]
                                   );
                                   
   var store = new Ext.data.Store({
								   	reader:new Ext.data.JsonReader({
								   		root:'obj.list',
										totalProperty:'obj.count'
								   	},records),
								   	proxy:new Ext.data.HttpProxy({
								   			disableCaching :false,
											url: getPath() + '/pos/findAllPos'
									}),
								   	remoteStore : true, // 是否远程调用数据
									remoteSort : true   // 是否远程排序
                       			});

   //读入数据之前的事件,store不需要在写baseParams,因为会覆盖掉. (每次调用都载入此函数,'load'则第一次不调用外,其余都调用).
     store.on('beforeload', function() {    
     	  this.baseParams = {
     			 orgId:window.parent.orgId,
     			 groupId:window.parent.groupId
     	  };
     });
var tool = new Ext.Toolbar({ //工具栏
	items:['职位名称：',{
		xtype:'textfield',
		emptyText:'请输入职位名',
		id:'searchCondition',
		width:150
	},'-']
});

Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 27
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
    	new Ext.grid.RowNumberer(),{id:'fid',hidden:true},
    	{
    		id:'fnumber', 
    		header: '编号', 
    		width: 240, 
    		hidden: true, 
    		dataIndex:'fnumber'
       },{
    		id:'fname', 
    		header: '名称', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fname'
       },{
	       	id:'forganizationId',
	   		hidden: true, 
	   		dataIndex:'forganizationId'
       },{
    	    id:'fsocialGroupsId',
    	    hidden:true,
    	    dataIndex:'fsocialGroupsId'
       },{
    		id:'fsuperPositonId', 
    		header: '上级职位', 
    		dataIndex:'fsuperPositonId',
    		hidden:true
       },{
    	   id:'fhide',
    	   dataIndex:'fhide',
    	   hidden:true,
    	   header:'是否隐藏'/*,
    	   renderer:function(value){
    		   if(value==0){
    			   return '是';
    		   }else
    			   return '否';
    	   }*/
       },{
    	   id:'fleafNode',
    	   dataIndex:'fleafNode',
    	   header:'叶子节点',
    	   renderer:function(value){
    		   if(value==0){
    			   return '是';
    		   }else
    			   return '否';
    	   }
       },{
    	   id:'fseq',
    	   dataIndex:'fseq',
    	   header:'排序号'
       },{
    		id:'fcomment', 
    		header: '备注', 
    		width: 150, 
	        maxLength:125,
    		sortable: true, 
    		allowBlank: true,
    		dataIndex:'fcomment'
       },{
	   	    id:'fcreaterId',
	   		dataIndex:'fcreaterId' ,
			hidden:true  
       },{
    	    id:'pcreateTime',
	   		dataIndex:'pcreateTime' ,
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
	grid.on('dblclick',function(){ editData("detail")});
});

/**************************** 自定义元素 **********************************/
//行政组织
//var yBasicOrganizationStore = new Ext.data.Store({
//	autoLoad:true,
//	proxy : new Ext.data.HttpProxy({ 
//		disableCaching :false,
//		method:'get',
//		url : getPath() + '/org/findAllOrgBySuperId'
//	}),
//	reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
//});
//
//var yBasicOrganization = new Ext.form.ComboBox({
//	fieldLabel : "行政组织",
//	store : yBasicOrganizationStore,
//	id:'yBasicOrganization',
//	hiddenName : 'forganizationId',
//	triggerAction : 'all',
//	lazyInit : true,
//	valueField : 'fid',
//	displayField : 'fname',
//	selectOnFocus:true,
//	editable:false,
//	emptyText:'请选择行政组织',
//	listeners:{
//		select:function(n){
//			orgId = Ext.getCmp('yBasicOrganization').getValue();
//			//alert(orgId);
//			fsocialGroupsIdStore.load({params:{orgId:orgId}});
//		}
//  	}
//});

//所属团体
//var fsocialGroupsIdStore = new Ext.data.Store({
//	//autoLoad:true,
//	proxy : new Ext.data.HttpProxy({ 
//		disableCaching :false,
//		method:'post',
//		url : getPath() + '/groups/findAllGroupPagingList'
//	}),
//	basePamars:{orgId:orgId},
//	reader : new Ext.data.JsonReader({root:'list'}, [{name : 'fid'}, {name : 'fname'}])
//});
//var fsocialGroupsCom = new Ext.form.ComboBox({
//	fieldLabel : "所属团队",
//	store : fsocialGroupsIdStore,
//	id:'fsocialGroupsId',
//	hiddenName : 'fsocialGroupsId',
//	triggerAction : 'all',
//	lazyInit : true,
//	mode:'local',
//	valueField : 'fid',
//	displayField : 'fname',
//	selectOnFocus:true,
//	editable:false,
//	emptyText:'请选择所属团体'
//});

//上级职位
var fsuperiorPositionStore = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'post',
		url : getPath() + '/pos/findSuper'
	}),
	basePamars:{selfFid:selfFid},
	reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
});
//fsuperiorPositionStore.load();
//上级职位
var fsuperiorPosition = new Ext.form.ComboBox({
	fieldLabel : "上级职位",
	store : fsuperiorPositionStore,
	id:'fsuperiorPosition',
	hiddenName : 'fsuperiorPosition',
	triggerAction : 'all',
	lazyInit : true,
	mode:'local',
	valueField : 'fid',
	displayField : 'fname',
	selectOnFocus:true,
	editable:false,
	emptyText:'请选择上级职位'
});


var editZwxxForm = new Ext.form.FormPanel({
    border : false,
	 frame	: true,
	 labelAlign: 'left',
	 buttonAlign: 'center',
	 labelWidth:90,
	 autoScroll:false,
	 containerScroll: true,
	 width: 600,
     height: 130,
	 items:{
        layout:'column',
        items:[{
        	 columnWidth: .5,
             layout: 'form',
             border: false,
             defaults:{xtype:'textfield',anchor:'92%'},
             items: [
			        {xtype:'hidden',id:'fid',name:'fid'},{
				        fieldLabel:'编号',
				        id:'fnumber',
				        name:'fnumber',
				        xtype:'hidden'
			        },{
				        fieldLabel:'名称',
				        id:'fname',
				        maxLength:24,
				        name:'fname',
			            blankText:'该字段不允许为空',
			            allowBlank: false
				 	},{
				 		id:'fhide',
				 		name:'fhide',
				 		value:'0',
				 		xtype:'hidden'
				 	},/*{
     				 	xtype:'radiogroup',
    				 	layout:'column',
    				 	fieldLabel:'是否隐藏',
    				 	id:'fhide',
    				 	columns:2,
    				 	items:[{boxLabel: "是", name: 'fhide',inputValue: 1},  
    				 	       {boxLabel: "否", name: 'fhide',inputValue: 0,checked:true}],
        	            blankText:'该字段不允许为空',
        	            allowBlank: false
                    },*/{
    				 	xtype:'radiogroup',
    				 	layout:'column',
    				 	fieldLabel:'没有下级',
    				 	id:'fleafNode',
    				 	columns:2,
    				 	items:[{boxLabel: "是", name: 'fleafNode',checked:true,inputValue: 0},  
    				 	       {boxLabel: "否", name: 'fleafNode',inputValue: 1}],
        	            blankText:'该字段不允许为空',
        	            allowBlank: false
                    }
			 ]},{ 
			 columnWidth: .5,
             layout: 'form',
             border: false,
             defaults:{xtype:'textfield',anchor:'92%'},
             items: [
                    fsuperiorPosition,
                    {
                    	xtype:'hidden',
                    	fieldLabel:'所属组织',
                    	id:'forganizationId',
                    	name : 'forganizationId'
                    },{
                    	xtype:'hidden',
                    	fieldLabel:'所属团体',
                    	id:'fsocialGroupsId',
                    	name : 'fsocialGroupsId'
                    },{
                    	xtype:'numberfield',
                    	fieldLabel:'排序号',
                    	id:'fseq',
 				        name:'fseq',
                    	blankText:'该字段不允许为空',
                    	allowBlank: false
                    },{
				        fieldLabel:'备注',
				        maxLength:125,
				        id:'fcomment',
				        name:'fcomment'
             		},{
					 	xtype:'hidden',
				        id:'pcreateTime',
				        name:'pcreateTime'
             		},{
					 	xtype:'hidden',
				        id:'fbillState',
				        name:'fbillState'
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
			 }]
	 }
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
//删除
function delBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var row = Ext.getCmp("grid").getSelectionModel().getSelected();
	if(!row){
   	 	alertWarring("请选择需要删除的职位信息!");return;
    }
	Ext.Ajax.request({
		url : getPath() + '/pos/deletePos',
		params : {fid : row.get('fid')},
		success: function (result, request){
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

function editData(row){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.orgId){
		alertWarring("请先选择团体，再进行操作");return;
	}else{
		Ext.getCmp('forganizationId').setValue(window.parent.orgId);
	}
	if(getUser()[2]==0){
		if(!window.parent.groupId){
			alertWarring("请先选择团体，再进行操作");return;
		}
	}
	Ext.getCmp('fsocialGroupsId').setValue(window.parent.groupId);
	var detail = row;
	if(row!='add' && row!='detail'){
			 row = Ext.getCmp("grid").getSelectionModel().getSelected();
		     if(!row){
		    	 alertWarring("请选择需要修改的职位信息!");return;
		     }
		     var billState = row.get("fbillState");
		     if(billState == 9){
					alertError("该信息已作废，无法进行操作！");return;
			 }
		}else if(row =='detail'){
			row = Ext.getCmp("grid").getSelectionModel().getSelected();
		}else{
			getNumberStore();
		}
    	this.row = row;
    	var that = this;
         if(!editWin){
            editWin = new Ext.Window({
		        id: 'editWin',
		        layout:'fit',
                width:600,
                height:180,
                frame:true,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
			    items :[editZwxxForm],
			    buttons:[{
					    text: '保存',
					    handler:function(){
					    	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
					          if(editZwxxForm.form.isValid()){
					        	  editZwxxForm.form.submit({
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
				    	  editZwxxForm.form.reset();
		    		}
			    }
            });
		}
        setTitleAndUrl(row, editWin);

	    var date = new Date();  // 得到系统日期
	    var dateString = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	    editZwxxForm.getForm().findField('fbillState').setValue(5);
	    editZwxxForm.getForm().findField('pcreateTime').setValue(dateString);
	    editZwxxForm.getForm().findField('fcreaterId').setValue(getUser()[1]);	
	    
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
	    	editZwxxForm.getForm().findField('fmodifiedTime').setValue(dateString);
	    	editZwxxForm.getForm().findField('fmodifiedId').setValue(getUser()[1]);
	    	editZwxxForm.getForm().findField('flastModifiedTime').setValue(dateString);
	    	editZwxxForm.getForm().findField('flastModifiedId').setValue(getUser()[1]);
		    //zyxxstore.reload({params:{id:row.get('fid')}});;//加载职位相关职员信息
	      	loadData(row);
		}else{
			var tool = Ext.getCmp('editWin').buttons;
			for(var i =0;i<tool.length;i++){
				tool[i].setVisible(true);
			}
		}		
	}

function loadData(row) {
	selfFid = row.get('fid');
	fsuperiorPositionStore.baseParams={selfFid : selfFid};
	fsuperiorPositionStore.load({
		params:{orgId:window.parent.orgId,groupId:window.parent.groupId},
		callback:function(){
			editZwxxForm.form.setValues({
				fid				: row.get('fid'),
				fnumber  		: row.get('fnumber'),
				fname    		: row.get('fname'),
				forganizationId : row.get('forganizationId'),
				fsocialGroupsId : row.get('fsocialGroupsId'),
				fsuperPositonId : row.get('fsuperPositonId'),
				fhide			: row.get('fhide'),
				fseq			: row.get('fseq'),
				fleafNode		: row.get('fleafNode'),
				fcomment  		: row.get('fcomment'),
				pcreateTime		: row.get('pcreateTime'),
				fcreaterId		: row.get('fcreaterId'),
				flag			: row.get('flag')
			});
		}
	});
	
}
function setTitleAndUrl(row, win) {
	if(row == 'add') {
 		win.setTitle('增加职位信息');
 		editZwxxForm.form.url = getPath() + '/pos/savePos';
	} else {
 		win.setTitle('修改职位信息');
  		editZwxxForm.form.url = getPath() + '/pos/updatePos';
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
		}
		Ext.MessageBox.prompt("提示", "请输入将这条信息作废的原因！",function(bu,txt){
			if(txt && 0<txt.length<125){
				Ext.Ajax.request({
					url : getPath()+'pos/Invalid',
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

function getNumberStore(){
	Ext.Ajax.request({
		url:getPath() + '/serialNumber/getSerialNumber',
		params:{prefix:'JC-ZWGL'},
		success:function(response, options) {
			Ext.getCmp("fnumber").setValue(Ext.util.JSON.decode(response.responseText).serialNumber);
		}
	});
}

//重写组织树的单击事件
treePanel1.removeListener("click");
treePanel1.on('click',function(n){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	window.parent.orgId = n.attributes.id;
	window.parent.groupId = "";
	store.reload({params:{orgId:window.parent.orgId,start:0,limit:LIMIT}});
	fsuperiorPositionStore.load({
		params:{orgId:window.parent.orgId}
	});
});

//重写团体树的单击事件
treePanel2.removeListener("click");
treePanel2.on('click',function(n){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	window.parent.groupId = n.attributes.id;
	store.reload({params:{groupId:window.parent.groupId,start:0,limit:LIMIT}});
	fsuperiorPositionStore.load({
		params:{orgId:window.parent.orgId,groupId:window.parent.groupId}});
});