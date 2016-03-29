package com.mawujun.utils.page;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class PageParam {
	protected int pageNo = 1;
	protected int pageSize = 50;// 默认是每页50条
	protected int start = 1;

	// 这个优先级较高，如果设置了这个值，传递到后台的话，就使用这个值作为参数
	// mybatis时候用的较多
	protected Object params;// 具体的参数形式，可能是Map也可能是Bean
	
	//protected List result = null;
	//protected int total = -1;
	/**
	 * PageParam.getInstance().setPageSize(1).setStart(1).setParams("");
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @return
	 */
	public static PageParam getInstance(){
		
		return new PageParam();
	}
	public static PageParam getInstance(int start,int limit){
		PageParam param = new PageParam();
		param.setStart(start);
		param.setPageSize(limit);
		return param;
	}
	
	public int getPageNo() {
		//开始计算pageNo。看getFirst()
		this.pageNo=Double.valueOf(Math.ceil(new Double(start)/new Double(pageSize))).intValue()+1;
		return pageNo;
	}

	/**
	 * 每页显示多少条
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		//return this;
	}
	public void setLimit(int pageSize) {
		this.pageSize = pageSize;
		//return this;
	}
	public int getLimit() {
		return this.pageSize;
	}
	public int getStart() {
		return start;
	}
	public PageParam setStart(int start) {
		this.start = start;
		return this;
	}
	public Object getParams() {
		return params;
	}
	/**
	 * 设置参数，一般是作为where条件的，可以是map，bean等各种类型
	 * 和addParams是冲突的，只能选一个
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @param params
	 */
	public PageParam setParams(Object params) {
		this.params = params;
		return this;
	}
	/**
	 * 调用这个方法，就默认参数Map类型的,
	 * 默认null和空字符串不会添加进去
	 * 和setParams是冲突的，只能选一个
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @param key
	 * @param params
	 * @return
	 */
	public PageParam addParam(String key,Object value) {
		if(value==null || "".equals(value)){
			return this;
		}
		if(this.params==null || !(this.params instanceof Map)){
			this.params=new HashMap<String,Object>();
		}
		((Map<String,Object>)this.params).put(key, value);
		return this;
	}


	
	
	
}
