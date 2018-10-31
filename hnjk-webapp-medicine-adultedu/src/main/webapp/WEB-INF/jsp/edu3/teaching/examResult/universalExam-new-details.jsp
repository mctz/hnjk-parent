<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${page.result[0].courseName }详情</title>
</head>
<body>
<script type="text/javascript">
	
</script>
	<div class="page">
		<div class="pageHeader">
			
			<form action="${baseUrl }/edu3/teaching/universalExam/universalExam-new-details.html" method="post" onsubmit="return navTabSearch(this);">
			<div class="searchBar">
				<ul class="searchContent">
					<input type = "hidden" name="_term" value="${condition['_term'] }"/>
					<input type = "hidden" name="courseId" value="${condition['courseId'] }"/>
					<input type = "hidden" name="examType" value="${condition['examType'] }"/>
					<li><label>课程：</label><input type="text" disabled="true" value="${page.result[0].courseName }" /> </li>
					<li><label>学号：</label> <input type="text" name="studyNo" value="${condition['studyNo']}" /> </li>
					<li><label>姓名：</label> <input type="text" name="studentName" value="${condition['studentName']}" /> </li>
				</ul>
				<ul class="searchContent">
					<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>			
			</div>
			</form>
		</div>
		
		<div class="pageContent">
			<table class="table" layouth="111" width="100%">
				<thead>
					<tr>
						<th width="5%" align="center">教学点</th>
						<th width="5%" align="center">年级</th>
						<th width="5%" align="center">层次</th>
						<th width="10%" align="center">专业</th>
						<th width="10%" align="center">班级</th>
						<th width="5%" align="center">学号</th>
						<th width="5%" align="center">姓名</th>
						<th width="5%" align="center">考试类型</th>
						<th width="5%" align="center">是否免考</th>
						<th width="5%" align="center">是否缓考</th>
						<th width="5%" align="center">卷面成绩</th>
						<th width="5%" align="center">平时成绩</th>
						<th width="5%" align="center">综合成绩</th>
					</tr>
				</thead>
				<tbody id="UniversalExamDetailsBody">
					<c:forEach items="${page.result}" var="r" varStatus="vs">
					<tr>
						<td align="center">${r.unitName }</td>
						<td align="center">${r.gradeName }</td>
						<td align="center">${r.classicName }</td>
						<td align="center">${r.majorName }</td>
						<td align="center">${r.classesName }</td>
						<td align="center">${r.studyNo }</td>
						<td align="center">${r.studentName }</td>
						<td align="center">${ghfn:dictCode2Val('ExamResult',r.examType) }</td>
						<td align="center">${ghfn:dictCode2Val('CodeNoExamAppType',r.unScore) }</td>
						<td align="center">${ghfn:dictCode2Val('CodeAbnormalType',r.abnormalType)}</td>
						<td align="center">${r.writtenScore }</td>
						<td align="center">${r.usuallyScore }</td>
						<td align="center">${r.integratedScore }</td>
					
					</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<gh:page page="${page}" targetType="navTab"
						goPageUrl="${baseUrl }/edu3/teaching/universalExam/universalExam-new-details.html"
						pageType="sys" condition="${condition }" />
			
		</div>
	</div>
</body>
</html>