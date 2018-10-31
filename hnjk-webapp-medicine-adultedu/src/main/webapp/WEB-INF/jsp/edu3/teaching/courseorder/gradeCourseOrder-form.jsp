<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年度预约权限-新增</title>
</head>
<body>
	<h2 class="contentTitle">编辑年度预约权限</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/courseorder/gradelearnbook-save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${setting.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">年度:</td>
							<td width="30%"><gh:selectModel name="yearInfo"
									bindValue="resourceid" displayValue="yearName"
									style="width:50%"
									modelClass="com.hnjk.edu.basedata.model.YearInfo"
									value="${setting.yearInfo.resourceid}" classCss="required"
									validate="Require" mes="年度" /><font color="red">*</font></td>
							<td width="20%">学期：</td>
							<td width="30%"><gh:select name="term"
									value="${setting.term}" dictionaryCode="CodeTerm" /><font
								color="red">*</font></td>
						</tr>
						<tr>
							<td width="20%">开始预约时间:</td>
							<td width="30%"><input type="text"
								value="<fmt:formatDate value='${setting.startDate}' pattern='yyyy-MM-dd HH:mm:ss'/>"
								name="startDate" class="required" readonly="readonly"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></td>
							<td width="20%">结束预约时间:</td>
							<td width="30%"><input
								value="<fmt:formatDate value='${setting.endDate}' pattern='yyyy-MM-dd HH:mm:ss' />"
								type="text" name="endDate" class="required" readonly="readonly"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></td>
						</tr>
						<tr>
							<td width="20%">预约科目上限数:</td>
							<td width="30%"><input type="text" name="limitOrderNum"
								class="required number" value="${setting.limitOrderNum }" /></td>
							<td width="20%">预约权限</td>
							<td width="30%"><select name="isOpened" class="required">
									<option value="">请选择</option>
									<option value="Y"
										<c:if test="${setting.isOpened eq 'Y' }"> selected="selected" </c:if>>开放</option>
									<option value="N"
										<c:if test="${setting.isOpened eq 'N' }">selected="selected"</c:if>>屏蔽
									</option>
							</select> <font color="red">*</font></td>
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