//上传图片类型  
var img_reg = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/;
	

var date = new Date(); // 得到系统日期
		var dateString = date.getFullYear() + "-" + (date.getMonth() + 1) + "-"
			+ date.getDate() + " " + date.getHours() + ":"+ date.getMinutes() + ":" + date.getSeconds();
		function getPath(){
			var curWwwPath=document.location.href;
		    var pathName=document.location.pathname;
		    var pos=curWwwPath.indexOf(pathName);
		    var localhostPaht=curWwwPath.substring(0,pos);
		    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
		    return(localhostPaht+projectName+'/');
		}
		
		var verity="";
		//产生验证码  
		//window.onload = createCode();
		function createCode(){  
			document.getElementById("createCheckCode").src=document.getElementById("createCheckCode").src + "?nocache="+new Date().getTime();    
		} 

		//初始化数据
		$(function(){
			
			$('.submit').click(function(){
				checkInfo();
			});
			$('.goback').click(function(){
				location.href=getPath();return false;  
			});
			
			$(".fynsigned").each(function(index, domEle){
				 if($(domEle).val()==0){
					 if($(domEle).is(':checked')){
						 $('.signing').addClass("hidden");
					 }else{
						 $('.signing').removeClass("hidden");
					 }
				 }
			});
			
			$('.fynsigned').change(function(){
				if(this.value==1){
					$('.signing').removeClass("hidden");
				}else{
					$('.signing').addClass("hidden");
				}
			});
			$("#fsignedTime").datePicker({
				clickInput:true
			});
			//所属组织加载下拉数据
			$.ajax({ 
				type: "POST",// 指定是post还是get,当然此处要提交,当然就要用post了   
		         url: getPath()+"/org/findAllOrg",  //发送请求的地址。   
		         dataType: "json",//返回纯文本字符串 timeout:20000,// 设置请求超时时间（毫秒）。   
		         error: function () {}, 
		         success://请求成功后回调函数。  
		        	 function(message) {  
		        	 	if(message.obj && message.obj.list){
        	 				var termItem = document.getElementById("YBasicOrganization").options;
		        	 		for(var i =0;i<message.obj.list.length;i++){
		        	 			var data  = message.obj.list[i];
		        	 			if(data && data.fname){
		        	 				var newoption = new Option(data.fname,data.fid);
		        	 				termItem.add(newoption);
		        	 			}
		        	 		}
		        			$("#YBasicOrganization").change(function(){
		        					getSuper(this.value);
        					});
		        	 	} 
		           }  
		    });
			
			//团体类型加载下拉数据
			$.ajax({ 
				type: "POST",
		         url: getPath()+"/type/findTypeHql",
		         data:{fmodelId:1},
		         dataType: "json",
		         error: function () {}, 
		         success:function(message) {  
		        	 	if(message.obj){
        	 				var termItem = document.getElementById('YBasicTypeFid').options;
		        	 		for(var i =0;i<message.obj.length;i++){
		        	 			var data  = message.obj[i];
		        	 			if(data && data.fname){
		        	 				var newoption = new Option(data.fname,data.fid);
		        	 				termItem.add(newoption);
		        	 			}
		        	 		}
		        	 	} 
		           }  
		    });
			
			//区域加载下拉数据
			$.ajax({ 
				type: "POST",
		         url: getPath()+"/dis/findAllDis",
		         dataType: "json",
		         error: function () {}, 
		         success:function(message) {  
		        	 	if(message.obj){
        	 				var termItem = document.getElementById('YBasicDistrictFid').options;
		        	 		for(var i =0;i<message.obj.length;i++){
		        	 			var data  = message.obj[i];
		        	 			if(data && data.fname){
		        	 				var newoption = new Option(data.fname,data.fid);
		        	 				termItem.add(newoption);
		        	 			}
		        	 		}
		        	 		$('#YBasicDistrictFid').change(function(e){
		        	 			$('#YBasicProvinceFid').find("option").remove();
		        	 			$('#YBasicCityFid').find("option").remove();
		        	 			$('#YBasicCountyFid').find("option").remove();
		        			    getPrio($('#YBasicDistrictFid').val());
		        	 		});
		        	 	} 
		           }  
		    });

			//所属销售加载下拉数据
			$.ajax({ 
				type: "POST",
		         url: getPath()+"emp/findAllEmp",
		         dataType: "json",
		         error: function () {}, 
		         success:function(message) {  
		        	 	if(message.obj && message.obj.list){
        	 				var termItem = document.getElementById('YBasicEmployee').options;
		        	 		for(var i =0;i<message.obj.list.length;i++){
		        	 			var data  = message.obj.list[i];
		        	 			if(data && data.fname){
		        	 				var newoption = new Option(data.fname,data.fid);
		        	 				termItem.add(newoption);
		        	 			}
		        	 		}
		        	 	} 
		           }  
		    });

			//加载编号
			$.ajax({ 
				type: "POST",
		         url: getPath()+"serialNumber/getSerialNumber",
		         data:{prefix:'XX-SHTTXX'},
		         dataType: "json",
		         error: function () {}, 
		         success:function(message) {  
		        	 	if(message && message.serialNumber){
				 			$('#fnumber').val(message.serialNumber);
		        	 	} 
		         }  
		    });
			
		});
		/*****监听选中组织后重新加载 上级团体数据   所属销售数据****/
		function getSuper(value){
			//上级团体加载下拉数据
			$.ajax({ 
				type: "POST",
		         url: getPath()+"groups/findAllGroupPagingList",
		         data:{orgId:value},
		         dataType: "json",   
		         error: function () {}, 
		         success:function(message) {  
		        	 	if(message.list){
        	 				var termItem = document.getElementById('fsuperSocialGroupsId').options;
		        	 		for(var i =0;i<message.list.length;i++){
		        	 			var data  = message.list[i];
		        	 			if(data && data.fname){
		        	 				var newoption = new Option(data.fname,data.fid);
		        	 				termItem.add(newoption);
		        	 			}
		        	 		}
		        	 	} 
		           }  
		    });
		}
		
		/*********监听选中区域加载省份数据***********/
		function getPrio(value){
	 			//省份加载下拉数据
				$.ajax({ 
					type: "POST",
			        url: getPath()+"province/findProvinceById",
			        data:{id:value},
			        dataType: "json",
			        error: function () {}, 
			        success:function(message) {  
			        	 	if(message.obj){
	        	 				var termItem = document.getElementById('YBasicProvinceFid').options;
			        	 		for(var i =0;i<message.obj.length;i++){
			        	 			var data  = message.obj[i];
			        	 			if(data && data.fname){
			        	 				var newoption = new Option(data.fname,data.fid);
			        	 				termItem.add(newoption);
			        	 			}
			        	 		}
			        	 		$('#YBasicProvinceFid').change(function(e){
			        	 			$('#YBasicCityFid').find("option").remove();
			        	 			$('#YBasicCountyFid').find("option").remove();
			        	 			getCity($('#YBasicProvinceFid').val());
			        	 		});
		        			    getCity($('#YBasicProvinceFid').val());
			        	 	} 
			        }  
			    });
		}
		/**********监听选中省份加载城市数据*************/
		function getCity(value){
			//城市加载下拉数据
			$.ajax({ 
				type: "POST",
		         url: getPath()+"/city/findCityHql",
		         data:{id:value},
		         dataType: "json",
		         error: function () {}, 
		         success:function(message) {  
		        	 	if(message.obj){
        	 				var termItem = document.getElementById('YBasicCityFid').options;
		        	 		for(var i =0;i<message.obj.length;i++){
		        	 			var data  = message.obj[i];
		        	 			if(data && data.fname){
		        	 				var newoption = new Option(data.fname,data.fid);
		        	 				termItem.add(newoption);
		        	 			}
		        	 		}
		        	 		$('#YBasicCityFid').change(function(){
		        	 			$('#YBasicCountyFid').find("option").remove();
		        	 			getCounty($('#YBasicCityFid').val());
		        	 		});
		        	 		getCounty($('#YBasicCityFid').val());
		        	 	} 
		           }  
		    });
		}
		
		/*************监听选中城市加载区县数据****************/
		function getCounty(value){
			//加载区县数据
			$.ajax({ 
				 type: "POST",
		         url: getPath()+"/county/findCountyHql",
		         data:{id:value},
		         dataType: "json",
		         error: function () {
		        	 
		         }, 
		         success:function(message) {  
		        	 	if(message.obj){
        	 				var termItem = document.getElementById('YBasicCountyFid').options;
		        	 		for(var i =0;i<message.obj.length;i++){
		        	 			var data  = message.obj[i];
		        	 			if(data && data.fname){
		        	 				var newoption = new Option(data.fname,data.fid);
		        	 				termItem.add(newoption);
		        	 			}
		        	 		}
		        	 	} 
		         }  
		    });
		}
		
		/*提交数据 验证数据格式是否正确*/
		function checkInfo(){
			var errorMsg="";
			
			var fname = $("#fname");
			if(fname.val()=="" || fname.val().length<4 || fname.val().length>50){
				errorMsg="请输入长度在4-50的团体名称";
				alert(errorMsg);
				fname.focus();
	    		return false;
			}
			var fnumberPeople = $("#fnumberPeople");
			if(fnumberPeople.val()=="" || !(/^\d+$/.test(fnumberPeople.val()))){
				errorMsg="请输入数字";
				alert(errorMsg);
				fnumberPeople.focus();
	    		return false;
			}
			
			var fynsigned = $(".fynsigned");
			var fynsignedVal =0;
			fynsigned.each(function(index, domEle){
				 if($(domEle).val()==0){
					 if($(domEle).is(':checked')){
						 fynsignedVal = 0;
					 }else{
						 fynsignedVal = 1;
					 }
				 }
			});
			var fregisterNum = $("#fnumberPeople");
			var fsignedTime = $("#fsignedTime");
			var YBasicOrganization = $("#YBasicOrganization");
			var YBasicEmployee = $("#YBasicEmployee");
			if(fynsignedVal==1){
				if(fregisterNum.val()=="" || !(/^\d+$/.test(fregisterNum.val()))){
					errorMsg="请输入数字";
					alert(errorMsg);
					fregisterNum.focus();
		    		return false;
				}
				if(YBasicOrganization.val()==""){
					errorMsg="请选择所属组织";
					alert(errorMsg);
					YBasicOrganization.focus();
		    		return false;
				}
				if(fsignedTime.val()=="" || !(/^([1-2]\d{3})[\/|\-](0?[1-9]|10|11|12)[\/|\-]([1-2]?[0-9]|0[1-9]|30|31)$/.test(fsignedTime.val()))){
					errorMsg="请输入格式为yyyy-mm-dd日期";
					alert(errorMsg);
					fsignedTime.focus();
		    		return false;
				}
				if(YBasicEmployee.val()==""){
					errorMsg="请选择所属销售人员";
					alert(errorMsg);
					YBasicEmployee.focus();
		    		return false;
				}
			}
			var flevel = $("#flevel");
			if(flevel.val()!="" && flevel.val().length>18){
				errorMsg="请输入长度在1-18的级别";
				alert(errorMsg);
				flevel.focus();
	    		return false;
			}
			var fclientContacts = $("#fclientContacts");
			if(fclientContacts.val()=="" || fclientContacts.val().length<1 || fclientContacts.val().length>50){
				errorMsg="请输入长度在1-18的客户联系人";
				alert(errorMsg);
				fclientContacts.focus();
				return false;
			}

			var fiphone = $("#fiphone");
			if(fiphone.val()=="" || !(/^(1+\d{10})$/.test(fiphone.val()))){
				errorMsg="请输入正确的手机号码";
				alert(errorMsg);
				fiphone.focus();
	    		return false;
			}
			var femail = $("#femail");
			if(femail.val()=="" || !(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/ .test(femail.val()))){
				errorMsg="请输入正确的邮箱";
				alert(errorMsg);
				femail.focus();
	    		return false;
			}
			var faddress = $("#faddress");
			if(faddress.val()!="" && faddress.val().length>50){
				errorMsg="请输入长度在1-50的详细地址";
				alert(errorMsg);
				faddress.focus();
	    		return false;
			}

			var time = fsignedTime.val()==""?"":fsignedTime.val()+" 00:00:00";
			var jsonData = {fsource:'自主注册',fmodifiedTime:dateString,flastModifiedTime:dateString,fcreateTime:dateString,fbillState:11,allowadd:1,fleafNode:0,
	   	        	 fnumber:$("#fnumber").val(),fname:fname.val(),fnumberPeople:fnumberPeople.val(),
		        	 fsuperSocialGroupsId:$("#fsuperSocialGroupsId").val()==""?null:$("#fsuperSocialGroupsId").val(),fynsigned:fynsignedVal,
		        	 fregisterNum:fregisterNum.val(),fsignedTime:time,'YBasicOrganization.fid':YBasicOrganization.val()==""?'4028800046ccc0e40146ccc0e4580000':YBasicOrganization.val(),
		        	 flevel:flevel.val(),fclientContacts:fclientContacts.val(),fiphone:fiphone.val(),femail:femail.val(),faddress:faddress.val()}

			var empVal = YBasicEmployee.val()==""?null:YBasicEmployee.val();
			
			if(YBasicEmployee.val()!=""){
				jsonData['YBasicEmployee.fid']=empVal;
			}
			var YBasicTypeFid = $("#YBasicTypeFid");
			if(YBasicTypeFid.val()!=""){
				jsonData['YBasicType.fid']=YBasicTypeFid.val();
			}
			
			
			var YBasicDistrictFid = $("#YBasicDistrictFid");
			var YBasicProvinceFid = $("#YBasicProvinceFid");
			var YBasicCityFid = $("#YBasicCityFid");
			var YBasicCountyFid = $("#YBasicCountyFid");
			if(YBasicDistrictFid.val()!=""){
				jsonData['YBasicDistrict.fid'] = YBasicDistrictFid.val();
				if(YBasicProvinceFid.val()!=""){
					jsonData['YBasicProvince.fid']=YBasicProvinceFid.val();
					if(YBasicCityFid.val()!=""){
						jsonData['YBasicCity.fid']=YBasicCityFid.val();
						if(YBasicCountyFid.val()!=""){
							jsonData['YBasicCounty.fid']=YBasicCountyFid.val();
						}
					}
				}
			}
			if(errorMsg==""){//验证通过
				var inputCode = $("#veritycode");
			    $.ajax({ 
					type: "POST",  
			         url: getPath()+"/user/checkInputCode",   
			         data: "inputCode="+inputCode.val(),
			         dataType: "json", 
			         error: function () {}, 
			         success:function(message) {
			        	 	if(message.success){
			    	    		$(".submit").addClass("hidden");
			    	    		$.ajax({ 
			    	    			 type: "POST",
			    	    	         url: getPath()+"groups/jx",  
			    	    	         data: jsonData,
			    	    	         dataType: "json",
			    	    	         success:function(data) {  
			    	    	        	 if(data.success){
			    	    	        		 alert("注册成功，请等待管理员审核！");
			    	    	        	 }else{
			    	    	        		 alert(data.msg);
			    	    	        		 $(".submit").removeClass("hidden");
			    	    	        	 }
			        	           	 },
			        	           	 error: function () {
			        	           		 $(".submit").removeClass("hidden");
			    	    	        	 alert("注册失败，请重新输入信息");     	
			        	        	 }
			        	          });
					    	}else{
					    		alert("验证码输入错误！");
					    		inputCode.focus();
					    	}
			           }  
			     });
	    		return false;
	    	}
		}