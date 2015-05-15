<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>云筑圈管理系统V1.0 --成都云筑科技有限公司</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/css/login.css">
	</head>
  <body>
	<div id="login_center">
		<div id="login_area">
			<div id="login_form">
				<form method="post" onsubmit="return  checkUserInfo()">
					<div id="login_tip">
						<p><input  name='type' value="0" type="radio" checked="checked"/>团体</p>
						<p style="margin-left:80px;"><input type="radio" name='type' value="1"/>职员</p>
					</div>
					<div><input class="username" id="username" type="text" placeholder="用户名" autofocus required></div>
					<div><input class="pwd" id="password" type="password"  placeholder="密码" required></div>
					<div id="btn_area">
						<input type="submit" name="submit" id="sub_btn" value="登&nbsp;&nbsp;录">&nbsp;&nbsp;<a href="${pageContext.request.contextPath }/forgetPass.jsp">忘记密码</a>&nbsp;&nbsp;&nbsp;<a href="${pageContext.request.contextPath }/register.jsp" >注&nbsp;&nbsp;册</a>
					</div>
				</form>
			</div>
		</div>
	</div>
	<div id="login_bottom">
		Copyright (C) 2014-2015 yunzo.net, All Rights Reserved 版权所有 成都云筑科技有限公司
	</div>
  </body>
  <script src='${pageContext.request.contextPath }/static/ext/jquery-1.9.1.js' type='text/javascript' charset='utf-8'></script>
	<script src='${pageContext.request.contextPath }/static/js/login.js' type='text/javascript' charset='utf-8'></script>
 	
    <script type="text/javascript">
		if (window.PIE) {
		    $('.rounded').each(function() {
		        PIE.attach(this);
		    });
		} 
		var  height = screen.height;
		$("#login_center").css({"margin-top":-((height-400)/2)});
		document.cookie="";
	</script>
  
</html>