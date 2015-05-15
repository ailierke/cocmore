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
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * YCharacter entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_character" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YCharacter implements java.io.Serializable {

	// Fields

	private String fid;
	//private YBasicOrganization YBasicOrganization;
	private String fnumber;
	private String fname;
	private String fremark;
	private Integer fstate;
	private String fcreaterId;
	private String fmodifiedId;
	private String flastModifiedId;
	private Date fcreateTime;
	private Date fmodifiedTime;
	private Date flastModifiedTime;
	//private String fgroupsID;
	//private String fisGroups;
	private Integer flag;
	private Integer fprivileges;
	private Set<YRoleauthorization> YRoleauthorizations = new HashSet<YRoleauthorization>(
			0);

	/** default constructor */
	public YCharacter() {
	}

	/** minimal constructor */
	public YCharacter(String fid) {
		this.fid = fid;
	}

	public YCharacter(String fid, String fnumber, String fname, String fremark,
			Integer fstate, String fcreaterId, String fmodifiedId,
			String flastModifiedId, Date fcreateTime, Date fmodifiedTime,
			Date flastModifiedTime, Integer flag, Integer fprivileges,
			Set<YRoleauthorization> yRoleauthorizations) {
		super();
		this.fid = fid;
		this.fnumber = fnumber;
		this.fname = fname;
		this.fremark = fremark;
		this.fstate = fstate;
		this.fcreaterId = fcreaterId;
		this.fmodifiedId = fmodifiedId;
		this.flastModifiedId = flastModifiedId;
		this.fcreateTime = fcreateTime;
		this.fmodifiedTime = fmodifiedTime;
		this.flastModifiedTime = flastModifiedTime;
		this.flag = flag;
		this.fprivileges = fprivileges;
		YRoleauthorizations = yRoleauthorizations;
	}
	public enum Status {
		//y_system_user和y_charater 中FPrivilege 0是未赋权1是赋权 2赋予角色权限 3是赋予用户权限  4是角色和用户都赋予 5待定
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

	/** full constructor */
	public YCharacter(String fid, YBasicOrganization YBasicOrganization,String fgroupsID,
			String fnumber, String fname, String fremark, Integer fstate,
			String fcreaterId, String fmodifiedId, String flastModifiedId,
			Date fcreateTime, Date fmodifiedTime, Date flastModifiedTime,
			String fisGroups,Integer flag, Set<YRoleauthorization> YRoleauthorizations) {
		this.fid = fid;
		//this.YBasicOrganization = YBasicOrganization;
		this.fnumber = fnumber;
		this.fname = fname;
		this.fremark = fremark;
		this.fstate = fstate;
		this.fcreaterId = fcreaterId;
		this.fmodifiedId = fmodifiedId;
		this.flastModifiedId = flastModifiedId;
		this.fcreateTime = fcreateTime;
		this.fmodifiedTime = fmodifiedTime;
		this.flastModifiedTime = flastModifiedTime;
		this.flag = flag;
		//this.fisGroups = fisGroups;
		//this.fgroupsID = fgroupsID;
		this.YRoleauthorizations = YRoleauthorizations;
	}

	@Column(name = "FCreaterID", length = 36)
	public String getFcreaterId() {
		return this.fcreaterId;
	}

	@Column(name = "FCreateTime", length = 19)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFcreateTime() {
		return this.fcreateTime;
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

	@Column(name = "Flag")
	public Integer getFlag() {
		return this.flag;
	}

	@Column(name = "FLastModifiedID", length = 36)
	public String getFlastModifiedId() {
		return this.flastModifiedId;
	}

	@Column(name = "FLastModifiedTime", length = 19)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFlastModifiedTime() {
		return this.flastModifiedTime;
	}

	@Column(name = "FModifiedID", length = 36)
	public String getFmodifiedId() {
		return this.fmodifiedId;
	}

	@Column(name = "FModifiedTime", length = 19)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFmodifiedTime() {
		return this.fmodifiedTime;
	}

	@Column(name = "FName", length = 36)
	public String getFname() {
		return this.fname;
	}

	@Column(name = "FNumber", length = 36)
	public String getFnumber() {
		return this.fnumber;
	}

	public Integer getFprivileges() {
		return fprivileges;
	}

	@Column(name = "FRemark", length = 300)
	public String getFremark() {
		return this.fremark;
	}

	@Column(name = "Fstate")
	public Integer getFstate() {
		return this.fstate;
	}

	//	@Column(name = "FGroupsID", length = 36)
//	public String getFgroupsID() {
//		return fgroupsID;
//	}
//
//	public void setFgroupsID(String fgroupsID) {
//		this.fgroupsID = fgroupsID;
//	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YCharacter")
	@JsonIgnore
	public Set<YRoleauthorization> getYRoleauthorizations() {
		return this.YRoleauthorizations;
	}

	public void setFcreaterId(String fcreaterId) {
		this.fcreaterId = fcreaterId;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "FOrganizationID")
//	public YBasicOrganization getYBasicOrganization() {
//		return this.YBasicOrganization;
//	}
//
//	public void setYBasicOrganization(YBasicOrganization YBasicOrganization) {
//		this.YBasicOrganization = YBasicOrganization;
//	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public void setFlastModifiedId(String flastModifiedId) {
		this.flastModifiedId = flastModifiedId;
	}

	public void setFlastModifiedTime(Date flastModifiedTime) {
		this.flastModifiedTime = flastModifiedTime;
	}

	public void setFmodifiedId(String fmodifiedId) {
		this.fmodifiedId = fmodifiedId;
	}

	public void setFmodifiedTime(Date fmodifiedTime) {
		this.fmodifiedTime = fmodifiedTime;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}
	
	
//	@Column(name = "FIsGroups", length = 36)
//	public String getFisGroups() {
//		return fisGroups;
//	}
//
//	public void setFisGroups(String fisGroups) {
//		this.fisGroups = fisGroups;
//	}

	public void setFprivileges(Integer fprivileges) {
		this.fprivileges = fprivileges;
	}

	public void setFremark(String fremark) {
		this.fremark = fremark;
	}
	
public void setFstate(Integer fstate) {
		this.fstate = fstate;
	}

	public void setYRoleauthorizations(
			Set<YRoleauthorization> YRoleauthorizations) {
		this.YRoleauthorizations = YRoleauthorizations;
	}

}