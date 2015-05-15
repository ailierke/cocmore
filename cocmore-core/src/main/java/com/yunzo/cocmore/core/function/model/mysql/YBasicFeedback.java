package com.yunzo.cocmore.core.function.model.mysql;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * YBasicFeedback entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_feedback" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicFeedback implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private String fcontactInfo;
	private String fmessage;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YBasicFeedback() {
	}

	/** minimal constructor */
	public YBasicFeedback(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicFeedback(String fid, String fcontactInfo, String fmessage) {
		this.fid = fid;
		this.fcontactInfo = fcontactInfo;
		this.fmessage = fmessage;
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

	@Column(name = "FContactInfo", length = 24)
	public String getFcontactInfo() {
		return this.fcontactInfo;
	}

	public void setFcontactInfo(String fcontactInfo) {
		this.fcontactInfo = fcontactInfo;
	}

	@Column(name = "FMessage", length = 65535)
	public String getFmessage() {
		return this.fmessage;
	}

	public void setFmessage(String fmessage) {
		this.fmessage = fmessage;
	}

	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}