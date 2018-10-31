<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成绩查看</title>
</head>
<body>
	<script type="text/javascript">

	
</script>
	<div class="page">

		<div class="pageHeader">
			<form id="publishedExamResultsSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/result/view-published-examresultlist.html"
				method="post">
				<input id="publishedExamResultsSearchForm_examInfoId" type="hidden"
					name="examInfoId" value="${condition['examInfoId'] }" />
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${ condition['isBrschool'] ne 'Y' }">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="publishedExamResultsSearchForm_brSchoolName"
									defaultValue="${condition['branchSchool']}" displayType="code"
									style="width:55%" /></li>
						</c:if>
						<li><label>年级：</label>
						<gh:selectModel id="publishedExamResultsSearchForm_gradeid"
								name="gradeid" bindValue="resourceid" displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['gradeid']}" orderBy="gradeName desc"
								style="width:55%" /></li>
						<li><label>层 次：</label> <gh:selectModel name="classic"
								bindValue="resourceid" displayValue="classicName"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${condition['classic']}" style="width:55%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>专 业：</label> <gh:selectModel name="major"
								bindValue="resourceid" displayValue="majorCodeName"
								modelClass="com.hnjk.edu.basedata.model.Major"
								orderBy="majorCode asc" value="${condition['major']}"
								style="width:55%" /></li>


						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" /></li>
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>成绩状态：</label> <gh:select name="checkStatus"
								dictionaryCode="CodeExamResultCheckStatus" choose="Y"
								value="${condition['checkStatus']}" style="width:55%" /></li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<!-- RES_TEACHING_RESULT_NETWORKSTUDY_INPUT_LIST 
	<gh:resAuth parentCode="RES_TEACHING_RESULT_NETWORKSTUDY_INPUT_LIST" pageType="netWorkStudySub"></gh:resAuth>-->
		<div class="pageContent">
			<table class="table" layouth="138" width="100%">
				<thead>
					<tr>
						<th width="4%"><input type="checkbox" name="checkall"
							id="check_all_examResults_published"
							onclick="checkboxAll('#check_all_examResults_published','resourceid','#publishedExamResultsBody')" /></th>

						<th width="10%">教学站</th>
						<th width="7%">层次</th>
						<th width="20%">专业</th>
						<th width="10%">学号</th>
						<th width="8%">姓名</th>
						<th width="6%">选考次数</th>
						<th width="6%">卷面成绩</th>
						<th width="6%">平时成绩</th>
						<th width="6%">综合成绩</th>
						<th width="6%">成绩异常</th>
						<th width="6%">成绩状态</th>
					</tr>
				</thead>
				<tbody id="publishedExamResultsBody">
					<c:forEach items="${page.result}" var="examResults" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${examResults.resourceid }" autocomplete="off" /></td>
							<td>${examResults.studentInfo.branchSchool.unitCode }-${examResults.studentInfo.branchSchool.unitShortName }</td>
							<td>${examResults.studentInfo.classic.classicName }</td>
							<td>${examResults.studentInfo.major.majorName}</td>
							<td>${examResults.studentInfo.studyNo}</td>
							<td>${examResults.studentInfo.studentName}</td>
							<td><c:choose>
									<c:when test="${not empty examResults.examCount}">${ examResults.examCount}</c:when>
									<c:otherwise>0</c:otherwise>
								</c:choose></td>
							<td>${examResults.writtenScore}</td>
							<td>${examResults.usuallyScore}</td>
							<td>${examResults.integratedScore}</td>
							<td>
								${ghfn:dictCode2Val('CodeExamAbnormity',examResults.examAbnormity)}

							</td>
							<td>${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.checkStatus)}</td>
						</tr>
					</c:forEach>

				</tbody>
			</table>

			<gh:page page="${page}" targetType="navTab"
				goPageUrl="${baseUrl }/edu3/teaching/result/view-published-examresultlist.html"
				pageType="sys" condition="${condition }" />

		</div>
	</div>
</body>
</html>