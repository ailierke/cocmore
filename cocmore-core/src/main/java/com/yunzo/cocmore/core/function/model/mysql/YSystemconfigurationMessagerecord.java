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
 * YSystemconfigurationMessagerecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_systemconfiguration_messagerecord" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YSystemconfigurationMessagerecord implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private YSystemconfigurationMessage YSystemconfigurationMessage;
	private String fuserId;
	private Date fdate;

	// Constructors

	/** default constructor */
	public YSystemconfigurationMessagerecord() {
	}

	/** minimal constructor */
	public YSystemconfigurationMessagerecord(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YSystemconfigurationMessagerecord(String fid,
			YSystemconfigurationMessage YSystemconfigurationMessage,
			String fuserId, Date fdate) {
		this.fid = fid;
		this.YSystemconfigurationMessage = YSystemconfigurationMessage;
		this.fuserId = fuserId;
		this.fdate = fdate;
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
	@JoinColumn(name = "FMessage")
	public YSystemconfigurationMessage getYSystemconfigurationMessage() {
		return this.YSystemconfigurationMessage;
	}

	public void setYSystemconfigurationMessage(
			YSystemconfigurationMessage YSystemconfigurationMessage) {
		this.YSystemconfigurationMessage = YSystemconfigurationMessage;
	}

	@Column(name = "FUserID", length = 36)
	public String getFuserId() {
		return this.fuserId;
	}

	public void setFuserId(String fuserId) {
		this.fuserId = fuserId;
	}

	@Column(name = "FDate")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFdate() {
		return this.fdate;
	}

	public void setFdate(Date fdate) {
		this.fdate = fdate;
	}

}