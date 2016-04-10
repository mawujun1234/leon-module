package com.mawujun.controller.shiro;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;


/**
 * 主要用于进行url过滤，如果具有某个url的权限，就让他过滤，如果没有，就不让他过滤。
 * 执行这个相当于有两级判断
 * filterChainDefinitionMap.put("\\/**\\/*.jsp", "authc,perms,kickout"); 先指定哪些url要进入这个过滤器
 * @author mawujun qq:16064988 mawujun1234@163.com
 *
 */
public class ShiroURLPermissionsFilter extends AuthorizationFilter {//PermissionsAuthorizationFilter {
	
	
	
	private Logger log=LogManager.getLogger(ShiroURLPermissionsFilter.class);
	private Set<String> ignoreUrls=new HashSet<String>();//过滤掉不进行权限判断的url
	protected PatternMatcher pathMatcher = new AntPathMatcher();
	
	//宽松权限控制，这里面存放的是所有要受到管理控制的url，如果不在管理范围内的，默认就是允许访问
	//如果是在这个管理范围内的，但是用户没有这个权限的就不准访问
	//有值就表示只在这个范围内进行权限判断，否则就是拦截在filterChainDefinitionMap指定的所有url进行权限判断
	//相当于是二级缩减
	//可以使用动态拦截方案进行替换
	private Set<String> controllerUrls=new HashSet<String>();
	 
	public void addIgnoreUrl(String url) {
		if(url!=null){
			ignoreUrls.add(url);
		}
	}
	protected boolean pathsMatch(String pattern, String path) {
        return pathMatcher.matches(pattern, path);
    }
	
//    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
//
//        Subject subject = getSubject(request, response);
//        String[] perms = (String[]) mappedValue;
//
//        boolean isPermitted = true;
//        if (perms != null && perms.length > 0) {
//            if (perms.length == 1) {
//                if (!subject.isPermitted(perms[0])) {
//                    isPermitted = false;
//                }
//            } else {
//                if (!subject.isPermittedAll(perms)) {
//                    isPermitted = false;
//                }
//            }
//        }
//
//        return isPermitted;
//    }
	/** 
     *@param mappedValue 指的是在声明url时指定的权限字符串，如/User/create.do=perms[User:create].我们要动态产生这个权限字符串，所以这个配置对我们没用 
     */  
	@Override
    public boolean isAccessAllowed(ServletRequest request,  
            ServletResponse response, Object mappedValue) throws IOException {  
    	Subject subject = getSubject(request, response);//SecurityUtils.getSubject(); 
//    	//如果是管理员账号，那也不进行权限教研
//    	if(subject.getPrincipal()!=null && ((UserVO)subject.getPrincipal()).isAdmin()){
//    		return true;
//    	}
    	
    	//如果是登录页面，就直接返回
    	if(super.isLoginRequest(request, response)){
    		return true;
    	}
    	String current_path=getPathWithinApplication(request);//buildPermissions(request);
    	//如果是过滤掉的path，那也就过滤掉
    	for (String path : this.ignoreUrls) {
             // If the path does match, then pass on to the subclass implementation for specific checks
             //(first match 'wins'):
             if (pathsMatch(path, current_path)) {
                 log.info("当前路径{}匹配 '{}'. 所以不进行权限验证", current_path,path);
                 //Object config = this.appliedPaths.get(path);
                // return isFilterChainContinued(request, response, path, config);
                 return true;
             }
         }
    	
    	//判断是否是在管辖范围内的权限，如果是管辖范围内的权限的话，如果没有授权就不允许访问，否则就可以方位
    	if(controllerUrls!=null && controllerUrls.size()!=0){
    		if(controllerUrls.contains(current_path)){
    			if (!subject.isPermitted(current_path)) {
    	              return false;
    	    	} else {
    	    		return true;
    	    	}
    		} else {
    			//如果不在范围内的话，就默认允许访问
    			return true;
    		}
    	} else {
    		if (!subject.isPermitted(current_path)) {
              return false;
    		}
    	}
    	
         return true;//super.isAccessAllowed(request, response, pathes);  
    }  
    /** 
     * 根据请求URL产生权限字符串，这里只产生，而比对的事交给Realm 
     * @param request 
     * @return 
     */  
    protected String buildPermissions(ServletRequest request) {  
        //String[] perms = new String[1];  
        HttpServletRequest req = (HttpServletRequest) request;  
        String path = req.getServletPath();  
        return path;
       // perms[0] = path;//path直接作为权限字符串  
        /*String regex = "/(.*?)/(.*?)\\.(.*)"; 
        if(url.matches(regex)){ 
            Pattern pattern = Pattern.compile(regex); 
            Matcher matcher = pattern.matcher(url); 
            String controller =  matcher.group(1); 
            String action = matcher.group(2); 
             
        }*/  
        //return perms;  
    }  
    
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {

        Subject subject = getSubject(request, response);
        // If the subject isn't identified, redirect to login URL
        if (subject.getPrincipal() == null) {
            saveRequestAndRedirectToLogin(request, response);
        } else {
//            // If subject is known but not authorized, redirect to the unauthorized URL if there is one
//            // If no unauthorized URL is specified, just return an unauthorized HTTP status code
//            String unauthorizedUrl = getUnauthorizedUrl();
//            //SHIRO-142 - ensure that redirect _or_ error code occurs - both cannot happen due to response commit:
//            if (StringUtils.hasText(unauthorizedUrl)) {
//                WebUtils.issueRedirect(request, response, unauthorizedUrl);
//            } else {
//                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
//            }
        	String jsonpCallback=request.getParameter("jsonpCallback");
    		if(jsonpCallback!=null){
    			WebUtils.toHttp(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    			response.getWriter().write(jsonpCallback+"({\"success\":false,\"errorMsg\":\"没有权限\"})");
        		response.getWriter().close();
    		} else {
    			String unauthorizedUrl = getUnauthorizedUrl();
            	String accept=((HttpServletRequest)request).getHeader("Accept");

            	if(accept!=null && accept.indexOf("application/json")!=-1){
            		WebUtils.toHttp(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            		response.setContentType(accept);
            		response.getWriter().write("({\"success\":false,\"errorMsg\":\"没有权限\"})");
            		//response.getWriter().write("{\"success\":false,\"reasons\":{\"code\":\"noPermission\"},\"root\":\""+unauthorizedUrl+"\"}");
                	response.getWriter().close();
            	} else {
                 
                  //SHIRO-142 - ensure that redirect _or_ error code occurs - both cannot happen due to response commit:
                  if (StringUtils.hasText(unauthorizedUrl)) {
                	  WebUtils.toHttp(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                      WebUtils.issueRedirect(request, response, unauthorizedUrl);
                  } else {
                      WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                  }
            	}
    		}
        
        }
        return false;
    }
	public Set<String> getControllerUrls() {
		return controllerUrls;
	}
	public void setControllerUrls(Set<String> controllerUrls) {
		this.controllerUrls = controllerUrls;
	}
	/**
	 * 增加要由shiro控制的url
	 * @param controllerUrl
	 */
	public void addControllerUrl(String controllerUrl) {
		this.controllerUrls.add(controllerUrl);
	}
}
