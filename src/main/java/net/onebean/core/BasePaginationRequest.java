package net.onebean.core;

import com.eakay.core.extend.Sort;

import javax.validation.Valid;

/**
 * @author 0neBean
 * @param <M> 泛型data对象class
 */
public class BasePaginationRequest<M> {

    public BasePaginationRequest() {
    }

    public BasePaginationRequest(M data) {
        this.data = data;
    }

    public BasePaginationRequest(M data, Pagination page) {
        this.data = data;
        this.page = page;
    }

    @Valid
    private M data = null;
    private Pagination page = null;
    private Sort sort = null;

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public M getData() {
        return data;
    }

    public void setData(M data) {
        this.data = data;
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }
}
