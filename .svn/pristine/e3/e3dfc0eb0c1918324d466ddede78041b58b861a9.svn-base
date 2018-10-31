<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考场教室</title>
</head>
<body>
	<h2 class="contentTitle">${(empty examroom.resourceid)?'新增':'编辑' }考室</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/sysmanager/examroom/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${examroom.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">考室名称:</td>
							<td width="30%"><input type="text" name="examroomName"
								style="width: 50%" value="${examroom.examroomName }"
								class="required" /></td>
							<td width="20%">所属教学站:</td>
							<td width="30%"><gh:selectModel name="branchSchoolId"
									bindValue="resourceid" displayValue="unitName"
									modelClass="com.hnjk.security.model.OrgUnit"
									value="${examroom.branchSchool.resourceid}"
									condition="unitType='brSchool'" style="width:52%"
									classCss="required" disabled="${brschool?'disabled':'' }" /> <c:if
									test="${brschool }">
									<input type="hidden" name="branchSchoolId"
										value="${examroom.branchSchool.resourceid}" />
								</c:if></td>
						</tr>
						<tr>
							<td>考室容量:</td>
							<td><input type="text" name="examroomSize"
								style="width: 50%" value="${examroom.examroomSize }"
								class="required" /></td>
							<td>排序:</td>
							<td><input type="text" name="showOrder" style="width: 50%"
								value="${examroom.showOrder }" /></td>
						</tr>
						<tr>
							<td>单隔位容量:</td>
							<td><input type="text" name="singleSeatNum"
								style="width: 50%" value="${examroom.singleSeatNum }" /></td>
							<td>双隔位容量:</td>
							<td><input type="text" name="doubleSeatNum"
								style="width: 50%" class="required"
								value="${examroom.doubleSeatNum }" /></td>
						</tr>
						<tr>
							<td>是否机房:</td>
							<td><gh:select name="isComputerRoom"
									value="${examroom.isComputerRoom }" dictionaryCode="yesOrNo" />
							</td>
							<td></td>
							<td></td>
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