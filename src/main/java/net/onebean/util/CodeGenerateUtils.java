package net.onebean.util;


import freemarker.template.Template;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成代码 工具类
 * @author 0neBean
 */
public class CodeGenerateUtils {

    private final String projectPath = getProjectPath();
    private final String mapperPath = PropUtil.getConfig("apache.freemarker.model.mapper.path");
    private final String daoPath = PropUtil.getConfig("apache.freemarker.dao.path");
    private final String servicePath = PropUtil.getConfig("apache.freemarker.service.path");
    private final String serviceImplPath = PropUtil.getConfig("apache.freemarker.service.impl.path");
    private final String actionPath = PropUtil.getConfig("apache.freemarker.action.path");
    private String tablename = "";

    /**
     * 生成代码方法入口
     * @param tablename
     * @throws Exception
     */
    public void generate(String tablename) throws Exception{
        try {
            this.tablename = StringUtils.replaceUnderLineAndUpperCase(tablename);
            generateMapperFile();//生成Mapper文件
            generateDaoFile();//生成Dao文件
            generateServiceFile();//生成Service文件
            generateServiceImplFile();//生成ServiceImpl文件
            generateActionFile();//生成action文件
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally{

        }
    }

    /**
     * 返回项目路径
     * @return
     */
    protected static String getProjectPath(){
        String proPath = System.getProperty("user.dir");
        proPath = proPath.replaceAll("\\\\", "\\\\\\\\");
        proPath += PropUtil.getConfig("apache.freemarker.project.name");
        return proPath;
    }

    private void generateMapperFile() throws Exception{
        final String suffix = "Mapper.xml";
        final String path = projectPath+mapperPath+tablename+suffix;
        final String templateName = "Mapper.ftl";
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("dao_package_name",PropUtil.getConfig("apache.freemarker.dao.packagename"));
        dataMap.put("table_name",tablename);
        generateFileByTemplate(templateName,mapperFile,dataMap);
    }


    private void generateDaoFile() throws Exception{
        final String suffix = "Dao.java";
        final String path = projectPath+daoPath+tablename+suffix;
        final String templateName = "Dao.ftl";
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("dao_package_name",PropUtil.getConfig("apache.freemarker.dao.packagename"));
        dataMap.put("model_package_name",PropUtil.getConfig("apache.freemarker.model.packagename"));
        dataMap.put("table_name",tablename);
        generateFileByTemplate(templateName,mapperFile,dataMap);
    }


    private void generateServiceFile() throws Exception{
        final String suffix = "Service.java";
        final String path = projectPath+servicePath+tablename+suffix;
        final String templateName = "Service.ftl";
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("service_package_name",PropUtil.getConfig("apache.freemarker.service.packagename"));
        dataMap.put("model_package_name",PropUtil.getConfig("apache.freemarker.model.packagename"));
        dataMap.put("table_name",tablename);
        generateFileByTemplate(templateName,mapperFile,dataMap);
    }


    private void generateServiceImplFile() throws Exception{
        final String suffix = "ServiceImpl.java";
        final String path = projectPath+serviceImplPath+tablename+suffix;
        final String templateName = "ServiceImpl.ftl";
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("dao_package_name",PropUtil.getConfig("apache.freemarker.dao.packagename"));
        dataMap.put("service_package_name",PropUtil.getConfig("apache.freemarker.service.packagename"));
        dataMap.put("model_package_name",PropUtil.getConfig("apache.freemarker.model.packagename"));
        dataMap.put("table_name",tablename);
        generateFileByTemplate(templateName,mapperFile,dataMap);
    }


    private void generateActionFile() throws Exception{
        final String suffix = "Controller.java";
        final String path = projectPath+actionPath+tablename+suffix;
        final String templateName = "Controller.ftl";
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("dao_package_name",PropUtil.getConfig("apache.freemarker.dao.packagename"));
        dataMap.put("service_package_name",PropUtil.getConfig("apache.freemarker.service.packagename"));
        dataMap.put("model_package_name",PropUtil.getConfig("apache.freemarker.model.packagename"));
        dataMap.put("action_package_name",PropUtil.getConfig("apache.freemarker.action.packagename"));
        dataMap.put("table_name",tablename);
        generateFileByTemplate(templateName,mapperFile,dataMap);
    }


    /**
     * 根据ftl生成代码方法
     * @param templateName ftl模板名
     * @param file 文件
     * @param dataMap 渲染数据
     * @throws Exception
     */
    private void generateFileByTemplate(final String templateName,File file,Map<String,Object> dataMap) throws Exception{
        Template template = FreeMarkerTemplateUtils.getTemplate(templateName);
        FileOutputStream fos = new FileOutputStream(file);
        Writer out = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"),10240);
        template.process(dataMap,out);
    }


}
