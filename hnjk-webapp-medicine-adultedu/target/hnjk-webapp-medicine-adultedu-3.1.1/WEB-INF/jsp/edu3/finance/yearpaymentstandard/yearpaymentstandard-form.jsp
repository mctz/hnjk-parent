<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年缴费标准管理</title>
<style type="text/css">
#childCustomformTable th {
	font-weight: bold;
}
</style>
<script type="text/javascript">  
    //新增子表
    function addYearPaymentStandardDetails(){
    	var rowNum = $("#yearPaymentStandardDetailsTable").get(0).rows.length;
    	$("#yearPaymentStandardDetailsTable").append("<tr><td><q>"+rowNum+"</q><input type='checkbox' name='checkfeeid' value='' />"
    	+"<input type='hidden' name='feeid' value='' /></td><td><input type='text' name='feeTerm' nameb='feeTerm' value='"+rowNum+"' class='required digits' style='width:90%'/></td>"
    	+"<td><input type='text' name='creditFee' nameb='creditFee' value='' class='required number' min='0' style='width:90%'/></td>"
    	+"<td><input type='text' name='payBeginDate' nameb='payBeginDate' value='' class='required' onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd'})\" style='width:90%'/></td>"
    	+"<td><input type='text' name='creditEndDate' nameb='creditEndDate' value='' class='required' onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd'})\" style='width:90%'/></td>"
    	+"<td><input type='text' name='showOrder' nameb='showOrder' value='"+rowNum+"' class='digits' style='width:90%'/></td></tr>");
    }
    
    //删除子表
    function delYearPaymentStandardDetails(){    	
    	var idArray = [], trArr = [];
  		if($("#yearPaymentStandardDetailsBody input[name='checkfeeid']:checked").size()<1){return;}
  		$("#yearPaymentStandardDetailsBody input[name='checkfeeid']:checked").each(function(index){
  			if($(this).val()!=""){
  				idArray.push($(this).val());
  			}  	
  			trArr.push($(this).parent().parent());
		});		
		alertMsg.confirm("确定删除该记录?",{okCall:function (){
			for(var tr in trArr){
				trArr[tr].remove();
			}
			var url = "${baseUrl}/edu3/framework/yearpaymentstandard/details/delete.html";
			if(idArray.length>0){
				$.get(url,{resourceid:$("#yearPaymentStandardDetails_resourceid").val(),feeids:idArray.join(',')});
			}
			$("#yearPaymentStandardDetailsBody q").each(function(index){
				$(this).text(index+1);
	    		$(this).parent().parent().find("input[name='showOrder']").val(index+1);
	    		$(this).parent().parent().find("input[name='feeTerm']").val(index+1);
	    	});
		}});
    }
    
    function yearPaymentStandardValidateCallback(form) {
    	var $form = $(form);
    	$("#yearPaymentStandardDetailsBody tr").each(function (row){
    		$(this).find("[nameb]").each(function (){//有名称附加属性的表单元素
    			$(this).attr("name",$(this).attr("name")+"_row"+row);
    		});
    	});
    	if (!$form.valid()) {
    		alertMsg.error(DWZ.msg["validateFormError"]);
    		return false; 
    	}
    	$("#yearPaymentStandardDetailsBody tr").each(function (row){
    		$(this).find("[nameb]").each(function (){    			
    			$(this).attr("name",$(this).attr("nameb"));//验证后还原名称
    		});
    	});
		$.ajax({
    		type:'POST',
    		url:$form.attr("action"),
    		data:$form.serializeArray(),
    		dataType:"json",
    		cache: false,
    		success: validateFormCallback,
    		
    		error: DWZ.ajaxError
    	});
    	return false;
    }
