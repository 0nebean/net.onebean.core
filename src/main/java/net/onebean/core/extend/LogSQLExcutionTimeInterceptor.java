package net.onebean.core.extend;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

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
