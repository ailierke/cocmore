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
 * YBasicMembercompany entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_basic_membercompany" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicMembercompany implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fcid;
	private YBasicIndustry YBasicIndustry;
	private YBasicMember YBasicMember;
	private YBasicTrade YBasicTrade;
	private YBasicSocialgroups YBasicSocialgroups;
	private String fcname;
	private String fctelphone;
	private String fcemail;
	private String fcurl;
	private String fclocation;
	private String fcproducts;
	private String fcintroduction;
	private String fcompanyPosition;
	private String fcompanyNature;
	private String fcompanyScale;
	private String fcompanyLatitude;
	private String fcompanyLogo;
	private Integer flag;
	
	private Set<YCompanyproduct> companyproducts = new HashSet<YCompanyproduct>(
			0);

	// Constructors

	/** default constructor */
	public YBasicMembercompany() {
	}

	/** minimal constructor */
	public YBasicMembercompany(String fcid) {
		this.fcid = fcid;
	}

	/** full constructor */
	public YBasicMembercompany(String fcid, YBasicIndustry YBasicIndustry,
			YBasicMember YBasicMember, YBasicTrade YBasicTrade,
			YBasicSocialgroups YBasicSocialgroups, String fcname,
			String fctelphone, String fcemail, String fcurl, String fclocation,
			String fcproducts, String fcintroduction, String fcompanyPosition,
			String fcompanyNature, String fcompanyScale, String fcompanyLatitude) {
		this.fcid = fcid;
		this.YBasicIndustry = YBasicIndustry;
		this.YBasicMember = YBasicMember;
		this.YBasicTrade = YBasicTrade;
		this.YBasicSocialgroups = YBasicSocialgroups;
		this.fcname = fcname;
		this.fctelphone = fctelphone;
		this.fcemail = fcemail;
		this.fcurl = fcurl;
		this.fclocation = fclocation;
		this.fcproducts = fcproducts;
		this.fcintroduction = fcintroduction;
		this.fcompanyPosition = fcompanyPosition;
		this.fcompanyNature = fcompanyNature;
		this.fcompanyScale = fcompanyScale;
		this.fcompanyLatitude = fcompanyLatitude;
	}

	// Property accessors
	@Id
	@Column(name = "FCID", unique = true, nullable = false, length = 36)
	public String getFcid() {
		if (!StringUtils.isBlank(this.fcid)) {
			return this.fcid;
		}
		return UUID.randomUUID().toString();
	}

	public void setFcid(String fcid) {
		this.fcid = fcid;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FIndustryID")
	public YBasicIndustry getYBasicIndustry() {
		return this.YBasicIndustry;
	}

	public void setYBasicIndustry(YBasicIndustry YBasicIndustry) {
		this.YBasicIndustry = YBasicIndustry;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FMemberID")
	public YBasicMember getYBasicMember() {
		return this.YBasicMember;
	}

	public void setYBasicMember(YBasicMember YBasicMember) {
		this.YBasicMember = YBasicMember;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FTradeID")
	public YBasicTrade getYBasicTrade() {
		return this.YBasicTrade;
	}

	public void setYBasicTrade(YBasicTrade YBasicTrade) {
		this.YBasicTrade = YBasicTrade;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FSocialGroupsID")
	public YBasicSocialgroups getYBasicSocialgroups() {
		return this.YBasicSocialgroups;
	}

	public void setYBasicSocialgroups(YBasicSocialgroups YBasicSocialgroups) {
		this.YBasicSocialgroups = YBasicSocialgroups;
	}

	@Column(name = "FCName", length = 2000)
	public String getFcname() {
		return this.fcname;
	}

	public void setFcname(String fcname) {
		this.fcname = fcname;
	}

	@Column(name = "FCTelphone", length = 36)
	public String getFctelphone() {
		return this.fctelphone;
	}

	public void setFctelphone(String fctelphone) {
		this.fctelphone = fctelphone;
	}

	@Column(name = "FCEmail", length = 1000)
	public String getFcemail() {
		return this.fcemail;
	}

	public void setFcemail(String fcemail) {
		this.fcemail = fcemail;
	}

	@Column(name = "FCUrl", length = 300)
	public String getFcurl() {
		return this.fcurl;
	}

	public void setFcurl(String fcurl) {
		this.fcurl = fcurl;
	}

	@Column(name = "FCLocation", length = 500)
	public String getFclocation() {
		return this.fclocation;
	}

	public void setFclocation(String fclocation) {
		this.fclocation = fclocation;
	}

	@Column(name = "FCProducts", length = 65535)
	public String getFcproducts() {
		return this.fcproducts;
	}

	public void setFcproducts(String fcproducts) {
		this.fcproducts = fcproducts;
	}

	@Column(name = "FCIntroduction", length = 65535)
	public String getFcintroduction() {
		return this.fcintroduction;
	}

	public void setFcintroduction(String fcintroduction) {
		this.fcintroduction = fcintroduction;
	}

	@Column(name = "FCompanyPosition", length = 100)
	public String getFcompanyPosition() {
		return this.fcompanyPosition;
	}

	public void setFcompanyPosition(String fcompanyPosition) {
		this.fcompanyPosition = fcompanyPosition;
	}

	@Column(name = "FCompanyNature", length = 100)
	public String getFcompanyNature() {
		return this.fcompanyNature;
	}

	public void setFcompanyNature(String fcompanyNature) {
		this.fcompanyNature = fcompanyNature;
	}

	@Column(name = "FCompanyScale", length = 100)
	public String getFcompanyScale() {
		return this.fcompanyScale;
	}

	public void setFcompanyScale(String fcompanyScale) {
		this.fcompanyScale = fcompanyScale;
	}

	@Column(name = "FCompanyLatitude", length = 100)
	public String getFcompanyLatitude() {
		return this.fcompanyLatitude;
	}

	public void setFcompanyLatitude(String fcompanyLatitude) {
		this.fcompanyLatitude = fcompanyLatitude;
	}
	@Column(name = "FCompanyLogo")
	public String getFcompanyLogo() {
		return fcompanyLogo;
	}

	public void setFcompanyLogo(String fcompanyLogo) {
		this.fcompanyLogo = fcompanyLogo;
	}

	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "YBasicMembercompany")
	@JsonIgnore
	public Set<YCompanyproduct> getCompanyproducts() {
		return companyproducts;
	}

	public void setCompanyproducts(Set<YCompanyproduct> companyproducts) {
		this.companyproducts = companyproducts;
	}
	
	
}