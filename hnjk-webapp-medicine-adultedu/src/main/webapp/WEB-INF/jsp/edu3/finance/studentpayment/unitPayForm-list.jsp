<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学点收费信息</title>

<script type="text/javascript">
	// 跳转到选择收费形式页面
	function setUnitPayForm() {
		var unitIds = [];
		$("#unitPayFormBody input[@name='resourceid']:checked").each(function(){
			unitIds.push($(this).val());
		});
		if(unitIds.length<1){
			alertMsg.warn('请选择一条要操作的记录！');
			return false;
		}
		var url = "${baseUrl}/edu3/finance/studentpayment/unitPayForm-select.html?unitIds="+unitIds.toString();
		$.pdialog.open(url,"RES_FINANCE_UNITPAYFORM_SET","设置教学点收费形式",{mask:true,width: 300, height: 200});
	}
	
	// 跳转到设置分成比例页面
	function setRoyaltyRate() {
		var unitIds = [];
		$("#unitPayFormBody input[@name='resourceid']:checked").each(function(){
			unitIds.push($(this).val());
		});
		if(unitIds.length<1){
			alertMsg.warn('请选择一条要操作的记录！');
			return false;
		}
		var url = "${baseUrl}/edu3/finance/studentpayment/setRoyaltyRateForm.html?unitIds="+unitIds.toString();
		$.pdialog.open(url,"RES_FINANCE_ROYALTYRATE_SET","设置分成比例",{mask:true,width: 300, height: 200});
	}
	
	// 跳转到设置缴费项目编号页面
	function setProjectNo() {
		var url = "${baseUrl}/edu3/finance/studentpayment/setProjectNoForm.html";
		$.pdialog.open(url,"RES_FINANCE_ROYALTYRATE_SET","设置缴费项目编号",{mask:true,width: 300, height: 200});
	}
	
	// 跳转到设置学校内部批次编号页面
	function setBarchNo() {
		var url = "${baseUrl}/edu3/finance/studentpayment/setBatchNoForm.html";
		$.pdialog.open(url,"RES_FINANCE_ROYALTYRATE_SET","设置学校内部批次编号",{mask:true,width: 300, height: 200});
	}
	
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="unitPayForm"
				action="${baseUrl }/edu3/finance/studentpayment/unitPayForm-list.html"
				method="post" onsubmit="return navTabSearch(this);">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>教学站名称：</label> <input type="text" name="unitName"
							id="unitPayForm_unitName" value="${condition['unitName'] }" class="custom-inp"/></li>
						<li><label>收费形式：</label> <gh:select style="width:100px;"
								dictionaryCode="CodeUnitPayForm" id="unitPayForm_payForm"
								name="payForm" value="${condition['payForm'] }" /></li>
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
			<gh:resAuth parentCode="RES_FINANCE_UNITPAYFORM" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_unitPayForm"
							onclick="checkboxAll('#check_all_unitPayForm','resourceid','#unitPayFormBody')" /></th>
						<th width="12%">教学站名称</th>
						<th width="5%">收费形式</th>
						<th width="5%">分成比例</th>
						<c:if test="${schoolCode eq '11846'}">
							<th width="8%">分成比例2(外语类)</th>
							<th width="5%">预留比例</th>
						</c:if>
						<th width="10%">缴费项目编号</th>
						<th width="10%">学校内部批次编号</th>
						<th width="10%">教学站简称</th>
						<th width="5%">教学站编码</th>
						<th width="10%">教学站描述</th>
						<th width="5%">市内教学点</th>
					</tr>
				</thead>
				<tbody id="unitPayFormBody">
					<c:forEach items="${unitPayFormList.result}" var="unitPayForm"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${unitPayForm.resourceid }" autocomplete="off" /></td>
							<td title="${unitPayForm.unitName }">${unitPayForm.unitName }</td>
							<td>${ghfn:dictCode2Val("CodeUnitPayForm",unitPayForm.payForm) }</td>
							<td>${unitPayForm.royaltyRate }</td>
							<c:if test="${schoolCode eq '11846'}">
								<td>${unitPayForm.royaltyRate2 }</td>
								<td>${unitPayForm.reserveRatio }</td>
							</c:if>
							<td>${projectNo }</td>
							<td>${batchNo }</td>
							<td title="${unitPayForm.unitShortName }">${unitPayForm.unitShortName }</td>
							<td>${unitPayForm.unitCode }</td>
							<td title="${unitPayForm.unitDescript }">${unitPayForm.unitDescript }</td>
							<td>${ghfn:dictCode2Val("yesOrNo",unitPayForm.isLocal) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${unitPayFormList}"
				goPageUrl="${baseUrl }/edu3/finance/studentpayment/unitPayForm-list.html"
				condition="${condition }" pageType="sys" />
		</div>
	</div>
</body>
</html>