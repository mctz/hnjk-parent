<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教师管理</title>
<script type="text/javascript">
	//新增
	function addTeacher(){
		navTab.openTab('navTab', '${baseUrl}/edu3/teaching/edumanager/edit.html', '新增教师');
	}
	
	//修改
	function modifyTeacher(){
		var url = "${baseUrl}/edu3/teaching/edumanager/edit.html";
		if(isCheckOnlyone('resourceid','#tedumanagerBody')){
			navTab.openTab('editTeacherTab', url+'?resourceid='+$("#tedumanagerBody input[name='resourceid']:checked").val(), '编辑教师');
		}			
	}
		
	//删除
	function deleteTeacher(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/edumanager/delete.html","#tedumanagerBody");
	}
	function setTeacherEnable(){
		pageBarHandle("您确定要激活这些账号吗？","${baseUrl}/edu3/framework/system/org/user/updateuserstatus.html?statusCode=Y&fwPage=2","#tedumanagerBody");
	}
	
	function setTeacherDisEnable(){
		pageBarHandle("您确定要禁用这些账号吗？","${baseUrl}/edu3/framework/system/org/user/updateuserstatus.html?statusCode=N&fwPage=2","#tedumanagerBody");
	}
	
	//导出教师信息
// 	function exportTeacher(){		
// 		window.location.href="${baseUrl}/edu3/teaching/edumanager/export.html?"+$("#teacherSearchForm").serialize();
// 	}
	//下载教师导入模板
	function exportModelTeacher(){
		window.location.href="${baseUrl}/edu3/teaching/edumanager/exportModel.html";
	}
	//导入教师信息
	function importTeacher(){
		var url ="${baseUrl}/edu3/teaching/edumanager/upload.html";
		$.pdialog.open(url,"RES_BASEDATA_TEACHER_IMPORT","导入教师信息", {mask:true,width:450, height:210});
	}
	
	function exportTeacherInfo(){
		var cnName = "${condition['cnName']}";
		var teacherCode = "${condition['teacherCode']}";
		var unitId = "${condition['unitId']}";
		var teacherType = "${condition['teacherType']}";
		var teachingType = "${condition['teachingType']}";
		var username = "${condition['username']}";
		var titleOfTechnical = "${condition['titleOfTechnical']}";
		var educationalLevel = "${condition['educationalLevel']}";
		var courseId = "${condition['courseId']}";
		var param = "?operatorType=export"+"&cnName="+cnName+"&teacherCode="+teacherCode+"&unitId="+unitId+"&teacherType="
				+teacherType+"&teachingType="+teachingType+"&username="+username+"&titleOfTechnical="+titleOfTechnical
				+"&titleOfTechnical="+titleOfTechnical+"&educationalLevel="+educationalLevel+"&courseId="+courseId;
		var url ="${baseUrl}/edu3/teaching/edumanager/exportTeacherInfo.html";
		alertMsg.confirm("你确定要导出当前查询出来的教师信息吗？", {
			okCall : function() {
				downloadFileByIframe(url + param, '_tgradeIframe');
			}
		});
		
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form id="teacherSearchForm" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/edumanager/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>姓名：</label><input type="text" name="cnName"
							value="${condition['cnName']}" /></li>
						<li><label>教师编号：</label><input type="text" name="teacherCode"
							value="${condition['teacherCode']}" /></li>
						<c:if test="${not isBrschool }">
							<li class="custom-li"><label>所属单位：</label> <gh:brSchoolAutocomplete
									name="unitId" tabindex="1" id="edumanager_orgUnitName"
									scope="all" displayType="code"
									defaultValue="${condition['unitId']}" /></li>
						</c:if>
					</ul>
					<%-- 
			<ul class="searchContent">
				<li>
					<label>教师角色：</label>
					<select name="teacherType">
						<option value="">请选择</option>
						<c:forEach items="${roleList }" var="role" varStatus="vs">
							<option value="${role.roleCode }" <c:if test="${role.roleCode eq condition['teacherType'] }"> selected</c:if> >${role.roleName }</option>
						</c:forEach>
					</select>
				</li>
				<li>
					<label>教学类型：</label><gh:select name="teachingType" value="${condition['teachingType']}" dictionaryCode="CodeTeachingType"/>
				</li>	
			</ul> --%>
					<div class="subBar">
						<ul>
							<li><span class="tips">提示：更多查询条件请点击高级查询</span></li>
							<div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div>
							<a class="buttonActive"
								href="${baseUrl }/edu3/teaching/edumanager/list.html?con=advance"
								target="dialog" rel="edumanager_advance" title="查询条件"><span>高级查询</span></a>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_ESTAB_TEACHER" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_tp"
							onclick="checkboxAll('#check_all_tp','resourceid','#tedumanagerBody')" /></th>
						<th width="11%">教师编号</th>
						<th width="11%">姓名</th>
						<th width="11%">登录帐号</th>

						<th width="20%">所属单位</th>
						<%-- 	
		        	<th width="10%">教师角色</th> 
		        	<th width="8%">教师类型</th> --%>

						<th width="6%">性别</th>
						<th width="10%">职称</th>
						<th width="11%">手机</th>
						<th width="8">是否班主任</th>
						<th width="8%">账号状态</th>
					</tr>
				</thead>
				<tbody id="tedumanagerBody">
					<c:forEach items="${edumanagerListPage.result}" var="edumanager"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${edumanager.resourceid }" autocomplete="off" /></td>
							<td>${edumanager.teacherCode }</td>
							<td><a
								href="${baseUrl }/edu3/framework/edumanager/view.html?userId=${edumanager.resourceid }"
								target="dialog" title="${edumanager.cnName }" width="700"
								height="500" rel="edumanager_view">${edumanager.cnName }</a></td>
							<td>${edumanager.username }</td>

							<td>${edumanager.orgUnit.unitName }</td>
							<%--  <td>
			            	<c:forEach items="${edumanager.roles}" var="role" varStatus="vss">
			            		${role.roleName }
			            		<c:if test="${not vss.last }">,</c:if>
			            	</c:forEach>
			            </td>
			            <td>${ghfn:dictCode2Val('CodeTeachingType',edumanager.teachingType ) }</td>	  --%>

							<td>${ghfn:dictCode2Val('CodeSex',edumanager.gender ) }</td>
							<td>${ghfn:dictCode2Val('CodeTitleOfTechnicalCode',edumanager.titleOfTechnical ) }</td>
							<td>${edumanager.mobile }</td>
							<td><c:choose>
									<c:when test="${edumanager.isMaster == 'Y' }">是</c:when>
									<c:when test="${edumanager.isMaster == 'N' }">否</c:when>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${edumanager.isDeleted == 1 }">
										<font color='red'><s>删除</s></font>
									</c:when>
									<c:when test="${!edumanager.enabled }">
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

			<gh:page page="${edumanagerListPage}"
				goPageUrl="${baseUrl }/edu3/teaching/edumanager/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
