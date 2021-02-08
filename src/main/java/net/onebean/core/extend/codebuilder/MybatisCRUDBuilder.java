package net.onebean.core.extend.codebuilder;

import com.alibaba.fastjson.JSONObject;
import net.onebean.core.extend.FiledName;
import net.onebean.core.extend.IgnoreColumn;
import net.onebean.core.extend.NullUpdatable;
import net.onebean.core.extend.TableName;
import net.onebean.core.metadata.ModelMappingManager;
import net.onebean.core.metadata.PropertyInfo;
import net.onebean.core.model.BaseIncrementIdModel;
import net.onebean.core.model.InterfaceBaseDeletedModel;
import net.onebean.core.model.InterfaceBaseSplitModel;
import net.onebean.util.ClassUtils;
import net.onebean.util.PropUtil;
import net.onebean.util.StringUtils;
import org.apache.ibatis.annotations.CacheNamespace;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;


/**
 * 根据类名生成BaseDao中定义的Mybatis的映射SQL语句
 */
public class MybatisCRUDBuilder extends UniversalCodeBuilder {

    private static final String templateFileKey = "org.mybaits.creatsql.vm.file.path";

    @Override
    public <T> String buildByClass(Class<T> clazz) {
        return this.createSqlByEntity(clazz);
    }


    /******************************************************************************/

    private List<String> pkList = new ArrayList<String>();
    private Map<String, String> nullUpdatableList = new HashMap<String, String>();

