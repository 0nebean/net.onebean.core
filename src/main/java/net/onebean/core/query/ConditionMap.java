package net.onebean.core.query;

import net.onebean.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConditionMap extends HashMap<String, Object>{


	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 2999593154628884149L;

	
	List<Condition> items = new ArrayList<>();
	
	@Override
	public void clear() {
		items.clear();
		super.clear();
	}

	@Override
	public Object remove(Object key) {
		Condition condition = this.get(key);
		items.remove(condition);
		return super.remove(key);
	}



	public List<Condition> getItems(){
		return items;
	}
	@Override
	public Condition get(Object key) {
		Condition condition = (Condition) super.get(key);
		if(condition == null){
			condition = Condition.parseCondition(key.toString());
			put(key.toString(),condition);
		}
		return condition;
	}

	@Override
	public Condition put(String key, Object value) {
		items.add((Condition) value);
		return (Condition) super.put(key, value);
	}

	/**
	 * 根据表达式格式化查询条件(字段为数据库字段名)
	 * 例子: user_type@string@eq$^username@string@like$
	 * @param conditionStr 查询表达式
	 */
	public void parseCondition(String conditionStr){
		if(StringUtils.isEmpty(conditionStr)){
			return;
		}

		String [] conditionArr =  new String[1];

		if (conditionStr.contains("^")) {
			conditionArr = conditionStr.split("\\^");
		}else{
			conditionArr[0] = conditionStr;
		}

		for (String s:conditionArr) {
			Condition temp = Condition.parseCondition(s.substring(0,s.indexOf("$")));
			temp.setValue(s.substring(s.indexOf("$")+1,s.length()));
			this.put(s,temp);
		}
	}

	/**
	 * 根据表达式格式化查询条件(字段为变量名)
	 * 例子: userType@string@eq$^username@string@like$
	 * @param conditionStr 查询表达式
	 */
	public void parseModelCondition(String conditionStr){
		if(StringUtils.isEmpty(conditionStr)){
			return;
		}

		String [] conditionArr =  new String[1];

		if (conditionStr.contains("^")) {
			conditionArr = conditionStr.split("\\^");
		}else{
			conditionArr[0] = conditionStr;
		}

		for (String s:conditionArr) {
			Condition temp = Condition.parseModelCondition(s.substring(0,s.indexOf("$")));
			temp.setValue(s.substring(s.indexOf("$")+1,s.length()));
			this.put(s,temp);
		}
	}

	


	
	
}
