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
 * YBasicMemberbypositionhide entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_memberbypositionhide" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicMemberbypositionhide implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private YBasicMember YBasicMember;
	private YBasicPosition YBasicPosition;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YBasicMemberbypositionhide() {
	}

	/** minimal constructor */
	public YBasicMemberbypositionhide(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicMemberbypositionhide(String fid, YBasicMember YBasicMember,
			YBasicPosition YBasicPosition) {
		this.fid = fid;
		this.YBasicMember = YBasicMember;
		this.YBasicPosition = YBasicPosition;
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
	@JoinColumn(name = "FMemberID")
	public YBasicMember getYBasicMember() {
		return this.YBasicMember;
	}

	public void setYBasicMember(YBasicMember YBasicMember) {
		this.YBasicMember = YBasicMember;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FPositionID")
	public YBasicPosition getYBasicPosition() {
		return this.YBasicPosition;
	}

	public void setYBasicPosition(YBasicPosition YBasicPosition) {
		this.YBasicPosition = YBasicPosition;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}