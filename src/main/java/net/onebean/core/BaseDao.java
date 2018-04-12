package net.onebean.core;

import java.util.List;
import java.util.Map;

import net.onebean.core.extend.Sort;
import org.apache.ibatis.annotations.Param;

import net.onebean.core.extend.SqlMapper;


public interface BaseDao<T> extends SqlMapper {

	public List<T> find(Pagination page,
                        @Param(value = "conditions") List<Condition> conditions,
                        @Param(value = "sort") Sort sort);

	public T findById(Object id);

	public void add(T entity);

	public void update(T entity);

	public void delete(T entity);

	public void deleteById(Object id);
	
	public Long getMaxId();

	/**
	 * 根据id的集合删除一批记录
	 * 
	 * @param ids
	 */
	public void deleteByIds(@Param("ids") List<Long> ids);

	/**
	 * 把ids对应的实体中的属性值更新成entity中所有非null的属性值
	 * 
	 * @param entity
	 * @param ids
	 */
	public void updateBatch(@Param("entity") T entity,
                            @Param("ids") List<Long> ids);

	/**
	 * 根据mybatis中的配置提供数据搜索功能，调用此方法传入参数返回搜索结果的条数
	 * 
	 * @param param
	 * @return
	 */
	public Integer searchCount(@Param("param") Map<String, Object> param);

	/**
	 * 提供统一的搜索功能
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> search(
            @Param("param") Map<String, Object> param);

	/**
	 * 提供统一的搜索功能
	 * 
	 * @param param
	 * @return
	 */
	public List<T> searchEntity(@Param("param") Map<String, Object> param);

}
