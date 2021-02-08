package ${model_package_name};
import FiledName;
import TableName;
import BaseModel;

<#if is_split_table == true>
import InterfaceBaseSplitModel;
<#elseif is_split_table == false && logically_delete == true>
import InterfaceBaseDeletedModel;
<#else>
import InterfaceBaseModel;
</#if>

<#if fieldArr?exists>
    <#list fieldArr as item>
        <#if item.column_type == 'BigDecimal'>
import java.math.BigDecimal;
            <#break>
        </#if>
    </#list>
</#if>

import java.sql.Timestamp;

/**
* @author ${author}
* @description ${description} model
* @date ${create_time}
*/
<#if is_split_table == true>
@TableName("${table_prefix}")
<#else>
@TableName("${table_name}")
</#if>
<#if is_split_table == true>
public class ${model_name} extends BaseModel implements InterfaceBaseSplitModel {
<#elseif is_split_table == false && logically_delete == true>
public class ${model_name} extends BaseModel implements InterfaceBaseDeletedModel {
<#else>
public class ${model_name} extends BaseModel implements InterfaceBaseModel {
</#if>



<#if fieldArr?exists>
    <#list fieldArr as item>
        <#if item.column_name != 'id'>
        /**
        * ${item.comment}
        */
        private ${item.column_type} ${item.column_name};
        @FiledName("${item.original_name}")
        public ${item.column_type} get${item.method_name}(){
            return this.${item.column_name};
        }
        public void set${item.method_name}(${item.column_type} ${item.column_name}){
            this.${item.column_name} = ${item.column_name};
        }


        </#if>
    </#list>
</#if>


}