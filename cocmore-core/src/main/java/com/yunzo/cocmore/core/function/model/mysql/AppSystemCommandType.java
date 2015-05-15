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
 * Description: <手机执行命令类型>. <br>
 * @date:2015年3月12日 下午3:31:12
 * @author beck
 * @version V1.0
 */
@Entity
@Table(name = "Y_appSystemCommandType" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class AppSystemCommandType {
	private static final long serialVersionUID = 1L;
	
	private String fid;
	private Integer fascTypeFlag;
	private Integer fbillState;
	private String fcomment;
	
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
	
	@Column(name = "FascTypeFlag")
	public Integer getFascTypeFlag() {
		return fascTypeFlag;
	}
	public void setFascTypeFlag(Integer fascTypeFlag) {
		this.fascTypeFlag = fascTypeFlag;
	}
	
	
	@Column(name = "FBillState")
	public Integer getFbillState() {
		return fbillState;
	}
	public void setFbillState(Integer fbillState) {
		this.fbillState = fbillState;
	}
	
	@Column(name = "FComment", length = 255)
	public String getFcomment() {
		return fcomment;
	}
	public void setFcomment(String fcomment) {
		this.fcomment = fcomment;
	}
	
	
}
