package com.yunzo.cocmore.core.function.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.TNewsCollect;
import com.yunzo.cocmore.core.function.model.mysql.TNewsHeadline;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @ClassName: TNewsHeadlineService 
 * @Description: TODO 新闻头条接口 
 * @date 2014年11月26日 上午10:53:45 
 * @author Ian
 *
 */
public interface TNewsHeadlineService {
	/**
	 * 
	 * @Title: save 
	 * @Description: TODO 新增新闻头条
	 * @param @param newsCollect    
	 * @return void   
	 * @throws
	 */
	public void save(TNewsHeadline newsHeadline);
	/**
	 * 
	 * @Title: update 
	 * @Description: TODO 修改新闻头条
	 * @param @param newsCollect    
	 * @return void   
	 * @throws
	 */
	public void update(TNewsHeadline newsHeadline);
	/**
	 * 
	 * @Title: delete 
	 * @Description: TODO 删除新闻头条
	 * @param @param newsCollect    
	 * @return void   
	 * @throws
	 */
	public void delete(TNewsHeadline newsHeadline);
	/**
	 * 
	 * @Title: findAll 
	 * @Description: TODO 查询所有的新闻头条
	 * @param     
	 * @return void   
	 * @throws
	 */
	public List<TNewsHeadline> findAll();
	/**
	 * 
	 * @Title: getAllDynamicPagingList 
	 * @Description: TODO 分页查询新闻头条
	 * @param @param page
	 * @param @param pageSize
	 * @param @return    
	 * @return PagingList<TNewsHeadline>   
	 * @throws
	 */
	public PagingList<TNewsHeadline> getAllDynamicPagingList(String fheadline,Integer start,Integer limit,String zsxs,String xwfl,String publicTime);
	/**
	 * 
	 * @Title: getById 
	 * @Description: TODO 根据id查询新闻头条
	 * @param @param fid
	 * @param @return    
	 * @return TNewsHeadline   
	 * @throws
	 */
	public TNewsHeadline getById(String fid);
	
	/**
	 * 查询新闻的接口
	 * @return
	 */
	public List<Map<String, Object>> getHeadlineMap();
	
	/**
	 * hql查询
	 * @param hql
	 * @return
	 */
	public List<TNewsHeadline> findByHql(String hql,Integer rowNum);
	
}
