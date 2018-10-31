<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年级预交费用设置</title>
</head>
<body>

	<h2 class="contentTitle">年级预交费用设置</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/schoolroll/studentfeerule/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid" value="${fee.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li><a href="#"><span>费用设置</span></a></li>
									<li class="#"><a href="#"><span>费用明细设置</span></a></li>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<!-- 1 -->
							<div>
								<table class="form">
									<tr>
										<td width="20%">年级:</td>
										<td width="30%"><gh:selectModel id="gradeFee"
												name="gradeid" bindValue="resourceid"
												displayValue="gradeName" style="width:52%"
												modelClass="com.hnjk.edu.basedata.model.Grade"
												value="${fee.grade.resourceid}" classCss="required"
												orderBy=" yearInfo.firstYear desc" /></td>
										<td width="20%">学习中心:</td>
										<td width="30%"><gh:brSchoolAutocomplete
												name="branchSchoolId" tabindex="1"
												id="studentfeeruleForm_brSchoolName"
												defaultValue="${fee.branchSchool.resourceid}"
												style="width:50%" /></td>
									</tr>
									<tr>
										<td>专业:</td>
										<td><gh:selectModel id="majorFee" name="majorid"
												bindValue="resourceid" displayValue="majorName"
												value="${fee.major.resourceid}"
												modelClass="com.hnjk.edu.basedata.model.Major"
												style="width:52%" onchange="queryPoint()" /></td>
										<td>层次:</td>
										<td><gh:selectModel id="classicFee" name="classicid"
												bindValue="resourceid" displayValue="classicName"
												value="${fee.classic.resourceid}"
												modelClass="com.hnjk.edu.basedata.model.Classic"
												style="width:52%" onchange="queryPoint()" /></td>
									</tr>
									<tr>
										<td>总学分:</td>
										<td><input type="text" id="_sumPoint" name="sumPoint"
											style="width: 50%" value="${sum }" class="required" /></td>
										<td>每学分缴费金额:</td>
										<td><input type="text" id="credit_Fee" name="creditFee"
											style="width: 50%" value="${fee.creditFee }" class="required"
											onkeyup="calculatePoint(this)" onfocus="calculatePoint(this)" />
										</td>
									</tr>
									<tr>
										<td>缴费总金额:</td>
										<td><input type="text" id="totalFee" style="width: 50%"
											value="${fee.totalFee }" name="totalFee" class="required"
											readonly="readonly" /></td>
										<td>教学模式:</td>
										<td><gh:select name="schoolType"
												value="${fee.schoolType }" dictionaryCode="CodeTeachingType"
												style="width:52%" /></td>
									</tr>
									<tr>
										<td>备注:</td>
										<td colspan="3"><textarea name="memo" rows="5"
												style="width: 60%">${fee.memo}</textarea></td>
									</tr>
								</table>
							</div>
							<!-- 2 -->
							<div>
								<table class="form" id="fr_Tab">
									<tr>
										<td colspan="5"><span class="buttonActive"><div
													class="buttonContent">
													<button type="button" onclick="addFeeDetai123()">增加</button>
												</div></span> <span class="buttonActive"><div
													class="buttonContent">
													<button type="button" onclick="delFeeDetai123()">删除</button>
												</div></span></td>
									</tr>
									<tr>
										<td style="width: 20%">&nbsp;</td>
										<td style="width: 20%">缴费期</td>
										<td style="width: 20%">缴费比例&nbsp;&nbsp;<font color="red">(缴费比例之和为100%)</font></td>
										<td style="width: 20%">缴费金额</td>
										<td style="width: 20%">排序</td>
									</tr>
									<c:forEach items="${fee.studentFeeRuleDetails }" var="fr"
										varStatus="vs">
										<tr>
											<td><input type='checkbox' name='feeCid'
												value='${fr.resourceid }'><INPUT type="hidden"
												name="feeDid" value="${fr.resourceid }" /> &nbsp;</td>
											<td><SELECT name='feeTermNum' style='width: 65%'>
													<OPTION value='1'
														<c:if test="${fr.feeTermNum eq '1'}">selected="selected"</c:if>>第一期</OPTION>
													<OPTION value='2'
														<c:if test="${fr.feeTermNum eq '2'}">selected="selected"</c:if>>第二期</OPTION>
													<OPTION value='3'
														<c:if test="${fr.feeTermNum eq '3'}">selected="selected"</c:if>>第三期</OPTION>
											</SELECT></td>
											<td><input type="text" name="feeScale"
												onkeyup="calculateFee(this);"
												value="<fmt:formatNumber value='${fr.feeScale }'/>"
												style="width: 65%">%</td>
											<td><input type="text" name="factfee"
												value="<fmt:formatNumber value='${fr.feeScale * fee.totalFee / 100}'/>"
												style="width: 65%"></td>
											<td><input type="text" name="showOrder"
												value="${fr.showOrder }" style="width: 65%"></td>
										</tr>
									</c:forEach>
								</table>
							</div>
						</div>
						<div class="formBar">
							<ul>
								<li><div class="buttonActive">
										<div class="buttonContent">
											<button type="submit">提交</button>
										</div>
									</div></li>
								<li><div class="button">
										<div class="buttonContent">
											<button type="button" class="close"
												onclick="navTab.closeCurrentTab();">取消</button>
										</div>
									</div></li>
							</ul>
						</div>
			</form>
		</div>
	</div>
</body>
<script type="text/javascript">
<!--
	function queryPoint(){
		var url = "${baseUrl}/edu3/schoolroll/studentfeerule/ajaxsumpoint.html";
		$.getJSON(url, {grade:$("#gradeFee").val(),major:$("#majorFee").val(),classic:$("#classicFee").val()},function(data){
			if(data.error !=""){
				alertMsg.warn(data.error);
				return false;
			}else{
				$("#_sumPoint").val(data.sumPoint);
			}
		  
		});
	}
	
	function calculatePoint(single_p){
		var sum_p = $("#_sumPoint").val();
		var credit = $("#credit_Fee").val();
		if(credit!="" && sum_p!="")	$("#totalFee").val(parseFloat(sum_p)*parseFloat(credit));
	}
	
	function calculateFee(scale){
		var tFee = $("#totalFee").val();
		var percent = scale.value;
		var factFee = (parseFloat(tFee)*parseFloat(percent)/100);
		scale.parentNode.nextSibling.firstChild.value=factFee;
	}
	
    function addFeeDetai123(){
    	var term = "";
    	var orderHtml = "";
    	var num = jQuery("#fr_Tab > tbody").children().size();
    	if( num == 2){
    		term = "<SELECT name='feeTermNum' style='width:65%'><OPTION value='1' selected='selected'>第一期</OPTION><OPTION value='2'>第二期</OPTION><OPTION value='3'>第三期</OPTION></SELECT>"
    		orderHtml = "<input name='showOrder' value='"+1+"' style='width:65%' type='text'>";
    	}else if(num == 3){
    		term = "<SELECT name='feeTermNum' style='width:65%'><OPTION value='1'>第一期</OPTION><OPTION value='2' selected='selected'>第二期</OPTION><OPTION value='3'>第三期</OPTION></SELECT>"
    		orderHtml = "<input name='showOrder' value='"+2+"' style='width:65%' type='text'>";
    	}else if(num == 4){
    		term = "<SELECT name='feeTermNum' style='width:65%'><OPTION value='1'>第一期</OPTION><OPTION value='2'>第二期</OPTION><OPTION value='3' selected='selected'>第三期</OPTION></SELECT>"
    		orderHtml = "<input name='showOrder' value='"+3+"' style='width:65%' type='text'>";
    	}
		var trHtml = "<tr><td><input type='checkbox' name='feeCid' value=''><input type='hidden' name='feeDid' value=''>&nbsp;</td><td>"+term+"</td><td><input name='feeScale' value='' style='width:65%' type='text' onkeyup='calculateFee(this)'>%</td><td><input name='factfee' value='' style='width:65%' type='text'></td><td>"+orderHtml+"</td></tr>";
    	if(num < 5)jQuery("#fr_Tab").append(trHtml);
    }
    
    function delFeeDetai123(){
    	var ids = new Array();
    	$("#fr_Tab input[name='feeCid']:checked").each(function(ind){
		  	$(this).parent().parent().remove();
		  	ids.push($(this).val());
		});
		if(ids.length>0){
			var url = "${baseUrl}/edu3/schoolroll/studentfeerule/deleteDetail.html";
			$.get(url, {c_id:ids.toString()});
		}
    }
//-->
</script>
</html>