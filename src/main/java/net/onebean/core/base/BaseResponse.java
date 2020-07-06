package net.onebean.core.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseResponse<T> {

    private final static Logger logger = LoggerFactory.getLogger(BaseResponse.class);


    private String errCode = null;
    private String errMsg = null;
    private T data = null;


    public BaseResponse<T> failure(String errorCode, String errorMessage) {
        BaseResponse<T> b = new BaseResponse<T>(){};
        b.setErrCode(errorCode);
        b.setErrMsg(errorMessage);
        logger.info("BaseResponse get an error resp,Exception ex =" + this.toString());
        return b;
    }


    public BaseResponse<T> ok(T data) {
        BaseResponse<T> b = new BaseResponse<T>(){};
        b.setData(data);
        b.setErrCode("0");
        return b;
    }

    public BaseResponse<T> ok() {
        BaseResponse<T> b = new BaseResponse<T>(){};
        b.setErrCode("0");
        return b;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "errCode='" + errCode + '\'' +
                ", errMsg='" + errMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
