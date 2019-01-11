package net.onebean.core;

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

    private M data = null;
    private Pagination page = null;

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
