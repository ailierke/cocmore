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
 * YBasicMemberbymemberhide entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_memberbymemberhide" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicMemberbymemberhide implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private YBasicMember YBasicMemberByFinMemberId;
	private YBasicMember YBasicMemberByFoutMemberId;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YBasicMemberbymemberhide() {
	}

	/** minimal constructor */
	public YBasicMemberbymemberhide(String fid,
			YBasicMember YBasicMemberByFoutMemberId) {
		this.fid = fid;
		this.YBasicMemberByFoutMemberId = YBasicMemberByFoutMemberId;
	}

	/** full constructor */
	public YBasicMemberbymemberhide(String fid,
			YBasicMember YBasicMemberByFinMemberId,
			YBasicMember YBasicMemberByFoutMemberId) {
		this.fid = fid;
		this.YBasicMemberByFinMemberId = YBasicMemberByFinMemberId;
		this.YBasicMemberByFoutMemberId = YBasicMemberByFoutMemberId;
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
	@JoinColumn(name = "FInMemberID")
	public YBasicMember getYBasicMemberByFinMemberId() {
		return this.YBasicMemberByFinMemberId;
	}

	public void setYBasicMemberByFinMemberId(
			YBasicMember YBasicMemberByFinMemberId) {
		this.YBasicMemberByFinMemberId = YBasicMemberByFinMemberId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FOutMemberID", nullable = false)
	public YBasicMember getYBasicMemberByFoutMemberId() {
		return this.YBasicMemberByFoutMemberId;
	}

	public void setYBasicMemberByFoutMemberId(
			YBasicMember YBasicMemberByFoutMemberId) {
		this.YBasicMemberByFoutMemberId = YBasicMemberByFoutMemberId;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}