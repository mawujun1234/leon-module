package com.mawujun.generator.MT;

import java.io.IOException;

import com.mawujun.generator.MT.GeneratorMT;
import com.mawujun.utils.file.FileUtils;

/**
 * 生成M类。
 * @author mawujun 16064988@qq.com  
 *
 */
public class GeneratorM {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		GeneratorMT generatorMT=new GeneratorMT();
		//generatorMT.generateM("com.mawujun","E:\\eclipse\\aaa\\knpcrm\\src\\main\\java","com.mawujun.utils");
		//System.out.println(GeneratorM.class.getResource("/").getPath());
		//System.out.println(System.getProperty("user.dir"));
		generatorMT.generateM("com.mawujun",System.getProperty("user.dir")+"/src/main/java/","com.mawujun.utils");
	}

}
