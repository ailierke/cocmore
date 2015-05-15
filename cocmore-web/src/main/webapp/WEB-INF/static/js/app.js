var LIMIT = 26;var editWin;var mbszWin;var superId;

var records = new Ext.data.Record.create([
                                          {name:'fid'},
                                          {name: 'versionNumber'},
                                          {name: 'downloadUrl'},
                                          {name: 'updateDetail'},
                                          {name: 'chanelNo'},
                                          {name: 'flag'}
                                       ]);
var store = new Ext.data.Store({
	   	reader:new Ext.data.JsonReader({
	   		root:'obj.list',
	   		totalProperty:'obj.count'
		},records),
		proxy:new Ext.data.HttpProxy({
				disableCaching :false,
				url: getPath() + '/appvn/findAllAppVn'
		}),
		remoteStore : true, // 是否远程调用数据
		remoteSort : true   // 是否远程排序
});

var tool = new Ext.Toolbar({ //工具栏
	items:['版本号:',
	       {xtype:'textfield',
	    	   emptyText:'请输入版本号',
	    	   id:'searchCondition',
	    	   width:150
    	   },'-']
});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 32
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
	       	id:'versionNumber', 
	   		dataIndex:'versionNumber',
	   		width:150,
	   		header: '版本号'
       },{
    		id:'downloadUrl', 
    		header: '下载地址', 
    		dataIndex:'downloadUrl'
       },{
    	 id:'updateDetail', 
   		header: '更新内容', 
   		dataIndex:'updateDetail'
   },{
  	 id:'chanelNo', 
		header: '渠道号', 
		dataIndex:'chanelNo'
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
    
    	new Ext.Viewport({
        	renderTo:'mainDiv',
        	id:'viewport',
        	layout:'border',
        	items:[grid]
        });
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
			        fieldLabel:'版本号',
			        id:'versionNumber',
			        name:'versionNumber',
		            blankText:'该字段不允许为空',
		            allowBlank: false
		        },{
			        fieldLabel:'下载地址',
			        id:'downloadUrl',
			        name:'downloadUrl'
		        },{
			        fieldLabel:'更新内容',
			        id:'updateDetail',
			        name:'updateDetail'
		        },{
			        fieldLabel:'渠道号',
			        id:'chanelNo',
			        name:'chanelNo'
		        },{
		        	xtype:'hidden',
			   	     id:'flag',
			   	     name:'flag'
		        }]
});

/**************************** 自定义元素结束 *******************************/
/**************************** 操作 **********************************/


//条件查询
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
   	 alertWarring("请选择需要删除的担保信息!");return;
    }
	gridSubAjax(row);
}

function editData(row){
		if(row!='add'){
		     row = Ext.getCmp("grid").getSelectionModel().getSelected();
		     if(!row){
		    	 alertWarring("请选择需要编辑的担保信息!");return;
		     }
		}else{
//			getNumberStore();
			//Ext.getCmp('ybasicSocialgroups').setValue(window.parent.groupId);
		}
    	this.row = row;
    	var that = this;
         if(!editWin){
            editWin = new Ext.Window({
		        id: 'editWin',
		        layout:'fit',
                width:360,
                height:200,
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
				versionNumber  					: row.get('versionNumber'),
				downloadUrl  	: row.get('downloadUrl'),
				updateDetail  	: row.get('updateDetail'),
				chanelNo  	: row.get('chanelNo'),
				flag  	: row.get('flag')
			});
}
function setTitleAndUrl(row, win) {
	if(row == 'add') {
 		win.setTitle('增加APP版本信息');
 		editDbxxForm.form.url = getPath() + '/appvn/saveAppVn';
	} else {
 		win.setTitle('修改APP版本信息');
 		editDbxxForm.form.url = getPath() + '/appvn/updateAppVn';
	}
	win.show();
}