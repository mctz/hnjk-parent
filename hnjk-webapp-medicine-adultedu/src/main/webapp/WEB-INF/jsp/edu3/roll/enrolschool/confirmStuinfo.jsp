<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>确认学生基本信息</title>
<script type="text/javascript">

	//初次登录确认信息
	function comfirmStuBaseInfo() {

		var form = $("#confirm_stuBaseInfo_form");
		var name = $("#confirm_stuBaseInfo_name").val();
		var certType = $("#confirm_stuBaseInfo_certType").val();
		var gender = $("#confirm_stuBaseInfo_gender").val();
		var certNum = $("#confirm_stuBaseInfo_certNum").val();
		var mobile = $("#confirm_stuBaseInfo_mobile").val();
		// 判断是否输入短信验证码
		var mobileCode = $("#confirm_stuBaseInfo_msgAuthCode").val();
		if(!isPhoneLegal(mobile)){
			alertMsg.warn("不合法的手机号！");
			return false;
		}
		
		if((!name||""==name) ||(!certType||certType=="")||(!certNum||""==certNum)||(!gender||""==gender)||(!mobile||""==mobile)){
			alertMsg.warn("姓名、证件类型、性别、身份证号码 手机号码  不允许为空");
			return false;
		} 
		alertMsg.confirm("信息确认后将无法修改，你确认提交吗", {
			okCall:function(){
				$.ajax({
					type : 'POST',
					url : "${baseUrl }/edu3/confirmMsg.html",
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
	
</script>
</head>
<body>
	<div class="page" style="margin-top: 10px;">
		<div class="pageContent">
			<form id="confirm_stuBaseInfo_form" method="post" class="pageForm">
				<input id="stuid" value="${studentInfo.resourceid }" type="hidden"
					name="stuid" /> <input id="confirm_stuBaseInfo_phoneComfirm"
					value="${phoneComfirm }" type="hidden" name="phoneComfirm" />
				<div class="pageFormContent">
					<table class="form">
						<tr>
							<td width="25%">姓名:</td>
							<td width="75%"><input type="text"
								id="confirm_stuBaseInfo_name" name="name" size="34"
								value="${studentInfo.studentBaseInfo.name}" class="required"
								maxlength="18" style="width: 130px; height: 20px;" /></td>
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
							<td width="75%"><gh:select id="confirm_stuBaseInfo_gender"
									name="gender" dictionaryCode="CodeSex"
									value="${studentInfo.studentBaseInfo.gender}"
									classCss="required" choose="false"
									style="width:130px;height: 20px;" /></td>
						</tr>
						<tr>
							<td width="25%">证件类型:</td>
							<td width="75%"><gh:select id="confirm_stuBaseInfo_certType"
									name="certType" dictionaryCode="CodeCertType"
									value="${studentInfo.studentBaseInfo.certType}" choose="false"
									classCss="required" style="width:130px;height: 20px;" /></td>
						</tr>
						<tr>
							<td width="25%">证件号码:</td>
							<td width="75%"><input type="text"
								id="confirm_stuBaseInfo_certNum" name="certNum" size="34"
								value="${studentInfo.studentBaseInfo.certNum}" class="required"
								maxlength="28" style="width: 130px; height: 20px;" /></td>
						</tr>
						<tr>
							<td width="25%">手机号码:</td>
							<td width="75%"><input type="text"
								id="confirm_stuBaseInfo_mobile" name="mobile" size="34"
								value="${studentInfo.studentBaseInfo.mobile}" class="required"
								maxlength="18" style="width: 130px; height: 20px;" /></td>
						</tr>
					</table>
				</div>
				<div>
					<table class="form">
						<tr>
							<td style="color: red">重要提示：以上信息为毕业证信息（毕业证注册后，不可修改），如有误请速提交“信息修改申请”，如本次不提交，我院将视同本人核对无误的正确信息。</td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="button" onclick="return comfirmStuBaseInfo();">
										确认</button>
								</div>
							</div></li>
						<li><div class="button" style="margin-left: 10px;">
								<div class="buttonContent">
									<button type="button" onclick="$.pdialog.closeCurrent();;">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>