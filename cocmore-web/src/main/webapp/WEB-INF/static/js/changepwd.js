var changePasswordWin;	//修改密码窗口	

Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	
    var changepwd = new Ext.form.FormPanel({
        baseCls: 'x-plain',
        labelWidth: 100,
        labelAlign: 'right',
        border: true,
    	frame: true,
        defaultType: 'textfield',

        items: [{id:'fid',name: 'fid',xtype:"hidden"},{
            fieldLabel: '原始密码',
            //maxLength:3,
            id:'startpwd',
            name: 'startpwd',
            inputType : 'password',
            allowBlank:false,
            blankText:'不能为空',
            listeners:{
	            'blur': function(data){
	            	//获取选中职员对象数据
 					Ext.Ajax.request({
                		url : getPath() + '/user/checkUserPassword',
                		params : {fid : getUser()[1],password : data.getValue()},
                		success : function(response, options) {
                			var obj = Ext.util.JSON.decode(response.responseText);
                			if(!obj.success){
                				Ext.MessageBox.alert('提示', obj.msg);
								changepwd.form.reset();
                			}
                		}
                		
                	});
	            }
	       }
        },{
            fieldLabel: '最新密码',
            minLength:1,
            maxLength:20,
            id:'newpwd',
            inputType : 'password',
            name: 'newpwd',
            allowBlank:false,
            blankText:'不能为空',
            validator:function check(){
            	if(Ext.get('newpwd').dom.value.length < 1 ||Ext.get('newpwd').dom.value.length > 20){
            		return "密码长度必须为1-20位";
            	}else{
            		return true;
            	}
            }
        },{
            fieldLabel: '确认密码',
            minLength:1,
            maxLength:20,
            id:'centainpwd',
            name: 'centainpwd',
            inputType : 'password',
            allowBlank:false,
            blankText:'不能为空',
            validator:function check(){
            	if(Ext.get('centainpwd').dom.value.length < 1 ||Ext.get('centainpwd').dom.value.length > 20){
            		return "密码长度必须为1-20位";
            	}else{
            		return true;
            	}
            }
        }]
    });
    
    changePasswordWin = new Ext.Window({
    	y : 100,
        width: 320,
        height: 180,
        closable : true,
        closeAction:'hide',
        resizable : false,
        title : '请记住您填写的新密码',
        expandOnShow:true,
        bodyStyle:'padding:15px;',
        buttonAlign:'center',
        modal : true,
        items: [changepwd],

        buttons: [{
            text: '修 改',
            handler: function(){
            	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
            	if(document.getElementById('newpwd').value != document.getElementById('centainpwd').value){
            		Ext.MessageBox.alert('提示', '密码输入不一致，请确认密码正确');
            		return false;
            	}else{
	            	if(changepwd.form.isValid()){
						changepwd.form.submit({
								waitTitle : '请稍候',
								waitMsg : '正在修改.......',
								url : getPath() + '/user//updateUserPassword',
								method : 'POST',
								success : function(form, action) {
									Ext.MessageBox.alert('提示', action.result.msg);
									top.location = getPath(); 
								},
								failure : function(form, action) {
									Ext.MessageBox.alert('警告', action.result.errors);
									changepwd.form.reset();
								}
						}); 
	            	}
            	}
            }
        },{
            text: '重 置',
            handler: function(){
            	changepwd.form.reset();
            }
        }]
    });
    Ext.getCmp("fid").setValue(getUser()[1]);
//    changePasswordWin.show();
    
});