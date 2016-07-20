package com.mawujun.shiro;

import org.apache.shiro.SecurityUtils;

import com.mawujun.exception.BusinessException;

public class ShiroUtils {
//	/**
//	 * 获取用户的登陆名
//	 * @return
//	 */
//	public static String getLoginName(){
//		return ((UserVO)SecurityUtils.getSubject().getPrincipal()).getLoginName();
//	}
//	/**
//	 * 获取用户的姓名
//	 * @author mawujun email:160649888@163.com qq:16064988
//	 * @return
//	 */
//	public static String getUserName(){
//		return ShiroUtils.getAuthenticationInfo().getName();
//	}
//	/**
//	 * 获取登陆用户的id
//	 * @author mawujun email:160649888@163.com qq:16064988
//	 * @return
//	 */
//	public static String getUserId(){
//		return ShiroUtils.getAuthenticationInfo().getId();
//	}
//	/**
//	 * 获取当前登陆的用户
//	 * @return
//	 */
//	public static UserVO getAuthenticationInfo(){
//		return (UserVO)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
//	}
//	
//	/**
//	 * 判断是否已经登录的
//	 * @author mawujun qq:16064988 mawujun1234@163.com
//	 * @return
//	 */
//	public static Boolean isLogon(){
//		if(SecurityUtils.getSubject().getPrincipals()==null){
//			return false;
//		}
//		return true;
//	}

}
