package net.onebean.core.extend.codebuilder;

import com.alibaba.fastjson.JSONObject;
import net.onebean.core.velocity.VelocityEngineFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author World
 */
public class VelocityUtils {

    private VelocityUtils() {
    }

    /**
     * 当前对象实例
     */
    private static VelocityUtils velocityUtils = new VelocityUtils();

    /**
     * 获取当前对象实例
     * @return 返回当前实例
     */
    protected static VelocityUtils getInstance() {
        return velocityUtils;
    }

    private static final Logger logger = LoggerFactory.getLogger(VelocityUtils.class);

    /**
     * 根据模板生成字符串
     *
     * @param param            参数
     * @param templateFilePath 模板路径
     * @return string
     */
    public String generateStringFromVelocity(JSONObject param, String templateFilePath) {
        VelocityContext context = new VelocityContext();
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            context.put(entry.getKey(), entry.getValue());
        }
        Template template;
        StringWriter writer = null;
        try {
            template = VelocityEngineFactory.getInstance().getEngine().getTemplate(templateFilePath, "UTF-8");
            writer = new StringWriter();
            if (template != null)
                template.merge(context, writer);
            writer.flush();
        } catch (Exception e) {
            logger.error("velocity mergeTemplate error = ", e);
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                logger.error("velocity mergeTemplate error = ", e);
            }
        }
        return writer.toString();
    }
}
