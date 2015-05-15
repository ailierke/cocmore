package com.yunzo.cocmore.core.function.model.mysql;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * YBasicTemplate entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_template" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicTemplate implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private String fcontent;
	private String faddress;
	private Integer ftype;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YBasicTemplate() {
	}

	/** minimal constructor */
	public YBasicTemplate(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicTemplate(String fid, String fcontent, String faddress,
			Integer ftype) {
		this.fid = fid;
		this.fcontent = fcontent;
		this.faddress = faddress;
		this.ftype = ftype;
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

	@Column(name = "FContent", length = 65535)
	public String getFcontent() {
		return this.fcontent;
	}

	public void setFcontent(String fcontent) {
		this.fcontent = fcontent;
	}

	@Column(name = "FAddress", length = 200)
	public String getFaddress() {
		return this.faddress;
	}

	public void setFaddress(String faddress) {
		this.faddress = faddress;
	}

	@Column(name = "FType")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}