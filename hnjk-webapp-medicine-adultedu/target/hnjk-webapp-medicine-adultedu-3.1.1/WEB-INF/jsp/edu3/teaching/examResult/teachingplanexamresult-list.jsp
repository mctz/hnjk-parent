<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看教学计划成绩录入情况</title>
<script type="text/javascript">
$(document).ready(function(){
	$("#tper_brSchoolName").flexselect({
		  specialevent:function(){brschool_Major();}
	});
	
	//brschool_Major();

	$("#tper_classesid").flexselect();
	$("#tper_examresults_major").flexselect();
	teachingplanexamresultQueryBegin();
});

//打开页面或者点击查询（即加载页面执行）
function teachingplanexamresultQueryBegin() {
	var defaultValue = "${condition['branchSchool']}";
	var schoolId = "";
	var isBrschool = "${isBrschool}";
	if(isBrschool==true || isBrschool=="true"){
		schoolId = defaultValue;
	}
	var gradeId = "${condition['stuGrade']}";
	var classicId = "${condition['classic']}";
	var teachingType = "${condition['teachingType']}";
	var majorId = "${condition['major']}";
	var classesId = "${condition['classesId']}";
	var selectIdsJson = "{unitId:'tper_brSchoolName',gradeId:'stuGrade',classicId:'classic',teachingType:'teachingType',majorId:'tper_examresults_major',classesId:'classesId'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function teachingplanexamresultQueryUnit() {
	var defaultValue = $("#tper_brSchoolName").val();
	var selectIdsJson = "{gradeId:'stuGrade',classicId:'classic',teachingType:'teachingType',majorId:'tper_examresults_major',classesId:'classesId'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function teachingplanexamresultQueryGrade() {
	var defaultValue = $("#tper_brSchoolName").val();
	var gradeId = $("#stuGrade").val();
	var selectIdsJson = "{classicId:'classic',teachingType:'teachingType',majorId:'tper_examresults_major',classesId:'classesId'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function teachingplanexamresultQueryClassic() {
	var defaultValue = $("#tper_brSchoolName").val();
	var gradeId = $("#stuGrade").val();
	var classicId = $("#classic").val();
	var selectIdsJson = "{teachingType:'teachingType',majorId:'tper_examresults_major',classesId:'classesId'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择学习形式
function teachingplanexamresultQueryTeachingType() {
	var defaultValue = $("#tper_brSchoolName").val();
	var gradeId = $("#stuGrade").val();
	var classicId = $("#classic").val();
	var teachingTypeId = $("#teachingType").val();
	var selectIdsJson = "{majorId:'tper_examresults_major',classesId:'classesId'}";
	cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
}

// 选择专业
function teachingplanexamresultQueryMajor() {
	var defaultValue = $("#tper_brSchoolName").val();
	var gradeId = $("#stuGrade").val();
	var classicId = $("#classic").val();
	var teachingTypeId = $("#teachingType").val();
	var majorId = $("#tper_examresults_major").val();
	var selectIdsJson = "{classesId:'classesId'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}

</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader" style="height: 75px;">
			<form onsubmit="return navTabSearch(this);" id="tperForm"
				action="${baseUrl }/edu3/teaching/examresult/teachingplanexamresult-list.html" method="post">
				<input type="hidden" id="isBrschool" name="isBrschool"
					value="${isBrschool }" />
				<div id="stuInfo" class="searchBar">
					<ul class="searchContent">
						<%-- <c:if test="${!isBrschool }"> --%>
							<li class="custom-li"><label>教学站：</label> <span sel-id="tper_brSchoolName"
								sel-name="branchSchool"
								sel-onchange="teachingplanexamresultQueryUnit()"
								sel-classs="flexselect"></span></li>
						<%-- </c:if>
						<c:if test="${isBrschool}">
							<input type="text" name="showSchoolName" id="showSchoolName"
								value="${showSchoolName}" readonly="readonly" />
							<input type="hidden" name="branchSchool" id="tper_brSchoolName"
								value="${condition['branchSchool']}" />
						</c:if> --%>
						<li><label>年级：</label> <span sel-id="stuGrade"
							sel-name="stuGrade"
							sel-onchange="teachingplanexamresultQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="classic"
							sel-name="classic"
							sel-onchange="teachingplanexamresultQueryClassic()"
							sel-style="width: 120px"></span></li>
						<li><label>学习方式：</label> <span sel-id="teachingType"
							sel-name="teachingType"
							sel-onchange="teachingplanexamresultQueryTeachingType()"
							dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
						</li>
					</ul>
					<ul class="searchContent">
						<li id="tper-gradeToMajor4" class="custom-li"><label>专业：</label> <span
							sel-id="tper_examresults_major" sel-name="major"
							sel-onchange="teachingplanexamresultQueryMajor()"
							sel-classs="flexselect"></span></li>
						
						<li><label>班主任：</label> <input type="text"
							name="classesmaster" id="classesmaster"
							value="${condition['classesmaster']}" style="width: 115px" /></li>
						<li><label>是否全部录入成绩：</label> <gh:select id="isInputed"
								name="isInputed" dictionaryCode="yesOrNo"
								value="${condition['isInputed']}" style="width: 120px" /></li>
					</ul>
					<ul class="searchContent">
						<li id="tper-gradeToMajorToClasses4" class="custom-li"><label>班级：</label> <span
							sel-id="classesId" sel-name="classesId" sel-classs="flexselect"></span></li>
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
			<gh:resAuth
				parentCode="RES_TEACHING_RESULT_TEACHINGPLANEXAMRESULT_LIST"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="165">
				<thead>
					<tr>
						<th width="10%">教学站</th>
						<th width="4%">年级</th>
						<th width="5%">层次</th>
						<th width="4%">学习形式</th>
						<th width="10%">专业</th>
						<th width="10%">教学计划</th>
						<th width="10%">班级名称</th>
						<th width="4%">人数</th>
						<th width="5%">班主任</th>
						<th width="6%">开课/总课程</th>
						<th width="7%">有登分老师/开课课程</th>
						<th width="7%">已排课/开课课程</th>
						<th width="8%">已录成绩/开课课程</th>
						<th width="5%">正考成绩录入情况</th>
						<th width="5%">补考成绩录入情况</th>
					</tr>
				</thead>
				<tbody id="teachingPlanExamresultBody">
					<c:forEach items="${tpelist.result}" var="tpe" varStatus="vs">
						<tr>
							<td style="text-align: center; vertical-align: middle;">${tpe.unitname}</td>
							<td style="text-align: center; vertical-align: middle;">${tpe.gradename}</td>
							<td style="text-align: center; vertical-align: middle;">${tpe.classicname}</td>
							<td style="text-align: center; vertical-align: middle;">${ghfn:dictCode2Val('CodeTeachingType',tpe.teachingtype)}</td>
							<td style="text-align: center; vertical-align: middle;">${tpe.majorname }</td>
							<td style="text-align: center; vertical-align: middle;"><a
								href="javaScript:void(0)" style="cursor: pointer;"
								onclick="viewTeachingPlan('${tpe.teachplanid}')">${tpe.trainingtarget}</a>
							</td>
							<td style="text-align: center; vertical-align: middle;">${tpe.classesname}</td>
							<td style="text-align: center; vertical-align: middle;">${tpe.studentNum }</td>
							<td style="text-align: center; vertical-align: middle;">${tpe.classesmaster }</td>
							<td style="text-align: center; vertical-align: middle;">${tpe.openCourseNum }/${tpe.totalCourse }</td>
							<td style="text-align: center; vertical-align: middle;">${tpe.hasTeacherNum }/${tpe.openCourseNum }</td>
							<td style="text-align: center; vertical-align: middle;">${tpe.arrangedCourseNum }/${tpe.openCourseNum }</td>
							<td style="text-align: center; vertical-align: middle;"><c:if
									test="${tpe.hasResultNum < tpe.openCourseNum }">
									<span style="color: red; line-height: 21px;">
								</c:if> ${tpe.hasResultNum }/${tpe.openCourseNum } <c:if
									test="${tpe.hasResultNum < tpe.openCourseNum }">
									</span>
								</c:if></td>
							<td style="text-align: center; vertical-align: middle;"><a
								href="javaScript:void(0)" style="cursor: pointer;"
								onclick="examResultInfo('normal','${tpe.orgunitid}','${tpe.gradeid}','${tpe.classicid}','${tpe.teachingtype}','${tpe.majorid}',
		           		 '${tpe.teachplanid}','${tpe.classesid}','${tpe.classesmaster}','${tpe.gradename}','${tpe.classicname}','${tpe.majorname }','${tpe.classesname}')">查看</a>
							</td>
							<td style="text-align: center; vertical-align: middle;"><a
								href="javaScript:void(0)" style="cursor: pointer;"
								onclick="examResultInfo('makeup','${tpe.orgunitid}','${tpe.gradeid}','${tpe.classicid}','${tpe.teachingtype}','${tpe.majorid}',
		           		 	'${tpe.teachplanid}','${tpe.classesid}','${tpe.classesmaster}','${tpe.gradename}','${tpe.classicname}','${tpe.majorname }','${tpe.classesname}')">查看</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${tpelist}"
				goPageUrl="${baseUrl }/edu3/teaching/examresult/teachingplanexamresult-list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
