var LIMIT = 150;var mbszWin;var excel;var editIMGList;var sendWin;var replyWin;var detailWin;var timeInterval;
var sendWin;var type=2;var lifeType;var replyLimit = 1;

	//上传图片类型  
	var img_reg = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/;
	var records = new Ext.data.Record.create([  
												{name: 'FID'},
												{name: 'FHeadline'},
												{name: 'FReleaseDate'},
												{name: 'FHeadlineImage'},
												{name: 'FContentHeadline'},
												{name: 'FTypeId'},
												{name: 'FContentMessage'},
												{name: 'FOrderNum'},
												{name: 'FOrderDescription'},
												{name: 'FOrderMoney'},
												{name: 'FUnit'},
												{name: 'FOrderImage'},
												{name: 'FCreaterID'},
												{name: 'FCreateTime'},
												{name: 'FBillState'},
												{name: 'FLag'},
												{name: 'FSeq'},
												{name: 'count'}
											]);

	var store = new Ext.data.Store({
		reader:new Ext.data.JsonReader({
			root:'obj',
			totalProperty:'total'
		},records),
		proxy:new Ext.data.HttpProxy({
				disableCaching :false,
				url: getPath() + 'life/findLifeProjectPage'
		}),
		remoteStore : true, // 是否远程调用数据
		remoteSort : true   // 是否远程排序
	});
	var tool = new Ext.Toolbar({ //工具栏
		items:['分类：',{
				xtype:'combo',
	   			width:100,
				id:'searchLifeType', 
				typeAhead: true,
				triggerAction: 'all',
				lazyRender:true,
			    mode: 'local', 
			  	store: new Ext.data.Store({
			  				autoLoad:true,
					        proxy : new Ext.data.HttpProxy({
				                disableCaching : false,
				                method : 'post',
				                url : getPath() + 'life/findLifeProjectType'
			            }),
				        reader : new Ext.data.JsonReader({root : 'obj'}, [{name : 'fid'}, {name : 'ftypeName'}])
				}),
			    valueField: 'fid',
			    displayField: 'ftypeName',
	   			listeners : {
	   				select: function() {
	   					lifeType = Ext.getCmp('searchLifeType').getValue();
	   					Ext.getCmp("grid").store.load({
	   						params:{
	   							lifeTypeId:lifeType,start:0,limit:LIMIT,type:type
   							}
	   					});
	   				},
	   				load : function(store){ 
	   				    Ext.getCmp('searchLifeType').setValue(store.getAt(0).get('fid')); 
	   					Ext.getCmp("grid").store.load({params:{start:0,limit:LIMIT,type:type,lifeTypeId:lifeType}});
	   				} 
	   			}
			},
		'-',{text: '查看详细',enableToggle: false,iconCls:'common_details',handler:function(){editData("detail");}},
		'-',{text: '启动刷新',enableToggle: false,iconCls:'common_flag',handler:function(){yunzo_start();}},
		'-',{text: '关闭刷新',enableToggle: false,iconCls:'common_nopast',handler:function(){yunzo_stop();}},
		'-',{text: '按评论排序',enableToggle: false,iconCls:'common_top',handler:function(){
			type=1;
			Ext.getCmp("grid").store.reload({params:{start:0,limit:LIMIT,type:type,lifeTypeId:lifeType}});
		}},
		'-',{text: '按点赞排序',enableToggle: false,iconCls:'common_top',handler:function(){
			type=0;
			Ext.getCmp("grid").store.reload({params:{start:0,limit:LIMIT,type:type,lifeTypeId:lifeType}});
		}},
		'-',{
			text: '推送消息',enableToggle: false,iconCls:'common_submit',handler:function(){
				sendData();
			}
		},
		'-',{
			text: '查看评论',
			enableToggle: false,
			iconCls:'common_search',
			handler:function(){
				seachReply();
			}
		}]
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
	    store.on('beforeload', function() {    
		  	  this.baseParams = {
		  			 start:0,limit:LIMIT,type:type,lifeTypeId:lifeType
		  	  };
		});
	    store.load();
        var cm = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(),
            {
                id : 'fid',
                dataIndex:'FID',
                hidden : true
            }, {
                id : 'fheadline',
                header : '标题',
                dataIndex : 'FHeadline'
            }, {
                id : 'fcontentHeadline',
                header : '内容标题',
                dataIndex : 'FContentHeadline'
            }, {
                id : 'ftypeId',
                header : '类型',
                dataIndex : 'FTypeId',
            	hidden:true
            }, {
                id : 'fheadlineImage',
                header : '标题图片',
                dataIndex : 'FHeadlineImage',
                hidden : true
            }, {
            	id:'forderImage',
            	header:'预约图片',
            	dataIndex:'FOrderImage',
            	hidden:true
            }, {
            	id:'forderDescription',
            	header:'分类描述',
            	dataIndex:'FOrderDescription',
            	hidden:true
            }, {
            	id:'forderMoney',
            	header:'预约价格',
            	dataIndex:'FOrderMoney',
            	hidden:true
            },{
        	 	id:'funit',
        	 	header:'计量单位',
        	 	dataIndex:'FUnit',
            	hidden:true
            },{
                id : 'freleaseDate',
                dataIndex : 'FReleaseDate',
                header : '发布日期'
            }, {
                id : 'fcontentMessage',
                dataIndex : 'FContentMessage',
                header : '内容详情',
                hidden : true
            },{
                id : 'forderNum',
                dataIndex : 'FOrderNum',
                header : '订单人数',
                hidden : true
            },{
            	id:'count',
            	dataIndex:'count',
            	header:'点赞数'
            }, {
                id : 'fbillState',
                header : '状态',
                width : 70,
                sortable : true,
                renderer : status,
                dataIndex : 'FBillState'
            },{
            	id:'fseq',
            	dataIndex:'FSeq',
            	hidden:true
            },{
                id : 'flag',
                dataIndex : 'FLag',
                hidden : true
            } ]);
        function status(data) {
            if (data == 0) {
                return '新增';
            } else if (data == 1) {
                return '保存';
            } else if (data == 2) {
                return '提交';
            } else if (data == 3) {
                return '审核';
            } else if (data == 4) {
                return '反审核';
            } else if (data == 5) {
                return '生效';
            } else if (data == 6) {
                return '<span style="color:red">失效</span>';
            } else if (data == 7) {
                return '启用';
            } else if (data == 8) {
                return '禁用';
            } else if (data == 9) {
                return '<span style="color:red">作废</span>';
            } else if (data == 10) {
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
	    
    	new Ext.Viewport({
        	renderTo:'mainDiv',
        	id:'viewport',
        	layout:'border',
        	items:[grid]
        });
    	
	    Ext.EventManager.onWindowResize(function(){ 
	    	grid.getView().refresh() ;
		});
		grid.on('dblclick',function(){ editData("detail");});
	});
	/**************************** 自定义元素 **********************************/
	
	var myImage1 = new Ext.BoxComponent({
		fieldLabel:'logo图片',
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
	//类型
	var ybasicTypeStore  = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({ 
			disableCaching :false,
			method:'post',
			url : getPath() + '/type/findTypeHql'
		}),
		reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
	});
	ybasicTypeStore.load({params:{fmodelId:4}});
	var YBasicType = new Ext.form.ComboBox({
				fieldLabel : '信息分类',
				id : 'ybasicType',
				hiddenName : 'YBasicType.fid',
				typeAhead : true,
				triggerAction : 'all',
				lazyRender : true,
				mode : 'local',
				store : ybasicTypeStore,
				forceSelection : true,
				valueField : 'fid',
				displayField : 'fname',
	            blankText:'该字段不允许为空',
	            allowBlank: false
			});
	function searchBy(){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		//通过id获取输入的数据
		var searchCondition = Ext.getCmp("searchCondition").getValue();
		//模糊查询
		store.load({params:{fheadline:searchCondition,start:0,limit:LIMIT}});
	}

	//删除
	function delBy(){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		var row = Ext.getCmp("grid").getSelectionModel().getSelected();
		if(!row){
	   	 	alertWarring("请选择需要删除的通知信息!");return;
	    }
		Ext.Ajax.request({
			url : getPath() + 'groupsInform/deleteInform',
			params : {fid : row.get('fid')},
			
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
	function editData(row){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		row = Ext.getCmp("grid").getSelectionModel().getSelected();
		if(!row){
	    	 alertWarring("请选择需要查看详情的生活项目信息!");
	    }else{
	 		showDetail(row);
	    }
		return;
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
			params:{prefix:'XX-SHTTTZ'},
			success:function(response, options) {
				Ext.getCmp("fnumber").setValue(Ext.util.JSON.decode(response.responseText).serialNumber);
			}
		});
	}
	
	/***********************回复相关**************************/
	var replyRecords = new Ext.data.Record.create([
                                             {name: 'fid'},
                                             {name: 'fmemberId'},
                                             {name: 'fcommentContent'},
                                             {name: 'fcommentTime'}
                                          ]);
   var replyStore = new Ext.data.Store({
   	   	reader:new Ext.data.JsonReader({
   	   		root:'obj',
   	   		totalProperty:'total'
   		},replyRecords),
   		proxy:new Ext.data.HttpProxy({
   				disableCaching :false,
   				url: getPath() + 'life/findLifeProjectComment'
   		}),
   		remoteStore : true, // 是否远程调用数据
   		remoteSort : true   // 是否远程排序
   });
   replyStore.on("beforeload", function(currentStore, options) {
		var row = Ext.getCmp("grid").getSelectionModel().getSelected();
		this.baseParams = {
				lifeProjectId : row?row.get('FID'):0,limit:replyLimit,start:0
		};
	});
   var replyCm = new Ext.grid.ColumnModel([
                                  	new Ext.grid.RowNumberer(),
                                	{id:'fid',hidden:true},
                                	{
                                		header: '评论人',
                                		id:'fmemberId',
                                		dataIndex:'fmemberId',
                                		hidden:true/*,
                                		renderer:function(value){
                                			return value.fname;
                                		}*/
                                     },{
                                	       	id:'fcommentContent', 
                                	   		dataIndex:'fcommentContent',
                                	   		width:150,
                                	   		header: '评论内容'
                                     },{
                                    		id:'fcommentTime', 
                                    		header: '评论时间', 
                                    		dataIndex:'fcommentTime'
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
 		items:[{
 			text: '&nbsp;删&nbsp;除&nbsp;',
 			enableToggle: false,
 			iconCls:'common_delete',
 			handler: function(){
 				deleteReply();
 			}
 		}]
 	}),
   	bbar: replyPagingToolBar
   });
