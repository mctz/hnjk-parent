<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>编辑教材信息</title>
</head>
<body>
	<h2 class="contentTitle">编辑教材信息</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/sysmanager/textbook/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${textBook.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td style="width: 12%">课程：</td>
							<td style="width: 38%">
							 <gh:courseAutocomplete name="courseid"
								id="textBook_form_courseid" tabindex="1" displayType="code"
								style="width:140px" classCss="required"
								value="${textBook.course.resourceid}" />
								</td>
							<td style="width: 12%">教材名称</td>
							<td style="width: 38%"><input type="text" name="bookName" style="width: 50%"
								value="${textBook.bookName }" class="required"/></td> 
						</tr>
						<tr>
							
							<td style="width: 12%">书号</td>
							<td style="width: 38%"><input type="text" name="bookSerial" style="width: 50%"
								value="${textBook.bookSerial }" class="required"/></td> 
							<td style="width: 12%">出版社</td>
							<td style="width: 38%"><input type="text" name="press" style="width: 50%"
								value="${textBook.press }" class="required"/></td> 
						</tr>
						
						<tr>
							<td style="width: 12%">主编</td>
							<td style="width: 38%"><input type="text" name="editor" style="width: 50%"
								value="${textBook.editor }" class="required"/></td> 
							<td style="width: 12%">单价</td>
							<td style="width: 38%"><input type="text" name="price" style="width: 50%"
								value="${textBook.price }" class="required"/></td> 
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