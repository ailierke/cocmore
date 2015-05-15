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

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * YSystemconfigurationMenu entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_systemconfiguration_menu" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YSystemconfigurationMenu implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private String fnumber;
	private String fmenuName;
	private String fsuperiorId;
	private String fpath;
	private String fremark;
	private Integer flag;
	private Set<YSystemconfigurationFunction> YSystemconfigurationFunctions = new HashSet<YSystemconfigurationFunction>(
			0);

	// Constructors

	/** default constructor */
	public YSystemconfigurationMenu() {
	}

	/** minimal constructor */
	public YSystemconfigurationMenu(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YSystemconfigurationMenu(String fid, String fnumber,
			String fmenuName, String fsuperiorId, String fpath, String fremark,
			Integer flag,
			Set<YSystemconfigurationFunction> YSystemconfigurationFunctions) {
		this.fid = fid;
		this.fnumber = fnumber;
		this.fmenuName = fmenuName;
		this.fsuperiorId = fsuperiorId;
		this.fpath = fpath;
		this.fremark = fremark;
		this.flag = flag;
		this.YSystemconfigurationFunctions = YSystemconfigurationFunctions;
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

	@Column(name = "FNumber", length = 36)
	public String getFnumber() {
		return this.fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	@Column(name = "FMenuName", length = 36)
	public String getFmenuName() {
		return this.fmenuName;
	}

	public void setFmenuName(String fmenuName) {
		this.fmenuName = fmenuName;
	}

	@Column(name = "FSuperiorID", length = 36)
	public String getFsuperiorId() {
		return this.fsuperiorId;
	}

	public void setFsuperiorId(String fsuperiorId) {
		this.fsuperiorId = fsuperiorId;
	}

	@Column(name = "FPath", length = 36)
	public String getFpath() {
		return this.fpath;
	}

	public void setFpath(String fpath) {
		this.fpath = fpath;
	}

	@Column(name = "FRemark", length = 300)
	public String getFremark() {
		return this.fremark;
	}

	public void setFremark(String fremark) {
		this.fremark = fremark;
	}

	@Column(name = "Flag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YSystemconfigurationMenu")
	@JsonIgnore
	public Set<YSystemconfigurationFunction> getYSystemconfigurationFunctions() {
		return this.YSystemconfigurationFunctions;
	}

	public void setYSystemconfigurationFunctions(
			Set<YSystemconfigurationFunction> YSystemconfigurationFunctions) {
		this.YSystemconfigurationFunctions = YSystemconfigurationFunctions;
	}

}