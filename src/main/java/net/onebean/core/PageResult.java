package net.onebean.core;

import com.alibaba.fastjson.JSON;
import net.onebean.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PageResult<T> {
    /**
     * 分页对象
     */
    private Pagination pagination;
    /**
     * 请求成功标识
     */
    private Boolean flag;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 返回的列表
     */
    private List<T> data = new ArrayList<T>(0);

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
