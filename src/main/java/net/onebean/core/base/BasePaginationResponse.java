package net.onebean.core.base;

import net.onebean.core.extend.Sort;
import net.onebean.core.query.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author 0neBean
 * @param <M> 泛型data对象class
 */
@SuppressWarnings("all")
public class BasePaginationResponse<M> {

    private final static Logger logger = LoggerFactory.getLogger(BasePaginationResponse.class);


    public BasePaginationResponse() {
    }

    public BasePaginationResponse(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public BasePaginationResponse(String errCode, String errMsg, List<M> data, Pagination page) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.data = data;
        this.page = page;
    }

    private String errCode = null;
    private String errMsg = null;
    private List<M> data = null;
    private Pagination page = null;
    private Sort sort = null;

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public  BasePaginationResponse<M> failure(String errorCode,String errorMessage){
        BasePaginationResponse<M> b = new BasePaginationResponse<>();
        b.setErrCode(errorCode);
        b.setErrMsg(errorMessage);
        logger.info("BasePaginationResponse get an error ,Exception ex ="+this.toString());
        return b;
    }

    public BasePaginationResponse<M> ok(List list){
        BasePaginationResponse<M> b = new BasePaginationResponse();
        b.setData(list);
        b.setErrCode("0");
        return b;
    }

    public BasePaginationResponse<M> ok(List list,Pagination page){
        BasePaginationResponse<M> b = new BasePaginationResponse();
        b.setData(list);
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

    public List<M> getData() {
        return data;
    }

    public void setData(List<M> data) {
        this.data = data;
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }
}
