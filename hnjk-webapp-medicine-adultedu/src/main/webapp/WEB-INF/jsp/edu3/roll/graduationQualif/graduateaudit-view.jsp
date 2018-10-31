<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看毕业审核数据</title>

</head>
<body>

	<h2 class="contentTitle">查看毕业审核数据</h2>
	<div class="page">
		<div class="pageContent">


			<div class="pageFormContent" layoutH="97">
				<div class="tabs">
					<div class="tabsHeader">
						<div class="tabsHeaderContent">
							<ul>
								<li class="selected"><a href="#"><span>毕业审核数据</span></a></li>
							</ul>
						</div>
					</div>
					<div class="tabsContent" style="height: 100%;">
						<div>
							<table class="form" id="changeTab">
								<tr>
									<td width="20%">学生姓名:</td>
									<td width="30%">${info.studentName}</td>
									<td width="20%">学生学号:</td>
									<td width="30%">${info.studyNo}</td>
								</tr>
								<tr>
									<td width="20%">年级:</td>
									<td width="30%">${info.grade.gradeName}</td>
									<td width="20%">教学站:</td>
									<td width="30%">${info.branchSchool.unitName}</td>
								</tr>
								<tr>
									<td width="20%">专业:</td>
									<td width="30%">${info.major.majorName}</td>
									<td width="20%">层次:</td>
									<td width="30%">${info.classic.classicName}</td>
								</tr>
								<tr>
									<td width="20%">限选课要求:</td>
									<td width="30%">${lim_t}</td>
									<td width="20%">限选课修读情况:</td>
									<td width="30%"><c:if test="${lim_t>lim_s}">
											<font color="red">
										</c:if> ${lim_s} <c:if test="${lim_t>lim_s}">
											</font>
										</c:if></td>
								</tr>
								<tr>
									<td width="20%">必修课总分要求:</td>
									<td width="30%">${nes_t}</td>
									<td width="20%">必修课修读情况:</td>
									<td width="30%"><c:if test="${nes_t>nes_s}">
											<font color="red">
										</c:if> ${nes_s} <c:if test="${nes_t>nes_s}">
											</font>
										</c:if></td>
								</tr>
								<tr>
									<td width="20%">总学分要求:</td>
									<td width="30%">${tol_t}</td>
									<td width="20%">总学分修读情况:</td>
									<td width="30%"><c:if test="${tol_t>tol_s}">
											<font color="red">
										</c:if> ${tol_s} <c:if test="${tol_t>tol_s}">
											</font>
										</c:if></td>
								</tr>
								<tr>
									<td width="20%">申请毕业:</td>
									<td colspan="3" width="80%"><c:if test="${app_s=='Y'}">申请学位、毕业</c:if>
										<c:if test="${app_s=='N'}">申请毕业</c:if> <c:if
											test="${app_s=='W'}">
											<font color="red">申请延迟毕业</font>
										</c:if></td>
								</tr>
								<tr>
									<td width="20%">入学资格审核:</td>
									<td colspan="3" width="80%"><c:if test="${ent_s=='Y'}">已通过</c:if>
										<c:if test="${ent_s=='N'}">
											<font color="red">未通过</font>
										</c:if></td>
								</tr>
								<tr>
									<td width="20%">学籍状态:</td>
									<td colspan="3" width="80%"><c:if
											test="${sta_s!=11 and sta_s!=21 and sta_s!=25}">
											<font color="red">
										</c:if> ${ghfn:dictCode2Val("CodeStudentStatus",sta_s)} <c:if
											test="${sta_s!=11 and sta_s!=21 and sta_s!=25}">
											</font>
										</c:if></td>
								</tr>
								<tr>
									<td width="20%">年限:</td>
									<td width="30%">${length}</td>
									<td width="20%">可申请毕业年度:</td>
									<td width="30%"><c:choose>
											<c:when test="${fn:split(gra,'.')[1]=='5'}">${fn:split(gra,'.')[0]}年秋季</c:when>
											<c:otherwise>${fn:split(gra,'.')[0]}年春季</c:otherwise>
										</c:choose></td>
								</tr>
							</table>
						</div>
					</div>
					<div class="tabsFooter">
						<div class="tabsFooterContent"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>