package com.mawujun.generator;

import java.awt.geom.IllegalPathStateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.SystemUtils;

import com.mawujun.generator.model.FtlFileInfo;
import com.mawujun.generator.model.SubjectRoot;
import com.mawujun.generator.other.NameStrategy;
import com.mawujun.generator.other.OperatorJAR;
import com.mawujun.utils.file.FileUtils;
import com.mawujun.utils.string.StringUtils;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.utility.NullArgumentException;

/**
 * 
 * @author mawujun
 *
 */
public class GeneratorService {
	
	private JavaEntityMetaDataService javaEntityMetaDataService=new JavaEntityMetaDataService();
	private Configuration cfg=null;
	List<FtlFileInfo> ftl_file_manes=new ArrayList<FtlFileInfo>();//ftl文件的名称
	Properties properties=new Properties();
	{
		properties.put("classpathftldir", "/templates/default");//一定要使用相对目录
		
		if(SystemUtils.IS_OS_MAC){
			properties.put("outputDir", "/opt/generate");
		} else if(SystemUtils.IS_OS_WINDOWS){
			properties.put("outputDir", "d:/webapp-generator-output");
		}
		properties.put("nameStrategy", "com.mawujun.generator.other.DefaultNameStrategy");
		
	}

	private boolean ftl_is_default=true;//标识ftl文件是不是使用默认的
	//额外的配置选项
	private ExtenConfig extenConfig;
	
