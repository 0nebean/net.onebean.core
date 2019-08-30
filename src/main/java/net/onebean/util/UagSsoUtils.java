package net.onebean.util;

import net.onebean.component.SpringUtil;
import net.onebean.core.form.Parse;
import net.onebean.core.model.UagLoginSessionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.util.Optional;

public class UagSsoUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(UagSsoUtils.class);
    private static final String UAG_SSO_LOGIN_INFO = "UAG-SSO-LOGIN-INFO";


    public static UagLoginSessionInfo getCurrentUagLoginSessionInfo() {
        UagLoginSessionInfo uagLoginInfo;
        try {
            String uagLoginInfoJson = Optional.ofNullable(SpringUtil.getHttpServletRequest().getSession().getAttribute(UAG_SSO_LOGIN_INFO)).map(s -> s + "").orElse("");
            uagLoginInfo = JSONUtil.toBean(uagLoginInfoJson, UagLoginSessionInfo.class);
            LOGGER.info("getCurrentUagLoginInfo uagLoginInfo = " + uagLoginInfo);
        } catch (Exception e) {
            LOGGER.error("getCurrentUagLoginSessionInfo failure e = ", e);
            return null;
        }
        return uagLoginInfo;
    }

    public static UagLoginSessionInfo getCurrentUagLoginHeaderInfo(){
        UagLoginSessionInfo uagLoginInfo = null;
        try {
            String uagHeaderUserInfoStr =  SpringUtil.getHttpServletRequest().getHeader("uagHeaderUserInfo");
            uagHeaderUserInfoStr = URLDecoder.decode(uagHeaderUserInfoStr, "utf-8");
            uagLoginInfo = JSONUtil.toBean(uagHeaderUserInfoStr,UagLoginSessionInfo.class);
        } catch (Exception e) {
            LOGGER.error("getCurrentUagLoginHeaderInfo failure e = ", e);
        }
        return uagLoginInfo;
    }


    public static void setUagUserInfoByHeader(Object model){
        UagLoginSessionInfo uagLoginInfo = getCurrentUagLoginHeaderInfo();
        String uagUserId = Optional.ofNullable(uagLoginInfo).map(UagLoginSessionInfo::getUagUserId).orElse("");
        String uagUserNickName = Optional.ofNullable(uagLoginInfo).map(UagLoginSessionInfo::getUagUserNickName).orElse("");
        try {
            ReflectionUtils.setFieldValue(model,"operatorId", Parse.toInt(uagUserId));
            ReflectionUtils.setFieldValue(model,"operatorName",uagUserNickName);
        } catch (Exception e) {
            LOGGER.error("setUagUserInfoByHeader failure e = ", e);
        }
    }

    public static void setUagUserInfoBySession(Object model){
        UagLoginSessionInfo uagLoginInfo = getCurrentUagLoginSessionInfo();
        String uagUserId = Optional.ofNullable(uagLoginInfo).map(UagLoginSessionInfo::getUagUserId).orElse("");
        String uagUserNickName = Optional.ofNullable(uagLoginInfo).map(UagLoginSessionInfo::getUagUserNickName).orElse("");
        try {
            ReflectionUtils.setFieldValue(model,"operatorId",Parse.toInt(uagUserId));
            ReflectionUtils.setFieldValue(model,"operatorName",uagUserNickName);
        } catch (Exception e) {
            LOGGER.error("setUagUserInfoBySession failure e = ", e);
        }
    }

}
