package com.yunzo.cocmore.core.function.model.mysql;

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
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * YSystemUsers entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_system_users" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YSystemUsers implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private String faccount;
	private String fuserPassword;
	private Integer fbillState;
	private String ftypeId;
	private Integer flag;
	private Integer fprivileges;
	private String fSGName;
	private Set<YBasicEmployee> YBasicEmployees = new HashSet<YBasicEmployee>(0);

	private Set<YBasicMember> YBasicMembers = new HashSet<YBasicMember>(0);

	private Set<YSystemUsersLoginrecord> YSystemUsersLoginrecords = new HashSet<YSystemUsersLoginrecord>(
			0);

	private Set<YOrganizationauthorized> YOrganizationauthorizeds = new HashSet<YOrganizationauthorized>(
			0);
	private Set<YRoleauthorization> YRoleauthorizations = new HashSet<YRoleauthorization>(
			0);
	/** default constructor */
	public YSystemUsers() {
	}
	/** minimal constructor */
	public YSystemUsers(String fid, String faccount) {
		this.fid = fid;
		this.faccount = faccount;
	}
	/** full constructor */
	public YSystemUsers(String fid, String faccount, String fuserPassword,
			Integer fbillState, String ftypeId) {
		this.fid = fid;
		this.faccount = faccount;
		this.fuserPassword = fuserPassword;
		this.fbillState = fbillState;
		this.ftypeId = ftypeId;
	}
	public enum Status {
		//y_system_user和y_charater 中FPrivilege 0是未赋权1是赋权 2赋予角色权限 3是赋予用户权限  4是角色和用户权限 5待定
		ADDNEW(0),SAVE(1),SUBMIT(2),AUDIT(3),UNAUDIT(4),EFFECT(5);

		//定义自己的构造器
		private int value;//中文名字

		private Status(int value){
			this.value=value;
		}

		public int value() {
			return value;
		}
	}
	// Constructors

	public YSystemUsers(String fid, String faccount, String fuserPassword,
			Integer fbillState, String ftypeId, Integer flag,
			Integer fprivileges, Set<YBasicEmployee> yBasicEmployees,
			Set<YBasicMember> yBasicMembers,
			Set<YSystemUsersLoginrecord> ySystemUsersLoginrecords,
			Set<YOrganizationauthorized> yOrganizationauthorizeds,
			Set<YRoleauthorization> yRoleauthorizations) {
		super();
		this.fid = fid;
		this.faccount = faccount;
		this.fuserPassword = fuserPassword;
		this.fbillState = fbillState;
		this.ftypeId = ftypeId;
		this.flag = flag;
		this.fprivileges = fprivileges;
		YBasicEmployees = yBasicEmployees;
		YBasicMembers = yBasicMembers;
		YSystemUsersLoginrecords = ySystemUsersLoginrecords;
		YOrganizationauthorizeds = yOrganizationauthorizeds;
		YRoleauthorizations = yRoleauthorizations;
	}

	@Column(name = "FAccount", nullable = false, length = 36)
	public String getFaccount() {
		return this.faccount;
	}

	@Column(name = "FBillState")
	public Integer getFbillState() {
		return this.fbillState;
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

	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public Integer getFprivileges() {
		return fprivileges;
	}

	@Column(name = "FTypeID", length = 36)
	public String getFtypeId() {
		return this.ftypeId;
	}

	@Column(name = "FUserPassword", length = 100)
	public String getFuserPassword() {
		return this.fuserPassword;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YSystemUsers")
	@JsonIgnore
	public Set<YBasicEmployee> getYBasicEmployees() {
		return this.YBasicEmployees;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YSystemUsers")
	@JsonIgnore
	public Set<YBasicMember> getYBasicMembers() {
		return this.YBasicMembers;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YSystemUsers")
	@JsonIgnore
	public Set<YOrganizationauthorized> getYOrganizationauthorizeds() {
		return this.YOrganizationauthorizeds;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YSystemUsers")
	@JsonIgnore
	public Set<YRoleauthorization> getYRoleauthorizations() {
		return YRoleauthorizations;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YSystemUsers")
	@JsonIgnore
	public Set<YSystemUsersLoginrecord> getYSystemUsersLoginrecords() {
		return this.YSystemUsersLoginrecords;
	}

	public void setFaccount(String faccount) {
		this.faccount = faccount;
	}

	public void setFbillState(Integer fbillState) {
		this.fbillState = fbillState;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public void setFprivileges(Integer fprivileges) {
		this.fprivileges = fprivileges;
	}

	public void setFtypeId(String ftypeId) {
		this.ftypeId = ftypeId;
	}

	public void setFuserPassword(String fuserPassword) {
		this.fuserPassword = fuserPassword;
	}

	public void setYBasicEmployees(Set<YBasicEmployee> YBasicEmployees) {
		this.YBasicEmployees = YBasicEmployees;
	}
	
	public void setYBasicMembers(Set<YBasicMember> YBasicMembers) {
		this.YBasicMembers = YBasicMembers;
	}

	public void setYOrganizationauthorizeds(
			Set<YOrganizationauthorized> YOrganizationauthorizeds) {
		this.YOrganizationauthorizeds = YOrganizationauthorizeds;
	}
	
	public void setYRoleauthorizations(Set<YRoleauthorization> yRoleauthorizations) {
		YRoleauthorizations = yRoleauthorizations;
	}

	public void setYSystemUsersLoginrecords(
			Set<YSystemUsersLoginrecord> YSystemUsersLoginrecords) {
		this.YSystemUsersLoginrecords = YSystemUsersLoginrecords;
	}
	@Transient
	public String getfSGName() {
		return fSGName;
	}
	public void setfSGName(String fSGName) {
		this.fSGName = fSGName;
	}
	
	
}