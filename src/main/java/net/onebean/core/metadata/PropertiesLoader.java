package net.onebean.core.metadata;

import ch.qos.logback.classic.Logger;
import net.onebean.util.ClassUtils;
import net.onebean.util.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * Properties文件载入工具类. 可载入多个properties文件,
 * 相同的属性在最后载入的文件中的值将会覆盖之前的值，但以System的Property优先.
 * @author World
 */
public class PropertiesLoader {

    private static Logger logger = (Logger) LoggerFactory.getLogger(PropertiesLoader.class);
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    private final Properties properties;

    public PropertiesLoader(String... resourcesPaths) {
        properties = loadProperties(resourcesPaths);
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * 取出Property，但以System的Property优先,取不到返回空字符串.
     */
    private String getValue(String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null) {
            return systemProperty;
        }
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        }
        return "";
    }


    /**
     * 取出String类型的Property，但以System的Property优先,如果都为Null则抛出异常.
     * @param key 键
     * @return value
     */
    public String getProperty(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    /**
     * 取出String类型的Property，但以System的Property优先.如果都为Null则返回Default值.
     * @param key 键
     * @param defaultValue 默认值
     * @return value
     */
    public String getProperty(String key, String defaultValue) {
        String value = getValue(key);
        return value != null ? value : defaultValue;
    }


    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
     * @param key 键
     * @return value
     */
    public Integer getInteger(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Integer.valueOf(value);
    }

    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
     * @param key 键
     * @param defaultValue 默认值
     * @return value
     */
    public Integer getInteger(String key, Integer defaultValue) {
        String value = getValue(key);
        return value != null ? Integer.valueOf(value) : defaultValue;
    }

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
     * @param key 键
     * @return value
     */
    public Double getDouble(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Double.valueOf(value);
    }

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
     * @param key 键
     * @param defaultValue 默认值
     * @return value
     */
    public Double getDouble(String key, Integer defaultValue) {
        String value = getValue(key);
        return value != null ? Double.valueOf(value) : defaultValue;
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都为Null抛出异常,如果内容不是true/false则返回false.
     * @param key 键
     * @return value
     */
    public Boolean getBoolean(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Boolean.valueOf(value);
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都为Null则返回Default值,如果内容不为true/false则返回false.
     * @param key 键
     * @param defaultValue 默认值
     * @return value
     */
    public Boolean getBoolean(String key, boolean defaultValue) {
        String value = getValue(key);
        return value != null ? Boolean.valueOf(value) : defaultValue;
    }

    /**
     * 载入多个文件, 文件路径使用Spring Resource格式.
     * @param resourcesPaths 配置文件路径
     * @return 配置
     */
    private Properties loadProperties(String... resourcesPaths) {
        Properties props = new Properties();
        String absoluteClassPath = ClassUtils.getAbsoluteClassPath();
        for (String location : resourcesPaths) {
            /*兼容maven-assembly-plugin*/
            if (location.startsWith("public-conf") || location.equals("application.properties")) {
                location = absoluteClassPath + ClassUtils.CURRENT_OPERATING_SYSTEM_SEPARATOR + "config" + ClassUtils.CURRENT_OPERATING_SYSTEM_SEPARATOR + location;
            }
            logger.info("Loading properties file from:" + location);

            InputStream is = null;
            try {
                if (ClassUtils.isAbsolutePath(location)) {
                    is = new FileInputStream(location);
                } else {
                    Resource resource = resourceLoader.getResource(location);
                    is = resource.getInputStream();
                }
                props.load(is);
            } catch (IOException ex) {
                logger.info("Could not load properties from path:" + location + ", " + ex.getMessage());
            } finally {
                IOUtils.closeQuietly(is);
            }
        }
        return props;
    }

}