/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mawujun.controller.spring.mvc.jackson;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * 这个类，默认就只是加载spring的DispatchServlet
 * http://hanqunfeng.iteye.com/blog/2114967
 * @author mawujun qq:16064988 mawujun1234@163.com
 *
 */
public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	/**
	 * 添加自定义的Servlet
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
//		//registerDispatcherServlet(servletContext);
//		StatViewServlet statViewServlet=new StatViewServlet();
//		ServletRegistration.Dynamic dynamic = servletContext.addServlet("DruidStatView", statViewServlet);  
//        dynamic.setLoadOnStartup(2);  
//        dynamic.addMapping("/druid/*");
	}
	/**
	 * 加载service层的application
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		//return new Class<?>[] { WebSecurityConfig.class };
		return null;
	}

	/**
	 * 设置spring相关配置的Config类
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { MvcConfig.class};
		//return new Class<?>[] { MvcConfig.class ,RepositoryConfig.class,ShiroConfig.class};
	}

	/**
	 * 获取spring的DispatchServlet拦截哪些url
	 */
	@Override
	protected String[] getServletMappings() {
		//System.out.println("-------------------------");
		//return new String[] { "/" };
		return new String[] {"*.do" };
	}
	/**
	 *  
     * 注册过滤器，映射路径与DispatcherServlet一致，路径不一致的过滤器需要注册到另外的WebApplicationInitializer中 
     * 参考ShiroWebApplicationInitializer
	 */
	@Override
    protected Filter[] getServletFilters() {
		CharacterEncodingFilter filter=new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
        return new Filter[] { filter };
    }


	/**
	 * 为DispatchServlet类设置额外的参数
	 */
	@Override
	protected void customizeRegistration(Dynamic registration) {
		registration.setInitParameter("dispatchOptionsRequest", "true");
	}

}
