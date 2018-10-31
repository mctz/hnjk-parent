<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>访问日志列表</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form onsubmit="return validateCallback(this,dialogAjaxDone);"
				action="${baseUrl }/edu3/system/accesslogs/import.html"
				method="post">
				<table class="table" layouth="81">
					<thead>
						<tr>
							<th width="5%">&nbsp;</th>
							<th width="50%">访问日志文件</th>
							<th width="45%">大小</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${accessLogsFiles}" var="f" varStatus="vs">
							<tr>
								<td><input type="radio" value="${f.key }"
									name="accesslogsPath" /></td>
								<td>${f.name }</td>
								<td>${ghfn:formatFileSize(f.value) }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">导入</button>
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
		</div>
	</div>
</body>
</html>