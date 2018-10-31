package com.hnjk.core.rao.dao.helper;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

/**
 * 提供一个分页实体. <p>
 * 封装分页和排序查询的结果,并继承<code>QueryParameter</code>的所有查询请求参数. 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-2-26上午10:14:42
 * @see 
 * @version 1.0
 */
public class Page extends QueryParameter implements java.io.Serializable{
	
	private static final long serialVersionUID = 6901801502908121012L;

	private List result = new ArrayList();

	private int totalCount = -1;

	public Page() {
	}

	public Page(int pageSize) {
		this.pageSize = pageSize;
	}

	public Page(int pageSize, boolean autoCount) {
		this.pageSize = pageSize;
		this.autoCount = autoCount;
	}

	public Page(int pageSize, boolean autoCount,String order,String orderBy) {
		this.pageSize = pageSize;
		this.autoCount = autoCount;
		if (ASC.equalsIgnoreCase(order) || DESC.equalsIgnoreCase(order)) {
			this.order = order.toLowerCase();
			this.orderBy=orderBy;
		} 
	}
	/***
	 * 
	 * @主要功能：构建分页对象
	 * @author: Snoopy Chen (ctfzh@yahoo.com.cn)
	 * @since： Jun 17, 2009 
	 * @param pageNum 要显示的页码
	 * @param pageSize 每页显示记录数
	 * @param autoCount 是否统计记录数
	 * @param order 排序方式 asc|desc
	 * @param orderBy 排序字段
	 */
	public Page(int pageNum,int pageSize, boolean autoCount,String order,String orderBy){
		this.pageSize = pageSize;
		this.pageNum = pageNum;
		this.autoCount = autoCount;
		if (StringUtils.hasText(orderBy) && (ASC.equalsIgnoreCase(order) || DESC.equalsIgnoreCase(order)) ) {
			this.order = order.toLowerCase();
			this.orderBy=orderBy;
		} 
	}
	
	/**
	 * 根据request获取Page对象
	 * (jGrid组件使用)
	 * @author cp
	 * @since:2009-4-3下午04:44:51
	 * @param request
	 * @deprecated 
	 * @return
	 */
	public static Page getInstance(HttpServletRequest request){
		String strPage=request.getParameter("page");     //要显示的页码
		String strRows=request.getParameter("rows");     //每页显示记录数
		String sidx=request.getParameter("sidx");        //排序字段
		String sord=request.getParameter("sord");        //排序规则 e.g. asc/desc
		int no = strPage==null ? 1 : Integer.parseInt(strPage);
		int size = strRows == null ?  20 : Integer.parseInt(strRows);
		if(!StringUtils.hasText(sidx)) {
			sidx=null;
		}
		return new Page(no,size, true,sord,sidx);
	}
	
	/***
	 * 
	 * @主要功能：根据request获取Page对象
	 * (分页标签使用)
	 * @author: Snoopy Chen (ctfzh@yahoo.com.cn)
	 * @since： Jun 17, 2009 
	 * @param request
	 * @deprecated
	 * @return 
	 */
	public static Page initPage(HttpServletRequest request){
		String strPageNo=request.getParameter("pageNum");      //要显示的页码
		String strPageSize=request.getParameter("pageSize");  //每页显示记录数
		String strOrder=request.getParameter("order");        //排序规则 e.g. asc/desc
		String strOrderBy=request.getParameter("orderBy");    //排序字段

		int pageNum = strPageNo==null ? 1 : Integer.parseInt(strPageNo);
		int pageSize = strPageSize == null ?  13 : Integer.parseInt(strPageSize);
		if(!StringUtils.hasText(strOrder)) {
			strOrder="desc";
		}
		if(!StringUtils.hasText(strOrderBy)) {
			strOrderBy=null;
		}

		return new Page(pageNum,pageSize, true,strOrder,strOrderBy);
	}

	/**
	 * 取得倒转的排序方向
	 */
	public String getInverseOrder() {
		if (order.equalsIgnoreCase(DESC)) {
			return ASC;
		} else {
			return DESC;
		}
	}

	/**
	 * 页内的数据列表.
	 */
	@SuppressWarnings("unchecked")
	public List getResult() {
		return result;
	}

	@SuppressWarnings("unchecked")
	public void setResult(List result) {
		this.result = result;
	}

	/**
	 * 总记录数.
	 */
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 计算总页数.
	 */
	public int getTotalPages() {
		if (totalCount == -1) {
			return -1;
		}

		int count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	/**
	 * 是否还有下一页.
	 */
	public boolean isHasNext() {
		return (pageNum + 1 <= getTotalPages());
	}

	/**
	 * 返回下页的页号,序号从1开始.
	 */
	public int getNextPage() {
		if (isHasNext()) {
			return pageNum + 1;
		} else {
			return pageNum;
		}
	}

	/**
	 * 是否还有上一页. 
	 */
	public boolean isHasPre() {
		return (pageNum - 1 >= 1);
	}

	/**
	 * 返回上页的页号,序号从1开始.
	 */
	public int getPrePage() {
		if (isHasPre()) {
			return pageNum - 1;
		} else {
			return pageNum;
		}
	}	
	
}
