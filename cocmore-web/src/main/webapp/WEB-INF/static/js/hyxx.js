var LIMIT = 26;var editWin;var mbszWin;var jsonStr=[];var orgId;var hyglPassWin;var editCompanyWin;var excel;var i =0;
//上传图片类型  
var img_reg = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/;
var records = new Ext.data.Record.create([
                                          	{name: 'fid'},
											{name: 'fnumber'},
											{name: 'fname'},
											{name: 'fnickName'},
											{name: 'fpassword'},
											{name: 'ybasicSocialgroups'},
											{name: 'fheadImage'},
											{name: 'fsex'},
											{name: 'fbirthday'},
											{name: 'femail'},
											{name: 'fpreviousEmail'},
											{name: 'fhomePhone'},
											{name: 'fmobilePhone'},
											{name: 'fsecondPhone'},
											{name: 'ybasicProvince'},
											{name: 'ybasicCity'},
											{name: 'ybasicCounty'},
											{name: 'fnativePlace'},
											{name: 'fsite'},
											{name: 'freceivingAddress'},
											{name: 'fsource'},
											{name: 'fcomment'},
											{name: 'ftypeId'},
											{name: 'ysystemUsers'},
											{name: 'fisHidePhone'},
											{name: 'fisOriginalPassword'},
											{name: 'fcreaterId'},
											{name: 'fcreateTime'},
											{name: 'fmodifiedId'},
											{name: 'fmodifiedTime'},
											{name: 'flastModifiedId'},
											{name: 'flastModifiedTime'},
											{name: 'flag'},
											{name: 'jsonStr'},
											{name: 'isAdmin'},
											{name: 'fbillState'}
                                       ]
                                   );
                                   
//根据组织获取职员信息    
var store = new Ext.data.Store({
    reader:new Ext.data.JsonReader({
       		root:"obj.list",
   		totalProperty:'obj.count'
   	},records),
   	proxy:new Ext.data.HttpProxy({
   			disableCaching :false,
			url: getPath() + '/member/findAllMember'
	}),
	sortInfo:{field: "fcreateTime", direction: " DESC"},
   	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});

