<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<table width="100%;">
	<c:set var="tmpType" value="" />
	<c:forEach items="${courseLearningGuids }" var="c" varStatus="vs">
		<c:if test="${c.type ne tmpType }">
			<c:set var="tmpType" value="${c.type }" />
			<tr>
				<td>
					<h5>${ghfn:dictCode2Val('CodeCourseLearningGuidType',c.type) }</h5>
				</td>
			</tr>
		</c:if>
		<tr>
			<td>
				<div style="line-height: 170%; font-size: 13px">${c.content }
				</div>
			</td>
		</tr>
	</c:forEach>
</table>
