<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>统考成绩查看列表</title>
</head>
<body>
	<script type="text/javascript">
		$(document).ready(
				function() {					
					universalexamQueryBegin();
				});

		//打开页面或者点击查询（即加载页面执行）
		function universalexamQueryBegin() {
			var defaultValue = "${condition['unitId']}";
			var schoolId = "";
			var isBrschool = "${isAdultEducation}";
			if (!isBrschool) {
				schoolId = defaultValue;
			}
			var gradeId = "${condition['stuGrade']}";
			var classicId = "${condition['stuClassic']}";
			var selectIdsJson = "{unitId:'universalexam_brSchoolName',gradeId:'universalexam_stuGrade',classicId:'universalexam_stuClassic'}";
			cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,
					"", "", "", selectIdsJson);
		}

		// 选择教学点
		function universalexamQueryUnit() {
			var defaultValue = $("#universalexam_brSchoolName").val();
			var selectIdsJson = "{gradeId:'universalexam_stuGrade',classicId:'universalexam_stuClassic'}";
			cascadeQuery("unit", defaultValue, "", "", "", "", "", "",
					selectIdsJson);
		}

		// 选择年级
		function universalexamQueryGrade() {
			var defaultValue = $("#universalexam_brSchoolName").val();
			var gradeId = $("#stuGrade").val();
			var selectIdsJson = "{classicId:'universalexam_stuClassic'}";
			cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",
					selectIdsJson);
		}

		//显示单门课程的详细情况
		function displayDetails(courseid, courseName, _term, examType,unitId,stuGrade) {
			var url = "${baseUrl }/edu3/teaching/universalExam/universalExam-new-details.html";
			var param = "?_term=" + _term + "&courseId=" + courseid
					+ "&examType=" + examType+ "&unitId=" + unitId+ "&stuGrade=" + stuGrade;
			navTab.openTab("RES_TEACHING_RESULT_UNIVERSALEXAM_NEW_DETAILS", url
					+ param, courseName + "统考成绩查看");
		}
		//导出所有学生名单模板
		function exporUniversalExamDetails() {
			var url = "${baseUrl}/edu3/teaching/universalExam/universalExam-new-export-details.html";
			var _term = "${_term}";
			var examType = $("#universalexam_type_new").val();
			var unitId = "${condition['unitId']}";
			var stuGrade = "${condition['stuGrade']}";
			var stuClassic = "${condition['stuClassic']}";
			var courseId = $("#universalexam_courseid_new").val();
			var param = "?_term=" + _term + "&examType=" + examType + "&unitId=" + unitId+ "&stuGrade=" + stuGrade +"&stuClassic=" + stuClassic +"&courseId="+courseId;
			if (_term == '' || _term == 'defined') {
				alertMsg
				.warn("请先选择查询条件中的<font color='red'>【学期】</font>和<font color='red'>【考试类型】</font>，并点击<font color='red'>【查询】</font>后再使用该功能");
				return false;
			}
			alertMsg.confirm("您确定要导出统考课程的名单吗？", {
				okCall : function() {
					downloadFileByIframe(url + param, '_tgradeIframe');
				}
			});
		}
		//导入--对应于导出模板
		function importUniversalExamResults() {
			
			var url = "${baseUrl}/edu/teaching/universalExam/universalExam-new-import.html";
			var term = "${_term}";
			var examType = $("#universalexam_type_new").val();
			if (term == ''|| term == 'defined') {
				alertMsg
						.warn("请先选择查询条件中的<font color='red'>【学期】</font>和<font color='red'>【考试类型】</font>，并点击<font color='red'>【查询】</font>后再使用该功能");
				return false;
			}
			var isAdultEducation = '${isAdultEducation}';
			if (isAdultEducation == "false" || !isAdultEducation) {
				alertMsg.warn("该导入功能只允许成教院单位的账号使用！");
				return false;
			}
			var param = "?_term=" + term + "&examType=" + examType;
			$.pdialog.open(url + param,
					"RES_TEACHING_RESULT_UNIVERSALEXAM_NEW_IMPORT", "导入统考成绩", {
						width : 480,
						height : 320
					});
		}
		//导出名单统计
		function exporUniversalExamCount() {
			var url = "${baseUrl}/edu3/teaching/universalExam/universalExam-new-export-count.html";
			var _term = "${_term}";
			var examType = $("#universalexam_type_new").val();
			var unitId = "${condition['unitId']}";
			var stuGrade = "${condition['stuGrade']}";
			var stuClassic = "${condition['stuClassic']}";
			var courseId = $("#universalexam_courseid_new").val();
			var param = "?_term=" + _term + "&examType=" + examType + "&unitId=" + unitId+ "&stuGrade=" + stuGrade +"&stuClassic=" + stuClassic +"&courseId="+courseId;
			if (_term == '' || _term == 'defined') {
				alertMsg
						.warn("请先选择查询条件中的<font color='red'>【学期】</font>和<font color='red'>【考试类型】</font>，并点击<font color='red'>【查询】</font>后再使用该功能");
				return false;
			}
			alertMsg.confirm("您确定要导出统考课程反馈人数表格吗？", {
				okCall : function() {
					downloadFileByIframe(url + param, '_tgradeIframe');
				}
			});
		}

		function checkParams(form) {
			var term = $("#universalexam_term_new").val();
			if (term == '' || term == null) {
				alertMsg
						.warn("请先选择查询条件中的<font color='red'>【学期】</font>后，再进行<font color='red'>【查询】</font>");
				return false;
			}
			return navTabSearch(form);
		}
	</script>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return checkParams(this);"
				action="${baseUrl}/edu3/teaching/universalExam/universalExam-new-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>学期：<span style="color: red;">(必选)</span></label> <gh:semesterAutocomplete
								name="term" id="universalexam_term_new" tabindex="1"
								displayType="code" defaultValue="${condition['examSubId']}"
								style="width:130px;"></gh:semesterAutocomplete><span
							style="color: red;">*</span></li>

						<li><label>考试类型：</label> <gh:select
								id="universalexam_type_new" name="examType"
								dictionaryCode="ExamResult" choose="N" filtrationStr="N,Y,T,Q"
								value="${condition['examType']}" style="width:120px;" /><span
							style="color: red;">*</span></li>
						<li><label>课程：</label> <gh:TKcourseAutocomplete
								name="courseId" tabindex="1" id="universalexam_courseid_new"
								value="${condition['courseId']}" displayType="code"
								isFilterTeacher="Y" style="width:55%" />
						<li class="custom-li"><label>教学站：</label> <span sel-id="universalexam_brSchoolName"
								sel-name="unitId" sel-onchange="universalexamQueryUnit()"
								sel-classs="flexselect" ></span></li>
						<li><label>年级：</label> <span sel-id="universalexam_stuGrade"
							sel-name="stuGrade" 
							sel-style="width: 120px"></span></li>
							<li><label>层次：</label> <span sel-id="universalexam_stuClassic"
							sel-name="stuClassic" 
							sel-style="width: 120px"></span></li>
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
		<gh:resAuth parentCode="RES_TEACHING_UNIVERSALEXAM_LIST_NEW"
			pageType="list"></gh:resAuth>
		<div class="pageContent">
			<table class="table" layouth="140" width="100%">
				<thead>
					<tr>
						<th width="10%" align="center">上课学期</th>
						<th width="10%" align="center">考试类型</th>
						<th width="10%" align="center">课程名称</th>
						<th width="10%" align="center">免考人数</th>
						<th width="10%" align="center">缓考人数</th>
						<th width="10%" align="center">未录入成绩人数</th>
						<th width="10%" align="center">总人数</th>
						<th width="10%" align="center">操作</th>
					</tr>
				</thead>
				<tbody id="UniversalExamNewBody">
					<c:forEach items="${page.result}" var="r" varStatus="vs">
						<tr>
							<td align="center">${fn:split(r.term,"_")[0]}年${fn:split(r.term,"_")[1] =='01' ?'一学期':'二学期'}</td>
							<td align="center">${ghfn:dictCode2Val('ExamResult',r.examType) }</td>
							<td align="center">${r.courseName }</td>
							<td align="center">${r.neCount }</td>
							<td align="center">${r.aeCount }</td>
							<td align="center">${r.wCount }</td>
							<td align="center">${r.stuCount }</td>
							<td align="center"><a href="javaScript:void(0)"
								onclick="displayDetails('${r.courseid}','${r.courseName }','${_term }','${r.examType }','${condition['unitId'] }','${condition['stuGrade'] }')">查看<c:if
										test="${r.wCount ne '-'}">
										<font color='red'>/未完成</font>
									</c:if></a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="pageContent">
			<gh:page page="${page}" targetType="navTab"
				goPageUrl="${baseUrl }/edu3/teaching/universalExam/universalExam-new-list.html"
				pageType="sys" condition="${condition }" />
			</div>
		</div>
	</div>
</body>
</html>