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
 * YBasicSocialgroupsguestbook entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_socialgroupsguestbook" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicSocialgroupsguestbook implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private YBasicSocialgroups YBasicSocialgroups;
	private YBasicMember YBasicMember;
	private String fimages;
	private String fmessage;
	private Date fguestBookTime;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YBasicSocialgroupsguestbook() {
	}

	/** minimal constructor */
	public YBasicSocialgroupsguestbook(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicSocialgroupsguestbook(String fid,
			YBasicSocialgroups YBasicSocialgroups, YBasicMember YBasicMember,
			String fimages, String fmessage, Date fguestBookTime) {
		this.fid = fid;
		this.YBasicSocialgroups = YBasicSocialgroups;
		this.YBasicMember = YBasicMember;
		this.fimages = fimages;
		this.fmessage = fmessage;
		this.fguestBookTime = fguestBookTime;
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
	@JoinColumn(name = "FSocialGroupsID")
	public YBasicSocialgroups getYBasicSocialgroups() {
		return this.YBasicSocialgroups;
	}

	public void setYBasicSocialgroups(YBasicSocialgroups YBasicSocialgroups) {
		this.YBasicSocialgroups = YBasicSocialgroups;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FGuestBookID")
	public YBasicMember getYBasicMember() {
		return this.YBasicMember;
	}

	public void setYBasicMember(YBasicMember YBasicMember) {
		this.YBasicMember = YBasicMember;
	}

	@Column(name = "FImages", length = 250)
	public String getFimages() {
		return this.fimages;
	}

	public void setFimages(String fimages) {
		this.fimages = fimages;
	}

	@Column(name = "FMessage", length = 65535)
	public String getFmessage() {
		return this.fmessage;
	}

	public void setFmessage(String fmessage) {
		this.fmessage = fmessage;
	}

	@Column(name = "FGuestBookTime", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFguestBookTime() {
		return this.fguestBookTime;
	}

	public void setFguestBookTime(Date fguestBookTime) {
		this.fguestBookTime = fguestBookTime;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}