<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生缴费标准管理</title>
<script type="text/javascript">
	//导出
	function exportStu(){
		var idsArray 	 = new Array();
		$("#studentPaymentBodyStu input[name='sturesourceid']:checked").each(function(){
			idsArray.push($(this).val());
		});
		var branchSchool = $("#studentPayment_brSchool_stu").val();	
		
		var name = $("#studentPayment_name_stu").val();	 
		var studyNo = $("#studentPayment_studyno_stu").val();
		var grade = $("#studentPayment_gradeid_stu").val();               
		var chargeStatus = $("#studentPayment_chargeStatusStu").val();  
		
		var qianfeipay = $("#studentPayment_qianfeipay_stu").val();  
		var params="";
		if(""!=branchSchool||""!=name||""!=studyNo||""!=grade||""!=chargeStatus||""!=qianfeipay){
			params= "&branchSchool="+branchSchool
			+"&name="+name+"&studyNo="+studyNo+"&grade="+grade
			+"&chargeStatus="+chargeStatus+"&qianfeipay="+qianfeipay;
		}
		var url          = "${baseUrl}/edu3/finance/studentpaymentstu/liststu-export.html?studynos="+idsArray.toString()+params;
		if(idsArray.length>0||""!=params){
			downloadFileByIframe(url,"studentPaymentExportIframe");
		}else{
			alertMsg.warn("请您勾选一个或多个要打印的学生或至少选择一个查询条件，在执行此功能。");	
			return false;
		}
	}


</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader" style="height: 75px;">
			<form id="studentPayment_search_form_stu"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/studentpaymentstu/liststu.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>学习中心：</label> <gh:brSchoolAutocomplete
								name="brSchool" tabindex="1" id="studentPayment_brSchool_stu"
								defaultValue="${condition['brSchool'] }" displayType="code"
								style="width:240px;"/></li>
						<!--<li>
					<label>专业：</label>
					<gh:selectModel name="majorid" id="studentPayment_majorid" bindValue="resourceid" displayValue="majorCodeName" 
						modelClass="com.hnjk.edu.basedata.model.Major" value="${condition['majorid']}" orderBy="majorCode,majorName" style="width:52%;" />
				</li>
				-->
						<li><label>年级：</label> <gh:selectModel
								id="studentPayment_gradeid_stu" name="gradeid"
								bindValue="resourceid" displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['gradeid']}"
								orderBy="yearInfo.firstYear desc,term desc" style="width:52%;" />
						</li>
						<li><label>缴费状态：</label>
						<gh:select name="chargeStatus" id="studentPayment_chargeStatusStu"
								dictionaryCode="CodeChargeStatus"
								value="${condition['chargeStatus']}" style="width:52%;" /></li>
						
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>欠费大于：</label><input type="text" name="qianfeipay"
							id="studentPayment_qianfeipay_stu"
							value="${condition['qianfeipay']}" style="width: 120px;" /></li>
						<li><label>姓名：</label><input type="text" name="name"
							id="studentPayment_name_stu" value="${condition['name']}"
							style="width: 120px;" /></li>
						<li><label>学号：</label><input type="text" name="studyNo"
							id="studentPayment_studyno_stu" value="${condition['studyNo']}"
							style="width: 120px;" /></li>
					</ul>
					<ul class="searchContent">

						<!--<li>
					<label>缴费学期：</label><gh:select id="studentPayment_term" name="term" value="${condition['term']}" dictionaryCode="CodeTerm" style="width:52%;" />
				</li>
				-->

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
			<gh:resAuth parentCode="RES_FINANCE_STUDENTPAYMENTSTU"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="160">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_studentPaymentStu"
							onclick="checkboxAll('#check_all_studentPaymentStu','sturesourceid','#studentPaymentBodyStu')" /></th>

						<th width="8%">学号</th>
						<th width="6%">姓名</th>
						<th width="5%">年级</th>
						<th width="20%">学习中心</th>
						<th width="10%">联系电话</th>
						<th width="8%">应缴总额</th>
						<th width="8%">当前应缴金额</th>
						<th width="8%">当前减免总额</th>
						<th width="8%">当前已缴金额</th>
						<th width="8%">当前欠缴金额</th>
						<th width="8%">缴费状态</th>
					</tr>
				</thead>
				<tbody id="studentPaymentBodyStu">
					<c:forEach items="${studentPaymentListStu.result}"
						var="studentPayment" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="sturesourceid"
								value="${studentPayment.studyno }" autocomplete="off" /></td>

							<td title="${studentPayment.studyNo }">${studentPayment.studyno }</td>
							<td title="${studentPayment.name }">${studentPayment.name }</td>
							<td title="${studentPayment.gradename }">${studentPayment.gradename }</td>
							<td title="${studentPayment.unitName }">${studentPayment.unitname }</td>
							<td title="${studentPayment.mobile }">${studentPayment.mobile }</td>
							<td style="text-align: right;"
								title="<fmt:formatNumber value='${studentPayment.recpayfeeall }' pattern='####.##' />"><fmt:formatNumber
									value="${studentPayment.recpayfeeall }" pattern="####.##" /></td>

							<td style="text-align: right;"
								title="<fmt:formatNumber value='${studentPayment.recpayFee }' pattern='####.##' />"><fmt:formatNumber
									value="${studentPayment.recpayfee }" pattern="####.##" /></td>
							<td style="text-align: right;"
								title="<fmt:formatNumber value='${studentPayment.derateFee }' pattern='####.##' />"><fmt:formatNumber
									value="${studentPayment.deratefee }" pattern="####.##" /></td>
							<td style="text-align: right;"
								title="<fmt:formatNumber value='${studentPayment.facepayFee }' pattern='####.##' />"><fmt:formatNumber
									value="${studentPayment.facepayfee }" pattern="####.##" /></td>

							<c:choose>
								<c:when test="${studentPayment.pays > 0 }">
									<td style="color: red; text-align: right;">${studentPayment.pays }</td>
								</c:when>
								<c:otherwise>
									<td style="text-align: right;">${studentPayment.pays }</td>
								</c:otherwise>
							</c:choose>
							<td
								title="${ghfn:dictCode2Val('CodeChargeStatus',studentPayment.chargestatus) }">${ghfn:dictCode2Val('CodeChargeStatus',studentPayment.chargestatus) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${studentPaymentListStu}"
				goPageUrl="${baseUrl }/edu3/finance/studentpaymentstu/liststu.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>