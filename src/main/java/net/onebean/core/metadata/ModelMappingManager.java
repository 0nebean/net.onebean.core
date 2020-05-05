package net.onebean.core.metadata;

import net.onebean.core.extend.NotLogged;
import net.onebean.core.extend.OrderBy;
import net.onebean.core.extend.TableName;
import net.onebean.util.ClassUtils;
import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ModelMappingManager {
	
	private static Map<Class<?>, BeanInfo> beanInfoMappings = new HashMap<Class<?>, BeanInfo>();
		
	/**
	 * 得到实体的元数据信息
	 * @param clazz 类型
	 * @param <T> 泛型实体类型的类型
	 * @return 泛型实体类型
	 */
	public static <T> BeanInfo getBeanInfo(Class<T> clazz){
		//if(!BaseIncrementIdModel.class.isAssignableFrom(clazz)){
			//throw new IllegalArgumentException(clazz.getName()+ "is not a model class");
		//}
		BeanInfo beanInfo = beanInfoMappings.get(clazz);
		if(beanInfo == null){
			beanInfo = new BeanInfo();
			beanInfo.setCached(clazz.isAnnotationPresent(CacheNamespace.class));
			beanInfo.setLogged(!clazz.isAnnotationPresent(NotLogged.class));
			//beanInfo.setChineseName(clazz.getSimpleName());
			
			if(clazz.isAnnotationPresent(OrderBy.class)){
				beanInfo.setOrderBy(clazz.getAnnotation(OrderBy.class).value());
			}
			
			beanInfo.setProperties(initPropertyInfos(clazz));
			beanInfoMappings.put(clazz, beanInfo);
		}
		return beanInfo;
	}
	
	private static <T> List<PropertyInfo> initPropertyInfos(Class<T> clazz){
		//final List<String> fields = new ArrayList<String>();
		
		final List<Field> fields = new ArrayList<Field>();
		
		List<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();
		ReflectionUtils.doWithFields(clazz, new FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException,
					IllegalAccessException {
				//fields.add(field.getName());
				fields.add(field);
				//System.out.println(field.getName());
			}
		});		
		TableName table =  clazz.getAnnotation(TableName.class);
		for(Field fieldName : fields){
			PropertyDescriptor prop = BeanUtils.getPropertyDescriptor(clazz, fieldName.getName());
			if(prop == null&&table!=null) continue;
			Method readMethod = null;
			Method writeMethod = null;
			if(prop != null)
			{
				 readMethod = prop.getReadMethod();
				 writeMethod = prop.getWriteMethod();
			}
			if(readMethod == null || writeMethod ==null)
			{
				if(readMethod==null&&writeMethod==null&&table==null)
				{
				PropertyInfo propertyInfo = new PropertyInfo();
				propertyInfo.setPropertyName(fieldName.getName());		
				
				propertyInfo.setReturnType(fieldName.getType());
				propertyInfo.setField(fieldName);
				propertyInfos.add(propertyInfo);
				}else
				{
				continue;
				}
			}else
			{
			if(!isCollectionType(readMethod)){
				PropertyInfo propertyInfo = new PropertyInfo();
				propertyInfo.setPropertyName(ClassUtils.getPropertyName(readMethod));
				propertyInfo.setReadMethod(readMethod);
				
				propertyInfo.setReturnType(readMethod.getReturnType());
				propertyInfos.add(propertyInfo);
			}}
		}
		propertyInfos = Collections.unmodifiableList(propertyInfos);
		return propertyInfos;		
		
	}
	

	
	private static boolean isCollectionType(Method m){
		boolean result = false;
		if(m.getReturnType().isPrimitive())
			return true;
		if(Collection.class.isAssignableFrom(m.getReturnType())){
			result = true;
		}else if(Map.class.isAssignableFrom(m.getReturnType())){
			result = true;
		}
		return result;
	}	
}
