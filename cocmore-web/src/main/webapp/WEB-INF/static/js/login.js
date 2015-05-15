function getPath(){
	var curWwwPath=document.location.href;
    var pathName=document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    var localhostPaht=curWwwPath.substring(0,pos);
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    return(localhostPaht+projectName+'/');
}
function checkUserInfo()
{
	var type =0;
	 $(':radio').each(function(index, domEle){
		 if($(domEle).val()==0){
			 if($(domEle).is(':checked')){
				 type = 0;
				}else{
				 type=1;
				}
		 }
	 });
	 var name = $("#username").val(); 
	 var pass = $("#password").val();
//提交到后台进行验证   
$.ajax({ 
		type: "POST",// 指定是post还是get,当然此处要提交,当然就要用post了   
         url: getPath()+"/login",  //发送请求的地址。   
         data: "faccount="+name+"&fuserPassword="+pass+"&type="+type,//发送到服务器的数据   
         dataType: "json",//返回纯文本字符串 timeout:20000,// 设置请求超时时间（毫秒）。   
         error: function () {//请求失败时调用函数。  
        	 
         }, 
         success://请求成功后回调函数。  
        	 function(message) {  
        	 	
        	 	if(message.success){
        	 		var funIds = "," + message.funIds;
        	 		var exp = new Date(); 
        	 		exp.setTime(exp.getTime() + 30*60*1000);
					document.cookie = "yunzoAdmin=" +  escape('["'+message.obj.faccount+'","'+message.obj.fid+'","'+type+'"]')+";expires= '"+ exp.toGMTString()+"'";
					location.href=getPath()+"function/main.jsp";return false;  
		    	}else{
		    		alert(message.msg);
		    	}  
           }  
          }); 
	return false;  
	 
	 	
}
/*
$(document).ready(function(){
	
		$("#login").submit(function() {
		    $("#login").ajaxSubmit({
		    	url:getPath()+"base/adsuser!doNotNeedSessionAndSecurity_login.ad",  
	            type:'POST',
	            dataType:'json',
	            success:function(responseText ){alert(111);
	                	if(responseText .success){
	    					document.cookie = "yunzoAdmin=" + message.obj; 
	    					alert(getPath());
	    					location.href=getPath()+"page/main.html";
	    		    	}else{
	    		    		alert('登陆失败！ 请重新输入账号(密码)');
	    		    	}  
	            }
		    });
		    // return false，这样可以阻止正常的浏览器表单提交和页面转向
//		    return false;
		});
});*/