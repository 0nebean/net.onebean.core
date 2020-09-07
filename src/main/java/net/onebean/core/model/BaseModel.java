package net.onebean.core.model;

import net.onebean.core.extend.IgnoreColumn;

public class BaseModel extends BaseIncrementIdModel {

	private static final long serialVersionUID = 3893026001274739846L;


	private String uagIdStr;
	@IgnoreColumn
	public String getUagIdStr() {
		return uagIdStr;
	}
	public void setUagIdStr(String uagIdStr) {
		this.uagIdStr = uagIdStr;
	}
}
