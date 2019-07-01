package net.onebean.core.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public class OperationLogInterceptor implements MethodInterceptor{

	@SuppressWarnings("unused")
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
		Method method = invocation.getMethod();
		Object proxy = ((ReflectiveMethodInvocation) invocation).getProxy(); 
		Class<?> class1 = method.getDeclaringClass();
		Type t = (ParameterizedType)class1.getGenericSuperclass();
        if(t!=null && t instanceof ParameterizedType) {
            Type[] args2 = ((ParameterizedType) t).getActualTypeArguments();
            if(args2!=null && args2.length>0) {
            }
        }
		return invocation.proceed();
	}
	
	public void operationLogPoint(){
		org.apache.ibatis.logging.LogFactory.useSlf4jLogging();
	}
}
