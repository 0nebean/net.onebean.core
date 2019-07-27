package net.onebean.core.base;


import java.util.Map;

/**
 * @author 0neBean
 * meizi UI tree model
 */
public class BaseTree {
    private String title;
    private String type;

    private Map<String,Object> attr;

    public Map<String, Object> getAttr() {
        return attr;
    }

    public void setAttr(Map<String, Object> attr) {
        this.attr = attr;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
