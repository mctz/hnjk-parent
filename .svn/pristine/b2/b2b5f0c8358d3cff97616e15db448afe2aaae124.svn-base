<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看补考成绩录入情况</title>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/examresult/teachingplanexamresult-uninputlist.html"
				method="post">
				<input type="hidden" id="type" name="type" value="makeup" /> <input
					type="hidden" id="gradeName" name="gradeName"
					value="${condition['gradeName'] }" /> <input type="hidden"
					id="classicName" name="classicName"
					value="${condition['classicName'] }" /> <input type="hidden"
					id="teachingtype" name="teachingtype"
					value="${condition['teachingtype'] }" /> <input type="hidden"
					id="majorName" name="majorName" value="${condition['majorName'] }" />
				<input type="hidden" id="className" name="className"
					value="${condition['className'] }" /> <input type="hidden"
					id="classesMaster" name="classesMaster"
					value="${condition['classesMaster'] }" /> <input type="hidden"
					id="teachingPlanId" name="teachingPlanId"
					value="${condition['teachingPlanId'] }" /> <input type="hidden"
					id="gradeId" name="gradeId" value="${condition['gradeId'] }" /> <input
					type="hidden" id="classesId" name="classesId"
					value="${condition['classesId'] }" /> <input type="hidden"
					id="majorId" name="majorId" value="${condition['majorId'] }" /> <input
					type="hidden" id="classicId" name="classicId"
					value="${condition['classicId'] }" /> <input type="hidden"
					id="teachingtype" name="teachingtype"
					value="${condition['teachingtype'] }" /> <input type="hidden"
					id="branchSchool" name="branchSchool"
					value="${condition['branchSchool'] }" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>考试批次：</label> <gh:selectModel
								id="makeupInfo_ExamSub" name="examSubId" bindValue="resourceid"
								displayValue="batchName" style="width:55%"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['examSubId']}"
								condition="isDeleted='0' and batchType='exam' and examType<>'N'"
								orderBy="examinputStartTime desc" /></li>
						<li><label>课程：</label> <gh:courseAutocomplete name="courseId"
								tabindex="1" id="makeupInfo_courseId"
								value="${condition['courseId']}" displayType="code"
								isFilterTeacher="Y" style="width:55%" /></li>
						<li><span class="buttonActive" style="margin-left: 50px"><span
								class="buttonContent"><button type="submit"
										style="cursor: pointer;">查 询</button></span></span></li>
					</ul>
					<div style="margin-top: 15px;">
						<ul class="searchContent">
							<li><strong>年级：</strong> ${condition['gradeName'] }</li>
							<li style="width: 40%"><strong>层次：</strong>
								${condition['classicName'] }</li>
							<li><strong>学习方式：</strong>
								${ghfn:dictCode2Val('CodeTeachingType',condition['teachingtype'])}
							</li>
						</ul>
						<ul class="searchContent">
							<li><strong>专业：</strong> ${condition['majorName'] }</li>
							<li style="width: 40%"><strong>班级：</strong>
								${condition['className'] }</li>
							<li><strong>班主任：</strong> ${condition['classesMaster'] }</li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="150">
				<thead>
					<tr>
						<th width="8%" style="text-align: center; vertical-align: middle;">考试批次</th>
						<th width="15%"
							style="text-align: center; vertical-align: middle;">课程名称</th>
						<th width="6%" style="text-align: center; vertical-align: middle;">教务员</th>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">教务员联系电话</th>
						<th width="6%" style="text-align: center; vertical-align: middle;">登分老师</th>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">登分老师联系电话</th>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">保存/提交/发布</th>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">录入人数/总人数</th>
					</tr>
				</thead>
				<tbody id="teachingPlanExamresultBody">
					<c:forEach items="${teachingPlanCourseList.result}" var="t"
						varStatus="vs">
						<tr>
							<td style="text-align: center; vertical-align: middle;">
								${t.batchname}</td>
							<td style="text-align: center; vertical-align: middle;">${t.coursename}</td>
							<td style="text-align: center; vertical-align: middle;">
								${t.emName}</td>
							<td style="text-align: center; vertical-align: middle;">
								${t.emPhone}</td>
							<td style="text-align: center; vertical-align: middle;">
								${t.teachername}</td>
							<td style="text-align: center; vertical-align: middle;">
								${t.teacherPhone}</td>
							<td style="text-align: center; vertical-align: middle;">${t.checkStatus0 }/${t.checkStatus1 }/${t.checkStatus4 }</td>
							<td style="text-align: center; vertical-align: middle;"><c:if
									test="${t.inputcount==0 }">
									<span
										style="color: red; line-height: 21px; vertical-align: middle;">
								</c:if> <c:if test="${t.checkStatus4== t.counts }">
									<span
										style="color: green; line-height: 21px; vertical-align: middle;">
								</c:if> ${t.inputcount }/${t.counts } <c:if
									test="${t.inputcount==0 ||t.checkStatus4== t.counts}">
									</span>
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${teachingPlanCourseList}"
				goPageUrl="${baseUrl }/edu3/teaching/examresult/teachingplanexamresult-uninputlist.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
<script type="text/javascript">
	
</script>
</html>