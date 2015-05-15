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

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * YBasicSocialgroupsdynamic entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_socialgroupsdynamic" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicSocialgroupsdynamic implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	public YBasicSocialgroupsdynamic(String fid, String fnumber,
			String fheadline, String flogoImage, String fmessage,
			Integer fbillState, String fcomment, Integer flag,
			String fdetailAddress, Date fpublishTime) {
		super();
		this.fid = fid;
		this.fnumber = fnumber;
		this.fheadline = fheadline;
		this.flogoImage = flogoImage;
		this.fmessage = fmessage;
		this.fbillState = fbillState;
		this.fcomment = fcomment;
		this.flag = flag;
		this.fdetailAddress = fdetailAddress;
		this.fpublishTime = fpublishTime;
	}

	private YBasicType YBasicType;
	private YBasicSocialgroups YBasicSocialgroups;
	private String fnumber;
	private String fheadline;
	private String flogoImage;
	private String fmessage;
	private Integer fbillState;
	private String fcomment;
	private Integer flag;
	private String fdetailAddress;
	private Date fpublishTime;
	
	@Column(name = "FPublishTime", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFpublishTime() {
		return fpublishTime;
	}

	public void setFpublishTime(Date fpublishTime) {
		this.fpublishTime = fpublishTime;
	}

	private Set<YBasicMemberbymemberblacklist> YBasicMemberbymemberblacklists = new HashSet<YBasicMemberbymemberblacklist>(
			0);
	private Set<YBasicMemberbypositionblacklist> YBasicMemberbypositionblacklists = new HashSet<YBasicMemberbypositionblacklist>(
			0);

	// Constructors

	/** default constructor */
	public YBasicSocialgroupsdynamic() {
	}

	/** minimal constructor */
	public YBasicSocialgroupsdynamic(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicSocialgroupsdynamic(
			String fid,
			YBasicType YBasicType,
			YBasicSocialgroups YBasicSocialgroups,
			String fnumber,
			String fheadline,
			String flogoImage,
			String fmessage,
			Integer fbillState,
			String fcomment,
			Integer flag,
			String fdetailAddress,
			Date fpublishTime,
			Set<YBasicMemberbymemberblacklist> YBasicMemberbymemberblacklists,
			Set<YBasicMemberbypositionblacklist> YBasicMemberbypositionblacklists) {
		this.fid = fid;
		this.YBasicType = YBasicType;
		this.YBasicSocialgroups = YBasicSocialgroups;
		this.fnumber = fnumber;
		this.fheadline = fheadline;
		this.flogoImage = flogoImage;
		this.fmessage = fmessage;
		this.fbillState = fbillState;
		this.fcomment = fcomment;
		this.flag = flag;
		this.fdetailAddress = fdetailAddress;
		this.fpublishTime = fpublishTime;
		this.YBasicMemberbymemberblacklists = YBasicMemberbymemberblacklists;
		this.YBasicMemberbypositionblacklists = YBasicMemberbypositionblacklists;
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
	@JoinColumn(name = "FTypeID")
	public YBasicType getYBasicType() {
		return this.YBasicType;
	}

	public void setYBasicType(YBasicType YBasicType) {
		this.YBasicType = YBasicType;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FSocialGroupsID")
	public YBasicSocialgroups getYBasicSocialgroups() {
		return this.YBasicSocialgroups;
	}

	public void setYBasicSocialgroups(YBasicSocialgroups YBasicSocialgroups) {
		this.YBasicSocialgroups = YBasicSocialgroups;
	}

	@Column(name = "FNumber", length = 100)
	public String getFnumber() {
		return this.fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	@Column(name = "FHeadline", length = 100)
	public String getFheadline() {
		return this.fheadline;
	}

	public void setFheadline(String fheadline) {
		this.fheadline = fheadline;
	}

	@Column(name = "FLogoImage", length = 250)
	public String getFlogoImage() {
		return this.flogoImage;
	}

	public void setFlogoImage(String flogoImage) {
		this.flogoImage = flogoImage;
	}

	@Column(name = "FMessage", length = 65535)
	public String getFmessage() {
		return this.fmessage;
	}

	public void setFmessage(String fmessage) {
		this.fmessage = fmessage;
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

	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@Column(name = "FDetailAddress", length = 200)
	public String getFdetailAddress() {
		return this.fdetailAddress;
	}

	public void setFdetailAddress(String fdetailAddress) {
		this.fdetailAddress = fdetailAddress;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicSocialgroupsdynamic")
	@JsonIgnore
	public Set<YBasicMemberbymemberblacklist> getYBasicMemberbymemberblacklists() {
		return this.YBasicMemberbymemberblacklists;
	}

	public void setYBasicMemberbymemberblacklists(
			Set<YBasicMemberbymemberblacklist> YBasicMemberbymemberblacklists) {
		this.YBasicMemberbymemberblacklists = YBasicMemberbymemberblacklists;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicSocialgroupsdynamic")
	@JsonIgnore
	public Set<YBasicMemberbypositionblacklist> getYBasicMemberbypositionblacklists() {
		return this.YBasicMemberbypositionblacklists;
	}

	public void setYBasicMemberbypositionblacklists(
			Set<YBasicMemberbypositionblacklist> YBasicMemberbypositionblacklists) {
		this.YBasicMemberbypositionblacklists = YBasicMemberbypositionblacklists;
	}

}