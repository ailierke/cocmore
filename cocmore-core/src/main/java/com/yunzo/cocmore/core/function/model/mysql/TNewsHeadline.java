package com.yunzo.cocmore.core.function.model.mysql;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * TNewsHeadline entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_news_headline" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class TNewsHeadline implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String ftid;
	private String ftitle;
	private String fimageUrl;
	private String ftImageUrl;
	private String fnewsContent;
	private String fsource;
	private Date freleaseTime;
	private String fdetailsUrl;
	private String fclassification;
	private String fdescribe;
	private Integer ftype;
	private Date fcreateTime;
	private Integer fisPush;
	private Integer flag;
	private Set<TNewsCollect> TNewsCollects = new HashSet<TNewsCollect>(0);

	// Constructors

	/** default constructor */
	public TNewsHeadline() {
	}

	/** minimal constructor */
	public TNewsHeadline(String ftid) {
		this.ftid = ftid;
	}

	/** full constructor */
	public TNewsHeadline(String ftid, String ftitle, String fimageUrl,
			String ftImageUrl, String fnewsContent, String fsource,
			Date freleaseTime, String fdetailsUrl, String fclassification,
			String fdescribe, Integer ftype, Date fcreateTime, Integer fisPush,
			Set<TNewsCollect> TNewsCollects) {
		this.ftid = ftid;
		this.ftitle = ftitle;
		this.fimageUrl = fimageUrl;
		this.ftImageUrl = ftImageUrl;
		this.fnewsContent = fnewsContent;
		this.fsource = fsource;
		this.freleaseTime = freleaseTime;
		this.fdetailsUrl = fdetailsUrl;
		this.fclassification = fclassification;
		this.fdescribe = fdescribe;
		this.ftype = ftype;
		this.fcreateTime = fcreateTime;
		this.fisPush = fisPush;
		this.TNewsCollects = TNewsCollects;
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

	@Column(name = "Ftitle", length = 64)
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Column(name = "Fimage_url", length = 200)
	public String getFimageUrl() {
		return this.fimageUrl;
	}

	public void setFimageUrl(String fimageUrl) {
		this.fimageUrl = fimageUrl;
	}

	@Column(name = "Ft_image_url", length = 200)
	public String getFtImageUrl() {
		return this.ftImageUrl;
	}

	public void setFtImageUrl(String ftImageUrl) {
		this.ftImageUrl = ftImageUrl;
	}

	@Column(name = "Fnews_content", length = 65535)
	public String getFnewsContent() {
		return this.fnewsContent;
	}

	public void setFnewsContent(String fnewsContent) {
		this.fnewsContent = fnewsContent;
	}

	@Column(name = "Fsource", length = 64)
	public String getFsource() {
		return this.fsource;
	}

	public void setFsource(String fsource) {
		this.fsource = fsource;
	}

	@Column(name = "Frelease_time", length = 19)
//	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFreleaseTime() {
		return this.freleaseTime;
	}

	public void setFreleaseTime(Date freleaseTime) {
		this.freleaseTime = freleaseTime;
	}

	@Column(name = "Fdetails_url", length = 200)
	public String getFdetailsUrl() {
		return this.fdetailsUrl;
	}

	public void setFdetailsUrl(String fdetailsUrl) {
		this.fdetailsUrl = fdetailsUrl;
	}

	@Column(name = "Fclassification", length = 36)
	public String getFclassification() {
		return this.fclassification;
	}

	public void setFclassification(String fclassification) {
		this.fclassification = fclassification;
	}

	@Column(name = "Fdescribe", length = 40)
	public String getFdescribe() {
		return this.fdescribe;
	}

	public void setFdescribe(String fdescribe) {
		this.fdescribe = fdescribe;
	}

	@Column(name = "Ftype")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "Fcreate_time", length = 19)
//	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "Fis_push")
	public Integer getFisPush() {
		return this.fisPush;
	}

	public void setFisPush(Integer fisPush) {
		this.fisPush = fisPush;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TNewsHeadline")
	@JsonIgnore
	public Set<TNewsCollect> getTNewsCollects() {
		return this.TNewsCollects;
	}

	public void setTNewsCollects(Set<TNewsCollect> TNewsCollects) {
		this.TNewsCollects = TNewsCollects;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}