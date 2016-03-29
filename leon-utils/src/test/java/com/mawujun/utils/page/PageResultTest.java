package com.mawujun.utils.page;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PageResultTest {

	@Test
	public void test() {
		//PageParam param=PageParam.getInstance(0, 10);
		
		//PageResult result=new PageResult(param);
		Pager<Object> pager=new Pager<Object>();
		pager.setStart(0);
		pager.setLimit(10);
		pager.setTotal(15);
		
		assertEquals(0, pager.getStart());
		assertEquals(10, pager.getLimit());
		assertEquals(1, pager.getPage());	
		assertEquals(15, pager.getTotal());
		assertEquals(0,pager.getRootSize());
		assertEquals(2,pager.getTotalPages());
		assertEquals(true,pager.hasNextPage());
		assertEquals(false,pager.isLastPage());
		assertEquals(2,pager.getNextPageNo());
		assertEquals(false,pager.hasPrePage());
		assertEquals(true,pager.isFirstPage());
		assertEquals(1,pager.getPrePageNo());

		
	}
	
	@Test
	public void test1() {
		//PageParam param=PageParam.getInstance(10, 10);
		
		//PageResult result=new PageResult(param);
		Pager<Object> pager=new Pager<Object>();
		pager.setStart(10);
		pager.setLimit(10);
		
		pager.setTotal(15);
		
		assertEquals(10, pager.getStart());
		assertEquals(10, pager.getLimit());
		assertEquals(2, pager.getPage());	
		assertEquals(15, pager.getTotal());
		assertEquals(0,pager.getRootSize());
		assertEquals(2,pager.getTotalPages());
		assertEquals(false,pager.hasNextPage());
		assertEquals(true,pager.isLastPage());
		assertEquals(2,pager.getNextPageNo());
		assertEquals(true,pager.hasPrePage());
		assertEquals(false,pager.isFirstPage());
		assertEquals(1,pager.getPrePageNo());

		
	}

}