	/**
	 * 设置输出路径，使用绝对路径
	 * @param output
	 */
	public void setOutputDir(String output){
		properties.put("outputDir", output);
	}
	/**
	 * 设置模板文件所在的目录，使用相对目录，相对于项目的根目录
	 * 首先都会在本地目录中查找，如果本地目录没有的话，就会去jar目录中查找对应的
	 * 一定要使用相对目录,例如/templates/default
	 * @author mawujun qq:16064988 mawujun1234@163.com
	 * @param classpathftldir
	 */
	public void setFtlDir(String classpathftldir){
		ftl_is_default=false;
		properties.put("classpathftldir", classpathftldir);
	}
	/**
	 * 领域模型的字段名字变成列名时的映射规则，还有表名
	 * @author mawujun qq:16064988 mawujun1234@163.com
	 * @param nameStrategy
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public void setNameStrategy(Class<? extends NameStrategy> nameStrategy) throws InstantiationException, IllegalAccessException{
		properties.put("nameStrategy", nameStrategy);
		javaEntityMetaDataService.setNameStrategy((NameStrategy)nameStrategy.newInstance());
	}
	
	/**
	 * 从指定目录中读取ftl文件的时候
	 * @author mawujun qq:16064988 mawujun1234@163.com
	 * @throws IOException
	 */
	public void initConfiguration() throws IOException{
		// TODO Auto-generated method stub
		if(cfg!=null){
			return;
		}
		
////		//加載多個文件
////		FileTemplateLoader ftl1 = new FileTemplateLoader(new File(basePath+"\\extjs4"));
////		FileTemplateLoader ftl2 = new FileTemplateLoader(new File(basePath+"\\java\\controller"));
////		FileTemplateLoader ftl3 = new FileTemplateLoader(new File(basePath+"\\java\\service"));
////		FileTemplateLoader ftl4 = new FileTemplateLoader(new File(basePath+"\\java\\mybatis"));
////		TemplateLoader[] loaders = new TemplateLoader[] { ftl1, ftl2,ftl3,ftl4 };
////		MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
////		cfg.setTemplateLoader(mtl);
//		
//		PathMatchingResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
//		Resource[] reses= resolver.getResources("classpath:templates/ftl1/**/*.ftl");
////		String basePath=this.getClass().getResource("/").toString();
////		Resource[] reses= resolver.getResources("file:"+1+"/templates/**/*.ftl");
//		if(reses==null || reses.length==0){
//			return ;
//		}
		String classpathftldir=properties.getProperty("classpathftldir");
		List<File> files=FileUtils.findFiles(FileUtils.getCurrentClassPath(this)+classpathftldir.substring(1), "*.ftl");
		//这表明，没有编写自己的模板，这样的话就去找默认的模板，就是在jar中的模板
		if(files==null || files.size()==0){
			
			//String basePath=this.getClass().getResource("").getPath().toString();
			//GeneratorService.class.getProtectionDomain().getCodeSource().getLocation().getFile()获取jar文件
			//因为是开发环境，leon-generator是直接存在的，所以直接到leon-generator中获取默认的模板文件了
			//http://blog.csdn.net/zyj8170/article/details/5599988
			String path = GeneratorService.class.getProtectionDomain().getCodeSource().getLocation().getFile()+classpathftldir;
			files=FileUtils.findFiles(path, "*.ftl");
			
			//如果是直接依赖于leon-generator.jar,而没有leon-generator这个项目存在的时候，要先获取到ftl文件所在的jar，然后通过
			//如果不是开发在开发环境中的话，就从jar文件中获取ftl内容
			if(files==null || files.size()==0){
				//返回的路劲格式如下：C:/ResourceJar.jar!/resource/res.txt,所以需要使用!进行分割，然后取前面一部分
				String jarpath=this.getClass().getResource("").getPath().toString().split("!")[0];
				//去掉file:\ 这个开头
				jarpath=jarpath.substring(5);
				files=OperatorJAR.readJARList(jarpath,classpathftldir);
			}
		}
		//ftl_file_manes=files;
		cfg = new Configuration();
		cfg.setEncoding(Locale.CHINA, "UTF-8");
		//cfg.setEncoding(Locale.CHINA, "UTF-8");
		
		//循环出 所有包含ftl的文件夹
		Set<String> list=new HashSet<String>();
		List<TemplateLoader> templateLoaders=new ArrayList<TemplateLoader>();
		for(File file:files){
			//System.out.println(res.getURI().getPath());
			//System.out.println(res.getURL().getPath());
			//String path=file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf('/'));//SystemUtils.FILE_SEPARATOR
			String path=file.getParent();
			if(!list.contains(path)){
				list.add(path);
				FileTemplateLoader ftl1 = new FileTemplateLoader(new File(path));
				templateLoaders.add(ftl1);
			}
			//ftl_file_manes.add(file.getName());
			
			FtlFileInfo ftlFileInfo=new FtlFileInfo();
			ftlFileInfo.setName(file.getName());
			ftlFileInfo.setParentpath(file.getParent());
			ftl_file_manes.add(ftlFileInfo);

		}
//		String path=reses[0].getURI().getPath().substring(0,reses[0].getURI().getPath().indexOf("templates")+9);
//		FileTemplateLoader ftl1 = new FileTemplateLoader(new File(path));
//		templateLoaders.add(ftl1);
		
		MultiTemplateLoader mtl = new MultiTemplateLoader(templateLoaders.toArray(new TemplateLoader[templateLoaders.size()]));
		cfg.setTemplateLoader(mtl);
		
		
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		
		
	}

//	/**
//	 * 从jar的文件夹中读取模板文件
//	 * 
//	 * @author mawujun qq:16064988 mawujun1234@163.com
//	 * @throws IOException
//	 */
//	public void initConfiguration_default() throws IOException {
//		// TODO Auto-generated method stub
//		if (cfg != null) {
//			return;
//		}
//
//		String classpathftldir = properties.getProperty("classpathftldir");
//		//获取目录下面的
//		List<File> files = FileUtils.findFiles(FileUtils.getCurrentClassPath(this) + classpathftldir.substring(1),"*.ftl");
//		// 这表明，没有编写自己的模板，这样的话就去找默认的模板，就是在jar中的模板
//		if (files == null || files.size() == 0) {
//			// 因为是开发环境，leon-generator是直接存在的，所以直接到leon-generator中获取默认的模板文件了
//			// http://blog.csdn.net/zyj8170/article/details/5599988
//			String path = GeneratorService.class.getProtectionDomain().getCodeSource().getLocation().getFile()+ classpathftldir;
//			files = FileUtils.findFiles(path, "*.ftl");
//
//			// 如果是直接依赖于leon-generator.jar,而没有leon-generator这个项目存在的时候，要先获取到ftl文件所在的jar，然后通过
//			// JarFileSearch专门搜索这个jar中的ftl文件，然后读取出来，然后再写到当前项目的classpath中，加上classpathftldir的前缀
//			// 然后再把这些模板文件读取出来，用来生成代码。
//
//			//
//			// String dir=FileUtils.getCurrentClassPath(this)+"../lib";
//			// ArrayList<InputStream> list=new ArrayList<InputStream>();
//			// JarFileSearch.searchFtl(dir, list);
//
//		}
//		// ftl_file_manes=files;
//		cfg = new Configuration();
//		cfg.setEncoding(Locale.CHINA, "UTF-8");
//		// cfg.setEncoding(Locale.CHINA, "UTF-8");
//
//		// 循环出 所有包含ftl的文件夹
//		Set<String> list = new HashSet<String>();
//		List<TemplateLoader> templateLoaders = new ArrayList<TemplateLoader>();
//		for (File file : files) {
//			// System.out.println(res.getURI().getPath());
//			// System.out.println(res.getURL().getPath());
//			// String
//			// path=file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf('/'));//SystemUtils.FILE_SEPARATOR
//			String path = file.getParent();
//			if (!list.contains(path)) {
//				list.add(path);
//				FileTemplateLoader ftl1 = new FileTemplateLoader(new File(path));
//				templateLoaders.add(ftl1);
//			}
//			// ftl_file_manes.add(file.getName());
//
//			FtlFileInfo ftlFileInfo = new FtlFileInfo();
//			ftlFileInfo.setName(file.getName());
//			ftlFileInfo.setParentpath(file.getParent());
//			ftl_file_manes.add(ftlFileInfo);
//
//		}
//
//
//		MultiTemplateLoader mtl = new MultiTemplateLoader(templateLoaders.toArray(new TemplateLoader[templateLoaders.size()]));
//		cfg.setTemplateLoader(mtl);
//		cfg.setObjectWrapper(new DefaultObjectWrapper());
//	}
	

