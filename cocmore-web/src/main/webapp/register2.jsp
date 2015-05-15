<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>云筑圈管理系统V1.0 -用户注册--成都云筑科技有限公司</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/ext/ext-all.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath }/static/ext/resources/css/xtheme-blue.css" type="text/css"></link>	
	<link rel="stylesheet" href="${pageContext.request.contextPath }/static/ext/ux/fileuploadfield/css/fileuploadfield.css" type="text/css"></link>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/css/button.css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath }/static/ext/ext-base.js"></script>	
	<script type="text/javascript" src="${pageContext.request.contextPath }/static/ext/ext-all.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/static/ext/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/static/js/commonUtil.js"></script>
	<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/ext/ux/fileuploadfield/FileUploadField.js"></script>
	<script type="text/javascript">
	var date = new Date(); // 得到系统日期
	var dateString = date.getFullYear() + "-" + (date.getMonth() + 1) + "-"
			+ date.getDate() + " " + date.getHours() + ":"
			+ date.getMinutes() + ":" + date.getSeconds();
	Ext.onReady(function(){
	
		Ext.Ajax.request({
			url:getPath() + '/serialNumber/getSerialNumber',
			params:{prefix:'XX-SHTTXX'},
			success:function(response, options) {
				Ext.getCmp("fnumber").setValue(Ext.util.JSON.decode(response.responseText).serialNumber);
			}
		});
		
	    Ext.QuickTips.init();
	    Ext.form.Field.prototype.msgTarget = 'side';
	    
	  //区域
		var districtStore  = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({ 
				disableCaching :false,
				method:'post',
				url : getPath() + '/dis/findAllDis'
			}),
			reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
		});
		districtStore.load();
		
		//省份
		var provinceStore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({ 
				disableCaching :false,
				method:'post',
				url : getPath() + '/province/findAllProvince'
			}),
			reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
		});
		
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
		//上级团体
		var fsuperSocialGroupsIdStore = new Ext.data.Store({
			        proxy : new Ext.data.HttpProxy({
			                disableCaching : false,
			                method : 'post',
			                url : getPath()
			                    + '/groups/findAllGroupPagingList'
		            }),
			        reader : new Ext.data.JsonReader({root : 'list'}, [{name : 'fid'}, {name : 'fname'}])
			});
		var fsuperSocialGroupsIdCom = new Ext.form.ComboBox({
					fieldLabel : '上级团体',
					id : 'fsuperSocialGroupsId',
					hiddenName : 'fsuperSocialGroupsId',
					typeAhead : true,
					triggerAction : 'all',
					lazyRender : true,
					mode : 'local',
					readOnly:true,
					store : fsuperSocialGroupsIdStore,
					valueField : 'fid',
					displayField : 'fname'
				});
		//所属组织
		var ybasicOrganizationStore = new Ext.data.Store({
					autoLoad:true,
			        proxy : new Ext.data.HttpProxy({
			                disableCaching : false,
			                method : 'post',
			                url : getPath() + '/org/findAllOrg'
		            }),
			        reader : new Ext.data.JsonReader({root : 'obj.list'}, [{name : 'fid'}, {name : 'fname'}])
			});
		var YBasicOrganizationCom = new Ext.form.ComboBox({
					fieldLabel : '所属组织',
					id : 'YBasicOrganization',
					hiddenName : 'YBasicOrganization.fid',
					typeAhead : true,
					triggerAction : 'all',
					lazyRender : true,
					mode : 'local',
					store : ybasicOrganizationStore,
					valueField : 'fid',
					displayField : 'fname',
					listeners:{
						select:function(){
							fsuperSocialGroupsIdStore.load({params:{orgId:this.getValue()}});
							fsuperSocialGroupsIdCom.setReadOnly(false);
						}
					}
				});
		//销售人员
		var YBasicEmployeeStore = new Ext.data.Store({
					autoLoad:true,
			        proxy : new Ext.data.HttpProxy({
			                disableCaching : false,
			                method : 'post',
			                url : getPath() + '/emp/findAllEmp'
		            }),
			        reader : new Ext.data.JsonReader({root : 'obj.list'}, [{name : 'fid'}, {name : 'fname'}])
			});
		var YBasicEmployeeCom = new Ext.form.ComboBox({
					fieldLabel : '销售人员',
					id : 'YBasicEmployee',
					hiddenName : 'YBasicEmployee.fid',
					typeAhead : true,
					triggerAction : 'all',
					lazyRender : true,
					mode : 'local',
					store : YBasicEmployeeStore,
					valueField : 'fid',
					displayField : 'fname'
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
		ybasicTypeStore.load({params:{fmodelId:1}});
		var YBasicType = new Ext.form.ComboBox({
					fieldLabel : '类型',
					id : 'YBasicTypeFid',
					hiddenName : 'YBasicType.fid',
					typeAhead : true,
					triggerAction : 'all',
					lazyRender : true,
					mode : 'local',
					store : ybasicTypeStore,
					valueField : 'fid',
					displayField : 'fname',
					forceSelection : true,
					blankText : '该字段不允许为空',
					allowBlank : false
				});
	/*更新修改所需要的form*/

	var ttxxForm = new Ext.form.FormPanel({
		region:"center",
		id:'ttxxForm',
		border : false,
		frame : true,
		autoScroll:true,
		labelAlign : 'left',
		buttonAlign : 'center',
		enctype:'multipart/form-data', 
		labelWidth : 110,
		containerScroll : true,
		fileUpload:true,
		layout : 'hbox',
		layoutConfig : {
			align : 'middle',
			pack : 'center'
		},
		buttons : [{
		    		text: '&nbsp;提&nbsp;交&nbsp;',
		    		enableToggle: false,
		    		handler:function(){
		    					ttxxForm.getForm().findField('fbillState').setValue(5);
								ttxxForm.getForm().findField('fmodifiedTime').setValue(dateString);
								ttxxForm.getForm().findField('flastModifiedTime').setValue(dateString);
								ttxxForm.getForm().findField('fcreateTime').setValue(dateString);
								if (ttxxForm.form.isValid()) {
									ttxxForm.form.url = getPath()+ 'groups/addRegisterGroup';
									ttxxForm.form.submit({
										method : 'POST',
										waitMsg : '正在提交.....',
										waitTitle : '请稍等',
										success : function(form, action) {
											//var exp = new Date(); 
						        	 		//exp.setTime(exp.getTime() + 30*60*1000);
											//document.cookie = "yunzoAdmin=" +  escape('["'+message.obj.fuserName+'","'+message.obj.fid+'","'+type+'"]')+";expires= '"+ exp.toGMTString()+"'";
											location.href=getPath();return false;
										},
										failure : function(form, action) {
											alertWarring("失败...");
										}
									});
								} else {
									alertWarring("内容填写不完整");
								}
		    		}
				},'-',{
		    		text: '&nbsp;取&nbsp;消&nbsp;',
		    		enableToggle: false,
		    		handler: function(){
		    			location.href=getPath();
					}
				}
		],
		labelWidth : 250,
		items : [{
			xtype : 'fieldset',
			labelWidth : 100,
			width : 800,
			title : '注册团体账号<span style="color:red;font-weight:normal;">&nbsp;&nbsp;&nbsp;&nbsp;账号是填写的电话号码 ,密码是888888</span>',
			items : [ {
			layout:'column',
			border:false,
			items:[{
							columnWidth:0.5,
							layout : "form",
							defaults : {anchor : '90%',xtype : 'textfield'},
							items:[ {
											xtype : 'hidden',
											fieldLabel : 'id主键',
											id : 'fid',
											name : 'fid'
										},{
											xtype: 'hidden',
											id : 'flag',
											name : 'flag'
										}, {
											id : 'fnumber',
											fieldLabel : '编号',
											name : 'fnumber',
											//	readOnly : true,
											blankText : '该字段不允许为空',
											allowBlank : false
										}, {
											fieldLabel : '团体名称',
											id : 'fname',
											name : 'fname',
											maxLength : 50,
											blankText : '该字段不允许为空',
											allowBlank : false
										}, {
											fieldLabel : '团体简称',
											id : 'fabbreviation',
											name : 'fabbreviation',
											maxLength : 18,
											blankText : '该字段不允许为空',
											allowBlank : false
										}, {
											fieldLabel : '人数',
											id : 'fnumberPeople',
											name : 'fnumberPeople',
											xtype:'numberfield',
											blankText : '该字段不允许为空',
											allowBlank : false
										},
										fsuperSocialGroupsIdCom,
										YBasicOrganizationCom, 
										YBasicEmployeeCom,YBasicType, {
											fieldLabel : '是否允许加入商会',
											id : 'allowadd',
											xtype:'radiogroup',
										 	layout:'column',
										 	columns:2,
										 	items:[{boxLabel: "是", name: 'allowadd',inputValue: 1},  
										 	       {boxLabel: "否", name: 'allowadd',inputValue: 0,checked:true}],
										 	blankText:'该字段不允许为空',
										 	allowBlank: false
										},{
											fieldLabel : '是否为叶子节点',
											id : 'fleafNode',
											xtype:'radiogroup',
										 	layout:'column',
										 	columns:2,
										 	items:[{boxLabel: "是", name: 'fleafNode',inputValue: 1},  
										 	       {boxLabel: "否", name: 'fleafNode',inputValue: 0,checked:true}],
										 	blankText:'该字段不允许为空',
										 	allowBlank: false
										},{
											fieldLabel : '信息来源',
											id : 'fsource',
											name : 'fsource',
											value:'自主注册',
											xtype:'hidden'
										}, {
											fieldLabel : '是否签约',
											id : 'fynsigned',
											xtype:'radiogroup',
										 	layout:'column',
										 	columns:2,
										 	items:[{boxLabel: "是", name: 'fynsigned',inputValue: 1},  
										 	       {boxLabel: "否", name: 'fynsigned',inputValue: 0,checked:true}],
										 	blankText:'该字段不允许为空',
										 	allowBlank: false
										},{
											fieldLabel:'签约时间',
											id:'fsignedTime',
											name:'fsignedTime',
											xtype:'datefield',
											format:'Y-m-d H:i:s'
										}, {
											fieldLabel : '级别',
											id : 'flevel',
											name : 'flevel',
											maxLength : 18
										},{
											fieldLabel:'原编号',
											id:'fpreviousNumber',
											name:'fpreviousNumber',
											xtype:'hidden'
										}
							]},{
					        			columnWidth:0.5,
										layout : "form",
										defaults : {anchor : '90%',xtype : 'textfield'},
					        			items:[new Ext.ux.form.FileUploadField({
					        							fieldLabel : 'Logo',
								        		    	id : 'logo',
								        		    	name : 'file',
								        		    	emptyText: 'Select an image',
								        				width : 150,
								        				buttonText: '浏览....'
								        			}),{
														fieldLabel : '客户联系人',
														id : 'fclientContacts',
														name : 'fclientContacts',
														maxLength : 24,
														blankText : '该字段不允许为空',
														allowBlank : false
													}, {
														fieldLabel : '地址',
														id : 'faddress',
														name : 'faddress',
														maxLength : 100
													}, {
														fieldLabel : '电话',
														id : 'fphone',
														name : 'fphone',
														regex : /^((1+\d{10})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/,
														blankText : '该字段不允许为空',
														allowBlank : false
													}, {
														fieldLabel : '邮箱',
														id : 'femail',
														name : 'femail',
														vtype: 'email',
														maxLength:'25',
														blankText : '该字段不允许为空',
														allowBlank : false
													}, {
														fieldLabel : '手机',
														id : 'fiphone',
														name : 'fiphone',
														regex : /^1[3|4|5|8]+\d{9}$/,
														blankText : '该字段不允许为空',
														allowBlank : false
													},
												    {xtype : 'combo',fieldLabel : '地区',id : 'YBasicDistrictFid',hiddenName : 'YBasicDistrict.fid',triggerAction : 'all',lazyInit : true,mode : 'local',
														store: districtStore,
														valueField : 'fid',
														displayField : 'fname',selectOnFocus : true,emptyText : '请选择省份',
														listeners:{
													        select:function(groupSelect){
													        	provinceStore.load({params:{id:groupSelect.getValue()}});
													        	Ext.getCmp('YBasicProvinceFid').focus();
															}
													    }
													} , 
													{xtype : 'combo',fieldLabel : '省份',id : 'YBasicProvinceFid',hiddenName : 'YBasicProvince.fid',triggerAction : 'all',lazyInit : true,mode : 'local',
														store: provinceStore,
														valueField : 'fid',
														displayField : 'fname',selectOnFocus : true,emptyText : '请选择省份',
														listeners:{
													        select:function(groupSelect){
													        	cityStore.load({params:{id:groupSelect.getValue()}});
													        	Ext.getCmp('YBasicCityFid').focus();
															}
													    }
													},
													{xtype : 'combo',fieldLabel : '城市',id : 'YBasicCityFid',hiddenName : 'YBasicCity.fid',triggerAction : 'all',lazyInit : true,mode : 'local',
														store: cityStore,
														valueField : 'fid',
														displayField : 'fname',selectOnFocus : true,emptyText : '请选择城市',
														listeners:{
													        select:function(groupSelect){
													        	countyStore.load({params:{id:groupSelect.getValue()}});
													        	Ext.getCmp('YBasicCountyFid').focus();
															}
													    }},
													{xtype : 'combo',fieldLabel : '区县',id : 'YBasicCountyFid',hiddenName : 'YBasicCounty.fid',triggerAction : 'all',lazyInit : true,mode : 'local',
													    store: countyStore,
													    valueField : 'fid',
													    displayField : 'fname',selectOnFocus : true,emptyText : '请选择区县'
												    }, {
														xtype:'hidden',
														fieldLabel : '状态',
														id : 'fbillState',
														name : 'fbillState'
													}, {
														fieldLabel : '签约人数',
														id : 'fregisterNum',
														name : 'fregisterNum',
														xtype:'numberfield'
													}, {
														fieldLabel : '备注',
														id : 'fcomment',
														name : 'fcomment',
														xtype:'textarea',
														height:50,
														maxLength : 125
													}, {
														fieldLabel : '创建人id',
														id : 'fcreaterId',
														name : 'fcreaterId',
														hidden:true
													}, {
														fieldLabel : '修改人id',
														id : 'fmodifiedId',
														name : 'fmodifiedId',
														hidden:true
													}, {
														fieldLabel : '最后修改人id',
														id : 'flastModifiedId',
														name : 'flastModifiedId',
														hidden:true
													}, {
														fieldLabel : '创建时间',
														id : 'fcreateTime',
														name : 'fcreateTime',
														hidden:true
													}, {
														fieldLabel : '修改时间',
														id : 'fmodifiedTime',
														name : 'fmodifiedTime',
														hidden:true
													}, {
														fieldLabel : '最后修改时间',
														id : 'flastModifiedTime',
														name : 'flastModifiedTime',
														hidden:true
													}
        			        	]
					}]	
				}]
			}]
	});
	    var displayPanel = new Ext.Viewport({
	    	renderTo:'mainDiv',
	    	layout:'border',
	    	items:[ttxxForm]
	    });
	});
	
	

	</script>
</head>

<body>
	<div id="mainDiv"></div>
</body>
</html>