<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<script type="text/javascript" src="../static/jquery/jquery.min.js"></script>
<script type="text/javascript"
	src="../static/easyUI/jquery.easyui.min.js"></script>
<link href="../static/css/easyui.css" rel="stylesheet" type="text/css" />
<link href="../static/css/icon.css" rel="stylesheet" type="text/css" />
<script>
	$(function(){
		$('#dg').datagrid({ 
			toolbar: '#tb',
		    url:'/cocmore-web/test/putCached',  
		    queryParams: {id:"1",random:new Date().toString()},
		    method:"post",
		    fitColumns:true,
		    columns:[[    
		        {field:'fid',title:'fid',width:100},    
		        {field:'freplyContents',title:'freplyContents',width:100},    
		        {field:'flag',title:'flag',width:100,align:'right'}    
		    ]]    
		});  

	});
</script>
<body>
	<table id="dg"></table>

	<div id="tb">
		<a onclick="del()" class="easyui-linkbutton">删除</a> 
		<a onclick="update()" class="easyui-linkbutton">修改</a>
	</div>
	<div id="win"> </div> 
</body>
<script type="text/javascript">
	function del(){
		var row = $('#dg').datagrid("getSelected");
		alert(row.fid);
		$.ajax(
			{ 
				url: "/cocmore-web/test/delCached", 
				data:{id:"1"},
				success: function(){
					alert("删除成功");
			   		 $('#dg').datagrid('reload');
				}
	    	}
	   );
	}
	
	function update(){
		$('#win').window({    
		    width:600,    
		    height:400,    
		    modal:true 
		}); 
		$('#win').window('refresh', 'test3-1.jsp');
		setTimeout("loadForm()",200);
	}
	function loadForm(){
		var row = $('#dg').datagrid("getSelected");
		$('#ff').form('load',{
			fid:row.fid,
			freplyContents:row.freplyContents,
			flag:row.flag
		});
	}
	function updateForm(){
		alert("1111111111111");
		alert($('#ff'));
		$('#ff').form('submit', {    
		    url:"/cocmore-web/test/updateCached",    
		    success:function(data){    
		    	 aler("修改成功"); 
		    } ,   
		});  

	}
</script>
</html>