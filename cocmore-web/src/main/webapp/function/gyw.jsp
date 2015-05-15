<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>云筑圈管理系统V1.0-关于我</title>
<link rel="stylesheet" type="text/css" href=" ../static/ext/ext-all.css" />
<link rel="stylesheet" href=" ../static/ext/resources/css/xtheme-blue.css" type="text/css"></link>
<link rel="stylesheet" type="text/css" href=" ../static/css/button.css" />
<script type="text/javascript" src=" ../static/ext/ext-base.js"></script>
<script type="text/javascript" src=" ../static/ext/ext-all.js"></script>
<script type="text/javascript" src=" ../static/ext/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src=" ../static/ext/ux/ProgressBarPager.js"></script>
<script type="text/javascript" src=" ../static/ext/ux/PanelResizer.js"></script>
<script type="text/javascript" src=" ../static/ext/ux/PagingMemoryProxy.js"></script>
<script type="text/javascript" src=" ../static/js/StarHtmleditor.js"></script>
<script type="text/javascript" src=" ../static/js/commonUtil.js"></script>
<!-- <script type="text/javascript" src=" ../static/js/gyw.js"></script> -->
<script type="text/javascript">
	var LIMIT = 3;
	var editWin;/*更改，新增window*/
	//var detailWin;/*查看详情window*/

	/*grid需要的数据源store*/
	var store = new Ext.data.Store({
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'count'
		}, [ {
			name : 'fid'
		}, {
			name : 'title'
		}, {
			name : 'content'
		}, {
			name : 'updateTime'
		}, {
			name : 'ybasicSocialgroups'
		},{
			name : 'sequenceNumber'
		} ,{
			name : 'flag'
		}]),
		proxy : new Ext.data.HttpProxy({
			disableCaching : false,
			url : getPath() + 'groupsAbout/findAllAboutPagingList'
		}),
		/*  params:{id:0},  */
		remoteStore : true, // 是否远程调用数据
		remoteSort : true
	// 是否远程排序
	});
	store.on("beforeload", function() {
		this.baseParams = {
				groupId : window.parent.groupId
		};
	});
	var tool = new Ext.Toolbar({ //工具栏
		});
	
	Ext.Ajax.request({
		url : getPath() + 'systemconfigurationMenu/getfunctionjs',
		method : 'GET',
		params : {
			functionId : 5
		},
		success : function(response, options) {
			obj=Ext.util.JSON.decode(response.responseText);
			for(var i=0;i<obj.length;i++){
				tool.add(Ext.util.JSON.decode(obj[i]));tool.add('-');
			}
			tool.doLayout();
		}
	});
	/*extjs准备初始化*/
	Ext.onReady(function() {
				Ext.QuickTips.init();
				
				/*加载数据源*/
				store.on('load', function(store, records, options) {
					if (records.length = 0) {
						Ext.Msg.alert("提示信息", "没有符合条件的数据!");
					}
				});
				/*重新加载store带参数*/
				store.load({
					params : {
						start : 0,
						limit : LIMIT,
						groupId : window.parent.groupId
					}
				});//这里需要改成当前用户所属商会id
				/*grid需要的列的展现模式*/
				var cm = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(),
						{
							id : 'fid',
							hidden : true
						}, {
							id : 'title',
							header : '公司简介',
							width : 80,
							sortable : true,
							dataIndex : 'title'
						}, {
							id : 'updateTime',
							header : '更新时间',
							width : 50,
							format : 'Y-m-d H:i:s',
							sortable : true,
							allowBlank : true,
							dataIndex : 'updateTime'
						}, {
							id : 'content',
							header : '描述',
							hidden : true,
							dataIndex : 'content'
						}, {
							id : 'ybasicSocialgroupsFid',
							header : '所属团体',
							dataIndex : 'ybasicSocialgroups',
							renderer : function(value) {
								return value.fname;
							}
						} ,{
							id : 'sequenceNumber',
							header : '排序号',
							dataIndex : 'sequenceNumber'
						},{
							id : 'flag',
							hidden : true,
							dataIndex : 'flag'
						}]);
				/*分页插件*/
				var pagingToolBar = new Ext.PagingToolbar(
						{
							pageSize : LIMIT,
							store : store,
							displayInfo : true,
							displayMsg : '当前显示第&nbsp;{0}&nbsp;到&nbsp;{1}&nbsp;条,共&nbsp;{2}&nbsp;条',
							emptyMsg : "没有记录",
							plugins : new Ext.ux.ProgressBarPager()
						});
				// create the Grid
				var grid = new Ext.grid.GridPanel(
						{
							region : 'center',
							id : 'grid',
							border : false,
							columnLines : true,
							loadMask : true,
							store : store,
							frame : false,
							viewConfig : {
								forceFit : true
							},
							monitorResize : true,
							cm : cm,
							tbar : tool,
							bbar : pagingToolBar
						});
				// render the grid to the specified div in the page
				if(getUser()[2]==1){
			    	new Ext.Viewport({
			        	renderTo:'mainDiv',//渲染到什么地方
			        	id:'viewport',
			        	layout:'border',
			        	 width:300,
			        	items:[treePanelTuanti,grid]
			        });
			    }else{
			    	new Ext.Viewport({
			        	renderTo:'mainDiv',//渲染到什么地方
			        	id:'viewport',
			        	layout:'border',
			        	items:[grid]
			        });
			    } 
				Ext.EventManager.onWindowResize(function() {
					grid.getView().refresh();
				});
				grid.on('dblclick', editData); 
				/*新增pannel的打开*/
			});
	/*======================自定义元素==========================================*/
	/*更新修改所需要的form*/

	var detailAndUpdateForm = new Ext.form.FormPanel({
		border : false,
		frame : true,
		labelAlign : 'left',
		buttonAlign : 'center',
		labelWidth : 60,
		autoScroll : false,
		containerScroll : true,
		layout : "form",
		defaults : {
			anchor : '95%',
			xtype : 'textfield'
		},
		items : [  {
			xtype : 'hidden',
			fieldLabel : 'flag',
			id : 'flag',
			name : 'flag'
		},{
			xtype : 'hidden',
			fieldLabel : 'id主键',
			id : 'fid',
			name : 'fid'
		}, {
			id : 'title',
			fieldLabel : '名称',
			name : 'title',
			maxLength:50,
			blankText : '该字段不允许为空',
			allowBlank : false
		},{
			xtype:'numberfield',
			fieldLabel : '排序号',
			id:'sequenceNumber',
			name:'sequenceNumber'
		}, {
			xtype:'htmleditor',
			fieldLabel : '内容描述',
			height:270,
			id : 'fdetails',
			name : 'content',
            blankText:'该字段不允许为空',
            allowBlank: false
		}, {
			fieldLabel : '更新时间',
			id : 'updateTime',
			name : 'updateTime',
			xtype:'hidden'
		}, {
			fieldLabel : '所属团体',
			id : 'YBasicSocialgroupsFid',
			name : 'YBasicSocialgroups.fid',
			xtype:'hidden'
		} ]
	});

	/**************************** 操作 **********************************/
	function detailGroupAbout(){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
		var selectRow = Ext.getCmp("grid").getSelectionModel().getSelected();
		if(selectRow){
				//if(!detailWin){
				var   detailWin  =   new  Ext.Window(
			                {
			                    title:selectRow.get("title") ,//标题
			                    width: 600 ,
			                    height: 500 ,
			                    html: "<div style='width:100%;height:100%;padding:5px;background-color:white'><h1 style='text-align:center;font-size:16px;line-height:40px;'>"+selectRow.get("title")+"</h1><div style='font-size:14px;'>"+selectRow.get("content")+"</div>"//panel主体中的内容，可以执行html代码
	
			                });    
			//   }        
			   detailWin.show();
	   }else{
		   alertError("请选择信息!");
	   }
	}
	
	function deleteGroupAbout(){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
		var selectRow = Ext.getCmp("grid").getSelectionModel().getSelected();
		if (selectRow) {
		Ext.MessageBox.confirm('提示', '确认删除!', function(button){
			if( button == 'yes'){
				Ext.Ajax.request({
					   url: getPath()+'groupsAbout/deleteAbout',
					   success: function(response, opts){
						   var data = Ext.util.JSON.decode(response.responseText);
						   alertWarring(data.msg);
						   Ext.getCmp('grid').getStore().reload();
					   },
					   failure: function(response, opts){
						   var data =Ext.util.JSON.decode(response.responseText);
						   alertWarring(data.msg);
					   },
					   params: { fid: selectRow.get('fid') }
					});
				}
			}); 
		} else {
			alertWarring("请选择需要删除的信息!");
		}
	}
	
	function editData(row) {
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
		if (row != 'add') {
			row = Ext.getCmp("grid").getSelectionModel().getSelected();
			if (!row) {
				alertWarring("请选择需要编辑的信息!");
				return;
			}
		}else{
			//getNumberStore();
		}
		this.row = row;
		if (!editWin) {
			editWin = new Ext.Window({
				y : 100,
				id : 'editWin',
				layout : 'fit',
				width : 600,
				height : 420,
				frame : true,
				plain : true,
				closeAction : 'hide',
				maximizable : false,
				resizable : false,
				modal : true,
				items : [ detailAndUpdateForm ],
				buttons : [ {
					text : '保存',
					handler : function() {
						if (detailAndUpdateForm.form.isValid()) {
							detailAndUpdateForm.form.submit({
								method : 'POST',
								waitMsg : '正在提交.....',
								waitTitle : '请稍等',
								success : function(form, action) {
									Ext.getCmp('grid').getStore().reload();
									Ext.getCmp("editWin").hide();
								},
								failure : function(form, action) {
									alertWarring("失败...");
								}
							});
						} else {
							alertWarring("内容填写不完整");
						}
					}
				}, {
					text : '取消',
					handler : function() {
						Ext.getCmp("editWin").hide();
					}

				} ],
				listeners : {
					hide : function() {
						detailAndUpdateForm.form.reset();
					}
				}
			});
		}
		setTitleAndUrl(row, editWin);
		var date = new Date(); // 得到系统日期
		var dateString = date.getFullYear() + "-" + (date.getMonth() + 1) + "-"+ date.getDate() + " " + date.getHours() + ":"+ date.getMinutes() + ":" + date.getSeconds();
		detailAndUpdateForm.getForm().findField('updateTime').setValue(dateString);
		
		if (row == 'add') {
			detailAndUpdateForm.getForm().findField('YBasicSocialgroupsFid').setValue(window.parent.groupId);//从用户的session中查出来
		} else if (row != 'add') {
			loadData(row);
		}
	}
	/*修改的时候为form加载原来的数据*/
	function loadData(row) {
		detailAndUpdateForm.form.setValues({
			fid 			: row.get('fid'),
			flag 			: row.get('flag'),
			content 	: row.get('content'),
			title 			: row.get('title'),
			sequenceNumber	: row.get('sequenceNumber'),
			YBasicSocialgroupsFid : row.get('ybasicSocialgroups').fid//通过dataIndex来获取
		});
	}
	/*为update和add加form的条状uri*/
	function setTitleAndUrl(row, win) {
		if (row == 'add') {
			win.setTitle('增加关于我信息');
			detailAndUpdateForm.form.url = getPath() + 'groupsAbout/addAbout';
		} else {
			win.setTitle('修改关于我信息');
			detailAndUpdateForm.form.url = getPath()+ 'groupsAbout/updateAbout';
		}
		win.show();
	}
</script>
</head>

<body>
	<div id="mainDiv"></div>
</body>
</html>