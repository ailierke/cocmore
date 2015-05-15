var LIMIT = 26;var editWin;var mbszWin;var excel;var editIMGList;var detailWin;var imgCount=0; var fileCount=0;

//上传图片类型  
var img_reg = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/;
var records = new Ext.data.Record.create([  
                                          {name: 'fid'},
                                          {name: 'fnumber'},
                                          {name: 'fheadline'},
                                          {name: 'fimages'},
                                          {name: 'fmessage'},
                                          {name: 'fcontacts'},
                                          {name: 'ftel'},
                                          {name: 'ybasicSocialgroups'},
                                          {name: 'ybasicMember'},
                                          {name: 'ybasicCity'},
                                          {name: 'ybasicTrade'},
                                          {name: 'ybasicCounty'},
                                          {name: 'ybasicProvince'},
                                          {name: 'fcomment'},
                                          {name: 'fbillState'},
                                          {name: 'fstartTime'},
                                          {name: 'ffinishTime'},
                                          {name: 'fpublisherTime'},
                                          {name: 'fisHide'},
                                          {name: 'flevel'},
                                          {name: 'flag'}
                                   ]);

var store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
		root:'list',
		totalProperty:'count'
	},records),
	proxy:new Ext.data.HttpProxy({
			disableCaching :false,
			url: getPath() + '/demand/findDemandPage'
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
	items:['标题：',{
		xtype:'textfield',
		emptyText:'请输入标题',
		id:'searchCondition',
		width:150
	},'-']
});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 9
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
    	{id:'flag',hidden:true},
    	{
    		id:'fnumber', 
    		header: '编号', 
    		width: 150, 
    		hidden:true,
    		dataIndex:'fnumber'
       },{
    		id:'fheadline', 
    		header: '标题', 
    		width: 150, 
    		dataIndex:'fheadline'
       },{
    		id:'fimages', 
    		hidden: true, 
    		dataIndex:'fimages'
       },{
    		id:'fmessage', 
    		header: '需求信息', 
    		width: 150,
    		hidden:true,
    		dataIndex:'fmessage'
       },{
        	id: 'fcontacts',
        	dataIndex:'fcontacts',
    		width: 150, 
        	header:'联系人'
		},{
        	id: 'ftel', 
        	dataIndex: 'ftel', 
    		width: 150, 
        	header:'联系电话'
		},{
        	id: 'fstartTime',
        	dataIndex:'fstartTime',
        	header:'开始时间',
        	width: 150, 
    		format:"Y-m-d H:i:s"
		},{
        	id: 'ffinishTime', 
        	dataIndex: 'ffinishTime', 
        	header:'结束时间',
        	width: 150, 
    		format:"Y-m-d H:i:s"
		},{
        	id: 'fpublisherTime', 
        	dataIndex: 'fpublisherTime', 
        	header:'发布时间',
        	width: 150, 
    		format:"Y-m-d H:i:s"
		},{
        	id: 'ybasicMember', 
        	dataIndex: 'ybasicMember', 
        	header:'发布人',
        	hidden:true
		},{
        	id: 'ybasicTrade', 
        	dataIndex: 'ybasicTrade', 
        	header:'行业',
        	renderer:function(value){
				if(value){
					return value.fname;
				}else{
					return "";
				}
        	}
		},{
        	id: 'ybasicSocialgroups', 
        	dataIndex: 'ybasicSocialgroups', 
        	header:'团体名称',
        	renderer:function(value){
        		return value.fname;
        	}
		},{
        	id: 'ybasicProvince', 
        	dataIndex: 'ybasicProvince', 
        	header:'省份',
        	renderer:function(value){
				if(value){
					return value.fname;
				}else{
					return "";
				}
        	}
		},{
        	id: 'ybasicCity', 
        	dataIndex: 'ybasicCity', 
        	header:'城市',
        	renderer:function(value){
				if(value){
					return value.fname;
				}else{
					return "";
				}
        	}
		},{
        	id: 'ybasicCounty', 
        	dataIndex: 'ybasicCounty', 
        	header:'区县',
        	renderer:function(value){
				if(value){
					return value.fname;
				}else{
					return "";
				}
        	}
		},{
			header:'级别',
        	id: 'flevel',
        	dataIndex:'flevel',
        	hidden:true
		},{
			id:'fisHide', 
			header: '是否隐藏', 
			width: 80, 
			sortable: true, 
			dataIndex:'fisHide',
			renderer:function(value){
				if(value==0){
					return '否';
				}else
					return '是';
			}
		},{
        	id: 'fcomment', 
        	dataIndex: 'fcomment', 
        	header:'备注'
		},{
			id:'fbillState',
			dataIndex: 'fbillState', 
			header:'状态',
    		renderer:status
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
	grid.on('dblclick',function(){ editData("detail")});
});
/**************************** 自定义元素 **********************************/
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
var YBasicMember = new Ext.form.ComboBox({
	fieldLabel : '发布人',
	id : 'ybasicMember',
	hiddenName : 'YBasicMember.fid',
	typeAhead : true,
	triggerAction : 'all',
	lazyRender : true,
	mode : 'local',
	store : memberStore,
	valueField : 'fid',
	displayField : 'fname',
	emptyText : '请选择发布人',
 	blankText:'该字段不允许为空',
 	allowBlank: false
});
//行业
var ybasicTradeStore = new Ext.data.Store({
	        proxy : new Ext.data.HttpProxy({
	                disableCaching : false,
	                method : 'post',
	                url : getPath() + '/trade/findAllTrade'
            }),
	        reader : new Ext.data.JsonReader({root : 'obj.list'}, [{name : 'fid'}, {name : 'fname'}])
	});
