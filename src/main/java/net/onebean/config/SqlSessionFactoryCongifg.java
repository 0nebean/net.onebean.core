package net.onebean.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.eakay.core.extend.DynamicMapperSqlSessionFactoryBean;
import com.eakay.core.extend.LogSQLExcutionTimeInterceptor;
import com.eakay.core.extend.PaginationInterceptor;
import com.eakay.util.PropUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * SqlSessionFactory的自定义实现
 * @author 0neBean
 */
@Configuration
public class SqlSessionFactoryCongifg {

    /**
     * 实例化自定义的sqlSessionFactory
     * @param dataSource 数据源
     * @return 返回一个sqlSessionFactory实例
     * @throws Exception 抛出异常
     */
    @Bean(name = "com.eakay.core.extend.DynamicMapperSqlSessionFactoryBean")
    public DynamicMapperSqlSessionFactoryBean sqlSessionFactory(@Qualifier("dataSource") DruidDataSource dataSource) throws Exception{
        DynamicMapperSqlSessionFactoryBean sqlSessionFactory = new DynamicMapperSqlSessionFactoryBean();
        //设置数据源
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setConfigLocation(new PathMatchingResourcePatternResolver().getResources(PropUtil.getInstance().getConfig("org.mybaits.config.path", PropUtil.PUBLIC_CONF_JDBC))[0]);
        //设置扫描业务Mapper和抽象Mapper的路径
        Resource[] mls1 = new PathMatchingResourcePatternResolver().getResources(PropUtil.getInstance().getConfig("org.mybaits.base.mapper.path", PropUtil.PUBLIC_CONF_JDBC));
        Resource [] mls2 = new PathMatchingResourcePatternResolver().getResources(PropUtil.getInstance().getConfig("org.mybaits.bussines.mapper.path", PropUtil.PUBLIC_CONF_JDBC));
        sqlSessionFactory.setMapperLocations((Resource [])ArrayUtils.addAll(mls1,mls2));
        //设置自定的'mbatis'的自定义插件
        Interceptor[] plugins = new Interceptor[2];
        //增加'mbatis'的sql执行施加统计插件
        Interceptor plg1 = new LogSQLExcutionTimeInterceptor();
        //增加'mbatis'的分页插件
        Interceptor plg2 = new PaginationInterceptor();
        plugins[0] = plg1;
        plugins[1] = plg2;
        sqlSessionFactory.setPlugins(plugins);
        return sqlSessionFactory;
    }
}
