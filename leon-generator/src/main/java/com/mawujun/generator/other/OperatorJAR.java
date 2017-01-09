package com.mawujun.generator.other;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.mawujun.utils.file.FileUtils;

/**
 * Hello world!
 *
 */
public class OperatorJAR 
{
    private static String tempdir=System.getProperty("java.io.tmpdir");
    
    public static List<File> readJARList(String fileName,String classpathftldir) throws IOException {// 显示JAR文件内容列表  
    	//tempdir="d:\\";
    	
    	//fileName=fileName.replaceAll(OperatorJAR.class.getResource("").getPath().toString(), "/");
    	
    	List<File> list=new ArrayList<File>();
    	//System.out.println(fileName);
        JarFile jarFile = new JarFile(fileName); // 创建JAR文件对象  
        Enumeration en = jarFile.entries(); // 枚举获得JAR文件内的实体,即相对路径  
        
        while (en.hasMoreElements()) { // 遍历显示JAR文件中的内容信息  
        	JarEntry entry = (JarEntry) en.nextElement();
            //process(entry); // 调用方法显示内容  
            //processStream(jarFile.getInputStream(entry));
        	String name=entry.getName();
        	
        	//如果不是模板文件，而且不是以指定的目录开头的话，就不拷贝了
        	if((name.lastIndexOf(".ftl")==-1 && name.lastIndexOf(".include")==-1) || !("/"+name).startsWith(classpathftldir)){
        		continue;
        	}
        	System.out.println(name);
        	String path=tempdir+File.separator+ name;
        	File f= new File(path) ;
        	if(!f.exists()){
        		File parent=f.getParentFile();
        		if(!parent.exists()){
        			parent.mkdirs();
        		}
        	}
        	FileUtils.copyStream(jarFile.getInputStream(entry), new FileOutputStream(f));
        	list.add(f);
        }  
        return list;
    }  
  
    private static void process(JarEntry entry) {// 显示对象信息  
       // 对象转化成Jar对象  
        String name = entry.getName();// 文件名称  
        //entry.get
        long size = entry.getSize();// 文件大小  
        long compressedSize = entry.getCompressedSize();// 压缩后的大小  
        System.out.println(name + "\t" + size + "\t" + compressedSize);  
       
    }  

	private static void processStream(InputStream input) throws IOException {
//		InputStreamReader isr = new InputStreamReader(input);
//		BufferedReader reader = new BufferedReader(isr);
//		String line;
//		while ((line = reader.readLine()) != null) {
//			System.out.println(line);
//		}
//		reader.close();
		
		
		
	}
  
//    public static void readJARFile(String jarFileName, String fileName)  
//            throws IOException {// 读取JAR文件中的单个文件信息  
//        JarFile jarFile = new JarFile(jarFileName);// 根据传入JAR文件创建JAR文件对象  
//        JarEntry entry = jarFile.getJarEntry(fileName);// 获得JAR文件中的单个文件的JAR实体  
//        InputStream input = jarFile.getInputStream(entry);// 根据实体创建输入流  
//        readFile(input);// 调用方法获得文件信息  
//        jarFile.close();// 关闭JAR文件对象流  
//    }  
//  
//    public static void readFile(InputStream input) throws IOException {// 读出JAR文件中单个文件信息  
//        InputStreamReader in = new InputStreamReader(input);// 创建输入读流  
//        BufferedReader reader = new BufferedReader(in);// 创建缓冲读流  
//        String line;  
//        while ((line = reader.readLine()) != null) {// 循环显示文件内容  
//            System.out.println(line);  
//        }  
//        reader.close();// 关闭缓冲读流  
//    }  
  
//    public static void main(String args[]) throws IOException {// java程序主入口处  
//        OperatorJAR j = new OperatorJAR();  
//        System.out.println("1.输入一个JAR文件(包括路径和后缀)");  
//        //Scanner scan = new Scanner(System.in);// 键盘输入值  
//        String jarFileName ="D:\\maven\\repository\\com\\mawujun\\leon-generator\\1.0-SNAPSHOT\\leon-generator-1.0-SNAPSHOT.jar" ;// 获得键盘输入的值  
//        readJARList(jarFileName);// 调用方法显示JAR文件中的文件信息  
//        System.out.println("2.查看该JAR文件中的哪个文件信息?");  
//
//    }  
    
}
