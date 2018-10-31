<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成绩分布详细列表</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="examResultsDistributionForm"
				onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/teaching/result/examresults-distribution-view.html"
				method="post">
				<input name="examSubId" type="hidden" value="${examSubId }" /> <input
					name="examInfoId" type="hidden" value="${examInfoId }">
				<table width="20%">
					<tbody id="customizeDistributionBody">
						<tr>
							<td style="width: 2%"></td>
							<td style="width: 13%; color: #183152;">成绩类型</td>
							<td colspan="3"><select name="scoreType" style="width: 80%;">
									<option value="w"
										<c:if test="${scoreType eq 'w' }">selected="selected"</c:if>>卷面成绩</option>
									<option value="i"
										<c:if test="${scoreType eq 'i' }">selected="selected"</c:if>>综合成绩</option>
							</select></td>
						</tr>
						<c:forEach items="${sectionList }" var="section" varStatus="vs">
							<tr>
								<td style="width: 2%"><input id="sectionFlag"
									name="sectionFlag" value="${section }" type="checkbox"
									checked="checked" onclick="moveSetion(this);" /></td>
								<td style="width: 13%; color: #183152;">成绩区间</td>
								<td style="width: 2%"><input type="text" name="startScore"
									style="height: 12px; width: 20px" class="number"
									onKeyUp="value=value.replace(/[^\d]/g,'') "
									onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
									value="${fn:split(section,'~')[0]}" /></td>
								<td style="width: 1%">~</td>
								<td style="width: 2%"><input type="text" name="endScore"
									style="height: 12px; width: 20px" class="number"
									onKeyUp="value=value.replace(/[^\d]/g,'') "
									onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
									value="${fn:split(section,'~')[1]}" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
		</div>
		<div class="panelBar">
			<ul class="toolBar">
				<li><a class="icon" onclick="customizeDistribution();"><span>查看自定义成绩分布区间</span></a></li>
				<li><a class="icon" onclick="addCustomizeDistribution();"><span>新增成绩区间</span></a></li>
			</ul>
		</div>
		<div class="pageContent">
			<table class="table" layouth="155" width="100%">
				<thead>
					<tr>
						<th width="5%">编号</th>
						<th width="20%">考试课程</th>
						<th width="5%">应考</th>
						<th width="5%">缺考</th>
						<th width="5%">实考</th>
						<th width="5%">作弊</th>
						<c:forEach items="${sectionList }" var="section" varStatus="vs">
							<th width="5%">${section }</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody id="examResultsDistributionBody">
					<c:set var="prefix" value="${scoreType eq 'i'?'INTEGRATED_':'' }" />
					<c:forEach items="${resultsList }" var="results" varStatus="vs">
						<tr>
							<td rowspan="3">${vs.index+1 }</td>
							<td style="font-weight: bold"><a href="javascript:void(0)"
								onclick="viewResultsDistributionSingle('${examSubId }','${results['RESOURCEID'] }');">${results['COURSENAME'] }</a></td>
							<td>${results['ORDERNUM'] }</td>
							<td>${results['ABSENTNUM'] }</td>
							<td>${results['EXAMNUM'] }</td>
							<td>${results['CHEATNUM'] }</td>
							<c:forEach items="${sectionList }" var="section" varStatus="vs">
								<c:set var="DISTRIBUTIONRATIONUMFLAG"
									value="${prefix }DISTRIBUTIONNUM${section }">
								</c:set>
								<td>${results[DISTRIBUTIONRATIONUMFLAG] }</td>
							</c:forEach>
						</tr>
						<tr>
							<td style="color: #183152">百分比</td>
							<td></td>
							<td>${results['ABSENTNUMRATIO']}</td>
							<td>${results['EXAMNUMRATIO']}</td>
							<td>${results['CHEATNUMRATIO']}</td>
							<c:forEach items="${sectionList }" var="section" varStatus="vs">
								<c:set var="DISTRIBUTIONRATIORATIOFLAG"
									value="${prefix }DISTRIBUTIONRATIO${section }"></c:set>
								<td>${results[DISTRIBUTIONRATIORATIOFLAG] }</td>
							</c:forEach>
						</tr>
						<tr>
							<td style="color: #183152">累计百分比</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<c:forEach items="${sectionList }" var="section" varStatus="vs">
								<c:set var="GRANDTOTALRATIOFLAG"
									value="${prefix }GRANDTOTALRATIO${section }"></c:set>
								<td>${results[GRANDTOTALRATIOFLAG] }</td>
							</c:forEach>
						</tr>
					</c:forEach>

				</tbody>
			</table>
		</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		var success  = "${success}"
		var errorMsg = "${errorMsg}"
		if(success==false){
			alertMsg.warn(errorMsg);
		}
	});
	//删除区间
	function moveSetion(obj){
		$(obj).parent().parent().html("");
	}
	//新增自定义区间
	function addCustomizeDistribution(){
	   
		var htmlStr = "<tr><td style='width:2%'><input id='sectionFlag'  type='checkbox' checked='checked' onclick='moveSetion(this);'/></td ><td style='width:13%;color: #183152;'>成绩区间</td>";
			htmlStr+= "<td style='width:2%'><input type='text' name='startScore' style='height: 12px;width: 22px' onKeyUp=\"value=value.replace(\/\[\^\\d]\/g,'') \" onbeforepaste=\"clipboardData.setData('text',clipboardData.getData('text').replace(\\\/\[\^\\d\]\/g,''))\" class='number'/> </td>";
			htmlStr+= "<td style='width:1%'>~</td>";
			htmlStr+= "<td style='width:2%'><input type='text' name='endScore' style='height: 12px;width: 22px;' onKeyUp=\"value=value.replace(\/\[\^\\d]\/g,'') \" onbeforepaste=\"clipboardData.setData('text',clipboardData.getData('text').replace(\\\/\[\^\\d\]\/g,''))\" class='number'/> </td></tr>";
			
		$("#customizeDistributionBody").append(htmlStr);
	}
	//自定义成绩分布区间
	function customizeDistribution(){
		var $form = $("#examResultsDistributionForm");
		if (!$form.valid()) {
			alertMsg.error(DWZ.msg["validateFormError"]);
			return false; 
		}
		var url = "${baseUrl}/edu3/teaching/result/examresults-distribution-view.html";
		navTab.reload(url,$("#examResultsDistributionForm").serializeArray(),"RES_TEACHING_RESULT_DISTRIBUTION_LIST");
	}
</script>
</body>
</html>