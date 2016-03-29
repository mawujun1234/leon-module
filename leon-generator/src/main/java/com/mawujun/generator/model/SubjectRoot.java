package com.mawujun.generator.model;

import java.util.ArrayList;
import java.util.List;

import com.mawujun.generator.ExtenConfig;

public class SubjectRoot {
	//private String dbName;
	private String tableName;//表名
	private String simpleClassName;//类名，不带包名的
	private String className;
	
	private String basepackage;//包名
	private String idType;
	//private String idColumnName;
	//private String idPropertyName;
	//private String idGeneratorStrategy="";
	//private String sequenceName;
	//private boolean hasResultMap;//是组件关联的时候
	
	//private String jsPackage;//用于用表生成的时候指定的
	//private Map<Object,Object> extenConfig=new HashMap<Object,Object>();
	//private Object extenConfig=new Object();
	
	private ExtenConfig extenConfig=new ExtenConfig();

	List<PropertyColumn> propertyColumns=new ArrayList<PropertyColumn>();
	//List<PropertyColumn> baseTypePropertyColumns=new ArrayList<PropertyColumn>();
	//存放需要产生查询条件的属性
	List<PropertyColumn> queryProperties =new ArrayList<PropertyColumn>();

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSimpleClassName() {
		return simpleClassName;
	}

	public void setSimpleClassName(String simpleClassName) {
		this.simpleClassName = simpleClassName;
	}

	public String getBasepackage() {
		return basepackage;
	}

	public void setBasepackage(String basepackage) {
		this.basepackage = basepackage;
	}

	public List<PropertyColumn> getPropertyColumns() {
		return propertyColumns;
	}

	public void setPropertyColumns(List<PropertyColumn> propertyColumns) {
		this.propertyColumns = propertyColumns;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public ExtenConfig getExtenConfig() {
		return extenConfig;
	}

	public void setExtenConfig(ExtenConfig extenConfig) {
		this.extenConfig = extenConfig;
	}

	public List<PropertyColumn> getQueryProperties() {
		return queryProperties;
	}

	public void setQueryProperties(List<PropertyColumn> queryProperties) {
		this.queryProperties = queryProperties;
	}

}
