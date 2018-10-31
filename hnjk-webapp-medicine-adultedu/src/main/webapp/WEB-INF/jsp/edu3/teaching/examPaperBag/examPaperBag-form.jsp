<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>生成试卷袋标签数据</title>
<script type="text/javascript">
		function dialogAjaxDone_examPageBag(json){
			var isSuccess = json['isSuccess'];
			var msg       = json['msg'];
			if (isSuccess == true){
				navTab.reload("${baseUrl}/edu3/teaching/exam/paperbag/list.html",$("#examPaperBagSearchForm").serializeArray(),"RES_TEACHING_EXAM_PAPER_LIST");
				$.pdialog.closeCurrent();
			}else{
				alertMsg.warn(msg);
			}
		}
	</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/exam/paperbag/gen.html"
				class="pageForm"
				onsubmit="return validateCallback(this,dialogAjaxDone_examPageBag);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="15%">考试批次:</td>
							<td width="35%"><input type="hidden" name="examSub"
								value="${examSub.resourceid}" /> ${examSub.batchName }</td>
							<td width="15%">试卷备份系数:</td>
							<td width="35%"><input type='text' name='backupCoefficient'
								value='${backupCoefficient }' readonly="readonly"
								class='required,number'></td>
						</tr>
					</table>
					<table class="list" width="100%">
						<thead>
							<tr>
								<th width="5%">序号</th>
								<th width="10%">考试编号</th>
								<th width="30%">课程名称</th>
								<th width="10%">考试形式</th>
								<th width="35%">教学站</th>
								<th width="10%">预约人 数</th>
							</tr>
						</thead>
						<tbody id="examPaperBagFormBody">
							<c:forEach items="${list }" var="stataVo" varStatus="vs">
								<tr>
									<td>${vs.index+1 }</td>
									<td>${stataVo['EXAMCOURSECODE'] }</td>
									<td>${stataVo['COURSENAME'] }</td>
									<td>${stataVo['EXAMTYPE'] }</td>
									<td>${stataVo['UNITNAME'] }</td>
									<td>${stataVo['TOTALNUM'] }</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">生成</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="$.pdialog.closeCurrent();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>