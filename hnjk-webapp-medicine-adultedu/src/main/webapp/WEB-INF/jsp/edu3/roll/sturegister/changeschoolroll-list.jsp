<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学籍信息审核</title>
<script type="text/javascript">
$(document).ready(function(){
	/* var studentStatusSet= '${stuStatusSet}';
	var statusRes= '${stuStatusRes}';
	orgStuStatus("#stuInfo #stuStatus",studentStatusSet,statusRes,"12,13,15,18,21,19,23,a11,b11");
	$("#changeschool_brSchoolName").flexselect({
		  specialevent:function(){brschool_Major();}
	});
	
	brschool_Major();
	$("#changeschool_classesid").flexselect();
	$("#changeschool_major").flexselect(); */
	changeschoolrollQueryBegin();
}); 

//打开页面或者点击查询（即加载页面执行）
function changeschoolrollQueryBegin() {
	var defaultValue = "${condition['branchSchool']}";
	var schoolId = "";
	var isBrschool = "${isBrschool}";
	if(isBrschool==true || isBrschool=="true"){
		schoolId = defaultValue;
	}
	var gradeId = "${condition['stuGrade']}";
	var classicId = "${condition['classic']}";
	var teachingType = "";
	var majorId = "${condition['major']}";
	var classesId = "${condition['classesid']}";
	var selectIdsJson = "{unitId:'changeschool_brSchoolName',gradeId:'changeschool_stuGrade',classicId:'changeschool_classic',majorId:'changeschool_major',classesId:'changeschool_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function changeschoolrollQueryUnit() {
	var defaultValue = $("#changeschool_brSchoolName").val();
	var selectIdsJson = "{gradeId:'changeschool_stuGrade',classicId:'changeschool_classic',majorId:'changeschool_major',classesId:'changeschool_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function changeschoolrollQueryGrade() {
	var defaultValue = $("#changeschool_brSchoolName").val();
	var gradeId = $("#changeschool_stuGrade").val();
	var selectIdsJson = "{classicId:'changeschool_classic',majorId:'changeschool_major',classesId:'changeschool_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function changeschoolrollQueryClassic() {
	var defaultValue = $("#changeschool_brSchoolName").val();
	var gradeId = $("#changeschool_stuGrade").val();
	var classicId = $("#changeschool_classic").val();
	var selectIdsJson = "{majorId:'changeschool_major',classesId:'changeschool_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择专业
function changeschoolrollQueryMajor() {
	var defaultValue = $("#changeschool_brSchoolName").val();
	var gradeId = $("#changeschool_stuGrade").val();
	var classicId = $("#changeschool_classic").val();
	
	var majorId = $("#changeschool_major").val();
	var selectIdsJson = "{classesId:'changeschool_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,"", majorId, "", selectIdsJson);
}
/*
function brschool_Major(){
	var unitId = $("#changeschool_brSchoolName").val();
	var majorId = $("#changeschool_major").val();
	var classicId = $("#changeschool_classic").val();
	var gradeId   = $("#changeschool_stuGrade").val();
	var classesId = $("#changeschool_classesid").val();//$("#classesId").val();
	var flag = "unitor";
	var url = "${baseUrl}/edu3/register/studentinfo/brschool_Major.html";
	$.ajax({
		type:'POST',
		url:url,
		data:{unitId:unitId,majorId:majorId,classicId:classicId,gradeId:gradeId,classesId:classesId,flag:flag},
		dataType:"json",
		cache: false,
		error: DWZ.ajaxError,
		success: function(data){
			if(data['result'] == 300){
				if(undefined!=data['msg']){
					alertMsg.warn(data['msg']);
				}
		    }else{
		    	//$("#major").replaceWith("<select id=\"major\" style=\"width:120px\" name=\"major\" onchange=\"brschool_Major()\">"+data['majorOption']+"</select>");
		    	$("#major").replaceWith("<select  class=\"flexselect textInput\" id=changeschool_major name=\"major\" tabindex=1 style=width:120px; onchange='brschool_Major()' >"+data['majorOption']+"</select>");
			    $("#major_flexselect").remove();
			    $("#major_flexselect_dropdown").remove();
			    $("#major").flexselect();
		    	$("#stuClasses").replaceWith("<select id=\"changeschool_classesid\" style=\"width: 120px\" name=\"classesid\">"+data['classesOption']+"</select>");
			}
		}
	});
	
}*/
//组合学籍状态的方法(参数为:空白的select控件,原始的组合学籍状态集合,上次查询选择的值,过滤得到的学籍状态)
function orgStuStatus(selectid,studentStatusSet,statusRes,val){
	var html = "<option value=''>请选择</option>";	
	var status= studentStatusSet.split(",");
	var filter = val.split(",");
	for(var i=0;i<(status.length-1)/2;i++){
		for(var j=0;j<filter.length;j++){
			if(filter[j]==status[2*i]){
				if(statusRes==status[2*i]){
					html += "<option selected='selected' value='"+status[2*i]+"'>"+status[2*i+1]+"</option>";
				}else{
					html += "<option value='"+status[2*i]+"'>"+status[2*i+1]+"</option>";
				}
			}
		}
	}
	$(selectid).html(html);
} 

function backStudentCardInfo(){//	退回已提交学籍卡
	if(isCheckOnlyone('resourceid','#infoBody1')){
		var obj = $("#infoBody1 input[@name='resourceid']:checked");
		var studentid = obj.val();
		var username = obj.attr("accept");
		pageBarHandle("您确定要退回"+username+"的学籍信息吗？","${baseUrl}/edu3/register/studentinfo/studentcardinfoback.html?studentid="+studentid,"#infoBody1");
	}else if(isChecked('resourceid','#infoBody1')){
		pageBarHandle("您确定要退回学籍信息吗？","${baseUrl}/edu3/register/studentinfo/studentcardinfoback.html?studentid="+studentid,"#infoBody1");
	}else{
		alertMsg.confirm("请选择学生进行操作！");
	}
}

</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader" style="height: 75px;">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/register/studentinfo/changeschoolroll-audit.html"
				method="post">
				<input type="hidden" id="isFromPage" name="isFromPage" value="1" />
				<input type="hidden" id="isBrschool" name="isBrschool"
					value="${isBrschool }" />
				<div id="stuInfo" class="searchBar">
					<ul class="searchContent">
						<%-- <c:if test="${!isBrschool }"> --%>
							<li class="custom-li"><label>教学站：</label> <span
								sel-id="changeschool_brSchoolName" sel-name="branchSchool"
								sel-onchange="changeschoolrollQueryUnit()"
								sel-classs="flexselect" ></span></li>
						<%-- </c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" name="branchSchool"
								id="changeschool_brSchoolName"
								value="${condition['branchSchool']}" />
						</c:if> --%>
						<li><label>年级：</label> <span sel-id="changeschool_stuGrade"
							sel-name="stuGrade" sel-onchange="changeschoolrollQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="changeschool_classic"
							sel-name="classic" sel-onchange="changeschoolrollQueryClassic()"
							sel-style="width: 120px"></span></li>
						<li><label>学籍状态：</label> <!-- <select name="stuStatus" id="stuStatus" style="width:120px" >
					</select> --> <gh:select name="stuStatus" id="stuStatus"
								value="${condition['stuStatus']}"
								dictionaryCode="CodeStudentStatus" style="width:100px" /></li>
						

					</ul>
					<ul class="searchContent">
						<li id="changeschool-gradeToMajor4"  class="custom-li"><label>专业：</label> <span
							sel-id="changeschool_major" sel-name="major" sel-onchange="changeschoolrollQueryMajor()"
							sel-classs="flexselect"></span></li>
						
						<li><label>学号：</label><input type="text"
							name="matriculateNoticeNo" id="matriculateNoticeNo"
							value="${condition['matriculateNoticeNo']}" style="width: 115px" />
						</li>
						<li><label>姓名：</label><input type="text" name="name"
							id="name" value="${condition['name']}" style="width: 115px" /></li>
						<li><label>学籍卡状态：</label>
						<gh:select id="rollCard" name="rollCard"
								dictionaryCode="CodeRollCardStatus"
								value="${condition['rollCard']}" choose="Y" style="width: 100px" />
						</li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span sel-id="changeschool_classesid"
							sel-name="classesid" sel-classs="flexselect"></span></li>
						<li><label>身份证号：</label><input type="text" name="certNum"
							id="certNum" value="${condition['certNum']}" style="width: 140px" />
						</li>
						
						<%--<li>
					<label>班级：</label>
						<gh:selectModel id="stuClasses" name="classesid" bindValue="resourceid" displayValue="classname" condition="brSchool.resourceid='${condition.branchSchool}'"
						  	modelClass="com.hnjk.edu.roll.model.Classes" value="${condition['classesid']}" orderBy="grade.gradeName desc,brSchool.unitCode,major.majorCode,classic,classname,resourceid" style="width: 120px" /> 	
				</li> --%>

						<li><label>入学资格：</label>
						<gh:select id="entranceFlag" name="entranceFlag"
								dictionaryCode="CodeAuditStatus"
								value="${condition['entranceFlag']}" choose="Y"
								style="width: 120px" /></li>
						<!--<li>
				    <label>联系地址：</label>
				    <input  type="text" name="contactAddress" id="contactAddress" value="${condition['contactAddress']}" style="width:120px"/>
				</li>
				-->
						<%-- 
				<li>
						<label>审核结果：</label><gh:select name="entranceFlag" dictionaryCode="CodeAuditStatus"  value="${condition['entranceFlag']}" choose="Y" style="width: 120px"/> 
				</li> --%>
						<li><label>审核结果：</label>
						<gh:select id="auditResults" name="auditResults"
								dictionaryCode="CodeAuditResults"
								value="${condition['auditResults']}" style="width: 100px" /></li>
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
			<gh:resAuth parentCode="RES_SCHOOL_SCHOOLROLL_MANAGER_AUDIT"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="165">
				<thead>
					<tr>
						<th width="2.5%"><input type="checkbox" id="check_all_info" name="checkall"
							onclick="checkboxAll('#check_all_info','resourceid','#infoBody1')"/>
						</th>
						<th width="4%">姓名</th>
						<th width="7%">学号</th>
						<th width="2.5%">性别</th>
						<th width="9%">身份证</th>
						<th width="3%">民族</th>
						<th width="4%">年级</th>
						<!--<th width="7.8%">联系地址</th>-->
						<th width="4%">培养层次</th>
						<th width="8.5%">专业</th>
						<th width="6%">办学单位</th>
						<th width="4%">学籍状态</th>
						<th width="3%">账号</th>
						<th width="9%">班级</th>
						<th width="5%">入学日期</th>
						<th width="4%">入学资格</th>
						<th width="3.5%">学籍卡</th>
						<th width="4%">学习形式</th>
						<th width="4%">审核结果</th>
						<th width="4.5%">身份证正面</th>
						<th width="4.5%">身份证反面</th>
					</tr>
				</thead>
				<tbody id="infoBody1">
					<c:forEach items="${stulist.result}" var="stu" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stu.resourceid }" accept="${stu.studentBaseInfo.name }"
								title="${stu.sysUser.username}"
								rel="${stu.branchSchool.resourceid}|${stu.grade.resourceid}|${stu.major.resourceid}|${stu.classic.resourceid}|${stu.teachingType}"
								autocomplete="off" /></td>
							<td><a href="#" onclick="viewStuInfo2('${stu.resourceid}')"
								title="点击查看">${stu.studentBaseInfo.name }</a></td>
							<td>${stu.studyNo}</td>
							<td>${ghfn:dictCode2Val('CodeSex',stu.studentBaseInfo.gender) }</td>
							<td>${stu.studentBaseInfo.certNum }</td>
							<td>${ghfn:dictCode2Val('CodeNation',stu.studentBaseInfo.nation) }</td>
							<td>${stu.grade.gradeName}</td>
							<!--<td >${stu.studentBaseInfo.contactAddress}</td>	-->
							<td>${stu.classic.classicName }</td>
							<td>${stu.major.majorName }</td>
							<td>${stu.branchSchool}</td>
							<td <c:if test="${stu.studentStatus == '11'}">style='color: green'</c:if>
								<c:if test="${stu.studentStatus == '13'}">style='color: red'</c:if>
								><c:choose>
									<c:when
										test="${stu.studentStatus == '11' and stu.accountStatus==1}">正常注册</c:when>
									<c:when
										test="${stu.studentStatus == '11' and stu.accountStatus==0}">正常未注册</c:when>
									<c:otherwise>${ghfn:dictCode2Val('CodeStudentStatus',stu.studentStatus)}</c:otherwise>
								</c:choose>

							</td>
							<td <c:if test="${stu.accountStatus ne 1}">style='color: red'</c:if>
								>${stu.accountStatus==1?"激活":"停用"}
							</td>
							<td title="${stu.classes.classname }">
								${stu.classes.classname }</td>
							<td><fmt:formatDate value="${stu.inDate }"
									pattern="yyyy-MM-dd" /></td>
							<td <c:if test="${stu.enterAuditStatus=='N'}">style='color:red'</c:if>
								<c:if test="${stu.enterAuditStatus=='Y'}">style='color:blue'</c:if>
							 ><c:choose>
									<c:when test="${stu.enterAuditStatus=='N'}">
										不通过
									</c:when>
									<c:when test="${stu.enterAuditStatus=='Y'}">通过</c:when>
									<c:otherwise>待审核</c:otherwise>
								</c:choose></td>
							<td <c:if test="${stu.rollCardStatus eq '2'}">style='color: blue'</c:if>
								<c:if test="${stu.rollCardStatus eq '1'}">style='color: green'</c:if>
								<c:if test="${stu.rollCardStatus eq '0'}">style='color: red'</c:if>
								><c:choose>
									<c:when test="${stu.rollCardStatus=='2' }">
										已提交
									</c:when>
									<c:when test="${stu.rollCardStatus=='1' }">
										已保存
									</c:when>
									<c:otherwise>
										未保存
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								${ghfn:dictCode2Val('CodeTeachingType',stu.teachingType)}</td>
							<td <c:if test="${stu.auditResults == '1'}">style='color: blue'</c:if>
								<c:if test="${stu.auditResults == '2'}">style='color: red'</c:if>
								<c:if test="${stu.auditResults == '3'}">style='color: #3A5FCD'</c:if>
								><c:choose>
									<c:when test="${stu.auditResults == '1'}">
										通过
									</c:when>
									<c:when test="${stu.auditResults == '2'}">
										不通过
									</c:when>
									<c:when test="${stu.auditResults == '3'}">
										未提交
									</c:when>
									<c:when test="${stu.auditResults == '0'}">待审核</c:when>
									<c:otherwise>${ghfn:dictCode2Val('CodeAuditResults',stu.auditResults)}</c:otherwise>
								</c:choose>
							</td>
							<td><c:choose>
									<c:when test="${stu.studentBaseInfo.certPhotoPath eq null  }">未上传</c:when>
									<c:otherwise><a href="javaScript:void(0)" title="查看身份证正面" onclick="viewCerPhoto('${stu.studentBaseInfo.certPhotoPath}')">查看</a></c:otherwise>
								</c:choose>
							</td>
							<td><c:choose>
									<c:when test="${stu.studentBaseInfo.certPhotoPathReverse eq null }">未上传</c:when>
									<c:otherwise><a href="javaScript:void(0)" title="查看身份证反面" onclick="viewCerPhoto('${stu.studentBaseInfo.certPhotoPathReverse}')">查看</a></c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<%-- <gh:page page="${stulist}"
				goPageUrl="${baseUrl }/edu3/register/studentinfo/changeschoolroll-audit.html"
				pageType="sys" condition="${condition}" /> --%>
		</div>
		<div  class="pageContent" style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${stulist}"
				goPageUrl="${baseUrl }/edu3/register/studentinfo/changeschoolroll-audit.html"
				pageType="sys" condition="${condition}" /></div>
	</div>
</body>
<script type="text/javascript">
	function auditStudentInfo(){
		var url = "${baseUrl}/edu3/register/studentinfo/audit.html";
		var checked = $("#infoBody1 input[@id='resourceid']:checked").val();
		if("" != checked){
			navTab.openTab('RES_SCHOOL_SCHOOLROLL_INFO_AUDIT', url+'?resourceid='+checked, '审核');
		}else{
			alertMsg.correct("请选择一条记录！");
		}			
	}
	function viewCerPhoto(path){
		//$.pdialog.open("${rootUrl}common/students/"+path,"RES_SCHOOL_SCHOOLROLL_CERPHOTO","身份证",{width:800, height:600});	
		var url = "${baseUrl}/edu3/register/studentinfo/viewCerPhoto.html?path="+path;
		$.pdialog.open(url, '查看身份证', {width: 800, height: 600});
	}
</script>
</html>