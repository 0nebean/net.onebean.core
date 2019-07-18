package net.onebean.core;

import net.onebean.core.extend.Sort;

import java.util.List;

/**
 * @author 0neBean
 * @param <M> 泛型data对象class
 */
public class BasePaginationResponse<M> {

    public BasePaginationResponse() {
    }

    public BasePaginationResponse(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public BasePaginationResponse(String errCode, String errMsg, List<M> datas, Pagination page) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.datas = datas;
        this.page = page;
    }

    private String errCode = null;
    private String errMsg = null;
    private List<M> datas = null;
    private Pagination page = null;
    private Sort sort = null;

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    @SuppressWarnings("unchecked")
    public static BasePaginationResponse ok(List list){
        BasePaginationResponse b = new BasePaginationResponse();
        b.setDatas(list);
        b.setErrCode("0");
        return b;
    }

    @SuppressWarnings("unchecked")
    public static BasePaginationResponse ok(List list,Pagination page){
        BasePaginationResponse b = new BasePaginationResponse();
        b.setDatas(list);
        b.setPage(page);
        b.setErrCode("0");
        return b;
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

    public List<M> getDatas() {
        return datas;
    }

    public void setDatas(List<M> datas) {
        this.datas = datas;
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }
}
