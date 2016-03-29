package com.mawujun.generator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.mawujun.utils.file.FileUtils;

public class Test {

	public static void main(String[] args) throws IOException {
		String aa=Test.class.getClassLoader().getResource(".").getFile();
		aa=aa.replaceAll("\\\\", "/");
		String bb="/templates/default";
		if(aa.indexOf(bb)!=-1){
			String ftlfilepath=aa.substring(aa.indexOf(bb)+bb.length());
			System.out.println(ftlfilepath);
		}
		
//		// TODO Auto-generated method stub
//		Test test=new Test();
//		List<File> files=FileUtils.findFiles(FileUtils.getCurrentClassPath(test)+"/templates/ftl1", "*.ftl");
//		for(File file:files){
//			System.out.println(file.getAbsolutePath());
//			System.out.println(file.getCanonicalPath());
//			System.out.println(file.getName());
//			System.out.println(file.getPath());
//			System.out.println(file.getParent());
//			System.out.println("-------------------");
//		}
	}

}
