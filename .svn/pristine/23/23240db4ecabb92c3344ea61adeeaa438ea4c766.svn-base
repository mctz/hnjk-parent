<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业生上报数据</title>
<script type="text/javascript">
function exportGraduationReport(){
	
	var branchSchool="${condition['branchSchool']}";
	var grade="${condition['grade']}";
	var major="${condition['major']}";
	var name="${condition['name']}";
	var studyNo="${condition['studyNo']}";
	var classic="${condition['classic']}";
	
	var url="${baseUrl}/edu3/schoolroll/graduation/student/report.html?branchSchool="+branchSchool+"&grade="+grade+"&major="+major+"&name="+name+"&studyNo="+studyNo+"&classic="+classic;
	window.location.href=url;
}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/schoolroll/graduation/student/reportList.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${showCenter eq 'show'}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1" id="eiinfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" style="width:120px" />
							</li>
						</c:if>
						<li><label>年级：</label>
						<gh:selectModel name="grade" bindValue="resourceid"
								displayValue="gradeName" style="width:120px"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								orderBy="gradeName desc" value="${condition['grade']}" /></li>
						<li><label>专业：</label>
						<gh:selectModel name="major" bindValue="resourceid"
								displayValue="majorName" value="${condition['major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:120px" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 120px" /></li>
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" style="width: 115px" /></li>
						<li><label>层次：</label>
						<gh:selectModel name="classic" bindValue="resourceid"
								displayValue="classicName" value="${condition['classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:120px" /></li>
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
			<gh:resAuth parentCode="RES_SCHOOL_GRADUATION_REPORT" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_gd"
							onclick="checkboxAll('#check_all_gd','resourceid','#graduateBody')" /></th>
						<th width="10%">学号</th>
						<th width="8%">姓名</th>
						<th width="8%">年级</th>
						<th width="9%">专业</th>
						<th width="10%">层次</th>
						<th width="10%">毕业证书编号</th>
						<th width="10%">学位证书编号</th>
						<th width="10%">学位名称</th>
						<th width="10%">毕业类型</th>
						<th width="10%">毕业日期</th>
					</tr>
				</thead>
				<tbody id="graduateBody">
					<c:forEach items="${graduationStudentList.result}" var="g"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${g.resourceid }" autocomplete="off" /></td>
							<td>${g.studentInfo.studyNo }</td>
							<td>${g.studentInfo.studentName }</td>
							<td>${g.studentInfo.grade }</td>
							<td>${g.studentInfo.major }</td>
							<td>${g.studentInfo.classic }</td>
							<td>${g.diplomaNum }</td>
							<td>${g.degreeNum }</td>
							<td>${g.degreeName }</td>
							<td>${ghfn:dictCode2Val("CodeGraduateType",g.graduateType) }</td>
							<td><fmt:formatDate pattern="yyyy-MM-dd"
									value="${g.graduateDate }" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${graduationStudentList}"
				goPageUrl="${baseUrl }/edu3/schoolroll/graduation/student/reportList.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>