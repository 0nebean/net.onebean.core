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
 * @author World
 * @param <T> 泛型实体类型
 */
public interface BaseSplitDao<T> extends SqlMapper {
	/**
	 * 条件查询
	 * @param page 分页参数
	 * @param conditions 条件参数
	 * @param sort 排序字段
	 * @param suffix 表后缀
	 * @param dp 权限sql
	 * @return 返回泛型的实体对象的list
	 */
	public List<T> find(Pagination page, @Param(value = "conditions") List<Condition> conditions, @Param(value = "sort") Sort sort
            , @Param(value = "suffix") String suffix, @Param(value = "dp") Map<String, Object> dp);
	/**
	 * 根据id查找对象
	 * @param id 主键
	 * @param suffix 表后缀
	 * @return 泛型对象
	 */
	public T findById(@Param(value = "id") Object id, @Param(value = "suffix") String suffix);
	/**
	 * 新增一条泛型实体对象数据
	 * @param entity 泛型的实体对象
	 * @param suffix 表后缀
	 */
	public void add(@Param("entity") T entity, @Param(value = "suffix") String suffix);
	/**
	 * 更新 泛型的实体对象的数据
	 * @param entity 泛型的实体对象
	 * @param suffix 表后缀
	 * @return 操作的行数
	 */
	public Integer update(@Param("entity") T entity, @Param(value = "suffix") String suffix);
	/**
	 * 删除 泛型的实体对象的数据
	 * @param entity 泛型的实体对象
	 * @param suffix 表后缀
	 * @return 操作的行数
	 */
	public Integer delete(@Param("entity") T entity, @Param(value = "suffix") String suffix);
	/**
	 * 根据id删除
	 * @param id 主键
	 * @param suffix 表后缀
	 * @return 操作的行数
	 */
	public Integer deleteById(@Param(value = "id") Object id, @Param(value = "suffix") String suffix);
	/**
	 * 获取最大的ID
	 * @param suffix 表后缀
	 * @return long型id
	 */
	public Long getMaxId(@Param(value = "suffix") String suffix);
	/**
	 * 根据id的集合删除一批记录
	 * @param ids list的id
	 * @param suffix 表后缀
	 */
	public void deleteByIds(@Param("ids") List<Long> ids, @Param(value = "suffix") String suffix);
	/**
	 * 把ids对应的实体中的属性值更新成entity中所有非null的属性值
	 * @param entity 泛型实体
	 * @param ids list的id
	 * @param suffix 表后缀
	 * @return 操作的行数
	 */
	public Integer updateBatch(@Param("entity") T entity, @Param("ids") List<Long> ids, @Param(value = "suffix") String suffix);

}
