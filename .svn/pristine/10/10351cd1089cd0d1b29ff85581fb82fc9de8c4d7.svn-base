<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>专业缴费类别设置</title>
<script type="text/javascript">
	//新增
	function addFeeMajor(){
		navTab.openTab('RES_FINANCE_FEEMAJOR_INPUT', '${baseUrl}/edu3/finance/feemajor/input.html', '新增专业缴费类别');
	}
	//修改
	function modifyFeeMajor(){
		if(isCheckOnlyone('resourceid','#feemajorBody')){
			navTab.openTab('RES_FINANCE_FEEMAJOR_INPUT', "${baseUrl}/edu3/finance/feemajor/input.html?resourceid="+$("#feemajorBody input[@name='resourceid']:checked").val(), '编辑专业缴费类别');
		}			
	}
	// 删除
	function deleteFeeMajor(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/finance/feemajor/delete.html","#feemajorBody");
	}
	
	function setFeeMajor(){
		$.ajax({
			type:'POST',
			url:"${baseUrl}/edu3/finance/feemajor/setlist.html",
			dataType:"json",
			cache: false,
			success:function(resultData){
				var statusCode = resultData['statusCode'];
				var msg     = resultData['message'];
				if(statusCode==200){
					alertMsg.confirm(msg);
				}else{
					alertMsg.confirm(msg);
				}
			}
		});		
		
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/feemajor/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>专业名称：</label><input type="text" name="majorName"
							value="${condition['majorName']}" class="custom-inp"/></li>
						<li><label>专业编码：</label><input type="text" name="majorCode"
							value="${condition['majorCode']}" /></li>
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
			<gh:resAuth parentCode="RES_FINANCE_FEEMAJOR" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%">
							<input type="checkbox" name="checkall" id="check_all_feemajor"
								onclick="checkboxAll('#check_all_feemajor','resourceid','#feemajorBody')" />
						</th>
						<th width="10%">专业编号</th>
						<th width="25%">专业名称</th>
						<c:if test="${schoolCode eq '10598' }">
							<th width="10%">学习形式</th>
						</c:if>
						<th width="15%">英文名称</th>
						<th width="15%">专业类别</th>
						<th width="20%">缴费类别</th>
					</tr>
				</thead>
				<tbody id="feemajorBody">
					<c:forEach items="${feeMajorList.result}" var="fm"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${fm.resourceid }" autocomplete="off" /></td>
							<td>${fm.major.majorCode }</td>
							<td>${fm.major.majorName }</td>
							<c:if test="${schoolCode eq '10598' }">
								<td>${ghfn:dictCode2Val('CodeTeachingType',fm.teachingType) }</td>
							</c:if>
							<td>${fm.major.majorEnName }</td>
							<td>${ghfn:dictCode2Val('CodeMajorClass',fm.major.majorClass) }</td>
							<td>${ghfn:dictCode2Val('CodePaymentType',fm.paymentType) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${feeMajorList}"
				goPageUrl="${baseUrl }/edu3/finance/feemajor/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>