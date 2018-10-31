<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>精品课程栏目</title>
</head>
<body>
	<h2 class="contentTitle">${(empty courseChannel.resourceid)?'新增':'编辑' }精品课程栏目</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/resources/coursechannel/save.html"
				class="pageForm"
				onsubmit="return validateCallback(this,dialogAjaxDone);">
				<input type="hidden" name="resourceid"
					value="${courseChannel.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="12%">课程:</td>
							<td colspan="3"><input type="text" size="30"
								style="width: 50%" value="${courseChannel.course.courseName }"
								readonly="readonly" /> <input type="hidden" name="courseId"
								value="${courseChannel.course.resourceid }" /></td>
						</tr>
						<tr>
							<td style="width: 12%">栏目名称:</td>
							<td style="width: 38%"><input type="text" name="channelName"
								size="34" value="${courseChannel.channelName }" class="required" />
							</td>
							<td style="width: 12%">排序:</td>
							<td style="width: 38%"><input type="text" name="showOrder"
								value="${courseChannel.showOrder }" size="6" /></td>
						</tr>
						<tr>
							<td>栏目内容:</td>
							<td colspan="3"><textarea rows="3" cols="" class="required"
									style="width: 75%;" name="channelContent">${courseChannel.channelContent }</textarea>
							</td>
						</tr>
						<tr>
							<td style="width: 12%">栏目状态:</td>
							<td style="width: 38%"><select name="channelStatus" size="1">
									<option value="1"
										<c:if test="${courseChannel.channelStatus eq 1 }"> selected="selected" </c:if>>停用</option>
									<option value="0"
										<c:if test="${courseChannel.channelStatus eq 0 }"> selected="seelcted" </c:if>>启用</option>
							</select></td>
							<td style="width: 12%">父栏目:</td>
							<td style="width: 38%"><select name="parentId"
								style="width: 50%;">
									<c:forEach items="${channelList }" var="c">
										<option value="${c.resourceid }"
											<c:if test="${c.resourceid eq courseChannel.parent.resourceid }">selected="selected"</c:if>>
											<c:forEach begin="1" end="${c.channelLevel}">
					              &nbsp;&nbsp;&nbsp;
					             </c:forEach>${c.channelName }
										</option>
									</c:forEach>
							</select></td>
						</tr>
						<tr>
							<td style="width: 12%">栏目类型:</td>
							<td style="width: 38%"><gh:select name="channelType"
									value="${courseChannel.channelType}"
									dictionaryCode="channelType" classCss="required" /></td>
							<td style="width: 12%">栏目位置:</td>
							<td style="width: 38%"><gh:select name="channelPosition"
									value="${courseChannel.channelPosition}"
									dictionaryCode="channelPosition" classCss="required" /></td>
						</tr>
						<tr>
							<td style="width: 12%">链接:</td>
							<td colspan="3"><input type="text" name="channelHref"
								id="coursechannelHref" style="width: 50%;"
								value="${courseChannel.channelHref}" />&nbsp;&nbsp; <gh:select
									dictionaryCode="CodeCourseResourceHref" id="courseResourceHref"
									name="courseResourceHref" style="margin-left:20px;width:120px;"
									onchange="courseResourceHrefOnChange(this);" /></td>
						</tr>
					</table>
					<div style="color: blue; padding: 8px">
						*说明： <br />1.如果栏目为系统上定制的功能，请选择"普通页面"或"本地链接"栏目类型,<br />
						其中"普通页面"在本窗口打开,"本地链接"在新窗口打开;<br /> 链接地址请在给定的链接列表中选择. <br />2.如果栏目为非系统上的功能，栏目类型请选择"外部链接",链接地址请填写完整的url地址.
						<br />3.如果为"学习阶段栏目",栏目类型请选择"专栏"，并定义为一级栏目.
					</div>
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
									<button type="button" class="close">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
			<script type="text/javascript">
	function courseResourceHrefOnChange(obj){
		$("#coursechannelHref").val($(obj).val());
	}
	</script>
		</div>
	</div>
</body>
</html>