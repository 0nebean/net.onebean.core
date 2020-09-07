package net.onebean.core.query;

import net.onebean.util.CollectionUtil;
import net.onebean.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @see Condition#parseCondition(String)
 */
public class MultiFieldCondition extends Condition implements Serializable {

    private List<SingleFieldCondition> singleConditions = null;

    public List<SingleFieldCondition> getSingleConditions() {
        return singleConditions;
    }

    /**
     *
     */
    private static final long serialVersionUID = 3576987175244428406L;


    @Override
    public String getOperator() {
        return singleConditions.get(0).getOperator();
    }

    @Override
    public void setOperator(String operator) {
        for (SingleFieldCondition condition : singleConditions) {
            condition.setOperator(operator);
        }

    }

    @Override
    public String getField() {
        return singleConditions.get(0).getField();
    }

    @Override
    public void setField(String field) {
        for (SingleFieldCondition condition : singleConditions) {
            condition.setField(field);
        }

    }

    @Override
    public Object getValue() {
        return singleConditions.get(0).getValue();
    }

    @Override
    public String getType() {
        return singleConditions.get(0).getType();
    }

    @Override
    public void setType(String type) {
        for (SingleFieldCondition condition : singleConditions) {
            condition.setType(type);
        }

    }

    @Override
    public Object getNewValue() {
        return singleConditions.get(0).getNewValue();
    }

    @Override
    public void setValue(Object value) {
        for (int i = 0; i < singleConditions.size(); i++) {
            if (CollectionUtil.isNumberOrStringArray((List<?>) value)) {
                String[] args = CollectionUtil.list2StringArr((List<?>) value);
                singleConditions.get(i).setValue(args[i]);
            } else {
                singleConditions.get(i).setValue(value);
            }
        }
    }

    @Override
    public void setDateValue(Object value) {
        for (SingleFieldCondition condition : singleConditions) {
            condition.setDateValue(value);
        }
    }


    /**
     * 如果输入一个值，查询两个或者多个字段，用这个方法来分解。字段之间用-分隔，其它规则和Condition一样。
     *
     * @param parameter 如 "name-title_String_eq"
     * @see Condition
     */
    @Override
    protected void parse(String parameter) {
        doParse(parameter, false);
    }

    private void doParse(String parameter, boolean isParseModel) {
        singleConditions = new ArrayList<>();
        int fieldIndex = parameter.indexOf("-");  //是否为多个字段
        if (fieldIndex == -1) {
            throw new RuntimeException("parameter 如 'status@string@eq-status@string@eq'");
        }


        String[] fields = parameter.split("-");

        for (String field : fields) {
            SingleFieldCondition singleCondition = new SingleFieldCondition();
            if (isParseModel) {
                singleCondition.parse(StringUtils.humpToUnderline(field));
            } else {
                singleCondition.parse(field);
            }
            singleConditions.add(singleCondition);
        }
    }

    @Override
    protected void parseModel(String parameter) {
        doParse(parameter, true);
    }


    @Override
    public String toString() {
        return "MultiFieldCondition{" +
                "singleConditions=" + singleConditions +
                '}';
    }

    public static void main(String[] args) {
        List<String> earningTypes = new ArrayList<>();
        earningTypes.add("1");
        earningTypes.add("2");
        Condition c = Condition.parseCondition("sex-name@int@eq");
        c.setValue(earningTypes);
        System.out.println(c.toString());
    }
}
