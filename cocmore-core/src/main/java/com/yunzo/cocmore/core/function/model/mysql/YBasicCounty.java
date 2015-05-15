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
 * YBasicCounty entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_county" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicCounty implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private YBasicCity YBasicCity;
	private String fnumber;
	private String fname;
	private String fcomment;
	private Integer fbillState;
	private Integer flag;
	private Set<YBasicSocialgroupssupply> YBasicSocialgroupssupplies = new HashSet<YBasicSocialgroupssupply>(
			0);
	private Set<YBasicSocialgroupsdemand> YBasicSocialgroupsdemands = new HashSet<YBasicSocialgroupsdemand>(
			0);
	private Set<YBasicSocialgroups> YBasicSocialgroupses = new HashSet<YBasicSocialgroups>(
			0);
	private Set<YBasicMember> YBasicMembers = new HashSet<YBasicMember>(0);

	// Constructors

	/** default constructor */
	public YBasicCounty() {
	}

	/** minimal constructor */
	public YBasicCounty(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicCounty(String fid, YBasicCity YBasicCity, String fnumber,
			String fname, String fcomment, Integer fbillState, Integer flag,
			Set<YBasicSocialgroupssupply> YBasicSocialgroupssupplies,
			Set<YBasicSocialgroupsdemand> YBasicSocialgroupsdemands,
			Set<YBasicSocialgroups> YBasicSocialgroupses,
			Set<YBasicMember> YBasicMembers) {
		this.fid = fid;
		this.YBasicCity = YBasicCity;
		this.fnumber = fnumber;
		this.fname = fname;
		this.fcomment = fcomment;
		this.fbillState = fbillState;
		this.flag = flag;
		this.YBasicSocialgroupssupplies = YBasicSocialgroupssupplies;
		this.YBasicSocialgroupsdemands = YBasicSocialgroupsdemands;
		this.YBasicSocialgroupses = YBasicSocialgroupses;
		this.YBasicMembers = YBasicMembers;
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
	@JoinColumn(name = "FCityID")
	public YBasicCity getYBasicCity() {
		return this.YBasicCity;
	}

	public void setYBasicCity(YBasicCity YBasicCity) {
		this.YBasicCity = YBasicCity;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicCounty")
	@JsonIgnore
	public Set<YBasicSocialgroupssupply> getYBasicSocialgroupssupplies() {
		return this.YBasicSocialgroupssupplies;
	}

	public void setYBasicSocialgroupssupplies(
			Set<YBasicSocialgroupssupply> YBasicSocialgroupssupplies) {
		this.YBasicSocialgroupssupplies = YBasicSocialgroupssupplies;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicCounty")
	@JsonIgnore
	public Set<YBasicSocialgroupsdemand> getYBasicSocialgroupsdemands() {
		return this.YBasicSocialgroupsdemands;
	}

	public void setYBasicSocialgroupsdemands(
			Set<YBasicSocialgroupsdemand> YBasicSocialgroupsdemands) {
		this.YBasicSocialgroupsdemands = YBasicSocialgroupsdemands;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicCounty")
	@JsonIgnore
	public Set<YBasicSocialgroups> getYBasicSocialgroupses() {
		return this.YBasicSocialgroupses;
	}

	public void setYBasicSocialgroupses(
			Set<YBasicSocialgroups> YBasicSocialgroupses) {
		this.YBasicSocialgroupses = YBasicSocialgroupses;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicCounty")
	@JsonIgnore
	public Set<YBasicMember> getYBasicMembers() {
		return this.YBasicMembers;
	}

	public void setYBasicMembers(Set<YBasicMember> YBasicMembers) {
		this.YBasicMembers = YBasicMembers;
	}

}