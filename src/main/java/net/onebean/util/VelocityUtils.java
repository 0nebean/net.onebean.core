package net.onebean.util;

import com.alibaba.fastjson.JSONObject;
import com.eakay.core.velocity.VelocityEngineFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author 0neBean
 */
public class VelocityUtils {

    private static final Logger logger = LoggerFactory.getLogger(VelocityUtils.class);

    /**
     * 根据模板生成字符串
     * @param param 参数
     * @param templateFilePath 模板路径
     * @return string
     */
    public static String generateStringFromVelocity(JSONObject param,String templateFilePath){
        VelocityContext context = new VelocityContext();
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            context.put(entry.getKey(),entry.getValue());
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
            logger.error("velocity mergeTemplate error = ",e);
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                logger.error("velocity mergeTemplate error = ",e);
            }
        }
        return writer.toString();
    }
}