</script>
</head>
<body>
	<h2 class="contentTitle">编辑年缴费标准</h2>
	<div class="page">
		<div class="pageContent">
			<form id="yearPaymentStandardDetails_form" method="post"
				action="${baseUrl}/edu3/finance/yearpaymentstandard/save.html"
				class="pageForm"
				onsubmit="return yearPaymentStandardValidateCallback(this);">
				<input type="hidden" id="yearPaymentStandardDetails_resourceid"
					name="resourceid" value="${yearPaymentStandard.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<!-- 
					<td width="20%">所属年度:</td>
					<td width="30%">
						<gh:selectModel id="yearPaymentStandardDetails_yearInfoId" name="yearInfoId" bindValue="resourceid" displayValue="yearName"  
							   	  modelClass="com.hnjk.edu.basedata.model.YearInfo" value="${yearPaymentStandard.yearInfo.resourceid }" style="width:55%;"
							   	  orderBy="firstYear desc" classCss="required"/>
							   	  <span style="color:red;">*</span>
					</td>
					 -->
							<td width="20%">所属年级:</td>
							<td width="30%"><gh:selectModel
									id="yearPaymentStandardDetails_gradeId" name="gradeId"
									bindValue="resourceid" displayValue="gradeName"
									modelClass="com.hnjk.edu.basedata.model.Grade"
									value="${yearPaymentStandard.grade.resourceid }"
									style="width:55%;" orderBy="gradeName desc" classCss="required" />
								<span style="color: red;">*</span></td>
							<td width="20%">缴费类型:</td>
							<td width="30%"><gh:select dictionaryCode="CodePaymentType"
									id="yearPaymentStandardDetails_paymentType" name="paymentType"
									classCss="required" style="width:55%;"
									value="${yearPaymentStandard.paymentType }" /> <span
								style="color: red;">*</span></td>
						</tr>
						<tr>
							<td>标准名称:</td>
							<td colspan="3"><input type="text" name="standerdName"
								value="${yearPaymentStandard.standerdName }" style="width: 55%;"
								class="required" /></td>
						</tr>
						<tr>
							<td>备注:</td>
							<td colspan="3"><textarea rows="3" cols=""
									style="width: 80%;" name="memo">${yearPaymentStandard.memo }</textarea>
							</td>
						</tr>
					</table>
					<table class="form">
						<tr>
							<td style="text-align: right">
								<div class="formBar">
									<ul>
										<li><div class="buttonActive">
												<div class="buttonContent">
													<button type="button"
														onclick="addYearPaymentStandardDetails()">新增一项</button>
												</div>
											</div></li>
										<li><div class="button">
												<div class="buttonContent">
													<button type="button"
														onclick="delYearPaymentStandardDetails()">删除</button>
												</div>
											</div></li>
									</ul>
								</div>
							</td>
						</tr>
					</table>
					<table class="form" id="yearPaymentStandardDetailsTable">
						<thead>
							<tr>
								<th width="5%"><input type="checkbox" name="checkall"
									id="check_all_yearPaymentStandardDetails"
									onclick="checkboxAll('#check_all_yearPaymentStandardDetails','checkfeeid','#yearPaymentStandardDetailsBody')" /></th>
								<th width="15%">所属缴费期</th>
								<th width="20%">缴费金额</th>
								<th width="25%">开始缴费日期</th>
								<th width="25%">截止缴费日期</th>
								<th width="10%">排序号</th>
							</tr>
						</thead>
						<tbody id="yearPaymentStandardDetailsBody">
							<c:forEach
								items="${yearPaymentStandard.yearPaymentStandardDetails}"
								var="detail" varStatus="vs">
								<tr>
									<td><q>${vs.index+1}</q><input type="checkbox"
										name="checkfeeid" value="${detail.resourceid}" /><input
										type="hidden" name="feeid" value="${detail.resourceid}" /></td>
									<td><input type="text" name="feeTerm" nameb="feeTerm"
										value="${detail.feeTerm }" class='required digits'
										style='width: 90%' /></td>
									<td><input type="text" name="creditFee" nameb="creditFee"
										value="<fmt:formatNumber value='${detail.creditFee }' pattern='####.##' />"
										class='required number' min='0' style='width: 90%' /></td>
									<td><input type="text" name="payBeginDate"
										nameb="payBeginDate"
										value="<fmt:formatDate value='${detail.payBeginDate }' pattern='yyyy-MM-dd'/>"
										class='required' onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
										style="width: 90%" /></td>
									<td><input type="text" name="creditEndDate"
										nameb="creditEndDate"
										value="<fmt:formatDate value='${detail.creditEndDate }' pattern='yyyy-MM-dd'/>"
										class='required' onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
										style="width: 90%" /></td>
									<td><input type="text" name="showOrder" nameb="showOrder"
										value="${detail.showOrder }" class='digits' style="width: 90%" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
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
</html>