package com.yunzo.cocmore.core.function.model.mysql;

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

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * YBasicDistrict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_district" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicDistrict implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private String fnumber;
	private String fname;
	private String fnationId;
	private String fcomment;
	private Integer fbillState;
	private Integer flag;
	private Set<YBasicProvince> YBasicProvinces = new HashSet<YBasicProvince>(0);
	private Set<YBasicSocialgroups> YBasicSocialgroupses = new HashSet<YBasicSocialgroups>(
			0);

	// Constructors

	/** default constructor */
	public YBasicDistrict() {
	}

	/** minimal constructor */
	public YBasicDistrict(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicDistrict(String fid, String fnumber, String fname,
			String fnationId, String fcomment, Integer fbillState,
			Integer flag, Set<YBasicProvince> YBasicProvinces,
			Set<YBasicSocialgroups> YBasicSocialgroupses) {
		this.fid = fid;
		this.fnumber = fnumber;
		this.fname = fname;
		this.fnationId = fnationId;
		this.fcomment = fcomment;
		this.fbillState = fbillState;
		this.flag = flag;
		this.YBasicProvinces = YBasicProvinces;
		this.YBasicSocialgroupses = YBasicSocialgroupses;
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

	@Column(name = "FNationID", length = 36)
	public String getFnationId() {
		return this.fnationId;
	}

	public void setFnationId(String fnationId) {
		this.fnationId = fnationId;
	}

	@Column(name = "FComment")
	public String getFcomment() {
		return this.fcomment;
	}

	public void setFcomment(String fcomment) {
		this.fcomment = fcomment;
	}

	@Column(name = "FBillState")
	public Integer getFbillState() {
		return this.fbillState;
	}

	public void setFbillState(Integer fbillState) {
		this.fbillState = fbillState;
	}

	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicDistrict")
	@JsonIgnore
	public Set<YBasicProvince> getYBasicProvinces() {
		return this.YBasicProvinces;
	}

	public void setYBasicProvinces(Set<YBasicProvince> YBasicProvinces) {
		this.YBasicProvinces = YBasicProvinces;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicDistrict")
	@JsonIgnore
	public Set<YBasicSocialgroups> getYBasicSocialgroupses() {
		return this.YBasicSocialgroupses;
	}

	public void setYBasicSocialgroupses(
			Set<YBasicSocialgroups> YBasicSocialgroupses) {
		this.YBasicSocialgroupses = YBasicSocialgroupses;
	}

}