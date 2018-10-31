<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课件复制课程选择</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/teachingcourse/copy.html"
				class="pageForm"
				onsubmit="return validateCallback(this,dialogAjaxDone);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">来源课程:</td>
							<td>${fromCourse.courseName } <input type="hidden"
								name="fromCourseId" value="${fromCourse.resourceid }" />
							</td>
						</tr>
						<tr>
							<td>复制到课程:</td>
							<td><gh:courseAutocomplete name="toCourseId" tabindex="2"
									id="course_copy_courseId" displayType="code" style="width:80%"
									value="" classCss="required" /></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">复制课件</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="$.pdialog.closeCurrent();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>