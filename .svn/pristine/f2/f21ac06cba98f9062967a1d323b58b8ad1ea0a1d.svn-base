package com.hnjk.extend.plugin.excel.config;

/**
 * Excel合并单元时所需的起始、终止单元格坐标参数.
 * <code>MergeCellsParam</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2011-9-27 下午02:09:12
 * @see 
 * @version 1.0
 */
public class MergeCellsParam implements java.io.Serializable{

	private static final long serialVersionUID = -2738206127156180075L;

	private Integer startColumnNum;//起始列坐标
	
	private Integer startRowNum;//起始行坐标
	
	private Integer endColumnNum;//终止列坐标
	
	private Integer endRowNum;//终止行坐标
	
	public MergeCellsParam() {		
	}
	
	public MergeCellsParam(Integer startColumnNum, Integer startRowNum,
			Integer endColumnNum, Integer endRowNum) {
		super();
		this.startColumnNum = startColumnNum;
		this.startRowNum = startRowNum;
		this.endColumnNum = endColumnNum;
		this.endRowNum = endRowNum;
	}


	/**
	 * @return the startColumnNum
	 */
	public Integer getStartColumnNum() {
		return startColumnNum;
	}

	/**
	 * @param startColumnNum the startColumnNum to set
	 */
	public void setStartColumnNum(Integer startColumnNum) {
		this.startColumnNum = startColumnNum;
	}

	/**
	 * @return the startRowNum
	 */
	public Integer getStartRowNum() {
		return startRowNum;
	}

	/**
	 * @param startRowNum the startRowNum to set
	 */
	public void setStartRowNum(Integer startRowNum) {
		this.startRowNum = startRowNum;
	}

	/**
	 * @return the endColumnNum
	 */
	public Integer getEndColumnNum() {
		return endColumnNum;
	}

	/**
	 * @param endColumnNum the endColumnNum to set
	 */
	public void setEndColumnNum(Integer endColumnNum) {
		this.endColumnNum = endColumnNum;
	}

	/**
	 * @return the endRowNum
	 */
	public Integer getEndRowNum() {
		return endRowNum;
	}

	/**
	 * @param endRowNum the endRowNum to set
	 */
	public void setEndRowNum(Integer endRowNum) {
		this.endRowNum = endRowNum;
	}
	
	@Override
	public String toString() {		
		return "["+getStartColumnNum()+","+getStartRowNum()+","+getEndColumnNum()+","+getEndRowNum()+"]";
	}
}