ybasicTradeStore.load();
var YBasicTrade = new Ext.form.ComboBox({
			fieldLabel : '行业',
			id : 'ybasicTrade',
			hiddenName : 'YBasicTrade.fid',
			typeAhead : true,
			triggerAction : 'all',
			lazyRender : true,
			mode : 'local',
			store : ybasicTradeStore,
			valueField : 'fid',
			displayField : 'fname',
			emptyText : '请选择行业',
		 	blankText:'该字段不允许为空',
		 	allowBlank: false
		});
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
		 	blankText:'该字段不允许为空',
		 	allowBlank: false,
			emptyText : '请选择区县'
		});

var editForm = new Ext.form.FormPanel({
	 id:'editForm', 
     border : false,
     fileUpload : true,
     waitMsgTarget : true,
     enctype : 'multipart/form-data',
	 frame	: true,
	 labelAlign: 'left',
	 buttonAlign: 'center',
	 labelWidth:60,
	 width: 600,
     height: 320,
 	 bodyStyle : 'overflow:auto; overflow-x:hidden',
	 items:[{
			        layout : 'column',
			        border : false,
			        items : [{
						            columnWidth: .5,
						            layout: 'form',
						            border: false,
						            defaultType:'textfield',
						            defaults:{anchor:'93%'},
						            items:[{xtype:'hidden',id:'fid',name:'fid'},{
									        id:'fnumber',
									        fieldLabel:'编号',
									        name:'fnumber',
								            xtype:'hidden'
					            	 },YBasicTrade,{
									        fieldLabel:'标题',
									        id:'fheadline',
									        name:'fheadline',
									        maxLength:50,
								            blankText:'该字段不允许为空',
								            allowBlank: false
									 },{
										 	xtype:'hidden',
									        fieldLabel:'图片',
									        id:'fimages',
									        name:'fimages'
									 },{
									        fieldLabel:'联系人',
									        id:'fcontacts',
									        name:'fcontacts',
									        maxLength:24
									 },{
									        fieldLabel:'联系电话',
									        id:'ftel',
									        name:'ftel',
									        regex : /^(1+\d{10}|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/,
									        blankText:'该字段不允许为空',
									        allowBlank: false
						            },{
									 	xtype:'hidden',
									    fieldLabel:'发布时间',
									    id:'fpublisherTime',
									    name:'fpublisherTime',
										format:"Y-m-d H:i:s"
									},{
										 	xtype:'datetimefield',
										    fieldLabel:'开始时间',
										    id:'fstartTime',
										    name:'fstartTime',
											format:"Y-m-d H:i:s"
										},YBasicMember]
		            },{
		            	columnWidth: .5,
			            layout: 'form',
			            border: false,
			            defaults:{anchor:'93%'},
			            items:[
									YBasicProvince,YBasicCity,YBasicCounty,{
									 	xtype:'radiogroup',
									 	layout:'column',
									 	fieldLabel:'是否隐藏',
									 	id:'fisHide',
									 	columns:2,
									 	items:[{boxLabel: "是", name: 'fisHide',inputValue: 1},  
									 	       {boxLabel: "否", name: 'fisHide',inputValue: 0,checked:true}],
									 	blankText:'该字段不允许为空',
									 	allowBlank: false
									},{
										xtype:'combo',
										id:'flevel', 
										fieldLabel: '级别', 
										hiddenName:'flevel',
										typeAhead: true,
										triggerAction: 'all',
										lazyRender:true,
									    mode: 'local',
									 	blankText:'该字段不允许为空',
									 	allowBlank: false, 
									  	store: new Ext.data.ArrayStore({
									          id: 4,
									          fields: [
									              'myId',
									              'displayText'
									          ],
									          data: [[1,1], [2, 2],[3, 3],[4, 4],[5, 5]]
									      }),
									     valueField: 'myId',
									     displayField: 'displayText',
									     renderer:function(value){
									      	return value.displayText;
									     }
								},{
								 	xtype:'datetimefield',
								    fieldLabel:'结束时间',
								    id:'ffinishTime',
								    name:'ffinishTime',
									vtype : 'daterange',
									startDateField : 'fstartTime', 
									format:"Y-m-d H:i:s"
								},{
									xtype:'textfield',
									fieldLabel:'备注',
							        id:'fcomment',
							        name:'fcomment'
								}]
		            }]
	 },{
		 	xtype:'htmleditor',
	        fieldLabel:'需求信息',
	        height:100,
	        width:570,
	        id:'fdetails',
	        name:'fmessage',
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
	    	
	    },
	 {
		 xtype:'hidden',
	     id:'ybasicSocialgroups',
	     name:'YBasicSocialgroups.fid'
	 },{
	        xtype:'hidden',
	        id:'fbillState',
	        name:'fbillState'
	 },{
		 xtype:'hidden',
	     id:'flag',
	     name:'flag'
	 }]
});

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
		    	 alertWarring("请选择需要编辑的需求信息!");return;
		     }
		     /*var billState = row.get("fbillState");
		     if(billState == 3){
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
		    	 alertWarring("请选择需要查看详情的需求信息!");
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
                height:450,
                frame:true,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
				border : false,
			    items :[editForm],
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
			    			editForm.remove('file'+fileCount,true);
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
        editForm.getForm().findField('ybasicSocialgroups').setValue(window.parent.groupId);
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
		ybasicCityStore.load({
			params:{id:row.get('ybasicProvince').fid},
			callback:function(){
				if(row.get('ybasicCity') && row.get('ybasicCity').fid){
					ybasicCountyStore.load({
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
			fheadline    			: row.get('fheadline'),
			fimages				  	: row.get('fimages'),
			fmessage				: row.get('fmessage'),
			fcontacts				: row.get('fcontacts'),
			ftel					: row.get('ftel'),
			fstartTime				: row.get('fstartTime'),
			ffinishTime				: row.get('ffinishTime'),
			fpublisherTime			: row.get('fpublisherTime'),
			fisHide					: row.get('fisHide'),
			flevel					: row.get('flevel'),
			ybasicMember			: row.get('ybasicMember').fid,
			ybasicTrade				: row.get('ybasicTrade').fid,
			ybasicSocialgroups		: row.get('ybasicSocialgroups').fid,
			fcomment  				: row.get('fcomment'),
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
			win.setTitle('增加需求信息');
			Ext.getCmp("editForm").form.url = getPath() + '/demand/saveDemand';
	}else{
			win.setTitle('修改需求信息');
			Ext.getCmp("editForm").form.url = getPath() + '/demand/updateDemand';
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
		/*var state = selectRow.get('fbillState');
		if(state == 9){
			 alertError("该信息已作废，无法操作！");return;
		}else if(state == 5){
			 alertError("该信息只能做反审核，失效操作！");return;
		}else if(state == 3){
			 alertError("该信息只能做反审核，生效操作！");return;
		}else if(state == 0 || state == 10){
			 alertError("该信息只能做审核操作！");return;
		}else if(state == 4 ){
			 alertError("该信息只能做修改，审核操作！");return;
		}*/
		Ext.MessageBox.prompt("提示", "请输入将这条信息作废的原因！",function(bu,txt){
			if(txt && 0<txt.length<125){
				Ext.Ajax.request({
					url : getPath()+'demand/Invalid',
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

//====================================修改状态========================================
function setState(stateId){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  var fids=new Array();
		  var status = new Array();
		  for(var i=0;i<records.length;i++){
				  fids[i]=records[i].get('fid');
				  /*status[i] = records[i].get('fbillState');
				  if(status[i] == stateId){
						 alertError("状态一致，勿重复操作！");return;
				  }else if(stateId != 9 && status[i] == 6){
						 alertError("该信息只能做作废操作！");return;
				  }else if((stateId != 6 && stateId != 4) && status[i] == 5){
						 alertError("该信息只能做反审核，失效操作！");return;
				  }else if((stateId != 4 && stateId != 5) && status[i] == 3){
						 alertError("该信息只能做反审核，生效操作！");return;
				  }else if(stateId != 3 && (status[i] == 0 || status[i] == 10)){
						 alertError("该信息只能做审核操作！");return;
				  }else if(stateId != 3 && status[i]==4){
						 alertError("该信息只能做修改,审核操作!");return;
				  }*/
			}
		 Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认要更新当前需求信息状态!",
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
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择需要更新状态的需求信息!",
			width: 250,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}

function gridSubAjax(param,stateId)
{
	
	Ext.Ajax.request({
		url : getPath() + '/demand/updateState',
		params : {fids : param , status : stateId},
		
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

//====================================删除========================================
function deleteDemand(){
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
		    msg: "确认要删除当前需求信息？!",
		    width:300,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			demandAjax(fids);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}
	else
	{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择需要删除的需求信息!",
			width: 250,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}

function demandAjax(param)
{
	Ext.Ajax.request({
		url : getPath() + '/demand/deleteDemand',
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
		params:{prefix:'FX-XQGL'},
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
		html:'<div class="xx_center hide"><div class=" xx_img scroll relative"><div class="scroll_box" id="scroll_img"><ul class="scroll_wrap"></ul></div><ul class="scroll_position scroll_position_bg hide" id="scroll_position">'+
		    '<li class="on"><a href="javascript:void(0);">1</a></li><li><a href="javascript:void(0);">2</a></li><li><a href="javascript:void(0);">3</a></li></ul></div><h1 class="xx_title"></h1>'+
			'<p class="xx_contect"><label></label></p><ul class="xx_detail"></ul><p class="publisher"> <img src=""/></p><div class="xx_desc"></div></div><div class="xx_loader"></div>'
		});
	}
	detailWin.show();

	$(".xx_title").html(row.get('fheadline'));
	//图片相关展示
	var img = row.get('fimages');

	var images="";
	var position ="";
	var imgAarry = img.split(",");
	for(var i = 0;i<imgAarry.length-1;i++){
		 if(i==0){
			position+='<li class="on"><a href="javascript:void(0);">'+(i+1)+'</a></li>';
		 }else{
			position+='<li><a href="javascript:void(0);">'+(i+1)+'</a></li>';
		 } 
		images+='<li><img src="'+imgAarry[i]+'"/></li>';
	}
	$(".scroll_wrap").html(images);
	$("#scroll_position").html(position);
	//发布人信息相关
	var detail = "";
	if(row.get('ybasicTrade')){
		  detail+='<li>行业&nbsp;'+row.get('ybasicTrade').fname+'</li>';
	}
	if(row.get('ybasicProvince')){
		  detail+='<li>地域&nbsp;'+row.get('ybasicProvince').fname;
		  if(row.get('ybasicCity')){
			  detail += '&nbsp;'+row.get('ybasicCity').fname;
		  }
		  if(row.get('ybasicCounty')){
			  detail += '&nbsp;'+row.get('ybasicCounty').fname;
		  }
		  detail+='</li>';
	}
	if(row.get('fauditTime')){
		  detail+='<li>时间&nbsp;'+(row.get('fstartTime').length>11?row.get('fstartTime').substr(0,10):row.get('fstartTime'));
		  if(row.get('fexpireTime'))
			  detail += '&nbsp;至&nbsp;'+(row.get('ffinishTime').length>11?row.get('ffinishTime').substr(0,10):row.get('ffinishTime'));
		  detail+='</li>';
	}
	//发布人
	if(row.get('ybasicMember')){
		 var publisher = '';
		 if(row.get('ybasicMember').fheadImage){
			publisher+='<img src="'+row.get('ybasicMember').fheadImage+'"/>';
		 }
		 publisher += '发布人&nbsp;&nbsp;'+row.get('ybasicMember').fname;
		 $(".publisher").html(publisher);
	}
	  
	$('.xx_detail').html(detail);
	$('.xx_desc').html('<h2>需求简介</h2><p>'+row.get('fmessage')+'</p>');

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