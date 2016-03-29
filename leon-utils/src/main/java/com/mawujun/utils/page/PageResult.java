package com.mawujun.utils.page;

import java.util.List;

@Deprecated
public class PageResult<T> extends PageParam{//
	protected int pageNo = 1;
//	protected int pageSize = 50;// 默认是每页50条
//	protected int start = 1;
//
//	// 这个优先级较高，如果设置了这个值，传递到后台的话，就使用这个值作为参数
//	// mybatis时候用的较多
//	protected Object params;// 具体的参数形式，可能是Map也可能是Bean
	
	protected List<T> result = null;
	protected int total = -1;
//	/**
//	 * PageParam.getInstance().setPageSize(1).setStart(1).setParams("");
//	 * @author mawujun email:160649888@163.com qq:16064988
//	 * @return
//	 */
//	public static PageResult<T> getInstance(){
//		
//		return new PageResult<T>();
//	}
//	public static PageResult getInstance(int start,int limit){
//		PageResult param = new PageResult();
//		param.setStart(start);
//		param.setPageSize(limit);
//		return param;
//	
//	}
	public PageResult(){
		super();
	}
	public PageResult(PageParam pageParam){
		super();
		this.setLimit(pageParam.getLimit());
		this.setParams(pageParam.getParams());
		this.setStart(pageParam.getStart());
	}
	
	public int getPageNo() {
		//开始计算pageNo。看getFirst()
		this.pageNo=Double.valueOf(Math.ceil(new Double(start)/new Double(pageSize))).intValue()+1;
		return pageNo;
	}

	
	
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}
	/**
	 * 返回记录总数
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @return
	 */
	public int getTotal() {
		return total;
	}
	public void setTotal(int totalItems) {
		this.total = totalItems;
	}
	/**
	 * 返回结果集中的数量
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @return
	 */
	public int getResultSize() {
		if(result==null){
			return 0;
		}
		return result.size();
	}
	
	
	/**
	 * 返回页数
	 * 根据pageSize与totalItems计算总页数.
	 */
	public int getTotalPages() {
		return (int) Math.ceil((double) total / (double) getPageSize());

	}

	/**
	 * 是否还有下一页.
	 */
	public boolean hasNextPage() {
		return (getPageNo() + 1 <= getTotalPages());
	}

	/**
	 * 是否最后一页.
	 */
	public boolean isLastPage() {
		return !hasNextPage();
	}

	/**
	 * 取得下页的页号, 序号从1开始.
	 * 当前页为尾页时仍返回尾页序号.
	 */
	public int getNextPageNo() {
		if (hasNextPage()) {
			return getPageNo() + 1;
		} else {
			return getPageNo();
		}
	}

	/**
	 * 是否还有上一页.
	 */
	public boolean hasPrePage() {
		return (getPageNo() > 1);
	}

	/**
	 * 是否第一页.
	 */
	public boolean isFirstPage() {
		return !hasPrePage();
	}

	/**
	 * 取得上页的页号, 序号从1开始.
	 * 当前页为首页时返回首页序号.
	 */
	public int getPrePageNo() {
		if (hasPrePage()) {
			return getPageNo() - 1;
		} else {
			return getPageNo();
		}
	}

//	/**
//	 * 计算以当前页为中心的页面列表,如"首页,。。。。23,24,25,26,27,末页"
//	 * @param count 需要计算的列表大小
//	 * @return pageNo列表 
//	 */
//	public List<Integer> getSlider(int count) {
//		int halfSize = count / 2;
//		int totalPage = getTotalPages();
//
//		int startPageNo = Math.max(getPageNo() - halfSize, 1);
//		int endPageNo = Math.min(startPageNo + count - 1, totalPage);
//
//		if (endPageNo - startPageNo < count) {
//			startPageNo = Math.max(endPageNo - count, 1);
//		}
//
//		List<Integer> result = new ArrayList<Integer>();
//		for (int i = startPageNo; i <= endPageNo; i++) {
//			result.add(i);
//		}
//		return result;
//	}

	
	
	
}
