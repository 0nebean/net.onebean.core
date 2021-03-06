package net.onebean.util;


import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * class类操作工具类
 * @author 0neBean
 */
public class ClassUtils {


	private static Logger logger = (Logger) LoggerFactory.getLogger(ClassUtils.class);
	public final static String CURRENT_OPERATING_SYSTEM_SEPARATOR = System.getProperty("file.separator");
	public final static String WINDOWS_SEPARATOR = "\\";
	public final static String LINUX_SEPARATOR = "/";
	public final static String OPERATING_SYSTEM_PLATFORM_WINDOWS = "0";
	public final static String OPERATING_SYSTEM_PLATFORM_LINUX = "1";

	public static String getLowerFirstLetterSimpleClassName(String className) {
		if (StringUtils.isEmpty(className))
			return "";
		String[] parts = className.split("\\.");
		String result = parts[parts.length - 1];
		return result.substring(0, 1).toLowerCase()
				+ (result.length() > 1 ? result.substring(1) : "");
	}

	/**
	 * 获取顶层调用者class
	 * @return class
	 */
	public static String getBasicCallerClassName() {
		StackTraceElement[] es = Thread.currentThread().getStackTrace();
		return es[es.length - 1].getClassName();
	}

	/**
	 * 1.获取属性名称的简单名，如果属性名称是entity.property，那么将返回property 2.如何输入的名称有 [ 标识
	 * 说明是查询字段，原样返回即可 ，不做任何处理
	 * 
	 * @param fullName
	 *            属性名称
	 * @return 处理后的属性名称
	 */
	public static String getSimplePropertyName(String fullName) {

		if (!StringUtils.isTrimEmpty(fullName) && fullName.indexOf("[") != -1) {
			// 如果字段含有 [ 说明是查询字段，直接返回
			return fullName;
		}

		if (!StringUtils.isTrimEmpty(fullName) && fullName.indexOf(".") != -1) {// 检查要获取的属性名是否含有.
			// 检查.是否是最后一个字符
			if (fullName.lastIndexOf(".") != fullName.length()) {
				fullName = fullName.substring(fullName.lastIndexOf(".") + 1);
			}
		}

		return fullName;
	}


	/**
	 * 获取 get 方法对应的属性名
	 * @param readMethod get方法名
	 * @return 方法对应的属性名
	 */
	public static String getPropertyName(Method readMethod) {
		String methodName = readMethod.getName();
		int getPosition = methodName.indexOf("get");
		if (getPosition == -1) {
			throw new RuntimeException(methodName + " 不是 以get开关的方法");
		}
		return getLowerFirstLetterSimpleClassName(methodName
				.substring(getPosition + 3));
	}

	/**
	 * 根据字符串转化成ID 1.如果属性名称是entity.property，那么将返回property 2.如何输入的名称有 [ 标识 说明是查询字段
	 * 如 ：conditions['date_date_gt'].value 用 split("'") 截断 返回第二个
	 * 
	 * @param fullName
	 *            属性名称
	 * @return 处理后的属性名称
	 */
	public static String changeToId(String fullName) {
		if (!StringUtils.isTrimEmpty(fullName) && fullName.indexOf("[") != -1) {
			// 如果字段含有 [ 说明是查询字段，直接返回
			String[] ids = fullName.split("'");
			if (ids.length == 3)
				return ids[1];
			return fullName;
		}

		if (!StringUtils.isTrimEmpty(fullName) && fullName.indexOf(".") != -1) {// 检查要获取的属性名是否含有.
			// 检查.是否是最后一个字符
			if (fullName.lastIndexOf(".") != fullName.length()) {
				fullName = fullName.substring(fullName.lastIndexOf(".") + 1);
			}
		}

		return fullName;
	}

