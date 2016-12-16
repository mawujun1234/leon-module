package com.mawujun.controller.spring.mvc;

import org.springframework.ui.ModelMap;



/**
 * spring mvc返回的数据格式
 * @author mawujun
 *
 */
public class ResultModel extends ModelMap {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static ResultModel getInstance(){
		ResultModel model= new ResultModel();
		model.setSuccess(true);
		model.setMsg("");
		return model;
	}
	public	ResultModel setSuccess(boolean bool){
		super.put("success", true);
		return this;
	}
	public	ResultModel setMsg(String msg){
		super.put("msg", msg);
		return this;
	}
	public	ResultModel setRoot(Object root){
		super.put("root", root);
		return this;
	}
}
