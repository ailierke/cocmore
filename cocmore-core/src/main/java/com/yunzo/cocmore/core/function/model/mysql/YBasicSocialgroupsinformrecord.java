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
 * YBasicSocialgroupsinformrecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_socialgroupsinformrecord" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicSocialgroupsinformrecord implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private YBasicSocialgroupsinform YBasicSocialgroupsinform;
	private String finformPeopleId;
	private String fynparticipation;
	private String freplyContent;
	private String finformPeopleName;
	private Integer flag;
	private Date updatetime;
	private String fisHide;   //是否隐藏

	/** default constructor */
	public YBasicSocialgroupsinformrecord() {
	}

	/** minimal constructor */
	public YBasicSocialgroupsinformrecord(String fid) {
		this.fid = fid;
	}

	public YBasicSocialgroupsinformrecord(
			String fid,
			com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsinform yBasicSocialgroupsinform,
			String finformPeopleId, String fynparticipation,
			String freplyContent, String finformPeopleName, Integer flag) {
		super();
		this.fid = fid;
		YBasicSocialgroupsinform = yBasicSocialgroupsinform;
		this.finformPeopleId = finformPeopleId;
		this.fynparticipation = fynparticipation;
		this.freplyContent = freplyContent;
		this.finformPeopleName = finformPeopleName;
		this.flag = flag;
	}

	public YBasicSocialgroupsinformrecord(
			String fid,
			com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsinform yBasicSocialgroupsinform,
			String finformPeopleId, String fynparticipation,
			String freplyContent, String finformPeopleName, Integer flag,
			Date updatetime) {
		super();
		this.fid = fid;
		YBasicSocialgroupsinform = yBasicSocialgroupsinform;
		this.finformPeopleId = finformPeopleId;
		this.fynparticipation = fynparticipation;
		this.freplyContent = freplyContent;
		this.finformPeopleName = finformPeopleName;
		this.flag = flag;
		this.updatetime = updatetime;
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

	// Constructors

	@Column(name = "FInformPeopleID", length = 36)
	public String getFinformPeopleId() {
		return this.finformPeopleId;
	}

	@Column(name = "FinformPeopleName")
	public String getFinformPeopleName() {
		return finformPeopleName;
	}


	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}
	@Column(name = "FReplyContent")
	public String getFreplyContent() {
		return this.freplyContent;
	}

	@Column(name = "FYNParticipation", length = 1)
	public String getFynparticipation() {
		return this.fynparticipation;
	}
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getUpdatetime() {
		return updatetime;
	}
	
	@Column(name = "fisHide")
	public String getFisHide() {
		return fisHide;
	}

	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FInformID")
	public YBasicSocialgroupsinform getYBasicSocialgroupsinform() {
		return this.YBasicSocialgroupsinform;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public void setFinformPeopleId(String finformPeopleId) {
		this.finformPeopleId = finformPeopleId;
	}

	public void setFinformPeopleName(String finformPeopleName) {
		this.finformPeopleName = finformPeopleName;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public void setFreplyContent(String freplyContent) {
		this.freplyContent = freplyContent;
	}

	public void setFynparticipation(String fynparticipation) {
		this.fynparticipation = fynparticipation;
	}
	
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	
	public void setFisHide(String fisHide) {
		this.fisHide = fisHide;
	}
	
	public void setYBasicSocialgroupsinform(
			YBasicSocialgroupsinform YBasicSocialgroupsinform) {
		this.YBasicSocialgroupsinform = YBasicSocialgroupsinform;
	}

}