<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看免考申请</title>
<script type="text/javascript">
//附件下载
function downloadAttachFile(attid){
		var url = baseUrl+"/edu3/framework/filemanage/download.html?id="+attid;
		var elemIF = document.createElement("iframe");  
	elemIF.src = url;  
	elemIF.style.display = "none";  
	document.body.appendChild(elemIF); 
}
</script>
</head>
<body>
	<h2 class="contentTitle">查看免考申请</h2>
	<div class="page">
		<div class="pageContent">
			<input type="hidden" name="resourceid"
				value="${noExamApply.resourceid }" />
			<div class="pageFormContent" layoutH="97">
				<table class="form">
					<tr>
						<td width="20%">教学站:</td>
						<td width="30%">${noExamApply.studentInfo.branchSchool }</td>
						<td width="20%">层次:</td>
						<td width="30%">${noExamApply.studentInfo.classic }</td>
					</tr>
					<tr>
						<td width="20%">学生学号:</td>
						<td width="30%">${noExamApply.studentInfo.studyNo }</td>
						<td width="20%">专业:</td>
						<td width="30%">${noExamApply.studentInfo.major }</td>
					</tr>
					<tr>
						<td width="20%">学生姓名:</td>
						<td width="30%">${noExamApply.studentInfo.studentName }</td>
						<td width="20%">年级:</td>
						<td width="30%">${noExamApply.studentInfo.grade }</td>
					</tr>
					<tr>
						<td width="20%">课程名称:</td>
						<td width="30%">${noExamApply.course.courseName }</td>
						<td width="20%">免修类型:</td>
						<td width="30%">${ghfn:dictCode2Val('CodeUnScoreStyle',noExamApply.unScore) }
						</td>
					</tr>
					<tr>
						<td width="20%">成绩类型:</td>
						<td width="30%">${ghfn:dictCode2Val('CodeCourseScoreStyle',noExamApply.courseScoreType ) }</td>
						<td width="20%">总评成绩:</td>
						<c:choose>
							<c:when test="${schoolCode eq '10601' }">
								<td width="30%">合格</td>
							</c:when>
							<c:otherwise>
								<td width="30%">${noExamApply.scoreForCount }</td>
							</c:otherwise>
						</c:choose>
					</tr>
					<tr>
						<td width="20%">申请时间:</td>
						<td width="30%"><fmt:formatDate
								value="${noExamApply.subjectTime}"
								pattern="yyyy年MM月dd日 HH:mm:ss" /></td>
						<td width="20%">审核时间:</td>
						<td width="30%"><fmt:formatDate
								value="${noExamApply.checkTime}" pattern="yyyy年MM月dd日 HH:mm:ss" /></td>
					</tr>
					<tr>
						<td width="20%">免试申请附件:</td>
						<td width="30%"><c:forEach items="${attachList}" var="attach">
								<li id="${attach.resourceid}"><img
									src="${ baseUrl}/jscript/jquery.uploadify/images/attach.png"
									style="cursor: pointer; height: 10px" /> <a href="#"
									onclick="downloadAttachFile('${attach.resourceid}')">
										${attach.attName}</a></li>
							</c:forEach></td>
						<td width="20%">备注:</td>
						<td width="30%">${noExamApply.memo }</td>
					</tr>
					<tr>

					</tr>
				</table>
			</div>

		</div>
	</div>
</body>
</html>