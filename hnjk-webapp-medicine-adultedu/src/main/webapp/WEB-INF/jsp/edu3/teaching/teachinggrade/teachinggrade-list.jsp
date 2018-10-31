<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年级教学计划</title>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		/* $("#teachPlan_brSchoolid").flexselect({
			  specialevent:function(){brschool_Major();}
		});
		//brschool_Major();
		$("#teachPlan_major").flexselect();
		$("#teachinggrade_major").flexselect();
		$("#planName").flexselect(); */
		teachinggradeQueryBegin();
	});
	
	//打开页面或者点击查询（即加载页面执行）
	function teachinggradeQueryBegin() {
		var defaultValue = "${condition['brSchoolid']}";
		var schoolId = "${linkageQuerySchoolId}";
		var gradeId = "${condition['gradeid']}";
		var classicId = "${condition['classic']}";
		var teachingType = "${condition['schoolType']}";
		var majorId = "${condition['major']}";
		var classesId = "${condition['name~classes']}";
		var selectIdsJson = "{unitId:'teachPlan_brSchoolid',gradeId:'teachinggrade_stuGrade',classicId:'teachinggrade_classic',teachingType:'teachingType',majorId:'teachinggrade_major',classesId:'id~classes'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
	}

	// 选择教学点
	function teachinggradeQueryUnit() {
		var defaultValue = $("#teachPlan_brSchoolid").val();
		var selectIdsJson = "{gradeId:'teachinggrade_stuGrade',classicId:'teachinggrade_classic',teachingType:'teachingType',majorId:'teachinggrade_major',classesId:'id~classes'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择年级
	function teachinggradeQueryGrade() {
		var defaultValue = $("#teachPlan_brSchoolid").val();
		var gradeId = $("#teachinggrade_stuGrade").val();
		var selectIdsJson = "{classicId:'teachinggrade_classic',teachingType:'teachingType',majorId:'teachinggrade_major',classesId:'id~classes'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function teachinggradeQueryClassic() {
		var defaultValue = $("#teachPlan_brSchoolid").val();
		var gradeId = $("#teachinggrade_stuGrade").val();
		var classicId = $("#teachinggrade_classic").val();
		var selectIdsJson = "{teachingType:'teachingType',majorId:'teachinggrade_major',classesId:'id~classes'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}

	// 选择学习形式
	function teachinggradeQueryTeachingType() {
		var defaultValue = $("#teachPlan_brSchoolid").val();
		var gradeId = $("#teachinggrade_stuGrade").val();
		var classicId = $("#teachinggrade_classic").val();
		var teachingTypeId = $("#teachingType").val();
		var selectIdsJson = "{majorId:'teachinggrade_major',classesId:'id~classes'}";
		cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
	}

	//新增
	function addTGrade(){
		navTab.openTab('navTab', '${baseUrl}/edu3/teaching/teachinggrade/edit.html', '新增年级教学计划');
	}
	
	//修改
	function modifyTGrade(){
		//alertMsg.warn("该功能已禁用！");
		var url = "${baseUrl}/edu3/teaching/teachinggrade/edit.html";
		if(isCheckOnlyone('resourceid','#tgradeBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#tgradeBody input[@name='resourceid']:checked").val(), '编辑年级教学计划');
		}			
	}
		
	//删除
	function deleteTGrade(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/teachinggrade/delete.html","#tgradeBody");
	}
	//浏览
	function openGuiTeachplanView(pid){
	$.pdialog.open(baseUrl+'/edu3/teaching/teachingplan/edit.html?resourceid='+pid+'&act=view',
					'selector',
					'查看教学计划',
					{width:800,height:600}
					);
	}
	
	//发布
	function publishTGrade(){
		pageBarHandle("您确定要发布这些记录吗？","${baseUrl}/edu3/teaching/teachinggrade/publish.html?pubType=publish","#tgradeBody");
	}
	
	//取消发布
	function unpublishTGrade(){
		pageBarHandle("您确定要取消发布这些记录吗？","${baseUrl}/edu3/teaching/teachinggrade/publish.html?pubType=unpublish","#tgradeBody");
	}
	
	//设置统考课程
	function setStateExamCourse(){	
		alertMsg.warn("该功能已禁用！");
		//if(isCheckOnlyone('resourceid','#tgradeBody')){	
		//$.pdialog.open(baseUrl+'/edu3/teaching/teachinggrade/statecourse.html?resourceid='+$("#tgradeBody input[@name='resourceid']:checked").val(),
			//		'setStateExamCourse',
			//		'设置统考课程对应表',
			//		{width:800,height:600}
			//		);
		//}
	}
	
	function exportTGrade(){
		if(isCheckOnlyone('resourceid','#tgradeBody')){
			var url = baseUrl+"/edu3/teaching/teachinggrade/export.html?teachingGuidePlanId="+$("#tgradeBody input[@name='resourceid']:checked").val()
			downloadFileByIframe(url,"tgradeIframe");
		}	
	}	
	//复制年级教学计划
	function copyGuidTeachPlan(){
		//navTab.openTab('RES_TEACHING_ESTAB_PLAN_EDIT', '${baseUrl}/edu3/teaching/teachingplan/edit.html', '新增教学计划');
		navTab.openTab('RES_TEACHING_ESTAB_GRADE_COPY', '${baseUrl}/edu3/teaching/teachinggrade/copy.html', '复制年级教学计划');
	}
	
	/* function brschool_Major(){
		var unitId = $("#teachPlan_brSchoolid").val();
		var majorId = $("#teachPlan_major").val();
		var url = "${baseUrl}/edu3/teaching/teachinggrade/brschool_Major.html";
		$.ajax({
			type:'POST',
			url:url,
			data:{unitId:unitId,majorId:majorId},
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success: function(data){
				if(data['result'] == 300){
					if(undefined!=data['msg']){
						alertMsg.warn(data['msg']);
					}
			    }else{
			    	$("#teachPlan_major").replaceWith("<select  class=\"flexselect textInput\" id=teachPlan_major name=\"major\" tabindex=1 style=width:55% >"+data['majorOption']+"</select>");
			    	$("#teachPlan_major_flexselect").remove();
			    	$("#teachPlan_major_flexselect_dropdown").remove();
			    	$("#teachPlan_major").flexselect();
				}
			}
		}); 
		
	}*/
	//导出模板
	function exportTGradePlanTemplate(){
		var url = baseUrl+"/edu3/teaching/teachinggrade/exportTGradeTemplate.html";
		alertMsg.confirm("你确定要导出课表模版吗？",{
			okCall:function(){
				downloadFileByIframe(url,'tgradeIframe');
			}
		});
	}
	//跳转到导入窗口
	function inputTGradePlan(){
		$.pdialog.open(baseUrl+"/edu3/teaching/teachinggrade/inputTGradePlan.html", 'RES_TEACHING_ESTAB_GRADE_INPUTTGRADEPLAN', '年级教学计划导入', {width: 600, height: 360});
	}
	//修改教学计划
	function modifyTPlan(){
		var url = "${baseUrl}/edu3/teaching/teachingplan/edit.html";
		var obj = $("#tgradeBody input[@name='resourceid']:checked");

		if(obj.attr("title") == 'Y'){
			alertMsg.warn('该教学计划已使用，不能修改！');
			return false;
		}
		if(isCheckOnlyone('resourceid','#tgradeBody')){
			navTab.openTab('RES_TEACHING_ESTAB_PLAN_EDIT', url+'?resourceid='+$("#tgradeBody input[@name='resourceid']:checked").attr("planid"), '编辑教学计划');
		}			
	}
	
	function setDegreeForeignLanguage_select(){
		var url="${baseUrl}/edu3/teaching/teachingplan/select-setDegreeForeignLanguage.html";
		var bodyname = "#tgradeBody";
		if(!isChecked('resourceid',bodyname)){
			alertMsg.warn('请选择一条要操作记录！');
			return false;
		}
		var res =[];
		$(bodyname+" input[@name='resourceid']:checked").each(function(){	
			res.push($(this).val()+'');
		});
		$.pdialog.open(url+"?resourceid="+res.join(','), 'RES_TEACHING_SETDEGREEFOREIGNLANGUAGE', '设置学位外语课程', {mask:true,width: 300, height: 200});
	}
	
</script>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/teachinggrade/list.html"
				method="post">
				<input type="hidden" name="isShow" value="Y">
				<div class="searchBar">
					<ul class="searchContent">
						<li style="width: 360px;"><label>计划名称：</label><select id="planName" name="planName"
							class="flexselect" style="width: 240px;">${planOptions }</select>
						</li>
						<%-- <c:if test="${condition['isBrSchool'] ne 'Y' }">
				<li>
					<label>教学站：</label>
					<span sel-id="teachPlan_brSchoolid" sel-name="brSchoolid" sel-onchange="teachinggradeQueryUnit()" sel-classs="flexselect"  sel-style="width: 120px" ></span>
				</li>
				</c:if> --%>
						<c:if test="${condition['isBrSchool'] eq 'Y' }">
							<input type="hidden" id="teachPlan_brSchoolid" name="brSchoolid"
								value="${condition['brSchoolid']}" />
						</c:if>
						<li><label>年级：</label> <span sel-id="teachinggrade_stuGrade"
							sel-name="gradeid" sel-onchange="teachinggradeQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="teachinggrade_classic"
							sel-name="classic" sel-onchange="teachinggradeQueryClassic()"
							sel-style="width: 120px"></span></li>
						<c:if test="${empty condition['roleModules'] }">
							<li><label>办学模式：</label> <span sel-id="teachingType"
								sel-name="schoolType"
								sel-onchange="teachinggradeQueryTeachingType()"
								dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
							</li>
						</c:if>

					</ul>
					<ul class="searchContent">
						
						<li style="width: 360px;" id="teachinggrade-gradeToMajor4"><label>专业：</label> <span
							sel-id="teachinggrade_major" sel-name="major"
							sel-classs="flexselect" sel-style="width: 240px"></span></li>
						<li><label>是否发布：</label> <gh:select name="ispublished"
								value="${condition['ispublished']}" dictionaryCode="yesOrNo"
								style="width: 120px" /></li>

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
			<gh:resAuth parentCode="RES_TEACHING_ESTAB_GRADE" pageType="list"></gh:resAuth>
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_tg"
							onclick="checkboxAll('#check_all_tg','resourceid','#tgradeBody')" /></th>
						<th width="8%">年级</th>
						<th width="8%">办学模式</th>
						<th width="8%">层次</th>
						<th width="20%">专业</th>
						<th width="25%">计划名称</th>
						<th width="5%">学制</th>
						<th width="5%">是否发布</th>
						<th width="16">学位外语</th>
					</tr>
				</thead>
				<tbody id="tgradeBody">
					<c:forEach items="${guiPlanPage.result}" var="g" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${g.resourceid }" planid="${g.planid }"
								autocomplete="off" /></td>
							<td>${g.gradename }</td>
							<td>${ghfn:dictCode2Val('CodeTeachingType',g.schooltype) }</td>
							<td>${g.classicname }</td>
							<td>${g.majorname }</td>
							<td><a href="#"
								onclick="openGuiTeachplanView('${g.planid }')"> <c:choose>
										<c:when test="${not empty g.planName }">
			            			${g.planName }
			            			</c:when>
										<c:when test="${not empty g.unitName }">
				            			${g.unitName}${g.gradeName }${g.majorname }${g.classicname }
				            		</c:when>
										<c:otherwise>
				            			${g.majorname } - ${g.classicname } (${g.versionnum})
				            		</c:otherwise>
									</c:choose>

							</a></td>
							<td>${g.eduYear }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',g.ispublished) }</td>
							<td>${g.courseName }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${guiPlanPage}"
				goPageUrl="${baseUrl }/edu3/teaching/teachinggrade/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>