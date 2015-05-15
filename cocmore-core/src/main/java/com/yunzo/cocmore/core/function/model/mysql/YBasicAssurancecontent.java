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
 * YBasicAssurancecontent entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_assurancecontent" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicAssurancecontent implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private YBasicSocialgroups YBasicSocialgroups;
	private String fcontent;
	private Integer flag;
	private Set<YSupplyGroup> ySupplyGroups = new HashSet<YSupplyGroup>(0);
	// Constructors

	/** default constructor */
	public YBasicAssurancecontent() {
	}

	/** minimal constructor */
	public YBasicAssurancecontent(String fid) {
		this.fid = fid;
	}

	public YBasicAssurancecontent(
			String fid,
			com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups yBasicSocialgroups,
			String fcontent, Integer flag, Set<YSupplyGroup> ySupplyGroups) {
		super();
		this.fid = fid;
		YBasicSocialgroups = yBasicSocialgroups;
		this.fcontent = fcontent;
		this.flag = flag;
		this.ySupplyGroups = ySupplyGroups;
	}
	@Column(name = "FContent", length = 65535)
	public String getFcontent() {
		return this.fcontent;
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

	@Column(name = "FLag")
	public Integer getFlag() {
		return flag;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FSocialGroupsID")
	public YBasicSocialgroups getYBasicSocialgroups() {
		return this.YBasicSocialgroups;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicSocialgroupssupply")
	@JsonIgnore
	public Set<YSupplyGroup> getySupplyGroups() {
		return ySupplyGroups;
	}

	public void setFcontent(String fcontent) {
		this.fcontent = fcontent;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}


	public void setYBasicSocialgroups(YBasicSocialgroups YBasicSocialgroups) {
		this.YBasicSocialgroups = YBasicSocialgroups;
	}

	public void setySupplyGroups(Set<YSupplyGroup> ySupplyGroups) {
		this.ySupplyGroups = ySupplyGroups;
	}
}