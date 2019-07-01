package net.onebean.core.model;

import com.eakay.core.extend.IgnoreColumn;
import com.eakay.core.extend.OrderBy;

import java.io.Serializable;


@OrderBy("id desc")
public class BaseIncrementIdModel implements Serializable{
	private static final long serialVersionUID = 3120480270658878412L;
	private Long id;

	/*排序字段*/
	private String base_sort = "";
	private String base_orderBy = "";
	/*分页字段*/
	private Integer base_pageSize;
	private Integer base_currentPage;
	private Integer base_offset;
	@IgnoreColumn
	public Integer getBase_offset() {
		base_pageSize = (null == base_pageSize)?10:base_pageSize;
		base_currentPage = (null == base_currentPage)?1:base_currentPage;
		return base_pageSize*(base_currentPage-1);
	}

	public void setBase_offset(Integer base_offset) {
		this.base_offset = base_offset;
	}

	@IgnoreColumn
	public String getBase_sort() {
		return base_sort;
	}

	public void setBase_sort(String base_sort) {
		this.base_sort = base_sort;
	}
	@IgnoreColumn
	public String getBase_orderBy() {
		return base_orderBy;
	}

	public void setBase_orderBy(String base_orderBy) {
		this.base_orderBy = base_orderBy;
	}
	@IgnoreColumn
	public Integer getBase_pageSize() {
		return base_pageSize;
	}

	public void setBase_pageSize(Integer base_pageSize) {
		this.base_pageSize = base_pageSize;
	}
	@IgnoreColumn
	public Integer getBase_currentPage() {
		return base_currentPage;
	}

	public void setBase_currentPage(Integer base_currentPage) {
		this.base_currentPage = base_currentPage;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseIncrementIdModel other = (BaseIncrementIdModel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
