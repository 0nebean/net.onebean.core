package net.onebean.util;

import com.sun.xml.internal.txw2.IllegalAnnotationException;

import java.io.*;
import java.util.*;

/**
 * 集合工具类
 */
public class CollectionUtil {
	
	/**
	 * 判断集合是否为空
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Collection<?> obj) {
		return (obj == null || obj.size() == 0) ? true : false;
	}

	/**
	 * 判断集合是否为空
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Collection<?> obj) {
		return (obj == null || obj.size() == 0) ? false : true;
	}

	/**
	 * 字符串数组转list
	 * @param stringArr 数组
	 * @return list
	 */
	public static final List<String> stringArr2List(String[] stringArr) {
		return (stringArr.length > 0)? Arrays.asList(stringArr):Collections.EMPTY_LIST;
	}


	/**
	 * 返回List<Long>类型集合
	 * @param list
	 * @param key
	 * @return
	 */
	public static final List<Long> getLongList(List<Map<String, Object>> list, String key) {
		List<Long> resultList = new ArrayList<Long>();
		for(Map<String, Object> map : list) {
			resultList.add(Long.parseLong(map.get(key).toString()));
		}
		return resultList;
	}
	
	/**
	 * 将字符串集合值转换成长整型类型集合，将其转换成ArrayList类型结果集
	 * @param collection 要转换的集合
	 * @return 结果
	 */
	public static <T extends Collection<Long>> T toLongCollection(Collection<String> collection){
		return toLongCollection(collection,ArrayList.class);
	}
	/**
	 * 将字符串数组值转换成长整型类型集合，将其转换成ArrayList类型结果集
	 * @param collection 要转换的集合
	 * @return 结果
	 */
	public static <T extends Collection<Long>> T toLongCollection(String[] collection){
		return toLongCollection(collection,ArrayList.class);
	}

	/**
	 * 将字符串数组转换成Long集合
	 * @param collection 字符串数组
	 * @param arrayType 要转换的结果集合类型
	 * @return 结果集合
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
			for(int i = 0;i < collection.length;i ++){
				rs.add(Long.parseLong(collection[i]));
			}
			return (T) rs;
		}catch (Exception e) {
			throw new IllegalAnnotationException("arrayType 必须是Collection子类",e);
		}
	}
	
	/**
	 * 将字符串集合值转换成长整型类型集合
	 * @param collection 要转换的集合
	 * @param arrayType 返回的集合类型，如果为空则默认为ArrayList
	 * @return 结果
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
			Iterator<String> it = collection.iterator();
			while(it.hasNext()){
				//转换并插入到结果集中
				rs.add(Long.parseLong(it.next()));
			}
			return (T) rs;
		}catch (Exception e) {
			throw new IllegalAnnotationException("arrayType 必须是Collection子类",e);
		}
	}


	/**
	 * 对象深度克隆---使用序列化进行深拷贝
	 *
	 * @param obj
	 * 要克隆的对象
	 * @return
	 * 注意：
	 * 使用序列化的方式来实现对象的深拷贝，但是前提是，对象必须是实现了 Serializable接口才可以，Map本身没有实现
	 * Serializable 这个接口，所以这种方式不能序列化Map，也就是不能深拷贝Map。但是HashMap是可以的，因为它实现了Serializable。
	 */
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
			if (item.contains(s)){
				flag = true;
			}
		}
		return flag;
	}
}
