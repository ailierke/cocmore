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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * YBasicEmployee entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_employee" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicEmployee implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private YSystemUsers YSystemUsers;
	private String fnumber;
	private String fname;
	private Integer fsex;
	private Date fbirthday;
	private String femail;
	private String fpreviousEmail;
	private String fhomePhone;
	private String fofficePhone;
	private String fmobilePhone;
	private String fsecondPhone;
	private String fsite;
	private String fcreaterId;
	private String fmodifiedId;
	private String flastModifiedId;
	private Date fcreateTime;
	private Date fmodifiedTime;
	private Date flastModifiedTime;
	private Integer fbillState;
	private String fcomment;
	private String forganizationId;
	private Integer flag;
	private String jsonStr;
	private Set<YBasicSocialgroups> YBasicSocialgroupses = new HashSet<YBasicSocialgroups>(
			0);
	private Set<YBasicentriesEmployeedistribution> YBasicentriesEmployeedistributions = new HashSet<YBasicentriesEmployeedistribution>(
			0);

	// Constructors

	/** default constructor */
	public YBasicEmployee() {
	}

	/** minimal constructor */
	public YBasicEmployee(String fid, String forganizationId) {
		this.fid = fid;
		this.forganizationId = forganizationId;
	}

	/** full constructor */
	public YBasicEmployee(
			String fid,
			YSystemUsers YSystemUsers,
			String fnumber,
			String fname,
			Integer fsex,
			Date fbirthday,
			String femail,
			String fpreviousEmail,
			String fhomePhone,
			String fofficePhone,
			String fmobilePhone,
			String fsecondPhone,
			String fsite,
			String fcreaterId,
			String fmodifiedId,
			String flastModifiedId,
			Date fcreateTime,
			Date fmodifiedTime,
			Date flastModifiedTime,
			Integer fbillState,
			String fcomment,
			String forganizationId,
			Integer flag,
			Set<YBasicSocialgroups> YBasicSocialgroupses,
			Set<YBasicentriesEmployeedistribution> YBasicentriesEmployeedistributions) {
		this.fid = fid;
		this.YSystemUsers = YSystemUsers;
		this.fnumber = fnumber;
		this.fname = fname;
		this.fsex = fsex;
		this.fbirthday = fbirthday;
		this.femail = femail;
		this.fpreviousEmail = fpreviousEmail;
		this.fhomePhone = fhomePhone;
		this.fofficePhone = fofficePhone;
		this.fmobilePhone = fmobilePhone;
		this.fsecondPhone = fsecondPhone;
		this.fsite = fsite;
		this.fcreaterId = fcreaterId;
		this.fmodifiedId = fmodifiedId;
		this.flastModifiedId = flastModifiedId;
		this.fcreateTime = fcreateTime;
		this.fmodifiedTime = fmodifiedTime;
		this.flastModifiedTime = flastModifiedTime;
		this.fbillState = fbillState;
		this.fcomment = fcomment;
		this.forganizationId = forganizationId;
		this.flag = flag;
		this.YBasicSocialgroupses = YBasicSocialgroupses;
		this.YBasicentriesEmployeedistributions = YBasicentriesEmployeedistributions;
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
	@JoinColumn(name = "FAdminID")
	public YSystemUsers getYSystemUsers() {
		return this.YSystemUsers;
	}

	public void setYSystemUsers(YSystemUsers YSystemUsers) {
		this.YSystemUsers = YSystemUsers;
	}

	@Column(name = "FNumber", length = 100)
	public String getFnumber() {
		return this.fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	@Column(name = "FName", length = 48)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "FSex")
	public Integer getFsex() {
		return this.fsex;
	}

	public void setFsex(Integer fsex) {
		this.fsex = fsex;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FBirthday", length = 10)
	public Date getFbirthday() {
		return this.fbirthday;
	}

	public void setFbirthday(Date fbirthday) {
		this.fbirthday = fbirthday;
	}

	@Column(name = "FEmail", length = 48)
	public String getFemail() {
		return this.femail;
	}

	public void setFemail(String femail) {
		this.femail = femail;
	}

	@Column(name = "FPreviousEmail", length = 48)
	public String getFpreviousEmail() {
		return this.fpreviousEmail;
	}

	public void setFpreviousEmail(String fpreviousEmail) {
		this.fpreviousEmail = fpreviousEmail;
	}

	@Column(name = "FHomePhone", length = 24)
	public String getFhomePhone() {
		return this.fhomePhone;
	}

	public void setFhomePhone(String fhomePhone) {
		this.fhomePhone = fhomePhone;
	}

	@Column(name = "FOfficePhone", length = 24)
	public String getFofficePhone() {
		return this.fofficePhone;
	}

	public void setFofficePhone(String fofficePhone) {
		this.fofficePhone = fofficePhone;
	}

	@Column(name = "FMobilePhone", length = 24)
	public String getFmobilePhone() {
		return this.fmobilePhone;
	}

	public void setFmobilePhone(String fmobilePhone) {
		this.fmobilePhone = fmobilePhone;
	}

	@Column(name = "FSecondPhone", length = 24)
	public String getFsecondPhone() {
		return this.fsecondPhone;
	}

	public void setFsecondPhone(String fsecondPhone) {
		this.fsecondPhone = fsecondPhone;
	}

	@Column(name = "FSite")
	public String getFsite() {
		return this.fsite;
	}

	public void setFsite(String fsite) {
		this.fsite = fsite;
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
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "FModifiedTime", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFmodifiedTime() {
		return this.fmodifiedTime;
	}

	public void setFmodifiedTime(Date fmodifiedTime) {
		this.fmodifiedTime = fmodifiedTime;
	}

	@Column(name = "FLastModifiedTime", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFlastModifiedTime() {
		return this.flastModifiedTime;
	}

	public void setFlastModifiedTime(Date flastModifiedTime) {
		this.flastModifiedTime = flastModifiedTime;
	}

	@Column(name = "FBillState")
	public Integer getFbillState() {
		return this.fbillState;
	}

	public void setFbillState(Integer fbillState) {
		this.fbillState = fbillState;
	}

	@Column(name = "FComment")
	public String getFcomment() {
		return this.fcomment;
	}

	public void setFcomment(String fcomment) {
		this.fcomment = fcomment;
	}

	@Column(name = "FOrganizationID", nullable = false, length = 36)
	public String getForganizationId() {
		return this.forganizationId;
	}

	public void setForganizationId(String forganizationId) {
		this.forganizationId = forganizationId;
	}

	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicEmployee")
	@JsonIgnore
	public Set<YBasicSocialgroups> getYBasicSocialgroupses() {
		return this.YBasicSocialgroupses;
	}

	public void setYBasicSocialgroupses(
			Set<YBasicSocialgroups> YBasicSocialgroupses) {
		this.YBasicSocialgroupses = YBasicSocialgroupses;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicEmployee")
	@JsonIgnore
	public Set<YBasicentriesEmployeedistribution> getYBasicentriesEmployeedistributions() {
		return this.YBasicentriesEmployeedistributions;
	}

	public void setYBasicentriesEmployeedistributions(
			Set<YBasicentriesEmployeedistribution> YBasicentriesEmployeedistributions) {
		this.YBasicentriesEmployeedistributions = YBasicentriesEmployeedistributions;
	}

	@Column(name = "jsonStr")
	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}
	
	

}