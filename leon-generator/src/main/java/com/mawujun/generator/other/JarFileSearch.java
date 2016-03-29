package com.mawujun.generator.other;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

//http://hanxuedog.iteye.com/blog/1825723
//http://blog.csdn.net/hfhwfw/article/details/7266957
public class JarFileSearch {
	
    protected static void searchFtl(String dir, List<InputStream> list) {  
    	if(list==null){
    		list=new ArrayList<InputStream>();
    	}
    	
        try {  
            File d = new File(dir);  
            File[] files = null;
            if (!d.isDirectory()) {  
                 if(d.getName().endsWith(".jar")||d.getName().endsWith(".zip")){
                	 files=new File[]{d};
                 } 
            }  else {
            	 files =d.listFiles(); 
            }
            
            for (int i = 0; i < files.length; i++) {  
                if (files[i].isDirectory()) {  
                	JarFileSearch.searchFtl(files[i].getAbsolutePath(), list);  
                } else {  
                    String filename = files[i].getAbsolutePath();  
                    if (filename.endsWith(".jar")||filename.endsWith(".zip")) {  
                        ZipFile zip = new ZipFile(filename);  
                        Enumeration entries = zip.entries();  
                        while (entries.hasMoreElements()) {  
                            ZipEntry entry = (ZipEntry) entries.nextElement();  
                            String entry_name = entry.getName();  
                              
                            //不搜索扩展名为.class的文件  
                            if(entry_name.lastIndexOf(".ftl")==-1){  
                            	InputStream inputStream=zip.getInputStream(entry);
                            	list.add(inputStream);
 //                               BufferedReader r = new BufferedReader(new InputStreamReader(zip.getInputStream(entry)));  
//                                while(r.read()!=-1){  
//                                    String tempStr = r.readLine();  
//                                    if(null!=tempStr && tempStr.indexOf(condition)>-1){  
//                                        this.jarFiles.add(filename + "  --->  " + thisClassName);  
//                                        break;  
//                                    }  
//                                }  
                            }  
                              
 
                        }  
                    }  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  

    }  

}
