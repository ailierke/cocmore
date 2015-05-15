var LIMIT = 26;var editWin;

Ext.onReady(function(){
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    
    
    // render the grid to the specified div in the page
    var displayPanel = new Ext.Viewport({
    	renderTo:'mainDiv',
    	layout:'border',
    	items:[]
    });
    Ext.EventManager.onWindowResize(function(){ 
    	grid.getView().refresh() 
	}) 
});
/**************************** 自定义元素 **********************************/

var csszForm = new Ext.form.FormPanel({
     border : false,
	 frame	: true,
	 labelAlign: 'left',
	 buttonAlign: 'center',
	 labelWidth:80,
	 autoScroll:false,
	 containerScroll: true,
	 width: 590,
     height: 240,
     items:[{
         layout: 'column',
         items: [{
             columnWidth: .5,
             layout: 'form',
             border: false,
             defaults:{xtype:'textfield',anchor:'98%'},
             items:[{xtype:'hidden',id:'fid',name:'fid'},{
					        fieldLabel:'编号',
					        id:'fnumber',
					        name:'fnumber',
					        readOnly:true
					 	},{
					        fieldLabel:'固定值',
					        id:'ffixedValue',
					        name:'ffixedValue',
					        maxLength:12,
					        blankText:'该字段不允许为空',
					        listeners:{
					        	blur:function(){
					        		var ffixedValue = Ext.getCmp('ffixedValue').getValue();
					        		var fserialNumber = Ext.getCmp('fserialNumber').getValue();
					        		var fdateValue = Ext.getCmp('fdateValue').getValue();
					        		// 得到系统日期
					        		var date = new Date();
								    var dateString = date.getFullYear()+"_"+(date.getMonth()+1)+"_"+date.getDate();
					        		if(fdateValue=='是'){
					        			Ext.getCmp('fpreview').setValue(ffixedValue + '_' + dateString + '_' + fserialNumber);
					        		}else{
					        			Ext.getCmp('fpreview').setValue(ffixedValue + '_' + fserialNumber);
					        		}
					        	}
					        },
					        allowBlank: false
					 	},{
					        fieldLabel:'是否添加日期',
					        id:'fdateValue',
					        layout:'column',
						 	columns:2,
						 	xtype:'radiogroup',
						 	items:[{boxLabel: "是", name: 'fdateValue',inputValue: '是'},  
						 	       {boxLabel: "否", name: 'fdateValue',inputValue: '否'}]
					 	},{
					        fieldLabel:'流水号',
					        id:'fserialNumber',
					        name:'fserialNumber',
					        listeners:{
					        	blur:function(){
					        		var ffixedValue = Ext.getCmp('ffixedValue').getValue();
					        		var fserialNumber = Ext.getCmp('fserialNumber').getValue();
					        		var fdateValue = Ext.getCmp('fdateValue').getValue();
					        		//获取流水号长度
					        		var length = fserialNumber.length;
					        		Ext.getCmp('fserialNumberLength').setValue(length);
					        		// 得到系统日期
					        		var date = new Date();
								    var dateString = date.getFullYear()+"_"+(date.getMonth()+1)+"_"+date.getDate();
					        		if(fdateValue=='是'){
					        			Ext.getCmp('fpreview').setValue(ffixedValue + '_' + dateString + '_' + fserialNumber);
					        		}else{
					        			Ext.getCmp('fpreview').setValue(ffixedValue + '_' + fserialNumber);
					        		}
					        	}
					        },
					        maxLength:150
					 	}
			 	]},{
			            columnWidth: .5,
			            layout: 'form',
			            border: false,
			            defaults:{xtype:'textfield',anchor:'98%'},
			            items: [yBasicOrganization,{
					 		fieldLabel:'单据类型',
					 		id:'finvoicesType',
					 		name:'finvoicesType',
					        allowBlank: false
					 	},{
					        fieldLabel:'摘要',
					        id:'fpreview',
					        name:'fpreview',
					        maxLength:150
					 	},{
						 	xtype:'hidden',
					        id:'fbillState',
					        name:'fbillState'
					 	},{
					        xtype:'hidden',
					        id:'fcreateTime',
					        name:'fcreateTime'
					 	},{
					        xtype:'hidden',
					        id:'fcreaterId',
					        name:'fcreaterId'
					 	},{
					        xtype:'hidden',
					        id:'fmodifiedTime',
					        name:'fmodifiedTime'
					 	},{
					        xtype:'hidden',
					        id:'fmodifiedId',
					        name:'fmodifiedId'
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
						     id:'flag',
						     name:'flag'
					 	}] 
		        }]
     }]
});
/**************************** 自定义元素结束 *******************************/
/******************************** 操作 **********************************/

function editData(row){
         if(!editWin){
            editWin = new Ext.Window({
		        id: 'editWin',
		        title:'修改编码规则',
		        layout:'fit',
                width:600,
                height:220,
                frame:true,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
			    items :[bmgzForm],
			    buttons:[{
				    text: '保存',
				    handler:function(){
				    	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
				    	if(bmgzForm.form.isValid()){
				        	  bmgzForm.form.submit({
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
			    listeners:{hide:function(){}}
            });
		}
         
	    var date = new Date();  // 得到系统日期
	    var dateString = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	    bmgzForm.getForm().findField('fmodifiedTime').setValue(dateString);
	    bmgzForm.getForm().findField('fmodifiedId').setValue(getUser()[1]);	
	    bmgzForm.getForm().findField('flastModifiedTime').setValue(dateString);
	    bmgzForm.getForm().findField('flastModifiedId').setValue(getUser()[1]);
      	loadData(row);
      	

        bmgzForm.form.url = getPath() + 'base/encodingRules!doNotNeedSessionAndSecurity_updateEncodingRules.ad';
        editWin.show();
	}

function loadData(row) {
	bmgzForm.form.setValues({
			fid : row.get('fid'),
			fnumber  : row.get('fnumber'),
			ffixedValue    : row.get('ffixedValue'),
			fdateValue  : row.get('fdateValue'),
			fserialNumber : row.get('fserialNumber'),
			fpreview : row.get('fpreview'),
			ybasicOrganization  : row.get('ybasicOrganization').fid,
			finvoicesType				: row.get('finvoicesType'),
			fcreaterId : row.get('fcreaterId'),
			fcreateTime:row.get('fcreateTime'),
			flag:row.get('flag'),
			fbillState:10
		});
}

function RcmdForm(stateId){
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  var fids=new Array();
		  for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
		 }
		Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认更新当前编码状态!",
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
		alertWarring("请选择更新状态的编码规则!");
	}
}
function gridSubAjax(param,stateId)
{
	Ext.Ajax.request({
		url : getPath() + 'base/encodingRules!doNotNeedSessionAndSecurity_updateEncodingrulesState.ad',
		params : {fids : param , stateId : stateId},
		//method : 'POST',
		success: function (result, request) 
		{
			Ext.MessageBox.show({
				title : "提示",
				msg : "更新成功！",
				width : 250,
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
			//store.reload();
			Ext.getCmp("grid").store.reload();
		},
		failure: function ( result, request) {
			alertError("系统运行超时或执行失败");
		}
	});
}