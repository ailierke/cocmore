var feedbackWin;	//修改密码窗口	

Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	
    var feedback = new Ext.form.FormPanel({
        baseCls: 'x-plain',
        labelWidth: 40,
        labelAlign: 'right',
        border: true,
    	frame: true,
        defaultType: 'textfield',
        defaults:{anchor:'93%'},
        items: [{
	 		fieldLabel:'手机',
	 		id:'fcontactInfo',
	 		name:'fcontactInfo',
            regex : /^((1[3|4|5|8]+\d{9})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/,
	        regexText:"格式不正确!",
            blankText:'该字段不允许为空',
            allowBlank: false
	 	},{
	 		xtype:'textarea',
	 		height:270,
	 		fieldLabel:'内容',
	 		id:'fmessage',
	 		name:'fmessage',
            allowBlank: false
	 	}]
    });
    
    feedbackWin = new Ext.Window({
    	y : 100,
        width: 320,
        height: 380,
        closable : true,
        closeAction:'hide',
        resizable : false,
        title : '管理系统使用反馈',
        expandOnShow:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        modal : true,
        items: [feedback],
        buttons: [{
            text: '保存',
            handler: function(){
            	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
    			
	            	if(feedback.form.isValid()){
	            		feedback.form.submit({
								waitTitle : '请稍候',
								waitMsg : '正在保存.......',
								url : getPath() + '/feedback/savefeedback',
								method : 'POST',
								success : function(form, action) {
									alertWarring(action.result.msg);
									feedbackWin.hide();
									//top.location = getPath(); 
								},
								failure : function(form, action) {
									alertWarring(action.result.errors);
									changepwd.form.reset();
								}
						}); 
	            	}
            	
            }
        },{
            text: '重 置',
            handler: function(){
            	feedback.form.reset();
            }
        }]
    });
    
//    changePasswordWin.show();
    
});