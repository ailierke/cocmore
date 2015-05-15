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
 * YBasicMemberbypositionblacklist entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_memberbypositionblacklist" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicMemberbypositionblacklist implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private YBasicPosition YBasicPosition;
	private YBasicSocialgroupsdynamic YBasicSocialgroupsdynamic;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YBasicMemberbypositionblacklist() {
	}

	/** minimal constructor */
	public YBasicMemberbypositionblacklist(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicMemberbypositionblacklist(String fid,
			YBasicPosition YBasicPosition,
			YBasicSocialgroupsdynamic YBasicSocialgroupsdynamic) {
		this.fid = fid;
		this.YBasicPosition = YBasicPosition;
		this.YBasicSocialgroupsdynamic = YBasicSocialgroupsdynamic;
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
	@JoinColumn(name = "FPositionID")
	public YBasicPosition getYBasicPosition() {
		return this.YBasicPosition;
	}

	public void setYBasicPosition(YBasicPosition YBasicPosition) {
		this.YBasicPosition = YBasicPosition;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FDynamicID")
	public YBasicSocialgroupsdynamic getYBasicSocialgroupsdynamic() {
		return this.YBasicSocialgroupsdynamic;
	}

	public void setYBasicSocialgroupsdynamic(
			YBasicSocialgroupsdynamic YBasicSocialgroupsdynamic) {
		this.YBasicSocialgroupsdynamic = YBasicSocialgroupsdynamic;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}