package net.onebean.util;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/** 
 * 反射工具类. 
 *  
 * 提供访问私有变量,获取泛型类型Class, 提取集合中元素的属性, 转换字符串到对象等Util函数. 
 * @author 0neBean
 *  
 */  
public abstract class ReflectionUtils {
  
    private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    /**
     * 调用Getter方法.
     * @param obj 调用对象
     * @param propertyName 属性名
     * @return 属性值
     */
    public static Object invokeGetterMethod(Object obj, String propertyName) {  
        String getterMethodName = "get" + StringUtils.capitalize(propertyName);  
        return invokeMethod(obj, getterMethodName, new Class[] {}, new Object[] {});  
    }

    /**
     * 调用Setter方法.使用value的Class来查找Setter方法.
     * @param obj 调用对象
     * @param propertyName 属性名
     * @param value 为空用value的class代替
     */
    public static void invokeSetterMethod(Object obj, String propertyName, Object value) {  
        invokeSetterMethod(obj, propertyName, value, null);  
    }  
  

    /**
     * 调用Setter方法.
     * @param obj 调用对象
     * @param propertyName 属性名 用于查找Setter方法,为空时使用value的Class替代.
     * @param value 为空用value的class代替
     * @param propertyType 属性类型
     */
    public static void invokeSetterMethod(Object obj, String propertyName, Object value, Class<?> propertyType) {  
        Class<?> type = propertyType != null ? propertyType : value.getClass();  
        String setterMethodName = "set" + StringUtils.capitalize(propertyName);  
        invokeMethod(obj, setterMethodName, new Class[] { type }, new Object[] { value });  
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     * @param obj 调用对象
     * @param fieldName 字段名
     * @return 属性值
     */
    public static Object getFieldValue(final Object obj, final String fieldName) {  
        Field field = getAccessibleField(obj, fieldName);  
  
        if (field == null) {  
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");  
        }  
  
        Object result = null;  
        try {  
            result = field.get(obj);  
        } catch (IllegalAccessException e) {  
            logger.error("不可能抛出的异常{}", e.getMessage());  
        }  
        return result;  
    }


    /**
     * 直接读取对象的成员变量, 无视private/protected修饰符.
     * @param obj 调用对象
     * @param fieldName 字段名
     * @return 字段
     */
    public static Field getField(final Object obj, final String fieldName) {
        return getAccessibleField(obj, fieldName);
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     * @param obj 调用对象
     * @param fieldName 字段名
     * @param value 为空用value的class代替
     */
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {  
        Field field = getAccessibleField(obj, fieldName);  
  
        if (field == null) {  
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");  
        }  
  
        try {  
            field.set(obj, value);  
        } catch (IllegalAccessException e) {  
            logger.error("不可能抛出的异常:{}", e.getMessage());  
        }  
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField,   并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * @param obj 调用对象
     * @param fieldName 字段名
     * @return 字段
     */
    public static Field getAccessibleField(final Object obj, final String fieldName) {  
        Assert.notNull(obj, "object不能为空");  
        Assert.hasText(fieldName, "fieldName");  
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {  
            try {  
                Field field = superClass.getDeclaredField(fieldName);  
                field.setAccessible(true);  
                return field;  
            } catch (NoSuchFieldException e) {//NOSONAR  
                // Field不在当前类定义,继续向上转型  
            }  
        }  
        return null;  
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符.
     * 用于一次性调用的情况.
     * @param obj 调用对象
     * @param methodName 方法名
     * @param parameterTypes 参数类型
     * @param args 参数
     * @return 方法返回值
     */
    public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,  
            final Object[] args) {  
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);  
        if (method == null) {  
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");  
        }  
  
        try {  
            return method.invoke(obj, args);  
        } catch (Exception e) {  
            throw convertReflectionExceptionToUnchecked(e);  
        }  
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     * @param obj 调用对象
     * @param methodName 方法名
     * @param parameterTypes 参数类型
     * @return 方法对象
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName,  
            final Class<?>... parameterTypes) {  
        Assert.notNull(obj, "object不能为空");  
  
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {  
            try {  
                Method method = superClass.getDeclaredMethod(methodName, parameterTypes);  
  
                method.setAccessible(true);  
  
                return method;  
  
            } catch (NoSuchMethodException e) {//NOSONAR  
                // Method不在当前类定义,继续向上转型  
            }  
        }  
        return null;  
    }  
  

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * 如无法找到, 返回Object.class.
     * eg.
     * public UserDao extends HibernateDao《User》
     * @param clazz The class to introspect
     * @param <T> 泛型类型
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })  
    public static <T> Class<T> getSuperClassGenricType(final Class clazz) {  
        return getSuperClassGenricType(clazz, 0);  
    }  
  
    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * 如无法找到, 返回Object.class.
     * 如public UserDao extends HibernateDao《User,Long》
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("rawtypes")
    public static Class getSuperClassGenricType(final Class clazz, final int index) {  
  
        Type genType = clazz.getGenericSuperclass();  
  
        if (!(genType instanceof ParameterizedType)) {  
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");  
            return Object.class;  
        }  
  
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
  
        if (index >= params.length || index < 0) {  
            logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "  
                    + params.length);  
            return Object.class;  
        }  
        if (!(params[index] instanceof Class)) {  
            logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");  
            return Object.class;  
        }  
  
