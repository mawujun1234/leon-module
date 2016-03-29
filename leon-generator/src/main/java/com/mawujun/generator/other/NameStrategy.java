package com.mawujun.generator.other;

/**
 * 类的名称到表的名称的转换规则
 * @author mawujun email:16064988@qq.com qq:16064988
 *
 */
public interface NameStrategy {
	/**
	 * 把类名转换为表名
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @param className
	 * @return
	 */
	public String classToTableName(String className);
	/**
	 * 把一个属性名称转换为表名
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @param propertyName
	 * @return
	 */
	public String propertyToColumnName(String propertyName);
}
