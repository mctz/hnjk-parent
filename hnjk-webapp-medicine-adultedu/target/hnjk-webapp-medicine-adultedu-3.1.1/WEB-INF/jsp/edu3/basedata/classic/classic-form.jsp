<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>层次设置</title>
</head>
<body>
	<h2 class="contentTitle">编辑年度</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/sysmanager/classic/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${classic.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">层次名称:</td>
							<td width="30%"><input type="text" name="classicName"
								style="width: 50%" value="${classic.classicName }"
								class="required" /></td>
							<td width="20%">层次英文名称:</td>
							<td width="30%"><input type="text" name="classicEnName"
								style="width: 50%" value="${classic.classicEnName }"
								class="required" /></td>
						</tr>
						<tr>
							<td>起点:</td>
							<td><input type="text" name="startPoint" style="width: 50%"
								value="${classic.startPoint }" class="required" /></td>
							<td>终点:</td>
							<td><input type="text" name="endPoint" style="width: 50%"
								value="${classic.endPoint }" class="required" /></td>
						</tr>
						<tr>
							<td>简称:</td>
							<td><input type="text" name="shortName" style="width: 50%"
								value="${classic.shortName }" class="required" /></td>
							<td>层次代码:</td>
							<td><select name="classicCode" class="required"
								style="width: 53%">
									<c:forEach begin="0" end="9" var="classicCode">
										<option value="${classicCode}"
											<c:if test="${classic.classicCode eq classicCode}">selected="selected"</c:if>>${classicCode}</option>
									</c:forEach>
							</select></td>
						</tr>
						<tr>
							<td>层次编码(用于编学号):</td>
							<td><input type="text" name="classicCode4StudyNo" style="width: 50%"
								value="${classic.classicCode4StudyNo }" /></td>
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