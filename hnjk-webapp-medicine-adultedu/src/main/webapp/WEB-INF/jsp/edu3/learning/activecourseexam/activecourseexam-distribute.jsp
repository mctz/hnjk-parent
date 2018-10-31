<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看随堂练习分布情况</title>
<script type="text/javascript">
$(document).ready(function(){
	$("select[class*=flexselect]").flexselect();
});
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/metares/exercise/activeexercise/distribute.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>课程：</label> ${courseSelect}</li>
						<li><span class="tips"> 请选择您需要查询的课程 </span></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"></th>
						<th width="25%">课程</th>
						<th width="50%">知识节点</th>
						<th width="20%">随堂练习数目</th>
					</tr>
				</thead>
				<tbody>
					<c:set value="0" var="examcounts" />
					<c:forEach items="${activecourseexamDistributeList }" var="stat">
						<c:set value="${examcounts + stat.counts }" var="examcounts" />
						<tr>
							<td>&nbsp;</td>
							<td>${stat.coursename }</td>
							<td><span style="width: ${stat.nodelevel*20 }px;float:left;">&nbsp;</span>${stat.nodename }</td>
							<td>${stat.counts }</td>
						</tr>
					</c:forEach>
					<tr>
						<td colspan="3" style="text-align: right;">合计:</td>
						<td>${examcounts }</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	</script>
</body>
</html>