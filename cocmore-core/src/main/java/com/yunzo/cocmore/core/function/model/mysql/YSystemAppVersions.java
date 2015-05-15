package com.yunzo.cocmore.core.function.model.mysql;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Description: <版本信息>. <br>
 * @date:2014年12月13日 上午11:40:37
 * @author beck
 * @version V1.0
 */
@Entity
@Table(name = "y_system_appversions" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YSystemAppVersions implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// Fields
	private String fid;
	private String versionNumber;
	private String downloadUrl;
	private String updateDetail;
	private Integer flag;
	private Integer chanelNo;
	
	/** default constructor */
	public YSystemAppVersions() {
	}
	
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
	
	@Column(name = "FVersionNumber")
	public String getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
	
	@Column(name = "FDownloadUrl")
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	
	@Column(name = "FUpdateDetail")
	public String getUpdateDetail() {
		return updateDetail;
	}
	public void setUpdateDetail(String updateDetail) {
		this.updateDetail = updateDetail;
	}
	
	@Column(name = "Flag")
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@Column(name = "ChanelNo")
	public Integer getChanelNo() {
		return chanelNo;
	}

	public void setChanelNo(Integer chanelNo) {
		this.chanelNo = chanelNo;
	}
	
	
	
}
