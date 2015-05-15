package com.yunzo.cocmore.core.function.model.mysql;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * YBasicMember entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_member" )
public class YBasicMember implements java.io.Serializable {

	// Fields

	private String fid;
	private YBasicCity YBasicCity;
	private YSystemUsers YSystemUsers;
	private YBasicCounty YBasicCounty;
	private YBasicProvince YBasicProvince;
	private YBasicSocialgroups YBasicSocialgroups;
	private String fnumber;
	private String fmobilePhone;
	private String fpassword;
	private String ftempPwd;
	private String fheadImage;
	private Integer fisOriginalPassword;
	private String fname;
	private Date fbirthday;
	private Integer fsex;
	private String fsource;
	private String fpreviousEmail;
	private String femail;
	private String fhomePhone;
	private String fsecondPhone;
	private String ftypeId;
	private String fsite;
	private String fnickName;
	private String freceivingAddress;
	private String fcomment;
	private String fnativePlace;
	private Integer fisHidePhone;
	private String fcreaterId;
	private String fmodifiedId;
	private String flastModifiedId;
	private Date fcreateTime;
	private Date fmodifiedTime;
	private Date flastModifiedTime;
	private Integer flag;
	private String jsonStr;
	private int isAdmin;			//是否是管理员
	private Integer fbillState;
	private Set<YBasicMembercompany> YBasicMembercompanies = new HashSet<YBasicMembercompany>(
			0);
	private Set<YPointlike> YPointlikes = new HashSet<YPointlike>(0);
	private Set<YBasicMemberbymemberhide> YBasicMemberbymemberhidesForFinMemberId = new HashSet<YBasicMemberbymemberhide>(
			0);
	private Set<YComment> YComments = new HashSet<YComment>(0);
	private Set<YBasicMemberbymemberhide> YBasicMemberbymemberhidesForFoutMemberId = new HashSet<YBasicMemberbymemberhide>(
			0);
	private Set<YBasicSocialgroupsguestbook> YBasicSocialgroupsguestbooks = new HashSet<YBasicSocialgroupsguestbook>(
			0);
	private Set<FSmsSendrecord> FSmsSendrecords = new HashSet<FSmsSendrecord>(0);
	private Set<YManshowinformation> YManshowinformations = new HashSet<YManshowinformation>(
			0);
	private Set<YBasicMemberbymemberblacklist> YBasicMemberbymemberblacklists = new HashSet<YBasicMemberbymemberblacklist>(
			0);
	private Set<YBasicSocialgroupsdemand> YBasicSocialgroupsdemands = new HashSet<YBasicSocialgroupsdemand>(
			0);
	private Set<YBasicSocialgroupssupply> YBasicSocialgroupssupplies = new HashSet<YBasicSocialgroupssupply>(
			0);
	private Set<YBasicentriesMemberdistribution> YBasicentriesMemberdistributions = new HashSet<YBasicentriesMemberdistribution>(
			0);
	private Set<YWallreply> YWallreplies = new HashSet<YWallreply>(0);
	private Set<YBasicSocialgroupsactivity> YBasicSocialgroupsactivities = new HashSet<YBasicSocialgroupsactivity>(
			0);
	private Set<YBasicMemberbypositionhide> YBasicMemberbypositionhides = new HashSet<YBasicMemberbypositionhide>(
			0);

	// Constructors

	/** default constructor */
	public YBasicMember() {
	}

