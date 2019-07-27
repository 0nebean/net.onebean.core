package net.onebean.core.base;

import net.onebean.core.query.Condition;
import net.onebean.core.query.Pagination;
import net.onebean.core.extend.Sort;
import net.onebean.core.extend.SqlMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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
	 * @param dp 权限sql
	 * @return 返回泛型的实体对象的list
	 */
	public List<T> find(Pagination page, @Param(value = "conditions") List<Condition> conditions, @Param(value = "sort") Sort sort, @Param(value = "dp") Map<String, Object> dp);
	/**
	 * 根据id查找对象
	 * @param id 主键
	 * @return 泛型对象
	 */
	public T findById(@Param(value = "id") Object id);
	/**
	 * 新增一条泛型实体对象数据
	 * @param entity 泛型的实体对象
	 */
	public void add(@Param("entity") T entity);
	/**
	 * 更新 泛型的实体对象的数据
	 * @param entity 泛型的实体对象
	 */
	public Integer update(@Param("entity") T entity);
	/**
	 * 删除 泛型的实体对象的数据
	 * @param entity 泛型的实体对象
	 */
	public Integer delete(@Param("entity") T entity);
	/**
	 * 根据id删除
	 * @param id 主键
	 */
	public Integer deleteById(@Param(value = "id") Object id);
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
	public Integer updateBatch(@Param("entity") T entity, @Param("ids") List<Long> ids);

}
