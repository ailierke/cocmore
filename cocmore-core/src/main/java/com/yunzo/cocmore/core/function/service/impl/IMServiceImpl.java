package com.yunzo.cocmore.core.function.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicGroup;
import com.yunzo.cocmore.core.function.model.mysql.YBasicGrouppeople;
import com.yunzo.cocmore.core.function.model.mysql.YBasicImaccount;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.service.IMService;
import com.yunzo.cocmore.utils.base.AliyunOSSUtils;
import com.yunzo.cocmore.utils.base.CombineHeadphotoUtil;
import com.yunzo.cocmore.utils.base.IMGSize;
import com.yunzo.cocmore.utils.number.EncryptionForTellPhone;
@Service("imService")
@Transactional
public class IMServiceImpl implements IMService {
	private static Logger logger = Logger.getLogger(IMServiceImpl.class);
	private ObjectMapper objectMapper = new ObjectMapper();
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "获取用户属性（批量）")
	public List<Map<String, Object>> getUserList(String infoMap) throws IOException{
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> resultObject = null;
		String hql  ="";
		JsonNode node =  objectMapper.readTree(infoMap);

		JsonNode userList = node.path("userList");
		if (userList.isArray()){  
			for (JsonNode objNode : userList){ 
				logger.info("IM用户账号："+objNode.asText()); 
				hql = "from YBasicImaccount imacount where imacount.fimkey='"+objNode.asText()+"'";
				List<YBasicImaccount> imacountList =(List<YBasicImaccount>) dao.findAllByHQL(hql);//获取IM表
				if(imacountList!=null&&imacountList.size()>0){
					resultObject = new HashMap<String,Object>();
					String ftel = imacountList.get(0).getFimtel();
					hql="from YBasicMember ymember where ymember.fmobilePhone='"+ftel+"'";
					List<YBasicMember> merberList =  null;
					try {
						merberList =   (List<YBasicMember>) dao.findAllByHQL(hql);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(merberList!=null&&merberList.size()>0){
						YBasicMember merber = merberList.get(0);
						resultObject.put("n1",merber.getFname());//姓名
						resultObject.put("mid",merber.getFmobilePhone());//主键tel
						resultObject.put("sex",merber.getFsex());//0男1女 与数据库一样
						resultObject.put("tel",merber.getFmobilePhone());//手机号码
						resultObject.put("n2",objNode.asText());//环信账号
						resultObject.put("n3",merber.getFheadImage());//用户头像
						resultList.add(resultObject);//加入返回的list中
					}

				}
			}  
		}  
		return resultList;
	}
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "批量获取群组属性")
	public List<Map<String, Object>> getGroupProperty(String infoMap) throws JsonProcessingException, IOException {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> resultObject = null;
		JsonNode node =  objectMapper.readTree(infoMap);
		String hql= "";
		JsonNode userList = node.path("groupIdList");//群组id
		if (userList.isArray()){  
			for (JsonNode objNode : userList){ 
				logger.info("群组id："+objNode.asText());  
				//群聊表的主键就为IM群的主键(首先查找到群创建者--->获取群信息和Im用的所在商会)
				hql ="from YBasicGrouppeople ybasicpeople where ybasicpeople.fisCreater=0 and ybasicpeople.YBasicGroup.fid='"+objNode.textValue()+"'";//查询此群创建人
				List<YBasicGrouppeople> ybasicgrouppeopleList =(List<YBasicGrouppeople>) dao.findAllByHQL(hql);//获取群创建者信息
				if(ybasicgrouppeopleList!=null&&ybasicgrouppeopleList.size()>0){
					YBasicGrouppeople ybasicgrouppeople = ybasicgrouppeopleList.get(0);
					resultObject = new HashMap<String,Object>();
					resultObject.put("groupId",ybasicgrouppeople.getYBasicGroup().getFid());//环信群Id也为主键id
					resultObject.put("groupName", ybasicgrouppeople.getYBasicGroup().getFgroupName());//群名字
					resultObject.put("iconUrl", ybasicgrouppeople.getYBasicGroup().getFgroupHeadImage());//群头像
					resultObject.put("maxMember", ybasicgrouppeople.getYBasicGroup().getFgroupMaxPeople());//群最大人数
					resultObject.put("master", ybasicgrouppeople.getYBasicImaccount().getFimkey());//群创建者  环信userName
					resultObject.put("businessId",ybasicgrouppeople.getYBasicGroup().getBusinessId());//所属商会
					resultList.add(resultObject);
				}
			}  
		} 
		return resultList;
	}
	@Override
	@SystemServiceLog(description = "设置群组属性")
	public void saveGroupProperty(String infoMap) throws JsonProcessingException, IOException {
		JsonNode node = objectMapper.readTree(infoMap);
		String groupId = node.get("groupId")==null?null:node.get("groupId").textValue();//群组
		String groupName =node.get("groupName")==null?null:node.get("groupName").textValue();//群组最大人数
		int maxMember = node.get("maxMember")==null?0:new Integer(node.get("maxMember").textValue()); 
		YBasicGroup basicGroup = (YBasicGroup) dao.findById(YBasicGroup.class,groupId);
		if(basicGroup!=null){
			if(maxMember!=0){
				basicGroup.setFgroupMaxPeople(maxMember);
			}
			if(groupName!=null){
				basicGroup.setFgroupName(groupName);
			}
		}
		logger.info("*************"+groupId);
	}
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "创建IM群组")
	public void createGroup(String infoMap) throws OSSException, ClientException, Exception {
		try {
			JsonNode node = objectMapper.readTree(infoMap);
			String groupId = node.get("groupId").textValue();//IM群组的id就作为群聊表的fid
			String groupName =node.get("groupName").textValue();//群组最大人数
			int maxMember = node.get("maxMember")==null?100:new Integer(node.get("maxMember").textValue());
			String tel = node.get("mid").textValue();
			String businessId = node.get("businessId").textValue();
			JsonNode userList = node.path("memberUsers");//群成员（没包含创建者）
			String downloadPath =null;
			String photoUrl =null;
			ArrayList<String> photoUri = new ArrayList<String>();//创建群头像的4张图片
			String hql ="";
			//创建群
			YBasicGroup basicGroup = new YBasicGroup();
			basicGroup.setFid(groupId);
			basicGroup.setFgroupName(groupName);
			basicGroup.setFgroupMaxPeople(maxMember);
			basicGroup.setBusinessId(businessId);
			dao.save(basicGroup);
			//添加群成员（创建者）
			YBasicGrouppeople basicGrouppeople = null;
			YBasicMember member =(YBasicMember)dao.findAllByHQL("from YBasicMember ymember where ymember.fmobilePhone='"+tel+"' and ymember.YBasicSocialgroups.fid='"+businessId+"'").get(0);
			photoUrl = member.getFheadImage().replace(",", "");
			downloadPath = photoUrl.substring(photoUrl.lastIndexOf("/")+1);
			photoUri.add(downloadPath);//添加创建者的头像uri

			hql="from YBasicImaccount account where account.fimtel='"+ member.getFmobilePhone()+"'";
			List<YBasicImaccount> basicImaccountsList =   (List<YBasicImaccount>) dao.findAllByHQL(hql);
			YBasicImaccount yBasicImaccount =null;

			if(basicImaccountsList!=null&&basicImaccountsList.size()>0){
				yBasicImaccount = basicImaccountsList.get(0);
			}
			basicGrouppeople = new YBasicGrouppeople();
			basicGrouppeople.setYBasicImaccount(yBasicImaccount);//设置IM账号
			basicGrouppeople.setFisCreater(0);//是创建者
			basicGrouppeople.setYBasicGroup(basicGroup);//设置所属群
			dao.saveOrUpdate(basicGrouppeople);//添加创建者
			int count =0 ;
			if (userList.isArray()&&userList.size()>0) { 
				//循环添加用户
				//			int count = 0;//提取3个群组头像和群创建者的投放组成群头像
				for (JsonNode objNode : userList){  
					basicGrouppeople = new YBasicGrouppeople();
					//通过imUsername来获取 YBasicImaccount的记录实体
					hql="from YBasicImaccount ybasicimaccount where  ybasicimaccount.fimkey='"+objNode.get("imUsername").textValue()+"'";//查询群成员表记录;
					List<YBasicImaccount> imaccountList =  (List<YBasicImaccount>) dao.findAllByHQL(hql);
					if(imaccountList!=null&&imaccountList.size()>0){
						yBasicImaccount =  imaccountList.get(0);
						if(count<3){
							hql="from YBasicMember ymember where ymember.fmobilePhone='"+ yBasicImaccount.getFimtel()+"'";
							List<YBasicMember> merberList =   (List<YBasicMember>) dao.findAllByHQL(hql);
							if(merberList.get(0).getFheadImage()!=null&&!merberList.get(0).getFheadImage().equals("")){
								photoUrl = merberList.get(0).getFheadImage().replace(",", "");
								downloadPath = photoUrl.substring(photoUrl.lastIndexOf("/")+1);
								photoUri.add(downloadPath);//添加其他3张图片的头像uri
							}else{
								photoUri.add("addressbook_avatar.png");//如果用户的头像是空就使用默认图片，手动传到服务器上的图片名称在ailierke_test域下
							}
						}
						logger.info("**************"+downloadPath);
						basicGrouppeople.setYBasicImaccount(yBasicImaccount);//设置IM账号
						basicGrouppeople.setFisCreater(1);//不是创建者
						basicGrouppeople.setYBasicGroup(basicGroup);//设置所属群
						dao.saveOrUpdate(basicGrouppeople);
						count++;
						System.out.println("用户IM id："+objNode.get("imUsername"));  
						System.out.println("用户所属团体id："+objNode.get("businessId"));
					}else{
						System.out.println("没有IM账号!");
					}

				}  

			} 

			if(photoUri.size()<4){
				for(int i =0;i<4-photoUri.size();i++){
					photoUri.add("addressbook_avatar.png");//如果用户的头像是空就使用默认图片，手动传到服务器上的图片名称在ailierke_test域下
				}
			}
			String url = CombineHeadphotoUtil.combineHeadPhotoImgs(AliyunOSSUtils.downloadFile(photoUri),IMGSize.X88.value());//获取群图片
			basicGroup.setFgroupHeadImage(url);//设置群图片的网络地址到数据库
			dao.saveOrUpdate(basicGroup);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "群加成员")
	public void addGroupPersons(String infoMap) throws OSSException, ClientException, Exception {
		JsonNode node = objectMapper.readTree(infoMap);
		String groupId = node.get("groupId").textValue();//群组ID
		JsonNode userList = node.path("memberUsers");//用户
		String hql= "";
		int count =0;
		String downloadPath =null;
		String photoUrl =null;
		ArrayList<String> photoUri = new ArrayList<String>();//创建群头像的4张图片
		YBasicGrouppeople basicGrouppeople = null;
		if (userList.isArray()){  
			for (JsonNode objNode : userList){
				basicGrouppeople = new YBasicGrouppeople();
				basicGrouppeople.setFisCreater(1);//不是创建者
				basicGrouppeople.setYBasicGroup(new YBasicGroup(groupId));//所属群组
				//通过imUsername来获取 YBasicImaccount的记录实体
				hql="from YBasicImaccount ybasicimaccount where  ybasicimaccount.fimkey='"+objNode.get("imUsername").textValue()+"'";//查询群成员表记录;
				YBasicImaccount yBasicImaccount = (YBasicImaccount) dao.findAllByHQLClearCache(hql).get(0);
				basicGrouppeople.setYBasicImaccount(yBasicImaccount);//设置IM账号Id
				dao.save(basicGrouppeople);
				logger.info("用户IM id："+objNode.get("imUsername"));  
				count++;
				//System.out.println("用户所属团体id："+objNode.get("businessId"));  暂时没用
			}  
		}else{
			throw new RuntimeException("传入参数错误.....");
		}
		/**
		 * 如果最开始的用户小于4就需要重新生成IMgroup图片
		 */
		YBasicGroup basicGroup = (YBasicGroup) dao.findByIdClearCache(YBasicGroup.class, groupId,null);
		/**
		 * 如果原来的群组成员的个数小于4个，那么加群组成员后群头像需要修改,因为头像是用4个用户的头像来拼成，没有4个图片就用的默认图片，大于4个就不需要
		 */
		Integer peopleNUM = basicGroup.getYBasicGrouppeoples().size();//增加人数后的总数
		/**
		 * 如果在增加之前人总数小于4个头像就要重新读取
		 */
		if(peopleNUM-count<4){
			/**
			 * 加入管理员的头像 ,一个群肯定有一个管理员
			 */
			YBasicGrouppeople IMgrouppeople = (YBasicGrouppeople) dao.findAllByHQLClearCache("from YBasicGrouppeople ygrouppeople where ygrouppeople.fisCreater=0 and ygrouppeople.YBasicGroup.fid='"+groupId+"'").get(0);
			hql="from YBasicMember ymember where ymember.fmobilePhone='"+ IMgrouppeople.getYBasicImaccount().getFimtel()+"'";
			List<YBasicMember> merberList =   (List<YBasicMember>) dao.findAllByHQLClearCache(hql);
			/**
			 * 判断用户有头像，并且格式是在我们的图片服务器上面
			 */
			if(merberList.get(0).getFheadImage()!=null&&!merberList.get(0).getFheadImage().equals("")&&merberList.get(0).getFheadImage().contains("http://yunzo.oss.aliyuncs.com/cocmore/")){
				photoUrl = merberList.get(0).getFheadImage().replace(",", "");
				downloadPath = photoUrl.substring(photoUrl.lastIndexOf("/")+1);
				photoUri.add(downloadPath);//添加其他3张图片的头像uri
			}else{
				photoUri.add("addressbook_avatar.png");//如果用户的头像是空就使用默认图片，手动传到服务器上的图片名称在ailierke_test域下
			}

			/**
			 * 加入不是管理员的另外三个人的头像
			 */
			count=0;
			List<YBasicGrouppeople> IMgrouppeopleList = (List<YBasicGrouppeople>) dao.findAllByHQLClearCache("from YBasicGrouppeople ygrouppeople where ygrouppeople.fisCreater=1 and ygrouppeople.YBasicGroup.fid='"+groupId+"'");
			if(IMgrouppeopleList!=null&&IMgrouppeopleList.size()>0){
				for(YBasicGrouppeople IMpeople :IMgrouppeopleList){
					if(count==3){
						break;
					}
					hql="from YBasicMember ymember where ymember.fmobilePhone='"+ IMpeople.getYBasicImaccount().getFimtel()+"'";
					merberList =   (List<YBasicMember>) dao.findAllByHQLClearCache(hql);
					/**
					 * 判断用户有头像，并且格式是在我们的图片服务器上面
					 */
					if(merberList.get(0).getFheadImage()!=null&&!merberList.get(0).getFheadImage().equals("")&&merberList.get(0).getFheadImage().contains("http://yunzo.oss.aliyuncs.com/cocmore/")){
						photoUrl = merberList.get(0).getFheadImage().replace(",", "");
						downloadPath = photoUrl.substring(photoUrl.lastIndexOf("/")+1);
						photoUri.add(downloadPath);//添加其他3张图片的头像uri
					}else{
						photoUri.add("addressbook_avatar.png");//如果用户的头像是空就使用默认图片，手动传到服务器上的图片名称在ailierke_test域下
					}
					count++;
				}
				//如果加上管理员还是没有4个人就用默认头像补充
				if(photoUri.size()<4){
					for(int i =0;i<4-photoUri.size();i++){
						photoUri.add("addressbook_avatar.png");//如果用户的头像是空就使用默认图片，手动传到服务器上的图片名称在ailierke_test域下
					}
				}
			}

			String url = CombineHeadphotoUtil.combineHeadPhotoImgs(AliyunOSSUtils.downloadFile(photoUri),IMGSize.X88.value());//获取群图片
			basicGroup.setFgroupHeadImage(url);//设置群图片的网络地址到数据库
			dao.saveOrUpdate(basicGroup);
		}

	}
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "删除群组成员")
	public void delGroupPersons(String infoMap) throws OSSException, ClientException, Exception {
		JsonNode node = objectMapper.readTree(infoMap);
		String hql="";
		int count =0;
		String downloadPath =null;
		String photoUrl =null;
		ArrayList<String> photoUri = new ArrayList<String>();//创建群头像的4张图片
		List<YBasicGrouppeople> ybasicgrouppeopleList =null;
		String groupId = node.get("groupId").textValue();//群组ID
		JsonNode userList = node.path("memberUsers");//用户
		if (userList.isArray()){  
			for (JsonNode objNode : userList){  
				hql ="from YBasicGrouppeople ybasicpeople where ybasicpeople.YBasicImaccount.fimkey='"+objNode.asText()+"' and ybasicpeople.YBasicGroup.fid='"+groupId+"'";//查询要删除群组
				ybasicgrouppeopleList =(List<YBasicGrouppeople>) dao.findAllByHQL(hql);//获取群创建者信息
				if(ybasicgrouppeopleList!=null&&ybasicgrouppeopleList.size()>0){
					dao.delete(ybasicgrouppeopleList.get(0));//删除群成员的此用户
				}
				logger.info("环信用户name"+objNode.asText());  
			}  
		} 


		List<YBasicMember> merberList  = null;
		/**
		 * 如果删除后总人数小于4就改变群头像
		 */
		YBasicGroup basicGroup = (YBasicGroup) dao.findByIdClearCache(YBasicGroup.class, groupId,null);
		/**
		 * 如果组成员的个数小于4个，那么加群组成员后群头像需要修改
		 */
		Integer peopleNUM = basicGroup.getYBasicGrouppeoples().size();//增加人数后的总数
		/**
		 * 如果删除后总人数小于4就改变群头像
		 */
		if(peopleNUM<4){
			/**
			 * 加入所有查询到的人的头像（管理员）在其中
			 */
			count=0;
			List<YBasicGrouppeople> IMgrouppeopleList = (List<YBasicGrouppeople>) dao.findAllByHQLClearCache("from YBasicGrouppeople ygrouppeople where  ygrouppeople.YBasicGroup.fid='"+groupId+"'");
			if(IMgrouppeopleList!=null&&IMgrouppeopleList.size()>0){
				for(YBasicGrouppeople IMpeople :IMgrouppeopleList){
					if(count==3){
						break;
					}
					hql="from YBasicMember ymember where ymember.fmobilePhone='"+ IMpeople.getYBasicImaccount().getFimtel()+"'";
					merberList =   (List<YBasicMember>) dao.findAllByHQLClearCache(hql);
					/**
					 * 判断用户有头像，并且格式是在我们的图片服务器上面
					 */
					if(merberList.get(0).getFheadImage()!=null&&!merberList.get(0).getFheadImage().equals("")&&merberList.get(0).getFheadImage().contains("http://yunzo.oss.aliyuncs.com/cocmore/")){
						photoUrl = merberList.get(0).getFheadImage().replace(",", "");
						downloadPath = photoUrl.substring(photoUrl.lastIndexOf("/")+1);
						photoUri.add(downloadPath);//添加其他3张图片的头像uri
					}else{
						photoUri.add("addressbook_avatar.png");//如果用户的头像是空就使用默认图片，手动传到服务器上的图片名称在ailierke_test域下
					}
					count++;
				}
				//如果加上管理员还是没有4个人就用默认头像补充
				if(photoUri.size()<4){
					for(int i =0;i<4-photoUri.size();i++){
						photoUri.add("addressbook_avatar.png");//如果用户的头像是空就使用默认图片，手动传到服务器上的图片名称在ailierke_test域下
					}
				}
			}

			String url = CombineHeadphotoUtil.combineHeadPhotoImgs(AliyunOSSUtils.downloadFile(photoUri),IMGSize.X88.value());//获取群图片
			basicGroup.setFgroupHeadImage(url);//设置群图片的网络地址到数据库
			dao.saveOrUpdate(basicGroup);
		}
	}
	@Override
	@SystemServiceLog(description = "删除群组")
	public void delGroupById(String infoMap) throws JsonProcessingException, IOException {
		JsonNode  node = objectMapper.readTree(infoMap);
		String groupId = node.get("groupId").textValue();//群组id
		dao.deleteByKey(groupId, YBasicGroup.class);//删除群组，同时自动的删除下面的群组成员
	}
	@Override
	public void sava(YBasicImaccount obj) {
		// TODO Auto-generated method stub
		dao.save(obj);
	}
	@Override
	public boolean findaccount(String tel) throws Exception {
		boolean flag = true;
		List list = dao.findAllByHQL("from YBasicImaccount ybasicimaccount where ybasicimaccount.fimtel='"+tel+"'");
		if(list.size()>0){
			flag = false;
		}
		return flag;
	}
	
}
