<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>角色管理</title>
</head>
<body>

	<h2 class="contentTitle">编辑角色</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"  class="pageForm"  id="role_Form">
				<input type="hidden" id="role_resourceid" name="resourceid" value="${role.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="15%">角色编码:</td>
							<td width="30%"><input type="text" id="role_roleCode"
								name="roleCode" value="${role.roleCode }"
								class="required alphanumeric" alt="必须以ROLE_为前缀"
								style="width: 63%" /> <span class="buttonActive" style="margin-left: 8px">
								<div class="buttonContent">
										<button type="button" onclick="validateOnlyRole()">检查唯一性</button>
									</div></span></td>
							<td width="15%">角色名称:</td>
							<td width="30%"><input type="text" name="roleName" id="role_roleName"
								value="${role.roleName }" class="required" style="width: 63%" /></td>
						</tr>
						<tr>
							<td>父角色:</td>
							<td><gh:selectModel name="parentId" bindValue="resourceid"  id="role_parentId" 
									disabled="disabled" displayValue="roleName"
									condition="resourceid<>'${role.resourceid}'"
									modelClass="com.hnjk.security.model.Role"
									value="${role.parent.resourceid}" style="width:65%" />
							<td>角色描述:</td>
							<td><input type="text" name="roleDescript" id="role_roleDescript" 
								value="${role.roleDescript }" class="required"
								style="width: 63%" /></td>
						</tr>
						<tr>
							<td>角色所属教学模式：</td>
							<td><gh:select name="roleModule" value="${role.roleModule}" id="role_roleModule" 
									dictionaryCode="CodeTeachingType" /></td>
							<td>排序:</td>
							<td><input type="text" name="showOrder" style="width: 63%" id="role_showOrder" 
								value="${role.showOrder }" /></td>
						</tr>

					</table>
					<table class="form" id="childDict">
						<tr>
							<td width="10%"><b>分配权限:</b></td>
							<td colspan="3">&nbsp; <font color="red">说明：给角色分配资源，选择相应的资源名称，勾选需要分配的功能，再提交即可</font></td>
						</tr>
						<tr>
							<td></td>
							<td colspan="3">
								<table cellpadding="0" cellspacing="0" width="100%" id="role_resourceFuncs">
									<c:forEach items="${resList}" var="res" varStatus="vs">
										<tr>
											<td width='22%'
												<c:if test="${res.resourceLevel == 0 }"> style="font-weight:bold;color: red" </c:if>
												<c:if test="${res.resourceLevel == 1 }"> style="font-weight:bold;color: green" </c:if>
												<c:if test="${res.resourceLevel == 2 }"> style="font-weight:bold;color: cornflowerblue" </c:if>
												>
												<c:forEach begin="1" end="${res.resourceLevel }" step="1">
													<span style="margin-left: 10px">&nbsp;</span>
												</c:forEach> ${res.resourceName }
											</td>
											<td
												<c:if test="${res.resourceLevel == 0 }"> style="background-color: coral;" </c:if>
												<c:if test="${res.resourceLevel == 1 }"> style="background-color: aquamarine;" </c:if>
												>
												<input type="checkbox" name="resourceFuncId" 
												<c:if test="${fn:indexOf(roleAuthoritys,res.resourceid)>-1 }"> checked="checked" </c:if>
												value="${res.resourceid }" />查看&nbsp; <!-- 遍历各菜单下的功能点 --> 
												<c:if test="${not empty res.children }">
													<c:forEach items="${res.children }" var="child"
														varStatus="vs">
														<c:if test="${child.resourceType eq 'func' or child.resourceType eq 'module' }"> &nbsp;&nbsp;									 	 	 
									 	  					<input type="checkbox" name="resourceFuncId"
																<c:if test="${fn:indexOf(roleAuthoritys,child.resourceid)>-1 }"> checked="checked" </c:if>
																value='${child.resourceid }' />
															<a href="#" title="${child.resourceDescript }"> <c:choose>
																	<c:when test="${child.resourceType eq 'module' }">
																		<font color='green'>${child.resourceName }(模块)</font>
																	</c:when>
																	<c:otherwise>${child.resourceName }</c:otherwise>
																</c:choose></a>
														</c:if>
													</c:forEach>
												</c:if></td>
										</tr>
									</c:forEach>

								</table>
							</td>
						</tr>
					</table>

				</div>

				<div class="formBar">
					<ul class="searchContent">
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" onclick="_role_validateCallback()">提交</button>
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
	function _role_validateCallback() {
		var $roleForm = $("#role_Form");
		if (!$roleForm.valid()) {
			alertMsg.error(DWZ.msg["validateFormError"]);
			return false; 
		}
		var resid=  $("#role_resourceid").val();
		var roleCode=  $("#role_roleCode").val();
		var roleName=  $("#role_roleName").val();
		var parentId=  $("#role_parentId").val();
		var roleDescript=  $("#role_roleDescript").val();
		var roleModule=  $("#role_roleModule").val();
		var showOrder=  $("#role_showOrder").val();
		var resourceFuncIds="";
		var k=0; 
	    var num  = $("#role_resourceFuncs input[name='resourceFuncId']:checked").size();
		$("#role_resourceFuncs input[name='resourceFuncId']:checked").each(function(){
			resourceFuncIds+=$(this).val();
            if(k != num -1 ) resourceFuncIds += ",";
            k++;
        }); 
		
		$.ajax({
			type:"post",
			url:"${baseUrl }/edu3/system/authoriza/role/save.html",
			data:{resourceid:resid,roleCode:roleCode,roleName:roleName,parentId:parentId,roleDescript:roleDescript,roleModule:roleModule,showOrder:showOrder,resourceFuncIds:resourceFuncIds},
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success:function(data){
				if(data.statusCode==200){
					if(data.message && alertMsg) alertMsg.correct(data.message);
                    //jQuery.post("${baseUrl}/edu3/system/authoriza/resource/recache.html",{});
					//navTab.reload(data.reloadUrl);
				} else {
					alertMsg.error(data.message);
				}
			}
		});
	}
	
	 function validateOnlyRole(){
    	var role = jQuery("#roleForm #resourceid");
    	var roleCode = jQuery("#roleForm #roleCode");
    	if(roleCode.val()==""){ alertMsg.warn("请输入角色编码"); roleCode.focus() ;return false; }
    	var url = "${baseUrl}/edu3/framework/system/authoriza/resource/validateCode.html";
    	jQuery.post(url,{roleCode:roleCode.val(),resourceid:role.val(),type:"role"},function(data){
    		if(data == "exist"){ alertMsg.warn("角色编码已存在!"); }else{ alertMsg.correct("恭喜，此编码可用！")}
    	})
    }
    
   
	</script>
</body>
</html>