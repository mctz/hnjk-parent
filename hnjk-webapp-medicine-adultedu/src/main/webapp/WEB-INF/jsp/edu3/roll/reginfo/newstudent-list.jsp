<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新生数据库</title>
<script type="text/javascript">
	function viewStuInfo2(id){
	var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
	//navTab.openTab('_blank', url+'?resourceid='+id, '修改学籍');
	$.pdialog.open(url+'?resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});　
	}
	function exportNewStu(){
		var branchSchool="${condition['branchSchool']}";
		var major="${condition['major']}";
		var grade="${condition['grade']}";
		var classic="${condition['classic']}";
		var name="${condition['name']}";
		var matriculateNoticeNo="${condition['matriculateNoticeNo']}";
		var url="${baseUrl}/edu3/register/reginfo/newstudent-export.html?branchSchool="+branchSchool+"&major="+major+"&grade="+grade+"&classic="+classic+"&name="+name+"&matriculateNoticeNo="+matriculateNoticeNo;
		window.location.href=url;
	}
	//表一.试点高校网络教育招生计划备案表（招生办）.xls
	function exportRecruit()
	{
		var grade="${condition['grade']}";
		var url="${baseUrl}/edu3/register/reginfo/exportRecruitPlan.html?grade="+grade;
		window.location.href=url;
	}
	
	//表二.试点高校网络教育实际录取情况表（学籍办）.xls
	function existRealityEnrollExcel()
	{
		var grade="${condition['grade']}";
		var url="${baseUrl}/edu3/register/reginfo/existRealityEnrollExcel.html?grade="+grade;
		window.location.href=url;
	}

	//表四.试点高校本年度计划招生的教学站备案表（招生办）.xls
	function exportRecruitForYear(){
		var url = "${baseUrl}/edu3/roll/selectCondition/selectCondition.html?actionType=exportRecruitForYear";
		doSelectCondition(url);
	}
	//导出通过入学资格审核学生
	function entranceFlagStudent(){
		var url = "${baseUrl}/edu3/roll/selectCondition/selectCondition.html?actionType=entranceFlagStudentExcel";
		doSelectCondition(url);
	}
	//表四.试点高校本年度计划招生的教学站备案表（招生办）.xls 的条件选择、导出通过入学资格审核名单的条件t
	function doSelectCondition(url){
		$.pdialog.open(url, 'doSelectCondition', '导出条件选择', {width:100, height:50});　
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/register/reginfo/newstudent-list.html"
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
			<gh:resAuth parentCode="RES_SCHOOL_SCHOOLROLL_NEWSTU" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr class="head_bg">
						<th width="6%">学号</th>
						<!--<th width="6%">生源省份代码</th>-->
						<th width="6%">姓名</th>
						<th width="5%">性别</th>
						<th width="6%">出生年月</th>
						<th width="6%">身份证号</th>
						<th width="6%">政治面貌</th>
						<th width="6%">民族</th>
						<!--  <th width="6%">院校代码</th> -->
						<th width="7%">院校名称</th>
						<!--  <th width="6%">专业代码</th> -->
						<th width="6%">专业</th>
						<th width="7%">层次</th>
						<th width="6%">学习形式</th>
						<th width="5%">学制</th>
						<!--<th width="4%">&nbsp;</th>-->
						<th width="6%">招生季节</th>
					</tr>
				</thead>
				<tbody id="newBody">
					<c:forEach items="${reglist.result}" var="R" varStatus="vs">
						<tr>
							<td>${R.studentInfo.studyNo}</td>
							<!--<td >${R.province}</td>-->
							<td><a href="#"
								onclick="viewStuInfo2('${R.studentInfo.resourceid}')"
								title="点击查看">${R.studentInfo.studentName }</a></td>
							<td>${ghfn:dictCode2Val('CodeSex',R.studentInfo.studentBaseInfo.gender) }</td>
							<td>${R.studentInfo.studentBaseInfo.bornDay}</td>
							<td>${R.studentInfo.studentBaseInfo.certNum}</td>
							<td>${ghfn:dictCode2Val('CodePolitics',R.studentInfo.studentBaseInfo.politics) }</td>
							<td>${ghfn:dictCode2Val('CodeNation',R.studentInfo.studentBaseInfo.nation) }</td>
							<!--  <td >${R.schoolCode}</td> -->
							<td>${R.schoolName}</td>
							<!--  <td >${R.studentInfo.major.majorNationCode}</td> -->
							<td>${R.studentInfo.major.majorName }</td>
							<td>${R.studentInfo.classic.classicName }</td>
							<td>${ghfn:dictCode2Val('CodeLearningStyle',R.studentInfo.learningStyle) }</td>
							<td>${R.stuLimite }</td>
							<!--<td >${R.emptyStr }</td>-->
							<td>${R.season }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${reglist}"
				goPageUrl="${baseUrl }/edu3/register/reginfo/newstudent-list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>

</body>

</html>