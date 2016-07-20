package com.mawujun.shiro;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.mawujun.controller.shiro.ShiroURLPermissionsFilter;
/**
 * 监听spring的事件，当spring启动后，为shiro准备数据,
 * @author mawujun qq:16064988 mawujun1234@163.com
 *
 */
public class ShiroApplicationListener  implements ApplicationListener<ContextRefreshedEvent> {
	ShiroURLPermissionsFilter shiroURLPermissionsFilter;
	//MenuService menuService;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//为ShiroURLPermissionsFilter准备控制范围内的url
		shiroURLPermissionsFilter=event.getApplicationContext().getBean(ShiroURLPermissionsFilter.class);
		//menuService=event.getApplicationContext().getBean(MenuService.class);
		
		initShiroURLPermissionsFilter();
	}
	
	public void initShiroURLPermissionsFilter() {
//		// 获取所有菜单
//		List<Menu> menus = menuService.query(Cnd.select().andIsNotNull(M.Menu.url));
//		for (Menu menu : menus) {
//			if (menu.getUrl() != null) {
//				shiroURLPermissionsFilter.addControllerUrl(menu.getUrl());
//			}
//		}
	}
}
