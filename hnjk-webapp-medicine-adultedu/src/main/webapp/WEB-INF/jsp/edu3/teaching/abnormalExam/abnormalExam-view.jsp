<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看缓考申请</title>
</head>
<body>
	<h2 class="contentTitle">查看缓考申请详细信息</h2>
	<div class="page">
		<div class="pageContent">
			<input type="hidden" name="resourceid"
				value="${abnormalExam.resourceid }" />
			<div class="pageFormContent" layoutH="97">
				<table class="form">
					<tr>
						<td width="20%">教学站:</td>
						<td width="30%">${abnormalExam.studentInfo.branchSchool }</td>
						<td width="20%">层次:</td>
						<td width="30%">${abnormalExam.studentInfo.classic }</td>
					</tr>
					<tr>
						<td width="20%">学生学号:</td>
						<td width="30%">${abnormalExam.studentInfo.studyNo }</td>
						<td width="20%">专业:</td>
						<td width="30%">${abnormalExam.studentInfo.major }</td>
					</tr>
					<tr>
						<td width="20%">学生姓名:</td>
						<td width="30%">${abnormalExam.studentInfo.studentName }</td>
						<td width="20%">年级:</td>
						<td width="30%">${abnormalExam.studentInfo.grade }</td>
					</tr>
					<tr>
						<td width="20%">课程名称:</td>
						<td width="30%">${abnormalExam.course.courseName }</td>
						<td width="20%">申请类型:</td>
						<td width="30%">${ghfn:dictCode2Val('CodeAbnormalType',abnormalExam.abnormalType) }
						</td>
					</tr>
					<tr>
						<td width="20%">考试类型:</td>
						<td width="30%">${ghfn:dictCode2Val('CodeAbnormalExamType',abnormalExam.examType ) }</td>
						<td width="20%">总评成绩:</td>
						<td width="30%">${abnormalExam.scoreForCount }</td>
					</tr>
					<tr>
						<td width="20%">申请时间:</td>
						<td width="30%"><fmt:formatDate
								value="${abnormalExam.applyDate}" pattern="yyyy年MM月dd日 HH:mm:ss" /></td>
						<td width="20%">申请人:</td>
						<td width="30%">${abnormalExam.applyManName }</td>
					</tr>
					<tr>
						<td width="20%">审核时间:</td>
						<td width="30%"><fmt:formatDate
								value="${abnormalExam.checkDate}" pattern="yyyy年MM月dd日 HH:mm:ss" /></td>
						<td width="20%">审核人:</td>
						<td width="30%">${abnormalExam.checkManName }</td>
					</tr>
					<tr>
						<td width="20%">申请理由:</td>
						<td width="30%">${abnormalExam.reason }</td>
						<td width="20%">查看附件（点击可下载）:</td>
						<td width="30%"><c:forEach items="${attachList}" var="attach">
								<li id="${attach.resourceid}"><img
									src="${ baseUrl}/jscript/jquery.uploadify/images/attach.png"
									style="cursor: pointer; height: 10px" /> <a href="#"
									onclick="downloadAttachFile('${attach.resourceid}')">
										${attach.attName}</a></li>
							</c:forEach></td>
					</tr>
				</table>
			</div>

		</div>
	</div>
</body>
</html>