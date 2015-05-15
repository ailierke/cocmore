package com.yunzo.cocmore.core.function.vo;

import java.util.List;

/**
 * 
 * @author ailierke
 *主要使用来装加密的电话号码和 渠道号
 */
public class PushVo {
	private List<String> tels;
	public List<String> getTels() {
		return tels;
	}
	public void setTels(List<String> tels) {
		this.tels = tels;
	}
	private int channelno;
	public int getChannelno() {
		return channelno;
	}
	public void setChannelno(int channelno) {
		this.channelno = channelno;
	}
	
}
