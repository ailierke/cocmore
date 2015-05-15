var LIMIT = 150;var editWin;var editCompanyWin;var i =0;var loginStatusWin;var replyWin;var activeTime=0;var replyLimit = 10;

//create the data store
var records = new Ext.data.Record.create([
       {name: 'fid'},
       {name: 'fname'},
       {name: 'fuserID'},
       {name: 'faccessTime'},
       {name: 'flastAccessTime'},
       {name: 'flag'},
       {name: 'ftelphone'}
    ]
);

var store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
		root:'obj.list',
		totalProperty:'obj.count'
	},records),
	proxy:new Ext.data.HttpProxy({
			disableCaching :false,
			url: getPath() + 'useroc/findAllLogin'
	}),
	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});

var tool = new Ext.Toolbar({ //工具栏
	items:[{text: '查看公司信息',enableToggle: false,iconCls:'common_search',handler:function(){companyData();}},
	   '-',{text: '查看个人信息',enableToggle: false,iconCls:'common_search',handler:function(){editData();}},
	   '-',{text: '查看登陆状态',enableToggle: false,iconCls:'common_search',handler:function(){loginStatus();}},
	   '-',{text: '查看活跃度',enableToggle: false,iconCls:'common_search',handler:function(){selectActive();}}]
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
    	{
    		id:'fid',
    		dataIndex:'fid',
    		hidden:true
		},{
    		id:'fname', 
    		header: '会员名称', 
    		sortable: true, 
    		dataIndex:'fname'
         },{
 			id:'ftelphone',
 			dataIndex:'ftelphone',
 			header:'电话',
 			renderer:function(value){
 				return decryptByABC(value);;
 			}
     	 },{
    	 	name: 'faccessTime', 
    		header: '访问时间', 
    		dataIndex:'faccessTime'
         },{
    		id:'flastAccessTime', 
    		header: '最后访问时间', 
    		dataIndex:'flastAccessTime'
         },{
        	id:'fuserID', 
     		hidden: true, 
     		dataIndex:'fuserID'
         },{
        	id:'flag', 
     		hidden: true,
     		dataIndex:'flag'
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
    var displayPanel = new Ext.Viewport({
    	renderTo:'mainDiv',
    	layout:'border',
    	items:[grid]
    });
    Ext.EventManager.onWindowResize(function(){ 
    	grid.getView().refresh() 
	});
    grid.on('dblclick',function(){ editData("detail")});
});
/**************************** 自定义元素 **********************************/

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
			        maxLength:12,
		            blankText:'该字段不允许为空',
		            allowBlank: false
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
				 	fieldLabel:'申请管理',
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
			        maxLength:24,
    	            blankText:'该字段不允许为空',
    	            allowBlank: false,
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
		     items: [myImage1,{
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

/**************************** 自定义元素结束 *******************************/
/**************************** 操作 **********************************/

/*
 * 查看会员信息
 */
function editData(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var row = Ext.getCmp('grid').getSelectionModel().getSelected();
	if(row){
		this.row = row;
    	var that = this;
         if(!editWin){
            editWin = new Ext.Window({
            	id: 'editWin',
		        layout:'fit',
                width:650,
                height:280,
                frame:true,
                border:false,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
			    items :[editForm],
			    listeners:{hide:function(){editForm.form.reset();}}
            });
		}
        setTitleAndUrl(row, editWin);
        loadData(row);
	}
	else{
		alertWarring("请选中列!");
	}
}

function loadData(row) {
	Ext.Ajax.request({
		url:getPath() + 'useroc/selectInfo',
		params:{fUserId:row.get("fuserID")},
		success:function(response, options) {
			var data=Ext.util.JSON.decode(response.responseText);
			
			if(data.obj){
				var row = data.obj; 
				editForm.form.setValues({
					fid             	: row.fid,
					fnumber 			: row.fnumber,
					fname    			: row.fname,
					fnickName			: row.fnickName,
					fheadImage  		: row.fheadImage,
					fpassword  			: row.fpassword,
					fsex  				: row.fsex,
					fbirthday   		: row.fbirthday,
					femail  			: row.femail,
					fpreviousEmail  	: row.fpreviousEmail,
					fhomePhone  		: row.fhomePhone,
					fmobilePhone  		: decryptByABC(row.fmobilePhone),
					fsecondPhone 		: row.fsecondPhone,
					fsite  				: row.fsite,
					fcomment  			: row.fcomment,
					freceivingAddress  	: row.freceivingAddress,
					fnativePlace  		: row.fnativePlace,
					ftypeId  			: row.ftypeId,
					fisHidePhone  		: row.fisHidePhone,
					fisOriginalPassword : row.fisOriginalPassword,
					isAdmin  			: row.isAdmin,
					jsonStr 			: row.jsonStr,
					fcreaterId			: row.fcreaterId,
					fcreateTime			: row.fcreateTime,
					flag				: row.flag,
					fbillState  		: 10,
					YSystemUsers		: row.ysystemUsers == null ? "" : row.ysystemUsers.fid,
					YBasicSocialgroups	: row.ybasicSocialgroups.fid
					
				});
				Ext.getCmp("fmobilePhone").setReadOnly(true);
				if(row.ybasicProvince){
					editForm.form.setValues({
						ybasicProvince			: row.ybasicProvince.fid
					});
					if(row.ybasicCity){
						ybasicCityStore.load({
							params:{id:row.ybasicProvince.fid},
							callback:function(){
								editForm.form.setValues({
									ybasicCity			: row.ybasicCity.fid
								});
								
								if(row.ybasicCounty){
									ybasicCountyStore.load({
										params:{id:row.ybasicCity.fid},
										callback:function(){
											editForm.form.setValues({
												ybasicCounty		: row.ybasicCounty.fid
											});
										}
									});
								}
							}
						});
					}
				}
				if(row.fheadImage &&  row.fheadImage.length>0){
					var imgLength = row.fheadImage.split(',');
				    for(var i =0;i<imgLength.length;i++){
				    	if(i==imgLength.length-1)return;
				        photoSrc(imgLength[i],(i+1));
				    }
			    }
			}
		}
	});
}
function setTitleAndUrl(row, win) {
	win.setTitle('会员详情');
	win.show();
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
/***********************************添加公司信息相关**********************************/
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
var industryStore = new Ext.data.Store({
	autoLoad:true,
	proxy : new Ext.data.HttpProxy({ 
		disableCaching :false,
		method:'post',
		url : getPath() + '/ind/findAllInd'
	}),
	reader : new Ext.data.JsonReader({root:'obj.list'}, [{name : 'fid'}, {name : 'fname'}])
});

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


function companyData(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
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
			    }
        	 });
		}
        editCompanyWin.setTitle('查看公司信息');
        
        /****************通过会员id获取公司信息*************/
        Ext.Ajax.request({
    		url:getPath() + '/com/getComByMemberID',
    		params:{fid:row.get("fuserID")},
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
    		 		}/*
    		 		if(com.ybasicIndustry && com.ybasicIndustry.fid){
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
    		 			fcintroduction		: com.fcintroduction
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
    							fdescription0	: ptInfo.fdescription,
    							flag			: ptInfo.flag
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
/***********************登陆状态相关**************************/
var loginStatusRecords = new Ext.data.Record.create([
                                         {name: 'fid'},
                                         {name: 'fuserID'},
                                         {name: 'fname'},
                                         {name: 'ftelphone'},
                                         {name: 'faccessTime'},
                                         {name: 'flastAccessTime'}
                                      ]);
var loginStatusStore = new Ext.data.Store({
	   	reader:new Ext.data.JsonReader({
	   		root:'obj'
		},loginStatusRecords),
		proxy:new Ext.data.HttpProxy({
				disableCaching :false,
				url: getPath() + 'useroc/loginStatus'
		}),
		remoteStore : true, // 是否远程调用数据
		remoteSort : true   // 是否远程排序
});
loginStatusStore.on("beforeload", function(currentStore, options) {
	var fids=new Array();
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  for(var i=0;i<records.length;i++){
				  fids[i]=records[i].get('fuserID');
			}
	}
	this.baseParams = {
			fUserId:fids
	};
});
var loginStatusCm = new Ext.grid.ColumnModel([
                              	new Ext.grid.RowNumberer(),
                            	{id:'fid',hidden:true},
                            	{
                            		header: '评论人',
                            		id:'fuserID',
                            		dataIndex:'fuserID',
                            		hidden:true
                                 },{
                        	       	id:'fname', 
                        	   		dataIndex:'fname',
                        	   		width:150,
                        	   		header: '名称'
                                 },{
                                	id:'ftelphone', 
                         	   		dataIndex:'ftelphone',
                         			renderer:function(value){
                         				return decryptByABC(value);;
                         			},
                         	   		header: '电话'
                                 },{
                            		id:'faccessTime', 
                            		header: '登陆时间', 
                            		dataIndex:'faccessTime'
                                 },{
                            		id:'flastAccessTime', 
                            		header: '最后登陆时间', 
                            		dataIndex:'flastAccessTime'
                                 }]);
var loginStatusPagingToolBar = new Ext.PagingToolbar({
	pageSize:replyLimit,
	store:replyStore,
	displayInfo: true,
	displayMsg: '当前显示第&nbsp;{0}&nbsp;到&nbsp;{1}&nbsp;条,共&nbsp;{2}&nbsp;条',
	emptyMsg: "没有记录",
	plugins: new Ext.ux.ProgressBarPager()
}); 

// create the Grid
var loginStatusGrid = new Ext.grid.GridPanel({
	id:'loginStatusGrid',
	border: true,
	columnLines:true,
	loadMask:true,
	store: loginStatusStore,
	frame:false,
	viewConfig: {forceFit:true},
	monitorResize: true,
	cm: loginStatusCm
});

/**
 * 查看登陆状态
 */
function loginStatus(){
	if(checkLoginTimeOut()){alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var fids=new Array();
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  for(var i=0;i<records.length;i++){
				  fids[i]=records[i].get('fuserID');
			}
	}else{
		 	alertWarring("请选择需要查看活跃度的用户!");return;
	}
	if(!loginStatusWin){
		loginStatusWin = new Ext.Window({
			title:'用户登陆状态查询',
	        id: 'loginStatusWin',
	        layout:'fit',
	        width:700,
	        height:350,
	        frame:true,
	        plain: true,
	        closeAction:'hide',
			maximizable:false,
			resizable:false,
			modal:true,
			border : false,
		    items :[loginStatusGrid]
	    });
	}
	loginStatusStore.load({params:{fUserId:fids}});
	loginStatusWin.show();
}

/***********************活跃度相关**************************/
var replyRecords = new Ext.data.Record.create([
                                         {name: 'fname'},
                                         {name: 'count'}
                                      ]);
var replyStore = new Ext.data.Store({
	   	reader:new Ext.data.JsonReader({
	   		root:'obj'
		},replyRecords),
		proxy:new Ext.data.HttpProxy({
				disableCaching :false,
				url: getPath() + 'useroc/selectActive'
		}),
		remoteStore : true, // 是否远程调用数据
		remoteSort : true   // 是否远程排序
});
replyStore.on("beforeload", function(currentStore, options) {
	var fids=new Array();
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  for(var i=0;i<records.length;i++){
				  fids[i]=records[i].get('fuserID');
			}
	}
	this.baseParams = {
			fUserIds:fids,flag:activeTime
	};
});
var replyCm = new Ext.grid.ColumnModel([
                              	new Ext.grid.RowNumberer(),
                            	{
                            		header: '名称',
                            		id:'fname',
                            		dataIndex:'fname'
                                },{
                            	       	id:'count', 
                            	   		dataIndex:'count',
                            	   		header: '活跃度'
                                }]);
