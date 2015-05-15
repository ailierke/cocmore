package com.yunzo.cocmore.core.dao.hibernate.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;

/**
 * Description: <COC框架 全数据访问组件>. <br>
 * <p>
 * <数据访问组件实现类>
 * </p>
 * Makedate:2014年11月18日 上午4:26:59
 * 
 * @author xiaobo
 * @version V1.0
 */
@Component("hibernateDAO")
public class COC_HibernateDAOImpl implements COC_HibernateDAO {

	/**
	 * @Fields logger : TODO(log日志)
	 */
	private Logger logger = Logger.getLogger(COC_HibernateDAOImpl.class);

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private void lazyInitialize(Class<?> entityClazz, List<?> l, String[] fields) {
		if (fields != null) {
			for (String field : fields) {

				String targetMethod = "get" + upperFirstWord(field);

				Method method;
				try {
					method = entityClazz.getDeclaredMethod(targetMethod);
					for (Object o : l) {
						Hibernate.initialize(method.invoke(o));
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private void lazyInitialize(Class<?> entityClazz, Object obj,
			String[] fields) {
		if (fields != null) {
			for (String field : fields) {

				String targetMethod = "get" + upperFirstWord(field);

				Method method;
				try {
					method = entityClazz.getDeclaredMethod(targetMethod);
					Hibernate.initialize(method.invoke(obj));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private String upperFirstWord(String str) {
		StringBuffer sb = new StringBuffer(str);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		return sb.toString();
	}
	private Session getHibernateSession() {
		Session session = sessionFactory.getCurrentSession();
		return session;
	}

	@Override
	public Object get(Class<?> entityClazz, Serializable id, String... str) {
		Object obj = getHibernateSession().get(entityClazz, id);
		lazyInitialize(entityClazz, obj, str);
		return obj;
	}
	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#load(java.io.Serializable)
	 */
	@Override
	public Object load(Class<?> entityClazz, Serializable id, String... str) {
		Object obj = getHibernateSession().load(entityClazz, id);
		lazyInitialize(entityClazz, obj, str);
		return obj;
	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#findById(java.io.Serializable
	 * )
	 */
	@Override
	public Object findById(Class<?> entityClazz, Serializable id, String... str) {
		Object obj = getHibernateSession().get(entityClazz, id);
		lazyInitialize(entityClazz, obj, str);
		return obj;
	}
	@Override
	public Object findByIdClearCache(Class<?> entityClazz, Serializable id, String[] str) {
		Object obj = getHibernateSession().get(entityClazz, id);
		lazyInitialize(entityClazz, obj, str);
		getHibernateSession().clear();
		return obj;
	}
	/*
	 * @see com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#loadAll()
	 */
	@Override
	public List<?> loadAll(Class<?> entityClazz, String... str) {
		return findAll(entityClazz, str);
	}

	/*
	 * @see com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#findAll()
	 */
	@Override
	public List<?> findAll(Class<?> entityClazz, String... str) {
		DetachedCriteria dc = DetachedCriteria.forClass(entityClazz);
		List<?> list = findAllByCriteria(dc);
		lazyInitialize(entityClazz, list, str);
		return list;
	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#update(java.lang.Object)
	 */
	@Override
	public void update(Object entity) {
		getHibernateSession().clear();
		getHibernateSession().update(entity);
	}

	/*
	 * @see com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#save(java.lang.Object)
	 */
	@Override
	public void save(Object entity) {
		getHibernateSession().save(entity);
	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#saveOrUpdate(java.lang.
	 * Object)
	 */
	@Override
	public void saveOrUpdate(Object entity) {
		getHibernateSession().saveOrUpdate(entity);
	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#saveOrUpdateAll(java.util
	 * .Collection)
	 */
	@Override
	public void saveOrUpdateAll(Collection<?> entities) {
		logger.info(".......saveOrUpdateAll...");
		for (Object entity : entities) {
			getHibernateSession().saveOrUpdate(entity);
		}
	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#delete(java.lang.Object)
	 */
	@Override
	public void delete(Object entity) {
		getHibernateSession().delete(entity);
	}

	/*
	 * @see com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#deleteByKey(java.io.
	 * Serializable)
	 */
	@Override
	public void deleteByKey(Serializable id, Class<?> entityClazz) {
		getHibernateSession().delete(load(entityClazz, id));
	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#deleteAll(java.util.Collection
	 * )
	 */
	@Override
	public void deleteAll(Collection<?> entities) {
		for (Object entity : entities) {
			getHibernateSession().delete(entity);
		}
	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#bulkUpdate(java.lang.String
	 * )
	 */
	@Override
	public int bulkUpdate(String hql) {
		Query queryObject = getHibernateSession().createQuery(hql);
		return queryObject.executeUpdate();
	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#bulkUpdate(java.lang.String
	 * , java.lang.Object[])
	 */
	@Override
	public int bulkUpdate(String hql, Object... values) {
		Query queryObject = getHibernateSession().createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject.executeUpdate();
	}

	/*
	 * @see com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#find(java.lang.String)
	 */
	@Override
	public List<?> findAllByHQL(String hql) {
		Query queryObject = getHibernateSession().createQuery(hql);
		return queryObject.list();
	}
	/*
	 * @see com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#find(java.lang.String)
	 */
	@Override
	public List<?> findAllByHQLClearCache(String hql) {
		Query queryObject = getHibernateSession().createQuery(hql);
		getHibernateSession().clear();
		return queryObject.list();
	}
	/*
	 * @see com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#find(java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public List<?> find(String hql, Object... values) {
		Query queryObject = getHibernateSession().createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject.list();
	}

	/*
	 * @see com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#createCriteria()
	 */
	@Override
	public Criteria createCriteria(Class<?> entityClazz) {
		return this.createDetachedCriteria(entityClazz).getExecutableCriteria(
				getHibernateSession());
	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#createDetachedCriteria()
	 */
	@Override
	public DetachedCriteria createDetachedCriteria(Class<?> entityClazz) {
		return DetachedCriteria.forClass(entityClazz);
	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#findByCriteria(org.hibernate
	 * .criterion.DetachedCriteria)
	 */
	@Override
	public List<?> findByCriteria(DetachedCriteria detachedCriteria) {
		Criteria executableCriteria = detachedCriteria
				.getExecutableCriteria(getHibernateSession());
		return executableCriteria.list();
	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#findByCriteria(org.hibernate
	 * .criterion.DetachedCriteria, int, int)
	 */
	@Override
	public List<?> findByCriteria(final DetachedCriteria detachedCriteria,
			final int startIndex, final int pageSize) {

		Criteria criteria = detachedCriteria
				.getExecutableCriteria(getHibernateSession())
				.setFirstResult(startIndex).setMaxResults(pageSize);
		return criteria.list();

	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#getRowCount(org.hibernate
	 * .criterion.DetachedCriteria)
	 */

	@Override
	public Integer getTotalCountByCriteria(
			final DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(
				getHibernateSession()).setProjection(Projections.rowCount());
		Number count = (Number) criteria.uniqueResult();

		return count.intValue();

	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#findEqualByEntity(java.
	 * lang.Object, java.lang.String[])
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<?> findEqualByEntity(Object entity, String[] propertyNames,
			Class<?> entityClazz, String... str) {
		Criteria criteria = this.createCriteria(entityClazz);
		Example exam = Example.create(entity);
		exam.excludeZeroes();
		String[] defPropertys = getSessionFactory().getClassMetadata(
				entityClazz).getPropertyNames();
		for (String defProperty : defPropertys) {
			int ii = 0;
			for (ii = 0; ii < propertyNames.length; ++ii) {
				if (defProperty.equals(propertyNames[ii])) {
					criteria.addOrder(Order.asc(defProperty));
					break;
				}
			}
			if (ii == propertyNames.length) {
				exam.excludeProperty(defProperty);
			}
		}
		criteria.add(exam);
		List list = criteria.list();
		lazyInitialize(entityClazz, list, str);
		return list;
	};

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#merge(java.lang.Object)
	 */
	@Override
	public Object merge(Object entity) {
		return getHibernateSession().merge(entity);
	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#findByNamedParam(java.lang
	 * .String, java.lang.String[], java.lang.Object[])
	 */
	@Override
	public List<?> findByNamedParam(String queryString, String[] paramNames,
			Object[] values) {
		Query queryObject = getHibernateSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				applyNamedParameterToQuery(queryObject, paramNames[i],
						values[i]);
			}
		}
		return queryObject.list();
	}

	@SuppressWarnings("rawtypes")
	protected void applyNamedParameterToQuery(Query queryObject,
			String paramName, Object value) throws HibernateException {

		if (value instanceof Collection) {
			queryObject.setParameterList(paramName, (Collection) value);
		} else if (value instanceof Object[]) {
			queryObject.setParameterList(paramName, (Object[]) value);
		} else {
			queryObject.setParameter(paramName, value);
		}
	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#findByNamedQuery(java.lang
	 * .String)
	 */
	@Override
	public List<?> findByNamedQuery(String queryName) {
		Query queryObject = getHibernateSession().createQuery(queryName);

		return queryObject.list();
	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#findByNamedQuery(java.lang
	 * .String, java.lang.Object[])
	 */
	@Override
	public List<?> findByNamedQuery(String queryName, Object[] values) {
		Query queryObject = getHibernateSession().getNamedQuery(queryName);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject.list();
	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#findByNamedQueryAndNamedParam
	 * (java.lang.String, java.lang.String[], java.lang.Object[])
	 */
	@Override
	public List<?> findByNamedQueryAndNamedParam(String queryName,
			String[] paramNames, Object[] values) {
		Query queryObject = getHibernateSession().getNamedQuery(queryName);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				applyNamedParameterToQuery(queryObject, paramNames[i],
						values[i]);
			}
		}
		return queryObject.list();
	}

	/*
	 * @see com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#flush()
	 */
	@Override
	public void flush() {
		getHibernateSession().flush();
	}

	/*
	 * @see
	 * com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#initialize(java.lang.Object
	 * )
	 */
	@Override
	public void initialize(Object proxy) {
		Hibernate.initialize(proxy);
	}

	@Override
	public List<?> findAllByCriteria(DetachedCriteria detachedCriteria) {
		// TODO Auto-generated method stub
		Criteria criteria = detachedCriteria
				.getExecutableCriteria(getHibernateSession());
		return criteria.list();
	}

	@Override
	public List<?> getListBySql(String sql) {
		// TODO Auto-generated method stub
		logger.info("getListBySql=" + sql);
		final String tempsql = sql;
		List<?> list = getHibernateSession().createSQLQuery(tempsql).list();
		return list;
	}

	public int updateBySQL(String sql) {
		logger.info("updateBySQL=" + sql);
		final String tempsql = sql;
		SQLQuery sqlQuery = getHibernateSession().createSQLQuery(tempsql);
		return sqlQuery.executeUpdate();
	}
	
	/*
	 * @see com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO#find(java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public List<?> find(String hql,Integer firstResult,Integer maxResult,Object[] values) {
		Query queryObject = getHibernateSession().createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		/**
		 * 如果有分页条件就分页
		 */
		if(null!=firstResult&&null!=maxResult){
			queryObject.setFirstResult(firstResult).setMaxResults(maxResult);
		}
		return queryObject.list();
	}
	
	@Override
	public Integer getTotalCountByCondition(String hql,Integer firstResult,Integer maxResult,Object[] values) {
		Query queryObject = getHibernateSession().createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		/**
		 * 查询总数据
		 */
		Number count = null;
		try {
			 count = (Number) queryObject.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(count==null) return 0;
		return count.intValue();
	}
	
/**
 * 生成vo的class
 */
	@Override
	public List<?> getListBySqlVO(String sql,Class tagert) {
		// TODO Auto-generated method stub
		logger.info("getListBySql=" + sql);
		final String tempsql = sql;
		List<?> list = getHibernateSession().createSQLQuery(tempsql).setResultTransformer(Transformers.aliasToBean(tagert)).list();
		return list;
	}
	
	@Override
	public List<?> getListBySql(String sql,String classStr,Class tagert) {
		// TODO Auto-generated method stub
		logger.info("getListBySql=" + sql);
		final String tempsql = sql;
		List<?> list = getHibernateSession().createSQLQuery(tempsql).addEntity(classStr, tagert).list();
		return list;
	}

}
