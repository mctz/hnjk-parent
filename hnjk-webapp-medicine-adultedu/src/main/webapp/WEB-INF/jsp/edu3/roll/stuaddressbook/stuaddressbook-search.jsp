<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>高级查询</title>
<script type="text/javascript">
	$(document).ready(function(){
		stuaddressbook_search_QueryBegin();
	});
	//打开页面或者点击查询（即加载页面执行）
	function stuaddressbook_search_QueryBegin() {
		var defaultValue = "${condition['branchSchool']}";
		var schoolId = "${linkageQuerySchoolId}";
		var gradeId = "${condition['gradeid']}";
		var classicId = "${condition['classic']}";
		var teachingType = "${condition['schoolType']}";
		var majorId = "${condition['major']}";
		
		var selectIdsJson = "{unitId:'stuaddressbook_search_branchSchool',gradeId:'stuaddressbook_search_gradeid',classicId:'stuaddressbook_search_classicid',teachingType:'stuaddressbook_search_schoolTypeid',majorId:'stuaddressbook_search_majorid'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, "", selectIdsJson);
	}

	// 选择教学点
	function stuaddressbook_search_QueryUnit() {
		var defaultValue = $("#stuaddressbook_search_branchSchool").val();
		var selectIdsJson = "{gradeId:'stuaddressbook_search_gradeid',classicId:'stuaddressbook_search_classicid',teachingType:'stuaddressbook_search_schoolTypeid',majorId:'stuaddressbook_search_majorid'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择年级
	function stuaddressbook_search_QueryGrade() {
		var defaultValue = $("#stuaddressbook_search_branchSchool").val();
		var gradeId = $("#stuaddressbook_search_gradeid").val();
		var selectIdsJson = "{classicId:'stuaddressbook_search_classicid',teachingType:'stuaddressbook_search_schoolTypeid',majorId:'stuaddressbook_search_majorid'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function stuaddressbook_search_QueryClassic() {
		var defaultValue = $("#stuaddressbook_search_branchSchool").val();
		var gradeId = $("#stuaddressbook_search_gradeid").val();
		var classicId = $("#stuaddressbook_search_classicid").val();
		var selectIdsJson = "{teachingType:'stuaddressbook_search_schoolTypeid',majorId:'stuaddressbook_search_majorid'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}

	// 选择学习形式
	function stuaddressbook_search_QueryTeachingType() {
		var defaultValue = $("#stuaddressbook_search_branchSchool").val();
		var gradeId = $("#stuaddressbook_search_gradeid").val();
		var classicId = $("#stuaddressbook_search_classicid").val();
		var teachingTypeId = $("#stuaddressbook_search_schoolTypeid").val();
		var selectIdsJson = "{majorId:'stuaddressbook_search_majorid'}";
		cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
	}

</script>
</head>
<body>
	<script type="text/javascript">
</script>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl }/edu3/student/info/addressbook.html"
				class="pageForm" onsubmit="return navTabSearch(this);">
				<div class="pageFormContent" layoutH="58">
					<c:if test="${empty brSchool }">
						<div>
							<label>教学站：</label>
							<%--
								<gh:selectModel name="branchSchool" bindValue="resourceid" displayValue="unitName" style="width:120px"
								modelClass="com.hnjk.security.model.OrgUnit" value="${condition['branchSchool']}"  condition="unitType='brSchool'"/> --%>

							<span sel-id="stuaddressbook_search_branchSchool"
								sel-name="branchSchool"
								sel-onchange="stuaddressbook_search_QueryUnit()"
								sel-classs="flexselect" sel-style="width: 120px"></span>
						</div>
					</c:if>
					<div>
						<label>年级：</label> <span sel-id="stuaddressbook_search_gradeid"
							sel-name="gradeid"
							sel-onchange="stuaddressbook_search_QueryGrade()"
							sel-style="width: 120px"></span>
					</div>
					<div>
						<label>层次：</label> <span sel-id="stuaddressbook_search_classicid"
							sel-name="classic"
							sel-onchange="stuaddressbook_search_QueryClassic()"
							sel-style="width: 120px"></span>
					</div>
					<div>
						<label>办学模式：</label> <span
							sel-id="stuaddressbook_search_schoolTypeid" sel-name="schoolType"
							sel-onchange="stuaddressbook_search_QueryTeachingType()"
							dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
					</div>
					<div>
						<label>专业：</label> <span sel-id="stuaddressbook_search_majorid"
							sel-name="major"
							sel-onchange="stuaddressbook_search_QueryMajor()"
							sel-classs="flexselect" sel-style="width: 120px"></span>
					</div>
					<div>
						<label>学籍状态：</label>
						<gh:select name="stuStatus" value="${condition['stuStatus']}"
							dictionaryCode="CodeStudentStatus" style="width:120px" />
					</div>
					<div>
						<label>学号：</label><input type="text" name="matriculateNoticeNo"
							value="${condition['matriculateNoticeNo']}" style="width: 115px" />
					</div>
					<div>
						<label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 115px" />
					</div>
					<div>
						<label>性别：</label>
						<gh:select name="gender" value="${condition['gender']}"
							dictionaryCode="CodeSex" style="width:120px" />
					</div>
					<div class="divider">divider</div>
					<div>
						<label>排序条件：</label> <select name="orderBy">
							<option value="">--请选择--</option>
							<option value="branchSchool">教学站</option>
							<option value="grade">年级</option>
							<option value="major">专业</option>
							<option value="classic">层次</option>
							<option value="studentStatus">学籍状态</option>
							<option value="teachingPlan.schoolType">办学模式</option>
							<option value="studyNo">学号</option>
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
