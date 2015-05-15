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
 * YBasicSocialgroups entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_socialgroups" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicSocialgroups implements java.io.Serializable {

	@Override
	public String toString() {
		return "YBasicSocialgroups [fid=" + fid + ", YBasicDistrict="
				+ YBasicDistrict + ", YBasicOrganization=" + YBasicOrganization
				+ ", YBasicEmployee=" + YBasicEmployee + ", YBasicCity="
				+ YBasicCity + ", YBasicCounty=" + YBasicCounty
				+ ", YBasicType=" + YBasicType + ", YBasicProvince="
				+ YBasicProvince + ", fnumber=" + fnumber + ", fname=" + fname
				+ ", fabbreviation=" + fabbreviation + ", fnumberPeople="
				+ fnumberPeople + ", fsuperSocialGroupsId="
				+ fsuperSocialGroupsId + ", fleafNode=" + fleafNode
				+ ", fpreviousNumber=" + fpreviousNumber + ", fsource="
				+ fsource + ", fynsigned=" + fynsigned + ", fsignedTime="
				+ fsignedTime + ", fregisterNum=" + fregisterNum + ", flevel="
				+ flevel + ", fclientContacts=" + fclientContacts
				+ ", faddress=" + faddress + ", fphone=" + fphone + ", femail="
				+ femail + ", fiphone=" + fiphone + ", fbillState="
				+ fbillState + ", fcomment=" + fcomment + ", fcreaterId="
				+ fcreaterId + ", fmodifiedId=" + fmodifiedId
				+ ", flastModifiedId=" + flastModifiedId + ", fcreateTime="
				+ fcreateTime + ", fmodifiedTime=" + fmodifiedTime
				+ ", flastModifiedTime=" + flastModifiedTime + ", flag=" + flag
				+ ", logo=" + logo + ", allowadd=" + allowadd
				+ ", localCacheVersion="
				+ localCacheVersion
				+ ", YBasicSocialgroupsguestbooks="
				+ YBasicSocialgroupsguestbooks
				+ ", YBasicSocialgroupsdynamics=" + YBasicSocialgroupsdynamics
				+ ", YBasicSocialgroupsabouts=" + YBasicSocialgroupsabouts
				+ ", YBasicMembercompanies=" + YBasicMembercompanies
				+ ", YBasicSocialgroupscontacts=" + YBasicSocialgroupscontacts
				+ ", YBasicSocialgroupsinforms=" + YBasicSocialgroupsinforms
				+ ", YAppdevices=" + YAppdevices + ", YBasicMembers="
				+ YBasicMembers + ", YBasicSocialgroupsdemands="
				+ YBasicSocialgroupsdemands + ", YBasicAssurancecontents="
				+ YBasicAssurancecontents + ", YBasicSocialgroupsactivities="
				+ YBasicSocialgroupsactivities + ", YWallactivities="
				+ YWallactivities + ", YBasicSocialgroupssupplies="
				+ YBasicSocialgroupssupplies + "]";
	}

	public static enum AllowAdd{
		//ALLOW  允许加入   UNALLOW  不允许加入
		ALLOW,UNALLOW
	}
	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private YBasicDistrict YBasicDistrict;
	private YBasicOrganization YBasicOrganization;
	private YBasicEmployee YBasicEmployee;
	private YBasicCity YBasicCity;
	private YBasicCounty YBasicCounty;
	private YBasicType YBasicType;
	private YBasicProvince YBasicProvince;
	private String fnumber;
	private String fname;
	private String fabbreviation;
	private Integer fnumberPeople;
	private String fsuperSocialGroupsId;
	private String fleafNode;
	private String fpreviousNumber;
	private String fsource;
	private String fynsigned;
	private Date fsignedTime;
	private Integer fregisterNum;
	private String flevel;
	private String fclientContacts;
	private String faddress;
	private String fphone;
	private String femail;
	private String fiphone;
	private Integer fbillState;
	private String fcomment;
	private String fcreaterId;
	private String fmodifiedId;
	private String flastModifiedId;
	private Date fcreateTime;
	private Date fmodifiedTime;
	private Date flastModifiedTime;
	private Integer flag;
	private String logo;
	private String allowadd;
	private  String localCacheVersion;
	public String getLocalCacheVersion() {
		return localCacheVersion;
	}

	public void setLocalCacheVersion(String localCacheVersion) {
		this.localCacheVersion = localCacheVersion;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getAllowadd() {
		return allowadd;
	}

	public void setAllowadd(String allowadd) {
		this.allowadd = allowadd;
	}

	private Set<YBasicSocialgroupsguestbook> YBasicSocialgroupsguestbooks = new HashSet<YBasicSocialgroupsguestbook>(
			0);
	private Set<YBasicSocialgroupsdynamic> YBasicSocialgroupsdynamics = new HashSet<YBasicSocialgroupsdynamic>(
			0);
	private Set<YBasicSocialgroupsabout> YBasicSocialgroupsabouts = new HashSet<YBasicSocialgroupsabout>(
			0);
	private Set<YBasicMembercompany> YBasicMembercompanies = new HashSet<YBasicMembercompany>(
			0);
	private Set<YBasicSocialgroupscontact> YBasicSocialgroupscontacts = new HashSet<YBasicSocialgroupscontact>(
			0);
	private Set<YBasicSocialgroupsinform> YBasicSocialgroupsinforms = new HashSet<YBasicSocialgroupsinform>(
			0);
	private Set<YAppdevice> YAppdevices = new HashSet<YAppdevice>(0);
	
	private Set<YBasicMember> YBasicMembers = new HashSet<YBasicMember>(
			0);
	private Set<YBasicSocialgroupsdemand> YBasicSocialgroupsdemands = new HashSet<YBasicSocialgroupsdemand>(
			0);
	private Set<YBasicAssurancecontent> YBasicAssurancecontents = new HashSet<YBasicAssurancecontent>(
			0);
	private Set<YBasicSocialgroupsactivity> YBasicSocialgroupsactivities = new HashSet<YBasicSocialgroupsactivity>(
			0);
	private Set<YWallactivity> YWallactivities = new HashSet<YWallactivity>(0);
	private Set<YBasicSocialgroupssupply> YBasicSocialgroupssupplies = new HashSet<YBasicSocialgroupssupply>(
			0);
	// Constructors

	/** default constructor */
	public YBasicSocialgroups() {
	}

	/** minimal constructor */
	public YBasicSocialgroups(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicSocialgroups(String fid, YBasicDistrict YBasicDistrict,
			YBasicOrganization YBasicOrganization,
			YBasicEmployee YBasicEmployee, YBasicCity YBasicCity,
			YBasicCounty YBasicCounty, YBasicType YBasicType,
			YBasicProvince YBasicProvince, String fnumber, String fname,
			String fabbreviation, Integer fnumberPeople,
			String fsuperSocialGroupsId, String fleafNode,
			String fpreviousNumber, String fsource, String fynsigned,
			Date fsignedTime, Integer fregisterNum, String flevel,
			String fclientContacts, String faddress, String fphone,
			String femail, String fiphone, Integer fbillState, String fcomment,
			String fcreaterId, String fmodifiedId, String flastModifiedId,
			Date fcreateTime, Date fmodifiedTime, Date flastModifiedTime,
			Integer flag, String logo,String allowadd, String localCacheVersion,
			Set<YBasicSocialgroupsguestbook> YBasicSocialgroupsguestbooks,
			Set<YBasicSocialgroupsdynamic> YBasicSocialgroupsdynamics,
			Set<YBasicSocialgroupsabout> YBasicSocialgroupsabouts,
			Set<YBasicMembercompany> YBasicMembercompanies,
			Set<YBasicSocialgroupscontact> YBasicSocialgroupscontacts,
			Set<YBasicSocialgroupsinform> YBasicSocialgroupsinforms,
			Set<YAppdevice> YAppdevices,
			Set<YBasicMember> YBasicMembers,
			Set<YBasicSocialgroupsdemand> YBasicSocialgroupsdemands,
			Set<YBasicAssurancecontent> YBasicAssurancecontents,
			Set<YBasicSocialgroupsactivity> YBasicSocialgroupsactivities,
			Set<YWallactivity> YWallactivities,
			Set<YBasicSocialgroupssupply> YBasicSocialgroupssupplies) {
		this.fid = fid;
		this.YBasicDistrict = YBasicDistrict;
		this.YBasicOrganization = YBasicOrganization;
		this.YBasicEmployee = YBasicEmployee;
		this.YBasicCity = YBasicCity;
		this.YBasicCounty = YBasicCounty;
		this.YBasicType = YBasicType;
		this.YBasicProvince = YBasicProvince;
		this.fnumber = fnumber;
		this.fname = fname;
		this.fabbreviation = fabbreviation;
		this.fnumberPeople = fnumberPeople;
		this.fsuperSocialGroupsId = fsuperSocialGroupsId;
		this.fleafNode = fleafNode;
		this.fpreviousNumber = fpreviousNumber;
		this.fsource = fsource;
		this.fynsigned = fynsigned;
		this.fsignedTime = fsignedTime;
		this.fregisterNum = fregisterNum;
		this.flevel = flevel;
		this.fclientContacts = fclientContacts;
		this.faddress = faddress;
		this.fphone = fphone;
		this.femail = femail;
		this.fiphone = fiphone;
		this.fbillState = fbillState;
		this.fcomment = fcomment;
		this.fcreaterId = fcreaterId;
		this.fmodifiedId = fmodifiedId;
		this.flastModifiedId = flastModifiedId;
		this.fcreateTime = fcreateTime;
		this.fmodifiedTime = fmodifiedTime;
		this.flastModifiedTime = flastModifiedTime;
		this.flag = flag;
		this.logo=logo;
		this.allowadd=allowadd;
		this.localCacheVersion=localCacheVersion;
		this.YBasicSocialgroupsguestbooks = YBasicSocialgroupsguestbooks;
		this.YBasicSocialgroupsdynamics = YBasicSocialgroupsdynamics;
		this.YBasicSocialgroupsabouts = YBasicSocialgroupsabouts;
		this.YBasicMembercompanies = YBasicMembercompanies;
		this.YBasicSocialgroupscontacts = YBasicSocialgroupscontacts;
		this.YBasicSocialgroupsinforms = YBasicSocialgroupsinforms;
		this.YAppdevices = YAppdevices;
		this.YBasicMembers = YBasicMembers;
		this.YBasicSocialgroupsdemands = YBasicSocialgroupsdemands;
		this.YBasicAssurancecontents = YBasicAssurancecontents;
		this.YBasicSocialgroupsactivities = YBasicSocialgroupsactivities;
		this.YWallactivities = YWallactivities;
		this.YBasicSocialgroupssupplies = YBasicSocialgroupssupplies;
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
	@JoinColumn(name = "FDistrictID")
	public YBasicDistrict getYBasicDistrict() {
		return this.YBasicDistrict;
	}

	public void setYBasicDistrict(YBasicDistrict YBasicDistrict) {
		this.YBasicDistrict = YBasicDistrict;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FCompaniesID")
	public YBasicOrganization getYBasicOrganization() {
		return this.YBasicOrganization;
	}

	public void setYBasicOrganization(YBasicOrganization YBasicOrganization) {
		this.YBasicOrganization = YBasicOrganization;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FSalesmanID")
	public YBasicEmployee getYBasicEmployee() {
		return this.YBasicEmployee;
	}

	public void setYBasicEmployee(YBasicEmployee YBasicEmployee) {
		this.YBasicEmployee = YBasicEmployee;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FCityID")
	public YBasicCity getYBasicCity() {
		return this.YBasicCity;
	}

	public void setYBasicCity(YBasicCity YBasicCity) {
		this.YBasicCity = YBasicCity;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FCountyID")
	public YBasicCounty getYBasicCounty() {
		return this.YBasicCounty;
	}

	public void setYBasicCounty(YBasicCounty YBasicCounty) {
		this.YBasicCounty = YBasicCounty;
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
	@JoinColumn(name = "FProvinceID")
	public YBasicProvince getYBasicProvince() {
		return this.YBasicProvince;
	}

	public void setYBasicProvince(YBasicProvince YBasicProvince) {
		this.YBasicProvince = YBasicProvince;
	}

	@Column(name = "FNumber", length = 100)
	public String getFnumber() {
		return this.fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	@Column(name = "FName", length = 100)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "FAbbreviation", length = 36)
	public String getFabbreviation() {
		return this.fabbreviation;
	}

	public void setFabbreviation(String fabbreviation) {
		this.fabbreviation = fabbreviation;
	}

	@Column(name = "FNumberPeople")
	public Integer getFnumberPeople() {
		return this.fnumberPeople;
	}

	public void setFnumberPeople(Integer fnumberPeople) {
		this.fnumberPeople = fnumberPeople;
	}

	@Column(name = "FSuperSocialGroupsID", length = 36)
	public String getFsuperSocialGroupsId() {
		return this.fsuperSocialGroupsId;
	}

	public void setFsuperSocialGroupsId(String fsuperSocialGroupsId) {
		this.fsuperSocialGroupsId = fsuperSocialGroupsId;
	}

	@Column(name = "FLeafNode", length = 1)
	public String getFleafNode() {
		return this.fleafNode;
	}

	public void setFleafNode(String fleafNode) {
		this.fleafNode = fleafNode;
	}

	@Column(name = "FPreviousNumber", length = 100)
	public String getFpreviousNumber() {
		return this.fpreviousNumber;
	}

	public void setFpreviousNumber(String fpreviousNumber) {
		this.fpreviousNumber = fpreviousNumber;
	}

	@Column(name = "FSource", length = 100)
	public String getFsource() {
		return this.fsource;
	}

	public void setFsource(String fsource) {
		this.fsource = fsource;
	}

	@Column(name = "FYNSigned", length = 1)
	public String getFynsigned() {
		return this.fynsigned;
	}

	public void setFynsigned(String fynsigned) {
		this.fynsigned = fynsigned;
	}

	@Column(name = "FSignedTime", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFsignedTime() {
		return this.fsignedTime;
	}

	public void setFsignedTime(Date fsignedTime) {
		this.fsignedTime = fsignedTime;
	}

	@Column(name = "FRegisterNum")
	public Integer getFregisterNum() {
		return this.fregisterNum;
	}

	public void setFregisterNum(Integer fregisterNum) {
		this.fregisterNum = fregisterNum;
	}

	@Column(name = "FLevel", length = 36)
	public String getFlevel() {
		return this.flevel;
	}

	public void setFlevel(String flevel) {
		this.flevel = flevel;
	}

	@Column(name = "FClientContacts", length = 48)
	public String getFclientContacts() {
		return this.fclientContacts;
	}

	public void setFclientContacts(String fclientContacts) {
		this.fclientContacts = fclientContacts;
	}

	@Column(name = "FAddress", length = 200)
	public String getFaddress() {
		return this.faddress;
	}

	public void setFaddress(String faddress) {
		this.faddress = faddress;
	}

	@Column(name = "FPhone", length = 50)
	public String getFphone() {
		return this.fphone;
	}

	public void setFphone(String fphone) {
		this.fphone = fphone;
	}

	@Column(name = "FEmail", length = 50)
	public String getFemail() {
		return this.femail;
	}

	public void setFemail(String femail) {
		this.femail = femail;
	}

	@Column(name = "FIPhone", length = 50)
	public String getFiphone() {
		return this.fiphone;
	}

	public void setFiphone(String fiphone) {
		this.fiphone = fiphone;
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

	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicSocialgroups")
	@JsonIgnore
	public Set<YBasicSocialgroupsguestbook> getYBasicSocialgroupsguestbooks() {
		return this.YBasicSocialgroupsguestbooks;
	}

	public void setYBasicSocialgroupsguestbooks(
			Set<YBasicSocialgroupsguestbook> YBasicSocialgroupsguestbooks) {
		this.YBasicSocialgroupsguestbooks = YBasicSocialgroupsguestbooks;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicSocialgroups")
	@JsonIgnore
	public Set<YBasicSocialgroupsdynamic> getYBasicSocialgroupsdynamics() {
		return this.YBasicSocialgroupsdynamics;
	}

	public void setYBasicSocialgroupsdynamics(
			Set<YBasicSocialgroupsdynamic> YBasicSocialgroupsdynamics) {
		this.YBasicSocialgroupsdynamics = YBasicSocialgroupsdynamics;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicSocialgroups")
	@JsonIgnore
	public Set<YBasicSocialgroupsabout> getYBasicSocialgroupsabouts() {
		return this.YBasicSocialgroupsabouts;
	}

	public void setYBasicSocialgroupsabouts(
			Set<YBasicSocialgroupsabout> YBasicSocialgroupsabouts) {
		this.YBasicSocialgroupsabouts = YBasicSocialgroupsabouts;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicSocialgroups")
	@JsonIgnore
	public Set<YBasicMembercompany> getYBasicMembercompanies() {
		return this.YBasicMembercompanies;
	}

	public void setYBasicMembercompanies(
			Set<YBasicMembercompany> YBasicMembercompanies) {
		this.YBasicMembercompanies = YBasicMembercompanies;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicSocialgroups")
	@JsonIgnore
	public Set<YBasicSocialgroupscontact> getYBasicSocialgroupscontacts() {
		return this.YBasicSocialgroupscontacts;
	}

	public void setYBasicSocialgroupscontacts(
			Set<YBasicSocialgroupscontact> YBasicSocialgroupscontacts) {
		this.YBasicSocialgroupscontacts = YBasicSocialgroupscontacts;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicSocialgroups")
	@JsonIgnore
	public Set<YBasicSocialgroupsinform> getYBasicSocialgroupsinforms() {
		return this.YBasicSocialgroupsinforms;
	}

	public void setYBasicSocialgroupsinforms(
			Set<YBasicSocialgroupsinform> YBasicSocialgroupsinforms) {
		this.YBasicSocialgroupsinforms = YBasicSocialgroupsinforms;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicSocialgroups")
	@JsonIgnore
	public Set<YAppdevice> getYAppdevices() {
		return this.YAppdevices;
	}

	public void setYAppdevices(Set<YAppdevice> YAppdevices) {
		this.YAppdevices = YAppdevices;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicSocialgroups")
	@JsonIgnore
	public Set<YBasicMember> getYBasicMembers() {
		return this.YBasicMembers;
	}

	public void setYBasicMembers(
			Set<YBasicMember> YBasicMembers) {
		this.YBasicMembers = YBasicMembers;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicSocialgroups")
	@JsonIgnore
	public Set<YBasicSocialgroupsdemand> getYBasicSocialgroupsdemands() {
		return this.YBasicSocialgroupsdemands;
	}

	public void setYBasicSocialgroupsdemands(
			Set<YBasicSocialgroupsdemand> YBasicSocialgroupsdemands) {
		this.YBasicSocialgroupsdemands = YBasicSocialgroupsdemands;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicSocialgroups")
	@JsonIgnore
	public Set<YBasicAssurancecontent> getYBasicAssurancecontents() {
		return this.YBasicAssurancecontents;
	}

	public void setYBasicAssurancecontents(
			Set<YBasicAssurancecontent> YBasicAssurancecontents) {
		this.YBasicAssurancecontents = YBasicAssurancecontents;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicSocialgroups")
	@JsonIgnore
	public Set<YBasicSocialgroupsactivity> getYBasicSocialgroupsactivities() {
		return this.YBasicSocialgroupsactivities;
	}

	public void setYBasicSocialgroupsactivities(
			Set<YBasicSocialgroupsactivity> YBasicSocialgroupsactivities) {
		this.YBasicSocialgroupsactivities = YBasicSocialgroupsactivities;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicSocialgroups")
	@JsonIgnore
	public Set<YWallactivity> getYWallactivities() {
		return this.YWallactivities;
	}

	public void setYWallactivities(Set<YWallactivity> YWallactivities) {
		this.YWallactivities = YWallactivities;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicSocialgroups")
	@JsonIgnore
	public Set<YBasicSocialgroupssupply> getYBasicSocialgroupssupplies() {
		return this.YBasicSocialgroupssupplies;
	}

	public void setYBasicSocialgroupssupplies(
			Set<YBasicSocialgroupssupply> YBasicSocialgroupssupplies) {
		this.YBasicSocialgroupssupplies = YBasicSocialgroupssupplies;
	}

}