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

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * YWallreply entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_wallreply" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YWallreply implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private YBasicMember YBasicMember;
	private YWallactivity YWallactivity;
	private String fcontent;
	private String fcommerceId;
	private Date freplyTime;
	private Integer fstate;
	private Integer fsum;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YWallreply() {
	}

	/** minimal constructor */
	public YWallreply(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YWallreply(String fid, YBasicMember YBasicMember,
			YWallactivity YWallactivity, String fcontent, String fcommerceId,
			Date freplyTime, Integer fstate, Integer fsum) {
		this.fid = fid;
		this.YBasicMember = YBasicMember;
		this.YWallactivity = YWallactivity;
		this.fcontent = fcontent;
		this.fcommerceId = fcommerceId;
		this.freplyTime = freplyTime;
		this.fstate = fstate;
		this.fsum = fsum;
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
	@JoinColumn(name = "FUserID")
	public YBasicMember getYBasicMember() {
		return this.YBasicMember;
	}

	public void setYBasicMember(YBasicMember YBasicMember) {
		this.YBasicMember = YBasicMember;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FWallActivityID")
	public YWallactivity getYWallactivity() {
		return this.YWallactivity;
	}

	public void setYWallactivity(YWallactivity YWallactivity) {
		this.YWallactivity = YWallactivity;
	}

	@Column(name = "FContent", length = 300)
	public String getFcontent() {
		return this.fcontent;
	}

	public void setFcontent(String fcontent) {
		this.fcontent = fcontent;
	}

	@Column(name = "FCommerceID", length = 36)
	public String getFcommerceId() {
		return this.fcommerceId;
	}

	public void setFcommerceId(String fcommerceId) {
		this.fcommerceId = fcommerceId;
	}

	@Column(name = "FReplyTime", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFreplyTime() {
		return this.freplyTime;
	}

	public void setFreplyTime(Date freplyTime) {
		this.freplyTime = freplyTime;
	}

	@Column(name = "FState")
	public Integer getFstate() {
		return this.fstate;
	}

	public void setFstate(Integer fstate) {
		this.fstate = fstate;
	}

	@Column(name = "FSum")
	public Integer getFsum() {
		return this.fsum;
	}

	public void setFsum(Integer fsum) {
		this.fsum = fsum;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}