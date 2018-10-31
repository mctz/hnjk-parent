<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的老师</title>
</head>
<body>

	<div class="page">
		<div class="pageContent">
			<table class="table" layouth="96">
				<thead>
					<tr>
						<th width="5%">&nbsp;</th>
						<th width="20%">班主任姓名</th>
						<th width="20%">联系电话</th>
					</tr>
				</thead>
				<tbody id="taskdBody">
					<c:if test="${not empty studentInfo.classes.classesMaster }">
						<td></td>
						<td>${studentInfo.classes.classesMaster }</td>
						<td>${studentInfo.classes.classesMasterPhone }</td>
					</c:if>
				</tbody>
			</table>
			<%-- 
		<table class="table" layouth="96">
			<thead>
				<tr>
					<th width="5%">&nbsp;</th>		        	      
		            <th width="20%">课程</th> 		            
		            <th width="25%">主讲老师(邮箱)</th>
		            <th width="25%">辅导老师(邮箱)</th>
				</tr>
			</thead>
			<tbody id="taskdBody">
		       <c:forEach items="${listStudentTask}" var="t" varStatus="vs">
			        <tr>
			        	<td>&nbsp;</td>
			            <td>${t.course.courseName }</td>
			            <td>${t.teacherName } <c:if test="${not empty emails[t.teacherId] }">(${emails[t.teacherId] })</c:if></td>
			            <td>		            
			            	<c:forEach items="${fn:split(t.assistantIds, ',')}" var="tid" varStatus="vs">	
			            		<c:if test="${not empty tid }">		            	
			            		${ghfn:ids2Names('user',tid)} <c:if test="${not empty emails[t.teacherId] }">(${emails[tid] })</c:if>
			            		<c:if test="${not vs.last }">，</c:if>
			            		</c:if>
			            	</c:forEach>
			            </td>
			        </tr>
	       		</c:forEach>
			</tbody>
		</table>
		 --%>
		</div>
	</div>
</body>
</html>