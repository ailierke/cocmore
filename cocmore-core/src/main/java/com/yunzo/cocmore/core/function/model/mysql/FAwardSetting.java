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
 * FAwardSetting entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "f_award_setting" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class FAwardSetting implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private YWallactivity YWallactivity;
	private String fawardName;
	private String fprizeName;
	private Integer fawardOrder;
	private Integer frandom;
	private Integer fawardsPerson;
	private String fdesignatedPerson;
	private Integer flag;
	private Set<FAwardsRecord> FAwardsRecords = new HashSet<FAwardsRecord>(0);

	// Constructors

	/** default constructor */
	public FAwardSetting() {
	}

	/** minimal constructor */
	public FAwardSetting(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public FAwardSetting(String fid, YWallactivity YWallactivity,
			String fawardName, String fprizeName, Integer fawardOrder,
			Integer frandom, Integer fawardsPerson, String fdesignatedPerson,
			Set<FAwardsRecord> FAwardsRecords) {
		this.fid = fid;
		this.YWallactivity = YWallactivity;
		this.fawardName = fawardName;
		this.fprizeName = fprizeName;
		this.fawardOrder = fawardOrder;
		this.frandom = frandom;
		this.fawardsPerson = fawardsPerson;
		this.fdesignatedPerson = fdesignatedPerson;
		this.FAwardsRecords = FAwardsRecords;
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
	@JoinColumn(name = "FWallsID")
	public YWallactivity getYWallactivity() {
		return this.YWallactivity;
	}

	public void setYWallactivity(YWallactivity YWallactivity) {
		this.YWallactivity = YWallactivity;
	}

	@Column(name = "FAwardName", length = 36)
	public String getFawardName() {
		return this.fawardName;
	}

	public void setFawardName(String fawardName) {
		this.fawardName = fawardName;
	}

	@Column(name = "FPrizeName", length = 100)
	public String getFprizeName() {
		return this.fprizeName;
	}

	public void setFprizeName(String fprizeName) {
		this.fprizeName = fprizeName;
	}

	@Column(name = "FAwardOrder")
	public Integer getFawardOrder() {
		return this.fawardOrder;
	}

	public void setFawardOrder(Integer fawardOrder) {
		this.fawardOrder = fawardOrder;
	}

	@Column(name = "FRandom")
	public Integer getFrandom() {
		return this.frandom;
	}

	public void setFrandom(Integer frandom) {
		this.frandom = frandom;
	}

	@Column(name = "FAwardsPerson")
	public Integer getFawardsPerson() {
		return this.fawardsPerson;
	}

	public void setFawardsPerson(Integer fawardsPerson) {
		this.fawardsPerson = fawardsPerson;
	}

	@Column(name = "FDesignatedPerson", length = 65535)
	public String getFdesignatedPerson() {
		return this.fdesignatedPerson;
	}

	public void setFdesignatedPerson(String fdesignatedPerson) {
		this.fdesignatedPerson = fdesignatedPerson;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "FAwardSetting")
	@JsonIgnore
	public Set<FAwardsRecord> getFAwardsRecords() {
		return this.FAwardsRecords;
	}

	public void setFAwardsRecords(Set<FAwardsRecord> FAwardsRecords) {
		this.FAwardsRecords = FAwardsRecords;
	}

}