	/** minimal constructor */
	public YBasicMember(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicMember(
			String fid,
			YBasicCity YBasicCity,
			YSystemUsers YSystemUsers,
			YBasicCounty YBasicCounty,
			YBasicProvince YBasicProvince,
			YBasicSocialgroups YBasicSocialgroups,
			String fnumber,
			String fmobilePhone,
			String fpassword,
			String fheadImage,
			Integer fisOriginalPassword,
			String fname,
			Date fbirthday,
			Integer fsex,
			String fsource,
			String fpreviousEmail,
			String femail,
			String fhomePhone,
			String fsecondPhone,
			String ftypeId,
			String fsite,
			String fnickName,
			String freceivingAddress,
			String fcomment,
			String fnativePlace,
			Integer fisHidePhone,
			String fcreaterId,
			String fmodifiedId,
			String flastModifiedId,
			Date fcreateTime,
			Date fmodifiedTime,
			Date flastModifiedTime,
			Integer flag,
			Set<YBasicMembercompany> YBasicMembercompanies,
			Set<YPointlike> YPointlikes,
			Set<YBasicMemberbymemberhide> YBasicMemberbymemberhidesForFinMemberId,
			Set<YComment> YComments,
			Set<YBasicMemberbymemberhide> YBasicMemberbymemberhidesForFoutMemberId,
			Set<YBasicSocialgroupsguestbook> YBasicSocialgroupsguestbooks,
			Set<FSmsSendrecord> FSmsSendrecords,
			Set<YManshowinformation> YManshowinformations,
			Set<YBasicMemberbymemberblacklist> YBasicMemberbymemberblacklists,
			Set<YBasicSocialgroupsdemand> YBasicSocialgroupsdemands,
			Set<YBasicSocialgroupssupply> YBasicSocialgroupssupplies,
			Set<YBasicentriesMemberdistribution> YBasicentriesMemberdistributions,
			Set<YWallreply> YWallreplies,
			Set<YBasicSocialgroupsactivity> YBasicSocialgroupsactivities,
			Set<YBasicMemberbypositionhide> YBasicMemberbypositionhides) {
		this.fid = fid;
		this.YBasicCity = YBasicCity;
		this.YSystemUsers = YSystemUsers;
		this.YBasicCounty = YBasicCounty;
		this.YBasicProvince = YBasicProvince;
		this.YBasicSocialgroups = YBasicSocialgroups;
		this.fnumber = fnumber;
		this.fmobilePhone = fmobilePhone;
		this.fpassword = fpassword;
		this.fheadImage = fheadImage;
		this.fisOriginalPassword = fisOriginalPassword;
		this.fname = fname;
		this.fbirthday = fbirthday;
		this.fsex = fsex;
		this.fsource = fsource;
		this.fpreviousEmail = fpreviousEmail;
		this.femail = femail;
		this.fhomePhone = fhomePhone;
		this.fsecondPhone = fsecondPhone;
		this.ftypeId = ftypeId;
		this.fsite = fsite;
		this.fnickName = fnickName;
		this.freceivingAddress = freceivingAddress;
		this.fcomment = fcomment;
		this.fnativePlace = fnativePlace;
		this.fisHidePhone = fisHidePhone;
		this.fcreaterId = fcreaterId;
		this.fmodifiedId = fmodifiedId;
		this.flastModifiedId = flastModifiedId;
		this.fcreateTime = fcreateTime;
		this.fmodifiedTime = fmodifiedTime;
		this.flastModifiedTime = flastModifiedTime;
		this.flag = flag;
		this.YBasicMembercompanies = YBasicMembercompanies;
		this.YPointlikes = YPointlikes;
		this.YBasicMemberbymemberhidesForFinMemberId = YBasicMemberbymemberhidesForFinMemberId;
		this.YComments = YComments;
		this.YBasicMemberbymemberhidesForFoutMemberId = YBasicMemberbymemberhidesForFoutMemberId;
		this.YBasicSocialgroupsguestbooks = YBasicSocialgroupsguestbooks;
		this.FSmsSendrecords = FSmsSendrecords;
		this.YManshowinformations = YManshowinformations;
		this.YBasicMemberbymemberblacklists = YBasicMemberbymemberblacklists;
		this.YBasicSocialgroupsdemands = YBasicSocialgroupsdemands;
		this.YBasicSocialgroupssupplies = YBasicSocialgroupssupplies;
		this.YBasicentriesMemberdistributions = YBasicentriesMemberdistributions;
		this.YWallreplies = YWallreplies;
		this.YBasicSocialgroupsactivities = YBasicSocialgroupsactivities;
		this.YBasicMemberbypositionhides = YBasicMemberbypositionhides;
	}

	// Property accessors
	@Id
	@Column(name = "FID", unique = true, nullable = false, length = 36)
	public String getFid() {
		return this.fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
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
	@JoinColumn(name = "FAdminID")
	public YSystemUsers getYSystemUsers() {
		return this.YSystemUsers;
	}

	public void setYSystemUsers(YSystemUsers YSystemUsers) {
		this.YSystemUsers = YSystemUsers;
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
	@JoinColumn(name = "FProvinceID")
	public YBasicProvince getYBasicProvince() {
		return this.YBasicProvince;
	}

	public void setYBasicProvince(YBasicProvince YBasicProvince) {
		this.YBasicProvince = YBasicProvince;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FSocialGroupsID")
	public YBasicSocialgroups getYBasicSocialgroups() {
		return this.YBasicSocialgroups;
	}

	public void setYBasicSocialgroups(YBasicSocialgroups YBasicSocialgroups) {
		this.YBasicSocialgroups = YBasicSocialgroups;
	}

	@Column(name = "FNumber", length = 36)
	public String getFnumber() {
		return this.fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	@Column(name = "FMobilePhone", length = 100)
	public String getFmobilePhone() {
		return this.fmobilePhone;
	}

	public void setFmobilePhone(String fmobilePhone) {
		this.fmobilePhone = fmobilePhone;
	}

	@Column(name = "FPassword", length = 36)
	public String getFpassword() {
		return this.fpassword;
	}

	public void setFpassword(String fpassword) {
		this.fpassword = fpassword;
	}

	@Column(name = "FHeadImage", length = 200)
	public String getFheadImage() {
		return this.fheadImage;
	}

	public void setFheadImage(String fheadImage) {
		this.fheadImage = fheadImage;
	}

	@Column(name = "FIsOriginalPassword")
	public Integer getFisOriginalPassword() {
		return this.fisOriginalPassword;
	}

	public void setFisOriginalPassword(Integer fisOriginalPassword) {
		this.fisOriginalPassword = fisOriginalPassword;
	}

	@Column(name = "FName", length = 36)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "FBirthday", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFbirthday() {
		return this.fbirthday;
	}

	public void setFbirthday(Date fbirthday) {
		this.fbirthday = fbirthday;
	}

	@Column(name = "FSex", length = 4)
	public Integer getFsex() {
		return this.fsex;
	}

	public void setFsex(Integer fsex) {
		this.fsex = fsex;
	}

	@Column(name = "FSource", length = 50)
	public String getFsource() {
		return this.fsource;
	}

	public void setFsource(String fsource) {
		this.fsource = fsource;
	}

	@Column(name = "FPreviousEmail", length = 48)
	public String getFpreviousEmail() {
		return this.fpreviousEmail;
	}

	public void setFpreviousEmail(String fpreviousEmail) {
		this.fpreviousEmail = fpreviousEmail;
	}

	@Column(name = "FEmail", length = 48)
	public String getFemail() {
		return this.femail;
	}

	public void setFemail(String femail) {
		this.femail = femail;
	}

	@Column(name = "FHomePhone", length = 48)
	public String getFhomePhone() {
		return this.fhomePhone;
	}

	public void setFhomePhone(String fhomePhone) {
		this.fhomePhone = fhomePhone;
	}

	@Column(name = "FSecondPhone", length = 48)
	public String getFsecondPhone() {
		return this.fsecondPhone;
	}

	public void setFsecondPhone(String fsecondPhone) {
		this.fsecondPhone = fsecondPhone;
	}

	@Column(name = "FTypeID", length = 36)
	public String getFtypeId() {
		return this.ftypeId;
	}

	public void setFtypeId(String ftypeId) {
		this.ftypeId = ftypeId;
	}

	@Column(name = "FSite", length = 300)
	public String getFsite() {
		return this.fsite;
	}

	public void setFsite(String fsite) {
		this.fsite = fsite;
	}

	@Column(name = "FNickName", length = 50)
	public String getFnickName() {
		return this.fnickName;
	}

	public void setFnickName(String fnickName) {
		this.fnickName = fnickName;
	}

	@Column(name = "FReceivingAddress", length = 200)
	public String getFreceivingAddress() {
		return this.freceivingAddress;
	}

	public void setFreceivingAddress(String freceivingAddress) {
		this.freceivingAddress = freceivingAddress;
	}

	@Column(name = "FComment")
	public String getFcomment() {
		return this.fcomment;
	}

	public void setFcomment(String fcomment) {
		this.fcomment = fcomment;
	}

	@Column(name = "FNativePlace", length = 100)
	public String getFnativePlace() {
		return this.fnativePlace;
	}

	public void setFnativePlace(String fnativePlace) {
		this.fnativePlace = fnativePlace;
	}

	@Column(name = "FIsHidePhone")
	public Integer getFisHidePhone() {
		return this.fisHidePhone;
	}

	public void setFisHidePhone(Integer fisHidePhone) {
		this.fisHidePhone = fisHidePhone;
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

	@Column(name = "Flag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
	@Column(name = "jsonStr")
	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}
	
	
	@Column(name = "isAdmin")
	public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicMember")
	@JsonIgnore
	public Set<YBasicMembercompany> getYBasicMembercompanies() {
		return this.YBasicMembercompanies;
	}

	public void setYBasicMembercompanies(
			Set<YBasicMembercompany> YBasicMembercompanies) {
		this.YBasicMembercompanies = YBasicMembercompanies;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicMember")
	@JsonIgnore
	public Set<YPointlike> getYPointlikes() {
		return this.YPointlikes;
	}

	public void setYPointlikes(Set<YPointlike> YPointlikes) {
		this.YPointlikes = YPointlikes;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicMemberByFinMemberId")
	@JsonIgnore
	public Set<YBasicMemberbymemberhide> getYBasicMemberbymemberhidesForFinMemberId() {
		return this.YBasicMemberbymemberhidesForFinMemberId;
	}

	public void setYBasicMemberbymemberhidesForFinMemberId(
			Set<YBasicMemberbymemberhide> YBasicMemberbymemberhidesForFinMemberId) {
		this.YBasicMemberbymemberhidesForFinMemberId = YBasicMemberbymemberhidesForFinMemberId;
	}


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicMember")
	@JsonIgnore
	public Set<YComment> getYComments() {
		return this.YComments;
	}

	public void setYComments(Set<YComment> YComments) {
		this.YComments = YComments;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicMemberByFoutMemberId")
	@JsonIgnore
	public Set<YBasicMemberbymemberhide> getYBasicMemberbymemberhidesForFoutMemberId() {
		return this.YBasicMemberbymemberhidesForFoutMemberId;
	}

	public void setYBasicMemberbymemberhidesForFoutMemberId(
			Set<YBasicMemberbymemberhide> YBasicMemberbymemberhidesForFoutMemberId) {
		this.YBasicMemberbymemberhidesForFoutMemberId = YBasicMemberbymemberhidesForFoutMemberId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicMember")
	@JsonIgnore
	public Set<YBasicSocialgroupsguestbook> getYBasicSocialgroupsguestbooks() {
		return this.YBasicSocialgroupsguestbooks;
	}

	public void setYBasicSocialgroupsguestbooks(
			Set<YBasicSocialgroupsguestbook> YBasicSocialgroupsguestbooks) {
		this.YBasicSocialgroupsguestbooks = YBasicSocialgroupsguestbooks;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicMember")
	@JsonIgnore
	public Set<FSmsSendrecord> getFSmsSendrecords() {
		return this.FSmsSendrecords;
	}

	public void setFSmsSendrecords(Set<FSmsSendrecord> FSmsSendrecords) {
		this.FSmsSendrecords = FSmsSendrecords;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicMember")
	@JsonIgnore
	public Set<YManshowinformation> getYManshowinformations() {
		return this.YManshowinformations;
	}

	public void setYManshowinformations(
			Set<YManshowinformation> YManshowinformations) {
		this.YManshowinformations = YManshowinformations;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicMember")
	@JsonIgnore
	public Set<YBasicMemberbymemberblacklist> getYBasicMemberbymemberblacklists() {
		return this.YBasicMemberbymemberblacklists;
	}

	public void setYBasicMemberbymemberblacklists(
			Set<YBasicMemberbymemberblacklist> YBasicMemberbymemberblacklists) {
		this.YBasicMemberbymemberblacklists = YBasicMemberbymemberblacklists;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicMember")
	@JsonIgnore
	public Set<YBasicSocialgroupsdemand> getYBasicSocialgroupsdemands() {
		return this.YBasicSocialgroupsdemands;
	}

	public void setYBasicSocialgroupsdemands(
			Set<YBasicSocialgroupsdemand> YBasicSocialgroupsdemands) {
		this.YBasicSocialgroupsdemands = YBasicSocialgroupsdemands;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicMember")
	@JsonIgnore
	public Set<YBasicSocialgroupssupply> getYBasicSocialgroupssupplies() {
		return this.YBasicSocialgroupssupplies;
	}

	public void setYBasicSocialgroupssupplies(
			Set<YBasicSocialgroupssupply> YBasicSocialgroupssupplies) {
		this.YBasicSocialgroupssupplies = YBasicSocialgroupssupplies;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicMember")
	@JsonIgnore
	public Set<YBasicentriesMemberdistribution> getYBasicentriesMemberdistributions() {
		return this.YBasicentriesMemberdistributions;
	}

	public void setYBasicentriesMemberdistributions(
			Set<YBasicentriesMemberdistribution> YBasicentriesMemberdistributions) {
		this.YBasicentriesMemberdistributions = YBasicentriesMemberdistributions;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicMember")
	@JsonIgnore
	public Set<YWallreply> getYWallreplies() {
		return this.YWallreplies;
	}

	public void setYWallreplies(Set<YWallreply> YWallreplies) {
		this.YWallreplies = YWallreplies;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicMember")
	@JsonIgnore
	public Set<YBasicSocialgroupsactivity> getYBasicSocialgroupsactivities() {
		return this.YBasicSocialgroupsactivities;
	}

	public void setYBasicSocialgroupsactivities(
			Set<YBasicSocialgroupsactivity> YBasicSocialgroupsactivities) {
		this.YBasicSocialgroupsactivities = YBasicSocialgroupsactivities;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicMember")
	@JsonIgnore
	public Set<YBasicMemberbypositionhide> getYBasicMemberbypositionhides() {
		return this.YBasicMemberbypositionhides;
	}

	public void setYBasicMemberbypositionhides(
			Set<YBasicMemberbypositionhide> YBasicMemberbypositionhides) {
		this.YBasicMemberbypositionhides = YBasicMemberbypositionhides;
	}
	
	@Column(name = "FBillState")
	public Integer getFbillState() {
		return this.fbillState;
	}

	public void setFbillState(Integer fbillState) {
		this.fbillState = fbillState;
	}

	@Column(name = "FTempPwd")
	public String getFtempPwd() {
		return ftempPwd;
	}

	public void setFtempPwd(String ftempPwd) {
		this.ftempPwd = ftempPwd;
	}
	
	

}