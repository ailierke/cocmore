package com.yunzo.cocmore.core.function.model.mysql;

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

/**
 * YBasicGrouppeople entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_grouppeople" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicGrouppeople implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private YBasicGroup YBasicGroup;
	private YBasicImaccount YBasicImaccount;
	private Integer fisCreater;
	private String fnickName;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YBasicGrouppeople() {
	}

	/** minimal constructor */
	public YBasicGrouppeople(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicGrouppeople(String fid, YBasicGroup YBasicGroup,
			YBasicImaccount YBasicImaccount, Integer fisCreater,
			String fnickName) {
		this.fid = fid;
		this.YBasicGroup = YBasicGroup;
		this.YBasicImaccount = YBasicImaccount;
		this.fisCreater = fisCreater;
		this.fnickName = fnickName;
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
	@JoinColumn(name = "FGroupID")
	public YBasicGroup getYBasicGroup() {
		return this.YBasicGroup;
	}

	public void setYBasicGroup(YBasicGroup YBasicGroup) {
		this.YBasicGroup = YBasicGroup;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FIMID")
	public YBasicImaccount getYBasicImaccount() {
		return this.YBasicImaccount;
	}

	public void setYBasicImaccount(YBasicImaccount YBasicImaccount) {
		this.YBasicImaccount = YBasicImaccount;
	}

	@Column(name = "FIsCreater")
	public Integer getFisCreater() {
		return this.fisCreater;
	}

	public void setFisCreater(Integer fisCreater) {
		this.fisCreater = fisCreater;
	}

	@Column(name = "FNickName", length = 100)
	public String getFnickName() {
		return this.fnickName;
	}

	public void setFnickName(String fnickName) {
		this.fnickName = fnickName;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}