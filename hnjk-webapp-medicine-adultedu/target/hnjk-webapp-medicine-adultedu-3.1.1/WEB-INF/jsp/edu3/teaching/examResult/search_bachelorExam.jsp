<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学士学位考试成绩列表</title>
<script type="text/javascript">
/**
	$(document).ready(function(){
		search_bachelorExamQueryBegin();
	});
	//打开页面或者点击查询（即加载页面执行）
	function search_bachelorExamQueryBegin() {
		var defaultValue = "${condition['search_bachelorExam_branchSchool']}";
		var schoolId = "${linkageQuerySchoolId}";
		var gradeId = "${condition['search_bachelorExam_grade']}";
		var classicId = "${condition['search_bachelorExam_classic']}";
		var teachingType = "${condition['search_bachelorExam_teachingType']}";
		var majorId = "${condition['search_bachelorExam_major']}";
		var classesId = "${condition['search_bachelorExam_classes']}";
		var selectIdsJson = "{unitId:'search_bachelorExam_branchSchool',gradeId:'search_bachelorExam_grade',classicId:'search_bachelorExam_classic',teachingType:'search_bachelorExam_teachingType',majorId:'search_bachelorExam_major',classesId:'search_bachelorExam_classes'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
	}

	// 选择教学点
	function search_bachelorExamQueryUnit() {
		var defaultValue = $("#search_bachelorExam_branchSchool").val();
		var selectIdsJson = "{gradeId:'search_bachelorExam_grade',classicId:'search_bachelorExam_classic',teachingType:'search_bachelorExam_teachingType',majorId:'search_bachelorExam_major',classesId:'search_bachelorExam_classes'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择年级
	function search_bachelorExamQueryGrade() {
		var defaultValue = $("#search_bachelorExam_branchSchool").val();
		var gradeId = $("#search_bachelorExam_grade").val();
		var selectIdsJson = "{classicId:'search_bachelorExam_classic',teachingType:'search_bachelorExam_teachingType',majorId:'search_bachelorExam_major',classesId:'search_bachelorExam_classes'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function search_bachelorExamQueryClassic() {
		var defaultValue = $("#search_bachelorExam_branchSchool").val();
		var gradeId = $("#search_bachelorExam_grade").val();
		var classicId = $("#search_bachelorExam_classic").val();
		var selectIdsJson = "{teachingType:'search_bachelorExam_teachingType',majorId:'search_bachelorExam_major',classesId:'search_bachelorExam_classes'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}

	// 选择学习形式
	function search_bachelorExamQueryTeachingType() {
		var defaultValue = $("#search_bachelorExam_branchSchool").val();
		var gradeId = $("#search_bachelorExam_grade").val();
		var classicId = $("#search_bachelorExam_classic").val();
		var teachingTypeId = $("#search_bachelorExam_teachingType").val();
		var selectIdsJson = "{majorId:'search_bachelorExam_major',classesId:'search_bachelorExam_classes'}";
		cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
	}

	// 选择专业
	function search_bachelorExamQueryMajor() {
		var defaultValue = $("#search_bachelorExam_branchSchool").val();
		var gradeId = $("#search_bachelorExam_grade").val();
		var classicId = $("#search_bachelorExam_classic").val();
		var teachingTypeId = $("#search_bachelorExam_teachingType").val();
		var majorId = $("#search_bachelorExam_major").val();
		var selectIdsJson = "{classesId:'search_bachelorExam_classes'}";
		cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
	}
	**/
	//下载模板
	function downloadERModel(){
		window.location.href="${baseUrl }/edu3/teaching/examResult/download-file.html"
	}
	//导入学士学位英语考试成绩
	function importScore(){
		$.pdialog.open(baseUrl+"/edu3/teaching/examResult/inputScoreForm.html", '导入学士学位英语考试成绩', {width: 600, height: 360});
	}
	
	//设置学士学位英语成绩查询批次
	function setBatch(){
		$.pdialog.open(baseUrl+"/edu3/teaching/examResult/setBatch.html", '设置学士学位英语成绩查询批次');
	}
	//查询接口
	function queryInterface(){
		window.open("${baseUrl}/bachelorExam.html");   
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="search_bachelorExam" onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/examResult/search_bachelorExam.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label> <gh:selectModel
								id="search_bachelorExam_yearInfoId" name="yearInfoId"
								bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfoId']}" orderBy="yearName desc"
								style="width:120px" /></li>
						<li><label>学期：</label> <gh:select name="term"
								dictionaryCode="CodeTerm" value="${condition['term']}"
								style="width:120px" /></li>
						<li><label>学号：</label> <input type="text" name="studentNO"
							id="search_bachelorExam_studentid"
							value="${condition['studentNO']}" style="width: 120px" /></li>
						<li><label>姓名：</label> <input type="text" name="studentName"
							id="search_bachelorExam_studentName"
							value="${condition['studentName']}" style="width: 120px" /></li>
						<!-- 	
				<c:if test="${!isBrschool }">
				<li>
					<label>教学点：</label> 
					<span sel-id="search_bachelorExam_branchSchool" sel-name="search_bachelorExam_branchSchool" sel-onchange="search_bachelorExamQueryUnit()" sel-classs="flexselect"  sel-style="width: 120px" ></span>
					
				</li>
				</c:if>
				<c:if test="${isBrschool}">
						<input type="hidden" name="search_bachelorExam_branchSchool" id="search_bachelorExam_branchSchool" value="${condition['search_bachelorExam_branchSchool']}" />
				</c:if>
				<li>
					<label>年级：</label> 
					<span sel-id="search_bachelorExam_grade" sel-name="search_bachelorExam_grade" sel-onchange="search_bachelorExamQueryGrade()"  sel-style="width: 120px"></span>
				</li>
			</ul>
			<ul class="searchContent">
				<li>
					<label>层次：</label> 
					<span sel-id="search_bachelorExam_classic" sel-name="search_bachelorExam_classic" sel-onchange="search_bachelorExamQueryClassic()" sel-style="width: 120px"></span>
				</li>
				<li>
					<label>学习形式：</label>
					<span sel-id="search_bachelorExam_teachingType" sel-name="search_bachelorExam_teachingType" sel-onchange="search_bachelorExamQueryTeachingType()" dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
				</li>
			
			
				<li>
					<label>专业：</label>
					<span sel-id="search_bachelorExam_major" sel-name="search_bachelorExam_major" sel-onchange="search_bachelorExamQueryMajor()" sel-classs="flexselect" sel-style="width: 120px"></span>
				</li>
			</ul>
			<ul class="searchContent">
				<li>
					<label>班级：</label>
					<span sel-id="search_bachelorExam_classes" sel-name="search_bachelorExam_classes" sel-classs="flexselect" sel-style="width: 120px"></span>
				</li>
				<li>
                 <label>学号：</label> 
                 <input  type="text" name="studentid" id="search_bachelorExam_studentid" value="${condition['studentid']}" style="width:120px"/>
                </li>
				<li>
					<label>姓名：</label>
					<input  type="text" name="studentName" id="search_bachelorExam_studentName" value="${condition['studentName']}" style="width:120px"/>
				</li>
			</ul>
			 -->
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
			<gh:resAuth parentCode="RES_TEACHING_BACHELOREXAMRESULTS_LIST"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="110" width="100%">
				<thead>
					<tr>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">年度</th>
						<th width="4%" style="text-align: center; vertical-align: middle;">学期</th>
						<th width="12%"
							style="text-align: center; vertical-align: middle;">教学站</th>
						<th width="8%" style="text-align: center; vertical-align: middle;">序号</th>
						<th width="8%" style="text-align: center; vertical-align: middle;">考场号</th>
						<th width="8%" style="text-align: center; vertical-align: middle;">座位号</th>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">学号</th>
						<th width="8%" style="text-align: center; vertical-align: middle;">姓名</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">成绩</th>
						<th width="15%"
							style="text-align: center; vertical-align: middle;">时间</th>
					</tr>
				</thead>
				<tbody id="search_bachelorExamBody">
					<c:forEach items="${examArrangementList.result}" var="examArr"
						varStatus="vs">
						<tr>
							<td style="text-align: center; vertical-align: middle;">${examArr.yearInfo.yearName }</td>
							<td style="text-align: center; vertical-align: middle;">${examArr.term }</td>
							<td style="text-align: center; vertical-align: middle;">${examArr.unitName }</td>
							<td style="text-align: center; vertical-align: middle;">${examArr.ordinal }</td>
							<td style="text-align: center; vertical-align: middle;">${examArr.examNo }</td>
							<td style="text-align: center; vertical-align: middle;">${examArr.seatNo }</td>
							<td style="text-align: center; vertical-align: middle;">${examArr.studentNO }</td>
							<td style="text-align: center; vertical-align: middle;">${examArr.studentName }</td>
							<td style="text-align: center; vertical-align: middle;">${examArr.examResults }</td>
							<td style="text-align: center; vertical-align: middle;">${examArr.examTime }</td>
							<%-- <td ><fmt:formatDate value="${examArr. }" pattern="yyyy年MM月dd HH:mm:ss"/> </td> --%>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${examArrangementList}"
				goPageUrl="${baseUrl }/edu3/teaching/examResult/search_bachelorExam.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>