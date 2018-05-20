package net.onebean.core;

/**
 * 面包屑model
 * @author 0neBean
 */
public class BreadCrumbs {
    /**
     * 面包屑url
     */
    private String breadCrumbsUrl;
    /**
     * 面包屑 title
     */
    private String breadCrumbsTitle;

    public String getBreadCrumbsUrl() {
        return breadCrumbsUrl;
    }

    public void setBreadCrumbsUrl(String breadCrumbsUrl) {
        if (null != breadCrumbsUrl){
            breadCrumbsUrl = (breadCrumbsUrl.endsWith("/"))?breadCrumbsUrl:breadCrumbsUrl+"/";
        }
        this.breadCrumbsUrl = breadCrumbsUrl;
    }

    public String getBreadCrumbsTitle() {
        return breadCrumbsTitle;
    }

    public void setBreadCrumbsTitle(String breadCrumbsTitle) {
        this.breadCrumbsTitle = breadCrumbsTitle;
    }
}



