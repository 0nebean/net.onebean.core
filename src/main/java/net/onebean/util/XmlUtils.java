package net.onebean.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * xml 操作工具类
 * @author 0neBean
 */
public class XmlUtils {

    private static final String PREFIX_XML = "<xml>";
    private static final String SUFFIX_XML = "</xml>";
    private static final String PREFIX_CDATA = "<![CDATA[";
    private static final String SUFFIX_CDATA = "]]>";


    @SuppressWarnings("unchecked")
    public static JSONObject getJsonFromRequestXml(HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        InputStream inputStream = request.getInputStream();
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();
        List<Element> elementList = root.elements();
        for (Element element : elementList){
            jsonObject.put(element.getName(),element.getText());
        }
        inputStream.close();
        return jsonObject;
    }


    /**
     * 转化成xml, 单层无嵌套
     * @param param map
     * @param isAddCDATA 添加专业
     * @return xml str
     */
    public static String mapToXml(Map<String, Object> param, boolean isAddCDATA) {
        return StringUtils.isEmpty(param)?"":iterMap(param,isAddCDATA);
    }

    /**
     * 转化成xml, 单层无嵌套
     * @param jsonObject json
     * @param isAddCDATA 添加专业
     * @return xml str
     */
    public static String jsonToXml(JSONObject jsonObject, boolean isAddCDATA) {
        LinkedHashMap<String, Object> jsonMap = JSON.parseObject(JSON.toJSONString(jsonObject), new TypeReference<LinkedHashMap<String, Object>>() {});
        return StringUtils.isEmpty(jsonMap)?"":iterMap(jsonMap,isAddCDATA);
    }

    private static String iterMap(Map<String,Object> jsonMap,boolean isAddCDATA){
        StringBuilder stringBuilder = new StringBuilder(PREFIX_XML);
        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            stringBuilder.append("<").append(entry.getKey()).append(">");
            if (isAddCDATA) {
                stringBuilder.append(PREFIX_CDATA);
                if (null != entry.getValue()) {
                    stringBuilder.append(entry.getValue());
                }
                stringBuilder.append(SUFFIX_CDATA);
            } else {
                if (null != entry.getValue()) {
                    stringBuilder.append(entry.getValue());
                }
            }
            stringBuilder.append("</").append(entry.getKey()).append(">");
        }
        return stringBuilder.append(SUFFIX_XML).toString();
    }


    /**
     * 将xml字符串转换成json
     * @param xml xm字符串
     * @return JSONObject
     */
    public static JSONObject json2Map(String xml) {
        JSONObject map = new JSONObject();
        Document doc;
        try {
            // 将字符串转为XML
            doc = DocumentHelper.parseText(xml);
            // 获取根节点
            Element rootElt = doc.getRootElement();
            // 获取根节点下所有节点
            @SuppressWarnings("unchecked")
            List<Element> list = rootElt.elements();
            // 遍历节点
            for (Element element : list) {
                // 节点的name为map的key，text为map的value
                map.put(element.getName(), element.getText());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }
}
