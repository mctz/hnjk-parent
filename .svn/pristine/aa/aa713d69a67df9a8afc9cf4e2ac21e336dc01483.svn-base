<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>访问统计排行</title>
</head>
<body>

	<div class="page">
		<div class="pageContent" layoutH="0">
			<div class="tabs">
				<div class="tabsHeader">
					<div class="tabsHeaderContent">
						<ul>
							<li><a href="#"><span>并发访问用户数TOP10</span></a></li>
							<li><a href="#"><span>访问次数最多的用户TOP10</span></a></li>
							<li><a href="#"><span>访问最多的资源TOP10</span></a></li>
							<li><a href="#"><span>处理最慢的资源TOP10</span></a></li>
							<li><a href="#"><span>500错误最多的资源TOP10</span></a></li>
						</ul>
					</div>
				</div>
				<div class="tabsContent" style="height: 100%;">
					<!-- 1 -->
					<div>
						<table class="list" style="width: 96%">
							<thead>
								<tr>
									<th width="5%"></th>
									<th width="50%">访问时间</th>
									<th width="45%">用户数</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${list1}" var="a" varStatus="vs">
									<tr>
										<td>${vs.index+1 }</td>
										<td><fmt:formatDate value="${a['accesstime'] }"
												pattern="yyyy-MM-dd HH:mm:ss" /></td>
										<td>${a['ipcount'] }</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div>
						<table class="list" style="width: 96%">
							<thead>
								<tr>
									<th width="5%"></th>
									<th width="50%">用户IP</th>
									<th width="45%">访问次数</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${list2}" var="a" varStatus="vs">
									<tr>
										<td>${vs.index+1 }</td>
										<td>${a['ipaddress'] }</td>
										<td>${a['ipcount'] }</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div>
						<table class="list" style="width: 96%">
							<thead>
								<tr>
									<th width="5%"></th>
									<th width="30%">资源名称</th>
									<th width="35%">资源路径</th>
									<th width="30%">访问次数</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${list3}" var="a" varStatus="vs">
									<tr>
										<td>${vs.index+1 }</td>
										<td>${ghfn:resourcePath2Names(a['url'])}</td>
										<td>${a['url']}</td>
										<td>${a['morecount'] }</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div>
						<table class="list" style="width: 96%">
							<thead>
								<tr>
									<th width="5%"></th>
									<th width="25%">资源名称</th>
									<th width="30%">资源路径</th>
									<th width="20%">处理时间</th>
									<th width="20%">访问时间</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${list4}" var="a" varStatus="vs">
									<tr>
										<td>${vs.index+1 }</td>
										<td>${ghfn:resourcePath2Names(a['url'])}</td>
										<td>${a['url']}</td>
										<td><fmt:formatNumber value="${a['runningtime'] }"
												pattern="0.000" /></td>
										<td><fmt:formatDate value="${a['accesstime']}"
												pattern="yyyy-MM-dd HH:mm:ss" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div>
						<table class="list" style="width: 96%">
							<thead>
								<tr>
									<th width="5%"></th>
									<th width="30%">资源名称</th>
									<th width="35%">资源路径</th>
									<th width="30%">错误个数</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${list5}" var="a" varStatus="vs">
									<tr>
										<td>${vs.index+1 }</td>
										<td>${ghfn:resourcePath2Names(a['url'])}</td>
										<td>${a['url']}</td>
										<td>${a['count500'] }</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<div class="tabsFooter">
					<div class="tabsFooterContent"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>