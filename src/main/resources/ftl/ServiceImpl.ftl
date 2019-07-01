package ${service_package_name}.impl;
import org.springframework.stereotype.Service;
<#if is_split_table == true>
import com.eakay.core.BaseSplitBizManual;
<#else>
import com.eakay.core.BaseBiz;
</#if>
import ${model_package_name}.${model_name};
import ${service_package_name}.${model_name}Service;
import ${dao_package_name}.${model_name}Dao;

/**
* @author ${author}
* @description ${description} serviceImpl
* @date ${create_time}
*/
@Service
<#if is_split_table == true>
public class ${model_name}ServiceImpl extends BaseSplitBizManual<${model_name}, ${model_name}Dao> implements ${model_name}Service{
<#else>
public class ${model_name}ServiceImpl extends BaseBiz<${model_name}, ${model_name}Dao> implements ${model_name}Service{
</#if>


}