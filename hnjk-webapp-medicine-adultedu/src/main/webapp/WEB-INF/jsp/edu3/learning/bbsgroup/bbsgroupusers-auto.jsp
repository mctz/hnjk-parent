<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>自动分配学习小组</title>
<script type="text/javascript">
		function getStudentNumber(){
			var courseId = $("#autoassignbbsgroup_courseId").val();
			if(courseId==""){
				alertMsg.warn("请选择一门课程！");
				return false;
			}
			var url = "${baseUrl}/edu3/framework/bbs/bbsgroupusers/getstudentcount.html";
			$.post(url,{courseId:courseId},function(json){
	    		$("#assignMembersNum").val(json);
	    	},"json");
		}
	</script>
</head>
<body>
	<h2 class="contentTitle">自动分配学习小组</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/framework/bbs/bbsgroupusers/assign.html"
				class="pageForm"
				onsubmit="return validateCallback(this,dialogAjaxDone);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">课程:</td>
							<td colspan="3"><gh:courseAutocomplete name="courseId"
									tabindex="2" id="autoassignbbsgroup_courseId"
									classCss="required" isFilterTeacher="Y" style="width:50%;" />
								&nbsp;<a class="button" href="javascript:;"
								onclick="getStudentNumber();"><span>查询可分配人数</span></a></td>
						</tr>
						<tr>
							<td>可分配人数:</td>
							<td colspan="3"><input type="text" id="assignMembersNum"
								readonly="readonly" size="10" /></td>
						</tr>
						<tr>
							<td width="20%">小组数目:</td>
							<td colspan="3"><input type="text" name="bbsgroupnum"
								class="required digits" size="10" /></td>
						</tr>
					</table>
				</div>
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
										onclick="$.pdialog.closeCurrent();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>