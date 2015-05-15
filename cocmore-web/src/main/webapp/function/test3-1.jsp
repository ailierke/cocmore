<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
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
	<form id="ff" method="post">   
    <div>   
        <label for="name">主键id:</label>   
        <input class="easyui-validatebox" type="text" name="fid"  />   
    </div>   
    <div>   
        <label for="email">类容:</label>   
        <input class="easyui-validatebox" type="text" name="freplyContents" />   
    </div>  
    <div>   
        <label for="email">标识:</label>   
        <input class="easyui-validatebox" type="text" name="flag" />   
    </div> 
</form>  
 <a id="btn" href="#" onclick="updateForm()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">修改</a>  
</body>
<script type="text/javascript">

</script>
</html>