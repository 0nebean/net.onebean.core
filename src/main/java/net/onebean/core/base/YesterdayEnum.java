package net.onebean.core.base;

public enum YesterdayEnum {

    SAME_DAY(-1, "同一天"),
    YESTERDAY(0, "昨天"),
    LEAST_THE_DAY_BEFORE_YESTERDAY(1, "至少是前天");


    YesterdayEnum() {
    }

    private YesterdayEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }


    private Integer key;
    private String value;


    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}