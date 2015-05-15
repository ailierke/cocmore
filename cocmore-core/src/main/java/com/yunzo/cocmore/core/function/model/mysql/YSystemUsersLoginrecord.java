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
 * YSystemUsersLoginrecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_system_users_loginrecord" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YSystemUsersLoginrecord implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private YSystemUsers YSystemUsers;
	private Date fusersLoginTime;
	private String fusersLoginIp;
	private String fusersName;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YSystemUsersLoginrecord() {
	}

	/** minimal constructor */
	public YSystemUsersLoginrecord(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YSystemUsersLoginrecord(String fid, YSystemUsers YSystemUsers,
			Date fusersLoginTime, String fusersLoginIp, String fusersName) {
		this.fid = fid;
		this.YSystemUsers = YSystemUsers;
		this.fusersLoginTime = fusersLoginTime;
		this.fusersLoginIp = fusersLoginIp;
		this.fusersName = fusersName;
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
	@JoinColumn(name = "FUsersID")
	public YSystemUsers getYSystemUsers() {
		return this.YSystemUsers;
	}

	public void setYSystemUsers(YSystemUsers YSystemUsers) {
		this.YSystemUsers = YSystemUsers;
	}

	@Column(name = "FUsersLoginTime", length = 19)
	public Date getFusersLoginTime() {
		return this.fusersLoginTime;
	}

	public void setFusersLoginTime(Date fusersLoginTime) {
		this.fusersLoginTime = fusersLoginTime;
	}

	@Column(name = "FUsersLoginIP", length = 22)
	public String getFusersLoginIp() {
		return this.fusersLoginIp;
	}

	public void setFusersLoginIp(String fusersLoginIp) {
		this.fusersLoginIp = fusersLoginIp;
	}

	@Column(name = "FUsersName", length = 36)
	public String getFusersName() {
		return this.fusersName;
	}

	public void setFusersName(String fusersName) {
		this.fusersName = fusersName;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}