<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生发帖统计</title>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		studentBBSQueryBegin();
	});
	//打开页面或者点击查询（即加载页面执行）
	function studentBBSQueryBegin() {
		var defaultValue = "${condition['unitId']}";
		var schoolId = "${linkageQuerySchoolId}";
		var gradeId = "${condition['gradeid']}";
		var classicId = "${condition['classic']}";
	
		var majorId = "${condition['major']}";
	
		var selectIdsJson = "{unitId:'studentBbsstat_as_unitId',gradeId:'studentBbsstat_as_gradeId',classicId:'studentBbsstat_as_classic',majorId:'studentBbsstat_as_major'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,"", majorId, "", selectIdsJson);
	}

	// 选择教学点
	function studentBBSQueryUnit() {
		var defaultValue = $("#studentBbsstat_as_unitId").val();
		var selectIdsJson = "{gradeId:'studentBbsstat_as_gradeId',classicId:'studentBbsstat_as_classic',majorId:'studentBbsstat_as_major'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}
	
	// 选择年级
	function studentBBSQueryGrade() {
		var defaultValue = $("#studentBbsstat_as_unitId").val();
		var gradeId = $("#studentBbsstat_as_gradeId").val();
		var selectIdsJson = "{classicId:'studentBbsstat_as_classic',majorId:'studentBbsstat_as_major'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function studentBBSQueryClassic() {
		var defaultValue = $("#studentBbsstat_as_unitId").val();
		var gradeId = $("#studentBbsstat_as_gradeId").val();
		var classicId = $("#studentBbsstat_as_classic").val();
		var selectIdsJson = "{majorId:'major'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}

</script>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/learning/bbs/student/stat.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label> <span
							sel-id="studentBbsstat_as_unitId" sel-name="unitId"
							sel-onchange="studentBBSQueryUnit()" sel-classs="flexselect"
							sel-style="width: 120px"></span></li>
						<li><label>年级：</label> <span
							sel-id="studentBbsstat_as_gradeId" sel-name="gradeid"
							sel-onchange="studentBBSQueryGrade()" sel-style="width: 120px"></span>
						</li>

						<li><label>层次：</label> <span
							sel-id="studentBbsstat_as_classic" sel-name="classic"
							sel-onchange="studentBBSQueryClassic()" sel-style="width: 120px"></span>
						</li>
						<li><label>专业：</label> <span sel-id="studentBbsstat_as_major"
							sel-name="major" sel-classs="flexselect" sel-style="width: 120px"></span>
						</li>
						<li><label>课程：</label> <gh:courseAutocomplete
								id="studentBbsstat_as_studentbbsstatCourseId" name="courseId"
								tabindex="1" value="${condition['courseId']}" displayType="code" />
						</li>
						<li><label>姓名：</label><input type="text" name="studentName"
							value="${condition['studentName'] }" /></li>
					</ul>

					<div class="subBar">
						<ul>
							<li><span class="tips">提示：更多查询条件请点击高级查询</span></li>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
							<li><a class="button"
								href="${baseUrl }/edu3/learning/bbs/student/stat.html?con=advance"
								target="dialog" rel="student_stat" title="查询条件"><span>高级查询</span></a></li>
						</ul>
					</div>
				</div>
			</form>
		</div>

		<div class="pageContent">
			<table class="table" layouth="134">
				<thead>
					<tr>
						<th width="7%">年级</th>
						<th width="10%">专业</th>
						<th width="8%">层次</th>
						<th width="10%">教学站</th>
						<th width="10%">课程</th>
						<th width="10%">学号</th>
						<th width="8%">姓名</th>
						<th width="8%">登录名</th>
						<th width="8%">主题数</th>
						<th width="8%">回复数</th>
						<th width="8%">总发帖数</th>
						<th width="8%">优秀贴数</th>
					</tr>
				</thead>
				<tbody id="bbsStatBody">
					<c:forEach items="${bbsStatList.result }" var="stat">
						<tr>
							<td>${stat.gradeName }</td>
							<td>${stat.majorName }</td>
							<td>${stat.classicName }</td>
							<td>${stat.unitName }</td>
							<td>${stat.courseName }</td>
							<td>${stat.studyNo }</td>
							<td>${stat.studentName }</td>
							<td>${stat.username }</td>
							<td>${stat.topic }</td>
							<td>${stat.reply }</td>
							<td>${stat.topic + stat.reply }</td>
							<td>${stat.isbest }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${bbsStatList}"
				goPageUrl="${baseUrl}/edu3/learning/bbs/student/stat.html"
				condition="${condition }" pageType="sys" />
		</div>
	</div>
</body>
</html>