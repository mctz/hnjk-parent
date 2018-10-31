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
		<h2 class="text-left" >
				<span class="label label-danger">
					<strong><i>第三步</i></strong>
					</span>
				</h2>
		 <br>
		
		<div class="row">
			
				<form id='reSetPassWordForm' role='form' class="form-horizontal" >
					<div class="form-group has-success">
						<label class="col-sm-3 control-label" for="password">输入密码</label>
						<div class='col-sm-9'>
							<input name='password' type='password' class='form-control'
								id='passwordId' placeholder='请输入密码'>
						</div>
					</div>
					<div class="form-group has-success">
						<label class="col-sm-3 control-label" for="password1">再次输入</label>
						<div class='col-sm-9'>
							<input name='password1' type='password' class='form-control'
								id='password1Id' placeholder='请输入再次输入密码'>
						</div>
					</div>
					<button id="validateReSetPassWordButton" type="button"
						class="btn btn-success pull-right" onclick="validateReSetPassWord()">提交</button>
				</form>
			</div>
		</div>
	</div>
	<div class="col-md-3"></div>
	<script type="text/javascript">
	
		$(function() {
			$("#validateReSetPassWordButton").removeAttr("disabled");
		})

		function validateReSetPassWord() {
			var userId = "${userId}";
			var password = $("#passwordId").val();
			var password1 = $("#password1Id").val();

			if (!password || !password1) {
				toastr.warning("新密码不能为空！");
				return false;
			}
			if (password != password1) {
				toastr.warning("两次输入密码不一致，请重新输入！");
				$("#passwordId").val("");
				$("#password1Id").val("");
				return false;
			}
			$
					.ajax({
						type : "POST",
						url : "${baseUrl}/edu3/portal/user/validateReSetPassWord.html",
						data : {
							userId : userId,
							password : password
						},
						dataType : 'json',

						success : function(data) {
							if (data.statusCode == 200) {
								toastr.success(data.message);
								$("#validateReSetPassWordButton").attr("disabled",true);
								var closePageButton = '<br><center><button id="closePageButton" type="button" class="btn btn-warning" onclick="custom_close()">关闭页面</button></center><br><br><br>';
								$("#closePage").append(closePageButton);
								// 	       			$("#thirdStep").load("${baseUrl}/edu3/portal/user/findPwdSecStep.html?mobile="+data.mobile);
							} else {
								toastr.warning(data.message);
								// 	    			$("#findPwd_getMsgAuthCodeCU").removeAttr("disabled");// 启用按钮
								// 	    			$("#findPwd_getMsgAuthCodeCU").text("获取手机验证码");
								// 	    			toastr.warning(data.message+",请稍后再试");
							}
						}
					});
		}

		function custom_close() {
			window.opener = null;
			window.open('', '_self');
			window.close()
		}
	</script>
</body>
</html>