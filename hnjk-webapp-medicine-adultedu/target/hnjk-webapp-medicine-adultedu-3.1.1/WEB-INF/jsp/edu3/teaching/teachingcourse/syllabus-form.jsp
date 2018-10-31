<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>知识结构树管理</title>
</head>
<body>
	<h2 class="contentTitle">编辑知识结构树</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/syllabus/save.html"
				id="syllabusForm" class="pageForm"
				onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${syllabus.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td style="width: 10%">课程名称:</td>
							<td style="width: 40%"><input type="text" style="width: 50%"
								value="${syllabus.course.courseName }" readonly="readonly" /> <input
								type="hidden" name="courseId"
								value="${syllabus.course.resourceid }" /></td>
							<td>父节点:</td>
							<td><select name="syllabusId">
									<option value=""
										<c:if test="${empty syllabus.parent.resourceid}"> selected </c:if>>请选择</option>
									<c:forEach items="${syllabusList}" var="syb" varStatus="vs">
										<option value="${syb.resourceid }"
											<c:if test="${syb.resourceid eq syllabus.parent.resourceid}"> selected </c:if>>
											<c:forEach var="seconds" begin="1" end="${syb.syllabusLevel}"
												step="1">&nbsp;&nbsp;</c:forEach> ${syb.syllabusName }
										</option>
									</c:forEach>
							</select> <!-- <td><input type="text" style="width:50%" value="${syllabus.parent.syllabusName }" readonly="readonly"/>
										  <input type="hidden" name="syllabusId" value="${syllabus.parent.resourceid }"/> -->
							</td>
						</tr>
						<tr>
							<td style="width: 10%">知识节点名称:</td>
							<td style="width: 40%"><input type="text" name="node_Name"
								style="width: 50%" value="${syllabus.syllabusName }"
								class="required" /></td>
							<td>节点类型:</td>
							<td><gh:select name="syllabusType"
									value="${syllabus.syllabusType}"
									dictionaryCode="CodeTeachingNodeType" /></td>

						</tr>
						<tr>
							<td>教学要求度:</td>
							<td><gh:select name="required" value="${syllabus.required}"
									dictionaryCode="CodeTeachingRequest" style="width:52%" /></td>
							<td>能力目标:</td>
							<td><gh:select name="abilityTarget"
									value="${syllabus.abilityTarget}"
									dictionaryCode="CodeTeachingAbilityTarget" /></td>
						</tr>
						<tr>
							<td>学时分配:</td>
							<td><input type="text" name="provideStydyHour"
								style="width: 50%" value="${syllabus.provideStydyHour }"
								class="number" /></td>
							<td>排序号:</td>
							<td><input type="text" name="showOrder"
								value="${syllabus.showOrder }" /></td>
						</tr>
						<tr>
							<td>节点内容:</td>
							<td colspan="3"><textarea name="syllabusContent" rows="5"
									cols="" style="width: 50%">${syllabus.nodeContent }</textarea></td>
						</tr>
						<tr>
							<td>备注:</td>
							<td colspan="3"><textarea name="memo" rows="5" cols=""
									style="width: 50%">${syllabus.memo }</textarea></td>
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