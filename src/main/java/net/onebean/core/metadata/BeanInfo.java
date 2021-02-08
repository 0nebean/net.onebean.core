package net.onebean.core.metadata;
import java.io.Serializable;
import java.util.List;

/**
 * 
 *
 */
public class BeanInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6665037435903059802L;
	
	private boolean logged = true;
	private String chineseName;
	private boolean cached = false;
	private String orderBy ;
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	private List<PropertyInfo> properties;
	
	public boolean isLogged() {
		return logged;
	}
	public void setLogged(boolean logged) {
		this.logged = logged;
	}
	public boolean isCached() {
		return cached;
	}
	public void setCached(boolean cached) {
		this.cached = cached;
	}

	
	public List<PropertyInfo> getProperties() {
		return properties;
	}
	public void setProperties(List<PropertyInfo> properties) {
		this.properties = properties;
	}

	public String getChineseName() {
		return chineseName;
	}
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

}
