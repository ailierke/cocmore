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
 * YBasicGroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_group" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicGroup implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private String fgroupHeadImage;
	private Integer fgroupMaxPeople;
	private String fgroupName;
	private Integer flag;
	private String businessId;
	private Set<YBasicGrouppeople> YBasicGrouppeoples = new HashSet<YBasicGrouppeople>(
			0);

	/** default constructor */
	public YBasicGroup() {
	}

	/** minimal constructor */
	public YBasicGroup(String fid) {
		this.fid = fid;
	}

	public YBasicGroup(String fid, String fgroupHeadImage,
			Integer fgroupMaxPeople, String fgroupName, Integer flag,
			String businessId, Set<YBasicGrouppeople> yBasicGrouppeoples) {
		super();
		this.fid = fid;
		this.fgroupHeadImage = fgroupHeadImage;
		this.fgroupMaxPeople = fgroupMaxPeople;
		this.fgroupName = fgroupName;
		this.flag = flag;
		this.businessId = businessId;
		YBasicGrouppeoples = yBasicGrouppeoples;
	}

	// Constructors

	/** full constructor */
	public YBasicGroup(String fid, String fgroupHeadImage,
			Integer fgroupMaxPeople, String fgroupName,
			Set<YBasicGrouppeople> YBasicGrouppeoples) {
		this.fid = fid;
		this.fgroupHeadImage = fgroupHeadImage;
		this.fgroupMaxPeople = fgroupMaxPeople;
		this.fgroupName = fgroupName;
		this.YBasicGrouppeoples = YBasicGrouppeoples;
	}

	public String getBusinessId() {
		return businessId;
	}

	@Column(name = "FGroupHeadImage", length = 200)
	public String getFgroupHeadImage() {
		return this.fgroupHeadImage;
	}

	@Column(name = "FGroupMaxPeople")
	public Integer getFgroupMaxPeople() {
		return this.fgroupMaxPeople;
	}

	@Column(name = "FGroupName", length = 100)
	public String getFgroupName() {
		return this.fgroupName;
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
		return this.flag;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "YBasicGroup")
	@JsonIgnore
	public Set<YBasicGrouppeople> getYBasicGrouppeoples() {
		return this.YBasicGrouppeoples;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public void setFgroupHeadImage(String fgroupHeadImage) {
		this.fgroupHeadImage = fgroupHeadImage;
	}

	public void setFgroupMaxPeople(Integer fgroupMaxPeople) {
		this.fgroupMaxPeople = fgroupMaxPeople;
	}

	public void setFgroupName(String fgroupName) {
		this.fgroupName = fgroupName;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}
	
	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public void setYBasicGrouppeoples(Set<YBasicGrouppeople> YBasicGrouppeoples) {
		this.YBasicGrouppeoples = YBasicGrouppeoples;
	}

}