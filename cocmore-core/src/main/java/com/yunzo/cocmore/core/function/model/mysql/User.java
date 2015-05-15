package com.yunzo.cocmore.core.function.model.mysql;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.context.annotation.Lazy;
@Entity
@Table(name ="y_system_users")
@Lazy(value=true)
@XmlRootElement
public class User {
	private static final long serialVersionUID = -827759092352842181L;
	
	@Id
	@Column(name="FID")
	private String fid;

	@Column(name="FAccount")
	private String faccount;
	
	@Column(name="FUserPassword")
	private String fpassword;
	
	@Column(name="FBillState")
	private Integer fbillstate;
	
	@XmlElement
	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}
	@XmlElement
	public String getFaccount() {
		return faccount;
	}

	public void setFaccount(String faccount) {
		this.faccount = faccount;
	}
	@XmlElement
	public String getFpassword() {
		return fpassword;
	}

	public void setFpassword(String fpassword) {
		this.fpassword = fpassword;
	}
	@XmlElement
	public Integer getFbillstate() {
		return fbillstate;
	}

	public void setFbillstate(Integer fbillstate) {
		this.fbillstate = fbillstate;
	}
}
