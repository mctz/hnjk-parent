<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设置学籍图片的审核状态</title>
<script type="text/javascript">
	function doBatchSetRecruitStatus(){
		var userid              = $("#user").val(); //用户id
		var aduitimgstatus		= $("#aduitimgstatus").val(); //审核状态
		
		var url			        = "${baseUrl}/edu3/register/studentinfo/aduitImgStatus.html?userid="+userid+"&aduitimgstatus="+aduitimgstatus;
		
		if(''==aduitimgstatus){
			alertMsg.warn("您尚未设置状态,无法保存。");
			return false; 
		}
		
		$.ajax({
			type:"post",
			url:url,
			dataType:"json",
			success:function(data){
				if(data.errMsg){
					alertMsg.info("设置失败!");	
				}else{
					alertMsg.info("设置成功!");
				}	
			}
		});
		$.pdialog.closeCurrent();
		}
</script>
</head>
<body>
	<div>
		<div class="pageContent">
			<div class="searchBar">
				姓名：${aduitImgUserName} <br />
				<br /> 状态:<select id="aduitimgstatus"
					style="width: 50%; height: 40p; margin-left: 10px;">
					<option value="">请选择</option>
					<c:if test="${aduit eq '0' or aduit eq ''}">
						<option value="1">通过</option>
						<option value="2">不通过</option>
					</c:if>
					<c:if test="${aduit eq '1'}">
						<option value="0">撤销</option>
						<option value="2">不通过</option>
					</c:if>

				</select> <input type="hidden" value="${stus}" id="user" />
			</div>
			<br />
			<div class="buttonActive" style="float: none; margin-left: 10px;">
				<div class="buttonContent">
					<button type="button" onclick="doBatchSetRecruitStatus();">确定
					</button>
				</div>
			</div>
		</div>

	</div>
</body>
</html>