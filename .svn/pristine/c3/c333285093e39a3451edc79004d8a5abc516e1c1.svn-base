<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<title>在线帮助--忘记密码</title>
	<link rel="stylesheet" href="${baseUrl }/themes/bootstrap/bootstrap.min.css" rel="stylesheet"/>
	<link rel="stylesheet" href="${baseUrl }/themes/bootstrap/toastr.min.css" rel="stylesheet"/>
	<script src="${baseUrl }/jscript/jquery-2.1.1.js" type="text/javascript"></script>
	<script src="${baseUrl }/jscript/bootstrap/bootstrap.min.js" type="text/javascript"></script>
	<script src="${baseUrl }/jscript/bootstrap/toastr.min.js" type="text/javascript"></script>

<style type="text/css">
.labelbox {
	font-size: 18px;
	border: solid 1px #b3b3b3;
	outline: none;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border-radius: 8px;
	padding-top: 20px;
	margin-top: 30px;
}

</style>

</head>
<body>
	<div class="container" style="margin-top: 85px">
		<h1 align="center"> <span class="label label-success">在线帮助--找回密码</span></h1>
		<div class="row">
			<div class="col-md-3"></div>
			<div class="col-md-6 labelbox" id="labelbox">
				<p class="text-left">提示：
				</p>
				<p class="text-left">1、如果您为<strong style="color:red">非学生</strong>用户，请联系管理员重置密码。
				</p>
				<p class="text-left">2、学生用户，也可自行联系教学点教务员，请求帮忙重置用户密码。
				</p>
				<p class="text-left">3、如果您为<strong style="color:red">学生</strong>并登录过系统，确认过首次登录信息（即<strong style="color:red">已绑定过手机</strong>），找回密码共<strong style="color:red">三步</strong>，请进行下面第一步。
				
				</p>
			</div>
			<div class="col-md-3"></div>
		</div>
		
		<hr width=80% size=20 color="red" style="FILTER: alpha(opacity=100,finishopacity=0,style=3)">

		<div class="row">
			<div class="col-md-3"></div>
			<div class="col-md-6" id="labelbox">
				<h2 class="text-left" >
				<span class="label label-danger">
					<strong><i>第一步</i></strong>
					</span>
				</h2>
				<br>
				<form id='userInfoForm' role='form' class="form-horizontal"
					method='post'
					action='${baseUrl}/edu3/portal/user/validateUserSubmit.html'>
					<div class="form-group has-success">
						<label class="col-sm-3 control-label" for="userNameId">登录账号</label>
						<div class='col-sm-9'>
							<input name='userName' type='text' class='form-control'
								id='userNameId' placeholder='请输入登录账号'>
						</div>
					</div>
					<div class="form-group has-success">
						<label class="col-sm-3 control-label" for="userCnNameId">姓名</label>
						<div class='col-sm-9'>
							<input name='userCnName' type='text' class='form-control'
								id='userCnNameId' placeholder='请输入姓名'>
						</div>
					</div>
					<button id="validateSubmitButton" type="button" class="btn btn-success pull-right"
						onclick="validateSubmit()">提交</button>
				</form>
			</div>
			<div class="col-md-3"></div>
		</div>
		<div id="secStep" class="row"></div>
		<div id="thirdStep" class="row"></div>
		<div id="closePage" class="row"></div>
	</div>
	<script type="text/javascript">
	$(function(){
		$("#validateSubmitButton").removeAttr("disabled");
	})
	toastr.options = {
		closeButton : true,
		debug : false,
		progressBar : true,
		positionClass : "toast-top-center",
		onclick : null,
		showDuration : "300",
		hideDuration : "1000",
		timeOut : "6000",
		extendedTimeOut : "1000",
		showEasing : "swing",
		hideEasing : "linear",
		showMethod : "fadeIn",
		hideMethod : "fadeOut"
	};
	//检查合法性
	function validateSubmit(){
		if($.trim($("#userNameId").val())==""){
			toastr.warning("登录账号不能为空");
			return false;
		}
		if($.trim($("#userNameId").val())==""){
			toastr.warning("用户不能为空");
			return false;
		}
		var $form = $("#userInfoForm");
		$.ajax({
			type:'POST',
			url:$form.attr("action"),
			data:$form.serializeArray(),
			dataType:"json",
			cache: false,
			success:function(data){
				if(data.statusCode==200){
					toastr.success("第一步验证成功");
					var userId= data.userId;
					$("#secStep").load("${baseUrl}/edu3/portal/user/findPwdSecStep.html?mobile="+data.mobile+"&userId="+userId);
					$("#validateSubmitButton").attr("disabled",true);
				}else if(data.statusCode==300){
					toastr.warning(data.message);
					return false;
				}
				
			}
		})
	}
</script>
</body>
</html>