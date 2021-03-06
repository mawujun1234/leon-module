package com.mawujun.controller.spring.mvc.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mawujun.exception.BusinessException;

public class MappingExceptionResolver extends SimpleMappingExceptionResolver {
	
	private static Logger logger=LogManager.getLogger(MappingExceptionResolver.class);
	
	private String errorMsgAttribute = "msg";//异常消息的属性名称，可以自定义,也就是对异常进行文字化描述，而不是其他信息
	private String defaultErrorMsg="系统异常,请联系管理员!";
	/**
	 * key是viewname，value是错误消息
	 */
	private Map<Class<? extends Exception>, String> errorMsgs = new HashMap<Class<? extends Exception>, String>();
	
//	public void setErrorMsgs(Properties errorMsgs) {
//		for (Enumeration<?> enumeration = errorMsgs.propertyNames(); enumeration.hasMoreElements();) {
//			String viewName = (String) enumeration.nextElement();
//			String errorMsg = errorMsgs.getProperty(viewName);
//			this.errorMsgs.put(viewName, errorMsg);
//		}
//	}

	/**
	 * 设置对应的viewname中的异常信息
	 * 
	 */
	public void addErrorMsg(Class<? extends Exception> exception, String errorMsg) {
		this.errorMsgs.put(exception, errorMsg);
	}
	/**
	 * 返回默认的错误消息
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @param request
	 * @param viewName
	 * @return
	 */
	protected String determineErrorMsg( String viewName, Exception ex) {
		
		if(ex instanceof BusinessException){
			return ((BusinessException)ex).getMessage();
		} else if (this.errorMsgs.containsKey(ex.getClass())) {
			return this.errorMsgs.get(ex.getClass());
		} 
//		else if(ex.getMessage()!=null && !"".equals(ex.getMessage())){
//			return ex.getMessage();
//		} 
		//只有在ExceptionMappings没有定义过的时候才会走到这里
		return this.defaultErrorMsg;
		
//		if(ex instanceof BusinessException){
//			return ((BusinessException)ex).getMessage();
//		} else if (this.errorMsgs.containsKey(viewName)) {
//			return this.errorMsgs.get(viewName);
//		} else if(ex.getMessage()!=null && !"".equals(ex.getMessage())){
//			return ex.getMessage();
//		} 
//		//只有在ExceptionMappings没有定义过的时候才会走到这里
//		return this.defaultErrorMsg;
	}
	
	private ObjectMapper objectMapper;
	public ObjectMapper getObjectMapper(){
		if(objectMapper!=null){
			return objectMapper;
		}
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
	        .indentOutput(true)
	        .dateFormat(simpleDateFormat);
	        //.modulesToInstall(new ParameterNamesModule());
		ObjectMapper mapper=builder.build();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true); //允许出现没有双引号的字段名称
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true) ;//允许出现单引�?
		return mapper;
	}
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) {
		ex.printStackTrace();
		logger.error(ex);
		//如果是json的时候怎么办？
		//判断是不是以ResponseBody返回的
		HandlerMethod handlerMethod = (HandlerMethod) handler;   
		ResponseBody body = handlerMethod.getMethodAnnotation(ResponseBody.class);
		if(body!=null){
			//http://wenku.baidu.com/link?url=VU-cIAmVAqII8J4_jc96YlVV6IdlSJfhpGg0dUCx69mm6xsCx0CJtESW4nR5FQn7T3zRFS0bUAXodgwYq_I67nYBG4NZhATKAyAgGrEW3ki
			//response.setStatus(HttpStatus.OK.value());
			response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
			
			// 设置ContentType    
			response.setContentType(MediaType.APPLICATION_JSON_VALUE); 
			// 避免乱码    
			response.setCharacterEncoding("UTF-8");    
			response.setHeader("Cache-Control", "no-cache, must-revalidate"); 
			
			try {

				PrintWriter writer = response.getWriter();
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("success", false);
				map.put("msg", determineErrorMsg(null,ex));
				if(ex instanceof BusinessException){
					map.put("errorCode", ((BusinessException)ex).getErrorCode());
				}
				//writer.write();    
				getObjectMapper().writeValue(writer, map);
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    
			return new ModelAndView();
		}

		// Expose ModelAndView for chosen error view.
		String viewName = determineViewName(ex, request);
		if (viewName != null) {
			// Apply HTTP status code for error views, if specified.
			// Only apply it if we're processing a top-level request.
			Integer statusCode = determineStatusCode(request, viewName);
			if (statusCode != null) {
				applyStatusCodeIfPossible(request, response, statusCode);
			}
			return getModelAndView(viewName, ex, request);
		}
		else {
			return null;
		}
	}
	

	
	@Override
	protected ModelAndView getModelAndView(String viewName, Exception ex) {

		//调用父的getModelAndView获取到viewname，并且把异常信息放到exception属性中
		ModelAndView mv=super.getModelAndView(viewName, ex);//是否增加，一个条件，如果是移动端的话，就不返回异常stack，只返回异常信息
		String errorMsg=determineErrorMsg(viewName, ex);
		mv.addObject(this.errorMsgAttribute, errorMsg);
		return mv;
	}

	public void setErrorMsgAttribute(String errorMsgAttribute) {
		this.errorMsgAttribute = errorMsgAttribute;
	}

}
