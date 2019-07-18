package net.onebean.tool;

import freemarker.template.Template;
import net.onebean.util.DateUtils;
import net.onebean.util.FreeMarkerTemplateUtils;
import net.onebean.util.PropUtil;
import net.onebean.util.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class CreateMongoJavaTool {
	private JFrame frame;
	private JTextField modelName_text;
	private JTextField modelPath_text;
	private JTextField daoPath_text;
	private JTextField servicePath_text;
	private JTextField author_text;
	private JTextField description_text;


	private String projectPath;
	private String daoPath;
	private String servicePath;
	private String serviceImplPath;
	private String modelPath;
	private String daoPackageName;
	private String modelPackageName;
	private String servicePackageName;
	private String createTimeStr;

	private final static String CHARSET_STR ="utf-8";

	private static final int WIN_WIDTH = 525;
	private static final int WIN_HEIGHT = 460;

	public CreateMongoJavaTool(){
		init();
	}

	private void init(){
		frame = new JFrame();
		frame.setSize(WIN_WIDTH, WIN_HEIGHT);
		frame.setTitle("代码生成工具 by 0neBean - mongoDB version");
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		frame.setLocation(screenWidth / 2 - WIN_WIDTH / 2, screenHeight / 2 - WIN_HEIGHT / 2);
		frame.setLayout(null);

		JLabel lab = new JLabel("Model名称:");
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
		daoPath = PropUtil.getInstance().getConfig("apache.freemarker.dao.path", PropUtil.PUBLIC_CONF_FREEMARKER)+"mongo\\";
		servicePath = PropUtil.getInstance().getConfig("apache.freemarker.service.path", PropUtil.PUBLIC_CONF_FREEMARKER)+"mongo\\";
		serviceImplPath = PropUtil.getInstance().getConfig("apache.freemarker.service.impl.path", PropUtil.PUBLIC_CONF_FREEMARKER)+"mongo\\";
		modelPath = PropUtil.getInstance().getConfig("apache.freemarker.model.path", PropUtil.PUBLIC_CONF_FREEMARKER)+"mongo\\";
		daoPackageName = PropUtil.getInstance().getConfig("apache.freemarker.dao.packagename", PropUtil.PUBLIC_CONF_FREEMARKER);
		modelPackageName = PropUtil.getInstance().getConfig("apache.freemarker.model.packagename", PropUtil.PUBLIC_CONF_FREEMARKER);
		servicePackageName = PropUtil.getInstance().getConfig("apache.freemarker.service.packagename", PropUtil.PUBLIC_CONF_FREEMARKER);

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
			generateModelFile();//生成model文件
			generateDaoFile();//生成Dao文件
			generateServiceFile();//生成Service文件
			generateServiceImplFile();//生成ServiceImpl文件

			JOptionPane.showMessageDialog(frame, "创建完成，刷新目录查看");
			System.exit(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void generateDaoFile() throws Exception{
		final String suffix = "Dao.java";
		final String path = projectPath+daoPath+modelName_text.getText()+suffix;
		File mapperFile = new File(path);
		Map<String,Object> dataMap = new HashMap<>();
		dataMap.put("dao_package_name",daoPackageName);
		dataMap.put("model_package_name",modelPackageName);
		dataMap.put("model_name",modelName_text.getText());
		dataMap.put("author",author_text.getText());
		dataMap.put("description",description_text.getText());
		dataMap.put("create_time",createTimeStr);
		File pathCheck = new File(projectPath+daoPath);
		if  (!pathCheck .exists()  && !pathCheck .isDirectory()){
			pathCheck.mkdir();
		}
		generateFileByTemplate("mongo/Dao.ftl",mapperFile,dataMap);
	}

	private void generateServiceFile() throws Exception{
		final String suffix = "Service.java";
		final String path = projectPath+servicePath+modelName_text.getText()+suffix;
		File mapperFile = new File(path);
		Map<String,Object> dataMap = new HashMap<>();
		dataMap.put("service_package_name",servicePackageName);
		dataMap.put("model_package_name",modelPackageName);
		dataMap.put("model_name",modelName_text.getText());
		dataMap.put("author",author_text.getText());
		dataMap.put("description",description_text.getText());
		dataMap.put("create_time",createTimeStr);
		File pathCheck = new File(projectPath+servicePath);
		if  (!pathCheck .exists()  && !pathCheck .isDirectory()){
			pathCheck.mkdir();
		}
		generateFileByTemplate("mongo/Service.ftl",mapperFile,dataMap);
	}

	private void generateServiceImplFile() throws Exception{
		final String suffix = "ServiceImpl.java";
		final String path = projectPath+serviceImplPath+modelName_text.getText()+suffix;
		File mapperFile = new File(path);
		Map<String,Object> dataMap = new HashMap<>();
		dataMap.put("dao_package_name",daoPackageName);
		dataMap.put("service_package_name",servicePackageName);
		dataMap.put("model_package_name",modelPackageName);
		dataMap.put("model_name",modelName_text.getText());
		dataMap.put("author",author_text.getText());
		dataMap.put("description",description_text.getText());
		dataMap.put("create_time",createTimeStr);
		File pathCheck = new File(projectPath+serviceImplPath);
		if  (!pathCheck .exists()  && !pathCheck .isDirectory()){
			pathCheck.mkdir();
		}
		generateFileByTemplate("mongo/ServiceImpl.ftl",mapperFile,dataMap);
	}

	private void generateModelFile() throws Exception{
		final String suffix = ".java";
		final String path = projectPath+modelPath+modelName_text.getText()+suffix;
		File mapperFile = new File(path);
		Map<String,Object> dataMap = new HashMap<>();
		dataMap.put("model_package_name",modelPackageName);
		dataMap.put("model_name",modelName_text.getText());
		dataMap.put("author",author_text.getText());
		dataMap.put("description",description_text.getText());
		dataMap.put("create_time",createTimeStr);
		File pathCheck = new File(projectPath+modelPath);
		if  (!pathCheck .exists()  && !pathCheck .isDirectory()){
			pathCheck.mkdir();
		}
		generateFileByTemplate("mongo/Model.ftl",mapperFile,dataMap);
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
		abstractButton.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// 获取事件源（即开关按钮本身）
				JToggleButton toggleBtn = (JToggleButton) e.getSource();
				if (toggleBtn.isSelected()){
					abstractButton.setText(trueText);
				}else{
					abstractButton.setText(falseText);
				}
			}
		});
	}


	public static void runIt(){
		new CreateMongoJavaTool();
	}
}
