/*************************************************
 	author 广东数据通信网络有限公司-政企中心-项目一部-cp
 	version 1.0,CreateTime:2009-05-25
 	
支持三种错误提示方式：1.显示在表单的上面的div，2.弹出错误提示窗口，3.显示在每个验证域的后面
三种方式的调用分别为：1.$("#formid").validator("div")
				  2.$("#formid").validator("alert")
				  3.$("#formid").validator("tail")

验证域的例子如：<input type="text" name="courseShortName" validate="Require,Chinese" mes="课程简称"/>
validate为验证的类型，mes为验证的字段描述,多个验证用逗号分隔符分开

Validator目前可实现的验证类型有：
1.Require  		： 是否为空字符串
2.Chinese  		：  中文
3.English  		：  英文
4.Number   		：  正整数
5.LoginCount	:	是否账号(字母开头的字母数字下划线组合,3到18位)
6.Illegal		:	不能是非法字符(只能字母数字中文和下划线等)
7.Date    		：  日期 (格式 MM/dd/yyyy)
8.Time    		：  时间 (格式 MM/dd/yyyy HH:mm:ss)
9.Date2    		：  中国日期 (格式 yyyy-MM-dd)
10.Time2   		：  中国时间 (格式 yyyy-MM-dd HH:mm:ss)
11.Email    		：  邮件
8.Url      		：  使用HTTP协议的网址
9.IpAddress     ：  ip地址
10.PostalCode   ：  邮政编码
11.PhoneCall   	：  电话号码
12.Mobile  		：  手机号码
13.Repeat       ：  某项的重复值 比如 Repeat(other:dgsg)
14.Compare      ：  两数的关系比较  可选配置：eq(默认等于),neq(不等于),gt(大于),gteq(大于等于),lt(小于),lteq(小于等于).比如 Compare(gt:80)
15.Range        ：  范围的验证,的格式为：Range(min:50|max:100)
16.Group        ：  限制具有相同名称的多选按钮的选中数目 可选配置 min, max, eq 比如： Group(min:2|max:5)
17.IDNumber		:	身份证号(15位或18位规则定义)
18 LimitLen     :   验证输入长度是否合法,可选配置：max(最大长度).比如 LimitLen(min:2|max:80),LimitLen(min:2),LimitLen(max:80),add by Terry
*/

