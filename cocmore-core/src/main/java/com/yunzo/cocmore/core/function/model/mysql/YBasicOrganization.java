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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * YBasicOrganization entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_organization" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicOrganization implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private String fnumber;
	private String fname;
	private String fabbreviation;
	private String fenglishName;
	private String fdescribe;
	private Date fstartTime;
	private Date fstorageTime;
	private String falterDescribe;
	private String faddress;
	private String fsuperiorOrganizationId;
	private Integer fgradationType;
	private String fphone;
	private Integer funitGradation;
	private String ffax;
	private String fdistrictId;
	private String femail;
	private String fpostcode;
	private Date festablishDate;
	private double fregisteredCapital;
	private String fregisterNumber;
	private String forganizationCode;
	private String fbusinessIndate;
	private String ftaxAffairsNumber;
	private String fchurchyard;
	private String foverseas;
	private String flegalPersonCorporation;
	private String flegalPersonDelegate;
	private String fleafNode;
	private Integer flevel;
	private Integer fbillState;
	private String fcomment;
	private String fcreaterId;
	private String fmodifiedId;
	private String flastModifiedId;
	private Date fcreateTime;
	private Date fmodifiedTime;
	private Date flastModifiedTime;
	private Integer flag;
	private Set<YSystemconfigurationParameter> YSystemconfigurationParameters = new HashSet<YSystemconfigurationParameter>(
			0);
	private Set<YBasicSocialgroups> YBasicSocialgroupses = new HashSet<YBasicSocialgroups>(
			0);
	private Set<YSystemconfigurationEncodingrules> YSystemconfigurationEncodingruleses = new HashSet<YSystemconfigurationEncodingrules>(
			0);
