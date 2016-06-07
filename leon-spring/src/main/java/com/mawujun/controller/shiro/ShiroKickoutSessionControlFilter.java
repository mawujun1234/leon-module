package com.mawujun.controller.shiro;

import java.io.Serializable;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
/**
 * 并发控制同个账号登录用户的额数量
 * @author mawujun qq:16064988 mawujun1234@163.com
 *
 */
public class ShiroKickoutSessionControlFilter extends AccessControlFilter {
	
	private String cache_name="shiro-kickout-session";

	private String kickoutUrl; //踢出后到的地址
    private boolean kickoutAfter = false; //false=踢出之前登录的,之后登录的用户 默认踢出之前登录的用户
    private int maxSession = -1; //同一个帐号最大会话数 默认-1,表示无限制

    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(cache_name);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if(!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }

        Session session = subject.getSession();
        IShiroUser userVO = (IShiroUser) subject.getPrincipal();
        String username=userVO.getLoginName();
        Serializable sessionId = session.getId();

        //TODO 同步控制
        Deque<Serializable> deque = cache.get(username);
        if(deque == null) {
            deque = new LinkedList<Serializable>();
            cache.put(username, deque);
        }

        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if(!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            deque.push(sessionId);
        }

        Serializable kickoutSessionId = null;
        //如果队列里的sessionId数超出最大会话数，开始踢人
        while(maxSession!=-1 && deque.size() > maxSession) {
            if(kickoutAfter) { //如果踢出后者
                kickoutSessionId = deque.removeFirst();
            } else { //否则踢出前者
                kickoutSessionId = deque.removeLast();
            }
            
            try {
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if(kickoutSession != null) {
                    //设置另一个会话的kickout属性表示踢出了
                    kickoutSession.setAttribute("kickout", true);
                }
            } catch (Exception e) {//ignore exception
            	e.printStackTrace();
            }
        }
        //当另一个账号访问的时候，就会发现，被标记了退出，这个时候就退出，如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickout") != null) {
            //会话被踢出了
            try {
                subject.logout();
            } catch (Exception e) { //ignore
            }
//            saveRequest(request);
//            
//            WebUtils.issueRedirect(request, response, kickoutUrl);
//            return false;
            
            String errorMsg="你已经被踢出!";
            String jsonpCallback=request.getParameter("jsonpCallback");
    		if(jsonpCallback!=null){
    			response.getWriter().write(jsonpCallback+"({\"success\":false,\"errorMsg\":\""+errorMsg+"\"})");
        		response.getWriter().close();
    		} else {
    			
            	String accept=((HttpServletRequest)request).getHeader("Accept");

            	if(accept!=null && accept.indexOf("application/json")!=-1){
            		WebUtils.toHttp(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            		response.setContentType(accept);
            		response.getWriter().write("({\"success\":false,\"errorMsg\":\""+errorMsg+"\"})");
            		//response.getWriter().write("{\"success\":false,\"reasons\":{\"code\":\"noPermission\"},\"root\":\""+unauthorizedUrl+"\"}");
                	response.getWriter().close();
            	} else {
                 
        
                  if (StringUtils.hasText(kickoutUrl)) {
                	  //request.setAttribute("errorMsg", errorMsg);
                	  saveRequest(request);
                	  WebUtils.toHttp(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                	  Map<String,String> params=new HashMap<String,String>();
                	  params.put("errorMsg", errorMsg);
                      WebUtils.issueRedirect(request, response, kickoutUrl,params);
                  } else {
                      WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                  }
            	}
    		}
    		return false;
        }
//        if(kickoutSessionId!=null){
//            //会话被踢出了
//            try {
//                subject.logout();
//            } catch (Exception e) { //ignore
//            }
//            saveRequest(request);
//            //request.setAttribute("errMsg", "该账号已登录,请过半小时再登录或者检查是否有其他人使用该账号进行登录!");
//            WebUtils.issueRedirect(request, response, kickoutUrl);
//            return false;
//          }

        return true;
    }

}
