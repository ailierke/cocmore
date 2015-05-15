package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YReplytocomment;
import com.yunzo.cocmore.core.function.service.ReplytocommentServiceI;

/**
 * 
 * 测试缓存
 * @author：jackpeng
 * @date：2014年12月17日上午4:39:25
 * 回复service实现类
 */
@Service("replyTocommentService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class ReplytocommentServiceImpl implements ReplytocommentServiceI {
	
	private static final Logger logger = Logger.getLogger(ReplytocommentServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部回复，并分页")
	public List<Map<String, Object>> getReplyList(String mid,String fId,int pageSize,String commentID) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		Map<String, Object> mapValue = null;
		YReplytocomment reply = null;
		String hql = "from YReplytocomment y where y.YComment.fid = '"+commentID+"' order by y.flag desc";
		List<YReplytocomment> list = (List<YReplytocomment>) dao.find(hql, 0, pageSize, null);
		for(int i = 0;i < list.size();i++){
			mapValue = new HashMap<String, Object>();
			reply = list.get(i);
			
			mapValue.put("userImageUrl", reply.getYComment().getYBasicMember().getFheadImage());
			mapValue.put("userNickeName", reply.getYComment().getYBasicMember().getFnickName());
			mapValue.put("reviewDate", reply.getFreplyTime());
			mapValue.put("reviewContent", reply.getFreplyContents());
			mapValue.put("mid", mid);
			listMap.add(mapValue);
		}
		return listMap;
	}
	@Override
	@Cacheable(value="commonCache",key="#id+'user'")//先看缓存里面有没有，没有就查出来，返回的对象加入缓存，有就直接返回
	public YReplytocomment findReplyById(String id) {
		YReplytocomment reply = null;
		reply = (YReplytocomment) dao.findById(YReplytocomment.class, id);	
		return reply;
	}
	/**
	 * value="commonCache"这个cache的名称要和Ehcache中的名称一致否则会报错
	 */
	@Override
	@CachePut(value="commonCache",key="#reply.fid+'user'")//新增对象后，将cache中key相同的进行跟新，对应更新的值是return的对象，放在save、update对象上面
	public YReplytocomment save(YReplytocomment reply) {
		dao.save(reply);
		return reply;
	}
	
	@Override
	@CacheEvict(value="commonCache",key="#id+'user'")//在删除的时候进行删除操作
	public void del(String id) {
		dao.deleteByKey(id, YReplytocomment.class);
	}
	@Override
	@CachePut(value="commonCache",key="#reply.fid+'user'")//修改对象后，将cache中key相同的进行跟新，对应更新的值是return的对象(一定要有)
	public YReplytocomment update(YReplytocomment reply) {
		dao.update(reply);
		return reply;
	}
	/**
	 * 总结以上现象，一般的cache都使用在dao层,业务层使用在更新缓存就有问题,而且对一个个单独的模块来说一般的更新、删除、增加的key都是一样的，通过id或者通过name，而且在相同的cache(commonCache)中，各个模块的key不能重复
	 * 所有的key最好就使用 #id+方法名或者 查询关键字+方法名  但这里没有自己的专有dao。这种架构最好是使用每个模块专有的dao
	 */
}
