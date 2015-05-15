package com.yunzo.cocmore.core.function.vo;

import java.util.Date;

import com.yunzo.cocmore.core.function.model.mysql.YBasicCity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCounty;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicProvince;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicTrade;


public class GuaranteeVo {
	private String fid;
	private String ispass;
	private String groupid;//需要做担保的商会id
	private String content;//担保内容
	/**
	 * 以下供应信息
	 */
	private YBasicCity YBasicCity;//城市
	private YBasicTrade YBasicTrade;//行业
	private YBasicCounty YBasicCounty;//区县
	private YBasicProvince YBasicProvince;//省份
	private YBasicSocialgroups YBasicSocialgroups;//这条供应信息所属人所在商会
	private YBasicMember YBasicMember;//会员
	private String fnumber;
	private String fheadline;
	private String fimages;
	private String fmessage;
	private String ftel;
	private String fnationalCertification;
	private Integer fareGuarantee;
	private String fcontacts;
	private String fexpireTime;
	private String fauditTime;
	private String fpublisherTime;
	private String fauditIdea;
	private Integer fbillState;
	private String fcomment;
	private Integer flag;
	private Integer fisHide;
	private Integer flevel;
	public GuaranteeVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public GuaranteeVo(
			String fid,
			String ispass,
			String groupid,
			com.yunzo.cocmore.core.function.model.mysql.YBasicCity yBasicCity,
			com.yunzo.cocmore.core.function.model.mysql.YBasicTrade yBasicTrade,
			com.yunzo.cocmore.core.function.model.mysql.YBasicCounty yBasicCounty,
			com.yunzo.cocmore.core.function.model.mysql.YBasicProvince yBasicProvince,
			com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups yBasicSocialgroups,
			com.yunzo.cocmore.core.function.model.mysql.YBasicMember yBasicMember,
			String fnumber, String fheadline, String fimages, String fmessage,
			String ftel, String fnationalCertification, Integer fareGuarantee,
			String fcontacts, String fexpireTime, String fauditTime,
			String fpublisherTime, String fauditIdea, Integer fbillState,
			String fcomment, Integer flag, Integer fisHide, Integer flevel) {
		super();
		this.fid = fid;
		this.ispass = ispass;
		this.groupid = groupid;
		YBasicCity = yBasicCity;
		YBasicTrade = yBasicTrade;
		YBasicCounty = yBasicCounty;
		YBasicProvince = yBasicProvince;
		YBasicSocialgroups = yBasicSocialgroups;
		YBasicMember = yBasicMember;
		this.fnumber = fnumber;
		this.fheadline = fheadline;
		this.fimages = fimages;
		this.fmessage = fmessage;
		this.ftel = ftel;
		this.fnationalCertification = fnationalCertification;
		this.fareGuarantee = fareGuarantee;
		this.fcontacts = fcontacts;
		this.fexpireTime = fexpireTime;
		this.fauditTime = fauditTime;
		this.fpublisherTime = fpublisherTime;
		this.fauditIdea = fauditIdea;
		this.fbillState = fbillState;
		this.fcomment = fcomment;
		this.flag = flag;
		this.fisHide = fisHide;
		this.flevel = flevel;
	}
	public GuaranteeVo(
			String fid,
			String ispass,
			String groupid,
			String content,
			com.yunzo.cocmore.core.function.model.mysql.YBasicCity yBasicCity,
			com.yunzo.cocmore.core.function.model.mysql.YBasicTrade yBasicTrade,
			com.yunzo.cocmore.core.function.model.mysql.YBasicCounty yBasicCounty,
			com.yunzo.cocmore.core.function.model.mysql.YBasicProvince yBasicProvince,
			com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups yBasicSocialgroups,
			com.yunzo.cocmore.core.function.model.mysql.YBasicMember yBasicMember,
			String fnumber, String fheadline, String fimages, String fmessage,
			String ftel, String fnationalCertification, Integer fareGuarantee,
			String fcontacts, String fexpireTime, String fauditTime,
			String fpublisherTime, String fauditIdea, Integer fbillState,
			String fcomment, Integer flag, Integer fisHide, Integer flevel) {
		super();
		this.fid = fid;
		this.ispass = ispass;
		this.groupid = groupid;
		this.content = content;
		YBasicCity = yBasicCity;
		YBasicTrade = yBasicTrade;
		YBasicCounty = yBasicCounty;
		YBasicProvince = yBasicProvince;
		YBasicSocialgroups = yBasicSocialgroups;
		YBasicMember = yBasicMember;
		this.fnumber = fnumber;
		this.fheadline = fheadline;
		this.fimages = fimages;
		this.fmessage = fmessage;
		this.ftel = ftel;
		this.fnationalCertification = fnationalCertification;
		this.fareGuarantee = fareGuarantee;
		this.fcontacts = fcontacts;
		this.fexpireTime = fexpireTime;
		this.fauditTime = fauditTime;
		this.fpublisherTime = fpublisherTime;
		this.fauditIdea = fauditIdea;
		this.fbillState = fbillState;
		this.fcomment = fcomment;
		this.flag = flag;
		this.fisHide = fisHide;
		this.flevel = flevel;
	}
	public String getContent() {
		return content;
	}
	public Integer getFareGuarantee() {
		return fareGuarantee;
	}
	public String getFauditIdea() {
		return fauditIdea;
	}
	public String getFauditTime() {
		return fauditTime;
	}
	public Integer getFbillState() {
		return fbillState;
	}
	public String getFcomment() {
		return fcomment;
	}
	public String getFcontacts() {
		return fcontacts;
	}
	public String getFexpireTime() {
		return fexpireTime;
	}
	public String getFheadline() {
		return fheadline;
	}
	public String getFid() {
		return fid;
	}
	public String getFimages() {
		return fimages;
	}
	public Integer getFisHide() {
		return fisHide;
	}
	public Integer getFlag() {
		return flag;
	}
	public Integer getFlevel() {
		return flevel;
	}
	public String getFmessage() {
		return fmessage;
	}
	public String getFnationalCertification() {
		return fnationalCertification;
	}
	public String getFnumber() {
		return fnumber;
	}
	public String getFpublisherTime() {
		return fpublisherTime;
	}
	public String getFtel() {
		return ftel;
	}
	public String getGroupid() {
		return groupid;
	}
	public String getIspass() {
		return ispass;
	}
	public YBasicCity getYBasicCity() {
		return YBasicCity;
	}
	public YBasicCounty getYBasicCounty() {
		return YBasicCounty;
	}
	public YBasicMember getYBasicMember() {
		return YBasicMember;
	}
	public YBasicProvince getYBasicProvince() {
		return YBasicProvince;
	}
	public YBasicSocialgroups getYBasicSocialgroups() {
		return YBasicSocialgroups;
	}
	public YBasicTrade getYBasicTrade() {
		return YBasicTrade;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setFareGuarantee(Integer fareGuarantee) {
		this.fareGuarantee = fareGuarantee;
	}
	public void setFauditIdea(String fauditIdea) {
		this.fauditIdea = fauditIdea;
	}
	public void setFauditTime(String fauditTime) {
		this.fauditTime = fauditTime;
	}
	public void setFbillState(Integer fbillState) {
		this.fbillState = fbillState;
	}
	public void setFcomment(String fcomment) {
		this.fcomment = fcomment;
	}
	public void setFcontacts(String fcontacts) {
		this.fcontacts = fcontacts;
	}
	public void setFexpireTime(String fexpireTime) {
		this.fexpireTime = fexpireTime;
	}
	public void setFheadline(String fheadline) {
		this.fheadline = fheadline;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public void setFimages(String fimages) {
		this.fimages = fimages;
	}
	public void setFisHide(Integer fisHide) {
		this.fisHide = fisHide;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public void setFlevel(Integer flevel) {
		this.flevel = flevel;
	}
	public void setFmessage(String fmessage) {
		this.fmessage = fmessage;
	}
	public void setFnationalCertification(String fnationalCertification) {
		this.fnationalCertification = fnationalCertification;
	}
	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}
	public void setFpublisherTime(String fpublisherTime) {
		this.fpublisherTime = fpublisherTime;
	}
	public void setFtel(String ftel) {
		this.ftel = ftel;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public void setIspass(String ispass) {
		this.ispass = ispass;
	}
	public void setYBasicCity(YBasicCity yBasicCity) {
		YBasicCity = yBasicCity;
	}
	public void setYBasicCounty(YBasicCounty yBasicCounty) {
		YBasicCounty = yBasicCounty;
	}
	public void setYBasicMember(YBasicMember yBasicMember) {
		YBasicMember = yBasicMember;
	}
	public void setYBasicProvince(YBasicProvince yBasicProvince) {
		YBasicProvince = yBasicProvince;
	}
	public void setYBasicSocialgroups(YBasicSocialgroups yBasicSocialgroups) {
		YBasicSocialgroups = yBasicSocialgroups;
	}
	public void setYBasicTrade(YBasicTrade yBasicTrade) {
		YBasicTrade = yBasicTrade;
	}
}
