package com.yunzo.cocmore.core.function.model.mysql;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
@Entity
@Table(name = "y_basic_dynmicandinfo_pushinfo" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicDynamicInfoPush {
	private String fid;
	private String tel;
	private Integer statu;
	private Date updatetime;
	private String pushtitle;
	private String dynamicinformid;
	private String remark;
	private Integer type;
	private String groupId;
	
	public String getGroupId() {
		return groupId;
	}
	public YBasicDynamicInfoPush() {
		super();
		// TODO Auto-generated constructor stub
	}
	public YBasicDynamicInfoPush(String fid, String tel, Integer statu,
			Date updatetime, String pushtitle, String dynamicinformid,
			String remark, Integer type, String groupId) {
		super();
		this.fid = fid;
		this.tel = tel;
		this.statu = statu;
		this.updatetime = updatetime;
		this.pushtitle = pushtitle;
		this.dynamicinformid = dynamicinformid;
		this.remark = remark;
		this.type = type;
		this.groupId = groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	@Id
	@Column(name = "FID", unique = true, nullable = false, length = 36)
	public String getFid() {
		if (!StringUtils.isBlank(this.fid)) {
			return this.fid;
		}
		return UUID.randomUUID().toString();
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Integer getStatu() {
		return statu;
	}
	public void setStatu(Integer statu) {
		this.statu = statu;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getPushtitle() {
		return pushtitle;
	}
	public void setPushtitle(String pushtitle) {
		this.pushtitle = pushtitle;
	}
	public String getDynamicinformid() {
		return dynamicinformid;
	}
	public void setDynamicinformid(String dynamicinformid) {
		this.dynamicinformid = dynamicinformid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
