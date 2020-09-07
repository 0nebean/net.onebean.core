package net.onebean.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.*;

/**
 * xml 操作工具类
 *
 * @author World
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
        for (Element element : elementList) {
            jsonObject.put(element.getName(), element.getText());
        }
        inputStream.close();
        return jsonObject;
    }


    /**
     * 转化成xml, 单层无嵌套
     *
     * @param param      map
     * @param isAddCDATA 添加专业
     * @return xml str
     */
    public static String mapToXml(Map<String, Object> param, boolean isAddCDATA) {
        return StringUtils.isEmpty(param) ? "" : iterMap(param, isAddCDATA);
    }

    /**
     * 转换一个xml格式的字符串到json格式
     *
     * @param xml xml格式的字符串
     * @return 成功返回json 格式的字符串;失败反回null
     */
    public static JSONObject xml2JSON(String xml) {
        JSONObject obj = new JSONObject();
        try {
            Document doc = DocumentHelper.parseText(xml);
            Element root = doc.getRootElement();
            obj.put(root.getName(), iterateElement(root));
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 一个迭代方法
     *
     * @param element
     * @return java.util.Map 实例
     */
    @SuppressWarnings("unchecked")
    private static Map iterateElement(Element element) {
        List jiedian = element.elements();
        Element et = null;
        Map obj = new HashMap();
        Object temp;
        List list = null;
        for (int i = 0; i < jiedian.size(); i++) {
            list = new LinkedList();
            et = (Element) jiedian.get(i);
            if (et.getTextTrim().equals("")) {
                if (et.elements().size() == 0)
                    continue;
                if (obj.containsKey(et.getName())) {
                    temp = obj.get(et.getName());
                    if (temp instanceof List) {
                        list = (List) temp;
                        list.add(iterateElement(et));
                    } else if (temp instanceof Map) {
                        list.add((HashMap) temp);
                        list.add(iterateElement(et));
                    } else {
                        list.add((String) temp);
                        list.add(iterateElement(et));
                    }
                    obj.put(et.getName(), list);
                } else {
                    obj.put(et.getName(), iterateElement(et));
                }
            } else {
                if (obj.containsKey(et.getName())) {
                    temp = obj.get(et.getName());
                    if (temp instanceof List) {
                        list = (List) temp;
                        list.add(et.getTextTrim());
                    } else if (temp instanceof Map) {
                        list.add((HashMap) temp);
                        list.add(iterateElement(et));
                    } else {
                        list.add((String) temp);
                        list.add(et.getTextTrim());
                    }
                    obj.put(et.getName(), list);
                } else {
                    obj.put(et.getName(), et.getTextTrim());
                }

            }

        }
        return obj;
    }

    /**
     * 转化成xml, 单层无嵌套
     *
     * @param jsonObject json
     * @param isAddCDATA 添加专业
     * @return xml str
     */
    public static String jsonToXml(JSONObject jsonObject, boolean isAddCDATA) {
        LinkedHashMap<String, Object> jsonMap = JSON.parseObject(JSON.toJSONString(jsonObject), new TypeReference<LinkedHashMap<String, Object>>() {
        });
        return StringUtils.isEmpty(jsonMap) ? "" : iterMap(jsonMap, isAddCDATA);
    }

    private static String iterMap(Map<String, Object> jsonMap, boolean isAddCDATA) {
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
     *
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
