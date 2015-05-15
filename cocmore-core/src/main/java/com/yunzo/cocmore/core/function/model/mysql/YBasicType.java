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
 * YBasicType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_type" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicType implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private String fname;
	private String fmoduleId;
	private String fynexhibition;
	private String fcomment;
	private Integer flag;
	private Set<YComment> YComments = new HashSet<YComment>(0);
	private Set<YPointlike> YPointlikes = new HashSet<YPointlike>(0);
	private Set<YBasicSocialgroupsdynamic> YBasicSocialgroupsdynamics = new HashSet<YBasicSocialgroupsdynamic>(
			0);
	private Set<YBasicSocialgroupsinform> YBasicSocialgroupsinforms = new HashSet<YBasicSocialgroupsinform>(
			0);
	private Set<YBasicSocialgroupsactivity> YBasicSocialgroupsactivities = new HashSet<YBasicSocialgroupsactivity>(
			0);
	private Set<YBasicSocialgroups> YBasicSocialgroupses = new HashSet<YBasicSocialgroups>(
			0);

	// Constructors

	/** default constructor */
	public YBasicType() {
	}

	/** minimal constructor */
	public YBasicType(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicType(String fid, String fname, String fmoduleId,
			String fynexhibition, String fcomment, Set<YComment> YComments,
			Set<YPointlike> YPointlikes,
			Set<YBasicSocialgroupsdynamic> YBasicSocialgroupsdynamics,
			Set<YBasicSocialgroupsinform> YBasicSocialgroupsinforms,
			Set<YBasicSocialgroupsactivity> YBasicSocialgroupsactivities,
			Set<YBasicSocialgroups> YBasicSocialgroupses) {
		this.fid = fid;
		this.fname = fname;
		this.fmoduleId = fmoduleId;
		this.fynexhibition = fynexhibition;
		this.fcomment = fcomment;
		this.YComments = YComments;
		this.YPointlikes = YPointlikes;
		this.YBasicSocialgroupsdynamics = YBasicSocialgroupsdynamics;
		this.YBasicSocialgroupsinforms = YBasicSocialgroupsinforms;
		this.YBasicSocialgroupsactivities = YBasicSocialgroupsactivities;
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

	@Column(name = "FName", length = 100)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "FModuleID", length = 100)
	public String getFmoduleId() {
		return this.fmoduleId;
	}

	public void setFmoduleId(String fmoduleId) {
		this.fmoduleId = fmoduleId;
	}

	@Column(name = "FYNExhibition", length = 1)
	public String getFynexhibition() {
		return this.fynexhibition;
	}

	public void setFynexhibition(String fynexhibition) {
		this.fynexhibition = fynexhibition;
	}

	@Column(name = "FComment")
	public String getFcomment() {
		return this.fcomment;
	}

	public void setFcomment(String fcomment) {
		this.fcomment = fcomment;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicType")
	@JsonIgnore
	public Set<YComment> getYComments() {
		return this.YComments;
	}

	public void setYComments(Set<YComment> YComments) {
		this.YComments = YComments;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicType")
	@JsonIgnore
	public Set<YPointlike> getYPointlikes() {
		return this.YPointlikes;
	}

	public void setYPointlikes(Set<YPointlike> YPointlikes) {
		this.YPointlikes = YPointlikes;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicType")
	@JsonIgnore
	public Set<YBasicSocialgroupsdynamic> getYBasicSocialgroupsdynamics() {
		return this.YBasicSocialgroupsdynamics;
	}

	public void setYBasicSocialgroupsdynamics(
			Set<YBasicSocialgroupsdynamic> YBasicSocialgroupsdynamics) {
		this.YBasicSocialgroupsdynamics = YBasicSocialgroupsdynamics;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicType")
	@JsonIgnore
	public Set<YBasicSocialgroupsinform> getYBasicSocialgroupsinforms() {
		return this.YBasicSocialgroupsinforms;
	}

	public void setYBasicSocialgroupsinforms(
			Set<YBasicSocialgroupsinform> YBasicSocialgroupsinforms) {
		this.YBasicSocialgroupsinforms = YBasicSocialgroupsinforms;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicType")
	@JsonIgnore
	public Set<YBasicSocialgroupsactivity> getYBasicSocialgroupsactivities() {
		return this.YBasicSocialgroupsactivities;
	}

	public void setYBasicSocialgroupsactivities(
			Set<YBasicSocialgroupsactivity> YBasicSocialgroupsactivities) {
		this.YBasicSocialgroupsactivities = YBasicSocialgroupsactivities;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicType")
	@JsonIgnore
	public Set<YBasicSocialgroups> getYBasicSocialgroupses() {
		return this.YBasicSocialgroupses;
	}

	public void setYBasicSocialgroupses(
			Set<YBasicSocialgroups> YBasicSocialgroupses) {
		this.YBasicSocialgroupses = YBasicSocialgroupses;
	}

	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}