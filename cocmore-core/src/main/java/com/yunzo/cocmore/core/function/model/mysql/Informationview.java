package com.yunzo.cocmore.core.function.model.mysql;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 视图实体
 * @author yunzo
 *
 */
@Entity
@Table(name = "informationview" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class Informationview implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String fid;
	private String ImageUrl;
	private String Content;
	private String  state;
	private Date newsDate;
	private String businessId;
	private String memberId;
	private String noticeId;
	private Integer type;
	private String status;
	
	@Id
	@Column(name = "fid", unique = true, nullable = false, length = 36)
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	
	@Column(name = "ImageUrl")
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}
	@Column(name = "Content")
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	@Column(name = "state")
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	@Column(name = "newsDate")
	public Date getNewsDate() {
		return newsDate;
	}
	public void setNewsDate(Date newsDate) {
		this.newsDate = newsDate;
	}
	
	@Column(name = "businessId")
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
	@Column(name = "memberId")
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	@Column(name = "noticeId")
	public String getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	@Column(name = "type")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Column(name = "Status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
