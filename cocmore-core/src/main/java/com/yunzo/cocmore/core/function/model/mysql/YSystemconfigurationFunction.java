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
 * YSystemconfigurationFunction entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_systemconfiguration_function" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YSystemconfigurationFunction implements java.io.Serializable {

	// Fields

	private String fid;
	private YSystemconfigurationMenu YSystemconfigurationMenu;
	private String fnumber;
	private String faccount;
	private String fremark;
	private Integer flag;
	private Set<YRoleauthorization> YRoleauthorizations = new HashSet<YRoleauthorization>(
			0);
	private Set<YFunctionentriesFunctionjournalentry> YFunctionentriesFunctionjournalentries = new HashSet<YFunctionentriesFunctionjournalentry>(
			0);
	private Set<YOrganizationauthorized> YOrganizationauthorizeds = new HashSet<YOrganizationauthorized>(
			0);

	// Constructors

	/** default constructor */
	public YSystemconfigurationFunction() {
	}

	/** minimal constructor */
	public YSystemconfigurationFunction(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YSystemconfigurationFunction(
			String fid,
			YSystemconfigurationMenu YSystemconfigurationMenu,
			String fnumber,
			String faccount,
			String fremark,
			Integer flag,
			Set<YRoleauthorization> YRoleauthorizations,
			Set<YOrganizationauthorized> YOrganizationauthorizeds,
			Set<YFunctionentriesFunctionjournalentry> YFunctionentriesFunctionjournalentries) {
		this.fid = fid;
		this.YSystemconfigurationMenu = YSystemconfigurationMenu;
		this.fnumber = fnumber;
		this.faccount = faccount;
		this.fremark = fremark;
		this.flag = flag;
		this.YRoleauthorizations = YRoleauthorizations;
		this.YFunctionentriesFunctionjournalentries = YFunctionentriesFunctionjournalentries;
		this.YOrganizationauthorizeds = YOrganizationauthorizeds;
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
	@JoinColumn(name = "FMenuID")
	public YSystemconfigurationMenu getYSystemconfigurationMenu() {
		return this.YSystemconfigurationMenu;
	}

	public void setYSystemconfigurationMenu(
			YSystemconfigurationMenu YSystemconfigurationMenu) {
		this.YSystemconfigurationMenu = YSystemconfigurationMenu;
	}

	@Column(name = "FNumber", length = 36)
	public String getFnumber() {
		return this.fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	@Column(name = "FAccount", length = 36)
	public String getFaccount() {
		return this.faccount;
	}

	public void setFaccount(String faccount) {
		this.faccount = faccount;
	}

	@Column(name = "FRemark", length = 300)
	public String getFremark() {
		return this.fremark;
	}

	public void setFremark(String fremark) {
		this.fremark = fremark;
	}

	@Column(name = "Flag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YSystemconfigurationFunction")
	@JsonIgnore
	public Set<YRoleauthorization> getYRoleauthorizations() {
		return this.YRoleauthorizations;
	}

	public void setYRoleauthorizations(
			Set<YRoleauthorization> YRoleauthorizations) {
		this.YRoleauthorizations = YRoleauthorizations;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YSystemconfigurationFunction")
	@JsonIgnore
	public Set<YFunctionentriesFunctionjournalentry> getYFunctionentriesFunctionjournalentries() {
		return this.YFunctionentriesFunctionjournalentries;
	}

	public void setYFunctionentriesFunctionjournalentries(
			Set<YFunctionentriesFunctionjournalentry> YFunctionentriesFunctionjournalentries) {
		this.YFunctionentriesFunctionjournalentries = YFunctionentriesFunctionjournalentries;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YFunctionentriesFunctionjournalentry")
	@JsonIgnore
	public Set<YOrganizationauthorized> getYOrganizationauthorizeds() {
		return this.YOrganizationauthorizeds;
	}

	public void setYOrganizationauthorizeds(
			Set<YOrganizationauthorized> YOrganizationauthorizeds) {
		this.YOrganizationauthorizeds = YOrganizationauthorizeds;
	}
}