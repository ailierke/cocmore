package com.yunzo.cocmore.core.function.model.mysql;

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

/**
 * YAppdevice entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_appdevice" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YAppdevice implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields
	private String fid;
	private String fdeviceId;
	private YBasicSocialgroups YBasicSocialgroups;
	private String fdeviceOs;
	private String fdeviceOsversion;
	private String fdeviceType;
	private String fappVersion;
	private String fappChannelNo;
	private String fuserName;
	private String fclient;
	private String fsessionToken;
	private String fsign;
	private String fmd5info;
	private String fmid;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YAppdevice() {
	}

	/** minimal constructor */
	public YAppdevice(String fdeviceId) {
		this.fdeviceId = fdeviceId;
	}

	/** full constructor */
	public YAppdevice(String fdeviceId, YBasicSocialgroups YBasicSocialgroups,
			String fdeviceOs, String fdeviceOsversion, String fdeviceType,
			String fappVersion, String fappChannelNo, String fuserName,
			String fclient, String fsessionToken, String fsign,
			String fmd5info, String fmid) {
		this.fdeviceId = fdeviceId;
		this.YBasicSocialgroups = YBasicSocialgroups;
		this.fdeviceOs = fdeviceOs;
		this.fdeviceOsversion = fdeviceOsversion;
		this.fdeviceType = fdeviceType;
		this.fappVersion = fappVersion;
		this.fappChannelNo = fappChannelNo;
		this.fuserName = fuserName;
		this.fclient = fclient;
		this.fsessionToken = fsessionToken;
		this.fsign = fsign;
		this.fmd5info = fmd5info;
		this.fmid = fmid;
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
	
	@Column(name = "FDeviceId",length = 36)
	public String getFdeviceId() {
		return this.fdeviceId;
	}

	public void setFdeviceId(String fdeviceId) {
		this.fdeviceId = fdeviceId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FbusinessId")
	public YBasicSocialgroups getYBasicSocialgroups() {
		return this.YBasicSocialgroups;
	}

	public void setYBasicSocialgroups(YBasicSocialgroups YBasicSocialgroups) {
		this.YBasicSocialgroups = YBasicSocialgroups;
	}

	@Column(name = "FdeviceOs", length = 50)
	public String getFdeviceOs() {
		return this.fdeviceOs;
	}

	public void setFdeviceOs(String fdeviceOs) {
		this.fdeviceOs = fdeviceOs;
	}

	@Column(name = "FdeviceOsversion", length = 50)
	public String getFdeviceOsversion() {
		return this.fdeviceOsversion;
	}

	public void setFdeviceOsversion(String fdeviceOsversion) {
		this.fdeviceOsversion = fdeviceOsversion;
	}

	@Column(name = "FdeviceType", length = 50)
	public String getFdeviceType() {
		return this.fdeviceType;
	}

	public void setFdeviceType(String fdeviceType) {
		this.fdeviceType = fdeviceType;
	}

	@Column(name = "FappVersion", length = 50)
	public String getFappVersion() {
		return this.fappVersion;
	}

	public void setFappVersion(String fappVersion) {
		this.fappVersion = fappVersion;
	}

	@Column(name = "FappChannelNo", length = 50)
	public String getFappChannelNo() {
		return this.fappChannelNo;
	}

	public void setFappChannelNo(String fappChannelNo) {
		this.fappChannelNo = fappChannelNo;
	}

	@Column(name = "FUserName", length = 100)
	public String getFuserName() {
		return this.fuserName;
	}

	public void setFuserName(String fuserName) {
		this.fuserName = fuserName;
	}

	@Column(name = "Fclient", length = 50)
	public String getFclient() {
		return this.fclient;
	}

	public void setFclient(String fclient) {
		this.fclient = fclient;
	}

	@Column(name = "FsessionToken", length = 100)
	public String getFsessionToken() {
		return this.fsessionToken;
	}

	public void setFsessionToken(String fsessionToken) {
		this.fsessionToken = fsessionToken;
	}

	@Column(name = "Fsign", length = 100)
	public String getFsign() {
		return this.fsign;
	}

	public void setFsign(String fsign) {
		this.fsign = fsign;
	}

	@Column(name = "Fmd5Info", length = 100)
	public String getFmd5info() {
		return this.fmd5info;
	}

	public void setFmd5info(String fmd5info) {
		this.fmd5info = fmd5info;
	}

	@Column(name = "Fmid", length = 100)
	public String getFmid() {
		return this.fmid;
	}

	public void setFmid(String fmid) {
		this.fmid = fmid;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}