package com.yunzo.cocmore.core.function.model.mysql;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * YBasicCity entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_city" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicCity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private YBasicProvince YBasicProvince;
	private String fnumber;
	private String fname;
	private String fcityAreaCode;
	private String fmunicipalities;
	private String fcomment;
	private Integer fbillState;
	private Integer flag;
	private Set<YBasicSocialgroups> YBasicSocialgroupses = new HashSet<YBasicSocialgroups>(
			0);
	private Set<YBasicSocialgroupssupply> YBasicSocialgroupssupplies = new HashSet<YBasicSocialgroupssupply>(
			0);
	private Set<YWallactivity> YWallactivities = new HashSet<YWallactivity>(0);
	private Set<YBasicMember> YBasicMembers = new HashSet<YBasicMember>(0);
	private Set<YBasicCounty> YBasicCounties = new HashSet<YBasicCounty>(0);
	private Set<YBasicSocialgroupsdemand> YBasicSocialgroupsdemands = new HashSet<YBasicSocialgroupsdemand>(
			0);

	// Constructors

	/** default constructor */
	public YBasicCity() {
	}

	/** minimal constructor */
	public YBasicCity(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicCity(String fid, YBasicProvince YBasicProvince,
			String fnumber, String fname, String fcityAreaCode,
			String fmunicipalities, String fcomment, Integer fbillState,
			Integer flag, Set<YBasicSocialgroups> YBasicSocialgroupses,
			Set<YBasicSocialgroupssupply> YBasicSocialgroupssupplies,
			Set<YWallactivity> YWallactivities,
			Set<YBasicMember> YBasicMembers, Set<YBasicCounty> YBasicCounties,
			Set<YBasicSocialgroupsdemand> YBasicSocialgroupsdemands) {
		this.fid = fid;
		this.YBasicProvince = YBasicProvince;
		this.fnumber = fnumber;
		this.fname = fname;
		this.fcityAreaCode = fcityAreaCode;
		this.fmunicipalities = fmunicipalities;
		this.fcomment = fcomment;
		this.fbillState = fbillState;
		this.flag = flag;
		this.YBasicSocialgroupses = YBasicSocialgroupses;
		this.YBasicSocialgroupssupplies = YBasicSocialgroupssupplies;
		this.YWallactivities = YWallactivities;
		this.YBasicMembers = YBasicMembers;
		this.YBasicCounties = YBasicCounties;
		this.YBasicSocialgroupsdemands = YBasicSocialgroupsdemands;
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
	@JoinColumn(name = "FProvinceID")
	public YBasicProvince getYBasicProvince() {
		return this.YBasicProvince;
	}

	public void setYBasicProvince(YBasicProvince YBasicProvince) {
		this.YBasicProvince = YBasicProvince;
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

	@Column(name = "FCityAreaCode", length = 36)
	public String getFcityAreaCode() {
		return this.fcityAreaCode;
	}

	public void setFcityAreaCode(String fcityAreaCode) {
		this.fcityAreaCode = fcityAreaCode;
	}

	@Column(name = "Fmunicipalities", length = 1)
	public String getFmunicipalities() {
		return this.fmunicipalities;
	}

	public void setFmunicipalities(String fmunicipalities) {
		this.fmunicipalities = fmunicipalities;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicCity")
	@JsonIgnore
	public Set<YBasicSocialgroups> getYBasicSocialgroupses() {
		return this.YBasicSocialgroupses;
	}

	public void setYBasicSocialgroupses(
			Set<YBasicSocialgroups> YBasicSocialgroupses) {
		this.YBasicSocialgroupses = YBasicSocialgroupses;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicCity")
	@JsonIgnore
	public Set<YBasicSocialgroupssupply> getYBasicSocialgroupssupplies() {
		return this.YBasicSocialgroupssupplies;
	}

	public void setYBasicSocialgroupssupplies(
			Set<YBasicSocialgroupssupply> YBasicSocialgroupssupplies) {
		this.YBasicSocialgroupssupplies = YBasicSocialgroupssupplies;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicCity")
	@JsonIgnore
	public Set<YWallactivity> getYWallactivities() {
		return this.YWallactivities;
	}

	public void setYWallactivities(Set<YWallactivity> YWallactivities) {
		this.YWallactivities = YWallactivities;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicCity")
	@JsonIgnore
	public Set<YBasicMember> getYBasicMembers() {
		return this.YBasicMembers;
	}

	public void setYBasicMembers(Set<YBasicMember> YBasicMembers) {
		this.YBasicMembers = YBasicMembers;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicCity")
	@JsonIgnore
	public Set<YBasicCounty> getYBasicCounties() {
		return this.YBasicCounties;
	}

	public void setYBasicCounties(Set<YBasicCounty> YBasicCounties) {
		this.YBasicCounties = YBasicCounties;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicCity")
	@JsonIgnore
	public Set<YBasicSocialgroupsdemand> getYBasicSocialgroupsdemands() {
		return this.YBasicSocialgroupsdemands;
	}

	public void setYBasicSocialgroupsdemands(
			Set<YBasicSocialgroupsdemand> YBasicSocialgroupsdemands) {
		this.YBasicSocialgroupsdemands = YBasicSocialgroupsdemands;
	}

}