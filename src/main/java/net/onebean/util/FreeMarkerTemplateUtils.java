package net.onebean.util;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @author 0neBean
 * FreeMarker配置类
 */
public class FreeMarkerTemplateUtils {

    private final static String FREEMARKER_FTL_KEY = "apache.freemarker.ftl.path";
    private FreeMarkerTemplateUtils(){}
    private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_22);

    static{
        //这里比较重要，用来指定加载模板所在的路径
        CONFIGURATION.setTemplateLoader(new ClassTemplateLoader(FreeMarkerTemplateUtils.class, PropUtil.getInstance().getConfig(FREEMARKER_FTL_KEY,PropUtil.PUBLIC_CONF_FREEMARKER)));
        CONFIGURATION.setDefaultEncoding("UTF-8");
        CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        CONFIGURATION.setCacheStorage(NullCacheStorage.INSTANCE);
    }

    public static Template getTemplate(String templateName) throws IOException {
        try {
            return CONFIGURATION.getTemplate(templateName);
        } catch (IOException e) {
            throw e;
        }
    }

    public static void clearCache() {
        CONFIGURATION.clearTemplateCache();
    }

    /**
     * 通过模板和数据获取
     * @param ftlFileName 模板文件名称
     * @param data 数据
     * @return string
     */
    public static String process(String ftlFileName, Object data) {
        try (StringWriter sw = new StringWriter()) {
            Template temp = CONFIGURATION.getTemplate(ftlFileName);
            temp.process(data, sw);
            return sw.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException("通过模板生成数据失败"+e);
        }
    }
}
