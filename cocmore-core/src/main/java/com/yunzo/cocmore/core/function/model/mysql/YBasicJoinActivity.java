package com.yunzo.cocmore.core.function.model.mysql;

import java.util.UUID;

import javax.annotation.Generated;
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
 * 活动上墙人员参加表
 * @author ailierke
 *
 */
@Entity
@Table(name = "y_basic_joinactivity" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class YBasicJoinActivity {
	public YBasicJoinActivity() {
		super();
	}
	private String fid;
	private String userName;//姓名
	private String tel;//电话
	private Integer setNumber;//座位号
	private String groupName;//团体名称
	private YWallactivity YWallactivity;
	public YBasicJoinActivity(String fid) {
		super();
		this.fid = fid;
	}
	public YBasicJoinActivity(
			String fid,
			String userName,
			String tel,
			int setNumber,
			String groupName,
			com.yunzo.cocmore.core.function.model.mysql.YWallactivity yWallactivity) {
		super();
		this.fid = fid;
		this.userName = userName;
		this.tel = tel;
		this.setNumber = setNumber;
		this.groupName = groupName;
		YWallactivity = yWallactivity;
	}
	@Id
	@Column(name = "fid", unique = true, nullable = false, length = 36)
	public String getFid() {
		if (!StringUtils.isBlank(this.fid)) {
			return this.fid;
		}
		return UUID.randomUUID().toString();
	}
	public String getGroupName() {
		return groupName;
	}
	public Integer getSetNumber() {
		return setNumber;
	}
	public String getTel() {
		return tel;
	}
	public String getUserName() {
		return userName;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "activityId")
	public YWallactivity getYWallactivity() {
		return YWallactivity;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public void setSetNumber(Integer setNumber) {
		this.setNumber = setNumber;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setYWallactivity(YWallactivity yWallactivity) {
		YWallactivity = yWallactivity;
	}
}
