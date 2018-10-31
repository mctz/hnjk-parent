<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/edu3/learning/bbssection/bbs-header.jsp"%>

<DIV id=topLayout>
	<!--导航-->
	<DIV class=notice>
		<DIV
			style="PADDING-LEFT: 10px; FLOAT: left; WIDTH: auto; TEXT-ALIGN: left"
			class="STYLE1">
			<A
				href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/index.html${querys}"
				class="STYLE1"> <c:choose>
					<c:when test="${empty course}">广东学苑在线</c:when>
					<c:otherwise>${course.courseName }课程论坛</c:otherwise>
				</c:choose>
			</A> → 论坛提示
		</DIV>
	</DIV>
</DIV>

<div>
	<table cellspacing="1" cellpadding="3" align="center"
		class="tableborder1">
		<tbody>
			<tr>
				<td width="100%" colspan="2" class="tablebody1"
					style="text-align: center;"><strong>${errorMsg }</strong></td>
			</tr>
			<tr>
				<td valign="middle" align="center" style="text-align: center;"
					colspan="2" class="tablebody2"><a style="color: #0B7AC0;"
					href="javascript:history.go(-1);">&lt;&lt; 返回上一页</a>&nbsp;&nbsp; <a
					style="color: #0B7AC0;" href="javascript:window.close();">关闭本页</a>&nbsp;&nbsp;
					<a style="color: #0B7AC0;"
					href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/index.html${querys}">返回首页
						&gt;&gt;</a></td>
			</tr>
		</tbody>
	</table>
</div>
<BR>
<%@ include file="/WEB-INF/jsp/edu3/learning/bbssection/bbs-footer.jsp"%>
