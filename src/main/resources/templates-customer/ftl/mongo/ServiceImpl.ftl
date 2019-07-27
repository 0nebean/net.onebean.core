package ${service_package_name}.impl.mongo;

import org.springframework.stereotype.Service;
import net.onebean.core.mongo.base.BaseMongoBiz;
import ${model_package_name}.mongo.${model_name};
import ${service_package_name}.mongo.${model_name}Service;
import ${dao_package_name}.mongo.${model_name}Dao;

/**
* @author ${author}
* @description ${description} serviceImpl
* @date ${create_time}
*/
@Service
public class ${model_name}ServiceImpl extends BaseMongoBiz<${model_name}, ${model_name}Dao> implements ${model_name}Service{

}

