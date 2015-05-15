package com.yunzo.cocmore.core.function.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCounty;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDistrict;
import com.yunzo.cocmore.core.function.model.mysql.YBasicEmployee;
import com.yunzo.cocmore.core.function.model.mysql.YBasicImaccount;
import com.yunzo.cocmore.core.function.model.mysql.YBasicJoinActivity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicLabel;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicOrganization;
import com.yunzo.cocmore.core.function.model.mysql.YBasicProvince;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicType;
import com.yunzo.cocmore.core.function.model.mysql.YComment;
import com.yunzo.cocmore.core.function.model.mysql.YSystemUsers;
import com.yunzo.cocmore.core.function.model.mysql.YWallactivity;
import com.yunzo.cocmore.core.function.service.GroupsService;
import com.yunzo.cocmore.core.function.util.LabelXMLToObject;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.util.Status;
import com.yunzo.cocmore.core.function.util.Tree1;
import com.yunzo.cocmore.core.function.vo.GroupTreeVo;
import com.yunzo.cocmore.core.function.vo.ImportGroupsVo;
import com.yunzo.cocmore.utils.base.CRC32Util;
import com.yunzo.cocmore.utils.base.IMUtils;
import com.yunzo.cocmore.utils.base.MD5Util;
import com.yunzo.cocmore.utils.number.EncryptionForTellPhone;
/**
 * 社会团体业务实现类
 * @author jiangxing
 *
 */
@Transactional
@Service("groupsService")
public class GroupsServiceImpl implements GroupsService {
	private static final Logger logger = Logger.getLogger(GroupsServiceImpl.class);
	@Resource
	COC_HibernateDAO cOC_HibernateDAO;
	private ObjectMapper objectMapper = new ObjectMapper();
	
