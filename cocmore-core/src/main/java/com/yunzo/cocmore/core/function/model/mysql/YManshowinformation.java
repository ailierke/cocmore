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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * YManshowinformation entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_manshowinformation" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YManshowinformation implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private YBasicMember YBasicMember;
	private String fcontents;
	private Date fpublished;
	private Integer fisOpen;
	private Integer flag;
	private Set<YImage> YImages = new HashSet<YImage>(0);

	// Constructors

	/** default constructor */
	public YManshowinformation() {
	}

	/** minimal constructor */
	public YManshowinformation(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YManshowinformation(String fid, YBasicMember YBasicMember,
			String fcontents, Date fpublished, Integer fisOpen,
			Set<YImage> YImages) {
		this.fid = fid;
		this.YBasicMember = YBasicMember;
		this.fcontents = fcontents;
		this.fpublished = fpublished;
		this.fisOpen = fisOpen;
		this.YImages = YImages;
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
	@JoinColumn(name = "FMemberID")
	public YBasicMember getYBasicMember() {
		return this.YBasicMember;
	}

	public void setYBasicMember(YBasicMember YBasicMember) {
		this.YBasicMember = YBasicMember;
	}

	@Column(name = "FContents", length = 65535)
	public String getFcontents() {
		return this.fcontents;
	}

	public void setFcontents(String fcontents) {
		this.fcontents = fcontents;
	}

	@Column(name = "FPublished", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFpublished() {
		return this.fpublished;
	}

	public void setFpublished(Date fpublished) {
		this.fpublished = fpublished;
	}

	@Column(name = "FIsOpen")
	public Integer getFisOpen() {
		return this.fisOpen;
	}

	public void setFisOpen(Integer fisOpen) {
		this.fisOpen = fisOpen;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YManshowinformation")
	@JsonIgnore
	public Set<YImage> getYImages() {
		return this.YImages;
	}

	public void setYImages(Set<YImage> YImages) {
		this.YImages = YImages;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}