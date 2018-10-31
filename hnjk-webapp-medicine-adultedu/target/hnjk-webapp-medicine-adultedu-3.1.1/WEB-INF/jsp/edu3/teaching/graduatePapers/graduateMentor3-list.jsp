<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>已分配老师</title>
<script type="text/javascript">
	//function ctx787(){
	//	$.pdialog.reload("${baseUrl}/edu3/teaching/graduatePapers/chooseTeacher.html?batchId=${batchId}&mentor="+$('#mentor').val());
	//}
		
	function clickThis678(obj){
		if(obj.checked){
			$("#guidTeacherId2").val(obj.value);
			$("#guidTeacherName2").val(obj.alt);
		}		
	}
	
	function clickThisRow978(tr){
		var tdEle = tr.children[0].children[0].children[0];
		tdEle.checked = true;
		clickThis678(tdEle);
	}
	
	$(document).ready(function(){
			// 修改高度
			$("#cd_right").css("height",parseInt($(".dialogContent").css("height")));
			
			window.setTimeout(function(){
				var existId = $("#guidTeacherId2").val();
				$("#gMentorBody input[name=resourceid]").each(function(){
					if(existId==$(this).val()){
						$(this).attr("checked",true);
					}
				});
			},500);
			
	});
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/teaching/graduatePapers/chooseTeacher.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>导师名称：</label><input type="text" id="mentor"
							name="mentor" value="${condition['mentor']}" style="width: 120px" />
							<input type="hidden" name="batchId" value="${batchId}" /></li>
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
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%">&nbsp;</th>
						<th width="10%">教师编号</th>
						<th width="10%">姓名</th>
						<th width="10%">登录帐号</th>
						<th width="10%">性别</th>
						<th width="10%">职称</th>
						<th width="10%">手机</th>
						<th width="10%">账号状态</th>
					</tr>
				</thead>
				<tbody id="gMentorBody">
					<c:forEach items="${mentorList.result}" var="mentor" varStatus="vs">
						<tr onclick="clickThisRow978(this)">
							<td><input type="radio" name="resourceid"
								value="${mentor.edumanager.resourceid }"
								onclick="clickThis678(this)" alt="${mentor.edumanager.cnName }" /></td>
							<td>${mentor.edumanager.teacherCode }</td>
							<td>${mentor.edumanager.cnName }</td>
							<td>${mentor.edumanager.username }</td>
							<td>${ghfn:dictCode2Val('CodeSex',mentor.edumanager.gender ) }</td>
							<td>${ghfn:dictCode2Val('CodeTitleOfTechnicalCode',mentor.edumanager.titleOfTechnical ) }</td>
							<td>${mentor.edumanager.mobile }</td>
							<td><c:choose>
									<c:when test="${mentor.edumanager.isDeleted == 1 }">
										<font color='red'><s>删除</s></font>
									</c:when>
									<c:when test="${!mentor.edumanager.enabled }">
										<font color='red'>未注册</font>
									</c:when>
									<c:otherwise>
										<font color='green'>正常</font>
									</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${mentorList}"
				goPageUrl="${baseUrl}/edu3/teaching/graduatePapers/chooseTeacher.html"
				pageType="sys" pageNumShown="4" targetType="dialog"
				condition="${condition}" />
		</div>
	</div>

</body>
</html>