        return (Class) params[index];  
    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     * @param e 异常
     * @return 运行异常
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {  
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException  
                || e instanceof NoSuchMethodException) {  
            return new IllegalArgumentException("Reflection Exception.", e);  
        } else if (e instanceof InvocationTargetException) {  
            return new RuntimeException("Reflection Exception.", ((InvocationTargetException) e).getTargetException());  
        } else if (e instanceof RuntimeException) {  
            return (RuntimeException) e;  
        }  
        return new RuntimeException("Unexpected Checked Exception.", e);  
    }

    /**
     * 得到指定类型的指定位置的泛型实参
     * @param clazz 类型
     * @param index 下标
     * @param <T> 泛型类型
     * @return 实参
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> findParameterizedType(Class<?> clazz, int index) {
        Type parameterizedType = clazz.getGenericSuperclass();
        //CGLUB subclass target object(泛型在父类上)
        if (!(parameterizedType instanceof ParameterizedType)) {
            parameterizedType = clazz.getSuperclass().getGenericSuperclass();
        }
        if (!(parameterizedType instanceof  ParameterizedType)) {
            return null;
        }
        Type[] actualTypeArguments = ((ParameterizedType) parameterizedType).getActualTypeArguments();
        if (actualTypeArguments == null || actualTypeArguments.length == 0) {
            return null;
        }
        return (Class<T>) actualTypeArguments[index];
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClass(String className)
    {
        try
        {
            return (Class<T>)Class.forName(className);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> Object newInstance(String className)
    {
        try
        {
            return Class.forName(className).newInstance();
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return null;
    }



    /**
     * 从request中取出请求参数，把这些参数对应反射为JavaBean对象
     * @param request 请求
     * @param cls 类型
     * @param <T> 泛型对象
     * @return 参数对象
     * @throws InstantiationException 抛出各种异常
     * @throws IllegalAccessException 抛出各种异常
     * @throws UnsupportedEncodingException 抛出各种异常
     * @throws IllegalArgumentException 抛出各种异常
     * @throws InvocationTargetException 抛出各种异常
     */
    public static <T> T getParamFromRequest(HttpServletRequest request, Class<T> cls) throws InstantiationException, IllegalAccessException, UnsupportedEncodingException, IllegalArgumentException, InvocationTargetException {
        request.setCharacterEncoding("utf-8");//设置请求(request)的编码集
        T t=cls.newInstance();
        Map<String, String[]> parameterMap=request.getParameterMap();//获取所有请求参数
        List<Method> objectSetMethods=getObjectSetMethods(cls);//获取JavaBean中所有的set方法
        String key="";
        String value="";
        for(Map.Entry<String,String[]> parameter:parameterMap.entrySet()){
            key="set"+parameter.getKey();
            String[] values =parameter.getValue();
            if(values!=null && values.length>0){
                value=values[0];
            }
            for(Method method:objectSetMethods){
                if(key.equalsIgnoreCase(method.getName())){
                    Class c=method.getParameterTypes()[0];
                    String parameterType=c.getTypeName();
                    if (!"".equals(value)) {
                        if ("int".equals(parameterType) || "java.lang.Integer".equals(parameterType)) {
                            int v = Integer.parseInt(value);
                            method.invoke(t, v);
                        } else if ("float".equals(parameterType) || "java.lang.Float".equals(parameterType)) {
                            float v = Float.parseFloat(value);
                            method.invoke(t, v);
                        } else if ("double".equals(parameterType) || "java.lang.Double".equals(parameterType)) {
                            double v = Double.parseDouble(value);
                            method.invoke(t, v);
                        } else if("short".equals(parameterType) || "java.lang.Short".equals(parameterType)){
                            short v = Short.parseShort(value);
                            method.invoke(t, v);
                        } else if("long".equals(parameterType) || "java.lang.Long".equals(parameterType)){
                            long v = Long.parseLong(value);
                            method.invoke(t, v);
                        } else if("BigDecimal".equals(parameterType) || "java.math.BigDecimal".equals(parameterType)){
                            Number v = new BigDecimal(value+"");
                            method.invoke(t, v);
                        } else if("Timestamp".equals(parameterType) || "java.sql.Timestamp".equals(parameterType)){
                            Date v = DateUtils.stringToTimeStamp(value);
                            method.invoke(t, v);
                        }else {
                            method.invoke(t, value);
                        }
                    }
                }
            }
        }
        return t;
    }


    /**
     * 获取对象(JavaBean)的全部set方法
     * @param cls 对象(JavaBean)
     * @return List<Method>
     */
    private static <T> List<Method> getObjectSetMethods(Class<T> cls){
        List<Method> setMethods = new ArrayList<>();
        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                setMethods.add(method);
            }
        }
        return setMethods;
    }

    /**
     * 获取接口上的泛型T
     * @param o     接口
     * @param index 泛型索引
     */
    public static Class<?> getInterfaceT(Object o, int index) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[index];
        Type type = parameterizedType.getActualTypeArguments()[index];
        return checkType(type, index);

    }


    /**
     * 获取类上的泛型T
     * @param o     接口
     * @param index 泛型索引
     */
    public static Class<?> getClassT(Object o, int index) {
        Type type = o.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type actType = parameterizedType.getActualTypeArguments()[index];
            return checkType(actType, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType" + ", but <" + type + "> is of type " + className);
        }
    }

    /**
     * 检查类型
     * @param type 类型
     * @param index 下标
     * @return Class
     */
    private static Class<?> checkType(Type type, int index) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type t = pt.getActualTypeArguments()[index];
            return checkType(t, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType"
                    + ", but <" + type + "> is of type " + className);
        }
    }
}