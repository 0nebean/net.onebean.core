package net.onebean.core.extend;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * 记录每条SQL 的运行时长
 *
 */
//只拦截select部分
@Intercepts( {@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
      RowBounds.class, ResultHandler.class})})
public class LogSQLExcutionTimeInterceptor implements  Interceptor {
	private final static Log log = LogFactory.getLog(LogSQLExcutionTimeInterceptor.class);
	private static final Long DEFAULT_ZERO = new Long(0);

	public Object intercept(Invocation invocation) throws Throwable {
		Long now = DEFAULT_ZERO;
		if(log.isDebugEnabled())
			now = System.currentTimeMillis();
		Object result = invocation.proceed();
		if(log.isDebugEnabled()){
			log.debug("*** total time is " + ( System.currentTimeMillis() - now));
		}		
		return result;
	}
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
	public void setProperties(Properties properties) {
		
	}

}
