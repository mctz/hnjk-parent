<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成绩分布概况</title>
</head>
<body>
	<div class="page">
		<div class="panelBar">
			<ul class="toolBar">
				<li><a class="icon"
					onclick="viewResultsMultiDistribution('w');"><span>查看卷面成绩分布信息</span></a></li>
				<li><a class="icon"
					onclick="viewResultsMultiDistribution('i');"><span>查看综合成绩分布信息</span></a></li>
			</ul>
		</div>
		<div class="pageContent">

			<table class="table" layouth="45" width="100%">
				<thead>
					<tr>
						<th width="4%"><input type="checkbox" name="checkall"
							id="check_all_examresults_distribution"
							onclick="checkboxAll('#check_all_examresults_distribution','resourceid','#examResultsDistributionAllBody')" /></th>
						<th width="4%">序号</th>
						<th width="14%">考试课程</th>
						<th width="7%">考试形式</th>
						<th width="13%">考试时间</th>
						<th width="7%">应考人数</th>
						<th width="7%">实考人数</th>
						<th width="7%">缺考人数</th>
						<th width="7%">作弊人数</th>
						<th width="6%">实考率</th>
						<th width="6%">缺考率</th>
						<th width="6%">作弊率</th>
						<th width="6%">卷面合格率</th>
						<th width="6%">综合合格率</th>
					</tr>
				</thead>
				<input id="examResultsDistributionAllBodyExamSubId"
					name="examResultsDistributionAllBodyExamSubId" type="hidden"
					value="${examSubId }" />
				<tbody id="examResultsDistributionAllBody">
					<c:forEach items="${resultsList }" var="results" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${results['RESOURCEID'] }" autocomplete="off" /></td>
							<td>${vs.index+1 }</td>
							<td style="font-weight: bold" title="${results['COURSENAME'] }">
								<a href="javascript:void(0)"
								onclick="viewResultsDistributionSingle('${examSubId }','${results['RESOURCEID'] }');">
									${results['COURSENAME'] }</a>
							</td>
							<td><c:choose>
									<c:when test="${results['ismachineexam'] eq 'Y' }">
		      			机考
		      		</c:when>
									<c:otherwise>
		      			笔试
		      		</c:otherwise>
								</c:choose></td>
							<td><fmt:formatDate value="${results['examstarttime']}"
									pattern="yyyy-MM-dd HH:mm" />- <fmt:formatDate
									value="${results['examendtime']}" pattern="HH:mm" /></td>
							<td>${results['ORDERNUM'] }</td>
							<td>${results['EXAMNUM'] }</td>
							<td>${results['ABSENTNUM'] }</td>
							<td>${results['CHEATNUM'] }</td>
							<td>${results['EXAMNUMRATIO'] }</td>
							<td>${results['ABSENTNUMRATIO']}</td>
							<td>${results['CHEATNUMRATIO']}</td>
							<td>${results['PASSRATIO']}</td>
							<td>${results['INTEGRATEDPASSRATIO'] }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		var success  = "${success}";
		var errorMsg = "${errorMsg}";
		if(success==false){
			alertMsg.warn(errorMsg);
		}
	});
	//查看多门成绩
	function viewResultsMultiDistribution(scoreType){
		var examSubId  = $("#examResultsDistributionAllBodyExamSubId").val();
		var examInfoId = new Array();
		jQuery("#examResultsDistributionAllBody input[name='resourceid']:checked").each(function(){
			examInfoId.push(jQuery(this).val());
		});
		if(examInfoId.length>0){
			var url = "${baseUrl}/edu3/teaching/result/examresults-distribution-view.html?examSubId="+examSubId+"&scoreType="+scoreType+"&examInfoId="+examInfoId.toString();
			navTab.openTab('RES_TEACHING_RESULT_DISTRIBUTION_LIST',url,'多门成绩分布');
		}else{
			alertMsg.warn("请选择至少一门要查看成绩分布信息的课程！");
		}
	}
	//查看单科成绩分布信息
	function viewResultsDistributionSingle(examSubId,courseId){
		var url = "${baseUrl}/edu3/teaching/result/examresults-distribution-view.html?examSubId="+examSubId+"&examInfoId="+courseId;
		navTab.openTab('RES_TEACHING_RESULT_DISTRIBUTION_LIST',url,'单科成绩分布');
	}
</script>
</body>
</html>