<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看学位审核数据</title>

</head>
<body>

	<h2 class="contentTitle">查看学位审核数据</h2>
	<div class="page">
		<div class="pageContent">


			<div class="pageFormContent" layoutH="97">
				<div class="tabs">
					<div class="tabsHeader">
						<div class="tabsHeaderContent">
							<ul>
								<li class="selected"><a href="#"><span>学位审核数据</span></a></li>
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
									<td colspan="3" width="30%">${info.studyNo}</td>
								</tr>
								<tr>
									<td width="20%">年级:</td>
									<td width="30%">${info.grade.gradeName}</td>
									<td width="20%">教学站:</td>
									<td colspan="3" width="30%">${info.branchSchool.unitName}</td>
								</tr>
								<tr>
									<td width="20%">专业:</td>
									<td width="30%">${info.major.majorName}</td>
									<td width="20%">层次:</td>
									<td colspan="3" width="30%">${info.classic.classicName}</td>
								</tr>
								<tr>
									<td width="20%">主干课要求:</td>
									<td width="30%">10门主干课平均分为70分或以上</td>
									<td width="20%">主干课修读门数:</td>
									<td colspan="3" width="30%">${sumNumMainCourse}</td>
								</tr>
								<tr>
									<td width="20%">主干课修读总分:</td>
									<td width="30%">${sumScoreMainCourse}</td>
									<td width="20%"></td>
									<td colspan="3" width="30%"></td>
								</tr>
								<tr>
									<td width="20%"><b>课程名称</b></td>
									<td width="30%"><b>课程属性</b></td>
									<td width="20%"><b>考试时间</b></td>
									<td width="10%"><b>卷面成绩</b></td>
									<td width="10%"><b>总评成绩</b></td>
									<td width="10%"><b>备注</b></td>
								</tr>
								<c:forEach items="${gdList}" var="gdCourse">
									<tr>
										<td width="20%">${fn:split(gdCourse,'|')[0]}:</td>
										<td colspan="5" width="80%"><font color="red">${ghfn:dictCode2Val('CodeScoreChar',fn:split(fn:split(gdCourse,'|')[1],'.')[0]) }
										</font></td>
									</tr>
								</c:forEach>
								<c:forEach items="${flList}" var="flCourse">
									<tr>
										<td width="20%">${fn:split(flCourse,'|')[0]}:</td>
										<td colspan="5" width="80%">${fn:split(flCourse,'|')[1]}</td>
									</tr>
								</c:forEach>
								<c:forEach items="${mainCourseList}" var="mainCourse">
									<tr>
										<td width="20%">${fn:split(mainCourse,'|')[0]}:</td>
										<td width="30%"><font color="blue"><b>${fn:split(mainCourse,'|')[1]}</b></font></td>
										<td width="20%">-</td>
										<td width="10%">${fn:split(mainCourse,'|')[2]}</td>
										<td width="10%">${fn:split(fn:split(mainCourse,'|')[3],':')[0]}</td>
										<td width="10%">${fn:split(fn:split(mainCourse,'|')[3],':')[1]}</td>
									</tr>
								</c:forEach>

								<!-- 12-12-19补充需要学位专业基础课  学位主干课1 学位主干课2-->
								<c:forEach items="${list3001}" var="l3001">
									<tr>
										<td width="20%">${fn:split(l3001,'|')[0]}:</td>
										<td width="30%"><font color="blue"><b>${fn:split(l3001,'|')[1]}</b></font></td>
										<td width="20%">${fn:split(l3001,'|')[4]}</td>
										<td width="10%">${fn:split(l3001,'|')[2]}</td>
										<td width="10%">${fn:split(fn:split(l3001,'|')[3],':')[0]}</td>
										<td width="10%">${fn:split(fn:split(l3001,'|')[3],':')[1]}</td>
									</tr>
								</c:forEach>
								<c:forEach items="${list3002}" var="l3002">
									<tr>
										<td width="20%">${fn:split(l3002,'|')[0]}:</td>
										<td width="30%"><font color="blue"><b>${fn:split(l3002,'|')[1]}</b></font></td>
										<td width="20%">${fn:split(l3002,'|')[4]}</td>
										<td width="10%">${fn:split(l3002,'|')[2]}</td>
										<td width="10%">${fn:split(fn:split(l3002,'|')[3],':')[0]}</td>
										<td width="10%">${fn:split(fn:split(l3002,'|')[3],':')[1]}</td>
									</tr>
								</c:forEach>
								<c:forEach items="${list3003}" var="l3003">
									<tr>
										<td width="20%">${fn:split(l3003,'|')[0]}:</td>
										<td width="30%"><font color="blue"><b>${fn:split(l3003,'|')[1]}</b></font></td>
										<td width="20%">${fn:split(l3003,'|')[4]}</td>
										<td width="10%">${fn:split(l3003,'|')[2]}</td>
										<td width="10%">${fn:split(fn:split(l3003,'|')[3],':')[0]}</td>
										<td width="10%">${fn:split(fn:split(l3003,'|')[3],':')[1]}</td>
									</tr>
								</c:forEach>
								<c:forEach items="${majorBaseList}" var="majorBaseCourse">
									<tr>
										<td width="20%">${fn:split(majorBaseCourse,'|')[0]}:</td>
										<td width="30%">${fn:split(majorBaseCourse,'|')[1]}</td>
										<td width="20%">-</td>
										<td width="10%">${fn:split(majorBaseCourse,'|')[2]}</td>
										<td width="10%">${fn:split(fn:split(majorBaseCourse,'|')[3],':')[0]}</td>
										<td width="10%">${fn:split(fn:split(majorBaseCourse,'|')[3],':')[1]}</td>
									</tr>
								</c:forEach>
								<c:forEach items="${majorList}" var="majorCourse">
									<tr>
										<td width="20%">${fn:split(majorCourse,'|')[0]}:</td>
										<td width="30%">${fn:split(majorCourse,'|')[1]}</td>
										<td width="20%">-</td>
										<td width="10%">${fn:split(majorCourse,'|')[2]}</td>
										<td width="10%">${fn:split(fn:split(majorCourse,'|')[3],':')[0]}</td>
										<td width="10%">${fn:split(fn:split(majorCourse,'|')[3],':')[1]}</td>
									</tr>
								</c:forEach>
								<c:forEach items="${list1100}" var="l1100">
									<tr>
										<td width="20%">${fn:split(l1100,'|')[0]}:</td>
										<td width="30%">${fn:split(l1100,'|')[1]}</td>
										<td width="20%">-</td>
										<td width="10%">${fn:split(l1100,'|')[2]}</td>
										<td width="10%">${fn:split(fn:split(l1100,'|')[3],':')[0]}</td>
										<td width="10%">${fn:split(fn:split(l1100,'|')[3],':')[1]}</td>
									</tr>
								</c:forEach>
								<c:forEach items="${list1200}" var="l1200">
									<tr>
										<td width="20%">${fn:split(l1200,'|')[0]}:</td>
										<td width="30%">${fn:split(l1200,'|')[1]}</td>
										<td width="20%">-</td>
										<td width="10%">${fn:split(l1200,'|')[2]}</td>
										<td width="10%">${fn:split(fn:split(l1200,'|')[3],':')[0]}</td>
										<td width="10%">${fn:split(fn:split(l1200,'|')[3],':')[1]}</td>
									</tr>
								</c:forEach>
								<c:forEach items="${list1400}" var="l1400">
									<tr>
										<td width="20%">${fn:split(l1400,'|')[0]}:</td>
										<td width="30%">${fn:split(l1400,'|')[1]}</td>
										<td width="20%">-</td>
										<td width="10%">${fn:split(l1400,'|')[2]}</td>
										<td width="10%">${fn:split(fn:split(l1400,'|')[3],':')[0]}</td>
										<td width="10%">${fn:split(fn:split(l1400,'|')[3],':')[1]}</td>
									</tr>
								</c:forEach>
								<c:forEach items="${list1600}" var="l1600">
									<tr>
										<td width="20%">${fn:split(l1600,'|')[0]}:</td>
										<td width="30%">${fn:split(l1600,'|')[1]}</td>
										<td width="20%">-</td>
										<td width="10%">${fn:split(l1600,'|')[2]}</td>
										<td width="10%">${fn:split(fn:split(l1600,'|')[3],':')[0]}</td>
										<td width="10%">${fn:split(fn:split(l1600,'|')[3],':')[1]}</td>
									</tr>
								</c:forEach>
								<c:forEach items="${list2010}" var="l2010">
									<tr>
										<td width="20%">${fn:split(l2010,'|')[0]}:</td>
										<td width="30%">${fn:split(l2010,'|')[1]}</td>
										<td width="20%">-</td>
										<td width="10%">${fn:split(l2010,'|')[2]}</td>
										<td width="10%">${fn:split(fn:split(l2010,'|')[3],':')[0]}</td>
										<td width="10%">${fn:split(fn:split(l2010,'|')[3],':')[1]}</td>
									</tr>
								</c:forEach>
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