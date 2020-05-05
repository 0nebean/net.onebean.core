package net.onebean.util;

import net.onebean.component.SpringUtil;
import org.apache.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * rest请求工具类
 * @author 0neBean
 */
public class RestUtils {

    private final static Logger logger = Logger.getLogger(RestUtils.class);
    static private HttpHeaders headers;


    private RestUtils() {
    }

    /**
     * 初始化配置文件
     */
    private void initHeader() {
        headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("ContentType", MediaType.APPLICATION_JSON.toString());
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
    }

    /**
     * 当前对象实例
     */
    private static RestUtils restUtils = new RestUtils();

    /**
     * 获取当前对象实例
     * @return 实例
     */
    public static RestUtils getInstance() {
        restUtils.initHeader();
        return restUtils;
    }

    public Object doPostForRef(String url, Object request, ParameterizedTypeReference<?> responseType){
        RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
        HttpEntity<Object> httpEntity = new HttpEntity<>(request,headers);
        logger.info("RestUtils doPostForRef url = " + url);
        return restTemplate.exchange(url, HttpMethod.POST,httpEntity,responseType).getBody();
    }

    @SuppressWarnings("unchecked")
    public Object doPostForObj(String url, Object request, Class clazz){
        RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
        HttpEntity<Object> httpEntity = new HttpEntity<>(request,headers);
        logger.info("RestUtils doPostForObj url = " + url);
        return  restTemplate.postForEntity(url,httpEntity,clazz).getBody();
    }

    @SuppressWarnings("unchecked")
    public Object doGetForObj(String url, Class clazz){
        RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
        logger.info("RestUtils doGetForObj url = " + url);
        return  restTemplate.getForObject(url,clazz);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 拼接url参数 的形式。
     * @return 所代表远程资源的响应结果
     */
    public String sendPostByUrlParam(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("contentType", "UTF-8");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

}
