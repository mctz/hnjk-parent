<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>确认基本信息</title>
<script type="text/javascript">
	jQuery(document).ready(function(){		
		var schoolCode = "${schoolCode}";
		if(schoolCode=='10601'){//桂林医，修改信息需要申请并审核
			$("#comfirmBtn").html("确认无误");
			$("#applyli").show();
			document.getElementById('studentBaseInfo_confirm_name').readOnly=true;
			document.getElementById('studentBaseInfo_confirm_certNum').readOnly=true;
			document.getElementById('studentBaseInfo_confirm_certType').disabled=true;
			document.getElementById('studentBaseInfo_confirm_gender').disabled=true;
		}
	})
	
	function editStuInfo(){
		$("#comfirmBtn").html("确认");
		$("#applyli").hide();
		$('input[name=name]').removeAttr("readOnly");
		$('input[name=certNum]').removeAttr("readOnly");
		$('#studentBaseInfo_confirm_certType').removeAttr("disabled");
		$('#studentBaseInfo_confirm_gender').removeAttr("disabled");
	}
	
	//初次登录确认信息
	function comfirmStuInfo() {

		var form = $("#studentBaseInfo_confirm_form");
		var name = $("#studentBaseInfo_confirm_name").val();
		var certType = $("#studentBaseInfo_confirm_certType").val();
		var gender = $("#studentBaseInfo_confirm_gender").val();
		var certNum = $("#studentBaseInfo_confirm_certNum").val();
		var mobile = $("#studentBaseInfo_confirm_mobile").val();
		// 判断是否输入短信验证码
		var phoneComfirm = "${phoneComfirm}";
		var mobileCode = $("#studentBaseInfo_confirm_msgAuthCode").val();
		if(phoneComfirm=='1'){
			if(!mobileCode){
				alertMsg.warn("手机验证码不能为空！");
				return false;
			}
			
			if(!isPhoneLegal(mobile)){
				alertMsg.warn("不合法的手机号！");
				return false;
			}
		}
		
		if((!name||""==name) ||(!certType||certType=="")||(!certNum||""==certNum)||(!gender||""==gender)||(!mobile||""==mobile)){
			alertMsg.warn("姓名、证件类型、性别、身份证号码 手机号码  不允许为空");
			return false;
		} 
		var url = "${baseUrl }/edu3/confirmMsg.html";
		if("${schoolCode}"=='10601'){
			url += "?isApply=Y";
		}
		alertMsg.confirm("信息确认后将无法修改，你确认提交吗", {
			okCall:function(){
				$.ajax({
					type : 'POST',
					url : url,
					data : form.serializeArray(),
					dataType : "json",
					cache : false,
					success : function(json) {
						remainTimeStuInfo = 0;
						if(json.statusCode == 200){
			 				alertMsg.correct(json.message);
			 				$.pdialog.closeCurrent();
			 			} else {
			 				alertMsg.error(json.message);
			 			}
					}
				});
			}
		})
		
	}
	
	// 获取手机短信验证码
	//timer变量，控制时间
	var interValObjStuInfo;
	var remainTimeStuInfo;
	function getStuInfoAuthCode(){
		var validateCode = $("#studentBaseInfo_confirm_authCode").val();
		if(!validateCode){
			alertMsg.warn("验证码不能为空！");
			return false;
		}
		var phone = $("#studentBaseInfo_confirm_mobile").val();
		if(!phone){
			alertMsg.warn("手机号码不能为空！");
			return false;
		}
		if( !isPhoneLegal(phone)){
			alertMsg.warn("不合法的手机号！");
			return false;
		}
		remainTimeStuInfo=120;// 两分钟后重发
		$("#studentBaseInfo_confirm_getMsgAuthCode").attr("disabled","disabled");
		interValObjStuInfo = window.setInterval(setRemainTimeStuInfo, 1000); // 启动计时器，1秒执行一次
		$.ajax({
	        type:"POST",
	        url:"${baseUrl}/mas/getMsgAuthCode.html",
	        data:{phone:phone,authCode:validateCode},
	        dataType:'json',       
	        global:false,
	        success:function(data){  
	       		 if(data.statusCode == 200){  
// 	       			console.log(phone+"短信验证码发送成功");
	       			alertMsg.correct(phone+"短信验证码发送成功");
	       			// 刷新验证码
		       		$("#studentBaseInfo_confirm_authCodeImg").attr("src","${baseUrl}/imageCaptcha?now="+ new Date().getTime());
	       		 }else{
	       			window.clearInterval(interValObjStuInfo);// 停止计时器 
	    			$("#studentBaseInfo_confirm_getMsgAuthCode").removeAttr("disabled");// 启用按钮
	    			$("#studentBaseInfo_confirm_getMsgAuthCode").val("发送");	    			
	    			alertMsg.warn(phone+"短信验证码发送失败"+"<br> 失败原因："+data.message+" 请联系系统管理员");
// 	    			alertMsg.warn(data.message);
	       		 }
	        }            
		});
		
	}

	//timer处理函数
	function setRemainTimeStuInfo() {
		if (remainTimeStuInfo == 0) {                
			window.clearInterval(interValObjStuInfo);// 停止计时器
			$("#studentBaseInfo_confirm_getMsgAuthCode").removeAttr("disabled");// 启用按钮
			$("#studentBaseInfo_confirm_getMsgAuthCode").val("发送");
		}else {
			remainTimeStuInfo--;
			$("#studentBaseInfo_confirm_getMsgAuthCode").val(remainTimeStuInfo+"秒后重发");
		}
	}
	
	function closeComfirmTab(){
		alertMsg.confirm("未确认信息前无法使用系统，取消则退出系统，你确认退出吗",{
			okCall:function(){
// 				$.pdialog.closeCurrent();				
				window.location.href="${basePath}/j_spring_security_logout";
			}
		})
		
	}
	
