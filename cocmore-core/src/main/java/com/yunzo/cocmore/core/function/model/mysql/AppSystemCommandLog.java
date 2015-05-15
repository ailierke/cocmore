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

/**
 * Description: <手机执行命令记录>. <br>
 * @date:2015年3月12日 上午11:11:55
 * @author beck
 * @version V1.0
 */
@Entity
@Table(name = "Y_appSystemCommandLog" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class AppSystemCommandLog {
	private static final long serialVersionUID = 1L;
	
	private String fid;
	private String fascTypeID;
	private String fmid;
	private String fgroupID;
	private Integer fbillState;
	private String fcomment;
	private Date fextTime;
	
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
	
	@Column(name = "FascTypeID", length = 36)
	public String getFascTypeID() {
		return fascTypeID;
	}
	public void setFascTypeID(String fascTypeID) {
		this.fascTypeID = fascTypeID;
	}
	
	@Column(name = "Fmid", length = 50)
	public String getFmid() {
		return fmid;
	}
	
	public void setFmid(String fmid) {
		this.fmid = fmid;
	}
	
	@Column(name = "FgroupID", length = 36)
	public String getFgroupID() {
		return fgroupID;
	}
	public void setFgroupID(String fgroupID) {
		this.fgroupID = fgroupID;
	}
	
	@Column(name = "FBillState")
	public Integer getFbillState() {
		return fbillState;
	}
	public void setFbillState(Integer fbillState) {
		this.fbillState = fbillState;
	}
	
	@Column(name = "FComment", length = 255)
	public String getFcomment() {
		return fcomment;
	}
	public void setFcomment(String fcomment) {
		this.fcomment = fcomment;
	}
	
	@Column(name = "FextTime")
	public Date getFextTime() {
		return fextTime;
	}
	public void setFextTime(Date fextTime) {
		this.fextTime = fextTime;
	}
	
	
}
