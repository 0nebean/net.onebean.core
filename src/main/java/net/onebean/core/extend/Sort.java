package net.onebean.core.extend;

/**
 * @author 0neBean
 * 排序封装字段
 */
public class Sort {

    public static final String ASC = "asc";
    public static final String DESC = "desc";
    private String sort = "";
    private String orderBy = "";

    public Sort(String sort, String orderBy) {
        this.sort = sort;
        this.orderBy = orderBy;
    }

    public Sort() {
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

}
