<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<style>
* {
	font-size: 12px;
}

.type {
	text-align: center;
	line-height: 28px;
}
</style>
<script type="text/javascript">
		$(document).ready(function(){
			window.setTimeout(function(){
				var existIds = $("#${usersN}").val();
				$("#messageUsersBody input[name=resourceid]").each(function(){
					if(existIds.indexOf($(this).val())>=0){
						$(this).attr("checked",true);
					}					
				});				
			},500);			
		});
					
		function clickThis(obj){
			if(obj.checked){				
				if($("#${usersN}").val().indexOf(obj.value)<0){
					$("#${usersN}").val($("#${usersN}").val()+obj.value+",");
					$("#${namesN}").val($("#${namesN}").val()+$(obj).attr('rel')+",");
				}
			}else{
				if($("#${param.usersN}").val().indexOf(obj.value)>=0){
					var ids = $("#${usersN}").val().replace((obj.value+","),"");
					var names = $("#${namesN}").val().replace(($(obj).attr('rel')+","),"");
					$("#${usersN}").val(ids);
					$("#${namesN}").val(names);
				}
			}
		}
		
		function clickAll(obj){
			var arr = $("#messageUsersBody input[name='resourceid']").get();
			for(i=0;i<arr.length;i++){
				arr[i].checked = obj.checked;
				clickThis(arr[i]);
			}
		}	
		
	</script>
</head>

<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/portal/message/user.html" method="post">
				<div class="searchBar">
					<c:if test="${condition['sendto'] ne 'unit' }">
						<ul class="searchContent">
							<li><label>教学站：</label> <c:choose>
									<c:when test="${brschool_student }">
										<input type="text" value="${unitName }" style="width: 120px"
											readonly="readonly" />
										<input type="hidden" name="unitId"
											value="${condition['unitId']}" />
									</c:when>
									<c:otherwise>
										<gh:brSchoolAutocomplete name="unitId" tabindex="1"
											id="message_unitId" style="width:120px"
											defaultValue="${condition['unitId']}" scope="all" />
									</c:otherwise>
								</c:choose></li>
							<li><label>用户类型：</label> <select name="userType"
								style="width: 120px;">
									<option value=""
										${(empty condition['userType'])?'selected':'' }>请选择</option>
									<option value="student"
										${(condition['userType'] eq 'student')?'selected':'' }>学生</option>
									<option value="edumanager"
										${(condition['userType'] eq 'edumanager')?'selected':'' }>教务人员</option>
							</select></li>
						</ul>
					</c:if>
					<ul class="searchContent">
						<li><label>登录名：</label><input type="text" name="userName"
							value="${condition['userName']}" style="width: 115px" /> <c:if
								test="${condition['sendto'] eq 'unit' }">
								<input type="hidden" name="unitId"
									value="${condition['unitId']}" />
								<input type="hidden" name="userType"
									value="${condition['userType']}" />
								<input type="hidden" name="sendto"
									value="${condition['sendto']}" />
							</c:if> <input type="hidden" name="usersN" value="${usersN}" /> <input
							type="hidden" name="namesN" value="${namesN}" /></li>
						<li><label>姓名：</label><input type="text" name="cnName"
							value="${condition['cnName']}" style="width: 115px" /></li>
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
										<button type="button" class="close">确 定</button>
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
						<th width="10%"><input type="checkbox" name="checkall"
							onclick="clickAll(this)" /></th>
						<th width="30%">用户名</th>
						<th width="30%">姓名</th>
						<th width="30%">教学站</th>
					</tr>
				</thead>
				<tbody id="messageUsersBody">
					<c:forEach items="${userlist.result}" var="u" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${u.username }" rel="${u.cnName }"
								onclick="clickThis(this)" /></td>
							<td>${u.username}</td>
							<td>${u.cnName}</td>
							<td>${u.orgUnit.unitName}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${userlist}"
				goPageUrl="${baseUrl }/edu3/portal/message/user.html" pageType="sys"
				targetType="dialog" pageNumShown="3" condition="${condition}" />
		</div>
	</div>
</body>