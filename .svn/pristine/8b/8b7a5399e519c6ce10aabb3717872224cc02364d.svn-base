<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
span {
font-size: 18px;
}
</style>
</head>
<body>
	<div class="col-md-3"></div>
	<div class="col-md-6">
		<h2 class="text-left">
			<span class="label label-danger"> <strong><i>第二步</i></strong>
			</span>
		</h2>
		<br> 
		<span>  这是您之前在系统中绑定的手机号码：${tmpMobile }，<br>如您确认无误，请输入验证码并点击【获取手机验证码】
		</span> 
		<br>
		<div class="row">
			<div class="col-sm-3">
				<input type="text" id="findPwd_authCode" name="authCode"
					class="form-control" placeholder='请输入验证码'>
			</div>
			<div class="col-sm-3">
				<img src="${baseUrl}/imageCaptcha" id="findPwd_authCodeImg"
					style="margin-bottom: -6px; padding-left: 3px; cursor: pointer;"
					onclick="this.src='${baseUrl}/imageCaptcha?now=' + new Date().getTime()"
					title="换一张">
			</div>
		</div>
		<br>
		<div class="row">
			<div class="col-sm-3">
				<button id="findPwd_getMsgAuthCodeCU"
					onclick="return getMsgAuthCodeCU();" class="btn btn-success">获取手机验证码</button>
			</div>
			<div class="col-sm-9">

				<form id='userInfoForm' role='form' class="form-horizontal"
					method='post'
					action='${baseUrl}/edu3/portal/user/validateUserSubmit.html'>
					<div class="form-group has-success">
						<!-- 				<label class="col-sm-3 control-label" for="userNameId">手机验证码</label> -->
						<div class='col-sm-12'>
							<input name='msgAuthCode' type='text' class='form-control'
								id='msgAuthCodeId' placeholder='请输入手机验证码'>
						</div>
					</div>
					<button id="validateMsgAuthCodeButton" type="button"
						class="btn btn-success pull-right" onclick="validateMsgAuthCode()">提交</button>
				</form>
			</div>
		</div>
	</div>
	<div class="col-md-3"></div>
	<script type="text/javascript">
	$(function(){
		$("#validateMsgAuthCodeButton").removeAttr("disabled");
	})
	function getMsgAuthCodeCU(){
		var validateCode = $("#findPwd_authCode").val();
		if(!validateCode){
			toastr.warning("验证码不能为空！");
			return false;
		}
		var phone = "${mobile}";
		remainTime = 120;// 两分钟后重发 
		
		$("#findPwd_getMsgAuthCodeCU").attr("disabled",true);
		interValObj = window.setInterval(setRemainTime, 1000); // 启动计时器，1秒执行一次
		
		$.ajax({
	        type:"POST",
	        url:"${baseUrl}/mas/getMsgAuthCode.html",
	        data:{phone:phone,authCode:validateCode},
	        dataType:'json',
	        ajaxComplete:function(){
	        	toastr.success("手机验证码已发送成功，请注意查看手机消息，获取手机验证码后，请填写进下方的手机验证码");
	        },
	        success:function(data){  
	       		 if(data.statusCode == 200){        
	       			 
	       		 }else{
	       			window.clearInterval(interValObj);// 停止计时器
	    			$("#findPwd_getMsgAuthCodeCU").removeAttr("disabled");// 启用按钮
	    			$("#findPwd_getMsgAuthCodeCU").text("获取手机验证码");
	    			toastr.warning(data.message+" 请稍后再试");
	       		 }
	        }            
		});
	}

	//timer处理函数
	function setRemainTime() {
		if (remainTime == 0) {                
			window.clearInterval(interValObj);// 停止计时器
			$("#findPwd_getMsgAuthCodeCU").removeAttr("disabled");// 启用按钮
			$("#findPwd_getMsgAuthCodeCU").text("获取手机验证码");
		}else {
			remainTime--;
			$("#findPwd_getMsgAuthCodeCU").text(remainTime+"秒后重发");
		}
	}
	function validateMsgAuthCode(){
		var msgAuthCode= $("#msgAuthCodeId").val();
		var userId ="${userId}";
		var mobile="${mobile}";		
		if(!msgAuthCode){
			toastr.warning("手机验证码不能为空！");
			return false;
		}
		$.ajax({
	        type:"POST",
	        url:"${baseUrl}/edu3/portal/user/validateMsgAuthCode.html",
	        data:{userId:userId,mobile:mobile,msgAuthCode:msgAuthCode},
	        dataType:'json',
	        success:function(data){  
	       		 if(data.statusCode == 200 && data.isCorret =='Y'){
	       			toastr.success("第二步验证成功");
	       			$("#validateMsgAuthCodeButton").attr("disabled",true);
	       			$("#thirdStep").load("${baseUrl}/edu3/portal/user/reSetPassWord.html?userId="+data.userId);
	       		 }else{	       			
	    			toastr.warning(data.message);
	       		 }
	        }            
		});
	}
	</script>
</body>
</html>