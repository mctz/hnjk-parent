<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>高级查询</title>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		studentBbsstatSearchQueryBegin();
	});
	//打开页面或者点击查询（即加载页面执行）
	function studentBbsstatSearchQueryBegin() {
		var defaultValue = "${condition['unitId']}";
		var schoolId = "${linkageQuerySchoolId}";
		var gradeId = "${condition['gradeid']}";
		var classicId = "${condition['classic']}";
		
		var majorId = "${condition['major']}";
		
		var selectIdsJson = "{unitId:'studentBbsstatSearch_as_unitId',gradeId:'studentBbsstatSearch_as_gradeId',classicId:'studentBbsstatSearch_as_classic',majorId:'studentBbsstatSearch_as_major'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,"", majorId, "", selectIdsJson);
	}

	// 选择教学点
	function studentBbsstatSearchQueryUnit() {
		var defaultValue = $("#studentBbsstatSearch_as_unitId").val();
		var selectIdsJson = "{gradeId:'studentBbsstatSearch_as_gradeId',classicId:'studentBbsstatSearch_as_classic',majorId:'studentBbsstatSearch_as_major'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择年级
	function studentBbsstatSearchQueryGrade() {
		var defaultValue = $("#studentBbsstatSearch_as_unitId").val();
		var gradeId = $("#studentBbsstatSearch_as_gradeId").val();
		var selectIdsJson = "{classicId:'studentBbsstatSearch_as_classic',majorId:'studentBbsstatSearch_as_major'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function studentBbsstatSearchQueryClassic() {
		var defaultValue = $("#studentBbsstatSearch_as_unitId").val();
		var gradeId = $("#studentBbsstatSearch_as_gradeId").val();
		var classicId = $("#studentBbsstatSearch_as_classic").val();
		var selectIdsJson = "{majorId:'studentBbsstatSearch_as_major'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}

</script>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl }/edu3/learning/bbs/student/stat.html"
				class="pageForm" onsubmit="return navTabSearch(this);">
				<div class="pageFormContent" layoutH="58">
					<div>
						<label>教学站：</label> <span sel-id="studentBbsstatSearch_as_unitId"
							sel-name="unitId" sel-onchange="studentBbsstatSearchQueryUnit()"
							sel-classs="flexselect" sel-style="width: 120px"></span>
					</div>
					<div>
						<label>年级：</label> <span sel-id="studentBbsstatSearch_as_gradeId"
							sel-name="gradeid"
							sel-onchange="studentBbsstatSearchQueryGrade()"
							sel-style="width: 120px"></span>
					</div>

					<div>
						<label>层次：</label> <span sel-id="studentBbsstatSearch_as_classic"
							sel-name="classic"
							sel-onchange="studentBbsstatSearchQueryClassic()"
							sel-style="width: 120px"></span>
					</div>
					<div>
						<label>专业：</label> <span sel-id="studentBbsstatSearch_as_major"
							sel-name="major" sel-classs="flexselect" sel-style="width: 120px"></span>
					</div>

					<div>
						<label>课程：</label>
						<gh:courseAutocomplete id="studentbbsstatsearchCourseId"
							name="courseId" tabindex="2" value="${condition['courseId']}"
							displayType="code" />
					</div>
					<div>
						<label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo'] }" />
					</div>
					<div>
						<label>姓名：</label><input type="text" name="studentName"
							value="${condition['studentName'] }" />
					</div>
					<div>
						<label>开始时间：</label> <input type="text" name="startTime"
							value="${condition['startTime']}" class="Wdate"
							id="studentStatStartTime"
							onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'studentStatEndTime\')}'})" />
					</div>
					<div>
						<label>结束时间：</label> <input type="text" name="endTime"
							value="${condition['endTime']}" class="Wdate"
							id="studentStatEndTime"
							onFocus="WdatePicker({minDate:'#F{$dp.$D(\'studentStatStartTime\')}'})" />
					</div>
					<div class="divider">divider</div>
					<%-- 
				<div>
					<label>是否只查询优秀贴：</label>
					<select name="isBest">						
						<option value="N">否</option>	
						<option value="Y" ${(condition['isBest'] eq 'Y')?'selected':''}>是</option>		
					</select>
				</div>
				 --%>
					<div>
						<label>排序条件：</label> <select name="orderBy">
							<option value="">--请选择--</option>
							<option value="course.courseName">课程</option>
							<option value="unit.unitName">教学站</option>
							<option value="grade.gradeName">年级</option>
							<option value="major.majorName">专业</option>
							<option value="classic.classicName">层次</option>
							<option value="stuinfo.studyNo">学号</option>
						</select> <label class="radioButton"><input name="orderType"
							type="radio" value="ASC" />顺序</label> <label class="radioButton"><input
							name="orderType" type="radio" value="DESC" />倒序</label>
					</div>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">开始检索</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="reset">清空重输</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
