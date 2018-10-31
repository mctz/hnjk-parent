<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业生数据</title>
</head>
<body>
	<h2 class="contentTitle">毕业生数据</h2>
	<div class="page">
		<div class="pageContent">
			<form id="graduate4" method="post"
				action="${baseUrl}/edu3/roll/graduation/student/addOrEdit.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${graduateData.resourceid }" /> <input type="hidden"
					name="studentId" value="${graduateData.studentInfo.resourceid }" />
				<input type="hidden" name="version" value="${graduateData.version }" />
				<!-- <input type="hidden" name="studentInfo" value="${graduateData.studentInfo }"/>      -->
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">学生学号:</td>
							<td width="30%"><input type="text" id="stuNum3"
								name="stuNum" title="${graduateData.studentInfo.studyNo }"
								style="width: 50%" value="${graduateData.studentInfo.studyNo }"
								class="required" /></td>
							<td width="20%">姓名:</td>
							<td width="30%"><input type="text" id="stuName3"
								name="stuName" title="${graduateData.studentInfo.studentName }"
								style="width: 50%"
								value="${graduateData.studentInfo.studentName }"
								readonly="readonly" class="required" /></td>
						</tr>
						<tr>
							<td>教学站:</td>
							<td><input type="text" id="stuCenter3" name="stuCenter"
								title="${graduateData.studentInfo.branchSchool }"
								style="width: 50%"
								value="${graduateData.studentInfo.branchSchool }"
								readonly="readonly" /></td>
							<td>年级:</td>
							<td><input type="text" id="grade3" name="grade"
								title="${graduateData.studentInfo.grade }" style="width: 50%"
								value="${graduateData.studentInfo.grade }" readonly="readonly" /></td>
						</tr>
						<tr>
							<td>专业:</td>
							<td><input type="text" id="major3" name="major"
								title="${graduateData.studentInfo.major }" style="width: 50%"
								value="${graduateData.studentInfo.major }" readonly="readonly" /></td>
							<td>层次:</td>
							<td><input type="text" id="classic3" name="classic"
								title="${graduateData.studentInfo.classic }" style="width: 50%"
								value="${graduateData.studentInfo.classic }" readonly="readonly" /></td>
						</tr>
						<tr>
							<td>毕业证书编号:</td>
							<td><input type="text" id="diplomaNum" name="diplomaNum"
								style="width: 50%" value="${graduateData.diplomaNum }" /></td>
							<td>学位证书编号:</td>
							<td><input type="text" id="degreeNum" name="degreeNum"
								style="width: 50%" value="${graduateData.degreeNum }" /></td>
						</tr>
						<tr>
							<td>学位名称:</td>
							<td><input type="text" id="degreeName" name="degreeName"
								style="width: 50%" value="${graduateData.degreeName }" /></td>
							<td>毕业类型:</td>
							<td><gh:select name="graduateType"
									value="${graduateData.graduateType }"
									dictionaryCode="CodeGraduateType" style="width:52%" /></td>
						</tr>
						<tr>
							<td>毕业日期:</td>
							<td><input type="text" name="graduateDate"
								style="width: 50%"
								value="<fmt:formatDate value="${graduateData.graduateDate}" pattern="yyyy-MM-dd"/>"
								class="Wdate" onclick="WdatePicker({isShowWeek:true})" /></td>
							<td>入学日期:</td>
							<td><input type="text" name="entranceDate"
								style="width: 50%"
								value="<fmt:formatDate value="${graduateData.entranceDate}" pattern="yyyy-MM-dd"/>"
								readonly="readonly" /></td>
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
<!--

//-->
</script>
</html>