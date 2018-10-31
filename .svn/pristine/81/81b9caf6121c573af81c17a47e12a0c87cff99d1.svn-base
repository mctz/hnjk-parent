<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学籍信息管理</title>
<script type="text/javascript">
	//function ctx357(){
	//	$.pdialog.reload("${baseUrl}/edu3/teaching/graduatePapers/chooseStudent.html?branchSchool="+$('#branchSchool').val()+"&major="+$('#major').val()+"&classic="+$('#classic').val()+"&stuStatus="+$('#stuStatus').val()+"&name="+$('#name3').val()+"&matriculateNoticeNo="+$('#matriculateNoticeNo').val());
	//}
		
	function clickThis567(obj){
		if(obj.checked){
			var alts = (obj.alt).split('|');
			$("#studentId").val(obj.value);
			$("#studentName").val(alts[0]);
			$("#studentNo4").val(alts[1]);
			$("#stuCenter4").val(alts[2]);
			$("#grade4").val(alts[3]);
			$("#major4").val(alts[4]);
			$("#classic4").val(alts[5]);
			/*var url = baseUrl+"/edu3/teaching/graduatePapers/chooseStudentCourse.html"; 
			$.get(url,{studentId:obj.value},function(data){
				var d = (data.split(','));
				$("#courseId").val(d[0]);
				$("#courseName").val(d[1]);
			});*/	
			$("#g_courseId").val(alts[6]);
			$("#g_courseName").val(alts[7]);
		}	
	}
	
	$(document).ready(function(){
			// 修改高度
			$("#cd_right").css("height",parseInt($(".dialogContent").css("height")));
			
			window.setTimeout(function(){
				$("#infoBody input[value='"+$("#studentId").val()+"']").attr("checked",true);
			},500);
			
	});
	
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/teaching/graduatePapers/chooseStudent.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label> <gh:selectModel name="branchSchool"
								bindValue="resourceid" displayValue="unitName"
								style="width:120px" modelClass="com.hnjk.security.model.OrgUnit"
								value="${condition['branchSchool']}"
								condition="unitType='brSchool'" /></li>
						<li><label>专业：</label>
						<gh:selectModel name="major" bindValue="resourceid"
								displayValue="majorName" value="${condition['major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:120px" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>层次：</label>
						<gh:selectModel name="classic" bindValue="resourceid"
								displayValue="classicName" value="${condition['classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:120px" /></li>
						<li><label>学籍状态：</label>
						<gh:select name="stuStatus" value="${condition['stuStatus']}"
								dictionaryCode="CodeStudentStatus" style="width:120px" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>姓名：</label><input type="text" id="name3"
							name="name" value="${condition['name']}" style="width: 115px" />
						</li>
						<li><label>学号：</label><input type="text"
							id="matriculateNoticeNo" name="matriculateNoticeNo"
							value="${condition['matriculateNoticeNo']}" style="width: 115px" />
						</li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
							<li><div class="button">
									<div class="buttonContent">
										<button type="button" onclick="$.pdialog.closeCurrent();">
											确 定</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="168">
				<thead>
					<tr>
						<th width="5%">&nbsp;</th>
						<th width="10%">姓名</th>
						<th width="10%">学号</th>
						<th width="5%">性别</th>
						<th width="10%">身份证</th>
						<th width="10%">年级</th>
						<th width="10%">层次</th>
						<th width="10%">专业</th>
						<th width="10%">教学站</th>
						<th width="10%">学籍状态</th>
						<th width="10%">账号状态</th>
					</tr>
				</thead>
				<tbody id="infoBody">
					<c:forEach items="${stulist.result}" var="stu" varStatus="vs">
						<tr>
							<td><input type="radio" name="resourceid"
								value="${stu.resourceid }"
								alt="${stu.studentBaseInfo.name}|${stu.studyNo}|${stu.branchSchool}|${stu.grade.gradeName}|${stu.major.majorName }|${stu.classic.classicName }|${courseMap[stu.resourceid]}"
								onclick="clickThis567(this)" /></td>
							<td>${stu.studentBaseInfo.name }</td>
							<td>${stu.studyNo}</td>
							<td>${ghfn:dictCode2Val('CodeSex',stu.studentBaseInfo.gender) }</td>
							<td>${stu.studentBaseInfo.certNum }</td>
							<td>${stu.grade.gradeName}</td>
							<td>${stu.classic.classicName }</td>
							<td>${stu.major.majorName }</td>
							<td>${stu.branchSchool}</td>
							<td>${ghfn:dictCode2Val('CodeStudentStatus',stu.learingStatus) }</td>
							<td><c:if test="${ stu.accountStatus == 1}">激活</c:if><font
								color="red"><c:if test="${ stu.accountStatus == 0}">停用</c:if></font>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${stulist}"
				goPageUrl="${baseUrl }/edu3/teaching/graduatePapers/chooseStudent.html"
				pageType="sys" pageNumShown="4" targetType="dialog"
				condition="${condition}" />
		</div>
	</div>

</body>
</html>