    /**
     * 功能:初始化类字段属性
     * @param clazz 要转换的实体类
     * @param <T> 泛型实体类型
     * @return 如果type=file返回xml文件绝对路径，否则返回生成的xml内容
     */
    public <T> String createSqlByEntity(Class<T> clazz) {
        String tableName = null;

        TableName table = clazz.getAnnotation(TableName.class);
        if (table != null) {
            tableName = table.value();

        } else {
            Object tName = ClassUtils.getAnnotationValue(clazz, "Table", "name");
            if (tName != null) {
                tableName = tName.toString();
            }
            if (tableName == null) {
                tableName = ClassUtils.getLowerFirstLetterSimpleClassName(clazz
                        .getSimpleName());
            }
        }

        pkList.clear();
        List<String> columns = new ArrayList<String>();
        Map<String, String> columnMap = new HashMap<String, String>();
        List<String> paramColumns = new ArrayList<String>();
        List<PropertyInfo> propertyinfos = ModelMappingManager.getBeanInfo(clazz).getProperties();
        boolean idIsString = false;
        for (PropertyInfo propertyinfo : propertyinfos) {
            Method m = propertyinfo.getReadMethod();
            if (m != null) {
                IgnoreColumn ignore = m.getAnnotation(IgnoreColumn.class);
                if (ignore != null)
                    continue;
                String mName = null;
                FiledName filed = m.getAnnotation(FiledName.class);
                if (filed != null) {
                    mName = filed.value();
                    mName = StringUtils.humpToUnderline(mName);
                } else {
                    Object cName = ClassUtils.getMethodAnnotationValue(m, "Column", "name");
                    if (cName != null) {
                        mName = cName.toString();
                    }
                    if (mName == null) {
                        mName = ClassUtils.getPropertyName(m);
                    }
                }
                String paramName = generateColumn(m);
                columns.add(mName);
                paramColumns.add(paramName);
                columnMap.put(mName, ClassUtils.getPropertyName(m));
                // 判断是否是主键
                if (mName.equalsIgnoreCase("id")) {
                    pkList.add(mName);
                    idIsString = String.class.isAssignableFrom(m.getReturnType());
                }
                //判断值为Null时是否可更新
                NullUpdatable updatable = m.getAnnotation(NullUpdatable.class);
                if (updatable != null) {
                    nullUpdatableList.put(mName, "Y");
                }
            } else {
                Field field = propertyinfo.getField();
                Object cName = ClassUtils.getFieldAnnotationValue(field, "Column", "name");
                if (cName != null) {
                    String mName = cName.toString();
                    String paramName = generateColumn4Field(field);
                    columns.add(mName);
                    paramColumns.add(paramName);
                    columnMap.put(mName, field.getName());
                    // 判断是否是主键
                    if (mName.equalsIgnoreCase("id")) {
                        pkList.add(mName);
                        idIsString = String.class.isAssignableFrom(field.getType());
                    }
                }

            }

        }
        String entityClassName = clazz.getName();
        String findByIdSql = findByIdSql(tableName, columnMap, clazz);
        String findSql = findSql(tableName, columnMap, clazz);
        String start_selectKey = "";
        String selectKeySql = "";
        String end_selectKey = "";
        if (!idIsString) {
            start_selectKey = "<selectKey keyProperty=\"entity.id\" resultType=\"long\">";
            selectKeySql = selectKeySql(tableName);
            end_selectKey = "</selectKey>";
        }
        String inserSql = insertSql(tableName, columns, paramColumns, clazz);
        String updateSql = updateSql(tableName, columnMap, clazz);
        String updateBatchSql = updateBatchSql(tableName, columnMap, clazz);

        String deleteSql;
        String deleteByIdSql;
        String deleteByIdsSql;
        String getMaxIdSql;
        StringBuilder getMaxIdSqlBuild = new StringBuilder();
        getMaxIdSqlBuild.append("select id from " + tableName);
        if (InterfaceBaseSplitModel.class.isAssignableFrom(clazz)) {
            getMaxIdSqlBuild.append("${suffix}");
        }
        getMaxIdSqlBuild.append(" order by id desc limit 1");
        getMaxIdSql = getMaxIdSqlBuild.toString();
        if (InterfaceBaseDeletedModel.class.isAssignableFrom(clazz)) {
            deleteSql = deleteDeletedSql(tableName, false, true, clazz);
            deleteByIdSql = deleteDeletedSql(tableName, false, false, clazz);
            deleteByIdsSql = deleteDeletedSql(tableName, true, false, clazz);
        } else {
            deleteSql = deleteSql(tableName, true, clazz);
            deleteByIdSql = deleteSql(tableName, false, clazz);
            StringBuilder deleteByIdsSqlBuilder = new StringBuilder();
            deleteByIdsSqlBuilder.append("delete from " + tableName);
            if (InterfaceBaseSplitModel.class.isAssignableFrom(clazz)) {
                deleteByIdsSqlBuilder.append("${suffix}");
            }
            deleteByIdsSqlBuilder.append(" WHERE id in <include refid=\"common.idsForEach\"/>");
            deleteByIdsSql = deleteByIdsSqlBuilder.toString();
        }
        JSONObject param = new JSONObject();
//		VelocityContext context = new VelocityContext();
        param.put("delete", deleteSql);
        param.put("deleteById", deleteByIdSql);
        param.put("deleteByIdsSql", deleteByIdsSql);

        param.put("getMaxIdSql", getMaxIdSql);

        param.put("findById", findByIdSql);
        param.put("findSql", findSql);
        param.put("start_selectKey", start_selectKey);
        param.put("selectKey", selectKeySql);
        param.put("end_selectKey", end_selectKey);
        param.put("insert", inserSql);
        param.put("update", updateSql);
        param.put("updateBatchSql", updateBatchSql);

        param.put("tableName", tableName);
        // 把model替换成Dao
        String daoClassName = entityClassName.replaceFirst("model|entity", "dao") + "Dao";

        param.put("daoClass", daoClassName);
        param.put("entityClass", entityClassName);
        if (clazz.isAnnotationPresent(CacheNamespace.class)) {
            //TODO: 缓存还没有实现
            param.put("cacheClass", "");
            param.put("cached", true);
        } else {
            param.put("cacheClass", "");
            param.put("cached", false);
        }
        String templateFile = PropUtil.getInstance().getConfig(templateFileKey, PropUtil.PUBLIC_CONF_JDBC);
        return VelocityUtils.getInstance().generateStringFromVelocity(param, templateFile);
    }

