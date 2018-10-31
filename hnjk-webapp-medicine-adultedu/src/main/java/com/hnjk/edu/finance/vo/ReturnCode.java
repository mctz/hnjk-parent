package com.hnjk.edu.finance.vo;


public enum ReturnCode {
	code00(00,"成功"),
	code01(01,"删除成功"),
	code02(02,"已付"),
	code03(03,"未付"),
	code05(05,"批量查询完成"),
	code11(11,"签名信息不正确"),
	code12(12,"两次信息不匹配，当收费记录已存在时会出现此错误提示"),
	code13(13,"错误的系统编号"),
	code21(21,"收费记录不存在，在删除时会出现此错误提示"),
	code22(22,"收费项目不存在"),
	code23(23,"收费记录无法删除，在删除时会出现此错误提示"),
	code31(31,"金额格式不正确"),
	code32(32,"otherId长度超出"),
	code99(99,"其他异常错误")
	;
	
	private int code;
	private String text;
	
	private ReturnCode(int code,String text){
		this.setCode(code);
		this.setText(text);
	}
	public static String getReturnCode(int code){
		for (ReturnCode c : ReturnCode.values()) {
            if (c.getCode() == code) {
                return c.text;
            }
        }
        return null;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
