<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生学籍异动审核状态设置</title>
<script type="text/javascript">	
function doBatchAuditStatus(){
	var postUrl     = "${baseUrl}/edu3/framework/graduation/student/stuaudit.html"; 
	var auditStatus = $("#auditStatus").val();
	if(''==auditStatus){
		alertMsg.warn("您尚未设置状态,无法完成审核。");
		return false; 
	}
	$.ajax({
		type:"post",
		data: "resourceid=${studentId}&auditType="+auditStatus,
		url:postUrl,
		dataType:"json",
		success:function(data){
			if(data.statusCode==200){
				alertMsg.correct(data.message);
			}else{
				alertMsg.error(data.message);
			}
			var form = $("#studentChangeInfoList #pagerForm");
			navTab.reload(form.action, $(form).serializeArray());
			$.pdialog.closeCurrent();
		}
	});
}
</script>
</head>
<body>

	<div class="page">
		<div class="pageContent">
			<!-- Hidden District -->
			<input type="hidden" name="studentId" id="studentId"
				value="${studentId}" />
			<div class="searchBar">
				<ul class="searchContent">
					<li><label>审核状态:</label> <gh:select id="auditStatus"
							name="auditStatus" filtrationStr="Y,N"
							dictionaryCode="CodeAuditStatus" /></li>
				</ul>

				<div class="buttonActive" style="float: right;">
					<div class="buttonContent">
						<button type="button" onclick="doBatchAuditStatus();">确定
						</button>
					</div>
				</div>
			</div>

		</div>
</body>
</html>