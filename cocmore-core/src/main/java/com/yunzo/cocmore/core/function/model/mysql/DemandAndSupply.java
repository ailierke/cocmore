package com.yunzo.cocmore.core.function.model.mysql;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author：jackpeng
 * @date：2015年2月2日上午10:55:43
 * 供需视图实体
 */
@Entity
@Table(name = "demandandsupply" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class DemandAndSupply implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String fid;//供需id
	private String fheadline;//供需标题
	private String fimages;//供需图片
	private String fmessage;//供需内容
	private String fpublisherID;//发布人id
	private String fsocialGroupsID;//所属团体
	private String fprovinceID;//省份id
	private String fcityID;//城市id
	private String fcountyID;//区县id
	private String ftradeID;//行业id
	private Date fpublisherTime;//发布时间
	private Integer fareGuarantee;//是否担保
	private Integer ftype;//类型（0：供应，1：需求）
	private Integer flevel;//等级
	private Integer fbillState;//状态
	private String fmobilePhone;//状态
	private Integer fRank;
	
	public DemandAndSupply(String fid, String fheadline, String fimages,
			String fmessage, String fpublisherID, String fsocialGroupsID,
			String fprovinceID, String fcityID, String fcountyID,
			String ftradeID, Date fpublisherTime, Integer fareGuarantee,
			Integer ftype, Integer flevel, Integer fbillState,
			String fmobilePhone, Integer fRank, long flag) {
		super();
		this.fid = fid;
		this.fheadline = fheadline;
		this.fimages = fimages;
		this.fmessage = fmessage;
		this.fpublisherID = fpublisherID;
		this.fsocialGroupsID = fsocialGroupsID;
		this.fprovinceID = fprovinceID;
		this.fcityID = fcityID;
		this.fcountyID = fcountyID;
		this.ftradeID = ftradeID;
		this.fpublisherTime = fpublisherTime;
		this.fareGuarantee = fareGuarantee;
		this.ftype = ftype;
		this.flevel = flevel;
		this.fbillState = fbillState;
		this.fmobilePhone = fmobilePhone;
		this.fRank = fRank;
		this.flag = flag;
	}
	public DemandAndSupply() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Column(name = "FRank")
	public Integer getfRank() {
		return fRank;
	}
	public void setfRank(Integer fRank) {
		this.fRank = fRank;
	}
	@Column(name = "FMobilePhone")
	public String getFmobilePhone() {
		return fmobilePhone;
	}
	public void setFmobilePhone(String fmobilePhone) {
		this.fmobilePhone = fmobilePhone;
	}
	private long flag;//自动增长
	
	@Id
	@Column(name = "FID", unique = true, nullable = false, length = 36)
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	@Column(name = "FHeadline")
	public String getFheadline() {
		return fheadline;
	}
	public void setFheadline(String fheadline) {
		this.fheadline = fheadline;
	}
	@Column(name = "FImages")
	public String getFimages() {
		return fimages;
	}
	public void setFimages(String fimages) {
		this.fimages = fimages;
	}
	@Column(name = "FMessage")
	public String getFmessage() {
		return fmessage;
	}
	public void setFmessage(String fmessage) {
		this.fmessage = fmessage;
	}
	@Column(name = "FPublisherID")
	public String getFpublisherID() {
		return fpublisherID;
	}
	public void setFpublisherID(String fpublisherID) {
		this.fpublisherID = fpublisherID;
	}
	@Column(name = "FSocialGroupsID")
	public String getFsocialGroupsID() {
		return fsocialGroupsID;
	}
	public void setFsocialGroupsID(String fsocialGroupsID) {
		this.fsocialGroupsID = fsocialGroupsID;
	}
	@Column(name = "FProvinceID")
	public String getFprovinceID() {
		return fprovinceID;
	}
	public void setFprovinceID(String fprovinceID) {
		this.fprovinceID = fprovinceID;
	}
	@Column(name = "FCityID")
	public String getFcityID() {
		return fcityID;
	}
	public void setFcityID(String fcityID) {
		this.fcityID = fcityID;
	}
	@Column(name = "FCountyID")
	public String getFcountyID() {
		return fcountyID;
	}
	public void setFcountyID(String fcountyID) {
		this.fcountyID = fcountyID;
	}
	@Column(name = "FTradeID")
	public String getFtradeID() {
		return ftradeID;
	}
	public void setFtradeID(String ftradeID) {
		this.ftradeID = ftradeID;
	}
	@Column(name = "FPublisherTime")
	public Date getFpublisherTime() {
		return fpublisherTime;
	}
	public void setFpublisherTime(Date fpublisherTime) {
		this.fpublisherTime = fpublisherTime;
	}
	@Column(name = "FAreGuarantee")
	public Integer getFareGuarantee() {
		return fareGuarantee;
	}
	public void setFareGuarantee(Integer fareGuarantee) {
		this.fareGuarantee = fareGuarantee;
	}
	@Column(name = "FType")
	public Integer getFtype() {
		return ftype;
	}
	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}
	@Column(name = "Flevel")
	public Integer getFlevel() {
		return flevel;
	}
	public void setFlevel(Integer flevel) {
		this.flevel = flevel;
	}
	@Column(name = "FBillState")
	public Integer getFbillState() {
		return fbillState;
	}
	public void setFbillState(Integer fbillState) {
		this.fbillState = fbillState;
	}
	@Column(name = "FLag")
	public long getFlag() {
		return flag;
	}
	public void setFlag(long flag) {
		this.flag = flag;
	}
}
