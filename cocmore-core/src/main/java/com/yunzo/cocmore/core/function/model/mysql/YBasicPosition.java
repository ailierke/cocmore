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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * YBasicPosition entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_position" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicPosition implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private String fnumber;
	private String fname;
	private String forganizationId;
	private String fcreaterId;
	private String fmodifiedId;
	private String flastModified;
	private Date pcreateTime;
	private Date fmodifiedTime;
	private Date flastModifiedTime;
	private Integer fhide;
	private Integer fbillState;
	private Integer flag;
	private String fcomment;
	private String fsocialGroupsId;
	private String fsuperPositonId;
	private Integer fleafNode;
	private Integer fseq;
	private Integer version;
	private Set<YBasicentriesEmployeedistribution> YBasicentriesEmployeedistributions = new HashSet<YBasicentriesEmployeedistribution>(
			0);
	private Set<YBasicMemberbypositionhide> YBasicMemberbypositionhides = new HashSet<YBasicMemberbypositionhide>(
			0);
	private Set<YBasicMemberbypositionblacklist> YBasicMemberbypositionblacklists = new HashSet<YBasicMemberbypositionblacklist>(
			0);
	private Set<YBasicentriesMemberdistribution> YBasicentriesMemberdistributions = new HashSet<YBasicentriesMemberdistribution>(
			0);
	private Set<YBasicContactsexhibition> YBasicContactsexhibitions = new HashSet<YBasicContactsexhibition>(
			0);

	// Constructors

	/** default constructor */
	public YBasicPosition() {
	}

	/** minimal constructor */
	public YBasicPosition(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicPosition(
			String fid,
			String fnumber,
			String fname,
			String forganizationId,
			String fcreaterId,
			String fmodifiedId,
			String flastModified,
			Date pcreateTime,
			Date fmodifiedTime,
			Date flastModifiedTime,
			Integer fhide,
			Integer fbillState,
			Integer flag,
			String fcomment,
			String fsocialGroupsId,
			String fsuperPositonId,
			Integer fleafNode,
			Integer fseq,
			Set<YBasicentriesEmployeedistribution> YBasicentriesEmployeedistributions,
			Set<YBasicMemberbypositionhide> YBasicMemberbypositionhides,
			Set<YBasicMemberbypositionblacklist> YBasicMemberbypositionblacklists,
			Set<YBasicentriesMemberdistribution> YBasicentriesMemberdistributions,
			Set<YBasicContactsexhibition> YBasicContactsexhibitions) {
		this.fid = fid;
		this.fnumber = fnumber;
		this.fname = fname;
		this.forganizationId = forganizationId;
		this.fcreaterId = fcreaterId;
		this.fmodifiedId = fmodifiedId;
		this.flastModified = flastModified;
		this.pcreateTime = pcreateTime;
		this.fmodifiedTime = fmodifiedTime;
		this.flastModifiedTime = flastModifiedTime;
		this.fhide = fhide;
		this.fbillState = fbillState;
		this.flag = flag;
		this.fcomment = fcomment;
		this.fsocialGroupsId = fsocialGroupsId;
		this.fsuperPositonId = fsuperPositonId;
		this.fleafNode = fleafNode;
		this.fseq = fseq;
		this.YBasicentriesEmployeedistributions = YBasicentriesEmployeedistributions;
		this.YBasicMemberbypositionhides = YBasicMemberbypositionhides;
		this.YBasicMemberbypositionblacklists = YBasicMemberbypositionblacklists;
		this.YBasicentriesMemberdistributions = YBasicentriesMemberdistributions;
		this.YBasicContactsexhibitions = YBasicContactsexhibitions;
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

	@Column(name = "FName", length = 36)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "FOrganizationID", length = 36)
	public String getForganizationId() {
		return this.forganizationId;
	}

	public void setForganizationId(String forganizationId) {
		this.forganizationId = forganizationId;
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

	@Column(name = "FLastModified", length = 36)
	public String getFlastModified() {
		return this.flastModified;
	}

	public void setFlastModified(String flastModified) {
		this.flastModified = flastModified;
	}

	@Column(name = "PCreateTime", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getPcreateTime() {
		return this.pcreateTime;
	}

	public void setPcreateTime(Date pcreateTime) {
		this.pcreateTime = pcreateTime;
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

	@Column(name = "Fhide")
	public Integer getFhide() {
		return this.fhide;
	}

	public void setFhide(Integer fhide) {
		this.fhide = fhide;
	}

	@Column(name = "FBillState")
	public Integer getFbillState() {
		return this.fbillState;
	}

	public void setFbillState(Integer fbillState) {
		this.fbillState = fbillState;
	}

	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@Column(name = "FComment")
	public String getFcomment() {
		return this.fcomment;
	}

	public void setFcomment(String fcomment) {
		this.fcomment = fcomment;
	}

	@Column(name = "FSocialGroupsID", length = 36)
	public String getFsocialGroupsId() {
		return this.fsocialGroupsId;
	}

	public void setFsocialGroupsId(String fsocialGroupsId) {
		this.fsocialGroupsId = fsocialGroupsId;
	}

	@Column(name = "FSuperPositonID", length = 36)
	public String getFsuperPositonId() {
		return this.fsuperPositonId;
	}

	public void setFsuperPositonId(String fsuperPositonId) {
		this.fsuperPositonId = fsuperPositonId;
	}

	@Column(name = "FLeafNode")
	public Integer getFleafNode() {
		return this.fleafNode;
	}

	public void setFleafNode(Integer fleafNode) {
		this.fleafNode = fleafNode;
	}

	@Column(name = "FSeq")
	public Integer getFseq() {
		return this.fseq;
	}

	public void setFseq(Integer fseq) {
		this.fseq = fseq;
	}


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicPosition")
	@JsonIgnore
	public Set<YBasicentriesEmployeedistribution> getYBasicentriesEmployeedistributions() {
		return this.YBasicentriesEmployeedistributions;
	}

	public void setYBasicentriesEmployeedistributions(
			Set<YBasicentriesEmployeedistribution> YBasicentriesEmployeedistributions) {
		this.YBasicentriesEmployeedistributions = YBasicentriesEmployeedistributions;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicPosition")
	@JsonIgnore
	public Set<YBasicMemberbypositionhide> getYBasicMemberbypositionhides() {
		return this.YBasicMemberbypositionhides;
	}

	public void setYBasicMemberbypositionhides(
			Set<YBasicMemberbypositionhide> YBasicMemberbypositionhides) {
		this.YBasicMemberbypositionhides = YBasicMemberbypositionhides;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicPosition")
	@JsonIgnore
	public Set<YBasicMemberbypositionblacklist> getYBasicMemberbypositionblacklists() {
		return this.YBasicMemberbypositionblacklists;
	}

	public void setYBasicMemberbypositionblacklists(
			Set<YBasicMemberbypositionblacklist> YBasicMemberbypositionblacklists) {
		this.YBasicMemberbypositionblacklists = YBasicMemberbypositionblacklists;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicPosition")
	@JsonIgnore
	public Set<YBasicentriesMemberdistribution> getYBasicentriesMemberdistributions() {
		return this.YBasicentriesMemberdistributions;
	}

	public void setYBasicentriesMemberdistributions(
			Set<YBasicentriesMemberdistribution> YBasicentriesMemberdistributions) {
		this.YBasicentriesMemberdistributions = YBasicentriesMemberdistributions;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicPosition")
	@JsonIgnore
	public Set<YBasicContactsexhibition> getYBasicContactsexhibitions() {
		return this.YBasicContactsexhibitions;
	}

	public void setYBasicContactsexhibitions(
			Set<YBasicContactsexhibition> YBasicContactsexhibitions) {
		this.YBasicContactsexhibitions = YBasicContactsexhibitions;
	}

	@Column(name = "FVersion")
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	

}