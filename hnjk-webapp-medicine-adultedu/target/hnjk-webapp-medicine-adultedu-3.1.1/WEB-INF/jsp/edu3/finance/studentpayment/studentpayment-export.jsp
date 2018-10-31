<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>缴费情况统计</title>
<style type="text/css">
.feeHeadHide th .gridCol {
	height: 0px;
}

#stufee_stat_pageContent2 th {
	text-align: center;
	vertical-align: middle;
	font-weight: bold;
}
</style>


</head>
<body>
	<div class="page">
		<div class="pageContent" id="stufee_stat_pageContent2">
			<div class="tabs"
				<c:if test="${not empty condition['currentIndex'] }">currentIndex="${condition['currentIndex'] }"</c:if>>
				<%-- <div class="tabsHeader">
				<div class="tabsHeaderContent">
					<ul>
						<c:forEach items="${statTypes }" var="statType" varStatus="vss">
						<li><a href="#" onclick="changeStatTypeText(this);" rel="${vss.index}">
						<span>
							<c:choose>
					        	<c:when test="${statType eq 'teachingType' }">办学模式</c:when>
					        	<c:when test="${statType eq 'major' }">专业</c:when>
					        	<c:when test="${statType eq 'classic' }">层次</c:when>
					        	<c:otherwise>学习中心</c:otherwise>
					        </c:choose>
						</span>
						</a>
						</li>
						</c:forEach>
					</ul>
				</div>
			</div> --%>
				<div class="tabsContent" style="height: 100%;">
					<%-- <c:forEach items="${statTypes }" var="statType"> --%>
					<div style="width: 100%">
						<table class="_self_table" layoutH="168" border="1">
							<thead>
								<tr>
									<th width="8%" rowspan="2">自然年度</th>
									<th width="14%" rowspan="2"
										<c:if test="${statType eq 'teachingType' }">colspan="1"</c:if>>
										<c:choose>
											<c:when test="${statType eq 'teachingType' }">办学模式</c:when>
											<c:when test="${statType eq 'major' }">专业</c:when>
											<c:when test="${statType eq 'classic' }">层次</c:when>
											<c:otherwise>学习中心</c:otherwise>
										</c:choose>
									</th>
									<th width="14%" colspan="2">应缴费用</th>
									<th width="14%" colspan="2">完费</th>
									<th width="14%" colspan="2">减免费用</th>
									<th width="14%" colspan="2">部分费用</th>
									<th width="14%" colspan="2">欠费</th>
									<th width="8%" rowspan="2">完费率</th>
								</tr>
								<tr>
									<th width="7%">人数</th>
									<th width="7%">金额</th>
									<th width="7%">人数</th>
									<th width="7%">金额</th>
									<th width="7%">人数</th>
									<th width="7%">金额</th>
									<th width="7%">人数</th>
									<th width="7%">金额</th>
									<th width="7%">人数</th>
									<th width="7%">金额</th>
								</tr>
								<%-- <tr class="feeHeadHide">
					            <th width="7%"> </th>
					            <c:choose>
					            	<c:when test="${statType eq 'teachingType' }">
					            	<th width="6%"> </th>
						        	<th width="8%"> </th>
					            	</c:when>
					            	<c:otherwise><th width="14%"> </th></c:otherwise>
					            </c:choose>			
					            <th width="15%"> </th>			        
					            <th width="7%"> </th>
					            <th width="7%"> </th>
					            <th width="7%"> </th>
					            <th width="7%"> </th>
					            <th width="7%"> </th>
					            <th width="7%"> </th>
					            <th width="7%"> </th>
					            <th width="7%"> </th>
					            <th width="7%"> </th>
					            <th width="7%"> </th>
					            <th width="8%"> </th>
						    </tr> --%>
							</thead>
							<tbody>
								<c:forEach items="${statListMap[statType] }" var="stat"
									varStatus="vs">
									<tr>
										<td
											<c:if test="${not vs.last }">class="stufee_merge_${statType }"</c:if>
											style="text-align: center;" title="${stat.firstyear }">${stat.firstyear }</td>
										<c:choose>
											<c:when test="${statType eq 'teachingType' }">

												<c:if test="${not vs.last }">
													<td class="stufee_merge0">${ghfn:dictCode2Val('CodeTeachingType',stat.statType) }</td>
												</c:if>

												<c:if test="${vs.last }">
													<td class="stufee_merge0">${stat.statType }</td>
												</c:if>

											</c:when>
											<c:otherwise>
												<td title="${stat.statType }">${stat.statType }</td>
											</c:otherwise>
										</c:choose>

										<td class="right" title="${stat.allfeeCount }">${stat.allfeeCount }&nbsp;</td>
										<td class="right" title="${stat.recpayfee }"><fmt:formatNumber
												value="${stat.recpayfee }" pattern="#,#00" />&nbsp;</td>
										<td class="right" title="${stat.fullfeeCount }">${stat.fullfeeCount }&nbsp;</td>
										<td class="right" title="${stat.fullfeeSum }">${stat.fullfeeSum }</td>
										<td class="right" title="${stat.deratefeeCount }">${stat.deratefeeCount }&nbsp;</td>
										<td class="right" title="${stat.deratefeeSum }">${stat.deratefeeSum }</td>
										<td class="right" title="${stat.partfeeCount }">${stat.partfeeCount }&nbsp;</td>
										<td class="right" title="${stat.partfeeSum }">${stat.partfeeSum }</td>
										<td class="right" title="${stat.owefeeCount }">${stat.owefeeCount }&nbsp;</td>
										<td class="right" title="${stat.owefeeSum }"><fmt:formatNumber
												value="${stat.owefeeSum }" pattern="#,#00" />&nbsp;</td>
										<td class="right"
											<c:if test="${stat.fullfeePer ge 1.0 }">style="color: blue;"</c:if>><fmt:formatNumber
												type="percent" pattern="##0.0%" value="${stat.fullfeePer }" />
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<%-- </c:forEach> --%>
				</div>
			</div>
		</div>
	</div>
</body>
</html>