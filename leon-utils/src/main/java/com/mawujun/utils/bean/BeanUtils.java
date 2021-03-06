/**
 * Copyright (c) 2005-2011 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: ObjectMapper.java 1627 2011-05-23 16:23:18Z calvinxiu $
 */
package com.mawujun.utils.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.dozer.DozerBeanMapper;

import com.mawujun.utils.Assert;
import com.mawujun.utils.ReflectUtils;
import com.mawujun.utils.string.StringUtils;

/**
 * 对象转换工具类.
 * 1.封装Dozer, 深度转换对象到对象
 * 2.封装Apache Commons BeanUtils, 将字符串转换为对象.
 * 
 * 
 */
public abstract class BeanUtils {

	/**
	 * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
	 */
	private static DozerBeanMapper dozer = new DozerBeanMapper();

	/**
	 * 基于Dozer转换对象的类型.
	 * 主要用于对象之间进行拷贝
	 * 
	 * 1：Long[] aa=BeanUtils.copy(value, long[].class)value也是数组对象
	 * 2：BeanUtils.copyOrCast(map, Product.class);把map对象里的值拷贝到类里面
	 */
	public static <T> T copyOrCast(Object source, Class<T> destinationClass) {
//		Class fromType=source.getClass();
//		if (fromType == destinationClass || destinationClass == null || fromType == null)
//			return (T) source;
//		if (fromType.getName().equals(destinationClass.getName()))
//			return (T) source;
//		if (destinationClass.isAssignableFrom(fromType))
//			return (T) source;
		return dozer.map(source, destinationClass);
	}

	/**
	 * 基于Dozer转换Collection中对象的类型.
	 */
	public static <T> List<T> copyList(Collection sourceList, Class<T> destinationClass) {
		//List<T> destinationList = Lists.newArrayList();
		List<T> destinationList = new ArrayList<T>();//Lists.newArrayList();
		for (Object sourceObject : sourceList) {
			T destinationObject = dozer.map(sourceObject, destinationClass);
			destinationList.add(destinationObject);
		}
		return destinationList;
	}
	/**
	 * 基于Dozer将对象A的值拷贝到对象B中.
	 */
	public static void copyOrCast(Object source, Object destinationObject) {
		//dozer.
		dozer.map(source, destinationObject);
	}
	
	
	private static  HashMap<String,PropertyDescriptor[]> beanPropertyCache=new HashMap<String,PropertyDescriptor[]>();
	private static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws  IntrospectionException {
		 PropertyDescriptor[] pds = beanPropertyCache.get(clazz.getName());
		if(pds!=null){
			//return pds;
		} else {
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);         
			pds = beanInfo.getPropertyDescriptors();
			beanPropertyCache.put(clazz.getName(), pds);
		}
		return pds;
	}
	private static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String propertyName)
			throws  IntrospectionException {         
		 PropertyDescriptor[] pds = getPropertyDescriptors(clazz);
		 for(PropertyDescriptor pd:pds){
			 if(pd.getName().equals(propertyName)){
				 return pd;
			 }
		 }
		 return null;
	}
	/**
	 * 最简单的属性拷贝，不考虑类型转换。
	 * 忽略为null的属性
	 * @param source
	 * @param target
	 * @throws BeansException
	 * @throws IntrospectionException
	 */
	public static void copyExcludeNull(Object source, Object target)
			throws  IntrospectionException {

		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		Class<?> actualEditable = target.getClass();

		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);


		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						if(value==null){//字符串为空就不拷贝
							continue;
						}
						Method writeMethod = targetPd.getWriteMethod();
						if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
							writeMethod.setAccessible(true);
						}
						writeMethod.invoke(target, value);
					}
					catch (Throwable ex) {
						throw new RuntimeException("Could not copy properties from source to target", ex);
					}
				}
			}
		}
	}


	static {
		//初始化日期格式为yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss
		registerDateConverter("yyyy-MM-dd,yyyy-MM-dd HH:mm:ss");
		
		//ConvertUtils.deregister(Integer.class);		
		//ConvertUtils.register(new IntegerConverter(null), Integer.class);

	}

	/**
	 * 定义Apache BeanUtils日期Converter的格式,可注册多个格式,以','分隔
	 */
	public static void registerDateConverter(String patterns) {
		DateConverter dc = new DateConverter();
		dc.setUseLocaleFormat(true);
		dc.setPatterns(StringUtils.split(patterns, ","));
		ConvertUtils.register(dc, Date.class);
	}

	/**
	 * 基于Apache BeanUtils转换字符串到相应类型.
	 * 可以将字符串转换成Integer等类型，转换为数字时，如果转换失败，会返回0，例如"10.123"转换为Intger的时候就会返回0
	 * @param value 待转换的字符串.
	 * @param toType 转换目标类型.
	 */
	public static <T> T convert(String value, Class<T> toType) {
//		//如果类型相同，就不转换了
//		if(toType==String.class){
//			return (T)value;
//		}
		try {
			return (T)ConvertUtils.convert(value, toType);
		} catch (Exception e) {
			throw ReflectUtils.convertReflectionExceptionToUnchecked(e);
		}
	}
	
	public static <T> T convert(Object value, Class<T> toType) {
//		//如果类型相同，就不转换了
//		if(value.getClass()==toType){
//			return (T)value;
//		}
		
		
		try {
			return (T)ConvertUtils.convert(value, toType);
		} catch (Exception e) {
			throw ReflectUtils.convertReflectionExceptionToUnchecked(e);
		}
	}
	/**
	 * 把字符串数组转换成其他类型的数组
	 * @param values
	 * @param toType
	 * @return
	 */
	public static <T> T convert(String[] values, Class<T> toType) {
		return (T)ConvertUtils.convert(values, toType);
	}
	
//	 /**
//     * <p>Copy the specified property value to the specified destination bean,
//     * performing any type conversion that is required.</p>    
//     *
//     * <p>For more details see <code>BeanUtilsBean</code>.</p>
//     *
//     * @param bean Bean on which setting is to be performed
//     * @param name Property name (can be nested/indexed/mapped/combo)
//     * @param value Value to be set
//     *
//     * @exception IllegalAccessException if the caller does not have
//     *  access to the property accessor method
//     * @exception InvocationTargetException if the property accessor method
//     *  throws an exception
//     * @see BeanUtilsBean#copyProperty     
//     */
//    public static void copyProperty(Object bean, String name, Object value)
//        throws IllegalAccessException, InvocationTargetException {
//
//        BeanUtilsBean.getInstance().copyProperty(bean, name, value);
//    }
//	
//	public static void copyProperties(Object source, Object target, String[] ignoreProperties)
//			throws BeansException {
//
//		org.springframework.beans.BeanUtils.copyProperties(source, target, ignoreProperties);
//	}




}