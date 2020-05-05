package net.onebean.core.base;

public class BaseResponse<T> {

    private String errCode = null;
    private String errMsg = null;
    private T datas = null;

    @SuppressWarnings("unchecked")
    public BaseResponse<T> ok(Object datas){
        BaseResponse<T> b = new BaseResponse<>();
        b.setDatas((T) datas);
        b.setErrCode("0");
        return b;
    }

    public BaseResponse<T> ok(){
        BaseResponse<T> b = new BaseResponse<>();
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