//	private Set<YRoleauthorization> YRoleauthorizations = new HashSet<YRoleauthorization>(
//			0);
//	private Set<YCharacter> YCharacters = new HashSet<YCharacter>(0);

	// Constructors

	/** default constructor */
	public YBasicOrganization() {
	}

	/** minimal constructor */
	public YBasicOrganization(String fid) {
		this.fid = fid;
	}
	
	public YBasicOrganization(String fid,String fname,Integer fbillState,String fsuperiorOrganizationId) {
		this.fid = fid;
		this.fname = fname;
		this.fbillState = fbillState;
		this.fsuperiorOrganizationId = fsuperiorOrganizationId;
	}

	/** full constructor */
	public YBasicOrganization(
			String fid,
			String fnumber,
			String fname,
			String fabbreviation,
			String fenglishName,
			String fdescribe,
			Date fstartTime,
			Date fstorageTime,
			String falterDescribe,
			String faddress,
			String fsuperiorOrganizationId,
			Integer fgradationType,
			String fphone,
			Integer funitGradation,
			String ffax,
			String fdistrictId,
			String femail,
			String fpostcode,
			Date festablishDate,
			double fregisteredCapital,
			String fregisterNumber,
			String forganizationCode,
			String fbusinessIndate,
			String ftaxAffairsNumber,
			String fchurchyard,
			String foverseas,
			String flegalPersonCorporation,
			String flegalPersonDelegate,
			String fleafNode,
			Integer flevel,
			Integer fbillState,
			String fcomment,
			String fcreaterId,
			String fmodifiedId,
			String flastModifiedId,
			Date fcreateTime,
			Date fmodifiedTime,
			Date flastModifiedTime,
			Integer flag,
			Set<YSystemconfigurationParameter> YSystemconfigurationParameters,
			Set<YBasicSocialgroups> YBasicSocialgroupses,
			Set<YSystemconfigurationEncodingrules> YSystemconfigurationEncodingruleses
			//Set<YRoleauthorization> YRoleauthorizations,
			/*Set<YCharacter> YCharacters*/) {
		this.fid = fid;
		this.fnumber = fnumber;
		this.fname = fname;
		this.fabbreviation = fabbreviation;
		this.fenglishName = fenglishName;
		this.fdescribe = fdescribe;
		this.fstartTime = fstartTime;
		this.fstorageTime = fstorageTime;
		this.falterDescribe = falterDescribe;
		this.faddress = faddress;
		this.fsuperiorOrganizationId = fsuperiorOrganizationId;
		this.fgradationType = fgradationType;
		this.fphone = fphone;
		this.funitGradation = funitGradation;
		this.ffax = ffax;
		this.fdistrictId = fdistrictId;
		this.femail = femail;
		this.fpostcode = fpostcode;
		this.festablishDate = festablishDate;
		this.fregisteredCapital = fregisteredCapital;
		this.fregisterNumber = fregisterNumber;
		this.forganizationCode = forganizationCode;
		this.fbusinessIndate = fbusinessIndate;
		this.ftaxAffairsNumber = ftaxAffairsNumber;
		this.fchurchyard = fchurchyard;
		this.foverseas = foverseas;
		this.flegalPersonCorporation = flegalPersonCorporation;
		this.flegalPersonDelegate = flegalPersonDelegate;
		this.fleafNode = fleafNode;
		this.flevel = flevel;
		this.fbillState = fbillState;
		this.fcomment = fcomment;
		this.fcreaterId = fcreaterId;
		this.fmodifiedId = fmodifiedId;
		this.flastModifiedId = flastModifiedId;
		this.fcreateTime = fcreateTime;
		this.fmodifiedTime = fmodifiedTime;
		this.flastModifiedTime = flastModifiedTime;
		this.flag = flag;
		this.YSystemconfigurationParameters = YSystemconfigurationParameters;
		this.YBasicSocialgroupses = YBasicSocialgroupses;
		this.YSystemconfigurationEncodingruleses = YSystemconfigurationEncodingruleses;
		//this.YRoleauthorizations = YRoleauthorizations;
		//this.YCharacters = YCharacters;
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

	@Column(name = "FAbbreviation", length = 48)
	public String getFabbreviation() {
		return this.fabbreviation;
	}

	public void setFabbreviation(String fabbreviation) {
		this.fabbreviation = fabbreviation;
	}

	@Column(name = "FEnglishName", length = 48)
	public String getFenglishName() {
		return this.fenglishName;
	}

	public void setFenglishName(String fenglishName) {
		this.fenglishName = fenglishName;
	}

	@Column(name = "FDescribe")
	public String getFdescribe() {
		return this.fdescribe;
	}

	public void setFdescribe(String fdescribe) {
		this.fdescribe = fdescribe;
	}

	@Column(name = "FStartTime", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFstartTime() {
		return this.fstartTime;
	}

	public void setFstartTime(Date fstartTime) {
		this.fstartTime = fstartTime;
	}

	@Column(name = "FStorageTime", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFstorageTime() {
		return this.fstorageTime;
	}

	public void setFstorageTime(Date fstorageTime) {
		this.fstorageTime = fstorageTime;
	}

	@Column(name = "FAlterDescribe")
	public String getFalterDescribe() {
		return this.falterDescribe;
	}

	public void setFalterDescribe(String falterDescribe) {
		this.falterDescribe = falterDescribe;
	}

	@Column(name = "FAddress", length = 100)
	public String getFaddress() {
		return this.faddress;
	}

	public void setFaddress(String faddress) {
		this.faddress = faddress;
	}

	@Column(name = "FSuperiorOrganizationID", length = 36)
	public String getFsuperiorOrganizationId() {
		return this.fsuperiorOrganizationId;
	}

	public void setFsuperiorOrganizationId(String fsuperiorOrganizationId) {
		this.fsuperiorOrganizationId = fsuperiorOrganizationId;
	}

	@Column(name = "FGradationType")
	public Integer getFgradationType() {
		return this.fgradationType;
	}

	public void setFgradationType(Integer fgradationType) {
		this.fgradationType = fgradationType;
	}

	@Column(name = "FPhone", length = 100)
	public String getFphone() {
		return this.fphone;
	}

	public void setFphone(String fphone) {
		this.fphone = fphone;
	}

	@Column(name = "FUnitGradation")
	public Integer getFunitGradation() {
		return this.funitGradation;
	}

	public void setFunitGradation(Integer funitGradation) {
		this.funitGradation = funitGradation;
	}

	@Column(name = "FFax", length = 48)
	public String getFfax() {
		return this.ffax;
	}

	public void setFfax(String ffax) {
		this.ffax = ffax;
	}

	@Column(name = "FDistrictID", length = 36)
	public String getFdistrictId() {
		return this.fdistrictId;
	}

	public void setFdistrictId(String fdistrictId) {
		this.fdistrictId = fdistrictId;
	}

	@Column(name = "FEmail", length = 48)
	public String getFemail() {
		return this.femail;
	}

	public void setFemail(String femail) {
		this.femail = femail;
	}

	@Column(name = "FPostcode", length = 48)
	public String getFpostcode() {
		return this.fpostcode;
	}

	public void setFpostcode(String fpostcode) {
		this.fpostcode = fpostcode;
	}

	//@Temporal(TemporalType.DATE)
	@Column(name = "FEstablishDate", length = 10)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFestablishDate() {
		return this.festablishDate;
	}

	public void setFestablishDate(Date festablishDate) {
		this.festablishDate = festablishDate;
	}

	@Column(name = "FRegisteredCapital", precision = 22, scale = 0)
	public double getFregisteredCapital() {
		return this.fregisteredCapital;
	}

	public void setFregisteredCapital(double fregisteredCapital) {
		this.fregisteredCapital = fregisteredCapital;
	}

	@Column(name = "FRegisterNumber", length = 48)
	public String getFregisterNumber() {
		return this.fregisterNumber;
	}

	public void setFregisterNumber(String fregisterNumber) {
		this.fregisterNumber = fregisterNumber;
	}

	@Column(name = "FOrganizationCode", length = 48)
	public String getForganizationCode() {
		return this.forganizationCode;
	}

	public void setForganizationCode(String forganizationCode) {
		this.forganizationCode = forganizationCode;
	}

	@Column(name = "FBusinessIndate", length = 48)
	public String getFbusinessIndate() {
		return this.fbusinessIndate;
	}

	public void setFbusinessIndate(String fbusinessIndate) {
		this.fbusinessIndate = fbusinessIndate;
	}

	@Column(name = "FTaxAffairsNumber", length = 48)
	public String getFtaxAffairsNumber() {
		return this.ftaxAffairsNumber;
	}

	public void setFtaxAffairsNumber(String ftaxAffairsNumber) {
		this.ftaxAffairsNumber = ftaxAffairsNumber;
	}

	@Column(name = "FChurchyard", length = 1)
	public String getFchurchyard() {
		return this.fchurchyard;
	}

	public void setFchurchyard(String fchurchyard) {
		this.fchurchyard = fchurchyard;
	}

	@Column(name = "FOverseas", length = 1)
	public String getFoverseas() {
		return this.foverseas;
	}

	public void setFoverseas(String foverseas) {
		this.foverseas = foverseas;
	}

	@Column(name = "FLegalPersonCorporation", length = 1)
	public String getFlegalPersonCorporation() {
		return this.flegalPersonCorporation;
	}

	public void setFlegalPersonCorporation(String flegalPersonCorporation) {
		this.flegalPersonCorporation = flegalPersonCorporation;
	}

	@Column(name = "FLegalPersonDelegate", length = 36)
	public String getFlegalPersonDelegate() {
		return this.flegalPersonDelegate;
	}

	public void setFlegalPersonDelegate(String flegalPersonDelegate) {
		this.flegalPersonDelegate = flegalPersonDelegate;
	}

	@Column(name = "FLeafNode", length = 1)
	public String getFleafNode() {
		return this.fleafNode;
	}

	public void setFleafNode(String fleafNode) {
		this.fleafNode = fleafNode;
	}

	@Column(name = "FLevel")
	public Integer getFlevel() {
		return this.flevel;
	}

	public void setFlevel(Integer flevel) {
		this.flevel = flevel;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicOrganization")
	@JsonIgnore
	public Set<YSystemconfigurationParameter> getYSystemconfigurationParameters() {
		return this.YSystemconfigurationParameters;
	}

	public void setYSystemconfigurationParameters(
			Set<YSystemconfigurationParameter> YSystemconfigurationParameters) {
		this.YSystemconfigurationParameters = YSystemconfigurationParameters;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicOrganization")
	@JsonIgnore
	public Set<YBasicSocialgroups> getYBasicSocialgroupses() {
		return this.YBasicSocialgroupses;
	}

	public void setYBasicSocialgroupses(
			Set<YBasicSocialgroups> YBasicSocialgroupses) {
		this.YBasicSocialgroupses = YBasicSocialgroupses;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicOrganization")
	@JsonIgnore
	public Set<YSystemconfigurationEncodingrules> getYSystemconfigurationEncodingruleses() {
		return this.YSystemconfigurationEncodingruleses;
	}

	public void setYSystemconfigurationEncodingruleses(
			Set<YSystemconfigurationEncodingrules> YSystemconfigurationEncodingruleses) {
		this.YSystemconfigurationEncodingruleses = YSystemconfigurationEncodingruleses;
	}

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicOrganization")
//	@JsonIgnore
//	public Set<YRoleauthorization> getYRoleauthorizations() {
//		return this.YRoleauthorizations;
//	}
//
//	public void setYRoleauthorizations(
//			Set<YRoleauthorization> YRoleauthorizations) {
//		this.YRoleauthorizations = YRoleauthorizations;
//	}

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicOrganization")
//	@JsonIgnore
//	public Set<YCharacter> getYCharacters() {
//		return this.YCharacters;
//	}
//
//	public void setYCharacters(Set<YCharacter> YCharacters) {
//		this.YCharacters = YCharacters;
//	}

}