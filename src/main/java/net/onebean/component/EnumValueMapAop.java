package net.onebean.component;

import net.onebean.core.Json.EnableEnumDeserialize;
import net.onebean.core.Json.EnumDeserialize;
import net.onebean.util.CollectionUtil;
import net.onebean.util.ReflectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@Configuration
@Aspect
public class EnumValueMapAop {

    private final String ExpGetResultDataPonit = "execution(* net.onebean..action..*.*(..))) && @annotation(enableEnumDeserialize)";

    /**
     * 环绕aop 设置枚举映射值
     * @param proceedingJoinPoint 切入点
     * @param enableEnumDeserialize 开启枚举映射注解
     * @return obj
     * @throws Throwable 抛出异常
     */
    @Around(value = ExpGetResultDataPonit)
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint, EnableEnumDeserialize enableEnumDeserialize) throws Throwable {
        List<String> keys = CollectionUtil.stringArr2List(enableEnumDeserialize.key());
        Object result = proceedingJoinPoint.proceed();

        Object data = null;
        if (CollectionUtil.isNotEmpty(keys)){
            data = result;
            for (String key : keys) {
                data = ReflectionUtils.invokeGetterMethod(data,key);
            }
        }

        if (null != data){
            if(data instanceof List){
                @SuppressWarnings("unchecked")
                List<Object> res  = (List<Object>) data;
                for (Object re : res) {
                    invokeObject(re);
                }
            }else{
                invokeObject(data);
            }
        }

        return result;
    }

    /**
     * 通过反射设置枚举值
     * @param target 目标
     * @param field 字段
     * @throws Exception 异常
     */
    private void setEnumValue(Object target,Field field) throws Exception{
        // 判断字段上是否有次注解
        if (field.isAnnotationPresent(EnumDeserialize.class)) {
            // 获取注解
            EnumDeserialize fieldAnnotation = field.getAnnotation(EnumDeserialize.class);
            Class<?> enumClz = fieldAnnotation.using();
            String value = ReflectionUtils.invokeGetterMethod(target,field.getName()).toString();
            Method getValueByKeyMethod = enumClz.getMethod("getValueByKey",String.class);
            Object enumValue = getValueByKeyMethod.invoke(null,value);
            ReflectionUtils.setFieldValue(target,field.getName(),enumValue);
        }
    }

    /**
     * 反射对象找到有枚举映射注解的值
     * @param target 目标对象
     * @throws Exception 异常
     */
    private void invokeObject(Object target) throws Exception{
        Class clz = target.getClass();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            //判断字段是否是list 嵌套对象
            if(field.getType().isAssignableFrom(List.class)){
                @SuppressWarnings("unchecked")
                List <Object> fieldList = (List <Object>) ReflectionUtils.invokeGetterMethod(target,field.getName());
                if (CollectionUtil.isNotEmpty(fieldList)){
                    for (Object f : fieldList) {
                        invokeObject(f);
                    }
                }
            }else {
                //设置字段反射
                setEnumValue(target,field);
            }
        }
    }


}