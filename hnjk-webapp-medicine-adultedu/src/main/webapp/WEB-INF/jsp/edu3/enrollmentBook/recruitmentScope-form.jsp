<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生范围管理</title>
</head>
<body>
	<h2 class="contentTitle">编辑招生范围</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/enrollment/booking/recruitmentScope/save.html" class="pageForm"
				onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid" value="${scope.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="15%">教学点:</td>
							<td width="85%">
								<gh:brSchoolAutocomplete name="unitId"  tabindex="2"
										id="recruitmentScope_form_unitId"
										defaultValue="${scope.unit.resourceid}" displayType="code" style="width:40%;" />
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>服务地区:</td>
							<td>
								<gh:select name="areaScope" value="${scope.areaScope}" size="150"
									dictionaryCode="CodeRecruitmentScope" style="width:40%" classCss="required" />
							</td>
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