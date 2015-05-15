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
 * YBasicentriesEmployeedistribution entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basicentries_employeedistribution" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicentriesEmployeedistribution implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private YBasicPosition YBasicPosition;
	private YBasicEmployee YBasicEmployee;
	private String fkeyPost;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YBasicentriesEmployeedistribution() {
	}

	/** minimal constructor */
	public YBasicentriesEmployeedistribution(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YBasicentriesEmployeedistribution(String fid,
			YBasicPosition YBasicPosition, YBasicEmployee YBasicEmployee,
			String fkeyPost) {
		this.fid = fid;
		this.YBasicPosition = YBasicPosition;
		this.YBasicEmployee = YBasicEmployee;
		this.fkeyPost = fkeyPost;
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
	@JoinColumn(name = "FPositionID")
	public YBasicPosition getYBasicPosition() {
		return this.YBasicPosition;
	}

	public void setYBasicPosition(YBasicPosition YBasicPosition) {
		this.YBasicPosition = YBasicPosition;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FEmployeeID")
	public YBasicEmployee getYBasicEmployee() {
		return this.YBasicEmployee;
	}

	public void setYBasicEmployee(YBasicEmployee YBasicEmployee) {
		this.YBasicEmployee = YBasicEmployee;
	}

	@Column(name = "FKeyPost")
	public String getFkeyPost() {
		return this.fkeyPost;
	}

	public void setFkeyPost(String fkeyPost) {
		this.fkeyPost = fkeyPost;
	}

	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}