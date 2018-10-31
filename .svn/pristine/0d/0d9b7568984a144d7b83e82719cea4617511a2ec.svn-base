<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设置</title>

</head>

<body>
	<div class="page">
		<div class="pageContent">
			<div class="tabs" currentIndex="0" eventType="click">
				<div class="tabsHeader">
					<div class="tabsHeaderContent">
						<ul>
							<li class="selected"><a href="javascript:void(0)"><span>个人信息</span></a></li>
							<li><a
								href="${baseUrl }/edu3/framework/user/setting.html?act=changepwd"
								class="j-ajax"><span>修改密码</span></a></li>
							<li><a
								href="${baseUrl }/edu3/framework/user/setting.html?act=setting"
								class="j-ajax"><span>个人设置</span></a></li>
							<c:if test="${brSchool == true }">
								<li><a
									href="${baseUrl }/edu3/framework/user/setting.html?act=unitSetting"
									class="j-ajax"><span>设置单位联系信息</span></a></li>
							</c:if>
						</ul>
					</div>
				</div>
				<div class="tabsContent" style="height: 260px;">
					<div>
						<!-- 个人信息设置 -->
						<form method="post"
							action="${baseUrl }/edu3/framework/user/savesetting.html?act=info&isChange="
							+isChange class="pageForm" id="userForm"
							onsubmit="return validate();">
							<input type="hidden" name="resourceid" id="resourceid"
								value="${user.resourceid }">
							<c:if test="${currentRole eq 'student' }">
								<input type="hidden" name="certNum" id="certNum"
									value="${user.studentBaseInfo.certNum }">
							</c:if>
							<table class="form">
								<tr>
									<td style="width: 24%">用户姓名:</td>
									<td style="width: 66%"><c:if
											test="${currentRole eq 'student' }">
											<input type="text" name="cnName" readonly="readonly"
												size="40" value="${user.studentBaseInfo.name }"
												class="required" />
										</c:if> <c:if test="${currentRole eq 'edumanage' }">
											<input type="text" name="cnName" readonly="readonly"
												size="40" value="${user.cnName }" class="required" />
										</c:if></td>
								</tr>
								<c:if test="${currentRole eq 'student' }">
									<tr>
										<td style="width: 24%">简易登录账号:</td>
										<td style="width: 66%"><input type="text"
											name="customUsername" size="20"
											value="${user.sysUser.customUsername }" class="alphanumeric" />
										</td>
									</tr>
								</c:if>
								<tr>
									<td style="width: 24%">办公电话:</td>
									<td style="width: 66%"><c:if
											test="${currentRole eq 'student' }">
											<input type="text" name=officeTel size="40"
												value="${user.studentBaseInfo.officePhone }" class="phone" />
										</c:if> <c:if test="${currentRole eq 'edumanage' }">
											<input type="text" name=officeTel size="40"
												value="${user.officeTel }" class="required phone" />
										</c:if></td>
								</tr>
								<tr>
									<td style="width: 24%">家庭电话:</td>
									<td style="width: 66%"><c:if
											test="${currentRole eq 'student' }">
											<input type="text" name="homeTel" size="40"
												value="${user.studentBaseInfo.homePhone }" class="phone" />
										</c:if> <c:if test="${currentRole eq 'edumanage' }">
											<input type="text" name="homeTel" size="40"
												value="${user.homeTel }" class="phone" />
										</c:if></td>
								</tr>
								<tr>
									<c:choose>
										<c:when test="${phoneComfirm=='1' }">
											<td style="width: 24%">手机:</td>
											<td style="width: 66%"><c:if
													test="${currentRole eq 'student' }">
													<input type="text;width:30%" id="changeuser_mobile"
														name="mobile" value="${user.studentBaseInfo.mobile }"
														class="required phone" size="20" />
												</c:if> <c:if test="${currentRole eq 'edumanage' }">
													<input type="text;width:30%" id="changeuser_mobile"
														name="mobile" value="${user.mobile }"
														class="required phone" size="20" />
												</c:if> 验证码:<input type="text" id="changeuser_authCode"
												name="authCode" size="4" /> <img
												src="${baseUrl}/imageCaptcha" id="changeuser_authCodeImg"
												style="margin-bottom: -6px; padding-left: 3px; cursor: pointer;"
												onclick="this.src='${baseUrl}/imageCaptcha?now=' + new Date().getTime()"
												title="换一张"> <br>手机验证码：<input type="text"
												id="changeuser_msgAuthCode" name="msgAuthCode" size="5" />
												<button id="changeuser_getMsgAuthCodeCU"
													onclick="return getMsgAuthCodeCU();"
													style="cursor: pointer;">获取手机验证码</button> <!-- <button  onclick="return editContact('mobile');" style="cursor: pointer;">确认</button></td>	 -->
										</c:when>
										<c:otherwise>
											<td style="width: 24%">手机:</td>
											<td style="width: 66%"></td>
										</c:otherwise>
									</c:choose>
								</tr>
								<tr>
									<td style="width: 24%">邮件地址:</td>
									<td style="width: 66%"><c:if
											test="${currentRole eq 'student' }">
											<input type="text" name="email" size="40"
												value="${user.studentBaseInfo.email }"
												class="required email" />
										</c:if> <c:if test="${currentRole eq 'edumanage' }">
											<input type="text" name="email" size="40"
												value="${user.email }" class="required email" />
										</c:if></td>
								</tr>
							</table>

							<div class="formBar">
								<ul>
									<li><div class="buttonActive">
											<div class="buttonContent">

												<button type="submit">提交</button>
											</div>
										</div></li>
									<li><div class="button">
											<div class="buttonContent">
												<button type="button" class="close"
													onclick="navTab.closeCurrentTab();">取消</button>
											</div>
										</div></li>
								</ul>
							</div>
						</form>
						<!-- end -->
					</div>
					<div></div>
					<div></div>
					<div></div>
				</div>
				<div class="tabsFooter">
					<div class="tabsFooterContent"></div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
