<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">


//导出执行结果
function exportResults(){
	var url = "${baseUrl}/edu3/system/standby/executeSQL.html?flag=export";
	var map = "${map}"
	if(map==null){
		alertMsg.confirm("无查询结果");
		return false;
	}else{
		downloadFileByIframe(url,'executeSQL_Ifram',"post");
	}
}

</script>
</head>
<body>
	<div class="page">
		<c:if test="${flag!='export' }">
			<div class="pageHeader">
				<form onsubmit="return navTabSearch(this);" id="executeSQLForm"
					action="${baseUrl }/edu3/system/standby/executeSQL.html" method="post">
					<div class="searchBar">
						<ul class="searchContent">
							<li style="width: 90%;height: 200px;"><label>SQL<br>查询语句：<br></label>
								<textarea rows="10" cols="1" id="executeSQL" name="sql" style="width: 90%;height: 100%;">${sql}</textarea>
							</li>
							<div class="buttonActive" style="float: right;margin-top: 80px;margin-right: 10px;" >
								<div class="buttonContent">
									<button type="submit">查 询</button>
								</div>
							</div>
							<li style="width: 90%;">
								<label style="width:200px;color: red;">注意：只能执行查询语句，查询结果字段名不能重复</label>
							</li>
						</ul>
					</div>
				</form>
			</div>
		</c:if>
		<div class="pageContent">
			<c:if test="${flag!='export' }">
				<gh:resAuth parentCode="RES_SYS_STANDBY_MENU" pageType="hlist"></gh:resAuth>
			</c:if>
			<div  style="overflow:scroll;position:relative;" layouth="268">
			<table class="list" border="1">
				<thead style="table-layout: fixed;top: 0px;">
					<tr>
						<th style="width: 3%;"></th>
						<c:forEach var="item" items="${map}"> 
							<th style="width: 10%;">${item.key}</th>
						</c:forEach> 
					</tr>
				</thead>
				<tbody>
					<c:forEach var="m" items="${mapList}" varStatus="status">
						<tr>
							<td>${status.count }</td>
							<c:forEach var="item" items="${m}"> 
								<td title="[${item.key } , ${item.value }]">${item.value }</td>
							</c:forEach>
						</tr>
					</c:forEach> 
				</tbody>
			</table>
			</div>
		</div>
	</div>
</body>
</html>