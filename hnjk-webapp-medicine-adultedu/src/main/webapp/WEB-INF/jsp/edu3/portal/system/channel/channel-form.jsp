<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>栏目管理</title>

<script type="text/javascript">
	
</script>
</head>
<body>
	<h2 class="contentTitle">编辑栏目</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/portal/manage/channel/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<input type="hidden" name="resourceid"
							value="${channel.resourceid }" />
						<tr>
							<td style="width: 12%">栏目名称:</td>
							<td style="width: 38%"><input type="text" name="channelName"
								size="34" value="${channel.channelName }" class="required" /></td>
							<td>栏目内容:</td>
							<td><input type="text" name="channelContent" size="40"
								value="${channel.channelContent }" class="required" /></td>
						</tr>
						<tr>
							<td style="width: 12%">栏目状态:</td>
							<td style="width: 38%"><select name="channelStatus" size="1"
								validate="Require" mes="栏目名称">
									<option value="${channel.channelStatus }"
										<c:if test="${channel.channelStatus == 1 }"> SELECTED </c:if>>停用</option>
									<option value="${channel.channelStatus }"
										<c:if test="${channel.channelStatus == 0 }"> SELECTED </c:if>>启用</option>
							</select></td>
							<td style="width: 12%">父栏目:</td>
							<td style="width: 38%"><gh:channel
									channelList="${allChanList }" viewType="select"
									defaultValue="${channel.parent.resourceid }" /></td>
						</tr>
						<tr>
							<td style="width: 12%">栏目类型:</td>
							<td style="width: 38%"><gh:select name="channelType"
									value="${channel.channelType}" dictionaryCode="channelType"
									style="width:33%" classCss="required" /></td>
							<td style="width: 12%">栏目位置:</td>
							<td style="width: 38%"><gh:select name="channelPosition"
									value="${channel.channelPosition}"
									dictionaryCode="channelPosition" style="width:53%"
									classCss="required" /></td>
						</tr>
						<tr>
							<td style="width: 12%">链接:</td>
							<td colspan="3"><input type="text" name="channelHref"
								id="channelHref" size="60" value="${channel.channelHref}" />&nbsp;&nbsp;
								<a href="#" class="button"
								onclick="javascript:$.pdialog.open('${baseUrl }/edu3/framework/chanel/selector/article.html?idsN=channelHref','article-selector','关联文章',{height:600,width:800,mask:true});"><span>关联文章</span></a>
							</td>
						</tr>
						<tr>
							<td style="width: 12%">是否为教学站栏目:</td>
							<td style="width: 38%"><input type="checkbox"
								name="isOpenBrSchool" value="Y"
								<c:if test="${channel.isOpenBrSchool eq 'Y' }">checked="checked"</c:if> />
								是</td>
							<td style="width: 12%">排序:</td>
							<td style="width: 38%"><input type="text" name="showOrder"
								value="${channel.showOrder }" size="6" /> <span
								style="padding: 4px; color: red">数字越小越靠前</span></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<!-- 隐藏域 -->
									<input type="hidden" name="fillinMan"
										value="${channel.fillinMan }" /> <input type="hidden"
										name="fillinManId" value="${channel.fillinManId }" /> <input
										type="hidden" name="fillinDate"
										value=" <fmt:formatDate value="${channel.fillinDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" />
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