//获取手机短信验证码
//timer变量，控制时间
var interValObj;
var remainTime;
var isChange;

$("#changeuser_mobile").change(function(){//绑定修改事件
	isChange = 'Y';
})

function getMsgAuthCodeCU(){
	var validateCode = $("#changeuser_authCode").val();
	if(!validateCode){
		alert("验证码不能为空！");
		return false;
	}
	var phone = $("#changeuser_mobile").val();
	if(!phone){
		alert("手机号码不能为空！");
		return false;
	}
	if(!isPhoneLegal(phone)){
		alert("不合法的手机号！");
		return false;
	}
	remainTime = 120;// 两分钟后重发 
	
	$("#changeuser_getMsgAuthCodeCU").attr("disabled","disabled");
	interValObj = window.setInterval(setRemainTime, 1000); // 启动计时器，1秒执行一次
	$.ajax({
        type:"POST",
        url:"${baseUrl}/mas/getMsgAuthCode.html",
        data:{phone:phone,authCode:validateCode},
        dataType:'json',       
        success:function(data){  
       		 if(data.statusCode == 200){        
       			 
       		 }else{
       			window.clearInterval(interValObj);// 停止计时器
    			$("#changeuser_getMsgAuthCodeCU").removeAttr("disabled");// 启用按钮
    			$("#changeuser_getMsgAuthCodeCU").text("获取手机验证码");
       			alert(data.message);
       		 }
        }            
	});
}

//timer处理函数
function setRemainTime() {
	if (remainTime == 0) {                
		window.clearInterval(interValObj);// 停止计时器
		$("#changeuser_getMsgAuthCodeCU").removeAttr("disabled");// 启用按钮
		$("#changeuser_getMsgAuthCodeCU").text("获取手机验证码");
	}else {
		remainTime--;
		$("#changeuser_getMsgAuthCodeCU").text(remainTime+"秒后重发");
	}
}

//修改联系方式 
function editContact(editFlag){
	
	var editResult        =  $("#changeuser_"+editFlag).val();
	var currentRole = "${currentRole}";
	var certNum = $("#certNum").val();
	var userid = $("#resourceid").val();; 
	// 判断是否输入短信验证码
	var phoneComfirm = "${phoneComfirm}";
	var msgAuthCode = $("#changeuser_msgAuthCode").val();
	if(phoneComfirm=='1'){
		if(!msgAuthCode){
			alert("短信验证码不能为空！");
			return false;
		}
	}
	
	if(editFlag =='mobile' && !isPhoneLegal(editResult)){
		alert("不合法的手机号！");
		return false;
	}
	if(editFlag =='contactPhone' && !(/(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^[0-9]{3,4}[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)/.test(editResult))){
		alert("不合法的电话号码!");
		return false;
	}
	$.ajax({
          type:"POST",
          url:"${baseUrl}/system/org/changeuser-form/editcontact.html",
          data:{editFlag:editFlag,editResult:editResult,isChange:isChange,userid:userid,certNum:certNum,msgAuthCode:msgAuthCode},
          dataType:'json',       
          success:function(date){          	   		
         		 if(date.success == 'Y'){         		 	
         			//$("#userForm").submit();
         			isChange = 'V';//验证通过
         		 }
         		 alert(date.msg);
          }            
	});
}

function validate(){
	var $form = $("#userForm");
	if(!$form.valid()){
		return false;
	}
	if(isChange == 'Y'){
		editContact('mobile')
		if(isChange != 'V'){
			return false;
		}
	}
	$.ajax({
		type:'POST',
		url:$form.attr("action"),
		data:$form.serializeArray(),
		dataType:"json",
		cache: false,
		success: dialogAjaxDone,
		error: DWZ.ajaxError
	});
	return false;
}

</script>
</body>
</html>