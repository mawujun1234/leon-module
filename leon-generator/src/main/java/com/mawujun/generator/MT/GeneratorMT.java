package com.mawujun.generator.MT;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mawujun.utils.Assert;
import com.mawujun.utils.file.FileUtils;

/**
 * 生成的类用来快速的引用某个领域类的字段，这样既可以保证字段的准确度，又可以快速引用
 * @author mawujun email:16064988@qq.com qq:16064988
 *
 */
public class GeneratorMT {
	 Logger logger = LogManager.getLogger(FileUtils.class);
	
	private Class annotationClass=javax.persistence.Entity.class;
	private Class annotationTable=javax.persistence.Table.class;
	
	private String targetPackage;
	
	/**
	 * 搜索某个路径下面，标注了@Entity的类，并生成和android中的R类似的类，M
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @param packageName 从哪些包中进行搜索
	 * @param targetMDir 生成的目标地址 存放在那个路径下:E:\\eclipse\\workspace\\hujibang\\src\\main\\java，最终的目录是targetMDir+targetPackage
	 * @param targetPackage com.mawujun.utils 生成的类的包名
	 * @throws IOException
	 */
	public void generateM(String packageName,String targetMDir,String targetPackage) throws IOException{
		Assert.notNull(packageName);
		Assert.notNull(targetMDir);
		Assert.notNull(targetPackage);
		this.targetPackage=targetPackage;
		generateM(getClasssFromPackage(packageName),targetMDir);
	}
	
