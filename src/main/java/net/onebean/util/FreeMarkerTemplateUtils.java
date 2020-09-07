package net.onebean.util;

import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSONObject;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import net.onebean.core.error.BusinessException;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author World
 * FreeMarker 操作工具类
 */
public class FreeMarkerTemplateUtils {


    private static Logger logger = (Logger) LoggerFactory.getLogger(FreeMarkerTemplateUtils.class);
    private final static String FREEMARKER_FTL_KEY = "apache.freemarker.ftl.path";
    private final static String CHARSET_STR ="utf-8";

    private FreeMarkerTemplateUtils() {
    }

    private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_22);

    public static void main(String[] args) {
        String asas = "\\templates-customer\\ftl";

    }

    static {
        String basePackage = PropUtil.getInstance().getConfig(FREEMARKER_FTL_KEY, PropUtil.PUBLIC_CONF_FREEMARKER);
        String absoluteClassPath = ClassUtils.getAbsoluteClassPath();
        absoluteClassPath = absoluteClassPath.equals(".") ? "" : absoluteClassPath;
        logger.info("un replace separator, basePackage = "+basePackage);
        if (ClassUtils.getOperatingSystemPlatform().equals(ClassUtils.OPERATING_SYSTEM_PLATFORM_WINDOWS)){
            logger.info("replace basePackage separator to windows = "+basePackage);
            basePackage = basePackage.replace(ClassUtils.LINUX_SEPARATOR,ClassUtils.WINDOWS_SEPARATOR);
        }else{
            logger.info("replace basePackage separator to linux = "+basePackage);
            basePackage = basePackage.replace(ClassUtils.WINDOWS_SEPARATOR,ClassUtils.LINUX_SEPARATOR);
        }
        logger.info("replaced separator, basePackage = "+basePackage);
        basePackage = absoluteClassPath + basePackage + ClassUtils.CURRENT_OPERATING_SYSTEM_SEPARATOR;
        logger.info("full path, basePackage = "+basePackage);
        try {
            if (ClassUtils.isAbsolutePath(basePackage)) {
                logger.info("init freeMarker by FileTemplateLoader, basePackage = "+ basePackage);
                CONFIGURATION.setTemplateLoader(new FileTemplateLoader(new File(basePackage)));
            } else {
                logger.info("init freeMarker by ClassTemplateLoader, basePackage = "+ basePackage);
                CONFIGURATION.setTemplateLoader(new ClassTemplateLoader(FreeMarkerTemplateUtils.class, basePackage));
            }
        } catch (IOException e) {
            logger.error("FreeMarker setTemplateLoader err , e = ", e);
        }
        CONFIGURATION.setDefaultEncoding("UTF-8");
        CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        CONFIGURATION.setCacheStorage(NullCacheStorage.INSTANCE);
    }


    /**
     * 获取模板
     * @param templateName 模板路径
     * @return Template
     */
    public static Template getTemplate(String templateName) {
        try {
            return CONFIGURATION.getTemplate(templateName);
        } catch (IOException e) {
            logger.error("getTemplate get an err , e = ",e);
            return null;
        }
    }

    /**
     * 根据模板生成string
     * @param param 参数
     * @param templatePath 模板名字
     * @return string
     */
    public static String generateStringFromFreeMarker(JSONObject param, String templatePath){
        try {
            Map<String,Object> data = new HashMap<>();
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                data.put(entry.getKey(),entry.getValue());
            }
            Template template = FreeMarkerTemplateUtils.getTemplate(templatePath);
            StringWriter out = new StringWriter();
            template.process(data, out);
            return out.getBuffer().toString();
        } catch (Exception e) {
            logger.error("generateStringFromFreeMarker get an err , e = ",e);
            return null;
        }
    }

    /**
     * 生成模板文件
     * @param param 参数
     * @param targetFilePath 目标路径
     * @param templatePath 模板路径
     */
    public static void generateFile(JSONObject param, String targetFilePath, String templatePath){
        File mapperFile = new File(targetFilePath);
        FileOutputStream fos = null;
        Writer out = null;
        try {
            fos = new FileOutputStream(mapperFile);
            out = new BufferedWriter(new OutputStreamWriter(fos, CHARSET_STR),10240);
            Template template = FreeMarkerTemplateUtils.getTemplate(templatePath);
            template.process(param,out);
        } catch (Exception e) {
            logger.error("generateUpstream error e = ",e);
            throw new BusinessException("999999","generate freeMarker template failure");
        }finally {
            try {
                fos.close();
                out.close();
            } catch (IOException e) {
                logger.error("generateUpstream error e = ",e);
            }
        }
    }

    public static void clearCache() {
        CONFIGURATION.clearTemplateCache();
    }

    /**
     * 通过模板和数据获取
     *
     * @param ftlFileName 模板文件名称
     * @param data        数据
     * @return string
     */
    public static String process(String ftlFileName, Object data) {
        try (StringWriter sw = new StringWriter()) {
            Template template = FreeMarkerTemplateUtils.getTemplate(ftlFileName);
            template.process(data, sw);
            return sw.toString();
        } catch (Exception e) {
            throw new RuntimeException("通过模板生成数据失败" + e);
        }
    }
}
