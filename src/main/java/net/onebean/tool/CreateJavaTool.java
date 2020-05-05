package net.onebean.tool;

import net.onebean.util.DateUtils;
import net.onebean.util.FreeMarkerTemplateUtils;
import net.onebean.util.PropUtil;
import net.onebean.util.StringUtils;
import freemarker.template.Template;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateJavaTool {
    private JFrame frame;
    private JTextField tabName_text;
    private JTextField modelName_text;
    private JTextField modelPath_text;
    private JTextField daoPath_text;
    private JTextField servicePath_text;
    private JTextField author_text;
    private JTextField description_text;
    private JTextField table_prefix;
    private JToggleButton is_split_table;
    private JToggleButton logically_delete;


    private String projectPath;
    private String mapperPath;
    private String daoPath;
    private String servicePath;
    private String serviceImplPath;
    private String modelPath;
    private String daoPackageName;
    private String modelPackageName;
    private String servicePackageName;
    private String createTimeStr;
    private String driverClassName;
    private String databaseurl;
    private String databaseUsername;
    private String databasePassword;

    private final static String CHARSET_STR ="utf-8";

    private static final int WIN_WIDTH = 525;
    private static final int WIN_HEIGHT = 460;

    public CreateJavaTool(){
        init();
    }

    private void init(){
        frame = new JFrame();
        frame.setSize(WIN_WIDTH, WIN_HEIGHT);
        frame.setTitle("代码生成工具 by 0neBean");
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        frame.setLocation(screenWidth / 2 - WIN_WIDTH / 2, screenHeight / 2 - WIN_HEIGHT / 2);
        frame.setLayout(null);

        JLabel lab = new JLabel("表名:");
        lab.setBounds(20, 20, 100, 30);
        frame.add(lab);
        tabName_text = new  JTextField(30);
        tabName_text.setBounds(100,20,375,30);
        frame.add(tabName_text);

        lab = new JLabel("Model名称:");
        lab.setBounds(20, 50, 100, 30);
        frame.add(lab);
        modelName_text = new  JTextField(30);
        modelName_text.setBounds(100,50,375,30);
        frame.add(modelName_text);

        lab = new JLabel("作者信息:");
        lab.setBounds(20, 80, 100, 30);
        frame.add(lab);
        author_text = new JTextField(30);
        author_text.setBounds(100,80,375,30);
        frame.add(author_text);

        lab = new JLabel("Model注释:");
        lab.setBounds(20, 110, 100, 30);
        frame.add(lab);
        description_text = new JTextField(30);
        description_text.setBounds(100,110,375,30);
        frame.add(description_text);


        lab = new JLabel("Model路径:");
        lab.setBounds(20, 140, 100, 30);
        frame.add(lab);
        modelPath_text = new  JTextField(30);
        modelPath_text.setBounds(100,140,375,30);
        frame.add(modelPath_text);

        lab = new JLabel("Dao路径:");
        lab.setBounds(20, 170, 100, 30);
        frame.add(lab);
        daoPath_text = new  JTextField(30);
        daoPath_text.setBounds(100,170,375,30);
        frame.add(daoPath_text);

        lab = new JLabel("Service路径:");
        lab.setBounds(20, 200, 100, 30);
        frame.add(lab);
        servicePath_text = new JTextField(30);
        servicePath_text.setBounds(100,200,375,30);
        frame.add(servicePath_text);

        is_split_table = new JToggleButton();
        lab = new JLabel("是否分表:");
        lab.setBounds(20, 230, 100, 30);
        frame.add(lab);
        is_split_table.setBorderPainted(false);
        is_split_table.setText("否");
        is_split_table.setBounds(100,230,375,30);
        setToggleButtonListenerText(is_split_table,"是(已包含逻辑删除)","否");
        is_split_table.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println(e);
                // 获取事件源（即开关按钮本身）
                JToggleButton toggleBtn = (JToggleButton) e.getSource();
                if (toggleBtn.isSelected()){
                    table_prefix.setEnabled(true);
                    is_split_table.setText("是 (已包含逻辑删除)");
                    logically_delete.setBorderPainted(false);
                    logically_delete.setEnabled(false);
                    logically_delete.setText("否");
                }else{
                    table_prefix.setEnabled(false);
                    table_prefix.setText("");
                    is_split_table.setText("否");
                    logically_delete.setEnabled(true);
                    logically_delete.setSelected(false);
                }
            }
        });
        frame.add(is_split_table);


        lab = new JLabel("表名前缀:");
        lab.setBounds(20, 260, 100, 30);
        frame.add(lab);
        table_prefix = new JTextField(30);
        table_prefix.setBounds(100,260,375,30);
        table_prefix.setEnabled(false);
        frame.add(table_prefix);

        logically_delete = new JToggleButton();
        lab = new JLabel("是否逻辑删除:");
        lab.setBounds(20, 290, 100, 30);
        frame.add(lab);
        logically_delete.setBorderPainted(false);
        logically_delete.setText("否");
        logically_delete.setBounds(100,290,375,30);
        setToggleButtonListenerText(logically_delete,"是","否");
        frame.add(logically_delete);

        JButton btn = new JButton("确定");
        btn.setBounds(WIN_WIDTH/2 - 50 / 2, 340, 80, 30);
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generate();
            }
        });
        frame.add(btn);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter()  {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        initPath();
    }

    private void initPath(){
        projectPath = getProjectPath();
        mapperPath = PropUtil.getInstance().getConfig("apache.freemarker.model.mapper.path", PropUtil.PUBLIC_CONF_FREEMARKER);
        daoPath = PropUtil.getInstance().getConfig("apache.freemarker.dao.path", PropUtil.PUBLIC_CONF_FREEMARKER);
        servicePath = PropUtil.getInstance().getConfig("apache.freemarker.service.path", PropUtil.PUBLIC_CONF_FREEMARKER);
        serviceImplPath = PropUtil.getInstance().getConfig("apache.freemarker.service.impl.path", PropUtil.PUBLIC_CONF_FREEMARKER);
        modelPath = PropUtil.getInstance().getConfig("apache.freemarker.model.path", PropUtil.PUBLIC_CONF_FREEMARKER);
        daoPackageName = PropUtil.getInstance().getConfig("apache.freemarker.dao.packagename", PropUtil.PUBLIC_CONF_FREEMARKER);
        modelPackageName = PropUtil.getInstance().getConfig("apache.freemarker.model.packagename", PropUtil.PUBLIC_CONF_FREEMARKER);
        servicePackageName = PropUtil.getInstance().getConfig("apache.freemarker.service.packagename", PropUtil.PUBLIC_CONF_FREEMARKER);

        driverClassName= PropUtil.getInstance().getConfig("spring.datasource.driver-class-name", PropUtil.PUBLIC_CONF_JDBC);
        databaseurl= PropUtil.getInstance().getConfig("spring.datasource.url", PropUtil.PUBLIC_CONF_JDBC);
        databaseUsername= PropUtil.getInstance().getConfig("spring.datasource.username", PropUtil.PUBLIC_CONF_JDBC);
        databasePassword= PropUtil.getInstance().getConfig("spring.datasource.password", PropUtil.PUBLIC_CONF_JDBC);

        createTimeStr = DateUtils.getNowyyyy_MM_dd_HH_mm_ss();
        daoPath_text.setText(daoPath);
        modelPath_text.setText(modelPath);
        servicePath_text.setText(servicePath);
    }
    /**
     * 返回项目路径
     * @return
     */
    private static String getProjectPath(){
        String proPath = System.getProperty("user.dir");
        proPath = proPath.replaceAll("\\\\", "\\\\\\\\");
        return proPath;
    }

    /**
     * 生成代码
     */
    private void generate() {
        try {
            if(StringUtils.isEmpty(tabName_text.getText())){
                JOptionPane.showMessageDialog(frame, "表名不能为空");
                return;
            }
            if(StringUtils.isEmpty(modelName_text.getText())){
                JOptionPane.showMessageDialog(frame, "Model名称不能为空");
                return;
            }
            if(StringUtils.isEmpty(author_text.getText())){
                JOptionPane.showMessageDialog(frame, "作者信息不能为空");
                return;
            }
            if(StringUtils.isEmpty(description_text.getText())){
                JOptionPane.showMessageDialog(frame, "注释不能为空");
                return;
            }
            List<Map<String,Object>> fields = coverField(getColumns(tabName_text.getText()));
            generateModelFile(fields);//生成model文件
            generateDaoFile();//生成Dao文件
            generateMapperFile(fields);//生成Mapper文件
            generateServiceFile();//生成Service文件
            generateServiceImplFile();//生成ServiceImpl文件

            JOptionPane.showMessageDialog(frame, "创建完成，刷新目录查看");
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateMapperFile(List<Map<String,Object>> columns) throws Exception{
        final String suffix = "Mapper.xml";
        final String path = projectPath+mapperPath+modelName_text.getText()+suffix;
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("model_package_name",modelPackageName);
        dataMap.put("dao_package_name",daoPackageName);
        dataMap.put("model_name",modelName_text.getText());
        dataMap.put("fieldArr",columns);
        dataMap.put("author",author_text.getText());
        dataMap.put("description",description_text.getText());
        dataMap.put("create_time",createTimeStr);
        generateFileByTemplate("/mysql/Mapper.ftl",mapperFile,dataMap);
    }

    private void generateDaoFile() throws Exception{
        final String suffix = "Dao.java";
        final String path = projectPath+daoPath+modelName_text.getText()+suffix;
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("is_split_table",is_split_table.isSelected());
        dataMap.put("dao_package_name",daoPackageName);
        dataMap.put("model_package_name",modelPackageName);
        dataMap.put("model_name",modelName_text.getText());
        dataMap.put("author",author_text.getText());
        dataMap.put("description",description_text.getText());
        dataMap.put("create_time",createTimeStr);
        generateFileByTemplate("/mysql/Dao.ftl",mapperFile,dataMap);
    }

    private void generateServiceFile() throws Exception{
        final String suffix = "Service.java";
        final String path = projectPath+servicePath+modelName_text.getText()+suffix;
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("is_split_table",is_split_table.isSelected());
        dataMap.put("service_package_name",servicePackageName);
        dataMap.put("model_package_name",modelPackageName);
        dataMap.put("model_name",modelName_text.getText());
        dataMap.put("author",author_text.getText());
        dataMap.put("description",description_text.getText());
        dataMap.put("create_time",createTimeStr);
        generateFileByTemplate("/mysql/Service.ftl",mapperFile,dataMap);
    }

    private void generateServiceImplFile() throws Exception{
        final String suffix = "ServiceImpl.java";
        final String path = projectPath+serviceImplPath+modelName_text.getText()+suffix;
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("is_split_table",is_split_table.isSelected());
        dataMap.put("dao_package_name",daoPackageName);
        dataMap.put("service_package_name",servicePackageName);
        dataMap.put("model_package_name",modelPackageName);
        dataMap.put("model_name",modelName_text.getText());
        dataMap.put("author",author_text.getText());
        dataMap.put("description",description_text.getText());
        dataMap.put("create_time",createTimeStr);
        generateFileByTemplate("/mysql/ServiceImpl.ftl",mapperFile,dataMap);
    }

    private void generateModelFile(List<Map<String,Object>> columns) throws Exception{
        final String suffix = ".java";
        final String path = projectPath+modelPath+modelName_text.getText()+suffix;
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("table_prefix",table_prefix.getText());
        dataMap.put("is_split_table",is_split_table.isSelected());
        dataMap.put("logically_delete",logically_delete.isSelected());
        dataMap.put("model_package_name",modelPackageName);
        dataMap.put("model_name",modelName_text.getText());
        dataMap.put("table_name",tabName_text.getText());
        dataMap.put("fieldArr",columns);
        dataMap.put("author",author_text.getText());
        dataMap.put("description",description_text.getText());
        dataMap.put("create_time",createTimeStr);
        generateFileByTemplate("/mysql/Model.ftl",mapperFile,dataMap);
    }

    private List<Map<String,Object>> coverField(List<Map<String,Object>> columns){
        List<Map<String,Object>> res = new ArrayList<>();
        for (Map<String, Object> column : columns) {
            Map<String, Object> resMap = new HashMap<>();
            String field = column.get("name").toString().toLowerCase();

            if(field.equals("id")) continue;
            String type = column.get("type").toString();
            String classes = "String";
            if(type.contains("int")){
                classes = "Integer";
            }
            if(type.contains("bigint")){
                classes = "Long";
            }
            if(type.contains("numeric") || type.contains("double")){
                classes = "Double";
            }
            if(type.contains("decimal")){
                classes = "BigDecimal";
            }
            if(type.contains("float")){
                classes = "Float";
            }
            if(type.contains("time") || type.contains("date")){
                classes = "Timestamp";
            }


            resMap.put("original_name",field);
            String method_name = StringUtils.replaceUnderLineAndUpperCase(field);
            field = method_name.substring(0, 1).toLowerCase() + method_name.substring(1);
            resMap.put("jdbc_type",getJdbcType(type));
            resMap.put("column_type",classes);
            resMap.put("column_name",field);
            resMap.put("method_name",method_name);
            resMap.put("comment",column.get("comment").toString());
            res.add(resMap);
        }
        return res;
    }

    /*获取jdbcType*/
    private String getJdbcType(String databaseType){
        String jdbcType = "";
        databaseType = databaseType.toUpperCase();
        if(databaseType.equals("ARRAY")){
            jdbcType = "ARRAY";
        }
        if(databaseType.equals("BIT")){
            jdbcType = "BIT";
        }
        if(databaseType.equals("TINYINT")){
            jdbcType = "TINYINT";
        }
        if(databaseType.equals("SMALLINT")){
            jdbcType = "SMALLINT";
        }
        if(databaseType.equals("INT") || databaseType.equals("MEDIUMINT")){
            jdbcType = "INTEGER";
        }
        if(databaseType.equals("BIGINT")){
            jdbcType = "BIGINT";
        }
        if(databaseType.equals("FLOAT")){
            jdbcType = "FLOAT";
        }
        if(databaseType.equals("REAL")){
            jdbcType = "REAL";
        }
        if(databaseType.equals("DOUBLE")){
            jdbcType = "DOUBLE";
        }
        if(databaseType.equals("NUMERIC")){
            jdbcType = "NUMERIC";
        }
        if(databaseType.equals("DECIMAL")){
            jdbcType = "DECIMAL";
        }
        if(databaseType.equals("CHAR")){
            jdbcType = "CHAR";
        }
        if(databaseType.equals("VARCHAR")){
            jdbcType = "VARCHAR";
        }
        if(databaseType.equals("LONGVARCHAR")){
            jdbcType = "LONGVARCHAR";
        }
        if(databaseType.equals("DATE")){
            jdbcType = "DATE";
        }
        if(databaseType.equals("TIME")){
            jdbcType = "TIME";
        }
        if(databaseType.equals("TIMESTAMP")){
            jdbcType = "TIMESTAMP";
        }
        if(databaseType.equals("BINARY")){
            jdbcType = "BINARY";
        }
        if(databaseType.equals("VARBINARY")){
            jdbcType = "VARBINARY";
        }
        if(databaseType.equals("LONGVARBINARY")){
            jdbcType = "LONGVARBINARY";
        }
        if(databaseType.equals("NULL")){
            jdbcType = "NULL";
        }
        if(databaseType.equals("OTHER")){
            jdbcType = "OTHER";
        }
        if(databaseType.equals("BLOB")){
            jdbcType = "BLOB";
        }
        if(databaseType.equals("CLOB")){
            jdbcType = "CLOB";
        }
        if(databaseType.equals("BOOLEAN")){
            jdbcType = "BOOLEAN";
        }
        if(databaseType.equals("CURSOR")){
            jdbcType = "CURSOR";
        }
        if(databaseType.equals("UNDEFINED")){
            jdbcType = "UNDEFINED";
        }
        if(databaseType.equals("NVARCHAR")){
            jdbcType = "NVARCHAR";
        }
        if(databaseType.equals("NCHAR")){
            jdbcType = "NCHAR";
        }
        if(databaseType.equals("NCLOB")){
            jdbcType = "NCLOB";
        }
        if(databaseType.equals("STRUCT")){
            jdbcType = "STRUCT";
        }
        if(databaseType.equals("JAVA_OBJECT")){
            jdbcType = "JAVA_OBJECT";
        }
        if(databaseType.equals("DISTINCT")){
            jdbcType = "DISTINCT";
        }
        if(databaseType.equals("REF")){
            jdbcType = "REF";
        }
        if(databaseType.equals("DATALINK")){
            jdbcType = "DATALINK";
        }
        if(databaseType.equals("ROWID")){
            jdbcType = "ROWID";
        }
        if(databaseType.equals("LONGNVARCHAR")){
            jdbcType = "LONGNVARCHAR";
        }
        if(databaseType.equals("SQLXML")){
            jdbcType = "SQLXML";
        }
        if(databaseType.equals("DATETIMEOFFSET")){
            jdbcType = "DATETIMEOFFSET";
        }
        return jdbcType;
    }


    /**
     * 根据ftl生成代码方法
     * @param templateName ftl模板名
     * @param file 文件
     * @param dataMap 渲染数据
     * @throws Exception 向上抛出异常
     */
    private void generateFileByTemplate(final String templateName,File file,Map<String,Object> dataMap) throws Exception{
        Template template = FreeMarkerTemplateUtils.getTemplate(templateName);
        FileOutputStream fos = new FileOutputStream(file);
        Writer out = new BufferedWriter(new OutputStreamWriter(fos, CHARSET_STR),10240);
        template.process(dataMap,out);
        fos.close();
        out.close();
    }

    public Connection getJdbcConnect(){
        if(null == driverClassName){
            JOptionPane.showMessageDialog(frame, "连接数据库失败,请检查是否配置Apollo参数");
            System.exit(0);
        }

        try {
            Class.forName(driverClassName) ;
            return DriverManager.getConnection(databaseurl, databaseUsername, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String,Object>> getColumns(String tableName){
        String sql = "show full fields from "+tableName;
        Connection con = getJdbcConnect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            while(rs.next()){
                Map<String,Object> m = new HashMap<String, Object>();
                m.put("name", rs.getString("Field"));
                m.put("comment", rs.getString("Comment"));
                String jdbcType = rs.getString("Type");
                if (StringUtils.isNotEmpty(jdbcType) && jdbcType.contains("(")){
                    jdbcType = jdbcType.substring(0,jdbcType.indexOf("("));
                }
                m.put("type", jdbcType);
                list.add(m);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closeAll(con,pstmt,rs);
        }
        return null;
    }

    public void closeAll(Connection con, PreparedStatement pstmt,ResultSet rs){
        try {
            if(con != null && !con.isClosed()){
                con.close();
                con = null;
            }
            if(pstmt != null && !pstmt.isClosed()){
                pstmt.close();
                pstmt = null;
            }
            if(rs != null && !rs.isClosed()){
                rs.close();
                rs  = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setToggleButtonListenerText(AbstractButton abstractButton,String trueText,String falseText){
        abstractButton.addChangeListener(e -> {
            // 获取事件源（即开关按钮本身）
            JToggleButton toggleBtn = (JToggleButton) e.getSource();
            if (toggleBtn.isSelected()){
                abstractButton.setText(trueText);
            }else{
                abstractButton.setText(falseText);
            }
        });
    }


    public static void runIt(){
        new CreateJavaTool();
    }
}