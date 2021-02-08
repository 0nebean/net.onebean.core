package net.onebean.core.error;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 0neBean
 */
public class BusinessException extends RuntimeException {

    private static Logger logger = (Logger) LoggerFactory.getLogger(BusinessException.class);

    private String code;
    private String msg;

    public BusinessException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
        logger.error("BusinessException e = "+msg+" code = "+code);
    }

    public BusinessException(String message, String code, String msg) {
        super(message);
        this.code = code;
        this.msg = msg;
        logger.error("BusinessException e = "+msg+" code = "+code);
    }

    public BusinessException(String message, Throwable cause, String code, String msg) {
        super(message, cause);
        this.code = code;
        this.msg = msg;
        logger.error("BusinessException e = "+msg+" code = "+code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
