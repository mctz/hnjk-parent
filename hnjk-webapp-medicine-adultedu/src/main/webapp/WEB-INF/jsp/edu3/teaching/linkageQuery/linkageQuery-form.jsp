<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增或编辑联动查询表单页面</title>
<script type="text/javascript">
		$(document).ready(function(){
			$("select.flexselect").each(function(){$(this).flexselect();});
		});
		
		function closeThisTab(){
			var pageNum = "${page.pageNum}";
			if(pageNum==""){
				pageNum = "1";
			}
			navTab.closeCurrentTab();
			navTabPageBreak({pageNum:pageNum});
		}
	</script>
</head>
<body>
	<h2 class="contentTitle">${empty linkageQuery.resourceid ?"新增":"编辑" }联动查询</h2>
	<div class="page">
		<div class="pageContent">
			<form id="linkageQuery-form" method="post"
				action="${baseUrl}/edu3/teaching/linkageQuery/saveOrUpdate.html"
				class="pageForm" onsubmit="return validateCallback(this)">
				<input type="hidden" name="resourceid"
					value="${linkageQuery.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="15%">教学点:</td>
							<td width="85%"><gh:brSchoolAutocomplete name="unitId"
									tabindex="1" id="linkageQuery-brSchoolId" displayType="code"
									defaultValue="${linkageQuery.unit.resourceid}"
									classCss="required" style="width:25%" /></td>
						</tr>
						<tr>
							<td width="15%">年级:</td>
							<td width="85%"><gh:selectModel id="linkageQuery-gradeId"
									name="gradeId" bindValue="resourceid" displayValue="gradeName"
									classCss="flexselect required" defaultOptionText=""
									modelClass="com.hnjk.edu.basedata.model.Grade"
									value="${linkageQuery.grade.resourceid}"
									orderBy="yearInfo.firstYear desc" style="width:25%" /></td>
						</tr>
						<tr>
							<td width="15%">层次:</td>
							<td width="85%"><gh:selectModel id="linkageQuery-cassicId"
									name="classicId" bindValue="resourceid"
									displayValue="classicName" classCss="flexselect required"
									modelClass="com.hnjk.edu.basedata.model.Classic"
									value="${linkageQuery.classic.resourceid}" orderBy="showOrder"
									style="width:25%" /></td>
						</tr>
						<tr>
							<td width="15%">学习形式:</td>
							<td width="85%"><gh:select dictionaryCode="CodeTeachingType"
									name="teachingType" id="linkageQuery-teachingType"
									value="${linkageQuery.teachingType}"
									classCss="flexselect required" style="width:25%" /></td>
						</tr>
						<tr>
							<td width="15%">专业:</td>
							<td width="85%"><gh:majorAutocomplete name="majorId"
									tabindex="1" id="linkageQuery-majorId" displayType="code"
									defaultValue="${linkageQuery.major.resourceid}"
									classCss="required" style="width:25%" orderBy="majorCode" /></td>
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
									<button type="button" class="close" onclick="closeThisTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>