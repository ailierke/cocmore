/**
 * 重载EXTJS-HTML编辑器
 * 
 * @class HTMLEditor
 * @extends Ext.form.HtmlEditor
 * @author wuliangbo
 */
HTMLEditor = Ext.extend(Ext.form.HtmlEditor, {
	    buttonTips : {
	        bold : {
	            title: '加粗 (Ctrl+B)',
	            text: 'Make the selected text bold.',
	            cls: 'x-html-editor-tip'
	        },
	        italic : {
	            title: '斜体 (Ctrl+I)',
	            text: 'Make the selected text italic.',
	            cls: 'x-html-editor-tip'
	        },
	        underline : {
	            title: '下划线 (Ctrl+U)',
	            text: 'Underline the selected text.',
	            cls: 'x-html-editor-tip'
	        },
	        increasefontsize : {
	            title: '字体变大',
	            text: 'Increase the font size.',
	            cls: 'x-html-editor-tip'
	        },
	        decreasefontsize : {
	            title: '字体缩小',
	            text: 'Decrease the font size.',
	            cls: 'x-html-editor-tip'
	        },
	        backcolor : {
	            title: '背景颜色',
	            text: 'Change the background color of the selected text.',
	            cls: 'x-html-editor-tip'
	        },
	        forecolor : {
	            title: '字体颜色',
	            text: 'Change the color of the selected text.',
	            cls: 'x-html-editor-tip'
	        },
	        justifyleft : {
	            title: '左对齐',
	            text: 'Align text to the left.',
	            cls: 'x-html-editor-tip'
	        },
	        justifycenter : {
	            title: '居中',
	            text: 'Center text in the editor.',
	            cls: 'x-html-editor-tip'
	        },
	        justifyright : {
	            title: '右对齐',
	            text: 'Align text to the right.',
	            cls: 'x-html-editor-tip'
	        },
	        insertunorderedlist : {
	            title: '项目符号',
	            text: 'Start a bulleted list.',
	            cls: 'x-html-editor-tip'
	        },
	        insertorderedlist : {
	            title: '编号',
	            text: 'Start a numbered list.',
	            cls: 'x-html-editor-tip'
	        },
	        createlink : {
	            title: '插入超链接',
	            text: 'Make the selected text a hyperlink.',
	            cls: 'x-html-editor-tip'
	        },
	        sourceedit : {
	            title: '源代码',
	            text: 'Switch to source editing mode.',
	            cls: 'x-html-editor-tip'
	        }
	    },addImage : function() {
			var editor = this;
			var imgform = new Ext.FormPanel({
			    region : 'center',
			    labelWidth : 55,
			    frame : true,
			    bodyStyle : 'padding:5px 5px 0',
			    autoScroll : true,
			    border : false,
			    fileUpload : true,
			    items : [{
			       xtype : 'textfield',
			       fieldLabel : '选择文件',
			       name : 'file',
			       id : 'userfile',
			       inputType : 'file',
			       allowBlank : false,
			       blankText : '文件不能为空',
			       height : 25,
			       anchor : '95%'
			    }],
			    buttons : [{
				     text : '上传',
				     type : 'submit',
				     handler : function() {
				      var furl="";
				      furl=imgform.form.findField('userfile').getValue();
				      var type=furl.substring(furl.length-3).toLowerCase();
				      var filename=furl.substring(furl.lastIndexOf("\\")+1);
				      if (furl==""||furl==null) {return;}
				      if(type!='jpg'&&type!='bmp'&&type!='gif'&&type!='png'){
				    	  alert('仅支持jpg、bmp、gif、png格式的图片');
				    	  return;
				      }
				      imgform.form.submit({
					       waitMsg : '正在上传......',
					       waitTitle : '请等待',
					       method : 'POST',
					       url : 'http://10.0.0.21:8082/admanagesystem-web/base/uploadIMG!doNotNeedSessionAndSecurity_uploadListpicture.ad',
		                   success:function(form, action){
					    	   var test=action.result;
					    	   var dd = "<img src=\""+test.obj+"\"/>";
					    	   var value = Ext.getCmp("fdetails").getValue();
					    	   Ext.getCmp("fdetails").setValue(value+dd);
						       win.close();
					       },
					       failure : function(form, action) {
					    	   	form.reset();
					    	   	if (action.failureType == Ext.form.Action.SERVER_INVALID)
					    	   		Ext.MessageBox.alert('警告','上传失败，仅支持jpg、bmp、gif、png格式的图片');
						        }
				       });
				     }
			    }, {
			     text : '关闭',
			     type : 'submit',
			     handler : function() {
			    	 win.close(this);
			     }}
			]
	   });
		var win = new Ext.Window({
          title : "上传图片",
          width : 500,
          height : 120,
          modal : true,
          border : false,
          iconCls : "picture.png",
          layout : "fit",
          items : imgform
		});
		win.show();
	},createToolbar : function(editor) {
		HTMLEditor.superclass.createToolbar.call(this, editor);
		this.tb.insertButton(16, {
					cls : "ss_tb_img",
					handler : this.addImage,
					tooltip: {
				          title: '上传图片/文件',
				          text: 'Insert/edit an image.',
				          cls: 'ss_tb_img'
			        },
					scope : this
				});
		}
});
Ext.reg('StarHtmleditor', HTMLEditor);