//查看评论
function seachReply(){
	if(checkLoginTimeOut()){alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var row = Ext.getCmp("grid").getSelectionModel().getSelected();
	if(!row){
   	 	alertWarring("请选择生活项目信息!");return;
    }
	if(!replyWin){
		replyWin = new Ext.Window({
			title:'生活项目评论',
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
	replyStore.load({params:{lifeProjectId:row.get('fid'),start:0,limit:replyLimit}});
	replyWin.show();
}

function deleteReply(){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		if(Ext.getCmp('replyGrid').getSelectionModel().hasSelection()){
			  var records=Ext.getCmp('replyGrid').getSelectionModel().getSelections();
			  var fids=new Array();
			  for(var i=0;i<records.length;i++){
				  fids[i]=records[i].get('fid');
			  }
			 Ext.MessageBox.show({
			    title: '提示',
			    msg: "确认要删除生活项目评论!",
			    width:250,
			    buttons: Ext.MessageBox.YESNO,
			    fn: function(e) {
			    		if(e == 'yes') {
			    			deleteReplyAjax(fids);
			    		}
			    	},
			    icon : Ext.MessageBox.QUESTION
			});
		}
		else
		{
			Ext.MessageBox.show({
				title: '提示',
				msg: "请选择生活项目评论!",
				width: 150,
				buttons: Ext.MessageBox.OK,
				buttonAlign: 'center'
			});
		}
	}

	function deleteReplyAjax(param){
		Ext.Ajax.request({
			url : getPath() + 'life/deleteLifeProjectComment',
			params : {commentIds : param},
			success: function (result, request) {
				var data = Ext.util.JSON.decode(result.responseText);
				Ext.Msg.alert("消息",data.msg);
				Ext.getCmp("replyGrid").store.reload();
			},
			failure: function ( result, request) {
				alertError("系统运行超时或执行失败");
			}
		});
	}
//以手机端查看信息详情
function showDetail(row){
	if(!detailWin){
		detailWin = new Ext.Window({
        	id: 'detailWin',
        	title:'查看详情',
        	closeAction:'hide',
			maximizable:false,
			resizable:false,
        	autoScroll:true,
            width:360,
            height:590,
			bodyStyle:'background:#fff;padding:5px;',
			html:'<div style="width:320px;height:100%;"><p class="xx_title"></p><div class="time_people"><p class="xx_time"></p><p class="xx_peoples"></p></div>'+
					'<div class="xx_center"><ul class="xx_item"></ul><div class="xx_detail"></div><div class="xx_desc"></div></div></div><div class="xx_loader"></div>'
		});
	}


	detailWin.show();
	
	$(".xx_title").html(row.get('FContentHeadline'));
		  $(".xx_time").html((row.get('FReleaseDate').length>11?row.get('FReleaseDate').substr(0,10):row.get('FReleaseDate')));
		  if(row.get('FOrderNum') && row.get('FOrderNum')!=0){
		  	$(".xx_peoples").html(row.get('FOrderNum')+"人参加");
		  }
		  if(row.get('FOrderImage') && row.get('FOrderImage').length>0){
		  	$('.xx_detail').after('<img class="xx_img" src="'+row.get('FOrderImage')+'" />');
		  }
		  var item = "";
		  if(row.get('FOrderMoney')){
			 //item+='<li>预约金额:'+row.get('FOrderMoney')+((row.get('FUnit'))?row.get('FUnit'):"")+'</li>';
		  }
		  $('.xx_item').html(item);
		  $('.xx_detail').html(row.get('FContentMessage').replace(/width:/g,"").replace(/text-indent:/g,"").replace(/margin:/g,"").replace(/font-size:/g,"").replace(/line-height/g,""));
		  //$('.xx_desc').html(row.get('FOrderDescription').replace(/width:/g,"").replace(/text-indent:/g,"").replace(/margin:/g,"").replace(/font-size:/g,"").replace(/line-height/g,""));
		  $(".xx_detail img").each(function () {
		        var src = $(this).attr("src");
		        var str= '<img src="'+src+'" class="img" alt=""/>';
		        $(this).replaceWith(str);
		  });
		  $(".xx_desc img").each(function () {
		        var src = $(this).attr("src");
		        var str= '<img src="'+src+'" class="img" alt=""/>';
		        $(this).replaceWith(str);
		  });
	$(".xx_title").removeClass("hide");
	$(".time_people").removeClass("hide");
	$(".xx_center").removeClass("hide");
	$(".xx_loader").addClass("hide");
}
//启动实时刷新页面
function yunzo_start(){
	if(!timeInterval){
		timeInterval = setInterval(function(){
			Ext.getCmp("grid").store.reload();
	    },300000);
	}
	alertWarring("已启动实时刷新");
}
//关闭实时刷新页面
function yunzo_stop(){
	if(timeInterval){
		clearInterval(timeInterval);
	}
	alertWarring("已关闭实时刷新");
}
/***********************************推送生活信息给会员*******************************************/

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
	title:'选择会员',
	loader : treeLoaderHY,
	border:false,
	rootVisible : false,
	autoScroll : true,
	monitorResize: true,
	mode:'local',
	root:rootNodeHY,
	listeners:{
		beforeload:function(node) {//加载前
			if(node.attributes.level=='1'){
				treeLoaderHY.dataUrl = getPath() + 'life/createTreechild?id='+node.attributes.id; // 定义每个节点的Loader	加载团体
			}else if(node.attributes.level=='2'){
				treeLoaderHY.dataUrl = getPath() + 'life/createTrees?id='+node.attributes.id; // 定义每个节点的Loader	加载会员
			}else{
				treeLoaderHY.dataUrl = getPath() + 'life/getOrgTree'; // 定义每个节点的Loader	加载组织
			}
		}
	}
});
treePanelHY.on('checkchange', function(node, checked) {   
	node.expand();   
	node.attributes.checked = checked;   
	node.eachChild(function(child) {   
		child.ui.toggleCheck(checked);   
		child.attributes.checked = checked;   
		child.fireEvent('checkchange', child, checked);   
	});   
}, treePanelHY); 

function sendData(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var row = Ext.getCmp("grid").getSelectionModel().getSelected();
	if(!row){
    	 alertWarring("请选择需要推送的生活项目信息!");
    }else{
	     if(!sendWin){
	    	 sendWin = new Ext.Window({
		        id: 'sendWin',
		        title:'推送消息',
		        layout:'fit',
	            width:400,
	            height:580,
	            frame:true,
	            plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
				autoScroll:false,
			    items :[treePanelHY],
			    buttons:[{
				    text: '发送',
				    handler:function(){
				    	var ids = treePanelHY.getChecked();
				    	var funEntryIds=[];
				    	if(ids && ids.length){
			    			 for(var i=0;i<ids.length;i++){
			    				funEntryIds.push(ids[i].attributes.id);
			    			 }
			    		}
				    	var lifeIds = new Array();
				    	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
							  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
							  for(var i=0;i<records.length;i++){
								  lifeIds[i]=records[i].get('FID');
							  }
				    	}else{
				    		Ext.Msg.alert("消息","未选中生活项目！");
				    		return;
				    	}
				 		var myMask = new Ext.LoadMask(Ext.getBody(), {
								msg : '正在处理......请稍后！',
								removeMask : true // 完成后移除
							});
					 	myMask.show();
						Ext.Ajax.request({
							url: getPath() +'life/pushLifeProject',
							method: 'POST',
							params:'memberIds='+funEntryIds+'&lifeIds='+lifeIds,
			     			text: "saving...",    
			     			success: function (result, request){
			     					var data = Ext.util.JSON.decode(result.responseText);
			          			   	Ext.Msg.alert("消息",data.msg);
			          			   	
									myMask.hide();
						    		Ext.getCmp('sendWin').hide();
			     			}
						});
				    }
			    },{
			    	text:'取消',
				    handler:function(){
					 	 Ext.getCmp('sendWin').hide();
				    }
			    }]
	        });
		 }
	     sendWin.show();
     }
}

