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
 * YBasicProvince entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_province" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicProvince implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private YBasicDistrict YBasicDistrict;
	private YBasicNation YBasicNation;
	private String fnumber;
	private String fname;
	private String fabbreviation;
	private String fcomment;
	private Integer fbillState;
	private Integer flag;
	private Set<YBasicSocialgroupsdemand> YBasicSocialgroupsdemands = new HashSet<YBasicSocialgroupsdemand>(
			0);
	private Set<YBasicCity> YBasicCities = new HashSet<YBasicCity>(0);
	private Set<YWallactivity> YWallactivities = new HashSet<YWallactivity>(0);
	private Set<YBasicMember> YBasicMembers = new HashSet<YBasicMember>(0);
	private Set<YBasicSocialgroupssupply> YBasicSocialgroupssupplies = new HashSet<YBasicSocialgroupssupply>(
			0);
	private Set<YBasicSocialgroups> YBasicSocialgroupses = new HashSet<YBasicSocialgroups>(
			0);

	// Constructors

	/** default constructor */
	public YBasicProvince() {
	}

	/** minimal constructor */
	public YBasicProvince(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicProvince(String fid, YBasicDistrict YBasicDistrict,
			YBasicNation YBasicNation, String fnumber, String fname,
			String fabbreviation, String fcomment, Integer fbillState,
			Integer flag,
			Set<YBasicSocialgroupsdemand> YBasicSocialgroupsdemands,
			Set<YBasicCity> YBasicCities, Set<YWallactivity> YWallactivities,
			Set<YBasicMember> YBasicMembers,
			Set<YBasicSocialgroupssupply> YBasicSocialgroupssupplies,
			Set<YBasicSocialgroups> YBasicSocialgroupses) {
		this.fid = fid;
		this.YBasicDistrict = YBasicDistrict;
		this.YBasicNation = YBasicNation;
		this.fnumber = fnumber;
		this.fname = fname;
		this.fabbreviation = fabbreviation;
		this.fcomment = fcomment;
		this.fbillState = fbillState;
		this.flag = flag;
		this.YBasicSocialgroupsdemands = YBasicSocialgroupsdemands;
		this.YBasicCities = YBasicCities;
		this.YWallactivities = YWallactivities;
		this.YBasicMembers = YBasicMembers;
		this.YBasicSocialgroupssupplies = YBasicSocialgroupssupplies;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FDistrictID")
	public YBasicDistrict getYBasicDistrict() {
		return this.YBasicDistrict;
	}

	public void setYBasicDistrict(YBasicDistrict YBasicDistrict) {
		this.YBasicDistrict = YBasicDistrict;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FNationID")
	public YBasicNation getYBasicNation() {
		return this.YBasicNation;
	}

	public void setYBasicNation(YBasicNation YBasicNation) {
		this.YBasicNation = YBasicNation;
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

	@Column(name = "FAbbreviation", length = 4)
	public String getFabbreviation() {
		return this.fabbreviation;
	}

	public void setFabbreviation(String fabbreviation) {
		this.fabbreviation = fabbreviation;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicProvince")
	@JsonIgnore
	public Set<YBasicSocialgroupsdemand> getYBasicSocialgroupsdemands() {
		return this.YBasicSocialgroupsdemands;
	}

	public void setYBasicSocialgroupsdemands(
			Set<YBasicSocialgroupsdemand> YBasicSocialgroupsdemands) {
		this.YBasicSocialgroupsdemands = YBasicSocialgroupsdemands;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicProvince")
	@JsonIgnore
	public Set<YBasicCity> getYBasicCities() {
		return this.YBasicCities;
	}

	public void setYBasicCities(Set<YBasicCity> YBasicCities) {
		this.YBasicCities = YBasicCities;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicProvince")
	@JsonIgnore
	public Set<YWallactivity> getYWallactivities() {
		return this.YWallactivities;
	}

	public void setYWallactivities(Set<YWallactivity> YWallactivities) {
		this.YWallactivities = YWallactivities;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicProvince")
	@JsonIgnore
	public Set<YBasicMember> getYBasicMembers() {
		return this.YBasicMembers;
	}

	public void setYBasicMembers(Set<YBasicMember> YBasicMembers) {
		this.YBasicMembers = YBasicMembers;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicProvince")
	@JsonIgnore
	public Set<YBasicSocialgroupssupply> getYBasicSocialgroupssupplies() {
		return this.YBasicSocialgroupssupplies;
	}

	public void setYBasicSocialgroupssupplies(
			Set<YBasicSocialgroupssupply> YBasicSocialgroupssupplies) {
		this.YBasicSocialgroupssupplies = YBasicSocialgroupssupplies;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicProvince")
	@JsonIgnore
	public Set<YBasicSocialgroups> getYBasicSocialgroupses() {
		return this.YBasicSocialgroupses;
	}

	public void setYBasicSocialgroupses(
			Set<YBasicSocialgroups> YBasicSocialgroupses) {
		this.YBasicSocialgroupses = YBasicSocialgroupses;
	}

}