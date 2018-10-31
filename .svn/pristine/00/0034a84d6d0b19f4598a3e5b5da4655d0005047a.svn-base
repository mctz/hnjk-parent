<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>按教学计划开课</title>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		openCourseGplanQueryBegin();
	});
	
	//打开页面或者点击查询（即加载页面执行）
	function openCourseGplanQueryBegin() {
		var defaultValue = "${condition['brSchoolid']}";
		var schoolId = "${defaultSchoolId}";
		var gradeId = "${condition['gradeid']}";
		var classicId = "${condition['classic']}";
		var teachingType = "${condition['schoolType']}";
		var majorId = "${condition['major']}";
		var classesId = "";
		var selectIdsJson = "{unitId:'openCourseGP_brSchoolid',gradeId:'openCourseGP_stuGrade',classicId:'openCourseGP_classic',teachingType:'openCourseGP_teachingType',majorId:'openCourseGP_major'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
	}

	// 选择教学点
	function openCourseGplanQueryUnit() {
		var defaultValue = $("#openCourseGP_brSchoolid").val();
		var selectIdsJson = "{gradeId:'openCourseGP_stuGrade',classicId:'openCourseGP_classic',teachingType:'openCourseGP_teachingType',majorId:'openCourseGP_major'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择年级
	function openCourseGplanQueryGrade() {
		var defaultValue = $("#openCourseGP_brSchoolid").val();
		var gradeId = $("#openCourseGP_stuGrade").val();
		var selectIdsJson = "{classicId:'openCourseGP_classic',teachingType:'openCourseGP_teachingType',majorId:'openCourseGP_major'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function openCourseGplanQueryClassic() {
		var defaultValue = $("#openCourseGP_brSchoolid").val();
		var gradeId = $("#openCourseGP_stuGrade").val();
		var classicId = $("#openCourseGP_classic").val();
		var selectIdsJson = "{teachingType:'openCourseGP_teachingType',majorId:'openCourseGP_major'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}

	// 选择学习形式
	function openCourseGplanQueryTeachingType() {
		var defaultValue = $("#openCourseGP_brSchoolid").val();
		var gradeId = $("#openCourseGP_stuGrade").val();
		var classicId = $("#openCourseGP_classic").val();
		var teachingTypeId = $("#openCourseGP_teachingType").val();
		var selectIdsJson = "{majorId:'openCourseGP_major'}";
		cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
	}

	
	function exportGplanOpenCourse(){
		if(isCheckOnlyone('resourceid','#openCourseGPBody')){
			var url = baseUrl+"/edu3/teaching/teachinggrade/export.html?teachingGuidePlanId="+$("#openCourseGPBody input[@name='resourceid']:checked").val()
			downloadFileByIframe(url,"openCourseGPIframe");
		}	
	}	
	
	//按教学计划建议学期开课
	function openCourseBySuggestTerm(){
		var guiplanIds = new Array();
		var isPublised = true;
		var url = baseUrl+"/edu3/teaching/teachinggrade/openCourseBySuggestTerm.html";
		$("#openCourseGPBody input[name='resourceid']:checked").each(function(){
			if($(this).attr("isPublished")!='Y'){
				isPublised = false;
			}
			guiplanIds.push($(this).val());
		});
		
		if(guiplanIds.length < 1){
			alertMsg.warn('请选择一条要操作记录！');
			return false;
		}
		if(!isPublised){
			alertMsg.warn('请选择<font color=red>已发布</font>的教学计划！');
			return false;
		}
		alertMsg.confirm("您确定按该教学计划开课吗？", {
			okCall:function(){
				$.ajax({
					type:'POST',
					url:url,
					data:{guiplanIds:guiplanIds.toString()},
					dataType:"json",
					cache: false,
					error: DWZ.ajaxError,
					success: function(data){
						if(data['resultCode'] == 300){
							alertMsg.error(data['msg']);
					    }else{
					    	if(data['isDerectOpen']=='N'){
					    		var selectSchoolUrl = baseUrl+"/edu3/teaching/teachinggrade/openCourse-selectSchool.html?guiplanIds="+guiplanIds;
					    		$.pdialog.open(selectSchoolUrl, 'RES_TEACHING_ESTAB_GRADE_SELECTSCHOOL', '选择教学点', {mask:true,width: 300, height: 200});
					    	} else {
					    		alertMsg.correct(data['msg']);
					    	}
						}
					}
				});
			}});
	}
	
	//浏览
	function openCourseGplanView(pid){
	$.pdialog.open(baseUrl+'/edu3/teaching/teachingplan/edit.html?resourceid='+pid+'&act=view',
					'selector',
					'查看教学计划',
					{width:800,height:600}
					);
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/teachinggrade/openCourseList.html"
				method="post">
				<input type="hidden" name="isShow" value="Y">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>计划名称：</label><select id="planName" name="planName"
							class="flexselect" style="width: 140px;">${planOptions }</select>
						</li>
						<%-- <c:if test="${condition['isBrSchool'] ne 'Y' }"> --%>
							<li style="width: 360px;"><label>教学站：</label> <span
								sel-id="openCourseGP_brSchoolid" sel-name="brSchoolid"
								sel-onchange="openCourseGplanQueryUnit()"
								sel-classs="flexselect required" sel-style="width: 240px"></span>
							</li>
						<%-- </c:if> --%>
						<%-- <c:if test="${condition['isBrSchool'] eq 'Y' }">
							<input type="hidden" id="openCourseGP_brSchoolid"
								name="brSchoolid" value="${condition['brSchoolid']}" />
						</c:if> --%>
						
						<li><label>年级：</label> <span sel-id="openCourseGP_stuGrade"
							sel-name="gradeid" sel-onchange="openCourseGplanQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="openCourseGP_classic"
							sel-name="classic" sel-onchange="openCourseGplanQueryClassic()"
							sel-style="width: 120px"></span></li>

					</ul>
					<ul class="searchContent">
						<c:if test="${empty condition['roleModules'] }">
							<li><label>办学模式：</label> <span
								sel-id="openCourseGP_teachingType" sel-name="schoolType"
								sel-onchange="openCourseGplanQueryTeachingType()"
								dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
							</li>
						</c:if>
						<li style="width: 360px;"><label>专业：</label> <span sel-id="openCourseGP_major"
							sel-name="major" sel-classs="flexselect" ></span>
						</li>
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
			<gh:resAuth parentCode="RES_TEACHING_OPENCOURSE_BYGPLAN"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_openCourseGP"
							onclick="checkboxAll('#check_all_openCourseGP','resourceid','#openCourseGPBody')" /></th>
						<th width="15%">教学点</th>
						<th width="10%">年级</th>
						<th width="10%">层次</th>
						<th width="10%">办学模式</th>
						<th width="15%">专业</th>
						<th width="20%">计划名称</th>
						<th width="5%">学制</th>
						<th width="5%">是否发布</th>
						<th width="5">学位外语</th>
					</tr>
				</thead>
				<tbody id="openCourseGPBody">
					<c:forEach items="${OCPage.result}" var="g" varStatus="vs">
						<c:if test="${g.stuNumber ne '0' }">
							<tr>
								<td><input type="checkbox" name="resourceid"
									value="${g.resourceid }" planid="${g.planid }"
									isPublished="${g.ispublished }" autocomplete="off" /></td>
								<td>${g.unitName }</td>
								<td>${g.gradename }</td>
								<td>${g.classicname }</td>
								<td>${ghfn:dictCode2Val('CodeTeachingType',g.teachingtype) }</td>
								<td>${g.majorname }</td>
								<td><a href="#"
									onclick="openCourseGplanView('${g.planid }')"> <c:choose>
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
						</c:if>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${OCPage}"
				goPageUrl="${baseUrl }/edu3/teaching/teachinggrade/openCourseList.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>