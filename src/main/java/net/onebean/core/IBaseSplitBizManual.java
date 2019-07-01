package net.onebean.core;

import com.eakay.core.extend.Sort;
import com.eakay.core.model.BaseIncrementIdModel;

import java.util.List;
import java.util.Map;


/**
 * 所有service接口的父接口
 * @author 0neBean
 * @param <T>
 */
public interface IBaseSplitBizManual<T extends BaseIncrementIdModel> {
	/**
	 * 根据ID删除实体
	 * @param id 主键
	 */
	public Integer deleteById(Object id, String suffix);
	/**
	 * 删除实体对象
	 * @param entity 泛型实体对象
	 */
	public Integer delete(T entity, String suffix);
	/**
	 * 根据ID查找对象
	 * @param id 主键
	 * @return 泛型实体对象
	 */
	public T findById(Object id, String suffix);
	/**
	 * 获取最大的ID
	 * @return long型id
	 */
	public Long getMaxId(String suffix);
	/**
	 * 根据分页和条件进行查询。如果不需要分页，把pagination设为null。 主要是为了方便一个条件的查询，不用在调用时自己封装成List
	 * @param pagination 分页参数
	 * @param condition 查询条件
	 * @return 泛型实体对象的list
	 */
	public List<T> find(Pagination pagination, Condition condition, String suffix);
	/**
	 * 根据分页和条件进行查询。如果不需要分页，把pagination设为null。
	 *
	 * @param pagination 分页参数
	 * @param conditions 查询条件
	 * @return 泛型实体对象的list
	 */
	public List<T> find(Pagination pagination, ConditionMap conditions, String suffix);

	/**
	 *
	 * @param pagination 分页参数
	 * @param conditions 查询条件
	 * @param sort 排序字段条件
	 * @return 泛型实体对象的list
	 */
	public List<T> find(Pagination pagination, ConditionMap conditions, Sort sort, String suffix);
	/**
	 * 根据分页条件查询一批实体
	 * @param query 分页排序查询条件封装对象
	 * @return 泛型实体对象的list
	 */
	public List<T> find(ListPageQuery query, String suffix);
	/**
	 * 根据分页条件查询一批实体
	 * @param query 分页排序查询条件封装对象
	 * @param dp 权限sql
	 * @return 泛型实体对象的list
	 */
	public List<T> find(ListPageQuery query, Map<String, Object> dp, String suffix);
	/**
	 * 查找所有的记录
	 *
	 * @return 泛型实体对象的list
	 */
	public List<T> findAll(String suffix);

	/**
	 * 查找所有的记录
	 * @param dp 权限sql
	 * @return 泛型实体对象的list
	 */
	public List<T> findAll(Map<String, Object> dp, String suffix);
	/**
	 *
	 * @param sort 排序字段条件
	 * @return 泛型实体对象的list
	 */
	public List<T> findAll(Sort sort, String suffix);
	/**
	 * 根据分页和条件进行查询。如果不需要分页，把pagination设为null。
	 *
	 * @param pagination 分页参数
	 * @param conditions 查询条件
	 * @return 泛型实体对象的list
	 */
	public List<T> find(Pagination pagination, List<Condition> conditions, String suffix);

	/**
	 *
	 * @param pagination 分页参数
	 * @param conditions 查询条件
	 * @param sort 排序字段条件
	 * @return 泛型实体对象的list
	 */
	public List<T> find(Pagination pagination, List<Condition> conditions, Sort sort, String suffix);
	/**
	 * 根据分页信息查找实体
	 * @param pagination 分页参数
	 * @return 泛型实体对象的list
	 */
	public List<T> find(Pagination pagination, String suffix);

	/**
	 *
	 * @param pagination 分页参数
	 * @param sort 排序字段条件
	 * @return 泛型实体对象的list
	 */
	public List<T> find(Pagination pagination, Sort sort, String suffix);
	/**
	 * 保存实体
	 * @param entity 泛型实体对象
	 */
	public void save(T entity, String suffix);

	/**
	 * 批量保存所有实体
	 *
	 * @param entities 泛型实体对象的list
	 */
	public void saveBatch(List<T> entities, String suffix);

	/**
	 * 根据id的集合删除一批记录
	 *
	 * @param ids id的list
	 */
	public void deleteByIds(List<Long> ids, String suffix);
	/**
	 * 更新实体
	 *
	 * @param entity 泛型实体对象
	 */
	public Integer update(T entity, String suffix);
	/**
	 * 把ids对应的实体中的属性值更新成entity中所有非null的属性值
	 *
	 * @param entity 泛型实体对象
	 * @param ids id的list
	 */
	public Integer updateBatch(T entity, List<Long> ids, String suffix);
	/**
	 * 更新list中所有的实体。
	 *
	 * @param entities 泛型实体对象的list
	 */
	public void updateBatch(List<T> entities, String suffix);
	/**
	 * 查询一批实体
	 * @param ids id的list
	 * @return list 泛型实体对象的list
	 */
	public List<T> findByIds(List<Long> ids, String suffix);
	/**
	 * 查询一批实体
	 * @param ids id的list
	 * @return list 泛型实体对象的list
	 */
	public List<T> findByIds(String ids, String suffix);

}