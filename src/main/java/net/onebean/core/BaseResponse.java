package net.onebean.core;

public class BaseResponse<T> {

    private String errCode = null;
    private String errMsg = null;
    private T datas = null;

    public static BaseResponse ok(Object datas){
        BaseResponse b = new BaseResponse();
        b.setDatas(datas);
        b.setErrCode("0");
        return b;
    }

    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
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
