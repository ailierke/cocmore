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
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * YBasicSocialgroupsabout entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_socialgroupsabout" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicSocialgroupsabout implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private YBasicSocialgroups YBasicSocialgroups;
	private String title;
	private String content;
	private Date updateTime;
	private Integer flag;
	private Integer sequenceNumber;

	// Constructors
	@Column(name = "sequenceNumber")
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/** default constructor */
	public YBasicSocialgroupsabout() {
	}

	/** minimal constructor */
	public YBasicSocialgroupsabout(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicSocialgroupsabout(String fid,
			YBasicSocialgroups YBasicSocialgroups, String title,
			String content, Date updateTime,Integer flag,Integer sequenceNumber) {
		this.fid = fid;
		this.YBasicSocialgroups = YBasicSocialgroups;
		this.title = title;
		this.content = content;
		this.updateTime = updateTime;
		this.flag = flag;
		this.sequenceNumber =sequenceNumber;
	}

	// Property accessors
	@Id
	@Column(name = "fid", unique = true, nullable = false, length = 36)
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
	@JoinColumn(name = "FSocialGroupID")
	public YBasicSocialgroups getYBasicSocialgroups() {
		return this.YBasicSocialgroups;
	}

	public void setYBasicSocialgroups(YBasicSocialgroups YBasicSocialgroups) {
		this.YBasicSocialgroups = YBasicSocialgroups;
	}

	@Column(name = "title", length = 100)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "content", length = 65535)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "updateTime", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getUpdateTime() {
		return this.updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}