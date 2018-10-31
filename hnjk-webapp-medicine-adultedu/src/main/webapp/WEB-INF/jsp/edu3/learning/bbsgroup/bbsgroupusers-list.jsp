<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生讨论小组成员管理</title>
<script type="text/javascript">
	//新增
	function addBbsGroupUsers(){
		var groupId = $("#bbsgroupusersform select[name='groupId']").val();
		if(groupId==""){
			alertMsg.warn("请先选择小组！");
		} else {
			var url = "${baseUrl}/edu3/framework/bbs/bbsgroup/member.html";
			//navTab.openTab('RES_METARES_BBSGROUP_EDIT', url+'?groupId='+groupId, '新增学习小组成员');
			$.pdialog.open(url+'?groupId='+groupId+"&classesId=${condition['classesId']}",'chooseLeader','新增学习小组成员',{mask:true,height:500,width:750});
		}
	}
	//删除
	function removeBbsGroupUsers(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/framework/bbs/bbsgroupusers/remove.html","#bbsGroupUsersBody");
	}
	$(document).ready(function(){
		//alert($("#bbsgroup_form_classid").length);
		$("#bbsgroupusers_list_classid option:first").remove();
		$("#groupId option:first").remove();
		//$("select[class*=flexselect]").flexselect();
	});
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="bbsgroupusersform" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/framework/bbs/bbsgroupusers/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>所属小组：</label> <gh:selectModel name="groupId"
								bindValue="resourceid" displayValue="groupName"
								style="width:120px"
								modelClass="com.hnjk.edu.learning.model.BbsGroup"
								value="${condition['groupId'] }" condition="${querys }"
								orderBy="course.courseCode" /></li>
						<li style="display: block;"><label>班级：</label> <gh:classesAutocomplete
								name="classesId" id="bbsgroupusers_list_classid" tabindex="1"
								displayType="code" defaultValue="${condition['classesId']}"
								exCondition="${classesCondition}"></gh:classesAutocomplete></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>

		<div class="pageContent">
			<gh:resAuth parentCode="RES_METARES_BBSGROUP" pageType="sublist"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_bbsGroupUsers"
							onclick="checkboxAll('#check_all_bbsGroupUsers','resourceid','#bbsGroupUsersBody')" /></th>
						<th width="10%">组名</th>
						<th width="10%">课程</th>
						<th width="5%">姓名</th>
						<th width="8%">学号</th>
						<th width="5%">年级</th>
						<th width="5%">培养层次</th>
						<th width="10%">专业</th>
						<th width="15%">班级</th>
						<th width="15%">教学站</th>
					</tr>
				</thead>
				<tbody id="bbsGroupUsersBody">
					<c:forEach items="${bbsGroupUsersListPage.result }"
						var="bbsGroupUsers" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${bbsGroupUsers.resourceid }" autocomplete="off" /></td>
							<td>${bbsGroupUsers.bbsGroup.groupName }</td>
							<td>${bbsGroupUsers.bbsGroup.course.courseName}</td>
							<td>${bbsGroupUsers.studentInfo.studentName}</td>
							<td>${bbsGroupUsers.studentInfo.studyNo}</td>
							<td>${bbsGroupUsers.studentInfo.grade.gradeName}</td>
							<td>${bbsGroupUsers.studentInfo.classic.classicName}</td>
							<td>${bbsGroupUsers.studentInfo.major.majorName}</td>
							<td>${bbsGroupUsers.studentInfo.classes.classname}</td>
							<td>${bbsGroupUsers.studentInfo.branchSchool}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${bbsGroupUsersListPage}"
				goPageUrl="${baseUrl}/edu3/framework/bbs/bbsgroupusers/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>