	/**
	 * 根据字符串产生名称
	 * @param clazz
	 * @param ftl
	 * @return
	 * @throws ClassNotFoundException
	 * @throws TemplateException
	 * @throws IOException
	 */
	private  String generatorFileName(Class clazz,String ftl) throws ClassNotFoundException, TemplateException, IOException  {
		
		SubjectRoot root =javaEntityMetaDataService.getClassProperty(clazz);
		if(this.getExtenConfig()!=null){
			root.setExtenConfig(this.getExtenConfig());
		}
		if(root==null){
			throw new NullPointerException("SubjectRoot为null");
		}
		
		String fileName=FreemarkerHelper.processTemplateString(ftl,root, cfg);
		fileName=fileName.substring(0,fileName.lastIndexOf('.'));
		return fileName;
	}
//	public  String generatorFileName(String className,String ftl) throws ClassNotFoundException, TemplateException, IOException  {
//		Class clazz=Class.forName(className);
//		return generatorFileName(clazz,ftl);
//	}

	/**
	 * 
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @param clazz
	 * @param idClass id的class类型
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws TemplateException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public  void generatorAllFile(Class clazz) throws IOException, ClassNotFoundException, TemplateException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		initConfiguration();
		/* 创建数据模型 */
		SubjectRoot root =javaEntityMetaDataService.initClassProperty(clazz);
		if(this.getExtenConfig()!=null){
			root.setExtenConfig(this.getExtenConfig());
		}
		
		
		String output=properties.getProperty("outputDir");//PropertiesUtils.load("generator.properties").getProperty("output");
		FileUtils.createDir(output);