<script type="text/javascript">
	//查看成绩录入情况
	function examResultInfo(type,orgunitid,gradeid,classicid,teachingtype,majorid,planid,classesid,
			classesMaster,gradeName,classicName,majorName,className){
		var title = "成绩录入情况";
		var tabId = "RES_TEACHING_RESULT_TEACHINGPLANEXAMRESULT_UNINPUTLIST";
		if(type=="makeup"){
			title = "补考成绩录入情况";
			tabId = "RES_TEACHING_RESULT_TEACHINGPLANEXAMRESULT_MAKEUPINPUTINFO";
		}
		var url = "${baseUrl}/edu3/teaching/examresult/teachingplanexamresult-uninputlist.html"
			+"?branchSchool="+orgunitid+"&gradeId="+gradeid+"&classicId="+classicid+"&teachingtype="+teachingtype+"&fromPage=Y"
			+"&majorId="+majorid+"&teachingPlanId="+planid+"&classesId="+classesid+"&classesMaster="+encodeURIComponent(classesMaster)+"&type="+type
			+"&gradeName="+encodeURIComponent(gradeName)+"&classicName="+encodeURIComponent(classicName)+"&majorName="
			+encodeURIComponent(majorName)+"&className="+encodeURIComponent(className);
		navTab.openTab(tabId,url,title);	
	}
	
	//浏览教学计划
	function viewTeachingPlan(pid){
		$.pdialog.open(baseUrl+'/edu3/teaching/teachingplan/edit.html?resourceid='+pid+'&act=view',
					'selector','查看教学计划',{width:800,height:600});
	}

	//导出
	function exportTeachingPlanExamResultList() {
        var url = "${baseUrl}/edu3/teaching/examresult/teachingplanexamresult-list.html?flag=export&"+$("#tperForm").serialize();
		downloadFileByIframe(url,'executeSQL_Ifram',"post");
    }

</script>
</html>