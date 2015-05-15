<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>云筑圈管理系统V1.0 -用户注册--成都云筑科技有限公司</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/css/register.css">
    <link href="${pageContext.request.contextPath }/static/jquery/default/datePicker.css" rel="stylesheet" media="screen">
</head>

<body>
	<div class="content">	
		<div class="head"></div> 
		<div class="main">
			<h1>团体注册<label>(登陆账号为手机 ,初始密码888888)</label></h1>
			<form id="register" >
				<input id="fid" name="fid" type="hidden"/>
				<input id="flag" name="flag" type="hidden"/>
				<input id="fnumber" name="fnumber" type="hidden"/>
				<input id="fsource" name="fsource" type="hidden"/>
				<input id="fbillState" name="fbillState" type="hidden"/>
				<input id="fmodifiedTime" name="fmodifiedTime" type="hidden"/>
				<input id="flastModifiedTime" name="flastModifiedTime" type="hidden"/>
				<input id="fcreateTime" name="fcreateTime" type="hidden"/>
				<input id="fpreviousNumber" name="fpreviousNumber" type="hidden"/>
				<input id="allowadd" name="allowadd" type="hidden"/>
				<input id="fleafNode" name="fleafNode" type="hidden"/>
				<p><label>团体名称</label><input id="fname" name="fname" type="text" placeholder="团体名称不大于50个文字" autofocus required/><span>*</span></p>
				<!-- <p><label>团体简称</label><input id="fabbreviation" name="fabbreviation" type="text" placeholder="团体简称不大于18个文字" autofocus required/></p> -->
				<p><label>人数</label><input id="fnumberPeople" name="fnumberPeople" type="text" placeholder="人数为整数" autofocus required/><span>*</span></p>
				<p><label>级别</label><input id="flevel" name="flevel" type="text" placeholder="级别" autofocus/></p>
				<p><label>客户联系人</label><input id="fclientContacts" name="fclientContacts" type="text" placeholder="客户联系人" autofocus required/><span>*</span></p>
				<p><label>手机</label><input id="fiphone" name="fiphone" type="text" placeholder="手机如13402343334" autofocus required/><span>*</span></p>
				<!-- <p><label>电话</label><input id="fphone" name="fphone" type="text" placeholder="电话如028-2342312或者手机号码" autofocus required/></p>
				 -->
				<p><label>邮箱</label><input id="femail" name="femail" type="text" placeholder="邮箱如tiaya@qq.com" autofocus required/><span>*</span></p>
				<p><label>是否签约</label><input type="radio" class="fynsigned" name="fynsigned" value="0" checked/>否<input type="radio" class="fynsigned" name="fynsigned" value="1"/>是</p>
				<div class="signing">
					<p>
						<label>所属组织</label>
						<select id="YBasicOrganization" name="YBasicOrganization.fid">
							<option value="">请选择所属组织</option>
						</select><span>*</span>
					</p>
					<p>
						<label>上级团体</label>
						<select id="fsuperSocialGroupsId" name="fsuperSocialGroupsId">
							<option value="">请选择上级团体</option>
						</select>
					</p>
					<p><label>签约人数</label><input id="fregisterNum" name="fregisterNum" type="text" placeholder="签约人数为整数"><span>*</span></p>
					<p><label>签约时间</label><input id="fsignedTime" name="fsignedTime" type="text" placeholder="请输入yyyy-mm-dd日期格式"/><span>*</span></p>
					<p>
						<label>销售人员</label>
						<select id="YBasicEmployee" name="YBasicEmployee.fid">
						<option value="">请选择所属销售人员</option>
						</select><span>*</span>
					</p>
				</div>
				<p>
					<label>团体类型</label>
					<select id="YBasicTypeFid" name="YBasicType.fid" autofocus required>
						<option value="">请选择团体类型</option>
					</select>
				</p>
				<p>
					<select id="YBasicDistrictFid" name="YBasicDistrict.fid"  autofocus>
						<option value="">请选择地区</option>
					</select>
					<select id="YBasicProvinceFid" name="YBasicProvince.fid"  autofocus>
						<option value="">请选择省份</option>
					</select>
					<select id="YBasicCityFid" name="YBasicCity.fid"  autofocus>
						<option value="">请选择城市</option>
					</select>
					<select id="YBasicCountyFid" name="YBasicCounty.fid"  autofocus>
						<option value="">请选择区县</option>
					</select>
				</p>
				<p><label>详细地址</label><input id="faddress" name="faddress" type="text" placeholder="地址" autofocus/></p>
				<p>
					<label>验证码</label>
					<input type="text" name="veritycode" placeholder="验证码" id="veritycode"  autofocus required> 
					<img id="createCheckCode" src="PictureCheckCode"> 
					<a href="javascript:createCode()">看不清？</a> 
					<input type="hidden" name="captcha_str" value=""> <span class="pass-input-msg"></span> 
				</p>
				<div class="btn_area">
					<input type="button" class="submit" value="注&nbsp;&nbsp;册"/>&nbsp;&nbsp;<input type="button" class="goback" value="返回登陆页"/>	
				</div>
			</form>
		</div>
		<div class="login_bottom">Copyright (C) 2014-2015 yunzo.net, All Rights Reserved 版权所有 成都云筑科技有限公司</div>
	</div>
</body>
	<script src='${pageContext.request.contextPath }/static/ext/jquery-1.9.1.js' type='text/javascript'></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/static/jquery/jquery.datePicker-min.js"></script>
	<script src='${pageContext.request.contextPath }/static/js/register.js' type="text/javascript" charset='utf-8'></script>
</html>