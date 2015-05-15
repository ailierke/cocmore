package com.yunzo.cocmore.core.function.model.mysql;

import java.util.Date;
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
 * FAwardsRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "f_awards_record" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class FAwardsRecord implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private FAwardSetting FAwardSetting;
	private String fmemberId;
	private String fthemeId;
	private Date fawardDate;
	private Integer flag;

	// Constructors

	/** default constructor */
	public FAwardsRecord() {
	}

	/** minimal constructor */
	public FAwardsRecord(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public FAwardsRecord(String fid, FAwardSetting FAwardSetting,
			String fmemberId, String fthemeId, Date fawardDate) {
		this.fid = fid;
		this.FAwardSetting = FAwardSetting;
		this.fmemberId = fmemberId;
		this.fthemeId = fthemeId;
		this.fawardDate = fawardDate;
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
	@JoinColumn(name = "FAwardSettingID")
	public FAwardSetting getFAwardSetting() {
		return this.FAwardSetting;
	}

	public void setFAwardSetting(FAwardSetting FAwardSetting) {
		this.FAwardSetting = FAwardSetting;
	}

	@Column(name = "FMemberID", length = 36)
	public String getFmemberId() {
		return this.fmemberId;
	}

	public void setFmemberId(String fmemberId) {
		this.fmemberId = fmemberId;
	}

	@Column(name = "FThemeID", length = 36)
	public String getFthemeId() {
		return this.fthemeId;
	}

	public void setFthemeId(String fthemeId) {
		this.fthemeId = fthemeId;
	}

	@Column(name = "FAwardDate", length = 19)
	public Date getFawardDate() {
		return this.fawardDate;
	}

	public void setFawardDate(Date fawardDate) {
		this.fawardDate = fawardDate;
	}

	@Column(name = "FLag")
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}