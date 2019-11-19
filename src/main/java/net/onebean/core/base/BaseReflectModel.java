package net.onebean.core.base;

import org.springframework.beans.BeanUtils;

/**
 * 用于提供函数式的反射方法
 * @author 0neBean
 */
public class BaseReflectModel {

    /**
     * 反射成指定了类型
     * @param target 目标
     * @param <T> 类型
     * @return t
     */
    public <T> T reflectToBean(T target){
        BeanUtils.copyProperties(this,target);
        return target;
    }
}
