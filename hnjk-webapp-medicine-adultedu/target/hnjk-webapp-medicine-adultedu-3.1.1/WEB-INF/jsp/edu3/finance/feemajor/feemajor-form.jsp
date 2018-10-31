<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>专业缴费类别设置</title>
<script type="text/javascript">
	$(document).ready(function(){
		$("select.flexselect").each(function(){$(this).flexselect();});
	});
</script>
</head>
<body>
	<h2 class="contentTitle">专业缴费类别设置</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/finance/feemajor/save.html" class="pageForm"
				onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid" value="${feeMajor.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<c:choose>
							<c:when test="${ empty feeMajor.resourceid }">
								<tr>
									<td width="20%">专业:</td>
									<td colspan="3">
										<gh:majorAutocomplete name="majorId"
											tabindex="1" id="feeMajorList-majorId" displayType="code"
											defaultValue="${feeMajor.major.resourceid}"
											classCss="required" style="width:25%" orderBy="majorCode" />
									</td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td width="20%">专业编号:<input type="hidden" name="majorId" value="${feeMajor.major.resourceid }" /></td>
									<td width="30%">${feeMajor.major.majorCode }</td>
									<td width="20%">专业名称:</td>
									<td width="30%">${feeMajor.major.majorName }</td>
								</tr>
							</c:otherwise>
						</c:choose>
						<c:if test="${schoolCode eq '10598' }">
							<tr>
								<td>学习形式:</td>
									<td colspan="3">
										<gh:select name="teachingType" value="${feeMajor.teachingType}"
											dictionaryCode="CodeTeachingType" style="width:25%"
											classCss="required" /><span style="color: red;">*</span>
									</td>
							</tr>
						</c:if>
						<tr>
							<td>缴费类别:</td>
							<td colspan="3">
								<gh:select name="paymentType" value="${feeMajor.paymentType}" dictionaryCode="CodePaymentType" style="width:25%"
												classCss="required" /><span style="color: red;">*</span>
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