<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看学生答题情况</title>
<script type="text/javascript">
$(document).ready(function(){
	$("select[class*=flexselect]").flexselect();
});
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/metares/exercise/activeexercise/state.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						
						<li style="width: 200px;"><label style="width: 50px;">年度：</label> <gh:selectModel
								id="activecourseexam_state_yearInfo" name="yearInfoId"
								bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo" style="width:100px;"
								value="${condition['yearInfoId']}" orderBy="firstYear desc " />
						</li>
						<li style="width: 160px;"><label style="width: 45px;">学期：</label>
						<gh:select id="activecourseexam_state_term" name="term" style="width:80px;"
								value="${condition['term']}" dictionaryCode="CodeTerm" /></li>
						<li class="custom-li"><label>课程：</label> ${activecourseexamCourseSelect }</li>
					</ul>
					<ul class="searchContent">
						<c:if test="${empty activecourseexamstateUnit}">
							<li class="custom-li"><label>教学站：</label> <gh:brSchoolAutocomplete
									name="unitid" tabindex="2" id="activecourseexam_state_unitId" 
									displayType="code" defaultValue="${condition['unitid']}" style="width:240px;"/></li>
						</c:if>
						<li class="custom-li"><label>班级：</label> <gh:classesAutocomplete
								name="classesId" id="activecorseexam_state_classid" tabindex="1"
								displayType="code" defaultValue="${condition['classesId']}"
								exCondition="${classesCondition}"  style="width: 240px;"></gh:classesAutocomplete></li>
					</ul>
					<div class="subBar">
						<ul>
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
			<table class="table" layouth="158">
				<thead>
					<tr>
						<th width="2%"></th>
						<th width="20%">课程</th>
						<th width="20%">知识节点</th>
						<th width="20%">班级</th>
						<th width="5%">习题序号</th>
						<th width="8%">已答题学生人数</th>
						<th width="5%">答对人数</th>
						<th width="13%">答题正确率</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${activecourseexamStateList }" var="stat">
						<tr>
							<td>&nbsp;</td>
							<td>${stat.coursename }</td>
							<td>${stat.nodename }</td>
							<td>${stat.classesname }</td>
							<td>${stat.showOrder }</td>
							<td>${stat.correctcount+stat.mistakecount }</td>
							<td>${stat.correctcount }</td>
							<td><fmt:formatNumber type="percent" pattern="###.##%"
									value="${stat.correctper }" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>