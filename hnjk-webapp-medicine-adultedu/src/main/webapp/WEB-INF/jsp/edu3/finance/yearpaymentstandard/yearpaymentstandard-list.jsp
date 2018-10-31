<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年缴费标准</title>
<script type="text/javascript">
	//新增
	function addYearPaymentStandard(){
		navTab.openTab('RES_FINANCE_YEARPAYMENTSTANDARD_INPUT', '${baseUrl}/edu3/finance/yearpaymentstandard/input.html', '新增年缴费标准');
	}
	//编辑
	function modifyYearPaymentStandard(){
		if(isCheckOnlyone('resourceid','#yearPaymentStandardBody')){
			navTab.openTab('RES_FINANCE_YEARPAYMENTSTANDARD_INPUT', '${baseUrl}/edu3/finance/yearpaymentstandard/input.html?resourceid='+$("#yearPaymentStandardBody input[@name='resourceid']:checked").val(), '编辑年缴费标准');
		}
	}
	//删除
	function removeYearPaymentStandard(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/finance/yearpaymentstandard/remove.html","#yearPaymentStandardBody");
	}
	
	//扫面缴费标准记录
	function scanPaymentStandar(){
		if(isCheckOnlyone('resourceid','#yearPaymentStandardBody')){
			var resourceid = $("#yearPaymentStandardBody input[name='resourceid']:checked").val();
			$.ajax({
				type:'POST',
				url:"${baseUrl}/edu3/finance/yearpaymentstandard/scan.html?resourceid="+resourceid,
				dataType:"json",
//	 			data:{re},
				cache: false,
				error: DWZ.ajaxError,
				success:function(resultData){
					if(resultData.statusCode==200){
						alertMsg.correct(resultData.message);
					} else {
						alertMsg.error(resultData.message);
					}
				}
			});
		}
		
		
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/yearpaymentstandard/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<!-- 
				<li>
				  <label>年度：</label>
				  <gh:selectModel id="yearPaymentStandard_yearInfoId" name="yearInfoId" bindValue="resourceid" displayValue="yearName"  
							   	  modelClass="com.hnjk.edu.basedata.model.YearInfo" value="${condition['yearInfoId']}" style="width:55%;"
							   	  orderBy="firstYear desc"/>
	            </li>
	             -->
						<li><label>年级：</label> <gh:selectModel
								id="yearPaymentStandard_gradeId" name="gradeId"
								bindValue="resourceid" displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['gradeId']}" style="width:55%;"
								orderBy="gradeName desc" /></li>
						<li><label>名称：</label><input type="text" name="standerdName"
							value="${condition['standerdName']}" /></li>
						<li><label>缴费类型：</label> <gh:select
								dictionaryCode="CodePaymentType"
								id="yearPaymentStandard_paymentType" name="paymentType"
								style="width:55%;" value="${condition['paymentType'] }" /></li>
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
			<gh:resAuth parentCode="RES_FINANCE_YEARPAYMENTSTANDARD"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_yearPaymentStandard"
							onclick="checkboxAll('#check_all_yearPaymentStandard','resourceid','#yearPaymentStandardBody')" /></th>
						<th width="20%">年级</th>
						<th width="35%">年缴费标准名称</th>
						<th width="20%">缴费类型</th>
						<th width="20%">备注</th>
					</tr>
				</thead>
				<tbody id="yearPaymentStandardBody">
					<c:forEach items="${yearPaymentStandardList.result}"
						var="yearPaymentStandard" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${yearPaymentStandard.resourceid }" autocomplete="off" /></td>
							<td>${yearPaymentStandard.grade.gradeName }</td>
							<td>${yearPaymentStandard.standerdName }</td>
							<td>${ghfn:dictCode2Val('CodePaymentType',yearPaymentStandard.paymentType) }</td>
							<td>${yearPaymentStandard.memo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${yearPaymentStandardList}"
				goPageUrl="${baseUrl }/edu3/finance/yearpaymentstandard/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>