package com.hnjk.edu.roll.vo;

public class StudentStatisticsVo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7645938486605874232L;
	
	private String gradeName;//年级名称
	//本科
	private String LCYX5;//临床医学
	private String HLX5;//护理学
	private String YXJY5;//医学检验
	private String YXYXX5;//医学影像学
	private String YX5;//药学
	private String KQYX5;//口腔医学
	private String XXGL5;//信息管理
	private String ZYX5;//中药学
	private String SUM1;//小计1
	//专科
	private String LCYX6;//临床医学
	private String HL6;//护理
	private String YXJYJS6;//医学检验技术
	private String YXYXJS6;//医学影像技术
	private String YX6;//药学
	private String KQYX6;//口腔医学
	private String ZY6;//中药
	private String SUM2;//小计2
	private String SUM3;//合计
	public StudentStatisticsVo(){};
	public StudentStatisticsVo(String sum1,String sum2,String sum3){
		this.gradeName = "总计";
		this.SUM1=sum1;
		this.SUM2=sum2;
		this.SUM3=sum3;
		this.LCYX5      ="\\" ;
		this.HLX5       ="\\" ;
		this.YXJY5      ="\\" ;
		this.YXYXX5     ="\\" ;
		this.YX5        ="\\" ;
		this.KQYX5      ="\\" ;
		this.XXGL5      ="\\" ;
		this.ZYX5       ="\\" ;
		this.LCYX6      ="\\" ;
		this.HL6        ="\\" ;
		this.YXJYJS6    ="\\" ;
		this.YXYXJS6    ="\\" ;
		this.YX6        ="\\" ;
		this.KQYX6      ="\\" ;
		this.ZY6        ="\\" ;
	}
	
	public String getHLX5() {
		return HLX5;
	}
	public void setHLX5(String hLX5) {
		if(hLX5==null){
			hLX5="0";
		}
		HLX5 = hLX5;
	}
	public String getYXJY5() {
		return YXJY5;
	}
	public void setYXJY5(String yXJY5) {
		if(yXJY5==null){
			yXJY5="0";
		}
		YXJY5 = yXJY5;
	}
	public String getYXYXX5() {
		return YXYXX5;
	}
	public void setYXYXX5(String yXYXX5) {
		if(yXYXX5==null){
			yXYXX5="0";
		}
		YXYXX5 = yXYXX5;
	}
	public String getYX5() {
		return YX5;
	}
	public void setYX5(String yX5) {
		if(yX5==null){
			yX5="0";
		}
		YX5 = yX5;
	}
	public String getKQYX5() {
		return KQYX5;
	}
	public void setKQYX5(String kQYX5) {
		if(kQYX5==null){
			kQYX5="0";
		}
		KQYX5 = kQYX5;
	}
	public String getXXGL5() {
		return XXGL5;
	}
	public void setXXGL5(String xXGL5) {
		if(xXGL5==null){
			xXGL5="0";
		}
		XXGL5 = xXGL5;
	}
	public String getZYX5() {
		return ZYX5;
	}
	public void setZYX5(String zYX5) {
		if(zYX5==null){
			zYX5="0";
		}
		ZYX5 = zYX5;
	}
	public String getSUM1() {
		return SUM1;
	}
	public void setSUM1(String sUM1) {
		if(sUM1==null){
			sUM1="0";
		}
		SUM1 = sUM1;
	}
	
	public String getHL6() {
		return HL6;
	}
	public void setHL6(String hL6) {
		if(hL6==null){
			hL6="0";
		}
		HL6 = hL6;
	}
	public String getYXJYJS6() {
		return YXJYJS6;
	}
	public void setYXJYJS6(String yXJYJS6) {
		if(yXJYJS6==null){
			yXJYJS6="0";
		}
		YXJYJS6 = yXJYJS6;
	}
	public String getYXYXJS6() {
		return YXYXJS6;
	}
	public void setYXYXJS6(String yXYXJS6) {
		if(yXYXJS6==null){
			yXYXJS6="0";
		}
		YXYXJS6 = yXYXJS6;
	}
	public String getYX6() {
		return YX6;
	}
	public void setYX6(String yX6) {
		if(yX6==null){
			yX6="0";
		}
		YX6 = yX6;
	}
	public String getKQYX6() {
		return KQYX6;
	}
	public void setKQYX6(String kQYX6) {
		if(kQYX6==null){
			kQYX6="0";
		}
		KQYX6 = kQYX6;
	}
	public String getZY6() {
		return ZY6;
	}
	public void setZY6(String zY6) {
		if(zY6==null){
			zY6="0";
		}
		this.ZY6 = zY6;
	}
	public String getSUM2() {
		return SUM2;
	}
	public void setSUM2(String sUM2) {
		if(sUM2==null){
			sUM2="0";
		}
		this.SUM2 = sUM2;
	}
	public String getSUM3() {
		return SUM3;
	}
	public void setSUM3(String sUM3) {
		if(sUM3==null){
			sUM3="0";
		}
		this.SUM3 = sUM3;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getLCYX5() {
		return LCYX5;
	}
	public void setLCYX5(String lCYX5) {
		if(lCYX5==null){
			lCYX5="0";
		}
		LCYX5 = lCYX5;
	}
	public String getLCYX6() {
		return LCYX6;
	}
	public void setLCYX6(String lCYX6) {
		if(lCYX6==null){
			lCYX6 = "0";
		}
		LCYX6 = lCYX6;
	}
	
	
}
