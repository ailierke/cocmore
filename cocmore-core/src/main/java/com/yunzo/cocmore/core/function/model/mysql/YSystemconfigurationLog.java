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
 * YSystemconfigurationLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_systemconfiguration_log" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YSystemconfigurationLog implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private String fnumber;
	private String fuserId;
	private Integer ftype;
	private Date ftime;
	private String fcontent;
	private String fip;
	private String fremark;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YSystemconfigurationLog() {
	}

	/** minimal constructor */
	public YSystemconfigurationLog(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YSystemconfigurationLog(String fid, String fnumber, String fuserId,
			Integer ftype, Date ftime, String fcontent, String fip,
			String fremark, Integer flag) {
		this.fid = fid;
		this.fnumber = fnumber;
		this.fuserId = fuserId;
		this.ftype = ftype;
		this.ftime = ftime;
		this.fcontent = fcontent;
		this.fip = fip;
		this.fremark = fremark;
		this.flag = flag;
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

	@Column(name = "FNumber", length = 36)
	public String getFnumber() {
		return this.fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	@Column(name = "FUserID", length = 36)
	public String getFuserId() {
		return this.fuserId;
	}

	public void setFuserId(String fuserId) {
		this.fuserId = fuserId;
	}

	@Column(name = "FType")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "FTime", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFtime() {
		return this.ftime;
	}

	public void setFtime(Date ftime) {
		this.ftime = ftime;
	}

	@Column(name = "FContent", length = 300)
	public String getFcontent() {
		return this.fcontent;
	}

	public void setFcontent(String fcontent) {
		this.fcontent = fcontent;
	}

	@Column(name = "FIP", length = 100)
	public String getFip() {
		return this.fip;
	}

	public void setFip(String fip) {
		this.fip = fip;
	}

	@Column(name = "FRemark", length = 300)
	public String getFremark() {
		return this.fremark;
	}

	public void setFremark(String fremark) {
		this.fremark = fremark;
	}

	@Column(name = "Flag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}