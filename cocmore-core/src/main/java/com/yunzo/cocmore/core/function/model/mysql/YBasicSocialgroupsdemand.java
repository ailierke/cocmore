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
 * YBasicSocialgroupsdemand entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_socialgroupsdemand" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicSocialgroupsdemand implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private YBasicMember YBasicMember;
	private YBasicCity YBasicCity;
	private YBasicTrade YBasicTrade;
	private YBasicCounty YBasicCounty;
	private YBasicProvince YBasicProvince;
	private YBasicSocialgroups YBasicSocialgroups;
	private String fnumber;
	private String fheadline;
	private String fimages;
	private String fmessage;
	private String fcontacts;
	private String ftel;
	private Date fstartTime;
	private Date ffinishTime;
	private Integer fbillState;
	private String fcomment;
	private Integer flag;
	private Integer fisHide;
	private Integer flevel;
	private Date fpublisherTime;
	private Integer fRank;

	public YBasicSocialgroupsdemand(
			String fid,
			com.yunzo.cocmore.core.function.model.mysql.YBasicMember yBasicMember,
			com.yunzo.cocmore.core.function.model.mysql.YBasicCity yBasicCity,
			com.yunzo.cocmore.core.function.model.mysql.YBasicTrade yBasicTrade,
			com.yunzo.cocmore.core.function.model.mysql.YBasicCounty yBasicCounty,
			com.yunzo.cocmore.core.function.model.mysql.YBasicProvince yBasicProvince,
			com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups yBasicSocialgroups,
			String fnumber, String fheadline, String fimages, String fmessage,
			String fcontacts, String ftel, Date fstartTime, Date ffinishTime,
			Integer fbillState, String fcomment, Integer flag, Integer fisHide,
			Integer flevel, Date fpublisherTime, Integer fRank) {
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
		this.fcontacts = fcontacts;
		this.ftel = ftel;
		this.fstartTime = fstartTime;
		this.ffinishTime = ffinishTime;
		this.fbillState = fbillState;
		this.fcomment = fcomment;
		this.flag = flag;
		this.fisHide = fisHide;
		this.flevel = flevel;
		this.fpublisherTime = fpublisherTime;
		this.fRank = fRank;
	}
	public YBasicSocialgroupsdemand(
			String fid,
			String fnumber, String fheadline, String fimages, String fmessage,
			String fcontacts, String ftel, Date fstartTime, Date ffinishTime,
			Integer fbillState, String fcomment, Integer flag, Integer fisHide,
			Integer flevel, Date fpublisherTime, Integer fRank) {
		super();
		this.fid = fid;
		this.fnumber = fnumber;
		this.fheadline = fheadline;
		this.fimages = fimages;
		this.fmessage = fmessage;
		this.fcontacts = fcontacts;
		this.ftel = ftel;
		this.fstartTime = fstartTime;
		this.ffinishTime = ffinishTime;
		this.fbillState = fbillState;
		this.fcomment = fcomment;
		this.flag = flag;
		this.fisHide = fisHide;
		this.flevel = flevel;
		this.fpublisherTime = fpublisherTime;
		this.fRank = fRank;
	}
	// Constructors
	@Column(name = "FRank")
	public Integer getfRank() {
		return fRank;
	}

	public void setfRank(Integer fRank) {
		this.fRank = fRank;
	}

	/** default constructor */
	public YBasicSocialgroupsdemand() {
	}

	/** minimal constructor */
	public YBasicSocialgroupsdemand(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicSocialgroupsdemand(String fid, YBasicMember YBasicMember,
			YBasicCity YBasicCity, YBasicTrade YBasicTrade,
			YBasicCounty YBasicCounty, YBasicProvince YBasicProvince,
			YBasicSocialgroups YBasicSocialgroups, String fnumber,
			String fheadline, String fimages, String fmessage,
			String fcontacts, String ftel, Date fstartTime, Date ffinishTime,
			Integer fbillState, String fcomment, Integer flag, Integer fisHide,
			Integer flevel) {
		this.fid = fid;
		this.YBasicMember = YBasicMember;
		this.YBasicCity = YBasicCity;
		this.YBasicTrade = YBasicTrade;
		this.YBasicCounty = YBasicCounty;
		this.YBasicProvince = YBasicProvince;
		this.YBasicSocialgroups = YBasicSocialgroups;
		this.fnumber = fnumber;
		this.fheadline = fheadline;
		this.fimages = fimages;
		this.fmessage = fmessage;
		this.fcontacts = fcontacts;
		this.ftel = ftel;
		this.fstartTime = fstartTime;
		this.ffinishTime = ffinishTime;
		this.fbillState = fbillState;
		this.fcomment = fcomment;
		this.flag = flag;
		this.fisHide = fisHide;
		this.flevel = flevel;
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
	@JoinColumn(name = "FCityID")
	public YBasicCity getYBasicCity() {
		return this.YBasicCity;
	}

	public void setYBasicCity(YBasicCity YBasicCity) {
		this.YBasicCity = YBasicCity;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FTradeID")
	public YBasicTrade getYBasicTrade() {
		return this.YBasicTrade;
	}

	public void setYBasicTrade(YBasicTrade YBasicTrade) {
		this.YBasicTrade = YBasicTrade;
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

	@Column(name = "FImages", length = 65535)
	public String getFimages() {
		return this.fimages;
	}

	public void setFimages(String fimages) {
		this.fimages = fimages;
	}

	@Column(name = "FMessage", length = 65535)
	public String getFmessage() {
		return this.fmessage;
	}

	public void setFmessage(String fmessage) {
		this.fmessage = fmessage;
	}

	@Column(name = "FContacts", length = 48)
	public String getFcontacts() {
		return this.fcontacts;
	}

	public void setFcontacts(String fcontacts) {
		this.fcontacts = fcontacts;
	}

	@Column(name = "FTel", length = 50)
	public String getFtel() {
		return this.ftel;
	}

	public void setFtel(String ftel) {
		this.ftel = ftel;
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

	@Column(name = "FIsHide")
	public Integer getFisHide() {
		return this.fisHide;
	}

	public void setFisHide(Integer fisHide) {
		this.fisHide = fisHide;
	}

	@Column(name = "Flevel")
	public Integer getFlevel() {
		return this.flevel;
	}

	public void setFlevel(Integer flevel) {
		this.flevel = flevel;
	}
	@Column(name = "FPublisherTime")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFpublisherTime() {
		return fpublisherTime;
	}

	public void setFpublisherTime(Date fpublisherTime) {
		this.fpublisherTime = fpublisherTime;
	}

}