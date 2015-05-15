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

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author：jackpeng
 * @date：2014年12月9日上午10:15:30
 * 
 */
@Entity
@Table(name = "y_initiationapply" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YInitiationApply implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String fid;	//主键	VARCHAR	36	唯一约束、主键约束、索引
	private String fname;	//姓名	VARCHAR	100	申请人姓名
	private String fphone;	//联系电话	VARCHAR	50	申请人联系电话
	private String fcompanyName;	//公司名称	VARCHAR	100	申请人所在公司的名称
	private String fcompanyPosition;	//公司职位	VARCHAR	50	申请人所在公司所属职位
	private String fnativePlace;	//籍贯	VARCHAR	36	申请人所属籍贯
	private String fgroupsId;//社会团体
	private Integer fstate;	//状态	INT		状态
	private Integer flag;
	private Date fapplyDate;	//申请时间
	private String fmemberId;   //申请人id
	private String fisHide;   //是否隐藏
	
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
	@Column(name = "FName", length = 100)
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	@Column(name = "FPhone", length = 50)
	public String getFphone() {
		return fphone;
	}
	public void setFphone(String fphone) {
		this.fphone = fphone;
	}
	@Column(name = "FCompanyName", length = 100)
	public String getFcompanyName() {
		return fcompanyName;
	}
	public void setFcompanyName(String fcompanyName) {
		this.fcompanyName = fcompanyName;
	}
	@Column(name = "FCompanyPosition", length = 50)
	public String getFcompanyPosition() {
		return fcompanyPosition;
	}
	public void setFcompanyPosition(String fcompanyPosition) {
		this.fcompanyPosition = fcompanyPosition;
	}
	@Column(name = "FNativePlace", length = 36)
	public String getFnativePlace() {
		return fnativePlace;
	}
	public void setFnativePlace(String fnativePlace) {
		this.fnativePlace = fnativePlace;
	}
	@Column(name = "FGroupsId")
	public String getFgroupsId() {
		return fgroupsId;
	}
	public void setFgroupsId(String fgroupsId) {
		this.fgroupsId = fgroupsId;
	}
	@Column(name = "FState")
	public Integer getFstate() {
		return fstate;
	}
	public void setFstate(Integer fstate) {
		this.fstate = fstate;
	}
	
	@Column(name = "FLag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
	@Column(name = "FApplyDate", length = 19)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getFapplyDate() {
		return fapplyDate;
	}
	public void setFapplyDate(Date fapplyDate) {
		this.fapplyDate = fapplyDate;
	}
	@Column(name = "FmemberId")
	public String getFmemberId() {
		return fmemberId;
	}
	public void setFmemberId(String fmemberId) {
		this.fmemberId = fmemberId;
	}
	@Column(name = "FisHide")
	public String getFisHide() {
		return fisHide;
	}
	public void setFisHide(String fisHide) {
		this.fisHide = fisHide;
	}
	
	
}
