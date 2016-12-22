package com.mawujun.controller.spring.mvc;

import java.util.Map;

/**
 * 主要是为了兼容Pager<T>这个分页查询的时候，这样后台接受参数的格式就统一了，只要在前端写一次参数获取就可以了，否则话直接使用Map<String,Object>就可以解决问题了
 * 用于批量接受前段的参数的，主要用于分页查询的时候，前端可能参数很多，或者参数是可选的时候。
 * public void onimport(MapParams params);这个是Controller接受参数的方式，
 * 前段传递参数的方式是：
 * getParams:function(){
 * var toolbars=this.getDockedItems('toolbar[dock="top"]');
 * var params={
				"params['ormtno']":toolbars[0].down("#ordmtcombo").getValue(),
				"params['bradno']":toolbars[0].down("#bradno").getValue(),
				"params['spclno']":toolbars[0].down("#spclno").getValue(),
				"params['sptyno']":toolbars[0].down("#sptyno").getValue(),
				"params['spseno']":toolbars[0].down("#spseno").getValue(),
				
				"params['prsuno']":toolbars[1].down("#prsuno").getValue(),
				"params['prod_state']":toolbars[1].down("#prod_state").getValue(),
				"params['sampnm']":toolbars[1].down("#sampnm").getValue(),
				"params['prodnm']":toolbars[1].down("#prodnm").getValue(),
				"params['sample_state']":toolbars[1].down("#sample_state").getValue()
		};
		return params;
	}
	调用：
	grid.getStore().getProxy().extraParams=grid.getParams();
 * 
 * 
 * 当然主要继承这个类，再加上自己的参数名字(不使用params[xxx]开头的)就可以了
 * @author mawujun qq:16064988 mawujun1234@163.com
 *
 */
public class MapParams {
	Map<String,Object> params;

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

}
