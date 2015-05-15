var gongGao = "";
var orgId;
var groupId;
var memberId;
var obj;
/*获取项目端口部分地址*/
function getPos(){
	var curWwwPath=document.location.href;
    var pathName=document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    var localhostPaht=curWwwPath.substring(0,pos);
    return localhostPaht;
}
function getCookie(name) {
	var arr = document.cookie
			.match(new RegExp("(^|)" + name + "=([^;]*)(;|$)"));
	if (arr != null) {
		return unescape(arr[2]);
	}
	return null;
}
function delCookie(name) {
	var exp = new Date();
	exp.setTime(exp.getTime() - 1);
	var cval = getCookie(name);
	if (cval != null) {
		document.cookie = name + "= " + cval + ";expires= " + exp.toGMTString();
	}
}
 if(getUser()[2]==0){//团体登陆
	 Ext.Ajax.request({//获取团体管理员的会员Id,团体Id,组织Id
		 url : getPath() + '/member/getByUserId',
		 params : {adminId : getUser()[1]},
		 success: function (response, request){
			 var data = Ext.util.JSON.decode(response.responseText);
			 memberId=data.memberId;
			 groupId=data.groupId;
			 orgId = data.orgId;
		 },
		 failure: function ( result, request) {
		 alertError("系统运行超时或执行失败");
		 }
	 });
 }

Ext.BLANK_IMAGE_URL = getPath() + "/static/ext/resources/images/default/s.gif";

Ext
		.onReady(function() {

			// 顶层控制区
			var topPanel = new Ext.Panel(
					{
						id : 'topPanel',
						region : 'north',
						split : false,
						collapsible : true,
						margins : '5 5 0 5',
						cmargins : '5 5 0 5',
						bodyStyle : 'background:url(' + getPath()
								+ '/static/ext/resources/images/head_bg.jpg);',
						html : '<img src="' + getPath()
								+ '/static/images/logo.png" height="50px"/>',
						height : 100,
						bbar : [
								'当前用户：' + getUser()[0],
								{
									xtype : "tbfill"
								},
								//{
								//	width : 800,
								//	html : '<div id="yunzo_gundong"></div>'
								//},
								{
									xtype : 'tbbutton',
									text : '主页',
									listeners : {
										click : function() {
											var _tabPanel = Ext
													.getCmp("main_tab");
											_tabPanel.setActiveTab('tab_first');
										}
									}
								},
								{
									xtype : 'tbseparator'
								},
								{
									xtype : 'tbbutton',
									text : '修改密码',
									listeners : {
										click : function() {
											changePasswordWin.show();
										}
									}
								},
								{
									xtype : 'tbseparator'
								},
								{
									xtype : 'tbbutton',
									text : '意见反馈',
									handler : function() {
										feedbackWin.show();
									}
								},
								{
									xtype : 'tbseparator'
								},
								{
									xtype : 'tbbutton',
									text : '关闭标签',
									handler : function() {
										// 删除
										Ext.getCmp('main_tab').removeAll();
										Ext
												.getCmp('main_tab')
												.add(
														{
															id : '0',
															title : '首页',
															bodyStyle : 'border:solid 1px #ffffff;padding:0px;',
															iconCls : "main_tab",
															html : '<iframe width="100%" height="100%" frameborder="no" src="welcome.jsp"></iframe>'
														});
										Ext.getCmp('main_tab').activate(0);
									}
								},
								{
									xtype : 'tbseparator'
								},
								{
									xtype : 'tbbutton',
									text : '退出登录',
									listeners : {
										click : function() {
											Ext.Msg.confirm("提示",
													"确认要退出系统吗？\n",
													function(btn) {
														if (btn == "yes") {
															ajaxLoginOut();
														}
													})
										}
									}
								} ]
					});
			// 主面板(已有Tab)
			var _tabPanel = new Ext.TabPanel(
					{
						id : 'main_tab',
						region : 'center',
						margins : '5 5 5 5',
						resizeTabs : false,
						enableTabScroll : true,
						autoScroll : true,
						autoShow : true,
						activeTab : 0,
						items : [ {
							id : 'tab_first',
							title : "首页",
							autoWidth : true,
							bodyBorder : false,
							iconCls : "main_tab",
							layout : 'fit',
							html : '<iframe width="100%" height="100%" frameborder="no" src="welcome.jsp"></iframe>'
						} ]
					});
			// 配置视图
			viewport = new Ext.Viewport({
				layout : 'border',
				listeners : {
					afterRender : function(cmp) {
						Ext.TaskMgr.start(task);// 启动定时器
					}
				},
				items : [ topPanel, _leftForm, _tabPanel ]
			});

			initTree();
		});
