<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>云筑圈管理系统V1.0 -用户注册--成都云筑科技有限公司</title>
	<style>
		body,h1,h2,h3,h4,h5,h6,hr,p,blockquote,dl,dt,dd,ul,ol,li,pre,form,fieldset,legend,button,input,textarea,th,td{margin:0;padding:0}html{color:#000;background:#fff;overflow-y:scroll}body,button,input,select,textarea,pre{font-size:12px;font-family:Arial, Helvetica, STHeiti,"宋体"}html.mod-without-msyahei body{font-family:Arial,Helvetica,STHeiti}h1,h2,h3,h4,h5,h6{font-size:100%}address,cite,dfn,em,var{font-style:normal}small{font-size:12px}ul,ol{list-style:none}a{text-decoration:none;color:#2b78e4}a:hover{color:#1d61ad;-webkit-transition:color .15s linear , background-color .3s linear;-moz-transition:color .15s linear , background-color .3s linear;-o-transition:color .15s linear , background-color .3s linear;-ms-transition:color .15s linear , background-color .3s linear;transition:color .15s linear , background-color .3s linear;cursor:pointer}sup{vertical-align:text-top}sub{vertical-align:text-bottom}legend{color:#000}fieldset,img{border:0}button,input,select,textarea{font-size:100%}table{border-collapse:collapse;border-spacing:0}img{-ms-interpolation-mode:bicubic}textarea{resize:vertical}*:focus{outline:0}.left{float:left}.right{float:right}.overflow{overflow:hidden}.hide{display:none}.inlineBlock{display:inline-block;zoom:1}.block{display:block}.inline{display:inline}.error{color:#F00;font-size:12px}.fb{font-weight:bold}.f14{font-size:14px}button{cursor:pointer}.clearfix:after{content:'\20';display:block;height:0;clear:both;visibility:hidden;overflow:hidden}.clearfix{zoom:1}.wordwrap{word-break:break-all;word-wrap:break-word}pre.wordwrap{white-space:pre-wrap}#wrapper{width:100%}#head{height:75px;z-index:100}#nav{width:100%;background:#fafafa;z-index:1}#head,#content,#foot{width:700px;margin-left:auto;margin-right:auto}#foot{text-align:center;color:#7a77c8;margin-top:20px;margin-bottom:20px}body{background:url(/static/passpc-base/img/header_bg.png?v=5e5eae1e.png) repeat-x top}.mod-header{display:inline;font-size:66px}.mod-userbar{position:absolute;top:5px;right:10px;z-index:300}.mod-userbar li{float:left;margin:0 3px;color:#999;height:24px}.mod-userbar li a{color:#261cdc;text-decoration:underline}.mod-userbar-select{position:relative}.mod-ui-arr{border-left:3px solid #fff;border-right:3px solid #fff;display:inline-block;height:4px;line-height:0;width:1px;border-top:4px solid #00c;color:#00c;vertical-align:middle;margin-left:2px;margin-top:2px}.mod-userbar-select div{position:absolute;right:0;top:20px;border:1px solid #9b9b9b;box-shadow:1px 1px 2px #ccc;-moz-box-shadow:1px 1px 2px #ccc;-webkit-box-shadow:1px 1px 2px #ccc;background:#fff;display:none;width:65px}.mod-userbar-select div a{display:block;line-height:24px;padding:0 6px;border-bottom:1px solid #ebebeb;text-decoration:none;width:53px;height:24px}.mod-userbar-select div a:hover{background:#ebebeb}.mod-feedback{position:fixed;display:block;top:200px;right:-1px;height:80px;width:24px;border:1px solid #dfdfdf;border-top-left-radius:5px;-moz-border-radius-topleft:5px;-webkit-border-top-left-radius:5px;border-bottom-left-radius:5px;-moz-border-radius-bottomleft:5px;-webkit-border-bottom-left-radius:5px;background-color:#f7f7f7}.mod-feedback .feedback-inner{color:#1d61ad;font-size:12px;width:16px;margin-top:10px;margin-left:7px}a.mod-feedback:hover .feedback-inner{color:#2e7fdb}.mod-feedback{-moz-background-clip:padding;-webkit-background-clip:padding-box;background-clip:padding-box}
		img{vertical-align: middle;}
		#wrapper{width:100%;/**background:red;**/}
		#head{width:700px;margin:0px auto;}
		#content{width:700px;margin:0px auto;}
		.mod-sub-nav{width:100%;list-style:none;float:left;color:#418dda;font: 16px/36px \5fae\8f6f\96c5\9ed1,\9ed1\4f53;background:url(static/images/pass_ul_nav.png) no-repeat;}
		.mod-sub-nav li{width:183px;padding-left:50px;list-style:none;float:left;}
		.list-active{color:#fff;background:url(static/images/passWord.png) no-repeat right;}
		.mod-step-detail{margin:10px 0px;line-height:30px;}.step-email-info{font-size:14px;margin:10px 0px;}
		.pass-input-container{
				height:50px;
			    margin-buttom:50px;
		}
		.pass-input-container  span{width:80px;float:left;}
		#firstPwd{
				color: #333;
			    font-size: 14px;
			    height: 16px;
			    line-height: 16px;
			    outline: medium none;
			    overflow: hidden;
			    padding: 7px 0 7px 8px;
			    resize: none;
			    transition-duration: 0.3s;
			    transition-property: all;
			    vertical-align: middle;
			    width: 253px;
	    }
		#secondPwd{
				border-color: #ccc #ddd #ddd #ccc;
			    border-image: none;
			    border-style: solid;
			    border-width: 1px;
			    color: #333;
			    font-size: 14px;
			    height: 16px;
			    line-height: 16px;
			    outline: medium none;
			    overflow: hidden;
			    padding: 7px 0 7px 8px;
			    resize: none;
			    transition-duration: 0.3s;
			    transition-property: all;
			    vertical-align: middle;
			    width: 253px;}
		.code{width:150px;background:#cdcdcd;border:1px solid red;}
		input[type='submit']{width:97px;height:41px;background:url(static/images/login/login_bt.jpg);border:none;color:#fff;font: 16px/36px 微软雅黑,黑体;}
		input[type='button']{width:97px;height:41px;margin-left:20px;background:url(static/images/login/login_bt.jpg);border:none;color:#fff;font: 16px/36px 微软雅黑,黑体;}
		#login_bottom {
			color: #555;
		    height: 30px;
		    left: 50%;
		    line-height: 30px;
		    margin-left: -320px;
		    position: absolute;
		    text-align: center;
		    top: 96%;
		    width: 680px;
		}	
		</style>
</head> 
<body>
<div id="wrapper" class=""> 
	<div id="head">  
		<div class="mod-header"> 
			<a href="${pageContext.request.contextPath }"><img src="${pageContext.request.contextPath }/static/images/logo.png"></a>
		</div>
	</div> 
	<div id="content">   
			<ul class="mod-sub-nav">  
				<li class="mod-sub-list">1、确认帐号  </li>
				<li class="mod-sub-list">2、安全验证  </li>
				<li class="mod-sub-list  list-active">3、重置密码 </li>
			</ul> 
			<form action="javascript:validate()" method="post" id="forgotsel"> 
						<div class="mod-step-detail"> 
							<p class="step-email-info">请填写新密码:</p>    
							<div class="pass-input-container" id="pass-auth-select"> 
								<span>新密码:</span><input type="password" class="pass-input vcode-input" name="username" value="" id="firstPwd"> 
								<label class="accountMsg"></label> 
								<span class="pass-input-msg"></span> 
							</div> 
							<div class="pass-input-container vcode-container clearfix"> 
								<span>确认新密码:</span><input type="password" class="pass-input vcode-input" name="veritycode" value="" id="secondPwd">
								 <span class="pass-input-msg"></span> 
							</div> 
						<div>
							<input type="submit" name="" value="修改密码" class="pass-button-submit" id="submit"/> <input type="button" name="" value="返回登陆" class="pass-button-submit"/> 
						</div> 
					</div> 
			</form> 
	</div> 
	<div id="login_bottom">Copyright (C) 2014-2015 yunzo.net, All Rights Reserved 版权所有 成都云筑科技有限公司</div>
</div>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/ext/ext-all.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath }/static/ext/resources/css/xtheme-blue.css" type="text/css"></link>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/css/button.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath }/static/ext/ext-base.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/static/ext/ext-all.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/static/ext/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/static/js/commonUtil.js"></script>
<script src='${pageContext.request.contextPath }/static/ext/jquery-1.9.1.js' type='text/javascript' charset='utf-8'></script>
<script type="text/javascript">
	$(function(){
		$('input[type="button"]').click(function(){
			location.href=getPath();return false;  
		});
	});
/*  var code ; //在全局定义验证码   
window.onload=function check(){
	var url = location.href;  
	var paraString = url.indexOf("=");
	if(paraString == -1){
		document.getElementById("content").innerHTML="操作无效！<a href='"+getPath()+"'>返回</a>";return;
	}else{
		var paraString = url.substring(url.indexOf("=")+1,url.length);
		document.getElementById("step-form-tip").innerHTML="您正在找回的帐号是："+paraString;
	}
} */
//校验验证码  
function validate(){  
		var firstPwd = document.getElementById("firstPwd").value;
		var secondPwd = document.getElementById("secondPwd").value;
		if(!(firstPwd==secondPwd)){
			Ext.MessageBox.show({
	 			title : "提示",
	 			msg : '两次输入的密码不一致！',
	 			width : 250,
	 			buttons : Ext.MessageBox.OK,
	 			icon : Ext.MessageBox.INFO
	 		});
			return;
		}
		var url = location.href; 
		var index = url.indexOf("=");
		var fid = url.substring(index+1,url.length);
		var newpwd = document.getElementById("firstPwd").value;
	 $.ajax({ 
			type: "POST",// 指定是post还是get,当然此处要提交,当然就要用post了   
	         url: getPath()+"/user/updateUserPassword",  //发送请求的地址。   
	         data: "fid="+fid+"&newpwd=" + newpwd,//发送到服务器的数据   
	         dataType: "json",//返回纯文本字符串 timeout:20000,// 设置请求超时时间（毫秒）。   
	         error: function () {//请求失败时调用函数。  
	        	 
	         }, 
	         success://请求成功后回调函数。  
	        	 function(message) {  
	        	 	if(message.success){
	        	 		top.location = getPath(); 
			    	}else{
			    		Ext.MessageBox.show({
				 			title : "提示",
				 			msg : '修改密码失败！',
				 			width : 250,
				 			buttons : Ext.MessageBox.OK,
				 			icon : Ext.MessageBox.INFO
				 		});
			    	}
	           }  
	     });            
} 

</script>
</body>
</html>