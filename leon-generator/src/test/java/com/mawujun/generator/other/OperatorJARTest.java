package com.mawujun.generator.other;

import java.io.IOException;

public class OperatorJARTest {

	public static void main(String args[]) throws IOException {// java程序主入口处
		OperatorJAR j = new OperatorJAR();
		//System.out.println("1.输入一个JAR文件(包括路径和后缀)");
		// Scanner scan = new Scanner(System.in);// 键盘输入值
		String jarFileName = "D:/maven/repository/com/mawujun/leon-generator/1.0-SNAPSHOT/leon-generator-1.0-SNAPSHOT.jar";// 获得键盘输入的值
		OperatorJAR.readJARList(jarFileName,"/templates/default");// 调用方法显示JAR文件中的文件信息
		//System.out.println("2.查看该JAR文件中的哪个文件信息?");

	}

}
