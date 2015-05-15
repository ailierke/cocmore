<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
	<title>云筑圈管理系统V1.0-会员留言管理</title>
	<link rel="stylesheet" type="text/css" href=" ../static/ext/ext-all.css" />
	<link rel="stylesheet" href=" ../static/ext/resources/css/xtheme-blue.css" type="text/css"></link>
	<link rel="stylesheet" type="text/css" href=" ../static/css/button.css"/>
	<link rel="stylesheet" href="../static/ext/ux/fileuploadfield/css/fileuploadfield.css" type="text/css"></link>
	<script type="text/javascript" src=" ../static/ext/ext-base.js"></script>	
	<script type="text/javascript" src=" ../static/ext/ext-all.js"></script>
	<script type="text/javascript" src=" ../static/ext/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src=" ../static/ext/ux/ProgressBarPager.js"></script>
	<script type="text/javascript" src=" ../static/ext/ux/PanelResizer.js"></script>
	<script type="text/javascript" src=" ../static/ext/ux/PagingMemoryProxy.js"></script>
	<script type="text/javascript" src=" ../static/js/StarHtmleditor.js"></script>
	<script type="text/javascript" src=" ../static/ext/ux/fileuploadfield/FileUploadField.js"></script>
	<script type="text/javascript" src=" ../static/js/commonUtil.js"></script>
	<script type="text/javascript">
	var LIMIT = 26;var editWin;var mbszWin;var excel;var editIMGList;

	var records = new Ext.data.Record.create([  
	                                          {name: 'fid'},
	                                          {name: 'fimages'},
	                                          {name: 'fmessage'},
	                                          {name: 'fguestBookTime'},
	                                          {name: 'ybasicMember'},
	                                          {name: 'ybasicSocialgroups'},
	                                   ]);

	var store = new Ext.data.Store({
		reader:new Ext.data.JsonReader({
			root:'list',
			totalProperty:'count'
		},records),
		proxy:new Ext.data.HttpProxy({
				disableCaching :false,
				url: getPath() + 'groupsGuestbook/findAllGuestbookPagingList'
		}),
		remoteStore : true, // 是否远程调用数据
		remoteSort : true   // 是否远程排序
	});
	store.load("beforeload", function() {
		this.baseParams = {
			groupId : window.parent.groupId
		}
	});
	var tool = new Ext.Toolbar({ //工具栏
		items:['留言人：',{
			xtype:'textfield',
			emptyText:'请输入留言人',
			id:'searchCondition',
			width:150,
			listeners : {
			'specialkey' : function(field, e) {
				if(e.getKey() == Ext.EventObject.ENTER) {
					store.load({params:{start:0,limit:LIMIT,groupId:window.parent.groupId}});
				}}
			} 
		}]
	});
	Ext.Ajax.request({
		url : getPath() + 'systemconfigurationMenu/getfunctionjs',
		method : 'GET',
		params : {
			functionId : 7
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
	    	/* {
	    		id:'fimages', 
	    		hidden: true, 
	    		dataIndex:'fimages'
	       }, */{
	    		id:'fmessage', 
	    		header: '通知信息', 
	    		width: 150, 
	    		dataIndex:'fmessage'
	       },{
	    		id:'fguestBookTime', 
	    		header: '留言时间', 
	    		width: 150, 
	    		dataIndex:'fguestBookTime'
	       },{
				id:'ybasicMember',
				dataIndex: 'ybasicMember', 
				header:'创建人',
	    		renderer:function(value){
	        		return value.fname;
	        	}
			},{
	        	id: 'ybasicSocialgroups', 
	        	dataIndex: 'ybasicSocialgroups', 
	        	header:'团体名称',
	        	renderer:function(value){
	        		return value.fname;
	        	}
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
	    	grid.getView().refresh() ;
		});
	});
	/**************************** 自定义元素 **********************************/   
	function searchBy(){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
		//通过id获取输入的数据
		var searchCondition = Ext.getCmp("searchCondition").getValue();
		//模糊查询
		store.load({params:{createUserName:searchCondition,start:0,limit:LIMIT,groupId:window.parent.groupId}});
	}

	</script>
</head>
	<body>
		<div id="mainDiv"></div>
	</body>
</html>