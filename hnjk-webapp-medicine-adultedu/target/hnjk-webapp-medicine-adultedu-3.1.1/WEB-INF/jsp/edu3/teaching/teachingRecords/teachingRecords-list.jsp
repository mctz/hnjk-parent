<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学记录</title>
<script type="text/javascript">
	$(document).ready(function(){
		teachRecordsQueryBegin();
	});
	//打开页面或者点击查询（即加载页面执行）
	function teachRecordsQueryBegin() {
		var defaultValue = "${condition['brSchoolId']}";
		var schoolId = "";
		var gradeId = "${condition['gradeId']}";
		var classicId = "${condition['classic']}";
		var teachingType = "${condition['teachRecords_teachingType']}";
		var majorId = "${condition['majorId']}";
		var selectIdsJson = "{unitId:'teachRecords_brSchoolId',gradeId:'teachRecords_gradeId',classicId:'teachRecords_classic',teachingType:'teachRecords_teachingType',majorId:'teachRecords_majorId'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, "", selectIdsJson);
	}
	
	// 选择教学点
	function teachRecordsQueryUnit() {
		var defaultValue = $("#teachRecords_brSchoolId").val();
		var selectIdsJson = "{gradeId:'teachRecords_gradeId',classicId:'teachRecords_classic',teachingType:'teachRecords_teachingType',majorId:'teachRecords_majorId'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}
	
	// 选择年级
	function teachRecordsQueryGrade() {
		var defaultValue = $("#teachRecords_brSchoolId").val();
		var gradeId = $("#teachRecords_gradeId").val();
		var selectIdsJson = "{classicId:'teachRecords_classic',teachingType:'teachRecords_teachingType',majorId:'teachRecords_majorId'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}
	
	// 选择层次
	function teachRecordsQueryClassic() {
		var defaultValue = $("#teachRecords_brSchoolId").val();
		var gradeId = $("#teachRecords_gradeId").val();
		var classicId = $("#teachRecords_classic").val();
		var selectIdsJson = "{teachingType:'teachRecords_teachingType',majorId:'teachRecords_majorId'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}
	
	//选择学习形式
	function teachRecordsQueryTeachingType() {
		var defaultValue = $("#teachRecords_brSchoolId").val();
		var gradeId = $("#teachRecords_gradeId").val();
		var classicId = $("#teachRecords_classic").val();
		var teachingType = $("#teachRecords_teachingType").val();
		var selectIdsJson = "{majorId:'teachRecords_majorId'}";
		cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingType, "", "", selectIdsJson);
	}
	//选择专业
	function teachRecordsQueryMajor() {
		var defaultValue = $("#teachRecords_brSchoolId").val();
		var gradeId = $("#teachRecords_gradeId").val();
		var classicId = $("#teachRecords_classic").val();
		var teachingType = $("#teachRecords_teachingType").val();
		var majorId = $("#teachRecords_majorId").val();
		var selectIdsJson = "{classesId:'stuchange_classesid'}";
		cascadeQuery("major", defaultValue, "", gradeId, classicId,"", majorId, "", selectIdsJson);
	}
	//下载模板
	function downloadModel(){
		window.location.href="${baseUrl }/edu3/teaching/teachingrecords/download-file.html"
	}
	
	//新增
	function addTeachRecords(){
		navTab.openTab('RES_TEACHING_TEACHINGRECORDS_INPUT', '${baseUrl}/edu3/teaching/teachingrecords/input.html', '新增教学记录');
	}
	
	//修改
	function modifyTeachRecords(){
		var url = "${baseUrl}/edu3/teaching/teachingrecords/input.html";
		if(isCheckOnlyone('resourceid','#teachingRecordsBody')){
			navTab.openTab('RES_TEACHING_TEACHINGRECORDS_INPUT', url+'?resourceid='+$("#teachingRecordsBody input[@name='resourceid']:checked").val(), '编辑教学记录');
		}else{
			alertMsg.warn("请选择一条记录进行操作！");
		}		
	}
	
	//删除
	function removeTeachRecords(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/teachingrecords/remove.html","#teachingRecordsBody");
	}
	
	//打印
	function printTeachRecords(){
		var url = getUrlByParam("printView");
		if(url!=''){
			$.pdialog.open(url,"RES_TEACHING_TEACHINGRECORDS_PRINT","打印预览",{width:800, height:600});
		}
	}
	
	//导出
	function exportTeachRecords(){
		$('#frame_exportExcel').remove();
		var iframe = document.createElement("iframe");
		iframe.id = "frame_exportExcel";
		iframe.src = getUrlByParam("export");
		//创建完成之后，添加到body中
		document.body.appendChild(iframe);
	}
	
	function getUrlByParam(flag){//flag：export;printView
		var url = "";
		var num = $("#teachingRecordsBody input[name='resourceid']:checked").size();
		if(isChecked('resourceid','#teachingRecordsBody')){
			var resIds = "";
			var k   = 0;
			$("#teachingRecordsBody  input[@name='resourceid']:checked").each(function(){
				resIds+=$(this).val();
		        if(k != num -1 ) resIds += ",";
		        k++;
		    });
			url = "${baseUrl}/edu3/teaching/teachingrecords/"+flag+".html?ids="+resIds+"&flag="+flag;
		}else{
			var brSchoolId    = $("#teachRecords_brSchoolId").val();
			var gradeId 	  = $("#teachRecords_gradeId").val();
			var majorId 	  = $("#teachRecords_majorId").val();
			var courseId	  = $("#teachRecords_courseId").val();	
			var term		  = $("#teachRecords_term").val();
			var teacherName   = $("#teachRecords_teacherName").val();
			var timePeriod   = $("#teachRecords_timePeriod").val();
			var timePeriod2   = $("#teachRecords_timePeriod2").val();
			//if(flag=='printView'){
				if(courseId=='' || gradeId=='' || majorId==''){
					alertMsg.warn("课程，年级，专业为必填项！")
					return "";
				}
			//}
			url =  "${baseUrl }/edu3/teaching/teachingrecords/"+flag+".html?brSchoolId="+brSchoolId
					+"&gradeId="+gradeId+"&majorId="+majorId+"&courseId="+courseId+"&term="+term
					+"&teacherName="+teacherName+"&timePeriod="+timePeriod+"&timePeriod2="+timePeriod2+"&flag="+flag;	
		}
		return url;
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="teachingRecordsForm" onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/teachingrecords/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label> <c:choose>
								<c:when test="${not isBrschool }">
									<span sel-id="teachRecords_brSchoolId" sel-name="brSchoolId"
										sel-onchange="teachRecordsQueryUnit()" sel-classs="flexselect"
										sel-style="width: 120px"></span>
								</c:when>
								<c:otherwise>
									<input type="hidden" value="${condition['brSchoolId']}"
										name="brSchoolId" id="teachRecords_brSchoolId">
									<input type="text" value="${brschoolName}" style="width: 120px"
										readonly="readonly">
								</c:otherwise>
							</c:choose></li>
						<li><label>年级：</label> <span sel-id="teachRecords_gradeId"
							sel-name="gradeId" sel-onchange="teachRecordsQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>专业：</label> <span sel-id="teachRecords_majorId"
							sel-name="majorId" sel-classs="flexselect"
							sel-style="width: 120px"></span></li>
						<li><label>课程名称：</label> <gh:courseAutocomplete
								name="courseId" tabindex="1" id="teachRecords_courseId"
								value="${condition['courseId']}" displayType="code"
								isFilterTeacher="Y" style="width:120px" /></li>
					</ul>
					<ul class="searchContent">
						<%-- <li>
					<label>学期：</label>
					<gh:select id="teachRecords_term" name="term" dictionaryCode="CodeTerm" value="${condition['term']}" style="width:120px" />
				</li>	 --%>
						<li><label>教师：</label> <input type="text"
							id="teachRecords_teacherName" name="teacherName"
							id="teachRecords_teacherName" value="${condition['teacherName']}"
							style="width: 120px" /></li>
						<li>日期： <input type="text" style="width: 70px"
							id="teachRecords_timePeriod" name="timePeriod" class="Wdate"
							value="${condition['timePeriod']}"
							onfocus="WdatePicker({dateFmt:'yyyy-MM'})" /> 至 <input
							type="text" style="width: 70px" id="teachRecords_timePeriod2"
							name="timePeriod2" class="Wdate"
							value="${condition['timePeriod2']}"
							onfocus="WdatePicker({dateFmt:'yyyy-MM'})" />
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
			<gh:resAuth parentCode="RES_TEACHING_TEACHINGRECORDS" pageType="list"></gh:resAuth>
			<table class="table" layouth="110" width="100%">
				<thead>
					<tr>
						<th style="text-align: center; vertical-align: middle;" width="3%"><input
							type="checkbox" name="checkall" id="check_all_teachingRecords"
							onclick="checkboxAll('#check_all_teachingRecords','resourceid','#teachingRecordsBody')" /></th>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">教学点</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">年级</th>
						<th width="8%" style="text-align: center; vertical-align: middle;">专业</th>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">课程</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">学时</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">教学手段</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">周次</th>
						<th width="8%" style="text-align: center; vertical-align: middle;">日期</th>
						<th width="8%" style="text-align: center; vertical-align: middle;">授课教师</th>
						<th width="8%" style="text-align: center; vertical-align: middle;">职称</th>
						<th width="8%" style="text-align: center; vertical-align: middle;">联系电话</th>
						<th width="8%" style="text-align: center; vertical-align: middle;">地点</th>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">理论教学内容</th>
					</tr>
				</thead>
				<tbody id="teachingRecordsBody">
					<c:forEach items="${page.result}" var="tr" varStatus="vs">
						<tr>
							<td style="text-align: center; vertical-align: middle;"><input
								type="checkbox" name="resourceid" value="${tr.resourceid}" /></td>
							<td style="text-align: center; vertical-align: middle;">${tr.unit.unitName }</td>
							<td style="text-align: center; vertical-align: middle;">${tr.grade.gradeName }</td>
							<td style="text-align: center; vertical-align: middle;">${tr.major.majorName }</td>
							<td style="text-align: center; vertical-align: middle;">${tr.planCourse.course.courseName }</td>
							<td style="text-align: center; vertical-align: middle;">${tr.planCourse.stydyHour }</td>
							<td style="text-align: center; vertical-align: middle;">${tr.teachType }</td>
							<td style="text-align: center; vertical-align: middle;">${tr.week }</td>
							<td style="text-align: center; vertical-align: middle;"><fmt:formatDate
									value="${tr.timeperiod }" pattern="yyyy年MM月" /></td>
							<td style="text-align: center; vertical-align: middle;">${tr.teacher.cnName }</td>
							<td style="text-align: center; vertical-align: middle;">${tr.teacher.titleOfTechnical }</td>
							<td style="text-align: center; vertical-align: middle;"><c:choose>
									<c:when test="${not empty tr.teacher.mobile }"> ${tr.teacher.mobile }</c:when>
									<c:otherwise>${tr.teacher.officeTel }</c:otherwise>
								</c:choose></td>
							<td style="text-align: center; vertical-align: middle;">${tr.classroom }</td>
							<td style="text-align: center; vertical-align: middle;">${tr.contents }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/teaching/teachingrecords/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>