</script>
</head>
<body>
	<div class="page" style="margin-top: 10px;">
		<div class="pageContent">
			<form id="studentBaseInfo_confirm_form" method="post"
				class="pageForm">
				<input id="stuid" value="${studentInfo.resourceid }" type="hidden"
					name="stuid" />
				<div class="pageFormContent">
					<table class="form">
						<tr>
							<td width="25%">姓名:</td>
							<td width="75%"><c:choose>
									<c:when test="${schoolCode eq '11078' }">
										<input type="text" id="studentBaseInfo_confirm_name"
											name="name" value="${studentInfo.studentBaseInfo.name}"
											disabled="true" />
									</c:when>
									<c:otherwise>
										<input type="text" id="studentBaseInfo_confirm_name"
											name="name" size="34"
											value="${studentInfo.studentBaseInfo.name}" class="required"
											maxlength="18" style="width: 130px; height: 20px;" />
									</c:otherwise>
								</c:choose></td>
						</tr>
						<tr>
							<td width="25%">层次:</td>
							<td width="75%">${studentInfo.classic.classicName}</td>
						</tr>
						<tr>
							<td width="25%">专业:</td>
							<td width="75%">${studentInfo.major.majorName}</td>
						</tr>
						<tr>
							<td width="25%">性别:</td>
							<td width="75%"><c:choose>
									<c:when test="${schoolCode eq '11078' }">
										<input type="text" id="studentBaseInfo_confirm_gender"
											name="gender"
											value="${ghfn:dictCode2Val('CodeSex',studentInfo.studentBaseInfo.gender)}"
											disabled="true" />
									</c:when>
									<c:otherwise>
										<gh:select id="studentBaseInfo_confirm_gender" name="gender"
											dictionaryCode="CodeSex"
											value="${studentInfo.studentBaseInfo.gender}"
											classCss="required" choose="false"
											style="width:130px;height: 20px;" />
									</c:otherwise>
								</c:choose></td>
						</tr>
						<tr>
							<td width="25%">证件类型:</td>
							<td width="75%"><c:choose>
									<c:when test="${schoolCode eq '11078' }">
										<input type="text" id="studentBaseInfo_confirm_certType"
											name="certType"
											value="${ghfn:dictCode2Val('CodeCertType',studentInfo.studentBaseInfo.certType)}"
											disabled="true" />
									</c:when>
									<c:otherwise>
										<gh:select id="studentBaseInfo_confirm_certType"
											name="certType" dictionaryCode="CodeCertType"
											value="${studentInfo.studentBaseInfo.certType}"
											choose="false" classCss="required"
											style="width:130px;height: 20px;" />
									</c:otherwise>
								</c:choose></td>
						</tr>
						<tr>
							<td width="25%">证件号码:</td>
							<td width="75%"><c:choose>
									<c:when test="${schoolCode eq '11078' }">
										<input type="text" id="studentBaseInfo_confirm_certNum"
											name="certNum" value="${studentInfo.studentBaseInfo.certNum}"
											disabled="true" />
									</c:when>
									<c:otherwise>
										<input type="text" id="studentBaseInfo_confirm_certNum"
											name="certNum" size="34"
											value="${studentInfo.studentBaseInfo.certNum}"
											class="required" maxlength="28"
											style="width: 130px; height: 20px;" />
									</c:otherwise>
								</c:choose></td>
						</tr>
						<tr>
							<td width="25%">手机号码:</td>
							<td width="75%"><input type="text"
								id="studentBaseInfo_confirm_mobile" name="mobile" size="34"
								value="${studentInfo.studentBaseInfo.mobile}" class="required"
								maxlength="18" style="width: 130px; height: 20px;" /></td>
						</tr>
						<c:if test="${phoneComfirm eq '1'}">

							<tr>
								<td width="25%">验证码:</td>
								<td width="75%"><input type="text"
									id="studentBaseInfo_confirm_authCode" name="authCode" size="4"
									style="width: 130px; height: 20px;" /> <img
									src="${baseUrl}/imageCaptcha"
									id="studentBaseInfo_confirm_authCodeImg"
									style="margin-left: 3px; cursor: pointer;"
									onclick="this.src='${baseUrl}/imageCaptcha?now=' + new Date().getTime()"
									title="换一张"></td>
							</tr>
							<tr>
								<td width="25%">手机验证码:</td>
								<td width="75%"><input type="text"
									id="studentBaseInfo_confirm_msgAuthCode" name="mobileCode"
									size="5" class="required" style="width: 130px; height: 20px;" />
									<input type="button"
									id="studentBaseInfo_confirm_getMsgAuthCode"
									onclick="return getStuInfoAuthCode();"
									style="margin-left: 3px; cursor: pointer;" value="发送">
								</td>
							</tr>
						</c:if>
					</table>
				</div>
				<div>
					<table class="form">

						<tr>
							<td style="color: red"><c:choose>
								<c:when test="${schoolCode eq '11078' }">
									重要提示：以上信息为学籍注册信息（除手机号码外），请认真核对，如有错误，请线下提交相关证明到教学点，由教学点确认后，提交修改资料及证明材料至成教院，成教院管理人员核实后再进行修改。	
								</c:when>
								<c:when test="${schoolCode eq '10601' }">
									重要提示：凡修改以上信息（手机号码除外），请于10个工作日内到教学点提交纸质“信息更正申请”材料。
								</c:when>
								<c:otherwise>
									重要提示：以上信息为毕业证信息（毕业证注册后，不可修改），如有误请速提交“信息修改申请”，如本次不提交，我院将视同本人核对无误的正确信息。
								</c:otherwise>
								</c:choose></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="button" id="comfirmBtn" onclick="return comfirmStuInfo();">
										确认</button>
								</div>
							</div></li>
						<li id="applyli" style="display: none;"><div class="buttonActive">
								<div class="buttonContent">
									<button type="button" id="applyBtn" onclick="return editStuInfo();">
										信息修改申请</button>
								</div>
							</div></li>
						<li><div class="button" style="margin-left: 10px;">
								<div class="buttonContent">
									<button type="button" id="closeBtn" onclick="closeComfirmTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>