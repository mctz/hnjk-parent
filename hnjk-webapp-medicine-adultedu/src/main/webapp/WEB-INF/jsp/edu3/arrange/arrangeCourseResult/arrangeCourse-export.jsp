<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/common.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>发布课表</title>
</head>
<body>
<div class="page">
	<div class="pageContent">	
		<table class="table" layouth="140" border="1">
			<thead>
				<tr>
		            <th align="center" width="8%">教学班名</th>  
		            <th align="center" width="8%">班级信息</th>
		            <th align="center" width="5%">上课学期</th>
		            <th align="center" width="3%">层次</th>  
		            <th align="center" width="5%">课程名称</th>
		            <th align="center" width="3%" >主讲老师</th>
		           <!--  <th align="center" width="6%" >老师电话</th> -->
		            <th align="center" width="5%" >星期一</th>
		            <th align="center" width="5%" >星期二</th>
		            <th align="center" width="5%" >星期三</th>
		            <th align="center" width="5%" >星期四</th>
		            <th align="center" width="5%" >星期五</th>
		            <th align="center" width="5%" >星期六</th>
		            <th align="center" width="5%" >星期天</th>
				</tr>
			</thead>
			<tbody>
		        <c:forEach items="${list}" var="item" varStatus="vs">
			        <tr>
			            <td align="center">${item.teachCourse.teachingClassname }</td>
			            <td align="center">${item.className }</td>
			            <td align="center">${ghfn:dictCode2Val('CodeCourseTermType',item.teachCourse.openTerm) }</td>
			            <td align="center">${item.teachCourse.classic.classicName }</td>
			            <td align="center">${item.teachCourse.courseName }</td>
			            <td align="center">${item.teacherName}&nbsp;</td>
			           <%--  <td align="center" title="${item.teacher.mobile}">${item.teacher.mobile}</td> --%>
			       		<c:forEach items="${item.timePeriod}" var="t" varStatus="index">
			       			<td align="center" title="${fn:replace(item.classroom[index.count-1], "；", "&#13;&#10;")}">${t}</td>
			       		</c:forEach>
			        </tr>
		      		</c:forEach>
			</tbody>
		</table>
	</div>
</div>
</body>
</html>