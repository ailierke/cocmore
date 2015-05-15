package com.yunzo.cocmore.core.function.model.mysql;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
/**
 * @author：jackpeng
 * @date：2015年3月18日上午11:16:25
 * 我的供需评论推送信息表
 */

@Entity
@Table(name = "y_basic_demandsupplycment_pushinfo" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicDemandsupplycmentPushinfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String fmobilePhone;//电话号码（加密）	mobilephone	varchar(100)	100		TRUE	FALSE	TRUE
	private Integer fstatu;//是否有未读评论	statu	int			FALSE	FALSE	FALSE
	private Date fupdateTime;//修改时间	updatetime	datetime			FALSE	FALSE	FALSE
	private String fremark;//备注	remark	varchar(200)	200		FALSE	FALSE	FALSE
	
	@Id
	@Column(name = "FMobilephone", unique = true, nullable = false, length = 100)
	public String getFmobilePhone() {
		return fmobilePhone;
	}
	public void setFmobilePhone(String fmobilePhone) {
		this.fmobilePhone = fmobilePhone;
	}
	@Column(name = "FStatu")
	public Integer getFstatu() {
		return fstatu;
	}
	public void setFstatu(Integer fstatu) {
		this.fstatu = fstatu;
	}
	@Column(name = "FUpdatetime")
	public Date getFupdateTime() {
		return fupdateTime;
	}
	public void setFupdateTime(Date fupdateTime) {
		this.fupdateTime = fupdateTime;
	}
	@Column(name = "FRemark")
	public String getFremark() {
		return fremark;
	}
	public void setFremark(String fremark) {
		this.fremark = fremark;
	}
	
	
}
