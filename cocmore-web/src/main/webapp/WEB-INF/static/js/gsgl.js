var LIMIT = 26;var editWin;var i =0;
var records = new Ext.data.Record.create([
                                           {name: 'fcid'},
								           {name: 'ybasicIndustry'},
								           {name: 'ybasicMember'},
								           {name: 'ybasicTrade'},
								           {name: 'ybasicSocialgroups'},
								           {name: 'fcname'},
								           {name: 'fctelphone'},
								           {name: 'fcemail'},
								           {name: 'fcurl'},
								           {name: 'fclocation'},
								           {name: 'fcproducts'},
								           {name: 'fcintroduction'},
								           {name: 'fcompanyPosition'},
								           {name: 'fcompanyNature'},
								           {name: 'fcompanyScale'},
								           {name: 'fcompanyLatitude'},
								           {name: 'ptList'}
								        ]
								    );
var store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
		root:'list',
		totalProperty:'count'
	},records),
	proxy:new Ext.data.HttpProxy({
			disableCaching :false,
			url: getPath() + '/com/findCompanyPage'
	}),
	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});

//行业
var tradeStore = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'post',
		url : getPath() + '/trade/findAllTrade'
	}),
	reader : new Ext.data.JsonReader({root:'obj.list'}, [{name : 'fid'}, {name : 'fname'}])
});
tradeStore.load();
/*//产业
var industryStore = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'post',
		url : getPath() + '/ind/findAllInd'
	}),
	reader : new Ext.data.JsonReader({root:'obj.list'}, [{name : 'fid'}, {name : 'fname'}])
});
industryStore.load();*/
var tool = new Ext.Toolbar({ //工具栏
	items:['行业：',{
		xtype : 'combo',
		id : 'searchCondition',
		triggerAction : 'all',
		lazyInit : true,
		mode : 'local',
    	store: tradeStore, 
    	valueField : 'fid',
    	displayField : 'fname',
    	selectOnFocus : true,
    	editable : false,
    	emptyText : '请选择行业',
		width:150,
		listeners : {
			select : function() {				
				searchBy();
			}
		} 
	},'-'/*,'产业：',{
		xtype : 'combo',
		id : 'searchIndustryId',
		triggerAction : 'all',
		lazyInit : true,
		mode : 'local',
    	store: industryStore,
    	valueField : 'fid',
    	displayField : 'fname',
    	selectOnFocus : true,
    	editable : false,
    	emptyText : '请选择产业',
		width:150,
		listeners : {
			select : function() {
				searchBy();
			}
		} 
	},'-'*/]
});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 23
	},
	success : function(response, options) {
		obj=Ext.util.JSON.decode(response.responseText);
		for(var i=0;i<obj.length;i++){
			if(null==obj[i] ){continue;}
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
    store.on('beforeload', function() {    
	  	  this.baseParams = {
	  			 start:0,limit:LIMIT,groupId:window.parent.groupId
	  	  };
	});
    store.load();
    var cm = new Ext.grid.ColumnModel([
	       new Ext.grid.RowNumberer(),
	       {id:'fcid',hidden:true},
	       {
	    		id:'fcname', 
	    		header: '公司名称', 
	    		width: 240, 
	    		sortable: true, 
	    		dataIndex:'fcname'
           },{
	    		id:'fctelphone', 
	    		header: '公司电话', 
	    		width: 150, 
	    		sortable: true, 
	    		dataIndex:'fctelphone'
           },{
	    		id:'fcemail', 
	    		header: '邮箱', 
	    		width: 150, 
	    		sortable: true, 
	    		dataIndex:'fcemail'
           },{
	    		id:'fcurl', 
	    		header: '网址', 
	    		width: 150, 
	    		sortable: true, 
	    		dataIndex:'fcurl'
           },{
	    		id:'fclocation', 
	    		header: '公司地址', 
	    		width: 150, 
	    		sortable: true, 
	    		dataIndex:'fclocation'
           },{
	    		id:'fcproducts', 
	    		hidden: true, 
	    		dataIndex:'fcproducts'
           },{
	    		id:'fcintroduction', 
	    		header: '公司简介', 
	    		width: 150, 
	    		hidden: true, 
	    		dataIndex:'fcintroduction'
           },{
	    		id:'ybasicTrade', 
	    		header: '行业', 
	    		width: 150, 
	    		sortable: true, 
	    		dataIndex:'ybasicTrade',
	    		renderer:function(value){
					if(value){
						return value.fname;
					}else{
						return "";
					}
	    		}
           },{
	    		id:'ybasicIndustry', 
	    		header: '产业', 
	    		width: 150, 
	    		hidden: true, 
	    		dataIndex:'ybasicIndustry',
	    		renderer:function(value){
					if(value){
						return value.fname;
					}else{
						return "";
					}
	    		}
           },{
	    		id:'ybasicSocialgroups', 
	    		header: '团体', 
	    		hidden: true,
	    		dataIndex:'ybasicSocialgroups'
           },{
	    		id:'ybasicMember', 
	    		header: '会员', 
	    		width: 150, 
	    		sortable: true, 
	    		dataIndex:'ybasicMember',
	    		renderer:function(value){
	    			return value.fname;
	    		}
           },{
	    		id:'fcompanyPosition', 
	    		header: '公司职务', 
	    		width: 150, 
	    		sortable: true, 
	    		dataIndex:'fcompanyPosition'
           },{
	        	id:'fcompanyNature', 
	       		header: '公司性质', 
	       		dataIndex:'fcompanyNature'   
           },{
	    		id:'fcompanyScale', 
	    		header: '公司规模', 
	    		dataIndex:'fcompanyScale'
           },{
	    		id:'fcompanyLatitude', 
	    		header: '公司经纬度', 
	    		dataIndex:'fcompanyLatitude'
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

var editCompanyForm = new Ext.form.FormPanel({
	border : false,
    id:'editCompanyForm',
    waitMsgTarget : true,
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
					        allowBlank: true,
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
						 }
				 ]
		 }]
	},{
		layout:'column',
		border:true,
		id:'items0',
		labelWidth:60,
		items:[{
				 columnWidth: .5,
			     layout: 'form',
			     border: false,
			     defaults:{xtype:'textfield',anchor:'92%'},
			     items:[
			            {xtype:'hidden',id:'fid0',name:'fid',value:""},
			            {fieldLabel:'产品名称',id:'fcname0',name:'fname',xtype:'textfield',allowBlank: false,maxLength:50},
			            new Ext.BoxComponent({
			            	fieldLabel:'产品预览',
			                id : 'flogoImage0',
			                autoEl : {
			                    height : 60,
			                    tag : 'img',
			                    src : Ext.BLANK_IMAGE_URL,
			                    style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
			                    complete : 'off'
			                }
			            })]
		     },{
				 columnWidth: .5,
			     layout: 'form',
			     border: false,
			     defaults:{xtype:'textfield',anchor:'92%'},
			     items:[
			            {fieldLabel:'产品描述',id:'fdescription0',name:'fdescription',value:"",height:80,xtype:'textarea'},
			            {fieldLabel:'flag',id:'flag0',name:'flag',value:"",xtype:'hidden'}
			     ]
		     }]
	}]
});

