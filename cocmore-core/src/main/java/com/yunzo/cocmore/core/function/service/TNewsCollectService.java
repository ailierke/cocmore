package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.TNewsCollect;
import com.yunzo.cocmore.core.function.model.mysql.TNewsHeadline;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @ClassName: TNewsCollectService 
 * @Description: TODO 新闻收藏接口 
 * @date 2014年11月26日 上午10:53:09 
 * @author Ian
 *
 */
public interface TNewsCollectService {
	/**
	 * 
	 * @Title: save 
	 * @Description: TODO 新增新闻收藏
	 * @param @param newsCollect    
	 * @return void   
	 * @throws
	 */
	public void save(TNewsCollect newsCollect);
	/**
	 * 
	 * @Title: update 
	 * @Description: TODO 修改新闻收藏
	 * @param @param newsCollect    
	 * @return void   
	 * @throws
	 */
	public void update(TNewsCollect newsCollect);
	/**
	 * 
	 * @Title: delete 
	 * @Description: TODO 删除新闻收藏
	 * @param @param newsCollect    
	 * @return void   
	 * @throws
	 */
	public void delete(TNewsCollect newsCollect);
	/**
	 * 
	 * @Title: findAll 
	 * @Description: TODO 查询所有的的新闻收藏
	 * @param @return    
	 * @return List<TNewsHeadline>   
	 * @throws
	 */
	public List<TNewsCollect> findAll();
	/**
	 * 
	 * @Title: getAllDynamicPagingList 
	 * @Description: TODO 分页查询新闻收藏
	 * @param @param page
	 * @param @param pageSize
	 * @param @return    
	 * @return PagingList<TNewsCollect>   
	 * @throws
	 */
	public PagingList<TNewsCollect> getAllDynamicPagingList(Integer page,Integer pageSize);
	/**
	 * 
	 * @Title: getById 
	 * @Description: TODO 根据id查询新闻收藏
	 * @param @param id
	 * @param @return    
	 * @return TNewsCollect   
	 * @throws
	 */
	public TNewsCollect getById(String fid);
}
