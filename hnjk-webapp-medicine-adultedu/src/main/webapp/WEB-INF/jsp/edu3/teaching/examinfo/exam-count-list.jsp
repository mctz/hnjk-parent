<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试信息统计</title>
</head>
<script type="text/javascript">

	
</script>
<body>
	<div class="page">
		<div class="pageHeader"></div>

		<div class="pageContent">
			<table class="table" layouth="64">
				<thead>
					<tr>
						<th width="15%">考试批次</th>
						<th width="15%">教学点</th>
						<th width="15%">课程</th>
						<th width="15%">已安排机考人数</th>
					</tr>
				</thead>
				<tbody id="examCountBody">
					<c:forEach items="${examcontlist.result }" var="ecl" varStatus="vs">
						<tr>
							<td>${ecl.batchname }</td>
							<td>${ecl.unitname }</td>
							<td>${ecl.coursename }</td>
							<td>${ecl.ct }</td>


						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${examcontlist}"
				goPageUrl="${baseUrl }/edu3/teaching/examinfo/count/list.html"
				condition="${condition }" pageType="sys" />
		</div>
	</div>
</body>
</html>