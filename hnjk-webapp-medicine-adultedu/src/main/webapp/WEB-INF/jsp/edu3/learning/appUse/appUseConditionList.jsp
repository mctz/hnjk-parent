<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>app使用情况统计</title>
<script type="text/javascript">
$(document).ready(function(){
	appUseConditionQueryBegin();
});
//打开页面或者点击查询（即加载页面执行）
function appUseConditionQueryBegin() {
	var defaultValue = "${condition['brSchoolId']}";
	var schoolId = "${linkageQuerySchoolId}";
	var gradeId = "${condition['gradeId']}";
	var classicId = "${condition['classicId']}";
	var teachingType = "${condition['teachingType']}";
	var majorId = "${condition['majorId']}";
	var classesId = "${condition['classesId']}";
	var selectIdsJson = "{unitId:'appUseCondition_school',gradeId:'appUseCondition_grade',classicId:'appUseCondition_classic',teachingType:'appUseCondition_teachingType',majorId:'appUseCondition_major',classesId:'appUseCondition_classes'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function appUseConditionQueryUnit() {
	var defaultValue = $("#appUseCondition_school").val();
	var selectIdsJson = "{gradeId:'appUseCondition_grade',classicId:'appUseCondition_classic',teachingType:'appUseCondition_teachingType',majorId:'appUseCondition_major',classesId:'appUseCondition_classes'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function appUseConditionQueryGrade() {
	var defaultValue = $("#appUseCondition_school").val();
	var gradeId = $("#appUseCondition_grade").val();
	var selectIdsJson = "{classicId:'appUseCondition_classic',teachingType:'appUseCondition_teachingType',majorId:'appUseCondition_major',classesId:'appUseCondition_classes'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function appUseConditionQueryClassic() {
	var defaultValue = $("#appUseCondition_school").val();
	var gradeId = $("#appUseCondition_grade").val();
	var classicId = $("#appUseCondition_classic").val();
	var selectIdsJson = "{teachingType:'appUseCondition_teachingType',majorId:'appUseCondition_major',classesId:'appUseCondition_classes'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择学习形式
function appUseConditionQueryTeachingType() {
	var defaultValue = $("#appUseCondition_school").val();
	var gradeId = $("#appUseCondition_grade").val();
	var classicId = $("#appUseCondition_classic").val();
	var teachingTypeId = $("#appUseCondition_teachingType").val();
	var selectIdsJson = "{majorId:'appUseCondition_major',classesId:'appUseCondition_classes'}";
	cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
}

// 选择专业
function appUseConditionQueryMajor() {
	var defaultValue = $("#appUseCondition_school").val();
	var gradeId = $("#appUseCondition_grade").val();
	var classicId = $("#appUseCondition_classic").val();
	var teachingTypeId = $("#appUseCondition_teachingType").val();
	var majorId = $("#appUseCondition_major").val();
	var selectIdsJson = "{classesId:'appUseCondition_classes'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}

//安查询条件导出Excel
function exportExcel(){
	$('#frame_exportExcel').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frame_exportExcel";
	
	var brSchoolId    = $("#appUseCondition_school").val();
	var gradeId 	  = $("#appUseCondition_grade").val();
	var classicId 	  = $("#appUseCondition_classic").val();
	var teachingType  = $("#appUseCondition_teachingType").val();
	var majorId 	  = $("#appUseCondition_major").val();
	var classesId 	  = $("#appUseCondition_classes").val(); 
	var terminalType  = $("#appUseCondition_terminalType").val();
	var isUsemobileTerminal	= $("#appUseCondition_isUsemobileTerminal").val();
	var k   = 0;
	var resIds = "";
	var num = $("#useConditionIBody input[name='resourceid']:checked").size();
	$("#useConditionIBody  input[@name='resourceid']:checked").each(function(){
		resIds+=$(this).val();
        if(k != num -1 ) resIds += ",";
        k++;
    });
	// 勾选
	if(isChecked('resourceid','#useConditionIBody')){
		iframe.src = "${baseUrl}/edu3/learning/app/useCondition/export.html?exportType=1&ids="+resIds;
	}else{
		iframe.src = "${baseUrl }/edu3/learning/app/useCondition/export.html?brSchoolId="
			+brSchoolId+"&gradeId="+gradeId+"&classicId="+classicId+"&teachingType="+teachingType
			+"&majorId="+majorId+"&classesId="+classesId+"&terminalType="+terminalType				
			+"&isUsemobileTerminal="+isUsemobileTerminal;
	}
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/learning/app/useCondition/list.html?flag=Y"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${!isBrschool }">
							<li><label>教学点：</label> <span
								sel-id="appUseCondition_school" sel-name="brSchoolId"
								sel-onchange="appUseConditionQueryUnit()"
								sel-classs="flexselect" sel-style="width: 120px"></span></li>
						</c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" name="brSchoolId"
								id="appUseCondition_school" value="${condition['brSchoolId']}" />
						</c:if>
						<li><label>年级：</label> <span sel-id="appUseCondition_grade"
							sel-name="gradeId" sel-onchange="appUseConditionQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="appUseCondition_classic"
							sel-name="classicId" sel-onchange="appUseConditionQueryClassic()"
							sel-style="width: 120px"></span></li>
						<li><label>学习形式：</label> <span
							sel-id="appUseCondition_teachingType" sel-name="teachingType"
							sel-onchange="appUseConditionQueryTeachingType()"
							dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
						</li>
					</ul>
					<ul class="searchContent">
						<li><label>专业：</label> <span sel-id="appUseCondition_major"
							sel-name="majorId" sel-onchange="appUseConditionQueryMajor()"
							sel-classs="flexselect" sel-style="width: 120px"></span></li>
						<li><label>班级：</label> <span sel-id="appUseCondition_classes"
							sel-name="classesId" sel-classs="flexselect"
							sel-style="width: 120px"></span></li>
						<li><label>终端类型：</label> <gh:select
								id="appUseCondition_terminalType" name="terminalType"
								value="${condition['terminalType']}"
								dictionaryCode="terminalType" style="width: 120px" /></li>
						<li><label>是否使用移动端：</label> <gh:select
								id="appUseCondition_isUsemobileTerminal"
								dictionaryCode="yesOrNo" name="isUsemobileTerminal"
								value="${condition['isUsemobileTerminal']}" style="width: 120px" />
						</li>
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
			<gh:resAuth parentCode="RES_LEARN_APP_USECONDITION" pageType="list"></gh:resAuth>
			<table class="table" layouth="160">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_useCondition"
							onclick="checkboxAll('#check_all_useCondition','resourceid','#useConditionIBody')" /></th>
						<th width="15%"
							style="text-align: center; vertical-align: middle;">教学点</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">年级</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">层次</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">学习形式</th>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">专业</th>
						<th width="15%"
							style="text-align: center; vertical-align: middle;">班级</th>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">姓名</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">终端类型</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">是否使用移动端</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">系统版本</th>
					</tr>
				</thead>
				<tbody id="useConditionIBody">
					<c:forEach items="${appUsePage.result }" var="item" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${item['resourceid']}" autocomplete="off" /></td>
							<td style="text-align: center; vertical-align: middle;">${item['unitname']}</td>
							<td style="text-align: center; vertical-align: middle;">${item['gradeName']}</td>
							<td style="text-align: center; vertical-align: middle;">${item['classicName']}</td>
							<td style="text-align: center; vertical-align: middle;">${ghfn:dictCode2Val('CodeTeachingType',item['teachingType'])}</td>
							<td style="text-align: center; vertical-align: middle;">${item['majorName']}</td>
							<td style="text-align: center; vertical-align: middle;">${item['classesname']}</td>
							<td style="text-align: center; vertical-align: middle;">${item['studentname']}</td>
							<td style="text-align: center; vertical-align: middle;">${ghfn:dictCode2Val('terminalType',item['terminalType'])}</td>
							<td style="text-align: center; vertical-align: middle;">${ghfn:dictCode2Val('yesOrNo',item['isusemobileterminal'])}</td>
							<td style="text-align: center; vertical-align: middle;">${item['loginversion']}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${appUsePage}"
				goPageUrl="${baseUrl}/edu3/learning/app/useCondition/list.html"
				condition="${condition }" pageType="sys" />
		</div>
	</div>
</body>
</html>