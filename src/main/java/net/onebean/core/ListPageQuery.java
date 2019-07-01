package net.onebean.core;

import com.eakay.core.extend.Sort;

import java.io.Serializable;

/**
 * 包含分页，排序，查询条件的统一条件对象
 * @author 0neBean
 */
public class ListPageQuery implements Serializable {
	/**
	 * 序列化反序列化的ID
	 */
	private static final long serialVersionUID = -2504823418194131828L;

	private Pagination pagination;
	private ConditionMap conditions;
	private Sort sort;

	public Pagination getPagination() {
		if (pagination == null){
			pagination = new Pagination();
		}
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public ConditionMap getConditions() {
		if (conditions == null){
			conditions = new ConditionMap();
		}
		return conditions;
	}

	public void setConditions(ConditionMap conditions) {
		this.conditions = conditions;
	}

	public Sort getSort() {
		if (sort == null){
			sort = new Sort();
		}
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "ListPageQuery{" +
				"pagination=" + pagination +
				", conditions=" + conditions +
				", sort=" + sort +
				'}';
	}
}
