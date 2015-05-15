package com.yunzo.cocmore.core.function.model.mysql;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * YFunctionentriesFunctionjournalentry entity. @author MyEclipse Persistence
 * Tools
 */
@Entity
@Table(name = "y_functionentries_functionjournalentry" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YFunctionentriesFunctionjournalentry implements
		java.io.Serializable {

	// Fields

	private String fid;
	private YSystemconfigurationFunction YSystemconfigurationFunction;
	private String fseq;
	private String ffunctionName;
	private String fremark;
	private String fjscode;
	private Integer flag;
	private String fIsBasic;            //是否是基础功能
	private Set<YOrganizationauthorized> YOrganizationauthorizeds = new HashSet<YOrganizationauthorized>(
			0);
	private Set<YRoleauthorization> YRoleauthorizations = new HashSet<YRoleauthorization>(
			0);

	// Constructors

	/** default constructor */
	public YFunctionentriesFunctionjournalentry() {
	}

	/** minimal constructor */
	public YFunctionentriesFunctionjournalentry(String fid, String fseq) {
		this.fid = fid;
		this.fseq = fseq;
	}

	/** full constructor */
	public YFunctionentriesFunctionjournalentry(String fid,
			YSystemconfigurationFunction YSystemconfigurationFunction,
			String fseq, String ffunctionName, String fremark, String fjscode,
			Integer flag,String fIsBasic,
			Set<YOrganizationauthorized> YOrganizationauthorizeds,
			Set<YRoleauthorization> YRoleauthorizations) {
		this.fid = fid;
		this.YSystemconfigurationFunction = YSystemconfigurationFunction;
		this.fseq = fseq;
		this.ffunctionName = ffunctionName;
		this.fremark = fremark;
		this.fjscode = fjscode;
		this.flag = flag;
		this.fIsBasic = fIsBasic;
		this.YOrganizationauthorizeds = YOrganizationauthorizeds;
		this.YRoleauthorizations = YRoleauthorizations;
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
	@JoinColumn(name = "FParentID")
	public YSystemconfigurationFunction getYSystemconfigurationFunction() {
		return this.YSystemconfigurationFunction;
	}

	public void setYSystemconfigurationFunction(
			YSystemconfigurationFunction YSystemconfigurationFunction) {
		this.YSystemconfigurationFunction = YSystemconfigurationFunction;
	}

	@Column(name = "FSeq", nullable = false, length = 36)
	public String getFseq() {
		return this.fseq;
	}

	public void setFseq(String fseq) {
		this.fseq = fseq;
	}

	@Column(name = "FFunctionName", length = 36)
	public String getFfunctionName() {
		return this.ffunctionName;
	}

	public void setFfunctionName(String ffunctionName) {
		this.ffunctionName = ffunctionName;
	}

	@Column(name = "FRemark", length = 300)
	public String getFremark() {
		return this.fremark;
	}

	public void setFremark(String fremark) {
		this.fremark = fremark;
	}

	@Column(name = "Fjscode", length = 65535)
	public String getFjscode() {
		return this.fjscode;
	}

	public void setFjscode(String fjscode) {
		this.fjscode = fjscode;
	}

	@Column(name = "Flag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
	@Column(name = "FIsBasic", length = 36)
	public String getfIsBasic() {
		return fIsBasic;
	}

	public void setfIsBasic(String fIsBasic) {
		this.fIsBasic = fIsBasic;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YFunctionentriesFunctionjournalentry")
	@JsonIgnore
	public Set<YOrganizationauthorized> getYOrganizationauthorizeds() {
		return this.YOrganizationauthorizeds;
	}

	public void setYOrganizationauthorizeds(
			Set<YOrganizationauthorized> YOrganizationauthorizeds) {
		this.YOrganizationauthorizeds = YOrganizationauthorizeds;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YFunctionentriesFunctionjournalentry")
	@JsonIgnore
	public Set<YRoleauthorization> getYRoleauthorizations() {
		return this.YRoleauthorizations;
	}

	public void setYRoleauthorizations(
			Set<YRoleauthorization> YRoleauthorizations) {
		this.YRoleauthorizations = YRoleauthorizations;
	}

}