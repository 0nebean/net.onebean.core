package net.onebean.core;

import java.util.List;
import java.util.Map;

import net.onebean.core.extend.Sort;
import org.apache.ibatis.annotations.Param;

import net.onebean.core.extend.SqlMapper;

/**
 * mybatis接口
 * 继承了SqlMapper接口，MapperScannerConfigurer配置中指定了markerInterface为该接口
 * @author 0neBean
 * @param <T>
 */
public interface BaseDao<T> extends SqlMapper {
	/**
	 * 条件查询
	 * @param page 分页参数
	 * @param conditions 条件参数
	 * @param sort 排序字段
	 * @return 返回泛型的实体对象的list
	 */
	public List<T> find(Pagination page,@Param(value = "conditions") List<Condition> conditions,@Param(value = "sort") Sort sort);
	/**
	 * 根据id查找对象
	 * @param id 主键
	 * @return 泛型对象
	 */
	public T findById(Object id);
	/**
	 * 新增一条泛型实体对象数据
	 * @param entity 泛型的实体对象
	 */
	public void add(T entity);
	/**
	 * 更新 泛型的实体对象的数据
	 * @param entity 泛型的实体对象
	 */
	public void update(T entity);
	/**
	 * 删除 泛型的实体对象的数据
	 * @param entity 泛型的实体对象
	 */
	public void delete(T entity);
	/**
	 * 根据id删除
	 * @param id 主键
	 */
	public void deleteById(Object id);
	/**
	 * 获取最大的ID
	 * @return long型id
	 */
	public Long getMaxId();

	/**
	 * 根据id的集合删除一批记录
	 *
	 * @param ids list的id
	 */
	public void deleteByIds(@Param("ids") List<Long> ids);

	/**
	 * 把ids对应的实体中的属性值更新成entity中所有非null的属性值
	 *
	 * @param entity 泛型实体
	 * @param ids list的id
	 */
	public void updateBatch(@Param("entity") T entity,@Param("ids") List<Long> ids);

	/**
	 * 根据mybatis中的配置提供数据搜索功能，调用此方法传入参数返回搜索结果的条数
	 *
	 * @param param map类型参数
	 * @return Integer
	 */
	public Integer searchCount(@Param("param") Map<String, Object> param);

	/**
	 * 提供统一的搜索功能
	 *
	 * @param param map类型参数
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> search(@Param("param") Map<String, Object> param);

	/**
	 * 提供统一的搜索功能
	 *
	 * @param param map类型参数
	 * @return List<T> 泛型对象的list
	 */
	public List<T> searchEntity(@Param("param") Map<String, Object> param);

}
