package com.hnjk.core.rao.dao.helper;

import org.apache.commons.lang.StringUtils;

/**
 * 封装分页和排序查询请求参数. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-3-3下午08:54:34
 * @see 
 * @version 1.0
 */
public class QueryParameter implements java.io.Serializable {

	private static final long serialVersionUID = -1365314618631261502L;

	/**顺序˳��*/
	public static final String ASC = "asc";
	
	/**����降序*/
	public static final String DESC = "desc";
	
	protected int pageNum = 1; 		//默认起始业号
	protected int pageSize = 100;	//默认页数
	protected String orderBy = null;//排序
	protected String order = DESC;	//默认排序
	protected boolean autoCount = true; //是否统计记录数

	/**
	 *  获得每页的记录数量,无默认值.
	 * @return 每页记录数
	 */
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 是否已设置每页的记录数量.
	 * @return boolean
	 */
	public boolean isPageSizeSetted() {
		return pageSize > -1;
	}

	/**
	 * 获得当前页的页号,序号从1开始,默认为1.
	 * @return 当前页号
	 */
	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	/**
	 * 根据pageNum和pageSize计算当前页第一条记录在总结果集中的位置,序号从0开始.
	 * @return 第一条记录在结果集中的位置
	 */
	public int getFirst() {
		if (pageNum < 1 || pageSize < 1) {
			return -1;
		} else {
			return ((pageNum - 1) * pageSize);
		}
	}

	/**
	 * 是否已设置第一条记录记录在总结果集中的位置.
	 * @return boolean
	 */
	public boolean isFirstSetted() {
		return (pageNum > 0 && pageSize > 0);
	}

	/**
	 * 获得排序字段,无默认值.
	 * @return 排序字段
	 */
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 是否已设置排序字段.
	 * @return boolean
	 */
	public boolean isOrderBySetted() {
		return StringUtils.isNotBlank(orderBy);
	}

	/**
	 *  获得排序方向,默认为asc.
	 * @return 排序方向
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * 设置排序方式向.
	 * @param order 可选值为desc或asc.
	 */
	public void setOrder(String order) {
		if (ASC.equalsIgnoreCase(order) || DESC.equalsIgnoreCase(order)) {
			this.order = order.toLowerCase();
		} else {
			throw new IllegalArgumentException("order should be 'desc' or 'asc'");
		}
	}

	/**
	 * 是否自动获取总页数,默认为false.
	 * 注意本属性仅于query by Criteria时有效,query by HQL时本属性无效.
	 * @return boolean
	 */
	public boolean isAutoCount() {
		return autoCount;
	}

	public void setAutoCount(boolean autoCount) {
		this.autoCount = autoCount;
	}
}