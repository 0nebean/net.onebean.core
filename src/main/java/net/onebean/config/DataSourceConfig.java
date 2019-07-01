package net.onebean.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.eakay.core.form.Parse;
import com.eakay.util.PropUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * druid数据库连接池配置
 * @author 0neBean
 */
@Configuration
public class DataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    /**
     * 配置监控服务器
     *
     * @return 返回监控注册的servlet对象
     */
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // 添加IP白名单
        servletRegistrationBean.addInitParameter("allow", PropUtil.getInstance().getConfig("com.alibaba.druid.ip.allow", PropUtil.PUBLIC_CONF_JDBC));
        // 添加IP黑名单，当白名单和黑名单重复时，黑名单优先级更高
        servletRegistrationBean.addInitParameter("deny", PropUtil.getInstance().getConfig("com.alibaba.druid.ip.deny", PropUtil.PUBLIC_CONF_JDBC));
        // 添加控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername", PropUtil.getInstance().getConfig("com.alibaba.druid.username", PropUtil.PUBLIC_CONF_JDBC));
        servletRegistrationBean.addInitParameter("loginPassword", PropUtil.getInstance().getConfig("com.alibaba.druid.password", PropUtil.PUBLIC_CONF_JDBC));
        // 是否能够重置数据
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        //慢sql日志
        servletRegistrationBean.addInitParameter("logSlowSql", PropUtil.getInstance().getConfig("com.alibaba.druid.logSlowSql", PropUtil.PUBLIC_CONF_JDBC));
        return servletRegistrationBean;
    }

    /**
     * 配置服务过滤器
     *
     * @return 返回过滤器配置对象
     */
    @Bean
    public FilterRegistrationBean statFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // 添加过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        // 忽略过滤格式
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*,");
        return filterRegistrationBean;
    }


    /**
     * 数据库连接池
     * @return 返回数据库连接池实例
     */
    @Bean(name = "dataSource")
    public DruidDataSource druidDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(PropUtil.getInstance().getConfig("spring.datasource.url", PropUtil.PUBLIC_CONF_JDBC));
        datasource.setUsername(PropUtil.getInstance().getConfig("spring.datasource.username", PropUtil.PUBLIC_CONF_JDBC));
        datasource.setPassword(PropUtil.getInstance().getConfig("spring.datasource.password", PropUtil.PUBLIC_CONF_JDBC));
        datasource.setDriverClassName(PropUtil.getInstance().getConfig("spring.datasource.driver-class-name", PropUtil.PUBLIC_CONF_JDBC));
        //初始化大小，最小，最大
        datasource.setInitialSize(Parse.toInt(PropUtil.getInstance().getConfig("spring.datasource.initialSize", PropUtil.PUBLIC_CONF_JDBC)));
        datasource.setMinIdle(Parse.toInt(PropUtil.getInstance().getConfig("spring.datasource.minIdle", PropUtil.PUBLIC_CONF_JDBC)));
        datasource.setMaxActive(Parse.toInt(PropUtil.getInstance().getConfig("spring.datasource.maxActive", PropUtil.PUBLIC_CONF_JDBC)));
        //配置获取连接等待超时的时间
        datasource.setMaxWait(Parse.toLong(PropUtil.getInstance().getConfig("spring.datasource.maxWait", PropUtil.PUBLIC_CONF_JDBC)));
        //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        datasource.setTimeBetweenEvictionRunsMillis(Parse.toLong(PropUtil.getInstance().getConfig("spring.datasource.timeBetweenEvictionRunsMillis", PropUtil.PUBLIC_CONF_JDBC)));
        //配置一个连接在池中最小生存的时间，单位是毫秒
        datasource.setMinEvictableIdleTimeMillis(Parse.toLong(PropUtil.getInstance().getConfig("spring.datasource.minEvictableIdleTimeMillis", PropUtil.PUBLIC_CONF_JDBC)));
        datasource.setValidationQuery(PropUtil.getInstance().getConfig("spring.datasource.validationQuery", PropUtil.PUBLIC_CONF_JDBC));
        datasource.setTestWhileIdle(Parse.toBoolean(PropUtil.getInstance().getConfig("spring.datasource.testWhileIdle", PropUtil.PUBLIC_CONF_JDBC)));
        datasource.setTestOnBorrow(Parse.toBoolean(PropUtil.getInstance().getConfig("spring.datasource.testOnBorrow", PropUtil.PUBLIC_CONF_JDBC)));
        datasource.setTestOnReturn(Parse.toBoolean(PropUtil.getInstance().getConfig("spring.datasource.testOnReturn", PropUtil.PUBLIC_CONF_JDBC)));
        //打开PSCache，并且指定每个连接上PSCache的大小
        datasource.setPoolPreparedStatements(Parse.toBoolean(PropUtil.getInstance().getConfig("spring.datasource.poolPreparedStatements", PropUtil.PUBLIC_CONF_JDBC)));
        datasource.setMaxPoolPreparedStatementPerConnectionSize(Parse.toInt(PropUtil.getInstance().getConfig("spring.datasource.maxPoolPreparedStatementPerConnectionSize", PropUtil.PUBLIC_CONF_JDBC)));
        WallConfig wallConfig = new WallConfig();
        wallConfig.setMultiStatementAllow(true);
        WallFilter wallFilter = new WallFilter();
        wallFilter.setConfig(wallConfig);
        List<Filter> filters = new ArrayList<>();
        filters.add(wallFilter);
        datasource.setProxyFilters(filters);
        try {
            //配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
            datasource.setFilters(PropUtil.getInstance().getConfig("spring.datasource.filters", PropUtil.PUBLIC_CONF_JDBC));
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }
        return datasource;
    }

}
