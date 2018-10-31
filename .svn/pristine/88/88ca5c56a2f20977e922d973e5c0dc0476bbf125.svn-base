package com.hnjk.platform.system.util;


public enum TMSStatueCode {
	code1(1,"操作成功"),
	code2(-1,"用户名或密码为空"),
	code3(-2,"用户名或者密码错误"),
	code4(-3,"用户名被锁"),
	code5(-7,"发送内容为空"),
	code6(-8,"余额不足"),
	code7(-9,"没有正确的号码"),
	code8(-10,"提交的logid记录不存在"),
	code9(-11,"内容里有不允许的关键字"),
	code10(-12,"本次的手机号码列表超过1000个"),
	code11(-1000,"读取余额时出错"),
	code12(-100,"系统错误"),
	;
	private int statueCode;
	private String text;
	private TMSStatueCode(int code,String text){
		this.setStatueCode(code);
		this.setText(text);
	}
	
	public static String getText(int code) {
        for (TMSStatueCode c : TMSStatueCode.values()) {
            if (c.getStatueCode() == code) {
                return c.text;
            }
        }
        return null;
    }
	public int getStatueCode() {
		return statueCode;
	}
	public void setStatueCode(int statueCode) {
		this.statueCode = statueCode;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
