<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>班级管理</title>
</head>
<body>
	<script type="text/javascript">

function goSelectClassesTeacher(type,idsN,namesN,title){
	$.pdialog.open("${baseUrl }/edu3/roll/classes/select-classesmaster.html?idsN="+idsN+"&namesN="+namesN+"&type="+type,"selector","选择"+title,{mask:true,height:600,width:800});
}

function getClassCode(){
	var classCodeRule = "${classCodeRule}";
	var brSchoolid = $("#classes_form_brSchoolid").val();
	var gradeid = $("#classes_form_gradeid").val();
	if(classCodeRule=='1'){
		
	}else if(classCodeRule='2'){
		if(brSchoolid=="" || gradeid==""){
			alertMsg.warn("请选择教学点和年级！");
			return;
		}
	}
	jQuery.ajax({
		url:baseUrl+"/edu3/roll/classes/getClassCode.html",
		data:{classCodeRule:classCodeRule,brSchoolid:brSchoolid,gradeid:gradeid},
		dataType:"json",
		success:function(data){
			$("#classCode").val(data.classCode);
		}
	});
}

</script>
	<h2 class="contentTitle">${empty classes.resourceid ? '新增' : '编辑' }班级</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post" action="${baseUrl}/edu3/roll/classes/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${classes.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">教学站:</td>
							<td width="30%"><c:choose>
								<c:when test="${not isBrschool and empty classes.resourceid }">
									<gh:brSchoolAutocomplete name="brSchoolid" tabindex="2"
										id="classes_form_brSchoolid"
										defaultValue="${classes.brSchool.resourceid}"
										displayType="code" style="width:55%;" />
									<span style="color: red;">*</span>
								</c:when>
								<c:otherwise>
									<input type="hidden" id="classes_form_brSchoolid" name="brSchoolid" value="${classes.brSchool.resourceid }"> 
									${classes.brSchool.unitName }
								</c:otherwise>
								</c:choose></td>
							<td width="20%"></td>
							<td width="30%"></td>
						</tr>
						<tr>
							<td width="20%">班级编号:</td>
							<td width="30%"><input type="text" id="classCode" name="classCode"
								style="width: 40%" value="${classes.classCode }"/>
								<a href="javascript:;" class="button" style="margin-left: 10px;"
								onclick="getClassCode();"><span>生成班级编号</span></a></td>
							<td width="20%">班级名称:</td>
							<td width="30%"><input type="text" name="classname"
								style="width: 80%" value="${classes.classname }"
								class="required" /></td>
						</tr>
						<c:choose>
							<c:when test="${empty classes.resourceid }">
								<tr>
									<td>年级:</td>
									<td><gh:selectModel id="classes_form_gradeid"
											name="gradeid" bindValue="resourceid"
											displayValue="gradeName"
											orderBy="yearInfo.firstYear desc,term desc,resourceid desc"
											modelClass="com.hnjk.edu.basedata.model.Grade"
											value="${classes.grade.resourceid}" classCss="required" /> <span
										style="color: red;">*</span></td>
									<td>专业:</td>
									<td><gh:selectModel name="majorid"
											id="classes_form_majorid" bindValue="resourceid"
											displayValue="majorCodeName" classCss="required"
											modelClass="com.hnjk.edu.basedata.model.Major"
											value="${classes.major.resourceid}"
											orderBy="majorCode,majorName" style="width:55%;" /> <span
										style="color: red;">*</span></td>
								</tr>
								<tr>
									<td>层次:</td>
									<td><gh:selectModel name="classicid"
											id="classes_form_classicid" bindValue="resourceid"
											displayValue="classicName"
											value="${classes.classic.resourceid}"
											modelClass="com.hnjk.edu.basedata.model.Classic"
											classCss="required" /> <span style="color: red;">*</span></td>
									<td>学习方式:</td>
									<td><gh:select id="classes_form_teachingType"
											name="teachingType" value="${classes.teachingType}"
											dictionaryCode="CodeTeachingType" classCss="required" /> <span
										style="color: red;">*</span></td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td>年级:</td>
									<td>${classes.grade.gradeName }</td>
									<td>专业:</td>
									<td>${classes.major.majorName }</td>
								</tr>
								<tr>
									<td>层次:</td>
									<td>${classes.classic.classicName }</td>
									<td>学习方式:</td>
									<td>${ghfn:dictCode2Val('CodeTeachingType',classes.teachingType) }</td>
								</tr>
							</c:otherwise>
						</c:choose>
						<tr>
							<td>班主任:</td>
							<td><input type="text" name="classesMaster"
								value="${classes.classesMaster }" id="classesMaster"
								readonly="readonly" /> <input type="hidden"
								name="classesMasterId" id="classesMasterId"
								value="${classes.classesMasterId }" /> <a href="javascript:;"
								class="button" style="margin-left: 10px;"
								onclick="goSelectClassesTeacher('0','classesMasterId','classesMaster','班主任');"><span>班主任</span></a>

							</td>
							<td>班主任联系电话:</td>
							<td><input type="text" name="classesMasterPhone"
								value="${classes.classesMasterPhone }" class="phone" /></td>
						</tr>
						<%-- <tr>
							<td>备注:</td>
							<td colspan="3"><input type="text" name="memo"
								value="${classes.memo }" style="width: 50%;" /></td>
						</tr> --%>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>