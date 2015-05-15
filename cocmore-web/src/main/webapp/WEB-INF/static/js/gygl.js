var LIMIT = 26;var editWin;var mbszWin;var excel;var jsonStr=[];var detailWin;var imgCount=0; var fileCount=0;

//上传图片类型  
var img_reg = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/;

//create the data store
var records = new Ext.data.Record.create([  
       {name: 'fid'},
       {name: 'fnumber'},
       {name: 'ybasicMember'},
       {name: 'ybasicAssurancecontent'},
       {name: 'ybasicCity'},
       {name: 'ybasicTrade'},
       {name: 'ybasicCounty'},
       {name: 'ybasicProvince'},
       {name: 'ybasicSocialgroups'},
       {name: 'fheadline'},
       {name: 'fimages'},
       {name: 'fmessage'},
       {name: 'ftel'},
       {name: 'fnationalCertification'},
       {name: 'fareGuarantee'},
       {name: 'fcontacts'},
       {name: 'fexpireTime'},
       {name: 'fauditTime'},
       {name: 'fpublisherTime'},
       {name: 'fauditIdea'},
       {name: 'fbillState'},
       {name: 'fcomment'},
       {name: 'fisHide'},
       {name: 'flevel'},
       {name: 'jsonStr'},
       {name: 'flag'}
]);
var store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
		root:'list',
		totalProperty:'count'
	},records),
	proxy:new Ext.data.HttpProxy({
			disableCaching :false,
			url: getPath() + '/supply/findall'
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
		functionId : 8
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
    store.load({params:{start:0,limit:LIMIT,groupId:window.parent.groupId}});
    
    var cm = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer(),
    	{id:'fid',hidden:true},{
   			id:'fnumber', 
   			header: '编号', 
   			width: 150, 
   			hidden: true, 
   			dataIndex:'fnumber'
    	},{
   			id:'fheadline', 
   			header: '标题', 
   			width: 150, 
   			sortable: true, 
   			dataIndex:'fheadline'
    	},{
    	   	id:'ybasicMember',
    	   	dataIndex:'ybasicMember',
   	   		header: '发布人', 
  			width: 150, 
  			sortable: true, 
  			renderer : function(value) {
  				if(value == null || value.fname==null){
  					return "null";
  				}
                return value.fname;
            }
    	},{
    	   	id:'jsonStr',
    	   	dataIndex:'jsonStr',
  	   		header: '担保内容',
 			hidden: true
       },{
    	   	id:'ybasicSocialgroups',
    	   	dataIndex:'ybasicSocialgroups',
 	   		header: '社会团体', 
			width: 150, 
			sortable: true, 
			allowBlank: true,
			renderer : function(value) {
				if(value){
					return value.fname;
				}else{
					return "";
				}
			}
       },{
    	   	id:'ybasicCity',
   	   		dataIndex:'ybasicCity',
	   		header: '城市', 
			hidden: true,
			renderer : function(value) {
				if(value){
					return value.fname;
				}else{
					return "";
				}
			}
       },{
    	   	id:'ybasicTrade',
   	   		dataIndex:'ybasicTrade',
	   		header: '行业', 
			width: 150, 
			sortable: true, 
			renderer : function(value) {
				if(value){
					return value.fname;
				}else{
					return "";
				}
			}
       },{
    	   	id:'ybasicCounty',
   	   		dataIndex:'ybasicCounty',
	   		header: '区县', 
			hidden: true,
			renderer : function(value) {
				if(value){
					return value.fname;
				}else{
					return "";
				}
			}
       },{
    	   	id:'ybasicProvince',
    	   	dataIndex:'ybasicProvince',
	   		header: '省份', 
			hidden: true,
			renderer : function(value) {
				if(value){
					return value.fname;
				}else{
					return "";
				}
			}
       },{
    	  	id:'fimages',
	   	   	header:'图片',
	   	   	width:150,
	   	   	hidden:true,
	   	   	dataIndex:'fimages'
      },{
	   		id:'fmessage', 
	   		header: '供应信息', 
	   		hidden: true, 
	   		dataIndex:'fmessage'
      },{
   	   		id:'ftel', 
   	   		header: '联系电话', 
  			width: 150, 
  			sortable: true, 
  			dataIndex:'ftel' 
      },{
    	   	id:'fnationalCertification', 
   	   		header: '国家认证', 
  			width: 150, 
  			hidden: true, 
  			dataIndex:'fnationalCertification'
       },{
    	   	id:'fareGuarantee', 
  	   		header: '是否担保', 
 			width: 150, 
 			hidden: true, 
 			dataIndex:'fareGuarantee',
 			renderer:function(value){
    			if(value==0){
    				return '否';
    			}else
    				return '是';
    		}
       },{
    	   	id:'fcontacts', 
 	   		header: '联系人', 
			width: 150, 
			sortable: true, 
			dataIndex:'fcontacts'
       },{
   	   		id:'fauditTime', 
   	   		header: '审核时间', 
   	   		width: 150, 
   	   		hidden: true, 
   	   		dataIndex:'fauditTime'
       },{
   	   		id:'fpublisherTime', 
   	   		header: '发布时间', 
   	   		width: 150, 
   	   		hidden: true, 
   	   		dataIndex:'fpublisherTime'
       },{
    	   	id:'fexpireTime', 
	   		header: '到期时间', 
			width: 150, 
			hidden: true, 
			dataIndex:'fexpireTime'
       },{
    	   	id:'fauditIdea', 
	   		header: '审核意见', 
			width: 150, 
			hidden: true, 
			dataIndex:'fauditIdea'
       },{
   	   		id:'fisHide', 
   	   		header: '是否隐藏', 
   	   		width: 150, 
   	   		hidden: true, 
   	   		dataIndex:'fisHide',
   	   		renderer:function(value){
   	   			if(value==0){
   	   				return '否';
   	   			}else
   	   				return '是';
   	   		}
       },{
    		id:'flevel', 
    		header: '等级', 
    		width: 70, 
    		sortable: true,
    		dataIndex:'flevel'
       },{
    	   	id:'fcomment', 
   			header: '备注', 
   			width: 70, 
   			hidden: true,
   			dataIndex:'fcomment'
       },{
    	   id : 'fbillState',
           header : '状态',
           width : 70,
           sortable : true,
           renderer : status,
           dataIndex : 'fbillState'
       },{id:'flag', dataIndex : 'flag',hidden:true}
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
    	}else if(data == 11){
    		return '<span style="color:blue">待审核</span>';
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
	}) 
	grid.on('dblclick',function(){ editData("detail")});
});
/**************************** 自定义元素 **********************************/
//担保内容
var assuranceContentStore = new Ext.data.Store({
	autoLoad:true,
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'post',
		url : getPath() + '/content/findContentPage'
	}),
	reader : new Ext.data.JsonReader({root:'list'}, [{name : 'fid'}, {name : 'fcontent'}])
});

