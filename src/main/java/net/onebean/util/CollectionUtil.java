package net.onebean.util;

import com.sun.xml.internal.txw2.IllegalAnnotationException;

import java.io.*;
import java.util.*;

/**
 * 集合工具类
 */
public class CollectionUtil {

	/**
	 * 是否是字符串或数字的数组
	 * @param list list
	 * @return bool
	 */
	public static Boolean isNumberOrStringArray(List<?> list){
		if (CollectionUtil.isEmpty(list)){
			throw new RuntimeException("empty list");
		}
		return CollectionUtil.getListTClass(list) == String.class || StringUtils.isNumeric(list.get(0).toString());
	}

	/**
	 * 获取数组的类型
	 * @param list list
	 * @return 类型
	 */
	public static Class<?> getListTClass(List<?> list){
		if (CollectionUtil.isNotEmpty(list)){
			return list.get(0).getClass();
		}else {
			return null;
		}
	}

	/**
	 * list 转 数组
	 * @param list list
	 * @return array
	 */
	public static String[] list2StringArr(List<?> list){
		String[] strArray = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			strArray[i] = list.get(i).toString();
		}
		return strArray;
	}
	/**
	 * 是否是数组
	 * @param obj 对象
	 * @return bool
	 */
	public static boolean isArray(Object obj) {
		return obj.getClass().isArray() || obj instanceof List;
	}
	/**
	 * 判断集合是否为空
	 * @param obj 集合
	 * @return bool
	 */
	public static boolean isEmpty(Collection<?> obj) {
		return obj == null || obj.size() == 0;
	}

	/**
	 * 判断集合是否为空
	 * @param obj 集合
	 * @return bool
	 */
	public static boolean isNotEmpty(Collection<?> obj) {
		return obj != null && obj.size() != 0;
	}

	/**
	 * 字符串数组转list
	 * @param stringArr 数组
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public static List<String> stringArr2List(String[] stringArr) {
		return (stringArr.length > 0)? Arrays.asList(stringArr):Collections.EMPTY_LIST;
	}



	/**
	 * 从集合的列表中提取long类型的字段
	 * @param list 列表
	 * @param key 字段key
	 * @return list
	 */
	public static List<Long> getLongList(List<Map<String, Object>> list, String key) {
		List<Long> resultList = new ArrayList<Long>();
		for(Map<String, Object> map : list) {
			resultList.add(Long.parseLong(map.get(key).toString()));
		}
		return resultList;
	}
	

	/**
	 * 将泛型集合转成long型的集合
	 * @param collection 要转换的集合
	 * @param <T> 泛型类型
	 * @return 集合
	 */
	public static <T extends Collection<Long>> T toLongCollection(Collection<String> collection){
		return toLongCollection(collection,ArrayList.class);
	}


	/**
	 * 将字符串数组值转换成长整型类型集合，将其转换成ArrayList类型结果集
	 * @param collection 要转换的集合
	 * @param <T> 泛型类型
	 * @return 集合
	 */
	public static <T extends Collection<Long>> T toLongCollection(String[] collection){
		return toLongCollection(collection,ArrayList.class);
	}


	/**
	 * 将字符串数组转换成Long集合
	 * @param collection 要转换的集合
	 * @param arrayType 数组类型
	 * @param <T> 泛型类型
	 * @return 集合
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Collection<Long>> T toLongCollection(String[] collection,Class<?> arrayType){
		//检查参数
		if(collection == null){
			return null;
		}
		//如果不存在
		if(arrayType == null){
			arrayType = ArrayList.class;
		}
		//检查类型是否是collection
		if(!Collection.class.isAssignableFrom(arrayType)){
			throw new IllegalAnnotationException("arrayType 必须是Collection子类");
		}
		//检查是否是接口
		if(arrayType.isInterface()){
			throw new IllegalAnnotationException("arrayType 必须是可以实例化的子类");
		}
		try {
			//实例化结果类
			Collection<Long> rs = (Collection<Long>)arrayType.newInstance();
			for (String s : collection) {
				rs.add(Long.parseLong(s));
			}
			return (T) rs;
		}catch (Exception e) {
			throw new IllegalAnnotationException("arrayType 必须是Collection子类",e);
		}
	}
	

	/**
	 * 将字符串集合值转换成长整型类型集合
	 * @param collection 要转换的集合
	 * @param arrayType 数组类型
	 * @param <T> 泛型类型
	 * @return 集合
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Collection<Long>> T toLongCollection(Collection<String> collection,Class<?> arrayType){
		//检查参数
		if(collection == null){
			return null;
		}
		//如果不存在
		if(arrayType == null){
			arrayType = ArrayList.class;
		}
		//检查类型是否是collection
		if(!Collection.class.isAssignableFrom(arrayType)){
			throw new IllegalAnnotationException("arrayType 必须是Collection子类");
		}
		//检查是否是接口
		if(arrayType.isInterface()){
			throw new IllegalAnnotationException("arrayType 必须是可以实例化的子类");
		}
		try {
			//实例化结果类
			Collection<Long> rs = (Collection<Long>)arrayType.newInstance();
			for (String s : collection) {
				//转换并插入到结果集中
				rs.add(Long.parseLong(s));
			}
			return (T) rs;
		}catch (Exception e) {
			throw new IllegalAnnotationException("arrayType 必须是Collection子类",e);
		}
	}


	/**
	 * 对象深度克隆---使用序列化进行深拷贝
	 * 注意：
	 * 使用序列化的方式来实现对象的深拷贝，但是前提是，对象必须是实现了 Serializable接口才可以，Map本身没有实现
	 * Serializable 这个接口，所以这种方式不能序列化Map，也就是不能深拷贝Map。但是HashMap是可以的，因为它实现了Serializable。
	 * @param obj 要克隆的对象
	 * @param <T> 对象类型
	 * @return 集合
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T deepCloneHashMap(T obj) {
		T clonedObj = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.close();
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			clonedObj = (T) ois.readObject();
			ois.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return clonedObj;
	}

	/**
	 * 是否包含改元素
	 * @param arr 数组
	 * @param item 元素
	 * @return bool
	 */
	public static Boolean isTargetContainsArrItem(String[] arr,String item){
		boolean flag =  false;
		for (String s : arr) {
			if (item.contains(s)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
}
