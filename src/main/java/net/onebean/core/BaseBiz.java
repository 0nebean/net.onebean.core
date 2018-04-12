package net.onebean.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.onebean.core.extend.Sort;
import net.onebean.core.model.BaseIncrementIdModel;
import net.onebean.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import net.onebean.util.CollectionUtil;


/**
 * service 层的基类，所有service类必须继承自此类，该类不能直接使用。
 * 将service层一些通用的操作给抽离出来，封装到此类中，其他service类必须继承此类，子类可以直接使用此类中的方法。
 * 该类使用泛型实现了实体和dao层封装
 * ，子类继承此方法时必须指明对应的Model和Dao具体实现类，在setBaseDao(BaseDao)方法中使用spring注解方式实现。
 * 子类在需要使用dao对象的地方 ，直接调用baseDao.method()，该类当前只支持自动装配一个dao实例，如果需要多个，
 * 在自己的service类中以spring注解方式自行配置。
 * 
 * @param <T>
 *            主要操作的实体类型
 * @param <K>
 *            主要操作的Dao类型
 */
public abstract class BaseBiz<T extends BaseIncrementIdModel, K extends BaseDao<T>> implements IBaseBiz<T> {

	/**
	 * dao原型属性
	 */
	protected K baseDao;


	/**
	 * 根据K泛型自动装载BaseDao
	 * 
	 * @param baseDao
	 */
	@Autowired
	public final void setBaseDao(K baseDao) {
		this.baseDao = baseDao;
	}

	public void deleteById(Object id) {
		baseDao.deleteById(id);

	}
	
	public Long getMaxId(){
		Long id = baseDao.getMaxId();
		if (null == id){
			return 0l;
		}
		
		return id;
	}

	public void delete(T entity) {
		baseDao.delete(entity);
	}

	public T findById(Object id) {
		if (StringUtils.isEmpty(id))
			return null;
		return baseDao.findById(id);
	}

	/**
	 * 根据分页和条件进行查询。如果不需要分页，把pagination设为null。 主要是为了方便一个条件的查询，不用在调用时自己封装成List
	 * 
	 * @param pagination
	 * @param condition
	 * @return
	 */
	public List<T> find(Pagination pagination, Condition condition) {
		List<Condition> conditions = null;
		if (condition != null) {
			conditions = new ArrayList<Condition>();
			conditions.add(condition);
		}
		return find(pagination, conditions);
	}

	/**
	 * 根据分页和条件进行查询。如果不需要分页，把pagination设为null。
	 * 
	 * @param pagination
	 * @param conditions
	 * @return
	 */
	public List<T> find(Pagination pagination, ConditionMap conditions) {
		List<Condition> conditionList = null;
		if (conditions != null) {
			conditionList = conditions.getItems();
		}
		return baseDao.find(pagination, conditionList,null);
	}
	/**
	 * 根据分页和条件进行查询。如果不需要分页，把pagination设为null。
	 *
	 * @param pagination
	 * @param conditions
	 * @return
	 */
	public List<T> find(Pagination pagination, ConditionMap conditions, Sort sort) {
		List<Condition> conditionList = null;
		if (conditions != null) {
			conditionList = conditions.getItems();
		}
		return baseDao.find(pagination, conditionList,sort);
	}

	public List<T> find(ListPageQuery query) {
		return baseDao.find(query.getPagination(),query.getConditions().getItems(),query.getSort());
	}

	/**
	 * 查找所有的记录
	 * 
	 * @return
	 */
	public List<T> findAll() {
		return baseDao.find(null, null,null);
	}

	/**
	 * 查找所有的记录
	 *
	 * @return
	 */
	public List<T> findAll(Sort sort) {
		return baseDao.find(null, null,sort);
	}

	/**
	 * 根据分页和条件进行查询。如果不需要分页，把pagination设为null。
	 * 
	 * @param pagination
	 * @param conditions
	 * @return
	 */
	public List<T> find(Pagination pagination, List<Condition> conditions,Sort sort) {
		return baseDao.find(pagination, conditions,sort);

	}	/**
	 * 根据分页和条件进行查询。如果不需要分页，把pagination设为null。
	 *
	 * @param pagination
	 * @param conditions
	 * @return
	 */
	public List<T> find(Pagination pagination, List<Condition> conditions) {
		return baseDao.find(pagination, conditions,null);

	}

	public List<T> find(Pagination pagination,Sort sort) {
		return baseDao.find(pagination, null,sort);

	}
	public List<T> find(Pagination pagination) {
		return baseDao.find(pagination, null,null);

	}

	public void save(T entity) {
		if (entity.getId() == null) {
			baseDao.add(entity);
		} else {
			baseDao.update(entity);
		}
	}

	/**
	 * 批量保存所有实体
	 * 
	 * @param entitiess
	 */
	public void saveBatch(List<T> entities) {
		if (!CollectionUtil.isEmpty(entities)) {
			for (T entity : entities) {
				save(entity);
			}
		}
	}

	/**
	 * 根据id的集合删除一批记录
	 * 
	 * @param ids
	 */
	public void deleteByIds(List<Long> ids) {
		if (!CollectionUtil.isEmpty(ids)) {
			baseDao.deleteByIds(ids);
		}
	}

	/**
	 * 更新实体
	 * 
	 * @param entity
	 */
	public void update(T entity) {
		baseDao.update(entity);
	}

	/**
	 * 把ids对应的实体中的属性值更新成entity中所有非null的属性值
	 * 
	 * @param entity
	 * @param ids
	 */
	public void updateBatch(T entity, List<Long> ids) {
		if (!CollectionUtil.isEmpty(ids)) {
			baseDao.updateBatch(entity, ids);
		}
	}

	/**
	 * 更新list中所有的实体。
	 * 
	 * @param entities
	 */
	public void updateBatch(List<T> entities) {
		if (!CollectionUtil.isEmpty(entities)) {
			for (T entity : entities) {
				save(entity);
			}
		}
	}

	/**
	 * @功能描述: 查询一批实体
	 * @param ids
	 * @return list
	 */
	public List<T> findByIds(List<Long> ids) {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		int length = ids.size();
		for (Long id : ids) {
			sb.append(id);
			if (i < length - 1)
				sb.append(",");
			i++;
		}
		return findByIds(sb.toString());
	}

	/**
	 * @功能描述: 查询一批实体
	 * @param ids "1,2,3,4"...
	 * @return list
	 */
	public List<T> findByIds(String ids) {
		Condition con = Condition.parseCondition("ID@int@in");
		con.setValue(ids);
		return find(null, con);
	}

	@Override
	public Integer searchCount(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return baseDao.searchCount(param);
	}

	@Override
	public List<Map<String, Object>> search(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return baseDao.search(param);
	}

	@Override
	public List<T> searchEntity(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return baseDao.searchEntity(param);
	}
}
