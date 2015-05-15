package com.yunzo.cocmore.core.function.model.mysql;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

/**
 * @author：jackpeng
 * @date：2015年3月27日下午4:31:41
 * 预约推送信息记录
 */
@Entity
@Table(name = "y_basic_shoping_pushinfo" )
public class YBasicShopingPushinfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String fid;//FID	varchar	36	0	0	0	0	0	0		0		utf8	utf8_general_ci		-1	0
	private String ftel;//FTel	varchar	100	0	-1	0	0	0	0		0		utf8	utf8_general_ci		0	0
	private Integer fstatu;//FStatu	int	11	0	-1	0	0	0	0		0					0	0
	private Date fupdateTime;//FUpdatetime	datetime	0	0	-1	0	0	0	0		0					0	0
	private String fpushTitle;//FPushtitle	varchar	1000	0	-1	0	0	0	0		0		utf8	utf8_general_ci		0	0
	private String fremark;//FRemark	varchar	200	0	-1	0	0	0	0		0		utf8	utf8_general_ci		0	0
	private String fshopingId;//FShopingId	varchar	36	0	-1	0	0	0	0		0		utf8	utf8_general_ci		0	0
	private int ftype;//FType	int	11	0	-1	0	0	0	0		0					0	0
	
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
	@Column(name = "FShopingId")
	public String getFshopingId() {
		return fshopingId;
	}
	public void setFshopingId(String fshopingId) {
		this.fshopingId = fshopingId;
	}
	@Column(name = "FType")
	public int getFtype() {
		return ftype;
	}
	public void setFtype(int ftype) {
		this.ftype = ftype;
	}
}