		for (FtlFileInfo ftlFile : ftl_file_manes) {	
			this.generatorFile(clazz,ftlFile,output);	
		}
		//打开文件夹
		System.out.println("输出目录为："+output);
		if(SystemUtils.IS_OS_MAC){
			Runtime.getRuntime().exec("/usr/bin/open "+output);
		} else if(SystemUtils.IS_OS_WINDOWS){
			Runtime.getRuntime().exec("cmd.exe /c start "+output);
		}
		
	}
	/**
	 * 
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @param clazz 领域类
	 * @param ftl 模板文件的名称
	 * @param dirPath 生成后文件存放的地址
	 * @param extenConfig 额外的属性，用来控制生成的代码的
	 * @throws TemplateException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public  void generatorFile(Class clazz,FtlFileInfo ftlfile,String dirPath) throws TemplateException, IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		initConfiguration();
		/* 创建数据模型 */
		SubjectRoot root =javaEntityMetaDataService.initClassProperty(clazz);
		if(this.getExtenConfig()!=null){
			root.setExtenConfig(this.getExtenConfig());
		}
		
		String fileName=this.generatorFileName(clazz, ftlfile.getName());
		//按照模板的目录结构生成
		String classpathftldir=properties.getProperty("classpathftldir");////PropertiesUtils.load("generator.properties").getProperty("classpathftldir");
		String parentpath=ftlfile.getParentpath();
		
		parentpath=parentpath.replaceAll("\\\\", "/");
		if(parentpath.indexOf(classpathftldir)==-1){
			throw new IllegalPathStateException("路径错误，请注意是window还是linux平台");
		}
		String ftlfilepath=parentpath.substring(parentpath.indexOf(classpathftldir)+classpathftldir.length());//   .replaceAll(classpathftldir, "");
		
		
		String fileDir=dirPath+ftlfilepath;
		String filePath=dirPath+ftlfilepath+File.separatorChar+fileName;
		
		File dir=new File(fileDir);
		if(!dir.exists()){
			dir.mkdirs();
		}
		File file=new File(filePath);
		if(file.exists()){
//			int i=filePath.lastIndexOf("/");
//			if(i==-1){
//				i=filePath.lastIndexOf("\\");
//			}
//			Runtime.getRuntime().exec("cmd.exe /c start "+filePath.substring(0, i));
//			throw new FileExistsException(file);
			
		} else {
			//filePath.
			//filePath.substring(beginIndex, endIndex)filePath.lastIndexOf(File.separatorChar);
			file.createNewFile();
		}
		FileWriter fileWriter=new FileWriter(filePath);
		generator(clazz, ftlfile.getName(), fileWriter);
	}
	/**
	 * 
	 * @param clazz 要
	 * @param ftl 模板文件在的地方
	 * @throws TemplateException
	 * @throws IOException
	 */
	private  void generator(Class clazz,String ftl,Writer writer) throws TemplateException, IOException {
		if(!StringUtils.hasLength(ftl)) {
			throw new NullArgumentException("模板文件名称不能为null");
		}
		
		//String basePath=System.getProperty("user.dir");
		

		/* 在整个应用的生命周期中，这个工作你可以执行多次 */
		/* 获取或创建模板 */
		Template templete = cfg.getTemplate(ftl,"UTF-8");
		
		//templete.setEncoding("UTF-8");
		//templete.setOutputEncoding("UTF-8");
		/* 创建数据模型 */
		SubjectRoot root =javaEntityMetaDataService.getClassProperty(clazz);
		if(this.getExtenConfig()!=null){
			root.setExtenConfig(this.getExtenConfig());
		}

		templete.process(root, writer);
		//out.flush();
		//return out;
	}

	public JavaEntityMetaDataService getJavaEntityMetaDataService() {
		return javaEntityMetaDataService;
	}

	public void setJavaEntityMetaDataService(
			JavaEntityMetaDataService javaEntityMetaDataService) {
		this.javaEntityMetaDataService = javaEntityMetaDataService;
	}

	public ExtenConfig getExtenConfig() {
		return extenConfig;
	}

	public void setExtenConfig(ExtenConfig extenConfig) {
		this.extenConfig = extenConfig;
	}
	

}