    private <T> String deleteDeletedSql(String tableName, boolean batch, boolean useAlias, Class<T> clazz) {
        StringBuilder updateSql = new StringBuilder();
        if (batch) {
            updateSql.append("update ").append(tableName);
            if (InterfaceBaseSplitModel.class.isAssignableFrom(clazz)) {
                updateSql.append("${suffix}");
            }
            updateSql.append(" set is_deleted=1 where id in <include refid=\"common.idsForEach\"/>");
        } else {
            updateSql.append("update ").append(tableName);
            if (InterfaceBaseSplitModel.class.isAssignableFrom(clazz)) {
                updateSql.append("${suffix}");
            }
            if (useAlias) {
                updateSql.append(" set is_deleted=1 where id=#{entity.id}");
            } else {
                updateSql.append(" set is_deleted=1 where id=#{id}");
            }
        }
        return updateSql.toString();
    }

    private String selectKeySql(String tableName) {
        return "SELECT LAST_INSERT_ID() AS ID";
    }


    /**
     * 功能:生成insert语句
     *
     * @param tableName
     * @param columns
     * @return
     */
    private <T> String insertSql(String tableName, List<String> columns, List<String> paramColumn, Class<T> clazz) {
        StringBuilder insertSql = new StringBuilder();
        StringBuilder valueSql = new StringBuilder();
        insertSql.append("insert into ").append(tableName);
        if (InterfaceBaseSplitModel.class.isAssignableFrom(clazz)) {
            insertSql.append("${suffix}");
        }
        insertSql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
        valueSql.append("<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >");
        for (int i = 0; i < columns.size(); i++) {
            String field = paramColumn.get(i);
            field = field.substring(0, field.indexOf(","));
            if (columns.get(i).toLowerCase().equals("id") || columns.get(i).toLowerCase().equals("create_time"))
                continue;
            /*空值默认不插入数据库*/

            insertSql.append(" <if test=\"entity.").append(field).append(" != null\"> ");
            insertSql.append(columns.get(i)).append(",");
            insertSql.append(" </if> ");

            valueSql.append(" <if test=\"entity.").append(field).append(" != null\"> ");
            valueSql.append("#{entity.").append(paramColumn.get(i)).append("},");
            valueSql.append(" </if> ");

        }


        insertSql.append("</trim>");
        valueSql.append("</trim>");
        insertSql.append(valueSql);
        return insertSql.toString();
    }


    /**
     * 功能:生成update语句
     *
     * @param tableName 表
     * @param columnMap 行
     * @return sql
     */
    private <T> String updateSql(String tableName, Map<String, String> columnMap, Class<T> clazz) {
        // <set>元素会动态前置 SET关键字,而且也会消除任意无关的逗号
        StringBuilder updateSql = updateFields(tableName, columnMap, clazz);
        if (pkList.size() > 0) {
            updateSql.append(pkWhereSqlStr(true));
        }
        return updateSql.toString();
    }

    private <T> StringBuilder updateFields(String tableName, Map<String, String> columnMap, Class<T> clazz) {
        StringBuilder updateSql = new StringBuilder();
        updateSql.append("update ").append(tableName);
        if (InterfaceBaseSplitModel.class.isAssignableFrom(clazz)) {
            updateSql.append("${suffix}");
        }
        updateSql.append(" <set> ");
        for (String key : columnMap.keySet()) {
            String value = columnMap.get(key);
            updateSql.append(" <if test=\"entity.").append(value).append(" != null\"> ");
            updateSql.append(key).append("=#{entity.").append(value).append("},");
            updateSql.append(" </if> ");

            //20140821修改， 如果为null，update时可将字段置空
            if (nullUpdatableList.get(key) != null) {
                updateSql.append(" <if test=\"entity.").append(value).append(" == null\"> ");
                updateSql.append(key).append("=null,");
                updateSql.append(" </if> ");
            }

        }

        updateSql.append(" </set> ");
        return updateSql;
    }