//发布人
var memberStore = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'post',
		url : getPath() + '/member/findMemgerHql'
	}),
	reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
});
memberStore.load({params:{groupId:window.parent.groupId}});

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

//省份
var provinceStore = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'post',
		url : getPath() + '/province/findAllProvince'
	}),
	reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
});
provinceStore.load();

//城市
var cityStore = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'post',
		url : getPath() + '/city/findCityHql'
	}),
	reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
});

//区县
var countyStore = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'post',
		url : getPath() + '/county/findCountyHql'
	}),
	reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
});

var editForm = new Ext.form.FormPanel({
    border : true,
    fileUpload : true,
    waitMsgTarget : true,
    enctype : 'multipart/form-data',
    id:'editForm',
	labelAlign: 'left',
    bodyStyle:'padding:5px',
    labelWidth:60,
	frame	: true,
	containerScroll: true, 
	bodyStyle : 'overflow:auto; overflow-x:hidden',
	width: 700,
    height: 380,
    items:[{
   	 	layout:'column',
        items:[{
	    	columnWidth:.33,
	 	    layout: 'form',
		    border:false,
		    defaults:{ anchor:'91%',xtype:'textfield'},
		    items:[{xtype:'hidden',id:'fid',name:'fid'},
			        {fieldLabel:'编号',id:'fnumber',name:'fnumber',xtype:'hidden'},
			        {fieldLabel:'标题',id:'fheadline',name:'fheadline',blankText:'该字段不允许为空',allowBlank: false,maxLength:50},
			        {xtype : 'combo',fieldLabel : '行业',id : 'ybasicTrade',hiddenName : 'YBasicTrade.fid',triggerAction : 'all',lazyInit : true,mode : 'local',
			        	store: tradeStore,
			        	valueField : 'fid',
			        	displayField : 'fname',selectOnFocus : true,allowBlank: false,editable : false,emptyText : '请选择行业'},
			        
			        {xtype : 'combo',fieldLabel : '省份',id : 'ybasicProvince',hiddenName : 'YBasicProvince.fid',triggerAction : 'all',lazyInit : true,mode : 'local',
			        	store: provinceStore,
			        	valueField : 'fid',
			        	displayField : 'fname',emptyText : '请选择省份',allowBlank: false,
			        	listeners:{
			                select:function(groupSelect){
			                	cityStore.load({params:{id:groupSelect.getValue()}});
			                	Ext.getCmp('ybasicCity').focus();
			        		}
			            }
			        },
			        {xtype : 'combo',fieldLabel : '城市',id : 'ybasicCity',hiddenName : 'YBasicCity.fid',triggerAction : 'all',lazyInit : true,mode : 'local',
			        	store: cityStore,
			        	valueField : 'fid',
			        	displayField : 'fname',emptyText : '请选择城市',allowBlank: false,
			        	listeners:{
			                select:function(groupSelect){
			                	countyStore.load({params:{id:groupSelect.getValue()}});
			                	Ext.getCmp('ybasicCounty').focus();
			        		}
			            }},
			        {xtype : 'combo',fieldLabel : '区县',id : 'ybasicCounty',hiddenName : 'YBasicCounty.fid',triggerAction : 'all',lazyInit : true,mode : 'local',
				        store: countyStore,
				        valueField : 'fid',allowBlank: false,
				        displayField : 'fname',emptyText : '请选择区县'},
			        {fieldLabel:'社会团体',id:'ybasicSocialgroups',name:'YBasicSocialgroups.fid',xtype: 'hidden'},
			        {xtype:'hidden',id:'jsonStr',name:'jsonStr'}
				]
	   },{
   	    columnWidth:.34,
   	    layout: 'form',
   	    border:false,
		defaults:{ anchor:'90%',xtype:'textfield'},
   	    items: [
		        {fieldLabel: '开始时间',id:'fauditTime',name:'fauditTime',xtype:'datetimefield',format:'Y-m-d H:i:s',allowBlank: false,editable: false	},
		        {fieldLabel: '发布时间',id:'fpublisherTime',name:'fpublisherTime',xtype:'hidden',format:'Y-m-d H:i:s',allowBlank: false,editable: false},
		        {fieldLabel: '到期时间',id:'fexpireTime',name:'fexpireTime',xtype:'datetimefield',format:'Y-m-d H:i:s',allowBlank: false,editable: false,
						vtype : 'daterange',
						startDateField : 'fauditTime'},
			   	{fieldLabel:'等级',id:'flevel',hiddenName:'flevel',xtype:'combo',
					typeAhead: true,
					triggerAction: 'all',
					lazyRender:true,
				    mode: 'local', allowBlank: false,
			   		store: new Ext.data.ArrayStore({
			          id: 4,
			          fields: [
			              'myId',
			              'displayText'
			          ],
			          data: [[1,1], [2, 2],[3, 3],[4, 4],[5, 5]]
			   		}) ,
			   		valueField: 'myId',
				    displayField: 'displayText',
				    renderer:function(value){
				      	return value.displayText;
				    }
			   	},	
   	   	        {fieldLabel:'联系人',id:'fcontacts',name:'fcontacts',maxLength:24},
			   	{fieldLabel:'审核意见',id:'fauditIdea',name:'fauditIdea',maxLength:125},
			   	{fieldLabel:'状态',id:'fbillState',name:'fbillState',xtype: 'hidden'},
			   	{fieldLabel:'自动增长',id:'flag',name:'flag',xtype: 'hidden'}
	   	    ]
	   },{
	   	    columnWidth:.33,
	   	    layout: 'form',
	   	    border:false,
			defaults:{ anchor:'90%',xtype:'textfield'},
	   	    items: [{fieldLabel:'图片',id:'fimages',name:'fimages',maxLength:125,xtype:'hidden'},
	   	   	        {fieldLabel:'联系电话',id:'ftel',name:'ftel',regex : /^((1+\d{10})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/},
	   	   	        {fieldLabel:'国家认证',id:'fnationalCertification',name:'fnationalCertification',maxLength:25},
			        {xtype : 'combo',fieldLabel : '发布人',id : 'ybasicMember',hiddenName : 'YBasicMember.fid',triggerAction : 'all',lazyInit : true, mode : 'local',
			        	store: memberStore,
			        	valueField : 'fid',
			        	displayField : 'fname',emptyText : '请选择发布人',allowBlank: false,
			        	listeners:{
			        		select:function(select){
			        			fsocialGroupsIdStore.load({params:{id:select.getValue()}});
			        		}
			        	}},
			        {fieldLabel:'是否担保',id:'fareGuarantee',xtype:'radiogroup',layout:'column',columns:2,
	   				 	items:[{boxLabel: "是", name: 'fareGuarantee',inputValue: 1},
	   				 	       {boxLabel: "否", name: 'fareGuarantee',inputValue: 0,checked:true}]
	   	   	        },
					{fieldLabel:'是否隐藏',id:'fisHide',xtype:'radiogroup',layout:'column',columns:2,
		   	        	items:[{boxLabel: "是", name: 'fisHide',inputValue: 1},
					 	       {boxLabel: "否", name: 'fisHide',inputValue: 0,checked:true}]
				   	},
			        {fieldLabel:'备注',id:'fcomment',name:'fcomment',maxLength:125}
	   			   	 ]
		   }]
    },{
    	fieldLabel:'供应信息',
    	id:'fmessage',
    	name:'fmessage',
    	xtype:'htmleditor',
    	height:100,
    	width:600,
    	blankText:'该字段不允许为空',
    	allowBlank: false
	},{
    	xtype:'button',
    	text:'上传图片',
    	id:'uploadFile',
    	listeners:{
    		click:function(){
    			Ext.getCmp("uploadFile").hide();
    			Ext.getCmp('fileTool').show();
    		}
    	}
    },{
    	layout:'column',
    	id:'fileTool',
	    hidden:true,
	    width:500,
        items:[{
			 columnWidth: .5,
		     layout: 'form',
		     border: false,
		     items:[{
		 		xtype:'button',
				text:'添加图片',
				listeners:{click:function(){
						fileCount++;
						addFile(fileCount,"file","fileUpload");
					}
				}
			}]
	     },{
			 columnWidth: .5,
		     layout: 'form',
		     border: false,
		     items:[{
		 		xtype:'button',
				text:'依次移除图片',
				listeners:{click:function(){
						if(fileCount>0){
							Ext.getCmp('fileUpload').remove('file'+fileCount,true);
							fileCount--;
							Ext.getCmp('fileUpload').doLayout();
						}
					}
				}
			}]
	     },{
	        	columnWidth:0.8,
		   	    layout: 'form',
		   	    border:false,
			    id:'fileUpload',
		   	    items: [{fieldLabel:'图片',id:'file0',name:'file',xtype:'fileuploadfield',buttonText: '浏览..'}]
		}]
    	
    }]
});

var dbContent = new Ext.form.ComboBox({
	fieldLabel : '担保内容',
	id:'dbContent',
	hiddenName : 'YBasicAssurancecontent.fid',
	triggerAction : 'all',
	lazyInit : true,
	mode : 'local',
	store : assuranceContentStore,
	valueField : 'fid',
	displayField : 'fcontent',
	selectOnFocus : true,
	editable : false,
	allowBlank: false,
	emptyText : '请选择担保内容'
});

var fsocialGroupsIdStore = new Ext.data.ArrayStore({
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'post',
		url : getPath() + '/groups/findGroupList'
	}),
	fields: [
        'fid',
        'fname'
    ]
});
var fsocialGroupsCom = new Ext.form.ComboBox({
	fieldLabel : "所属团队",
	store : fsocialGroupsIdStore,
	id:'fsocialGroupsId',
	triggerAction : 'all',
	lazyInit : true,
	mode:'local',
	valueField : 'fid',
	displayField : 'fname',
	selectOnFocus:true,
	editable:false,
	emptyText:'请选择所属团体',
	listeners:{
		select:function(groupSelect){
			//assuranceContentStore.load({params:{groupId:groupSelect.getValue()}});
		}
	}
});
/***********************************添加担保信息相关**********************************/
var db_sm = new Ext.grid.CheckboxSelectionModel({handleMouseDown: Ext.emptyFn});
var db_cm = new Ext.grid.ColumnModel([
   	new Ext.grid.RowNumberer(),
   	db_sm,	
   	  {header:'ID',dataIndex:'fid',hidden:true,editor: {xtype:'hidden',name:'fid',value:'1'}},
   	  {header:'所属团体',dataIndex:'ybasicAssurancecontent.ybasicSocialgroups.fid',editor: fsocialGroupsCom,
    	renderer: function(value, cellmeta, record,rowIndex,columnIndex) {
            var index = fsocialGroupsIdStore.find(Ext.getCmp('fsocialGroupsId').valueField,value);
            var record = fsocialGroupsIdStore.getAt(index);
            var returnvalue = "";
            if (record) {
               returnvalue = record.data.fname;
            }
            return returnvalue;//注意这个地方的value是上面displayField中的value
    	}
   	  },
   	  {header:'担保内容',dataIndex:'ybasicAssurancecontent.fid',editor: dbContent,
	    	renderer: function(value, cellmeta, record,rowIndex,columnIndex) {
	            var index = assuranceContentStore.find(Ext.getCmp('dbContent').valueField,value);
	            var record = assuranceContentStore.getAt(index);
	            var returnvalue = "";
	            if (record) {
	               returnvalue = record.data.fcontent;
	            }
	            return returnvalue;//注意这个地方的value是上面displayField中的value
	    	}
   	  }
     ]);

