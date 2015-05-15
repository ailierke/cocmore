package com.yunzo.cocmore.core.function.model.mysql;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder.In;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * YBasicentriesMemberdistribution entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basicentries_memberdistribution" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicentriesMemberdistribution implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private YBasicMember YBasicMember;
	private YBasicPosition YBasicPosition;
	private String fkeyPost;
	private Integer fversion;
	private Integer flag;
	private Integer fseq;

	// Constructors

	/** default constructor */
	public YBasicentriesMemberdistribution() {
	}

	/** minimal constructor */
	public YBasicentriesMemberdistribution(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicentriesMemberdistribution(String fid,
			YBasicMember YBasicMember, YBasicPosition YBasicPosition,
			String fkeyPost) {
		this.fid = fid;
		this.YBasicMember = YBasicMember;
		this.YBasicPosition = YBasicPosition;
		this.fkeyPost = fkeyPost;
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

	@Column(name = "FKeyPost")
	public String getFkeyPost() {
		return this.fkeyPost;
	}

	public void setFkeyPost(String fkeyPost) {
		this.fkeyPost = fkeyPost;
	}

	@Column(name = "FVersion", length = 50)
	public Integer getFversion() {
		return this.fversion;
	}

	public void setFversion(Integer fversion) {
		this.fversion = fversion;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@Column(name = "FSeq")
	public Integer getFseq() {
		return fseq;
	}

	public void setFseq(Integer fseq) {
		this.fseq = fseq;
	}
	
	

}