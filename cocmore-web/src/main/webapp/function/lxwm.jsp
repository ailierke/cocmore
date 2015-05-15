<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
	<title>云筑圈管理系统V1.0-联系我们</title>
	<link rel="stylesheet" type="text/css" href=" ../static/ext/ext-all.css" />
	<link rel="stylesheet" href=" ../static/ext/resources/css/xtheme-blue.css" type="text/css"></link>
	<link rel="stylesheet" type="text/css" href=" ../static/css/button.css"/>
	<script type="text/javascript" src=" ../static/ext/ext-base.js"></script>	
	<script type="text/javascript" src=" ../static/ext/ext-all.js"></script>
	<script type="text/javascript" src=" ../static/ext/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src=" ../static/ext/ux/ProgressBarPager.js"></script>
	<script type="text/javascript" src=" ../static/ext/ux/PanelResizer.js"></script>
	<script type="text/javascript" src=" ../static/ext/ux/PagingMemoryProxy.js"></script>
	<script type="text/javascript" src=" ../static/js/commonUtil.js"></script>
	<script type="text/javascript">
	
	var date = new Date(); // 得到系统日期
	var dateString = date.getFullYear() + "-" + (date.getMonth() + 1) + "-"+ date.getDate() + " " + date.getHours() + ":"+ date.getMinutes() + ":" + date.getSeconds();
	var editWin;/*更改，新增window*/
	var detailWin;/*查看详情window*/
	/*extjs准备初始化*/
	Ext.onReady(function() {
				Ext.QuickTips.init();
				 // render the grid to the specified div in the page
				 /*根据会员或者职员来进行页面的不同显示*/
			   if(getUser()[2]==1){
			    	new Ext.Viewport({
			        	renderTo:'mainDiv',//渲染到什么地方
			        	id:'viewport',
			        	layout:'border',
			        	width:300,
			        	items:[treePanelTuanti,detailAndUpdateForm]
			        });
			    }else{
			    	new Ext.Viewport({
			        	renderTo:'mainDiv',//渲染到什么地方
			        	id:'viewport',
			        	layout:'border',
			        	items:[detailAndUpdateForm]
			        });
			    } 
				loadData(window.parent.groupId) ;
				/* grid.on('dblclick', editData); */
				/*新增pannel的打开*/
			});
	/*======================自定义元素==========================================*/
	/*更新修改所需要的form*/

	var detailAndUpdateForm = new Ext.form.FormPanel({
		region:"center",
		id:'detailAndUpdateForm',
		width:300,
		border : false,
		frame : true,
		labelAlign : 'left',
		buttonAlign : 'center',
		layout : 'hbox',
		layoutConfig : {
			align : 'middle',
			pack : 'center'
		},
		buttons : [ {
			text : '保存',
			handler : function() {
				if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
				if(!(window.parent.groupId)){
					alertWarring("还未选择团体");return;
				}
				Ext.getCmp('YBasicSocialgroupsFid').setValue(window.parent.groupId);
				detailAndUpdateForm.getForm().findField('updateTime').setValue(dateString);
				if (detailAndUpdateForm.form.isValid()) {
					detailAndUpdateForm.form.submit({
						method : 'POST',
						waitMsg : '正在提交.....',
						waitTitle : '请稍等',
						success : function(form, action) {
							loadData(window.parent.groupId);//从新加载form
							alertWarring('保存成功...');
						},
						failure : function(form, action) {
							alertWarring("失败...");
						}
					});
				} else {
					alertWarring("内容填写不完整");
				}
			}
		}
		],
		autoScroll : false,
		containerScroll : true,
		labelWidth : 250,
		items : [{
			xtype : 'fieldset',
			layout : 'form',
			defaults : {anchor : '94%',xtype : 'textfield'},
			labelWidth : 80,
			width : 550,
			title : '联系我们',
			items : [ {
				xtype : 'hidden',
				fieldLabel : 'id主键',
				id : 'fid',
				name : 'fid'
			},{
				xtype : 'hidden',
				fieldLabel : '自动增长',
				id : 'flag',
				name : 'flag'
			}, {
				id : 'tell',
				fieldLabel : '联系电话',
				name : 'tell',
				regex : /^(1[3|4|5|8]+\d{9}|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/
			}, {
				fieldLabel : '公司地址',
				id : 'adress',
				name : 'adress',
				maxLength : 100
			}, {
				fieldLabel : '公司邮箱',
				id : 'mail',
				name : 'mail',
				maxLength : 50,
				vtype:'email'
			}, {
				fieldLabel : '公司网址',
				id : 'uri',
				name : 'uri',
				maxLength : 100
			}, {
				fieldLabel : '公开微信号',
				id : 'wechat',
				name : 'wechat',
				maxLength : 50
			}, {
				xtype:'hidden',
				fieldLabel : '更新时间',
				id : 'updateTime',
				name : 'updateTime'
			}, {
				fieldLabel : '所属团体',
				xtype:'hidden',
				id : 'YBasicSocialgroupsFid',
				name : 'YBasicSocialgroups.fid'
			} ]
		}]
	});

	/**************************** 操作 **********************************/

	/*根据商会的id来获取信息*/
	var row = "" ;
	function loadData(groupId) {
		/*根据groupId获取信息，如果不存在则row=add.存在则为update*/
		if(groupId||groupId!=''){
			Ext.Ajax.request({
				   url: getPath()+'groupsContact/findAllContactPagingList',//查询单条根据groupId
				   params:{groupId:groupId},
				   success: function(response, opts){
					   var data = Ext.util.JSON.decode(response.responseText);
					  // alert("条数："+data.count);
					   if(data.count==1){//最多有一条数据
						   detailAndUpdateForm.form.setValues({
								fid : data.list.fid,
								tell : data.list.tell,
								adress : data.list.adress,
								mail : data.list.mail,
								uri : data.list.uri,
								wechat :data.list.wechat,
								updateTime:data.list.updateTime,
								YBasicSocialgroupsFid : data.list.ybasicSocialgroups.fid,
								flag: data.list.flag
							});
					   	   row = "update";
					   }else if(data.count==0){
						   row = "add";
					   }
					   //alert("##########"+row);

					   /*设置提交时更新还是add还是提示选择group*/
					   setTitleAndUrl(row, editWin);
				   }
				});
		}else{
			alertWarring("请先选择团体，再进行操作");return false;
		}
	}
	/*为update和add加form的条状uri*/
	function setTitleAndUrl(row) {
		if (row == 'add') {
			detailAndUpdateForm.getForm().findField('YBasicSocialgroupsFid').setValue(window.parent.groupId);//从用户的session中查出来
			detailAndUpdateForm.getForm().findField('updateTime').setValue(dateString);
			detailAndUpdateForm.form.url = getPath() + 'groupsContact/addContact';
		} else if(row=='update') {
			detailAndUpdateForm.form.url = getPath()+ 'groupsContact/updateContact';
		}else{
			detailAndUpdateForm.form.url ="javascript:alert('请先选择团体!')";
		}
	}
	
	//重写团体树的单击事件
	treePanel2.removeListener("click", groupClick);
	treePanel2.on('click',function(n){
		window.parent.groupId = n.attributes.id;
		Ext.getCmp('detailAndUpdateForm').getForm().reset();
		loadData(n.attributes.id);
	});
</script>
</head>
	<body>
		<div id="mainDiv"></div>
	</body>
</html>