var task = { // Ext的定时器，每隔2秒刷新store。
	run : function() {
		// ajaxWarin()
	},
	interval : 2 * 60 * 1000
}
var _leftForm = new Ext.Panel({
	id : 'west-panel',
	region : 'west',
	boarder : true,
	title : '功能导航',
	split : true,
	width : 200,
	minSize : 200,
	maxSize : 200,
	margins : '5 0 5 5',
	cmargins : '5 5 5 5',
	bodyCfg : {
		cls : 'yunzo_toolBg'
	},
	collapsible : true,
	layout : 'accordion',
	layoutConfig : {
		animate : true
	}
});
function addTab(tabPanel, tabId, tabTitle, targetUrl) {
	if (tabPanel.items.length > 10) {
		tabPanel.remove(1);
	}
	//特殊要求  异步上传图片
	if(tabId == "tab10103"){
		tabPanel.add(
				{
					id : tabId,
					title : tabTitle,
					autoWidth : true,
					bodyBorder : false,
					layout : 'fit',
					html : '<iframe id="' + tabId
							+ '" width="100%" height="100%" frameborder="no" src="'+getPos()+'/admanagesystem-web/dtxxgl.html?type='+getUser()[2]+'&orgId='+orgId+'&groupId='+groupId+'"></iframe>',
					closable : true
				}).show();
	}else{
		tabPanel.add(
			{
				id : tabId,
				title : tabTitle,
				autoWidth : true,
				bodyBorder : false,
				layout : 'fit',
				html : '<iframe id="' + tabId
						+ '" width="100%" height="100%" frameborder="no" src="'
						+ getPath() + 'function/' + targetUrl + '"></iframe>',
				closable : true
			}).show();
	}
}
function clickNode(node, event) {
	if (node.isLeaf()) {
		var nodeid = node.id;
		var url = node.attributes.url;
		var id = 'tab' + nodeid;
		var tabPanel = Ext.getCmp('main_tab');
		var n = tabPanel.getComponent(id);
		if (!n) {
			addTab(tabPanel, id, node.text, url);
		} else {
			tabPanel.setActiveTab(id);
		}
	}
}
var tree101;var tree201;var tree301;var tree401;var tree501;
function initTree() {
	Ext.Ajax.request({
		url : getPath() + 'systemconfigurationMenu/getMenu',
		method : 'GET',
		params : {
			adminId : getUser()[1]
		},
		success : function(response, options) {
			obj=Ext.util.JSON.decode(response.responseText);
			var num = obj.length;
			for(var i=0;i<obj.length;i++){
				_leftForm.add(obj[i]);
			}
			_leftForm.doLayout();
			for(var i = 0 ;i<obj.length;i++){
				 this['tree'+(obj[i].mid)+'01'] = new Ext.tree.TreePanel({
					renderTo : "root"+(obj[i].mid)+"01",
					root : new Ext.tree.AsyncTreeNode({
						id : obj[i].mid,
						text : obj[i].title
					}),
					rootVisible : false,
					autoScroll : true,
					bodyStyle : 'background:url(' + getPath()
							+ '/static/ext/resources/images/item_content_bg.jpg);',
			
					height:_leftForm.getInnerHeight()-24*num,
					autoWidth : true,
					border : false,
					loader : new Ext.tree.TreeLoader({
					 dataUrl:getPath() + 'systemconfigurationMenu/createMenutree?menuId='+obj[i].mid
					}),
					width : 200
				});
				 this['tree'+(obj[i].mid)+'01'].on("click", clickNode);
			}
		}
	});
//	var number = 4;
//	var node101 = new Ext.tree.AsyncTreeNode({
//		id : "4",
//		text : "团体管理"
//	});
//	var tree101 = new Ext.tree.TreePanel({
//		renderTo : "root101",
//		root : node101,
//		rootVisible : false,
//		autoScroll : true,
//		bodyStyle : 'background:url(' + getPath()
//				+ '/static/ext/resources/images/item_content_bg.jpg);',
//
//		height:_leftForm.getInnerHeight()-24*number,
//		autoWidth : true,
//		border : false,
//		loader : new Ext.tree.TreeLoader({
//		 dataUrl:getPath() + 'systemconfigurationMenu/createMenutree?menuId=1'
//		}),
//		width : 200
//	});
//	tree101.on("click", clickNode);
//
//	var node201 = new Ext.tree.AsyncTreeNode({
//		id : "1",
//		text : "系统设置"
//	});
//	var tree201 = new Ext.tree.TreePanel({
//		renderTo : "root201",
//		root : node201,
//		rootVisible : false,
//		autoScroll : true,
//		bodyStyle : 'background:url(' + getPath()
//				+ '/static/ext/resources/images/item_content_bg.jpg);',
//		height:_leftForm.getInnerHeight()-24*number,
//		autoWidth : true,
//		border : false,
//		loader : new Ext.tree.TreeLoader({dataUrl:getPath() + 'systemconfigurationMenu/createMenutree?menuId=2'}),
//		width : 200
//	});
//	tree201.on("click", clickNode);
//
//	var node301 = new Ext.tree.AsyncTreeNode({
//		id : "2",
//		text : "权限管理"
//	});
//	var tree301 = new Ext.tree.TreePanel({
//		renderTo : "root301",
//		root : node301,
//		rootVisible : false,
//		autoScroll : true,
//		bodyStyle : 'background:url(' + getPath()
//				+ '/static/ext/resources/images/item_content_bg.jpg);',
//		height:_leftForm.getInnerHeight()-24*number,	
//		autoWidth : true,
//		border : false,
//		loader : new Ext.tree.TreeLoader({dataUrl:getPath() + 'systemconfigurationMenu/createMenutree?menuId=3'}),
//		width : 200
//	});
//	tree301.on("click", clickNode);
//
//	var node401 = new Ext.tree.AsyncTreeNode({
//		id : "3",
//		text : "基础设置"
//	});
//	var tree401 = new Ext.tree.TreePanel({
//		renderTo : "root401",
//		root : node401,
//		rootVisible : false,
//		autoScroll : true,
//		bodyStyle : 'background:url(' + getPath()
//				+ '/static/ext/resources/images/item_content_bg.jpg);',
//		height:_leftForm.getInnerHeight()-24*number,
//		autoWidth : true,
//		border : false,
//		loader : new Ext.tree.TreeLoader({dataUrl:getPath() + 'systemconfigurationMenu/createMenutree?menuId=4'}),
//		width : 200
//	});
//	tree401.on("click", clickNode);

}

function ajaxLoginOut() {
//	Ext.Ajax
//			.request({
//				url : getPath()
//						+ '/base/adsuser!doNotNeedSessionAndSecurity_logout.ad',
//				success : function(response, options) {
					document.cookie = "";
					window.location.href = getPath();
//				}
//			});
}

function ajaxWarin() {
	Ext.Ajax
			.request({
				url : getPath()
						+ '/base/adsystemwaring!doNotNeedSecurity_checkWarningDate.ad',
				success : function(response, options) {
					//document.getElementById("yunzo_gundong").innerHTML = '<marquee class="gonggao" scrolldelay="1" scrollamount="2" direction="left" loop=-1 onMouseOut="this.start()" onMouseOver="this.stop()">'
						//	+ response.responseText + '</marquee>';
				}
			});
}

// {contentEl: 'root101',title:'团体管理',border:false,iconCls:'nav'},
// {contentEl: 'root201',title:'系统设置',border:false,iconCls:'nav'},
// {contentEl: 'root301',title:'权限管理',border:false,iconCls:'nav'},
// {contentEl: 'root401',title:'基础设置',border:false,iconCls:'nav'}>>>>>>> .r504
