<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业/结业审核记录</title>
<script type="text/javascript">
$(document).ready(function(){
	graduateAudHistoryQueryBegin();
});

//打开页面或者点击查询（即加载页面执行）
function graduateAudHistoryQueryBegin() {
	var defaultValue = "${condition['branchSchool']}";
	var schoolId = "";
	var isBrschool = "${isBrschool}";
	if(isBrschool==true || isBrschool=="true"){
		schoolId = defaultValue;
	}
	var gradeId = "${condition['grade']}";
	var classicId = "${condition['classic']}";
	var teachingType = "${condition['teachingType']}";
	var majorId = "${condition['major']}";
	var classesId = "${condition['classesId']}";
	var selectIdsJson = "{unitId:'branchSchool',gradeId:'grade',classicId:'classic',teachingType:'teachingType',majorId:'major',classesId:'graduateauditHistory_classid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function graduateAudHistoryQueryUnit() {
	var defaultValue = $("#branchSchool").val();
	var selectIdsJson = "{gradeId:'grade',classicId:'classic',teachingType:'teachingType',majorId:'major',classesId:'graduateauditHistory_classid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function graduateAudHistoryQueryGrade() {
	var defaultValue = $("#branchSchool").val();
	var gradeId = $("#grade").val();
	var selectIdsJson = "{classicId:'classic',teachingType:'teachingType',majorId:'major',classesId:'graduateauditHistory_classid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function graduateAudHistoryQueryClassic() {
	var defaultValue = $("#branchSchool").val();
	var gradeId = $("#grade").val();
	var classicId = $("#classic").val();
	var selectIdsJson = "{teachingType:'teachingType',majorId:'major',classesId:'graduateauditHistory_classid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择专业
function graduateAudHistoryQueryMajor() {
	var defaultValue = $("#branchSchool").val();
	var gradeId = $("#grade").val();
	var classicId = $("#classic").val();
	var teachingTypeId = $("#teachingType").val();
	var majorId = $("#major").val();
	var selectIdsJson = "{classesId:'graduateauditHistory_classid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}

//导出审核结果 [按班级]
function exportGraduationAuditByClasses(){
	var classId = $('#graduateauditHistory_classid').val();
	if('' == classId){
		alertMsg.info("请选择班级！");
		return false;
	}
	var auditStatus	   = $("#gAuditConfirm #auditStatus").val()==undefined?"":$('#gAuditConfirm #auditStatus').val();
	var other_auditStatus = $("#gAuditConfirm #other_auditStatus").val()==undefined?"":$('#gAuditConfirm #other_auditStatus').val();
	var confirmStatus = $("#gAuditConfirm #confirmStatus").val()==undefined?"":$('#gAuditConfirm #confirmStatus').val();
	$('#frame_exportGraduationAuditByClasses').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frame_exportGraduationAuditByClasses";
	iframe.src = "${baseUrl}/edu3/schoolroll/graduation/student/exportExcelByClasses.html?classId="+classId+"&auditStatus="+auditStatus+"&other_auditStatus="+other_auditStatus+"&confirmStatus="+confirmStatus;
	iframe.style.display = "none";
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
}

function exportGraduationAudit(){//导出审核结果(按查询条件导出)
	var branchSchool = $("#gAuditConfirm #branchSchool").val()==undefined?"":$('#gAuditConfirm #branchSchool').val();
	var major		       = $("#gAuditConfirm #major").val()==undefined?"":$('#gAuditConfirm #major').val();
	var classic			   = $("#gAuditConfirm #classic").val()==undefined?"":$('#gAuditConfirm #classic').val();
	var grade			   = $("#gAuditConfirm #grade").val()==undefined?"":$('#gAuditConfirm #grade').val();
	var name			   = $("#gAuditConfirm #name").val()==undefined?"":$('#gAuditConfirm #name').val();
	name = encodeURIComponent(name);
	var studyNo	       = $("#gAuditConfirm #studyNo").val()==undefined?"":$('#gAuditConfirm #studyNo').val();
	var auditStatus	   = $("#gAuditConfirm #auditStatus").val()==undefined?"":$('#gAuditConfirm #auditStatus').val();
	var confirmStatus = $("#gAuditConfirm #confirmStatus").val()==undefined?"":$('#gAuditConfirm #confirmStatus').val();
	var graduateAuditBeginTime = $("#graduateAuditBeginTime").val()==undefined?"":$('#graduateAuditBeginTime').val();//毕业审核时间起
	var graduateAuditEndTime = $("#graduateAuditEndTime").val()==undefined?"":$('#graduateAuditEndTime').val();//毕业审核时间终
	var classId = $('#graduateauditHistory_classid').val(); //班级
	$('#frame_exportGraduateAuditList').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frame_exportGraduateAuditList";
	iframe.src = "${baseUrl }/edu3/schoolroll/graduation/student/exportExcel_new.html?isSelectAll=1&checkHis=1&branchSchool="+branchSchool+"&major="+major+"&classic="+classic+"&name="+name+"&studyNo="+studyNo+"&grade="+grade+"&auditStatus="+auditStatus+"&confirmStatus="+confirmStatus
			+"&graduateAuditBeginTime="+graduateAuditBeginTime+"&graduateAuditEndTime="+graduateAuditEndTime+"&classId="+classId;
	iframe.style.display = "none";
	//创建完成之后，添加到body中
	alertMsg.confirm("按查询条件导出毕业审核记录吗？",{ 
		okCall:function(){
		document.body.appendChild(iframe);
		}
	});
	
}
/* 
function exportGraduationAudit2(){//导出审核结果(按勾选导出)
	var stus="";
	$("input[name='graudateAuditConfirmid']").each(function(){
		if($(this).attr("checked")){
			stus += ""+$(this).val()+",";
		}
	});
	if(""==stus){
		alertMsg.warn("请选择一条或多条记录！");return false;
	}else{
		$('#frame_exportGraduateAuditList2').remove();
		var iframe = document.createElement("iframe");
		iframe.id = "frame_exportGraduateAuditList2";
		iframe.src = "${baseUrl }/edu3/schoolroll/graduation/student/exportExcel.html?exportType=click&stus="+stus;
		iframe.style.display = "none";
		//创建完成之后，添加到body中
		document.body.appendChild(iframe);
	}
}

*/
//查看毕业审核数据
function viewStuGraduateAuditData(stuId){//查看
	$.pdialog.open('${baseUrl}/edu3/schoolroll/graduation/student/view.html?studentId='+stuId, 'RES_SCHOOL_GRADUATEAUDITDATA_VIEW', '查看毕业审核数据', {width: 800, height: 600});
}

/* //年级-专业 级联
function graduateAuditHistoryChangMajor(){
	var gradeId = $("#gAuditConfirm #grade").val(); //年级
	$.ajax({
		type:"post",
		url:"${baseUrl}/edu3/schoolroll/graduate/audit/list/grade-major/page1.html",
		data:{gradeId_page1:gradeId,id_page1:'graduateauditHistory_major',name_page1:'major',click_page1:'graduateAuditHistoryMajorClick()'},
		dataType:"json",
		success:function(data){
			$('#graduateudit-gradeToMajor1').html('<label>专业：</label>'+data['msg']);
			$("select[class*=flexselect]").flexselect();
			graduateAuditHistoryMajorClick();
		}
	});
 }
//年级-专业-班级 级联
function graduateAuditHistoryMajorClick(){
	var gradeId = $('#grade').val(); //年级
	var majorId = $('#graduateauditHistory_major').val(); //专业
	var brschoolId = $('#branchSchool').val(); //教学站
	$.ajax({
		type:"post",
		url:"${baseUrl}/edu3/schoolroll/graduate/audit/list/grade-major-classes/page1.html",
		data:{gradeId_page1:gradeId,majorId_page1:majorId,brschoolId_page1:brschoolId,id_page1:'graduateauditHistory_classid',name_page1:'classId'},
		dataType:"json",
		success:function(data){
			$('#graduateudit-gradeToMajorToClasses1').html('<label>班级：</label>'+data['msg']);
			$("select[class*=flexselect]").flexselect();
		}
	});
 } */
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader" style="height: 75px;">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/schoolroll/graduation/student/confirmPage.html"
				method="post">
				<div class="searchBar" id="gAuditConfirm">
					<ul class="searchContent">
						<li class="custom-li"><label>教学站：</label> <span sel-id="branchSchool"
							sel-name="branchSchool"
							sel-onchange="graduateAudHistoryQueryUnit()"
							sel-classs="flexselect"></span></li>
						<li><label>年级：</label> <span sel-id="grade" sel-name="grade"
							sel-onchange="graduateAudHistoryQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="classic"
							sel-name="classic"
							sel-onchange="graduateAudHistoryQueryClassic()"
							displayValue="classicName" sel-style="width: 120px"></span></li>
						<li><label>毕业审核状态：</label><select id="auditStatus"
							name="auditStatus" style="width: 100px">
								<option value="">请选择</option>
								<option
									<c:if test="${condition['auditStatus']==1}">selected="selected"</c:if>
									value="1">通过</option>
								<option
									<c:if test="${condition['auditStatus']==0}">selected="selected"</c:if>
									value="0">不通过</option>
						</select></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span sel-id="major" sel-name="major"
							sel-onchange="graduateAudHistoryQueryMajor()"
							sel-classs="flexselect" ></span></li>
						
						<li><label>姓名：</label><input type="text" id="name"
							name="name" value="${condition['name']}" style="width: 115px" />
						</li>
						<li><label>学号：</label><input type="text" id="studyNo"
							name="studyNo" value="${condition['studyNo']}"
							style="width: 115px" /></li>
						<li><label>结业审核状态：</label><select id="other_auditStatus"
							name="other_auditStatus" style="width: 100px">
								<option value="">请选择</option>
								<option
									<c:if test="${condition['other_auditStatus']==1}">selected="selected"</c:if>
									value="1">通过</option>
								<option
									<c:if test="${condition['other_auditStatus']==0}">selected="selected"</c:if>
									value="0">不通过</option>
						</select></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span
							sel-id="graduateauditHistory_classid" sel-name="classesId"
							sel-classs="flexselect" ></span> <%--<gh:classesAutocomplete name="classId" id="graduateauditHistory_classid" tabindex="1" displayType="code" defaultValue="${condition['classesId']}"></gh:classesAutocomplete> --%>
						</li>
						<!-- </ul>
			<ul class="searchContent"> -->
						<li style="width: 480px"><label>审核时间：</label><input
							type="text" name="graduateAuditBeginTime" style="width: 80px"
							value="${condition['graduateAuditBeginTime']}"
							class="Wdate date1" id="graduateAuditBeginTime"
							onFocus="WdatePicker({isShowWeek:true,maxDate:'#F{$dp.$D(\'graduateAuditEndTime\')}'})" />

							到 <input type="text" name="graduateAuditEndTime"
							style="width: 80px" value="${condition['graduateAuditEndTime']}"
							class="Wdate date1" id="graduateAuditEndTime"
							onFocus="WdatePicker({isShowWeek:true,minDate:'#F{$dp.$D(\'graduateAuditBeginTime\')}'})" />
						</li>
						<li><label>确认状态：</label><select id="confirmStatus"
							name="confirmStatus" style="width: 100px">
								<option value="">请选择</option>
								<option
									<c:if test="${condition['confirmStatus']==1}">selected="selected"</c:if>
									value="1">已确认</option>
								<option
									<c:if test="${condition['confirmStatus']==0}">selected="selected"</c:if>
									value="0">未确认</option>
						</select></li>

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

			<gh:resAuth parentCode="RES_SCHOOL_GRADUATION_AUDITCONFIRM"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="163">
				<thead>
					<tr>
						<!-- <th width="5%"><input type="checkbox" name="checkall" id="check_all_qualif" onclick="checkboxAll('#check_all_qualif','graudateAuditConfirmid','#confirmGraduateAuditBody')"/></th> -->
						<th width="8%">学号</th>
						<th width="5%">姓名</th>
						<th width="12%">班级</th>
						<th width="4%">确认</th>
						<th width="8%">审核状态</th>
						<th width="43%">原因</th>
						<th width="10%">毕业审核时间</th>


					</tr>
				</thead>
				<tbody id="confirmGraduateAuditBody">
					<c:forEach items="${graduationAuditList.result}"
						var="graudateAuditConfirm" varStatus="vs">
						<tr>
							<!--<td><c:if test="${graudateAuditConfirm.graduateAuditStatus=='1'&&fn:split(graudateAuditConfirm.graduateAuditMemo,'|')[2]=='#0'}"><input type="checkbox" name="graudateAuditConfirmid" value="${graudateAuditConfirm.studentInfo.resourceid }" autocomplete="off" /></c:if></td>-->
							<td rowspan="2" style="padding-top: 13px">${graudateAuditConfirm.studentInfo.studyNo}</td>
							<td rowspan="2" style="padding-top: 13px"><a href="#"
								onclick="viewStuGraduateAuditData('${graudateAuditConfirm.studentInfo.resourceid}')"
								title="点击查看">${graudateAuditConfirm.studentInfo.studentName}</a>
							</td>
							<td rowspan="2" style="padding-top: 13px">${graudateAuditConfirm.studentInfo.classes.classname}</td>
							<td rowspan="2" style="padding-top: 13px"><c:if
									test="${graudateAuditConfirm.comfirm eq '1'}">
									<font color="blue">已确认</font>
								</c:if> <c:if test="${graudateAuditConfirm.comfirm ne '1'}">
									<font color="red">未确认</font>
								</c:if></td>
							<td>[毕业]<c:if
									test="${graudateAuditConfirm.graduateAuditStatus=='1'}">
									<font color="blue">审核通过</font>
								</c:if> <c:if test="${graudateAuditConfirm.graduateAuditStatus=='0'}">
									<font color="red">审核不通过</font>
								</c:if></td>
							<td>[毕业]${fn:split(graudateAuditConfirm.graduateAuditMemo,'|')[0]}</td>
							<td rowspan="2" style="padding-top: 13px"><fmt:formatDate
									value="${graudateAuditConfirm.auditTime}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
						<tr>
							<td>[结业]<c:if
									test="${graudateAuditConfirm.theGraduationStatis=='1'}">
									<font color="blue">审核通过</font>
								</c:if> <c:if test="${graudateAuditConfirm.theGraduationStatis=='0'}">
									<font color="red">审核不通过</font>
								</c:if></td>
							<td>[结业]${fn:split(graudateAuditConfirm.theGraduationMemo,'|')[0] eq 'null' ? '' : fn:split(graudateAuditConfirm.theGraduationMemo,'|')[0]}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<%-- <gh:page page="${graduationAuditList}"
				goPageUrl="${baseUrl }/edu3/schoolroll/graduation/student/confirmPage.html"
				pageType="sys" condition="${condition}" /> --%>
		</div>
		<div  class="pageContent" style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${graduationAuditList}"
				goPageUrl="${baseUrl }/edu3/schoolroll/graduation/student/confirmPage.html"
				pageType="sys" condition="${condition}" /></div>
	</div>
	<input type="hidden" id="pNum" value="${condition['pNum']}" />
</body>

</html>