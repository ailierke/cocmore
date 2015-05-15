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
 * YComment entity. @author MyEclipse Persistence Tools
 * 评论
 */
@Entity
@Table(name = "y_comment" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YComment implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private YBasicType YBasicType;
	private YBasicMember YBasicMember;
	private String fcontents;
	private Date ftime;
	private String fforeignId;
	private Integer flag;
	private Set<YReplytocomment> YReplytocomments = new HashSet<YReplytocomment>(
			0);

	// Constructors

	/** default constructor */
	public YComment() {
	}

	/** minimal constructor */
	public YComment(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YComment(String fid, YBasicType YBasicType,
			YBasicMember YBasicMember, String fcontents, Date ftime,
			String fforeignId, Set<YReplytocomment> YReplytocomments) {
		this.fid = fid;
		this.YBasicType = YBasicType;
		this.YBasicMember = YBasicMember;
		this.fcontents = fcontents;
		this.ftime = ftime;
		this.fforeignId = fforeignId;
		this.YReplytocomments = YReplytocomments;
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
	@JoinColumn(name = "FType")
	public YBasicType getYBasicType() {
		return this.YBasicType;
	}

	public void setYBasicType(YBasicType YBasicType) {
		this.YBasicType = YBasicType;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FMemberID")
	public YBasicMember getYBasicMember() {
		return this.YBasicMember;
	}

	public void setYBasicMember(YBasicMember YBasicMember) {
		this.YBasicMember = YBasicMember;
	}

	@Column(name = "FContents", length = 300)
	public String getFcontents() {
		return this.fcontents;
	}

	public void setFcontents(String fcontents) {
		this.fcontents = fcontents;
	}

	@Column(name = "FTime", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFtime() {
		return this.ftime;
	}

	public void setFtime(Date ftime) {
		this.ftime = ftime;
	}

	@Column(name = "FForeignID", length = 36)
	public String getFforeignId() {
		return this.fforeignId;
	}

	public void setFforeignId(String fforeignId) {
		this.fforeignId = fforeignId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YComment")
	@JsonIgnore
	public Set<YReplytocomment> getYReplytocomments() {
		return this.YReplytocomments;
	}

	public void setYReplytocomments(Set<YReplytocomment> YReplytocomments) {
		this.YReplytocomments = YReplytocomments;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}