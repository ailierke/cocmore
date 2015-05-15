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
 * YBasicImaccount entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_imaccount" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicImaccount implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private String fimkey;
	private String fimpassword;
	private Integer flag;
	private Set<YBasicGrouppeople> YBasicGrouppeoples = new HashSet<YBasicGrouppeople>(
			0);
	private String fimtel;

	// Constructors

	/** default constructor */
	public YBasicImaccount() {
	}

	/** minimal constructor */
	public YBasicImaccount(String fid) {
		this.fid = fid;
	}

	public YBasicImaccount(String fid, String fimkey, String fimpassword,
			Integer flag, Set<YBasicGrouppeople> yBasicGrouppeoples,
			String fimtel) {
		super();
		this.fid = fid;
		this.fimkey = fimkey;
		this.fimpassword = fimpassword;
		this.flag = flag;
		YBasicGrouppeoples = yBasicGrouppeoples;
		this.fimtel = fimtel;
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

	@Column(name = "FIMkey", length = 100)
	public String getFimkey() {
		return this.fimkey;
	}

	@Column(name = "FIMPassword", length = 100)
	public String getFimpassword() {
		return this.fimpassword;
	}
	@Column(name = "fimtel", length = 100)
	public String getFimtel() {
		return fimtel;
	}


	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicImaccount")
	@JsonIgnore
	public Set<YBasicGrouppeople> getYBasicGrouppeoples() {
		return this.YBasicGrouppeoples;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public void setFimkey(String fimkey) {
		this.fimkey = fimkey;
	}

	public void setFimpassword(String fimpassword) {
		this.fimpassword = fimpassword;
	}

	public void setFimtel(String fimtel) {
		this.fimtel = fimtel;
	}
	
	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public void setYBasicGrouppeoples(Set<YBasicGrouppeople> YBasicGrouppeoples) {
		this.YBasicGrouppeoples = YBasicGrouppeoples;
	}
}