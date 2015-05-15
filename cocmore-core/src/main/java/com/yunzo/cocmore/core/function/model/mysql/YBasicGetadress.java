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


@Entity
@Table(name = "y_basic_getadress" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicGetadress implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String fid;
	private YBasicMember YBasicMember;
	private String fadress;
	private Date fupdatetime;
	private String fremark;
	private Integer fqueue;
	private String fisDefault;
	private YBasicProvince YBasicProvince;
	private YBasicCity YBasicCity;
	private YBasicCounty YBasicCounty;
	private String fharvestingName;
	private String fharvestPhone;
	
	
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
	@JoinColumn(name = "fmemberid")
	public YBasicMember getYBasicMember() {
		return YBasicMember;
	}
	public void setYBasicMember(YBasicMember yBasicMember) {
		YBasicMember = yBasicMember;
	}
	@Column(name = "fadress", length = 36)
	public String getFadress() {
		return fadress;
	}
	public void setFadress(String fadress) {
		this.fadress = fadress;
	}
	@Column(name = "fupdatetime", length = 36)
	public Date getFupdatetime() {
		return fupdatetime;
	}
	public void setFupdatetime(Date fupdatetime) {
		this.fupdatetime = fupdatetime;
	}
	
	@Column(name = "fremark", length = 36)
	public String getFremark() {
		return fremark;
	}
	public void setFremark(String fremark) {
		this.fremark = fremark;
	}
	
	@Column(name = "fqueue", length = 36)
	public Integer getFqueue() {
		return fqueue;
	}
	public void setFqueue(Integer fqueue) {
		this.fqueue = fqueue;
	}
	
	@Column(name = "fisDefault", length = 36)
	public String getFisDefault() {
		return fisDefault;
	}
	public void setFisDefault(String fisDefault) {
		this.fisDefault = fisDefault;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fprovincialId")
	public YBasicProvince getYBasicProvince() {
		return YBasicProvince;
	}
	public void setYBasicProvince(YBasicProvince yBasicProvince) {
		YBasicProvince = yBasicProvince;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fcityId")
	public YBasicCity getYBasicCity() {
		return YBasicCity;
	}
	public void setYBasicCity(YBasicCity yBasicCity) {
		YBasicCity = yBasicCity;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fcountryId")
	public YBasicCounty getYBasicCounty() {
		return YBasicCounty;
	}
	public void setYBasicCounty(YBasicCounty yBasicCounty) {
		YBasicCounty = yBasicCounty;
	}
	
	@Column(name = "fharvestingName", length = 36)
	public String getFharvestingName() {
		return fharvestingName;
	}
	public void setFharvestingName(String fharvestingName) {
		this.fharvestingName = fharvestingName;
	}
	
	@Column(name = "fharvestPhone", length = 36)
	public String getFharvestPhone() {
		return fharvestPhone;
	}
	public void setFharvestPhone(String fharvestPhone) {
		this.fharvestPhone = fharvestPhone;
	}

	
	
}
