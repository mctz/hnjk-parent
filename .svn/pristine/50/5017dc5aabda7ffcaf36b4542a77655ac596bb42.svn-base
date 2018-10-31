<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>班级备注信息</title>
<script type="text/javascript">
//新增子表
function addChildMemo(){
	var td1 = "<tr><td style='display: none;'><input type='hidden' name='userName' value='${user.username}' readonly='readonly'/></td>";
	var td2 = "<td width='20%'><input type='text' style='width: 100%' name='cnName' value='${user.cnName}'/></td><td width='60%'><input type='text' style='width: 100%' name='memo' value=''/></td>";
	var td3 = "<td width='20%'><input type='text' style='width: 100%' name='date' value='${date}'/></td>";
	jQuery("#childMemo").append("<tr>"+td1+td2+td3+"</tr>");
}

</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form  onsubmit="return validateCallback(this);" class="pageForm" action="${baseUrl }/edu3/roll/classes/saveMemoInfo.html" method="post" id="classesListForm">
				<input type="hidden" name="classesid" value="${classes.resourceid }" />
				<div class="pageFormContent" layoutH="60">
					<div class="formBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="button" onclick="addChildMemo()">新增一项</button>
									</div>
								</div></li>
						</ul>
					</div>
					<table class="form" id="childMemo">
						<tr style="height: 20px">
							<th style="display: none;"></th>
							<th width="20%" style="text-align: center;">记录人</th>
							<th width="60%" style="text-align: center;">备注信息</th>
							<th width="20%" style="text-align: center;">添加时间</th>
						</tr>
						<c:forEach items="${memoList}" var="m" varStatus="vs">
							<tr>
								<td style="display: none;"><input type="hidden" name="userName" value="${m.userName }"></td>
								<td><input type="text" name="cnName" value="${m.cnName }" readonly="readonly"></td>
								<td><input type="text" style="width: 100%" name="memo" value="${m.memo }" readonly="readonly"></td>
								<td><input type="text" name="date" value="${m.date }" readonly="readonly"></td>
							</tr>
						</c:forEach>
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
									<!-- onclick="$.pdialog.closeCurrent();navTab.closeCurrentTab();" -->
									<button type="button" class="close" onclick="$.pdialog.closeCurrent();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>