package com.hnjk.edu.recruit.helper;
/**
 * 第三方短信平台的代码定义
 */
public enum NoteCode {

	Code0(0,"提交成功"),
	Code101(101,"无此用户"),
	Code102(102,"密码错误"),
	Code103(103,"提交过快（提交速度超过流速限制）"),
	Code104(104,"系统忙（因平台侧原因，暂时无法处理提交的短信）"),
	Code105(105,"敏感短信（短信内容包含敏感词）"),
	Code106(106,"消息长度错（>700或<=0）"),
	Code107(107,"包含错误的手机号码"),
	Code108(108,"手机号码个数错（群发>50000或<=0;单发>200或<=0）"),
	Code109(109,"无发送额度（该用户可用短信数已使用完）"),
	Code110(110,"不在发送时间内"),
	Code111(111,"超出该账户当月发送额度限制"),
	Code112(112,"无此产品，用户没有订购该产品"),
	Code113(113,"extno格式错（非数字或者长度不对）"),
	Code115(115,"自动审核驳回"),
	Code116(116,"签名不合法，未带签名（用户必须带签名的前提下）"),
	Code117(117,"IP地址认证错,请求调用的IP地址不是系统登记的IP地址"),
	Code118(118,"用户没有相应的发送权限"),
	Code119(119,"用户已过期"),
	Code120(120,"内容不在白名单模板中")
	;	
	
	private int code;
	private String text;
	
	private NoteCode(int code,String text){
		this.setCode(code);
		this.setText(text);
	}

	public static String getText(int code) {
        for (NoteCode c : NoteCode.values()) {
            if (c.getCode() == code) {
                return c.text;
            }
        }
        return null;
    }
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public static void main(String[] args) {
        System.out.println(NoteCode.getText(1));
    }
}
