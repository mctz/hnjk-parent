<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教评结果列表</title>
</head>
<script type="text/javascript">

	$(document).ready(function() {
		stuQuestionnaireQueryBegin();
	});
	//打开页面或者点击查询（即加载页面执行）
	function stuQuestionnaireQueryBegin() {
		var defaultValue = "${condition['brSchool']}";
		var schoolId = "${linkageQuerySchoolId}";
		var classesId = "${condition['classesId']}";
		var selectIdsJson = "{unitId:'stuQuestionnaire_unitId'}";
		cascadeQuery("begin", defaultValue, schoolId, "", "", "", "",
				classesId, selectIdsJson);
	}
// 	function questionnaireQueryUnit() {
// 		var defaultValue = $("#questionnaire_brSchool").val();
// 		var selectIdsJson = "{classesId:'questionnaire_classesid'}";
// 		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",
// 				selectIdsJson);
// 	}

	function evaluationResultExport(){
		var url="${baseUrl }/edu3/teaching/quality/evaluation/stuQuestionnaire/evaluationResultExport.html";
		var brSchool=$("#stuQuestionnaire_unitId").val();
		var yearId=$("#stuQuestionnaireYearId").val();
		var term=$("#stuQuestionnaireTerm").val();
		var orgUnitType=$("#stuQuestionnaireIsExternal").val();		
		var param = "?brSchool="+brSchool+"&yearId="+yearId+"&term="+term+"&orgUnitType="+orgUnitType;
		alertMsg.confirm("您确定要按照当前所选择条件进行导出吗？", {
			okCall:function(){
				downloadFileByIframe(url+param, "stuQuestionnaireExportIframe");
			}
		})
		
	}
	function showStuQuestionnaire(teacherid){
		var url = "${baseUrl }/edu3/teaching/quality/evaluation/stuQuestionnaire/TeacherView.html?teacherid="+teacherid;
		$.pdialog.open(url,'stuQuestionnaireTeachrView','课堂教学质量问卷列表',{width:800,height:450});
	}
</script>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="stuQuestionnaireResult_form" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/quality/evaluation/stuQuestionnaire/resultPage.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li style="width: 480px;"><label>教学点：</label> <span
							sel-id="stuQuestionnaire_unitId" sel-name="brSchool"
							sel-onchange="stuQuestionnaireQueryUnit()"
							sel-classs="flexselect"></span></li>
						<li><label>年度：</label> <gh:selectModel
								id="stuQuestionnaireYearId" name="yearId" bindValue="resourceid"
								displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearId']}" orderBy="yearName desc"
								style="width:125px" /></li>
						<li><label>学期：</label> <gh:select id="stuQuestionnaireTerm"
								name="term" dictionaryCode="CodeTerm"
								value="${condition['term']}" style="width:125px" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>编制：</label>
						<gh:select name="orgUnitType" value="${condition['orgUnitType']}"
								dictionaryCode="teacherOrgUnitType" style="width:125px" /></li>
						<li><label>教师姓名：</label> <input type="text"
							name="teacherName" value="${condition['teacherName']}"
							style="width: 125px" /></li>
						<li><label>教师账号：</label> <input type="text"
							name="teacherAccount" value="${condition['teacherAccount']}"
							style="width: 125px" /></li>

					</ul>
					<ul class="searchContent">
					<br>
					<span style="color:red">提示：点击教师姓名或账号，可查看教师每门课程的评价详情； 当有效问卷》=5份时，计算分数时，系统自动去掉最高10%及最低10%分数的问卷。</span>
					
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
			<gh:resAuth parentCode="RES_TEACHING_QUALITY_EVALUATION_RESULT" pageType="list"></gh:resAuth>
			<table class="table" layouth="188">
				<thead>
					<tr>
						<th width="15%">教学点</th>						
						<th width="10%">教师姓名</th>
						<th width="15%">教师账号</th>						
						<th width="10%">年度</th>
						<th width="10%">学期</th>
						<th width="10%">平均分</th>
						<th width="10%">有效采样率</th>
										
					</tr>
				</thead>
				<tbody id="stuQuestionnaireResultBody">
					<c:forEach items="${page.result}" var="sqn"
						varStatus="vs">
						<tr>
							<td>${sqn.unitname }</td>							
							<td><a href="#" onclick="showStuQuestionnaire('${sqn.teacherid}')">${sqn.cnname }</a></td>
							<td><a href="#" onclick="showStuQuestionnaire('${sqn.teacherid}')">${sqn.username }</a></td>							
							<td>${sqn.yearname }</td>							
							<td>${ghfn:dictCode2Val('CodeTerm',sqn.term) }</td>
							<td>${sqn.validavg }</td>
							<td>${sqn.validper }</td>
							
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/teaching/quality/evaluation/stuQuestionnaire/resultPage.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>