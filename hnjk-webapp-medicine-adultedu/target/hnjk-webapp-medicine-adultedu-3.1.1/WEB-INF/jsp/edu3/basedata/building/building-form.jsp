<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学楼管理</title>
</head>
<body>
	<h2 class="contentTitle">${(empty building.resourceid)?'新增':'编辑' }教学楼</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/sysmanager/building/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${building.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">教学楼名称:</td>
							<td width="30%"><input type="text" name="buildingName"
								style="width: 50%" value="${building.buildingName }"
								class="required" /></td>
							<td width="20%">所属教点:</td>
							<td width="30%"><gh:selectModel name="branchSchoolId"
									bindValue="resourceid" displayValue="unitName"
									modelClass="com.hnjk.security.model.OrgUnit"
									value="${building.branchSchool.resourceid}" style="width:52%"
									classCss="required" disabled="${brschool?'disabled':'' }" /><font
								color="red">*</font> <c:if test="${brschool }">
									<input type="hidden" name="branchSchoolId"
										value="${building.branchSchool.resourceid}" />
								</c:if></td>
						</tr>
						<tr>
							<td>最高楼层:</td>
							<td><input type="text" name="maxLayers" style="width: 50%"
								value="${building.maxLayers }" class="required number" /></td>
							<td>每层最大单元数:</td>
							<td><input type="text" name="maxUnits" style="width: 50%"
								value="${building.maxUnits }" class="required number" /></td>
						</tr>
						<tr>
							<td>电话:</td>
							<td><input type="text" name="phone" style="width: 50%"
								value="${building.phone }" /></td>
							<td>排序号:</td>
							<td><input type="text" name="showOrder" style="width: 50%"
								value="${building.showOrder }" /></td>
						</tr>
						<tr>
							<td>备注:</td>
							<td colspan="3"><textarea name="memo" style="width: 50%"
									cols="" rows="3">${building.memo }</textarea></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
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
</body>
</html>