package net.onebean.core.base;

public class BaseResponse<T> {

    private String errCode = null;
    private String errMsg = null;
    private T data = null;

    @SuppressWarnings("all")
    public static BaseResponse ok(Object data){
        BaseResponse b = new BaseResponse<>();
        b.setData(data);
        b.setErrCode("0");
        return b;
    }

    @SuppressWarnings("all")
    public static BaseResponse ok(){
        BaseResponse b = new BaseResponse<>();
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
}
