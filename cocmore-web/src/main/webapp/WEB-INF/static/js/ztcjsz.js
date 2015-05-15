var LIMIT = 26;var editWin;var mbszWin;var superId;var ryWin;

var records = new Ext.data.Record.create([
                                          {name: 'fid'},
                                          {name: 'fcontent'},
                                          {name: 'ywallactivity'},
                                          {name: 'fawardName'},
                                          {name: 'fprizeName'},
                                          {name: 'fawardOrder'},
                                          {name: 'frandom'},
                                          {name: 'fawardsPerson'},
                                          {name: 'fdesignatedPerson'},
                                          {name: 'flag'}
                                       ]);
var store = new Ext.data.Store({
	   	reader:new Ext.data.JsonReader({
	   		root:'list',
	   		totalProperty:'count'
		},records),
		proxy:new Ext.data.HttpProxy({
				disableCaching :false,
				url: getPath() + '/setting/findSettingPage'
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
		width:200
	},'-']
});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 12
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
    store.load({params:{start:0,limit:LIMIT,groupId:window.parent.groupId}});
    var cm = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer(),
    	{id:'fid',hidden:true},
    	{
	       	id:'ywallactivity', 
	   		dataIndex:'ywallactivity',
	   		width:150,
	   		header: '上墙主题', 
	   		renderer:function(value){
	   			return value.ftheme;
	   		}
       },{
    		id:'fawardName', 
    		header: '奖项名称', 
    		dataIndex:'fawardName'
       },{
	   		id:'fprizeName', 
			header: '奖品名称', 
			dataIndex:'fprizeName'
	   },{
			id:'fawardOrder', 
			header: '奖项顺序', 
			dataIndex:'fawardOrder'
	   },{
			id:'frandom', 
			header: '是否随机', 
			dataIndex:'frandom',
			renderer:function(value){
				if(value==0){
					return '否';
				}else
					return '是';
			}
	   },{
			id:'fawardsPerson', 
			header: '获奖人数', 
			dataIndex:'fawardsPerson'
	   },{
			id:'fdesignatedPerson', 
			header: '指定获奖人', 
			dataIndex:'fdesignatedPerson',
			hidden:true
	   },{
		   id:'flag',  
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
	grid.on('dblclick', editData);
});

/**************************** 自定义元素 **********************************/
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

var editZtcjszxxForm = new Ext.form.FormPanel({
     border : false,
	 frame	: true,
	 labelAlign: 'left',
	 buttonAlign: 'center',
	 labelWidth:80,
	 autoScroll:false,
	 containerScroll: true,
	 defaults:{anchor:'93%',xtype:'textfield'},
	 items:[{xtype:'hidden',id:'fid',name:'fid'},
	        	YWallactivity,
		        {
			        fieldLabel:'奖项名称',
			        id:'fawardName',
			        name:'fawardName',
			        maxLength:18,
			        blankText:'该字段不允许为空',
			        allowBlank: false
			    },{
			        fieldLabel:'奖品名称',
			        id:'fprizeName',
			        name:'fprizeName',
			        maxLength:18
			    },{
			    	xtype:'numberfield',
			        fieldLabel:'奖项顺序',
			        id:'fawardOrder',
			        name:'fawardOrder',
			        blankText:'该字段不允许为空',
			        allowBlank: false
			    },{
			        fieldLabel:'是否随机',
			        id:'frandom',
			        xtype:'radiogroup',
				 	layout:'column',
				 	columns:2,
				 	items:[{boxLabel: "是", name: 'frandom',inputValue: 1},  
				 	       {boxLabel: "否", name: 'frandom',inputValue: 0,checked:true}],
			        blankText:'该字段不允许为空',
			        allowBlank: false
			    },{
			        fieldLabel:'获奖人数',
			        id:'fawardsPerson',
			        name:'fawardsPerson',
			        blankText:'该字段不允许为空',
			        allowBlank: false
			    },{
			    	xtype:'hidden',
			        fieldLabel:'指定获奖人',
			        id:'fdesignatedPerson',
			        name:'fdesignatedPerson'
			    },{
			    	xtype:'button',
			    	width:80,
			    	text:'指定获奖人',
			    	listeners:{
			    		click :function(){
			    			ry_store.load({params:{ztid:Ext.getCmp("ywallactivity").getValue()}});
			    			if(!ryWin){
			    				ryWin = new Ext.Window({
			    			        id: 'ryWin',
			    			        title:'请勾选中奖人员',
			    			        layout:'fit',
			    		            width:500,
			    		            height:340,
			    		            frame:true,
			    		            plain: true,
			    			        closeAction:'hide',
			    					maximizable:false,
			    					resizable:false,
			    					modal:true,
			    					border:false,
			    				    items :[ry_grid],
			    				    buttons:[{
			    					    text: '保存',
			    					    handler:function(){ //设置指定中奖人员
			    					    	if(Ext.getCmp('ry_grid').getSelectionModel().hasSelection()){
			    					  		  var records=Ext.getCmp('ry_grid').getSelectionModel().getSelections();
			    					  		  var fids=new Array();
			    					  		  var awardS = Ext.getCmp("fawardsPerson").getValue();
			    					  		  if(awardS && awardS<records.length){
			    					  			Ext.MessageBox.show({
			    					  			    title: '提示',
			    					  			    msg: "人数大于了中奖人数,是否更改获奖人数?",
			    					  			    width:300,
			    					  			    buttons: Ext.MessageBox.YESNO,
			    					  			    fn: function(e) {
			    					  			    		if(e == 'yes') {
			    					  			    			Ext.getCmp("fawardsPerson").setValue(records.length);
			    					  			    		}else{
			    					  			    			return false;
			    					  			    		}
			    					  			    	},
			    					  			    icon : Ext.MessageBox.QUESTION
			    					  			});
			    					  		  }
			    					  		  for(var i=0;i<records.length;i++){
			    					  			  fids[i]=records[i].get('fid');
			    					  		  }
			    					  		  Ext.getCmp("fdesignatedPerson").setValue(fids.toString());
			    					  		  Ext.getCmp("ryWin").hide();
		    					  		  	}else{
		    					  				Ext.MessageBox.show({
		    					  					title: '提示',
		    					  					msg: "请选中中奖人员 再点击保存!",
		    					  					width: 250,
		    					  					buttons: Ext.MessageBox.OK,
		    					  					buttonAlign: 'center'
		    					  				});
		    					  			}
			    					     }
			    					 },{
			    						 text:'取消',
			    					      handler:function(){
			    					    	  Ext.getCmp("ryWin").hide();
			    					      }
			    					 }]
			    		        });
			    			}
			    			Ext.getCmp('ryWin').show();
			    		}
			    	}
			    },{
			        id:'flag',
			        name:'flag',
			        xtype:'hidden'
			    }]
});

