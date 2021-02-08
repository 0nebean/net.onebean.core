package net.onebean.core.metadata;

import net.onebean.core.model.BaseIncrementIdModel;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class PropertyInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3858591765739593679L;
	
	private Method readMethod;
	private Field field;
	private Class<? extends BaseIncrementIdModel> parentModel;
	private String propertyName;
	private String pattern ;
	private boolean required = false;
	private int maxLength =0;
	public Field getField()
	{
		return field;
	}
	public void setField(Field value)
	{
		 field=value;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public Class<?> getReturnType() {
		return returnType;
	}
	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}
	private Class<?> returnType;
	
	public Method getReadMethod() {
		return readMethod;
	}
	public void setReadMethod(Method readMethod) {
		this.readMethod = readMethod;
	}
	public Class<? extends BaseIncrementIdModel> getParentModel() {
		return parentModel;
	}
	public void setParentModel(Class<? extends BaseIncrementIdModel> parentModel) {
		this.parentModel = parentModel;
	}
	
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}	
	
	public String toString(){
		return "propertyName:"+propertyName +"    readMethod   "+readMethod + "      returnType  "+returnType;
	}
}
