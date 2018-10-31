<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>编辑教学意愿</title>
<script language="javascript">
$(document).ready(function(){
	if("${user}" == null){
		alertMsg.warn("获取当前用户信息失败！");
	}
	 if("${willingness.info}" != null){
		$("#willingness_applyInfo").val("${willingness.info}");
	}
	$('#willingnessForm select[id=timePeriod]').multiselect2side({
			selectedPosition: 'right',
			moveOptions: false,
			labelsx: '',
			labeldx: ''
	});
	//回显数据
	  var checkeds = "${willingness.days}";
	  var checkArray =checkeds.split(",");
	  var checkBoxAll = $("input[name='day']");
	  for(var i=0;i<checkArray.length;i++){
	   $.each(checkBoxAll,function(j,checkbox){
			var checkValue=$(checkbox).val();
			if(checkArray[i]==checkValue){
				$(checkbox).attr("checked",true);
			}
	   })
	  }
	//上课时间
	$("#willingness_timePeriodL").html("${timePeriodOption}");
	$("#willingness_timePeriodR").html("");
	//将选中的option移到右边
	var leftSel = $("#willingness_timePeriodL");
	var rightSel = $("#willingness_timePeriodR");
	leftSel.find("option:selected").each(function(){
		$(this).remove().appendTo(rightSel);
	});
});
$(function(){//时间段
	var leftSel = $("#willingness_timePeriodL");
	var rightSel = $("#willingness_timePeriodR");
	$("#toright").bind("click",function(){		
		leftSel.find("option:selected").each(function(){
			$(this).remove().appendTo(rightSel);
		});
	});
	$("#toleft").bind("click",function(){		
		rightSel.find("option:selected").each(function(){
			$(this).remove().appendTo(leftSel);
		});
	});
	leftSel.dblclick(function(){
		$(this).find("option:selected").each(function(){
			$(this).remove().appendTo(rightSel);
		});
	});
	rightSel.dblclick(function(){
		$(this).find("option:selected").each(function(){
			$(this).remove().appendTo(leftSel);
		});
	});
});
//选择任课老师
function setTeach(){//record  lecturer 
	var days = [];
	$("#willingnessForm input[name='day']:checked").each(function(i,n) {
		days.push(n.value);
	});
	var weeks = [];
	$("#willingnessForm input[name='week']:checked").each(function(i,n) {
		weeks.push(n.value);
	});
	var timePeriod = []//上课时间
	var select = document.getElementById("willingness_timePeriodR"); 
    for(i=0;i<select.length;i++){ 
    	timePeriod.push(select[i].value); 
    }
	var willid = "${willingness.resourceid}";
	var teachCourseid = $("#willingness_teachCourseid").val();
	var userid = "${user.resourceid}";
	var userRole = "${userRole}";
	var branchSchool = $("#willingness_branchSchool").val();
	var classroomType = $("#willingness_classroomType").val();
	var applyInfo = encodeURI($("#willingness_applyInfo").val());
	var teacherids = $("#willingness_teacherids").val();
	var teacherNames = encodeURI($("#willingness_willTeacherName").val());
	var url = "${baseUrl}/edu3/arrange/teachingWillingness/selectteacher.html";
	$.pdialog.open(url+"?resId="+willid+'&teachCourseid='+teachCourseid+'&userid='+userid+'&userRole='+userRole+"&branchSchool="+branchSchool+"&teacherids="+teacherids+"&teacherNames="+teacherNames+"&classroomType="+classroomType+"&applyInfo="+applyInfo+"&teachtype=lecturer"+"&days="+days+"&timePeriod="+timePeriod,"applySelectTeacher","选择老师",{height:600,width:800});
}
//更新form表单
function queryTeachCourse() {
	var teachCourseid = $("#willingness_teachingClassname").val();
	if(teachCourseid!=0){
		$.ajax({
			type:"post",
			url:"${baseUrl}/edu3/arrange/teachingWillingness/queryTeachCourse.html",
			data:{"teachCourseid":teachCourseid},
			dataType:"json",
			success:function(data){
					if(data.statusCode == 200){
						$.pdialog.closeCurrent();
						$("#willingness_timePeriodL").html(data.timePeriodOption);
						$("#willingness_timePeriodR").html("");
						$("#willingness_teachingClassname").val(data.teachCourse.teachingClassname);
						$("#willingness_teachingCode").val(data.teachCourse.teachingCode);
						$("#willingness_courseName").val(data.teachCourse.courseName);
						$("#willingness_openTerm").val(data.teachCourse.openTerm);
						$("#willingness_classic").val(data.teachCourse.classic.classicName);
						
						$("#willingness_teachingtype").val(data.teachingtype);
						$("#willingness_studyHour").val(data.studyHour);
						$("#willingness_examClassType").val(data.examClassType);
						$("#willingness_status").val(data.status);
						$("#willingness_publishStatus").val(data.publishStatus);
						$("#willingness_classNames").val(data.classNames);
						//将选中的option移到右边
						var leftSel = $("#willingness_timePeriodL");
						var rightSel = $("#willingness_timePeriodR");
						leftSel.find("option:selected").each(function(){
							$(this).remove().appendTo(rightSel);
						});
					} else {
						alertMsg.error(data.message);
					}
			}
		});
	}
}
function dialogAjaxDone1(json){//排课页面，设置成功后，直接对话框及TAB，返回到选课
	DWZ.ajaxDone(json);
	if (json.statusCode == 200){
		//$.pdialog.closeCurrent();
		if (json.navTabId){
			navTab.closeCurrentTab();
		}
		if(json.isEdit == 'Y'){
			navTab.reload("${baseUrl}/edu3/arrange/teachingWillingness/list.html", {}, "RES_ARRANGE_WILLINGNESS_LIST");
		}else{
			navTab.reload("${baseUrl}/edu3/arrange/selectcourseresult/list.html", {},"RES_ARRANGE_SELECTCOURSE_RESULR_LIST")
		}
	}
}
</script>
</head>
<body>
	<div class="page">
	<div class="pageContent">
	<form method="post" action="${baseUrl}/edu3/arrange/teachingWillingness/save.html" class="pageForm" onsubmit="return validateCallback(this,dialogAjaxDone1);" id="willingnessForm">
		<input type="hidden" id="willingness_teachCourseid" name="teachCourseid" value="${willingness.teachCourse.resourceid }"/>
		<input type="hidden" id="willingness_branchSchool" name="branchSchool" value="${brSchoolid}"/>
		<input type="hidden" id="willingness_resIds" name="resId" value="${willingness.resourceid}" >
		<c:choose>
		<c:when test="${empty willingness.proposer }">
			<input type="hidden" id="willingness_teacherids" name="teacherids" value="${teacherids}" >
		</c:when>
		<c:otherwise>
			<input type="hidden" id="willingness_teacherids" name="teacherids" value="${willingness.proposer.resourceid}" >
		</c:otherwise>
		</c:choose>
		<input type="hidden" name="userid" value="${user.resourceid }"/>  
		<div layoutH="36">
			<table class="form" id="saveApplyTable">
				<tr>
					<td width="20%">教学班名：</td>
					<c:choose>
						<c:when test="${not empty willingness.teachCourse }">
							<td width="30%"><input type="text" id="willingness_teachingClassname" name="teachingClassname" style="width:50%" value="${willingness.teachCourse.teachingClassname}" readOnly="true"/></td>
						</c:when>
						<c:otherwise>
							<td width="30%"><select class="flexselect" id="willingness_teachingClassname" name="teachingClassname" style="width:50%;" onchange="queryTeachCourse()">${option}</select></td>
						</c:otherwise>
					</c:choose>
					<td width="20%">教学班号：</td>
					<td width="30%"><input type="text" id="willingness_teachingCode" name="teachingCode" style="width:50%" value="${willingness.teachCourse.teachingCode }" readOnly="true"/></td>
				</tr>
				<tr>
					<td width="20%">课程名称：</td>
					<td width="30%"><input type="text" id="willingness_courseName" name="courseName" style="width:50%" value="${willingness.teachCourse.courseName}" readOnly="true"/></td>
					
					<td width="20%">上课学期：</td>
					<td width="30%"><gh:select id="willingness_openTerm" name="openTerm" value="${willingness.teachCourse.openTerm }" dictionaryCode="CodeCourseTermType" style="width:50%" disabled="disabled"/></td>
					<%-- <td width="30%"><input type="text" class="required" name="openTerm" style="width:50%" value="${teachCourse.openTerm }" /></td> --%>
				</tr>
				<tr>
					<td width="20%">层次：</td>
					<td width="30%"><input type="text" id="willingness_classic" name="classic" style="width:50%" value="${(empty willingness.teachCourse.classic)?'':willingness.teachCourse.classic.classicName}" readOnly="true"/></td>
					
					<td width="20%">学习形式：</td>
					<td width="30%"><input id="willingness_teachingtype" name="teachingtype" value="${ghfn:dictCode2Val('CodeTeachingType',willingness.teachCourse.teachingtype) }" style="width:50%"  readOnly="true"/></td>
				</tr>
				<tr>
					<td width="20%">学时：</td>
					<td width="30%"><input type="text" id="willingness_studyHour" name="studyHour" style="width:50%" value="${willingness.teachCourse.studyHour}" readOnly="true"/></td>
					
					<td width="20%">考核形式：</td>
					<td width="30%"><input type="text" id="willingness_examClassType" name="examClassType" style="width:50%" value="${ghfn:dictCode2Val('CodeCourseExamType',willingness.teachCourse.examClassType) }" readOnly="true"/></td>
				</tr>
				<tr>
					<td width="20%">合班状态：</td>
					<td width="30%"><input type="text" id="willingness_status" name="status" style="width:50%" value="${ghfn:dictCode2Val('CodeTeachClassesStatus',willingness.teachCourse.status) }" readOnly="true"/></td>
					
					<td width="20%">发布状态：</td>
					<td width="30%"><input type="text" id="willingness_publishStatus" name="publishStatus" style="width:50%" value="${ghfn:dictCode2Val('CodePublishStatus',willingness.teachCourse.publishStatus) }" readOnly="true"/></td>
				</tr>
				<%-- <tr>
					<td width="20%">创建人：</td>
					<td width="30%"><input type="text" class="required" name="operatorName" style="width:50%" value="${teachCourse.operatorName}" readOnly="true"/></td>
					
					<td width="20%">创建时间：</td>
					<td width="30%"><input type="text" name="createDate" style="width:50%" value="<fmt:formatDate value="${teachCourse.createDate }" pattern="yyyy-MM-dd" />" class="required date1" readOnly="true"
								onFocus="WdatePicker({isShowWeek:true})"/></td>
				</tr> --%>
				<tr>
					<td width="20%">班级信息：</td>
					<td width="80%" colspan="3"><input type="text" id="willingness_classNames" name="classNames" style="width:70%" value="${willingness.teachCourse.classNames}" readOnly="true"/></td>
					
					<!-- <td width="20%">备注：</td>
					<td width="30%"><input type="text" name="memo" style="width:50%" value="" /></td> -->
				</tr>
				<tr>
					<td width="20%">星期：</td>
					<td colspan="3">
						<input name="day" type="checkbox"  value="1"/>星期一
						<input name="day" type="checkbox"  value="2"/>星期二
						<input name="day" type="checkbox"  value="3"/>星期三
						<input name="day" type="checkbox"  value="4"/>星期四
						<input name="day" type="checkbox"  value="5"/>星期五
						<input name="day" type="checkbox"  value="6"/>星期六
						<input name="day" type="checkbox"  value="7"/>星期日
					</td>
				</tr>
				<tr>
					<td>上课时间</td>
					<td style="width:80%" colspan="3">
					<div>
						<select id="willingness_timePeriodL" name="timePeriodL" size="6" multiple='multiple' style="width: 30%;float: left;"></select>
						<div style="float: left;margin-left: 8px;margin-top: 20px;height: 100%;width: 40px"> 
        					<input id="toright" type="button" style="margin-top:5px" title="添加" value=">>">
        					<input id="toleft" type="button" style="margin-top:5px" title="移除" value="<<">
    					 </div>
    					 <select id="willingness_timePeriodR" name="timePeriod" size="6" multiple="multiple" style="width: 30%;float: left;" ></select>
					</div>
					</td>
				</tr>
				<tr>
					<td width="20%">申请人：</td>
					<td width="30%">
					<c:if test="${userRole eq 'teacher'}"><input type="text" name="teacherNames" style="width:50%" value="${user.cnName }" readonly="readonly" /></c:if>
					<c:if test="${userRole eq 'jwy'}"><input type="text" id="willingness_willTeacherName" name="teacherNames" style="width:50%" value="${teacherNames }" readonly="readonly" class="required"/>
					<label></label><button type="button" onclick="setTeach();">选择老师</button> </c:if>
					</td>
					<td width="20%">教室类型：</td>
					<td width="30%"><gh:select id="willingness_classroomType" name="classroomType" value="${willingness.classroomType }" dictionaryCode="CodeClassRoomStyle" style="width:50%" /></td>
				</tr>
				<tr>
					<td width="20%" rowspan="2" >意愿信息：</td>
					<td width="80%" colspan="3" rowspan="2"><textarea id="willingness_applyInfo" name="info" value="${willingness.info }" style="width:70%" rows="2" /></td>
				</tr>
			</table>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent">
					<button type="submit">保存</button>
					</div></div></li>
					<li><div class="button"><div class="buttonContent"><button type="button" class="close" onclick="navTab.closeCurrentTab();">取消</button></div></div></li>
			</ul>
		</div>
	</form>
	</div>
	</div>	
</body>
</html>