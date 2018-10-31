<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>在校学生库</title>
<script type="text/javascript">
	function viewStuInfo2(id){
	var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
	//navTab.openTab('_blank', url+'?resourceid='+id, '修改学籍');
	$.pdialog.open(url+'?resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});　
	}
	function exportExistStu(){
		var branchSchool="${condition['branchSchool']}";
		var major="${condition['major']}";
		var grade="${condition['grade']}";
		var classic="${condition['classic']}";
		var name="${condition['name']}";
		var matriculateNoticeNo="${condition['matriculateNoticeNo']}";
		
		var url="${baseUrl}/edu3/register/reginfo/existstudent-export.html?branchSchool="+branchSchool+"&major="+major+"&grade="+grade+"&classic="+classic+"&name="+name+"&matriculateNoticeNo="+matriculateNoticeNo;
		window.location.href=url;
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/register/reginfo/existstudent-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${!isBrschool }">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1" id="eiinfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" style="width:120px" />
							</li>
						</c:if>
						<li><label>专业：</label>
						<gh:selectModel name="major" bindValue="resourceid"
								displayValue="majorName" value="${condition['major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:120px" /></li>
						<li><label>层次：</label>
						<gh:selectModel name="classic" bindValue="resourceid"
								displayValue="classicName" value="${condition['classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:120px" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>年级：</label>
						<gh:selectModel name="grade" bindValue="resourceid"
								displayValue="gradeName" value="${condition['grade']}"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								orderBy="gradeName desc" style="width:125px" />
						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 115px" /></li>
						<li><label>学号：</label><input type="text"
							name="matriculateNoticeNo"
							value="${condition['matriculateNoticeNo']}" style="width: 115px" />
						</li>
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
			<gh:resAuth parentCode="RES_SCHOOL_SCHOOLROLL_EXISTSTU"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr class="head_bg">
						<th width="6%">姓名</th>
						<th width="5%">性别</th>
						<th width="6%">年级码</th>
						<!--  <th width="6%">班别码</th> -->
						<th width="6%">学籍号</th>
						<th width="6%">出生日期</th>
						<!--  <th width="5%">民族码</th> -->
						<th width="6%">户籍所在地</th>
						<!--  <th width="6%">户籍码</th> -->
						<th width="6%">户籍名</th>
						<!--  <th width="6%">户籍性质码</th> -->
						<!--  <th width="6%">学校码</th> -->
						<th width="6%">身份证号</th>
						<th width="6%">是否华侨</th>
						<th width="6%">健康状况码</th>
						<th width="6%">就读方式码</th>
						<th width="6%">学前教育码</th>
					</tr>
				</thead>
				<tbody id="existBody">
					<c:forEach items="${reglist.result}" var="r" varStatus="vs">
						<tr>
							<td><a href="#"
								onclick="viewStuInfo2('${r.studentInfo.resourceid}')"
								title="点击查看">${r.studentInfo.studentName }</a></td>
							<td>${ghfn:dictCode2Val('CodeSex',r.studentInfo.studentBaseInfo.gender) }</td>
							<td>${r.studentInfo.grade.gradeName}</td>
							<!--  <td >&nbsp;</td> -->
							<td>${r.studentInfo.studyNo }</td>
							<td><fmt:formatDate
									value="${r.studentInfo.studentBaseInfo.bornDay}"
									pattern="yyyy-MM-dd" /></td>
							<!--  <td >${r.studentInfo.studentBaseInfo.nation}</td> -->
							<td>${r.studentInfo.studentBaseInfo.residence}</td>
							<!--  <td >${r.studentInfo.studentBaseInfo.homePlace}</td> -->
							<td>${ghfn:dictCode2Val('CodeRegisteredResidenceKind',r.studentInfo.studentBaseInfo.residenceKind) }</td>
							<!--  <td >${r.studentInfo.studentBaseInfo.residenceKind}</td> -->
							<!-- <td >${r.schoolCode }</td> -->
							<td>${r.studentInfo.studentBaseInfo.certNum}</td>
							<td>${r.studentInfo.studentBaseInfo.gaqCode }</td>
							<td>${r.studentInfo.studentBaseInfo.health }</td>
							<td>${r.studentInfo.studyInSchool }</td>
							<td>&nbsp;</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${reglist}"
				goPageUrl="${baseUrl }/edu3/register/reginfo/existstudent-list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>

</body>

</html>