package com.yunzo.cocmore.core.function.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicEmployee;
import com.yunzo.cocmore.core.function.model.mysql.YBasicIndustry;
import com.yunzo.cocmore.core.function.model.mysql.YBasicOrganization;
import com.yunzo.cocmore.core.function.model.mysql.YBasicPosition;
import com.yunzo.cocmore.core.function.model.mysql.YBasicentriesEmployeedistribution;
import com.yunzo.cocmore.core.function.service.EmployeeServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <职员服务接口>. <br>
 * @date:2014年11月25日 下午4:49:40
 * @author beck
 * @version V1.0                             
 */
@Service("empService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class EmployeeServiceImpl implements EmployeeServiceI{
	private static final Logger logger = Logger
			.getLogger(EmployeeServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部职员，或根据条件查询，并分页")
	public PagingList<YBasicEmployee> findAll(String orgId,String searchName,Integer start,Integer limit) {
		logger.info("List<YBasicEmployee> findAll()");
		PagingList<YBasicEmployee> pagingList = new PagingList<YBasicEmployee>();
		//不分页就是查询全部
		if(start == null && limit == null){
			pagingList.setList((List<YBasicEmployee>)dao.findAll(YBasicEmployee.class));
			return pagingList;
		}
		
		String hql = "from YBasicEmployee y where 1=1 ";
		//获取数据
		if(searchName != null && !searchName.equals("")){
			hql += "and y.fname like '%" + searchName + "%' ";
		}
		if(orgId != null && !orgId.equals("")){
			hql += "and y.forganizationId = '" + orgId + "' ";
		}
		pagingList.setList((List<YBasicEmployee>) dao.find(hql, start, limit, null));
		
		//获取总条数
		List<YBasicEmployee> list = (List<YBasicEmployee>)dao.find(hql);
		pagingList.setCount(list.size());
		
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "根据id查询职员")
	public YBasicEmployee getById(String fid) {
		// TODO Auto-generated method stub
		logger.info("YBasicEmployee getById(String fid) || id==" + fid);
		return (YBasicEmployee) dao.findById(YBasicEmployee.class, fid);
	}

	@Override
	@SystemServiceLog(description = "新增职员和分录")
	public void save(YBasicEmployee demo) {
		Set<YBasicentriesEmployeedistribution> empDis = null;
		String[] str = demo.getJsonStr().split("}");
		demo.setFid(UUID.randomUUID().toString());
		if(!demo.getJsonStr().equals("")){
			try {
				while(demo.getJsonStr().indexOf("undefined") != -1){
					String fid = UUID.randomUUID().toString();
					String newJsonStr = demo.getJsonStr().replaceFirst("undefined", fid);
					demo.setJsonStr(newJsonStr);
				}
				List<YBasicentriesEmployeedistribution> list = JSON.parseArray(demo.getJsonStr(),YBasicentriesEmployeedistribution.class);
				empDis = new HashSet<YBasicentriesEmployeedistribution>();
				for (int i = 0; i < list.size(); i++) {
					YBasicentriesEmployeedistribution obj = list.get(i);
					String[] array = str[i].split(",");
					for(String temp : array){
						String[] field = temp.split(":");
						if(field[0].indexOf("yBasicPosition.fid") != -1){
							YBasicPosition pos = (YBasicPosition) dao.get(YBasicPosition.class, field[1].replaceAll("\"",""));
							obj.setYBasicPosition(pos);
						}
					}
					obj.setYBasicEmployee(demo);
					empDis.add(obj);
				}
				
				demo.setYBasicentriesEmployeedistributions(empDis);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		dao.save(demo);
	}
	
	@Override
	@SystemServiceLog(description = "修改职员和分录")
	public void update(YBasicEmployee demo) {
		Set<YBasicentriesEmployeedistribution> empDis = null;
		List<YBasicentriesEmployeedistribution> oldEmpDis = findEDByPosID(demo.getFid());
		//获取分录的JSON字符串
		//String jsonStr = demo.getJsonStr();
		String[] str = demo.getJsonStr().split("}");
		//记录修改的原有记录（不是新创建ID的数据记录）
		//String cMsgId = "";
		while(demo.getJsonStr().indexOf("undefined") != -1){
			String fid = UUID.randomUUID().toString();
			String newJsonStr = demo.getJsonStr().replaceFirst("undefined", fid);
			demo.setJsonStr(newJsonStr);
		}
		
		if(!demo.getJsonStr().equals("")){
			
			try {
				List<YBasicentriesEmployeedistribution> interData = JSON.parseArray(demo.getJsonStr(),YBasicentriesEmployeedistribution.class);
				empDis = new HashSet<YBasicentriesEmployeedistribution>();
				 
				for(int i = 0;i < interData.size(); i++){
					YBasicentriesEmployeedistribution y = interData.get(i);
					String[] fields = StringUtils.split(str[i], ",");
					for(String s : fields){
						String[] field = StringUtils.split(s, ":");
						if(field[0].indexOf("yBasicPosition.fid") != -1){
							YBasicPosition pos = (YBasicPosition) dao.get(YBasicPosition.class, field[1].replaceAll("\"",""));
							y.setYBasicPosition(pos);
						}
					}
					y.setYBasicEmployee(demo);
					//demo.getYBasicentriesEmployeedistributions().add(y);
					empDis.add(y);
				}
				
				//循环删除前台选中删除的条款记录
				for(YBasicentriesEmployeedistribution cla : oldEmpDis){
					boolean b = true;
					for(YBasicentriesEmployeedistribution clas : interData){
						if(cla.getFid().equals(clas.getFid())){
							b = false;
							break;
						}
					}
					if(b){
						dao.updateBySQL("delete from y_basicentries_employeedistribution where FID='" + cla.getFid() +"'");
					}
				}
				
				demo.setYBasicentriesEmployeedistributions(empDis);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// TODO Auto-generated method stub
		dao.update(demo);
	}

	@Override
	@SystemServiceLog(description = "删除职员")
	public void delete(YBasicEmployee demo) {
		// TODO Auto-generated method stub
		dao.delete(demo);
	}
	
	@Override
	@SystemServiceLog(description = "修改职员状态")
	public void updateStatus(YBasicEmployee demo) {
		// TODO Auto-generated method stub
		dao.update(demo);
	}

	
	
	/**
	 * 根据职员ID查询职员职位分配的数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "根据职员ID查询职员职位分配的数据")
	public List<YBasicentriesEmployeedistribution> findEDByPosID(String fid){
		List<YBasicentriesEmployeedistribution> list = null;
		list = (List<YBasicentriesEmployeedistribution>) dao.findAllByHQL("from YBasicentriesEmployeedistribution y where y.YBasicEmployee.fid = '" + fid + "'");
		//list = (List<YBasicentriesEmployeedistribution>) dao.getListByHql("from YBasicentriesEmployeedistribution y where y.YBasicEmployee.fid = '" + fid + "'");
		return list;
	}
	
	/**
	 * 根据职位查找职员
	 */
	@Override
	@SystemServiceLog(description = "根据职位查找职员")
	public List<YBasicentriesEmployeedistribution> findPosByEdID(String fid) {
		List<YBasicentriesEmployeedistribution> list = null;
		list = (List<YBasicentriesEmployeedistribution>) dao.findAllByHQL("from YBasicentriesEmployeedistribution y where y.YBasicPosition.fid = '" + fid + "'");
		//list = (List<YBasicentriesEmployeedistribution>) dao.getListByHql("from YBasicentriesEmployeedistribution y where y.YBasicEmployee.fid = '" + fid + "'");
		return list;
	}
	
}
