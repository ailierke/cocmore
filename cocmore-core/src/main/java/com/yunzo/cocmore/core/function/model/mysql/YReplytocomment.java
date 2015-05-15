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

/**
 * YReplytocomment entity. @author MyEclipse Persistence Tools
 * 回复
 */
@Entity
@Table(name = "y_replytocomment" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YReplytocomment implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private YComment YComment;
	private String freplyId;
	private String freplyContents;
	private Date freplyTime;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YReplytocomment() {
	}

	/** minimal constructor */
	public YReplytocomment(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YReplytocomment(String fid, YComment YComment, String freplyId,
			String freplyContents, Date freplyTime) {
		this.fid = fid;
		this.YComment = YComment;
		this.freplyId = freplyId;
		this.freplyContents = freplyContents;
		this.freplyTime = freplyTime;
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
	@JoinColumn(name = "FCommentID")
	public YComment getYComment() {
		return this.YComment;
	}

	public void setYComment(YComment YComment) {
		this.YComment = YComment;
	}

	@Column(name = "FReplyID", length = 36)
	public String getFreplyId() {
		return this.freplyId;
	}

	public void setFreplyId(String freplyId) {
		this.freplyId = freplyId;
	}

	@Column(name = "FReplyContents", length = 300)
	public String getFreplyContents() {
		return this.freplyContents;
	}

	public void setFreplyContents(String freplyContents) {
		this.freplyContents = freplyContents;
	}

	@Column(name = "FReplyTime", length = 19)
	public Date getFreplyTime() {
		return this.freplyTime;
	}

	public void setFreplyTime(Date freplyTime) {
		this.freplyTime = freplyTime;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}