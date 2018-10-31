<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增学生讨论小组成员</title>
<script type="text/javascript">
$(document).ready(function(){
	$("#classesLi").hide();
});
function _MycheckboxAll(obj){
	$("#groupUserBody input[name='resourceid']").each(function(){
		if(!$(this).attr("disabled")){
			$(this).attr("checked",obj.checked);
		}
	});
}
	function addMembers(){
		var url = "${baseUrl}/edu3/framework/bbs/bbsgroupusers/save.html";
		if(!isChecked('resourceid',"#groupUserBody")){
	 		alertMsg.warn('请选择要加入的成员！');
			return false;
	 	}	
		var gid = $('#usersgroupId').val();		
		var res = "";
		$("#groupUserBody input[@name='resourceid']:checked").each(function(){
					if(!$(this).attr("disabled")){
						res+=$(this).val();
		                res += ",";
					} 					  
                  });
         if(res!="")
        	 res = res.substring(0,res.length-1);
		$.post(url,{groupId:gid,resourceid:res}, function (json){
			DWZ.ajaxDone(json);
			if (json.statusCode == 200){
				if (json.navTabId){
					navTab.reload(null, {}, json.navTabId);
				}
				if(json.reloadUrl){
					$.pdialog.reload(json.reloadUrl);
				}
			}
		}, "json");
			
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/framework/bbs/bbsgroup/member.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>姓名：</label><input type="text" id="name"
							name="name" value="${condition['name']}" style="width: 115px" />
							<input type="hidden" id="usersgroupId" name="groupId"
							value="${condition['groupId']}" /></li>
						<li><label>学号：</label><input type="text" id="studyNo"
							name="studyNo" value="${condition['studyNo']}"
							style="width: 115px" /></li>
						<c:if test="${empty branchSchool }">
							<li><label>教学站：</label>
							<gh:selectModel name="branchSchool" bindValue="resourceid"
									displayValue="unitName" style="width:120px"
									modelClass="com.hnjk.security.model.OrgUnit"
									value="${condition['branchSchool']}"
									condition="unitType='brSchool'" /></li>
						</c:if>
						<li><label>学习层次：</label>
						<gh:selectModel name="classic" bindValue="resourceid"
								displayValue="classicName" value="${condition['classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:120px" /></li>
						<li id="classesLi"><label>班级：</label> <gh:classesAutocomplete
								name="classesId" id="bbsgroupusers_form_classid" tabindex="1"
								displayType="code" defaultValue="${condition['classesId']}"
								exCondition="${classesCondition}"></gh:classesAutocomplete></li>
					</ul>
					<div class="subBar">
						<c:if test="${not empty bbsGroup }">
							<span style="color: blue">(${bbsGroup.course.courseName }
								· ${bbsGroup.groupName } 成员分配，当前已有${groupUserCount}个成员) </span>
						</c:if>
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="button" onclick="addMembers();">加入小组</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>

		<div class="pageContent">
			<table class="table" layouth="142">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_groupUser" onclick="_MycheckboxAll(this)" /></th>
						<th width="8%">姓名</th>
						<th width="13%">学号</th>
						<th width="8%">年级</th>
						<th width="8%">培养层次</th>
						<th width="15%">专业</th>
						<th width="20%">班级</th>
						<th width="15%">教学站</th>
					</tr>
				</thead>
				<tbody id="groupUserBody">
					<c:forEach items="${stuplanlist.result}" var="plan" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${plan.resourceid }"
								<c:if test="${fn:contains(groupUserIds,plan.resourceid) }">disabled="disabled"</c:if>
								<c:if test="${fn:contains(filterids,plan.resourceid) }">disabled="disabled"</c:if>
								autocomplete="off" /></td>
							<td><span
								style="color:${fn:contains(groupUserIds,plan.resourceid)?'green':'' }">${plan.studentName}</span></td>
							<td>${plan.studyNo}</td>
							<td>${plan.grade.gradeName}</td>
							<td>${plan.classic.classicName }</td>
							<td>${plan.major.majorName }</td>
							<td>${plan.classes.classname }</td>
							<td>${plan.branchSchool }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${stuplanlist}"
				goPageUrl="${baseUrl}/edu3/framework/bbs/bbsgroup/member.html"
				targetType="dialog" condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>