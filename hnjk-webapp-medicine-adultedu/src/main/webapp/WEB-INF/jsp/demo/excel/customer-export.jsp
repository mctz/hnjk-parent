<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Excel导入</title>
</head>
<body>
	<script type="text/javascript">
 function validateForm(){
 	if(!isChecked('excelColumnName','#exportExcelBody')){
 	    alertMsg.warn("至少需要选择一条记录！");
 		return false;
 	}
 	return true;
 }
 jQuery(document).ready(function(){
 	var excelModel = "${condition['excelModel']}";
 	var exportRequestPath = "${condition['exportRequestPath']}";
 	var msg = "";
 	if (""==excelModel) msg="Excel配置对象有误！";
 	if (""==exportRequestPath) msg = "请传入数据导出请求处理地址！";
 	if (""!=msg){
 	    jQuery("#coustomerExportSubmit").attr("disabled","disabled");
 		alertMsg.warrn(msg);
 	}else{
 		jQuery("#customerExportForm").attr("action","${baseUrl}"+exportRequestPath);
 	}	
 });
</script>
	<h2 class="contentTitle">选择要导出的列</h2>
	<div class="page">
		<div class="pageContent">
			<form id="customerExportForm" method="post" action=""
				class="pageForm" onsubmit="return validateForm();">
				<input type="hidden" name="recruitPlan"
					value="${condition['recruitPlan']}" /> <input type="hidden"
					name="major" value="${condition['major']}" />

				<div class="pageFormContent" layoutH="97">
					<table class="table" layouth="142">
						<thead>
							<tr>
								<th><input type="checkbox" name="checkall"
									id="check_all_exportenrolmanage"
									onclick="checkboxAll('#check_all_exportenrolmanage','excelColumnName','#exportExcelBody')" /></th>
								<th>序号</th>
								<th>列名称</th>
								<th>列宽</th>
								<th>列值类型</th>
							</tr>
						<thead>
						<tbody id="exportExcelBody">
							<c:forEach items="${paramList}" var="pa" varStatus="vs">
								<tr>
									<td><input type="checkbox" name="excelColumnName"
										value="${pa.name }" autocomplete="off" /></td>
									<td>${pa.column }</td>
									<td>${pa.excelTitleName }</td>
									<td>${pa.columnWidth }</td>
									<td>${pa.dataType }</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button id="coustomerExportSubmit" type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>