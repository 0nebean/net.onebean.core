package net.onebean.core;

import net.onebean.core.extend.Sort;
import net.onebean.core.model.BaseIncrementIdModel;
import net.onebean.util.CollectionUtil;
import net.onebean.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * service 层的基类，所有service类必须继承自此类，该类不能直接使用。
 * 将service层一些通用的操作给抽离出来，封装到此类中，其他service类必须继承此类，子类可以直接使用此类中的方法。
 * 该类使用泛型实现了实体和dao层封装
 * ，子类继承此方法时必须指明对应的Model和Dao具体实现类，在setBaseDao(BaseDao)方法中使用spring注解方式实现。
 * 子类在需要使用dao对象的地方 ，直接调用baseDao.method()，该类当前只支持自动装配一个dao实例，如果需要多个，
 * 在自己的service类中以spring注解方式自行配置。
 * @author 0neBean
 * @param <T> 主要操作的实体类型
 * @param <K> 主要操作的Dao类型
 */
public abstract class BaseSplitBiz<T extends BaseIncrementIdModel, K extends BaseSplitDao<T>> implements IBaseSplitBiz<T> {

	/**
	 * dao原型属性
	 */
	protected K baseDao;

	/**
	 * 根据K泛型自动装载BaseDao
	 * @param baseDao mybatis接口
	 */
	@Autowired
	public final void setBaseDao(K baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public Integer deleteById(Object id, String suffix) {
		return baseDao.deleteById(id, suffix);
	}

	@Override
	public Long getMaxId(String suffix){
		Long id = baseDao.getMaxId(suffix);
		if (null == id){
			return 0L;
		}
		return id;
	}

	@Override
	public Integer delete(T entity, String suffix) {
		return baseDao.delete(entity, suffix);
	}

	@Override
	public T findById(Object id, String suffix) {
		if (StringUtils.isEmpty(id)){
			return null;
		}
		return baseDao.findById(id, suffix);
	}

	@Override
	public List<T> find(Pagination pagination, Condition condition, String suffix) {
		List<Condition> conditions = null;
		if (condition != null) {
			conditions = new ArrayList<Condition>();
			conditions.add(condition);
		}
		return find(pagination, conditions, suffix);
	}

	@Override
	public List<T> find(Pagination pagination, ConditionMap conditions, String suffix) {
		List<Condition> conditionList = null;
		if (conditions != null) {
			conditionList = conditions.getItems();
		}
		return baseDao.find(pagination, conditionList,null, suffix);
	}


	@Override
	public List<T> find(Pagination pagination, ConditionMap conditions, Sort sort, String suffix) {
		List<Condition> conditionList = null;
		if (conditions != null) {
			conditionList = conditions.getItems();
		}
		return baseDao.find(pagination, conditionList,sort, suffix);
	}

	@Override
	public List<T> find(ListPageQuery query, String suffix) {
		return baseDao.find(query.getPagination(),query.getConditions().getItems(),query.getSort(), suffix);
	}

	@Override
	public List<T> findAll(String suffix) {
		return baseDao.find(null, null,null, suffix);
	}

	@Override
	public List<T> findAll(Sort sort, String suffix) {
		return baseDao.find(null, null,sort, suffix);
	}

	@Override
	public List<T> find(Pagination pagination, List<Condition> conditions,Sort sort, String suffix) {
		return baseDao.find(pagination, conditions,sort, suffix);

	}

	@Override
	public List<T> find(Pagination pagination, List<Condition> conditions, String suffix) {
		return baseDao.find(pagination, conditions,null, suffix);
	}

	@Override
	public List<T> find(Pagination pagination,Sort sort, String suffix) {
		return baseDao.find(pagination, null,sort, suffix);
	}

	@Override
	public List<T> find(Pagination pagination, String suffix) {
		return baseDao.find(pagination, null,null, suffix);
	}

	@Override
	public void save(T entity, String suffix) {
		if (entity.getId() == null) {
			baseDao.add(entity, suffix);
		} else {
			baseDao.update(entity, suffix);
		}
	}

	@Override
	public void saveBatch(List<T> entities, String suffix) {
		if (!CollectionUtil.isEmpty(entities)) {
			for (T entity : entities) {
				save(entity, suffix);
			}
		}
	}

	@Override
	public void deleteByIds(List<Long> ids, String suffix) {
		if (!CollectionUtil.isEmpty(ids)) {
			baseDao.deleteByIds(ids, suffix);
		}
	}

	@Override
	public Integer update(T entity, String suffix) {
		return baseDao.update(entity, suffix);
	}

	@Override
	public Integer updateBatch(T entity, List<Long> ids, String suffix) {
		int res = 0;
		if (!CollectionUtil.isEmpty(ids)) {
			res =  baseDao.updateBatch(entity, ids, suffix);
		}
		return res;
	}

	@Override
	public void updateBatch(List<T> entities, String suffix) {
		if (!CollectionUtil.isEmpty(entities)) {
			for (T entity : entities) {
				save(entity, suffix);
			}
		}
	}

	@Override
	public List<T> findByIds(List<Long> ids, String suffix) {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		int length = ids.size();
		for (Long id : ids) {
			sb.append(id);
			if (i < length - 1){
				sb.append(",");
			}
			i++;
		}
		return findByIds(sb.toString(), suffix);
	}

	@Override
	public List<T> findByIds(String ids, String suffix) {
		Condition con = Condition.parseCondition("ID@int@in");
		con.setValue(ids);
		return find(null, con, suffix);
	}

	@Override
	public Integer searchCount(Map<String, Object> param, String suffix) {
		// TODO Auto-generated method stub
		return baseDao.searchCount(param, suffix);
	}

	@Override
	public List<Map<String, Object>> search(Map<String, Object> param, String suffix) {
		// TODO Auto-generated method stub
		return baseDao.search(param, suffix);
	}

	@Override
	public List<T> searchEntity(Map<String, Object> param, String suffix) {
		// TODO Auto-generated method stub
		return baseDao.searchEntity(param, suffix);
	}
}
