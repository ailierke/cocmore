package com.yunzo.cocmore.core.function.model.mysql;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * YVisitorsRecordNotlogin entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_visitors_record_notlogin" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YVisitorsRecordNotlogin implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private String fuserID;
	private String fname;
	private String ftelphone;
	private Date faccessTime;
	private Date flastAccessTime;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YVisitorsRecordNotlogin() {
	}

	/** minimal constructor */
	public YVisitorsRecordNotlogin(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YVisitorsRecordNotlogin(String fid, 
			Date faccessTime,  String ftelphone) {
		this.fid = fid;
		this.faccessTime = faccessTime;
		this.ftelphone = ftelphone;
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

	@Column(name = "FAccessTime", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFaccessTime() {
		return this.faccessTime;
	}

	public void setFaccessTime(Date faccessTime) {
		this.faccessTime = faccessTime;
	}

	@Column(name = "FTelphone", length = 100)
	public String getFtelphone() {
		return this.ftelphone;
	}

	public void setFtelphone(String ftelphone) {
		this.ftelphone = ftelphone;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
	
	@Column(name = "FUserID")
	public String getFuserID() {
		return fuserID;
	}

	public void setFuserID(String fuserID) {
		this.fuserID = fuserID;
	}

	@Column(name = "FName")
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "FLastAccessTime")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFlastAccessTime() {
		return flastAccessTime;
	}

	public void setFlastAccessTime(Date flastAccessTime) {
		this.flastAccessTime = flastAccessTime;
	}
	
	
	
	

}