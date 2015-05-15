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
 * YAudit entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "y_commentscore" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YCommentScore implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private String fid;
	private String tel; //用户电话
	private Date updateTime;//更新时间
	private String demandOrSupplyId;//供应需求id
	private String score;//得分
	private String remark;//备注
	private Integer type;//供应还是需求 类型 0是供应，1是需求
	private Integer flag;

	
	public YCommentScore() {
		super();
		// TODO Auto-generated constructor stub
	}


	public YCommentScore(String fid, String tel, Date updateTime,
			String demandOrSupplyId, String score, String remark, Integer type,
			Integer flag) {
		super();
		this.fid = fid;
		this.tel = tel;
		this.updateTime = updateTime;
		this.demandOrSupplyId = demandOrSupplyId;
		this.score = score;
		this.remark = remark;
		this.type = type;
		this.flag = flag;
	}


	public String getDemandOrSupplyId() {
		return demandOrSupplyId;
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


	public Integer getFlag() {
		return flag;
	}


	public String getRemark() {
		return remark;
	}


	public String getScore() {
		return score;
	}


	public String getTel() {
		return tel;
	}


	public Integer getType() {
		return type;
	}


	public Date getUpdateTime() {
		return updateTime;
	}


	public void setDemandOrSupplyId(String demandOrSupplyId) {
		this.demandOrSupplyId = demandOrSupplyId;
	}


	public void setFid(String fid) {
		this.fid = fid;
	}


	public void setFlag(Integer flag) {
		this.flag = flag;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public void setScore(String score) {
		this.score = score;
	}


	public void setTel(String tel) {
		this.tel = tel;
	}


	public void setType(Integer type) {
		this.type = type;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
	