    /** 
     * 获得包下面的所有的class 
     *  
     * @param pack 
     *            package完整名称 
     * @return List包含所有class的实例 
     */  
    private List<Class> getClasssFromPackage(String pack) {  
        List<Class> clazzs = new ArrayList<Class>();  
      
        // 是否循环搜索子包  
        boolean recursive = true;  
      
        // 包名字  
        String packageName = pack;  
        // 包名对应的路径名称  
        String packageDirName = packageName.replace('.','/');  
      
        Enumeration<URL> dirs;  
      
        try {  
            dirs = GeneratorMT.class.getClassLoader().getResources(packageDirName);  
            while (dirs.hasMoreElements()) {  
                URL url = dirs.nextElement();  
      
                String protocol = url.getProtocol();  
      
                if ("file".equals(protocol)) {  
                	logger.info("file类型的扫描");  
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");  
                    findClassInPackageByFile(packageName, filePath, recursive, clazzs);  
                } else if ("jar".equals(protocol)) {  
                	logger.info("jar类型的扫描");  
                }  
            }  
      
        } catch (Exception e) {  
            e.printStackTrace();  
            logger.info(e.getMessage()); 
        }  
      
        return clazzs;  
    } 
    
    
    /** 
     * 在package对应的路径下找到所有的class 
     *  
     * @param packageName 
     *            package名称 
     * @param filePath 
     *            package对应的路径 
     * @param recursive 
     *            是否查找子package 
     * @param clazzs 
     *            找到class以后存放的集合 
     */  
    private void findClassInPackageByFile( String packageName, String filePath, final boolean recursive, List<Class> clazzs) {  

        File dir = new File(filePath);  
        if (!dir.exists() || !dir.isDirectory()) {  
            return;  
        }  
        // 在给定的目录下找到所有的文件，并且进行条件过滤  
        File[] dirFiles = dir.listFiles(new FileFilter() {  
            public boolean accept(File file) {  
                boolean acceptDir = recursive && file.isDirectory();// 接受dir目录  
                boolean acceptClass = file.getName().endsWith("class");// 接受class文件  
                return acceptDir || acceptClass;  
            }  
        });  
      
        for (File file : dirFiles) {  
            if (file.isDirectory()) {  
                findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzs);  
            } else {  
                String className = file.getName().substring(0, file.getName().length() - 6);  
                try {  
                	Class clazz=GeneratorMT.class.getClassLoader().loadClass(packageName + "." + className);
                	Annotation annoation=clazz.getAnnotation(annotationClass);
    				if(annoation!=null){
    					logger.info("============================找到实体类:"+clazz.getName());
    					clazzs.add(clazz); 
    				}		
                     
                   
                } catch (Exception e) {  
                    //e.printStackTrace();  
                    logger.error(e);  
                    
                }  catch (NoClassDefFoundError e) {  
                    //e.printStackTrace();  
                    logger.error(e);  
                    return;
                }  
                logger.info(packageName + "." + className);  
            }  
        }  
    }  
    /**
     * 产生领域模型的类
     * @author mawujun email:160649888@163.com qq:16064988
     * @param entities
     * @throws IOException
     */
    private void generateM(List<Class> entities,String targetMDir) throws IOException{
    	//生成M
    	File file=new File(targetMDir+File.separatorChar+this.targetPackage.replace('.', File.separatorChar)+File.separatorChar+"M.java");
    	//file.delete();
    	if(!file.exists()){
    		file.createNewFile();
    	}
    	FileWriter fileWrite=new FileWriter(file);
    	
    	    	
    	//StringBuilder builder=new StringBuilder();
    	fileWrite.append("package "+this.targetPackage+";\n");
    	fileWrite.append("public final class M {\n");
    	
    	
    	for(Class clazz:entities){
    		logger.info("============================================="+clazz.getName());

    		fileWrite.append("public static final class "+clazz.getSimpleName()+" {\n");
    		 //Field[]fields = clazz.getDeclaredFields();
    		 List<Field> fields= getClassField(clazz);
    		 
    		 Set<String> existField=new HashSet<String>();
             for (Field field : fields) { //完全等同于上面的for循环
            	 if(!existField.contains(field.getName())){
            		 existField.add(field.getName());
            	 } else {
            		 continue;
            	 }
            	 logger.info(field.getName());
                 //System.out.println(field.getName()+" "+field.getType());
                 //fileWrite.append("public static final "+field.getType().getName()+" "+field.getName()+"=\""+field.getName()+"\";\n");
                 if(isBaseType(field.getType()) || field.getType().isEnum()){
                	 fileWrite.append("	public static final String "+field.getName()+"=\""+field.getName()+"\";\n");
                 } else if(!isOf(field.getType(),Map.class) && !isOf(field.getType(),Collection.class)){
                	 Class<?> fieldClass=field.getType();
                	 Annotation embeddedIdAnnotataion=field.getAnnotation(EmbeddedId.class);
                	 //是复合主键的情况下
                	 if(embeddedIdAnnotataion!=null){
                		 fileWrite.append("	 /**\n");
                    	 fileWrite.append("	 * 返回复合主键的组成，，以对象关联的方式:"+field.getName()+"\n");
                    	 fileWrite.append("	 */\n");
                    	 fileWrite.append("	public static final class "+field.getName()+" {\n");
                    	 //Field[] embeddedIdFields = fieldClass.getDeclaredFields();
                    	 List<Field> embeddedIdFields= getClassField(fieldClass);
                    	 for (Field embeddedIdfield : embeddedIdFields) { 
                    		 fileWrite.append("		public static final String "+embeddedIdfield.getName()+"=\""+field.getName()+"."+embeddedIdfield.getName()+"\";\n");
                    	 }
                    	 fileWrite.append("			\n");
                    	 
                     	 fileWrite.append("	    /**\n");
	                	 fileWrite.append("	    * 返回的是复合主键的属性名称，主要用于属性过滤或以id来查询的时候\n");
	                	 fileWrite.append("	    */\n");
	                	 fileWrite.append("	    public static String name(){ \n");
	                	 fileWrite.append("		    return \""+field.getName()+"\";\n");
	                	 fileWrite.append("	    }\n");
	                	 
                    	 fileWrite.append("	}\n");
                    	 
                    	 
//                    	 fileWrite.append("	/**\n");
//                    	 fileWrite.append("	* 这是一个复合主键，返回的是该复合主键的属性名称，在hql中使用:"+field.getName()+"\n");
//                    	 fileWrite.append("	*/\n");
//                    	 fileWrite.append("	public static final String "+field.getName()+"=\""+field.getName()+"\";\n");
                	 } else {

	                	 //返回关联类的属性，以对象关联的方式
	                	 fileWrite.append("	 /**\n");
                    	 fileWrite.append("	 * 返回关联对象的属性，，以对象关联的方式(a.b这种形式)，只有一些基本属性，层级不再往下了\n");
                    	 fileWrite.append("	 */\n");
                    	 fileWrite.append("	public static final class "+field.getName()+" {\n");
                    	 //Field[] embeddedIdFields = fieldClass.getDeclaredFields();
                    	 List<Field> embeddedIdFields= getClassField(fieldClass);
                    	 for (Field embeddedIdfield : embeddedIdFields) { 
                    		 if(isBaseType(embeddedIdfield.getType()) || embeddedIdfield.getType().isEnum()) {
                    			 fileWrite.append("		public static final String "+embeddedIdfield.getName()+"=\""+field.getName()+"."+embeddedIdfield.getName()+"\";\n");
                    		 }
                    	 }
                    	 //返回该属性的名称
                    	 fileWrite.append("			\n");
                     	 fileWrite.append("	    /**\n");
	                	 fileWrite.append("	    * 返回的是关联类的属性名称，主要用于属性过滤的时候\n");
	                	 fileWrite.append("	    */\n");
	                	 fileWrite.append("	    public static String name(){ \n");
	                	 fileWrite.append("		    return \""+field.getName()+"\";\n");
	                	 fileWrite.append("	    }\n");
	                	 
	                	 
                    	 fileWrite.append("	}\n");
                    	 
	   
                    	        	 
//	                	 fileWrite.append("	/**\n");
//	                	 fileWrite.append("	* 访问关联类的id，用于hql的时候，返回的是"+field.getName()+".id\n");
//	                	 fileWrite.append("	*/\n");
//	                	 fileWrite.append("	public static final String "+field.getName()+"_id=\""+field.getName()+".id\";\n");
//	                	 fileWrite.append("	/**\n");
//	                	 fileWrite.append("	* 返回的是关联类的属性名称，主要用于属性过滤的时候\n");
//	                	 fileWrite.append("	*/\n");
//	                	 fileWrite.append("	public static final String "+field.getName()+"=\""+field.getName()+"\";\n");
                	 }
                 } else {
                	 //其他关联类，例如集合等
                	 fileWrite.append("	/**\n");
                	 fileWrite.append("	* 这里一般是集合属性，返回的是"+field.getName()+"\n");
                	 fileWrite.append("	*/\n");
                	 fileWrite.append("	public static final String "+field.getName()+"=\""+field.getName()+"\";\n");
                 }
                
             }
             fileWrite.append("}\n");
    	}
    	fileWrite.append("}\n");
    	fileWrite.close();
    }
    
    
    /**
     * 产生表的字段名
     * @author mawujun email:160649888@163.com qq:16064988
     * @param entities
     * @throws IOException
     */
    public void generateT(List<Class> entities,String targetMDir) throws IOException{
    	//生成T
    	File file=new File(targetMDir+File.separatorChar+this.targetPackage.replace('.', File.separatorChar)+File.separatorChar+"M.java");
    	//file.delete();
    	if(!file.exists()){
    		file.createNewFile();
    	}
    	FileWriter fileWrite=new FileWriter(file);
    	
    	    	
    	//StringBuilder builder=new StringBuilder();
    	fileWrite.append("package "+this.targetPackage+";\n");
    	fileWrite.append("public final class T {\n");
    	
    	
    	for(Class clazz:entities){
    		

    		Table annoation=(Table)clazz.getAnnotation(annotationTable);
    		if(annoation==null){
    			throw new NullPointerException(clazz.getClass()+"的Table注解没有设置");
    		}
    		logger.info("============================================="+annoation.name());
    		
    		//fileWrite.append("public static final class "+clazz.getSimpleName()+" {\n");
    		fileWrite.append("public static final class "+annoation.name()+" {\n");
    		 //Field[]fields = clazz.getDeclaredFields();
    		 Set<String> existField=new HashSet<String>();
    		 
    		List<Field> fields= getClassField(clazz);
             for (Field field : fields) { //完全等同于上面的for循环
                 //System.out.println(field.getName()+" "+field.getType());
            	 if(!existField.contains(field.getName())){
            		 existField.add(field.getName());
            	 } else {
            		 continue;
            	 }
            	 logger.info(field.getName());
            	
            	 Annotation embeddedIdAnnotataion=field.getAnnotation(EmbeddedId.class);
            	 //是复合主键的情况下
            	 if(embeddedIdAnnotataion!=null){
            		 Class<?> fieldClass=field.getType();
            		 fileWrite.append("	 /**\n");
                	 fileWrite.append("	 * 这个是复合主键。里面的是复合组件的组成列的列名\n");
                	 fileWrite.append("	 */\n");
                	 fileWrite.append("	public static final class "+fieldClass.getSimpleName()+" {\n");
                	 //Field[] embeddedIdFields = fieldClass.getDeclaredFields();
                	 List<Field> embeddedIdFields= getClassField(fieldClass);
                	 for (Field embeddedIdfield : embeddedIdFields) { 
                		 Column columnAnnotation=(Column)embeddedIdfield.getAnnotation(Column.class);
                		 if(columnAnnotation==null || (columnAnnotation!=null && columnAnnotation.name().equals(""))){
            				 fileWrite.append("		public static final String "+embeddedIdfield.getName()+"=\""+embeddedIdfield.getName()+"\";\n");
            			 } else {
            				 fileWrite.append("		public static final String "+columnAnnotation.name()+"=\""+columnAnnotation.name()+"\";\n");
            			 }
                	 }
                	 fileWrite.append("			\n");
                	 fileWrite.append("	}\n");
            	 } else if(isBaseType(field.getType()) || field.getType().isEnum()){
            			 
            			 Column columnAnnotation=(Column)field.getAnnotation(Column.class);
            			 if(columnAnnotation==null || (columnAnnotation!=null && columnAnnotation.name().equals(""))){
            				 fileWrite.append("	public static final String "+field.getName()+"=\""+field.getName()+"\";\n");
            			 } else {
            				 fileWrite.append("	public static final String "+columnAnnotation.name()+"=\""+columnAnnotation.name()+"\";\n");
            			 }
                    	
                 } else if(!isOf(field.getType(),Map.class) && !isOf(field.getType(),Collection.class)){ 
                    	 JoinColumn columnAnnotation=(JoinColumn)field.getAnnotation(Column.class);
                    	 if(columnAnnotation==null || (columnAnnotation!=null && columnAnnotation.name().equals(""))){
                    		 fileWrite.append("	/**\n");
                        	 fileWrite.append("	* 访问外键的列名，用于sql的时候，返回的是"+field.getName()+"_id\n");
                        	 fileWrite.append("	*/\n");
                        	 fileWrite.append("	public static final String "+field.getName()+"_id=\""+field.getName()+"_id\";\n");
                    	 } else {
                    		 fileWrite.append("	/**\n");
                        	 fileWrite.append("	* 访问外键的列名，用于sql的时候，返回的是"+columnAnnotation.name()+"_id\n");
                        	 fileWrite.append("	*/\n");
                        	 fileWrite.append("	public static final String "+columnAnnotation.name()+"=\""+columnAnnotation.name()+"\";\n");
                    	 }
                    	 
                 }     
                
             }
             fileWrite.append("}\n");
    	}
    	fileWrite.append("}\n");
    	fileWrite.close();
    }
    
    /** 
     * 这个方法，是最重要的，关键的实现在这里面 
     *  
     * @param aClazz 
     * @param aFieldName 
     * @return 
     */  
    private List<Field> getClassField(Class aClazz) {  
	    Field[] declaredFields = aClazz.getDeclaredFields();  
	    List<Field> fields=new ArrayList<Field>();
	    
	    for (Field field : declaredFields) {  
	    	if("serialVersionUID".equals(field.getName())){
	    		continue;
	    	}
	    	fields.add(field);
	    }  
	  
	    Class superclass = aClazz.getSuperclass();  
	    
	    if (superclass != null) {// 简单的递归一下  
	       
	        fields.addAll( getClassField(superclass));
	    }  
	    return fields;  
	} 
    public boolean isBaseType(Class clz){
		//如果是基本类型就返回true
		if(clz == UUID.class || clz == URL.class || clz == String.class || clz==Date.class || clz==java.sql.Date.class || clz==java.sql.Timestamp.class || clz.isPrimitive() || isWrapClass(clz)){
			return true;
		}
		return false;
	}
    public boolean isOf(Class<?> orginType,Class<?> type){
    	return type.isAssignableFrom(orginType);
    }
    
    public boolean isWrapClass(Class clz) {
		try {
			return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return false;
		}
	}
}