/**************************** 自定义元素结束 *******************************/
/**************************** 操作 **********************************/
function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	//通过id获取输入的数据
	var searchCondition = Ext.getCmp("searchCondition").getValue();
	var industryId = Ext.getCmp("searchIndustryId").getValue();
	//模糊查询
	store.load({params:{tradeId:searchCondition,industryId:industryId,start:0,limit:LIMIT,groupId:window.parent.groupId}});
}

function editData(){
	
	Ext.getCmp('editCompanyForm').getForm().reset();

	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	var row = Ext.getCmp('grid').getSelectionModel().getSelected();
	
	if(row){
		this.row = row;
    	var that = this;
         if(!editWin){
            editWin = new Ext.Window({
		        id: 'editWin',
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
			    }
            });
		}
        setTitleAndUrl(row, editWin);
        loadData(row);
	}else{
		alertWarring("请选中公司列!");
	}
}

function loadData(row) {
	if(row.get('ybasicTrade') && row.get('ybasicTrade').fname){
		editCompanyForm.form.setValues({
	        ybasicTrade  						: row.get('ybasicTrade').fname
	    });
	}
	if(row.get('ybasicIndustry') && row.get('ybasicIndustry').fname){
		editCompanyForm.form.setValues({
	        ybasicIndustry     					: row.get('ybasicIndustry').fname
	    });
	}
	editCompanyForm.form.setValues({
        fcid  								: row.get('fcid'),
        ybasicMember  						: row.get('ybasicMember').fname,
        ybasicSocialgroups  				: row.get('ybasicSocialgroups').fname,
        fcname  							: row.get('fcname'),
        fctelphone  						: row.get('fctelphone'),
        fcemail								: row.get('fcemail'),
        fcurl								: row.get('fcurl'),
        fclocation							: row.get('fclocation'),
        fcproducts							: row.get('fcproducts'),
        fcintroduction						: row.get('fcintroduction'),
        fcompanyPosition					: row.get('fcompanyPosition'),
        fcompanyNature						: row.get('fcompanyNature'),
        fcompanyScale						: row.get('fcompanyScale'),
        fcompanyLatitude					: row.get('fcompanyLatitude')
	});
	Ext.Ajax.request({
		url : getPath() + 'com/findProductByComID',
		params : {fid : row.get('fcid')},
		success: function (result, request) {
			var ptlist =  Ext.util.JSON.decode(result.responseText).obj;
			if(ptlist && ptlist.length>0){
				for(i;i<ptlist.length;i++){
					var ptInfo = ptlist[i];
					if(i==0){
						editCompanyForm.form.setValues({
							fid0			: ptInfo.fid,
							fcname0			: ptInfo.fname,
							fdescription0	: ptInfo.fdescription,
							flag			: ptInfo.flag
						});
						Ext.getCmp('flogoImage0').getEl().dom.src= ptInfo?(ptInfo.flogoImage.length>0?ptInfo.flogoImage:Ext.BLANK_IMAGE_URL):Ext.BLANK_IMAGE_URL;
					}else{
						addPt(i,ptInfo);
					}
				}
				i = i == 0 ? 0 : i - 1;
			}
			
		},
		failure: function ( result, request) {
			alertError("系统运行超时或执行失败");
		}
	});
	
	
	editWin.show();
}
//动态添加主营产品
function addPt(row,ptInfo){
	Ext.getCmp('editCompanyForm').add({
		layout:'column',
		id:'items'+row,
		border:true,
		labelWidth:60,
		items:[{
				 columnWidth: .5,
			     layout: 'form',
			     border: false,
			     defaults:{xtype:'textfield',anchor:'92%'},
			     items:[
			            {xtype:'hidden',id:'fid'+row,name:'fid',value:ptInfo?ptInfo.fid:undefined},
			            {fieldLabel:'产品名称',id:'fcname'+row,name:'fname',xtype:'textfield',value:ptInfo?ptInfo.fname:"",allowBlank: false,maxLength:50},
			            new Ext.BoxComponent({
			            	fieldLabel:'产品预览',
			                id : 'flogoImage'+row,
			                autoEl : {
			                    height : 60,
			                    tag : 'img',
			                    src : ptInfo?(ptInfo.flogoImage.length>0?ptInfo.flogoImage:Ext.BLANK_IMAGE_URL):Ext.BLANK_IMAGE_URL,
			                    style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
			                    complete : 'off'
			                }
			            })
		            ]
		     },{
				 columnWidth: .5,
			     layout: 'form',
			     border: false,
			     defaults:{xtype:'textfield',anchor:'92%'},
			     items:[
			            {fieldLabel:'产品描述',id:'fdescription'+row,name:'fdescription',value:ptInfo?ptInfo.fdescription:"",height:80,xtype:'textarea'},
			            {fieldLabel:'flag',id:'flag'+row,name:'flag',value:ptInfo?ptInfo.flag:"",xtype:'hidden'}
			     ]
		     }]
	});
	Ext.getCmp('editCompanyForm').doLayout();
}

function setTitleAndUrl(row, win) {
	win.setTitle('公司详情');
	win.show();
}