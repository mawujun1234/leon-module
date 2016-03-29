package com.mawujun.repository.hibernate.schemaExport;


public class SchemaExportApp {
	 public static void main(String[] args) {   
//		 //Configuration configuration=new AnnotationConfiguration();   
//		 //设置成test，就是返回测试库的sql，设置成其他developement，就返回其他sql
//		 String profile="test";//还可以设置成developement等，这个要看applicationContext.xml中配置的
//		 
//		 System.setProperty("spring.profiles.active", profile);//spring.profiles.default这个也可以设置
//		 ClassPathXmlApplicationContext appContext=new ClassPathXmlApplicationContext("/applicationContext.xml");//
//
//
//		 LocalSessionFactoryBean localSessionFactoryBean=(LocalSessionFactoryBean)appContext.getBean("&sessionFactory");
//		 System.out.println(localSessionFactoryBean);
//
//		 System.out.println("==============================================="+System.getProperty("user.dir"));	
//		 String path=System.getProperty("user.dir");//SchemaExportApp.class.getResource("/").getPath();
//		 //path=path+"/src/sql/"+profile+".sql";
//		 path=path+FileUtils.FILE_SEPARATOR+"src"+FileUtils.FILE_SEPARATOR+"sql"+FileUtils.FILE_SEPARATOR+profile+".sql";
//		 System.out.println(path);
//		 
//		 Configuration configuration =localSessionFactoryBean.getConfiguration();// new Configuration();  
//		 //configuration.
//	     SchemaExport schemaExport=new SchemaExport(configuration);   
//	     schemaExport.setDelimiter(";");
//	     schemaExport.setFormat(true);
//	     schemaExport.setOutputFile(path);
//	     schemaExport.create(false, true);   
	 }   
}
