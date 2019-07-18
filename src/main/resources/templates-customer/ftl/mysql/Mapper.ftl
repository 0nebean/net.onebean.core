<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--author ${author}-->
<!--description ${description} mapper-->
<!--date ${create_time}-->
<mapper namespace="${dao_package_name}.${model_name}Dao">

    <#if fieldArr?exists>
        <sql id="basicFiled">
                t.id,
            <#list fieldArr as item>
                t.${item.original_name}<#sep>, </#sep>
            </#list>
        </sql>

        <resultMap id="basicResultMap" type="${model_package_name}.${model_name}">
            <id column="id" jdbcType="INTEGER" property="id"/>
            <#list fieldArr as item>
                <result column="${item.original_name}" jdbcType="${item.jdbc_type}" property="${item.column_name}"/>
            </#list>
        </resultMap>
    </#if>


</mapper>