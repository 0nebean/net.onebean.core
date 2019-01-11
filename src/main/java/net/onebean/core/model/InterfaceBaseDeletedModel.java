package net.onebean.core.model;

/**
 * 逻辑删除的实体继承此接口
 */

public interface InterfaceBaseDeletedModel {
	/**
	 * 删除状态：已删除
	 */
	public static final Integer DELETE_TRUE = 1;
	/**
	 * 删除状态：正常
	 */
	public static final Integer DELETE_FALSE = 0;


	public String getIsDeleted();
	public void setIsDeleted(String isDeleted);


}
