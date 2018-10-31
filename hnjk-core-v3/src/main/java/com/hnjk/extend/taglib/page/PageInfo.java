package com.hnjk.extend.taglib.page;

import com.hnjk.core.rao.dao.helper.Page;

/**
 * 分页标签实体. <code>PageInfo</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-26 下午01:34:51
 * @see 
 * @version 1.0
 */
public class PageInfo {
	
	private int recordCount;//记录总数
	
	private int pageSize;//单页记录数
	
	private int pageIndex;//当前页索引，从 0 开始 -1表示最后一页
	
	private int pageCount; // 总页数
	
	private boolean isFirstPage; // 是否第一页
	
	private boolean isLastPage; // 是否最后一页
	
	private String order;//排序字段
	
	private String orderBy;//排序方式
	
	/**
	 * 根据page对象获取一个pageinfo对象
	 * @since  2009-7-14 下午05:29:15
	 * @param page
	 * @return
	 */
	public static PageInfo getByPage(Page page){
		PageInfo pi = new PageInfo();
		if(page.getTotalCount()==-1){
			pi.setRecordCount(0);
		}else{
			pi.setRecordCount(page.getTotalCount());
		}
		pi.setPageSize(page.getPageSize());
		pi.setPageIndex(page.getPageNum());
		if(page.getTotalPages()==-1){
			pi.setPageCount(0);
		}else{
			pi.setPageCount(page.getTotalPages());
		}
		if(page.getPageNum()==1){
			pi.setFirstPage(true);
		}else{
			pi.setFirstPage(false);
		}
		if(page.getPageNum()==page.getTotalPages()){
			pi.setLastPage(true);
		}else{
			pi.setLastPage(false);
		}
		pi.setOrder(page.getOrder());
		pi.setOrderBy(page.getOrderBy());
		return pi;
	}

	public boolean getIsFirstPage() {
		return isFirstPage;
	}

	public void setFirstPage(boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public boolean getIsLastPage() {
		return isLastPage;
	}

	public void setLastPage(boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	/**
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	
}

