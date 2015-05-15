var LIMIT = 26;var editWin;var mbszWin;var excel;
//create the data store
var records = new Ext.data.Record.create([  
       {name: 'fid'},
       {name: 'fnumber'},
       {name: 'fname'},
       {name: 'fcomment'},
       {name: 'fbillState'},
       {name: 'fcreaterId'},
       {name: 'fcreateTime'},
       {name: 'fmodifiedId'},
       {name: 'fmodifiedTime'},
       {name: 'flastModifiedId'},
       {name: 'flastModifiedTime'},
       {name: 'flag'}
]);
var store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
		root:'obj.list',
		totalProperty:'obj.count'
	},records),
	proxy:new Ext.data.HttpProxy({
			disableCaching :false,
			url: getPath() + '/ind/findAllInd'
	}),
	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});
var tool = new Ext.Toolbar({ //工具栏
	items:['产业名称：',{
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
		functionId : 30
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
    	{id:'flag',hidden:true},
    	{
    		id:'fnumber', 
    		header: '编号', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fnumber'
       },{
    		id:'fname', 
    		header: '名称', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fname'
       },{
    		id:'fcomment', 
    		header: '备注', 
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
        	id: 'flastModifiedId',
        	dataIndex:'flastModifiedId',
        	hidden:true
		},{
        	id: 'flastModifiedTime', 
        	dataIndex: 'flastModifiedTime', 
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
//excel模板设置
function mbsz(){
	var export_url = getPath() + 'ind/download';
    window.location.href = export_url;
}
//导入
/*导入数据功能*/
function showImportExcel(){
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
	    	 var logourl = Ext.getCmp('flshlogo').getValue();
    		if(Ext.getCmp('uploadFileLogo').getForm().isValid() && /\.(xls)$/.test(logourl)){
    			Ext.getCmp('uploadFileLogo').getForm().submit({
		                   method:'POST',
		                   waitMsg:'正在提交.....',
						   waitTitle:'请稍等',
						   url : getPath() + 'ind/import',
		                   success:function(form, action){
		                	    location.reload();
		                	    Ext.getCmp("showImportExcel").hide();
		                   },
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

var editCyglForm = new Ext.form.FormPanel({
     border : false,
	 frame	: true,
	 labelAlign: 'left',
	 buttonAlign: 'center',
	 labelWidth:60,
	 autoScroll:false,
	 containerScroll: true,
	 width: 480,
     height: 220,
     defaults:{anchor:'95%',xtype:'textfield'},
	 items:[{xtype:'hidden',id:'fid',name:'fid'},{
	        id:'fnumber',
	        fieldLabel:'编号',
	        name:'fnumber',
	        readOnly:true,
            blankText:'该字段不允许为空',
            allowBlank: false
	 },{
	        fieldLabel:'名称',
	        id:'fname',
	        name:'fname',
	        maxLength:12,
            blankText:'该字段不允许为空',
            allowBlank: false
	 },{
		 	xtype:'textarea',
	        fieldLabel:'备注',
	        maxLength:150,
	        height:50,
	        id:'fcomment',
	        name:'fcomment'
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
	        name:'data.fcreaterId'
	 },{
		 xtype:'hidden',
	     id:'flag',
	     name:'data.flag'
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
	 }]
});

function editData(row){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(row!='add'){
		     row = Ext.getCmp("grid").getSelectionModel().getSelected();
		     if(!row){
		    	 alertWarring("请选择需要编辑的产业信息!");return;
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
                height:200,
                frame:true,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
			    items :[editCyglForm],
			    buttons:[{
				    text: '保存',
				    handler:function(){				    	
				          if(editCyglForm.form.isValid()){
				        	  editCyglForm.form.submit({
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
			    listeners:{hide:function(){editCyglForm.form.reset();}}
            });
		}
        setTitleAndUrl(row, editWin);
        
	    var date = new Date();  // 得到系统日期
	    var dateString = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	    editCyglForm.getForm().findField('fbillState').setValue(0);
	    editCyglForm.getForm().findField('fcreateTime').setValue(dateString);
	    editCyglForm.getForm().findField('fcreaterId').setValue(getUser()[1]);	
	    
	    if(row != 'add') {
	    	 editCyglForm.getForm().findField('fmodifiedTime').setValue(dateString);
	 	     editCyglForm.getForm().findField('fmodifiedId').setValue(getUser()[1]);
	 	     editCyglForm.getForm().findField('flastModifiedTime').setValue(dateString);
	 	     editCyglForm.getForm().findField('flastModifiedId').setValue(getUser()[1]);
	      	 loadData(row);
		}
	}

function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	//通过id获取输入的数据
	var searchCondition = Ext.getCmp("searchCondition").getValue();
	//模糊查询
	store.load({params:{searchName:searchCondition,start:0,limit:LIMIT}});
}
function loadData(row) {
		editCyglForm.form.setValues({
				fid : row.get('fid'),
				fnumber  : row.get('fnumber'),
				fname    : row.get('fname'),
				fsupperIndustry  : row.get('fsupperIndustry'),
				fcomment  : row.get('fcomment'),
				fcreaterId				:row.get('fcreaterID'),
				fcreateTime				:row.get('fcreateTime'),
				fbillState				:'10',
				flag					:row.get('flag')
		});
}
function setTitleAndUrl(row, win) {
	if(row == 'add') {
			win.setTitle('增加产业信息');
			editCyglForm.form.url = getPath() + '/ind/saveInd';
	} else {
			win.setTitle('修改产业信息');
			editCyglForm.form.url = getPath() + '/ind/updateInd';
	}
	win.show();
}

function setState(stateId){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var selectRow = Ext.getCmp('grid').getSelectionModel().getSelected();
	if(selectRow)
	{
		Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认要更新当前产业信息状态!",
		    width:300,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			gridSubAjax(selectRow.get('fid'),stateId);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}
	else
	{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择需要更新状态的信息!",
			width: 250,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}



function gridSubAjax(param,stateId)
{
	
	Ext.Ajax.request({
		url : getPath() + '/ind/updateIndStatus',
		params : {fid : param , status : stateId},
		
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



//获取number
function getNumberStore(){
	Ext.Ajax.request({
		url:getPath() + '/serialNumber/getSerialNumber',
		params:{prefix:'JC-CYGL'},
		success:function(response, options) {
			Ext.getCmp("fnumber").setValue(Ext.util.JSON.decode(response.responseText).serialNumber);
		}
	});
}