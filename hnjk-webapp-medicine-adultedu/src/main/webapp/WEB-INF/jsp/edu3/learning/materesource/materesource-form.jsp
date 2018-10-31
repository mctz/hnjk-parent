<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程素材资源管理</title>
</head>
<body>
	<h2 class="contentTitle">编辑课程素材资源</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/metares/courseware/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${mateResource.resourceid }" /> <input type="hidden"
					name="channelType" value="${mateResource.channelType }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td style="width: 12%">所属知识节点:</td>
							<td style="width: 88%" colspan="3"><input type="text"
								style="width: 40%"
								value="${mateResource.syllabus.syllabusName }"
								readonly="readonly" /> <input type="hidden" name="syllabusId"
								value="${mateResource.syllabus.resourceid }" /></td>
						</tr>
						<tr>
							<td style="width: 12%">材料编码:</td>
							<td style="width: 38%"><input type="text" name="mateCode"
								value="${mateResource.mateCode}" class="required number"
								style="width: 30%" /></td>
							<td style="width: 12%">材料类型:</td>
							<td style="width: 38%"><gh:select name="mateType"
									value="${mateResource.mateType }" dictionaryCode="CodeMateType"
									style="width:128px" classCss="required" /><font color="red">*</font></td>
						</tr>
						<tr>
							<td>材料名称:</td>
							<td><input type="text" name="mateName" style="width: 50%"
								value="${mateResource.mateName }" class="required" /></td>
							<td>是否发布:</td>
							<td><gh:select name="isPublished"
									value="${mateResource.isPublished }" dictionaryCode="yesOrNo"
									style="width:128px" /></td>
						</tr>
						<tr>
							<td>URL路径:</td>
							<td colspan="3"><input type="text" name="mateUrl"
								value="${mateResource.mateUrl }" style="width: 80%"
								class="required" /></td>
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