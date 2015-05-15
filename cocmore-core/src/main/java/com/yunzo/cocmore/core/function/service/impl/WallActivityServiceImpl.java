package com.yunzo.cocmore.core.function.service.impl;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.FAwardSetting;
import com.yunzo.cocmore.core.function.model.mysql.FAwardsRecord;
import com.yunzo.cocmore.core.function.model.mysql.YBasicJoinActivity;
import com.yunzo.cocmore.core.function.model.mysql.YWallactivity;
import com.yunzo.cocmore.core.function.service.WallActivityService;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @author：jackpeng
 * @date：2014年11月26日下午1:54:54
 * 上墙活动service实现类
 */
@Service("waService")
@Transactional(propagation = Propagation.REQUIRED,readOnly = false,rollbackFor = {Exception.class})
public class WallActivityServiceImpl implements WallActivityService {
	
	private static final Logger logger = Logger.getLogger(WallActivityServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部上墙活动，并分页")
	public PagingList<YWallactivity> getAllActivityPagingList(Integer page,
			Integer pageSize, String groupId, String theme) {
		// TODO Auto-generated method stub
		logger.info("查询信息列表，并分页....");
		PagingList<YWallactivity> pagingList = new PagingList<YWallactivity>();
		List<Object> values = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from YWallactivity y where 1=1"); 
		StringBuffer hqlCount = new StringBuffer("select count(0) from YWallactivity y where 1=1"); 
		/**
		 * 判断是否通过groupId或headline
		 */
		if(groupId != null){
			hqlList.append(" and y.YBasicSocialgroups.fid=?");
			hqlCount.append(" and y.YBasicSocialgroups.fid=?");
			values.add(groupId);
		}
		if(theme != null){
			hqlList.append(" and y.ftheme like '%"+theme+"%'");
			hqlCount.append(" and y.ftheme like '%"+theme+"%'");
		}
		//获得此页数据
		pagingList.setList((List<YWallactivity>) dao.find(hqlList.toString(), page, pageSize, values.toArray()));
		//获得总条数
		pagingList.setCount(dao.getTotalCountByCondition(hqlCount.toString(), page, pageSize, values.toArray()));
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部上墙活动")
	public List<YWallactivity> findAll() {
		// TODO Auto-generated method stub
		logger.info("List<YWallactivity> findAll()");
		return (List<YWallactivity>)dao.findAll(YWallactivity.class);
	}

	@Override
	@SystemServiceLog(description = "根据id查询上墙活动")
	public YWallactivity getById(String id) {
		// TODO Auto-generated method stub
		logger.info("YWallactivity getById(String id) fid=="+id);
		return (YWallactivity)dao.findById(YWallactivity.class, id);
	}

	@Override
	@SystemServiceLog(description = "新增上墙活动")
	public void save(YWallactivity wallActivity) {
		// TODO Auto-generated method stub
		dao.save(wallActivity);
	}

	@Override
	@SystemServiceLog(description = "删除上墙活动")
	public void delete(YWallactivity wallActivity) {
		// TODO Auto-generated method stub
		dao.delete(wallActivity);
	}

	@Override
	public void update(YWallactivity wallActivity) {
		// TODO Auto-generated method stub
		dao.update(wallActivity);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询上墙活动")
	public List<YWallactivity> getByHql(String hql) {
		// TODO Auto-generated method stub
		logger.info("List<YWallactivity> getByHql(String hql)");
		return (List<YWallactivity>) dao.findAllByHQL(hql);
	}

	@Override
	@SystemServiceLog(description = "保存单个参加活动上墙的人（可手动导入）")
	public void saveJoinActivity(YBasicJoinActivity yBasicJoinActivity) {
		logger.info("参加上墙活动添加...");
		 dao.save(yBasicJoinActivity);
	}

	@Override
	@SystemServiceLog(description = "删除多个ids参加活动人")
	public void deleteJoinActivity(String id) {
		 dao.deleteByKey(id, YBasicJoinActivity.class);
	}

	@Override
	@SystemServiceLog(description = "修改参加活动上墙记录")
	public void updateJoinActivity(YBasicJoinActivity yBasicJoinActivity) {
		dao.update(yBasicJoinActivity);
	}

	@Override
	@SystemServiceLog(description = "根据excel文件来导入读取")
	public void importJoinActivity(MultipartFile file, String activityId) throws Exception {
		this.importJoinPeople(file,activityId);
	}

	@Override
	@SystemServiceLog(description = "查询上墙活动，并分页")
	public PagingList<YBasicJoinActivity> getAllGroupsPagingList(Integer start,
			Integer limit, String searchCondition, String ztid) {
		logger.info("查询团体信息列表....");
		PagingList<YBasicJoinActivity> pagingList = new PagingList<YBasicJoinActivity>();
		List<Object> values = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from YBasicJoinActivity joinActivity"); 
		StringBuffer hqlCount = new StringBuffer("select count(0) from YBasicJoinActivity joinActivity"); 
		/**
		 * 通过groupId
		 */
		if(null!=ztid&&!"".equals(ztid)){
			hqlList.append(" where joinActivity.YWallactivity.fid=?");
			hqlCount.append(" where joinActivity.YWallactivity.fid=?");
			values.add(ztid);
		}
		/**
		 * 前台只通过userName名称的模糊查询
		 */
		if(null!=searchCondition&&!"".equals(searchCondition)){
			if(values.size()>0){
				hqlList.append(" and ");
				hqlCount.append(" and ");
			}else{
				hqlList.append(" where ");
				hqlCount.append(" where ");
			}
			hqlList.append("joinActivity.userName like ?");
			hqlCount.append("joinActivity.userName like ?");
			values.add("%"+searchCondition+"%");
		}
		
		/**
		 * 获得此页数据
		 */
		pagingList.setList((List<YBasicJoinActivity>) dao.find(hqlList.toString(), start, limit, values.toArray()));
		/**
		 * 获得总条数
		 */
		pagingList.setCount(dao.getTotalCountByCondition(hqlCount.toString(), start, limit, values.toArray()));
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}
	
	@SystemServiceLog(description = "")
	public  void importJoinPeople(MultipartFile file2, String tzid) throws Exception{
//		File file = new File("D:\\joinActivityPeople.xlsx");
		    Workbook  wb = null;  
		    InputStream in= file2.getInputStream();
		    if (!in.markSupported()) {

		    	in = new PushbackInputStream(in, 8);

		    	}
		    if (POIFSFileSystem.hasPOIFSHeader(in)) {
		    	wb= new HSSFWorkbook(in);
		    }else{
		    	if (POIXMLDocument.hasOOXMLHeader(in)) {
			    	wb= new XSSFWorkbook(OPCPackage.open(in));
			    }
		    }
         Cell cell = null; 
         YBasicJoinActivity activity = null;
         for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {  
             Sheet st = wb.getSheetAt(sheetIndex);  
             for (int rowIndex = 1; rowIndex <= st.getLastRowNum(); rowIndex++) {  
                 Row row = st.getRow(rowIndex);  
                short clumLength = row.getLastCellNum();
                activity = new YBasicJoinActivity();
                for(short i=0;i<clumLength;i++){
                	String value ="";
                	cell = row.getCell(i);  
                    if(cell!=null){
                    	 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    	 value = cell.getStringCellValue();
                    }else{
                    	value="";
                    }
                    switch (i) {
					case 0:
						activity.setUserName(value);
						break;
					case 1:
						activity.setTel(value);
						break;
					case 2:
						activity.setSetNumber(isNum(value)==false?null:new Integer(value));
						break;
					case 3:
						activity.setGroupName(value);
						break;
					default:
						break;
					}
                }
                activity.setYWallactivity(new YWallactivity(tzid));
                dao.save(activity);
             }  
         }  
           
	}
	private  boolean isNum(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
//	public static void main(String[] args) throws Exception {
//		importJoinPeople();
//	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "填写随机座位号的范围，改变签到人员的座位号")
	public void changeSeatNumberForJionWallActivitypeople(Integer startNum,Integer endNum, String ztid) {
			//查出所有的已存的此主题的人员的座位号。在范围中排除掉，将范围中其他的数的作为号赋值给没有座位号的人
		List<YBasicJoinActivity> noSeatNumberJoinPeoples = (List<YBasicJoinActivity>) dao.findAllByHQL("from YBasicJoinActivity joinActivity where joinActivity.YWallactivity.fid='"+ztid+"' and joinActivity.setNumber is null");//获取没有座位号的人员员
		List<YBasicJoinActivity> hasSeatNumberJoinPeoples = (List<YBasicJoinActivity>)dao.findAllByHQL("from YBasicJoinActivity joinActivity where joinActivity.YWallactivity.fid='"+ztid+"' and joinActivity.setNumber is not null");//获取有座位号的人
		List<Integer> hasNumberList = new ArrayList<Integer>();//所有已经存在的座位号编号
		if(endNum-startNum+1<noSeatNumberJoinPeoples.size()){
			throw new RuntimeException("设置的座位号范围不够没有座位号的人员分配。");
		}else{
			for(YBasicJoinActivity activity:hasSeatNumberJoinPeoples){
				hasNumberList.add(activity.getSetNumber());
			}
			//将范围内的所有数字座位号-已经存在的座位号<noNumber的。说明也不够分配
			List<Integer> NumberList = new ArrayList<Integer>();//可以用来赋予座位号的num
			for(int i=startNum;i<=endNum;i++){
				if(!hasNumberList.contains(new Integer(i))){
					NumberList.add(new Integer(i));
				}
			}
			if(NumberList.size()<noSeatNumberJoinPeoples.size()){
				throw new RuntimeException("设置的座位号范围不够没有座位号的人员分配。");
			}else{
				//给所有的没有座位的赋予座位，从NumberList中选不重复的
				for(int i=0;i<noSeatNumberJoinPeoples.size();i++){
					noSeatNumberJoinPeoples.get(i).setSetNumber(NumberList.get(i));//给其赋予座位，由于前面判断NumberList长度肯定大于noSeatNumberJoinPeoples
					dao.saveOrUpdate(noSeatNumberJoinPeoples.get(i));
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "为抽奖查询奖项和参与人（去掉已经中奖的人）")
	public Map<String, Object> findAllJionWallActivitypeople(String ztid) {
		Map<String,Object> map =null;
		try{
			 map = new HashMap<String,Object>();
			String hql = "from FAwardSetting  fAwardSetting where fAwardSetting.YWallactivity.fid='"+ztid+"'";
			//获取此活动主题所有奖项设置
			List<FAwardSetting> fAwardSettingList =(List<FAwardSetting>) dao.findAllByHQL(hql);
			//获取已中奖人数    
			hql ="from FAwardsRecord fAwardsRecord where fAwardsRecord.fthemeId='"+ztid+"'"; 
			List<FAwardsRecord> fAwardsRecordList =(List<FAwardsRecord>) dao.findAllByHQL(hql);
			//获取所有参与人   
			hql ="from YBasicJoinActivity yBasicJoinActivity where yBasicJoinActivity.YWallactivity.fid='"+ztid+"'"; 
			List<YBasicJoinActivity> yBasicJoinActivityList =null;
			yBasicJoinActivityList =(List<YBasicJoinActivity>) dao.findAllByHQL(hql);
			//通过电话号码来去重复
			
			if(fAwardsRecordList!=null&&fAwardsRecordList.size()>0){
				for(FAwardsRecord fAwardsRecord :fAwardsRecordList){
					if(fAwardsRecordList!=null&&fAwardsRecordList.size()>0){
						Iterator<YBasicJoinActivity> yBasicJoinActivity = yBasicJoinActivityList.iterator();
						while (yBasicJoinActivity.hasNext()) {
							YBasicJoinActivity yBasicJoinActivity1 = (YBasicJoinActivity) yBasicJoinActivity.next();
							if(fAwardsRecord.getFmemberId()!=null&&fAwardsRecord.getFmemberId().equals(yBasicJoinActivity1.getTel())){//代表的是电话号码
								yBasicJoinActivity.remove();
								break;
							}
						}
					}
				}
			}
			map.put("people", yBasicJoinActivityList);
			map.put("winSetting", fAwardSettingList);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}

	@Override
	@SystemServiceLog(description = "同时满足一下条件 查询座位号")
	public Integer getWallActivitypeopleSeatNum(String userName,
			String tel, String groupname, String ztid) {
		Integer setNum = null;
		String hql = "from YBasicJoinActivity  yBasicJoinActivity where yBasicJoinActivity.userName='"+userName+"' and yBasicJoinActivity.groupName='"+groupname+"' and yBasicJoinActivity.YWallactivity.fid='"+ztid+"'";
		if(tel!=null&&!tel.equals("")){
			 hql = hql+" and yBasicJoinActivity.tel='"+tel+"'";
		}
		List<YBasicJoinActivity> list =(List<YBasicJoinActivity>) dao.findAllByHQL(hql);
		if(list!=null&&list.size()>0){
			setNum =list.get(0).getSetNumber();
		}
		return setNum;
	}

	@Override
	@SystemServiceLog(description = "增加中奖人信息")
	public void saveWinActivitypeople(String ztid, String[] tel,
			String settingIds) {
		FAwardsRecord record = null;
		for(String t:tel){
			record =new FAwardsRecord();
			record.setFawardDate(new java.sql.Date(new java.util.Date().getTime()));
			record.setFAwardSetting(new FAwardSetting(settingIds));
			record.setFmemberId(t);
			record.setFthemeId(ztid);
			dao.save(record);
		}
	}
	
//	public static void main(String[] a){
//		List<YBasicJoinActivity> activities = new ArrayList<YBasicJoinActivity>();
//		activities.add(new YBasicJoinActivity("fid"));
//		activities.get(0).setFid("jx");
//		System.out.println(activities.get(0).getFid());
//	}
	
}