<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学站替学生申请缓考</title>
</head>
<body>

	<div class="page">
		<div class="pageHeader" style="height: 75px;">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/abnormalExam/abnormalExam-brschool-stu-list.html"
				method="post">
				<div id="brSchool_abnormalExam_stuInfo" class="searchBar">
					<ul class="searchContent">
						<%-- <c:if test="${!isBrschool }"> --%>
							<li class="custom-li"><label>教学站：</label> <span
								sel-id="brSchool_abnormalExam_brSchoolName"
								sel-name="abnormalExamSchool"
								sel-onchange="brSchool_abnormalExamQueryUnit()"
								sel-classs="flexselect"></span></li>
						<%-- </c:if>
						<c:if test="${isBrschool }">
							<input type="hidden" id="brSchool_abnormalExam_brSchoolName"
								name="abnormalExamSchool" value="${branchSchoolId }" />
						</c:if> --%>

						<li><label>年级：</label> <span
							sel-id="brSchool_abnormalExam_stuGrade"
							sel-name="abnormalExamGrade"
							sel-onchange="brSchool_abnormalExamQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span
							sel-id="brSchool_abnormalExam_classic"
							sel-name="abnormalExamClassic"
							sel-onchange="brSchool_abnormalExamQueryClassic()"
							sel-style="width: 120px"></span></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"> <label>专业：</label><span
						 	sel-id="brSchool_abnormalExam_major"
							sel-name="abnormalExamMajor" sel-classs="flexselect"
							></span>
						</li>
						<li><label>姓名：</label><input type="text"
							name="abnormalExamName" id="brSchool_abnormalExam_name"
							value="${condition['abnormalExamName']}" style="width: 115px" />
						</li>
						
						<li><label>学号：</label><input type="text"
							name="abnormalExamStudyNo" id="brSchool_abnormalExam_studyNo"
							value="${condition['abnormalExamStudyNo']}" style="width: 115px" />
						</li>
						
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>身份证号：</label><input type="text"
							name="abnormalExamCertNum" id="brSchool_abnormalExam_certNum"
							value="${condition['abnormalExamCertNum']}" style="width: 240px" />
						</li>
						<li><label>学籍状态：</label> <gh:select name="abnormalExamStatus"
								id="brSchool_abnormalExam_stuStatus"
								value="${condition['abnormalExamStatus']}"
								dictionaryCode="CodeStudentStatus" filtrationStr="11,21,25"
								style="width:120px" /></li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_ABNORMALEXAM_BYBRSCHOOL_LIST"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="163">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="brSchool_abnormalExam_check_all_info"
							onclick="checkboxAll('#brSchool_abnormalExam_check_all_info','resourceid','#brSchool_abnormalExam_infoBody')" /></th>
						<th width="10%">姓名</th>
						<th width="10%">学号</th>
						<th width="5%">性别</th>
						<th width="12%">身份证</th>
						<th width="8%">民族</th>
						<th width="8%">年级</th>
						<th width="8%">培养层次</th>
						<th width="13%">专业</th>
						<th width="11%">教学站</th>
						<th width="6%">学籍状态</th>
					</tr>
				</thead>
				<tbody id="brSchool_abnormalExam_infoBody">
					<c:forEach items="${stulist.result}" var="stu" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stu.resourceid }" autocomplete="off" /></td>
							<td><a href="#" onclick="viewStuInfo2('${stu.resourceid}')"
								title="点击查看">${stu.studentBaseInfo.name }</a></td>
							<td>${stu.studyNo}</td>
							<td>${ghfn:dictCode2Val('CodeSex',stu.studentBaseInfo.gender) }</td>
							<td>${stu.studentBaseInfo.certNum }</td>
							<td>${ghfn:dictCode2Val('CodeNation',stu.studentBaseInfo.nation) }</td>
							<td>${stu.grade.gradeName}</td>
							<td>${stu.classic.classicName }</td>
							<td>${stu.major.majorName }</td>
							<td>${stu.branchSchool}</td>
							<td>${ghfn:dictCode2Val('CodeStudentStatus',stu.studentStatus) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${stulist}"
				goPageUrl="${baseUrl }/edu3/teaching/abnormalExam/abnormalExam-brschool-stu-list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
<script type="text/javascript">
$(document).ready(function(){
	brSchool_abnormalExamQueryBegin();
});

//打开页面或者点击查询（即加载页面执行）
function brSchool_abnormalExamQueryBegin() {
	var defaultValue = "${condition['abnormalExamSchool']}";
	var schoolId = "";
	var isBrschool = "${isBrschool}";
	if(isBrschool==true || isBrschool=="true"){
		schoolId = defaultValue;
	}
	var gradeId = "${condition['abnormalExamGrade']}";
	var classicId = "${condition['abnormalExamClassic']}";
	
	var majorId = "${condition['abnormalExamMajor']}";
	
	var selectIdsJson = "{unitId:'brSchool_abnormalExam_brSchoolName',gradeId:'brSchool_abnormalExam_stuGrade',classicId:'brSchool_abnormalExam_classic',majorId:'brSchool_abnormalExam_major'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,"", majorId, "", selectIdsJson);
}

// 选择教学点
function brSchool_abnormalExamQueryUnit() {
	var defaultValue = $("#brSchool_abnormalExam_brSchoolName").val();
	var selectIdsJson = "{gradeId:'brSchool_abnormalExam_stuGrade',classicId:'brSchool_abnormalExam_classic',majorId:'brSchool_abnormalExam_major'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function brSchool_abnormalExamQueryGrade() {
	var defaultValue = $("#brSchool_abnormalExam_brSchoolName").val();
	var gradeId = $("#brSchool_abnormalExam_stuGrade").val();
	var selectIdsJson = "{classicId:'brSchool_abnormalExam_classic',majorId:'brSchool_abnormalExam_major'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function brSchool_abnormalExamQueryClassic() {
	var defaultValue = $("#brSchool_abnormalExam_brSchoolName").val();
	var gradeId = $("#brSchool_abnormalExam_stuGrade").val();
	var classicId = $("#brSchool_abnormalExam_classic").val();
	var selectIdsJson = "{majorId:'brSchool_abnormalExam_major'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

	function viewStuInfo2(id){
		var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
		$.pdialog.open(url+'?resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});
	}
	//申请免考
	function appAbnormalExam(){
		if(isCheckOnlyone('resourceid','#brSchool_abnormalExam_infoBody')){
			var studentId 	  = $("#brSchool_abnormalExam_infoBody input[@name='resourceid']:checked").val();
			var url           = "${baseUrl}/edu3/teaching/abnormalExam/abnormalExam-brschool-form.html?studentId="+studentId;
			navTab.openTab('RES_TEACHING_ABNORMALEXAM_APPLY_BYBRSCHOOL_FORM',url,'缓考申请');
		}
	}
	
	/* //年级-专业 级联
	function searchExamResultChangMajor(){
		var gradeId = $("#brSchool_abnormalExam_stuGrade").val(); //年级
		var classic=$("#brSchool_abnormalExam_classic").val();
		$.ajax({
			type:"post",
			url:"${baseUrl}/edu3/schoolroll/graduate/audit/list/grade-major/page2.html",
			data:{gradeId_page1:gradeId,id_page1:'query_abnormalExam_major',name_page1:'abnormalExamMajor',classic:classic},
			dataType:"json",
			success:function(data){
				$('#abnormalExam-gradeToMajor').html('<label>专业：</label>'+data['msg']);
				$("select[class*=flexselect]").flexselect();
			}
		});
	 } */
	
</script>
</html>