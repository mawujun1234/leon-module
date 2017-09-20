package com.mawujun.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClearJavaComment {
	/** 根目录 */  
    //public static String rootDir = "D:\\workspace\\proj_map\\src\\com";  
//"D:\\testdir  
    // D:\\workspace\\proj_map\\src\\com  
	static Set<String> excludePath=new HashSet<String>();
    public static void main(String args[]) throws FileNotFoundException,  
            UnsupportedEncodingException {  
    	excludePath.add("E:\\eclipse\\aaa\\orderingSystem\\src\\main\\webapp\\ext6");
		excludePath.add("E:\\eclipse\\aaa\\orderingSystem\\src\\main\\webapp\\css");
		excludePath.add("E:\\eclipse\\aaa\\orderingSystem\\src\\main\\webapp\\font");
		excludePath.add("E:\\eclipse\\aaa\\orderingSystem\\src\\main\\webapp\\photoes");
		excludePath.add("E:\\eclipse\\aaa\\orderingSystem\\src\\main\\webapp\\photoes_bak");
		excludePath.add("E:\\eclipse\\aaa\\orderingSystem\\src\\main\\webapp\\qrcode_temp");
		excludePath.add("E:\\eclipse\\aaa\\orderingSystem\\src\\main\\webapp\\war");
		
    	String rootDir = "E:\\eclipse\\aaa\\orderingSystem\\src";
    	//添加过滤文件类型的功能，只删除指定文件类型中的注释
    	//还可以添加排除的目录，或者指定目录
        deepDir(rootDir);  
  
    }  
  
    public static void deepDir(String rootDir) throws FileNotFoundException,  
            UnsupportedEncodingException { 
    	if(excludePath.contains(rootDir)) {
    		return;
    	}
        File folder = new File(rootDir);  
        if (folder.isDirectory()) {  
            String[] files = folder.list();  
            for (int i = 0; i < files.length; i++) {  
                File file = new File(folder, files[i]);  
                if (file.isDirectory() && file.isHidden() == false) {  
                    System.out.println(file.getPath());  
                    deepDir(file.getPath());  
                } else if (file.isFile()) {  
                    clearComment(file.getPath());  
                }  
            }  
        } else if (folder.isFile()) {  
        	
            clearComment(folder.getPath());  
        }  
    }  

  
    /** 
     * @param currentDir 
     *            当前目录 
     * @param currentFileName 
     *            当前文件名 
     * @throws FileNotFoundException 
     * @throws UnsupportedEncodingException 
     */  
    /** 
     * @param filePathAndName 
     * @throws FileNotFoundException 
     * @throws UnsupportedEncodingException 
     */  
    public static void clearComment(String filePathAndName )  
            throws FileNotFoundException, UnsupportedEncodingException {  
    	 // 1、清除单行的注释，如： //某某，正则为 ：\/\/.*  
        // 2、清除单行的注释，如：/** 某某 */，正则为：\/\*\*.*\*\/  
        // 3、清除单行的注释，如：/* 某某 */，正则为：\/\*.*\*\/  
        // 4、清除多行的注释，如:  
        // /* 某某1  
        // 某某2  
        // */  
        // 正则为：.*/\*(.*)\*/.*  
        // 5、清除多行的注释，如：  
        // /** 某某1  
        // 某某2  
        // */  
        // 正则为：/\*\*(\s*\*\s*.*\s*?)*  
    	 Map<String, String> patterns = new HashMap<String, String>(); 
         if(filePathAndName.lastIndexOf(".java")!=-1 
     			|| filePathAndName.lastIndexOf(".js")!=-1
     			||filePathAndName.lastIndexOf(".css")!=-1) {
         	 patterns.put("([^:])\\/\\/.*", "$1");// 匹配在非冒号后面的注释，此时就不到再遇到http://  
              patterns.put("\\s+\\/\\/.*", "");// 匹配“//”前是空白符的注释  
              patterns.put("^\\/\\/.*", "");  
              patterns.put("^\\/\\*\\*.*\\*\\/$", "");  
              patterns.put("\\/\\*.*\\*\\/", "");  
              patterns.put("/\\*(\\s*\\*\\s*.*\\s*?)*\\*\\/", "");  
              //patterns.put("/\\*(\\s*\\*?\\s*.*\\s*?)*", "");  
     	} else if( filePathAndName.lastIndexOf(".xml")!=-1) {
     		patterns.put("(?s)<!--.*?-->", ""); //xml中的<!-- -->注释
     	} else if(filePathAndName.lastIndexOf(".properties")!=-1) {
     		patterns.put("#.*", "");
     	} else {
     		return;
     	}
         
        StringBuffer buffer = new StringBuffer();  
        String line = null; // 用来保存每行读取的内容  
        InputStream is = new FileInputStream(filePathAndName);  
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,  
                "UTF-8"));  
        try {  
            line = reader.readLine();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } // 读取第一行  
        while (line != null) { // 如果 line 为空说明读完了  
            buffer.append(line); // 将读到的内容添加到 buffer 中  
            buffer.append("\r\n"); // 添加换行符  
            try {  
                line = reader.readLine();  
            } catch (IOException e) {  
                e.printStackTrace();  
            } // 读取下一行  
        }  
       
        String filecontent = buffer.toString();  
  
       
        
       
        
        Iterator<String> keys = patterns.keySet().iterator();  
        String key = null, value = "";  
        while (keys.hasNext()) {  
            // 经过多次替换  
            key = keys.next();  
            value = patterns.get(key);  
            filecontent = replaceAll(filecontent, key, value);  
        }  
        System.out.println(filecontent);  
        // 再输出到原文件  
        try {  
            File f = new File(filePathAndName);  
            if (!f.getParentFile().exists()) {  
                f.getParentFile().mkdirs();  
            }  
            FileOutputStream out = new FileOutputStream(filePathAndName);  
            byte[] bytes = filecontent.getBytes("UTF-8");  
            out.write(bytes);  
            out.flush();  
            out.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * @param fileContent 
     *            内容 
     * @param patternString 
     *            匹配的正则表达式 
     * @param replace 
     *            替换的内容 
     * @return 
     */  
    public static String replaceAll(String fileContent, String patternString,  
            String replace) {  
        String str = "";  
        Matcher m = null;  
        Pattern p = null;  
        try {  
            p = Pattern.compile(patternString);  
            m = p.matcher(fileContent);  
            str = m.replaceAll(replace);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            m = null;  
            p = null;  
        }  
        // 获得匹配器对象  
        return str;  
  
    }  
}
