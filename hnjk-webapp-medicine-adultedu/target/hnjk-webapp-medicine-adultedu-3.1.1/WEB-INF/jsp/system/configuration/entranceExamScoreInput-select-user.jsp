<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>入学考试成绩录入人设定_选择用户</title>
<script type="text/javascript">

		
	function selectThisUserFroRadio(obj){
		if(obj.checked){
			$("#paramValue_entranceExamScoreInput").val(obj.value);
			$("#paramValue_entranceExamScoreInput_show").val(obj.alt);
		}		
	}
	
	function selectThisUserFroTR(tr){
		var tdEle = tr.children[0].children[0].children[0];
		tdEle.checked = true;
		selectThisUserFroRadio(tdEle);
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
				action="${baseUrl}/edu3/system/configuration/entranceExamScoreInput-select-user.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>用户名称：</label><input type="text"
							id="entranceExamScoreInput_select_user_cnName" name="cnName"
							value="${condition['cnName']}" style="width: 120px" /></li>
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
						<th width="5%">选项</th>
						<th width="20%">姓名</th>
						<th width="65%">授于角色</th>
						<th width="10%">用户状态</th>
					</tr>
				</thead>
				<tbody id="entranceExamScoreInputSelectUserBody">
					<c:forEach items="${page.result}" var="user" varStatus="vs">
						<tr onclick="selectThisUserFroTR(this)">
							<td><input type="radio" name="resourceid"
								value="${user.resourceid }"
								onclick="selectThisUserFroRadio(this)" alt="${user.cnName }" /></td>
							<td>${user.cnName }</td>
							<td><c:forEach items="${user.roles }" var="role">
			            		${role.roleName }
			            	</c:forEach></td>
							<td><c:choose>
									<c:when test="${user.isDeleted == 1 }">
										<font color='red'><s>删除</s></font>
									</c:when>
									<c:when test="${!user.enabled }">
										<font color='red'>禁用</font>
									</c:when>
									<c:otherwise>
										<font color='green'>正常</font>
									</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl}/edu3/system/configuration/entranceExamScoreInput-select-user.html"
				pageType="sys" pageNumShown="4" targetType="dialog"
				condition="${condition}" />
		</div>
	</div>

</body>
</html>