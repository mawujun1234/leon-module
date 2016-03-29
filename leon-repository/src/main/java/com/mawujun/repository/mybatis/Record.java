package com.mawujun.repository.mybatis;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.apache.ibatis.type.Alias;

import com.mawujun.utils.bean.BeanUtils;

/**
 * 当通过mysql进行查询的时候，当需要返回map的时候可以使用这个，这个自带了数据类型转换，降低转换的代码
 * 还有一个功能就是解决数据库对列名大小写不敏感的问题，把列名全部转换成小写，提高程序的跨数据库的可移植性
 * @author mawujun
 *
 */
@Alias("record")
public class Record extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	private DataParser dataParser=new DataParser();
	
	//private String[] columns=null;
	
	public Set<String> getColumns(){
//		if(columns==null){
//			columns=super.keySet().toArray(new String[super.keySet().size()]);
//		}
//		
		return super.keySet();
	}
	
	/**
	 * 把列面全部转换成大写，解决不同数据返回大小写不一样的问题
	 */
	public Object put(String key, Object value){
		return super.put(key.toLowerCase(), value);
	}
	
	public Object getValue(String name)
	{	
		return this.get(name);
	}
	
	public Character getChar(String name)
	{
		return DataParser.parseChar(getValue(name));
	}

	public String getString(String name)
	{
		return DataParser.parseString(getValue(name));
	}
	
	
	public Boolean getBoolean(String name)
	{
		return DataParser.parseBoolean(getValue(name));
	}
	public Byte getByte(String name)
	{
		return DataParser.parseByte(getValue(name));
	}
	public Short getShort(String name)
	{
		return DataParser.parseShort(getValue(name));
	}

	public Integer getInteger(String name)
	{
		return DataParser.parseInteger(getValue(name));
	}

	public Long getLong(String name)
	{
		return DataParser.parseLong(getValue(name));
	}

	public BigInteger getBigInteger(String name)
	{
		return DataParser.parseBigInteger(getValue(name));
	}

	public Float getFloat(String name)
	{
		return DataParser.parseFloat(getValue(name));
	}

	public Double getDouble(String name)
	{
		return DataParser.parseDouble(getValue(name));
	}

	public BigDecimal getBigDecimal(String name)
	{
		return DataParser.parseBigDecimal(getValue(name));
	}

	public Date getDate(String name)
	{
		return DataParser.parseDate(getValue(name));
	}

	public static Record toRecord(Object obj){
		return BeanUtils.copyOrCast(obj, Record.class);
	}

}
