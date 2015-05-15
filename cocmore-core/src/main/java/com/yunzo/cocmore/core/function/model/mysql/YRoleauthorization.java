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
 * YRoleauthorization entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_roleauthorization" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YRoleauthorization implements java.io.Serializable {

	// Fields

	private String fid;
	private YSystemUsers YSystemUsers;
	private YCharacter YCharacter;
	//private YBasicOrganization YBasicOrganization;
	private YFunctionentriesFunctionjournalentry YFunctionentriesFunctionjournalentry;
	private YSystemconfigurationFunction YSystemconfigurationFunction;
	private String forganizationId;
	private String fGroupsId;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YRoleauthorization() {
	}

	/** minimal constructor */
	public YRoleauthorization(String fid/*, YCharacter YCharacter,
			YBasicOrganization YBasicOrganization*/) {
		this.fid = fid;
		//this.YCharacter = YCharacter;
		//this.YBasicOrganization = YBasicOrganization;
	}

	/** full constructor */
	public YRoleauthorization(
			String fid,
			YCharacter YCharacter,
			/*YBasicOrganization YBasicOrganization,*/
			YSystemUsers YSystemUsers,
			YFunctionentriesFunctionjournalentry YFunctionentriesFunctionjournalentry,
			YSystemconfigurationFunction YSystemconfigurationFunction,
			String forganizationId, String fGroupsId) {
		this.fid = fid;
		this.YCharacter = YCharacter;
		/*this.YBasicOrganization = YBasicOrganization;*/
		this.YFunctionentriesFunctionjournalentry = YFunctionentriesFunctionjournalentry;
		this.YSystemconfigurationFunction = YSystemconfigurationFunction;
		this.forganizationId = forganizationId;
		this.YSystemUsers = YSystemUsers;
		this.fGroupsId = fGroupsId;
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
	@JoinColumn(name = "FCharacterID", nullable = false)
	public YCharacter getYCharacter() {
		return this.YCharacter;
	}

	public void setYCharacter(YCharacter YCharacter) {
		this.YCharacter = YCharacter;
	}

	/*@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FID", unique = true, nullable = false, insertable = false, updatable = false)
	public YBasicOrganization getYBasicOrganization() {
		return this.YBasicOrganization;
	}

	public void setYBasicOrganization(YBasicOrganization YBasicOrganization) {
		this.YBasicOrganization = YBasicOrganization;
	}*/

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FFunctionJournalEntryID")
	public YFunctionentriesFunctionjournalentry getYFunctionentriesFunctionjournalentry() {
		return this.YFunctionentriesFunctionjournalentry;
	}

	public void setYFunctionentriesFunctionjournalentry(
			YFunctionentriesFunctionjournalentry YFunctionentriesFunctionjournalentry) {
		this.YFunctionentriesFunctionjournalentry = YFunctionentriesFunctionjournalentry;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FFunctionID")
	public YSystemconfigurationFunction getYSystemconfigurationFunction() {
		return this.YSystemconfigurationFunction;
	}

	public void setYSystemconfigurationFunction(
			YSystemconfigurationFunction YSystemconfigurationFunction) {
		this.YSystemconfigurationFunction = YSystemconfigurationFunction;
	}

	@Column(name = "FOrganizationID")
	public String getForganizationId() {
		return this.forganizationId;
	}

	public void setForganizationId(String forganizationId) {
		this.forganizationId = forganizationId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FAdminID")
	public YSystemUsers getYSystemUsers() {
		return this.YSystemUsers;
	}

	public void setYSystemUsers(YSystemUsers YSystemUsers) {
		this.YSystemUsers = YSystemUsers;
	}
	
	@Column(name = "FGroupsId")
	public String getfGroupsId() {
		return fGroupsId;
	}

	public void setfGroupsId(String fGroupsId) {
		this.fGroupsId = fGroupsId;
	}

	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}