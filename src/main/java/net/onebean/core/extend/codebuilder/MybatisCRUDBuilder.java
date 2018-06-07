package net.onebean.core.extend.codebuilder;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.onebean.core.extend.FiledName;
import net.onebean.core.extend.IgnoreColumn;
import net.onebean.core.extend.NullUpdatable;
import net.onebean.core.extend.TableName;
import net.onebean.core.metadata.ModelMappingManager;
import net.onebean.core.metadata.PropertyInfo;
import net.onebean.core.model.BaseIncrementIdModel;
import net.onebean.util.PropUtil;
import net.onebean.util.StringUtils;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import net.onebean.core.model.Deleted;
import net.onebean.util.ClassUtils;

/**
 * 根据类名生成BaseDao中定义的Mybatis的映射SQL语句
 * 
 */
public class MybatisCRUDBuilder extends UniversalCodeBuilder {

	@Override
	public <T> String buildByClass(Class<T> clazz) {
		return this.createSqlByEntity(clazz);
	}

	private static String templateFile = null;
	private static VelocityEngine ve = null;

	static {
		ve = new VelocityEngine();
		// 可选值："class"--从classpath中读取，"file"--从文件系统中读取
		ve.setProperty("resource.loader", "class");
		// 如果从文件系统中读取模板，那么属性值为org.apache.velocity.runtime.resource.loader.FileResourceLoader
		ve.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		try {
			ve.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		templateFile = PropUtil.getConfig("org.mybaits.creatsql.vm.file.path");
	}

	public String mergeTemplate(VelocityContext context) {
		Template template = null;
		StringWriter writer = null;
		try {
			template = ve.getTemplate(templateFile, "UTF-8");
			writer = new StringWriter();
			if (template != null)
				template.merge(context, writer);
			writer.flush();

		} catch (ResourceNotFoundException rnfe) {
			rnfe.printStackTrace();
		} catch (ParseErrorException pee) {
			pee.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return writer.toString();
	}

	/******************************************************************************/

	private List<String> pkList = new ArrayList<String>();
	private Map<String,String> nullUpdatableList = new HashMap<String,String>();

	/**
	 * 功能:初始化类字段属性
	 * <p>
	 * 
	 * @param clazz 要转换的实体类
	 * @param type 类型 file代表文件，否则代表字符串
	 * @return 如果type=file返回xml文件绝对路径，否则返回生成的xml内容
	 */
	public <T> String createSqlByEntity(Class<T> clazz) {
		String tableName = null;
	
		TableName table =  clazz.getAnnotation(TableName.class);
		if(table!=null){
			tableName = table.value();
			
		}else					
		{
			Object tName= ClassUtils.getAnnotationValue(clazz, "Table", "name");
			if(tName!=null)
			{
				tableName=tName.toString();
			}
			if(tableName==null)
			{
			tableName = ClassUtils.getLowerFirstLetterSimpleClassName(clazz
				.getSimpleName());
			}
		}
		
		pkList.clear();
		List<String> columns = new ArrayList<String>();
		Map<String,String> columnMap = new HashMap<String,String>();
		List<String> paramColumns = new ArrayList<String>();
		List<PropertyInfo> propertyinfos = ModelMappingManager.getBeanInfo(
				clazz).getProperties();
		boolean idIsString=false;
		for (PropertyInfo propertyinfo : propertyinfos) {
			Method m = propertyinfo.getReadMethod();
			if(m!=null)
			{
				IgnoreColumn ignore = m.getAnnotation(IgnoreColumn.class);
				if(ignore != null)
					continue;
				String mName = null;
				FiledName filed = m.getAnnotation(FiledName.class);
				if(filed!=null){
					mName = filed.value();
				}else{

					Object cName=ClassUtils.getMethodAnnotationValue(m, "Column", "name");
					if(cName!=null)
					{
						mName=cName.toString();
					}				
					if(mName==null)
					{
						mName = ClassUtils.getPropertyName(m);
					}
				}
				String paramName = generateColumn(m);
				columns.add(mName);
				paramColumns.add(paramName);
				columnMap.put(mName, ClassUtils.getPropertyName(m));
				// 判断是否是主键
				if (mName.equalsIgnoreCase("id"))
				{
					pkList.add(mName);
					idIsString=String.class.isAssignableFrom(m.getReturnType());
				}
				//判断值为Null时是否可更新
				NullUpdatable updatable = m.getAnnotation(NullUpdatable.class);
				if(updatable != null){
					nullUpdatableList.put(mName,"Y");
				}
			}else
			{
				Field field=propertyinfo.getField();
				Object cName=ClassUtils.getFieldAnnotationValue(field, "Column", "name");
				
				if(cName!=null)
				{
					String mName=cName.toString();
					String paramName = generateColumn4Field(field);
					columns.add(mName);
					paramColumns.add(paramName);
					columnMap.put(mName, field.getName());
					// 判断是否是主键
					if (mName.equalsIgnoreCase("id"))
					{
						pkList.add(mName);
						idIsString=String.class.isAssignableFrom(field.getType());
					}
				}
				
			}
			
		}
		String entityClassName = clazz.getName();
		String findByIdSql = findByIdSql(tableName, columnMap);
		String findSql = findSql(tableName, columnMap, clazz);
		String start_selectKey="";
		String selectKeySql = "";
		String end_selectKey="";
		if(!idIsString)
		{
			start_selectKey="<selectKey keyProperty=\"id\" resultType=\"long\">";
			selectKeySql=selectKeySql(tableName);
			end_selectKey="</selectKey>";
		}
		String inserSql = insertSql(tableName, columns,paramColumns);
		String updateSql = updateSql(tableName, columnMap);
		String updateBatchSql = updateBatchSql(tableName, columnMap);
		VelocityContext context = new VelocityContext();
		String deleteSql;
		String deleteByIdsSql;
		if (Deleted.class.isAssignableFrom(clazz)) {
			deleteSql = deleteDeletedSql(tableName, false);
			deleteByIdsSql = deleteDeletedSql(tableName, true);
		} else {
			deleteSql = deleteSql(tableName, columns);
			deleteByIdsSql = "delete from " + tableName
					+ " WHERE id in <include refid=\"common.idsForEach\"/>";
		}

		context.put("delete", deleteSql);
		context.put("deleteById", deleteSql);
		context.put("deleteByIdsSql", deleteByIdsSql);
		
		context.put("getMaxIdSql", "select id from "+tableName+" order by id desc limit 1");

		context.put("findById", findByIdSql);
		context.put("findSql", findSql);
		context.put("start_selectKey", start_selectKey);
		context.put("selectKey", selectKeySql);
		context.put("end_selectKey", end_selectKey);
		context.put("insert", inserSql);
		context.put("update", updateSql);
		context.put("updateBatchSql", updateBatchSql);

		context.put("tableName", tableName);
		// 把model替换成Dao
		String daoClassName = entityClassName.replaceFirst("model|entity", "dao") + "Dao";

		context.put("daoClass", daoClassName);
		context.put("entityClass", entityClassName);
		if (clazz.isAnnotationPresent(CacheNamespace.class)) {
			//TODO: 缓存还没有实现
			context.put("cacheClass","");
			context.put("cached", true);
		} else {
			context.put("cacheClass", "");
			context.put("cached", false);
		}

		return mergeTemplate(context);

	}

	private String deleteDeletedSql(String tableName, boolean batch) {
		StringBuilder updateSql = new StringBuilder();
		if (batch) {
			updateSql
					.append("update ")
					.append(tableName)
					.append(" set deleted_state=1 where id in <include refid=\"common.idsForEach\"/>");
		} else {
			updateSql.append("update ").append(tableName)
					.append(" set deleted_state=1 where id=#{id}");
		}
		return updateSql.toString();
	}
	
	private String selectKeySql(String tableName){
		return "SELECT LAST_INSERT_ID() AS ID";
	}
	

	/**
	 * 功能:生成insert语句
	 * @param tableName
	 * @param columns
	 * @return
	 */
	private String insertSql(String tableName, List<String> columns,List<String> paramColumn) {
		StringBuilder insertSql = new StringBuilder();
		StringBuilder valueSql = new StringBuilder();
		insertSql.append("insert into ").append(tableName).append("(");
		for (int i=0;i<columns.size();i++) {
			if(columns.get(i).toLowerCase().equals("id"))continue;
			insertSql.append(columns.get(i)).append(",");
			valueSql.append("#{").append(paramColumn.get(i)).append("},");
		}
		insertSql.deleteCharAt(insertSql.length() - 1);
		valueSql.deleteCharAt(valueSql.length() - 1);
		insertSql.append(") values (").append(valueSql).append(")");
		return insertSql.toString();
	}

	/**
	 * 功能:生成update语句
	 * @param tableName
	 * @param columns
	 * @return
	 */
	private String updateSql(String tableName, Map<String,String> columnMap) {
		// <set>元素会动态前置 SET关键字,而且也会消除任意无关的逗号
		StringBuilder updateSql = updateFields(tableName, columnMap, false);
		if (pkList.size() > 0) {
			updateSql.append(pkWhereSqlStr());
		}
		return updateSql.toString();
	}

	private StringBuilder updateFields(String tableName, Map<String,String> columnMap,
			boolean useAlias) {
		StringBuilder updateSql = new StringBuilder();
		updateSql.append("update ").append(tableName).append(" <set> ");
		for(Iterator<String> itor = columnMap.keySet().iterator();itor.hasNext();){
			String key = itor.next();
			String value = columnMap.get(key);
			//String columnAlias = useAlias ? "entity." + value : key;
			// if(useAlias) column = ;
			updateSql.append(" <if test=\"").append(value)
					.append(" != null\"> ");
			updateSql.append(key).append("=#{").append(value)
					.append("},");
			updateSql.append(" </if> ");
			
			//20140821修改， 如果为null，update时可将字段置空
			if(nullUpdatableList.get(key) != null){
				updateSql.append(" <if test=\"").append(value)
				.append(" == null\"> ");
				updateSql.append(key).append("=null,");
				updateSql.append(" </if> ");
			}
			
		}		
		
		updateSql.append(" </set> ");
		return updateSql;
	}
	
	private StringBuilder updateFields2(String tableName, Map<String,String> columnMap,
      boolean useAlias) {
    StringBuilder updateSql = new StringBuilder();
    updateSql.append("update ").append(tableName).append(" <set> ");
    for(Iterator<String> itor = columnMap.keySet().iterator();itor.hasNext();){
      String key = itor.next();
      String value = columnMap.get(key);
      //String columnAlias = useAlias ? "entity." + value : key;
      // if(useAlias) column = ;
      updateSql.append(" <if test=\"entity.").append(value)
          .append(" != null\"> ");
      updateSql.append(key).append("=#{entity.").append(value)
          .append("},");
      updateSql.append(" </if> ");
    }   
    
    updateSql.append(" </set> ");
    return updateSql;
  }

	private String updateBatchSql(String tableName, Map<String,String> columnMap) {
		StringBuilder updateSql = updateFields2(tableName, columnMap, true);
		updateSql.append(NEW_LINE_BREAK).append(" WHERE id in ");
		updateSql.append("<include refid=\"common.idsForEach\"/>");
		return updateSql.toString();
	}

	/**
	 * 功能:生成findById语句
	 * @param tableName
	 * @param columns
	 * @return
	 */
	private String findByIdSql(String tableName, Map<String,String> columnMap) {
		StringBuilder findByIdSql = new StringBuilder();
		findByIdSql.append("select ");
		for(Iterator<String> itor = columnMap.keySet().iterator();itor.hasNext();){
			String key = itor.next();
			String value = columnMap.get(key);
			findByIdSql.append(key).append(" as ").append(value).append(",");
		}
		findByIdSql = new StringBuilder(findByIdSql.substring(0, findByIdSql.length()-1));
		findByIdSql.append(" from ").append(tableName);
		findByIdSql.append(pkWhereSqlStr());
		findByIdSql.append(" limit 1");
		return findByIdSql.toString();
	}

	private <T> String findSql(String tableName, Map<String,String> columnMap, Class<T> clazz) {

		StringBuilder findSql = new StringBuilder();
		findSql.append("select ");
		for(Iterator<String> itor = columnMap.keySet().iterator();itor.hasNext();){
			String key = itor.next();
			String value = columnMap.get(key);
			findSql.append(key).append(" as ").append(value).append(",");
		}
		findSql = new StringBuilder(findSql.substring(0, findSql.length()-1));
		findSql.append(" from ").append(tableName);
		findSql.append(NEW_LINE_BREAK).append("<where>");
		findSql.append(NEW_LINE_BREAK).append("<include refid=\"common.dynamicConditionsNoWhere\"/>");
		if (Deleted.class.isAssignableFrom(clazz)) {
			findSql.append(NEW_LINE_BREAK).append("AND deleted_state = 0");
		}

		findSql.append(NEW_LINE_BREAK).append("</where>");
		String orderBy = ModelMappingManager.getBeanInfo(clazz).getOrderBy();
		if (!StringUtils.isEmpty(orderBy)) {
			findSql.append(NEW_LINE_BREAK);
			findSql.append("<if test=\"null != sort and sort.orderBy == null\">");
			findSql.append(orderBy);
			findSql.append("</if>");
		}
		findSql.append(NEW_LINE_BREAK).append("<include refid=\"common.orderBySql\"/>");
		findSql.append(NEW_LINE_BREAK).append("<include refid=\"common.sortSql\"/>");
		return findSql.toString();
	}

	/**
	 * 功能:生成delete语句
	 * <p>
	 * 作者文齐辉 2012-11-16 下午5:57:48
	 * 
	 * @param tableName
	 * @param columns
	 * @return
	 */
	private String deleteSql(String tableName, List<String> columns) {
		StringBuilder deleteSql = new StringBuilder();
		deleteSql.append("delete from ").append(tableName);
		deleteSql.append(pkWhereSqlStr());
		return deleteSql.toString();
	}

	/**
	 * 主键where条件拼接
	 * 
	 * @return
	 */
	private String pkWhereSqlStr() {
		if (pkList.size() == 0)
			return "";
		StringBuilder pkStr = new StringBuilder();
		pkStr.append(" where ");
		for (String pk : pkList) {
			pkStr.append(pk).append("=").append("#{").append(pk).append("}")
					.append(" and ");
		}

		return pkStr.delete(pkStr.length() - 4, pkStr.length()).toString();
	}
	
	/**
	 * 根据get方法名称生成基于mybatis的列名
	 * @param m
	 * @return
	 */
	private String generateColumn(Method m){
		String mName = ClassUtils.getPropertyName(m);
		if(Short.class.isAssignableFrom(m.getReturnType())){
			mName +=",jdbcType=BIGINT";
		}else if(Long.class.isAssignableFrom(m.getReturnType())){
			mName +=",jdbcType=NUMERIC";
		}else if(Float.class.isAssignableFrom(m.getReturnType())){
			mName +=",jdbcType=FLOAT";
		}else if(Double.class.isAssignableFrom(m.getReturnType())){
			mName +=",jdbcType=DOUBLE";
		}else if(Integer.class.isAssignableFrom(m.getReturnType())){
			mName +=",jdbcType=INTEGER";
		}else if(Byte.class.isAssignableFrom(m.getReturnType())){
			mName +=",jdbcType=BINARY";
		}else if(Character.class.isAssignableFrom(m.getReturnType())){
			mName +=",jdbcType=VARCHAR";
		}else if(Boolean.class.isAssignableFrom(m.getReturnType())){
			mName +=",jdbcType=BOOLEAN";
		}else if(String.class.isAssignableFrom(m.getReturnType())){
			mName +=",jdbcType=VARCHAR";
		}else if(Date.class.isAssignableFrom(m.getReturnType())){
			mName +=",jdbcType=DATE";
		}else if(BaseIncrementIdModel.class.isAssignableFrom(m.getReturnType())){
		  mName +=".id,jdbcType=NUMERIC";
		}
		return mName;
	}
	private String generateColumn4Field(Field m){
		String mName = m.getName();
		Class<?> t= m.getType();
		if(Short.class.isAssignableFrom(t)){
			mName +=",jdbcType=BIGINT";
		}else if(Long.class.isAssignableFrom(t)){
			mName +=",jdbcType=NUMERIC";
		}else if(Float.class.isAssignableFrom(t)){
			mName +=",jdbcType=FLOAT";
		}else if(Double.class.isAssignableFrom(t)){
			mName +=",jdbcType=DOUBLE";
		}else if(Integer.class.isAssignableFrom(t)){
			mName +=",jdbcType=INTEGER";
		}else if(Byte.class.isAssignableFrom(t)){
			mName +=",jdbcType=BINARY";
		}else if(Character.class.isAssignableFrom(t)){
			mName +=",jdbcType=VARCHAR";
		}else if(Boolean.class.isAssignableFrom(t)){
			mName +=",jdbcType=BOOLEAN";
		}else if(String.class.isAssignableFrom(t)){
			mName +=",jdbcType=VARCHAR";
		}else if(Date.class.isAssignableFrom(t)){
			mName +=",jdbcType=DATE";
		}else if(BaseIncrementIdModel.class.isAssignableFrom(t)){
		  mName +=".id,jdbcType=NUMERIC";
		}
		return mName;
	}
}
