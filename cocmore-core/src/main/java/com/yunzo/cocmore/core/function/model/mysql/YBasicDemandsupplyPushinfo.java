package com.yunzo.cocmore.core.function.model.mysql;

import java.util.Date;
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
 * @date：2015年3月18日上午10:57:57
 * 供需推送信息记录
 */
@Entity
@Table(name = "y_basic_demandsupply_pushinfo" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicDemandsupplyPushinfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String fid;//主键	fid	varchar(36)	36		TRUE	FALSE	TRUE
	private String ftel;//用户电话号码	tel	varchar(36)	36		FALSE	FALSE	FALSE
	private Integer fstatu;//状态	statu	int			FALSE	FALSE	FALSE
	private Date fupdateTime;//修改时间	updatetime	datetime			FALSE	FALSE	FALSE
	private String fpushTitle;//推送标题	pushtitle	varchar(1000)	1000		FALSE	FALSE	FALSE
	private String fremark;//备注	remark	varchar(200)	200		FALSE	FALSE	FALSE
	private String fdemandsupplyId;//供应需求id	demandsupplyid	varchar(36)	36		FALSE	FALSE	FALSE
	private Integer ftype;//类型	ftype	int			FALSE	FALSE	FALSE
	
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
	@Column(name = "FTel")
	public String getFtel() {
		return ftel;
	}
	public void setFtel(String ftel) {
		this.ftel = ftel;
	}
	@Column(name = "FStatu")
	public Integer getFstatu() {
		return fstatu;
	}
	public void setFstatu(Integer fstatu) {
		this.fstatu = fstatu;
	}
	@Column(name = "FUpdatetime")
	public Date getFupdateTime() {
		return fupdateTime;
	}
	public void setFupdateTime(Date fupdateTime) {
		this.fupdateTime = fupdateTime;
	}
	@Column(name = "FPushtitle")
	public String getFpushTitle() {
		return fpushTitle;
	}
	public void setFpushTitle(String fpushTitle) {
		this.fpushTitle = fpushTitle;
	}
	@Column(name = "FRemark")
	public String getFremark() {
		return fremark;
	}
	public void setFremark(String fremark) {
		this.fremark = fremark;
	}
	@Column(name = "FDemandsupplyid")
	public String getFdemandsupplyId() {
		return fdemandsupplyId;
	}
	public void setFdemandsupplyId(String fdemandsupplyId) {
		this.fdemandsupplyId = fdemandsupplyId;
	}
	@Column(name = "FType")
	public Integer getFtype() {
		return ftype;
	}
	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}
}
