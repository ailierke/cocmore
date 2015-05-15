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
 * YComplaint entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_complaint" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YComplaint implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private String fsupplyDemandId;
	private Integer ftype;
	private Integer flag;
private String complaintId;
	// Constructors

	/** default constructor */
	public YComplaint() {
	}

	/** minimal constructor */
	public YComplaint(String fid) {
		this.fid = fid;
	}


	public String getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(String complaintId) {
		this.complaintId = complaintId;
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

	public YComplaint(String fid, String fsupplyDemandId, Integer ftype,
			Integer flag, String complaintId) {
		super();
		this.fid = fid;
		this.fsupplyDemandId = fsupplyDemandId;
		this.ftype = ftype;
		this.flag = flag;
		this.complaintId = complaintId;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	@Column(name = "FSupplyDemandID", length = 36)
	public String getFsupplyDemandId() {
		return this.fsupplyDemandId;
	}

	public void setFsupplyDemandId(String fsupplyDemandId) {
		this.fsupplyDemandId = fsupplyDemandId;
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