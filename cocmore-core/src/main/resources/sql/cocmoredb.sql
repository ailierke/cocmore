/*
Navicat MySQL Data Transfer

Source Server         : 10.0.0.21
Source Server Version : 50173
Source Host           : 10.0.0.21:3306
Source Database       : cocmoredb

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2015-05-14 12:15:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for c3p0_TestTable
-- ----------------------------
DROP TABLE IF EXISTS `c3p0_TestTable`;
CREATE TABLE `c3p0_TestTable` (
  `a` char(1) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for f_award_setting
-- ----------------------------
DROP TABLE IF EXISTS `f_award_setting`;
CREATE TABLE `f_award_setting` (
  `FID` varchar(36) NOT NULL,
  `FWallsID` varchar(36) DEFAULT NULL,
  `FAwardName` varchar(36) DEFAULT NULL,
  `FPrizeName` varchar(100) DEFAULT NULL,
  `FAwardOrder` int(11) DEFAULT NULL,
  `FRandom` int(11) DEFAULT NULL,
  `FAwardsPerson` int(11) DEFAULT NULL,
  `FDesignatedPerson` text,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_Setting__REFERENCE_WallActivity` (`FWallsID`) USING BTREE,
  CONSTRAINT `f_award_setting_ibfk_1` FOREIGN KEY (`FWallsID`) REFERENCES `y_wallactivity` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for f_awards_record
-- ----------------------------
DROP TABLE IF EXISTS `f_awards_record`;
CREATE TABLE `f_awards_record` (
  `FID` varchar(36) NOT NULL,
  `FMemberID` varchar(36) DEFAULT NULL,
  `FThemeID` varchar(36) DEFAULT NULL,
  `FAwardSettingID` varchar(36) DEFAULT NULL,
  `FAwardDate` datetime DEFAULT NULL,
  `FLag` int(11) DEFAULT NULL,
  PRIMARY KEY (`FID`),
  KEY `FK_Record_REFERENCE_Setting` (`FAwardSettingID`) USING BTREE,
  CONSTRAINT `f_awards_record_ibfk_1` FOREIGN KEY (`FAwardSettingID`) REFERENCES `f_award_setting` (`FID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for f_sms_sendrecord
-- ----------------------------
DROP TABLE IF EXISTS `f_sms_sendrecord`;
CREATE TABLE `f_sms_sendrecord` (
  `FID` varchar(36) NOT NULL,
  `FMemberID` varchar(36) DEFAULT NULL,
  `FMobilePhone` varchar(36) DEFAULT NULL,
  `FSendTime` datetime DEFAULT NULL,
  `FContent` text,
  `FGroupsID` varchar(36) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_SendRecord_REFERENCE_Member` (`FMemberID`) USING BTREE,
  KEY `FK_SendRecord_REFERENCE_SocialGroups` (`FGroupsID`) USING BTREE,
  CONSTRAINT `f_sms_sendrecord_ibfk_1` FOREIGN KEY (`FMemberID`) REFERENCES `y_basic_member` (`FID`),
  CONSTRAINT `f_sms_sendrecord_ibfk_2` FOREIGN KEY (`FGroupsID`) REFERENCES `y_basic_socialgroups` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=702 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_news_collect
-- ----------------------------
DROP TABLE IF EXISTS `t_news_collect`;
CREATE TABLE `t_news_collect` (
  `Ftid` varchar(36) NOT NULL,
  `Fnewsid` varchar(36) DEFAULT NULL,
  `Ftel` varchar(11) DEFAULT NULL,
  `Fcreate_time` datetime DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`Ftid`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_collect_REFERENCE_headline` (`Fnewsid`) USING BTREE,
  CONSTRAINT `t_news_collect_ibfk_1` FOREIGN KEY (`Fnewsid`) REFERENCES `t_news_headline` (`Ftid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_news_headline
-- ----------------------------
DROP TABLE IF EXISTS `t_news_headline`;
CREATE TABLE `t_news_headline` (
  `Ftid` varchar(36) NOT NULL,
  `Ftitle` varchar(64) DEFAULT NULL,
  `Fimage_url` varchar(200) DEFAULT NULL,
  `Ft_image_url` varchar(200) DEFAULT NULL,
  `Fnews_content` text,
  `Fsource` varchar(64) DEFAULT NULL,
  `Frelease_time` datetime DEFAULT NULL,
  `Fdetails_url` varchar(200) DEFAULT NULL,
  `Fclassification` varchar(36) DEFAULT NULL,
  `Fdescribe` varchar(40) DEFAULT NULL,
  `Ftype` int(11) DEFAULT NULL,
  `Fcreate_time` datetime DEFAULT NULL,
  `Fis_push` int(11) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`Ftid`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for temp
-- ----------------------------
DROP TABLE IF EXISTS `temp`;
CREATE TABLE `temp` (
  `FID` varchar(36) NOT NULL,
  `FUserName` varchar(100) DEFAULT NULL,
  `FDeviceId` varchar(65) DEFAULT NULL,
  `FdeviceOs` varchar(50) DEFAULT NULL,
  `FdeviceOsversion` varchar(50) DEFAULT NULL,
  `FdeviceType` varchar(50) DEFAULT NULL,
  `FappVersion` varchar(50) DEFAULT NULL,
  `FappChannelNo` varchar(50) DEFAULT NULL,
  `Fclient` varchar(50) DEFAULT NULL,
  `FbusinessId` varchar(36) DEFAULT NULL,
  `FsessionToken` varchar(100) DEFAULT NULL,
  `Fsign` varchar(100) DEFAULT NULL,
  `Fmd5Info` varchar(100) DEFAULT NULL,
  `Fmid` varchar(100) DEFAULT NULL,
  `FLag` int(11) NOT NULL DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_appdevice
-- ----------------------------
DROP TABLE IF EXISTS `y_appdevice`;
CREATE TABLE `y_appdevice` (
  `FID` varchar(36) NOT NULL,
  `FUserName` varchar(100) DEFAULT NULL,
  `FDeviceId` varchar(65) DEFAULT NULL,
  `FdeviceOs` varchar(50) DEFAULT NULL,
  `FdeviceOsversion` varchar(50) DEFAULT NULL,
  `FdeviceType` varchar(50) DEFAULT NULL,
  `FappVersion` varchar(50) DEFAULT NULL,
  `FappChannelNo` varchar(50) DEFAULT NULL,
  `Fclient` varchar(50) DEFAULT NULL,
  `FbusinessId` varchar(36) DEFAULT NULL,
  `FsessionToken` varchar(100) DEFAULT NULL,
  `Fsign` varchar(100) DEFAULT NULL,
  `Fmd5Info` varchar(100) DEFAULT NULL,
  `Fmid` varchar(100) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_Y_APPDevice_REFERENCE_SocialGroups` (`FbusinessId`) USING BTREE,
  CONSTRAINT `y_appdevice_ibfk_1` FOREIGN KEY (`FbusinessId`) REFERENCES `y_basic_socialgroups` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=26607 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for Y_appSystemCommandLog
-- ----------------------------
DROP TABLE IF EXISTS `Y_appSystemCommandLog`;
CREATE TABLE `Y_appSystemCommandLog` (
  `FID` varchar(36) NOT NULL,
  `Fmid` varchar(50) DEFAULT NULL,
  `FascTypeID` varchar(36) DEFAULT NULL,
  `FgroupID` varchar(36) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FextTime` datetime DEFAULT NULL,
  PRIMARY KEY (`FID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for Y_appSystemCommandType
-- ----------------------------
DROP TABLE IF EXISTS `Y_appSystemCommandType`;
CREATE TABLE `Y_appSystemCommandType` (
  `FID` varchar(36) NOT NULL,
  `FascTypeFlag` int(11) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`FID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_audit
-- ----------------------------
DROP TABLE IF EXISTS `y_audit`;
CREATE TABLE `y_audit` (
  `FID` varchar(36) NOT NULL,
  `FAuditor` varchar(36) DEFAULT NULL,
  `FAuditTime` datetime DEFAULT NULL,
  `FAuditOpinion` varchar(300) DEFAULT NULL,
  `FReviewTheResults` varchar(36) DEFAULT NULL,
  `FAuditContentID` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_assurancecontent
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_assurancecontent`;
CREATE TABLE `y_basic_assurancecontent` (
  `FID` varchar(36) NOT NULL,
  `FSocialGroupsID` varchar(36) DEFAULT NULL,
  `FContent` text,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_AssuranceContent_REFERENCE_SocialGroups` (`FSocialGroupsID`) USING BTREE,
  CONSTRAINT `y_basic_assurancecontent_ibfk_1` FOREIGN KEY (`FSocialGroupsID`) REFERENCES `y_basic_socialgroups` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_bug
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_bug`;
CREATE TABLE `y_basic_bug` (
  `fid` varchar(36) NOT NULL,
  `ftagname` varchar(20) NOT NULL COMMENT 'BUG或需求或优化',
  `fprojectname` varchar(50) NOT NULL COMMENT 'APP或者云筑圈管理系统，广告管理系统',
  `fosversions` varchar(100) DEFAULT NULL COMMENT '记录操作系统及版本信息。BUG现场描述',
  `fhardwareinfo` varchar(100) DEFAULT NULL COMMENT '产品运行环境描述',
  `fbugcontent` text NOT NULL COMMENT 'BUG信息描述包含图片',
  `fenclosure` text COMMENT '上传资料的下载地址',
  `fpriority` bigint(20) NOT NULL COMMENT '处理优先级别：1到10级',
  `feedbackname` varchar(200) NOT NULL COMMENT '反馈者昵称',
  `feedbackemail` varchar(200) NOT NULL COMMENT '反馈者email，最好留下电子邮件我们能把处理结果及时回复于你。',
  `feedbacktel` varchar(13) NOT NULL COMMENT '反馈者手机号码',
  `fresult` text COMMENT '处理结果描述',
  `fstate` int(20) DEFAULT NULL COMMENT '状态（0新增，1提交，2待处理，3处理中，4完结）',
  `fcreatetime` datetime NOT NULL COMMENT '创建时间',
  `fmodifiedtime` datetime DEFAULT NULL COMMENT '修改时间',
  `flasttime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `flastname` varchar(200) DEFAULT NULL COMMENT '最后修改人',
  `flag` int(11) NOT NULL AUTO_INCREMENT,
  `fheadline` varchar(500) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`fid`),
  UNIQUE KEY `flag` (`flag`)
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_city
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_city`;
CREATE TABLE `y_basic_city` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(100) DEFAULT NULL,
  `FName` varchar(100) DEFAULT NULL,
  `FCityAreaCode` varchar(36) DEFAULT NULL,
  `Fmunicipalities` char(1) DEFAULT NULL,
  `FProvinceID` varchar(36) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_City_REFERENCE_Province` (`FProvinceID`) USING BTREE,
  CONSTRAINT `y_basic_city_ibfk_1` FOREIGN KEY (`FProvinceID`) REFERENCES `y_basic_province` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=1655 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_contactsexhibition
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_contactsexhibition`;
CREATE TABLE `y_basic_contactsexhibition` (
  `FID` varchar(36) NOT NULL,
  `FPositionID` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_ContactsExhibition_REFERENCE_Position` (`FPositionID`) USING BTREE,
  CONSTRAINT `y_basic_contactsexhibition_ibfk_1` FOREIGN KEY (`FPositionID`) REFERENCES `y_basic_position` (`FID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_county
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_county`;
CREATE TABLE `y_basic_county` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(100) DEFAULT NULL,
  `FName` varchar(100) DEFAULT NULL,
  `FCityID` varchar(36) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_County__REFERENCE_City` (`FCityID`) USING BTREE,
  CONSTRAINT `y_basic_county_ibfk_1` FOREIGN KEY (`FCityID`) REFERENCES `y_basic_city` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=6165 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_demandsupply_pushinfo
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_demandsupply_pushinfo`;
CREATE TABLE `y_basic_demandsupply_pushinfo` (
  `FID` varchar(36) NOT NULL,
  `FTel` varchar(100) DEFAULT NULL,
  `FStatu` int(11) DEFAULT NULL,
  `FUpdatetime` datetime DEFAULT NULL,
  `FPushtitle` varchar(1000) DEFAULT NULL,
  `FRemark` varchar(200) DEFAULT NULL,
  `FDemandsupplyid` varchar(36) DEFAULT NULL,
  `FType` int(11) DEFAULT NULL,
  PRIMARY KEY (`FID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_demandsupplycment_pushinfo
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_demandsupplycment_pushinfo`;
CREATE TABLE `y_basic_demandsupplycment_pushinfo` (
  `FMobilephone` varchar(100) NOT NULL,
  `FStatu` int(11) DEFAULT NULL,
  `FUpdatetime` datetime DEFAULT NULL,
  `FRemark` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`FMobilephone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_district
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_district`;
CREATE TABLE `y_basic_district` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(100) DEFAULT NULL,
  `FName` varchar(100) DEFAULT NULL,
  `FNationID` varchar(36) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_dynmicandinfo_pushinfo
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_dynmicandinfo_pushinfo`;
CREATE TABLE `y_basic_dynmicandinfo_pushinfo` (
  `fid` varchar(36) NOT NULL,
  `tel` varchar(100) DEFAULT NULL,
  `statu` int(2) DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `pushtitle` varchar(100) DEFAULT NULL,
  `dynamicinformid` varchar(36) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `type` int(2) DEFAULT NULL,
  `groupId` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`fid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_employee
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_employee`;
CREATE TABLE `y_basic_employee` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(100) DEFAULT NULL,
  `FName` varchar(48) DEFAULT NULL,
  `FSex` int(11) DEFAULT NULL,
  `FBirthday` date DEFAULT NULL,
  `FEmail` varchar(48) DEFAULT NULL,
  `FPreviousEmail` varchar(48) DEFAULT NULL,
  `FHomePhone` varchar(24) DEFAULT NULL,
  `FOfficePhone` varchar(24) DEFAULT NULL,
  `FMobilePhone` varchar(24) DEFAULT NULL,
  `FSecondPhone` varchar(24) DEFAULT NULL,
  `FSite` varchar(255) DEFAULT NULL,
  `FCreaterID` varchar(36) DEFAULT NULL,
  `FModifiedID` varchar(36) DEFAULT NULL,
  `FLastModifiedID` varchar(36) DEFAULT NULL,
  `FCreateTime` datetime DEFAULT NULL,
  `FModifiedTime` datetime DEFAULT NULL,
  `FLastModifiedTime` datetime DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FOrganizationID` varchar(36) DEFAULT NULL,
  `FAdminID` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  `jsonStr` text,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_Employee_REFERENCE_Y_Users` (`FAdminID`) USING BTREE,
  CONSTRAINT `y_basic_employee_ibfk_1` FOREIGN KEY (`FAdminID`) REFERENCES `y_system_users` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_feedback
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_feedback`;
CREATE TABLE `y_basic_feedback` (
  `FID` varchar(36) NOT NULL,
  `FContactInfo` varchar(24) DEFAULT NULL,
  `FMessage` text,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_getadress
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_getadress`;
CREATE TABLE `y_basic_getadress` (
  `fid` varchar(36) NOT NULL,
  `fmemberid` varchar(36) DEFAULT NULL,
  `fadress` varchar(1000) DEFAULT NULL,
  `fupdatetime` datetime DEFAULT NULL,
  `fremark` varchar(1000) DEFAULT NULL,
  `fqueue` int(11) DEFAULT NULL,
  `fisDefault` varchar(10) DEFAULT NULL,
  `fprovincialId` varchar(36) DEFAULT NULL,
  `fcityId` varchar(36) DEFAULT NULL,
  `fcountryId` varchar(36) DEFAULT NULL,
  `fharvestingName` varchar(36) DEFAULT NULL,
  `fharvestPhone` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`fid`),
  KEY `member_getadress` (`fmemberid`) USING BTREE,
  KEY `provincial_getadress` (`fprovincialId`) USING BTREE,
  KEY `city_getadress` (`fcityId`) USING BTREE,
  KEY `country_getadress` (`fcountryId`) USING BTREE,
  CONSTRAINT `y_basic_getadress_ibfk_1` FOREIGN KEY (`fcityId`) REFERENCES `y_basic_city` (`FID`),
  CONSTRAINT `y_basic_getadress_ibfk_2` FOREIGN KEY (`fcountryId`) REFERENCES `y_basic_county` (`FID`),
  CONSTRAINT `y_basic_getadress_ibfk_3` FOREIGN KEY (`fmemberid`) REFERENCES `y_basic_member` (`FID`),
  CONSTRAINT `y_basic_getadress_ibfk_4` FOREIGN KEY (`fprovincialId`) REFERENCES `y_basic_province` (`FID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_group
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_group`;
CREATE TABLE `y_basic_group` (
  `FID` varchar(36) NOT NULL,
  `FGroupHeadImage` varchar(200) DEFAULT NULL,
  `FGroupMaxPeople` int(11) DEFAULT NULL,
  `FGroupName` varchar(100) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  `businessId` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=224 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_grouppeople
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_grouppeople`;
CREATE TABLE `y_basic_grouppeople` (
  `FID` varchar(36) NOT NULL,
  `FIMID` varchar(36) DEFAULT NULL,
  `FIsCreater` int(11) DEFAULT NULL,
  `FNickName` varchar(100) DEFAULT NULL,
  `FGroupID` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_GroupPeople__REFERENCE_IMAccount` (`FIMID`) USING BTREE,
  KEY `FK_GroupPeople__REFERENCE_Group` (`FGroupID`) USING BTREE,
  CONSTRAINT `y_basic_grouppeople_ibfk_1` FOREIGN KEY (`FGroupID`) REFERENCES `y_basic_group` (`FID`),
  CONSTRAINT `y_basic_grouppeople_ibfk_2` FOREIGN KEY (`FIMID`) REFERENCES `y_basic_imaccount` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=2122 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_imaccount
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_imaccount`;
CREATE TABLE `y_basic_imaccount` (
  `FID` varchar(36) NOT NULL,
  `FIMkey` varchar(100) DEFAULT NULL,
  `FIMPassword` varchar(100) DEFAULT NULL,
  `fimtel` varchar(100) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_IMAccount_REFERENCE_Member` (`fimtel`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=61638 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_industry
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_industry`;
CREATE TABLE `y_basic_industry` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(100) DEFAULT NULL,
  `FName` varchar(100) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FCreaterID` varchar(36) DEFAULT NULL,
  `FModifiedID` varchar(36) DEFAULT NULL,
  `FLastModifiedID` varchar(36) DEFAULT NULL,
  `FCreateTime` datetime DEFAULT NULL,
  `FModifiedTime` datetime DEFAULT NULL,
  `FLastModifiedTime` datetime DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_joinactivity
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_joinactivity`;
CREATE TABLE `y_basic_joinactivity` (
  `fid` varchar(36) NOT NULL,
  `userName` varchar(50) DEFAULT NULL,
  `tel` varchar(255) DEFAULT NULL,
  `setNumber` int(11) DEFAULT NULL,
  `groupName` varchar(255) DEFAULT NULL,
  `activityId` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`fid`),
  KEY `FK_Y_BASIC__Y_WALLAC` (`activityId`) USING BTREE,
  CONSTRAINT `y_basic_joinactivity_ibfk_1` FOREIGN KEY (`activityId`) REFERENCES `y_wallactivity` (`FID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_label
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_label`;
CREATE TABLE `y_basic_label` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(100) DEFAULT NULL,
  `FTerritorys` text,
  `FProvincialId` varchar(36) DEFAULT NULL,
  `FCityId` varchar(36) DEFAULT NULL,
  `FCountryId` varchar(36) DEFAULT NULL,
  `FTrades` text,
  `FIndustrys` text,
  `FUserPhone` varchar(50) DEFAULT NULL,
  `FSupplyDemand` text,
  `FBillState` int(11) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FOrderIndex` int(11) DEFAULT NULL,
  `FType` int(11) DEFAULT NULL,
  `FLag` int(11) DEFAULT NULL,
  PRIMARY KEY (`FID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_life_pushinfo
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_life_pushinfo`;
CREATE TABLE `y_basic_life_pushinfo` (
  `FID` varchar(36) NOT NULL,
  `FTel` varchar(100) DEFAULT NULL,
  `FStatu` int(11) DEFAULT NULL,
  `FUpdatetime` datetime DEFAULT NULL,
  `FPushtitle` varchar(1000) DEFAULT NULL,
  `FLifeID` varchar(36) DEFAULT NULL,
  `FRemark` varchar(200) DEFAULT NULL,
  `FType` int(11) DEFAULT NULL,
  PRIMARY KEY (`FID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_member
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_member`;
CREATE TABLE `y_basic_member` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(36) DEFAULT NULL,
  `FMobilePhone` varchar(100) DEFAULT NULL,
  `FPassword` varchar(36) DEFAULT NULL,
  `FTempPwd` varchar(36) DEFAULT NULL,
  `FHeadImage` varchar(200) DEFAULT NULL,
  `FIsOriginalPassword` int(11) DEFAULT NULL,
  `FName` varchar(36) DEFAULT NULL,
  `FBirthday` datetime DEFAULT NULL,
  `FAdminID` varchar(36) DEFAULT NULL,
  `FSocialGroupsID` varchar(36) DEFAULT NULL,
  `FSex` int(11) DEFAULT NULL,
  `FSource` varchar(50) DEFAULT NULL,
  `FPreviousEmail` varchar(48) DEFAULT NULL,
  `FEmail` varchar(48) DEFAULT NULL,
  `FHomePhone` varchar(48) DEFAULT NULL,
  `FSecondPhone` varchar(48) DEFAULT NULL,
  `FTypeID` varchar(36) DEFAULT NULL,
  `FSite` varchar(300) DEFAULT NULL,
  `FNickName` varchar(50) DEFAULT NULL,
  `FReceivingAddress` varchar(200) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FNativePlace` varchar(100) DEFAULT NULL,
  `FIsHidePhone` int(11) DEFAULT '0',
  `FProvinceID` varchar(36) DEFAULT NULL,
  `FCountyID` varchar(36) DEFAULT NULL,
  `FCityID` varchar(36) DEFAULT NULL,
  `FCreaterID` varchar(36) DEFAULT NULL,
  `FModifiedID` varchar(36) DEFAULT NULL,
  `FLastModifiedID` varchar(36) DEFAULT NULL,
  `FCreateTime` datetime DEFAULT NULL,
  `FModifiedTime` datetime DEFAULT NULL,
  `FLastModifiedTime` datetime DEFAULT NULL,
  `Flag` int(11) NOT NULL AUTO_INCREMENT,
  `FBillState` int(11) DEFAULT NULL,
  `isAdmin` int(11) DEFAULT NULL,
  `jsonStr` text,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `Flag_UNIQUE` (`Flag`) USING BTREE,
  KEY `FK_Member_REFERENCE_Users` (`FAdminID`) USING BTREE,
  KEY `FK_Member_REFERENCE_City` (`FCityID`) USING BTREE,
  KEY `FK_Member_REFERENCE_County` (`FCountyID`) USING BTREE,
  KEY `FK_Member_REFERENCE_Province` (`FProvinceID`) USING BTREE,
  KEY `FK_Member_REFERENCE_SG` (`FSocialGroupsID`) USING BTREE,
  CONSTRAINT `y_basic_member_ibfk_1` FOREIGN KEY (`FCityID`) REFERENCES `y_basic_city` (`FID`),
  CONSTRAINT `y_basic_member_ibfk_2` FOREIGN KEY (`FCountyID`) REFERENCES `y_basic_county` (`FID`),
  CONSTRAINT `y_basic_member_ibfk_3` FOREIGN KEY (`FProvinceID`) REFERENCES `y_basic_province` (`FID`),
  CONSTRAINT `y_basic_member_ibfk_4` FOREIGN KEY (`FSocialGroupsID`) REFERENCES `y_basic_socialgroups` (`FID`),
  CONSTRAINT `y_basic_member_ibfk_5` FOREIGN KEY (`FAdminID`) REFERENCES `y_system_users` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=549933 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_memberbymemberblacklist
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_memberbymemberblacklist`;
CREATE TABLE `y_basic_memberbymemberblacklist` (
  `FID` varchar(36) NOT NULL,
  `FDynamicID` varchar(36) DEFAULT NULL,
  `FMemberID` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_MemberByMemberBlacklist__REFERENCE_SocialGroupsDynamic` (`FDynamicID`) USING BTREE,
  KEY `FK_MemberByMemberBlacklist_REFERENCE_Member` (`FMemberID`) USING BTREE,
  CONSTRAINT `y_basic_memberbymemberblacklist_ibfk_1` FOREIGN KEY (`FMemberID`) REFERENCES `y_basic_member` (`FID`),
  CONSTRAINT `y_basic_memberbymemberblacklist_ibfk_2` FOREIGN KEY (`FDynamicID`) REFERENCES `y_basic_socialgroupsdynamic` (`FID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_memberbymemberhide
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_memberbymemberhide`;
CREATE TABLE `y_basic_memberbymemberhide` (
  `FID` varchar(36) NOT NULL,
  `FInMemberID` varchar(36) DEFAULT NULL,
  `FOutMemberID` varchar(36) NOT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_MemberByMemberHide__Member` (`FInMemberID`) USING BTREE,
  KEY `FK_MemberByMemberHide_REFERENCE_Y_BASIC_Member` (`FOutMemberID`) USING BTREE,
  CONSTRAINT `y_basic_memberbymemberhide_ibfk_1` FOREIGN KEY (`FOutMemberID`) REFERENCES `y_basic_member` (`FID`),
  CONSTRAINT `y_basic_memberbymemberhide_ibfk_2` FOREIGN KEY (`FInMemberID`) REFERENCES `y_basic_member` (`FID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_memberbypositionblacklist
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_memberbypositionblacklist`;
CREATE TABLE `y_basic_memberbypositionblacklist` (
  `FID` varchar(36) NOT NULL,
  `FDynamicID` varchar(36) DEFAULT NULL,
  `FPositionID` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_MemberByPositionBlacklist_REFERENCE_SocialGroupsDynamic` (`FDynamicID`) USING BTREE,
  KEY `FK_MemberByPositionBlacklist__REFERENCE_Position` (`FPositionID`) USING BTREE,
  CONSTRAINT `y_basic_memberbypositionblacklist_ibfk_1` FOREIGN KEY (`FDynamicID`) REFERENCES `y_basic_socialgroupsdynamic` (`FID`),
  CONSTRAINT `y_basic_memberbypositionblacklist_ibfk_2` FOREIGN KEY (`FPositionID`) REFERENCES `y_basic_position` (`FID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_memberbypositionhide
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_memberbypositionhide`;
CREATE TABLE `y_basic_memberbypositionhide` (
  `FID` varchar(36) NOT NULL,
  `FMemberID` varchar(36) DEFAULT NULL,
  `FPositionID` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_MemberByPositionHide_REFERENCE_Member` (`FMemberID`) USING BTREE,
  KEY `FK_MemberByPositionHide_REFERENCE_Position` (`FPositionID`) USING BTREE,
  CONSTRAINT `y_basic_memberbypositionhide_ibfk_1` FOREIGN KEY (`FMemberID`) REFERENCES `y_basic_member` (`FID`),
  CONSTRAINT `y_basic_memberbypositionhide_ibfk_2` FOREIGN KEY (`FPositionID`) REFERENCES `y_basic_position` (`FID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_membercompany
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_membercompany`;
CREATE TABLE `y_basic_membercompany` (
  `FCID` varchar(36) NOT NULL,
  `FCName` varchar(2000) DEFAULT NULL,
  `FCTelphone` varchar(500) DEFAULT NULL,
  `FCEmail` varchar(1000) DEFAULT NULL,
  `FCUrl` varchar(300) DEFAULT NULL,
  `FCLocation` varchar(200) DEFAULT NULL,
  `FCProducts` text,
  `FCIntroduction` text,
  `FTradeID` varchar(36) DEFAULT NULL,
  `FIndustryID` varchar(36) DEFAULT NULL,
  `FSocialGroupsID` varchar(36) DEFAULT NULL,
  `FMemberID` varchar(36) DEFAULT NULL,
  `FCompanyPosition` varchar(100) DEFAULT NULL,
  `FCompanyNature` varchar(100) DEFAULT NULL,
  `FCompanyScale` varchar(100) DEFAULT NULL,
  `FCompanyLatitude` varchar(100) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  `FCompanyLogo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`FCID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_MemberCompany_REFERENCE_Trade` (`FTradeID`) USING BTREE,
  KEY `FK_MemberCompany_REFERENCE_Industry` (`FIndustryID`) USING BTREE,
  KEY `FK_MemberCompany_REFERENCE_Member` (`FMemberID`) USING BTREE,
  KEY `FK_MemberCompany_REFERENCE_SocialGroups` (`FSocialGroupsID`) USING BTREE,
  CONSTRAINT `y_basic_membercompany_ibfk_1` FOREIGN KEY (`FIndustryID`) REFERENCES `y_basic_industry` (`FID`),
  CONSTRAINT `y_basic_membercompany_ibfk_2` FOREIGN KEY (`FMemberID`) REFERENCES `y_basic_member` (`FID`),
  CONSTRAINT `y_basic_membercompany_ibfk_3` FOREIGN KEY (`FSocialGroupsID`) REFERENCES `y_basic_socialgroups` (`FID`),
  CONSTRAINT `y_basic_membercompany_ibfk_4` FOREIGN KEY (`FTradeID`) REFERENCES `y_basic_trade` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=67423 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_nation
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_nation`;
CREATE TABLE `y_basic_nation` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(100) DEFAULT NULL,
  `FName` varchar(100) DEFAULT NULL,
  `FISO` varchar(36) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_organization
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_organization`;
CREATE TABLE `y_basic_organization` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(100) DEFAULT NULL,
  `FName` varchar(100) DEFAULT NULL,
  `FAbbreviation` varchar(48) DEFAULT NULL,
  `FEnglishName` varchar(48) DEFAULT NULL,
  `FDescribe` varchar(255) DEFAULT NULL,
  `FStartTime` datetime DEFAULT NULL,
  `FStorageTime` datetime DEFAULT NULL,
  `FAlterDescribe` varchar(255) DEFAULT NULL,
  `FAddress` varchar(100) DEFAULT NULL,
  `FSuperiorOrganizationID` varchar(36) DEFAULT NULL,
  `FGradationType` int(11) DEFAULT NULL,
  `FPhone` varchar(100) DEFAULT NULL,
  `FUnitGradation` int(11) DEFAULT NULL,
  `FFax` varchar(48) DEFAULT NULL,
  `FDistrictID` varchar(36) DEFAULT NULL,
  `FEmail` varchar(48) DEFAULT NULL,
  `FPostcode` varchar(48) DEFAULT NULL,
  `FEstablishDate` datetime DEFAULT NULL,
  `FRegisteredCapital` double DEFAULT '0',
  `FRegisterNumber` varchar(48) DEFAULT NULL,
  `FOrganizationCode` varchar(48) DEFAULT NULL,
  `FBusinessIndate` varchar(48) DEFAULT NULL,
  `FTaxAffairsNumber` varchar(48) DEFAULT NULL,
  `FChurchyard` char(1) DEFAULT NULL,
  `FOverseas` char(1) DEFAULT NULL,
  `FLegalPersonCorporation` char(1) DEFAULT NULL,
  `FLegalPersonDelegate` varchar(36) DEFAULT NULL,
  `FLeafNode` char(1) DEFAULT NULL,
  `FLevel` int(11) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FCreaterID` varchar(36) DEFAULT NULL,
  `FModifiedID` varchar(36) DEFAULT NULL,
  `FLastModifiedID` varchar(36) DEFAULT NULL,
  `FCreateTime` datetime DEFAULT NULL,
  `FModifiedTime` datetime DEFAULT NULL,
  `FLastModifiedTime` datetime DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_position
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_position`;
CREATE TABLE `y_basic_position` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(36) DEFAULT NULL,
  `FName` varchar(36) DEFAULT NULL,
  `FOrganizationID` varchar(36) DEFAULT NULL,
  `FCreaterID` varchar(36) DEFAULT NULL,
  `FModifiedID` varchar(36) DEFAULT NULL,
  `FLastModified` varchar(36) DEFAULT NULL,
  `PCreateTime` datetime DEFAULT NULL,
  `FModifiedTime` datetime DEFAULT NULL,
  `FLastModifiedTime` datetime DEFAULT NULL,
  `Fhide` int(11) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  `FComment` varchar(255) DEFAULT NULL,
  `FSocialGroupsID` varchar(36) DEFAULT NULL,
  `FSuperPositonID` varchar(36) DEFAULT NULL,
  `FLeafNode` int(11) DEFAULT NULL,
  `FVersion` int(11) DEFAULT NULL,
  `FSeq` int(11) DEFAULT NULL,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11891 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_province
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_province`;
CREATE TABLE `y_basic_province` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(100) DEFAULT NULL,
  `FName` varchar(100) DEFAULT NULL,
  `FAbbreviation` char(4) DEFAULT NULL,
  `FNationID` varchar(24) DEFAULT NULL,
  `FDistrictID` varchar(24) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_Province__REFERENCE_Nation` (`FNationID`) USING BTREE,
  KEY `FK_Province_REFERENCE_District` (`FDistrictID`) USING BTREE,
  CONSTRAINT `y_basic_province_ibfk_1` FOREIGN KEY (`FDistrictID`) REFERENCES `y_basic_district` (`FID`),
  CONSTRAINT `y_basic_province_ibfk_2` FOREIGN KEY (`FNationID`) REFERENCES `y_basic_nation` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=163 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_searchrecords
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_searchrecords`;
CREATE TABLE `y_basic_searchrecords` (
  `FID` varchar(36) NOT NULL,
  `FMemberId` varchar(36) DEFAULT NULL,
  `FKeyword` varchar(100) DEFAULT NULL,
  `FModule` varchar(100) DEFAULT NULL,
  `FSearchTime` datetime DEFAULT NULL,
  `flag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `flag` (`flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_shoping_pushinfo
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_shoping_pushinfo`;
CREATE TABLE `y_basic_shoping_pushinfo` (
  `FID` varchar(36) NOT NULL,
  `FTel` varchar(100) DEFAULT NULL,
  `FStatu` int(11) DEFAULT NULL,
  `FUpdatetime` datetime DEFAULT NULL,
  `FPushtitle` varchar(1000) DEFAULT NULL,
  `FRemark` varchar(200) DEFAULT NULL,
  `FShopingId` varchar(36) DEFAULT NULL,
  `FType` int(11) DEFAULT NULL,
  PRIMARY KEY (`FID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_socialgroups
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_socialgroups`;
CREATE TABLE `y_basic_socialgroups` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(100) DEFAULT NULL,
  `FName` varchar(100) DEFAULT NULL,
  `FAbbreviation` varchar(36) DEFAULT NULL,
  `FDistrictID` varchar(36) DEFAULT NULL,
  `FNumberPeople` int(11) DEFAULT NULL,
  `FCompaniesID` varchar(36) DEFAULT NULL,
  `FSalesmanID` varchar(36) DEFAULT NULL,
  `FTypeID` varchar(36) DEFAULT NULL,
  `FSuperSocialGroupsID` varchar(36) DEFAULT NULL,
  `FLeafNode` char(1) DEFAULT NULL,
  `FPreviousNumber` varchar(100) DEFAULT NULL,
  `FSource` varchar(100) DEFAULT NULL,
  `FYNSigned` char(1) DEFAULT NULL,
  `FSignedTime` datetime DEFAULT NULL,
  `FRegisterNum` int(11) DEFAULT NULL,
  `FLevel` varchar(36) DEFAULT NULL,
  `FClientContacts` varchar(48) DEFAULT NULL,
  `FAddress` varchar(200) DEFAULT NULL,
  `FPhone` varchar(50) DEFAULT NULL,
  `FEmail` varchar(50) DEFAULT NULL,
  `FIPhone` varchar(50) DEFAULT NULL,
  `FProvinceID` varchar(36) DEFAULT NULL,
  `FCityID` varchar(36) DEFAULT NULL,
  `FCountyID` varchar(36) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FComment` text,
  `FCreaterID` varchar(36) DEFAULT NULL,
  `FModifiedID` varchar(36) DEFAULT NULL,
  `FLastModifiedID` varchar(36) DEFAULT NULL,
  `FCreateTime` datetime DEFAULT NULL,
  `FModifiedTime` datetime DEFAULT NULL,
  `FLastModifiedTime` datetime DEFAULT NULL,
  `flag` int(11) NOT NULL AUTO_INCREMENT,
  `logo` text,
  `allowadd` varchar(10) DEFAULT NULL,
  `localCacheVersion` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `flag` (`flag`),
  KEY `FK_SocialGroups_REFERENCE_Organization` (`FCompaniesID`) USING BTREE,
  KEY `FK_SocialGroups_REFERENCE_Province` (`FProvinceID`) USING BTREE,
  KEY `FK_SocialGroups_REFERENCE_City` (`FCityID`) USING BTREE,
  KEY `FK_SocialGroups_REFERENCE_County` (`FCountyID`) USING BTREE,
  KEY `FK_SocialGroups__REFERENCE_Type` (`FTypeID`) USING BTREE,
  KEY `FK_SocialGroups_REFERENCE_District` (`FDistrictID`) USING BTREE,
  KEY `FK_SocialGroups_REFERENCE_Employee` (`FSalesmanID`) USING BTREE,
  CONSTRAINT `y_basic_socialgroups_ibfk_1` FOREIGN KEY (`FCityID`) REFERENCES `y_basic_city` (`FID`),
  CONSTRAINT `y_basic_socialgroups_ibfk_2` FOREIGN KEY (`FCountyID`) REFERENCES `y_basic_county` (`FID`),
  CONSTRAINT `y_basic_socialgroups_ibfk_3` FOREIGN KEY (`FDistrictID`) REFERENCES `y_basic_district` (`FID`),
  CONSTRAINT `y_basic_socialgroups_ibfk_4` FOREIGN KEY (`FSalesmanID`) REFERENCES `y_basic_employee` (`FID`),
  CONSTRAINT `y_basic_socialgroups_ibfk_5` FOREIGN KEY (`FCompaniesID`) REFERENCES `y_basic_organization` (`FID`),
  CONSTRAINT `y_basic_socialgroups_ibfk_6` FOREIGN KEY (`FProvinceID`) REFERENCES `y_basic_province` (`FID`),
  CONSTRAINT `y_basic_socialgroups_ibfk_7` FOREIGN KEY (`FTypeID`) REFERENCES `y_basic_type` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=419 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_socialgroupsabout
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_socialgroupsabout`;
CREATE TABLE `y_basic_socialgroupsabout` (
  `fid` varchar(36) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `content` text,
  `updateTime` datetime DEFAULT NULL,
  `FSocialGroupID` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  `sequenceNumber` int(11) DEFAULT NULL,
  PRIMARY KEY (`fid`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_SocialGroupsAbout_REFERENCE_SocialGroups` (`FSocialGroupID`) USING BTREE,
  CONSTRAINT `y_basic_socialgroupsabout_ibfk_1` FOREIGN KEY (`FSocialGroupID`) REFERENCES `y_basic_socialgroups` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=420 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_socialgroupsactivity
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_socialgroupsactivity`;
CREATE TABLE `y_basic_socialgroupsactivity` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(100) DEFAULT NULL,
  `FHeadline` varchar(100) DEFAULT NULL,
  `FMessage` text,
  `FImages` text,
  `FSponsor` varchar(100) DEFAULT NULL,
  `FTypeID` varchar(36) DEFAULT NULL,
  `FSocialGroupsID` varchar(36) DEFAULT NULL,
  `FSite` varchar(50) DEFAULT NULL,
  `FStartTime` datetime DEFAULT NULL,
  `FFinishTime` datetime DEFAULT NULL,
  `FCost` double DEFAULT NULL,
  `FPeopleNum` int(11) DEFAULT NULL,
  `FPublisherID` varchar(36) DEFAULT NULL,
  `FSource` varchar(100) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_SocialGroupsActivity_REFERENCE_SocialGroups` (`FSocialGroupsID`) USING BTREE,
  KEY `FK_SocialGroupsActivity_REFERENCE_Type` (`FTypeID`) USING BTREE,
  KEY `FK_SocialGroupsActivity_REFERENCE_Member` (`FPublisherID`) USING BTREE,
  CONSTRAINT `y_basic_socialgroupsactivity_ibfk_1` FOREIGN KEY (`FPublisherID`) REFERENCES `y_basic_member` (`FID`),
  CONSTRAINT `y_basic_socialgroupsactivity_ibfk_2` FOREIGN KEY (`FSocialGroupsID`) REFERENCES `y_basic_socialgroups` (`FID`),
  CONSTRAINT `y_basic_socialgroupsactivity_ibfk_3` FOREIGN KEY (`FTypeID`) REFERENCES `y_basic_type` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_socialgroupscontact
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_socialgroupscontact`;
CREATE TABLE `y_basic_socialgroupscontact` (
  `fid` varchar(36) NOT NULL,
  `tell` varchar(100) DEFAULT NULL,
  `adress` varchar(200) DEFAULT NULL,
  `mail` varchar(100) DEFAULT NULL,
  `uri` varchar(100) DEFAULT NULL,
  `wechat` varchar(100) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `FSocialGroupID` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`fid`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_SocialGroupsContact_REFERENCE_SocialGroups` (`FSocialGroupID`) USING BTREE,
  CONSTRAINT `y_basic_socialgroupscontact_ibfk_1` FOREIGN KEY (`FSocialGroupID`) REFERENCES `y_basic_socialgroups` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_socialgroupsdemand
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_socialgroupsdemand`;
CREATE TABLE `y_basic_socialgroupsdemand` (
  `FID` varchar(36) NOT NULL DEFAULT '',
  `FNumber` varchar(100) DEFAULT NULL,
  `FHeadline` varchar(100) DEFAULT NULL,
  `FImages` text,
  `FMessage` text,
  `FSocialGroupsID` varchar(36) DEFAULT NULL,
  `FPublisherID` varchar(36) DEFAULT NULL,
  `FContacts` varchar(48) DEFAULT NULL,
  `FTel` varchar(50) DEFAULT NULL,
  `FStartTime` datetime DEFAULT NULL,
  `FFinishTime` datetime DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  `FIsHide` int(11) DEFAULT NULL,
  `FProvinceID` varchar(36) DEFAULT NULL,
  `FCityID` varchar(36) DEFAULT NULL,
  `FCountyID` varchar(36) DEFAULT NULL,
  `FTradeID` varchar(36) DEFAULT NULL,
  `Flevel` int(11) DEFAULT NULL,
  `FPublisherTime` datetime DEFAULT NULL,
  `FRank` int(11) DEFAULT NULL,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_SocialGroupsDemand_REFERENCE_SocialGroups` (`FSocialGroupsID`) USING BTREE,
  KEY `FK_SocialGroupsDemand_REFERENCE_Member` (`FPublisherID`) USING BTREE,
  KEY `FK_SocialGroupsDemand_REFERENCE_City` (`FCityID`) USING BTREE,
  KEY `FK_SocialGroupsDemand_REFERENCE_Province` (`FProvinceID`) USING BTREE,
  KEY `FK_SocialGroupsDemand_REFERENCE_County` (`FCountyID`) USING BTREE,
  KEY `FK_SocialGroupsDemand_REFERENCE_Trade` (`FTradeID`) USING BTREE,
  CONSTRAINT `y_basic_socialgroupsdemand_ibfk_1` FOREIGN KEY (`FCityID`) REFERENCES `y_basic_city` (`FID`),
  CONSTRAINT `y_basic_socialgroupsdemand_ibfk_2` FOREIGN KEY (`FCountyID`) REFERENCES `y_basic_county` (`FID`),
  CONSTRAINT `y_basic_socialgroupsdemand_ibfk_3` FOREIGN KEY (`FPublisherID`) REFERENCES `y_basic_member` (`FID`),
  CONSTRAINT `y_basic_socialgroupsdemand_ibfk_4` FOREIGN KEY (`FProvinceID`) REFERENCES `y_basic_province` (`FID`),
  CONSTRAINT `y_basic_socialgroupsdemand_ibfk_5` FOREIGN KEY (`FSocialGroupsID`) REFERENCES `y_basic_socialgroups` (`FID`),
  CONSTRAINT `y_basic_socialgroupsdemand_ibfk_6` FOREIGN KEY (`FTradeID`) REFERENCES `y_basic_trade` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_socialgroupsdynamic
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_socialgroupsdynamic`;
CREATE TABLE `y_basic_socialgroupsdynamic` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(100) DEFAULT NULL,
  `FHeadline` varchar(100) DEFAULT NULL,
  `FLogoImage` text,
  `FMessage` longtext,
  `FSocialGroupsID` varchar(36) DEFAULT NULL,
  `FTypeID` varchar(36) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  `FPublishTime` datetime DEFAULT NULL,
  `FDetailAddress` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_SocialGroupsDynamic_REFERENCE_SocialGroups` (`FSocialGroupsID`) USING BTREE,
  KEY `FK_SocialGroupsDynamic_REFERENCE_Type` (`FTypeID`) USING BTREE,
  CONSTRAINT `y_basic_socialgroupsdynamic_ibfk_1` FOREIGN KEY (`FSocialGroupsID`) REFERENCES `y_basic_socialgroups` (`FID`),
  CONSTRAINT `y_basic_socialgroupsdynamic_ibfk_2` FOREIGN KEY (`FTypeID`) REFERENCES `y_basic_type` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=2580 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_socialgroupsguestbook
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_socialgroupsguestbook`;
CREATE TABLE `y_basic_socialgroupsguestbook` (
  `FID` varchar(36) NOT NULL,
  `FImages` text,
  `FMessage` text,
  `FGuestBookID` varchar(36) DEFAULT NULL,
  `FGuestBookTime` datetime DEFAULT NULL,
  `FSocialGroupsID` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_SocialGroupsGuestBook_REFERENCE_Member` (`FGuestBookID`) USING BTREE,
  KEY `FK_SocialGroupsGuestBook_REFERENCE_SocialGroups` (`FSocialGroupsID`) USING BTREE,
  CONSTRAINT `y_basic_socialgroupsguestbook_ibfk_1` FOREIGN KEY (`FGuestBookID`) REFERENCES `y_basic_member` (`FID`),
  CONSTRAINT `y_basic_socialgroupsguestbook_ibfk_2` FOREIGN KEY (`FSocialGroupsID`) REFERENCES `y_basic_socialgroups` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_socialgroupsinform
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_socialgroupsinform`;
CREATE TABLE `y_basic_socialgroupsinform` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(100) DEFAULT NULL,
  `FHeadline` varchar(100) DEFAULT NULL,
  `FLogoImage` text,
  `FMessage` longtext,
  `FInformPeopleNum` int(11) DEFAULT NULL,
  `FParticipationNum` int(11) DEFAULT NULL,
  `FSocialGroupsID` varchar(36) DEFAULT NULL,
  `FStartTime` datetime DEFAULT NULL,
  `FFinishTime` datetime DEFAULT NULL,
  `FTypeID` varchar(36) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  `FDetailAddress` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_SocialGroupsInform_REFERENCE_SocialGroups` (`FSocialGroupsID`) USING BTREE,
  KEY `FK_SocialGroupsInform_REFERENCE_Type` (`FTypeID`) USING BTREE,
  CONSTRAINT `y_basic_socialgroupsinform_ibfk_1` FOREIGN KEY (`FSocialGroupsID`) REFERENCES `y_basic_socialgroups` (`FID`),
  CONSTRAINT `y_basic_socialgroupsinform_ibfk_2` FOREIGN KEY (`FTypeID`) REFERENCES `y_basic_type` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=1091 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_socialgroupsinformrecord
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_socialgroupsinformrecord`;
CREATE TABLE `y_basic_socialgroupsinformrecord` (
  `FID` varchar(36) NOT NULL,
  `FInformPeopleID` varchar(36) DEFAULT NULL,
  `FInformID` varchar(36) DEFAULT NULL,
  `FYNParticipation` char(1) DEFAULT NULL,
  `FReplyContent` varchar(255) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  `FinformPeopleName` varchar(255) DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `FisHide` varchar(36) DEFAULT '0',
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_SocialGroupsInformRecord_REFERENCE_SocialGroupsInform` (`FInformID`) USING BTREE,
  CONSTRAINT `y_basic_socialgroupsinformrecord_ibfk_1` FOREIGN KEY (`FInformID`) REFERENCES `y_basic_socialgroupsinform` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=4239 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_socialgroupssupply
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_socialgroupssupply`;
CREATE TABLE `y_basic_socialgroupssupply` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(100) DEFAULT NULL,
  `FHeadline` varchar(100) DEFAULT NULL,
  `Fimages` text,
  `FMessage` text,
  `FSocialGroupsID` varchar(36) DEFAULT NULL,
  `FTel` varchar(50) DEFAULT NULL,
  `FNationalCertification` varchar(50) DEFAULT NULL,
  `FAreGuarantee` int(11) DEFAULT NULL,
  `FPublisherID` varchar(36) DEFAULT NULL,
  `FContacts` varchar(48) DEFAULT NULL,
  `FAssuranceContentID` varchar(36) DEFAULT NULL,
  `FExpireTime` datetime DEFAULT NULL,
  `FAuditTime` datetime DEFAULT NULL,
  `FPublisherTime` datetime DEFAULT NULL,
  `FAuditIdea` varchar(255) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  `FIsHide` int(11) DEFAULT NULL,
  `FProvinceID` varchar(36) DEFAULT NULL,
  `FCityID` varchar(36) DEFAULT NULL,
  `FCountyID` varchar(36) DEFAULT NULL,
  `FTradeID` varchar(36) DEFAULT NULL,
  `Flevel` int(11) DEFAULT NULL,
  `jsonStr` text,
  `FRank` int(11) DEFAULT NULL,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_SocialGroupsSupply_REFERENCE_Province` (`FProvinceID`) USING BTREE,
  KEY `FK_SocialGroupsSupply_REFERENCE_County` (`FCountyID`) USING BTREE,
  KEY `FK_SocialGroupsSupply_REFERENCE_Trade` (`FTradeID`) USING BTREE,
  KEY `FK_SocialGroupsSupply_REFERENCE_SocialGroups` (`FSocialGroupsID`) USING BTREE,
  KEY `FK_SocialGroupsSupply_REFERENCE_AssuranceContent` (`FAssuranceContentID`) USING BTREE,
  KEY `FK_SocialGroupsSupply_REFERENCE_Member` (`FPublisherID`) USING BTREE,
  KEY `FK_SocialGroupsSupply_REFERENCE_City` (`FCityID`) USING BTREE,
  CONSTRAINT `y_basic_socialgroupssupply_ibfk_1` FOREIGN KEY (`FCityID`) REFERENCES `y_basic_city` (`FID`),
  CONSTRAINT `y_basic_socialgroupssupply_ibfk_2` FOREIGN KEY (`FCountyID`) REFERENCES `y_basic_county` (`FID`),
  CONSTRAINT `y_basic_socialgroupssupply_ibfk_3` FOREIGN KEY (`FPublisherID`) REFERENCES `y_basic_member` (`FID`),
  CONSTRAINT `y_basic_socialgroupssupply_ibfk_4` FOREIGN KEY (`FProvinceID`) REFERENCES `y_basic_province` (`FID`),
  CONSTRAINT `y_basic_socialgroupssupply_ibfk_5` FOREIGN KEY (`FSocialGroupsID`) REFERENCES `y_basic_socialgroups` (`FID`),
  CONSTRAINT `y_basic_socialgroupssupply_ibfk_6` FOREIGN KEY (`FTradeID`) REFERENCES `y_basic_trade` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=607 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_template
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_template`;
CREATE TABLE `y_basic_template` (
  `FID` varchar(36) NOT NULL,
  `FContent` text,
  `FAddress` varchar(200) DEFAULT NULL,
  `FType` int(11) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_trade
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_trade`;
CREATE TABLE `y_basic_trade` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(100) DEFAULT NULL,
  `FName` varchar(100) DEFAULT NULL,
  `FSuperTradeID` varchar(36) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FCreaterID` varchar(36) DEFAULT NULL,
  `FModifiedID` varchar(36) DEFAULT NULL,
  `FLastModifiedID` varchar(36) DEFAULT NULL,
  `FCreateTime` datetime DEFAULT NULL,
  `FModifiedTime` datetime DEFAULT NULL,
  `FLastModifiedTime` datetime DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_type
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_type`;
CREATE TABLE `y_basic_type` (
  `FID` varchar(36) NOT NULL,
  `FName` varchar(100) DEFAULT NULL,
  `FModuleID` varchar(100) DEFAULT NULL,
  `FYNExhibition` char(1) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basic_verification
-- ----------------------------
DROP TABLE IF EXISTS `y_basic_verification`;
CREATE TABLE `y_basic_verification` (
  `FID` varchar(36) NOT NULL,
  `FUserPhone` varchar(36) DEFAULT NULL,
  `FVerification` varchar(10) DEFAULT NULL,
  `FLoseDate` datetime DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=695 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basicentries_employeedistribution
-- ----------------------------
DROP TABLE IF EXISTS `y_basicentries_employeedistribution`;
CREATE TABLE `y_basicentries_employeedistribution` (
  `FID` varchar(36) NOT NULL,
  `FEmployeeID` varchar(36) DEFAULT NULL,
  `FPositionID` varchar(36) DEFAULT NULL,
  `FKeyPost` varchar(10) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_EmployeeDistribution_REFERENCE_Employee` (`FEmployeeID`) USING BTREE,
  KEY `FK_EmployeeDistribution_REFERENCE_Position` (`FPositionID`) USING BTREE,
  CONSTRAINT `y_basicentries_employeedistribution_ibfk_1` FOREIGN KEY (`FEmployeeID`) REFERENCES `y_basic_employee` (`FID`),
  CONSTRAINT `y_basicentries_employeedistribution_ibfk_2` FOREIGN KEY (`FPositionID`) REFERENCES `y_basic_position` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_basicentries_memberdistribution
-- ----------------------------
DROP TABLE IF EXISTS `y_basicentries_memberdistribution`;
CREATE TABLE `y_basicentries_memberdistribution` (
  `FID` varchar(36) NOT NULL,
  `FMemberID` varchar(36) DEFAULT NULL,
  `FPositionID` varchar(36) DEFAULT NULL,
  `FKeyPost` varchar(10) DEFAULT NULL,
  `FVersion` int(11) DEFAULT '1',
  `FSeq` int(11) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_MemberDistribution_REFERENCE_Member` (`FMemberID`) USING BTREE,
  KEY `FK_MemberDistribution_REFERENCE_Position` (`FPositionID`) USING BTREE,
  CONSTRAINT `y_basicentries_memberdistribution_ibfk_1` FOREIGN KEY (`FMemberID`) REFERENCES `y_basic_member` (`FID`) ON DELETE CASCADE,
  CONSTRAINT `y_basicentries_memberdistribution_ibfk_2` FOREIGN KEY (`FPositionID`) REFERENCES `y_basic_position` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=69075 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_character
-- ----------------------------
DROP TABLE IF EXISTS `y_character`;
CREATE TABLE `y_character` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(36) DEFAULT NULL,
  `FName` varchar(36) DEFAULT NULL,
  `FRemark` varchar(300) DEFAULT NULL,
  `FOrganizationID` varchar(36) DEFAULT NULL,
  `Fstate` int(11) DEFAULT NULL,
  `FCreaterID` varchar(36) DEFAULT NULL,
  `FModifiedID` varchar(36) DEFAULT NULL,
  `FLastModifiedID` varchar(36) DEFAULT NULL,
  `FCreateTime` datetime DEFAULT NULL,
  `FModifiedTime` datetime DEFAULT NULL,
  `FLastModifiedTime` datetime DEFAULT NULL,
  `FIsGroups` varchar(36) DEFAULT NULL,
  `FGroupsID` varchar(36) DEFAULT NULL,
  `Flag` int(11) NOT NULL AUTO_INCREMENT,
  `FPrivileges` int(11) DEFAULT '0',
  PRIMARY KEY (`FID`),
  UNIQUE KEY `Flag_UNIQUE` (`Flag`) USING BTREE,
  KEY `FK_Character_REFERENCE_Organization` (`FOrganizationID`) USING BTREE,
  KEY `FK_Character_REFERENCE_SocialGroups` (`FGroupsID`) USING BTREE,
  CONSTRAINT `y_character_ibfk_1` FOREIGN KEY (`FOrganizationID`) REFERENCES `y_basic_organization` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_comment
-- ----------------------------
DROP TABLE IF EXISTS `y_comment`;
CREATE TABLE `y_comment` (
  `FID` varchar(36) NOT NULL,
  `FContents` varchar(300) DEFAULT NULL,
  `FTime` datetime DEFAULT NULL,
  `FForeignID` varchar(36) DEFAULT NULL,
  `FMemberID` varchar(36) DEFAULT NULL,
  `FType` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_Comment_REFERENCE_Type` (`FType`) USING BTREE,
  KEY `FK_Comment_REFERENCE_Member` (`FMemberID`) USING BTREE,
  CONSTRAINT `y_comment_ibfk_1` FOREIGN KEY (`FMemberID`) REFERENCES `y_basic_member` (`FID`),
  CONSTRAINT `y_comment_ibfk_2` FOREIGN KEY (`FType`) REFERENCES `y_basic_type` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_commentscore
-- ----------------------------
DROP TABLE IF EXISTS `y_commentscore`;
CREATE TABLE `y_commentscore` (
  `Fid` varchar(36) NOT NULL,
  `tel` varchar(36) DEFAULT '',
  `demandOrSupplyId` varchar(36) NOT NULL,
  `score` varchar(11) DEFAULT NULL,
  `type` int(2) DEFAULT NULL COMMENT ' 0是供应 1是需求',
  `updateTime` datetime DEFAULT NULL,
  `remark` text,
  `flag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`Fid`),
  UNIQUE KEY `flag_UNIQUE` (`flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_companyproduct
-- ----------------------------
DROP TABLE IF EXISTS `y_companyproduct`;
CREATE TABLE `y_companyproduct` (
  `FID` varchar(36) NOT NULL,
  `FName` varchar(100) DEFAULT NULL,
  `FLogoImage` text,
  `FDescription` text,
  `FCompanyId` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_comPro_com` (`FCompanyId`) USING BTREE,
  CONSTRAINT `y_companyproduct_ibfk_1` FOREIGN KEY (`FCompanyId`) REFERENCES `y_basic_membercompany` (`FCID`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_complaint
-- ----------------------------
DROP TABLE IF EXISTS `y_complaint`;
CREATE TABLE `y_complaint` (
  `FID` varchar(36) NOT NULL,
  `FSupplyDemandID` varchar(36) DEFAULT NULL,
  `FType` int(11) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  `complaintId` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_functionentries_functionjournalentry
-- ----------------------------
DROP TABLE IF EXISTS `y_functionentries_functionjournalentry`;
CREATE TABLE `y_functionentries_functionjournalentry` (
  `FID` varchar(36) NOT NULL,
  `FSeq` varchar(36) NOT NULL,
  `FFunctionName` varchar(36) DEFAULT NULL,
  `FRemark` varchar(300) DEFAULT NULL,
  `FParentID` varchar(36) DEFAULT NULL,
  `Fjscode` text,
  `FIsBasic` varchar(36) DEFAULT NULL,
  `Flag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `Flag_UNIQUE` (`Flag`) USING BTREE,
  KEY `FK_FunctionJournalEntry_REFERENCE_Function` (`FParentID`) USING BTREE,
  CONSTRAINT `y_functionentries_functionjournalentry_ibfk_1` FOREIGN KEY (`FParentID`) REFERENCES `y_systemconfiguration_function` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=242 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_image
-- ----------------------------
DROP TABLE IF EXISTS `y_image`;
CREATE TABLE `y_image` (
  `FID` varchar(36) NOT NULL,
  `FManShowID` varchar(36) DEFAULT NULL,
  `FAddress` varchar(300) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_Image_REFERENCE_ManShowInformation` (`FManShowID`) USING BTREE,
  CONSTRAINT `y_image_ibfk_1` FOREIGN KEY (`FManShowID`) REFERENCES `y_manshowinformation` (`FID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_initiationapply
-- ----------------------------
DROP TABLE IF EXISTS `y_initiationapply`;
CREATE TABLE `y_initiationapply` (
  `FID` varchar(36) NOT NULL,
  `FName` varchar(100) DEFAULT NULL,
  `FPhone` varchar(50) DEFAULT NULL,
  `FCompanyName` varchar(100) DEFAULT NULL,
  `FCompanyPosition` varchar(50) DEFAULT NULL,
  `FNativePlace` varchar(36) DEFAULT NULL,
  `FApplyDate` datetime DEFAULT NULL,
  `FmemberId` varchar(36) DEFAULT NULL,
  `FGroupsId` varchar(36) DEFAULT NULL,
  `FState` int(11) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  `FisHide` varchar(36) DEFAULT '0',
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_manshowinformation
-- ----------------------------
DROP TABLE IF EXISTS `y_manshowinformation`;
CREATE TABLE `y_manshowinformation` (
  `FID` varchar(36) NOT NULL,
  `FMemberID` varchar(36) DEFAULT NULL,
  `FContents` text,
  `FPublished` datetime DEFAULT NULL,
  `FIsOpen` int(11) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_ManShowInformation_REFERENCE_Member` (`FMemberID`) USING BTREE,
  CONSTRAINT `y_manshowinformation_ibfk_1` FOREIGN KEY (`FMemberID`) REFERENCES `y_basic_member` (`FID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_orderentries_edesignateddriving
-- ----------------------------
DROP TABLE IF EXISTS `y_orderentries_edesignateddriving`;
CREATE TABLE `y_orderentries_edesignateddriving` (
  `fid` varchar(36) NOT NULL COMMENT '主键',
  `fnumber` varchar(36) DEFAULT NULL COMMENT '业务编码',
  `fbespeakid` varchar(100) DEFAULT NULL COMMENT '预约id',
  `fbespeaktotal` int(11) DEFAULT NULL COMMENT '预约人数',
  `forderid` varchar(100) DEFAULT NULL COMMENT '订单id',
  `forderstate` varchar(50) DEFAULT NULL COMMENT '订单状态码',
  `fdrivernumber` varchar(200) DEFAULT NULL COMMENT '司机工号',
  `fdriverrole` varchar(200) DEFAULT NULL COMMENT '角色',
  `fdrivername` varchar(100) DEFAULT NULL COMMENT '姓名',
  `fdriverphone` varchar(30) DEFAULT NULL COMMENT '司机电话',
  `frefreshtime` datetime DEFAULT NULL COMMENT '后台刷新订单列表时间',
  `ffromaddress` varchar(200) DEFAULT NULL COMMENT '出发地',
  `ftoaddress` varchar(200) DEFAULT NULL COMMENT '目的地',
  `flat` varchar(100) DEFAULT NULL COMMENT '下单时的经度',
  `flon` varchar(100) DEFAULT NULL COMMENT '下单时的纬度',
  `fisptypeid` varchar(50) DEFAULT NULL COMMENT '服务提供商类型id',
  `fordertime` datetime DEFAULT NULL COMMENT '订单时间',
  `fmemberid` varchar(36) DEFAULT NULL COMMENT '会员id',
  `fchamberofcommerceid` varchar(36) DEFAULT NULL COMMENT '会员所属商会',
  PRIMARY KEY (`fid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_organizationauthorized
-- ----------------------------
DROP TABLE IF EXISTS `y_organizationauthorized`;
CREATE TABLE `y_organizationauthorized` (
  `FID` varchar(36) NOT NULL,
  `FAdminID` varchar(36) DEFAULT NULL,
  `FFunctionEntryID` varchar(36) DEFAULT NULL,
  `FFunctionID` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_OrganizationAuthorized_REFERENCE_Users` (`FAdminID`) USING BTREE,
  KEY `FK_OrganizationAuthorized_REFERENCE_FunctionJournalEntrys` (`FFunctionEntryID`) USING BTREE,
  KEY `FK_OrganizationAuthorized_REFERENCE_Function` (`FFunctionID`) USING BTREE,
  CONSTRAINT `y_organizationauthorized_ibfk_1` FOREIGN KEY (`FFunctionID`) REFERENCES `y_systemconfiguration_function` (`FID`),
  CONSTRAINT `y_organizationauthorized_ibfk_2` FOREIGN KEY (`FFunctionEntryID`) REFERENCES `y_functionentries_functionjournalentry` (`FID`),
  CONSTRAINT `y_organizationauthorized_ibfk_3` FOREIGN KEY (`FAdminID`) REFERENCES `y_system_users` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=30764 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_pointlike
-- ----------------------------
DROP TABLE IF EXISTS `y_pointlike`;
CREATE TABLE `y_pointlike` (
  `FID` varchar(36) NOT NULL,
  `FManShowID` varchar(36) DEFAULT NULL,
  `FArePointLike` int(11) DEFAULT NULL,
  `FPointLikeType` int(11) DEFAULT NULL,
  `FPointLikeTime` datetime DEFAULT NULL,
  `FMemberID` varchar(36) DEFAULT NULL,
  `FTypeID` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_PointLike_REFERENCE_Type` (`FTypeID`) USING BTREE,
  KEY `FK_PointLike_REFERENCE_Member` (`FMemberID`) USING BTREE,
  CONSTRAINT `y_pointlike_ibfk_1` FOREIGN KEY (`FMemberID`) REFERENCES `y_basic_member` (`FID`),
  CONSTRAINT `y_pointlike_ibfk_2` FOREIGN KEY (`FTypeID`) REFERENCES `y_basic_type` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=223 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_replytocomment
-- ----------------------------
DROP TABLE IF EXISTS `y_replytocomment`;
CREATE TABLE `y_replytocomment` (
  `FID` varchar(36) NOT NULL,
  `FCommentID` varchar(36) DEFAULT NULL,
  `FReplyID` varchar(36) DEFAULT NULL,
  `FReplyContents` varchar(300) DEFAULT NULL,
  `FReplyTime` datetime DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_ReplyToComment_REFERENCE_Y_COMMEN` (`FCommentID`) USING BTREE,
  CONSTRAINT `y_replytocomment_ibfk_1` FOREIGN KEY (`FCommentID`) REFERENCES `y_comment` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_roleauthorization
-- ----------------------------
DROP TABLE IF EXISTS `y_roleauthorization`;
CREATE TABLE `y_roleauthorization` (
  `FID` varchar(36) NOT NULL,
  `FFunctionJournalEntryID` varchar(36) DEFAULT NULL,
  `FOrganizationID` text,
  `FCharacterID` varchar(36) NOT NULL,
  `FGroupsId` text,
  `FFunctionID` varchar(36) DEFAULT NULL,
  `FAdminID` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_RoleAuthorization_REFERENCE_Character` (`FCharacterID`) USING BTREE,
  KEY `FK_RoleAuthorization_REFERENCE_FunctionJournalEntry` (`FFunctionJournalEntryID`) USING BTREE,
  KEY `FK_RoleAuthorization_REFERENCE_Function` (`FFunctionID`) USING BTREE,
  KEY `FK_RoleAuthorization_REFERENCE_Users` (`FAdminID`) USING BTREE,
  CONSTRAINT `y_roleauthorization_ibfk_1` FOREIGN KEY (`FCharacterID`) REFERENCES `y_character` (`FID`),
  CONSTRAINT `y_roleauthorization_ibfk_2` FOREIGN KEY (`FFunctionID`) REFERENCES `y_systemconfiguration_function` (`FID`),
  CONSTRAINT `y_roleauthorization_ibfk_3` FOREIGN KEY (`FFunctionJournalEntryID`) REFERENCES `y_functionentries_functionjournalentry` (`FID`),
  CONSTRAINT `y_roleauthorization_ibfk_4` FOREIGN KEY (`FAdminID`) REFERENCES `y_system_users` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=49217 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_supply_group
-- ----------------------------
DROP TABLE IF EXISTS `y_supply_group`;
CREATE TABLE `y_supply_group` (
  `fid` varchar(36) NOT NULL,
  `supplyId` varchar(36) DEFAULT NULL,
  `assuranceId` varchar(36) DEFAULT NULL,
  `ispass` varchar(36) DEFAULT NULL,
  `groupid` varchar(36) DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `FisHide` varchar(36) DEFAULT '0',
  PRIMARY KEY (`fid`),
  KEY `supply_relationship` (`supplyId`) USING BTREE,
  KEY `y_supply_group_ibfk_2` (`assuranceId`) USING BTREE,
  CONSTRAINT `y_supply_group_ibfk_1` FOREIGN KEY (`supplyId`) REFERENCES `y_basic_socialgroupssupply` (`FID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `y_supply_group_ibfk_2` FOREIGN KEY (`assuranceId`) REFERENCES `y_basic_assurancecontent` (`FID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_system_appversions
-- ----------------------------
DROP TABLE IF EXISTS `y_system_appversions`;
CREATE TABLE `y_system_appversions` (
  `FID` varchar(36) NOT NULL,
  `FVersionNumber` varchar(50) DEFAULT NULL,
  `FDownloadUrl` varchar(200) DEFAULT NULL,
  `FUpdateDetail` text,
  `Flag` int(11) NOT NULL AUTO_INCREMENT,
  `ChanelNo` int(11) DEFAULT NULL,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `Flag_UNIQUE` (`Flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_system_config
-- ----------------------------
DROP TABLE IF EXISTS `y_system_config`;
CREATE TABLE `y_system_config` (
  `FID` int(11) NOT NULL AUTO_INCREMENT,
  `FKey` varchar(100) DEFAULT NULL,
  `FValue` varchar(100) DEFAULT NULL,
  `FComment` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`FID`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_system_users
-- ----------------------------
DROP TABLE IF EXISTS `y_system_users`;
CREATE TABLE `y_system_users` (
  `FID` varchar(36) NOT NULL,
  `FAccount` varchar(36) NOT NULL,
  `FUserPassword` varchar(100) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FTypeID` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  `FPrivileges` int(11) DEFAULT '0',
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=439 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_system_users_loginrecord
-- ----------------------------
DROP TABLE IF EXISTS `y_system_users_loginrecord`;
CREATE TABLE `y_system_users_loginrecord` (
  `FID` varchar(36) NOT NULL,
  `FUsersID` varchar(36) DEFAULT NULL,
  `FUsersLoginTime` datetime DEFAULT NULL,
  `FUsersLoginIP` varchar(22) DEFAULT NULL,
  `FUsersName` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_LoginRecord_REFERENCE_Users` (`FUsersID`) USING BTREE,
  CONSTRAINT `y_system_users_loginrecord_ibfk_1` FOREIGN KEY (`FUsersID`) REFERENCES `y_system_users` (`FID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_systemconfiguration_encodingrules
-- ----------------------------
DROP TABLE IF EXISTS `y_systemconfiguration_encodingrules`;
CREATE TABLE `y_systemconfiguration_encodingrules` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(36) DEFAULT NULL,
  `FBelongOrganizationID` varchar(36) DEFAULT NULL,
  `FInvoicesType` varchar(36) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FFixedValue` varchar(36) DEFAULT NULL,
  `FDateValue` varchar(36) DEFAULT NULL,
  `FSerialNumber` varchar(36) DEFAULT NULL,
  `FPreview` varchar(300) DEFAULT NULL,
  `FCreaterID` varchar(36) DEFAULT NULL,
  `FModifiedID` varchar(36) DEFAULT NULL,
  `FLastModifiedID` varchar(36) DEFAULT NULL,
  `FCreateTime` datetime DEFAULT NULL,
  `FModifiedTime` datetime DEFAULT NULL,
  `FLastModifiedTime` datetime DEFAULT NULL,
  `Flag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `Flag_UNIQUE` (`Flag`) USING BTREE,
  KEY `FK_EncodingRules_REFERENCE_Organization` (`FBelongOrganizationID`) USING BTREE,
  CONSTRAINT `y_systemconfiguration_encodingrules_ibfk_1` FOREIGN KEY (`FBelongOrganizationID`) REFERENCES `y_basic_organization` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_systemconfiguration_function
-- ----------------------------
DROP TABLE IF EXISTS `y_systemconfiguration_function`;
CREATE TABLE `y_systemconfiguration_function` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(36) DEFAULT NULL,
  `FAccount` varchar(36) DEFAULT NULL,
  `FRemark` varchar(300) DEFAULT NULL,
  `Flag` int(11) NOT NULL AUTO_INCREMENT,
  `FMenuID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `Flag_UNIQUE` (`Flag`) USING BTREE,
  KEY `FK_Function_REFERENCE_Menu` (`FMenuID`) USING BTREE,
  CONSTRAINT `y_systemconfiguration_function_ibfk_1` FOREIGN KEY (`FMenuID`) REFERENCES `y_systemconfiguration_menu` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_systemconfiguration_log
-- ----------------------------
DROP TABLE IF EXISTS `y_systemconfiguration_log`;
CREATE TABLE `y_systemconfiguration_log` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(36) DEFAULT NULL,
  `FUserID` varchar(36) DEFAULT NULL,
  `FType` int(11) DEFAULT NULL,
  `FTime` datetime DEFAULT NULL,
  `FContent` varchar(300) DEFAULT NULL,
  `FIP` varchar(100) DEFAULT NULL,
  `FRemark` varchar(300) DEFAULT NULL,
  `Flag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `Flag_UNIQUE` (`Flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=326395 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_systemconfiguration_menu
-- ----------------------------
DROP TABLE IF EXISTS `y_systemconfiguration_menu`;
CREATE TABLE `y_systemconfiguration_menu` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(36) DEFAULT NULL,
  `FMenuName` varchar(36) DEFAULT NULL,
  `FSuperiorID` varchar(36) DEFAULT NULL,
  `FPath` varchar(36) DEFAULT NULL,
  `FRemark` varchar(300) DEFAULT NULL,
  `Flag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `Flag_UNIQUE` (`Flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_systemconfiguration_message
-- ----------------------------
DROP TABLE IF EXISTS `y_systemconfiguration_message`;
CREATE TABLE `y_systemconfiguration_message` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(36) DEFAULT NULL,
  `FMessageType` varchar(36) DEFAULT NULL,
  `FContent` varchar(300) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FCreaterID` varchar(36) DEFAULT NULL,
  `FModifiedID` varchar(36) DEFAULT NULL,
  `FLastModifiedID` varchar(36) DEFAULT NULL,
  `FCreateTime` datetime DEFAULT NULL,
  `FModifiedTime` datetime DEFAULT NULL,
  `FLastModifiedTime` datetime DEFAULT NULL,
  `Flag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `Flag_UNIQUE` (`Flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=331 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_systemconfiguration_messagerecord
-- ----------------------------
DROP TABLE IF EXISTS `y_systemconfiguration_messagerecord`;
CREATE TABLE `y_systemconfiguration_messagerecord` (
  `FID` varchar(36) NOT NULL,
  `FMessage` varchar(36) DEFAULT NULL,
  `FUserID` varchar(36) DEFAULT NULL,
  `FDate` datetime DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_MessageRecord_REFERENCE_Message` (`FMessage`) USING BTREE,
  CONSTRAINT `y_systemconfiguration_messagerecord_ibfk_1` FOREIGN KEY (`FMessage`) REFERENCES `y_systemconfiguration_message` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=67253 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_systemconfiguration_parameter
-- ----------------------------
DROP TABLE IF EXISTS `y_systemconfiguration_parameter`;
CREATE TABLE `y_systemconfiguration_parameter` (
  `FID` varchar(36) NOT NULL,
  `FNumber` varchar(36) DEFAULT NULL,
  `FName` varchar(100) DEFAULT NULL,
  `FBelongOrganizationID` varchar(36) DEFAULT NULL,
  `FRule` varchar(100) DEFAULT NULL,
  `FScope` int(11) DEFAULT NULL,
  `FBillState` int(11) DEFAULT NULL,
  `FCreaterID` varchar(36) DEFAULT NULL,
  `FModifiedID` varchar(36) DEFAULT NULL,
  `FLastModifiedID` varchar(36) DEFAULT NULL,
  `FCreateTime` datetime DEFAULT NULL,
  `FModifiedTime` datetime DEFAULT NULL,
  `FLastModifiedTime` datetime DEFAULT NULL,
  `Flag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `Flag_UNIQUE` (`Flag`) USING BTREE,
  KEY `FK_Parameter_REFERENCE_Organization` (`FBelongOrganizationID`) USING BTREE,
  CONSTRAINT `y_systemconfiguration_parameter_ibfk_1` FOREIGN KEY (`FBelongOrganizationID`) REFERENCES `y_basic_organization` (`FID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_visitors_record_notlogin
-- ----------------------------
DROP TABLE IF EXISTS `y_visitors_record_notlogin`;
CREATE TABLE `y_visitors_record_notlogin` (
  `FID` varchar(36) NOT NULL,
  `FUserID` varchar(36) DEFAULT NULL,
  `FName` varchar(100) DEFAULT NULL,
  `FTelphone` varchar(100) DEFAULT NULL,
  `FAccessTime` datetime DEFAULT NULL,
  `FLastAccessTime` datetime DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11007 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_wallactivity
-- ----------------------------
DROP TABLE IF EXISTS `y_wallactivity`;
CREATE TABLE `y_wallactivity` (
  `FID` varchar(36) NOT NULL,
  `FTheme` varchar(50) DEFAULT NULL,
  `FTheUrl` varchar(100) DEFAULT NULL,
  `FProvincesID` varchar(36) DEFAULT NULL,
  `FCityID` varchar(36) DEFAULT NULL,
  `FCommerceID` varchar(36) DEFAULT NULL,
  `FState` int(11) DEFAULT NULL,
  `FCreateTime` datetime DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  `FComment` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_WallActivity_REFERENCE_SocialGroups` (`FCommerceID`) USING BTREE,
  KEY `FK_WallActivity_REFERENCE_Province` (`FProvincesID`) USING BTREE,
  KEY `FK_Y_WallActivity_REFERENCE_City` (`FCityID`) USING BTREE,
  CONSTRAINT `y_wallactivity_ibfk_1` FOREIGN KEY (`FProvincesID`) REFERENCES `y_basic_province` (`FID`),
  CONSTRAINT `y_wallactivity_ibfk_2` FOREIGN KEY (`FCommerceID`) REFERENCES `y_basic_socialgroups` (`FID`),
  CONSTRAINT `y_wallactivity_ibfk_3` FOREIGN KEY (`FCityID`) REFERENCES `y_basic_city` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_wallreply
-- ----------------------------
DROP TABLE IF EXISTS `y_wallreply`;
CREATE TABLE `y_wallreply` (
  `FID` varchar(36) NOT NULL,
  `FUserID` varchar(36) DEFAULT NULL,
  `FContent` varchar(300) DEFAULT NULL,
  `FCommerceID` varchar(36) DEFAULT NULL,
  `FReplyTime` datetime DEFAULT NULL,
  `FState` int(11) DEFAULT NULL,
  `FSum` int(11) DEFAULT NULL,
  `FWallActivityID` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_WallReply_REFERENCE_WallActivity` (`FWallActivityID`) USING BTREE,
  KEY `FK_WallReply_REFERENCE_Member` (`FUserID`) USING BTREE,
  CONSTRAINT `y_wallreply_ibfk_1` FOREIGN KEY (`FUserID`) REFERENCES `y_basic_member` (`FID`),
  CONSTRAINT `y_wallreply_ibfk_2` FOREIGN KEY (`FWallActivityID`) REFERENCES `y_wallactivity` (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=817 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for y_wallsadvertising
-- ----------------------------
DROP TABLE IF EXISTS `y_wallsadvertising`;
CREATE TABLE `y_wallsadvertising` (
  `FID` varchar(36) NOT NULL,
  `FImage` text,
  `FTitle` varchar(36) DEFAULT NULL,
  `FDescribe` text,
  `FCreationTime` datetime DEFAULT NULL,
  `FWallActivityID` varchar(36) DEFAULT NULL,
  `FLag` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`FID`),
  UNIQUE KEY `FLag_UNIQUE` (`FLag`) USING BTREE,
  KEY `FK_WallsAdvertising_REFERENCE_WallActivity` (`FWallActivityID`) USING BTREE,
  CONSTRAINT `y_wallsadvertising_ibfk_1` FOREIGN KEY (`FWallActivityID`) REFERENCES `y_wallactivity` (`FID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- View structure for demandandsupply
-- ----------------------------
DROP VIEW IF EXISTS `demandandsupply`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `demandandsupply` AS (select `supply`.`FID` AS `FID`,`supply`.`FHeadline` AS `FHeadline`,`supply`.`Fimages` AS `FImages`,`supply`.`FMessage` AS `FMessage`,`supply`.`FPublisherID` AS `FPublisherID`,`supply`.`FSocialGroupsID` AS `FSocialGroupsID`,`supply`.`FProvinceID` AS `FProvinceID`,`supply`.`FCityID` AS `FCityID`,`supply`.`FCountyID` AS `FCountyID`,`supply`.`FTradeID` AS `FTradeID`,`supply`.`FPublisherTime` AS `FPublisherTime`,`supply`.`FAreGuarantee` AS `FAreGuarantee`,0 AS `FType`,`supply`.`Flevel` AS `Flevel`,`supply`.`FBillState` AS `FBillState`,`supply`.`FLag` AS `FLag`,`ymember`.`FMobilePhone` AS `FMobilePhone`,`supply`.`FRank` AS `FRank`,`supply`.`FIsHide` AS `FIsHide` from (`y_basic_socialgroupssupply` `supply` join `y_basic_member` `ymember`) where (`supply`.`FPublisherID` = `ymember`.`FID`)) union (select `demand`.`FID` AS `FID`,`demand`.`FHeadline` AS `FHeadline`,`demand`.`FImages` AS `FImages`,`demand`.`FMessage` AS `FMessage`,`demand`.`FPublisherID` AS `FPublisherID`,`demand`.`FSocialGroupsID` AS `FSocialGroupsID`,`demand`.`FProvinceID` AS `FProvinceID`,`demand`.`FCityID` AS `FCityID`,`demand`.`FCountyID` AS `FCountyID`,`demand`.`FTradeID` AS `FTradeID`,`demand`.`FPublisherTime` AS `FPublisherTime`,0 AS `FAreGuarantee`,1 AS `FType`,`demand`.`Flevel` AS `Flevel`,`demand`.`FBillState` AS `FBillState`,`demand`.`FLag` AS `FLag`,`ymember`.`FMobilePhone` AS `FMobilePhone`,`demand`.`FRank` AS `FRank`,`demand`.`FIsHide` AS `FIsHide` from (`y_basic_socialgroupsdemand` `demand` join `y_basic_member` `ymember`) where (`demand`.`FPublisherID` = `ymember`.`FID`)) ;

-- ----------------------------
-- View structure for informationview
-- ----------------------------
DROP VIEW IF EXISTS `informationview`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `informationview` AS select `c`.`FID` AS `fid`,`s`.`FLogoImage` AS `ImageUrl`,`s`.`FMessage` AS `Content`,cast(`c`.`FYNParticipation` as signed) AS `state`,`c`.`updatetime` AS `newsDate`,`s`.`FSocialGroupsID` AS `businessId`,`c`.`FInformPeopleID` AS `memberId`,`s`.`FID` AS `noticeId`,1 AS `type`,`c`.`FisHide` AS `Status` from (`y_basic_socialgroupsinformrecord` `c` join `y_basic_socialgroupsinform` `s`) where ((`c`.`FInformID` = `s`.`FID`) and (`c`.`FisHide` <> '1')) union all select `g`.`fid` AS `fid`,`s`.`Fimages` AS `ImageUrl`,`s`.`FMessage` AS `Content`,`g`.`ispass` AS `state`,`g`.`updatetime` AS `newsDate`,`g`.`groupid` AS `businessId`,`s`.`FPublisherID` AS `memberId`,`s`.`FID` AS `noticeId`,2 AS `type`,`g`.`FisHide` AS `Status` from ((`y_supply_group` `g` join `y_basic_assurancecontent` `a`) join `y_basic_socialgroupssupply` `s`) where ((`g`.`assuranceId` = `a`.`FID`) and (`g`.`supplyId` = `s`.`FID`) and (`g`.`ispass` = '17') and (`g`.`FisHide` <> '1')) union all select distinct `a`.`FID` AS `fid`,NULL AS `ImageUrl`,`a`.`FName` AS `Content`,`a`.`FState` AS `state`,`a`.`FApplyDate` AS `newsDate`,`a`.`FGroupsId` AS `businessId`,`m`.`FID` AS `memberId`,NULL AS `noticeId`,3 AS `type`,`a`.`FisHide` AS `Status` from (`y_initiationapply` `a` join `y_basic_member` `m`) where ((`a`.`FmemberId` = `m`.`FID`) and (`a`.`FisHide` <> '1')) ;

-- ----------------------------
-- Procedure structure for jx
-- ----------------------------
DROP PROCEDURE IF EXISTS `jx`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `jx`(out outparam varchar(200))
begin 
	DECLARE outname VARCHAR(200);
	select fname from y_basic_member limit 4,1 into outname;
	select outname into outparam;
end
;;
DELIMITER ;
