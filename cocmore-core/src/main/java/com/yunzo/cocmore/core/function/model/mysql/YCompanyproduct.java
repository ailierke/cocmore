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
 * YCompanyproduct entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_companyproduct" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YCompanyproduct implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private String fname;
	private String flogoImage;
	private String fdescription;
	private YBasicMembercompany YBasicMembercompany;//private String fcompanyId;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YCompanyproduct() {
	}

	/** minimal constructor */
	public YCompanyproduct(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YCompanyproduct(String fid, String fname, String flogoImage,
			String fdescription,String fcompanyId) {
		this.fid = fid;
		this.fname = fname;
		this.flogoImage = flogoImage;
		this.fdescription = fdescription;
		this.YBasicMembercompany = YBasicMembercompany;
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

	@Column(name = "FName", length = 100)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "FLogoImage", length = 65535)
	public String getFlogoImage() {
		return this.flogoImage;
	}

	public void setFlogoImage(String flogoImage) {
		this.flogoImage = flogoImage;
	}

	@Column(name = "FDescription", length = 65535)
	public String getFdescription() {
		return this.fdescription;
	}

	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FCompanyId")
	public YBasicMembercompany getYBasicMembercompany() {
		return YBasicMembercompany;
	}

	public void setYBasicMembercompany(YBasicMembercompany yBasicMembercompany) {
		YBasicMembercompany = yBasicMembercompany;
	}

	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}