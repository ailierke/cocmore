package com.yunzo.cocmore.core.function.model.mysql;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author：jackpeng
 * @date：2014年12月15日下午3:07:35
 * 标签
 */
@Entity
@Table(name = "y_basic_label" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicLabel implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String fid;	//主键	VARCHAR	36	唯一约束、主键约束、索引
	private String fnumber;	//编号	VARCHAR	100	编号
	private String fterritorys;	//地域	TEXT		地域
	private String fprovincialId;//省份
	private String fcityId;//城市
	private String fcountryId;//区县
	private String ftrades;	//行业	TEXT		行业
	private String findustrys;	//产业	TEXT		产业
	private String fuserPhone;	//用户电话	VARCHAR	50	用户电话号码
	private Integer fbillState;	//状态	INT		状态（0、新增，1、保存，2、提交，3、审核，4、生效，5、失效，6、启用，7、禁用，8、作废，9、变更）
	private String fcomment;	//备注	VARCHAR	255	备注
	private Integer forderIndex;//排序号
	private Integer ftype;//类型（0地域，1行业）
	private Long flag;	//自动增长	INT		
	
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
		return fnumber;
	}
	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}
	@Column(name = "FTerritorys")
	public String getFterritorys() {
		return fterritorys;
	}
	public void setFterritorys(String fterritorys) {
		this.fterritorys = fterritorys;
	}
	@Column(name = "FProvincialId")
	public String getFprovincialId() {
		return fprovincialId;
	}
	public void setFprovincialId(String fprovincialId) {
		this.fprovincialId = fprovincialId;
	}
	@Column(name = "FCityId")
	public String getFcityId() {
		return fcityId;
	}
	public void setFcityId(String fcityId) {
		this.fcityId = fcityId;
	}
	@Column(name = "FCountryId")
	public String getFcountryId() {
		return fcountryId;
	}
	public void setFcountryId(String fcountryId) {
		this.fcountryId = fcountryId;
	}
	@Column(name = "FTrades")
	public String getFtrades() {
		return ftrades;
	}
	public void setFtrades(String ftrades) {
		this.ftrades = ftrades;
	}
	@Column(name = "FIndustrys")
	public String getFindustrys() {
		return findustrys;
	}
	public void setFindustrys(String findustrys) {
		this.findustrys = findustrys;
	}
	@Column(name = "FUserPhone", length = 50)
	public String getFuserPhone() {
		return fuserPhone;
	}
	public void setFuserPhone(String fuserPhone) {
		this.fuserPhone = fuserPhone;
	}
	@Column(name = "FBillState")
	public Integer getFbillState() {
		return fbillState;
	}
	public void setFbillState(Integer fbillState) {
		this.fbillState = fbillState;
	}
	@Column(name = "FComment", length = 255)
	public String getFcomment() {
		return fcomment;
	}
	public void setFcomment(String fcomment) {
		this.fcomment = fcomment;
	}
	@Column(name = "FOrderIndex")
	public Integer getForderIndex() {
		return forderIndex;
	}
	public void setForderIndex(Integer forderIndex) {
		this.forderIndex = forderIndex;
	}
	@Column(name = "FType")
	public Integer getFtype() {
		return ftype;
	}
	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}
	@Column(name = "FLag")
	public Long getFlag() {
		return flag;
	}
	public void setFlag(Long flag) {
		this.flag = flag;
	}
	
}
