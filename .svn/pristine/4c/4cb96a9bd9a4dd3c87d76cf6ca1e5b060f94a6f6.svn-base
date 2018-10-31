<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>推迟毕业申请</title>
</head>
<body>
	<h2 class="contentTitle">推迟毕业申请</h2>
	<div class="page">
		<div class="pageContent">
			<form id="graduateNoApplyForm" method="post"
				action="${baseUrl}/edu3/roll/graduateNo/save.html" class="pageForm"
				onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${graduate.resourceid }" />
				<div id="graduateNoApply" class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">学生学号:<font color="red">注:本申请仅适用于本科生</font></td>
							<td width="30%"><input type="text" id="stuNum" name="stuNum"
								style="width: 50%" value="${graduate.studentInfo.studyNo }"
								class="required" /> <span class="buttonActive"
								style="margin-left: 8px"><div class="buttonContent">
										<button type="button" onclick="queryStudentInfo8()">查询</button>
									</div></span></td>
							<td width="20%">姓名:</td>
							<td width="30%"><input type="text" id="stuName"
								name="stuName" style="width: 50%"
								value="${graduate.studentInfo.studentName }" readonly="readonly"
								class="required" /></td>
						</tr>
						<tr>
							<td>教学站:</td>
							<td><input type="text" id="stuCenter2" name="stuCenter"
								style="width: 50%" value="${graduate.studentInfo.branchSchool }"
								readonly="readonly" /></td>
							<td>年级:</td>
							<td><input type="text" id="grade2" name="grade"
								style="width: 50%" value="${graduate.studentInfo.grade }"
								readonly="readonly" /></td>
						</tr>
						<tr>
							<td>专业:</td>
							<td><input type="text" id="major12" name="major"
								style="width: 50%" value="${graduate.studentInfo.major }"
								readonly="readonly" /></td>
							<td>层次:</td>
							<td><input type="text" id="classic12" name="classic"
								style="width: 50%" value="${graduate.studentInfo.classic }"
								readonly="readonly" /></td>
						</tr>
						<tr>
							<td>申请时间:</td>
							<td><input type="text" name="applayDate" style="width: 50%"
								value="<fmt:formatDate value="${graduate.applayDate}" pattern="yyyy-MM-dd"/>"
								readonly="readonly" /></td>
							<td>申请关联批次:</td>
							<td><select name="applyNoGraduationPlan"
								id="applyNoGraduationPlan">
									<c:forEach items="${settings}" var="plan">
										<option value="${plan.resourceid}">${plan.yearInfo.yearName}${ghfn:dictCode2Val('CodeTerm',plan.term) }</option>
									</c:forEach>
							</select></td>
						</tr>
						<tr>
							<td>申请理由:</td>
							<td colspan="3"><textarea rows="5" cols=""
									style="width: 80%" id="applayCause" name="applayCause">${graduate.applayCause }</textarea></td>
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
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
<script type="text/javascript">
	
	function queryStudentInfo8(){
		var stuNum = jQuery("#graduateNoApply #stuNum").val();
		if(stuNum!=""){
			jQuery.ajax({
				data:"stuNum="+stuNum,
				dataType:"json",
				url:"${baseUrl}/edu3/roll/graduateNo/queryStudentInfo.html",
				success:function(data){
					if(data.msg=='success'){
					jQuery("#graduateNoApply #stuName").val(data.studentName);
					jQuery("#graduateNoApply #stuCenter2").val(data.schoolCenter);
					jQuery("#graduateNoApply #grade2").val(data.grade);
					jQuery("#graduateNoApply #major12").val(data.major);
					jQuery("#graduateNoApply #classic12").val(data.classic);
					}else if(data.msg=='classicerr'){
						alertMsg.warn("您选择的学生所在层次是<b>"+data.classic+"</b>,目前延迟毕业申请只适用于本科的学生。");
					}
				}
			})
		}
	}

</script>
</html>