package com.yunzo.cocmore.core.function.util;

public enum ResponseCode {
	// 100 客户端返回成功 101 用户验证不正确需要重新登陆 102 服务器验证表单填写不正确 103 操作异常
	// 104 用户版本过低需要升级才能使用 105 用户被禁止使用APP 106 服务器繁忙或者正在升级 107 sign签名不正确
	// 108 电话号码不存在 109 验证码超时 110 验证码不正确
	SUCCESS(100, "客户端返回成功"), 
	VALIDATEFAILER(101, "用户验证不正确需要重新登陆"), 
	FORMWRONG(102, "服务器验证表单填写不正确"), 
	EXCEPTION(103, "操作异常"), 
	VERSIONLOWER(104,"用户版本过低需要升级才能使用"), 
	STOPUSE(105, "用户被禁止使用APP"), 
	SERVERUPDATE(106,"服务器繁忙或者正在升级"), 
	SIGNWRONG(107, "sign签名不正确"), 
	TELLNOTEXSIT(108,"电话号码不存在"), 
	CODETIMEOUT(109, "验证码超时"), 
	VALIDATECODEWRONG(110,"验证码不正确"),
	USERGETSUCCESS(111,"密码修改成功"),
	PASSWORDWRONG(112,"旧密码不正确"),
	MSGC(0, "responseCode"),
	MSGM(1, "message"),
	MSGR(2, "result"),
	SIGNWRONGPARAM(4, "签名及请求参数不能为空"),
	GREEDISTURE(5, "游客也是要尊重产品哦"),
	MSERROR(6, "请使用云筑圈应用访问"),
	MSERRORG(7, "签名及请求参数不能为空"),
	MSERRORH(8, "游客也是要尊重产品哦");

	// 定义自己的构造器
	private int value;// 错误代码
	private String msg;//提示消息

	private ResponseCode(int value, String msg) {
		this.value = value;
		this.msg = msg;
	}

	public int value() {
		return value;
	}

	public String msg() {
		return msg;
	}

	public static void main(String arg[]) {
		System.out.println(ResponseCode.SUCCESS.value());
	}
}
