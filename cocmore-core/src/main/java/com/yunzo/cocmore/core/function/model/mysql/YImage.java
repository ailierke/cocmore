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
 * YImage entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_image" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YImage implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private YManshowinformation YManshowinformation;
	private String faddress;
	private Integer flag;

	// Constructors

	/** default constructor */
	public YImage() {
	}

	/** minimal constructor */
	public YImage(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public YImage(String fid, YManshowinformation YManshowinformation,
			String faddress) {
		this.fid = fid;
		this.YManshowinformation = YManshowinformation;
		this.faddress = faddress;
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
	@JoinColumn(name = "FManShowID")
	public YManshowinformation getYManshowinformation() {
		return this.YManshowinformation;
	}

	public void setYManshowinformation(YManshowinformation YManshowinformation) {
		this.YManshowinformation = YManshowinformation;
	}

	@Column(name = "FAddress", length = 300)
	public String getFaddress() {
		return this.faddress;
	}

	public void setFaddress(String faddress) {
		this.faddress = faddress;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}