	//加密前段
	private static final String BEGINSTRING="yunzo";
	
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "根据会员id查询")
	public List<YBasicSocialgroups> getGroupList(String id){
		List<YBasicSocialgroups> list = null;
		String sql = null;
		sql = "select y_basic_socialgroups.FID,y_basic_socialgroups.FName from y_basic_socialgroups,y_basic_member where y_basic_socialgroups.fbillState = 5 "+
				"and y_basic_socialgroups. FID = y_basic_member.FSocialGroupsID and y_basic_socialgroups.FID in ("+
				"select FSocialGroupsID from y_basic_member where FMobilePhone = (select y_basic_member.FMobilePhone from y_basic_member where y_basic_member.FID = '"+
				id+"')) group by y_basic_socialgroups.FID";
		list = (List<YBasicSocialgroups>) cOC_HibernateDAO.getListBySql(sql);
		return list;
	}
	
	@Override
	@SystemServiceLog(description = "添加团体")
	public Boolean addGroups(YBasicSocialgroups group) {
		boolean flag = true;
		group.setLocalCacheVersion("1");//初始化商会团体版本
		try{
			cOC_HibernateDAO.save(group);
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
	    return 	flag;
	}
	
	/**
	 * 自主注册
	 * @param group
	 */
	@SystemServiceLog(description = "自由注册")
	public void addRegisterGroup(YBasicSocialgroups group){
		group.setLocalCacheVersion("1");//初始化商会团体版本
		cOC_HibernateDAO.save(group);
		
		//创建管理员账号
		YSystemUsers user = new YSystemUsers();
		user.setFid(UUID.randomUUID().toString());
		user.setFbillState(5);
		user.setFtypeId("0");
		user.setFaccount(group.getFiphone());
		user.setFuserPassword(MD5Util.md5("888888"));
		cOC_HibernateDAO.save(user);
		
		//创建会员
		YBasicMember member = new YBasicMember();
		member.setFid(UUID.randomUUID().toString());
		member.setFbillState(5);
		member.setYBasicSocialgroups(group);
		member.setIsAdmin(1);
		member.setYSystemUsers(user);
		member.setFmobilePhone(group.getFiphone());
		member.setFpassword(MD5Util.md5("888888"));
		cOC_HibernateDAO.save(member);
		
		/**
		 * update by ailierke
		 */
		//如果这个电话号码所存在的会员部存在，就生成IM账号，如果存在就跳过
		List<YBasicMember> memberList = (List<YBasicMember>) cOC_HibernateDAO.findAllByHQL("from YBasicMember m where m.fmobilePhone = '"+member.getFmobilePhone()+"'");
		//保存生成的IM账号
		YBasicImaccount im = new YBasicImaccount();
		if(memberList!=null&&memberList.size()>0){
			im = (YBasicImaccount) cOC_HibernateDAO.findAllByHQL("from YBasicImaccount imaccount where imaccount.fimtel ='"+member.getFmobilePhone()+"'").get(0);
		}else{
			Map<String,String> userMap = new HashMap<String,String>();
			userMap.put("username", member.getFmobilePhone());
			userMap.put("password", MD5Util.md5("888888"));//默认密码888888
			IMUtils.createUser(userMap);//新增
			im.setFimkey(member.getFmobilePhone());
			im.setFimpassword(MD5Util.md5("888888"));
			im.setFimtel(member.getFmobilePhone());
			cOC_HibernateDAO.save(im);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部，或根据条件查询团体，并分页")
	public PagingList<YBasicSocialgroups> getAllGroupsPagingList(Integer page, Integer pageSize,String groupId,String orgId,String groupName) {
		logger.info("查询团体信息列表....");
		PagingList<YBasicSocialgroups> pagingList = new PagingList<YBasicSocialgroups>();
		List<Object> values = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from YBasicSocialgroups groups"); 
		StringBuffer hqlCount = new StringBuffer("select count(0) from YBasicSocialgroups groups"); 
		/**
		 * 通过groupId
		 */
		if(null!=groupId&&!"".equals(groupId)){
			hqlList.append(" where groups.fid=?");
			hqlCount.append(" where groups.fid=?");
			values.add(groupId);
		}
		/**
		 * 如果前台传来组织id就查询组织下面的所有团体
		 */
		if(null!=orgId&&!"".equals(orgId)){
			if(values.size()>0){
				hqlList.append(" and ");
				hqlCount.append(" and ");
			}else{
				hqlList.append(" where ");
				hqlCount.append(" where ");
			}
			hqlList.append("groups.YBasicOrganization.fid=?");
			hqlCount.append("groups.YBasicOrganization.fid=?");
			values.add(orgId);
		}
		/**
		 * 前台只通过group名称的模糊查询
		 */
		if(null!=groupName&&!"".equals(groupName)){
			if(values.size()>0){
				hqlList.append(" and ");
				hqlCount.append(" and ");
			}else{
				hqlList.append(" where ");
				hqlCount.append(" where ");
			}
			hqlList.append("groups.fname like ?");
			hqlCount.append("groups.fname like ?");
			values.add("%"+groupName+"%");
		}
			/**
			 * 获得此页数据
			 */
			pagingList.setList((List<YBasicSocialgroups>) cOC_HibernateDAO.find(hqlList.toString(), page, pageSize, values.toArray()));
			/**
			 * 获得总条数
			 */
			pagingList.setCount(cOC_HibernateDAO.getTotalCountByCondition(hqlCount.toString(), page, pageSize, values.toArray()));
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "修改团体")
	public boolean update(YBasicSocialgroups group) {
		boolean flag = true;
		YBasicSocialgroups group1 = (YBasicSocialgroups) cOC_HibernateDAO.get(YBasicSocialgroups.class, group.getFid());
		if(null != group1){
			group1.setFbillState(group.getFbillState());
			group1.setFabbreviation(group.getFabbreviation());
			group1.setFaddress(group.getFaddress());
			group1.setFclientContacts(group.getFclientContacts());
			group1.setFcomment(group.getFcomment());
			group1.setFcreaterId(group.getFcreaterId());
			group1.setFcreateTime(group.getFcreateTime());
			group1.setFemail(group.getFemail());
			group1.setFiphone(group.getFiphone());
			group1.setFlastModifiedId(group.getFlastModifiedId());
			group1.setFlastModifiedTime(group.getFlastModifiedTime());
			group1.setFname(group.getFname());
			group1.setFnumber(group.getFnumber());
			group1.setFnumberPeople(group.getFnumberPeople());
			group1.setFphone(group.getFphone());
			group1.setFregisterNum(group.getFregisterNum());
			group1.setFpreviousNumber(group.getFpreviousNumber());
			group1.setFsignedTime(group.getFsignedTime());
			group1.setFsource(group.getFsource());
			group1.setFsuperSocialGroupsId(group.getFsuperSocialGroupsId());
			group1.setFynsigned(group.getFynsigned());//是否签约
			group1.setFleafNode(group.getFleafNode());
			group1.setFlevel(group.getFlevel());
			group1.setFmodifiedId(group.getFmodifiedId());
			group1.setFmodifiedTime(group.getFmodifiedTime());
			group1.setAllowadd(group.getAllowadd());
			group1.setFlag(group.getFlag());
			group1.setLogo(group.getLogo());
			/**
			 * 关系对象
			 */
			group1.setYBasicDistrict(group.getYBasicDistrict());
			group1.setYBasicCity(group.getYBasicCity());//所属城市
			group1.setYBasicCounty(group.getYBasicCounty());//所属国家
			group1.setYBasicType(group.getYBasicType());//所属类型
			group1.setYBasicProvince(group.getYBasicProvince());//所属省会
			group1.setYBasicOrganization(group.getYBasicOrganization());//所属组织
			group1.setYBasicEmployee(group.getYBasicEmployee());//所属的销售人员
			/**
			 * 下面还要更新表中外键关系
			 */
			cOC_HibernateDAO.update(group1);//更新到持久层
			
			flag = true;
		}else{
			logger.info("数据不存在!");
			flag =false;
		}
		return flag;
	}

	@Override
	@SystemServiceLog(description = "根据id查询团体")
	public boolean findGroup(YBasicSocialgroups group) {
		boolean flag = true;
		try{
			cOC_HibernateDAO.findById(YBasicSocialgroups.class, group.getFid());
		}catch(Exception e){
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询团体")
	public List<YBasicSocialgroups> getByHql(String hql) {
		return (List<YBasicSocialgroups>)cOC_HibernateDAO.findAllByHQL(hql);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询团体")
	public List<GroupTreeVo> getByHql1(String hql) {
		return (List<GroupTreeVo>)cOC_HibernateDAO.findAllByHQL(hql);
	}
/**
 * 根据用户id 获取groups
 */
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "根据用户id 获取团体")
	public Map<String, Object> getGroupByUserId(String telphoe) {
		Map<String,Object> resultMap = new HashMap<String,Object> ();
		Map<String,String> resultObject = null;
		List<Map<String,String>> resultList = new ArrayList<Map<String,String>> ();
		String sql = "from YBasicMember as ymember where ymember.fmobilePhone='"+telphoe+"' and ymember.YBasicSocialgroups.fbillState="+Status.EFFECT.value()+" and ymember.fbillState ="+Status.EFFECT.value();
		
		List<YBasicMember> members  =(List<YBasicMember>)cOC_HibernateDAO.findAllByHQL(sql);
		if(members!=null){
			for(YBasicMember member:members){
				if(member.getYBasicSocialgroups()!=null&&member.getYBasicSocialgroups().getFbillState()!=null&&member.getYBasicSocialgroups().getFbillState()==Status.EFFECT.value()){
					resultObject = new HashMap<String,String>();
					resultObject.put("businessId",member.getYBasicSocialgroups().getFid());
					String url = (member.getYBasicSocialgroups().getLogo()==null?"":member.getYBasicSocialgroups().getLogo().replace(",", ""));
					resultObject.put("businessLogo",url);
					resultObject.put("businessName",member.getYBasicSocialgroups().getFname());
					resultList.add(resultObject);
				}else{
					//do nothing
				}
			}
		}
		resultMap.put("localCacheVersion", "");//从用户商会版本表中获取
		resultMap.put("businessList", resultList);
		return resultMap;
	}
	/**
	 *获取全部的商会 获取groups
	 */
		@Override
		@SystemServiceLog(description = "获取全部的商会 获取团体")
		public List<Map<String,Object>> getAllGroup() {
			String sql = "from YBasicProvince";
			@SuppressWarnings("unchecked")
			List<YBasicProvince> provinces  =(ArrayList<YBasicProvince>) cOC_HibernateDAO.findAllByHQL(sql);
			Set<YBasicCity> citys = null;//每个省份的城市集合
			Set<YBasicSocialgroups> groups = null;//每个省份的城市集合
			Map<String,String> groupInfoObject = null;//
			List<Map<String,String>> groupInfoList = new ArrayList<Map<String,String>>();//businessList(json数组，商会列表)
			Map<String,Object> cityInfoObject= null;//每一城市：包含的名字+团体信息集合
			List<Map<String,Object>> cityInfoList = new ArrayList<Map<String,Object>>();//cityInfoList(数组，城市信息)里面包含了多个cityInfoMap对象
			Map<String,Object> resultObject = null;//每一省份
			List<Map<String,Object>> resulInfoList = new ArrayList<Map<String,Object>>();
			
			for(YBasicProvince yBasicProvince : provinces){
				resultObject = new HashMap<String,Object>();
				resultObject.put("provincesName", yBasicProvince.getFname());
				resultObject.put("type", "省份类别");
				
				citys = yBasicProvince.getYBasicCities();
				
				for(YBasicCity city:citys){
					cityInfoObject = new  HashMap<String,Object>();
					cityInfoObject.put("cityName", city.getFname());//加入每个cityinfo的name
					groups = city.getYBasicSocialgroupses();
					for(YBasicSocialgroups basicSocialgroups :groups){
						if(basicSocialgroups.getFbillState()==Status.EFFECT.value()){//判断商会是否失效
							groupInfoObject = new HashMap<String,String>();
							groupInfoObject.put("businessName",basicSocialgroups.getFname());
							groupInfoObject.put("businessId",basicSocialgroups.getFid());
							groupInfoList.add(groupInfoObject);
						}else{
							//失效的商会不返回
						}
					}
					cityInfoObject.put("businessList", groupInfoList);//加入每个cityinfo的团体信息集合
					cityInfoList.add(cityInfoObject);//	城市信息集合(包含城市信息和所有团体信息)
				}
				resultObject.put("cityInfo", cityInfoList);
			}
			resulInfoList.add(resultObject);
			return resulInfoList;
		}
	@SuppressWarnings("unchecked")
	@Override
	public YBasicSocialgroups getById(YBasicSocialgroups group,String fid) {
		try {
			List<YBasicSocialgroups> groups=new ArrayList<YBasicSocialgroups>();
			groups=(List<YBasicSocialgroups>) cOC_HibernateDAO.findAllByHQL("from YBasicSocialgroups as y where y.fid='"+fid+"'");
			group=groups.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return group;
	}
	
	@Override
	@SystemServiceLog(description = "获取名称和logo")
	public Map<String,Object> getBygroupId(String fid){
		Map<String,Object> map= new HashMap<String, Object>();
		try {
			YBasicSocialgroups group=new YBasicSocialgroups();
			group=(YBasicSocialgroups)cOC_HibernateDAO.findById(YBasicSocialgroups.class, fid);
			map.put("name", group.getFname());
			map.put("logo", group.getLogo());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}	
		
	@Override
	@SystemServiceLog(description = "获取名称和logo")
	public Map<String,Object> getBygroupId1(String fid) throws Exception{
		Map<String,Object> map= new HashMap<String, Object>();
			YBasicSocialgroups group=new YBasicSocialgroups();
			group=(YBasicSocialgroups)cOC_HibernateDAO.findById(YBasicSocialgroups.class, fid);
			map.put("businessId", group.getFid());
			map.put("businessName", group.getFname());
			map.put("businessLogo", group.getLogo());
		return map;
	}	
		/**
		 * 创建树干
		 * @Title: createTree 
		 * @Description:  
		 * @param     
		 * @return void   
		 * @throws
		 */
		@Override
		public void createTree(List<Tree1> treelist,String orgId,String groupName,String type,String userId,HashSet<String> groupSet){
			//通过组织或者groupName来查询所有的企且拼装成树结构
			List<YBasicSocialgroups> groupList = new ArrayList<YBasicSocialgroups>();
			groupList=(List<YBasicSocialgroups>) this.getAllGroupsPagingList(null, null, null, orgId, groupName).getList();
			List<YBasicSocialgroups> groupsList = new ArrayList<YBasicSocialgroups>();
			
			if(userId.equals("1")){
				groupsList=groupList;
			}else{
				for(YBasicSocialgroups o:groupList){
					for(String groupId:groupSet){
						if(o.getFid().equals(groupId)){
							groupsList.add(o);
						}
					}
				}
			}
			if(type!=null&&type.equals("search")){//如果是查询，显示的就没有层级关系
				for(YBasicSocialgroups o:groupsList){
						Tree1 tree = new Tree1();
						String statu = o.getFbillState()==Status.UNEFFECT.value()?"(<span style='color:red'>封存</span>)":o.getFbillState()==Status.RELATION.value()?"(<span style='color:blue'>待审核</span>)":"";
						tree.setId(o.getFid());
						tree.setText(o.getFname()+ statu);
						tree.setLeaf(true);
						treelist.add(tree);
				}
			}else{
				for(YBasicSocialgroups o:groupsList){
					
					try {
						
					
					String statu = o.getFbillState()==Status.UNEFFECT.value()?"(<span style='color:red'>封存</span>)":o.getFbillState()==Status.RELATION.value()?"(<span style='color:blue'>待审核</span>)":"";
					if(o.getFsuperSocialGroupsId()==null||o.getFsuperSocialGroupsId().equals("")){
						Tree1 tree = new Tree1();
						tree.setId(o.getFid());
						tree.setText(o.getFname()+ statu);
						createTreechild(o.getFid(),tree,userId,groupSet);
						treelist.add(tree);
					}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		
		}
		/**
		 * 
		 * @Title: createTreechild 
		 * @Description: 
		 * @param @param id
		 * @param @param tree    
		 * @return void   
		 * @throws
		 */
		private  void createTreechild(String id,Tree1 tree,String userId,HashSet<String> groupSet){
			List<YBasicSocialgroups> list = this.getByHql("from YBasicSocialgroups as g where g.fsuperSocialGroupsId='"+id+"'");
			List<YBasicSocialgroups> lists = new ArrayList<YBasicSocialgroups>();
			if(userId.equals("1")){
				lists=list;
			}else{
				for(YBasicSocialgroups o:list){
					for(String groupId:groupSet){
						if(o.getFid().equals(groupId)){
							lists.add(o);
						}
					}
				}
			}
			if(lists!=null&&lists.size()>0){
				for(YBasicSocialgroups me : lists){
					Tree1 child = new Tree1();
					String statu = me.getFbillState()==Status.UNEFFECT.value()?"(<span style='color:red'>封存</span>)":me.getFbillState()==Status.RELATION.value()?"(<span style='color:blue'>待审核</span>)":"";
					
					child.setId(me.getFid());
					child.setText(me.getFname()+ statu);
					createTreechild(me.getFid(),child,userId,groupSet);
					tree.getChildren().add(child);
				}
			}else{
//				List<YBasicSocialgroups> list1 = this.getByHql("from YBasicSocialgroups as g where g.fid='"+id+"'");
//				tree.setText(list1.get(0).getFname()+ "("+(list1.get(0).getFbillState()==Status.EFFECT.value()?"<span style='color:green'>生效</span>":"<span style='color:red'>失效</span>")+")");
//				tree.setId(list1.get(0).getFid());
				tree.setLeaf(true);
			}
		}
	
		//重写上面的方法，查询更少的东西来提升速度
		
		/**
		 * 创建树干
		 * @Title: createTree 
		 * @Description:  
		 * @param     
		 * @return void   
		 * @throws
		 */
		@Override
		public void createTree1(List<Tree1> treelist,String orgId,String groupName,String type,String userId,HashSet<String> groupSet,String typeId){
			//通过组织或者groupName来查询所有的企且拼装成树结构
			List<GroupTreeVo> groupList = this.getAllGroupsPagingListByTree(null, orgId, groupName,typeId);
			List<GroupTreeVo> groupsList = new ArrayList<GroupTreeVo>();
			if(userId.equals("1")){
				groupsList=groupList;
			}else{
				for(GroupTreeVo o:groupList){
					for(String groupId:groupSet){
						if(o.getFid().equals(groupId)){
							groupsList.add(o);
						}
					}
				}
			}
			if(type!=null&&type.equals("search")){//如果是查询，显示的就没有层级关系
				for(Object object:groupsList){
						GroupTreeVo  o = (GroupTreeVo)object;
						Tree1 tree = new Tree1();
						String statu = o.getFbillState()==Status.UNEFFECT.value()?"(<span style='color:red'>封存</span>)":o.getFbillState()==Status.RELATION.value()?"(<span style='color:blue'>待审核</span>)":"";
						tree.setId(o.getFid());
						tree.setText(o.getFname()+ statu);
						tree.setLeaf(true);
						treelist.add(tree);
				}
			}else{
				try {
					
				
				for(GroupTreeVo o:groupsList){
					try {
					String statu = o.getFbillState()==Status.UNEFFECT.value()?"(<span style='color:red'>封存</span>)":o.getFbillState()==Status.RELATION.value()?"(<span style='color:blue'>待审核</span>)":"";
					if(o.getFsuperSocialGroupsId()==null||o.getFsuperSocialGroupsId().equals("")){
						Tree1 tree = new Tree1();
						tree.setId(o.getFid());
						tree.setText(o.getFname()+ statu);
						createTreechild1(o.getFid(),tree,userId,groupSet,typeId);
						treelist.add(tree);
					}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		}
		/**
		 * 
		 * @Title: createTreechild 
		 * @Description: 
		 * @param @param id
		 * @param @param tree    
		 * @return void   
		 * @throws
		 */
		private  void createTreechild1(String id,Tree1 tree,String userId,HashSet<String> groupSet,String typeId){
			
			/**
			 * beck
			 * 只有团体信息页面才显示出所有团体信息，其他页面只显示已生效的团体
			 */
			String hql = "";
			if(typeId == null){
				hql = "select fid,fname,fbillState,fsuperSocialGroupsId from YBasicSocialgroups as g where g.fsuperSocialGroupsId='"+id+"'";
			}else{
				hql = "select fid,fname,fbillState,fsuperSocialGroupsId from YBasicSocialgroups as g where g.fbillState = 5 and g.fsuperSocialGroupsId='"+id+"'";
			}
			List<GroupTreeVo> list = this.getByHql1(hql);
			
			//List<GroupTreeVo> list = this.getByHql1("select fid,fname,fbillState,fsuperSocialGroupsId from YBasicSocialgroups as g where g.fsuperSocialGroupsId='"+id+"'");
			List<GroupTreeVo> lists = new ArrayList<GroupTreeVo>();
			if(userId.equals("1")){
				lists=list;
			}else{
				for(GroupTreeVo o:lists){
					for(String groupId:groupSet){
						if(o.getFid().equals(groupId)){
							lists.add(o);
						}
					}
				}
			}
			if(lists!=null&&lists.size()>0){
				for(GroupTreeVo me : lists){
					Tree1 child = new Tree1();
					String statu = me.getFbillState()==Status.UNEFFECT.value()?"(<span style='color:red'>封存</span>)":me.getFbillState()==Status.RELATION.value()?"(<span style='color:blue'>待审核</span>)":"";
					
					child.setId(me.getFid());
					child.setText(me.getFname()+ statu);
					createTreechild1(me.getFid(),child,userId,groupSet,typeId);
					tree.getChildren().add(child);
				}
			}else{
//				List<YBasicSocialgroups> list1 = this.getByHql("from YBasicSocialgroups as g where g.fid='"+id+"'");
//				tree.setText(list1.get(0).getFname()+ "("+(list1.get(0).getFbillState()==Status.EFFECT.value()?"<span style='color:green'>生效</span>":"<span style='color:red'>失效</span>")+")");
//				tree.setId(list1.get(0).getFid());
				tree.setLeaf(true);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		@SystemServiceLog(description = "修改商会生效、失效")
		public Map<String,Object> updateStatus(YBasicSocialgroups group, String status) {
			boolean flag = true;
			Map<String,Object> map = new HashMap<String,Object>();
			YBasicSocialgroups group1 = (YBasicSocialgroups) cOC_HibernateDAO.findById(YBasicSocialgroups.class, group.getFid());
			group1.setFbillState(new Integer(status));
			if(group1.getFiphone()==null||group1.getFiphone().equals("")){
				flag = false;
				map.put("msg", "电话号码为空....");
				map.put("success", false);
				return map;
			}
			//判断是否为生效操作
			if(flag ==true){
				  	Pattern p = null;  
			        Matcher m = null;  
			        boolean b = false;   
			        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号  
			        m = p.matcher(group1.getFiphone());  
			        b = m.matches(); 
			        if(b==false){
			        	flag = false;
						map.put("msg", "电话号码不规范,请先修改....");
						map.put("success", false);
						return map;
			        }
			}
			cOC_HibernateDAO.update(group1);
			if(status.equals("5")){
				//判断是否已经创建了管理员
				List<YSystemUsers> users = (List<YSystemUsers>) cOC_HibernateDAO.find("from YSystemUsers y where y.faccount = ?", group1.getFiphone());

				if(users.isEmpty()){
					//创建管理员账号
					YSystemUsers user = new YSystemUsers();
					user.setFid(UUID.randomUUID().toString());
					user.setFbillState(5);
					user.setFtypeId("0");
					user.setFaccount(group1.getFiphone());
					user.setFuserPassword(MD5Util.md5(BEGINSTRING + "888888"));
					cOC_HibernateDAO.save(user);
					
					//创建会员
					YBasicMember member = new YBasicMember();
					member.setFid(UUID.randomUUID().toString());
					member.setFbillState(5);
					member.setYBasicSocialgroups(group1);
					member.setIsAdmin(1);
					member.setYSystemUsers(user);
					member.setFmobilePhone(EncryptionForTellPhone.encryptToABC(group1.getFiphone()));
					member.setFpassword(MD5Util.md5("888888"));
					member.setFcreateTime(new Date(System.currentTimeMillis()));
					cOC_HibernateDAO.save(member);
					
					//获取默认的十个标签
					List<YBasicLabel> lists = LabelXMLToObject.labelObject(EncryptionForTellPhone.encryptToABC(group1.getFiphone()));
					cOC_HibernateDAO.saveOrUpdateAll(lists);
					
					/**
					 * update by ailierke
					 */
					//如果这个电话号码所存在的会员部存在，就生成IM账号，如果存在就跳过
					try {
						
					
					List<YBasicMember> memberList = (List<YBasicMember>) cOC_HibernateDAO.findAllByHQL("from YBasicMember m where m.fmobilePhone = '"+member.getFmobilePhone()+"'");
					//保存生成的IM账号
					YBasicImaccount im = new YBasicImaccount();
					if(memberList!=null&&memberList.size()>1){
						im = (YBasicImaccount) cOC_HibernateDAO.findAllByHQL("from YBasicImaccount imaccount where imaccount.fimtel ='"+member.getFmobilePhone()+"'").get(0);
					}else{
						Map<String,String> userMap = new HashMap<String,String>();
						userMap.put("username", CRC32Util.getCRC32(group1.getFiphone()));
						userMap.put("password", MD5Util.md5("888888"));//默认密码888888
						IMUtils.createUser(userMap);//新增
						im.setFimkey(CRC32Util.getCRC32(group1.getFiphone()));
						im.setFimpassword(MD5Util.md5("888888"));
						im.setFimtel(member.getFmobilePhone());
						cOC_HibernateDAO.save(im);
					}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
			map.put("msg", "状态更新成功");
			map.put("success", true);
			return map;
		}

		@SuppressWarnings("unchecked")
		@Override
		@SystemServiceLog(description = "根据团体名查询id")
		public String findByName(String Name) {
			List<YBasicSocialgroups> groups = (List<YBasicSocialgroups>) cOC_HibernateDAO.findAllByHQL("from YBasicSocialgroups as y where y.fname='"+Name+"'");
			YBasicSocialgroups group=groups.get(0);
			String fid=group.getFid();
			return fid;
		}

		@SuppressWarnings("unchecked")
		@Override
		@SystemServiceLog(description = "查询团体")
		public List<Map<String, Object>> getRegionBusinessList(String infoMap) throws JsonProcessingException, IOException {
			Map<String,Object> resultObject = null;//每一group
			List<Map<String,Object>> resulInfoList = new ArrayList<Map<String,Object>>();
			JsonNode  node = objectMapper.readTree(infoMap);
			String provincialId = node.get("provincialId")==null?null:node.get("provincialId").textValue();//省份Id
			String cityId = node.get("cityId")==null?null:node.get("cityId").textValue();//城市id
			String countryId = node.get("countryId")==null?null:node.get("countryId").textValue();//县id
			int pageSize = node.get("pageSize")==null?null:node.get("pageSize").intValue();//分页大小
			String curBusinessId = node.get("curBusinessId")==null?null:node.get("curBusinessId").textValue();//当前商会id
			StringBuffer hql = new StringBuffer("from YBasicSocialgroups group where 1=1 and group.fname not like '%狮子%'");
			if(provincialId!=null&&!provincialId.equals("")){
				hql.append(" and group.YBasicProvince.fid ='"+provincialId+"' ");
			}
			if(cityId!=null&&!cityId.equals("")){
				hql.append(" and group.YBasicCity.fid ='"+cityId+"' ");
			}
			if(countryId!=null&&!countryId.equals("")){
				hql.append(" and group.YBasicCounty.fid ='"+countryId+"' ");
			}
			if(curBusinessId!=null&&!curBusinessId.equals("")){
				YBasicSocialgroups yBasicSocialgroups = (YBasicSocialgroups) cOC_HibernateDAO.findById(YBasicSocialgroups.class,curBusinessId);
				if(yBasicSocialgroups!=null){
					hql.append(" and flag < "+yBasicSocialgroups.getFlag()+" and fbillState =5 order by group.flag desc ");
				}	
			}else{
				hql.append(" and fbillState =5 order by group.flag desc");
			}
			List<YBasicSocialgroups> groupList = (List<YBasicSocialgroups>) cOC_HibernateDAO.find(hql.toString(), 0, pageSize, null);
			if(groupList!=null&&groupList.size()>0){
				String logo = null;
					for(YBasicSocialgroups basicSocialgroups:groupList){
							resultObject = new HashMap<String,Object>();
							resultObject.put("businessId", basicSocialgroups.getFid());
							logo = basicSocialgroups.getLogo();
							if(logo!=null&&!logo.equals("")){
								logo = logo.replace(",", "");
								resultObject.put("businessLogo", logo);
							}
							resultObject.put("businessName", basicSocialgroups.getFname());
							resulInfoList.add(resultObject);
					}
			}else{
				//没有数据不做任何操作
			}
			return resulInfoList;
		}

		@Override
		@SystemServiceLog(description = "查询是否有子节点，没有返回true 有子节点， 全是失效 返回true 否则选择false")
		public boolean checkChildrenNode(String groupId) {
			boolean flag = false;
			if(groupId!=null){
				String hql ="select count(0) from YBasicSocialgroups groups where groups.fsuperSocialGroupsId='"+groupId+"'";
				Integer count = cOC_HibernateDAO.getTotalCountByCondition(hql, null, null, null);
				if(count>0){
					hql ="select count(0) from YBasicSocialgroups groups where groups.fsuperSocialGroupsId='"+groupId+"' and fbillState ="+Status.UNEFFECT;
					Integer count1 = cOC_HibernateDAO.getTotalCountByCondition(hql, null, null, null);
					if(count1==count){//说明全是失效的
						flag = true;
					}else{
						flag = false;//说明有除了失效的其他状态
					}
				}else{
					flag = true;//不存在子集返回true
				}
			}else{
				throw new RuntimeException("groupId不存在，系统异常");
			}
			return flag;
		}

		@Override
		public YBasicSocialgroups getById(String groupId) {
			YBasicSocialgroups group=new YBasicSocialgroups();
			group=(YBasicSocialgroups)cOC_HibernateDAO.findById(YBasicSocialgroups.class, groupId);
			return group;
		}

		/**
		 * 主要用来查询团体树
		 */
		@SuppressWarnings("unchecked")
		@Override
		@SystemServiceLog(description = "查询全部，或根据条件查询团体，并分页")
		public List<GroupTreeVo> getAllGroupsPagingListByTree(String groupId,String orgId,String groupName,String typeId) {
			logger.info("查询团体信息列表....");
			List<Object> values = new ArrayList<Object>();
			List<GroupTreeVo> list = new ArrayList<GroupTreeVo>();
		
			StringBuffer sql= new StringBuffer("select fid,fname,fbillState,fsuperSocialGroupsId from y_basic_socialgroups groups "); 
			
			/**
			 * 通过groupId
			 */
			if(null!=groupId&&!"".equals(groupId)){
				sql.append(" where groups.fid='"+groupId+"'");
				values.add(groupId);
			}
			/**
			 * 如果前台传来组织id就查询组织下面的所有团体
			 */
			if(null!=orgId&&!"".equals(orgId)){
				if(values.size()>0){
					sql.append(" and ");
				}else{
					sql.append(" where ");
				}
				sql.append("groups.FCompaniesID='"+orgId+"'");
				values.add(orgId);
			}
			
			/**
			 * beck
			 * 只有团体信息页面才显示出所有团体信息，其他页面只显示已生效的团体
			 */
			if(typeId != null){
				if(values.size()>0){
					sql.append(" and ");
				}else{
					sql.append(" where ");
				}
				sql.append("fbillState = 5 ");
			}
			
			
			/**
			 * 前台只通过group名称的模糊查询
			 */
			if(null!=groupName&&!"".equals(groupName)){
				if(values.size()>0){
					sql.append(" and ");
				}else{
					sql.append(" where ");
				}
				sql.append("groups.fname like '%"+groupName+"%'");
				values.add("%"+groupName+"%");
			}
				/**
				 * 获得此页数据
				 */
			try {
				list = (List<GroupTreeVo>) cOC_HibernateDAO.getListBySqlVO(sql.toString(),GroupTreeVo.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}
		@SuppressWarnings("unchecked")
		@Override
		public void importOrUpdateGroups(ImportGroupsVo importGroupsVO, int rowIndex,HttpServletRequest request,File lockFile){
			checkImportVo(importGroupsVO, rowIndex, request,lockFile);
			//处理特殊的业务
			String hql = "";
			List<YSystemUsers> ySystemUsersList = null;
			Set<YBasicMember> ybasicMemberSet =null;
			YBasicSocialgroups  yBasicSocialgroup=  null;
			Integer size = 0;
			//如果管理员账号存在就说明是更新，不然就是新增
			if(!importGroupsVO.getLoginName().equals("")){
				//校验管理员账号是否存在
				hql="from YSystemUsers ySystemUsers where ySystemUsers.faccount='"+importGroupsVO.getLoginName()+"'";
				ySystemUsersList = (List<YSystemUsers>)cOC_HibernateDAO.findAllByHQL(hql);
				if(ySystemUsersList!=null){
					size=ySystemUsersList.size();
				}else{
					size=0;
				}
				checkParams(size, "管理员账号", 1, rowIndex, request,lockFile);
				//如果存在,查看是属于对应的商会、组织和填的是否一致(查看关联的会员即可知道)
				ybasicMemberSet = ySystemUsersList.get(0).getYBasicMembers();
			    List<YBasicMember> yBasicMemberList= new ArrayList<YBasicMember>(ybasicMemberSet);
			    if(yBasicMemberList!=null){
					size=yBasicMemberList.size();
				}
			    checkParams(size, "管理员账号未关联会员,填入账号有误", 2, rowIndex, request,lockFile);
			    size=0;
			    yBasicSocialgroup= yBasicMemberList.get(0).getYBasicSocialgroups();
			    if(yBasicSocialgroup==null){
			    	String messagetxt = "<--失败--> 第 "+rowIndex+" 行,管理员账号 所对应的商会不存在";
					writeStatus(request,messagetxt,lockFile,true);
					throw new RuntimeException(messagetxt);
			    }
			}
				//校验销售人员名称是否在职员中是否存在
				hql="from YBasicEmployee employee where employee.fname='"+importGroupsVO.getPeopleName()+"'";
				List<YBasicEmployee> eployeeList = (List<YBasicEmployee>)cOC_HibernateDAO.findAllByHQL(hql);
				if(eployeeList!=null){
					size=eployeeList.size();
				}else{
					size=0;
				}
				checkParams(size, "销售人员", 0, rowIndex, request,lockFile);
				//校验商会类型是否存在
				hql="from YBasicType ybasictype where ybasictype.fname='"+importGroupsVO.getType()+"' and ybasictype.fmoduleId='1' ";
				List<YBasicType> yBasicList = (List<YBasicType>)cOC_HibernateDAO.findAllByHQL(hql);
				if(yBasicList!=null){
					size=yBasicList.size();
				}else{
					size=0;
				}
				checkParams(size, "商会类型", 0, rowIndex, request,lockFile);
				//校验地区类型是否存在
				hql="from YBasicDistrict yBasicDistrict where yBasicDistrict.fname like '%"+importGroupsVO.getDistct()+"%'";
				List<YBasicDistrict> yBasicDistrictList = (List<YBasicDistrict>)cOC_HibernateDAO.findAllByHQL(hql);
				if(yBasicDistrictList!=null){
					size=yBasicDistrictList.size();
				}else{
					size=0;
				}
				checkParams(size, "地区", 1, rowIndex, request,lockFile);
				//校验省份类型是否存在
				hql="from YBasicProvince yBasicProvince where yBasicProvince.fname like '%"+importGroupsVO.getProvice()+"%'";
				List<YBasicProvince> yBasicProvinceList = (List<YBasicProvince>)cOC_HibernateDAO.findAllByHQL(hql);
				if(yBasicProvinceList!=null){
					size=yBasicProvinceList.size();
				}else{
					size=0;
				}
				checkParams(size, "省份", 1, rowIndex, request,lockFile);
				//校验城市类型是否存在
				hql="from YBasicCity yBasicCity where yBasicCity.fname like '%"+importGroupsVO.getCity()+"%'";
				List<YBasicCity> yBasicCityList = (List<YBasicCity>)cOC_HibernateDAO.findAllByHQL(hql);
				if(yBasicCityList!=null){
					size=yBasicCityList.size();
				}else{
					size=0;
				}
				checkParams(size, "城市", 1, rowIndex, request,lockFile);
				//校验区域类型是否存在
				hql="from YBasicCounty yBasicCounty where yBasicCounty.fname like '%"+importGroupsVO.getContry()+"%'";
				List<YBasicCounty> yBasicCountyList = (List<YBasicCounty>)cOC_HibernateDAO.findAllByHQL(hql);
				if(yBasicCountyList!=null){
					size=yBasicCountyList.size();
				}else{
					size=0;
				}
				checkParams(size, "区域", 1, rowIndex, request,lockFile);
				//校验组织类型是否存在
				hql="from YBasicOrganization yBasicOrganization where yBasicOrganization.fname like '%"+importGroupsVO.getOganazation()+"%'";
				List<YBasicOrganization> yBasicOrganizationList = (List<YBasicOrganization>)cOC_HibernateDAO.findAllByHQL(hql);
				if(yBasicOrganizationList!=null){
					size=yBasicOrganizationList.size();
				}else{
					size=0;
				}
				checkParams(size, "云筑组织", 0, rowIndex, request,lockFile);
				//如果不存在就新增 . 存在就是跟新
				if(importGroupsVO.getLoginName()!=""){
					yBasicSocialgroup =  new YBasicSocialgroups();
					yBasicSocialgroup.setAllowadd("1");
					yBasicSocialgroup.setFbillState(5);
					try {
						yBasicSocialgroup.setFcreateTime(new SimpleDateFormat("yyyy/MM/dd").parse(importGroupsVO.getSignTime()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					yBasicSocialgroup.setFnumberPeople(new Integer(1000));
				}
				yBasicSocialgroup.setFaddress(importGroupsVO.getAddress());
				yBasicSocialgroup.setFclientContacts(importGroupsVO.getContactName());
				try {
					yBasicSocialgroup.setFsignedTime(new SimpleDateFormat("yyyy/MM/dd").parse(importGroupsVO.getSignTime()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				yBasicSocialgroup.setFemail(importGroupsVO.getMail());
				yBasicSocialgroup.setFiphone(importGroupsVO.getTelphone());
				yBasicSocialgroup.setFlastModifiedTime(new java.sql.Date(new java.util.Date().getTime()));
				yBasicSocialgroup.setFlevel(importGroupsVO.getLevel());
				yBasicSocialgroup.setFmodifiedId(((YSystemUsers)request.getSession().getAttribute("user")).getFid());
				yBasicSocialgroup.setFmodifiedTime(new java.sql.Date(new java.util.Date().getTime()));
				yBasicSocialgroup.setFname(importGroupsVO.getGroupsName());
				yBasicSocialgroup.setFphone(importGroupsVO.getPhone());
				yBasicSocialgroup.setFregisterNum(new Integer(importGroupsVO.getPeopleNum()));
				yBasicSocialgroup.setFsource("系统管理员添加");
				yBasicSocialgroup.setYBasicType(yBasicList.get(0));
				yBasicSocialgroup.setYBasicProvince(yBasicProvinceList.get(0));
				yBasicSocialgroup.setYBasicOrganization(yBasicOrganizationList.get(0));
				yBasicSocialgroup.setYBasicEmployee(eployeeList.get(0));
				yBasicSocialgroup.setYBasicDistrict(yBasicDistrictList.get(0));
				yBasicSocialgroup.setYBasicCounty(yBasicCountyList.get(0));
				yBasicSocialgroup.setYBasicCity(yBasicCityList.get(0));
				cOC_HibernateDAO.saveOrUpdate(yBasicSocialgroup);
				cOC_HibernateDAO.flush();
		}
		
		private void checkImportVo(ImportGroupsVo importGroupsVO,int rowIndex,HttpServletRequest request,File lockFile){
			if(importGroupsVO.getAddress().equals("")){
				String messagetxt = "<--失败--> 第 "+rowIndex+" 行,地址没有填写";
				writeStatus(request,messagetxt,lockFile,true);
				throw new RuntimeException(messagetxt);
			}
			if(importGroupsVO.getCity().equals("")){
				String messagetxt = "<--失败--> 第 "+rowIndex+" 行,城市没有填写";
				writeStatus(request,messagetxt,lockFile,true);
				throw new RuntimeException(messagetxt);
			}
			if(importGroupsVO.getContactName().equals("")){
				String messagetxt = "<--失败-->第 "+rowIndex+" 行,联系人没有填写";
				writeStatus(request,messagetxt,lockFile,true);
				throw new RuntimeException(messagetxt);
			}
			if(importGroupsVO.getContry().equals("")){
				String messagetxt = "<--失败-->第 "+rowIndex+" 行,区县没有填写";
				writeStatus(request,messagetxt,lockFile,true);
				throw new RuntimeException(messagetxt);
			}
			if(importGroupsVO.getDistct().equals("")){
				String messagetxt = "<--失败-->第 "+rowIndex+" 行,地区没有填写";
				writeStatus(request,messagetxt,lockFile,true);
				throw new RuntimeException(messagetxt);
			}
			if(importGroupsVO.getGroupsName().equals("")){
				String messagetxt = "<--失败-->第 "+rowIndex+" 行,商会名称没有填写";
				writeStatus(request,messagetxt,lockFile,true);
				throw new RuntimeException(messagetxt);
			}
			if(importGroupsVO.getInfoComeFrom().equals("")){
				String messagetxt = "<--失败--> 第 "+rowIndex+" 行,信息来源没有填写";
				writeStatus(request,messagetxt,lockFile,true);
				throw new RuntimeException(messagetxt);
			}
			if(importGroupsVO.getIsSign().equals("")){
				String messagetxt = "<--失败--> 第 "+rowIndex+" 行,是否签约没有填写";
				writeStatus(request,messagetxt,lockFile,true);
				throw new RuntimeException(messagetxt);
			}
			if(importGroupsVO.getLevel().equals("")){
				String messagetxt = "<--失败--> 第 "+rowIndex+" 行,等级没有填写";
				writeStatus(request,messagetxt,lockFile,true);
				throw new RuntimeException(messagetxt);
			}
			if(importGroupsVO.getMail().equals("")){
				String messagetxt = "<--失败--> 第 "+rowIndex+" 行,邮箱地址没有填写";
				writeStatus(request,messagetxt,lockFile,true);
				throw new RuntimeException(messagetxt);
			}
			if(importGroupsVO.getOganazation().equals("")){
				String messagetxt = "<--失败--> 第 "+rowIndex+" 行,所属组织没有填写";
				writeStatus(request,messagetxt,lockFile,true);
				throw new RuntimeException(messagetxt);
			}
			if(importGroupsVO.getPeopleName().equals("")){
				String messagetxt = "<--失败--> 第 "+rowIndex+" 行,联系人没有填写";
				writeStatus(request,messagetxt,lockFile,true);
				throw new RuntimeException(messagetxt);
			}
			if(importGroupsVO.getPhone().equals("")){
				String messagetxt = "<--失败--> 第 "+rowIndex+" 行,商会电话没有填写";
				writeStatus(request,messagetxt,lockFile,true);
				throw new RuntimeException(messagetxt);
			}
			if(importGroupsVO.getTelphone().equals("")){
				String messagetxt = "<--失败--> 第 "+rowIndex+" 行,联系人手机号码没有填写";
				writeStatus(request,messagetxt,lockFile,true);
				throw new RuntimeException(messagetxt);
			}
			if(importGroupsVO.getType().equals("")){
				String messagetxt = "<--失败--> 第 "+rowIndex+" 行,商会类型没有填写";
				writeStatus(request,messagetxt,lockFile,true);
				throw new RuntimeException(messagetxt);
			}
			if(importGroupsVO.getProvice().equals("")){
				String messagetxt = "<--失败--> 第 "+rowIndex+" 行,省份没有填写";
				writeStatus(request,messagetxt,lockFile,true);
				throw new RuntimeException(messagetxt);
			}
			if(importGroupsVO.getSignTime().equals("")){
				String messagetxt = "<--失败--> 第 "+rowIndex+" 行,签约时间没有填写";
				writeStatus(request,messagetxt,lockFile,true);
				throw new RuntimeException(messagetxt);
			}else {
				boolean flag = true;
				try {
					new SimpleDateFormat("yyyy/MM/dd").parse(importGroupsVO.getSignTime());
				} catch (Exception e) {
					flag =false;
				}
				if(flag==false){
					String messagetxt = "<--失败--> 第 "+rowIndex+" 行,签约时间格式填写错误";
					writeStatus(request,messagetxt,lockFile,true);
					throw new RuntimeException(messagetxt);
				}
			}
		}
		/**
		 * 
		 * @param message 当时0时候传入
		 * @param type 是0就是判断是否为空，是1就是判断是否为空，且是否是等于1,2自定义消息模式
		 * @param rowIndex 行数
		 */
		private void checkParams(Integer size ,String message,int type,int rowIndex,HttpServletRequest request,File lockFile){
			String messagetxt ="";
			if(type==0){
				if(size==0){
					messagetxt = "<--失败--> 第 "+rowIndex+" 行,"+message+"不存在";
					writeStatus(request,messagetxt,lockFile,true);
					lockFile.delete();
					throw new RuntimeException(messagetxt);
				}
			}else if(type==1){
				if(size==0){
					messagetxt = "<--失败--> 第 "+rowIndex+" 行,"+message+"不存在";
					writeStatus(request,messagetxt,lockFile,true);
					lockFile.delete();
					throw new RuntimeException(messagetxt);
				}else if(size>1){
					messagetxt = "<--失败--> 第 "+rowIndex+" 行,"+message+"+匹配到多条,请填入精确的地区值";
					writeStatus(request,messagetxt,lockFile,true);
					lockFile.delete();
					throw new RuntimeException(messagetxt);
				}
			}else if(type==2){
				if(size==0){
					messagetxt = message;
					writeStatus(request,messagetxt,lockFile,true);
					lockFile.delete();
					throw new RuntimeException(messagetxt);
				}else if(size>1){
					messagetxt = "<--失败--> 第 "+rowIndex+" 行,"+message+"+匹配到多条,请填入精确的地区值";
					writeStatus(request,messagetxt,lockFile,true);
					lockFile.delete();
					throw new RuntimeException(messagetxt);
				}
			}
		}
		@Override
		public void writeStatus(HttpServletRequest request,String message,File lockFile,boolean flag){
			String filepath = request.getSession().getServletContext().getRealPath("/function/template/importStatus.txt");
			System.out.println("******************************************");
			System.out.println(filepath);
			System.out.println("*******************************************");
			FileWriter wt = null;
			try {
				wt = new FileWriter(filepath,flag);
				wt.write(message+"\r\n");
				wt.flush();
				wt.close();
			} catch (IOException e) {
				lockFile.delete();
				e.printStackTrace();
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public Map<String, Object> getLeastYComent(Integer start,Integer limit) {
			Map<String, Object> map = new HashMap<String,Object>();
			List<YComment> ycommentList = null; 
			String hql = "from YComment ycomment order by ycomment.time desc";
			String hql1 = "select count(*) from YComment ycomment ";
			ycommentList =  (List<YComment>)cOC_HibernateDAO.find(hql, start, limit, null);
			Integer count = 0;
			count =  cOC_HibernateDAO.getTotalCountByCondition(hql1, start, limit, null);
			map.put("count", count);
			if(ycommentList!=null&&ycommentList.size()>0){
				map.put("object", ycommentList);
			}else{
				map.put("object", new ArrayList<YComment>());
			}
			return map;
		}

		@Override
		public void delYComent(String fid) throws Exception{
			cOC_HibernateDAO.deleteByKey(fid, YComment.class);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		@SystemServiceLog(description = "查询团体")
		public List<Map<String, Object>> fuzzyQueryBusinessList(String infoMap) throws JsonProcessingException, IOException {
			Map<String,Object> resultObject = null;//每一group
			List<Map<String,Object>> resulInfoList = new ArrayList<Map<String,Object>>();
			JsonNode  node = objectMapper.readTree(infoMap);
			int pageSize = node.get("pageSize")==null?null:node.get("pageSize").intValue();//分页大小
			String curBusinessId = node.get("curBusinessId")==null?null:node.get("curBusinessId").textValue();//当前商会id
			String queryKey = node.get("queryKey")==null?null:node.get("queryKey").textValue();//当前商会id
			StringBuffer hql = new StringBuffer("from YBasicSocialgroups group where 1=1 and group.fname not like '%狮子%'");
			if(queryKey!=null){
				hql.append(" and group.fname like '%"+queryKey+"%'");
			}
			if(curBusinessId!=null&&!curBusinessId.equals("")){
				YBasicSocialgroups yBasicSocialgroups = (YBasicSocialgroups) cOC_HibernateDAO.findById(YBasicSocialgroups.class,curBusinessId);
				if(yBasicSocialgroups!=null){
					hql.append(" and flag < "+yBasicSocialgroups.getFlag()+" and fbillState =5 order by group.flag desc ");
				}	
			}else{
				hql.append(" and fbillState =5 order by group.flag desc");
			}
			List<YBasicSocialgroups> groupList = (List<YBasicSocialgroups>) cOC_HibernateDAO.find(hql.toString(), 0, pageSize, null);
			if(groupList!=null&&groupList.size()>0){
					String logo = null;
					for(YBasicSocialgroups basicSocialgroups:groupList){
							resultObject = new HashMap<String,Object>();
							resultObject.put("businessId", basicSocialgroups.getFid());
							logo = basicSocialgroups.getLogo();
							if(logo!=null&&!logo.equals("")){
								logo = logo.replace(",", "");
								resultObject.put("businessLogo", logo);
							}
							resultObject.put("businessName", basicSocialgroups.getFname());
							resulInfoList.add(resultObject);
					}
			}else{
				//没有数据不做任何操作
			}
			return resulInfoList;
		}
}
