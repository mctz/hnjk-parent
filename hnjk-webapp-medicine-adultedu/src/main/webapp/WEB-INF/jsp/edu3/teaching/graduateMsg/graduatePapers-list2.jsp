<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>论文预约信息</title>
<script type="text/javascript">
	function ctx777(){
		$.pdialog.reload("${baseUrl}/edu3/teaching/graduateMsg/chooseGmsg.html?branchSchool="+$('#branchSchool').val()+"&batchId="+$('#batchId').val()+"&grade="+$('#grade').val()+"&classic="+$('#classic').val()+"&name="+$('#name').val()+"&major="+$('#major').val());
	}
		
	function clickThis777(obj){
		if(obj.checked){
			$("#paperOrderId").val(obj.value);
			$("#subject").val(obj.alt);
		}
	}
	
	$(document).ready(function(){
			// 修改高度
			$("#cd_right").css("height",parseInt($(".dialogContent").css("height")));
			
			window.setTimeout(function(){
				var existId = $("#paperOrderId").val();
				$("#gpoBody158 input[value='"+existId+"']").attr("checked",true);
			},500);
	})
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<div class="searchBar">
				<ul class="searchContent">
					<c:if test="${showCenter eq 'show'}">
						<li><label>教学站：</label> <gh:selectModel name="branchSchool"
								bindValue="resourceid" displayValue="unitName"
								style="width:120px" modelClass="com.hnjk.security.model.OrgUnit"
								value="${condition['branchSchool']}"
								condition="unitType='brSchool'" /></li>
					</c:if>
					<li><label>批次：</label>
					<gh:selectModel name="batchId" bindValue="resourceid"
							displayValue="batchName" value="${condition['batchId']}"
							modelClass="com.hnjk.edu.teaching.model.ExamSub"
							style="width:120px" /></li>
				</ul>
				<ul class="searchContent">
					<li><label>年级：</label>
					<gh:selectModel name="grade" bindValue="resourceid"
							displayValue="gradeName" style="width:120px"
							modelClass="com.hnjk.edu.basedata.model.Grade"
							orderBy="gradeName desc" value="${condition['grade']}" /></li>
					<li><label>层次：</label>
					<gh:selectModel name="classic" bindValue="resourceid"
							displayValue="classicName" value="${condition['classic']}"
							modelClass="com.hnjk.edu.basedata.model.Classic"
							style="width:120px" /></li>
				</ul>
				<ul class="searchContent">
					<li><label>姓名：</label><input type="text" id="name" name="name"
						value="${condition['name']}" style="width: 115px" /></li>
					<li><label>专业：</label>
					<gh:selectModel name="major" bindValue="resourceid"
							displayValue="majorName" value="${condition['major']}"
							modelClass="com.hnjk.edu.basedata.model.Major"
							style="width:120px" /></li>
					<div class="subBar">
						<ul>
							<a class="button" href="javascript:;" onclick="ctx777()"><span>查
									询</span></a>&nbsp;&nbsp;
							<a class="button" href="javascript:;"
								onclick="$.pdialog.closeCurrent();"><span>确 定</span></a>
						</ul>
					</div>
				</ul>
			</div>
		</div>
		<div class="pageContent">
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%">&nbsp;</th>
						<th width="12%">题目</th>
						<th width="10%">论文批次</th>
						<th width="10%">学号</th>
						<th width="9%">姓名</th>
						<th width="9%">年级</th>
						<th width="9%">专业</th>
						<th width="9%">层次</th>
						<th width="9%">教学站</th>
						<th width="9%">指导老师</th>
						<th width="9%">审核状态</th>
					</tr>
				</thead>
				<tbody id="gpoBody158">
					<c:forEach items="${ordercList.result}" var="o" varStatus="vs">
						<tr>
							<td><input type="radio" name="resourceid"
								value="${o.resourceid }" onclick="clickThis777(this)"
								alt="${o.subject}" /></td>
							<td>${o.subject }</td>
							<td>${o.examSub.batchName }</td>
							<td>${o.studentInfo.studyNo }</td>
							<td>${o.studentInfo.studentName }</td>
							<td>${o.studentInfo.grade }</td>
							<td>${o.studentInfo.major }</td>
							<td>${o.studentInfo.classic }</td>
							<td>${o.studentInfo.branchSchool }</td>
							<td>${o.guidTeacherName }</td>
							<td><c:choose>
									<c:when test="${o.status eq 1}">通过</c:when>
									<c:otherwise>未审核</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${ordercList}"
				goPageUrl="${baseUrl }/edu3/teaching/graduateMsg/chooseGmsg.html"
				pageType="sys" targetType="dialog" condition="${condition}" />
		</div>
	</div>

</body>
</html>