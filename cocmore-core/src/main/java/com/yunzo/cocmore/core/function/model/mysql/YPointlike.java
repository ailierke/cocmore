package com.yunzo.cocmore.core.function.model.mysql;

import java.util.Date;
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
 * YPointlike entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_pointlike" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YPointlike implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private YBasicMember YBasicMember;
	private YBasicType YBasicType;
	private String fmanShowId;
	private Integer farePointLike;
	private Integer fpointLikeType;
	private Date fpointLikeTime;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YPointlike() {
	}

	/** minimal constructor */
	public YPointlike(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YPointlike(String fid, YBasicMember YBasicMember,
			YBasicType YBasicType, String fmanShowId, Integer farePointLike,
			Integer fpointLikeType, Date fpointLikeTime) {
		this.fid = fid;
		this.YBasicMember = YBasicMember;
		this.YBasicType = YBasicType;
		this.fmanShowId = fmanShowId;
		this.farePointLike = farePointLike;
		this.fpointLikeType = fpointLikeType;
		this.fpointLikeTime = fpointLikeTime;
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
	@JoinColumn(name = "FTypeID")
	public YBasicType getYBasicType() {
		return this.YBasicType;
	}

	public void setYBasicType(YBasicType YBasicType) {
		this.YBasicType = YBasicType;
	}

	@Column(name = "FManShowID", length = 36)
	public String getFmanShowId() {
		return this.fmanShowId;
	}

	public void setFmanShowId(String fmanShowId) {
		this.fmanShowId = fmanShowId;
	}

	@Column(name = "FArePointLike")
	public Integer getFarePointLike() {
		return this.farePointLike;
	}

	public void setFarePointLike(Integer farePointLike) {
		this.farePointLike = farePointLike;
	}

	@Column(name = "FPointLikeType")
	public Integer getFpointLikeType() {
		return this.fpointLikeType;
	}

	public void setFpointLikeType(Integer fpointLikeType) {
		this.fpointLikeType = fpointLikeType;
	}

	@Column(name = "FPointLikeTime", length = 19)
	public Date getFpointLikeTime() {
		return this.fpointLikeTime;
	}

	public void setFpointLikeTime(Date fpointLikeTime) {
		this.fpointLikeTime = fpointLikeTime;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}