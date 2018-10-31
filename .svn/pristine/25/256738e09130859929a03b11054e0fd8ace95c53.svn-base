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
				var existIds = $("#${param.idsN}").val();
				$("#masterBody input[name=resourceid]").each(function(){
					if(existIds.indexOf($(this).val())>=0){
						$(this).attr("checked",true);
					}
				});
			},500);
			
		});
						
		function clickThis(obj){
			if(obj.checked){				
				if($("#${param.idsN}").val().indexOf(obj.value)<0){
					$("#${param.idsN}").val($("#${param.idsN}").val()+obj.value+",");
					$("#${param.namesN}").val($("#${param.namesN}").val()+$(obj).attr('rel')+",");
				}
			}else{
				if($("#${param.idsN}").val().indexOf(obj.value)>=0){
					var ids = $("#${param.idsN}").val().replace((obj.value+","),"");
					var names = $("#${param.namesN}").val().replace(($(obj).attr('rel')+","),"");
					$("#${param.idsN}").val(ids);
					$("#${param.namesN}").val(names);
				}
			}
		}
		
		function clickAll(obj){
			var arr = $("#masterBody input[name='resourceid']").get();
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
			<div class="pageHeader">
				<form onsubmit="return dialogSearch(this);"
					action="${baseUrl}/edu3/metares/bbssection/master.html"
					method="post">
					<div class="searchBar">
						<ul class="searchContent">
							<li><label>教学站：</label>
							<gh:selectModel name="unitId" bindValue="resourceid"
									displayValue="unitName" style="width:120px"
									modelClass="com.hnjk.security.model.OrgUnit"
									value="${condition['unitId']}" condition="unitType='brSchool'" />
								<input type="hidden" name="idsN" value="${param.idsN}" /> <input
								type="hidden" name="namesN" value="${param.namesN}" /></li>
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
						<ul class="searchContent">
							<li><label>登录名：</label><input type="text" name="userName"
								value="${condition['userName']}" style="width: 115px" /></li>
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
					<tbody id="masterBody">
						<c:forEach items="${userlist.result}" var="u" varStatus="vs">
							<tr>
								<td><input type="checkbox" name="resourceid"
									value="${u.resourceid }" rel="${u.cnName }"
									onclick="clickThis(this)" /></td>
								<td>${u.username}</td>
								<td>${u.cnName}</td>
								<td>${u.orgUnit.unitName}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<gh:page page="${userlist}"
					goPageUrl="${baseUrl }/edu3/metares/bbssection/master.html"
					pageNumShown="3" pageType="sys" targetType="dialog"
					condition="${condition}" />
			</div>
		</div>
</body>