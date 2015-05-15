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
 * YBasicSocialgroupsactivity entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_socialgroupsactivity" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicSocialgroupsactivity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private YBasicMember YBasicMember;
	private YBasicType YBasicType;
	private YBasicSocialgroups YBasicSocialgroups;
	private String fnumber;
	private String fheadline;
	private String fmessage;
	private String fimages;
	private String fsponsor;
	private String fsite;
	private Date fstartTime;
	private Date ffinishTime;
	private double fcost;
	private Integer fpeopleNum;
	private String fsource;
	private Integer fbillState;
	private String fcomment;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YBasicSocialgroupsactivity() {
	}

	/** minimal constructor */
	public YBasicSocialgroupsactivity(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicSocialgroupsactivity(String fid, YBasicMember YBasicMember,
			YBasicType YBasicType, YBasicSocialgroups YBasicSocialgroups,
			String fnumber, String fheadline, String fmessage, String fimages,
			String fsponsor, String fsite, Date fstartTime, Date ffinishTime,
			double fcost, Integer fpeopleNum, String fsource,
			Integer fbillState, String fcomment, Integer flag) {
		this.fid = fid;
		this.YBasicMember = YBasicMember;
		this.YBasicType = YBasicType;
		this.YBasicSocialgroups = YBasicSocialgroups;
		this.fnumber = fnumber;
		this.fheadline = fheadline;
		this.fmessage = fmessage;
		this.fimages = fimages;
		this.fsponsor = fsponsor;
		this.fsite = fsite;
		this.fstartTime = fstartTime;
		this.ffinishTime = ffinishTime;
		this.fcost = fcost;
		this.fpeopleNum = fpeopleNum;
		this.fsource = fsource;
		this.fbillState = fbillState;
		this.fcomment = fcomment;
		this.flag = flag;
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
	@JoinColumn(name = "FPublisherID")
	public YBasicMember getYBasicMember() {
		return this.YBasicMember;
	}

	public void setYBasicMember(YBasicMember YBasicMember) {
		this.YBasicMember = YBasicMember;
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

	@Column(name = "FMessage", length = 65535)
	public String getFmessage() {
		return this.fmessage;
	}

	public void setFmessage(String fmessage) {
		this.fmessage = fmessage;
	}

	@Column(name = "FImages", length = 250)
	public String getFimages() {
		return this.fimages;
	}

	public void setFimages(String fimages) {
		this.fimages = fimages;
	}

	@Column(name = "FSponsor", length = 100)
	public String getFsponsor() {
		return this.fsponsor;
	}

	public void setFsponsor(String fsponsor) {
		this.fsponsor = fsponsor;
	}

	@Column(name = "FSite", length = 50)
	public String getFsite() {
		return this.fsite;
	}

	public void setFsite(String fsite) {
		this.fsite = fsite;
	}

	@Column(name = "FStartTime", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFstartTime() {
		return this.fstartTime;
	}

	public void setFstartTime(Date fstartTime) {
		this.fstartTime = fstartTime;
	}

	@Column(name = "FFinishTime", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFfinishTime() {
		return this.ffinishTime;
	}

	public void setFfinishTime(Date ffinishTime) {
		this.ffinishTime = ffinishTime;
	}

	@Column(name = "FCost", precision = 22, scale = 0)
	public double getFcost() {
		return this.fcost;
	}

	public void setFcost(double fcost) {
		this.fcost = fcost;
	}

	@Column(name = "FPeopleNum")
	public Integer getFpeopleNum() {
		return this.fpeopleNum;
	}

	public void setFpeopleNum(Integer fpeopleNum) {
		this.fpeopleNum = fpeopleNum;
	}

	@Column(name = "FSource", length = 100)
	public String getFsource() {
		return this.fsource;
	}

	public void setFsource(String fsource) {
		this.fsource = fsource;
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

}