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
 * YAudit entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_audit" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YAudit implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private String fauditor;
	private Date fauditTime;
	private String fauditOpinion;
	private String freviewTheResults;
	private String fauditContentId;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YAudit() {
	}

	/** minimal constructor */
	public YAudit(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YAudit(String fid, String fauditor, Date fauditTime,
			String fauditOpinion, String freviewTheResults,
			String fauditContentId) {
		this.fid = fid;
		this.fauditor = fauditor;
		this.fauditTime = fauditTime;
		this.fauditOpinion = fauditOpinion;
		this.freviewTheResults = freviewTheResults;
		this.fauditContentId = fauditContentId;
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

	@Column(name = "FAuditor", length = 36)
	public String getFauditor() {
		return this.fauditor;
	}

	public void setFauditor(String fauditor) {
		this.fauditor = fauditor;
	}

	@Column(name = "FAuditTime", length = 19)
	public Date getFauditTime() {
		return this.fauditTime;
	}

	public void setFauditTime(Date fauditTime) {
		this.fauditTime = fauditTime;
	}

	@Column(name = "FAuditOpinion", length = 300)
	public String getFauditOpinion() {
		return this.fauditOpinion;
	}

	public void setFauditOpinion(String fauditOpinion) {
		this.fauditOpinion = fauditOpinion;
	}

	@Column(name = "FReviewTheResults", length = 36)
	public String getFreviewTheResults() {
		return this.freviewTheResults;
	}

	public void setFreviewTheResults(String freviewTheResults) {
		this.freviewTheResults = freviewTheResults;
	}

	@Column(name = "FAuditContentID", length = 36)
	public String getFauditContentId() {
		return this.fauditContentId;
	}

	public void setFauditContentId(String fauditContentId) {
		this.fauditContentId = fauditContentId;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}