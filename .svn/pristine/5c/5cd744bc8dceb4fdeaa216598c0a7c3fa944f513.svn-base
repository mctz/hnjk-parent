<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程具体信息</title>
</head>
<body>
	<h2 class="contentTitle" style="border-bottom: medium;">${course.courseName}</h2>
	<div class="page">
		<div class="pageFormContent" layoutH="25">
			<table class="form">
				<tbody>
					<tr>
						<td width="20%">课程编码：</td>
						<td width="30%">${course.courseCode }</td>
						<td width="20%">课程名称：</td>
						<td width="30%">${course.courseName}</td>
					</tr>
					<tr>
						<td>课程英文名称：</td>
						<td>${course.courseEnName }</td>
						<td>课程简称：</td>
						<td>${course.courseShortName}</td>
					</tr>
					<tr>
						<td>中文简介：</td>
						<td colspan="3">${course.chsIntroduction }</td>
					</tr>
					<tr>
						<td>英文简介：</td>
						<td colspan="3">${course.enIntroduction }</td>
					</tr>
					<tr>
						<td>课程状态：</td>
						<td>${ghfn:dictCode2Val('CodeCourseState',course.status ) }<c:if
								test="${course.status eq 2 and not empty course.stopTime }">
		        	 (停用日期：<fmt:formatDate value="${course.stopTime}"
									pattern="yyyy-MM-dd" />)  
		        	</c:if>
						</td>
						<td>是否实践课程：</td>
						<td>${ghfn:dictCode2Val('yesOrNo',course.isPractice ) }</td>
					</tr>
					<tr>
						<td>是否统考课程：</td>
						<td>${ghfn:dictCode2Val('yesOrNo',course.isUniteExam ) }</td>
						<td>是否学位统考课程：</td>
						<td>${ghfn:dictCode2Val('yesOrNo',course.isDegreeUnitExam ) }</td>
					</tr>
					<tr>
						<td>课程考试形式：</td>
						<td>${ghfn:dictCode2Val('CodeExamMode',course.examType) }</td>
						<td>课程考试编码：</td>
						<td>${course.examCode }</td>
					</tr>
					<tr>
						<td>计划外学时:</td>
						<td>${course.planoutStudyHour }</td>
						<td>计划外学分:</td>
						<td>${course.planoutCreditHour }</td>
					</tr>
					<tr>
						<td>备注：</td>
						<td colspan="3">${course.memo }</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>

</body>
</html>