jQuery.extend({
	//去除左边的空格
    ltrim: function(_str){
        return _str.replace(/(^\s*)/g, "");
    },
    //去除右边的空格
    rtrim: function(_str){
        return _str.replace(/(\s*$)/g, "");
    },
    //因为jquery本身已经有了trim方法,故这里不再重新定义
    //计算字符串长度，一个双字节字符长度计2，ASCII字符计1
    lengthw: function(_str){
        return _str.replace(/[^\x00-\xff]/g, "rr").length;
    },
    //转换为大写
    toUpper: function(_str){
        return _str.toUpperCase();
    },
    //转换为小写
    toLower: function(_str){
        return _str.toLowerCase();
    },
    //根据参数名获取参数值。字符窜的格式为：(min:50|max:100)
    getParaValue: function(_str,paraName){
    	//var str=$.toLower(_str);
    	var str=_str;
    	var val="";
    	var start=str.indexOf(paraName);
    	if(start!=-1){
    		start=start+paraName.length+1;
    		var end=str.indexOf("|",start);
    		if(end==-1)end=str.length;
    		val=str.substring(start,end);
    	}
    	return val;
    },
    // 判断是否为双精度
    isDouble : function(input,precise){
		if(typeof precise == "undefined" || !((/^[#]+\.[#]+$/).test(precise)))
			return (/^[-\+]?\d+(\.\d+)?$/).test(input);
		var n = precise.split(".")[0].length;
		var m = precise.split(".")[1].length;
		return new RegExp("^[-\\+]?\\d{1,"+n+"}(\\.\\d{1,"+m+"})?$").test(input);
	},
    //是否为空字符串
    Require: function(input){
    	var _str=input.val();
        var tmp_str = jQuery.trim(_str);
        if(tmp_str.length > 0){
        	return "";
        }else{
        	return input.attr('mes')+"不能为空!";
        }
    },
    /* 不是必需填写字段(注意:此方法只供内部调用,不能在表单字段写此值) add by ctf*/
    NotRequire:function(input){
    	var _str=input.val();
        var tmp_str = jQuery.trim(_str);
        if(tmp_str.length == 0){
        	return "";
        }else{
        	return "false";
        }
    },
    // 是否汉字
    Chinese: function(input){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
    	var _str=input.val();
    	if(/^[\u4E00-\u9FA5]{0,25}$/.test(_str)){
    		return "";
    	}else{
    		return input.attr('mes')+"必须为汉字!";
    	}
    },
    // 是否英文 
    English: function(input){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
    	var _str=input.val();
    	if(/^[A-Za-z]{0,25}$/.test(_str)){
    		return "";
    	}else{
    		return input.attr('mes')+"必须为英文!";
    	}
    },
    // 是否实数
   	Numeric: function(input){
   		if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
   		var _str=input.val();
   		if(/^\-?([1-9]\d*|0)(\.\d*)?$/.test(_str)){
   			return "";
   		}else{
   			return input.attr('mes')+"必须为数字!";
   		}
    },
     // 是否正数
   	Number: function(input){
   		if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
   		var _str=input.val();
   		if(/^[\d|\.|,]+$/.test(_str)){
   			return "";
   		}else{
   			return input.attr('mes')+"必须为正数!";
   		}
    },
      // 是否正整数
   	Integer: function(input){
   		if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
   		var _str=input.val();
   		if(/^[1-9]\d*$/.test(_str)){
   			return "";
   		}else{
   			return input.attr('mes')+"必须为正整数!";
   		}
    },

    // 是否账号(字母开头的字母数字下划线以及"-"连接符组合,3到18位)  add by ctf
   	LoginCount: function(input){
   		if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
   		var _str=input.val();
   		if(/^[A-Za-z]+[A-Za-z0-9\-\_]{3,18}$/.test(_str)){
   			return "";
   		}else{
   			return input.attr('mes')+"只能字母开头的字母数字下划线组合!";
   		}
    },
    //不能是非法字符(只能字母数字中文和下划线,空格) add by ctf
    Illegal:function(input){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
    	var _str=input.val();
   		if(/^[a-zA-Z0-9,.，。\s\_\-\u4e00-\u9fa5]+$/.test(_str)){
   			return "";
   		}else{
   			return input.attr('mes')+"不能包含非法字符!";
   		}
    	
    },
    // 是否日期 日期格式 MM/dd/yyyy
    Date:function(input){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
    	var _str=input.val();
    	if(/^\d{2}\/\d{2}\/\d{4}$/.test(_str)){
    		return "";
    	}else{
    		return input.attr('mes')+"必须为日期格式 MM/dd/yyyy!";
    	}
    },
    // 是否时间 日期格式 MM/dd/yyyy HH:mm:ss
    Time: function(input){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
    	var _str=input.val();
    	if(/^\d{2}\/\d{2}\/\d{4}\s\d{2}:\d{2}:\d{2}$/.test(_str)){
    		return "";
    	}else{
    		return input.attr('mes')+"必须为时间格式 MM/dd/yyyy HH:mm:ss!";
    	}
    },
     //日期格式 yyyy-MM-dd   add by ctf
    Date2:function(input){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
    	var _str=input.val();
    	if(/^\d{4}\-\d{2}\-\d{2}$/.test(_str)){
    		return "";
    	}else{
    		return input.attr('mes')+"必须为日期格式 yyyy-MM-dd!";
    	}
    },
    //时间格式 yyyy-MM-dd HH:mm:ss  add by ctf
    Time2: function(input){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
    	var _str=input.val();
    	if(/^\d{4}\-\d{2}\-\d{2}\s\d{2}:\d{2}:\d{2}$/.test(_str)){
    		return "";
    	}else{
    		return input.attr('mes')+"必须为时间格式 yyyy-MM-dd HH:mm:ss!";
    	}
    },
    //是否为合法电子邮件地址
    Email: function(input){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
    	var _str=input.val();
    	if(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(_str)){
    		return "";
    	}else{
    		return input.attr('mes')+"不是合法电子邮件地址!";
    	}
    },
    //是否合法url地址
    URL: function(input){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
    	var _str=input.val();
        var regTextUrl = /^(file|http|https|ftp|mms|telnet|news|wais|mailto):\/\/(.+)$/;
        if(regTextUrl.test(_str)){
        	return "";
        }else{
        	return input.attr('mes')+"不是合法url地址!";
        }
    },
    //身份证号(15位或18位规则定义)
    IDNumber:function(input){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
    	var _str=input.val();
    	//身份证正则表达式(15位)
		//isIDCard1=/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
		//身份证正则表达式(18位)
		//isIDCard2=/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/
        //var regTextUrl = isIDCard1;
        if(_idCardValidate(_str)){
        	return "";
        //}else if(_idCardValidate(_str)){
        //	return "";
        }else{
        	return input.attr('mes')+"不是合法身份证号!";
        }
    },
    //是否为合法ip地址
    IpAddress: function(input){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
   	 	var _str=input.val();
        reVal = /^(\d{1}|\d{2}|[0-1]\d{2}|2[0-4]\d|25[0-5])\.(\d{1}|\d{2}|[0-1]\d{2}|2[0-4]\d|25[0-5])\.(\d{1}|\d{2}|[0-1]\d{2}|2[0-4]\d|25[0-5])\.(\d{1}|\d{2}|[0-1]\d{2}|2[0-4]\d|25[0-5])$/;
        if(reVal.test(_str)){
        	return "";
        }else{
        	return input.attr('mes')+"不是合法ip地址!";
        }
    },
    //是否邮政编码(中国)
    PostalCode: function(input){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
    	var _str=input.val();
        var regTextPost = /^(\d){6}$/;
        if(regTextPost.test(_str)){
        	return "";
        }else{
        	return input.attr('mes')+"不是有效的邮政编码(中国)!";
        }
    },
    //是否有效的电话号码(中国)
    PhoneCall: function(input){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
    	var _str=input.val();
    	if(/(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^[0-9]{3,4}[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)/.test(_str)){
    		return "";
    	}else{
    		return input.attr('mes')+"不是有效的电话号码(中国)!";
    	}
    },
    //是否有效的手机号码(最新的手机号码段可以是15开头的
    Mobile: function(input){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
    	var _str=input.val();
//    	if(/^0{0,1}1(3|5|8)[0-9]{9}$/.test(_str)){
    	if(/^0{0,1}1(3|4|5|7|8)[0-9]{9}$/.test(_str)){
    		return "";
    	}else{
    		return input.attr('mes')+"不是有效的手机号码!";
    	}
    },
    //是否有效密码,长度在6-20之间
    Password: function(input){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
    	var _str=input.val();
    	if(/^\w{6,20}$/.test(_str)){
    		return "";
    	}else{
    		return input.attr('mes')+"长度必须在6-20之间!";
    	}
    },
    // 某项的重复值 比如 Repeat(other:userPwd),其中other是固定的,userPwd是要比较的对象Id.
    Repeat: function (input,paraValue){
    	var _str=input.val();
    	var otherValue=$.getParaValue(paraValue,"other");
    	var other=$("#"+otherValue);
    	if(other.length==0){
    		other=$("input[name="+otherValue+"]");
    	}	
    	if(_str==other.val()){
    		return "";
    	}else{
    		return input.attr('mes')+"必须跟"+other.attr('mes')+"相同!";
    	}
    },
    // 范围的验证,compareValue的格式为：Range(min:50|max:100)
    Range: function (input,paraValue){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
    	var _str=input.val();
    	var min=$.getParaValue(paraValue,"min");
    	var max=$.getParaValue(paraValue,"max");
    	if(parseFloat(min,10) <= parseFloat(_str,10) && parseFloat(_str,10) <= parseFloat(max,10)){
    		return "";
    	}else{
    		return input.attr('mes')+"必须在"+min+"-"+max+"之间!";
    	}
    },
    //两数的关系比较  可选配置：eq(默认等于),neq(不等于),gt(大于),gteq(大于等于),lt(小于),lteq(小于等于).比如 Compare(gt:80)
    Compare: function(input,paraValue){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
    	var _str=input.val();
    	var opt = new Array('eq','neq','gt','ge','lt','le');
    	var optq = {
    		'eq'  : '==',
		 	'neq' : '!=',
		 	'gt'  : '>',
		 	'ge'  : '>=',
		 	'lt'  : '<',
		 	'le'  : '<='
		 };
		var opms = {
			'eq'  : '等于',
		 	'neq' : '不等于',
		 	'gt'  : '大于',
		 	'ge'  : '大于等于',
		 	'lt'  : '小于',
		 	'le'  : '小于等于'
		}
    	var op="";
    	var opval="";
    	for(var i=0;i<opt.length;i++){  		
    		opval=$.getParaValue(paraValue,opt[i]);
    		if(opval!=""){
    			op=opt[i];
    			break;
    		}
    	}
    	if(eval("parseFloat("+_str+",10) " + optq[op] + " parseFloat("+opval+",10)")){
    		return "";
    	}else{
    		return input.attr('mes')+"必须"+opms[op]+opval+"!";
    	}
    },
    
    //验证输入长度是否合法,可选配置：max(最大长度).比如 LimitLen(min:2|max:80),LimitLen(min:2),LimitLen(max:80),add by Terry
    LimitLen: function(input,paraValue){
    	if(jQuery.NotRequire(input) == ""){ //判断是否必填
    		return "";
    	}
    	var _str=input.val() || '';
		var min=$.getParaValue(paraValue,"min") || 0;    	
    	var max=$.getParaValue(paraValue,"max") || Number.MAX_VALUE;
    	
    	var len = _str.replace (/[^\x00-\xff]/g,"rr").length;
    	var isGeMin = len>=min;//大于等于最小
    	var isLeMax = len<=max;//小于等于最大
    	if(isGeMin && isLeMax){
    		return "";
    	}else{
    		if(!isGeMin){
    			return input.attr('mes') + "最少输入"+min+"个字符!";
    		}
    		if(!isLeMax){
    			return input.attr('mes') + "最多输入"+max+"个字符!";
    		}    		
    	}
    },    
    
    // 限制具有相同名称的多选按钮的选中数目 可选配置 min, max, eq 比如： Group(min:2|max:5)
    Group: function(input,paraValue){
    	var min=$.getParaValue(paraValue,"min");
    	var max=$.getParaValue(paraValue,"max");
    	var eq=$.getParaValue(paraValue,"eq");
    	var check=false;
    	var checkLength=$("form input[name="+input.attr("name")+"]:checked").length;
    	var errorMes="";
    	if(eq!=""){
    		if(checkLength==parseInt(eq,10)){
    			check=true;
    		}else{
    			errorMes="必须选中"+eq+"项!";
    		}
    	}
    	if(min!=""&&max==""){
    		if(checkLength>=parseInt(min,10)){
    			check=true;
    		}else{
    			errorMes="至少选中"+min+"项!";
    		}
    	}
    	if(max!=""&&min==""){
    		if(checkLength<=parseInt(max,10)){
    			check=true;
    		}else{
    			errorMes="最多能选中"+max+"项!";
    		}
    	}
    	if(min!=""&&max!=""){
    		if(checkLength>=parseInt(min,10)&&checkLength<=parseInt(max,10)){
    			check=true;
    		}else{
    			errorMes="只能选择"+min+"到"+max+"项!";
    		}
    	}
    	if(check){
    		return "";
    	}else{
    		return input.attr('mes')+errorMes;
    	}
    }
    
});
/**
 * 验证单独的域，返回错误信息，假如验证通过，返回的信息为"";
 */
$.fn.validatorField=function(){
	var config={
 		validateAttr:'validate', /* 表单中要验证的属性 */
 		mesAttr:'mes' /* 提示信息 */
 	}
	var errorMes="";
	var input=$(this);
	var validates = input.attr(config.validateAttr);
 	if(typeof(validates)!='undefined'){
 		var validateArr=validates.split(',');
 		for(var i=0;i<validateArr.length;i++){
 			var val=validateArr[i];
 			var returnMes="";
 			if(val.indexOf("(")!=-1){		
 				var paraValue=val.substring(val.indexOf("(")+1,val.indexOf(")"));
 				val=val.substring(0,val.indexOf("("));
 				returnMes=$[val](input,paraValue);
 			}else{
 				returnMes=$[val](input);
 			}
 			if(returnMes!=""){
 				errorMes+=returnMes+'  \n';
 				break;
 			}
 		}
 	}
 	return errorMes;
}

/**
 * 验证整个表单，返回错误信息
 */
$.fn.getValidatorMes=function(){
	var form=$(this);
 	var errorMes="";
 	form.find(':input').each(function(){
 		var returnMes=$(this).validatorField();
 		if(returnMes!=""){
 			errorMes+=returnMes+'  \n';
 		}
 	});
 	return errorMes;
}

function _idCardValidate(idCard){
	//var idCard     = $(obj).val();
	//alert(idCard);
	var Wi         = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];// 加权因子
	var ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];				        // 身份证验证位值.10代表X
	var sum        = 0;
	var isValidityBrith = function(year,month,day){

		var temp_date  = new Date(year,parseFloat(month)-1,parseFloat(day));
		if(year.length == 2){
			var temp_year = temp_date.getYear();
		}else if(year.length == 4){
			var temp_year = temp_date.getFullYear();
		}else{
			//alert("身份证的出生年份不正确");
			return false;
		}
		if(temp_year != parseFloat(year)   
	        || temp_date.getMonth() != parseFloat(month) - 1   
	        || temp_date.getDate() != parseFloat(day)){   
	        
			//alert("身份证的出生日期不正确");
			return false;
	    }else{
	        return true;
	    }
	}
	
	idCard = idCard.replace(/ /g, "").replace(/(^\s*)|(\s*$)/g, ""); 
	if(idCard.length == 15){
		var year  =  idCard.substring(6,8);   
	    var month = idCard.substring(8,10);   
	    var day   = idCard.substring(10,12); 
	    return isValidityBrith(year,month,day);
	}
	if(idCard.length != 18){
		//alert("身份证的长度不正确！");
		return false;
	}
	var a_idCard = idCard.split("");
	if (a_idCard[17].toLowerCase() == 'x') a_idCard[17] = 10;
	for ( var i = 0; i < 17; i++) {
		sum += Wi[i] * a_idCard[i];
	}
	valCodePosition = sum % 11;			// 得到验证码所在位置
	if (a_idCard[17] != ValideCode[valCodePosition]){ 
		//alert("身份证的验证位不正确！");
		return false;
	}
	var year  =  idCard.substring(6,10);   
    var month = idCard.substring(10,12);   
    var day   = idCard.substring(12,14);

    return isValidityBrith(year,month,day);
}


/**
 * 表单验证插件
 * 支持三种错误提示方式：1.显示在表单的上面的div，2.弹出错误提示窗口，3.显示在每个验证域的后面
 * 三种方式的调用分别为：1.$("#formid").validator("div")
 *				  	 2.$("#formid").validator("alert")
 *			  		 3.$("#formid").validator("tail")
 */
 $.fn.validator=function(pattern){
 	var config={
 		showAlert:'alert', /* alert提示方式 */
 		showTail:'tail', /* tail提示方式 */
 		showDiv:'div', /* div提示方式 */
 		validateAttr:'validate', /* 要验证的表单属性 */
 		mesAttr:'mes',  /* 提示信息 */
 		divClass:'errOutDiv', /* div提示方式的样式 */
 		spanClass:'errOutSpan' /* tail提示方式的样式 */
 	}
 	
 	var form=$(this);
 	var errorMes="";
 	// 清空表单域旁边的错误信息(tail提示方式)
 	if(pattern==config.showTail){
 		form.find('.'+config.spanClass).remove();
 	}
 	/* 遍历所有表单域 */
 	form.find(':input').each(function(i){
 		var input=$(this);
 		var validates = input.attr(config.validateAttr);
 		if(typeof(validates)!='undefined'){ /* 如果域需要验证 */
	 		var validateArr=validates.split(',');  /* 多个验证方式之间用逗号分隔 */
	 		for(var i=0;i<validateArr.length;i++){
	 			var val=validateArr[i];
	 			var returnMes="";
	 			if(val.indexOf("(")!=-1){ /* 范围比较验证 */		
	 				var paraValue=val.substring(val.indexOf("(")+1,val.indexOf(")"));
	 				val=val.substring(0,val.indexOf("("));
	 				returnMes=$[val](input,paraValue);
	 			}else{ /* 非范围比较验证 */
	 				returnMes=$[val](input);
	 			}
	 			if(returnMes!=""){ /* 如果验证不通过 */
	 				errorMes+=returnMes+'   \n';     /*多个错误信息分隔符*/
	 				if(pattern==config.showTail){ /* tail提示方式 */
	 					if(input.attr("type")=="checkbox"){
	 						/* 如果是checkbox类型的域,则提示信息添加到其父对象的最后面. */ /* todo: radio类型是否也一样?  */
	 						$('<span class="'+config.spanClass+'">'+returnMes+'</span>').appendTo(input.parent());
	 					}else{
	 						/* 其它类型域,则提示信息添加到当前验证域对象的后面 */
	 						$('<span class="'+config.spanClass+'">'+returnMes+'</span>').insertAfter(input);
	 					}
	 				}
	 				break;
	 			}else{
	 				input.parent().find("."+config.spanClass).remove();
	 			}
	 		}
	 	}
 	});
 	
 	if(errorMes!=""){ /* 如果验证不通过 */
 		if(pattern==config.showAlert){ /* alert提示方式 */
 			alert(errorMes);
 		}
 		if(pattern==config.showDiv){  /* div提示方式 */
 			form.find("#errOutput").remove();
 			/* 在form表单最前面增加错误消息提示层 */
 			$("<div/>").attr("id","errOutput").addClass(config.divClass).html(errorMes).prependTo(form);
 		}
 		return false;
 	}else{
 		if(pattern==config.showDiv){/* tail提示方式,在前面验证每个域时已经提示.这里是清除div方式的错误信息 */
 			form.find("#errOutput").remove();
 		}
 		return true;
 	}
 }