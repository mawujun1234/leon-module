package com.mawujun.generator.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyColumn {
	private String column;//列名
	


	private String property;//属性名称
	private String property_label;//列的中文名，如果没有设置，就使用column
	public boolean hidden=false;//是否是隐藏字段
	private Boolean nullable=true;//true表示可以为空
	private Integer sort=0;//显示的顺序
	private boolean genQuery=false;//是否生成查询条件，主要是在grid
	
	private Boolean isEnum=false;
	private String showType="none";//显示的类型，是textfield，还是combobox，还是radio，还是
	private Map<String,String> showType_values=new HashMap<String,String>();

	private String basepackage;//包名
	private String javaType;
	private String javaTypeClassName;//其实不用定义这个属性，其实是可以直接写get方法，下同
	private String jsType;
	
	private Class fk_class;//外键关联的class
	private String fk_table;//外键关联的表名
	private String fk_column;//外键关联的列名
	
	
	//private Boolean isIdProperty=false;//是不是属于id的列
	//private Boolean isComponentType=false;
	//private Boolean isAssociationType=false;
	//private Boolean isBaseType=false;
	//private Boolean isCollectionType=false;
	//private Boolean isConstantType=false;//判断是不是常数
	
	//List<PropertyColumn> propertyColumns=new ArrayList<PropertyColumn>();
	
//	//前段展示的时候的标签名字
//	private String label;
//	//展现方式，是下拉框，数字矿还是文本框
//	private String showModel;
	
	private static Map<Class,String> jsJavaMapper=new HashMap<Class,String>();
	static {
		jsJavaMapper.put(String.class, "string");
		jsJavaMapper.put(Charset.class, "string");
		jsJavaMapper.put(char.class, "string");
		
		jsJavaMapper.put(boolean.class, "bool");
		jsJavaMapper.put(Boolean.class, "bool");
		
		jsJavaMapper.put(byte.class, "int");
		jsJavaMapper.put(Byte.class, "int");
		jsJavaMapper.put(short.class, "int");
		jsJavaMapper.put(Short.class, "int");
		jsJavaMapper.put(int.class, "int");
		jsJavaMapper.put(Integer.class, "int");
		jsJavaMapper.put(long.class, "int");
		jsJavaMapper.put(Long.class, "int");
		jsJavaMapper.put(BigInteger.class, "int");
		
		jsJavaMapper.put(float.class, "float");
		jsJavaMapper.put(Float.class, "float");
		jsJavaMapper.put(double.class, "float");
		jsJavaMapper.put(Double.class, "float");
		jsJavaMapper.put(BigDecimal.class, "float");
		
		jsJavaMapper.put(java.util.Date.class, "date");
		jsJavaMapper.put(java.sql.Date.class, "date");
		//jsJavaMapper.put(, "");
		
		//jsJavaMapper.put(, "");
	}

	public void setJavaType(Class clazz) {
		this.javaType = clazz.getName();
		this.basepackage=clazz.getPackage().getName();
		//System.out.println(javaType);
		this.javaTypeClassName=javaType.substring(javaType.lastIndexOf('.')+1);
		//System.out.println(this.javaTypeClassName);
		if(jsJavaMapper.get(clazz)==null){
			this.jsType="string";
		} else {
			this.jsType=jsJavaMapper.get(clazz);//映射好后就是替换 自己写的类然后测试
		}
		
		//System.out.println(this.jsType);
	}
	
	public void addShowType_value(String key,String value) {
		if(this.showType_values==null) {
			this.showType_values=new HashMap<String,String>();
		}
		this.showType_values.put(key, value);
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}
	



	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getBasepackage() {
		return basepackage;
	}

//	public void setBasepackage(String basepackage) {
//		this.basepackage = basepackage;
//	}

	public String getJavaType() {
		return javaType;
	}

	public String getJavaTypeClassName() {
		return javaTypeClassName;
	}

	public String getJsType() {
		return jsType;
	}

	public boolean getHidden() {
		return hidden;
	}
	public Boolean getHidden_notrans() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public String getProperty_label() {
		return property_label;
	}

	public void setProperty_label(String property_label) {
		this.property_label = property_label;
	}

	public String getNullable() {
		return nullable.toString();
	}

	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public Map<String, String> getShowType_values() {
		return showType_values;
	}

	public void setShowType_values(Map<String, String> showType_values) {
		this.showType_values = showType_values;
	}

	public String getIsEnum() {
		return isEnum.toString();
	}

	public void setIsEnum(Boolean isEnum) {
		this.isEnum = isEnum;
	}

	public boolean getGenQuery() {
		return genQuery;
	}

	public void setGenQuery(boolean genQuery) {
		this.genQuery = genQuery;
	}

	public Class getFk_class() {
		return fk_class;
	}

	public void setFk_class(Class fk_class) {
		this.fk_class = fk_class;
	}

	public String getFk_table() {
		return fk_table;
	}

	public void setFk_table(String fk_table) {
		this.fk_table = fk_table;
	}

	public String getFk_column() {
		return fk_column;
	}

	public void setFk_column(String fk_column) {
		this.fk_column = fk_column;
	}


	
}
