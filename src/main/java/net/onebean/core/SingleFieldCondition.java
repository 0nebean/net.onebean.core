package net.onebean.core;

import com.eakay.util.DateUtils;
import com.eakay.util.StringUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


public class SingleFieldCondition extends Condition implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4466041047921086476L;
	private String field;
	private Object value;
	private Object newValue;
	private String operator;
	private String type;
	
	/**
	 * 设置日期值
	 */
	public void setDateValue(Object value){
		this.value = value;
		if(value==null || value.toString().trim().equals("")){
		    return;
		}
		if("date".equals(type) && !"".equals(value.toString().trim())) {
			Date date = null;
			try {
				//gt 大于一个日期 + " 00:00:00"
				if(getOperator("gt").equals(operator)){
					date = DateUtils.parse(value.toString(), DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS);
					//减一秒操作
					Calendar calendar=Calendar.getInstance();
					calendar.setTime(date);
					calendar.add(Calendar.SECOND, -1);
					date=calendar.getTime();
				}else if(getOperator("lt").equals(operator)){ //lt 小于一个日期 + " 23:59:59"
					date = DateUtils.parse(value.toString(), DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.newValue = date;
		}
	}
	
	/**
	 * 
	 * 设置value值
	 */
	public void setValue(Object value)  {
		this.value = value;
		if(value==null || value.toString().trim().equals("")){
		    return;
		}
		//字符串类型
		if("string".equals(type)) {
			if("=".equals(operator)) {
				this.newValue = value.toString();
			}
			else if("!=".equals(operator) || ">=".equals(operator) || "<=".equals(operator)){
					this.newValue = this.value;
			}
			else if("in".equals(operator)){
				if(this.value instanceof String){
					this.newValue = value.toString().split(",");
				}else{
					this.newValue = this.value;
				}			
			}else if("condition".equals(operator)){
					this.newValue = this.value;
			}
			else {
				this.newValue = "%" + value.toString() + "%";
			}
		}
		//日期类型
		else if("date".equals(type) && !"".equals(value.toString().trim())) {
			
			Date date = null;
			try {
				//gt 大于一个日期 + " 00:00:00"
				if(getOperator("gt").equals(operator)){
					date = DateUtils.parse(value.toString()+" 00:00:00", DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS);
					//减一秒操作
					Calendar calendar=Calendar.getInstance();
					calendar.setTime(date);
					calendar.add(Calendar.SECOND, -1);
					date=calendar.getTime();
				}else if(getOperator("lt").equals(operator)){ //lt 小于一个日期 + " 23:59:59"
					date = DateUtils.parse(value.toString()+" 23:59:59", DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS);
				}else{
					date = DateUtils.parse(value.toString(), DateUtils.PATTERN_YYYY_MM_DD);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.newValue = date;
		}
		else {
			//其他 int 或  string 用,隔开的 字符串  转化为 String [] 类型
			if("int".equals(operator)) {
				if(this.value instanceof String){
					this.newValue = value.toString().split(",");
				}else{
					this.newValue =  this.value;
				}
				//condition 条件  lzz 2013-06-07 加
			}else if("condition".equals(operator)){
				this.newValue = this.value;
			}else {
				this.newValue = Long.valueOf( this.value.toString());
			}
			
		}
	}

	
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Object getValue() {
		return value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getNewValue() {
		return newValue;
	}

	/**
	 * 
	 * 校验是否 操作符  
	 */
	private static boolean isOperSign(String str) {
		return "gt".equals(str) || "lt".equals(str) || "eq".equals(str) || "nq".equals(str) || "like".equals(str) || "in".equals(str) || "condition".equals(str);
	}
	
	/**
	 * 
	 * 校验是否日期
	 */
	private static boolean isDate(String str) {
		return "date".equals(str);
	}
	
	/**
	 * 
	 * 校验是否int string
	 */
	private static boolean isType(String str) {
		return "int".equals(str) || "string".equals(str) ;
	}
	
	/**
	 * 
	 * 返回sql操作符
	 */
	private static String getOperator(String str) {
		if("gt".equals(str)) {
			return ">=";
		}
		else if("lt".equals(str)) {
			return "<=";
		}
		else if("in".equals(str)) {
			return "in";
		}else if("condition".equals(str)) {
			return "condition";
		}else if("nq".equals(str)) {
			return "!=";
		}else if("like".equals(str)) {
			return "like";
		}else{//eq 或其他
			return "=";
		}
	}

	@Override
	protected void parseModel(String parameter) {
		String[] array = parameter.split("@");
		String field = array[0];
		array[0] = StringUtils.humpToUnderline(field);
		parameter = StringUtils.arrayToString(array,"@");
		this.parse(parameter);
	}

	@Override
	protected void parse(String parameter) {
		String[] array = parameter.split("@");
		String field = array[0];
		setField(field);
		
		//只有一个参数(字符串模糊查询) 字段
		if(array.length == 1) {
			setType("string");
			setOperator("like");
		}
		
		//只有二个参数            字段_操作符/date
		if(array.length == 2) {   
			setOperator("=");
			//类型查询
			if(isType(array[1])) {
				setOperator("=");
				setType(array[1]);
			}
			//字符串精确查询
			if(isOperSign(array[1])) {
				setOperator(getOperator(array[1]));
				setType("string");
			}
			//日期精确查询
			if(isDate(array[1])) {
				setType("date");
			}
		}
		
		//有三个参数(非字符串查询)   字段名_类型_操作符
		if(array.length == 3) {
			setType(array[1]);
			setOperator(getOperator(array[2]));
		}
		
	}
	@Override
	public String toString() {
		return "SingleFieldCondition [field=" + field + ", value=" + value + ", newValue="
				+ newValue + ", operator=" + operator + ", type=" + type + "]";
	}
	
	
	public static void main(String[] args) {
		/*Condition c = Condition.parseCondition("sex_int_gt");
		c.setValue("1");
		System.out.println(c.toString());*/
		System.out.println("123.".substring(0,"123.".length()-1));
	}
	
}
