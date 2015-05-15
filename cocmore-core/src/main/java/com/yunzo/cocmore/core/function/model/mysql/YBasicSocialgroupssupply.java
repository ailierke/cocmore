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
 * YBasicSocialgroupssupply entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_socialgroupssupply" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicSocialgroupssupply implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private YBasicMember YBasicMember;//会员

	private YBasicCity YBasicCity;//城市
	private YBasicTrade YBasicTrade;//行业
	private YBasicCounty YBasicCounty;//区县
	private YBasicProvince YBasicProvince;//省份
	private YBasicSocialgroups YBasicSocialgroups;//社会团体
	private String fnumber;
	private String fheadline;
	private String fimages;
	private String fmessage;
	private String ftel;
	private String fnationalCertification;
	private Integer fareGuarantee;
	private String fcontacts;
	private Date fexpireTime;
	private Date fauditTime;
	private Date fpublisherTime;
	private String fauditIdea;
	private Integer fbillState;
	private String fcomment;
	private Integer flag;
	private Integer fisHide;
	private Integer flevel;
	private String jsonStr;
	private Integer fRank;
	public YBasicSocialgroupssupply(
			String fid,
			com.yunzo.cocmore.core.function.model.mysql.YBasicMember yBasicMember,
			com.yunzo.cocmore.core.function.model.mysql.YBasicCity yBasicCity,
			com.yunzo.cocmore.core.function.model.mysql.YBasicTrade yBasicTrade,
			com.yunzo.cocmore.core.function.model.mysql.YBasicCounty yBasicCounty,
			com.yunzo.cocmore.core.function.model.mysql.YBasicProvince yBasicProvince,
			com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups yBasicSocialgroups,
			String fnumber, String fheadline, String fimages, String fmessage,
			String ftel, String fnationalCertification, Integer fareGuarantee,
			String fcontacts, Date fexpireTime, Date fauditTime,
			Date fpublisherTime, String fauditIdea, Integer fbillState,
			String fcomment, Integer flag, Integer fisHide, Integer flevel,
			String jsonStr, Integer fRank, Set<YSupplyGroup> ySupplyGroups) {
		super();
		this.fid = fid;
		YBasicMember = yBasicMember;
		YBasicCity = yBasicCity;
		YBasicTrade = yBasicTrade;
		YBasicCounty = yBasicCounty;
		YBasicProvince = yBasicProvince;
		YBasicSocialgroups = yBasicSocialgroups;
		this.fnumber = fnumber;
		this.fheadline = fheadline;
		this.fimages = fimages;
		this.fmessage = fmessage;
		this.ftel = ftel;
		this.fnationalCertification = fnationalCertification;
		this.fareGuarantee = fareGuarantee;
		this.fcontacts = fcontacts;
		this.fexpireTime = fexpireTime;
		this.fauditTime = fauditTime;
		this.fpublisherTime = fpublisherTime;
		this.fauditIdea = fauditIdea;
		this.fbillState = fbillState;
		this.fcomment = fcomment;
		this.flag = flag;
		this.fisHide = fisHide;
		this.flevel = flevel;
		this.jsonStr = jsonStr;
		this.fRank = fRank;
		this.ySupplyGroups = ySupplyGroups;
	}
	
	public YBasicSocialgroupssupply(
			String fid,
			String fnumber, String fheadline, String fimages, String fmessage,
			String ftel, String fnationalCertification, Integer fareGuarantee,
			String fcontacts, Date fexpireTime, Date fauditTime,
			Date fpublisherTime, String fauditIdea, Integer fbillState,
			String fcomment, Integer flag, Integer fisHide, Integer flevel,
			String jsonStr, Integer fRank, Set<YSupplyGroup> ySupplyGroups) {
		super();
		this.fid = fid;
		this.fnumber = fnumber;
		this.fheadline = fheadline;
		this.fimages = fimages;
		this.fmessage = fmessage;
		this.ftel = ftel;
		this.fnationalCertification = fnationalCertification;
		this.fareGuarantee = fareGuarantee;
		this.fcontacts = fcontacts;
		this.fexpireTime = fexpireTime;
		this.fauditTime = fauditTime;
		this.fpublisherTime = fpublisherTime;
		this.fauditIdea = fauditIdea;
		this.fbillState = fbillState;
		this.fcomment = fcomment;
		this.flag = flag;
		this.fisHide = fisHide;
		this.flevel = flevel;
		this.jsonStr = jsonStr;
		this.fRank = fRank;
		this.ySupplyGroups = ySupplyGroups;
	}
	@Column(name = "FRank")
	public Integer getfRank() {
		return fRank;
	}

	public void setfRank(Integer fRank) {
		this.fRank = fRank;
	}

	private Set<YSupplyGroup> ySupplyGroups = new HashSet<YSupplyGroup>(0);
	// Constructors
	
	@Column(name = "jsonStr")
	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}
	
	/** default constructor */
	public YBasicSocialgroupssupply() {
	}

	/** minimal constructor */
	public YBasicSocialgroupssupply(String fid) {
		this.fid = fid;
	}

	public YBasicSocialgroupssupply(
			String fid,
			com.yunzo.cocmore.core.function.model.mysql.YBasicMember yBasicMember,
			com.yunzo.cocmore.core.function.model.mysql.YBasicCity yBasicCity,
			com.yunzo.cocmore.core.function.model.mysql.YBasicTrade yBasicTrade,
			com.yunzo.cocmore.core.function.model.mysql.YBasicCounty yBasicCounty,
			com.yunzo.cocmore.core.function.model.mysql.YBasicProvince yBasicProvince,
			com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups yBasicSocialgroups,
			String fnumber, String fheadline, String fimages, String fmessage,
			String ftel, String fnationalCertification, Integer fareGuarantee,
			String fcontacts, Date fexpireTime, Date fauditTime,
			Date fpublisherTime, String fauditIdea, Integer fbillState,
			String fcomment, Integer flag, Integer fisHide, Integer flevel,
			Set<YSupplyGroup> ySupplyGroups) {
		super();
		this.fid = fid;
		YBasicMember = yBasicMember;
		YBasicCity = yBasicCity;
		YBasicTrade = yBasicTrade;
		YBasicCounty = yBasicCounty;
		YBasicProvince = yBasicProvince;
		YBasicSocialgroups = yBasicSocialgroups;
		this.fnumber = fnumber;
		this.fheadline = fheadline;
		this.fimages = fimages;
		this.fmessage = fmessage;
		this.ftel = ftel;
		this.fnationalCertification = fnationalCertification;
		this.fareGuarantee = fareGuarantee;
		this.fcontacts = fcontacts;
		this.fexpireTime = fexpireTime;
		this.fauditTime = fauditTime;
		this.fpublisherTime = fpublisherTime;
		this.fauditIdea = fauditIdea;
		this.fbillState = fbillState;
		this.fcomment = fcomment;
		this.flag = flag;
		this.fisHide = fisHide;
		this.flevel = flevel;
		this.ySupplyGroups = ySupplyGroups;
	}


	@Column(name = "FAreGuarantee", length = 1)
	public Integer getFareGuarantee() {
		return fareGuarantee;
	}

	public void setFareGuarantee(Integer fareGuarantee) {
		this.fareGuarantee = fareGuarantee;
	}
	
	@Column(name = "FAuditIdea")
	public String getFauditIdea() {
		return this.fauditIdea;
	}

	@Column(name = "FAuditTime", length = 19)
	//@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFauditTime() {
		return this.fauditTime;
	}

	@Column(name = "FBillState")
	public Integer getFbillState() {
		return this.fbillState;
	}

	@Column(name = "FComment")
	public String getFcomment() {
		return this.fcomment;
	}

	@Column(name = "FContacts", length = 48)
	public String getFcontacts() {
		return this.fcontacts;
	}

	@Column(name = "FExpireTime", length = 19)
	//@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFexpireTime() {
		return this.fexpireTime;
	}

	@Column(name = "FHeadline", length = 100)
	public String getFheadline() {
		return this.fheadline;
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

	@Column(name = "Fimages", length = 65535)
	public String getFimages() {
		return this.fimages;
	}

	@Column(name = "FIsHide")
	public Integer getFisHide() {
		return this.fisHide;
	}

	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	@Column(name = "Flevel")
	public Integer getFlevel() {
		return this.flevel;
	}

	@Column(name = "FMessage", length = 65535)
	public String getFmessage() {
		return this.fmessage;
	}

	@Column(name = "FNationalCertification", length = 50)
	public String getFnationalCertification() {
		return this.fnationalCertification;
	}

	@Column(name = "FNumber", length = 100)
	public String getFnumber() {
		return this.fnumber;
	}

	@Column(name = "FPublisherTime", length = 19)
	//@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFpublisherTime() {
		return this.fpublisherTime;
	}

	@Column(name = "FTel", length = 50)
	public String getFtel() {
		return this.ftel;
	}


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FCityID")
	public YBasicCity getYBasicCity() {
		return this.YBasicCity;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FCountyID")
	public YBasicCounty getYBasicCounty() {
		return this.YBasicCounty;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FPublisherID")
	public YBasicMember getYBasicMember() {
		return this.YBasicMember;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FProvinceID")
	public YBasicProvince getYBasicProvince() {
		return this.YBasicProvince;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FSocialGroupsID")
	public YBasicSocialgroups getYBasicSocialgroups() {
		return this.YBasicSocialgroups;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FTradeID")
	public YBasicTrade getYBasicTrade() {
		return this.YBasicTrade;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicSocialgroupssupply")
	@JsonIgnore
	public Set<YSupplyGroup> getySupplyGroups() {
		return ySupplyGroups;
	}

	public void setFauditIdea(String fauditIdea) {
		this.fauditIdea = fauditIdea;
	}

	public void setFauditTime(Date fauditTime) {
		this.fauditTime = fauditTime;
	}

	public void setFbillState(Integer fbillState) {
		this.fbillState = fbillState;
	}

	public void setFcomment(String fcomment) {
		this.fcomment = fcomment;
	}

	public void setFcontacts(String fcontacts) {
		this.fcontacts = fcontacts;
	}

	public void setFexpireTime(Date fexpireTime) {
		this.fexpireTime = fexpireTime;
	}

	public void setFheadline(String fheadline) {
		this.fheadline = fheadline;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public void setFimages(String fimages) {
		this.fimages = fimages;
	}

	public void setFisHide(Integer fisHide) {
		this.fisHide = fisHide;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public void setFlevel(Integer flevel) {
		this.flevel = flevel;
	}

	public void setFmessage(String fmessage) {
		this.fmessage = fmessage;
	}

	public void setFnationalCertification(String fnationalCertification) {
		this.fnationalCertification = fnationalCertification;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	public void setFpublisherTime(Date fpublisherTime) {
		this.fpublisherTime = fpublisherTime;
	}

	public void setFtel(String ftel) {
		this.ftel = ftel;
	}


	public void setYBasicCity(YBasicCity YBasicCity) {
		this.YBasicCity = YBasicCity;
	}

	public void setYBasicCounty(YBasicCounty YBasicCounty) {
		this.YBasicCounty = YBasicCounty;
	}

	public void setYBasicMember(YBasicMember YBasicMember) {
		this.YBasicMember = YBasicMember;
	}

	public void setYBasicProvince(YBasicProvince YBasicProvince) {
		this.YBasicProvince = YBasicProvince;
	}

	public void setYBasicSocialgroups(YBasicSocialgroups YBasicSocialgroups) {
		this.YBasicSocialgroups = YBasicSocialgroups;
	}

	public void setYBasicTrade(YBasicTrade YBasicTrade) {
		this.YBasicTrade = YBasicTrade;
	}

	public void setySupplyGroups(Set<YSupplyGroup> ySupplyGroups) {
		this.ySupplyGroups = ySupplyGroups;
	}

}