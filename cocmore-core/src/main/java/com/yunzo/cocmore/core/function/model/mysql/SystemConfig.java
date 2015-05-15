package com.yunzo.cocmore.core.function.model.mysql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Description: <>. <br>
 * @date:2015年3月13日 上午10:50:08
 * @author beck
 * @version V1.0
 */
@Entity
@Table(name = "y_system_config" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class SystemConfig {
	private static final long serialVersionUID = 1L;
	
	private Integer fid;
	private String fkey;
	private String fvalue;
	private String fcomment;
	
	@Id
	@Column(name = "FID", unique = true, nullable = false)
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	
	@Column(name = "FKey", length = 100)
	public String getFkey() {
		return fkey;
	}
	public void setFkey(String fkey) {
		this.fkey = fkey;
	}
	
	@Column(name = "FValue", length = 100)
	public String getFvalue() {
		return fvalue;
	}
	public void setFvalue(String fvalue) {
		this.fvalue = fvalue;
	}
	
	@Column(name = "FComment", length = 255)
	public String getFcomment() {
		return fcomment;
	}
	public void setFcomment(String fcomment) {
		this.fcomment = fcomment;
	}
	
	
}
