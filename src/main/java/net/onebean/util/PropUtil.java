package net.onebean.util;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.io.Resources;

import java.io.FileOutputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件读取类
 * @author 0neBean
 */
public class PropUtil {

    /**
     * 当前对象实例
     */
    private static PropUtil propUtil = new PropUtil();
    /**
     *  文件名
     */
    private static String fileName = "application.properties";
    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader loader = new PropertiesLoader(fileName);
    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = new HashMap<String, String>();

    /**
     * 获取当前对象实例
     */
    public static PropUtil getInstance() {
        return propUtil;
    }

    /**
     * 获取配置
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null){
            value = loader.getProperty(key);
            map.put(key, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }

    /**
     * 写入properties信息
     *
     * @param key 名称
     * @param value 值
     */
    public static void modifyConfig(String key, String value) {
        try {
            // 从输入流中读取属性列表（键和元素对）
            Properties prop = getProperties();
            prop.setProperty(key, value);
            String path = PropUtil.class.getResource("/"+fileName).getPath();
            FileOutputStream outputFile = new FileOutputStream(path);
            prop.store(outputFile, "modify");
            outputFile.close();
            outputFile.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回　Properties
     * @param
     * @return
     */
    public static Properties getProperties(){
        Properties prop = new Properties();
        try {
            Reader reader = Resources.getResourceAsReader("/"+fileName);
            prop.load(reader);
        } catch (Exception e) {
            return null;
        }
        return prop;
    }
}
