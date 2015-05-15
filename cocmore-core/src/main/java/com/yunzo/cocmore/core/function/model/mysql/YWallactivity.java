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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * YWallactivity entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_wallactivity" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YWallactivity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields

	private String fid;
	private YBasicCity YBasicCity;
	private YBasicProvince YBasicProvince;
	private YBasicSocialgroups YBasicSocialgroups;
	private String ftheme;
	private String ftheUrl;
	private Integer fstate;
	private Date fcreateTime;
	private Integer flag;
	private String fcomment;
	private Set<YWallsadvertising> YWallsadvertisings = new HashSet<YWallsadvertising>(
			0);
	private Set<YWallreply> YWallreplies = new HashSet<YWallreply>(0);
	private Set<FAwardSetting> FAwardSettings = new HashSet<FAwardSetting>(0);
	private Set<YBasicJoinActivity> YBasicJoinActivities = new HashSet<YBasicJoinActivity>(0);

	// Constructors

	/** default constructor */
	public YWallactivity() {
	}

	/** minimal constructor */
	public YWallactivity(String fid) {
		this.fid = fid;
	}


	public YWallactivity(
			String fid,
			com.yunzo.cocmore.core.function.model.mysql.YBasicCity yBasicCity,
			com.yunzo.cocmore.core.function.model.mysql.YBasicProvince yBasicProvince,
			com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups yBasicSocialgroups,
			String ftheme, String ftheUrl, Integer fstate, Date fcreateTime,
			Integer flag, Set<YWallsadvertising> yWallsadvertisings,
			Set<YWallreply> yWallreplies, Set<FAwardSetting> fAwardSettings,
			Set<YBasicJoinActivity> yBasicJoinActivities) {
		super();
		this.fid = fid;
		YBasicCity = yBasicCity;
		YBasicProvince = yBasicProvince;
		YBasicSocialgroups = yBasicSocialgroups;
		this.ftheme = ftheme;
		this.ftheUrl = ftheUrl;
		this.fstate = fstate;
		this.fcreateTime = fcreateTime;
		this.flag = flag;
		YWallsadvertisings = yWallsadvertisings;
		YWallreplies = yWallreplies;
		FAwardSettings = fAwardSettings;
		YBasicJoinActivities = yBasicJoinActivities;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YWallactivity")
	@JsonIgnore
	public Set<FAwardSetting> getFAwardSettings() {
		return this.FAwardSettings;
	}

	@Column(name = "FCreateTime", length = 19)
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

	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	@Column(name = "FState")
	public Integer getFstate() {
		return this.fstate;
	}

	@Column(name = "FTheme", length = 50)
	public String getFtheme() {
		return this.ftheme;
	}

	@Column(name = "FTheUrl", length = 100)
	public String getFtheUrl() {
		return this.ftheUrl;
	}
	
	@Column(name = "FComment")
	public String getFcomment() {
		return fcomment;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FCityID")
	public YBasicCity getYBasicCity() {
		return this.YBasicCity;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YWallactivity")
	@JsonIgnore
	public Set<YBasicJoinActivity> getYBasicJoinActivities() {
		return YBasicJoinActivities;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FProvincesID")
	public YBasicProvince getYBasicProvince() {
		return this.YBasicProvince;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FCommerceID")
	public YBasicSocialgroups getYBasicSocialgroups() {
		return this.YBasicSocialgroups;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YWallactivity")
	@JsonIgnore
	public Set<YWallreply> getYWallreplies() {
		return this.YWallreplies;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YWallactivity")
	@JsonIgnore
	public Set<YWallsadvertising> getYWallsadvertisings() {
		return this.YWallsadvertisings;
	}

	public void setFAwardSettings(Set<FAwardSetting> FAwardSettings) {
		this.FAwardSettings = FAwardSettings;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public void setFstate(Integer fstate) {
		this.fstate = fstate;
	}

	public void setFtheme(String ftheme) {
		this.ftheme = ftheme;
	}

	public void setFtheUrl(String ftheUrl) {
		this.ftheUrl = ftheUrl;
	}

	public void setYBasicCity(YBasicCity YBasicCity) {
		this.YBasicCity = YBasicCity;
	}

	public void setYBasicJoinActivities(Set<YBasicJoinActivity> yBasicJoinActivities) {
		YBasicJoinActivities = yBasicJoinActivities;
	}

	public void setYBasicProvince(YBasicProvince YBasicProvince) {
		this.YBasicProvince = YBasicProvince;
	}

	public void setYBasicSocialgroups(YBasicSocialgroups YBasicSocialgroups) {
		this.YBasicSocialgroups = YBasicSocialgroups;
	}
	
	public void setYWallreplies(Set<YWallreply> YWallreplies) {
		this.YWallreplies = YWallreplies;
	}

	public void setYWallsadvertisings(Set<YWallsadvertising> YWallsadvertisings) {
		this.YWallsadvertisings = YWallsadvertisings;
	}


	public void setFcomment(String fcomment) {
		this.fcomment = fcomment;
	}
	
}