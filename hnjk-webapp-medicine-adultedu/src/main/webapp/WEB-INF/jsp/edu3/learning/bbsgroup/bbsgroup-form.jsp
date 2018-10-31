<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生讨论小组管理</title>
<script type="text/javascript">		
		//选择组长
		function chooseLeader(){
			var courseId = $("#bbsGroupEditFor_courseId").val();			
			var classesId = $("#bbsgroup_form_classid").val();			
			if(courseId==""){
				alertMsg.warn("请先选择课程！");
			} else if(classesId==""){
				alertMsg.warn("请先选择班级！");
			} else {
				var url ="${baseUrl }/edu3/framework/bbs/bbsgroup/member.html?groupId=${bbsGroup.resourceid}&idsN=leaderId&namesN=leaderName&type=leader&courseId="+courseId+"&classesId="+classesId;
	    		$.pdialog.open(url,'chooseLeader','选择组长',{mask:true,height:500,width:750});
	    	}
		}
		//选择小组成员
		function chooseMember(){
			var courseId = $("#bbsGroupEditFor_courseId").val();
			var groupId = $("#bbsGroupEditForm input[name='resourceid']").val();
			var classesId = $("#bbsgroup_form_classid").val();	
			if(courseId==""){
				alertMsg.warn("请先选择课程！");
			} else if(classesId==""){
				alertMsg.warn("请先选择班级！");
			} else {
				var url ="${baseUrl }/edu3/framework/bbs/bbsgroup/member.html?groupId=${bbsGroup.resourceid}&idsN=memberId&namesN=memberName&type=member&courseId="+courseId+"&classesId="+classesId;
	    		$.pdialog.open(url,'chooseMember','选择组员',{mask:true,height:500,width:750});
	    	}
		}
		$(document).ready(function(){
			//alert($("#bbsgroup_form_classid").length);
			$("#bbsgroup_form_classid option:first").text("请选择");
			$("select[class*=flexselect]").flexselect();
		});
	</script>
</head>
<body>
	<h2 class="contentTitle">${(empty bbsGroup.resourceid)?'新增':'编辑' }学习小组</h2>
	<div class="page">
		<div class="pageContent">
			<form id="bbsGroupEditForm" method="post"
				action="${baseUrl}/edu3/framework/bbs/bbsgroup/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${bbsGroup.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="12%">组名:</td>
							<td width="38%"><input type="text" name="groupName"
								value="${bbsGroup.groupName }" class="required" /></td>
							<td width="12%">所属课程:</td>
							<td width="38%">${bbsgroupformCourseSelect }<font
								color="red">*</font>
							</td>
						</tr>
						<tr>
							<td width="12%">班级:</td>
							<td width="38%" colspan="3"><gh:classesAutocomplete
									name="classesId" id="bbsgroup_form_classid" tabindex="1"
									displayType="code"
									defaultValue="${bbsGroup.classes.resourceid}"
									exCondition="${classesCondition}"></gh:classesAutocomplete> <font
								color="red">*</font></td>
						</tr>
						<tr>
							<td>组描述:</td>
							<td colspan="3"><textarea rows="3" cols=""
									name="groupDescript" class="required" style="width: 80%;">${bbsGroup.groupDescript }</textarea>
							</td>
						</tr>
						<tr>
							<td>组长:</td>
							<td><input type="text" id="leaderName" name="leaderName"
								value="${bbsGroup.leaderName}" style="width: 100px;"
								readonly="readonly" /> <input type="hidden" id="leaderId"
								name="leaderId" value="${bbsGroup.leaderId}" />&nbsp;&nbsp; <a
								class="button" href="javascript:;" onclick="chooseLeader();"><span>选择组长</span></a>
							</td>
							<td>组状态:</td>
							<td><gh:select dictionaryCode="CodeBbsGroupStatus"
									name="status" value="${bbsGroup.status }" /></td>
						</tr>
						<tr>
							<td>小组成员:</td>
							<td colspan="3">
								<div>
									<a class="button" href="javascript:;" onclick="chooseMember();"><span>选择成员</span></a>
									<span style="color: green; line-height: 26px;">
										(当前已有${fn:length(bbsGroup.groupUsers)}个成员) </span>
								</div> <textarea rows="5" cols="" id="memberName" readonly="readonly"
									style="width: 70%;">${bbsGroup.groupUserNames}</textarea> <input
								type="hidden" id="memberId" name="groupUserIds"
								value="${bbsGroup.groupUserIds }" />
							</td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit" onclick="return checkMembers();">提交</button>
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
	<script type="text/javascript">
		function checkMembers(){
			var memberIds = $("#bbsGroupEditForm input[name='groupUserIds']");
			var memberNames = $("#bbsGroupEditForm input[id='memberName']");
			var leaderId = $("#bbsGroupEditForm input[name='leaderId']");
			var leaderName = $("#bbsGroupEditForm input[name='leaderName']");
			if(memberIds.val()!=""&&memberIds.val().indexOf(leaderId.val())==-1){
				memberIds.val(memberIds.val()+leaderId.val()+",");
				memberNames.val(memberNames.val()+leaderName.val()+",");
			}
			return true;
		}
	</script>
</body>
</html>