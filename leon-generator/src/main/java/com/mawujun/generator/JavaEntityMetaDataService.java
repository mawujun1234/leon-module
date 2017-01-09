package com.mawujun.generator;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.mawujun.generator.model.FieldDefine;
import com.mawujun.generator.model.PropertyColumn;
import com.mawujun.generator.model.PropertyColumnComparator;
import com.mawujun.generator.model.ShowType;
import com.mawujun.generator.model.SubjectRoot;
import com.mawujun.generator.other.DefaultNameStrategy;
import com.mawujun.generator.other.NameStrategy;
import com.mawujun.utils.ReflectUtils;
import com.mawujun.utils.properties.PropertiesUtils;

/**
 * 用于从领域模型中读取 meta信息的
 * 
 * @author mawujun qq:16064988 e-mail:16064988@qq.com
 */
public class JavaEntityMetaDataService {

	NameStrategy nameStrategy;
	
	String id_name="id";//默认的id名称
	
	private static Map<String,SubjectRoot> cache=new HashMap<String,SubjectRoot>();
	
	public JavaEntityMetaDataService() {
		
		try {
			//PropertiesUtils aa = PropertiesUtils.load("generator.properties");
			//String className=aa.getProperty("nameStrategy");
			//Class clazz=Class.forName(className);
			nameStrategy=(NameStrategy) DefaultNameStrategy.class.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	public SubjectRoot getClassProperty(Class clazz){
		if(cache.containsKey(clazz.getName())){
			return cache.get(clazz.getName());
		}
		return null;
	}
	public SubjectRoot initClassProperty(Class clazz) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if(cache.containsKey(clazz.getName())){
			return cache.get(clazz.getName());
		}
		SubjectRoot root=new SubjectRoot();
		//ll
		Table tableAnnotation=(Table)clazz.getAnnotation(Table.class);
		if(tableAnnotation!=null){
			root.setTableName(tableAnnotation.name());
		} else {
			Entity entityAnnotation=(Entity)clazz.getAnnotation(Entity.class);
			if(entityAnnotation!=null){
				root.setTableName(entityAnnotation.name());
			} else {
				//throw new RuntimeException("没有在实体类上添加@Entity注解");
				root.setTableName(clazz.getName());
			}
		}
		//root.setTableName(nameStrategy.classToTableName(clazz.getSimpleName().toLowerCase()));

		
		
		root.setSimpleClassName(clazz.getSimpleName());
		root.setClassName(clazz.getName());
		root.setBasepackage(clazz.getPackage().getName());
		
		IdClass idClassAnnotation=(IdClass)clazz.getAnnotation(IdClass.class);
		if(idClassAnnotation!=null){
			//idClassAnnotation.value().getTypeName();
			root.setIdType(idClassAnnotation.value().getCanonicalName());
		} else {
			root.setIdType("String");//默认是String
		}
		
		
		//
		
		
		Field[] fields=ReflectUtils.getAllDeclaredFields(clazz);
		List<PropertyColumn> propertyColumns =new ArrayList<PropertyColumn>();
		//存放需要产生查询条件的属性
		List<PropertyColumn> queryProperties =new ArrayList<PropertyColumn>();
		for(Field field:fields){
			if(Modifier.isFinal(field.getModifiers())){
				continue;
			}
			PropertyColumn propertyColumn=new PropertyColumn();
			propertyColumn.setProperty(field.getName());
			FieldDefine fieldDefine=field.getAnnotation(FieldDefine.class);
			if(fieldDefine!=null){
				if(fieldDefine.title()==null || "".equals(fieldDefine.title())){
					propertyColumn.setProperty_label(field.getName());
				} else {
					propertyColumn.setProperty_label(fieldDefine.title());
				}
				propertyColumn.setHidden(fieldDefine.hidden());
				propertyColumn.setSort(fieldDefine.sort());
				propertyColumn.setShowType(fieldDefine.showType().toString());
				if(fieldDefine.showType()!=ShowType.none){
					//如果是枚举类型，就反射获取枚举值，作为数据的内容，如果不是枚举类型，就弄成一个从后台获取内容的combobox
					if(field.getType().isEnum()){
						propertyColumn.setIsEnum(true);
						//field.get
						Class clz =field.getType();
						Method toName = clz.getMethod("getName");
						//Map<String,String> showTypeValues=new HashMap<String,String>();
						for (Object obj : clz.getEnumConstants()) {
							propertyColumn.addShowType_value(obj.toString(), toName.invoke(obj).toString());
							//System.out.println(obj);
							//System.out.println(toName.invoke(obj));
						}
					}
				}
				propertyColumn.setGenQuery(fieldDefine.genQuery());
			} else {
				propertyColumn.setProperty_label(propertyColumn.getProperty());
			}
			//不准为空的判断
			Column column=field.getAnnotation(Column.class);
			if(column!=null){
				propertyColumn.setNullable(column.nullable());
			}
			NotNull notNull=field.getAnnotation(NotNull.class);
			if(notNull!=null){
				propertyColumn.setNullable(false);
			}
			
			propertyColumn.setColumn(nameStrategy.propertyToColumnName(propertyColumn.getColumn()));
			propertyColumn.setJavaType(field.getType());
			propertyColumns.add(propertyColumn);
			
			//默认是使用id作为名称，这里只适合单个id的时候
			if(id_name.equals(propertyColumn.getProperty())){
				root.setIdType(field.getType().getSimpleName());
			}
			
			if(propertyColumn.getGenQuery()){
				queryProperties.add(propertyColumn);
			}
		}

		//对属性显示的时候进行排序
		propertyColumns.sort(new PropertyColumnComparator());
		
		
		root.setPropertyColumns(propertyColumns);
		root.setQueryProperties(queryProperties);
		cache.put(clazz.getName(), root);
		return root;
	}
	public NameStrategy getNameStrategy() {
		return nameStrategy;
	}
	public void setNameStrategy(NameStrategy nameStrategy) {
		this.nameStrategy = nameStrategy;
	}
	

}
