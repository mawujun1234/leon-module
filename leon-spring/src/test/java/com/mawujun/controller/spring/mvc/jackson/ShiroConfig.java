package com.mawujun.controller.spring.mvc.jackson;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mawujun.controller.shiro.ShiroFormAuthenticationFilter;
import com.mawujun.controller.shiro.ShiroKickoutSessionControlFilter;
import com.mawujun.controller.shiro.ShiroURLPermissionsFilter;
import com.mawujun.shiro.ShiroApplicationListener;
import com.mawujun.shiro.ShiroAuthorizingRealm;

/**
 * http://blog.aquariuslt.com/2015/10/25/apache-shiro-spring-integration/
 */
@Configuration
//@EnableAspectJAutoProxy(proxyTargetClass=true)
//@ComponentScan(basePackages = "com.youngor", excludeFilters = @Filter(type = FilterType.ANNOTATION, value = { Controller.class }))
//@MapperScan(basePackages = "com.youngor")
//@PropertySource("classpath:config.properties")
public class ShiroConfig {
	@Bean
	public ShiroApplicationListener ShiroApplicationListener(){
		ShiroApplicationListener shiroApplicationListener= new ShiroApplicationListener();
		return shiroApplicationListener;
	}

	@Bean
	public DefaultWebSecurityManager securityManager(){
		DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
		securityManager.setRealm(shiroRealm());
		securityManager.setSessionManager(sessionManager());
		return securityManager;
	}
	@Bean
	public Realm shiroRealm(){
		ShiroAuthorizingRealm shiroRealm=new ShiroAuthorizingRealm();

		shiroRealm.setCacheManager(ehcacheManager());
		
		return shiroRealm;
	}
//	@Bean
//	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
//		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor=new AuthorizationAttributeSourceAdvisor();
//		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
//		return authorizationAttributeSourceAdvisor;
//	}
	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
		LifecycleBeanPostProcessor lifecycleBeanPostProcessor=new LifecycleBeanPostProcessor();
		
