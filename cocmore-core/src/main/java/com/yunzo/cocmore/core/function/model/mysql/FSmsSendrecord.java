package com.yunzo.cocmore.core.function.model.mysql;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * FSmsSendrecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "f_sms_sendrecord" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class FSmsSendrecord implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private YBasicMember YBasicMember;
	private String fmobilePhone;
	private Date fsendTime;
	private String fcontent;
	private Integer fbillState;
	private YBasicSocialgroups YBasicSocialgroups;
	private Integer flag;

	// Constructors

	/** default constructor */
	public FSmsSendrecord() {
	}

	/** minimal constructor */
	public FSmsSendrecord(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public FSmsSendrecord(String fid, YBasicMember YBasicMember,
			String fmobilePhone, Date fsendTime, String fcontent,
			Integer fbillState,YBasicSocialgroups YBasicSocialgroups) {
		this.fid = fid;
		this.YBasicMember = YBasicMember;
		this.fmobilePhone = fmobilePhone;
		this.fsendTime = fsendTime;
		this.fcontent = fcontent;
		this.fbillState = fbillState;
		this.YBasicSocialgroups = YBasicSocialgroups;
	}

	// Property accessors
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FMemberID")
	public YBasicMember getYBasicMember() {
		return this.YBasicMember;
	}

	public void setYBasicMember(YBasicMember YBasicMember) {
		this.YBasicMember = YBasicMember;
	}

	@Column(name = "FMobilePhone", length = 36)
	public String getFmobilePhone() {
		return this.fmobilePhone;
	}

	public void setFmobilePhone(String fmobilePhone) {
		this.fmobilePhone = fmobilePhone;
	}

	@Column(name = "FSendTime", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFsendTime() {
		return this.fsendTime;
	}

	public void setFsendTime(Date fsendTime) {
		this.fsendTime = fsendTime;
	}

	@Column(name = "FContent", length = 65535)
	public String getFcontent() {
		return this.fcontent;
	}

	public void setFcontent(String fcontent) {
		this.fcontent = fcontent;
	}

	@Column(name = "FBillState")
	public Integer getFbillState() {
		return this.fbillState;
	}

	public void setFbillState(Integer fbillState) {
		this.fbillState = fbillState;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FGroupsID")
	public YBasicSocialgroups getYBasicSocialgroups() {
		return YBasicSocialgroups;
	}

	public void setYBasicSocialgroups(YBasicSocialgroups yBasicSocialgroups) {
		YBasicSocialgroups = yBasicSocialgroups;
	}

	@Column(name = "FLag")
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	
}