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
 * YBasicSocialgroupssupply entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_supply_group" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YSupplyGroup {
	private String fid;
	private YBasicSocialgroupssupply YBasicSocialgroupssupply ;
	private YBasicAssurancecontent YBasicAssurancecontent;
	private String ispass;
	private String groupid;
	//update by ailierke
	private Date updatetime;
	private String fisHide;
	
	public YSupplyGroup() {
		super();
		// TODO Auto-generated constructor stub
	}
	public YSupplyGroup(String fid) {
		super();
		this.fid = fid;
	}
	public YSupplyGroup(
			String fid,
			com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupssupply yBasicSocialgroupssupply,
			com.yunzo.cocmore.core.function.model.mysql.YBasicAssurancecontent yBasicAssurancecontent,
			String ispass) {
		super();
		this.fid = fid;
		YBasicSocialgroupssupply = yBasicSocialgroupssupply;
		YBasicAssurancecontent = yBasicAssurancecontent;
		this.ispass = ispass;
	}
	public YSupplyGroup(
			String fid,
			com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupssupply yBasicSocialgroupssupply,
			com.yunzo.cocmore.core.function.model.mysql.YBasicAssurancecontent yBasicAssurancecontent,
			String ispass, String groupid) {
		super();
		this.fid = fid;
		YBasicSocialgroupssupply = yBasicSocialgroupssupply;
		YBasicAssurancecontent = yBasicAssurancecontent;
		this.ispass = ispass;
		this.groupid = groupid;
	}
	public YSupplyGroup(
			String fid,
			com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupssupply yBasicSocialgroupssupply,
			com.yunzo.cocmore.core.function.model.mysql.YBasicAssurancecontent yBasicAssurancecontent,
			String ispass, String groupid, Date updatetime) {
		super();
		this.fid = fid;
		YBasicSocialgroupssupply = yBasicSocialgroupssupply;
		YBasicAssurancecontent = yBasicAssurancecontent;
		this.ispass = ispass;
		this.groupid = groupid;
		this.updatetime = updatetime;
	}
	@Id
	@Column(name = "fid", unique = true, nullable = false, length = 36)
	public String getFid() {
		if (!StringUtils.isBlank(this.fid)) {
			return this.fid;
		}
		return UUID.randomUUID().toString();
	}
	public String getGroupid() {
		return groupid;
	}
	public String getIspass() {
		return ispass;
	}
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getUpdatetime() {
		return updatetime;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "assuranceId")
	public YBasicAssurancecontent getYBasicAssurancecontent() {
		return YBasicAssurancecontent;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "supplyId")
	public YBasicSocialgroupssupply getYBasicSocialgroupssupply() {
		return YBasicSocialgroupssupply;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public void setIspass(String ispass) {
		this.ispass = ispass;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public void setYBasicAssurancecontent(
			YBasicAssurancecontent yBasicAssurancecontent) {
		YBasicAssurancecontent = yBasicAssurancecontent;
	}
	public void setYBasicSocialgroupssupply(
			YBasicSocialgroupssupply yBasicSocialgroupssupply) {
		YBasicSocialgroupssupply = yBasicSocialgroupssupply;
	}
	@Column(name = "FisHide")
	public String getFisHide() {
		return fisHide;
	}
	public void setFisHide(String fisHide) {
		this.fisHide = fisHide;
	}
	
	
}