var tool = new Ext.Toolbar({ //工具栏
	items:['名称：',{
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
		functionId : 21
	},
	success : function(response, options) {
		obj=Ext.util.JSON.decode(response.responseText);
		for(var i=0;i<obj.length;i++){
			tool.add(Ext.util.JSON.decode(obj[i]));tool.add('-');
		}
		tool.doLayout();
	}
});

/*会员加载器*/
var treeLoaderHY = new Ext.tree.TreeLoader({});
/*会员根节点*/
var rootNodeHY = new Ext.tree.AsyncTreeNode({
	draggable:false,
	id : "source",
	text : "会员",
	draggable : false// 根节点不容许拖动
});
var treePanelHY =  new Ext.tree.TreePanel({
	region:'east',
	layout:'fit',
	collapsible : true,
	split : true,
	title:'职位',
	width:180,
	height:600,
	loader : treeLoaderHY,
	rootVisible : false,
	monitorResize: true,
	autoScroll: true,
	border:true,
	root:rootNodeHY,
	listeners:{
		beforeload:function(node) {//加载前
			treeLoaderHY.dataUrl = getPath() + 'member/getPositionTree?groupId='+window.parent.groupId; // 定义每个节点的Loader
		}
	}
});

Ext.onReady(function(){
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side'; 
    
    
//读入数据之前的事件,store不需要在写baseParams,因为会覆盖掉. (每次调用都载入此函数,'load'则第一次不调用外,其余都调用).
store.on('beforeload', function() {    
	  this.baseParams = {
			 start:0,limit:LIMIT,groupId:window.parent.groupId
	  };
});
store.on('load', function(store, records, options) {
    if (records.length = 0) {
        Ext.Msg.alert("提示信息", "没有符合条件的数据!"); 
    }
});
store.load({params:{start:0,limit:LIMIT,groupId:window.parent.groupId}});
    var order_sm = new Ext.grid.CheckboxSelectionModel({handleMouseDown: Ext.emptyFn});
    var cm = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer(), {id:'fid',hidden:true,dataIndex:'fid'},{id:'jsonStr',dataIndex:'jsonStr',hidden:true},
    	{
    		id:'fnumber', 
    		header: '编号', 
    		width: 150, 
    		hidden: true, 
    		dataIndex:'fnumber'
       },{
    		id:'fname', 
    		header: '姓名', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fname'
       },{
	   		id:'fnickName', 
			header: '昵称', 
			width: 150, 
			hidden: true, 
			dataIndex:'fnickName'
       },{
      		id:'fpassword', 
    		hidden: true, 
    		dataIndex:'fpassword'
       },{
      		id:'YBasicSocialgroups', 
    		header: '团体', 
    		width: 150, 
    		hidden: true, 
    		dataIndex:'ybasicSocialgroups'
       },{
      		id:'fheadImage', 
    		header: '头像', 
    		width: 150, 
    		hidden: true, 
    		dataIndex:'fheadImage'
       },{
    		id:'fsex', 
    		header: '性别', 
    		width: 80, 
    		sortable: true, 
    		dataIndex:'fsex',
    		renderer:function(value){
    			if(value==0){
    				return '先生';
    			}else
    				return '女士';
    		}
       },{
    		id:'fbirthday', 
    		header: '出生年月', 
    		format:"Y-m-d H:i:s", 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fbirthday'
       },{
    		id:'femail', 
    		header: '邮箱', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'femail'
       },{
    		id:'fpreviousEmail', 
    		header: '备用邮箱', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fpreviousEmail',
    		hidden:true
       },{
    		id:'fhomePhone', 
    		header: '家庭电话', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fhomePhone',
    		hidden:true
       },{
    		id:'fmobilePhone', 
    		header: '手机', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fmobilePhone',
    		renderer:function(value){
    			return decryptByABC(value);
    		}
       },{
    		id:'fsecondPhone', 
    		header: '备用手机', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fsecondPhone',
    		hidden:true
       },{
	   		id:'YBasicProvince', 
			header: '省份', 
			width: 150, 
			hidden: true, 
			dataIndex:'ybasicProvince'
       },{
      		id:'YBasicCity', 
    		header: '城市', 
    		width: 150, 
    		hidden: true, 
    		dataIndex:'ybasicCity'
        },{
      		id:'YBasicCounty', 
    		header: '区县', 
    		width: 150, 
    		hidden: true, 
    		dataIndex:'ybasicCounty'
        },{
      		id:'fnativePlace', 
    		header: '籍贯', 
    		width: 150, 
    		hidden: true, 
    		dataIndex:'fnativePlace'
        },{
    		id:'fsite', 
    		header: '地址', 
    		width: 150, 
    		hidden: true, 
    		dataIndex:'fsite'
       },{
	   		id:'freceivingAddress', 
			header: '收货地址', 
			hidden: true, 
			dataIndex:'freceivingAddress'
       },{
      		id:'fsource', 
    		header: '来源', 
    		width: 150, 
    		hidden: true, 
    		dataIndex:'fsource'
       },{
    		id:'fcomment', 
    		header: '描述', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fcomment'
       },{
	       	id: 'ftypeId',
	    	dataIndex:'ftypeId',
	    	hidden:true
		},{
	       	id: 'YSystemUsers',
	    	dataIndex:'ysystemUsers',
	    	hidden:true
		},{
	       	id: 'fisHidePhone',
	    	dataIndex:'fisHidePhone',
	    	header:'是否隐藏电话',
	    	renderer:function(value){
	    		if(value==0){
	    			return '是';
	    		}else{
	    			return '否';
	    		}
	    	}
		},{
	       	id: 'fisOriginalPassword',
	    	dataIndex:'fisOriginalPassword',
	    	hidden:true
		},{
	       	id: 'fcreaterId',
	    	dataIndex:'fcreaterId',
	    	hidden:true
		},{
	    	id: 'fcreateTime', 
	    	dataIndex: 'fcreateTime', 
	    	hidden:true 
		},{
	    	id: 'isAdmin', 
	    	dataIndex: 'isAdmin', 
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
		},{
	    	id: 'flag', 
	    	dataIndex: 'flag', 
	    	hidden:true
		},{
	    	id: 'jsonStr', 
	    	dataIndex: 'jsonStr', 
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
    	id : 'grid',
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
    /************************************组织信息******************************/
    
    // render the grid to the specified div in the page
    if(getUser()[2]==1){
    	new Ext.Viewport({
        	renderTo:'mainDiv',
        	id:'viewport',
        	layout:'border',
        	items:[treePanelTuanti,treePanelHY,grid]
        });
    }else{
    	new Ext.Viewport({
        	renderTo:'mainDiv',
        	id:'viewport',
        	layout:'border',
        	items:[treePanelHY,grid]
        });
    }
    
    Ext.EventManager.onWindowResize(function(){ 
    	grid.getView().refresh();
	}) ;
    grid.on('dblclick',function(){ editData("detail")});
});
/**************************** 自定义元素 **********************************/
//excel模板设置
function mbsz(){
	var export_url = getPath() + '/member/download';
    window.location.href = export_url;
}
//导入
/*导入数据功能*/
function showImportExcel(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
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
	Ext.getCmp('importGroupId').setValue(window.parent.groupId);
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
	items : [{id:'importGroupId',name:'groupId',xtype:'hidden'},
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
						   url : getPath() + '/member/import',
		                   success:function(form, action){
		                	   var reloadUrl = window.location.href;
		                	   Ext.getCmp("showImportExcel").hide();
		                	   Ext.MessageBox.confirm('提示', '是否查看导入详情?', function(btn){
									if(btn=='yes'){
										var export_url = getPath() + 'member/downloadImportStatus';
									    window.location.href = export_url;
									}
								});
							    store.reload();
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
	var vExportContent = Ext.getCmp('grid').getExcelXml(); //获取数据
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

//省份
var ybasicProvinceStore = new Ext.data.Store({
	        proxy : new Ext.data.HttpProxy({
	                disableCaching : false,
	                method : 'post',
	                url : getPath() + '/province/findAllProvince'
            }),
	        reader : new Ext.data.JsonReader({root : 'obj'}, [{name : 'fid'}, {name : 'fname'}])
	});
ybasicProvinceStore.load();
var YBasicProvince = new Ext.form.ComboBox({
			fieldLabel : '省份',
	id : 'ybasicProvince',
	hiddenName : 'YBasicProvince.fid',
	typeAhead : true,
	triggerAction : 'all',
	lazyRender : true,
	mode : 'local',
	store : ybasicProvinceStore,
	valueField : 'fid',
	displayField : 'fname',
	emptyText : '请选择省份',
    blankText:'该字段不允许为空',
    allowBlank: false,
	listeners:{
        select:function(groupSelect){
        	ybasicCityStore.load({params:{id:groupSelect.getValue()}});
        	YBasicCity.focus();
		}
    }
});

//城市
var ybasicCityStore = new Ext.data.Store({
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
	store : ybasicCityStore,
	valueField : 'fid',
	displayField : 'fname',
	emptyText : '请选择城市',
    blankText:'该字段不允许为空',
    allowBlank: false,
	listeners:{
        select:function(groupSelect){
        	ybasicCountyStore.load({params:{id:groupSelect.getValue()}});
        	YBasicCounty.focus();
		}
    }
});

//区县
var ybasicCountyStore = new Ext.data.Store({
	        proxy : new Ext.data.HttpProxy({
	                disableCaching : false,
	                method : 'post',
            url : getPath() + '/county/findCountyHql'
    }),
    reader : new Ext.data.JsonReader({root : 'obj'}, [{name : 'fid'}, {name : 'fname'}])
	});
var YBasicCounty = new Ext.form.ComboBox({
			fieldLabel : '区(县)',
	id : 'ybasicCounty',
	hiddenName : 'YBasicCounty.fid',
	typeAhead : true,
	triggerAction : 'all',
	lazyRender : true,
	mode : 'local',
	store : ybasicCountyStore,
	valueField : 'fid',
	displayField : 'fname',
	emptyText : '请选择区县',
    blankText:'该字段不允许为空',
    allowBlank: false
});

//职位
var fPositionStore = new Ext.data.Store({
	autoLoad:true,
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'post',
		url : getPath() + '/pos/findAllPos'
	}),
	reader : new Ext.data.JsonReader({root:'obj.list'}, [{name : 'fid'}, {name : 'fname'}])
});
fPositionStore.on("beforeload", function() {
	this.baseParams = {
			orgId:window.parent.orgId,
			groupId:window.parent.groupId
	};
});
var myImage1 = new Ext.BoxComponent({
	fieldLabel:'logo',
    id : 'browseImage1',
    autoEl : {
        width : 60,
        height : 60,
        tag : 'img',
        src : Ext.BLANK_IMAGE_URL,
        style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
        complete : 'off',
        id : 'photo1'
    }
});
//会员信息表单
var editForm = new Ext.form.FormPanel({
     border : false,
     fileUpload : true,
     waitMsgTarget : true,
     enctype : 'multipart/form-data',
     id:'editZyxxForm',
	 frame	: true,
	 border:false,
	 labelAlign: 'left',
	 buttonAlign: 'center',
	 labelWidth:60,
	 autoScroll:false,
	 containerScroll: true,
	 width: 650,
     height: 240,
	 items:{
		layout:'column',
		items:[{
			 columnWidth: .33,
		     layout: 'form',
		     border: false,
		     defaults:{xtype:'textfield',anchor:'90%'},
		     items: [{id:'fid',name:'fid',xtype:'hidden'},{
			        fieldLabel:'编号',
			        id:'fnumber',
			        name:'fnumber',
			        xtype:'hidden'
			 },{
			        fieldLabel:'名称',
			        id:'fname',
			        name:'fname',
			        maxLength:12,
		            blankText:'该字段不允许为空',
		            allowBlank: false
			 },{
			        fieldLabel:'昵称',
			        id:'fnickName',
			        name:'fnickName',
			        maxLength:12/*,
		            blankText:'该字段不允许为空',
		            allowBlank: false*/
			 },{
				 	xtype:'hidden',
			        fieldLabel:'密码',
			        id:'fpassword',
			        name:'fpassword',
			        maxLength:12,
		            blankText:'该字段不允许为空',
		            allowBlank: false
			 },{
				 	xtype:'radiogroup',
				 	layout:'column',
				 	fieldLabel:'性别',
				 	id:'fsex',
				 	columns:2,
				 	items:[{boxLabel: "先生", name: 'fsex',inputValue: 0,checked:true},  
				 	       {boxLabel: "女士", name: 'fsex',inputValue: 1}],
    	            blankText:'该字段不允许为空',
    	            allowBlank: false
			 },{
			        xtype:'datefield',
			        fieldLabel:'出生年月',
			        format:"Y-m-d H:i:s",
			        id:'fbirthday',
			        name:'fbirthday',
			        editable:false
		     },{
				 	xtype:'radiogroup',
				 	layout:'column',
				 	fieldLabel:'类型',
				 	id:'ftypeId',
				 	columns:2,
				 	items:[{boxLabel: "会员", name: 'ftypeId',inputValue: 1,checked:true},  
				 	       {boxLabel: "游客", name: 'ftypeId',inputValue: 0}],
    	            blankText:'该字段不允许为空',
    	            allowBlank: false
             },{
				 	xtype:'radiogroup',
				 	layout:'column',
				 	fieldLabel:'是否为管理员',
				 	id:'isAdmin',
				 	columns:2,
				 	items:[{boxLabel: "是", name: 'isAdmin',inputValue: 1},  
				 	       {boxLabel: "否", name: 'isAdmin',inputValue: 0,checked:true}],
    	            blankText:'该字段不允许为空',
    	            allowBlank: false
             },{
				 	xtype:'radiogroup',
				 	layout:'column',
				 	fieldLabel:'是否隐藏电话',
				 	id:'fisHidePhone',
				 	columns:2,
				 	items:[{boxLabel: "是", name: 'fisHidePhone',inputValue: 0},  
				 	       {boxLabel: "否", name: 'fisHidePhone',inputValue: 1,checked:true}],
    	            blankText:'该字段不允许为空',
    	            allowBlank: false
             	}
		 ]},{ 
			 columnWidth: .33,
             layout: 'form',
             border: false,
             defaults:{xtype:'textfield',anchor:'90%'},
             items: [{
			        fieldLabel:'邮件',
			        id:'femail',       	
			        name:'femail',
			        maxLength:24,/*
    	            blankText:'该字段不允许为空',
    	            allowBlank: false,*/
    	            vtype:'email'
				},{
				        fieldLabel:'备用邮件',
			            id:'fpreviousEmail',
				        name:'fpreviousEmail',
				        maxLength:24,
				        vtype:'email'
				},YBasicProvince,YBasicCity,YBasicCounty,{
			        fieldLabel:'籍贯',
			        id:'fnativePlace',
			        name:'fnativePlace'
             	},{
			        fieldLabel:'地址',
			        id:'fsite',
			        name:'fsite'
             	},{
			        fieldLabel:'收货地址',
			        id:'freceivingAddress',
			        name:'freceivingAddress',
			        xtype:'hidden'
             	},{
				 	xtype:'hidden',
				 	fieldLabel:'是否修改原始密码',
				 	id:'fisOriginalPassword',
				 	name: 'fisOriginalPassword',
				 	value:0,
    	            blankText:'该字段不允许为空',
    	            allowBlank: false
             	},{
			        fieldLabel:'备注',
			        id:'fcomment',       	
			        name:'fcomment',
			        maxLength:200
 
             	}]
		 },{
			 columnWidth: .33,
		     layout: 'form',
		     border: false,
		     defaults:{xtype:'textfield',anchor:'90%'},
		     items: [myImage1,new Ext.ux.form.FileUploadField({
	 		    	id : 'file',
			    	name : 'file',
			    	emptyText: 'Select an image',
					width : 150,
					buttonText: '浏览....'
				 }),{
		    	 	xtype:'hidden',
		    	 	id:'fheadImage',
		    	 	name:'fheadImage'
		     	 },{
			        fieldLabel:'家庭电话',
		            id:'fhomePhone',
			        name:'fhomePhone',
			        regex: /^((\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/,
		        	regexText:'格式不正确'
			 	 },{
			        fieldLabel:'手机',
		            id:'fmobilePhone',
			        name:'fmobilePhone',
			        regex : /^1+\d{10}$/,
			        regexText:"格式不正确!", //正则表达式错误提示  
			        allowBlank:false,
			        blankText:'该字段不允许为空'
			 	},{
			        fieldLabel:'备用手机',
		            id:'fsecondPhone',
			        name:'fsecondPhone',
			        regex : /^1+\d{10}$/,
			        regexText:"格式不正确!" //正则表达式错误提示  
			 	},{
             		fieldLabel:'来源',
        	        id:'fsource',
        	        name:'fsource',
        	        readOnly:true
                },{
				 	xtype:'hidden',
			        fieldLabel:'创建人ID',
		            id:'fcreaterId',
			        name:'fcreaterId'
			 },{
				 	xtype:'hidden',
			        fieldLabel:'创建时间',
		            id:'fcreateTime',
			        name:'fcreateTime'
			 },{
				 	xtype:'hidden',
			        fieldLabel:'修改人ID',
		            id:'fmodifiedId',
			        name:'fmodifiedId'
			 },{
				 	xtype:'hidden',
			        fieldLabel:'修改时间',
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
			 },{
	     	     xtype:'hidden',
	    	     id:'jsonStr',
	    	     name:'jsonStr'
            },{
     	        xtype:'hidden',
    	        id:'flag',
    	        name:'flag'
            },{
     	        xtype:'hidden',
    	        id:'YBasicSocialgroups',
    	        name:'YBasicSocialgroups.fid'
            },{
     	        xtype:'hidden',
    	        id:'YSystemUsers',
    	        name:'YSystemUsers.fid'
            },{
     	        xtype:'hidden',
    	        id:'fbillState',
    	        name:'fbillState'
            }]}]
	}
});

/***********************************职位分录信息****************************/
//职位
var fposition = new Ext.form.ComboBox({
	fieldLabel : "职位",
	store : fPositionStore,
	id:'yBasicPosition',
	hiddenName:'YBasicPosition.fid',
	triggerAction : 'all',
	lazyInit : true,
	valueField : 'fid',
	displayField : 'fname',
	selectOnFocus:true,
	editable:false,
	mode:'local',
	emptyText:'请选择职位',
	blankText:'职位不能为空',
	allowBlank:false
});

var order_sm = new Ext.grid.CheckboxSelectionModel({handleMouseDown: Ext.emptyFn});
var order_cm = new Ext.grid.ColumnModel([
   	new Ext.grid.RowNumberer(),
   		order_sm,	
   	  {header:'ID',dataIndex:'fid',hidden:true,editor: {xtype:'hidden',name:'fid'}},
      {header:'主要职位',dataIndex:'fkeyPost',editor: {xtype:'radio',name:'mainJob'},blankText:'请选择是否为主要职位',allowBlank:false},
      {header:'职位',dataIndex:'ybasicPosition.fid',editor: fposition,
    	  renderer: function(value, cellmeta, record,rowIndex,columnIndex) {
              var index = fPositionStore.find(Ext.getCmp('yBasicPosition').valueField,value);
              var record = fPositionStore.getAt(index);
              var returnvalue = "";
              if (record) {
                 returnvalue = record.data.fname;
              }
              return returnvalue;//注意这个地方的value是上面displayField中的value
        }
      },{
    	dataIndex:'flag',hidden:true,editor: {xtype:'hidden',name:'flag'}  
      }
     ]);

var proxy = new Ext.data.MemoryProxy(jsonStr); 
var order_store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
		root:'obj'
	},[{name: 'fid'},
       {name: 'fkeyPost'},
       {name: 'ybasicPosition.fid'},
       {name: 'flag'}
      ]),
	proxy:new Ext.data.HttpProxy({
			disableCaching :false,
			url : getPath() + '/member/findMDByMemID'
	}),
	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});


var order_grid = new Ext.grid.EditorGridPanel({
	height: 170,
	id:'order_grid',
	title:'职位信息',
	anchor:'100%',
    style:'margin-bottom: 6px;',
    stripeRows: true,   //每行交替颜色
    loadMask: true,     //加载时提示load...
    store: order_store,
    clicksToEdit:1,
    region : 'center', 
	border: false,
	columnLines:true,
	frame:false,
	monitorResize: true,
	autoScroll:true,
    selModel: new Ext.grid.RowSelectionModel({singleSelect: false}), //设置为单行选中
    cm: order_cm,
    sm:order_sm,
    viewConfig:{
        forceFit: true
    },
    tbar: new Ext.Toolbar([
        '-',
        {
            iconCls : 'common_add',
            text : '新增',
            handler: function(){
                var n = order_grid.getStore().getCount();// 获得总行数
                var Plant = order_grid.getStore().recordType;
                var r = new Plant({
                	flag:''
                });
                order_grid.stopEditing();
                order_store.insert(n,r);
                order_grid.startEditing(n, 2);// 开始编辑2单元格
            }
        },
        '-',
        {
            iconCls : 'common_delete',
            text : '删除',
            id : 'deleteColumn',
            handler: function() {
                var rows = order_grid.getSelectionModel().getSelections();// 返回值为 Record 数组
                if(rows.length == 0){
                    alertError("请选择要删除的数据", 'deleteColumn');
                }
                else{
                    for(var i = 0; i<rows.length; i++){
                    	order_store.remove(rows[i]);
                    }               
                }
            } 
        }
    ])
});

//添加监听
order_grid.addListener('afteredit', setOrg);          
function setOrg(e){
	
	//主要职员编辑列
	if(e.column == 3){
		var fkeyPost = order_grid.getColumnModel().getDataIndex(3);	//获取列
		var record = order_grid.getStore().getAt(e.row);	//获取当前编辑的行
		
		//判断当前编辑列，如果是选中，则为true
		if(record.get(fkeyPost)){
			var records = order_grid.getStore();	//获取grid所有行
			
			//遍历设置其它行该列的值为false
			for(var i = 0; i < records.getCount(); i++){
				if(e.row == i){
					records.getAt(i).set(fkeyPost,'是');
				}else{
					records.getAt(i).set(fkeyPost,'否');
				}
			}
		}else{
			record.set(fkeyPost,'否');
			return;
		}
    }
	
	//职位编辑列
     if(e.column == 4){
         var record = order_grid.getStore().getAt(e.row);
         var position = order_grid.getColumnModel().getDataIndex(4); 
         var organization = order_grid.getColumnModel().getDataIndex(5); 
         var pValue = record.get(position);
         
         Ext.Ajax.request({
        		//url : getPath() + 'base/position!doNotNeedSessionAndSecurity_findPositionById.ad',
        		params : {id : pValue},
        		success : function(response, options) {
        			var txt = Ext.util.JSON.decode(response.responseText);
        			record.set(organization, txt.obj.yBasicOrganization.fid);
        		}
        });
     }
}
var changepwd = new Ext.form.FormPanel({
    baseCls: 'x-plain',
    labelWidth: 100,
    labelAlign: 'right',
    border: true,
	frame: true,
    defaultType: 'textfield',
    items: [{id:'fid',name:'fid',hidden:true},{
			        fieldLabel: '新密码',
			        id:'newpwd',
			        name: 'newpwd',
			        inputType : 'password',
			        allowBlank:false,
			        blankText:'不能为空'
        	}]
});

	
//
/**************************** 自定义元素结束 *******************************/
/**************************** 操作 **********************************/
function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	//通过id获取输入的数据
	var searchCondition = Ext.getCmp("searchCondition").getValue();
	//模糊查询
	store.load({params:{searchName:searchCondition,start:0,limit:LIMIT}});
}
function updatePass(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	var row = Ext.getCmp('grid').getSelectionModel().getSelected();
	if(row){
		if(!hyglPassWin){
			hyglPassWin = new Ext.Window({
			    width: 320,
			    height: 100,
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
										url : getPath() + '/member/updateMemberPassWord',
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
	}else{
		alertWarring("请选择需要修改密码的用户!");return;
	}
	Ext.getCmp("fid").setValue(row.get("fid"));
	hyglPassWin.show();
}
//职员新增窗口
function editData(row){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	var detail = row;
	if(row!='add' && row!='detail'){
		row = Ext.getCmp('grid').getSelectionModel().getSelected();
	     if(!row){
	    	 alertWarring("请选择需要编辑的职员!");return;
	     }
	     var billState = row.get("fbillState");
	     if(billState == 9){
				alertError("该信息已作废，无法进行操作！");return;
		 }
	}else if(row =='detail'){
		row = Ext.getCmp("grid").getSelectionModel().getSelected();
	}else{
		getNumberStore();
		editForm.form.reset();
		jsonStr=[];
		Ext.getCmp("YSystemUsers").setValue(getUser()[1]);
		Ext.getCmp("fsource").setValue("管理员录入");
		Ext.getCmp("YBasicSocialgroups").setValue(window.parent.groupId);
	}
    	this.row = row;
    	var that = this;
         if(!editWin){
            editWin = new Ext.Window({
		        id: 'editWin',
		        layout:'fit',
                width:650,
                height:480,
                frame:true,
                border:false,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
			    items :[{layout:'vbox',items:[editForm,order_grid]}],
			    buttons:['默认密码: 888888',{
				    text: '保存',
				    handler:function(){
				    	var logourl = Ext.getCmp('file').getValue();
						if(logourl !="" && !img_reg.test(logourl)){
							alertWarring("文件格式不正确！");return;
						}
						var isAdmin = Ext.getCmp('isAdmin').getValue();
						var email = Ext.getCmp('femail').getValue();
						
						if(isAdmin==1 && email.length==0){
							alertWarring("申请管理,邮箱为必填！");return;
						}
				        if(editForm.form.isValid()){
				        	 var n = order_grid.getStore().getCount();
				        	 jsonStr='[';
							 for(var i =0;i<n;i++){
								   var fkeyPost = order_grid.getStore().getAt(i).get("fkeyPost");
								   var yBasicPosition = order_grid.getStore().getAt(i).get("ybasicPosition.fid");
								   var flag = order_grid.getStore().getAt(i).get("flag");
								   var obj = [fkeyPost,yBasicPosition];
								   if(isNull(obj)){
									   jsonStr+= '{\"fid\":\"'+order_grid.getStore().getAt(i).get("fid")+'\",\"fkeyPost\":\"'
									   +fkeyPost+'\",\"yBasicPosition.fid\":\"'+yBasicPosition+'\",\"flag\":\"'+flag+'\"},'
								   }else{
									   jsonStr ="[]";
									   break;
								   }
							}
							if(jsonStr != "["){
								jsonStr = jsonStr.substring(0, jsonStr.length-1) + ']';
							}
						    if(jsonStr.length <3){
							   alertWarring("职位信息填写不完整");return;
						    }
							editForm.getForm().findField('jsonStr').setValue(jsonStr);
							   
				        	  editForm.form.submit({
				                   method:'POST',
				                   waitMsg:'正在提交.....',
								   waitTitle:'请稍等',
								   onSubmit: function(){  
									    return $(this).form('validate');  
							       },
				                   success:function(form, action){
				                	   	 jsonStr = [];
					                  	 Ext.getCmp("grid").store.reload();
					                  	 treePanelHY.root.reload();
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
			    listeners:{
			    	hide:function(){
			    		Ext.getCmp('browseImage1').getEl().dom.src =Ext.BLANK_IMAGE_URL;
			    		jsonStr = [];
						order_store.removeAll();
			    		editForm.getForm().reset();
			    		Ext.getCmp("fmobilePhone").setReadOnly(false);
			    	}
	    	 }
            });
		}
         
        setTitleAndUrl(row, editWin);

	    var date = new Date();  // 得到系统日期
	    var dateString = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	    editForm.getForm().findField('fcreateTime').setValue(dateString);
	    editForm.getForm().findField('fcreaterId').setValue(getUser()[1]);
	    editForm.getForm().findField('fbillState').setValue(5);
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
	    	editForm.getForm().findField('fmodifiedTime').setValue(dateString);
		    editForm.getForm().findField('fmodifiedId').setValue(getUser()[1]);
		    editForm.getForm().findField('flastModifiedTime').setValue(dateString);
		    editForm.getForm().findField('flastModifiedId').setValue(getUser()[1]);
	      	loadData(row);
		}else{
			var tool = Ext.getCmp('editWin').buttons;
			for(var i =0;i<tool.length;i++){
				tool[i].setVisible(true);
			}
		}
	}

function loadData(row) {

	order_store.reload({params : {fid : row.get("fid")}});
	
	editForm.form.setValues({
		fid             	: row.get('fid'),
		fnumber 			: row.get('fnumber'),
		fname    			: row.get('fname'),
		fnickName			: row.get('fnickName'),
		fheadImage  		: row.get('fheadImage'),
		fpassword  			: row.get('fpassword'),
		fsex  				: row.get('fsex'),
		fbirthday   		: row.get('fbirthday'),
		femail  			: row.get('femail'),
		fpreviousEmail  	: row.get('fpreviousEmail'),
		fhomePhone  		: row.get('fhomePhone'),
		fmobilePhone  		: decryptByABC(row.get('fmobilePhone')),
		fsecondPhone 		: row.get('fsecondPhone'),
		fsite  				: row.get('fsite'),
		fcomment  			: row.get('fcomment'),
		freceivingAddress  	: row.get('freceivingAddress'),
		fnativePlace  		: row.get('fnativePlace'),
		ftypeId  			: row.get('ftypeId'),
		fisHidePhone  		: row.get('fisHidePhone'),
		fisOriginalPassword : row.get('fisOriginalPassword'),
		isAdmin  			: row.get('isAdmin'),
		jsonStr 			: row.get('jsonStr'),
		fcreaterId			: row.get('fcreaterId'),
		fcreateTime			: row.get('fcreateTime'),
		flag				: row.get('flag'),
		fbillState  		: row.get('fbillState'),
		YSystemUsers		: row.get('ysystemUsers') == null ? "" : row.get('ysystemUsers').fid,
		YBasicSocialgroups	: row.get('ybasicSocialgroups').fid
		
	});
	Ext.getCmp("fmobilePhone").setReadOnly(true);
	if(row.get('ybasicProvince')){
		editForm.form.setValues({
			ybasicProvince			: row.get('ybasicProvince').fid
		});
		if(row.get('ybasicCity')){
			ybasicCityStore.load({
				params:{id:row.get('ybasicProvince').fid},
				callback:function(){
					editForm.form.setValues({
						ybasicCity			: row.get('ybasicCity').fid
					});
					
					if(row.get('ybasicCounty')){
						ybasicCountyStore.load({
							params:{id:row.get('ybasicCity').fid},
							callback:function(){
								editForm.form.setValues({
									ybasicCounty		: row.get('ybasicCounty').fid
								});
							}
						});
					}
				}
			});
		}
	}
	if(row.get('fheadImage') &&  row.get('fheadImage').length>0){
		var imgLength = row.get('fheadImage').split(',');
	    for(var i =0;i<imgLength.length;i++){
	    	if(i==imgLength.length-1)return;
	        photoSrc(imgLength[i],(i+1));
	    }
    }
}
function setTitleAndUrl(row, win) {
	if(row == 'add') {
 		win.setTitle('增加会员信息');
 		editForm.form.url = getPath()+'/member/saveMemberTwo';
	} else {
 		win.setTitle('修改会员信息');
 		editForm.form.url = getPath()+'/member/updateMemberTwo';
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
					url : getPath()+'member/Invalid',
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

/*设置职员状态*/
function setState(stateId){
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
				 }
		  }
		Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认要更新当前会员状态!",
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
		alertWarring("请选择需要更新的会员!");
	}
}
function gridSubAjax(param,stateId)
{
	Ext.Ajax.request({
		url : getPath() + '/member/updateMemStatus',
		params : {fids : param , status : stateId},
		success: function (result, request) 
		{
			Ext.MessageBox.show({
				title : "提示",
				msg : Ext.util.JSON.decode(result.responseText).msg,
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

//图片上传成功 给产品图片文本，预览img赋值
function photoSrc(src,index){
    if(Ext.isIE){
        var image = Ext.getCmp('browseImage'+index).getEl().dom;
        image.src = Ext.BLANK_IMAGE_URL;
        image.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod = scale)";
        image.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = src;
    }else{ // 支持FF
        Ext.getCmp('browseImage'+index).getEl().dom.src =src;
    }
}

function getNumberStore(){
	Ext.Ajax.request({
		url:getPath() + '/serialNumber/getSerialNumber',
		params:{prefix:'HY-HYXX'},
		success:function(response, options) {
			Ext.getCmp("fnumber").setValue(Ext.util.JSON.decode(response.responseText).serialNumber);
		}
	});
}

treePanel2.on('click',function(n){
	window.parent.groupId = n.attributes.id;
	fPositionStore.load({params:{groupId:window.parent.groupId}});
});

/***********************************添加公司信息相关**********************************/

var product_sm = new Ext.grid.CheckboxSelectionModel({handleMouseDown: Ext.emptyFn});
var product_cm = new Ext.grid.ColumnModel([
   	new Ext.grid.RowNumberer(),
   	product_sm,	
   	  {header:'ID',dataIndex:'fid',hidden:true,editor: {xtype:'hidden',name:'fid',value:'1'}},
      {header:'产品名称',dataIndex:'fname',editor: {xtype:'textfield',name:'fname',maxLength:50}},
      {header:'产品图片',dataIndex:'flogoImage',editor: new Ext.form.FileUploadField({name:'flogoImage',emptyText: 'Select an image',buttonText: '浏览'})},
      {header:'产品描述',dataIndex:'fdescription',editor: {xtype:'textfield',name:'fdescription'}},
      {header:'flag',dataIndex:'flag',hidden:true,editor: {xtype:'hidden',id:'fcflag',name:'flag'}  
      }
     ]);

var product_store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
		root:'obj'
	},[{name: 'fid'},
       {name: 'fname'},
       {name: 'flogoImage'},
       {name: 'fdescription'},
       {name:'flag'}
      ]),
	proxy:new Ext.data.HttpProxy({
			disableCaching :false,
			url : getPath() + '/member/findMDByMemID'
	}),
	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});


var product_grid = new Ext.grid.EditorGridPanel({
	height: 170,
	region:'center',
	id:'product_grid',
	title:'主营产品信息',
	anchor:'100%',
    style:'margin-bottom: 6px;',
    stripeRows: true,   //每行交替颜色
    loadMask: true,     //加载时提示load...
    store: product_store,
    clicksToEdit:1,
	border: false,
	columnLines:true,
	frame:false,
	monitorResize: true,
	autoScroll:true,
    selModel: new Ext.grid.RowSelectionModel({singleSelect: false}), //设置为单行选中
    cm: product_cm,
    sm:product_sm,
    viewConfig:{
        forceFit: true
    },
    tbar: new Ext.Toolbar(['-',{
            iconCls : 'common_add',
            text : '新增',
            handler: function(){
                var n = product_grid.getStore().getCount();// 获得总行数
                var Plant = product_grid.getStore().recordType;
                var r = new Plant({
                	fid: '',
                	fname: '',
                	//flogoImage: '',
                	fdescription:'',
                	flag:''
                });
                product_grid.stopEditing();
                product_store.insert(n,r);
                product_grid.startEditing(n, 2);// 开始编辑2单元格
            }
        },'-',{
            iconCls : 'common_delete',
            text : '删除',
            id : 'deleteColumn',
            handler: function() {
                var rows = product_grid.getSelectionModel().getSelections();// 返回值为 Record 数组
                if(rows.length == 0){
                    alertError("请选择要删除的数据", 'deleteColumn');
                }
                else{
                    for(var i = 0; i<rows.length; i++){
                    	product_store.remove(rows[i]);
                    }               
                }
            } 
        }
    ])
});

//行业
var tradeStore = new Ext.data.Store({
	autoLoad:true,
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'post',
		url : getPath() + '/trade/findAllTrade'
	}),
	reader : new Ext.data.JsonReader({root:'obj.list'}, [{name : 'fid'}, {name : 'fname'}])
});

//产业
/*var industryStore = new Ext.data.Store({
	autoLoad:true,
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'post',
		url : getPath() + '/ind/findAllInd'
	}),
	reader : new Ext.data.JsonReader({root:'obj.list'}, [{name : 'fid'}, {name : 'fname'}])
});*/

var editCompanyForm = new Ext.form.FormPanel({
	border : false,
    fileUpload : true,
    id:'editCompanyForm',
    waitMsgTarget : true,
    enctype : 'multipart/form-data',
	frame	: true,
	labelAlign: 'left',
	labelWidth:60,
	autoScroll:true,
	containerScroll: true,
	width: 600,
	items:[{
		layout:'column',
		items:[{
			 columnWidth: .5,
		     layout: 'form',
		     border: false,
		     defaults:{xtype:'textfield',anchor:'92%'},
		     items:[{fieldLabel:'ID',id:'fcid',name:'fcid',xtype:'hidden'},
		            {
				        fieldLabel:'公司名称',
				        id:'fcname',
				        name:'fcname',
					    maxLength:50,
						blankText : '该字段不允许为空',
						allowBlank : false
					 },{
				        fieldLabel:'公司电话',
				        id:'fctelphone',
				        name:'fctelphone',
				        regex : /^((1+\d{10})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/,
					    maxLength:50
					 },{
				        fieldLabel:'邮箱',
						vtype: 'email',
				        id:'fcemail',
				        name:'fcemail',
						maxLength:48
					 },{
						fieldLabel:'公司职务',
						id:'fcompanyPosition',
						name:'fcompanyPosition',
						maxLength:50
					 },{
						fieldLabel:'公司性质',
						id:'fcompanyNature',
						name:'fcompanyNature',
						maxLength:50
					 },{
						fieldLabel:'公司规模',
						id:'fcompanyScale',
						name:'fcompanyScale',
						maxLength:50
					 }]
		     },{ 
				 columnWidth: .5,
		         layout: 'form',
		         border: false,
		         defaults:{xtype:'textfield',anchor:'92%'},
		         items: [{
					        fieldLabel:'网址',
					        id:'fcurl',
					        name:'fcurl',
							maxLength:70
					 	},{
					        fieldLabel:'公司地址',
					        id:'fclocation',
					        name:'fclocation',
							maxLength:100
						 },{
					        fieldLabel:'主营产品',
					        id:'fcproducts',
					        name:'fcproducts',
					        hidden:true
						 },/*{
							xtype : 'combo',
							fieldLabel : '产业',
							id : 'ybasicIndustry',
							hiddenName : 'YBasicIndustry.fid',
							triggerAction : 'all',
							lazyInit : true,
							mode : 'local',
					        store: industryStore,
					        valueField : 'fid',
					        displayField : 'fname',
					        selectOnFocus : true,
					        allowBlank: false,
					        editable : false,
					        emptyText : '请选择产业'
						 },*/{
							xtype : 'combo',
							fieldLabel : '行业',
							id : 'ybasicTrade',
							hiddenName : 'YBasicTrade.fid',
							triggerAction : 'all',
							lazyInit : true,
							mode : 'local',
					        store: tradeStore,
					        valueField : 'fid',
					        displayField : 'fname',
					        selectOnFocus : true,
					        allowBlank: false,
					        editable : false,
					        emptyText : '请选择行业'
					    },
					    {
					    	xtype:'hidden',
							fieldLabel:'会员',
					        id:'ybasicMember',
					        name:'YBasicMember.fid'
						 },{
							 xtype:'hidden',
							fieldLabel:'团体',
					        id:'ybasicSocialgroups',
					        name:'YBasicSocialgroups.fid'
						 },
						 {
							xtype:'hidden',
							fieldLabel:'公司经纬度',
							id:'fcompanyLatitude',
							name:'fcompanyLatitude'
						 },{
						 	xtype:'textarea',
					        fieldLabel:'公司简介',
					        id:'fcintroduction',
					        name:'fcintroduction',
					        height:50
						 },{
							 xtype:'hidden',
						        fieldLabel:'flag',
						        id:'flag',
						        name:'flag',
						        height:50
							 }
				 ]
		 }]
	},{
		layout:'column',
		border:true,
		labelWidth:40,
		items:[{
				 columnWidth: .2,
			     layout: 'form',
			     border: false,
			     items:[{
			 		xtype:'button',
					text:'添加主营产品',
					listeners:{click:function(){
							i++;
							addPt(i);
						}
					}
				}]
		     },{
				 columnWidth: .2,
			     layout: 'form',
			     border: false,
			     items:[{
			 		xtype:'button',
					text:'依次移除主营产品',
					listeners:{click:function(){
							if(i>0){
								Ext.getCmp('editCompanyForm').remove('items'+i,true);
								i--;
								Ext.getCmp('editCompanyForm').doLayout();
							}
						}
					}
				}]
		     }]
	},{
		layout:'column',
		border:true,
		id:'items0',
		labelWidth:40,
		items:[{
				 columnWidth: .4,
			     layout: 'form',
			     border: false,
			     defaults:{xtype:'textfield',anchor:'92%'},
			     items:[
			            {xtype:'hidden',id:'fid0',name:'fid',value:""},{xtype:'hidden',id:'flogoImage0',name:'flogoImage',value:""},
			            {fieldLabel:'名称',id:'fcname0',name:'fname',xtype:'textfield',allowBlank: false,maxLength:50},
			            {fieldLabel:'图片',id:'file0',name:'file0',xtype:'fileuploadfield',buttonText: '浏览..'}]
		     },{
				 columnWidth: .6,
			     layout: 'form',
			     border: false,
			     defaults:{xtype:'textfield',anchor:'92%'},
			     items:[
			            {fieldLabel:'描述',id:'fdescription0',name:'fdescription',value:"",height:45,xtype:'textarea'},
			            {fieldLabel:'flag',id:'flag0',name:'flag',value:"",xtype:'hidden'}
			     ]
		     }]
	}]
});


function companyData(){
 	editCompanyForm.getForm().reset();
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	var row = Ext.getCmp('grid').getSelectionModel().getSelected();
	if(row){
		this.row = row;
    	var that = this;
        if(!editCompanyWin){
        	 editCompanyWin = new Ext.Window({
		        id: 'editCompanyWin',
		        layout:'fit',
                width:600,
                height:360,
                frame:true,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
			    items :[editCompanyForm],
			    listeners:{
			    	hide:function(){
			    	 	for(var j =i;j>0;j--){
			    	 		Ext.getCmp('editCompanyForm').remove('items'+j,true);
			    	 	}
			    	 	i=0;
			    		Ext.getCmp('editCompanyForm').doLayout();
		    		}
			    },
			    buttons:[{
						    text: '保存',
						    handler:function(){
						    	if(editCompanyForm.form.isValid()){
						    		//设置会员和团体ID
						    		Ext.getCmp('ybasicMember').setValue(row.get('fid'));
						    		Ext.getCmp('ybasicSocialgroups').setValue(window.parent.groupId);
						    		for(var descCount = 0;descCount<=i;descCount++){
						    			var desc = Ext.getCmp("fdescription"+descCount);
						    			if(desc.getValue().length==0){
						    				desc.setValue("null");
						    			}
						    		}
							    	editCompanyForm.form.submit({
					                   method:'POST',
					                   waitMsg:'正在提交.....',
									   waitTitle:'请稍等',
									   
					                   success:function(form, action){
						                	 Ext.getCmp("editCompanyWin").hide();
					                   },
					                   failure:function(form, action){
							                      alertWarring(action.result.msg);
					                   }
							    	});
						    	}
						    }
						 },{
							 text:'取消',
						      handler:function(){
							 	 Ext.getCmp('editCompanyWin').hide();
						      } 
						}]
            });
		}

        editCompanyWin.setTitle('编辑公司信息');
        
        /****************通过会员id获取公司信息*************/
        Ext.Ajax.request({
    		url:getPath() + '/com/getComByMemberID',
    		params:{fid:row.get("fid")},
    		success:function(response, options) {
    			var data=Ext.util.JSON.decode(response.responseText);
    			//有公司信息则为修改
    			if(data.obj){
    				var com = data.obj;
    		 		editCompanyForm.form.url = getPath()+'/com/updateCom';
    		 		
    		 		if(com.ybasicTrade && com.ybasicTrade.fid){
    		 			editCompanyForm.form.setValues({
    		 				ybasicTrade			: com.ybasicTrade.fid
    		 		    });
    		 		}
    		 		/*if(com.ybasicIndustry && com.ybasicIndustry.fid){
    		 			editCompanyForm.form.setValues({
    		 				ybasicIndustry		: com.ybasicIndustry.fid
    		 		    });
    		 		}*/
    		 		editCompanyForm.form.setValues({
    		 			fcid 				: com.fcid,
    		 			fcname 				: com.fcname,
    		 			fctelphone 			: com.fctelphone,
    		 			fcemail 			: com.fcemail,
    		 			fcurl 				: com.fcurl,
    		 			fcompanyPosition 	: com.fcompanyPosition,
    		 			fcompanyNature		: com.fcompanyNature,
    		 			fcompanyScale		: com.fcompanyScale,
    		 			fclocation			: com.fclocation,
    		 			fcproducts			: com.fcproducts,
    		 			fcompanyLatitude	: com.fcompanyLatitude,
    		 			fcintroduction		: com.fcintroduction,
    		 			flag				: com.flag
					});
    			}else{
    		 		editCompanyForm.form.url = getPath()+'/com/saveCom';
    		 		editCompanyForm.form.setValues({
    		 			fcid 				: "",
    		 			fcname 				: "",
    		 			fctelphone 			: "",
    		 			fcemail 			: "",
    		 			fcurl 				: "",
    		 			fcompanyPosition 	: "",
    		 			fcompanyNature		: "",
    		 			fcompanyScale		: "",
    		 			fclocation			: "",
    		 			fcproducts			: "",
    		 			/*ybasicIndustry		: "",*/
    		 			ybasicTrade			: "",
    		 			fcompanyLatitude	: "",
    		 			fcintroduction		: "",
    		 			fid0				: "",
						flogoImage0			: "",
						fcname0				: "",
						fdescription0		: "",
						flag				: ""
					});
    			}
    			
    			if(data.ptList && data.ptList.length>0){
    				var ptlist = data.ptList;
    				for(i;i<ptlist.length;i++){
    					var ptInfo = ptlist[i];
    					if(i==0){
    						editCompanyForm.form.setValues({
    							fid0			: ptInfo.fid,
    							flogoImage0		: ptInfo.flogoImage,
    							fcname0			: ptInfo.fname,
    							fdescription0	: ptInfo.fdescription
    						//	flag			: ptInfo.flag
    						});
    					}else{
    						addPt(i,ptInfo);
    					}
    				}
    				i = i == 0 ? 0 : i - 1;
    			}
    			editCompanyWin.show();
    		},failure:function(form, action){
                alertWarring(action.result.msg);
        	}
    	});
	}else{
		alertWarring("请选中会员列!");
	}
}

//动态添加主营产品
function addPt(row,ptInfo){
	Ext.getCmp('editCompanyForm').add({
		layout:'column',
		id:'items'+row,
		border:true,
		labelWidth:40,
		items:[{
				 columnWidth: .4,
			     layout: 'form',
			     border: false,
			     defaults:{xtype:'textfield',anchor:'92%'},
			     items:[
			            {xtype:'hidden',id:'fid'+row,name:'fid',value:ptInfo?ptInfo.fid:undefined},
			            {fieldLabel:'图片',id:'flogoImage'+row,name:'flogoImage',value:ptInfo?ptInfo.flogoImage:"",xtype:'hidden'},
			            {fieldLabel:'名称',id:'fcname'+row,name:'fname',xtype:'textfield',value:ptInfo?ptInfo.fname:"",allowBlank: false,maxLength:50},
			            {fieldLabel:'图片',id:'flogofile'+row,name:'file'+row,xtype:'fileuploadfield',buttonText: '浏览..'}
		            ]
		     },{
				 columnWidth: .6,
			     layout: 'form',
			     border: false,
			     defaults:{xtype:'textfield',anchor:'92%'},
			     items:[
			            {fieldLabel:'描述',id:'fdescription'+row,name:'fdescription',value:ptInfo?ptInfo.fdescription:"",height:45,xtype:'textarea'}
			     ]
		     }]
	});
	Ext.getCmp('editCompanyForm').doLayout();
}

function hideTel(type){
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  var fids=new Array();
		  for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
			  var hideTel = records[i].get('fisHidePhone');
			  if(hideTel==1){
				  alertError("该会员电话号码已设置为隐藏!");return;
			  }
		  }
		  Ext.Ajax.request({
				url : getPath() + '/member/updateMembersHideStatus',
				params : {fids : fids.toString(),status:type},
				success: function (result, request) 
				{
					//var msg = eval(result.responseText);
					Ext.MessageBox.show({
						title : "提示",
						msg : "成功！",
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
			msg: "请选择需要隐藏电话的会员信息!",
			width: 250,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}

treePanel2.on('click',function(n){
	window.parent.groupId = n.attributes.id;
	treePanelHY.loader.dataUrl = getPath() + 'pos/getPositionTree?groupId='+window.parent.groupId;
	treePanelHY.root.reload();
});