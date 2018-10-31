<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择毕业生列表</title>

</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/roll/graduation/student/selectForExecute.html"
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
						<li><label>层次：</label>
						<gh:selectModel name="classic" bindValue="resourceid"
								displayValue="classicName" value="${condition['classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:120px" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>学籍状态：</label>
						<gh:select name="stuStatus" value="${condition['stuStatus']}"
								dictionaryCode="CodeStudentStatus" style="width:120px" /></li>
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

			<gh:resAuth parentCode="RES_SCHOOL_GRADUATION_QUALIF" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="9%">学号</th>
						<th width="5%">姓名</th>
						<th width="4%">性别</th>
						<th width="8%">年级</th>
						<th width="8%">专业</th>
						<th width="8%">培养层次</th>
						<th width="11%">教学站</th>


						<th width="10%">身份证</th>
						<th width="8%">民族</th>
						<th width="10%">入学日期</th>


						<th width="8%">学籍状态</th>
						<th width="6%">操作</th>
					</tr>
				</thead>
				<tbody id="qualifSelectBody">
					<c:forEach items="${selectGraduationStudentList.result}" var="stu"
						varStatus="vs">
						<tr>
							<td>${stu.studyNo}</td>
							<td><a href="#" onclick="viewStuInfo2('${stu.resourceid}')"
								title="点击查看">${stu.studentBaseInfo.name }</a></td>
							<td>${ghfn:dictCode2Val('CodeSex',stu.studentBaseInfo.gender) }</td>
							<td>${stu.grade.gradeName}</td>
							<td>${stu.major.majorName }</td>
							<td>${stu.classic.classicName }</td>
							<td>${stu.branchSchool}</td>
							<td>${stu.studentBaseInfo.certNum }</td>
							<td>${ghfn:dictCode2Val('CodeNation',stu.studentBaseInfo.nation) }</td>
							<td></td>
							<td>${ghfn:dictCode2Val('CodeStudentStatus',stu.studentStatus) }</td>
							<td><input type="hidden" name="resourceid"
								value="${stu.resourceid }" autocomplete="off" /><a href="#"
								onclick="beforeAdd('${stu.resourceid }')">选择</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${graduationQualifList}"
				goPageUrl="${baseUrl }/edu3/roll/graduation/student/selectForExecute.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
<script type="text/javascript">

	function beforeAdd(stuResourceid){
		var url="${baseUrl}/edu3/roll/graduation/student/beforeAdd.html"+"?resourceid="+stuResourceid.toString()+"&mark=add";
		navTab.openTab('navTab', url, '新增毕业生');
	}

	function viewStuInfo2(id){
		var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
		//navTab.openTab('_blank', url+'?resourceid='+id, '修改学籍');
		$.pdialog.open(url+'?act=view&resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});　
	}
	
	function modifyStuInfo(){
		var url = "${baseUrl}/edu3/register/studentinfo/editstu.html";
		if(isCheckOnlyone('resourceid','#infoBody')){
			navTab.openTab('RES_SCHOOL_SCHOOLROLL_MANAGER_EDIT', url+'?resourceid='+$("#infoBody input[@name='resourceid']:checked").val(), '修改学籍');
		}			
	}

</script>
</html>