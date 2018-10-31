<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生预交费用明细</title>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/schoolroll/stuperpayfee/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${!isBrschool }">
							<li><label>学习中心：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="stuperpay_fee_eiinfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" style="width:120px" />
							</li>
						</c:if>
						<li><label>缴费年度：</label>
						<gh:selectModel name="grade" bindValue="firstYear"
								displayValue="firstYear" style="width:120px"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								orderBy="firstYear desc" value="${condition['grade']}" /></li>
						<li><label>缴费状态：</label> <select name="status"
							style="width: 100px;">
								<option value="">请选择</option>
								<option value="0"
									<c:if test="${condition['status'] eq '0' }">selected="selected"</c:if>>已缴清</option>
								<option value="1"
									<c:if test="${condition['status'] eq '1' }">selected="selected"</c:if>>欠费</option>
						</select></li>
					</ul>
					<ul class="searchContent">
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" /></li>
						<li><label>姓名：</label><input type="text" name="studentName"
							value="${condition['studentName']}" /></li>

						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_STU_PAYDETAIL" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_spf"
							onclick="checkboxAll('#check_all_spf','resourceid','#stuPayFeeBody')" /></th>
						<th width="10%">姓名</th>
						<th width="10%">学号</th>
						<th width="10%">缴费年度</th>
						<th width="25%">学习中心</th>
						<th width="10%">应缴金额</th>
						<th width="10%">已缴金额</th>
						<th width="10%">免缴金额</th>
						<th width="10%">缴费状态</th>
					</tr>
				</thead>
				<tbody id="stuPayFeeBody">
					<c:forEach items="${feeList.result}" var="s" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${s.resourceid }" autocomplete="off" /></td>
							<td>${s.studentInfo.studentName }</td>
							<td>${s.studyNo }</td>
							<td>${s.chargeyear }</td>
							<td>${s.studentInfo.branchSchool }</td>
							<td>${s.recpayFee }</td>
							<td>${s.facepayFee}</td>
							<td>${s.derateFee }</td>
							<td><c:choose>
									<c:when test="${(s.facepayFee + s.derateFee)>=  s.recpayFee }">
										<font color="green">已缴清</font>
									</c:when>
									<c:otherwise>
										<font color="red">欠费</font>
									</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
					<input type="checkbox" name="resourceid" style="display: none"
						checked="checked" />
				</tbody>
			</table>

			<gh:page page="${feeList}"
				goPageUrl="${baseUrl }/edu3/schoolroll/stuperpayfee/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>

<script type="text/javascript">
	function exportExcelStuFee(){
		alertMsg.warn("该功能已停止使用!");
		return false;
		//以免每次点击下载都创建一个iFrame，把上次创建的删除
		//$('#frameForDownload_fee').remove();
		//var iframe = document.createElement("iframe");
		//iframe.id = "frameForDownload_fee";
		//iframe.src = "${baseUrl}/edu3/schoolroll/stuperpayfee/exeExcel.html"
		//iframe.style.display = "none";
		//创建完成之后，添加到body中
		//document.body.appendChild(iframe);
	}

	function synchronStuFee(){
		var url = "${baseUrl}/edu3/schoolroll/stuperpayfee/saveSynchron.html";
		
		alertMsg.confirm("您确定要同步交费明细吗?<br/>这可能需要耗费比较长的时间，请耐心等待.", {
			okCall: function(){				
				$.post(url,'',  function(data){
					DWZ.ajaxDone(data);
					if (data.statusCode == 200){
						if (data.navTabId){
							navTab.reload(data.reloadTabUrl, {}, data.navTabId);
						}	
					}
				}, "json");
			}
		});	
		
		
	}
</script>

</html>