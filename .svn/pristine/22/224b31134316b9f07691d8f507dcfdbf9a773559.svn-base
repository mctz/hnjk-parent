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
			</A> → 用户信息
		</DIV>
	</DIV>
</DIV>

<div>
	<table cellspacing="1" cellpadding="3" align="" width="80%"
		class="tableborder1">
		<tbody>
			<tr>
				<td width="50%" class="tablebody1" style="text-align: center;">
					<DIV>
						<c:choose>
							<c:when test="${not empty bbsUserInfo.userface }">
								<img src="${bbsUserInfo.userface }" width="55" height="55" />
							</c:when>
							<c:otherwise>
								<img src="${baseUrl }/themes/default/images/person.png"
									width="55" height="55" />
							</c:otherwise>
						</c:choose>
					</DIV>
					<DIV>
						<IMG style="MARGIN: 5px 0px"
							src="${baseUrl }/style/default/images/start0.gif"> <FONT
							face=Verdana color=#61b713><B>${bbsUserInfo.userName}</B>
						</FONT>
					</DIV>
				</td>
				<td width="50%" class="tablebody1" style="text-align: left;"><c:if
						test="${not empty bbsUserInfo.studentInfo}">
						<DIV>学号：${bbsUserInfo.studentInfo.studyNo}</DIV>
						<DIV>年级：${bbsUserInfo.studentInfo.grade.gradeName}</DIV>
						<DIV>专业：${bbsUserInfo.studentInfo.major.majorName}</DIV>
					</c:if>
					<DIV>姓名：${bbsUserInfo.sysUser.cnName}</DIV>
					<DIV>发帖数量：${bbsUserInfo.topicCount}</DIV>
					<DIV>
						教学站：
						<c:choose>
							<c:when test="${not empty bbsUserInfo.studentInfo}">${bbsUserInfo.studentInfo.branchSchool.unitName}</c:when>
							<c:otherwise>${bbsUserInfo.sysUser.orgUnit.unitName}</c:otherwise>
						</c:choose>

					</DIV></td>
			</tr>
			<tr>
				<td valign="middle" align="center" style="text-align: center;"
					colspan="2" class="tablebody2"><a style="color: #0B7AC0;"
					href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/index.html${querys}">&lt;&lt;
						返回首页 &nbsp;&nbsp;</a> <a style="color: #0B7AC0;"
					href="javascript:window.close();">关闭本页</a>&nbsp;&nbsp;</td>
			</tr>
		</tbody>
	</table>
</div>
<BR>
<%@ include file="/WEB-INF/jsp/edu3/learning/bbssection/bbs-footer.jsp"%>
