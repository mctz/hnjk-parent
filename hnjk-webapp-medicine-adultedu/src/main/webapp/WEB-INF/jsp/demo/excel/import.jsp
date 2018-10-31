<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Excel导入</title>
</head>

<body>

	<div class="tabs" curentIndex="0" eventType="click">
		<div class="tabsHeader">
			<div class="tabsHeaderContent">
				<ul>
					<li><a href="javascript:void(0)"><span>导入</span></a></li>
					<li><a href="javascript:void(1)"><span>导出</span></a></li>
				</ul>
			</div>
		</div>
		<div class="tabsContent" style="height: 150px;">
			<div>
				<div style="color: green; padding: 4px">
					1.在etc/excel/excel-config.xml文件中配置excel数据文件与model的映射关系;<br />
					2.使用方法，具体请参见com.hnjk.platform.system.controller.WebpluginDemoController中的showExcelExportImport<br />
					及doExcelExportExport两个方法.
				</div>
				<h3>
					<c:if test="${success eq true }">导入成功！</c:if>
					<c:if test="${success eq false }">导入失败！</c:if>
				</h3>
				<form method="post"
					action="${baseUrl }/edu3/system/demo/webplugin/excel/show.html?act=import"
					enctype="multipart/form-data">
					请选择导入的Excel文件：<input name="importFile" id="importFile" type="file" />
					<a
						href="${baseUrl }/edu3/system/demo/webplugin/excel/downtemplate.html">模板文件下载</a>
					<p>
						<input type="submit" name="submit" value="导入" />
					</p>
				</form>
				<div>
					<c:if test="${success eq true }">
						<table class="list" border="0">
							<tr class="head_bg">
								<td>序号</td>
								<td>教学站</td>
								<td>姓名</td>
								<td>性别</td>
								<td>年龄</td>
								<td>出生日期</td>
								<td>课程1</td>
								<td>分数</td>
								<td>课程2</td>
								<td>分数</td>
							</tr>
							<c:forEach items="${studentList}" var="student" varStatus="vs">
								<tr <c:if test='${vs.index%2!=0 }'>class='dataspace_bg'</c:if>>
									<td>${student.sort }</td>
									<td>${student.className }</td>
									<td>${student.studentName }</td>
									<td>${student.sex }</td>
									<td>${student.age }</td>
									<td><fmt:formatDate value="${student.birthDate }"
											pattern="yyyy-MM-dd" /></td>
									<td>${student.course1 }</td>
									<td>${student.score1 }</td>
									<td>${student.course2 }</td>
									<td>${student.score2 }</td>
								</tr>
							</c:forEach>
						</table>
					</c:if>
				</div>
			</div>
			<div>
				<c:if test="${success eq true }">上传成功！</c:if>
				<c:if test="${success eq false }">上传失败！</c:if>
				</h3>
				<p>
					<a
						href="${baseUrl }/edu3/system/demo/webplugin/excel/export.html?act=default">1)默认导出</a>
				<div style="color: green; padding: 4px">说明：按默认的配置导出为excel.</div>
				</p>
				<p>
					<a
						href="${baseUrl }/edu3/system/demo/webplugin/excel/showcustomerexport.html"
						target="dialog" mask="true">2)自定义导出</a>
				<div style="color: green; padding: 4px">说明：按自定义的列导出为excel.</div>
				</p>
				<p>
					<a
						href="${baseUrl }/edu3/system/demo/webplugin/excel/export.html?act=template">3)按模板导出</a>
				<div style="color: green; padding: 4px">说明：按定义好的模板导出为excel.需要在WEB-INF/templates/excel目录下，定义好模板.</div>
				</p>
				<p>4)多工作表导出</p>
			</div>

		</div>
		<div class="tabsFooter">
			<div class="tabsFooterContent"></div>
		</div>
	</div>

</body>
</html>