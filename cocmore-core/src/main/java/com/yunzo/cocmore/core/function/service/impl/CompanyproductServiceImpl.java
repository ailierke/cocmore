package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMembercompany;
import com.yunzo.cocmore.core.function.model.mysql.YComment;
import com.yunzo.cocmore.core.function.model.mysql.YCompanyproduct;
import com.yunzo.cocmore.core.function.service.CompanyproductServiceI;

/**
 * 公司主营产品接口实现类
 * @author yunzo
 *
 */
@Service("companyproductService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class CompanyproductServiceImpl implements CompanyproductServiceI{

private static final Logger logger = Logger.getLogger(CompanyproductServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@Override
	@SystemServiceLog(description = "新增公司主营产品")
	public void save(YCompanyproduct companyproduct) {
		dao.save(companyproduct);
	}

	@Override
	@SystemServiceLog(description = "修改公司主营产品")
	public void update(YCompanyproduct companyproduct) {
		dao.update(companyproduct);
	}

	@Override
	@SystemServiceLog(description = "根据id查询公司主营产品")
	public YCompanyproduct getById(String fid) {
		
		return (YCompanyproduct)dao.findById(YCompanyproduct.class, fid);
	}

	@Override
	@SystemServiceLog(description = "删除公司主营产品")
	public void delete(YCompanyproduct companyproduct) {
		dao.delete(companyproduct);
	}

	@Override
	@SystemServiceLog(description = "hql查询公司主营产品")
	public List<YCompanyproduct> findByHql(String hql) {
		
		return (List<YCompanyproduct>)dao.findAllByHQL(hql);
	}

	@Override
	@SystemServiceLog(description = "查询全部公司主营产品并分页，或根据条件查询公司主营产品并分页")
	public List<Map<String, Object>> findList(String fid, Integer Size,
			String memberId) {
		//Map<String,Object> map = new HashMap<String, Object>();
		List<YCompanyproduct> productlist=null;
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		try {
			String hql=null;
			YBasicMember y =(YBasicMember) dao.findById(YBasicMember.class,memberId);
			List<YBasicMembercompany> companys=(List<YBasicMembercompany>)dao.findAllByHQL("from YBasicMembercompany as y where y.YBasicMember.fid='"+memberId+"'");
			if(companys.size()>0){
				YBasicMembercompany company=companys.get(0);
				if(null==fid||"".equals(fid)){
					hql="from YCompanyproduct as y where y.YBasicMembercompany.fcid='"+company.getFcid()+"' order by y.flag desc";
					productlist=(List<YCompanyproduct>)dao.find(hql, 0 ,Size ,null);
				}else{
					YCompanyproduct product=(YCompanyproduct)dao.findById(YCompanyproduct.class, fid);
					hql="from YCompanyproduct as y where y.YBasicMembercompany.fcid='"+company.getFcid()+"' and y.flag<"+product.getFlag()+" order by y.flag desc";
					productlist=(List<YCompanyproduct>)dao.find(hql, 0 ,Size ,null);
				}
				for(YCompanyproduct p:productlist){
					Map<String,Object> productmap=new HashMap<String, Object>();
					productmap.put("productId", p.getFid());//				productId	String	产品Id
					if(p.getFlogoImage()!=null&&!p.getFlogoImage().equals("")){
						String[] logos=p.getFlogoImage().split(",");
						productmap.put("productLogo", logos[0]);//				productLogo	String	产品Logo
					}
					productmap.put("productName", p.getFname());//				productName	String	产品名
					productmap.put("productDetail", p.getFdescription());//				productDetail	String	产品描述
					resultList.add(productmap);
				}
			}/*else{
//				productmap.put("productId", null);//				productId	String	产品Id
//				productmap.put("productLogo", null);//				productLogo	String	产品Logo
//				productmap.put("productName", null);//				productName	String	产品名
//				productmap.put("productDetail", null);//				productDetail	String	产品描述
				resultList.add(productmap);
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
}
