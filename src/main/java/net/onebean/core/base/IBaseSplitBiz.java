package net.onebean.core.base;

import net.onebean.core.query.Condition;
import net.onebean.core.query.ConditionMap;
import net.onebean.core.query.ListPageQuery;
import net.onebean.core.query.Pagination;
import net.onebean.core.extend.Sort;
import net.onebean.core.model.BaseIncrementIdModel;

import java.util.List;
import java.util.Map;


/**
 * 所有service接口的父接口
 * @author 0neBean
 * @param <T> 泛型实体类型
 */
public interface IBaseSplitBiz<T extends BaseIncrementIdModel> {
	/**
	 * 根据ID删除实体
	 * @param id 主键
	 * @return 操作的行数
	 */
	public Integer deleteById(Object id);
	/**
	 * 删除实体对象
	 * @param entity 泛型实体对象
	 * @return 操作的行数
	 */
	public Integer delete(T entity);
	/**
	 * 根据ID查找对象
	 * @param id 主键
	 * @return 泛型实体对象
	 */
	public T findById(Object id);
	/**
	 * 获取最大的ID
	 * @return long型id
	 */
	public Long getMaxId();
	/**
	 * 根据分页和条件进行查询。如果不需要分页，把pagination设为null。 主要是为了方便一个条件的查询，不用在调用时自己封装成List
	 * @param pagination 分页参数
	 * @param condition 查询条件
	 * @return 泛型实体对象的list
	 */
	public List<T> find(Pagination pagination, Condition condition);
	/**
	 * 根据分页和条件进行查询。如果不需要分页，把pagination设为null。
	 *
	 * @param pagination 分页参数
	 * @param conditions 查询条件
	 * @return 泛型实体对象的list
	 */
	public List<T> find(Pagination pagination, ConditionMap conditions);

	/**
	 *
	 * @param pagination 分页参数
	 * @param conditions 查询条件
	 * @param sort 排序字段条件
	 * @return 泛型实体对象的list
	 */
	public List<T> find(Pagination pagination, ConditionMap conditions, Sort sort);
	/**
	 * 根据分页条件查询一批实体
	 * @param query 分页排序查询条件封装对象
	 * @return 泛型实体对象的list
	 */
	public List<T> find(ListPageQuery query);
	/**
	 * 根据分页条件查询一批实体
	 * @param query 分页排序查询条件封装对象
	 * @param dp 权限sql
	 * @return 泛型实体对象的list
	 */
	public List<T> find(Map<String, Object> dp,ListPageQuery query);
	/**
	 * 查找所有的记录
	 *
	 * @return 泛型实体对象的list
	 */
	public List<T> findAll();

	/**
	 * 查找所有的记录
	 * @param dp 权限sql
	 * @return 泛型实体对象的list
	 */
	public List<T> findAll(Map<String, Object> dp);
	/**
	 *
	 * @param sort 排序字段条件
	 * @return 泛型实体对象的list
	 */
	public List<T> findAll(Sort sort);
	/**
	 * 根据分页和条件进行查询。如果不需要分页，把pagination设为null。
	 *
	 * @param pagination 分页参数
	 * @param conditions 查询条件
	 * @return 泛型实体对象的list
	 */
	public List<T> find(Pagination pagination, List<Condition> conditions);

	/**
	 *
	 * @param pagination 分页参数
	 * @param conditions 查询条件
	 * @param sort 排序字段条件
	 * @return 泛型实体对象的list
	 */
	public List<T> find(Pagination pagination, List<Condition> conditions, Sort sort);
	/**
	 * 根据分页信息查找实体
	 * @param pagination 分页参数
	 * @return 泛型实体对象的list
	 */
	public List<T> find(Pagination pagination);

	/**
	 *
	 * @param pagination 分页参数
	 * @param sort 排序字段条件
	 * @return 泛型实体对象的list
	 */
	public List<T> find(Pagination pagination, Sort sort);
	/**
	 * 保存实体
	 * @param entity 泛型实体对象
	 */
	public void save(T entity);

	/**
	 * 批量保存所有实体
	 *
	 * @param entities 泛型实体对象的list
	 */
	public void saveBatch(List<T> entities);

	/**
	 * 根据id的集合删除一批记录
	 *
	 * @param ids id的list
	 */
	public void deleteByIds(List<Long> ids);
	/**
	 * 更新实体
	 * @param entity 泛型实体对象
	 * @return 操作的行数
	 */
	public Integer update(T entity);
	/**
	 * 把ids对应的实体中的属性值更新成entity中所有非null的属性值
	 * @param entity 泛型实体对象
	 * @param ids id的list
	 * @return 操作的行数
	 */
	public Integer updateBatch(T entity, List<Long> ids);
	/**
	 * 更新list中所有的实体。
	 *
	 * @param entities 泛型实体对象的list
	 */
	public void updateBatch(List<T> entities);
	/**
	 * 查询一批实体
	 * @param ids id的list
	 * @return list 泛型实体对象的list
	 */
	public List<T> findByIds(List<Long> ids);
	/**
	 * 查询一批实体
	 * @param ids id的list
	 * @return list 泛型实体对象的list
	 */
	public List<T> findByIds(String ids);
	/**
	 * 获取租户ID
	 * @return 租户ID
	 */
	public abstract String getTenantId();
	/**
	 * 设置租户ID
	 * @param tenantId 租户ID
	 */
	public abstract void setTenantId(String tenantId);
}