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

/**
 * YBasicVerification entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_verification" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicVerification implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private String fuserPhone;
	private String fverification;
	private Date floseDate;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YBasicVerification() {
	}

	/** minimal constructor */
	public YBasicVerification(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicVerification(String fid, String fuserPhone,
			String fverification, Date floseDate) {
		this.fid = fid;
		this.fuserPhone = fuserPhone;
		this.fverification = fverification;
		this.floseDate = floseDate;
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

	@Column(name = "FUserPhone", length = 36)
	public String getFuserPhone() {
		return this.fuserPhone;
	}

	public void setFuserPhone(String fuserPhone) {
		this.fuserPhone = fuserPhone;
	}

	@Column(name = "FVerification", length = 10)
	public String getFverification() {
		return this.fverification;
	}

	public void setFverification(String fverification) {
		this.fverification = fverification;
	}

	@Column(name = "FLoseDate", length = 19)
	public Date getFloseDate() {
		return this.floseDate;
	}

	public void setFloseDate(Date floseDate) {
		this.floseDate = floseDate;
	}

	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}