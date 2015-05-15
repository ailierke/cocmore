package com.yunzo.cocmore.core.function.vo;

public class GroupTreeVo {
	private String  fid;
	private String fname;
	private Integer fbillState;
	private String fsuperSocialGroupsId;
	public GroupTreeVo(String fid, String fname, Integer fbillState,
			String fsuperSocialGroupsId) {
		super();
		this.fid = fid;
		this.fname = fname;
		this.fbillState = fbillState;
		this.fsuperSocialGroupsId = fsuperSocialGroupsId;
	}
	public GroupTreeVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public GroupTreeVo(String fid, String fname, Integer fbillState) {
		super();
		this.fid = fid;
		this.fname = fname;
		this.fbillState = fbillState;
	}
	public Integer getFbillState() {
		return fbillState;
	}
	public String getFid() {
		return fid;
	}
	public String getFname() {
		return fname;
	}
	public String getFsuperSocialGroupsId() {
		return fsuperSocialGroupsId;
	}
	public void setFsuperSocialGroupsId(String fsuperSocialGroupsId) {
		this.fsuperSocialGroupsId = fsuperSocialGroupsId;
	}
	public void setFbillState(Integer fbillState) {
		this.fbillState = fbillState;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
}
