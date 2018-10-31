<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年教材费标准管理</title>
<script type="text/javascript">
	//新增
	function addTextbookFee(){
		navTab.openTab('navTab', '${baseUrl}/edu3/finance/textbookFee/edit.html', '新增年教材费标准');
	}
	
	//修改
	function modifyTextbookFee(){
		var url = "${baseUrl}/edu3/finance/textbookFee/edit.html";
		if(isCheckOnlyone('resourceid','#textbookFeeBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#textbookFeeBody input[@name='resourceid']:checked").val(), '编辑年教材费标准');
		}			
	}
		
	//删除
	function deleteTextbookFee(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/finance/textbookFee/delete.html","#textbookFeeBody");
	}
	
	//下载模板
	function downloadTextbookFeeTemp(){
		var url = "${baseUrl }/edu3/finance/textbookFee/downloadTemplate.html";
		downloadFileByIframe(url, "downloadTextbookFeeTemp");
	}
	
	//导入成绩
	function uploadTextbookFee(){
		var url = "${baseUrl}/edu3/finance/textbookFee/importTextbookFee-view.html";
		$.pdialog.open(url,"RES_FINANCE_TEXTBOOKFEE_UPLOAD_VIEW","导入年教材费标准",{width:600, height:400});
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/textbookFee/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li>
							<label>年度：</label>
							<gh:selectModel name="yearId" bindValue="resourceid" displayValue="yearName"  value="${condition['yearId'] }" modelClass="com.hnjk.edu.basedata.model.YearInfo" orderBy="firstYear desc" style="width:55%" />
						</li>
						<li>
							<label>专业：</label>
							<gh:majorAutocomplete name="majorId"  defaultValue="${condition['majorId'] }"   id="textbookFee_list_majorId"  tabindex="1" displayType="code" orderBy="majorCode"  style="width:60%"></gh:majorAutocomplete>
						</li>
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
			<gh:resAuth parentCode="RES_FINANCE_TEXTBOOKFEE" pageType="list"></gh:resAuth>
			<table id="textbookFeeTab" class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall" id="check_all_textbookFee"
							onclick="checkboxAll('#check_all_textbookFee','resourceid','#textbookFeeBody')" /></th>
						<th width="10%">年度</th>
						<th width="10%">层次</th>
						<th width="10%">专业编码</th>
						<th width="50%">专业名称</th>
						<th width="15%">金额</th>
					</tr>
				</thead>
				<tbody id="textbookFeeBody">
					<c:forEach items="${textbookFeeList.result}" var="textbookFee" varStatus="vs">
						<tr>
							<td>
								<input type="checkbox" name="resourceid" value="${textbookFee.resourceid }" autocomplete="off" />
							</td>
							<td>${textbookFee.yearInfo.yearName }</td>
							<td>${classicMap[textbookFee.major.classicCode] }</td>
							<td>${textbookFee.major.majorCode }</td>
							<td>${textbookFee.major.majorName }</td>
							<td>${textbookFee.money }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${textbookFeeList}" goPageUrl="${baseUrl }/edu3/finance/textbookFee/list.html" pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>