package net.onebean.core.model;

/**
 * 逻辑删除的实体继承此接口
 */

public interface Deleted {
	/**
	 * 删除状态：已删除
	 */
	public static final Integer DELETE_TRUE = 1;
	/**
	 * 删除状态：正常
	 */
	public static final Integer DELETE_FALSE = 0;

	public Integer getDeletedState();

	public void setDeletedState(Integer deletedState);
}