/******************指定获奖人员列表设置*********************/
var ry_sm = new Ext.grid.CheckboxSelectionModel({handleMouseDown: Ext.emptyFn});
var ry_records = new Ext.data.Record.create([
                                          {name: 'fid'},
                                          {name: 'userName'},
                                          {name: 'tel'},
                                          {name: 'setNumber'},
                                          {name: 'groupName'},
                                          {name: 'ywallactivity'}
                                       ]);
var ry_store = new Ext.data.Store({
	   	reader:new Ext.data.JsonReader({
	   		root:'list',
	   		totalProperty:'count'
		},ry_records),
		proxy:new Ext.data.HttpProxy({
				disableCaching :false,
				url: getPath() + 'wallActivity/findAllJionWallActivitypeople'
		}),
		remoteStore : true, // 是否远程调用数据
		remoteSort : true   // 是否远程排序
});
var ry_cm = new Ext.grid.ColumnModel([
                               	new Ext.grid.RowNumberer(),
                               	ry_sm,
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

var ry_grid = new Ext.grid.GridPanel({
	region : 'center',
	id:'ry_grid',
	border: false,
	columnLines:true,
	loadMask:true,
	store: ry_store,
	frame:false,
	viewConfig: {forceFit:true},
	monitorResize: true,
	cm: ry_cm,
	sm: ry_sm
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
	store.load({params:{activityName:searchCondition,start:0,limit:LIMIT}});
}

function editData(row){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
		if(row!='add'){
		     row = Ext.getCmp("grid").getSelectionModel().getSelected();
		     if(!row){
		    	 alertWarring("请选择需要编辑的抽奖设置信息!");return;
		     }
		}else{
//			getNumberStore();
			
		}
    	this.row = row;
    	var that = this;
         if(!editWin){
            editWin = new Ext.Window({
		        id: 'editWin',
		        layout:'fit',
                width:360,
                height:260,
                frame:true,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
				border : false,
			    items :[editZtcjszxxForm],
			    buttons:[{
					    text: '保存',
					    handler:function(){
					          if(editZtcjszxxForm.form.isValid()){
					        	  editZtcjszxxForm.form.submit({
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
						editZtcjszxxForm.form.reset();
					}
			    }
            });
		}
        setTitleAndUrl(row, editWin);


        ywallactivityStore.load({
        	params:{groupId:window.parent.groupId},
        	callback:function(){
		    	    if(row != 'add') {
		    	      	loadData(row);
		    		}	
	        }
    	});	
	}

function loadData(row) {
		editZtcjszxxForm.form.setValues({
			fid								: row.get('fid'),
			fawardName  			: row.get('fawardName'),
			ywallactivity  				: row.get('ywallactivity').fid,
			fprizeName					: row.get('fprizeName'),
			fawardOrder				: row.get('fawardOrder'),
			frandom						: row.get('frandom'),
			fawardsPerson			: row.get('fawardsPerson'),
			fdesignatedPerson		: row.get('fdesignatedPerson'),
			flag					: row.get('flag')
		});
}

function setTitleAndUrl(row, win) {
	if(row == 'add') {
 		win.setTitle('增加主题抽奖设置信息');
 		editZtcjszxxForm.form.url = getPath() + '/setting/saveSetting';
	} else {
 		win.setTitle('修改主题抽奖设置信息');
 		editZtcjszxxForm.form.url = getPath() + '/setting/updateSetting';
	}
	win.show();
}

//====================================删除========================================
function deleteSetting(){
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
		    msg: "确认要删除当前抽奖设置信息？!",
		    width:300,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			settingAjax(fids);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}
	else
	{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择需要删除的抽奖设置信息!",
			width: 250,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}

function settingAjax(param)
{
	Ext.Ajax.request({
		url : getPath() + '/setting/deleteSetting',
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
treePanel2.on('click',function(n){
	window.parent.groupId = n.attributes.id;
	ywallactivityStore.load({
     	params:{groupId:window.parent.groupId}
 	});});