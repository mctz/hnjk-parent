<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>免考认定参数设定_选择用户</title>
<script type="text/javascript">

	$(document).ready(function(){
		window.setTimeout(function(){
			var parentTd = $("#${condition['tId']}");
			var existIds = $(parentTd).find("input[name='dictChildValue']").val();
			$("#noExamApplyConfigSelectUserBody input[name='resourceid']").each(function(){
				if(existIds.indexOf($(this).val())>=0){
					$(this).attr("checked",true);
				}					
			});				
		},500);			
	});
	
	function selectThisUserFroRadio(obj){
		
		var parentTd       = $("#${condition['tId']}");
		var parentSpan     = $(parentTd).find("span");
		var parentDictName = $(parentTd).find("input[name='dictChildName']");
		var parentDictValue= $(parentTd).find("input[name='dictChildValue']");
		
		if(obj.checked){
			
			if(parentDictValue.val().indexOf(obj.value)<0){
				$(parentSpan).text($(parentSpan).text()+obj.alt+",");
				$(parentDictName).val($(parentDictName).val()+obj.alt+",");
				$(parentDictValue).val($(parentDictValue).val()+obj.value+",");
			}
			
		}else{
			
			if(parentDictValue.val().indexOf(obj.value)>=0){
				
				var textStr  = parentSpan.text().replace((obj.alt+","),"");
				var nameStr  = parentDictName.val().replace((obj.alt+","),"");
				var valueStr = parentDictValue.val().replace((obj.value+","),"");

				$(parentSpan).text(textStr);
				$(parentDictName).val(nameStr);
				$(parentDictValue).val(valueStr);

			}
		}	
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/teaching/noexamapply/config-choose-user.html?tId=${condition['tId']}"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>用户名称：</label><input type="text"
							id="noExamApplyConfig_select_user_cnName" name="cnName"
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
				<tbody id="noExamApplyConfigSelectUserBody">
					<c:forEach items="${page.result}" var="user" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
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
				goPageUrl="${baseUrl}/edu3/teaching/noexamapply/config-choose-user.html"
				pageType="sys" pageNumShown="4" targetType="dialog"
				condition="${condition}" />
		</div>
	</div>

</body>
</html>