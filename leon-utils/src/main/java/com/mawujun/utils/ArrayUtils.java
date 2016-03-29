package com.mawujun.utils;

public class ArrayUtils {
	/**
	 * 
	 * @param array
	 * @param sperator 分隔符，把数组中的元素以分隔符分开
	 * @return
	 */
    public static String toString(String[] array, String sperator) {
        if (array == null) {
            return null;
        }
        StringBuilder builder=new StringBuilder();
       for(String str:array){
    	   builder.append(str);
    	   builder.append(sperator);
       }
       return builder.substring(0, builder.length()-1);
    }
}
