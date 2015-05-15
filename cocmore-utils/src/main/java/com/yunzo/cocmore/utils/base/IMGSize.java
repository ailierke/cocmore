package com.yunzo.cocmore.utils.base;

public enum IMGSize {
	//括号里面对应缩放后的缩略图
	X88("88X88"),X200("200X200"),X300("300X120"),X640("640X240");
	public String size;
	private IMGSize(String size){
		this.size = size;
	}
	public String value() {
		return size;
	}
}
