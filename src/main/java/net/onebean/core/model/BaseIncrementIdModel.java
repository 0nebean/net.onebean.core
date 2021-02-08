package net.onebean.core.model;

import net.onebean.core.extend.OrderBy;

import java.io.Serializable;


@OrderBy("id desc")
public class BaseIncrementIdModel implements Serializable{
	private static final long serialVersionUID = 3120480270658878412L;

	private Long id;
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
