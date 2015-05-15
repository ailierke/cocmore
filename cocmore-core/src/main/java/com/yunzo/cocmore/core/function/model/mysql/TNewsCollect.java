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
 * TNewsCollect entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_news_collect" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class TNewsCollect implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String ftid;
	private TNewsHeadline TNewsHeadline;
	private String ftel;
	private Date fcreateTime;
	private Integer flag;

	// Constructors

	/** default constructor */
	public TNewsCollect() {
	}

	/** minimal constructor */
	public TNewsCollect(String ftid) {
		this.ftid = ftid;
	}

	/** full constructor */
	public TNewsCollect(String ftid, TNewsHeadline TNewsHeadline, String ftel,
			Date fcreateTime) {
		this.ftid = ftid;
		this.TNewsHeadline = TNewsHeadline;
		this.ftel = ftel;
		this.fcreateTime = fcreateTime;
	}

	// Property accessors
	@Id
	@Column(name = "Ftid", unique = true, nullable = false, length = 36)
	public String getFtid() {
		if (!StringUtils.isBlank(this.ftid)) {
			return this.ftid;
		}
		return UUID.randomUUID().toString();
	}

	public void setFtid(String ftid) {
		this.ftid = ftid;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Fnewsid")
	public TNewsHeadline getTNewsHeadline() {
		return this.TNewsHeadline;
	}

	public void setTNewsHeadline(TNewsHeadline TNewsHeadline) {
		this.TNewsHeadline = TNewsHeadline;
	}

	@Column(name = "Ftel", length = 11)
	public String getFtel() {
		
		return this.ftel;
	}

	public void setFtel(String ftel) {
		this.ftel = ftel;
	}

	@Column(name = "Fcreate_time", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}