var db_store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
		root:'obj'
	},[{name: 'fid'},
       {name: 'ybasicAssurancecontent.ybasicSocialgroups.fid'},
       {name: 'ybasicAssurancecontent.fid'}
      ]),
	proxy:new Ext.data.HttpProxy({
			disableCaching :false,
			url : getPath() + 'supply/findSAList'
	}),
	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});


var db_grid = new Ext.grid.EditorGridPanel({
	height: 140,
	region:'center',
	id:'db_grid',
	title:'担保内容',
	anchor:'100%',
    style:'margin-bottom: 6px;',
    stripeRows: true,   //每行交替颜色
    loadMask: true,     //加载时提示load...
    store: db_store,
    clicksToEdit:1,
	border: false,
	columnLines:true,
	frame:false,
	monitorResize: true,
	autoScroll:true,
    selModel: new Ext.grid.RowSelectionModel({singleSelect: false}), //设置为单行选中
    cm: db_cm,
    sm:db_sm,
    viewConfig:{
        forceFit: true
    },
    tbar: new Ext.Toolbar(['-',{
            iconCls : 'common_add',
            text : '新增',
            handler: function(){
            	if(Ext.getCmp("fareGuarantee").getValue()==1){
	                var n = db_grid.getStore().getCount();// 获得总行数
	                var Plant = db_grid.getStore().recordType;
	                var r = new Plant({});
	                db_grid.stopEditing();
	                db_store.insert(n,r);
	                db_grid.startEditing(n, 2);// 开始编辑2单元格
                }else{
                	alertWarring("担保条件为否不能添加担保内容!");
                }
            }
        },'-',{
            iconCls : 'common_delete',
            text : '删除',
            id : 'deleteColumn',
            handler: function() {
                var rows = db_grid.getSelectionModel().getSelections();// 返回值为 Record 数组
                if(rows.length == 0){
                    alertError("请选择要删除的数据", 'deleteColumn');
                }
                else{
                    for(var i = 0; i<rows.length; i++){
                    	db_store.remove(rows[i]);
                    }               
                }
            } 
        }
    ])
});