    private <T> StringBuilder updateFields2(String tableName, Map<String, String> columnMap, boolean useAlias, Class<T> clazz) {
        StringBuilder updateSql = new StringBuilder();
        updateSql.append("update ").append(tableName);
        if (InterfaceBaseSplitModel.class.isAssignableFrom(clazz)) {
            updateSql.append("${suffix}");
        }
        updateSql.append(" <set> ");
        for (Iterator<String> itor = columnMap.keySet().iterator(); itor.hasNext(); ) {
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

    private <T> String updateBatchSql(String tableName, Map<String, String> columnMap, Class<T> clazz) {
        StringBuilder updateSql = updateFields2(tableName, columnMap, true, clazz);
        updateSql.append(NEW_LINE_BREAK).append(" WHERE id in ");
        updateSql.append("<include refid=\"common.idsForEach\"/>");
        return updateSql.toString();
    }

    /**
     * 功能:生成findById语句
     *
     * @param tableName 表名
     * @param columnMap 行
     * @return sql
     */
    private <T> String findByIdSql(String tableName, Map<String, String> columnMap, Class<T> clazz) {
        StringBuilder findByIdSql = new StringBuilder();
        findByIdSql.append("select ");
        for (Iterator<String> itor = columnMap.keySet().iterator(); itor.hasNext(); ) {
            String key = itor.next();
            String value = columnMap.get(key);
            findByIdSql.append(key).append(" as ").append(value).append(",");
        }
        findByIdSql = new StringBuilder(findByIdSql.substring(0, findByIdSql.length() - 1));
        findByIdSql.append(" from ").append(tableName);
        if (InterfaceBaseSplitModel.class.isAssignableFrom(clazz)) {
            findByIdSql.append("${suffix}");
        }
        findByIdSql.append(pkWhereSqlStr(false));
        if (InterfaceBaseDeletedModel.class.isAssignableFrom(clazz)) {
            findByIdSql.append("AND is_deleted = 0");
        }
        findByIdSql.append(" limit 1");
        return findByIdSql.toString();
    }

    private <T> String findSql(String tableName, Map<String, String> columnMap, Class<T> clazz) {

        StringBuilder findSql = new StringBuilder();
        findSql.append("select ");
        for (Iterator<String> itor = columnMap.keySet().iterator(); itor.hasNext(); ) {
            String key = itor.next();
            String value = columnMap.get(key);
            findSql.append("t.").append(key).append(" as ").append(value).append(",");
        }
        findSql = new StringBuilder(findSql.substring(0, findSql.length() - 1));
        findSql.append(" from ").append(tableName);
        if (InterfaceBaseSplitModel.class.isAssignableFrom(clazz)) {
            findSql.append("${suffix}");
        }
        findSql.append(" t");
        findSql.append(NEW_LINE_BREAK).append("<if test=\"null != dp and null != dp.hasDatePerm and dp.hasDatePerm and null != dp.join and '' != dp.join\">");
        findSql.append(NEW_LINE_BREAK).append(" ${dp.join}");
        findSql.append(NEW_LINE_BREAK).append("</if>");

        findSql.append(NEW_LINE_BREAK).append("<where>");
        findSql.append(NEW_LINE_BREAK).append("<include refid=\"common.dynamicConditionsNoWhere\"/>");

        if (InterfaceBaseDeletedModel.class.isAssignableFrom(clazz)) {
            findSql.append(NEW_LINE_BREAK).append("AND t.is_deleted = 0");
        }

        findSql.append(NEW_LINE_BREAK).append("<if test=\"null != dp and null != dp.hasDatePerm and dp.hasDatePerm and null != dp.sql and '' != dp.sql\">");
        findSql.append(NEW_LINE_BREAK).append("${dp.sql}");
        findSql.append(NEW_LINE_BREAK).append("</if>");

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
     * 2012-11-16 下午5:57:48
     *
     * @param tableName
     * @return
     */
    private <T> String deleteSql(String tableName, boolean useAlias, Class<T> clazz) {
        StringBuilder deleteSql = new StringBuilder();
        deleteSql.append("delete from ").append(tableName);
        if (InterfaceBaseSplitModel.class.isAssignableFrom(clazz)) {
            deleteSql.append("${suffix}");
        }
        deleteSql.append(pkWhereSqlStr(useAlias));
        return deleteSql.toString();
    }

    /**
     * 主键where条件拼接
     *
     * @return
     */
    private String pkWhereSqlStr(boolean useAlias) {
        if (pkList.size() == 0)
            return "";
        StringBuilder pkStr = new StringBuilder();
        pkStr.append(" where ");
        for (String pk : pkList) {
            if (useAlias) {
                pkStr.append(pk).append("=").append("#{entity.").append(pk).append("}").append(" and ");
            } else {
                pkStr.append(pk).append("=").append("#{").append(pk).append("}").append(" and ");
            }
        }
        return pkStr.delete(pkStr.length() - 4, pkStr.length()).toString();
    }

    /**
     * 根据get方法名称生成基于mybatis的列名
     *
     * @param m
     * @return
     */
    private String generateColumn(Method m) {
        String mName = ClassUtils.getPropertyName(m);
        if (Short.class.isAssignableFrom(m.getReturnType())) {
            mName += ",jdbcType=BIGINT";
        } else if (Long.class.isAssignableFrom(m.getReturnType())) {
            mName += ",jdbcType=NUMERIC";
        } else if (Float.class.isAssignableFrom(m.getReturnType())) {
            mName += ",jdbcType=FLOAT";
        } else if (Double.class.isAssignableFrom(m.getReturnType())) {
            mName += ",jdbcType=DOUBLE";
        } else if (BigDecimal.class.isAssignableFrom(m.getReturnType())) {
            mName += ",jdbcType=DECIMAL";
        } else if (Integer.class.isAssignableFrom(m.getReturnType())) {
            mName += ",jdbcType=INTEGER";
        } else if (Byte.class.isAssignableFrom(m.getReturnType())) {
            mName += ",jdbcType=BINARY";
        } else if (Character.class.isAssignableFrom(m.getReturnType())) {
            mName += ",jdbcType=VARCHAR";
        } else if (Boolean.class.isAssignableFrom(m.getReturnType())) {
            mName += ",jdbcType=BOOLEAN";
        } else if (String.class.isAssignableFrom(m.getReturnType())) {
            mName += ",jdbcType=VARCHAR";
        } else if (Date.class.isAssignableFrom(m.getReturnType())) {
            mName += ",jdbcType=DATE";
        } else if (BaseIncrementIdModel.class.isAssignableFrom(m.getReturnType())) {
            mName += ".id,jdbcType=NUMERIC";
        }
        return mName;
    }

    private String generateColumn4Field(Field m) {
        String mName = m.getName();
        Class<?> t = m.getType();
        if (Short.class.isAssignableFrom(t)) {
            mName += ",jdbcType=BIGINT";
        } else if (Long.class.isAssignableFrom(t)) {
            mName += ",jdbcType=NUMERIC";
        } else if (Float.class.isAssignableFrom(t)) {
            mName += ",jdbcType=FLOAT";
        } else if (Double.class.isAssignableFrom(t)) {
            mName += ",jdbcType=DOUBLE";
        } else if (BigDecimal.class.isAssignableFrom(t)) {
            mName += ",jdbcType=DECIMAL";
        } else if (Integer.class.isAssignableFrom(t)) {
            mName += ",jdbcType=INTEGER";
        } else if (Byte.class.isAssignableFrom(t)) {
            mName += ",jdbcType=BINARY";
        } else if (Character.class.isAssignableFrom(t)) {
            mName += ",jdbcType=VARCHAR";
        } else if (Boolean.class.isAssignableFrom(t)) {
            mName += ",jdbcType=BOOLEAN";
        } else if (String.class.isAssignableFrom(t)) {
            mName += ",jdbcType=VARCHAR";
        } else if (Date.class.isAssignableFrom(t)) {
            mName += ",jdbcType=DATE";
        } else if (BaseIncrementIdModel.class.isAssignableFrom(t)) {
            mName += ".id,jdbcType=NUMERIC";
        }
        return mName;
    }
}
