package com.hnjk.extend.plugin.excel.file;

/**
 * excel组件-消息实体. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-29下午12:08:32
 * @see 
 * @version 1.0
 */
public class MessageBean {
	
  private boolean result;//结果
  
  private String backGround;//背景色
  
  private String strMessage;//消息字符串
  
  public String getBackGround() {
    return backGround;
  }
  public void setBackGround(String backGround) {
    this.backGround = backGround;
  }
  public boolean isResult() {
    return result;
  }
  public void setResult(boolean result) {
    this.result = result;
  }
  public String getStrMessage() {
    return strMessage;
  }
  public void setStrMessage(String strMessage) {
    this.strMessage = strMessage;
  }
  
  
}
