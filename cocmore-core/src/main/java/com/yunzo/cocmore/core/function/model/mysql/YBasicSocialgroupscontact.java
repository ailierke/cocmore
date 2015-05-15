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
 * YBasicSocialgroupscontact entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_socialgroupscontact" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicSocialgroupscontact implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private YBasicSocialgroups YBasicSocialgroups;
	private String tell;
	private String adress;
	private String mail;
	private String uri;
	private String wechat;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	private Date updateTime;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YBasicSocialgroupscontact() {
	}

	/** minimal constructor */
	public YBasicSocialgroupscontact(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicSocialgroupscontact(String fid,
			YBasicSocialgroups YBasicSocialgroups, String tell, String adress,
			String mail, String uri, String wechat, Date updateTime) {
		this.fid = fid;
		this.YBasicSocialgroups = YBasicSocialgroups;
		this.tell = tell;
		this.adress = adress;
		this.mail = mail;
		this.uri = uri;
		this.wechat = wechat;
		this.updateTime = updateTime;
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

	@Column(name = "tell", length = 100)
	public String getTell() {
		return this.tell;
	}

	public void setTell(String tell) {
		this.tell = tell;
	}

	@Column(name = "adress", length = 200)
	public String getAdress() {
		return this.adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	@Column(name = "mail", length = 100)
	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@Column(name = "uri", length = 100)
	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Column(name = "wechat", length = 100)
	public String getWechat() {
		return this.wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
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