//添加监听
db_grid.addListener('beforeedit', setOrg);          
function setOrg(e){
	var record = db_grid.getStore().getAt(e.row);//获取某一行
	var groupId = record.get(db_grid.getColumnModel().getDataIndex(3));//获取当前输入的团体的值
	if(e.column==4){
		assuranceContentStore.load({params:{groupId:groupId}});
	}
}
	
function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	//通过id获取输入的数据
	var searchCondition = Ext.getCmp("searchCondition").getValue();
	//模糊查询
	store.load({params:{headline:searchCondition,groupId:window.parent.groupId,start:0,limit:LIMIT}});
}

function editData(row){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	var detail = row;
	if(row!='add' && row!='detail'){
	     row = Ext.getCmp("grid").getSelectionModel().getSelected();
	     if(!row){
	    	 alertWarring("请选择需要编辑的供应信息!");return;
	     } 
	     var billState = row.get("fbillState");
	     /*if(billState == 3){
				alertError("该信息只能做反审核，生效操作！");return;
		 }else if(billState == 5){
				alertError("该信息只能做反审核，失效操作！");return;
		 }else if(billState == 6){
				alertError("该信息只能做作废操作！");return;
		 }else if(billState == 9){
				alertError("该信息已作废，无法进行操作！");return;
		 }*/
	}else if(row =='detail'){
		row = Ext.getCmp("grid").getSelectionModel().getSelected();
		if(!row){
	    	 alertWarring("请选择需要查看详情的供应信息!");
	    }else{
	 		showDetail(row);
	    }
		return;
	}else{
		getNumberStore();
		Ext.getCmp('fbillState').setValue(5);
		Ext.getCmp('ybasicSocialgroups').setValue(window.parent.groupId);
	}
    	this.row = row;
    	var that = this;
        if(!editWin){
            editWin = new Ext.Window({
            	id: 'editWin',
		        layout:'fit',
                width:700,
                height:590,
                frame:true,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
				border : false,
			    items :[{layout:'vbox',items:[editForm,db_grid]}],
			    buttons:[{
				    text: '保存',
				    handler:function(){
				    	for(var j =0;j<(fileCount+1);j++){
				    		var file = Ext.getCmp('file'+j).getValue();
				    		if(file && file.length>0 && !img_reg.test(file)){
				        		alertWarring("请选择后缀名为 [jpg,jpeg,png,bmp]的文件");return;
				        	}
				    	}
				    	if(editForm.form.isValid()){
				    		var n = db_grid.getStore().getCount();
				        	jsonStr='[';
							for(var i =0;i<n;i++){
								   var YBasicAssurancecontent = db_grid.getStore().getAt(i).get("ybasicAssurancecontent.fid");
								   var obj = [YBasicAssurancecontent];
								   if(isNull(obj)){
									   jsonStr+= '{\"fid\":\"'+db_grid.getStore().getAt(i).get("fid")+'\",\"YBasicAssurancecontent.fid\":\"'+YBasicAssurancecontent+'\"},'
								   }else{
									   jsonStr ="[]";
									   break;
								   }
							}
							if(jsonStr != "["){
								jsonStr = jsonStr.substring(0, jsonStr.length-1) + ']';
							}
							if(Ext.getCmp('fareGuarantee').getValue() == 1){
								Ext.getCmp('fbillState').setValue(11);//存在担保的情况下该供应需要团体审核担保内容通过时才为生效
								if(jsonStr.length<3){
									jsonStr = '[]';
									alertWarring("担保内容不能为空!");return;
								}
							}
							editForm.getForm().findField('jsonStr').setValue(jsonStr=='[]'?"":jsonStr);
				    		editForm.form.submit({
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
			    listeners:{
			    	hide:function(){
				    	editForm.form.reset();	
				    	jsonStr = [];
						db_store.removeAll();
						editForm.form.reset();
						//移除表单中动态生成的预览图片
			    		for(imgCount;imgCount>-1;imgCount--){
			    			editForm.remove('browseImage'+imgCount,true);
			    		}
			    		imgCount=0;
			    		
						//移除表单中动态生成的文件上传控件
			    		for(fileCount;fileCount>0;fileCount--){
			    			Ext.getCmp("fileUpload").remove('file'+fileCount,true);
			    		}
			    		fileCount=0;
			    		
			    		Ext.getCmp("fileTool").hide();
			    		Ext.getCmp("uploadFile").show();
			    		Ext.getCmp('editForm').doLayout();
		    		}
			    }
            });
		}
        setTitleAndUrl(row, editWin);
        
	    editForm.getForm().findField('fbillState').setValue(5);
	    var date = new Date();  // 得到系统日期
	    var dateString = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	    editForm.getForm().findField('fpublisherTime').setValue(dateString);
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
		}else{
			var tool = Ext.getCmp('editWin').buttons;
			for(var i =0;i<tool.length;i++){
				tool[i].setVisible(true);
			}
		}
	}

function loadData(row) {
	if(row.get('ybasicProvince') && row.get('ybasicProvince').fid){
		cityStore.load({
			params:{id:row.get('ybasicProvince').fid},
			callback:function(){
				if(row.get('ybasicCity') && row.get('ybasicCity').fid){
					countyStore.load({
						params:{id:row.get('ybasicCity').fid},
						callback:function(){
							editForm.form.setValues({
								ybasicCity				: row.get('ybasicCity').fid
							});
							if(row.get('ybasicCounty') && row.get('ybasicCounty').fid){
								editForm.form.setValues({
									ybasicCounty			: row.get('ybasicCounty').fid
								});
							}
						}
					});
				}
				editForm.form.setValues({
					ybasicProvince			: row.get('ybasicProvince').fid
				});
			}
		});
	}
	
			
	editForm.form.setValues({
				fid 					: row.get('fid'),
				fnumber  				: row.get('fnumber'),
				fheadline				: row.get('fheadline'),
				ybasicMember			: row.get('ybasicMember').fid,
				ybasicTrade				: row.get('ybasicTrade').fid,
				ybasicSocialgroups		: row.get('ybasicSocialgroups').fid,
				fauditTime				: row.get('fauditTime'),
				fpublisherTime			: row.get('fpublisherTime'),
				fexpireTime				: row.get('fexpireTime'),
				fimages					: row.get('fimages'),
				fmessage				: row.get('fmessage'),
				ftel					: row.get('ftel'),
				fnationalCertification	: row.get('fnationalCertification'),
				fareGuarantee			: row.get('fareGuarantee'),
				fcontacts				: row.get('fcontacts'),
				fisHide					: row.get('fisHide'),
				flevel					: row.get('flevel'),
				fauditIdea				: row.get('fauditIdea'),
				fcomment				: row.get('fcomment'),
				jsonStr 				: row.get('jsonStr'),
				fbillState				: row.get('fbillState'),
				flag					: row.get('flag')
	});
	//获取点赞跟不赞同 的数量
	Ext.Ajax.request({
		url:getPath() + 'supply/getNumById',
		params:{supplyDemandId:row.get('fid')},
		success:function(response, options) {
			var data = Ext.util.JSON.decode(response.responseText);
			if(data && data.success && data.num){
				var span = "<span stype='margin-left:20px;font-weight:normal;color:blue;'>";
				if(data.num.Praisenum){
					span+="当前点赞数"+data.num.Praisenum+"&nbsp;&nbsp;";
				}
				if(data.num.Treadnum){
					span+="不赞同数"+data.num.Treadnum+"&nbsp;&nbsp;";
				}
				Ext.getCmp('editWin').setTitle(Ext.getCmp('editWin').title+span+"</span>");
			}
		}
	});
	fsocialGroupsIdStore.load({params:{id:row.get('ybasicMember').fid},callback:function(){
	    db_store.reload({params:{id:row.get('fid')}});
	}});
	
	if(row.get('fimages') && row.get('fimages').length>0){
		if(row.get('fimages').indexOf(",")>0){
			var imgLength = row.get('fimages').split(',');
		    for(var i =0;i<imgLength.length;i++){
		     imgCount++;
		   	 if(i==imgLength.length-1)return;
		        photoSrc(imgLength[i],(i+1));
		    }
	    }else{
	    	photoSrc(row.get('fimages'),0);
	    }
    }
}
function setTitleAndUrl(row, win) {
	if(row == 'add') {
		win.setTitle('增加供应');
		editForm.form.url = getPath() + '/supply/saveSupply';
	} else {
		win.setTitle('修改供应');
		editForm.form.url = getPath() + '/supply/updateSupply';
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
		Ext.MessageBox.prompt("提示", "请输入将这条信息作废的原因！",function(bu,txt){
			if(txt && 0<txt.length<125){
				Ext.Ajax.request({
					url : getPath()+'supply/Invalid',
					params : {fid : fid ,fbillState : 9,comment:txt},
					
					success: function (result, request) 
					{
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

//==============================修改状态================================
function setState(stateId){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		var fids=new Array();
		var status = new Array();
		for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
		}
		Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认要更新当前供应信息状态？!",
		    width:300,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			gridSubAjax(fids,stateId);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}else{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择需要更新状态的供应信息!",
			width: 250,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}

function gridSubAjax(param,stateId)
{
	Ext.Ajax.request({
		url : getPath() + '/supply/updateState',
		params : {fids : param , status : stateId},
		success: function (result, request){
			var obj = Ext.util.JSON.decode(result.responseText);
			Ext.MessageBox.show({
				title : "提示",
				msg : obj.msg,
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

//=================================删除====================================
function deleteSupply(){
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
		    msg: "确认要删除当前供应信息？!",
		    width:300,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			SupplyAjax(fids);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}else{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择需要删除的供应信息!",
			width: 250,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}

function SupplyAjax(param)
{
	Ext.Ajax.request({
		url : getPath() + '/supply/deleteSupply',
		params : {fids : param},
		success: function (result, request){
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
//图片上传成功 给产品图片文本，预览img赋值
function photoSrc(src,index){
	Ext.getCmp('editForm').add(
			new Ext.BoxComponent({
			    id : 'browseImage'+index,
			    autoEl : {
			        height : 60,
			        tag : 'img',
			        src : src,
			        style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
			        complete : 'off'
			    }
			})
		);
	Ext.getCmp('editForm').doLayout();
}

//获取number
function getNumberStore(){
	Ext.Ajax.request({
		url:getPath() + '/serialNumber/getSerialNumber',
		params:{prefix:'FX-GYGL'},
		success:function(response, options) {
			Ext.getCmp("fnumber").setValue(Ext.util.JSON.decode(response.responseText).serialNumber);
		}
	});
}
treePanel2.on('click',function(n){
	window.parent.groupId = n.attributes.id;
	memberStore.load({params:{groupId:window.parent.groupId}});
});
//以手机端查看信息详情
function showDetail(row){
	if(!detailWin){
		detailWin = new Ext.Window({
        	id: 'detailWin',
        	title:'查看详情',
        	autoScroll:true,
	        closeAction:'hide',
			maximizable:false,
			resizable:false,
            width:360,
            height:590,
			bodyStyle:'background:#F7F7F7;;padding:5px;',
			html:'<div class="xx_center hide" style="width:320px;"><p class="shrzIcon hide"></p><div class=" xx_img scroll relative"><div class="scroll_box" id="scroll_img"><ul class="scroll_wrap"></ul>'+
		    '</div><ul class="scroll_position scroll_position_bg hide" id="scroll_position"></ul></div><h1 class="xx_title"></h1><ul class="xx_db hide"></ul>'+
			'<p class="xx_contect"><label><a href="tel:13023232"></a></label></p><ul class="xx_detail"></ul><p class="publisher"> </p><div class="xx_desc"></div></div><div class="xx_loader"></div>'
		});
	}
	detailWin.show();
	$(".xx_title").html(row.get('fheadline'));
	
	if(row.get('fareGuarantee')==1){
		$.ajax({//获取供应的担保数据
			  type: "POST",
		      url: getPath()+"supply/findSAList",  
		      data: {id:row.get('fid')},
		      dataType: "json",
		      timeout:20000,
		      error: function () {}, 
		      success:function(data) {  
      	 	  if(data.success){
      	 		  var db="<li class='db_head'>商会担保</li>";
      	 		  var rzIS=false;
      	 		  for(var i =0;i<data.obj.length;i++){
      	 			  var dbxx = data.obj[i];
      	 			  var dbrz = "<li>"+dbxx.ybasicAssurancecontent.fcontent;
      	 			  if(dbxx.ybasicAssurancecontent.ybasicSocialgroups){
      	 				 dbrz +="&nbsp;-&nbsp;"+dbxx.ybasicAssurancecontent.ybasicSocialgroups.fname;
      	 			  }
      	 			  if(dbxx.ispass == 17){
      	 				  continue;
      	 			  }else if(dbxx.ispass == 15){
      	 				 dbrz+="&nbsp;-&nbsp;<font style='color:red'>待认证</font>";
      	 			  }else{
      	 				 if(!rzIS){
      	 					rzIS = true;
      	 				 }
      	 			  }
      	 			  db += dbrz+"</li>";
      	 		  }

  	 			  if(rzIS){
  	 				 $(".shrzIcon").removeClass("hide");
  	 			  }
      	 		  $(".xx_db").html(db);
      	 		  $(".xx_db").removeClass("hide");
      	 	  }
     	 	  }
        });
	}
	  //图片相关展示
	  var img = row.get('fimages');

	  var images="";
	  var position ="";
	  if(null!=img && img.length>0){
		  var imgAarry = img.split(",");
		  for(var i = 0;i<imgAarry.length-1;i++){
			 if(i==0){
				position+='<li class="on"><a href="javascript:void(0);">'+(i+1)+'</a></li>';
			 }else{
				position+='<li><a href="javascript:void(0);">'+(i+1)+'</a></li>';
			 } 
			images+='<li><img src="'+imgAarry[i]+'"/></li>';
		  }
	  }
	  $(".scroll_wrap").html(images);
	  $("#scroll_position").html(position);
	  //发布人信息相关
	  var detail = "";
	  if(row.get('ybasicTrade')){
		  detail+='<li>行业&nbsp;'+row.get('ybasicTrade').fname+'</li>';
	  }
	  if(row.get('ybasicProvince')){
		  detail+='<li>地址&nbsp;'+row.get('ybasicProvince').fname;
		  if(row.get('ybasicCity')){
			  detail += '&nbsp;'+row.get('ybasicCity').fname;
		  }
		  if(row.get('ybasicCounty')){
			  detail += '&nbsp;'+row.get('ybasicCounty').fname;
		  }
		  detail+='</li>';
	  }
	  if(row.get('fauditTime')){
		  detail+='<li>时间&nbsp;'+(row.get('fauditTime').length>11?row.get('fauditTime').substr(0,10):row.get('fauditTime'));
		  if(row.get('fexpireTime'))
			  detail += '&nbsp;至&nbsp;'+(row.get('fexpireTime').length>11?row.get('fexpireTime').substr(0,10):row.get('fexpireTime'));
		  detail+='</li>';
	  }
	  $('.xx_detail').html(detail);
	  
	  //发布人
	  if(row.get('ybasicMember')){
		 var publisher = '';
		 if(row.get('ybasicMember').fheadImage){
			publisher+='<img src="'+row.get('ybasicMember').fheadImage+'"/>';
		 }
		 publisher += '发布人&nbsp;&nbsp;'+row.get('ybasicMember').fname;
		 $(".publisher").html(publisher);
	  }
	  
	  $('.xx_desc').html('<h2>供应简介</h2><p>'+row.get('fmessage')+'</p>');

	var bullets = $('#scroll_position >li');
	Swipe(document.getElementById('scroll_img'), {
		auto: 3000,
		continuous: true,
		callback: function(pos) {
			var i = bullets.length;
			while (i--) {
				bullets[i].className = ' ';
			}
			bullets[pos].className = 'on';
		}
	});
	$(function(){
		$('.scroll_position_bg').css({
			width:$('#scroll_position').width()
		});
	});

	  $(".xx_center").removeClass('hide');
	  $(".xx_loader").addClass('hide');
}

//动态添加图片上传按钮
function addFile(row,tag,form){
	Ext.getCmp(form).add({
		fieldLabel:'图片',id:tag+row,name:'file',xtype:'fileuploadfield',buttonText: '浏览..'});
	Ext.getCmp(form).doLayout();
}
