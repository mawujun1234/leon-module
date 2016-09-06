package com.mawujun.utils;

public class NumberFormat {
	static java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
	static java.text.NumberFormat numberFormat=java.text.NumberFormat.getInstance();
	
	/**
	 * 保留2为小数，没有逗号分隔
	 * @author mawujun qq:16064988 mawujun1234@163.com
	 * @param value
	 * @return
	 */
	public static String formatNoComma(double value) {
		return df.format(value);
	}
	/**
	 * 12222222.222d返回12,222,222.222
	 * @author mawujun qq:16064988 mawujun1234@163.com
	 * @param value
	 * @return
	 */
	public static String format(double value) {
		return numberFormat.format(value);
	}

}
