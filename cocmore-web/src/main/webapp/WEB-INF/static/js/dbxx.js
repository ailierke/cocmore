var LIMIT = 26;var editWin;var mbszWin;var superId;

var records = new Ext.data.Record.create([
                                          {name:'fid'},
                                          {name: 'fcontent'},
                                          {name: 'ybasicSocialgroups'},
                                          {name: 'flag'}
                                       ]);
var store = new Ext.data.Store({
	   	reader:new Ext.data.JsonReader({
	   		root:'list',
	   		totalProperty:'count'
		},records),
		proxy:new Ext.data.HttpProxy({
				disableCaching :false,
				url: getPath() + '/content/findContentPage'
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
	items:['担保内容:',
	       {xtype:'textfield',
	    	   emptyText:'请输入担保内容',
	    	   id:'searchCondition',
	    	   width:150
    	   },'-']
});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 10
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
	       	id:'ybasicSocialgroups', 
	   		dataIndex:'ybasicSocialgroups',
	   		width:150,
	   		header: '团体', 
	   		hidden: true,
	   		renderer:function(value){
	   			return value.fname;
	   		}
       },{
    		id:'fcontent', 
    		header: '担保内容', 
    		dataIndex:'fcontent',
    		maxLength:18
       },{id:'flag',hidden:true}
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
		 			xtype:'textfield',
			        fieldLabel:'担保内容',
			        id:'fcontent',
			        name:'fcontent',
		            blankText:'该字段不允许为空',
		            allowBlank: false
		        },{
		        	xtype:'hidden',
			        fieldLabel:'团体',
			        id:'ybasicSocialgroups',
			        name:'YBasicSocialgroups.fid'
		        },{xtype:'hidden',id:'flag',name:'flag'}]
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
	store.load({params:{content:searchCondition,groupId:window.parent.groupId,start:0,limit:LIMIT}});
}
//删除
function delBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	var row = Ext.getCmp("grid").getSelectionModel().getSelected();
	if(!row){
   	 alertWarring("请选择需要删除的担保信息!");return;
    }
	gridSubAjax(row);
}

function editData(row){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
		if(row!='add'){
		     row = Ext.getCmp("grid").getSelectionModel().getSelected();
		     if(!row){
		    	 alertWarring("请选择需要编辑的担保信息!");return;
		     }
		}else{
//			getNumberStore();
			Ext.getCmp('ybasicSocialgroups').setValue(window.parent.groupId);
		}
    	this.row = row;
    	var that = this;
         if(!editWin){
            editWin = new Ext.Window({
		        id: 'editWin',
		        layout:'fit',
                width:360,
                height:110,
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
				fid								: row.get('fid'),
				fcontent  					: row.get('fcontent'),
				ybasicSocialgroups  	: row.get('ybasicSocialgroups').fid,
				flag					: row.get('flag')
			});
}
function setTitleAndUrl(row, win) {
	if(row == 'add') {
 		win.setTitle('增加担保信息');
 		editDbxxForm.form.url = getPath() + '/content/saveContent';
	} else {
 		win.setTitle('修改担保信息');
 		editDbxxForm.form.url = getPath() + '/content/updateContent';
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
		    msg: "确认要删除当前担保信息？!",
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
			msg: "请选择需要删除的担保信息!",
			width: 250,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}
function gridSubAjax(param){
	Ext.Ajax.request({
		url : getPath() + '/content/deleteContent',
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