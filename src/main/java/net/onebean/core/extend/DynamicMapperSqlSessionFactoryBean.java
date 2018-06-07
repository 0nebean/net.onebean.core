package net.onebean.core.extend;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.onebean.util.PropUtil;
import org.apache.commons.lang.ArrayUtils;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import net.onebean.core.extend.codebuilder.MybatisCRUDBuilder;
import net.onebean.core.model.BaseIncrementIdModel;
import net.onebean.core.model.BaseModel;
import net.onebean.util.ClassUtils;


/**
 * 继承自SqlSessionFactoryBean类，实现Mapper基本操作模块化功能
 * 	在一般系统中，每个Model都会有部分基本操作，这些操作模式基本相同，所以在我们系统内部将其抽象出来，通过动态的生成Mapper的形式注入到Mybatis中。
 * 	对于生成的mapper文件，在setMapperLocations(Resource[])方法中加入到Factory map locations中，让Ibatis自行处理。
 * @see SqlSessionFactoryBean
 * @see 'ScanBaseModelUtil'
 */
public class DynamicMapperSqlSessionFactoryBean extends SqlSessionFactoryBean {
	

	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicMapperSqlSessionFactoryBean.class);
	/**
	 * 再此方法中加入了动态mapper 文件插入
	 * 
	 * @param mapperLocations mapper 资源文件列表
	 */
	@Override
	public void setMapperLocations(Resource[] mapperLocations) {
		//生成mapper资源文件对象
		Resource[] resources = productModelMapperResources();
		//将原始source和生成的source整合
		resources = addResourcesByArray(mapperLocations,resources);
		//开始加载
		//------------------重写
		super.setMapperLocations(resources);
	}
	
	/**
	 * 将一个单独的resource mapper资源文件加入到resource资源文件列表中
	 * @param sources 资源文件列表
	 * @param resource 资源文件
	 * @return 添加后的资源
	 */
	private Resource[] addResourcesByArray(Resource[] sources,Resource... resource){
		Resource[] copy = new Resource[sources.length + resource.length];
		LOGGER.debug("add resource length " + resource.length);
		int i = 0;
		for(;i < sources.length;i ++){
			copy[i] = sources[i];
		}
		for(;i < copy.length;i ++){
			copy[i] = resource[i - sources.length];
		}
		return copy;
	}
	/**
	 * 根据当前model类，自动生成mapper对应的基本文件
	 * @return
	 */
	private Resource[] productModelMapperResources(){

		List<Class<?>> list = getAllBaseClass();
		LOGGER.debug("classes size is " + list.size());
		//根据class生成相应的Mapper文件
		Resource[] resources = new Resource[list.size()];
		//开始生成
		for(int i = 0;i < list.size();i ++){
			try {
				resources[i] = productModel(list.get(i));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return resources;
	} 
	/**
	 * 获取JVM中所有Model类列表
	 * @return Model类列表
	 */
	private List<Class<?>> getAllBaseClass(){
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader());
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
		List<Class<?>> classList = new ArrayList<Class<?>>();
		try {
			Resource[] rs_model = resolver.getResources(PropUtil.getConfig("org.mybaits.jvm.model.class.classpath"));
			// by tangmingbao 为了支持移动端数据上报 而添加，此处model的匹配规则应该是可配置
			Resource[] rs_entity = resolver.getResources(PropUtil.getConfig("org.mybaits.jvm.entity.class.classpath"));

			Resource[] rs=(Resource[]) ArrayUtils.addAll(rs_model,rs_entity);
			
			for(Resource resource : rs){								
				String className = metadataReaderFactory.getMetadataReader(resource).getClassMetadata().getClassName();				
				Class<?> clazz;
				try {
					clazz = Class.forName(className);
					//判断当前cl是否是BaseModel的子类，且不是BaseModel
					if(BaseIncrementIdModel.class.isAssignableFrom(clazz)){
						if(clazz != BaseIncrementIdModel.class && clazz != BaseModel.class)
						{
						classList.add(clazz);
						}
					}else					
					{
						if(ClassUtils.hasAnnotation(clazz, "Table"))
						{
							classList.add(clazz);
						}
						
						
					}
				} catch (ClassNotFoundException e) {
					LOGGER.error("实体" + className +"应该有一个无参的构造函数...");
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classList;
	}
	/**
	 * 根据ModelClass生成对应的Mapper基本文件
	 * @param cl Model class
	 * @return Mapper文件对象
	 * @throws UnsupportedEncodingException 
	 */
	private Resource productModel(Class<?> cl) throws UnsupportedEncodingException{		
		String data = null;
		try{
			data = new MybatisCRUDBuilder().buildByClass(cl);
		}catch(Exception e){
			LOGGER.debug("generate sql mapp  by class " + cl.getName() + "  error ",e);
			
			throw new IllegalArgumentException(e);
		}
		LOGGER.debug(cl.getName() + " is \n" + data);
		return new InputStreamResource(new ByteArrayInputStream(data.getBytes("UTF-8")),cl.getName());
	}
	

}
