<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<style>
.MzTreeViewCell0 {
	border-bottom: 1px solid #CCCCCC;
	padding: 0px;
	margin: 0px;
}

.MzTreeViewCell1 {
	border-bottom: 1px solid #CCCCCC;
	border-left: 1px solid #CCCCCC;
	width: 200px;
	padding: 0px;
	margin: 0px;
	display: none;
}

#treeDIV input {
	vertical-align: middle;
}
</style>

<script language="javaScript"
	src="${baseUrl}/jscript/mztree/MzTreeView12.js"></script>
<script type="text/javascript">
		$(document).ready(function(){
			window.setTimeout(function(){
				var codes = $("#${param.codesN}").val();
				if(codes!=""){
					var arr = codes.split(",");
					var isAll = true;
					$("input[name='messageRoleCheckbox']").each(function(){
						if(jQuery.inArray($(this).val(), arr)!=-1){
							$(this).attr("checked",true);
						}
						if($(this).attr("checked")==false){
							isAll = false;
						}
					});
					if(isAll){
						$("#messageRolesCheckboxAll").attr("checked",true);
					}
				}
			},500);
		});
		
		function selectCm() {
			var codes = "";
			var names = "";
			$("input[name='messageRoleCheckbox']").each(function(){
				if($(this).attr("checked")){
					codes += $(this).val()+",";
					names += $(this).attr("rel")+",";
				}
			});
			if(codes!=""){
				codes = codes.substring(0,codes.length-1);
			    names = names.substring(0,names.length-1);
			}
			
			$("#${param.codesN}").val(codes);
			$("#${param.namesN}").val(names);
			// 关闭
			$("#closeBTN").click();
		}
		
		function checkAllRoles(obj){
			$("input[name='messageRoleCheckbox']").each(function(){
				$(this).attr("checked",$(obj).attr("checked"));
			});
		}
	</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<div class="subBar">
				<ul>
					<li><div class="buttonActive" style="padding-right: 8px">
							<div class="buttonContent">
								<button type="submit" onclick='selectCm()'>确定</button>
							</div>
						</div></li>
					<li><div class="buttonActive" style="padding-right: 8px">
							<div class="buttonContent">
								<button type="submit" class="close" id="closeBTN">取消</button>
							</div>
						</div></li>
				</ul>
			</div>
		</div>

		<div class="pageContent">
			<table class="table" layouth="120">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox"
							id="messageRolesCheckboxAll" onclick="checkAllRoles(this)" /></th>
						<th>角色名称</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${rolelist }" var="role">
						<tr>
							<td><input type="checkbox" name="messageRoleCheckbox"
								value="${role.roleCode }" rel="${role.roleName }" /></td>
							<td>${role.roleName }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>