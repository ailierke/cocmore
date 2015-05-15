package com.yunzo.cocmore.core.function.model.mysql;

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
 * YOrganizationauthorized entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_organizationauthorized" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YOrganizationauthorized implements java.io.Serializable {

	// Fields

	private String fid;
	private YFunctionentriesFunctionjournalentry YFunctionentriesFunctionjournalentry;
	private YSystemUsers YSystemUsers;
	//private String ffunctionId;
	private YSystemconfigurationFunction YSystemconfigurationFunction;
	private Integer flag;
	// Constructors

	/** default constructor */
	public YOrganizationauthorized() {
	}

	/** minimal constructor */
	public YOrganizationauthorized(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YOrganizationauthorized(
			String fid,
			YFunctionentriesFunctionjournalentry YFunctionentriesFunctionjournalentry,
			YSystemUsers YSystemUsers, YSystemconfigurationFunction YSystemconfigurationFunction) {
		this.fid = fid;
		this.YFunctionentriesFunctionjournalentry = YFunctionentriesFunctionjournalentry;
		this.YSystemUsers = YSystemUsers;
		this.YSystemconfigurationFunction = YSystemconfigurationFunction;
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
	@JoinColumn(name = "FFunctionEntryID")
	public YFunctionentriesFunctionjournalentry getYFunctionentriesFunctionjournalentry() {
		return this.YFunctionentriesFunctionjournalentry;
	}

	public void setYFunctionentriesFunctionjournalentry(
			YFunctionentriesFunctionjournalentry YFunctionentriesFunctionjournalentry) {
		this.YFunctionentriesFunctionjournalentry = YFunctionentriesFunctionjournalentry;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FAdminID")
	public YSystemUsers getYSystemUsers() {
		return this.YSystemUsers;
	}

	public void setYSystemUsers(YSystemUsers YSystemUsers) {
		this.YSystemUsers = YSystemUsers;
	}

//	@Column(name = "FFunctionID", length = 0)
//	public String getFfunctionId() {
//		return this.ffunctionId;
//	}
//
//	public void setFfunctionId(String ffunctionId) {
//		this.ffunctionId = ffunctionId;
//	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FFunctionID")
	public YSystemconfigurationFunction getYSystemconfigurationFunction() {
		return this.YSystemconfigurationFunction;
	}

	public void setYSystemconfigurationFunction(
			YSystemconfigurationFunction YSystemconfigurationFunction) {
		this.YSystemconfigurationFunction = YSystemconfigurationFunction;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}