		return lifecycleBeanPostProcessor;
	}
	
	@Bean
	public ShiroURLPermissionsFilter shiroURLPermissionsFilter(){
		ShiroURLPermissionsFilter shiroURLPermissionsFilter=new ShiroURLPermissionsFilter();
		shiroURLPermissionsFilter.addIgnoreUrl("/main/**");
		shiroURLPermissionsFilter.addIgnoreUrl("/user/login.do*");
		shiroURLPermissionsFilter.addIgnoreUrl("/user/logout.do*");
		return shiroURLPermissionsFilter;
	}
	
	@Bean
	public ShiroFormAuthenticationFilter shiroFormAjaxAuthenticationFilter(){
		ShiroFormAuthenticationFilter formAjaxAuthenticationFilter=new ShiroFormAuthenticationFilter();
		return formAjaxAuthenticationFilter;
	}
	
	
	@Bean//(name = "shiroFilter")
	public Filter shiroFilter() throws Exception{
		ShiroFilterFactoryBean shiroFilter=new ShiroFilterFactoryBean();
		shiroFilter.setSecurityManager(securityManager());
		shiroFilter.setLoginUrl("/main/login.jsp");
		shiroFilter.setSuccessUrl("/main/index.jsp");
		shiroFilter.setUnauthorizedUrl("/main/unauthorized.jsp");
		
		
//		Map<String,Filter> filters=new HashMap<String,Filter>();
//		filters.put("authc", new ShiroFormAjaxAuthenticationFilter()());
//		filters.put("perms", new ShiroURLPermissionsFilter()());	
		//filters.put("kickout", shiroKickoutSessionControlFilter);
		//shiroFilter.setFilters(filters);
		
		
		ShiroKickoutSessionControlFilter shiroKickoutSessionControlFilter=new ShiroKickoutSessionControlFilter();
		shiroKickoutSessionControlFilter.setKickoutUrl("/main/login.jsp");
		//shiroKickoutSessionControlFilter.setMaxSession(1);
		shiroKickoutSessionControlFilter.setKickoutAfter(false);
		shiroKickoutSessionControlFilter.setCacheManager(ehcacheManager());
		shiroKickoutSessionControlFilter.setSessionManager(sessionManager());
		//还要添加到下面的路径中，哪些地址进行拦截
		//filterChainDefinitionMap.put("/**/*.jsp", "authc,perms,kickout");//表示就在这里进行登录
		//下面这3行，就是覆盖默认的实现
		shiroFilter.getFilters().put("kickout", shiroKickoutSessionControlFilter);
		shiroFilter.getFilters().put("authc", shiroFormAjaxAuthenticationFilter());//用户登录的判断
		shiroFilter.getFilters().put("perms", shiroURLPermissionsFilter());
		
		
		//http://jinnianshilongnian.iteye.com/blog/2025656
		Map<String,String> filterChainDefinitionMap=new HashMap<String,String>();
		//filterChainDefinitionMap.put("/**", "anon");//这个是临时的
		filterChainDefinitionMap.put("/", "anon");//anon只是用于判断认证，不用于判断授权
		filterChainDefinitionMap.put("/**/*.html", "anon");
		filterChainDefinitionMap.put("/main/login.jsp*", "anon");
		filterChainDefinitionMap.put("/menu/queryElementPermission.do*", "anon");
		
		filterChainDefinitionMap.put("/main/unauthorized.jsp*", "anon");
		filterChainDefinitionMap.put("/user/login.do*", "anon");
		filterChainDefinitionMap.put("/user/logout.do*", "anon");
		filterChainDefinitionMap.put("/user/mobile/login.do*", "anon");
		filterChainDefinitionMap.put("/user/mobile/getWxConfig.do*", "anon");
		filterChainDefinitionMap.put("/ord/mobile/getOrdmt.do*", "anon");
		filterChainDefinitionMap.put("/**/*.css", "anon");
		filterChainDefinitionMap.put("/**/*.js", "anon");
		filterChainDefinitionMap.put("/**/*.gif", "anon");
		filterChainDefinitionMap.put("/**/*.jpg", "anon");
		filterChainDefinitionMap.put("/**/*.png	", "anon");
	
		filterChainDefinitionMap.put("/**/*.jsp*", "authc,perms,kickout");//拦截哪些地址,进入哪些指定的过滤器
		filterChainDefinitionMap.put("/**/*.do*", "authc,perms,kickout");
		//filterChainDefinitionMap.put("/**", "authc");
		//filterChainDefinitionMap.put("", "");
		shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);
		
		return (Filter)shiroFilter.getObject();
		
	}
	


	

	
	@Bean
    public EhCacheManager ehcacheManager() {  
        EhCacheManager em = new EhCacheManager();  
        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");  
        return em;  
    } 
	
	//回话管理部分
	/**
	 * 会话Cookie模板 
	 * @author mawujun qq:16064988 mawujun1234@163.com
	 * @return
	 */
	@Bean
	public SimpleCookie sessionIdCookie(){
		SimpleCookie simpleCookie=new SimpleCookie("sid");
		simpleCookie.setHttpOnly(true);
		simpleCookie.setMaxAge(6000000);
		return simpleCookie;
	}
	@Bean
	public JavaUuidSessionIdGenerator sessionIdGenerator() {
		JavaUuidSessionIdGenerator sessionIdGenerator=new JavaUuidSessionIdGenerator();
		return sessionIdGenerator;
	}
	@Bean
	public EnterpriseCacheSessionDAO sessionDAO() {
		EnterpriseCacheSessionDAO sessionDAO=new EnterpriseCacheSessionDAO();
		sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
		sessionDAO.setSessionIdGenerator(sessionIdGenerator());
		return sessionDAO;
	}	
	@Bean
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager=new DefaultWebSessionManager();
		sessionManager.setGlobalSessionTimeout(1800000);//
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		//sessionManager.setSessionValidationScheduler(sessionValidationScheduler());
		sessionManager.setSessionDAO(sessionDAO());
		sessionManager.setSessionIdCookieEnabled(true);
		sessionManager.setSessionIdCookie(sessionIdCookie());
		//监听会话的开始，结束的监听器，实现SessionListenerAdapter方类
		//sessionManager.setSessionListeners();
		return sessionManager;
	}
	/**
	 * 会话验证调度器
	 * @author mawujun qq:16064988 mawujun1234@163.com
	 * @return
	 */
	@Bean
	public ExecutorServiceSessionValidationScheduler sessionValidationScheduler() {
		ExecutorServiceSessionValidationScheduler sessionValidationScheduler=new ExecutorServiceSessionValidationScheduler();
		sessionValidationScheduler.setInterval(120000);
		sessionValidationScheduler.setSessionManager( sessionManager());
		return sessionValidationScheduler;
	}
	
	
	
}
