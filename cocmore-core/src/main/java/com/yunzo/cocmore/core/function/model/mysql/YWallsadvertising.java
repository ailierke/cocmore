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
 * YWallsadvertising entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_wallsadvertising" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YWallsadvertising implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private YWallactivity YWallactivity;
	private String fimage;
	private String ftitle;
	private String fdescribe;
	private Date fcreationTime;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YWallsadvertising() {
	}

	/** minimal constructor */
	public YWallsadvertising(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YWallsadvertising(String fid, YWallactivity YWallactivity,
			String fimage, String ftitle, String fdescribe, Date fcreationTime) {
		this.fid = fid;
		this.YWallactivity = YWallactivity;
		this.fimage = fimage;
		this.ftitle = ftitle;
		this.fdescribe = fdescribe;
		this.fcreationTime = fcreationTime;
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
	@JoinColumn(name = "FWallActivityID")
	public YWallactivity getYWallactivity() {
		return this.YWallactivity;
	}

	public void setYWallactivity(YWallactivity YWallactivity) {
		this.YWallactivity = YWallactivity;
	}

	@Column(name = "FImage", length = 50)
	public String getFimage() {
		return this.fimage;
	}

	public void setFimage(String fimage) {
		this.fimage = fimage;
	}

	@Column(name = "FTitle", length = 36)
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Column(name = "FDescribe", length = 65535)
	public String getFdescribe() {
		return this.fdescribe;
	}

	public void setFdescribe(String fdescribe) {
		this.fdescribe = fdescribe;
	}

	@Column(name = "FCreationTime", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFcreationTime() {
		return this.fcreationTime;
	}

	public void setFcreationTime(Date fcreationTime) {
		this.fcreationTime = fcreationTime;
	}

	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}