	/**
	 * 通过className 获取class
	 * @param className 类名
	 * @return class
	 */
	public static Class<?> getClassByClassName(String className){
		Class<?> aClass = null;
		try {
			aClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return aClass;
	}

	/**
	 * 判断类似否指定名字的注解
	 * @param className 类型
	 * @param annotationClassName 注解名类型
	 * @return bool
	 */
	public static boolean hasAnnotation(String className, String annotationClassName) {
		Class<?> clazz = getClassByClassName(className);
		return hasAnnotation(clazz,annotationClassName);
	}

	/**
	 * 判断类似否指定名字的注解
	 * @param clazz 类型
	 * @param annotationClassName 注解名类型
	 * @return bool
	 */
	public static boolean hasAnnotation(Class<?> clazz, String annotationClassName) {

		Annotation[] annots = clazz.getAnnotations();
		for (Annotation anno : annots) {

			Class<?> annoclazz = anno.annotationType();
			String annName = annoclazz.getSimpleName();
			if (annName.equalsIgnoreCase(annotationClassName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取指定注解的注解值
	 * @param clazz 类型
	 * @param annotationClassName 注解名类型
	 * @param annoField 字段
	 * @return 注解的值
	 */
	public static Object getAnnotationValue(Class<?> clazz, String annotationClassName, String annoField) {
		Annotation[] annots = clazz.getAnnotations();
		for (Annotation anno : annots) {
			Class<?> annoclazz = anno.annotationType();
			String annName = annoclazz.getSimpleName();
			if (annName.equalsIgnoreCase(annotationClassName)) {

				try {
					Method m = anno.getClass().getDeclaredMethod(annoField,
							(Class<?>) null);
					try {
						if (m != null) {
							return m.invoke(anno, (Object) null);
						}
						break;
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		return null;
	}


	/**
	 * 获取方法上注解的值
	 * @param method 方法名
	 * @param annotationClassName 注解的class
	 * @param annoField 注解字段
	 * @return 注解值
	 */
	public static Object getMethodAnnotationValue(Method method, String annotationClassName, String annoField) {

		Annotation[] annots = method.getAnnotations();
		for (Annotation anno : annots) {
			Class<?> annoclazz = anno.annotationType();
			String annName = annoclazz.getSimpleName();
			if (annName.equalsIgnoreCase(annotationClassName)) {

				try {
					Method m = anno.getClass().getDeclaredMethod(annoField, (Class<?>) null);
					try {
						if (m != null) {
							return m.invoke(anno, (Object) null);
						}
						break;
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}

				} catch (NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		return null;
	}

	/**
	 * 获取属性上注解的值
	 * @param field 字段
	 * @param annotationClassName 注解的class
	 * @param annoField 注解字段
	 * @return 注解值
	 */
	public static Object getFieldAnnotationValue(Field field, String annotationClassName, String annoField) {
		Annotation[] annots = field.getAnnotations();
		for (Annotation anno : annots) {
			Class<?> annoclazz = anno.annotationType();
			String annName = annoclazz.getSimpleName();
			if (annName.equalsIgnoreCase(annotationClassName)) {

				try {
					Method m = anno.getClass().getDeclaredMethod(annoField,
							(Class<?>) null);
					try {
						if (m != null) {
							return m.invoke(anno, (Object) null);
						}
						break;
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		return null;
	}



	/**
	 * 将数据对象转换成map
	 * @param obj 对象
	 * @return map
	 */
	public static Map<String, Object> toMap(Object obj) {
		// 创建新map
		Map<String, Object> rs = new HashMap<String, Object>();
		return appendMap(rs, obj);
	}


	/**
	 * 将数据对象合并的map
	 * @param rs 结果map
	 * @param obj 数据对象
	 * @return map
	 */
	public static Map<String, Object> appendMap(Map<String, Object> rs,
			Object obj) {
		return appendMap(rs, obj, null);
	}

	/**
	 * 将数据对象合并的map
	 * @param rs 结果map
	 * @param obj 数据对象
	 * @param defaultValue 如果对象值为Null，默认填充的值
	 * @return map
	 */
	public static Map<String, Object> appendMap(Map<String, Object> rs,
			Object obj, Object defaultValue) {
		// 获取所有属性
		Field[] array = obj.getClass().getDeclaredFields();
		// 遍历取值
		for (Field f : array) {
			// 首先设置为属性可读
			f.setAccessible(true);
			try {
				Object value = f.get(obj);
				// 取值
				rs.put(f.getName(), null == value ? defaultValue : value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return rs;
	}

	/**
	 * 获取classpath 的绝对路径 需要配合启动脚本启动
	 * 如果该方法运行在classpath内 返回 '.'
	 * @return path
	 */
	public static String getAbsoluteClassPath(){
		String absoluteClassPath = "";
		try {
			/*linux sh startup*/
			absoluteClassPath = new File("").getCanonicalPath();
		} catch (IOException e) {
			logger.error("Could not load properties from path");
		}
		/*windows bat startup*/
		if (absoluteClassPath.endsWith(CURRENT_OPERATING_SYSTEM_SEPARATOR + "bin")) {
			absoluteClassPath = absoluteClassPath.substring(0, absoluteClassPath.lastIndexOf(CURRENT_OPERATING_SYSTEM_SEPARATOR));
		}
		/*idea startup*/
		if (!absoluteClassPath.endsWith(CURRENT_OPERATING_SYSTEM_SEPARATOR + "bin") && !absoluteClassPath.endsWith(CURRENT_OPERATING_SYSTEM_SEPARATOR + "server")) {
			absoluteClassPath = ".";
		}
		return absoluteClassPath;
	}

	/**
	 * 传入路径，返回是否是绝对路径，是绝对路径返回true，反之
	 * @param path 路径
	 * @return bool
	 */
	public static boolean isAbsolutePath(String path) {
		return path.startsWith("/") || path.indexOf(":") > 0;
	}


	/**
	 * 获取作业系统平台
	 * @return string
	 */
	public static String getOperatingSystemPlatform(){
		if (CURRENT_OPERATING_SYSTEM_SEPARATOR.equals(WINDOWS_SEPARATOR)){
			return OPERATING_SYSTEM_PLATFORM_WINDOWS;
		}else if(CURRENT_OPERATING_SYSTEM_SEPARATOR.equals(LINUX_SEPARATOR)){
			return OPERATING_SYSTEM_PLATFORM_LINUX;
		}
		return null;
	}

	/**
	 * 判断对象属性是否是基本数据类型,包括是否包括string
	 * @param className 类名
	 * @param incString 是否包括string判断,如果为true就认为string也是基本数据类型
	 * @return bool
	 */
	public static boolean isBaseType(Class<?> className, boolean incString) {
		if (incString && className.equals(String.class)) {
			return true;
		}
		return className.equals(Integer.class) ||
				className.equals(int.class) ||
				className.equals(Byte.class) ||
				className.equals(byte.class) ||
				className.equals(Long.class) ||
				className.equals(long.class) ||
				className.equals(Double.class) ||
				className.equals(double.class) ||
				className.equals(Float.class) ||
				className.equals(float.class) ||
				className.equals(Character.class) ||
				className.equals(char.class) ||
				className.equals(Short.class) ||
				className.equals(short.class) ||
				className.equals(Boolean.class) ||
				className.equals(boolean.class);
	}
}
