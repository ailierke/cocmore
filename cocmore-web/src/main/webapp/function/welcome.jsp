<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>欢迎使用</title>
</head>
<script type="text/javascript" src="../static/jquery/jquery.min.js"></script>
<script type="text/javascript" src="../static/easyUI/jquery.easyui.min.js"></script>
<link href="../static/css/easyui.css" rel="stylesheet" type="text/css"/>
<link href="../static/css/icon.css" rel="stylesheet" type="text/css"/>

<body style="background:url(${pageContext.request.contextPath }/static/images/welcome.png) repeat-x top center;">
	
	<!-- <form action="/cocmore-web/test/forword" enctype="multipart/form-data" method="POST">
		<input type="submit" value="跳转到团体信息管理界面" /> 
		<input type="file" value="文件上传" name="file" />
	</form> -->
	
	<form id="fileupload" method="post" enctype="multipart/form-data">   
    <div>   
        <label for="file">Email:</label>   
        <input type="file" value="文件上传" name="file" />
    </div>   
</form>  
<button  onclick="tijiao()">提交</button>
</body>

<script type="text/javascript">
	function tijiao(){
		$('#fileupload').submit(); 
	}
	$('#fileupload').form({    
	    url:"/cocmore-web/test/forword",    
	    onSubmit: function(){    
	        // do some check    
	        // return false to prevent submit;    
	    },    
	    success:function(data){    
	        alert(data);    
	    }    
	});    
	// submit the form    
 

</script>
</html>