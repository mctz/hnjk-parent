<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业论文成绩更正</title>
</head>
<body>
	<h2 class="contentTitle">编辑毕业论文成绩</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/examresult/thesis/correct/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${examResultAudit.resourceid }" /> <input type="hidden"
					name="resultAuditId" value="${examResultAudit.resultAuditId }" /> <input
					type="hidden" name="paperOrderId"
					value="${examResultAudit.paperOrderId }" /> <input type="hidden"
					name="firstScore" value="${examResultAudit.firstScore }" /> <input
					type="hidden" name="secondScore"
					value="${examResultAudit.secondScore }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="15%">论文批次：</td>
							<td width="85%" colspan="3">${examResultAudit.batchName }</td>
						</tr>
						<tr>
							<td width="15%">学号：</td>
							<td width="35%">${examResultAudit.studyNo }</td>
							<td width="15%">姓名：</td>
							<td width="35%">${examResultAudit.studentName }</td>
						</tr>
						<tr>
							<td>更正前初评成绩：</td>
							<td><fmt:formatNumber value="${examResultAudit.firstScore }"
									pattern="###.#" /></td>
							<td>更正后初评成绩：</td>
							<td><c:choose>
									<c:when test="${not empty examResultAudit.paperOrderId }">
										<input type="text" name="changedFirstScore"
											value="<fmt:formatNumber value='${examResultAudit.changedFirstScore }' pattern='###.#'/>"
											class="required number" range="[0,100]" />
									</c:when>
									<c:otherwise>
									</c:otherwise>
								</c:choose></td>
						</tr>
						<tr>
							<td>更正前答辩成绩：</td>
							<td><fmt:formatNumber
									value="${examResultAudit.secondScore }" pattern="###.#" /></td>
							<td>更正后答辩成绩：</td>
							<td><c:choose>
									<c:when test="${not empty examResultAudit.paperOrderId }">
										<input type="text" name="changedSecondScore"
											value="<fmt:formatNumber value='${not empty examResultAudit.changedSecondScore ? examResultAudit.changedSecondScore : 0 }' pattern='###.#'/>"
											class="required number" range="[0,100]" />
									</c:when>
									<c:otherwise>
									</c:otherwise>
								</c:choose></td>
						</tr>
						<tr>
							<td>更正前终评成绩：</td>
							<td>
								${ghfn:dictCode2Val('CodeScoreChar',examResultAudit.integratedScore) }
							</td>
							<td>更正后终评成绩：</td>
							<td><select class="required" name="changedThesisScore"
								id="thesis_changedIntegratedScore">
									<option value="">请选择</option>
									<option value="2515"
										<c:if test="${examResultAudit.changedIntegratedScore eq '2515' }">selected="selected"</c:if>>优</option>
									<option value="2514"
										<c:if test="${examResultAudit.changedIntegratedScore eq '2514' }">selected="selected"</c:if>>良</option>
									<option value="2513"
										<c:if test="${examResultAudit.changedIntegratedScore eq '2513' }">selected="selected"</c:if>>中</option>
									<option value="2512"
										<c:if test="${examResultAudit.changedIntegratedScore eq '2512' }">selected="selected"</c:if>>及格</option>
									<option value="2501"
										<c:if test="${examResultAudit.changedIntegratedScore eq '2501' }">selected="selected"</c:if>>不及格</option>
							</select> <span style="color: red;">*</span></td>
						</tr>
						<tr>
							<td>备注：</td>
							<td colspan="3"><textarea rows="3" name="memo" cols=""
									style="width: 50%;">${examResultAudit.memo }</textarea></td>
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