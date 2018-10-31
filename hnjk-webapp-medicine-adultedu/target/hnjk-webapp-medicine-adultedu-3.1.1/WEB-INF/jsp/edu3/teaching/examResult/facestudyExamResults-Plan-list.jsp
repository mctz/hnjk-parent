<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年级教学计划列表</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/examresults/facestudy-plan-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>考试批次：</label> <gh:selectModel
								id="faceStudyExamResults_ExamSub" name="examSubId"
								bindValue="resourceid" displayValue="batchName"
								style="width:55%"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['examSubId']}" condition="batchType='exam'"
								orderBy="examinputStartTime desc" /> <span style="color: red;">*</span>
						</li>
						<li><label>年级：</label>
						<gh:selectModel id="faceStudyExamResults_gradeid" name="gradeid"
								bindValue="resourceid" displayValue="gradeName"
								style="width:55%" modelClass="com.hnjk.edu.basedata.model.Grade"
								orderBy="gradeName desc" value="${condition['gradeid']}" /> <span
							style="color: red;">*</span></li>
					</ul>
					<ul class="searchContent">
						<%/* 
				<li>
					<label>层次：</label> <gh:selectModel id="faceStudyExamResults_classic" name="classic" bindValue="resourceid" displayValue="classicName" 
							modelClass="com.hnjk.edu.basedata.model.Classic"  value="${condition['classic']}" style="width:55%"/>
				</li>	
				<li >
					<label>专业：</label><gh:selectModel id="faceStudyExamResults_major" name="major" bindValue="resourceid" displayValue="majorCodeName" 
							modelClass="com.hnjk.edu.basedata.model.Major" value="${condition['major']}" condition="isAdult='Y'" orderBy="majorCode" style="width:55%"/>
				</li>	
				 		
				<li>
					<label>办学模式：</label>
					<gh:select id="faceStudyExamResults_schoolType" name="schoolType" value="${condition['schoolType']}" dictionaryCode="CodeTeachingType"/>	
				</li> */%>
					</ul>
					<div class="subBar">
						<ul>
							<li><span class="tips">考试批次和年级为必选项</span></li>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">

			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_faceStudyExamResults"
							onclick="checkboxAll('#check_all_faceStudyExamResults','resourceid','#faceStudyExamResultsgradeBody')" /></th>
						<th width="10%">年级</th>
						<th width="10%">办学模式</th>
						<th width="10%">层次</th>
						<th width="25%">专业</th>
						<th width="40%">计划名称</th>
					</tr>
				</thead>
				<tbody id="faceStudyExamResultsgradeBody">
					<c:forEach items="${gradeGuidePlanList.result}" var="g"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${g.resourceid }" autocomplete="off" /></td>
							<td>${g.grade }</td>
							<td>${ghfn:dictCode2Val('CodeTeachingType',g.teachingPlan.schoolType) }</td>
							<td>${g.teachingPlan.classic }</td>
							<td>${g.teachingPlan.major }</td>
							<td><a
								href="${baseUrl }/edu3/teaching/examresults/facestudy-course-list.html?guidPlanId=${g.resourceid }&examSubId=${condition['examSubId']}"
								target="navTab" rel="faceStudyExamResultsPlanCourse"
								title="${g.teachingPlan.teachingPlanName}">
									${g.teachingPlan.teachingPlanName} </a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${gradeGuidePlanList}"
				goPageUrl="${baseUrl }/edu3/teaching/examresults/facestudy-plan-list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>