var replyPagingToolBar = new Ext.PagingToolbar({
	pageSize:replyLimit,
	store:replyStore,
	displayInfo: true,
	displayMsg: '当前显示第&nbsp;{0}&nbsp;到&nbsp;{1}&nbsp;条,共&nbsp;{2}&nbsp;条',
	emptyMsg: "没有记录",
	plugins: new Ext.ux.ProgressBarPager()
}); 

// create the Grid
var replyGrid = new Ext.grid.GridPanel({
	id:'replyGrid',
	border: true,
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
		          data: [[0,'一周活跃度'], [1, '一个月活跃度'],[2, '三个月活跃度'],[3, '半年活跃度']]
		      }),
		     valueField: 'myId',
		     displayField: 'displayText',
		     renderer:function(value){
		      	return value.displayText;
		     },
   			listeners : {
   				select: function() {
   					var fids=new Array();
   					if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
   						  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
   						  for(var i=0;i<records.length;i++){
   								  fids[i]=records[i].get('fuserID');
   							}
   					}else{
   						 	alertWarring("请选择需要查看活跃度的用户!");return;
   					}
   					activeTime = Ext.getCmp("searchReply").getValue();
   					replyStore.load({params:{fUserIds:fids,flag:activeTime}});
   				}
   			} 
   		}]
	})
});

/**
 * 查看活跃度
 */
function selectActive(){
	if(checkLoginTimeOut()){alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var fids=new Array();
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  for(var i=0;i<records.length;i++){
				  fids[i]=records[i].get('fuserID');
			}
	}else{
		 	alertWarring("请选择需要查看活跃度的用户!");return;
	}
	if(!replyWin){
		replyWin = new Ext.Window({
			title:'用户活跃度查询',
	        id: 'replyWin',
	        layout:'fit',
	        width:700,
	        height:350,
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
	replyStore.load({params:{fUserIds:fids,flag:activeTime}});
	replyWin.show();
}
