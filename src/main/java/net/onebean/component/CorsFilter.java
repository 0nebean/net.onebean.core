//package net.onebean.component;
//
//import net.onebean.util.StringUtils;
//import org.apache.http.HttpStatus;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//
//@Component
//public class CorsFilter implements Filter {
//
//    final static Logger logger = LoggerFactory.getLogger(CorsFilter.class);
//
//    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) resp;
//        HttpServletRequest request = (HttpServletRequest) req;
//        if ("OPTIONS".equals(request.getMethod().toUpperCase())){
//            response.setStatus(HttpStatus.SC_NO_CONTENT);
//            return;
//        }
//
//        String accessControlAllowOrigin = request.getHeader("Access-Control-Allow-Origin");
//        String accessControlAllowMethods = request.getHeader("Access-Control-Allow-Methods");
//        String accessControlMaxAge = request.getHeader("Access-Control-Max-Age");
//        String accessControlAllowHeaders = request.getHeader("Access-Control-Allow-Headers");
//
//        if (StringUtils.isEmpty(accessControlAllowOrigin)){
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            logger.debug("CorsFilter set header Access-Control-Allow-Origin");
//        }
//
//        if (StringUtils.isEmpty(accessControlAllowMethods)){
//            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//            logger.debug("CorsFilter set header Access-Control-Allow-Methods");
//        }
//
//        if (StringUtils.isEmpty(accessControlMaxAge)){
//            response.setHeader("Access-Control-Max-Age", "3600");
//            logger.debug("CorsFilter set header Access-Control-Max-Age");
//        }
//
//        if (StringUtils.isEmpty(accessControlAllowHeaders)){
//            response.setHeader("Access-Control-Allow-Headers", "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type");
//            logger.debug("CorsFilter set header Access-Control-Allow-Headers");
//        }
//
//        chain.doFilter(req, resp);
//    }
//    public void init(FilterConfig filterConfig) {}
//    public void destroy() {}
//}