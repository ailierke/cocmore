package com.yunzo.cocmore.core.function.model.mysql;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * YSystemconfigurationMessage entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_systemconfiguration_message" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YSystemconfigurationMessage implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private String fnumber;
	private String fmessageType;
	private String fcontent;
	private Integer fbillState;
	private String fcreaterId;
	private String fmodifiedId;
	private String flastModifiedId;
	private Date fcreateTime;
	private Date fmodifiedTime;
	private Date flastModifiedTime;
	private Integer flag;
	private Set<YSystemconfigurationMessagerecord> YSystemconfigurationMessagerecords = new HashSet<YSystemconfigurationMessagerecord>(
			0);

	// Constructors

	/** default constructor */
	public YSystemconfigurationMessage() {
	}

	/** minimal constructor */
	public YSystemconfigurationMessage(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YSystemconfigurationMessage(
			String fid,
			String fnumber,
			String fmessageType,
			String fcontent,
			Integer fbillState,
			String fcreaterId,
			String fmodifiedId,
			String flastModifiedId,
			Date fcreateTime,
			Date fmodifiedTime,
			Date flastModifiedTime,
			Integer flag,
			Set<YSystemconfigurationMessagerecord> YSystemconfigurationMessagerecords) {
		this.fid = fid;
		this.fnumber = fnumber;
		this.fmessageType = fmessageType;
		this.fcontent = fcontent;
		this.fbillState = fbillState;
		this.fcreaterId = fcreaterId;
		this.fmodifiedId = fmodifiedId;
		this.flastModifiedId = flastModifiedId;
		this.fcreateTime = fcreateTime;
		this.fmodifiedTime = fmodifiedTime;
		this.flastModifiedTime = flastModifiedTime;
		this.flag = flag;
		this.YSystemconfigurationMessagerecords = YSystemconfigurationMessagerecords;
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

	@Column(name = "FNumber", length = 36)
	public String getFnumber() {
		return this.fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	@Column(name = "FMessageType")
	public String getFmessageType() {
		return this.fmessageType;
	}

	public void setFmessageType(String fmessageType) {
		this.fmessageType = fmessageType;
	}

	@Column(name = "FContent", length = 300)
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

	@Column(name = "FCreaterID", length = 36)
	public String getFcreaterId() {
		return this.fcreaterId;
	}

	public void setFcreaterId(String fcreaterId) {
		this.fcreaterId = fcreaterId;
	}

	@Column(name = "FModifiedID", length = 36)
	public String getFmodifiedId() {
		return this.fmodifiedId;
	}

	public void setFmodifiedId(String fmodifiedId) {
		this.fmodifiedId = fmodifiedId;
	}

	@Column(name = "FLastModifiedID", length = 36)
	public String getFlastModifiedId() {
		return this.flastModifiedId;
	}

	public void setFlastModifiedId(String flastModifiedId) {
		this.flastModifiedId = flastModifiedId;
	}

	@Column(name = "FCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "FModifiedTime", length = 19)
	public Date getFmodifiedTime() {
		return this.fmodifiedTime;
	}

	public void setFmodifiedTime(Date fmodifiedTime) {
		this.fmodifiedTime = fmodifiedTime;
	}

	@Column(name = "FLastModifiedTime", length = 19)
	public Date getFlastModifiedTime() {
		return this.flastModifiedTime;
	}

	public void setFlastModifiedTime(Date flastModifiedTime) {
		this.flastModifiedTime = flastModifiedTime;
	}

	@Column(name = "Flag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YSystemconfigurationMessage")
	@JsonIgnore
	public Set<YSystemconfigurationMessagerecord> getYSystemconfigurationMessagerecords() {
		return this.YSystemconfigurationMessagerecords;
	}

	public void setYSystemconfigurationMessagerecords(
			Set<YSystemconfigurationMessagerecord> YSystemconfigurationMessagerecords) {
		this.YSystemconfigurationMessagerecords = YSystemconfigurationMessagerecords;
	}

}