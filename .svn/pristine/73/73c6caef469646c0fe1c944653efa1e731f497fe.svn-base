<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看成绩录入情况</title>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/examresult/teachingplanexamresult-uninputlist.html"
				method="post">
				<div id="" class="searchBar">
					<ul class="searchContent">
						<li><strong>年级：</strong> ${condition['gradeName'] }</li>
						<li><strong>层次：</strong> ${condition['classicName'] }</li>
						<li><strong>学习方式：</strong>
							${ghfn:dictCode2Val('CodeTeachingType',condition['teachingtype'])}
						</li>
					</ul>
					<ul class="searchContent">
						<li><strong>专业：</strong> ${condition['majorName'] }</li>
						<li><strong>班级：</strong> ${condition['className'] }</li>
						<li><strong>班主任：</strong> ${condition['classesMaster'] }</li>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="112">
				<thead>
					<tr>
						<th width="15%"
							style="text-align: center; vertical-align: middle;">课程名称</th>
						<th width="8%" style="text-align: center; vertical-align: middle;">上课学期</th>
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
							<td style="text-align: center; vertical-align: middle;">${t.coursename}</td>
							<td style="text-align: center; vertical-align: middle;">
								${fn:split(t.coursestatusterm,"_")[0]}年${fn:split(t.coursestatusterm,"_")[1] =='01' ?'春季':'秋季'}
							</td>
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
								</c:if> <c:if test="${t.checkStatus4== t.inputTotalNum }">
									<span
										style="color: green; line-height: 21px; vertical-align: middle;">
								</c:if> ${t.inputcount }/${t.inputTotalNum } <c:if
									test="${t.inputcount==0 ||t.checkStatus4== t.inputTotalNum}">
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
</html>